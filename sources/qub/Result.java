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

    final public boolean isSuccess()
    {
        return !isError();
    }

    final public boolean isError()
    {
        return error != null;
    }

    final public Throwable getError()
    {
        return error;
    }

    final public String getErrorMessage()
    {
        return error == null ? null : error.getMessage();
    }

    public static <T> Result<T> success()
    {
        return Result.<T>success(null);
    }

    public static <T> Result<T> success(T value)
    {
        return new Result<T>(value, null);
    }

    public static <T> Result<T> error(Throwable error)
    {
        return new Result<T>(null, error);
    }
}
