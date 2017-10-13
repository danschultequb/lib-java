package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class CurrentThreadAsyncRunnerTests
{
    @Test
    public void constructor()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        assertEquals(0, runner.getScheduledTaskCount());
    }

    @Test
    public void scheduleAction0WithNull()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final AsyncAction asyncAction = runner.schedule(TestUtils.nullAction0);
        assertNull(asyncAction);
        assertEquals(0, runner.getScheduledTaskCount());
    }

    @Test
    public void scheduleAction0WithNonNull()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final Value<Integer> value = new Value<>(0);
        final AsyncAction asyncAction = runner.schedule(TestUtils.setValueAction0(value, 1));
        assertNotNull(asyncAction);
        assertEquals(1, runner.getScheduledTaskCount());
        assertEquals(0, value.get().intValue());
    }

    @Test
    public void scheduleFunction0WithNull()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final AsyncFunction<Integer> asyncFunction = runner.schedule(TestUtils.nullFunction0);
        assertNull(asyncFunction);
        assertEquals(0, runner.getScheduledTaskCount());
    }

    @Test
    public void scheduleFunction0WithNonNull()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final Value<Integer> value = new Value<>(0);
        final AsyncFunction<Integer> asyncFunction = runner.schedule(TestUtils.setValueFunction0(value, 1));
        assertNotNull(asyncFunction);
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void await()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        runner.await();
    }

    @Test
    public void awaitWithAction0()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final Value<Integer> value = new Value<>(0);
        runner.schedule(TestUtils.setValueAction0(value, 1));
        runner.await();
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(1, value.get().intValue());
    }

    @Test
    public void awaitWithAction0AndAction0()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
        final Value<Integer> value1 = new Value<>(0);
        runner.schedule(TestUtils.setValueAction0(value1, 1));

        final Value<Integer> value2 = new Value<>(0);
        runner.schedule(TestUtils.setValueAction0(value2, 2));

        runner.await();
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(1, value1.get().intValue());
        assertEquals(2, value2.get().intValue());
    }

    @Test
    public void awaitWithAction0ThenAction0()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();

        final Value<Integer> value = new Value<>(0);
        runner.schedule(TestUtils.emptyAction0)
            .then(TestUtils.setValueAction0(value, 1));

        assertEquals(1, runner.getScheduledTaskCount());
        runner.await();
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(1, value.get().intValue());
    }

    @Test
    public void awaitWithAction0ThenAction0ThenAction0()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();

        final Value<Integer> value = new Value<>(0);
        runner.schedule(TestUtils.emptyAction0)
            .then(TestUtils.emptyAction0)
            .then(TestUtils.setValueAction0(value, 1));

        assertEquals(1, runner.getScheduledTaskCount());
        runner.await();
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(1, value.get().intValue());
    }

    @Test
    public void awaitWithFunction0ThenAction1()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
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
    public void awaitWithFunction0ThenFunction1()
    {
        final CurrentThreadAsyncRunner runner = new CurrentThreadAsyncRunner();
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
}
