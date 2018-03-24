package qub;

public class BasicAsyncAction extends BasicAsyncTask implements AsyncAction
{
    private static final Action0 emptyAction0 = new Action0()
    {
        @Override
        public void run()
        {
        }
    };

    private final Action0 action;

    public BasicAsyncAction(AsyncRunner runner, Action0 action)
    {
        this(runner, runner.getSynchronization(), action);
    }

    public BasicAsyncAction(AsyncRunner runner, Synchronization synchronization, Action0 action)
    {
        super(runner, synchronization);

        this.action = action;
    }

    @Override
    public AsyncAction catchError(final Action1<Throwable> action)
    {
        return action == null ? null : catchErrorOn(getAsyncRunner(), action);
    }

    @Override
    public AsyncAction catchErrorOn(AsyncRunner asyncRunner, Action1<Throwable> action)
    {
        return asyncRunner == null || action == null ? null : onErrorOnInner(asyncRunner, action);
    }

    private AsyncAction onErrorOnInner(AsyncRunner asyncRunner, Action1<Throwable> action)
    {
        return scheduleOrEnqueue(new BasicAsyncActionErrorHandler(asyncRunner, action));
    }

    @Override
    public AsyncAction catchErrorAsyncAction(Function1<Throwable, AsyncAction> function)
    {
        return function == null ? null : catchErrorAsyncActionOn(getAsyncRunner(), function);
    }

    @Override
    public AsyncAction catchErrorAsyncActionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncAction> function)
    {
        return asyncRunner == null || function == null ? null : onErrorAsyncActionOnInner(asyncRunner, function);
    }

    protected AsyncAction onErrorAsyncActionOnInner(final AsyncRunner asyncRunner, Function1<Throwable,AsyncAction> function)
    {
        final BasicAsyncAction result = new BasicAsyncAction(null, getSynchronization(), null);

        onErrorOnInner(asyncRunner, function)
            .then(new Action1<AsyncAction>()
            {
                @Override
                public void run(final AsyncAction asyncFunctionResult)
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
                        asyncFunctionResult.then(new Action0()
                        {
                            @Override
                            public void run()
                            {
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
        return runner == getAsyncRunner() ? this : thenOn(runner, emptyAction0);
    }
}
