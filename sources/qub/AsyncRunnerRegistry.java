package qub;

public class AsyncRunnerRegistry
{
    AsyncRunnerRegistry()
    {
    }

    private static final Map<Long,AsyncRunner> asyncRunners = new ConcurrentHashMap<>();

    private static long getCurrentThreadId()
    {
        return java.lang.Thread.currentThread().getId();
    }

    public static AsyncRunner getCurrentThreadAsyncRunner()
    {
        final long currentThreadId = getCurrentThreadId();
        return getThreadAsyncRunner(currentThreadId).throwErrorOrGetValue();
    }

    public static Result<AsyncRunner> getThreadAsyncRunner(long threadId)
    {
        return asyncRunners.get(threadId);
    }

    public static void setCurrentThreadAsyncRunner(AsyncRunner runner)
    {
        final long currentThreadId = getCurrentThreadId();
        setThreadAsyncRunner(currentThreadId, runner);
    }

    public static void setThreadAsyncRunner(long threadId, AsyncRunner runner)
    {
        asyncRunners.set(threadId, runner);
    }

    public static void withCurrentThreadAsyncRunner(AsyncRunner runner, Action0 action)
    {
        final long currentThreadId = getCurrentThreadId();
        withThreadAsyncRunner(currentThreadId, runner, action);
    }

    public static void withThreadAsyncRunner(long threadId, AsyncRunner runner, Action0 action)
    {
        final Result<AsyncRunner> backup = getThreadAsyncRunner(threadId);
        setThreadAsyncRunner(threadId, runner);
        try
        {
            if (action != null)
            {
                action.run();
            }
        }
        finally
        {
            if (backup.hasError())
            {
                removeThreadAsyncRunner(threadId);
            }
            else
            {
                setThreadAsyncRunner(threadId, backup.getValue());
            }
        }
    }

    public static Result<AsyncRunner> removeCurrentThreadAsyncRunner()
    {
        final long currentThreadId = getCurrentThreadId();
        return removeThreadAsyncRunner(currentThreadId);
    }

    public static Result<AsyncRunner> removeThreadAsyncRunner(long threadId)
    {
        return asyncRunners.remove(threadId);
    }
}
