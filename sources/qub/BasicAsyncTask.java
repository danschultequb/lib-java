package qub;

public abstract class BasicAsyncTask implements PausedAsyncTask
{
    private final Getable<AsyncRunner> asyncRunner;
    private final Indexable<AsyncTask> parentTasks;
    private final List<BasicAsyncTask> pausedTasks;
    private volatile Action0 afterChildTasksScheduledBeforeCompletedAction;
    private volatile Throwable incomingError;
    private volatile Throwable outgoingError;
    private volatile boolean completed;

    BasicAsyncTask(Getable<AsyncRunner> asyncRunner, Indexable<AsyncTask> parentTask)
    {
        this.asyncRunner = asyncRunner;
        this.parentTasks = parentTask;
        this.pausedTasks = new SingleLinkList<>();
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner.get();
    }

    protected Getable<AsyncRunner> getAsyncRunnerGetable()
    {
        return asyncRunner;
    }

    @Override
    public Indexable<AsyncTask> getParentTasks()
    {
        return parentTasks;
    }

    @Override
    public void setAfterChildTasksScheduledBeforeCompletedAction(Action0 afterChildTasksScheduledBeforeCompletedAction)
    {
        this.afterChildTasksScheduledBeforeCompletedAction = afterChildTasksScheduledBeforeCompletedAction;
    }

    @Override
    public void await()
    {
        if (!isCompleted())
        {
            if (getAsyncRunner() == null)
            {
                final Indexable<AsyncTask> parentTasks = getParentTasks();
                for (int i = 0; i < parentTasks.getCount(); ++i)
                {
                    final AsyncTask parentTask = parentTasks.get(i);
                    parentTask.await();
                }
            }
            getAsyncRunner().await(this);
        }
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
        return completed;
    }

    @Override
    public Throwable getIncomingError()
    {
        return incomingError;
    }

    public void setIncomingError(Throwable incomingError)
    {
        this.incomingError = incomingError;
    }

    @Override
    public Throwable getOutgoingError()
    {
        return outgoingError;
    }

    void setOutgoingError(Throwable outgoingError)
    {
        this.outgoingError = outgoingError;
    }

    @Override
    public void schedule()
    {
        getAsyncRunner().schedule(this);
    }

    public AsyncAction then(Action0 action)
    {
        return action == null ? null : thenOnInner(asyncRunner, action);
    }

    public <T> AsyncFunction<T> then(Function0<T> function)
    {
        return function == null ? null : thenOnInner(asyncRunner, function);
    }

    public AsyncAction thenOn(AsyncRunner runner, Action0 action)
    {
        return runner == null || action == null ? null : thenOnInner(new Value<AsyncRunner>(runner), action);
    }

    public <T> AsyncFunction<T> thenOn(AsyncRunner runner, Function0<T> function)
    {
        return runner == null || function == null ? null : thenOnInner(new Value<AsyncRunner>(runner), function);
    }

    private BasicAsyncAction thenOnInner(Getable<AsyncRunner> runner, Action0 action)
    {
        return scheduleOrEnqueue(new BasicAsyncAction(runner, Array.fromValues(new AsyncTask[] { this }), action));
    }

    private <T> BasicAsyncFunction<T> thenOnInner(Getable<AsyncRunner> runner, Function0<T> function)
    {
        return scheduleOrEnqueue(new BasicAsyncFunction<T>(runner, Array.fromValues(new AsyncTask[] { this }), function));
    }

    public AsyncAction thenAsyncAction(Function0<AsyncAction> function)
    {
        return function == null ? null : thenOnAsyncActionInner(asyncRunner, function);
    }

    public <T> AsyncFunction<T> thenAsyncFunction(Function0<AsyncFunction<T>> function)
    {
        return function == null ? null : thenOnAsyncFunctionInner(asyncRunner, function);
    }

    public AsyncAction thenAsyncActionOn(AsyncRunner runner, Function0<AsyncAction> function)
    {
        return runner == null || function == null ? null : thenOnAsyncActionInner(new Value<>(runner), function);
    }

    private AsyncAction thenOnAsyncActionInner(final Getable<AsyncRunner> runner, Function0<AsyncAction> function)
    {
        final Value<AsyncRunner> resultAsyncRunner = new Value<>();
        final List<AsyncTask> resultParentTasks = SingleLinkList.fromValues(new AsyncTask[] { this });
        final BasicAsyncAction result = new BasicAsyncAction(resultAsyncRunner, resultParentTasks, null);

        resultParentTasks.add(this.thenOnInner(runner, function)
            .then(new Action1<AsyncAction>()
            {
                @Override
                public void run(final AsyncAction asyncFunctionResult)
                {
                    resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());
                    resultParentTasks.add(asyncFunctionResult
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
                        }));
                }
            })
            .catchError(new Action1<Throwable>()
            {
                @Override
                public void run(Throwable error)
                {
                    result.setOutgoingError(error);
                    resultAsyncRunner.set(runner.get());
                    result.schedule();
                }
            }));

        return result;
    }

    public <T> AsyncFunction<T> thenAsyncFunctionOn(AsyncRunner runner, Function0<AsyncFunction<T>> function)
    {
        return runner == null || function == null ? null : thenOnAsyncFunctionInner(new Value<>(runner), function);
    }

    private <T> AsyncFunction<T> thenOnAsyncFunctionInner(final Getable<AsyncRunner> runner, Function0<AsyncFunction<T>> function)
    {
        final Value<AsyncRunner> resultAsyncRunner = new Value<>();
        final List<AsyncTask> resultParentTasks = new SingleLinkList<>();
        final Value<T> asyncFunctionResultValue = new Value<>();
        final BasicAsyncFunction<T> result = new BasicAsyncFunction<T>(resultAsyncRunner, resultParentTasks, new Function0<T>()
        {
            @Override
            public T run()
            {
                return asyncFunctionResultValue.get();
            }
        });

        resultParentTasks.add(this.thenOnInner(runner, function)
            .then(new Action1<AsyncFunction<T>>()
            {
                @Override
                public void run(final AsyncFunction<T> asyncFunctionResult)
                {
                    resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());

                    resultParentTasks.add(asyncFunctionResult
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
                        }));
                }
            })
            .catchError(new Action1<Throwable>()
            {
                @Override
                public void run(Throwable error)
                {
                    result.setOutgoingError(error);
                    resultAsyncRunner.set(runner.get());
                    result.schedule();
                }
            }));

        return result;
    }

    protected <T> AsyncFunction<T> catchErrorOnInner(Getable<AsyncRunner> asyncRunner, Function1<Throwable,T> function)
    {
        return scheduleOrEnqueue(new BasicAsyncFunctionErrorHandler<T>(asyncRunner, Array.fromValues(new AsyncTask[] { this }), function));
    }

    protected AsyncAction catchErrorAsyncActionOnInner(final Getable<AsyncRunner> asyncRunner, Function1<Throwable, AsyncAction> function)
    {
        final Value<AsyncRunner> resultAsyncRunner = new Value<>();
        final List<AsyncTask> resultParentTasks = new SingleLinkList<>();
        final BasicAsyncAction result = new BasicAsyncAction(resultAsyncRunner, resultParentTasks, null);

        resultParentTasks.add(this.catchErrorOnInner(asyncRunner, function)
            .then(new Action1<AsyncAction>()
            {
                @Override
                public void run(final AsyncAction asyncFunctionResult)
                {
                    if (asyncFunctionResult == null)
                    {
                        resultAsyncRunner.set(asyncRunner.get());
                        result.schedule();
                    }
                    else
                    {
                        resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());
                        result.setIncomingError(asyncFunctionResult.getOutgoingError());
                        resultParentTasks.add(asyncFunctionResult.then(new Action0()
                        {
                            @Override
                            public void run()
                            {
                                result.schedule();
                            }
                        }));
                    }
                }
            }));

        return result;
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

        if (afterChildTasksScheduledBeforeCompletedAction != null)
        {
            afterChildTasksScheduledBeforeCompletedAction.run();
        }

        completed = true;
    }

    protected abstract void runTask();
}
