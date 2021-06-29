package qub;

public interface ParallelAsyncRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ParallelAsyncRunner.class, () ->
        {
            AsyncSchedulerTests.test(runner, ParallelAsyncRunner::create);

            runner.test("constructor()", (Test test) ->
            {
                final ParallelAsyncRunner asyncRunner = ParallelAsyncRunner.create();
                test.assertNotNull(asyncRunner);
            });

            runner.test("schedule(Action0)", (Test test) ->
            {
                CurrentThread.withParallelAsyncScheduler((ParallelAsyncRunner asyncRunner) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final AsyncTask<Void> result = asyncRunner.schedule(() -> { value.increment(); });

                    test.assertNull(result.await());
                    test.assertEqual(1, value.get());

                    test.assertNull(result.await());
                    test.assertEqual(1, value.get());
                });
            });

            runner.testGroup("await(Result<?>)", () ->
            {
                runner.test("await task that was scheduled for the ParallelAsyncRunner in ParallelAsyncRunner's thread", (Test test) ->
                {
                    CurrentThread.withParallelAsyncScheduler((ParallelAsyncRunner asyncRunner) ->
                    {
                        final long mainThreadId = CurrentThread.getId();
                        final Value<Long> asyncTaskThreadId = Value.create();

                        final Result<Void> asyncTask = asyncRunner.schedule(() -> { asyncTaskThreadId.set(CurrentThread.getId()); });
                        test.assertFalse(asyncTask.isCompleted());
                        test.assertFalse(asyncTaskThreadId.hasValue());

                        final Value<Long> asyncTaskThreadId2 = Value.create();
                        final Result<Void> asyncTask2 = asyncRunner.schedule(() ->
                        {
                            asyncTaskThreadId2.set(CurrentThread.getId());
                            asyncTask.await();
                        });
                        asyncRunner.await(asyncTask2);

                        test.assertTrue(asyncTask.isCompleted());
                        test.assertNotEqual(mainThreadId, asyncTaskThreadId.get());
                        test.assertNotEqual(mainThreadId, asyncTaskThreadId2.get());
                        test.assertNotEqual(asyncTaskThreadId.get(), asyncTaskThreadId2.get());
                    });
                });

                runner.test("await task that was scheduled for the ParallelAsyncRunner in ManualAsyncRunner's thread", (Test test) ->
                {
                    CurrentThread.withParallelAsyncScheduler((ParallelAsyncRunner asyncRunner) ->
                    {
                        final long mainThreadId = CurrentThread.getId();
                        final Value<Long> asyncTaskThreadId = Value.create();

                        final Result<Void> asyncTask = asyncRunner.schedule(() -> { asyncTaskThreadId.set(CurrentThread.getId()); });
                        test.assertFalse(asyncTask.isCompleted());
                        test.assertFalse(asyncTaskThreadId.hasValue());

                        asyncRunner.await(asyncTask);

                        test.assertTrue(asyncTask.isCompleted());
                        test.assertNotEqual(mainThreadId, asyncTaskThreadId.get());
                    });
                });
            });
        });
    }
}
