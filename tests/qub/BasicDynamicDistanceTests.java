package qub;

public interface BasicDynamicDistanceTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(BasicDynamicDistance.class, () ->
        {
            DynamicDistanceTests.test(runner, (FakeDesktopProcess process) ->
            {
                final AWTUIBase uiBase = AWTUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
                final SwingUIBuilder uiBuilder = SwingUIBuilder.create(uiBase);
                final UIButton uiButton = uiBuilder.createUIButton().await();
                return BasicDynamicDistance.create(uiButton::getWidth, uiButton::onSizeChanged);
            });

            runner.testGroup("create(Function0<Distance>,Function1<Action0,Disposable>)", () ->
            {
                runner.test("with null getter function", (Test test) ->
                {
                    test.assertThrows(() -> BasicDynamicDistance.create(null, (Action0 callback) -> null),
                        new PreConditionFailure("valueGetter cannot be null."));
                });

                runner.test("with null subscription function", (Test test) ->
                {
                    test.assertThrows(() -> BasicDynamicDistance.create(() -> Distance.zero, null),
                        new PreConditionFailure("valueChangedSubscriptionFunction cannot be null."));
                });

                runner.test("with valid values", (Test test) ->
                {
                    final Value<Distance> distance = Value.create(Distance.zero);
                    final RunnableEvent0 distanceChanged = Event0.create();

                    final BasicDynamicDistance dynamicDistance = BasicDynamicDistance.create(distance::get, distanceChanged::subscribe);
                    test.assertNotNull(dynamicDistance);
                    test.assertFalse(dynamicDistance.isDisposed());

                    test.assertEqual(Distance.zero, dynamicDistance.get());

                    final IntegerValue counter = IntegerValue.create(0);
                    dynamicDistance.onChanged(counter::increment);
                    test.assertEqual(0, counter.get());

                    distanceChanged.run();
                    test.assertEqual(Distance.zero, dynamicDistance.get());
                    test.assertEqual(0, counter.get());

                    distance.set(Distance.inches(1));
                    test.assertEqual(Distance.zero, dynamicDistance.get());
                    test.assertEqual(0, counter.get());

                    distanceChanged.run();
                    test.assertEqual(Distance.inches(1), dynamicDistance.get());
                    test.assertEqual(1, counter.get());

                    distanceChanged.run();
                    test.assertEqual(Distance.inches(1), dynamicDistance.get());
                    test.assertEqual(1, counter.get());

                    test.assertTrue(dynamicDistance.dispose().await());
                    test.assertTrue(dynamicDistance.isDisposed());

                    test.assertThrows(dynamicDistance::get,
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                });
            });
        });
    }
}
