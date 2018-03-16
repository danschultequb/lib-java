package qub;

public class Result<T>
{
    private final T value;
    private final Throwable error;

    private Result(T value, Throwable error)
    {
        this.value = value;
        this.error = error;
    }

    public boolean isSuccess()
    {
        return !isError();
    }

    public T getValue()
    {
        return value;
    }

    public boolean isError()
    {
        return error != null;
    }

    public Throwable getError()
    {
        return error;
    }

    public String getErrorMessage()
    {
        return error == null ? null : error.getMessage();
    }

    public static <T> Result<T> success(T value)
    {
        return new Result<>(value, null);
    }

    public static <T> Result<T> error(Throwable error)
    {
        return new Result<>(null, error);
    }
}
