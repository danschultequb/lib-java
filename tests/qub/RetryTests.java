package qub;

public interface RetryTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Retry.class, () ->
        {
            runner.testGroup("run(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Retry.run(null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with an action that doesn't throw", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final Result<Void> result = Retry.run(() -> { value.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(0, value.get());

                    test.assertNull(result.await());
                    test.assertEqual(1, value.get());

                    test.assertNull(result.await());
                    test.assertEqual(1, value.get());
                });
            });
        });
    }
}
