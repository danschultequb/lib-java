package qub;

public interface AsyncDisposable extends Disposable
{
    AsyncRunner getAsyncRunner();

    AsyncFunction<Result<Boolean>> disposeAsync();
}
