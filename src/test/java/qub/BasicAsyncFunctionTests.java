package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class BasicAsyncFunctionTests extends BasicAsyncTaskTests
{
    @Override
    protected BasicAsyncFunction<Integer> create(AsyncRunnerInner runner)
    {
        return new BasicAsyncFunction<>(runner, TestUtils.emptyFunction0);
    }

    private BasicAsyncFunction<Integer> create()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        return create(runner);
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
}
