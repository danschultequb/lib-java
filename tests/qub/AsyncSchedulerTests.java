package qub;

public interface AsyncSchedulerTests
{
    static void test(TestRunner runner, Function0<AsyncScheduler> creator)
    {
        runner.testGroup(AsyncScheduler.class, () ->
        {
            runner.testGroup("schedule(Action0)", () ->
            {
                runner.test("with null  action", (Test test) ->
                {
                    CurrentThread.withAsyncScheduler(creator, (AsyncScheduler asyncScheduler) ->
                    {
                        test.assertThrows(() -> asyncScheduler.schedule((Action0)null),
                            new PreConditionFailure("action cannot be null."));
                    });
                });

                runner.test("with non-null action", (Test test) ->
                {
                    CurrentThread.withAsyncScheduler(creator, (AsyncScheduler asyncScheduler) ->
                    {
                        final Value<Integer> value = Value.create();

                        final Result<Void> result = asyncScheduler.schedule(() -> { value.set(10); });
                        test.assertNotNull(result);

                        test.assertNull(result.await());
                        test.assertEqual(10, value.get());

                        test.assertNull(result.await());
                        test.assertEqual(10, value.get());
                    });
                });
            });

            runner.testGroup("schedule(Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    CurrentThread.withAsyncScheduler(creator, (AsyncScheduler asyncScheduler) ->
                    {
                        test.assertThrows(() -> asyncScheduler.schedule((Function0<Integer>)null),
                            new PreConditionFailure("function cannot be null."));
                    });
                });

                runner.test("with non-null function", (Test test) ->
                {
                    CurrentThread.withAsyncScheduler(creator, (AsyncScheduler asyncScheduler) ->
                    {
                        final Value<Integer> value = Value.create();
                        final Result<Integer> result = asyncScheduler.schedule(() ->
                        {
                            value.set(10);
                            return 5;
                        });
                        test.assertNotNull(result);

                        test.assertEqual(5, result.await());
                        test.assertEqual(10, value.get());
                    });
                });
            });

            runner.testGroup("scheduleResult(Function0<Result<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    CurrentThread.withAsyncScheduler(creator, (AsyncScheduler asyncScheduler) ->
                    {
                        test.assertThrows(() -> asyncScheduler.scheduleResult((Function0<Result<Integer>>)null),
                            new PreConditionFailure("function cannot be null."));
                    });
                });

                runner.test("with non-null function", (Test test) ->
                {
                    CurrentThread.withAsyncScheduler(creator, (AsyncScheduler asyncScheduler) ->
                    {
                        final Value<Integer> value = Value.create();
                        final Result<Integer> result = asyncScheduler.scheduleResult(() ->
                        {
                            value.set(10);
                            return Result.success(5);
                        });
                        test.assertNotNull(result);

                        test.assertEqual(5, result.await());
                        test.assertEqual(10, value.get());
                    });
                });
            });
        });
    }
}
