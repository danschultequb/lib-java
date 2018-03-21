package qub;

public class BasicAsyncFunctionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BasicAsyncFunction.class, () ->
        {
            BasicAsyncTaskTests.test(runner, BasicAsyncFunctionTests::create);
            
            runner.test("constructor()", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getMainRunner();
                final BasicAsyncFunction<Integer> basicAsyncFunction = new BasicAsyncFunction<>(asyncRunner, new Synchronization(), () -> 0);
                test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                test.assertFalse(basicAsyncFunction.isCompleted());
            });
            
            runner.testGroup("then(Action1<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    final AsyncAction thenAsyncAction = basicAsyncFunction.then((Action1<Integer>)null);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    final AsyncAction thenAsyncAction = basicAsyncFunction.then(() -> 0);
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    asyncRunner.await();

                    final AsyncAction thenAsyncAction = basicAsyncFunction.then(() -> 0);
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                });
            });

            runner.testGroup("then(Function1<T,U>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then((Function1<Integer,Integer>)null);
                    test.assertNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then((Integer value) -> value);
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    asyncRunner.await();

                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then((Integer value) -> value);
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenAsyncAction(Function1<T,AsyncAction>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    test.assertNull(basicAsyncFunction.thenAsyncAction((Function1<Integer,AsyncAction>)null));
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncAction asyncAction = basicAsyncFunction.thenAsyncAction((Integer value) -> asyncRunner.schedule(() -> value));
                    test.assertNotNull(asyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertTrue(basicAsyncFunction.isCompleted());
                    test.assertTrue(asyncAction.isCompleted());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    asyncRunner.await();
                    test.assertTrue(basicAsyncFunction.isCompleted());
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());

                    final AsyncAction asyncAction = basicAsyncFunction.thenAsyncAction((Integer value) -> asyncRunner.schedule(() -> value));
                    test.assertNotNull(asyncAction);
                    test.assertFalse(asyncAction.isCompleted());
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                });

                runner.test("with second thenAsyncAction()", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncAction asyncAction1 = basicAsyncFunction.thenAsyncAction((Integer value) -> asyncRunner.schedule(() -> {}));
                    test.assertNotNull(asyncAction1);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncAction asyncAction2 = asyncAction1.thenAsyncAction(() -> asyncRunner.schedule(() -> {}));
                    test.assertNotNull(asyncAction2);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenAsyncFunction(Function1<T,AsyncFunction<U>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncFunction<Integer> basicAsyncFunction = create(test);
                    test.assertNull(basicAsyncFunction.thenAsyncFunction((Function1<Integer,AsyncFunction<Boolean>>)null));
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncFunction<Boolean> asyncFunction = basicAsyncFunction.thenAsyncFunction((Integer value) -> asyncRunner.schedule(() -> true));
                    test.assertNotNull(asyncFunction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertTrue(basicAsyncFunction.isCompleted());
                    test.assertTrue(asyncFunction.isCompleted());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    asyncRunner.await();
                    test.assertTrue(basicAsyncFunction.isCompleted());
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());

                    final AsyncFunction<Boolean> asyncFunction = basicAsyncFunction.thenAsyncFunction((Integer value) -> asyncRunner.schedule(() -> true));
                    test.assertNotNull(asyncFunction);
                    test.assertFalse(asyncFunction.isCompleted());
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                });

                runner.test("with second thenAsyncFunction()", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncFunction<Boolean> asyncFunction1 = basicAsyncFunction.thenAsyncFunction((Integer value) -> asyncRunner.schedule(() -> true));
                    test.assertNotNull(asyncFunction1);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    final AsyncFunction<String> asyncFunction2 = asyncFunction1.thenAsyncFunction((Boolean value) -> asyncRunner.schedule(() -> "abc"));
                    test.assertNotNull(asyncFunction2);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Action1<T>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = null;
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> {});
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());

                    asyncRunner1.await();
                });

                runner.test("with null Action1", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner(asyncRunner1.getSynchronization());
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Action1<Integer>)null);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner(asyncRunner1.getSynchronization());
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> {});
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);
                    asyncRunner1.await();
                    test.assertTrue(basicAsyncFunction.isCompleted());

                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner(asyncRunner1.getSynchronization());
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> {});
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Function1<T,U>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = null;
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> value);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());

                    asyncRunner1.await();
                });

                runner.test("with null Action1", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner(asyncRunner1.getSynchronization());
                    final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(asyncRunner2, (Function1<Integer,Integer>)null);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner(asyncRunner1.getSynchronization());
                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> value);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);
                    asyncRunner1.await();

                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner(asyncRunner1.getSynchronization());
                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(asyncRunner2, (Integer value) -> value);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });
            });

            runner.testGroup("thenOn(AsyncRunner)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertNull(basicAsyncFunction.thenOn(null));
                });

                runner.test("with same AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner);
                    test.assertSame(basicAsyncFunction, basicAsyncFunction.thenOn(asyncRunner));
                    test.assertEqual(0, basicAsyncFunction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                });

                runner.test("with different AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainRunner();
                    final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(asyncRunner1);

                    final AsyncRunner asyncRunner2 = new ManualAsyncRunner(asyncRunner1.getSynchronization());
                    final AsyncFunction<Integer> thenAsyncAction = basicAsyncFunction.thenOn(asyncRunner2);
                    test.assertNotNull(thenAsyncAction);
                    test.assertNotSame(basicAsyncFunction, thenAsyncAction);
                    test.assertEqual(1, basicAsyncFunction.getPausedTaskCount());

                    asyncRunner1.await();
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                });
            });

            runner.test("awaitReturn()", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getParallelRunner();
                final AsyncFunction<Integer> asyncFunction = asyncRunner.schedule(() -> 20);
                test.assertEqual(20, asyncFunction.awaitReturn());
                test.assertTrue(asyncFunction.isCompleted());

                test.assertEqual(20, asyncFunction.awaitReturn());
            });
        });
    }
    
    private static BasicAsyncFunction<Integer> create(AsyncRunner runner)
    {
        return new BasicAsyncFunction<>(runner, new Synchronization(), () -> 0);
    }

    private static BasicAsyncFunction<Integer> create(Test test)
    {
        return create(test.getMainRunner());
    }

    private static BasicAsyncFunction<Integer> createScheduled(AsyncRunner runner)
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create(runner);
        basicAsyncFunction.schedule();
        return basicAsyncFunction;
    }
}
