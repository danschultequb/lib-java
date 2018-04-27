package qub;

public abstract class AsyncDisposableBase extends DisposableBase implements AsyncDisposable
{
    @Override
    final public AsyncFunction<Result<Boolean>> disposeAsync()
    {
        return disposeAsync(this);
    }

    public static AsyncFunction<Result<Boolean>> disposeAsync(final AsyncDisposable disposable)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final AsyncRunner disposableAsyncRunner = disposable.getAsyncRunner();
        return disposableAsyncRunner.schedule(new Function0<Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run()
                {
                    return disposable.dispose();
                }
            })
            .thenOn(currentAsyncRunner);
    }
}
