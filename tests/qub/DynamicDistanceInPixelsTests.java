package qub;

public interface DynamicDistanceInPixelsTests
{
    static void test(TestRunner runner, Function0<? extends DynamicDistanceInPixels> creator)
    {
        runner.testGroup(DynamicDistanceInPixels.class, () ->
        {
            runner.test("getValue()", (Test test) ->
            {
                final DynamicDistanceInPixels dynamicDistance = creator.run();
                final int distance = dynamicDistance.getValue();
                test.assertEqual(distance, dynamicDistance.getValue());
            });

            runner.testGroup("onChanged(Action0)", () ->
            {
                runner.test("with null callback", (Test test) ->
                {
                    final DynamicDistanceInPixels dynamicDistance = creator.run();
                    test.assertThrows(() -> dynamicDistance.onChanged(null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with non-null callback", (Test test) ->
                {
                    final DynamicDistanceInPixels dynamicDistance = creator.run();
                    final Disposable subscription = dynamicDistance.onChanged(Action0.empty);
                    test.assertNotNull(subscription);
                    test.assertFalse(subscription.isDisposed());

                    test.assertTrue(subscription.dispose().await());

                    test.assertTrue(subscription.isDisposed());
                });
            });
        });
    }
}
