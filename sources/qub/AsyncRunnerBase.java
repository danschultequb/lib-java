package qub;

public abstract class AsyncRunnerBase extends DisposableBase implements AsyncRunner
{
    @Override
    public AsyncAction scheduleAsyncAction(Function0<AsyncAction> function)
    {
        return schedule(new Action0()
            {
                @Override
                public void run()
                {
                }
            })
            .thenAsyncAction(function);
    }

    @Override
    public <T> AsyncFunction<T> scheduleAsyncFunction(Function0<AsyncFunction<T>> function)
    {
        return schedule(new Action0()
            {
                @Override
                public void run()
                {
                }
            })
            .thenAsyncFunction(function);
    }

    @Override
    public <T> AsyncFunction<Result<T>> success()
    {
        return done(null, null);
    }

    @Override
    public <T> AsyncFunction<Result<T>> success(final T value)
    {
        return done(value, null);
    }

    @Override
    public <T> AsyncFunction<Result<T>> error(Throwable error)
    {
        return done(null, error);
    }

    @Override
    public <T> AsyncFunction<Result<T>> done(final T value, final Throwable error)
    {
        return schedule(new Function0<Result<T>>()
        {
            @Override
            public Result<T> run()
            {
                return Result.done(value, error);
            }
        });
    }
}
