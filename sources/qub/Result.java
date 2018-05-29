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

    final public String getErrorMessage()
    {
        return error == null ? null : error.getMessage();
    }

    final public void throwError()
    {
        Exceptions.throwAsRuntime(error);
    }

    @SuppressWarnings("unchecked")
    final public <U extends Exception> void throwError(Class<U> exceptionType) throws U
    {
        if (error != null)
        {
            if (exceptionType != null && Types.isSubTypeOf(error, exceptionType))
            {
                throw (U)error;
            }
            else if (error instanceof RuntimeException)
            {
                throw (RuntimeException)error;
            }
            else
            {
                throw new RuntimeException(error);
            }
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
        return Result.done(null, error);
    }

    public static <U> Result<U> done(U value, Throwable error)
    {
        return new Result<U>(value, error);
    }

    public static <T,U> Result<U> equal(T expectedValue, T value, String parameterName)
    {
        Result<U> result = null;
        if (!Comparer.equal(expectedValue, value))
        {
            result = Result.error(new IllegalArgumentException(parameterName + " (" + value + ") must be " + expectedValue + "."));
        }
        return result;
    }

    public static <U> Result<U> notNull(Object value, String parameterName)
    {
        Result<U> result = null;
        if (value == null)
        {
            result = Result.error(new IllegalArgumentException(parameterName + " cannot be null."));
        }
        return result;
    }

    public static <U> Result<U> notNullAndNotEmpty(String value, String parameterName)
    {
        Result<U> result = Result.notNull(value, parameterName);
        if (result == null)
        {
            if (value.isEmpty())
            {
                result = Result.error(new IllegalArgumentException(parameterName + " cannot be empty."));
            }
        }
        return result;
    }

    public static <U> Result<U> notNullAndNotEmpty(byte[] value, String parameterName)
    {
        Result<U> result = Result.notNull(value, parameterName);
        if (result == null)
        {
            if (value.length == 0)
            {
                result = Result.error(new IllegalArgumentException(parameterName + " cannot be empty."));
            }
        }
        return result;
    }

    public static <U> Result<U> notNullAndNotEmpty(char[] value, String parameterName)
    {
        Result<U> result = Result.notNull(value, parameterName);
        if (result == null)
        {
            if (value.length == 0)
            {
                result = Result.error(new IllegalArgumentException(parameterName + " cannot be empty."));
            }
        }
        return result;
    }

    public static <U> Result<U> greaterThan(int lowerBound, int value, String parameterName)
    {
        Result<U> result = null;
        if (value <= lowerBound)
        {
            result = Result.error(new IllegalArgumentException(parameterName + " (" + value + ") must be greater than " + lowerBound + "."));
        }
        return result;
    }

    public static <U> Result<U> greaterThanOrEqualTo(int lowerBound, int value, String parameterName)
    {
        Result<U> result = null;
        if (value < lowerBound)
        {
            result = Result.error(new IllegalArgumentException(parameterName + " (" + value + ") must be greater than or equal to " + lowerBound + "."));
        }
        return result;
    }

    public static <U> Result<U> between(int lowerBound, int value, int upperBound, String parameterName)
    {
        Result<U> result = null;
        if (value < lowerBound || upperBound < value)
        {
            result = Result.error(new IllegalArgumentException(parameterName + " (" + value + ") must be between " + lowerBound + " and " + upperBound + "."));
        }
        return result;
    }

    public static <U> Result<U> notDisposed(Disposable disposable, String parameterName)
    {
        Result<U> result = null;
        if (disposable.isDisposed())
        {
            result = Result.error(new IllegalArgumentException(parameterName + " must not be disposed."));
        }
        return result;
    }
}
