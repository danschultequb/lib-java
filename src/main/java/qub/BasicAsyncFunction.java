package qub;

public class BasicAsyncFunction<T> extends BasicAsyncTask implements AsyncFunction<T>, PausedAsyncFunction<T>
{
    private final Function0<T> function;
    private final Value<T> functionResult;

    public BasicAsyncFunction(AsyncRunner runner, Function0<T> function)
    {
        super(runner);

        this.function = function;
        this.functionResult = new Value<>();
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
        functionResult.set(function.run());
    }
}
