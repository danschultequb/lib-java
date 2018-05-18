package qub;

public abstract class DisposableBase implements Disposable
{
    @Override
    public void close()
    {
        DisposableBase.close(this);
    }

    public static void close(Disposable disposable)
    {
        final Result<Boolean> result = disposable.dispose();
        result.throwError();
    }

    /**
     * Validate that the provided disposable is not null and not disposed.
     * @param disposable The Disposable value to validate.
     * @param parameterName The name of the Disposable value.
     * @param <T> The type of the Result that will be returned.
     * @return A Result object if the disposable is null or disposed, or null if the Disposable
     * passes validation.
     */
    public static <T> Result<T> validateNotDisposed(Disposable disposable, String parameterName)
    {
        Result<T> result = Result.notNull(disposable, parameterName);
        if (result == null)
        {
            result = Result.equal(false, disposable.isDisposed(), parameterName = ".isDisposed()");
        }
        return result;
    }
}
