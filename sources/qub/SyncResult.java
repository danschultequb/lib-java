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

    public T await()
    {
        if (error != null)
        {
            throw new AwaitException(error);
        }
        return value;
    }

    public T awaitError()
    {
        if (error != null)
        {
            throw Exceptions.asRuntime(error);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public <TError extends Throwable> T awaitError(Class<TError> expectedErrorType) throws TError
    {
        if (error != null)
        {
            final Throwable unwrappedError = Exceptions.unwrap(error);
            if (Types.instanceOf(unwrappedError, expectedErrorType))
            {
                throw (TError)unwrappedError;
            }
            else
            {
                throw Exceptions.asRuntime(error);
            }
        }
        return value;
    }

    @Deprecated
    final public T getValue()
    {
        return value;
    }

    @Deprecated
    final public boolean hasError()
    {
        return error != null;
    }

    @Deprecated
    final public Throwable getError()
    {
        return error;
    }

    @Deprecated
    final public Class<? extends Throwable> getErrorType()
    {
        return error == null ? null : error.getClass();
    }

    @Deprecated
    final public String getErrorMessage()
    {
        return error == null ? null : error.getMessage();
    }

    @Deprecated
    final public void throwError()
    {
        if (error != null)
        {
            throw Exceptions.asRuntime(error);
        }
    }

    @Deprecated
    public T throwErrorOrGetValue()
    {
        return awaitError();
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    final public <U> SyncResult<U> convertError()
    {
        return hasError() ? (SyncResult<U>)this : null;
    }

    /**
     * If this Result doesn't have an error, then run the provided action and return a new Result
     * object with the action's result.
     * @param action The action to run if this result does not have an error.
     * @return The Result of running the provided action.
     */
    @SuppressWarnings("unchecked")
    public SyncResult<Void> then(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return error != null
            ? (SyncResult<Void>)this
            : SyncResult.create(action);
    }

    /**
     * If this Result doesn't have an error, then run the provided action and return a new Result
     * object with the action's result.
     * @param action The action to run if this result does not have an error.
     * @return The Result of running the provided action.
     */
    @SuppressWarnings("unchecked")
    public SyncResult<Void> then(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        return error != null
            ? (SyncResult<Void>)this
            : SyncResult.create(() -> action.run(value));
    }

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    @SuppressWarnings("unchecked")
    public <U> Result<U> then(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return error != null
            ? (SyncResult<U>)this
            : SyncResult.create(function);
    }

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    @SuppressWarnings("unchecked")
    public <U> Result<U> thenResult(Function0<Result<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return error != null
            ? (Result<U>)this
            : SyncResult.createResult(function);
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
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    @SuppressWarnings("unchecked")
    public <U> Result<U> thenResult(Function1<T,Result<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return error != null
            ? (Result<U>)this
            : SyncResult.createResult(() -> function.run(value));
    }

    /**
     * If this result doesn't have an error, then run the provided action and return this Result
     * object.
     * @param action The action to run if this result does not have an error.
     * @return This Result object.
     */
    public SyncResult<T> onValue(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        SyncResult<T> result = this;
        if (error == null)
        {
            try
            {
                action.run(value);
            }
            catch (Throwable error)
            {
                result = SyncResult.error(error);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error of the provided type, then catch that error and return a
     * successful Result instead.
     * @param errorType The type of error to catch.
     * @param <TError> The type of error to catch.
     * @return This Result if it is successful, or an empty successful Result if this Result
     * contains an error of the provided type.
     */
    public <TError extends Throwable> SyncResult<T> catchError(Class<TError> errorType)
    {
        PreCondition.assertNotNull(errorType, "errorType");

        return Exceptions.instanceOf(error, errorType)
            ? SyncResult.success()
            : this;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    public SyncResult<T> catchError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return error != null
            ? SyncResult.create(action)
            : this;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    public SyncResult<T> catchError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        return error != null
            ? SyncResult.create(() -> action.run(error))
            : this;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    public SyncResult<T> catchResultError(Action1<Result<T>> action)
    {
        PreCondition.assertNotNull(action, "action");

        return error != null
            ? SyncResult.create(() -> action.run(this))
            : this;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    public <TError extends Throwable> SyncResult<T> catchError(Class<TError> errorType, Action0 action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        return Exceptions.instanceOf(error, errorType)
            ? SyncResult.create(action)
            : this;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    public <TError extends Throwable> SyncResult<T> catchError(Class<TError> errorType, Action1<TError> action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        final TError matchingError = Exceptions.getInstanceOf(error, errorType);
        return matchingError != null
            ? SyncResult.create(() -> action.run(matchingError))
            : this;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public SyncResult<T> catchError(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return error != null
            ? SyncResult.create(function)
            : this;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public SyncResult<T> catchError(Function1<Throwable,T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return error != null
            ? SyncResult.create(() -> function.run(error))
            : this;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public <TError extends Throwable> SyncResult<T> catchError(Class<TError> errorType, Function0<T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return Exceptions.instanceOf(error, errorType)
            ? SyncResult.create(function)
            : this;
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

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public Result<T> catchErrorResult(Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return error != null
            ? SyncResult.createResult(function)
            : this;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public Result<T> catchErrorResult(Function1<Throwable,Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return error != null
            ? SyncResult.createResult(() -> function.run(error))
            : this;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public <TError extends Throwable> Result<T> catchErrorResult(Class<TError> errorType, Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        return Exceptions.instanceOf(error, errorType)
            ? SyncResult.createResult(function)
            : this;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    @SuppressWarnings("unchecked")
    public <TError extends Throwable> Result<T> catchErrorResult(Class<TError> errorType, Function1<TError,Result<T>> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        final TError matchingError = Exceptions.getInstanceOf(error, errorType);
        return matchingError != null
            ? SyncResult.createResult(() -> function.run(matchingError))
            : this;
    }

    /**
     * Run the provided action if this Result has an error.
     * @param action The action to run if this Result has an error.
     * @return This Result with its error.
     */
    public Result<T> onError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        SyncResult<T> result = this;
        if (error != null)
        {
            try
            {
                action.run();
            }
            catch (Throwable error)
            {
                result = SyncResult.error(error);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Run the provided action if this Result has an error.
     * @param action The action to run if this Result has an error.
     * @return This Result with its error.
     */
    public Result<T> onError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        SyncResult<T> result = this;
        if (error != null)
        {
            try
            {
                action.run(error);
            }
            catch (Throwable error)
            {
                result = SyncResult.error(error);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Run the provided action if this Result has an error of the provided type.
     * @param errorType The type of error to run the provided action for.
     * @param action The action to run if this Result has an error of the provided type.
     * @return This Result with its error.
     */
    public <TError extends Throwable> Result<T> onError(Class<TError> errorType, Action0 action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        SyncResult<T> result = this;
        if (Exceptions.instanceOf(error, errorType))
        {
            try
            {
                action.run();
            }
            catch (Throwable error)
            {
                result = SyncResult.error(error);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Run the provided action if this Result has an error of the provided type.
     * @param errorType The type of error to run the provided action for.
     * @param action The action to run if this Result has an error of the provided type.
     * @return This Result with its error.
     */
    @SuppressWarnings("unchecked")
    public <TError extends Throwable> Result<T> onError(Class<TError> errorType, Action1<TError> action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        SyncResult<T> result = this;
        final TError matchingError = Exceptions.getInstanceOf(error, errorType);
        if (matchingError != null)
        {
            try
            {
                action.run(matchingError);
            }
            catch (Throwable error)
            {
                result = SyncResult.error(error);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public String toString()
    {
        String result = "";
        if (error == null)
        {
            result = "value: " + Objects.toString(value);
        }
        else
        {
            if (value != null)
            {
                result = "value: " + value.toString() + ", ";
            }
            result += "error: " + error.toString();
        }
        return result;
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    final public <U extends Exception> void throwError(Class<U> exceptionType) throws U
    {
        PreCondition.assertNotNull(exceptionType, "exceptionType");

        if (error != null)
        {
            final Throwable unwrappedError = Exceptions.unwrap(error);
            if (Types.instanceOf(unwrappedError, exceptionType))
            {
                throw (U)unwrappedError;
            }
            else
            {
                throw Exceptions.asRuntime(error);
            }
        }
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof SyncResult && equals((SyncResult<?>)rhs);
    }

    /**
     * Get whether or not the provided Result is equal to this Result.
     * @param rhs The provided Result.
     */
    public boolean equals(Result<?> rhs)
    {
        return rhs instanceof SyncResult && equals((SyncResult<?>)rhs);
    }

    /**
     * Get whether or not the provided Result is equal to this Result.
     * @param rhs The provided Result.
     */
    public boolean equals(SyncResult<?> rhs)
    {
        return rhs != null && Comparer.equal(value, rhs.value) && Comparer.equal(error, rhs.error);
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

    private static final SyncResult<Boolean> successTrue = SyncResult.success(true);
    /**
     * Get a successful Result that contains a true boolean value.
     */
    static SyncResult<Boolean> successTrue()
    {
        return successTrue;
    }

    private static final SyncResult<Boolean> successFalse = SyncResult.success(false);
    /**
     * Get a successful Result that contains a true boolean value.
     */
    static SyncResult<Boolean> successFalse()
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
     * Create a new Result by synchronously running the provided Function and returning the result.
     * @param function The function to run.
     * @param <U> The type of value the function will return.
     */
    public static <U> Result<U> createResult(Function0<Result<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        Result<U> result;
        try
        {
            result = function.run();
        }
        catch (Throwable error)
        {
            result = Result.error(error);
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

    public static Throwable createError(String message)
    {
        return new IllegalArgumentException(message);
    }
}
