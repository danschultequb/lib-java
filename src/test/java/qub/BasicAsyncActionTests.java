package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class BasicAsyncActionTests
{
    @Test
    public void constructor()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncAction asyncAction = new BasicAsyncAction(runner, TestUtils.emptyAction0);
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(0, asyncAction.getPausedTaskCount());
    }
    
    @Test
    public void thenAction0WithNull()
    {
        final BasicAsyncAction basicAsyncAction = create();
        final AsyncAction asyncAction2 = basicAsyncAction.then(TestUtils.nullAction0);
        assertNull(asyncAction2);
        assertEquals(0, basicAsyncAction.getPausedTaskCount());
    }

    @Test
    public void thenAction0WithNonNull()
    {
        final BasicAsyncAction asyncAction = create();
        final AsyncAction asyncAction2 = asyncAction.then(TestUtils.emptyAction0);
        assertNotNull(asyncAction2);
        assertEquals(1, asyncAction.getPausedTaskCount());
    }

    @Test
    public void thenFunction0WithNull()
    {
        final BasicAsyncAction asyncAction = create();
        final AsyncFunction<Integer> asyncFunction = asyncAction.then(TestUtils.nullFunction0);
        assertNull(asyncFunction);
        assertEquals(0, asyncAction.getPausedTaskCount());
    }

    @Test
    public void thenFunction0WithNonNull()
    {
        final BasicAsyncAction asyncAction = create();
        final AsyncFunction<Integer> asyncFunction = asyncAction.then(TestUtils.emptyFunction0);
        assertNotNull(asyncFunction);
        assertEquals(1, asyncAction.getPausedTaskCount());
    }

    @Test
    public void thenAction0AfterCompleted()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncAction basicAsyncAction = createScheduled(runner);
        runner.await();
        assertTrue(basicAsyncAction.isCompleted());

        final AsyncAction thenAsyncAction = basicAsyncAction.then(TestUtils.emptyAction0);

        assertNotNull(thenAsyncAction);
        assertEquals(0, basicAsyncAction.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenFunction0AfterCompleted()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncAction basicAsyncAction = createScheduled(runner);
        runner.await();
        assertTrue(basicAsyncAction.isCompleted());

        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncAction.then(TestUtils.emptyFunction0);

        assertNotNull(thenAsyncFunction);
        assertEquals(0, basicAsyncAction.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    private static BasicAsyncAction create()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        return create(runner);
    }

    private static BasicAsyncAction create(AsyncRunnerInner runner)
    {
        return new BasicAsyncAction(runner, TestUtils.emptyAction0);
    }

    private static BasicAsyncAction createScheduled(AsyncRunnerInner runner)
    {
        final BasicAsyncAction basicAsyncAction = create(runner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }
}
