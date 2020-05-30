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

    public static <T> SyncResult<T> create(T value)
    {
        return new SyncResult<>(value, null);
    }

    @Override
    public boolean isCompleted()
    {
        return true;
    }

    public T await()
    {
        if (error != null)
        {
            throw new AwaitException(error);
        }
        return value;
    }

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
    public <U> SyncResult<U> then(Function1<T,U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return error != null
            ? (SyncResult<U>)this
            : SyncResult.create(() -> function.run(value));
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
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

    private static final SyncResult<?> successNull = SyncResult.success(null);
    /**
     * Create a new empty successful Result.
     * @param <U> The type of value the Result should contain.
     */
    @SuppressWarnings("unchecked")
    public static <U> SyncResult<U> success()
    {
        return (SyncResult<U>)successNull;
    }

    private static final SyncResult<Integer> successZero = SyncResult.success(0);
    /**
     * Get a successful result that contains a 0 Integer value.
     */
    public static SyncResult<Integer> successZero()
    {
        return successZero;
    }

    private static final SyncResult<Integer> successOne = SyncResult.success(1);
    /**
     * Get a successful result that contains a 1 Integer value.
     */
    public static SyncResult<Integer> successOne()
    {
        return successOne;
    }

    private static final SyncResult<Boolean> successTrue = SyncResult.success(true);
    /**
     * Get a successful Result that contains a true boolean value.
     */
    public static SyncResult<Boolean> successTrue()
    {
        return successTrue;
    }

    private static final SyncResult<Boolean> successFalse = SyncResult.success(false);
    /**
     * Get a successful Result that contains a true boolean value.
     */
    public static SyncResult<Boolean> successFalse()
    {
        return successFalse;
    }

    /**
     * Create a new successful Result that contains the provided value.
     * @param value The value the Result should contain.
     * @param <U> The type of the value.
     */
    public static <U> SyncResult<U> success(U value)
    {
        return new SyncResult<>(value, null);
    }

    /**
     * Create a new Result by synchronously running the provided Action and returning the result.
     * @param action The action to run.
     */
    public static <U> SyncResult<U> create(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        SyncResult<U> result;
        try
        {
            action.run();
            result = SyncResult.success();
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
            result = SyncResult.success(function.run());
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

    /**
     * Create a new Result that contains an end of stream error.
     * @param <U> The type of value that the Result can contain.
     * @return A Result that contains an end of stream error.
     */
    public static <U> SyncResult<U> endOfStream()
    {
        return Result.error(new EndOfStreamException());
    }
}
