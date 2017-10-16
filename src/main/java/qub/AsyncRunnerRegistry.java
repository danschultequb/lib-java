package qub;

public class AsyncRunnerRegistry
{
    private static Map<Long,AsyncRunner> asyncRunners = new LockedListMap<>();

    private AsyncRunnerRegistry()
    {
    }

    private static long getCurrentThreadId()
    {
        return java.lang.Thread.currentThread().getId();
    }

    public static AsyncRunner getCurrentThreadAsyncRunner()
    {
        final long currentThreadId = getCurrentThreadId();
        return getThreadAsyncRunner(currentThreadId);
    }

    public static AsyncRunner getThreadAsyncRunner(long threadId)
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

    public static boolean removeCurrentThreadAsyncRunner()
    {
        final long currentThreadId = getCurrentThreadId();
        return removeThreadAsyncRunner(currentThreadId);
    }

    public static boolean removeThreadAsyncRunner(long threadId)
    {
        return asyncRunners.remove(threadId);
    }
}
