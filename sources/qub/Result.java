package qub;

final public class Result<T>
{
    private final T value;
    private final Throwable error;

    private Result(T value, Throwable error)
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
            try
            {
                action.run();
                result = Result.success();
            }
            catch (Throwable error)
            {
                result = Result.error(error);
            }
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

        return then(() -> action.run(getValue()));
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

        final Value<U> resultValue = new Value<>();
        final Result<Void> thenActionResult = then(() -> resultValue.set(function.run()));
        return Result.done(resultValue.get(), thenActionResult.getError());
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
        final Result<U> result = resultResult.getValue();
        return result != null ? result : resultResult.convertError();
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

        return then(() -> function.run(getValue()));
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

        return thenResult(() -> function.run(getValue()));
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

    private static final Result<Void> success = Result.success(null);
    public static Result<Void> success()
    {
        return success;
    }

    public static <U> Result<U> success(U value)
    {
        return Result.done(value, null);
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

        return Result.done(null, error);
    }

    public static <U> Result<U> done(U value, Throwable error)
    {
        return new Result<U>(value, error);
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
