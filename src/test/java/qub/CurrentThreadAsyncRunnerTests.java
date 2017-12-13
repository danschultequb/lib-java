package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class CurrentThreadAsyncRunnerTests extends AsyncRunnerTests
{
    @Override
    protected CurrentThreadAsyncRunner create()
    {
        return new CurrentThreadAsyncRunner(new Synchronization());
    }

    @Test
    public void constructor()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner(new Synchronization());
        assertEquals(0, runner.getScheduledTaskCount());
    }

    @Test
    public void withRegistered()
    {
        final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        try
        {
            final Synchronization synchronization = new Synchronization();
            CurrentThreadAsyncRunner.withRegistered(synchronization, new Action1<CurrentThreadAsyncRunner>()
            {
                @Override
                public void run(CurrentThreadAsyncRunner runner)
                {
                    assertNotNull(runner);
                    assertSame(synchronization, runner.getSynchronization());
                    assertSame(runner, AsyncRunnerRegistry.getCurrentThreadAsyncRunner());
                }
            });
        }
        finally
        {
            AsyncRunnerRegistry.setCurrentThreadAsyncRunner(backupRunner);
        }
    }

    @Test
    public void withRegisteredWithConsole()
    {
        final AsyncRunner backupRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        try
        {
            final Console console = new Console();
            CurrentThreadAsyncRunner.withRegistered(console, new Action1<CurrentThreadAsyncRunner>()
            {
                @Override
                public void run(CurrentThreadAsyncRunner runner)
                {
                    assertNotNull(runner);
                    assertSame(console.getSynchronization(), runner.getSynchronization());
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
