package qub;

public class Result<T>
{
    private final T value;
    private final Throwable error;

    Result(T value, Throwable error)
    {
        this.value = value;
        this.error = error;
    }

    final public T getValue()
    {
        return value;
    }

    final public boolean hasError()
    {
        return error != null;
    }

    final public Throwable getError()
    {
        return error;
    }

    final public Class<? extends Throwable> getErrorType()
    {
        return error == null ? null : error.getClass();
    }

    /**
     * If this Result has an error, then return the error's message. If this Result doesn't have an
     * error, then return null.
     * @return The message of this Result's error, or null if this Result doesn't have an error.
     */
    final public String getErrorMessage()
    {
        return error == null ? null : error.getMessage();
    }

    /**
     * If this Result has an error, then the error will be thrown as a RuntimeException.
     */
    final public void throwError()
    {
        Exceptions.throwAsRuntime(error);
    }

    /**
     * If this Result has an error, then the error will be thrown as a RuntimeException. If the
     * Result does not have an error, then the value will be returned.
     * @return The value of this Result.
     */
    public T throwErrorOrGetValue()
    {
        throwError();
        return value;
    }

    /**
     * Convert this Result object to the target Result object type if this Result has an error. If
     * this Result does not have an error, then return null.
     * @param <U> The target Result object type.
     * @return The converted Result object if this Result has an error or null if this Result does
     * not have an error.
     */
    final <U> Result<U> convertError()
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
     * If this Result doesn't have an error, then run the provided action and return a new Result
     * object with the action's result. If this Result's value is Disposable, then the value will be
     * disposed when the action is complete.
     * @param action The action to run if this result does not have an error.
     * @return The Result of running the provided action.
     */
    public Result<Void> thenDispose(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        Result<Void> result;
        try
        {
            result = then(action);
        }
        finally
        {
            if (Types.instanceOf(value, Disposable.class))
            {
                ((Disposable)value).dispose();
            }
        }
        return result;
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
        final Result<U> result = resultResult.hasError() ? resultResult.convertError() : resultResult.value;

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
    @SuppressWarnings("unchecked")
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
            result = invokeResult(function::run);
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
            result = invokeResult(function::run);
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
    @SuppressWarnings("unchecked")
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
        return rhs instanceof Result && equals((Result<T>)rhs);
    }

    public boolean equals(Result<T> rhs)
    {
        return rhs != null && Comparer.equal(value, rhs.value) && Comparer.equal(error, rhs.error);
    }

    public static <U> Result<U> success()
    {
        return success(null);
    }

    public static <U> Result<U> success(U value)
    {
        return new Result<>(value, null);
    }

    private static final Result<Boolean> successFalse = Result.success(false);
    public static Result<Boolean> successFalse()
    {
        return successFalse;
    }

    private static final Result<Boolean> successTrue = Result.success(true);
    public static Result<Boolean> successTrue()
    {
        return successTrue;
    }

    public static <U> Result<U> error(Throwable error)
    {
        PreCondition.assertNotNull(error, "error");

        return new Result<>(null, error);
    }

    /**
     * Run the provided body action as long as the provided condition function returns true. If
     * condition or body thrown an error, then the error will be returned as a Result.
     * @param condition The condition that will determine whether or not the body action is run.
     * @param body The body of the while loop.
     * @return The result of the while loop.
     */
    public static Result<Void> runWhile(Function0<Boolean> condition, Action0 body)
    {
        PreCondition.assertNotNull(condition, "condition");
        PreCondition.assertNotNull(body, "body");

        Result<Void> result;
        try
        {
            Boolean conditionResult = condition.run();
            while (conditionResult != null && conditionResult)
            {
                body.run();

                conditionResult = condition.run();
            }
            result = Result.success();
        }
        catch (Throwable error)
        {
            result = error(error);
        }

        return result;
    }

    public static <U> Result<U> isFalse(boolean value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return Result.equal(false, value, expressionName);
    }

    public static <U> Result<U> isTrue(boolean value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        return Result.equal(true, value, expressionName);
    }

    public static <T,U> Result<U> equal(T expectedValue, T value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (!Comparer.equal(expectedValue, value))
        {
            result = Result.error(createError(expressionName + " (" + value + ") must be " + expectedValue + "."));
        }
        return result;
    }

    public static <U> Result<U> notNull(Object value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (value == null)
        {
            result = Result.error(createError(expressionName + " cannot be null."));
        }
        return result;
    }

    public static <U> Result<U> notNullAndNotEmpty(String value, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = Result.notNull(value, expressionName);
        if (result == null)
        {
            if (value.isEmpty())
            {
                result = Result.error(createError(expressionName + " cannot be empty."));
            }
        }
        return result;
    }

    public static <U> Result<U> greaterThan(int value, int lowerBound, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (value <= lowerBound)
        {
            result = Result.error(createError(expressionName + " (" + value + ") must be greater than " + lowerBound + "."));
        }
        return result;
    }

    public static <U> Result<U> greaterThanOrEqualTo(int value, int lowerBound, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (!Comparer.greaterThanOrEqualTo(value, lowerBound))
        {
            result = Result.error(createError(AssertionMessages.greaterThanOrEqualTo(value, lowerBound, expressionName)));
        }
        return result;
    }

    public static <U> Result<U> between(int lowerBound, int value, int upperBound, String expressionName)
    {
        PreCondition.assertNotNullAndNotEmpty(expressionName, "expressionName");

        Result<U> result = null;
        if (lowerBound == upperBound)
        {
            result = Result.equal(lowerBound, value, expressionName);
        }
        else if (upperBound < lowerBound)
        {
            result = Result.between(upperBound, value, lowerBound, expressionName);
        }
        else if (value < lowerBound || upperBound < value)
        {
            result = Result.error(createError(AssertionMessages.between(lowerBound, value, upperBound, expressionName)));
        }
        return result;
    }

    private static Throwable createError(String message)
    {
        return new IllegalArgumentException(message);
    }
}
