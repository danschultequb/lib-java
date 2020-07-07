package qub;

public interface DynamicDistanceTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(DynamicDistance.class, () ->
        {
            runner.testGroup("fixed(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> DynamicDistance.fixed(null),
                        new PreConditionFailure("distance cannot be null."));
                });

                final Action1<Distance> fixedTest = (Distance distance) ->
                {
                    runner.test("with " + distance, (Test test) ->
                    {
                        final FixedDynamicDistance fixedDynamicDistance = DynamicDistance.fixed(distance);
                        test.assertNotNull(fixedDynamicDistance);
                        test.assertEqual(distance, fixedDynamicDistance.getValue());
                    });
                };

                fixedTest.run(Distance.inches(-1));
                fixedTest.run(Distance.zero);
                fixedTest.run(Distance.inches(2));
            });
        });
    }

    static void test(TestRunner runner, Function0<? extends DynamicDistance> creator)
    {
        runner.testGroup(DynamicDistance.class, () ->
        {
            runner.test("getValue()", (Test test) ->
            {
                final DynamicDistance dynamicDistance = creator.run();
                final Distance distance = dynamicDistance.getValue();
                test.assertNotNull(distance);
                test.assertEqual(distance, dynamicDistance.getValue());
            });

            runner.testGroup("onChanged(Action0)", () ->
            {
                runner.test("with null callback", (Test test) ->
                {
                    final DynamicDistance dynamicDistance = creator.run();
                    test.assertThrows(() -> dynamicDistance.onChanged(null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with non-null callback", (Test test) ->
                {
                    final DynamicDistance dynamicDistance = creator.run();
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
