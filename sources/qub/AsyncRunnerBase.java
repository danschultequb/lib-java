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
        return done(Result.success(value));
    }

    @Override
    public <T> AsyncFunction<Result<T>> error(Throwable error)
    {
        return done(Result.<T>error(error));
    }

    @Override
    public <T> AsyncFunction<Result<T>> done(final T value, final Throwable error)
    {
        return done(Result.done(value, error));
    }

    @Override
    public <T> AsyncFunction<Result<T>> done(final Result<T> result)
    {
        return schedule(new Function0<Result<T>>()
        {
            @Override
            public Result<T> run()
            {
                return result;
            }
        });
    }

    @Override
    public <T> AsyncFunction<Result<T>> notNull(Object value, String parameterName)
    {
        final Result<T> innerResult = Result.notNull(value, parameterName);
        return innerResult == null ? null : done(innerResult);
    }

    @Override
    public <T,U> AsyncFunction<Result<U>> equal(T expectedValue, T actualValue, String parameterName)
    {
        final Result<U> innerResult = Result.equal(expectedValue, actualValue, parameterName);
        return innerResult == null ? null : done(innerResult);
    }

    @Override
    public <T> AsyncFunction<Result<T>> between(int lowerBound, int value, int upperBound, String parameterName)
    {
        final Result<T> innerResult = Result.between(lowerBound, value, upperBound, parameterName);
        return innerResult == null ? null : done(innerResult);
    }

    @Override
    public <T> AsyncFunction<Result<T>> greaterThan(int lowerBound, int value, String parameterName)
    {
        final Result<T> innerResult = Result.greaterThan(lowerBound, value, parameterName);
        return innerResult == null ? null : done(innerResult);
    }
}
