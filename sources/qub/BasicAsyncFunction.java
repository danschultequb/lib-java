package qub;

public class BasicAsyncFunction<T> extends BasicAsyncTask implements AsyncFunction<T>
{
    private final Function0<T> function;
    private final Value<T> functionResult;

    public BasicAsyncFunction(AsyncRunner runner, Synchronization synchronization, Function0<T> function)
    {
        super(runner, synchronization);

        this.function = function;
        this.functionResult = new Value<>();
    }

    protected void setFunctionResult(T functionResult)
    {
        this.functionResult.set(functionResult);
    }

    @Override
    public T awaitReturn()
    {
        await();
        return functionResult.get();
    }

    @Override
    public AsyncAction then(final Action1<T> action)
    {
        return action == null ? null : then(new Action0()
        {
            @Override
            public void run()
            {
                action.run(functionResult.get());
            }
        });
    }

    @Override
    public <U> AsyncFunction<U> then(final Function1<T, U> function)
    {
        return function == null ? null : then(new Function0<U>()
        {
            @Override
            public U run()
            {
                return function.run(functionResult.get());
            }
        });
    }

    @Override
    public AsyncAction thenAsyncAction(final Function1<T,AsyncAction> function)
    {
        return function == null ? null : thenAsyncAction(new Function0<AsyncAction>()
        {
            @Override
            public AsyncAction run()
            {
                return function.run(functionResult.get());
            }
        });
    }

    @Override
    public <U> AsyncFunction<U> thenAsyncFunction(final Function1<T, AsyncFunction<U>> function)
    {
        return function == null ? null : thenAsyncFunction(new Function0<AsyncFunction<U>>()
        {
            @Override
            public AsyncFunction<U> run()
            {
                return function.run(functionResult.get());
            }
        });
    }

    @Override
    public AsyncFunction<T> thenOn(AsyncRunner runner)
    {
        return runner == null ? null : runner == getAsyncRunner() ? this : thenOn(runner, new Function1<T, T>()
        {
            @Override
            public T run(T value)
            {
                return value;
            }
        });
    }

    @Override
    public AsyncAction thenOn(AsyncRunner runner, final Action1<T> action)
    {
        return runner == null || action == null ? null : thenOn(runner, new Action0()
        {
            @Override
            public void run()
            {
                action.run(functionResult.get());
            }
        });
    }

    @Override
    public <U> AsyncFunction<U> thenOn(AsyncRunner runner, final Function1<T, U> function)
    {
        return runner == null || function == null ? null : thenOn(runner, new Function0<U>()
        {
            @Override
            public U run()
            {
                return function.run(functionResult.get());
            }
        });
    }

    @Override
    public AsyncFunction<T> catchError(Function1<Throwable, T> function)
    {
        return function == null ? null : catchErrorOn(getAsyncRunner(), function);
    }

    @Override
    public AsyncFunction<T> catchErrorOn(AsyncRunner asyncRunner, Function1<Throwable, T> function)
    {
        return asyncRunner == null || function == null ? null : onErrorOnInner(asyncRunner, function);
    }

    @Override
    public AsyncFunction<T> catchErrorAsyncFunction(Function1<Throwable, AsyncFunction<T>> function)
    {
        return function == null ? null : catchErrorAsyncFunctionOn(getAsyncRunner(), function);
    }

    @Override
    public AsyncFunction<T> catchErrorAsyncFunctionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncFunction<T>> function)
    {
        return asyncRunner == null || function == null ? null : onErrorAsyncFunctionOnInner(asyncRunner, function);
    }

    private AsyncFunction<T> onErrorAsyncFunctionOnInner(final AsyncRunner asyncRunner, Function1<Throwable,AsyncFunction<T>> function)
    {
        final Value<T> asyncFunctionResultValue = new Value<>();
        final BasicAsyncFunction<T> result = new BasicAsyncFunction<T>(null, getSynchronization(), new Function0<T>()
        {
            @Override
            public T run()
            {
                return asyncFunctionResultValue.get();
            }
        });

        onErrorOnInner(asyncRunner, function)
            .then(new Action1<AsyncFunction<T>>()
            {
                @Override
                public void run(final AsyncFunction<T> asyncFunctionResult)
                {
                    if (asyncFunctionResult == null)
                    {
                        result.setRunner(asyncRunner);
                        result.schedule();
                    }
                    else
                    {
                        result.setRunner(asyncFunctionResult.getAsyncRunner());
                        result.setIncomingError(asyncFunctionResult.getOutgoingError());
                        asyncFunctionResult.then(new Action1<T>()
                        {
                            @Override
                            public void run(T asyncFunctionResultResult)
                            {
                                asyncFunctionResultValue.set(asyncFunctionResultResult);
                                result.schedule();
                            }
                        });
                    }
                }
            });

        return result;
    }

    @Override
    protected void runTask()
    {
        setFunctionResult(function.run());
    }
}
