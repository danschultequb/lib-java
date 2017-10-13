package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class BasicAsyncFunctionTests
{
    @Test
    public void constructor()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = new BasicAsyncFunction<>(runner, TestUtils.emptyFunction0);
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
    }
    
    private static BasicAsyncFunction<Integer> create()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        return new BasicAsyncFunction<>(runner, TestUtils.emptyFunction0);
    }
    
    @Test
    public void thenAction0WithNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.nullAction0);
        assertNull(thenAsyncAction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenAction0WithNonNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.emptyAction0);
        assertNotNull(thenAsyncAction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());
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
    public void thenFunction0WithNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncAction asyncAction = basicAsyncFunction.then(TestUtils.nullFunction0);
        assertNull(asyncAction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenFunction0WithNonNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.emptyFunction0);
        assertNotNull(thenAsyncFunction);
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
