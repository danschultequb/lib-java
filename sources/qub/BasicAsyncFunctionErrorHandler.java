package qub;

public class BasicAsyncFunctionErrorHandler<T> extends BasicAsyncFunction<T>
{
    private final Function1<Throwable,T> function;

    public BasicAsyncFunctionErrorHandler(Getable<AsyncRunner> asyncRunner, Iterable<AsyncTask> parentTasks, Function1<Throwable,T> function)
    {
        super(asyncRunner, parentTasks, null);

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
