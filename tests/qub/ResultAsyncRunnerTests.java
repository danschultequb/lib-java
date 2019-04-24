package qub;

public interface ResultAsyncRunnerTests
{
    static void test(TestRunner runner, Function0<ResultAsyncRunner> creator)
    {
        runner.testGroup(ResultAsyncRunner.class, () ->
        {
            runner.testGroup("run(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();
                    test.assertThrows(() -> asyncRunner.run((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();
                    final Value<Integer> value = Value.create();

                    final Result<Void> result = asyncRunner.run(() -> value.set(10));
                    test.assertNotNull(result);

                    test.assertNull(result.await());
                    test.assertEqual(10, value.get());

                    test.assertNull(result.await());
                    test.assertEqual(10, value.get());
                });
            });

            runner.testGroup("run(Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();
                    test.assertThrows(() -> asyncRunner.run((Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();

                    final Value<Integer> value = Value.create();
                    final Result<Integer> result = asyncRunner.run(() ->
                    {
                        value.set(10);
                        return 5;
                    });
                    test.assertNull(result);
                    test.assertFalse(value.hasValue());
                });
            });

            runner.testGroup("runResult(Function0<Result<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();
                    test.assertThrows(() -> asyncRunner.runResult((Function0<Result<Integer>>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final ResultAsyncRunner asyncRunner = creator.run();

                    final Value<Integer> value = Value.create();
                    final Result<Integer> result = asyncRunner.runResult(() ->
                    {
                        value.set(10);
                        return Result.success(5);
                    });
                    test.assertNull(result);
                    test.assertFalse(value.hasValue());
                });
            });
        });
    }
}
