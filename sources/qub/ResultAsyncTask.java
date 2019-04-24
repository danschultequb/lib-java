package qub;

/**
 * A task that can be run by an AsyncRunner.
 * @param <T> The type of value that the task will return when it completes.
 */
public class ResultAsyncTask<T> implements Result<T>
{
    /**
     * The Result that must be completed before this AsyncTask can be run.
     */
    private final Result<?> parentResult;
    /**
     * The function that this AsyncTask will run when the parentResult is successful.
     */
    private final Function0<T> successFunction;
    /**
     * The type of error that this AsyncTask expects its parentResult to throw.
     */
    private final Class<? extends Throwable> expectedErrorType;
    /**
     * The function that this AsyncTask will run when the parentResult is not successful.
     */
    private final Function1<Throwable,T> errorFunction;
    /**
     * The value that this AsyncTask produced after it was run.
     */
    private T value;
    /**
     * The error that this AsyncTask produced after it was run.
     */
    private Throwable error;
    /**
     * Whether or not this AsyncTask has been run.
     */
    private boolean completed;

    /**
     * Create a new ResultAsyncTask with the provided action.
     * @param action The action that this AsyncTask will run when it is scheduled.
     */
    public ResultAsyncTask(Action0 action)
    {
        this(action, Result.success());
    }

    /**
     * Create a new ResultAsyncTask with the provided action.
     * @param action The action that this AsyncTask will run when it is scheduled.
     * @param parentResult The Result that must be completed before this AsyncTask will run.
     */
    public ResultAsyncTask(Action0 action, Result<?> parentResult)
    {
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(parentResult, "parentResult");

        this.successFunction = () ->
        {
            action.run();
            return null;
        };
        this.expectedErrorType = null;
        this.errorFunction = null;
        this.parentResult = parentResult;
    }

    /**
     * Create a new ResultAsyncTask with the provided action.
     * @param function The function that this AsyncTask will run when it is scheduled.
     */
    public ResultAsyncTask(Function0<T> function)
    {
        this(function, Result.success());
    }

    /**
     * Create a new ResultAsyncTask with the provided action.
     * @param function The action that this AsyncTask will run when it is scheduled.
     * @param parentResult The Result that must be completed before this AsyncTask will run.
     */
    public ResultAsyncTask(Function0<T> function, Result<?> parentResult)
    {
        PreCondition.assertNotNull(function, "function");
        PreCondition.assertNotNull(parentResult, "parentResult");

        this.successFunction = function;
        this.expectedErrorType = null;
        this.errorFunction = null;
        this.parentResult = parentResult;
    }

    /**
     * Create a new AsyncTask that will run the provided errorFunction when the parentResult throws
     * an error of the provided expectedErrorType.
     * @param expectedErrorType The type of error that this AsyncTask expects its parentResult to
     *                          throw.
     * @param errorFunction The function to run if the parentResult throws an error of the expected
     *                      type.
     * @param parentResult The Result that must be completed before this AsyncTask will run.
     */
    public ResultAsyncTask(Class<? extends Throwable> expectedErrorType, Function1<Throwable,T> errorFunction, Result<?> parentResult)
    {
        PreCondition.assertNotNull(expectedErrorType, "expectedErrorType");
        PreCondition.assertNotNull(errorFunction, "errorFunction");
        PreCondition.assertNotNull(parentResult, "parentResult");

        this.successFunction = null;
        this.expectedErrorType = expectedErrorType;
        this.errorFunction = errorFunction;
        this.parentResult = parentResult;
    }

    @SuppressWarnings("unchecked")
    private void innerAwait()
    {
        if (!completed)
        {
            completed = true;
            try
            {
                value = (T)parentResult.await();
                if (successFunction != null)
                {
                    value = successFunction.run();
                }
            }
            catch (Throwable error)
            {
                if (expectedErrorType != null)
                {
                    Throwable expectedError = Exceptions.getInstanceOf(error, expectedErrorType);
                    if (expectedError != null)
                    {
                        if (!Exceptions.defaultErrorTypesToGoPast.contains(expectedErrorType))
                        {
                            expectedError = Exceptions.unwrap(expectedError);
                        }

                        try
                        {
                            value = errorFunction.run(expectedError);
                        }
                        catch (Throwable innerError)
                        {
                            this.error = innerError;
                        }
                    }
                    else
                    {
                        this.error = error;
                    }
                }
                else
                {
                    this.error = error;
                }
            }
        }
    }

    @Override
    public T await()
    {
        innerAwait();

        if (error != null)
        {
            throw new AwaitException(error);
        }

        return value;
    }

    @Override
    public <TError extends Throwable> T await(Class<TError> expectedErrorType) throws TError
    {
        PreCondition.assertNotNull(expectedErrorType, "expectedErrorType");

        innerAwait();

        if (error != null)
        {
            final TError matchingError = Types.as(error, expectedErrorType);
            if (matchingError != null)
            {
                throw matchingError;
            }
            else
            {
                throw new AwaitException(error);
            }
        }

        return value;
    }

    @Override
    public Result<Void> then(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(action, this);
    }

    @Override
    public Result<Void> then(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(() -> action.run(value), this);
    }

    @Override
    public <U> Result<U> then(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(function, this);
    }

    @Override
    public <U> Result<U> thenResult(Function0<Result<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(() -> function.run().await(), this);
    }

    @Override
    public <U> Result<U> then(Function1<T,U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<U>(() -> function.run(value), this);
    }

    @Override
    public <U> Result<U> thenResult(Function1<T,Result<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(() -> function.run(value).await(), this);
    }

    @Override
    public Result<T> onValue(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(() -> { action.run(); return value; }, this);
    }

    @Override
    public Result<T> onValue(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(() -> { action.run(value); return value; }, this);
    }

    @Override
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType)
    {
        PreCondition.assertNotNull(errorType, "errorType");

        return new ResultAsyncTask<>(errorType, (Throwable error) -> null, this);
    }

    @Override
    public Result<T> catchError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(Throwable.class, (Throwable error) -> { action.run(); return value; }, this);
    }

    @Override
    public Result<T> catchError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(Throwable.class, (Throwable error) -> { action.run(error); return value; }, this);
    }

    @Override
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Action0 action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(errorType, (Throwable error) -> { action.run(); return value; }, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Action1<TError> action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(errorType, (Throwable error) -> { action.run(Exceptions.getInstanceOf(error, errorType)); return value; }, this);
    }

    @Override
    public Result<T> catchError(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(Throwable.class, (Throwable error) -> function.run(), this);
    }

    @Override
    public Result<T> catchError(Function1<Throwable,T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(Throwable.class, (Throwable error) -> function.run(Exceptions.unwrap(error)), this);
    }

    @Override
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Function0<T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(errorType, (Throwable error) -> function.run(), this);
    }

    @Override
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Function1<TError,T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(errorType, (Throwable error) -> function.run(Exceptions.getInstanceOf(error, errorType)), this);
    }

    @Override
    public Result<T> catchErrorResult(Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(Throwable.class, (Throwable error) -> function.run().await(), this);
    }

    @Override
    public Result<T> catchErrorResult(Function1<Throwable,Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(Throwable.class, (Throwable error) -> function.run(Exceptions.unwrap(error)).await(), this);
    }

    @Override
    public <TError extends Throwable> Result<T> catchErrorResult(Class<TError> errorType, Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(errorType, (Throwable error) -> function.run().await(), this);
    }

    @Override
    public <TError extends Throwable> Result<T> catchErrorResult(Class<TError> errorType, Function1<TError,Result<T>> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return new ResultAsyncTask<>(errorType, (Throwable error) -> function.run(Exceptions.getInstanceOf(error, errorType)).await(), this);
    }

    @Override
    public Result<T> onError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(
            Throwable.class,
            (Throwable error) ->
            {
                action.run();
                throw Exceptions.asRuntime(error);
            },
            this);
    }

    @Override
    public Result<T> onError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(
            Throwable.class,
            (Throwable error) ->
            {
                action.run(error);
                throw Exceptions.asRuntime(error);
            },
            this);
    }

    @Override
    public <TError extends Throwable> Result<T> onError(Class<TError> errorType, Action0 action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(
            errorType,
            (Throwable error) ->
            {
                action.run();
                throw Exceptions.asRuntime(error);
            },
            this);
    }

    @Override
    public <TError extends Throwable> Result<T> onError(Class<TError> errorType, Action1<TError> action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        return new ResultAsyncTask<>(
            errorType,
            (Throwable error) ->
            {
                action.run(Exceptions.getInstanceOf(error, errorType));
                throw Exceptions.asRuntime(error);
            },
            this);
    }
}
