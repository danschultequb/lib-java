package qub;

public class BasicAsyncActionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BasicAsyncAction.class, () ->
        {
            BasicAsyncTaskTests.test(runner, BasicAsyncActionTests::create);

            runner.test("constructor(AsyncRunner,Action0)", (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getMainRunner();
                final BasicAsyncAction basicAsyncAction = new BasicAsyncAction(asyncRunner, () -> {});
                test.assertSame(asyncRunner, basicAsyncAction.getRunner());
                test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                test.assertFalse(basicAsyncAction.isCompleted());
            });

            runner.testGroup("thenOn(AsyncRunner)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncAction basicAsyncAction = create(asyncRunner);
                    test.assertNull(basicAsyncAction.thenOn(null));
                });

                runner.test("with same AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(asyncRunner);
                    test.assertSame(basicAsyncAction, basicAsyncAction.thenOn(asyncRunner));
                    test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());
                });

                runner.test("with different AsyncRunner", (Test test) ->
                {
                    final AsyncRunner mainRunner = test.getMainRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(mainRunner);

                    final AsyncRunner parallelRunner = test.getParallelRunner();
                    final AsyncAction thenAsyncAction = basicAsyncAction.thenOn(parallelRunner);
                    test.assertNotNull(thenAsyncAction);
                    test.assertNotSame(basicAsyncAction, thenAsyncAction);
                    test.assertEqual(1, basicAsyncAction.getPausedTaskCount());

                    mainRunner.await();
                    test.assertEqual(1, parallelRunner.getScheduledTaskCount());

                    parallelRunner.await();
                    test.assertEqual(0, parallelRunner.getScheduledTaskCount());
                });
            });
        });
    }

    private static BasicAsyncAction create(AsyncRunner asyncRunner)
    {
        return new BasicAsyncAction(asyncRunner, () -> {});
    }

    private static BasicAsyncAction createScheduled(AsyncRunner runner)
    {
        final BasicAsyncAction basicAsyncAction = create(runner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }
}
