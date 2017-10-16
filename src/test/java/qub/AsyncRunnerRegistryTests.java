package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class AsyncRunnerRegistryTests
{
    @Test
    public void constructor()
    {
        assertNotNull(new AsyncRunnerRegistry());
    }

    @Test
    public void getCurrentThreadAsyncRunnerWithNoRegisteredRunner()
    {
        assertNull(AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
    }

    @Test
    public void getCurrentThreadAsyncRunnerWithRegisteredRunner()
    {
        final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        AsyncRunnerRegistry.setCurrentThreadAsyncRunner(runner);
        try
        {
            assertSame(runner, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
        }
        finally
        {
            AsyncRunnerRegistry.removeCurrentThreadAsyncRunner();
            if (backupRunner != null)
            {
                AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
            }
        }
    }
}
