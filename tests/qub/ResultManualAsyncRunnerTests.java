package qub;

public interface ResultManualAsyncRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ResultManualAsyncRunner.class, () ->
        {
            ResultAsyncRunnerTests.test(runner, ResultManualAsyncRunner::new);

            runner.test("constructor()", (Test test) ->
            {
                final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                test.assertNotNull(asyncRunner);
                test.assertEqual(Iterable.create(), asyncRunner.getScheduledTasks());
            });

            runner.test("schedule(Action0)", (Test test) ->
            {
                final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                final IntegerValue value = IntegerValue.create(0);
                final ResultAsyncTask<Void> result = asyncRunner.schedule(() -> { value.increment(); });
                test.assertEqual(Iterable.create(result), asyncRunner.getScheduledTasks());

                test.assertNull(result.await());
                test.assertEqual(1, value.get());
                test.assertEqual(Iterable.create(), asyncRunner.getScheduledTasks());

                test.assertNull(result.await());
                test.assertEqual(1, value.get());
                test.assertEqual(Iterable.create(), asyncRunner.getScheduledTasks());
            });
        });
    }
}
