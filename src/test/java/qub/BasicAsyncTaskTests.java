package qub;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class BasicAsyncTaskTests
{
    protected abstract BasicAsyncTask create(AsyncRunnerInner runner);

    private BasicAsyncTask create()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        return create(runner);
    }

    private BasicAsyncTask createScheduled(AsyncRunnerInner runner)
    {
        final BasicAsyncTask basicAsyncAction = create(runner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }

    @Test
    public void thenAction0WithNull()
    {
        final BasicAsyncTask basicAsyncTask = create();
        final AsyncAction thenAsyncAction = basicAsyncTask.then(TestUtils.nullAction0);
        assertNull(thenAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
    }

    @Test
    public void thenAction0WithNonNull()
    {
        final BasicAsyncTask basicAsyncTask = create();
        final AsyncAction thenAsyncAction = basicAsyncTask.then(TestUtils.emptyAction0);
        assertNotNull(thenAsyncAction);
        assertEquals(1, basicAsyncTask.getPausedTaskCount());
    }

    @Test
    public void thenFunction0WithNull()
    {
        final BasicAsyncTask basicAsyncTask = create();
        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(TestUtils.nullFunction0);
        assertNull(thenAsyncFunction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
    }

    @Test
    public void thenFunction0WithNonNull()
    {
        final BasicAsyncTask basicAsyncTask = create();
        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(TestUtils.emptyFunction0);
        assertNotNull(thenAsyncFunction);
        assertEquals(1, basicAsyncTask.getPausedTaskCount());
    }

    @Test
    public void thenAction0AfterCompleted()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner);
        runner.await();
        assertTrue(basicAsyncTask.isCompleted());

        final AsyncAction thenAsyncAction = basicAsyncTask.then(TestUtils.emptyAction0);

        assertNotNull(thenAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenFunction0AfterCompleted()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner);
        runner.await();
        assertTrue(basicAsyncTask.isCompleted());

        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(TestUtils.emptyFunction0);

        assertNotNull(thenAsyncFunction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenOnAction0()
    {
        final CurrentThreadAsyncRunner runner1 = new CurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = new CurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner1);

        final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, TestUtils.emptyAction0);
        assertNotNull(thenOnAsyncAction);
        assertEquals(1, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());

        runner1.await();
        assertEquals(1, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnAction0AfterCompleted()
    {
        final CurrentThreadAsyncRunner runner1 = new CurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = new CurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner1);
        runner1.await();

        final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, TestUtils.emptyAction0);
        assertNotNull(thenOnAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());
    }
}
