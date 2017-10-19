package qub;

public abstract class BasicAsyncTask implements AsyncAction, PausedAsyncTask
{
    private AsyncRunner runner;
    private final List<BasicAsyncTask> pausedTasks;
    private boolean completed;

    BasicAsyncTask(AsyncRunner runner)
    {
        this.runner = runner;
        this.pausedTasks = new SingleLinkList<>();
        this.completed = false;
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

    /**
     * Get the number of PausedAsyncTasks that are waiting for this AsyncTask to complete.
     * @return The number of PausedAsyncTasks that are waiting for this AsyncTask to complete.
     */
    public int getPausedTaskCount()
    {
        return pausedTasks.getCount();
    }

    /**
     * Get whether or not this BasicAsyncTask has been run.
     * @return Whether or not this BasicAsyncTask has been run.
     */
    public boolean isCompleted()
    {
        return completed;
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
        return scheduleOrEnqueue(new BasicAsyncAction(runner, action));
    }

    private <T> BasicAsyncFunction<T> thenOnInner(AsyncRunner runner, Function0<T> function)
    {
        return scheduleOrEnqueue(new BasicAsyncFunction<T>(runner, function));
    }

    @Override
    public AsyncAction thenAsyncAction(Function0<AsyncAction> function)
    {
        return function == null ? null : thenOnAsyncAction(runner, function);
    }

    @Override
    public <T> AsyncFunction<T> thenAsyncFunction(Function0<AsyncFunction<T>> function)
    {
        return function == null ? null : thenOnAsyncFunction(runner, function);
    }

    @Override
    public AsyncAction thenOnAsyncAction(AsyncRunner runner, Function0<AsyncAction> function)
    {
        final BasicAsyncAction result = runner == null || function == null ? null : new BasicAsyncAction(null, null);

        if (result != null)
        {
            thenOn(runner, function)
                    .then(new Action1<AsyncAction>()
                    {
                        @Override
                        public void run(final AsyncAction asyncFunctionResult)
                        {
                            final AsyncRunner asyncFunctionResultRunner = asyncFunctionResult.getRunner();
                            result.setRunner(asyncFunctionResultRunner);
                            asyncFunctionResult.then(new Action0()
                            {
                                @Override
                                public void run()
                                {
                                    result.schedule();
                                }
                            });
                        }
                    });
        }

        return result;
    }

    @Override
    public <T> AsyncFunction<T> thenOnAsyncFunction(AsyncRunner runner, Function0<AsyncFunction<T>> function)
    {
        final Value<T> asyncFunctionResultValue = new Value<>();
        final BasicAsyncFunction<T> result = (runner == null || function == null) ? null : new BasicAsyncFunction<T>(null, new Function0<T>()
        {
            @Override
            public T run()
            {
                return asyncFunctionResultValue.get();
            }
        });

        if (result != null)
        {
            thenOn(runner, function)
                    .then(new Action1<AsyncFunction<T>>()
                    {
                        @Override
                        public void run(final AsyncFunction<T> asyncFunctionResult)
                        {
                            result.setRunner(asyncFunctionResult.getRunner());

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
                    });
        }

        return result;
    }

    private <T extends BasicAsyncTask> T scheduleOrEnqueue(T asyncTask)
    {
        if (completed)
        {
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
        runTask();

        completed = true;

        while (pausedTasks.any())
        {
            final BasicAsyncTask pausedTask = pausedTasks.removeFirst();
            pausedTask.schedule();
        }
    }

    protected abstract void runTask();
}
