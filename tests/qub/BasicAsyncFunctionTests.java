package qub;

public class BasicAsyncFunctionTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("BasicAsyncFunction<T>", () ->
        {
            BasicAsyncTaskTests.test(runner, BasicAsyncFunctionTests::create);
            
            runner.test("constructor()", (Test test) ->
            {
                final CurrentThreadAsyncRunner runner13 = createCurrentThreadAsyncRunner();
                final BasicAsyncFunction<Integer> basicAsyncFunction = new BasicAsyncFunction<>(runner13, new Synchronization(), TestUtils.emptyFunction0);
                test.assertEqual(0, runner13.getScheduledTaskCount());
                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                test.assertFalse(basicAsyncFunction.isCompleted());
            });
            
            runner.testGroup("then(Action1)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create();
                    final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.nullAction1);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create();
                    final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.emptyAction1);
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner13 = createCurrentThreadAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner13);
                    runner13.await();

                    final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.emptyAction1);
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, runner13.getScheduledTaskCount());
                });
            });

            runner.testGroup("then(Function1)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create();
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.nullFunction1);
                    test.assertNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create();
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.emptyFunction1);
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner13 = createCurrentThreadAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner13);
                    runner13.await();

                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.emptyFunction1);
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, runner13.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Action1)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);

                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyAction1);
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());

                    runner2.await();
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);
                    runner1.await();
                    test.assertTrue(basicAsyncFunction.isCompleted());

                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyAction1);
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());

                    runner2.await();
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Function1)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);

                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyFunction1);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());

                    runner2.await();
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);
                    runner1.await();

                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyFunction1);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());

                    runner2.await();
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOn(AsyncRunner)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner13 = createCurrentThreadAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner13);
                    test.assertNull(basicAsyncFunction.thenOn(null));
                });

                runner.test("with same AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner13 = createCurrentThreadAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner13);
                    test.assertSame(basicAsyncFunction, basicAsyncFunction.thenOn(runner13));
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, runner13.getScheduledTaskCount());
                });

                runner.test("with different AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner13 = createCurrentThreadAsyncRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner13);

                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncFunction<Integer> thenAsyncAction = basicAsyncFunction.thenOn(runner2);
                    test.assertNotNull(thenAsyncAction);
                    test.assertNotSame(basicAsyncFunction, thenAsyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());

                    runner13.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());

                    runner2.await();
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                });
            });

            runner.test("awaitReturn()", (Test test) ->
            {
                try (final ParallelAsyncRunner runner12 = new ParallelAsyncRunner(new Synchronization()))
                {
                    final AsyncFunction<Integer> asyncFunction = runner12.schedule(() -> 20);
                    test.assertEqual(20, asyncFunction.awaitReturn());
                    test.assertTrue(asyncFunction.isCompleted());

                    test.assertEqual(20, asyncFunction.awaitReturn());
                }
            });
        });
    }
    
    private static BasicAsyncFunction<Integer> create(AsyncRunner runner)
    {
        return new BasicAsyncFunction<>(runner, new Synchronization(), TestUtils.emptyFunction0);
    }

    private static CurrentThreadAsyncRunner createCurrentThreadAsyncRunner()
    {
        final Synchronization synchronization = new Synchronization();
        return new CurrentThreadAsyncRunner(() -> synchronization);
    }

    private static BasicAsyncFunction<Integer> create()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        return create(runner);
    }

    private static BasicAsyncFunction<Integer> createScheduled(AsyncRunner runner)
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create(runner);
        basicAsyncFunction.schedule();
        return basicAsyncFunction;
    }
}
