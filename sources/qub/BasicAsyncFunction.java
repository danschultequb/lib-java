package qub;

public class BasicAsyncFunction<T> extends BasicAsyncTask implements AsyncFunction<T>
{
    private final Function0<T> function;
    private final Value<T> functionResult;

    public BasicAsyncFunction(Getable<AsyncRunner> runner, Function0<T> function)
    {
        super(runner, null);

        this.function = function;
        this.functionResult = Value.create();
    }

    protected void setFunctionResult(T functionResult)
    {
        this.functionResult.set(functionResult);
    }

    @Override
    public T awaitReturn()
    {
        await();
        return functionResult.hasValue() ? functionResult.get() : null;
    }

    @Override
    public AsyncAction then(Action1<T> action)
    {
        PreCondition.assertNotNull(action, "action");

        return then(() -> action.run(functionResult.hasValue() ? functionResult.get() : null));
    }

    @Override
    public <U> AsyncFunction<U> then(Function1<T, U> function)
    {
        PreCondition.assertNotNull(function, "function");

        return then(() -> function.run(functionResult.hasValue() ? functionResult.get() : null));
    }

    @Override
    public AsyncAction thenAsyncAction(Function1<T,AsyncAction> function)
    {
        PreCondition.assertNotNull(function, "function");

        return thenAsyncAction(() -> function.run(functionResult.hasValue() ? functionResult.get() : null));
    }

    @Override
    public <U> AsyncFunction<U> thenAsyncFunction(Function1<T, AsyncFunction<U>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return thenAsyncFunction(() -> function.run(functionResult.hasValue() ? functionResult.get() : null));
    }

    @Override
    public AsyncFunction<T> thenOn(AsyncRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        return runner == getAsyncRunner() ? this : thenOn(runner, (T value) -> value);
    }

    @Override
    public AsyncAction thenOn(AsyncRunner runner, Action1<T> action)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(action, "action");

        return thenOn(runner, () -> action.run(functionResult.hasValue() ? functionResult.get() : null));
    }

    @Override
    public <U> AsyncFunction<U> thenOn(AsyncRunner runner, Function1<T, U> function)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(function, "function");

        return thenOn(runner, () -> function.run(functionResult.hasValue() ? functionResult.get() : null));
    }

    @Override
    public AsyncFunction<T> catchError(final Action1<Throwable> action)
    {
        PreCondition.assertNotNull(action, "action");

        return catchError((Throwable error) ->
        {
            action.run(error);
            return null;
        });
    }

    @Override
    public AsyncAction catchErrorAsyncAction(Function1<Throwable, AsyncAction> function)
    {
        PreCondition.assertNotNull(function, "function");

        return catchErrorAsyncActionOnInner(getAsyncRunnerGetable(), function);
    }

    @Override
    public AsyncFunction<T> catchError(Function1<Throwable, T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return catchErrorOnInner(getAsyncRunnerGetable(), function);
    }

    @Override
    public AsyncAction catchErrorOn(AsyncRunner asyncRunner, final Action1<Throwable> action)
    {
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertNotNull(action, "action");

        return catchErrorOnInner(Value.create(asyncRunner), (Throwable error) ->
        {
            action.run(error);
            return null;
        });
    }

    @Override
    public AsyncFunction<T> catchErrorOn(AsyncRunner asyncRunner, Function1<Throwable, T> function)
    {
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertNotNull(function, "function");

        return catchErrorOnInner(Value.create(asyncRunner), function);
    }

    @Override
    public AsyncFunction<T> catchErrorAsyncFunction(Function1<Throwable, AsyncFunction<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return catchErrorAsyncFunctionOnInner(getAsyncRunnerGetable(), function);
    }

    @Override
    public AsyncAction catchErrorAsyncActionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncAction> function)
    {
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertNotNull(function, "function");

        return catchErrorOnInner(Value.create(asyncRunner), function);
    }

    @Override
    public AsyncFunction<T> catchErrorAsyncFunctionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncFunction<T>> function)
    {
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertNotNull(function, "function");

        return catchErrorAsyncFunctionOnInner(Value.create(asyncRunner), function);
    }

    private AsyncFunction<T> catchErrorAsyncFunctionOnInner(Getable<AsyncRunner> asyncRunner, Function1<Throwable,AsyncFunction<T>> function)
    {
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertNotNull(function, "function");

        final Value<AsyncRunner> resultAsyncRunner = Value.create();
        final Value<T> resultReturnValue = Value.create();
        final BasicAsyncFunction<T> result = new BasicAsyncFunction<>(resultAsyncRunner, () -> resultReturnValue.hasValue() ? resultReturnValue.get() : null);

        result.addParentTask(this.catchErrorOnInner(asyncRunner, function)
            .then((AsyncFunction<T> asyncFunctionResult) ->
            {
                if (asyncFunctionResult == null)
                {
                    resultAsyncRunner.set(asyncRunner.hasValue() ? asyncRunner.get() : null);
                    result.schedule();
                }
                else
                {
                    resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());
                    if (asyncFunctionResult.getOutgoingError() != null)
                    {
                        result.setIncomingError(asyncFunctionResult.getOutgoingError());
                    }
                    result.addParentTask(asyncFunctionResult
                        .then((T asyncFunctionResultResult) ->
                        {
                            resultReturnValue.set(asyncFunctionResultResult);
                            result.schedule();
                        }));
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
