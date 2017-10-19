package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class BasicAsyncFunctionTests extends BasicAsyncTaskTests
{
    @Override
    protected BasicAsyncFunction<Integer> create(AsyncRunner runner)
    {
        return new BasicAsyncFunction<>(runner, TestUtils.emptyFunction0);
    }

    private BasicAsyncFunction<Integer> create()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        return create(runner);
    }

    private BasicAsyncFunction<Integer> createScheduled(AsyncRunner runner)
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create(runner);
        basicAsyncFunction.schedule();
        return basicAsyncFunction;
    }

    @Test
    public void constructor()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = new BasicAsyncFunction<>(runner, TestUtils.emptyFunction0);
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertFalse(basicAsyncFunction.isCompleted());
    }

    @Test
    public void thenAction1WithNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.nullAction1);
        assertNull(thenAsyncAction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenAction1WithNonNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.emptyAction1);
        assertNotNull(thenAsyncAction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenAction1WithNonNullWhenCompleted()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
        runner.await();

        final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.emptyAction1);
        assertNotNull(thenAsyncAction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenFunction1WithNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.nullFunction1);
        assertNull(thenAsyncFunction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenFunction1WithNonNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.emptyFunction1);
        assertNotNull(thenAsyncFunction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenFunction1WithNonNullWhenCompleted()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
        runner.await();

        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.emptyFunction1);
        assertNotNull(thenAsyncFunction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenOnAction1()
    {
        final CurrentThreadAsyncRunner runner1 = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);

        final CurrentThreadAsyncRunner runner2 = new CurrentThreadAsyncRunner();
        final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyAction1);
        assertNotNull(thenOnAsyncAction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());

        runner1.await();
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnAction1WhenCompleted()
    {
        final CurrentThreadAsyncRunner runner1 = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);
        runner1.await();
        assertTrue(basicAsyncFunction.isCompleted());

        final CurrentThreadAsyncRunner runner2 = new CurrentThreadAsyncRunner();
        final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyAction1);
        assertNotNull(thenOnAsyncAction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnFunction1()
    {
        final CurrentThreadAsyncRunner runner1 = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);

        final CurrentThreadAsyncRunner runner2 = new CurrentThreadAsyncRunner();
        final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyFunction1);
        assertNotNull(thenOnAsyncFunction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());

        runner1.await();
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnFunction1WhenCompleted()
    {
        final CurrentThreadAsyncRunner runner1 = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);
        runner1.await();

        final CurrentThreadAsyncRunner runner2 = new CurrentThreadAsyncRunner();
        final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyFunction1);
        assertNotNull(thenOnAsyncFunction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnAsyncRunnerWithNullRunner()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
        assertNull(basicAsyncFunction.thenOn(null));
    }

    @Test
    public void thenOnAsyncRunnerWithSameRunner()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
        assertSame(basicAsyncFunction, basicAsyncFunction.thenOn(runner));
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenOnAsyncRunnerWithDifferentRunner()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);

        final CurrentThreadAsyncRunner runner2 = new CurrentThreadAsyncRunner();
        final AsyncFunction<Integer> thenAsyncAction = basicAsyncFunction.thenOn(runner2);
        assertNotNull(thenAsyncAction);
        assertNotSame(basicAsyncFunction, thenAsyncAction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());

        runner.await();
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }
}
