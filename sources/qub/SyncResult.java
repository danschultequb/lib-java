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
            if (Types.instanceOf(error, expectedErrorType))
            {
                throw (TError)error;
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
        Exceptions.throwAsRuntime(error);
    }

    @Deprecated
    public T throwErrorOrGetValue()
    {
        throwError();
        return value;
    }

    @Deprecated
    final public <U> Result<U> convertError()
    {
        return hasError() ? Result.error(error) : null;
    }

    /**
     * If this Result doesn't have an error, then run the provided action and return a new Result
     * object with the action's result.
     * @param action The action to run if this result does not have an error.
     * @return The Result of running the provided action.
     */
    public Result<Void> then(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        Result<Void> result;
        if (hasError())
        {
            result = convertError();
        }
        else
        {
            result = invokeThen(action);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result doesn't have an error, then run the provided action and return a new Result
     * object with the action's result.
     * @param action The action to run if this result does not have an error.
     * @return The Result of running the provided action.
     */
    public Result<Void> then(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        return then(() -> action.run(value));
    }

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    public <U> Result<U> then(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        final Value<U> resultValue = Value.create();
        final Result<Void> thenActionResult = then(() -> resultValue.set(function.run()));
        final Result<U> result = thenActionResult.hasError()
            ? thenActionResult.convertError()
            : Result.success(resultValue.get());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    public <U> Result<U> thenResult(Function0<Result<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        final Result<Result<U>> resultResult = then(function);
        final Result<U> result = resultResult.hasError() ? resultResult.convertError() : resultResult.getValue();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    public <U> Result<U> then(Function1<T,U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return then(() -> function.run(value));
    }

    /**
     * If this Result doesn't have an error, then run the provided function and return a new Result
     * object with the function's return value.
     * @param function The function to run if this result does not have an error.
     * @param <U> The type of value stored in the returned Result object.
     * @return The Result of running the provided function.
     */
    public <U> Result<U> thenResult(Function1<T,Result<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return thenResult(() -> function.run(value));
    }

    /**
     * If this result doesn't have an error, then run the provided action and return this Result
     * object.
     * @param action The action to run if this result does not have an error.
     * @return This Result object.
     */
    public Result<T> onValue(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        return thenResult(() ->
        {
            action.run(value);
            return this;
        });
    }

    /**
     * If this Result has an error of the provided type, then catch that error and return a
     * successful Result instead.
     * @param errorType The type of error to catch.
     * @param <TError> The type of error to catch.
     * @return This Result if it is successful, or an empty successful Result if this Result
     * contains an error of the provided type.
     */
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType)
    {
        PreCondition.assertNotNull(errorType, "errorType");

        Result<T> result = this;
        if (Types.instanceOf(getError(), errorType))
        {
            result = Result.success();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    public Result<T> catchError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        Result<T> result = this;
        if (hasError())
        {
            result = invokeCatch(action);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    public Result<T> catchError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        Result<T> result = this;
        if (hasError())
        {
            result = invokeCatch(() -> action.run(getError()));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    public Result<T> catchResultError(Action1<Result<T>> action)
    {
        PreCondition.assertNotNull(action, "action");

        Result<T> result = this;
        if (hasError())
        {
            result = invokeCatch(() -> action.run(this));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Action0 action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        Result<T> result = this;
        if (hasError() && Types.instanceOf(getError(), errorType))
        {
            result = invokeCatch(action);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided action.
     * @param action The action to run if this result has an error.
     * @return This result.
     */
    @SuppressWarnings("unchecked")
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Action1<TError> action)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(action, "action");

        Result<T> result = this;
        if (hasError() && Types.instanceOf(getError(), errorType))
        {
            result = invokeCatch(() -> action.run((TError)getError()));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public Result<T> catchError(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        Result<T> result = this;
        if (hasError())
        {
            result = invoke(function);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public Result<T> catchError(Function1<Throwable,T> function)
    {
        PreCondition.assertNotNull(function, "function");

        Result<T> result = this;
        if (hasError())
        {
            result = invoke(() -> function.run(getError()));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Function0<T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        Result<T> result = this;
        if (hasError() && Types.instanceOf(getError(), errorType))
        {
            result = invoke(function);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    @SuppressWarnings("unchecked")
    public <TError extends Throwable> Result<T> catchError(Class<TError> errorType, Function1<TError,T> function)
    {
        PreCondition.assertNotNull(errorType, "errorType");
        PreCondition.assertNotNull(function, "function");

        Result<T> result = this;
        if (hasError() && Types.instanceOf(getError(), errorType))
        {
            result = invoke(() -> function.run((TError)getError()));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public Result<T> catchErrorResult(Function0<Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        Result<T> result = this;
        if (hasError())
        {
            result = invokeResult(function);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * If this Result has an error, then run the provided function.
     * @param function The function to run if this result has an error.
     * @return This Result of running the provided function.
     */
    public Result<T> catchErrorResult(Function1<Throwable,Result<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        Result<T> result = this;
        if (hasError())
        {
            result = invokeResult(() -> function.run(getError()));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
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

        Result<T> result = this;
        if (hasError() && Types.instanceOf(getError(), errorType))
        {
            result = invokeResult(function);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
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

        Result<T> result = this;
        if (hasError() && Types.instanceOf(getError(), errorType))
        {
            result = invokeResult(() -> function.run((TError)getError()));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Run the provided action if this Result has an error.
     * @param action The action to run if this Result has an error.
     * @return This Result with its error.
     */
    public Result<T> onError(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        if (hasError())
        {
            action.run();
        }

        return this;
    }

    /**
     * Run the provided action if this Result has an error.
     * @param action The action to run if this Result has an error.
     * @return This Result with its error.
     */
    public Result<T> onError(Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        if (hasError())
        {
            action.run(getError());
        }

        return this;
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

        final Throwable error = getError();
        if (Types.instanceOf(error, errorType))
        {
            action.run();
        }

        return this;
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

        final Throwable error = getError();
        if (Types.instanceOf(error, errorType))
        {
            action.run((TError)error);
        }

        return this;
    }

    private Result<Void> invokeThen(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return invokeResult(() ->
        {
            action.run();
            return Result.success();
        });
    }

    private Result<T> invokeCatch(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return invokeResult(() ->
        {
            action.run();
            return Result.success(getValue());
        });
    }

    private <U> Result<U> invoke(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return invokeResult(() -> Result.success(function.run()));
    }

    private <U> Result<U> invokeResult(Function0<Result<U>> function)
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

        if (Types.instanceOf(error, exceptionType))
        {
            throw (U)error;
        }
        else
        {
            Exceptions.throwAsRuntime(error);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
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

    private static final Result<?> successNull = Result.success(null);
    /**
     * Create a new empty successful Result.
     * @param <U> The type of value the Result should contain.
     */
    @SuppressWarnings("unchecked")
    public static <U> Result<U> success()
    {
        return (Result<U>)successNull;
    }

    private static final Result<Boolean> successTrue = Result.success(true);
    /**
     * Get a successful Result that contains a true boolean value.
     */
    static Result<Boolean> successTrue()
    {
        return successTrue;
    }

    private static final Result<Boolean> successFalse = Result.success(false);
    /**
     * Get a successful Result that contains a true boolean value.
     */
    static Result<Boolean> successFalse()
    {
        return successFalse;
    }

    /**
     * Create a new successful Result that contains the provided value.
     * @param value The value the Result should contain.
     * @param <U> The type of the value.
     */
    public static <U> Result<U> success(U value)
    {
        return new SyncResult<>(value, null);
    }

    /**
     * Create a new Result by synchronously running the provided Action and returning the result.
     * @param action The action to run.
     */
    public static Result<Void> create(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return Result.create(() ->
        {
            action.run();
            return null;
        });
    }

    /**
     * Create a new Result by synchronously running the provided Function and returning the result.
     * @param function The function to run.
     * @param <U> The type of value the function will return.
     */
    public static <U> Result<U> create(Function0<U> function)
    {
        PreCondition.assertNotNull(function, "function");

        Result<U> result;
        try
        {
            result = Result.success(function.run());
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
    public static <U> Result<U> error(Throwable error)
    {
        PreCondition.assertNotNull(error, "error");

        return new SyncResult<>(null, error);
    }

    public static Throwable createError(String message)
    {
        return new IllegalArgumentException(message);
    }
}
