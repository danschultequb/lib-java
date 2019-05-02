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
     * The AsyncTasks that should be scheduled after this AsyncTask is completed.
     */
    private final List<ResultAsyncTask<?>> nextTasks;
    /**
     * The AsyncScheduler that determines when this AsyncTask will be run.
     */
    private final ResultAsyncScheduler asyncScheduler;

    /**
     * Create a new ResultAsyncTask with the provided action.
     * @param asyncScheduler The AsyncScheduler that determines when this AsyncTask is run.
     * @param action The action that this AsyncTask will run when it is scheduled.
     */
    public ResultAsyncTask(ResultAsyncScheduler asyncScheduler, Action0 action)
    {
        this(asyncScheduler, Result.success(), action);
    }

    /**
     * Create a new ResultAsyncTask with the provided action.
     * @param asyncScheduler The AsyncScheduler that determines when this AsyncTask is run.
     * @param parentResult The Result that must be completed before this AsyncTask will run.
     * @param action The action that this AsyncTask will run when it is scheduled.
     */
    public ResultAsyncTask(ResultAsyncScheduler asyncScheduler, Result<?> parentResult, Action0 action)
    {
        PreCondition.assertNotNull(asyncScheduler, "asyncScheduler");
        PreCondition.assertNotNull(action, "action");
        PreCondition.assertNotNull(parentResult, "parentResult");

        this.asyncScheduler = asyncScheduler;
        this.successFunction = () ->
        {
            action.run();
            return null;
        };
        this.expectedErrorType = null;
        this.errorFunction = null;
        this.parentResult = parentResult;
        this.nextTasks = List.create();
    }

    /**
     * Create a new ResultAsyncTask with the provided action.
     * @param asyncScheduler The AsyncScheduler that determines when this AsyncTask is run.
     * @param function The function that this AsyncTask will run when it is scheduled.
     */
    public ResultAsyncTask(ResultAsyncScheduler asyncScheduler, Function0<T> function)
    {
        this(asyncScheduler, Result.success(), function);
    }

    /**
     * Create a new ResultAsyncTask with the provided action.
     * @param asyncScheduler The AsyncScheduler that determines when this AsyncTask is run.
     * @param parentResult The Result that must be completed before this AsyncTask will run.
     * @param function The action that this AsyncTask will run when it is scheduled.
     */
    public ResultAsyncTask(ResultAsyncScheduler asyncScheduler, Result<?> parentResult, Function0<T> function)
    {
        PreCondition.assertNotNull(asyncScheduler, "asyncScheduler");
        PreCondition.assertNotNull(function, "function");
        PreCondition.assertNotNull(parentResult, "parentResult");

        this.asyncScheduler = asyncScheduler;
        this.successFunction = function;
        this.expectedErrorType = null;
        this.errorFunction = null;
        this.parentResult = parentResult;
        this.nextTasks = List.create();
    }

    /**
     * Create a new AsyncTask that will run the provided errorFunction when the parentResult throws
     * an error of the provided expectedErrorType.
     * @param asyncScheduler The AsyncScheduler that determines when this AsyncTask is run.
     * @param expectedErrorType The type of error that this AsyncTask expects its parentResult to
     *                          throw.
     * @param errorFunction The function to run if the parentResult throws an error of the expected
     *                      type.
     * @param parentResult The Result that must be completed before this AsyncTask will run.
     */
    public ResultAsyncTask(ResultAsyncScheduler asyncScheduler, Result<?> parentResult, Class<? extends Throwable> expectedErrorType, Function1<Throwable,T> errorFunction)
    {
        PreCondition.assertNotNull(asyncScheduler, "asyncScheduler");
        PreCondition.assertNotNull(parentResult, "parentResult");
        PreCondition.assertNotNull(expectedErrorType, "expectedErrorType");
        PreCondition.assertNotNull(errorFunction, "errorFunction");

        this.asyncScheduler = asyncScheduler;
        this.successFunction = null;
        this.expectedErrorType = expectedErrorType;
        this.errorFunction = errorFunction;
        this.parentResult = parentResult;
        this.nextTasks = List.create();
    }

    /**
     * Get the AsyncTasks that should be scheduled after this AsyncTask is completed.
     * @return The AsyncTasks that should be scheduled after this AsyncTask is completed.
     */
    public Iterable<ResultAsyncTask<?>> getNextTasks()
    {
        return nextTasks;
    }

    /**
     * Get whether or not this AsyncTask is completed.
     * @return Whether or not this AsyncTask is completed.
     */
    @Override
    public boolean isCompleted()
    {
        return completed;
    }

    /**
     * Schedule this AsyncTask to run on its AsyncRunner.
     */
    public void schedule()
    {
        PreCondition.assertFalse(isCompleted(), "isCompleted()");

        asyncScheduler.schedule(this);
    }

    @SuppressWarnings("unchecked")
    public void run()
    {
        PreCondition.assertFalse(isCompleted(), "isCompleted()");

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

        for (final ResultAsyncTask<?> nextTask : nextTasks)
        {
            nextTask.schedule();
        }
        completed = true;
    }

    @Override
    public T await()
    {
        if (!completed)
        {
            asyncScheduler.await(this);
        }

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

        if (!completed)
        {
            asyncScheduler.await(this);
        }

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
    public ResultAsyncTask<Void> then(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<Void> result = new ResultAsyncTask<>(asyncScheduler, this, action);
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<Void> then(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<Void> result = new ResultAsyncTask<>(asyncScheduler, this, () -> action.run(value));
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <U> ResultAsyncTask<U> then(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<U> result = new ResultAsyncTask<>(asyncScheduler, this, function);
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <U> ResultAsyncTask<U> thenResult(Function0<Result<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<U> result = new ResultAsyncTask<>(asyncScheduler, this, () -> function.run().await());
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <U> ResultAsyncTask<U> then(Function1<T,U> function)
    {
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<U> result = new ResultAsyncTask<U>(asyncScheduler, this, () -> function.run(value));
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <U> ResultAsyncTask<U> thenResult(Function1<T,Result<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<U> result = new ResultAsyncTask<>(asyncScheduler, this, () -> function.run(value).await());
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> onValue(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, () -> { action.run(); return value; });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> onValue(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, () -> { action.run(value); return value; });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> ResultAsyncTask<T> catchError(Class<TError> errorType)
    {
        PreCondition.assertNotNull(errorType, "errorType");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, errorType, (Throwable error) -> null);
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> catchError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, Throwable.class, (Throwable error) -> { action.run(); return value; });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> catchError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, Throwable.class, (Throwable error) -> { action.run(error); return value; });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> ResultAsyncTask<T> catchError(Class<TError> errorType, Action0 action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, errorType, (Throwable error) -> { action.run(); return value; });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> ResultAsyncTask<T> catchError(Class<TError> errorType, Action1<TError> action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, errorType, (Throwable error) -> { action.run(Exceptions.getInstanceOf(error, errorType)); return value; });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> catchError(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, Throwable.class, (Throwable error) -> function.run());
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> catchError(Function1<Throwable,T> function)
    {
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, Throwable.class, (Throwable error) -> function.run(Exceptions.unwrap(error)));
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> ResultAsyncTask<T> catchError(Class<TError> errorType, Function0<T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, errorType, (Throwable error) -> function.run());
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> ResultAsyncTask<T> catchError(Class<TError> errorType, Function1<TError,T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, errorType, (Throwable error) -> function.run(Exceptions.getInstanceOf(error, errorType)));
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> catchErrorResult(Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, Throwable.class, (Throwable error) -> function.run().await());
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> catchErrorResult(Function1<Throwable,Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, Throwable.class, (Throwable error) -> function.run(Exceptions.unwrap(error)).await());
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> ResultAsyncTask<T> catchErrorResult(Class<TError> errorType, Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, errorType, (Throwable error) -> function.run().await());
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> ResultAsyncTask<T> catchErrorResult(Class<TError> errorType, Function1<TError,Result<T>> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(asyncScheduler, this, errorType, (Throwable error) -> function.run(Exceptions.getInstanceOf(error, errorType)).await());
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> onError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(
            asyncScheduler,
            this,
            Throwable.class,
            (Throwable error) ->
            {
                action.run();
                throw Exceptions.asRuntime(error);
            });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public ResultAsyncTask<T> onError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(
            asyncScheduler,
            this,
            Throwable.class,
            (Throwable error) ->
            {
                action.run(error);
                throw Exceptions.asRuntime(error);
            });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> ResultAsyncTask<T> onError(Class<TError> errorType, Action0 action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(
            asyncScheduler,
            this,
            errorType,
            (Throwable error) ->
            {
                action.run();
                throw Exceptions.asRuntime(error);
            });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> ResultAsyncTask<T> onError(Class<TError> errorType, Action1<TError> action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        final ResultAsyncTask<T> result = new ResultAsyncTask<>(
            asyncScheduler,
            this,
            errorType,
            (Throwable error) ->
            {
                action.run(Exceptions.getInstanceOf(error, errorType));
                throw Exceptions.asRuntime(error);
            });
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
