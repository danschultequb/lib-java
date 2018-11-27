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
