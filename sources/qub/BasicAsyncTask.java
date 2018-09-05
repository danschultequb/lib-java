package qub;

public abstract class BasicAsyncTask implements PausedAsyncTask
{
    private final Getable<AsyncRunner> asyncRunner;
    private final List<AsyncTask> parentTasks;
    private final List<BasicAsyncTask> pausedTasks;
    private final Value<Boolean> completed;
    private final Mutex mutex;
    private volatile Throwable incomingError;
    private volatile Throwable outgoingError;

    BasicAsyncTask(Getable<AsyncRunner> asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        this.parentTasks = new SingleLinkList<AsyncTask>();
        this.pausedTasks = LockedList.from(new SingleLinkList<BasicAsyncTask>());
        this.completed = new Value<Boolean>(false);
        this.mutex = new SpinMutex();
    }

    protected void markCompleted()
    {
        completed.set(true);
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
    public int getParentTaskCount()
    {
        return mutex.criticalSection(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return parentTasks.getCount();
            }
        });
    }

    @Override
    public AsyncTask getParentTask(final int index)
    {
        return mutex.criticalSection(new Function0<AsyncTask>()
        {
            @Override
            public AsyncTask run()
            {
                return parentTasks.get(index);
            }
        });
    }

    @Override
    public void addParentTask(final AsyncTask parentTask)
    {
        mutex.criticalSection(new Action0()
        {
            @Override
            public void run()
            {
                parentTasks.add(parentTask);
            }
        });
    }

    @Override
    public boolean parentTasksContain(final AsyncTask parentTask)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return parentTasks.contains(parentTask);
        }
    }

    private void awaitParentTasks()
    {
        for (int i = 0; i < getParentTaskCount(); ++i)
        {
            final AsyncTask parentTask = getParentTask(i);
            try
            {
                parentTask.await();
            }
            catch (AwaitException ignored)
            {
                // It's okay to ignore this AwaitException because the parent task will have set
                // this task's IncomingException. That means that when this task awaits/runs, it
                // will react to the IncomingException.
            }
        }
    }

    @Override
    public void await()
    {
        if (!isCompleted())
        {
            awaitParentTasks();
            getAsyncRunner().await(this);
        }
        if (outgoingError != null)
        {
            throw new AwaitException(outgoingError);
        }
    }

    /**
     * Get the number of PausedAsyncTasks that are waiting for this AsyncTask to complete.
     * @return The number of PausedAsyncTasks that are waiting for this AsyncTask to complete.
     */
    public int getPausedTaskCount()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return pausedTasks.getCount();
        }
    }

    @Override
    public boolean isCompleted()
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            return completed.get();
        }
    }

    @Override
    public Throwable getIncomingError()
    {
        return incomingError;
    }

    public void setIncomingError(Throwable incomingError)
    {
        PreCondition.assertNotNull(incomingError, "incomingError");
        PreCondition.assertNull(this.incomingError, "this.incomingError");

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

    protected void schedule()
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
        final BasicAsyncAction asyncAction = new BasicAsyncAction(runner, action);
        asyncAction.addParentTask(this);
        return scheduleOrEnqueue(asyncAction);
    }

    private <T> BasicAsyncFunction<T> thenOnInner(Getable<AsyncRunner> runner, Function0<T> function)
    {
        final BasicAsyncFunction<T> asyncFunction = new BasicAsyncFunction<T>(runner, function);
        asyncFunction.addParentTask(this);
        return scheduleOrEnqueue(asyncFunction);
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
        final BasicAsyncAction result = new BasicAsyncAction(resultAsyncRunner, null);
        result.addParentTask(this);

        result.addParentTask(this.thenOnInner(runner, function)
            .then(new Action1<AsyncAction>()
            {
                @Override
                public void run(final AsyncAction asyncFunctionResult)
                {
                    resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());
                    result.addParentTask(asyncFunctionResult
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
        final Value<T> asyncFunctionResultValue = new Value<>();
        final BasicAsyncFunction<T> result = new BasicAsyncFunction<T>(resultAsyncRunner, new Function0<T>()
        {
            @Override
            public T run()
            {
                return asyncFunctionResultValue.get();
            }
        });
        result.addParentTask(this);

        result.addParentTask(this.thenOnInner(runner, function)
            .then(new Action1<AsyncFunction<T>>()
            {
                @Override
                public void run(final AsyncFunction<T> asyncFunctionResult)
                {
                    resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());
                    result.addParentTask(asyncFunctionResult
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
        final BasicAsyncFunctionErrorHandler<T> asyncFunction = new BasicAsyncFunctionErrorHandler<>(asyncRunner, function);
        asyncFunction.addParentTask(this);
        return scheduleOrEnqueue(asyncFunction);
    }

    protected AsyncAction catchErrorAsyncActionOnInner(final Getable<AsyncRunner> asyncRunner, Function1<Throwable, AsyncAction> function)
    {
        final Value<AsyncRunner> resultAsyncRunner = new Value<>();
        final BasicAsyncAction result = new BasicAsyncAction(resultAsyncRunner, null);
        result.addParentTask(this);

        result.addParentTask(this.catchErrorOnInner(asyncRunner, function)
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
                        if (asyncFunctionResult.getOutgoingError() != null)
                        {
                            result.setIncomingError(asyncFunctionResult.getOutgoingError());
                        }
                        result.addParentTask(asyncFunctionResult.then(new Action0()
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

    protected <T extends BasicAsyncTask> T scheduleOrEnqueue(final T asyncTask)
    {
        try (final Disposable criticalSection = mutex.criticalSection())
        {
            if (completed.get())
            {
                if (getOutgoingError() != null)
                {
                    asyncTask.setIncomingError(getOutgoingError());
                }
                asyncTask.schedule();
            }
            else
            {
                pausedTasks.add(asyncTask);
            }
            return asyncTask;
        }
    }

    @Override
    public void runAndSchedulePausedTasks()
    {
        awaitParentTasks();

        try
        {
            runTask();
        }
        catch (RuntimeException error)
        {
            setOutgoingError(error);
        }

        final Throwable outgoingError = getOutgoingError();

        try (final Disposable criticalSection = mutex.criticalSection())
        {
            while (pausedTasks.any())
            {
                final BasicAsyncTask pausedTask = pausedTasks.removeFirst();
                if (outgoingError != null)
                {
                    pausedTask.setIncomingError(outgoingError);
                }
                pausedTask.schedule();
            }
            getAsyncRunner().markCompleted(completed);
        }
    }

    protected abstract void runTask();
}
