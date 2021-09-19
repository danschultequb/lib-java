package qub;

public class SyncResult<T> implements Result<T>
{
    private final T value;
    private final Throwable error;

    private SyncResult(T value, Throwable error)
    {
        this.value = value;
        this.error = error;
    }

    /**
     * Create a new empty successful Result.
     * @param <U> The type of value the Result should contain.
     */
    public static <U> SyncResult<U> create()
    {
        return new SyncResult<>(null, null);
    }

    public static <T> SyncResult<T> create(T value)
    {
        return new SyncResult<>(value, null);
    }

    /**
     * Create a new Result by synchronously running the provided Action and returning the result.
     * @param action The action to run.
     */
    public static SyncResult<Void> create(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        SyncResult<Void> result;
        try
        {
            action.run();
            result = SyncResult.create();
        }
        catch (Throwable error)
        {
            result = SyncResult.error(error);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Create a new Result by synchronously running the provided Function and returning the result.
     * @param function The function to run.
     * @param <U> The type of value the function will return.
     */
    public static <U> SyncResult<U> create(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        SyncResult<U> result;
        try
        {
            result = SyncResult.create(function.run());
        }
        catch (Throwable error)
        {
            result = SyncResult.error(error);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Create a new Result that contains the provided error.
     * @param error The error that the Result should contain.
     * @param <U> The type of value the Result can contain.
     */
    public static <U> SyncResult<U> error(Throwable error)
    {
        PreCondition.assertNotNull(error, "error");

        return new SyncResult<>(null, error);
    }

    @Override
    public boolean isCompleted()
    {
        return true;
    }

    @Override
    public T await()
    {
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

        if (error != null)
        {
            final TError matchingError = Exceptions.getInstanceOf(error, expectedErrorType);
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

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <U> SyncResult<U> then(Function1<T,U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.error != null
            ? (SyncResult<U>)this
            : SyncResult.create(() -> function.run(value));
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    @Override
    public <TError extends Throwable> SyncResult<T> catchError(Class<TError> errorType, Function1<TError,T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        final TError matchingError = Exceptions.getInstanceOf(error, errorType);
        return matchingError != null
            ? SyncResult.create(() -> function.run(matchingError))
            : this;
    }

    @Override
    public String toString()
    {
        return Result.toString(this);
    }
}
