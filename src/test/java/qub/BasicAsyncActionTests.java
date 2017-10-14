package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class BasicAsyncActionTests extends BasicAsyncTaskTests
{
    @Override
    protected BasicAsyncAction create(AsyncRunnerInner runner)
    {
        return new BasicAsyncAction(runner, TestUtils.emptyAction0);
    }

    @Test
    public void constructor()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncAction basicAsyncAction = new BasicAsyncAction(runner, TestUtils.emptyAction0);
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(0, basicAsyncAction.getPausedTaskCount());
        assertFalse(basicAsyncAction.isCompleted());
    }
}
