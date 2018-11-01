package qub;

public class BasicAsyncFunction<T> extends BasicAsyncTask implements AsyncFunction<T>
{
    private final Function0<T> function;
    private final Value<T> functionResult;

    public BasicAsyncFunction(Getable<AsyncRunner> runner, Function0<T> function)
    {
        super(runner, null);

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
        PreCondition.assertNotNull(action, "action");

        return then(() -> action.run(functionResult.get()));
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
    public AsyncFunction<T> catchError(final Action1<Throwable> action)
    {
        return action == null ? null : catchError(new Function1<Throwable,T>()
        {
            @Override
            public T run(Throwable error)
            {
                action.run(error);
                return null;
            }
        });
    }

    @Override
    public AsyncAction catchErrorAsyncAction(Function1<Throwable, AsyncAction> function)
    {
        return function == null ? null : catchErrorAsyncActionOnInner(getAsyncRunnerGetable(), function);
    }

    @Override
    public AsyncFunction<T> catchError(Function1<Throwable, T> function)
    {
        return function == null ? null : catchErrorOnInner(getAsyncRunnerGetable(), function);
    }

    @Override
    public AsyncAction catchErrorOn(AsyncRunner asyncRunner, final Action1<Throwable> action)
    {
        return asyncRunner == null || action == null ? null : catchErrorOnInner(new Value<>(asyncRunner), new Function1<Throwable, T>()
        {
            @Override
            public T run(Throwable error)
            {
                action.run(error);
                return null;
            }
        });
    }

    @Override
    public AsyncFunction<T> catchErrorOn(AsyncRunner asyncRunner, Function1<Throwable, T> function)
    {
        return asyncRunner == null || function == null ? null : catchErrorOnInner(new Value<>(asyncRunner), function);
    }

    @Override
    public AsyncFunction<T> catchErrorAsyncFunction(Function1<Throwable, AsyncFunction<T>> function)
    {
        return function == null ? null : catchErrorAsyncFunctionOnInner(getAsyncRunnerGetable(), function);
    }

    @Override
    public AsyncAction catchErrorAsyncActionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncAction> function)
    {
        return asyncRunner == null || function == null ? null : catchErrorOnInner(new Value<>(asyncRunner), function);
    }

    @Override
    public AsyncFunction<T> catchErrorAsyncFunctionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncFunction<T>> function)
    {
        return asyncRunner == null || function == null ? null : catchErrorAsyncFunctionOnInner(new Value<>(asyncRunner), function);
    }

    private AsyncFunction<T> catchErrorAsyncFunctionOnInner(final Getable<AsyncRunner> asyncRunner, Function1<Throwable,AsyncFunction<T>> function)
    {
        final Value<AsyncRunner> resultAsyncRunner = new Value<AsyncRunner>();
        final Value<T> resultReturnValue = new Value<T>();
        final BasicAsyncFunction<T> result = new BasicAsyncFunction<T>(resultAsyncRunner, new Function0<T>()
        {
            @Override
            public T run()
            {
                return resultReturnValue.get();
            }
        });

        result.addParentTask(this.catchErrorOnInner(asyncRunner, function)
            .then(new Action1<AsyncFunction<T>>()
            {
                @Override
                public void run(final AsyncFunction<T> asyncFunctionResult)
                {
                    if (asyncFunctionResult == null)
                    {
                        resultAsyncRunner.set(asyncRunner.get());
                        result.schedule();
                    }
                    else
                    {
                        resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());
                        if (asyncFunctionResult.getOutgoingError() != null)
                        {
                            result.setIncomingError(asyncFunctionResult.getOutgoingError());
                        }
                        result.addParentTask(asyncFunctionResult.then(new Action1<T>()
                        {
                            @Override
                            public void run(T asyncFunctionResultResult)
                            {
                                resultReturnValue.set(asyncFunctionResultResult);
                                result.schedule();
                            }
                        }));
                    }
                }
            }));

        return result;
    }

    @Override
    protected void runTask()
    {
        setFunctionResult(function.run());
    }
}
