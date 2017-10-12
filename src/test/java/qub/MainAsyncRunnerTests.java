package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainAsyncRunnerTests
{
    @Test
    public void constructor()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        assertEquals(0, runner.getScheduledCount());
    }

    @Test
    public void scheduleAction0WithNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncAction asyncAction = runner.schedule(nullAction0);
        assertNull(asyncAction);
        assertEquals(0, runner.getScheduledCount());
    }

    @Test
    public void scheduleAction0WithNonNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final Value<Integer> value = new Value<>(0);
        final AsyncAction asyncAction = runner.schedule(setValueAction0(value, 1));
        assertNotNull(asyncAction);
        assertEquals(1, runner.getScheduledCount());
        assertEquals(0, value.get().intValue());
    }

    @Test
    public void scheduleFunction0WithNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(nullFunction0);
        assertNull(asyncFunction);
        assertEquals(0, runner.getScheduledCount());
    }

    @Test
    public void scheduleFunction0WithNonNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final Value<Integer> value = new Value<>(0);
        final AsyncFunction<Integer> asyncFunction = runner.schedule(setValueFunction0(value, 1));
        assertNotNull(asyncFunction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncActionThenAction0WithNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncAction asyncAction = runner.schedule(emptyAction0);
        final AsyncAction asyncAction2 = asyncAction.then(nullAction0);
        assertNull(asyncAction2);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncActionThenAction0WithNonNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncAction asyncAction = runner.schedule(emptyAction0);
        final AsyncAction asyncAction2 = asyncAction.then(emptyAction0);
        assertNotNull(asyncAction2);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncActionThenFunction0WithNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncAction asyncAction = runner.schedule(emptyAction0);
        final AsyncFunction<Integer> asyncFunction = asyncAction.then(nullFunction0);
        assertNull(asyncFunction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncActionThenFunction0WithNonNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncAction asyncAction = runner.schedule(emptyAction0);
        final AsyncFunction<Integer> asyncFunction = asyncAction.then(emptyFunction0);
        assertNotNull(asyncFunction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncFunctionThenAction0WithNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(emptyFunction0);
        final AsyncAction asyncAction = asyncFunction.then(nullAction0);
        assertNull(asyncAction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncFunctionThenAction0WithNonNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(emptyFunction0);
        final AsyncAction asyncAction = asyncFunction.then(emptyAction0);
        assertNotNull(asyncAction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncFunctionThenAction1WithNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(emptyFunction0);
        final AsyncAction asyncAction = asyncFunction.then(nullAction1);
        assertNull(asyncAction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncFunctionThenAction1WithNonNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(emptyFunction0);
        final AsyncAction asyncAction = asyncFunction.then(emptyAction1);
        assertNotNull(asyncAction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncFunctionThenFunction0WithNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(emptyFunction0);
        final AsyncAction asyncAction = asyncFunction.then(nullFunction0);
        assertNull(asyncAction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncFunctionThenFunction0WithNonNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(emptyFunction0);
        final AsyncAction asyncAction = asyncFunction.then(emptyFunction0);
        assertNotNull(asyncAction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncFunctionThenFunction1WithNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(emptyFunction0);
        final AsyncAction asyncAction = asyncFunction.then(nullFunction1);
        assertNull(asyncAction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void asyncFunctionThenFunction1WithNonNull()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(emptyFunction0);
        final AsyncAction asyncAction = asyncFunction.then(emptyFunction1);
        assertNotNull(asyncAction);
        assertEquals(1, runner.getScheduledCount());
    }

    @Test
    public void awaitWithNoScheduledTasks()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        runner.await();
    }

    @Test
    public void awaitWithOneScheduledActions()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final Value<Integer> value = new Value<>(0);
        runner.schedule(setValueAction0(value, 1));
        runner.await();
        assertEquals(0, runner.getScheduledCount());
        assertEquals(1, value.get().intValue());
    }

    @Test
    public void awaitWithTwoScheduledActions()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final Value<Integer> value1 = new Value<>(0);
        runner.schedule(setValueAction0(value1, 1));

        final Value<Integer> value2 = new Value<>(0);
        runner.schedule(setValueAction0(value2, 2));

        runner.await();
        assertEquals(0, runner.getScheduledCount());
        assertEquals(1, value1.get().intValue());
        assertEquals(2, value2.get().intValue());
    }

    @Test
    public void awaitWithOneScheduledActionAndOnePausedAction()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();

        final Value<Integer> value = new Value<>(0);
        runner.schedule(emptyAction0)
            .then(setValueAction0(value, 1));

        assertEquals(1, runner.getScheduledCount());
        runner.await();
        assertEquals(0, runner.getScheduledCount());
        assertEquals(1, value.get().intValue());
    }

    @Test
    public void awaitWithOneScheduledActionAndOnePausedActionWithOnePausedAction()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();

        final Value<Integer> value = new Value<>(0);
        runner.schedule(emptyAction0)
            .then(emptyAction0)
            .then(setValueAction0(value, 1));

        assertEquals(1, runner.getScheduledCount());
        runner.await();
        assertEquals(0, runner.getScheduledCount());
        assertEquals(1, value.get().intValue());
    }

    @Test
    public void awaitWithOneScheduledFunctionWithOneAction1()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final Value<Integer> functionReturnValue = new Value<>();
        final Value<Integer> actionArgument = new Value<>();

        runner.schedule(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                functionReturnValue.set(1);
                return functionReturnValue.get();
            }
        })
        .then(new Action1<Integer>()
        {
            @Override
            public void run(Integer arg1)
            {
                actionArgument.set(arg1);
            }
        });

        runner.await();
        assertEquals(1, functionReturnValue.get().intValue());
        assertEquals(1, actionArgument.get().intValue());
    }

    @Test
    public void awaitWithOneScheduledFunctionWithOneFunction1()
    {
        final MainAsyncRunner runner = new MainAsyncRunner();
        final Value<Integer> firstFunctionReturn = new Value<>();
        final Value<Integer> secondFunctionArgument = new Value<>();
        final Value<Integer> secondFunctionReturnValue = new Value<>();

        runner.schedule(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                firstFunctionReturn.set(1);
                return firstFunctionReturn.get();
            }
        })
        .then(new Function1<Integer,Integer>()
        {
            @Override
            public Integer run(Integer arg1)
            {
                secondFunctionArgument.set(arg1);
                secondFunctionReturnValue.set(arg1 + 1);
                return secondFunctionReturnValue.get();
            }
        });

        runner.await();
        assertEquals(1, firstFunctionReturn.get().intValue());
        assertEquals(1, secondFunctionArgument.get().intValue());
        assertEquals(2, secondFunctionReturnValue.get().intValue());
    }

    private static final Action0 nullAction0 = null;

    private static final Action1<Integer> nullAction1 = null;

    private static final Function0<Integer> nullFunction0 = null;

    private static final Function1<Integer,Integer> nullFunction1 = null;

    private static final Action0 emptyAction0 = new Action0()
    {
        @Override
        public void run()
        {
        }
    };

    private static final Action1<Integer> emptyAction1 = new Action1<Integer>()
    {
        @Override
        public void run(Integer arg1)
        {
        }
    };

    private static final Function0<Integer> emptyFunction0 = emptyFunction0(0);

    private static Function0<Integer> emptyFunction0(final int returnValue)
    {
        return new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return returnValue;
            }
        };
    }

    private static final Function1<Integer,Integer> emptyFunction1 = emptyFunction1(0);

    private static Function1<Integer,Integer> emptyFunction1(final int returnValue)
    {
        return new Function1<Integer, Integer>()
        {
            @Override
            public Integer run(Integer arg1)
            {
                return returnValue;
            }
        };
    }

    private static Action0 setValueAction0(final Value<Integer> value, final int valueToSet)
    {
        return new Action0()
        {
            @Override
            public void run()
            {
                value.set(valueToSet);
            }
        };
    }

    private static Function0<Integer> setValueFunction0(Value<Integer> value, int valueToSet)
    {
        return setValueFunction0(value, valueToSet, valueToSet);
    }

    private static Function0<Integer> setValueFunction0(final Value<Integer> value, final int valueToSet, final int valueToReturn)
    {
        return new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                value.set(valueToSet);
                return valueToReturn;
            }
        };
    }
}
