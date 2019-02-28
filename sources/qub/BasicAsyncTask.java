package qub;

public abstract class BasicAsyncTask implements PausedAsyncTask
{
    private final Mutex mutex;
    private final Getable<AsyncRunner> asyncRunner;
    private final Locked<List<AsyncTask>> parentTasks;
    private final Locked<List<BasicAsyncTask>> pausedTasks;
    private final Locked<Value<Boolean>> completed;
    private final String label;
    private volatile Throwable incomingError;
    private volatile Throwable outgoingError;

    BasicAsyncTask(Getable<AsyncRunner> asyncRunner, String label)
    {
        this.asyncRunner = asyncRunner;
        this.mutex = new SpinMutex();
        this.parentTasks = new Locked<>(List.create(), mutex);
        this.pausedTasks = new Locked<>(List.create(), mutex);
        this.completed = new Locked<>(Value.create(false), mutex);
        this.label = label;
    }

    protected void markCompleted()
    {
        completed.get((Value<Boolean> value) -> value.set(true));
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner.hasValue() ? asyncRunner.get() : null;
    }

    protected Getable<AsyncRunner> getAsyncRunnerGetable()
    {
        return asyncRunner;
    }

    @Override
    public int getParentTaskCount()
    {
        return parentTasks.get((List<AsyncTask> values) -> values.getCount());
    }

    @Override
    public AsyncTask getParentTask(int index)
    {
        return parentTasks.get((List<AsyncTask> values) -> values.get(index));
    }

    @Override
    public void addParentTask(AsyncTask parentTask)
    {
        parentTasks.get((List<AsyncTask> values) -> values.add(parentTask));
    }

    @Override
    public boolean parentTasksContain(final AsyncTask parentTask)
    {
        return parentTasks.get((List<AsyncTask> values) -> values.contains(parentTask));
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
        return pausedTasks.get((List<BasicAsyncTask> values) -> values.getCount());
    }

    @Override
    public boolean isCompleted()
    {
        return completed.get((Value<Boolean> value) -> value.hasValue() && value.get());
    }

    @Override
    public Throwable getIncomingError()
    {
        return incomingError;
    }

    public void setIncomingError(Throwable incomingError)
    {
        PreCondition.assertNotNull(incomingError, "incomingError");
        PreCondition.assertNull(getIncomingError(), "getIncomingError()");

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
        PreCondition.assertNotNull(action, "action");

        return thenOnInner(getAsyncRunnerGetable(), action);
    }

    public <T> AsyncFunction<T> then(Function0<T> function)
    {
        PreCondition.assertNotNull(function, "function");

        return thenOnInner(getAsyncRunnerGetable(), function);
    }

    public AsyncAction thenOn(AsyncRunner runner, Action0 action)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(action, "action");

        return thenOnInner(Value.create(runner), action);
    }

    public <T> AsyncFunction<T> thenOn(AsyncRunner runner, Function0<T> function)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(function, "function");

        return thenOnInner(Value.create(runner), function);
    }

    private BasicAsyncAction thenOnInner(Getable<AsyncRunner> runner, Action0 action)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(action, "action");

        final BasicAsyncAction asyncAction = new BasicAsyncAction(runner, action);
        asyncAction.addParentTask(this);
        return scheduleOrEnqueue(asyncAction);
    }

    private <T> BasicAsyncFunction<T> thenOnInner(Getable<AsyncRunner> runner, Function0<T> function)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(function, "function");

        final BasicAsyncFunction<T> asyncFunction = new BasicAsyncFunction<T>(runner, function);
        asyncFunction.addParentTask(this);
        return scheduleOrEnqueue(asyncFunction);
    }

    public AsyncAction thenAsyncAction(Function0<AsyncAction> function)
    {
        PreCondition.assertNotNull(function, "function");

        return thenOnAsyncActionInner(getAsyncRunnerGetable(), function);
    }

    public <T> AsyncFunction<T> thenAsyncFunction(Function0<AsyncFunction<T>> function)
    {
        PreCondition.assertNotNull(function, "function");

        return thenOnAsyncFunctionInner(getAsyncRunnerGetable(), function);
    }

    public AsyncAction thenAsyncActionOn(AsyncRunner runner, Function0<AsyncAction> function)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(function, "function");

        return thenOnAsyncActionInner(Value.create(runner), function);
    }

    private AsyncAction thenOnAsyncActionInner(Getable<AsyncRunner> runner, Function0<AsyncAction> function)
    {
        final Value<AsyncRunner> resultAsyncRunner = Value.create();
        final BasicAsyncAction result = new BasicAsyncAction(resultAsyncRunner, null);
        result.addParentTask(this);

        result.addParentTask(this.thenOnInner(runner, function)
            .then((AsyncAction asyncFunctionResult) ->
            {
                resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());
                result.addParentTask(asyncFunctionResult
                    .catchError(result::setOutgoingError)
                    .then(result::schedule));
            })
            .catchError((Throwable error) ->
            {
                result.setOutgoingError(error);
                resultAsyncRunner.set(runner.hasValue() ? runner.get() : null);
                result.schedule();
            }));

        return result;
    }

    public <T> AsyncFunction<T> thenAsyncFunctionOn(AsyncRunner runner, Function0<AsyncFunction<T>> function)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(function, "function");

        return thenOnAsyncFunctionInner(Value.create(runner), function);
    }

    private <T> AsyncFunction<T> thenOnAsyncFunctionInner(Getable<AsyncRunner> runner, Function0<AsyncFunction<T>> function)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(function, "function");

        final Value<AsyncRunner> resultAsyncRunner = Value.create();
        final Value<T> asyncFunctionResultValue = Value.create();
        final BasicAsyncFunction<T> result = new BasicAsyncFunction<>(resultAsyncRunner, () -> asyncFunctionResultValue.hasValue() ? asyncFunctionResultValue.get() : null);
        result.addParentTask(this);

        result.addParentTask(this.thenOnInner(runner, function)
            .then((AsyncFunction<T> asyncFunctionResult) ->
            {
                resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());
                result.addParentTask(asyncFunctionResult
                    .then(asyncFunctionResultValue::set)
                    .catchError(result::setOutgoingError)
                    .then(result::schedule));
            })
            .catchError((Throwable error) ->
            {
                result.setOutgoingError(error);
                resultAsyncRunner.set(runner.hasValue() ? runner.get() : null);
                result.schedule();
            }));

        return result;
    }

    protected <T> AsyncFunction<T> catchErrorOnInner(Getable<AsyncRunner> asyncRunner, Function1<Throwable,T> function)
    {
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertNotNull(function, "function");

        final BasicAsyncFunctionErrorHandler<T> asyncFunction = new BasicAsyncFunctionErrorHandler<>(asyncRunner, function);
        asyncFunction.addParentTask(this);
        return scheduleOrEnqueue(asyncFunction);
    }

    protected AsyncAction catchErrorAsyncActionOnInner(final Getable<AsyncRunner> asyncRunner, Function1<Throwable, AsyncAction> function)
    {
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");
        PreCondition.assertNotNull(function, "function");

        final Value<AsyncRunner> resultAsyncRunner = Value.create();
        final BasicAsyncAction result = new BasicAsyncAction(resultAsyncRunner, Action0.empty);
        result.addParentTask(this);

        result.addParentTask(this.catchErrorOnInner(asyncRunner, function)
            .then((AsyncAction asyncFunctionResult) ->
            {
                if (asyncFunctionResult == null)
                {
                    resultAsyncRunner.set(asyncRunner.hasValue() ? asyncRunner.get() : null);
                    result.schedule();
                }
                else
                {
                    resultAsyncRunner.set(asyncFunctionResult.getAsyncRunner());
                    if (asyncFunctionResult.getOutgoingError() != null)
                    {
                        result.setIncomingError(asyncFunctionResult.getOutgoingError());
                    }
                    result.addParentTask(asyncFunctionResult.then(result::schedule));
                }
            }));

        return result;
    }

    protected <T extends BasicAsyncTask> T scheduleOrEnqueue(T asyncTask)
    {
        return mutex.criticalSection(() ->
        {
            if (isCompleted())
            {
                final Throwable outgoingError = getOutgoingError();
                if (outgoingError != null)
                {
                    asyncTask.setIncomingError(outgoingError);
                }
                asyncTask.schedule();
            }
            else
            {
                pausedTasks.get((List<BasicAsyncTask> values) -> values.add(asyncTask));
            }
            return asyncTask;
        });
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
        final AsyncRunner asyncRunner = getAsyncRunner();

        mutex.criticalSection(() ->
        {
            while (pausedTasks.get((List<BasicAsyncTask> values) -> values.any()))
            {
                final BasicAsyncTask pausedTask = pausedTasks.get((List<BasicAsyncTask> values) -> values.removeFirst());
                if (outgoingError != null)
                {
                    pausedTask.setIncomingError(outgoingError);
                }
                pausedTask.schedule();
            }
            completed.get(asyncRunner::markCompleted);
        });
    }

    protected abstract void runTask();

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        if (!Strings.isNullOrEmpty(label))
        {
            builder.append(label);
            builder.append(' ');
        }
        builder.append(super.toString());
        final String result = builder.toString();

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }
}
