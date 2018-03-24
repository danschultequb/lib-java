package qub;

public abstract class BasicAsyncTask implements AsyncAction, PausedAsyncTask
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
    public AsyncRunner getRunner() {
        return runner;
    }

    protected void setRunner(AsyncRunner runner)
    {
        for (BasicAsyncTask pausedTask : pausedTasks)
        {
            if (pausedTask.getRunner() == this.runner)
            {
                pausedTask.setRunner(runner);
            }
        }
        this.runner = runner;
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
                    result.setRunner(asyncFunctionResult.getRunner());
                    asyncFunctionResult
                        .onError(new Action1<Throwable>()
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
            .onError(new Action1<Throwable>()
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
                    result.setRunner(asyncFunctionResult.getRunner());

                    asyncFunctionResult
                        .then(new Action1<T>()
                        {
                            @Override
                            public void run(T asyncFunctionResultResult)
                            {
                                asyncFunctionResultValue.set(asyncFunctionResultResult);
                            }
                        })
                        .onError(new Action1<Throwable>()
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
            .onError(new Action1<Throwable>()
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
    public AsyncAction onError(final Action1<Throwable> action)
    {
        return action == null ? null : onErrorOn(runner, action);
    }

    @Override
    public <T> AsyncFunction<T> onError(Function1<Throwable, T> function)
    {
        return function == null ? null : onErrorOn(runner, function);
    }

    @Override
    public AsyncAction onErrorOn(AsyncRunner asyncRunner, Action1<Throwable> action)
    {
        return asyncRunner == null || action == null ? null : onErrorOnInner(asyncRunner, action);
    }

    @Override
    public <T> AsyncFunction<T> onErrorOn(AsyncRunner asyncRunner, Function1<Throwable, T> function)
    {
        return asyncRunner == null || function == null ? null : onErrorOnInner(asyncRunner, function);
    }

    private AsyncAction onErrorOnInner(AsyncRunner asyncRunner, Action1<Throwable> action)
    {
        return scheduleOrEnqueue(new BasicAsyncActionErrorHandler(asyncRunner, action));
    }

    private <T> AsyncFunction<T> onErrorOnInner(AsyncRunner asyncRunner, Function1<Throwable,T> function)
    {
        return scheduleOrEnqueue(new BasicAsyncFunctionErrorHandler<T>(asyncRunner, function));
    }

    @Override
    public AsyncAction onErrorAsyncAction(Function1<Throwable, AsyncAction> function)
    {
        return function == null ? null : onErrorAsyncActionOn(runner, function);
    }

    @Override
    public <T> AsyncFunction<T> onErrorAsyncFunction(Function1<Throwable, AsyncFunction<T>> function)
    {
        return function == null ? null : onErrorAsyncFunctionOn(runner, function);
    }

    @Override
    public AsyncAction onErrorAsyncActionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncAction> function)
    {
        return asyncRunner == null || function == null ? null : onErrorAsyncActionOnInner(asyncRunner, function);
    }

    @Override
    public <T> AsyncFunction<T> onErrorAsyncFunctionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncFunction<T>> function)
    {
        return asyncRunner == null || function == null ? null : onErrorAsyncFunctionOnInner(asyncRunner, function);
    }

    private AsyncAction onErrorAsyncActionOnInner(final AsyncRunner asyncRunner, Function1<Throwable,AsyncAction> function)
    {
        final BasicAsyncAction result = new BasicAsyncAction(null, synchronization, null);

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
                        result.setRunner(asyncFunctionResult.getRunner());
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

    private <T> AsyncFunction<T> onErrorAsyncFunctionOnInner(final AsyncRunner asyncRunner, Function1<Throwable,AsyncFunction<T>> function)
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
                        result.setRunner(asyncFunctionResult.getRunner());
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

    private <T extends BasicAsyncTask> T scheduleOrEnqueue(T asyncTask)
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
