package qub;

public class BasicAsyncAction extends BasicAsyncTask implements AsyncAction
{
    private final Action0 action;

    BasicAsyncAction(Getable<AsyncRunner> runner, Iterable<AsyncTask> parentTasks, Action0 action)
    {
        super(runner, parentTasks);

        this.action = action;
    }

    @Override
    public AsyncAction catchError(final Action1<Throwable> action)
    {
        return action == null ? null : catchErrorOnInner(getAsyncRunnerGetable(), action);
    }

    @Override
    public AsyncAction catchErrorOn(AsyncRunner asyncRunner, Action1<Throwable> action)
    {
        return asyncRunner == null || action == null ? null : catchErrorOnInner(new Value<>(asyncRunner), action);
    }

    private AsyncAction catchErrorOnInner(Getable<AsyncRunner> asyncRunner, Action1<Throwable> action)
    {
        return scheduleOrEnqueue(new BasicAsyncActionErrorHandler(asyncRunner, Array.fromValues(new AsyncTask[] { this }), action));
    }

    @Override
    public AsyncAction catchErrorAsyncAction(Function1<Throwable, AsyncAction> function)
    {
        return function == null ? null : catchErrorAsyncActionOnInner(getAsyncRunnerGetable(), function);
    }

    @Override
    public AsyncAction catchErrorAsyncActionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncAction> function)
    {
        return asyncRunner == null || function == null ? null : catchErrorAsyncActionOnInner(new Value<>(asyncRunner), function);
    }

    @Override
    protected void runTask()
    {
        final Throwable error = getIncomingError();
        if (error != null)
        {
            setOutgoingError(error);
        }
        else
        {
            if (action != null)
            {
                action.run();
            }
        }
    }

    @Override
    public AsyncAction thenOn(AsyncRunner runner)
    {
        return runner == getAsyncRunner() ? this : thenOn(runner, new Action0()
        {
            @Override
            public void run()
            {
            }
        });
    }
}
