package qub;

public interface DynamicDistanceTests
{
    static void test(TestRunner runner, Function1<Test,? extends DynamicDistance> creator)
    {
        runner.testGroup(DynamicDistance.class, () ->
        {
            runner.test("getValue()", (Test test) ->
            {
                try (final DynamicDistance dynamicDistance = creator.run(test))
                {
                    final Distance distance = dynamicDistance.get();
                    test.assertNotNull(distance);
                    test.assertEqual(distance, dynamicDistance.get());
                }
            });

            runner.testGroup("onChanged(Action0)", () ->
            {
                runner.test("with null callback", (Test test) ->
                {
                    try (final DynamicDistance dynamicDistance = creator.run(test))
                    {
                        test.assertThrows(() -> dynamicDistance.onChanged(null),
                            new PreConditionFailure("callback cannot be null."));
                    }
                });

                runner.test("with non-null callback", (Test test) ->
                {
                    try (final DynamicDistance dynamicDistance = creator.run(test))
                    {
                        final Disposable subscription = dynamicDistance.onChanged(Action0.empty);
                        test.assertNotNull(subscription);
                        test.assertFalse(subscription.isDisposed());

                        test.assertTrue(subscription.dispose().await());

                        test.assertTrue(subscription.isDisposed());
                    }
                });
            });
        });
    }
}
