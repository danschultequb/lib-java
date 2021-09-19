package qub;

public class LazyResult<T> implements Result<T>
{
    private final Result<?> parentResult;
    private final Function2<Object,Throwable,T> function;
    private T value;
    private Throwable error;
    private boolean isCompleted;

    private LazyResult(Result<?> parentResult, Function2<Object,Throwable,T> function)
    {
        PreCondition.assertNotNull(function, "function");

        this.parentResult = parentResult;
        this.function = function;
    }

    /**
     * Create a new empty successful {@link LazyResult}.
     * @param <U> The type of value the {@link LazyResult} should contain.
     */
    public static <U> LazyResult<U> create()
    {
        return LazyResult.create(() -> null);
    }

    public static <U> LazyResult<U> create(U value)
    {
        return LazyResult.create(() -> value);
    }

    /**
     * Create a new Result by synchronously running the provided Action and returning the result.
     * @param action The action to run.
     */
    public static LazyResult<Void> create(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return LazyResult.create(() ->
        {
            action.run();
            return null;
        });
    }

    /**
     * Create a new LazyResult that will invoke the provided function only when the new LazyResult
     * is awaited.
     * @param function The function to invoke when the new LazyResult is awaited.
     * @return The new LazyResult.
     */
    public static <T> LazyResult<T> create(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return new LazyResult<>(null, (Object parentValue, Throwable parentError) ->
        {
            PreCondition.assertNull(parentValue, "parentValue");
            PreCondition.assertNull(parentError, "parentError");

            return function.run();
        });
    }

    /**
     * Create a new LazyResult that will invoke the provided function only when the provided
     * parentResult is completed and the new LazyResult is awaited.
     * @param parentResult The result that must complete before this LazyResult will run.
     * @param function The function to invoke when the new LazyResult is awaited.
     * @return The new LazyResult.
     */
    @SuppressWarnings("unchecked")
    private static <ParentT,T> LazyResult<T> create(Result<ParentT> parentResult, Function2<ParentT,Throwable,T> function)
    {
        PreCondition.assertNotNull(parentResult, "parentResult");
        PreCondition.assertNotNull(function, "function");

        return new LazyResult<>(parentResult, (Function2<Object,Throwable,T>)function);
    }

    /**
     * Create a new {@link LazyResult} that contains the provided {@link Throwable}.
     * @param error The {@link Throwable} that the {@link LazyResult} should contain.
     * @param <U> The type of value the {@link LazyResult} can contain.
     */
    public static <U> LazyResult<U> error(Throwable error)
    {
        PreCondition.assertNotNull(error, "error");

        return LazyResult.create(() -> { throw Exceptions.asRuntime(error); });
    }

    /**
     * Get whether or not this LazyResult doesn't have a parent result, or if it does have a parent
     * result, or if it does have a parent result, then whether or not that parent result is
     * completed.
     * @return Whether or not this LazyResult's parent exists and has completed.
     */
    public boolean isParentResultCompleted()
    {
        return this.parentResult == null || this.parentResult.isCompleted();
    }

    @Override
    public boolean isCompleted()
    {
        return this.isCompleted;
    }

    /**
     * Ensure that this LazyResult and it's parent Result (if it has a parent result) are both
     * completed.
     */
    private void ensureIsCompleted()
    {
        if (!this.isCompleted)
        {
            try
            {
                Object parentValue = null;
                Throwable parentError = null;
                if (!this.isParentResultCompleted())
                {
                    try
                    {
                        parentValue = this.parentResult.await();
                    }
                    catch (Throwable e)
                    {
                        parentError = e;
                    }
                }
                this.value = function.run(parentValue, parentError);
            }
            catch (Throwable e)
            {
                this.error = e;
            }
            this.isCompleted = true;
        }

        PostCondition.assertTrue(this.isParentResultCompleted(), "this.parentFunction.isCompleted()");
        PostCondition.assertTrue(this.isCompleted(), "this.isCompleted()");
    }

    @Override
    public T await()
    {
        this.ensureIsCompleted();
        if (this.error != null)
        {
            throw new AwaitException(this.error);
        }
        return this.value;
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
    public <U> LazyResult<U> then(Function1<T, U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return LazyResult.create(this, (T parentValue, Throwable parentError) ->
        {
            if (parentError != null)
            {
                throw Exceptions.asRuntime(parentError);
            }
            return function.run(parentValue);
        });
    }

    @Override
    public <TError extends Throwable> LazyResult<T> catchError(Class<TError> errorType, Function1<TError,T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return LazyResult.create(this, (T parentValue, Throwable parentError) ->
        {
            T result = parentValue;
            if (parentError != null)
            {
                final TError expectedError = Exceptions.getInstanceOf(parentError, errorType);
                if (expectedError == null)
                {
                    throw Exceptions.asRuntime(parentError);
                }
                else
                {
                    result = function.run(expectedError);
                }
            }
            return result;
        });
    }

    @Override
    public String toString()
    {
        return Result.toString(this);
    }
}
