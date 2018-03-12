package qub;

public class BasicAsyncActionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BasicAsyncAction.class, () ->
        {
            BasicAsyncTaskTests.test(runner, BasicAsyncActionTests::create);

            runner.test("constructor()", (Test test) ->
            {
                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                final BasicAsyncAction basicAsyncAction = new BasicAsyncAction(runner1, () -> {});
                test.assertEqual(0, runner1.getScheduledTaskCount());
                test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                test.assertFalse(basicAsyncAction.isCompleted());
            });

            runner.testGroup("thenOn()", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = create(runner1);
                    test.assertNull(basicAsyncAction.thenOn(null));
                });

                runner.test("with same AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(runner1);
                    test.assertSame(basicAsyncAction, basicAsyncAction.thenOn(runner1));
                    test.assertEqual(0, basicAsyncAction.getPausedTaskCount());
                    test.assertEqual(1, runner1.getScheduledTaskCount());
                });

                runner.test("with different AsyncRunner", (Test test) ->
                {
                    final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final BasicAsyncAction basicAsyncAction = createScheduled(runner1);

                    final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncAction thenAsyncAction = basicAsyncAction.thenOn(runner2);
                    test.assertNotNull(thenAsyncAction);
                    test.assertNotSame(basicAsyncAction, thenAsyncAction);
                    test.assertEqual(1, basicAsyncAction.getPausedTaskCount());

                    runner1.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());

                    runner2.await();
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                });
            });
        });
    }

    private static BasicAsyncAction create(AsyncRunner asyncRunner)
    {
        return new BasicAsyncAction(asyncRunner, () -> {});
    }

    private static CurrentThreadAsyncRunner createCurrentThreadAsyncRunner()
    {
        final Synchronization synchronization = new Synchronization();
        return new CurrentThreadAsyncRunner(() -> synchronization);
    }

    private static BasicAsyncAction createScheduled(AsyncRunner runner)
    {
        final BasicAsyncAction basicAsyncAction = create(runner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }
}
