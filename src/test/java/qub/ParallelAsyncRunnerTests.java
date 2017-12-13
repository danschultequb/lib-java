package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParallelAsyncRunnerTests extends AsyncRunnerTests
{
    @Override
    protected ParallelAsyncRunner create()
    {
        return new ParallelAsyncRunner(new Synchronization());
    }

    @Test
    public void constructor()
    {
        final ParallelAsyncRunner runner = new ParallelAsyncRunner(new Synchronization());
        assertEquals(0, runner.getScheduledTaskCount());
    }
}
