package qub;

public interface UIElementTests
{
    static void test(TestRunner runner, Function1<Test,? extends UIElement> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");
        
        runner.testGroup(UIElement.class, () ->
        {
            runner.testGroup("setWidth(Distance)", () ->
            {
                final Action2<Distance,Throwable> setWidthErrorTest = (Distance width, Throwable expected) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        test.assertThrows(() -> uiElement.setWidth(width), expected);
                    });
                };
    
                setWidthErrorTest.run(null, new PreConditionFailure("width cannot be null."));
                setWidthErrorTest.run(Distance.inches(-1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
    
                final Action1<Distance> setWidthTest = (Distance width) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
    
                        final UIElement setWidthResult = uiElement.setWidth(width);
                        test.assertSame(uiElement, setWidthResult);
    
                        test.assertEqual(width, uiElement.getWidth());
                    });
                };
    
                setWidthTest.run(Distance.inches(1));
                setWidthTest.run(Distance.inches(5));
            });
    
            runner.testGroup("setHeight(Distance)", () ->
            {
                final Action2<Distance,Throwable> setHeightErrorTest = (Distance height, Throwable expected) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        test.assertThrows(() -> uiElement.setHeight(height), expected);
                    });
                };
    
                setHeightErrorTest.run(null, new PreConditionFailure("height cannot be null."));
                setHeightErrorTest.run(Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
    
                final Action1<Distance> setHeightTest = (Distance height) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
    
                        final UIElement setHeightResult = uiElement.setHeight(height);
                        test.assertSame(uiElement, setHeightResult);
    
                        test.assertEqual(height, uiElement.getHeight());
                    });
                };
    
                setHeightTest.run(Distance.inches(1));
                setHeightTest.run(Distance.inches(5));
            });
    
            runner.testGroup("setSize(Size2D)", () ->
            {
                final Action2<Size2D,Throwable> setSizeErrorTest = (Size2D size, Throwable expected) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        test.assertThrows(() -> uiElement.setSize(size), expected);
                    });
                };
    
                setSizeErrorTest.run(null, new PreConditionFailure("size cannot be null."));
    
                final Action1<Size2D> setSizeTest = (Size2D size) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        final UIElement setSizeResult = uiElement.setSize(size);
                        test.assertSame(uiElement, setSizeResult);
                        test.assertEqual(size, uiElement.getSize());
                    });
                };
    
                setSizeTest.run(Size2D.create(Distance.zero, Distance.zero));
                setSizeTest.run(Size2D.create(Distance.zero, Distance.inches(1)));
                setSizeTest.run(Size2D.create(Distance.inches(1), Distance.zero));
                setSizeTest.run(Size2D.create(Distance.inches(2), Distance.inches(3)));
            });
    
            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> setSizeErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        test.assertThrows(() -> uiElement.setSize(width, height), expected);
                    });
                };
    
                setSizeErrorTest.run(null, Distance.inches(1), new PreConditionFailure("width cannot be null."));
                setSizeErrorTest.run(Distance.inches(-1), Distance.inches(1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                setSizeErrorTest.run(Distance.zero, null, new PreConditionFailure("height cannot be null."));
                setSizeErrorTest.run(Distance.zero, Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
    
                final Action2<Distance,Distance> setSizeTest = (Distance width, Distance height) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final UIElement uiElement = creator.run(test);
                        final UIElement setSizeResult = uiElement.setSize(width, height);
                        test.assertSame(uiElement, setSizeResult);
                        test.assertEqual(width, uiElement.getWidth());
                        test.assertEqual(height, uiElement.getHeight());
                    });
                };
    
                setSizeTest.run(Distance.zero, Distance.zero);
                setSizeTest.run(Distance.zero, Distance.inches(2));
                setSizeTest.run(Distance.inches(1.5), Distance.zero);
                setSizeTest.run(Distance.inches(5), Distance.inches(4));
            });

            runner.testGroup("onSizeChanged(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    test.assertThrows(() -> uiElement.onSizeChanged(null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with non-null", runner.skip(false), (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    final long currentThreadId = CurrentThread.getId();
                    final LongValue eventThreadId = LongValue.create();
                    final SpinGate gate = SpinGate.create(test.getClock());

                    final IntegerValue value = IntegerValue.create(0);
                    final Disposable subscription = uiElement.onSizeChanged(() ->
                    {
                        eventThreadId.set(CurrentThread.getId());
                        value.increment();
                        gate.open();
                    });
                    test.assertNotNull(subscription);
                    test.assertFalse(subscription.isDisposed());
                    test.assertFalse(eventThreadId.hasValue());
                    test.assertEqual(0, value.get());

                    uiElement.setSize(Distance.inches(10), Distance.inches(12));

                    // The callback should still not have been run, even though it's now been
                    // scheduled on the main async runner.
                    test.assertFalse(eventThreadId.hasValue());
                    test.assertEqual(0, value.get());

                    // Force the main async runner to run all of it's scheduled tasks.
                    gate.passThrough(Duration.seconds(1), () ->
                    {
                        test.getMainAsyncRunner().schedule(() -> {}).await();
                    }).await();

                    test.assertTrue(gate.isOpen());
                    test.assertTrue(eventThreadId.hasValue());
                    test.assertEqual(currentThreadId, eventThreadId.get());
                    test.assertEqual(1, value.get());

                    test.assertTrue(subscription.dispose().await());
                    test.assertEqual(1, value.get());

                    eventThreadId.clear();
                    value.set(0);
                    gate.close();

                    uiElement.setSize(Distance.inches(9), Distance.inches(11));

                    // Force the main async runner to run all of it's scheduled tasks.
                    test.assertThrows(TimeoutException.class, () ->
                    {
                        gate.passThrough(Duration.seconds(1), () ->
                        {
                            test.getMainAsyncRunner().schedule(() -> {}).await();
                        }).await();
                    });

                    test.assertFalse(gate.isOpen());
                    test.assertFalse(eventThreadId.hasValue());
                    test.assertEqual(0, value.get());
                });
            });

            runner.test("getPadding()", (Test test) ->
            {
                final UIElement uiElement = creator.run(test);
                final UIPadding padding = uiElement.getPadding();
                test.assertNotNull(padding);
                test.assertEqual(padding, uiElement.getPadding());
            });

            runner.testGroup("setPadding(UIPadding)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    final UIPadding padding = uiElement.getPadding();
                    test.assertThrows(() -> uiElement.setPadding(null),
                        new PreConditionFailure("padding cannot be null."));
                    test.assertEqual(padding, uiElement.getPadding());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    final UIPadding padding = UIPadding.create(Distance.inches(1), Distance.inches(2), Distance.inches(3), Distance.inches(4));
                    final UIElement setPaddingResult = uiElement.setPadding(padding);
                    test.assertSame(uiElement, setPaddingResult);
                    test.assertEqual(padding, uiElement.getPadding());
                });
            });

            runner.testGroup("onPaddingChanged(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    test.assertThrows(() -> uiElement.onPaddingChanged((Action0)null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("when padding set to equal padding", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    final IntegerValue changes = IntegerValue.create(0);
                    uiElement.onPaddingChanged(changes::increment);

                    uiElement.setPadding(uiElement.getPadding());

                    test.assertEqual(0, changes.get());
                });

                runner.test("when padding set to different padding", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    final IntegerValue changes = IntegerValue.create(0);
                    uiElement.onPaddingChanged(changes::increment);

                    uiElement.setPadding(UIPadding.create(Distance.inches(1)));

                    test.assertEqual(1, changes.get());
                });
            });

            runner.testGroup("onPaddingChanged(Action1<UIPadding>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    test.assertThrows(() -> uiElement.onPaddingChanged((Action1<UIPadding>)null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("when padding set to equal padding", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);

                    final List<UIPadding> changes = List.create();
                    uiElement.onPaddingChanged((UIPadding newPadding) -> changes.add(newPadding));

                    uiElement.setPadding(uiElement.getPadding());

                    test.assertEqual(Iterable.create(), changes);
                });

                runner.test("when padding set to equal padding", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);

                    final List<UIPadding> changes = List.create();
                    uiElement.onPaddingChanged((UIPadding newPadding) -> changes.add(newPadding));

                    uiElement.setPadding(UIPadding.create(Distance.inches(1)));

                    test.assertEqual(Iterable.create(UIPadding.create(Distance.inches(1))), changes);
                });
            });

            runner.testGroup("onPaddingChanged(Action2<UIPadding,UIPadding>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    test.assertThrows(() -> uiElement.onPaddingChanged((Action2<UIPadding,UIPadding>)null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("when padding set to equal padding", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);

                    final List<UIPadding> changes = List.create();
                    uiElement.onPaddingChanged((UIPadding oldPadding, UIPadding newPadding) -> changes.add(newPadding));

                    uiElement.setPadding(uiElement.getPadding());

                    test.assertEqual(Iterable.create(), changes);
                });

                runner.test("when padding set to equal padding", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    final UIPadding currentPadding = uiElement.getPadding();

                    final List<UIPadding> changes = List.create();
                    uiElement.onPaddingChanged((UIPadding oldPadding, UIPadding newPadding) -> changes.addAll(oldPadding, newPadding));

                    uiElement.setPadding(UIPadding.create(Distance.inches(1)));

                    test.assertEqual(
                        Iterable.create(
                            currentPadding,
                            UIPadding.create(Distance.inches(1))),
                        changes);
                });
            });

            runner.testGroup("setBackgroundColor(Color)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    final Color initialColor = uiElement.getBackgroundColor();
                    test.assertThrows(() -> uiElement.setBackgroundColor(null),
                        new PreConditionFailure("backgroundColor cannot be null."));
                    test.assertEqual(initialColor, uiElement.getBackgroundColor());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final UIElement uiElement = creator.run(test);
                    final UIElement setBackgroundColorResult = uiElement.setBackgroundColor(Color.blue);
                    test.assertSame(uiElement, setBackgroundColorResult);
                    test.assertEqual(Color.blue, uiElement.getBackgroundColor());
                });
            });
        });
    }
}
