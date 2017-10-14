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
}
