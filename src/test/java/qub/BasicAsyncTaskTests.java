package qub;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class BasicAsyncTaskTests
{
    protected abstract BasicAsyncTask create(AsyncRunner runner);

    private CurrentThreadAsyncRunner createCurrentThreadAsyncRunner()
    {
        final Synchronization synchronization = new Synchronization();
        return new CurrentThreadAsyncRunner(new Function0<Synchronization>()
        {
            @Override
            public Synchronization run()
            {
                return synchronization;
            }
        });
    }

    private BasicAsyncTask create()
    {
        return create(createCurrentThreadAsyncRunner());
    }

    private BasicAsyncTask createScheduled(AsyncRunner runner)
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
    public void thenAction0AfterCompleted()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner);
        runner.await();
        assertTrue(basicAsyncTask.isCompleted());

        final AsyncAction thenAsyncAction = basicAsyncTask.then(TestUtils.emptyAction0);

        assertNotNull(thenAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenAsyncActionWithNull()
    {
        final BasicAsyncTask basicAsyncTask = create();
        final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(null);
        assertNull(thenAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
    }

    @Test
    public void thenAsyncActionWithNonNull()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner);

        final Value<Integer> value = new Value<>();
        final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(new Function0<AsyncAction>()
        {
            @Override
            public AsyncAction run()
            {
                return runner.schedule(TestUtils.setValueAction0(value, 5));
            }
        });
        assertNotNull(thenAsyncAction);
        assertEquals(1, basicAsyncTask.getPausedTaskCount());

        runner.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertTrue(basicAsyncTask.isCompleted());
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(5, value.get().intValue());
    }

    @Test
    public void thenAsyncActionWithNonNullAfterCompleted()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner);
        runner.await();

        final Value<Integer> value = new Value<>();
        final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(new Function0<AsyncAction>()
        {
            @Override
            public AsyncAction run()
            {
                return runner.schedule(TestUtils.setValueAction0(value, 5));
            }
        });
        assertNotNull(thenAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());

        runner.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertTrue(basicAsyncTask.isCompleted());
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(5, value.get().intValue());
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
    public void thenFunction0AfterCompleted()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner);
        runner.await();
        assertTrue(basicAsyncTask.isCompleted());

        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(TestUtils.emptyFunction0);

        assertNotNull(thenAsyncFunction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenAsyncFunctionWithNull()
    {
        final BasicAsyncTask basicAsyncTask = create();
        final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncFunction(null);
        assertNull(thenAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
    }

    @Test
    public void thenAsyncFunctionWithNonNull()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner);

        final Value<Integer> value = new Value<>();
        final AsyncAction thenAsyncAction = basicAsyncTask
                .thenAsyncFunction(new Function0<AsyncFunction<Integer>>()
                {
                    @Override
                    public AsyncFunction<Integer> run()
                    {
                        return runner.schedule(TestUtils.setValueFunction0(value, 5, 6));
                    }
                })
                .then(new Action1<Integer>()
                {
                    @Override
                    public void run(Integer asyncFunctionReturnValue)
                    {
                        assertEquals(6, asyncFunctionReturnValue.intValue());
                    }
                });
        assertNotNull(thenAsyncAction);
        assertEquals(1, basicAsyncTask.getPausedTaskCount());

        runner.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertTrue(basicAsyncTask.isCompleted());
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(5, value.get().intValue());
    }

    @Test
    public void thenAsyncFunctionWithNonNullAfterCompleted()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner);
        runner.await();

        final Value<Integer> value = new Value<>();
        final AsyncAction thenAsyncAction = basicAsyncTask
                .thenAsyncFunction(new Function0<AsyncFunction<Integer>>()
                {
                    @Override
                    public AsyncFunction<Integer> run()
                    {
                        return runner.schedule(TestUtils.setValueFunction0(value, 5, 6));
                    }
                })
                .then(new Action1<Integer>()
                {
                    @Override
                    public void run(Integer asyncFunctionReturnValue)
                    {
                        assertEquals(6, asyncFunctionReturnValue.intValue());
                    }
                });
        assertNotNull(thenAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());

        runner.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertTrue(basicAsyncTask.isCompleted());
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(5, value.get().intValue());
    }

    @Test
    public void thenOnAction0()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
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
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner1);
        runner1.await();

        final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, TestUtils.emptyAction0);
        assertNotNull(thenOnAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnAsyncActionWithNullRunner()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = create();

        final Value<Integer> value = new Value<>();
        final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(null, new Function0<AsyncAction>()
        {
            @Override
            public AsyncAction run()
            {
                return runner.schedule(TestUtils.setValueAction0(value, 4));
            }
        });
        assertNull(thenOnAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
    }

    @Test
    public void thenOnAsyncActionWithNullFunction()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = create();

        final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(runner, null);
        assertNull(thenOnAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
    }

    @Test
    public void thenOnAsyncActionWithNonNull()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner1);

        final Value<Integer> value = new Value<>();
        final AsyncAction thenOnAsyncAction = basicAsyncTask
                .thenOnAsyncAction(runner2, new Function0<AsyncAction>()
                {
                    @Override
                    public AsyncAction run()
                    {
                        return runner3.schedule(TestUtils.setValueAction0(value, 4));
                    }
                })
                .then(new Action0()
                {
                    @Override
                    public void run()
                    {
                        assertEquals(4, value.get().intValue());
                    }
                });
        assertNotNull(thenOnAsyncAction);
        assertEquals(1, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner1.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner2.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(1, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner3.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertEquals(4, value.get().intValue());
    }

    @Test
    public void thenOnAsyncActionWithNonNullAfterCompleted()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner1);
        runner1.await();

        final Value<Integer> value = new Value<>();
        final AsyncAction thenOnAsyncAction = basicAsyncTask
                .thenOnAsyncAction(runner2, new Function0<AsyncAction>()
                {
                    @Override
                    public AsyncAction run()
                    {
                        return runner3.schedule(TestUtils.setValueAction0(value, 4));
                    }
                })
                .then(new Action0()
                {
                    @Override
                    public void run()
                    {
                        assertEquals(4, value.get().intValue());
                    }
                });
        assertNotNull(thenOnAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner2.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(1, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner3.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertEquals(4, value.get().intValue());
    }

    @Test
    public void thenOnFunction0()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner1);

        final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, TestUtils.emptyFunction0);
        assertNotNull(thenOnAsyncFunction);
        assertEquals(1, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());

        runner1.await();
        assertEquals(1, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnFunction0AfterCompleted()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner1);
        runner1.await();

        final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, TestUtils.emptyFunction0);
        assertNotNull(thenOnAsyncFunction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnAsyncFunctionWithNullFunction()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = create();

        final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(runner, null);
        assertNull(thenOnAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
    }

    @Test
    public void thenOnAsyncFunctionWithNonNull()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner1);

        final Value<Integer> value = new Value<>();
        final AsyncAction thenOnAsyncAction = basicAsyncTask
                .thenOnAsyncFunction(runner2, new Function0<AsyncFunction<Integer>>()
                {
                    @Override
                    public AsyncFunction<Integer> run()
                    {
                        return runner3.schedule(TestUtils.setValueFunction0(value, 4, 5));
                    }
                })
                .then(new Action1<Integer>()
                {
                    @Override
                    public void run(Integer asyncFunctionReturnValue)
                    {
                        assertEquals(5, asyncFunctionReturnValue.intValue());
                    }
                });
        assertNotNull(thenOnAsyncAction);
        assertEquals(1, basicAsyncTask.getPausedTaskCount());
        assertEquals(1, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner1.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner2.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(1, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner3.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertEquals(4, value.get().intValue());
    }

    @Test
    public void thenOnAsyncFunctionWithNonNullAfterCompleted()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
        final BasicAsyncTask basicAsyncTask = createScheduled(runner1);
        runner1.await();

        final Value<Integer> value = new Value<>();
        final AsyncAction thenOnAsyncAction = basicAsyncTask
                .thenOnAsyncFunction(runner2, new Function0<AsyncFunction<Integer>>()
                {
                    @Override
                    public AsyncFunction<Integer> run()
                    {
                        return runner3.schedule(TestUtils.setValueFunction0(value, 4, 5));
                    }
                })
                .then(new Action1<Integer>()
                {
                    @Override
                    public void run(Integer asyncFunctionReturnValue)
                    {
                        assertEquals(5, asyncFunctionReturnValue.intValue());
                    }
                });
        assertNotNull(thenOnAsyncAction);
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner2.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(1, runner3.getScheduledTaskCount());
        assertFalse(value.hasValue());

        runner3.await();
        assertEquals(0, basicAsyncTask.getPausedTaskCount());
        assertEquals(0, runner1.getScheduledTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());
        assertEquals(0, runner3.getScheduledTaskCount());
        assertEquals(4, value.get().intValue());
    }
}
