package qub;

public interface AsyncDisposable extends Disposable
{
    AsyncFunction<Result<Boolean>> disposeAsync();
}
