package qub;

public abstract class BasicAsyncTask implements AsyncAction, PausedAsyncTask
{
    private final AsyncRunner runner;
    private final Queue<PausedAsyncTask> pausedTasks;
    private boolean completed;

    BasicAsyncTask(AsyncRunner runner)
    {
        this.runner = runner;
        this.pausedTasks = new SingleLinkListQueue<>();
        this.completed = false;
    }

    @Override
    public AsyncRunner getRunner() {
        return runner;
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
        scheduleOn(runner);
    }

    @Override
    public void scheduleOn(AsyncRunner runner)
    {
        runner.schedule(this);
    }

    @Override
    public AsyncAction then(Action0 action)
    {
        return thenOn(runner, action);
    }

    @Override
    public <T> AsyncFunction<T> then(Function0<T> function)
    {
        return thenOn(runner, function);
    }

    @Override
    public AsyncAction thenAsync(Function0<AsyncAction> function)
    {
        return thenOnAsync(runner, function);
    }

    @Override
    public AsyncAction thenOn(AsyncRunner runner, Action0 action)
    {
        AsyncAction result = null;
        if (runner != null && action != null)
        {
            final PausedAsyncAction asyncAction = runner.create(action);
            if (completed)
            {
                asyncAction.schedule();
            }
            else
            {
                pausedTasks.enqueue(asyncAction);
            }
            result = asyncAction;
        }
        return result;
    }

    @Override
    public <T> AsyncFunction<T> thenOn(AsyncRunner runner, Function0<T> function)
    {
        AsyncFunction<T> result = null;
        if (function != null)
        {
            final PausedAsyncFunction<T> asyncFunction = runner.create(function);
            if (completed)
            {
                asyncFunction.schedule();
            }
            else
            {
                pausedTasks.enqueue(asyncFunction);
            }
            result = asyncFunction;
        }
        return result;
    }

    @Override
    public AsyncAction thenOnAsync(AsyncRunner runner, Function0<AsyncAction> function)
    {
        final boolean validArguments = (runner != null && function != null);

        final PausedAsyncAction result = !validArguments ? null : this.runner.create(new Action0()
        {
            @Override
            public void run()
            {
            }
        });

        if (validArguments)
        {
            thenOn(runner, function)
                    .then(new Action1<AsyncAction>()
                    {
                        @Override
                        public void run(final AsyncAction asyncFunctionResult)
                        {
                            asyncFunctionResult.then(new Action0()
                            {
                                @Override
                                public void run()
                                {
                                    result.scheduleOn(asyncFunctionResult.getRunner());
                                }
                            });
                        }
                    });
        }

        return result;
    }

    @Override
    public void runAndSchedulePausedTasks()
    {
        runTask();

        completed = true;

        while (pausedTasks.any())
        {
            final PausedAsyncTask pausedTask = pausedTasks.dequeue();
            pausedTask.schedule();
        }
    }

    protected abstract void runTask();
}
