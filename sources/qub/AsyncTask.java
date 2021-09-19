package qub;

/**
 * A task that can be run by an AsyncRunner.
 * @param <T> The type of value that the task will return when it completes.
 */
public class AsyncTask<T> implements PausedAsyncTask<T>
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
    private final BooleanValue completed;
    /**
     * The AsyncTasks that should be scheduled after this AsyncTask is completed.
     */
    private final List<AsyncTask<?>> nextTasks;
    /**
     * The AsyncScheduler that determines when this AsyncTask will be run.
     */
    private final AsyncScheduler asyncScheduler;

    /**
     * Create a new AsyncTask with the provided action.
     * @param asyncScheduler The AsyncScheduler that determines when this AsyncTask is run.
     * @param action The action that this AsyncTask will run when it is scheduled.
     */
    public AsyncTask(AsyncScheduler asyncScheduler, Action0 action)
    {
        this(asyncScheduler, Result.create(), action);
    }

    /**
     * Create a new AsyncTask with the provided action.
     * @param asyncScheduler The AsyncScheduler that determines when this AsyncTask is run.
     * @param parentResult The Result that must be completed before this AsyncTask will run.
     * @param action The action that this AsyncTask will run when it is scheduled.
     */
    public AsyncTask(AsyncScheduler asyncScheduler, Result<?> parentResult, Action0 action)
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
        this.completed = BooleanValue.create(false);
    }

    /**
     * Create a new AsyncTask with the provided action.
     * @param asyncScheduler The AsyncScheduler that determines when this AsyncTask is run.
     * @param function The function that this AsyncTask will run when it is scheduled.
     */
    public AsyncTask(AsyncScheduler asyncScheduler, Function0<T> function)
    {
        this(asyncScheduler, Result.create(), function);
    }

    /**
     * Create a new AsyncTask with the provided action.
     * @param asyncScheduler The AsyncScheduler that determines when this AsyncTask is run.
     * @param parentResult The Result that must be completed before this AsyncTask will run.
     * @param function The action that this AsyncTask will run when it is scheduled.
     */
    public AsyncTask(AsyncScheduler asyncScheduler, Result<?> parentResult, Function0<T> function)
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
        this.completed = BooleanValue.create(false);
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
    public AsyncTask(AsyncScheduler asyncScheduler, Result<?> parentResult, Class<? extends Throwable> expectedErrorType, Function1<Throwable,T> errorFunction)
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
        this.completed = BooleanValue.create(false);
    }

    /**
     * Get the AsyncTasks that should be scheduled after this AsyncTask is completed.
     * @return The AsyncTasks that should be scheduled after this AsyncTask is completed.
     */
    public Iterable<AsyncTask<?>> getNextTasks()
    {
        return this.nextTasks;
    }

    /**
     * Get whether or not this AsyncTask is completed.
     * @return Whether or not this AsyncTask is completed.
     */
    @Override
    public boolean isCompleted()
    {
        return this.completed.get();
    }

    /**
     * Schedule this AsyncTask to run on its AsyncRunner.
     */
    @Override
    public void schedule()
    {
        PreCondition.assertFalse(this.isCompleted(), "this.isCompleted()");

        this.asyncScheduler.schedule(this);
    }

    @SuppressWarnings("unchecked")
    public void run()
    {
        PreCondition.assertFalse(isCompleted(), "isCompleted()");

        try
        {
            this.value = (T)this.parentResult.await();
            if (this.successFunction != null)
            {
                this.value = this.successFunction.run();
            }
        }
        catch (Throwable error)
        {
            if (this.expectedErrorType != null)
            {
                Throwable expectedError = Exceptions.getInstanceOf(error, this.expectedErrorType);
                if (expectedError != null)
                {
                    if (!Exceptions.defaultErrorTypesToGoPast.contains(this.expectedErrorType))
                    {
                        expectedError = Exceptions.unwrap(expectedError);
                    }

                    try
                    {
                        this.value = this.errorFunction.run(expectedError);
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

        for (final AsyncTask<?> nextTask : nextTasks)
        {
            nextTask.schedule();
        }
        this.completed.set(true);
    }

    private void ensureIsCompleted()
    {
        if (!this.isCompleted())
        {
            final AsyncScheduler currentThreadAsyncRunner = CurrentThread.getAsyncRunner().await();
            currentThreadAsyncRunner.await(this);
        }
    }

    @Override
    public T await()
    {
        this.ensureIsCompleted();
        if (this.error != null)
        {
            throw new AwaitException(error);
        }
        return value;
    }

    @Override
    public <TError extends Throwable> T await(Class<TError> expectedErrorType) throws TError
    {
        PreCondition.assertNotNull(expectedErrorType, "expectedErrorType");

        this.ensureIsCompleted();
        if (this.error != null)
        {
            final TError matchingError = Exceptions.getInstanceOf(this.error, expectedErrorType);
            if (matchingError != null)
            {
                throw matchingError;
            }
            else
            {
                throw new AwaitException(this.error);
            }
        }

        return value;
    }

    @Override
    public <U> AsyncTask<U> then(Function1<T,U> function)
    {
        PreCondition.assertNotNull(function, "function");

        final AsyncTask<U> result = new AsyncTask<>(this.asyncScheduler, this, () -> function.run(this.value));
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public <TError extends Throwable> AsyncTask<T> catchError(Class<TError> errorType, Function1<TError,T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        final AsyncTask<T> result = new AsyncTask<>(this.asyncScheduler, this, errorType, (Throwable error) -> function.run(Exceptions.getInstanceOf(error, errorType)));
        this.nextTasks.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public String toString()
    {
        return Result.toString(this);
    }
}
