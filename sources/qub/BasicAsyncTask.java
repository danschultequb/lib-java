package qub;

public abstract class BasicAsyncTask implements AsyncTask, PausedAsyncTask
{
    private AsyncRunner runner;
    private final Synchronization synchronization;
    private final List<BasicAsyncTask> pausedTasks;
    private final Gate completionGate;
    private Throwable incomingError;
    private Throwable outgoingError;

    BasicAsyncTask(AsyncRunner runner, Synchronization synchronization)
    {
        this.runner = runner;
        this.synchronization = synchronization;
        this.pausedTasks = new SingleLinkList<>();
        this.completionGate = synchronization.createGate(false);
    }

    @Override
    public AsyncRunner getAsyncRunner() {
        return runner;
    }

    protected void setRunner(AsyncRunner runner)
    {
        for (BasicAsyncTask pausedTask : pausedTasks)
        {
            if (pausedTask.getAsyncRunner() == this.runner)
            {
                pausedTask.setRunner(runner);
            }
        }
        this.runner = runner;
    }

    protected Synchronization getSynchronization()
    {
        return synchronization;
    }

    @Override
    public void await()
    {
        completionGate.passThrough();
    }

    /**
     * Get the number of PausedAsyncTasks that are waiting for this AsyncTask to complete.
     * @return The number of PausedAsyncTasks that are waiting for this AsyncTask to complete.
     */
    public int getPausedTaskCount()
    {
        return pausedTasks.getCount();
    }

    @Override
    public boolean isCompleted()
    {
        return completionGate.isOpen();
    }

    @Override
    public Throwable getIncomingError()
    {
        return incomingError;
    }

    @Override
    public void setIncomingError(Throwable incomingError)
    {
        this.incomingError = incomingError;
    }

    @Override
    public Throwable getOutgoingError()
    {
        return outgoingError;
    }

    protected void setOutgoingError(Throwable outgoingError)
    {
        this.outgoingError = outgoingError;
    }

    @Override
    public void schedule()
    {
        runner.schedule(this);
    }

    @Override
    public AsyncAction then(Action0 action)
    {
        return action == null ? null : thenOnInner(runner, action);
    }

    @Override
    public <T> AsyncFunction<T> then(Function0<T> function)
    {
        return function == null ? null : thenOnInner(runner, function);
    }

    @Override
    public AsyncAction thenOn(AsyncRunner runner, Action0 action)
    {
        return runner == null || action == null ? null : thenOnInner(runner, action);
    }

    @Override
    public <T> AsyncFunction<T> thenOn(AsyncRunner runner, Function0<T> function)
    {
        return runner == null || function == null ? null : thenOnInner(runner, function);
    }

    private BasicAsyncAction thenOnInner(AsyncRunner runner, Action0 action)
    {
        return scheduleOrEnqueue(new BasicAsyncAction(runner, synchronization, action));
    }

    private <T> BasicAsyncFunction<T> thenOnInner(AsyncRunner runner, Function0<T> function)
    {
        return scheduleOrEnqueue(new BasicAsyncFunction<T>(runner, synchronization, function));
    }

    @Override
    public AsyncAction thenAsyncAction(Function0<AsyncAction> function)
    {
        return function == null ? null : thenOnAsyncActionInner(runner, function);
    }

    @Override
    public <T> AsyncFunction<T> thenAsyncFunction(Function0<AsyncFunction<T>> function)
    {
        return function == null ? null : thenOnAsyncFunctionInner(runner, function);
    }

    @Override
    public AsyncAction thenAsyncActionOn(AsyncRunner runner, Function0<AsyncAction> function)
    {
        return runner == null || function == null ? null : thenOnAsyncActionInner(runner, function);
    }

    protected AsyncAction thenOnAsyncActionInner(final AsyncRunner runner, Function0<AsyncAction> function)
    {
        final BasicAsyncAction result = new BasicAsyncAction(null, synchronization, null);

        thenOnInner(runner, function)
            .then(new Action1<AsyncAction>()
            {
                @Override
                public void run(final AsyncAction asyncFunctionResult)
                {
                    result.setRunner(asyncFunctionResult.getAsyncRunner());
                    asyncFunctionResult
                        .catchError(new Action1<Throwable>()
                        {
                            @Override
                            public void run(Throwable error)
                            {
                                result.setOutgoingError(error);
                            }
                        })
                        .then(new Action0()
                        {
                            @Override
                            public void run()
                            {
                                result.schedule();
                            }
                        });
                }
            })
            .catchError(new Action1<Throwable>()
            {
                @Override
                public void run(Throwable error)
                {
                    result.setOutgoingError(error);
                    result.setRunner(runner);
                    result.schedule();
                }
            });

        return result;
    }

    @Override
    public <T> AsyncFunction<T> thenAsyncFunctionOn(AsyncRunner runner, Function0<AsyncFunction<T>> function)
    {
        return runner == null || function == null ? null : thenOnAsyncFunctionInner(runner, function);
    }

    protected <T> AsyncFunction<T> thenOnAsyncFunctionInner(final AsyncRunner runner, Function0<AsyncFunction<T>> function)
    {
        final Value<T> asyncFunctionResultValue = new Value<>();
        final BasicAsyncFunction<T> result = new BasicAsyncFunction<T>(null, synchronization, new Function0<T>()
        {
            @Override
            public T run()
            {
                return asyncFunctionResultValue.get();
            }
        });

        thenOnInner(runner, function)
            .then(new Action1<AsyncFunction<T>>()
            {
                @Override
                public void run(final AsyncFunction<T> asyncFunctionResult)
                {
                    result.setRunner(asyncFunctionResult.getAsyncRunner());

                    asyncFunctionResult
                        .then(new Action1<T>()
                        {
                            @Override
                            public void run(T asyncFunctionResultResult)
                            {
                                asyncFunctionResultValue.set(asyncFunctionResultResult);
                            }
                        })
                        .catchError(new Action1<Throwable>()
                        {
                            @Override
                            public void run(Throwable error)
                            {
                                result.setOutgoingError(error);
                            }
                        })
                        .then(new Action0()
                        {
                            @Override
                            public void run()
                            {
                                result.schedule();
                            }
                        });
                }
            })
            .catchError(new Action1<Throwable>()
            {
                @Override
                public void run(Throwable error)
                {
                    result.setOutgoingError(error);
                    result.setRunner(runner);
                    result.schedule();
                }
            });

        return result;
    }

    protected <U> AsyncFunction<U> onErrorOnInner(AsyncRunner asyncRunner, Function1<Throwable,U> function)
    {
        return scheduleOrEnqueue(new BasicAsyncFunctionErrorHandler<U>(asyncRunner, function));
    }

    protected <T extends BasicAsyncTask> T scheduleOrEnqueue(T asyncTask)
    {
        if (isCompleted())
        {
            asyncTask.setIncomingError(getOutgoingError());
            asyncTask.schedule();
        }
        else
        {
            pausedTasks.add(asyncTask);
        }
        return asyncTask;
    }

    @Override
    public void runAndSchedulePausedTasks()
    {
        try
        {
            runTask();
        }
        catch (Throwable error)
        {
            setOutgoingError(error);
        }

        final Throwable outgoingError = getOutgoingError();

        while (pausedTasks.any())
        {
            final BasicAsyncTask pausedTask = pausedTasks.removeFirst();
            pausedTask.setIncomingError(outgoingError);
            pausedTask.schedule();
        }

        completionGate.open();
    }

    protected abstract void runTask();
}
