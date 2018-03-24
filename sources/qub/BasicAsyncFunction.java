package qub;

public class BasicAsyncFunction<T> extends BasicAsyncTask implements AsyncFunction<T>, PausedAsyncFunction<T>
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
        return runner == null ? null : runner == getRunner() ? this : thenOn(runner, new Function1<T, T>()
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
    protected void runTask()
    {
        setFunctionResult(function.run());
    }
}
