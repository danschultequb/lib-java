package qub;

public class BasicAsyncFunctionErrorHandler<T> extends BasicAsyncFunction<T>
{
    private final Function1<Throwable,T> function;

    public BasicAsyncFunctionErrorHandler(AsyncRunner runner, Function1<Throwable,T> function)
    {
        this(runner, runner.getSynchronization(), function);
    }

    public BasicAsyncFunctionErrorHandler(AsyncRunner runner, Synchronization synchronization, Function1<Throwable,T> function)
    {
        super(runner, synchronization, null);

        this.function = function;
    }

    @Override
    protected void runTask()
    {
        final Throwable error = getIncomingError();
        if (error != null)
        {
            setFunctionResult(function.run(error));
        }
    }
}
