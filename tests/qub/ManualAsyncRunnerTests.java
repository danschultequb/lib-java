package qub;

public interface ManualAsyncRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ManualAsyncRunner.class, () ->
        {
            AsyncSchedulerTests.test(runner, ManualAsyncRunner::new);

            runner.test("constructor()", (Test test) ->
            {
                final ManualAsyncRunner asyncRunner = new ManualAsyncRunner();
                test.assertNotNull(asyncRunner);
                test.assertEqual(Iterable.create(), asyncRunner.getScheduledTasks());
                test.assertNotSame(asyncRunner, CurrentThread.getAsyncRunner());
            });

            runner.test("schedule(Action0)", (Test test) ->
            {
                CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final AsyncTask<Void> result = asyncRunner.schedule(() -> { value.increment(); });
                    test.assertEqual(Iterable.create(result), asyncRunner.getScheduledTasks());

                    test.assertNull(result.await());
                    test.assertEqual(1, value.get());
                    test.assertEqual(Iterable.create(), asyncRunner.getScheduledTasks());

                    test.assertNull(result.await());
                    test.assertEqual(1, value.get());
                    test.assertEqual(Iterable.create(), asyncRunner.getScheduledTasks());
                });
            });

            runner.testGroup("await(Result<?>)", () ->
            {
                runner.test("await task that was scheduled for the ManualAsyncRunner in ManualAsyncRunner's thread", (Test test) ->
                {
                   CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                   {
                       final long mainThreadId = CurrentThread.getId();
                       final Value<Long> asyncTaskThreadId = Value.create();

                       final Result<Void> asyncTask = asyncRunner.schedule(() -> { asyncTaskThreadId.set(CurrentThread.getId()); });
                       test.assertFalse(asyncTask.isCompleted());
                       test.assertFalse(asyncTaskThreadId.hasValue());

                       asyncRunner.await(asyncTask);

                       test.assertTrue(asyncTask.isCompleted());
                       test.assertEqual(mainThreadId, asyncTaskThreadId.get());
                       test.assertEqual(Iterable.create(), asyncRunner.getScheduledTasks());
                   });
                });

                runner.test("await task that was scheduled for the ManualAsyncRunner in ParallelAsyncRunner's thread", (Test test) ->
                {
                    CurrentThread.withManualAsyncScheduler((ManualAsyncRunner asyncRunner) ->
                    {
                        final long mainThreadId = CurrentThread.getId();
                        final Value<Long> asyncTaskThreadId = Value.create();

                        final Result<Void> asyncTask = asyncRunner.schedule(() -> { asyncTaskThreadId.set(CurrentThread.getId()); });
                        test.assertFalse(asyncTask.isCompleted());
                        test.assertFalse(asyncTaskThreadId.hasValue());

                        final ParallelAsyncRunner parallelAsyncRunner = new ParallelAsyncRunner();
                        parallelAsyncRunner.schedule(() -> asyncRunner.await(asyncTask)).await();

                        test.assertTrue(asyncTask.isCompleted());
                        test.assertEqual(mainThreadId, asyncTaskThreadId.get());
                        test.assertEqual(Iterable.create(), asyncRunner.getScheduledTasks());
                    });
                });
            });
        });
    }
}
