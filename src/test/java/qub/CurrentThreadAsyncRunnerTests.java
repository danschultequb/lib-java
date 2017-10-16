package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class CurrentThreadAsyncRunnerTests extends AsyncRunnerTests
{
    @Override
    protected CurrentThreadAsyncRunner create()
    {
        return new CurrentThreadAsyncRunner();
    }

    @Test
    public void constructor()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        assertEquals(0, runner.getScheduledTaskCount());
    }

    @Test
    public void withRegistered()
    {
        final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        try
        {
            CurrentThreadAsyncRunner.withRegistered(new Action1<CurrentThreadAsyncRunner>()
            {
                @Override
                public void run(CurrentThreadAsyncRunner runner)
                {
                    assertNotNull(runner);
                    assertSame(runner, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                }
            });
        }
        finally
        {
            AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
        }
    }
}
