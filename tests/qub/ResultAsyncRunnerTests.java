package qub;

public interface ResultAsyncRunnerTests
{
    static void test(TestRunner runner, Function0<ResultAsyncRunner> creator)
    {
        runner.testGroup(ResultAsyncRunner.class, () ->
        {
            runner.testGroup("schedule(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();
                    test.assertThrows(() -> asyncRunner.schedule((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();
                    final Value<Integer> value = Value.create();

                    final Result<Void> result = asyncRunner.schedule(() -> value.set(10));
                    test.assertNotNull(result);

                    test.assertNull(result.await());
                    test.assertEqual(10, value.get());

                    test.assertNull(result.await());
                    test.assertEqual(10, value.get());
                });
            });

            runner.testGroup("schedule(Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();
                    test.assertThrows(() -> asyncRunner.schedule((Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();

                    final Value<Integer> value = Value.create();
                    final Result<Integer> result = asyncRunner.schedule(() ->
                    {
                        value.set(10);
                        return 5;
                    });
                    test.assertNotNull(result);

                    test.assertEqual(5, result.await());
                    test.assertEqual(10, value.get());
                });
            });

            runner.testGroup("scheduleResult(Function0<Result<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();
                    test.assertThrows(() -> asyncRunner.scheduleResult((Function0<Result<Integer>>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();

                    final Value<Integer> value = Value.create();
                    final Result<Integer> result = asyncRunner.scheduleResult(() ->
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
    }
}
