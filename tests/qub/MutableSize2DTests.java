package qub;

public interface MutableSize2DTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(MutableSize2D.class, () ->
        {
            runner.testGroup("create(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> createErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + width + " width and " + height + " height", (Test test) ->
                    {
                        test.assertThrows(() -> MutableSize2D.create(width, height),
                            expected);
                    });
                };

                createErrorTest.run(null, Distance.inches(1), new PreConditionFailure("width cannot be null."));
                createErrorTest.run(Distance.inches(-1), Distance.inches(1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                createErrorTest.run(Distance.inches(0.5), null, new PreConditionFailure("height cannot be null."));
                createErrorTest.run(Distance.inches(0.5), Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Distance,Distance> createTest = (Distance width, Distance height) ->
                {
                    runner.test("with " + width + " width and " + height + " height", (Test test) ->
                    {
                        final MutableSize2D size = MutableSize2D.create(width, height);
                        test.assertNotNull(size);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                createTest.run(Distance.zero, Distance.zero);
                createTest.run(Distance.inches(5), Distance.inches(6));
            });
        });
    }

    static void test(TestRunner runner, Function2<Distance,Distance,? extends MutableSize2D> creator)
    {
        runner.testGroup(MutableSize2D.class, () ->
        {
            runner.testGroup("setWidth(Distance)", () ->
            {
                final Action3<MutableSize2D,Distance,Throwable> setWidthErrorTest = (MutableSize2D size, Distance width, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, width), (Test test) ->
                    {
                        test.assertThrows(() -> size.setWidth(width), expected);
                    });
                };

                setWidthErrorTest.run(creator.run(Distance.inches(1), Distance.feet(2)), null, new PreConditionFailure("width cannot be null."));
                setWidthErrorTest.run(creator.run(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<MutableSize2D,Distance> setWidthTest = (MutableSize2D size, Distance width) ->
                {
                    runner.test("with " + English.andList(size, width), (Test test) ->
                    {
                        final boolean differentWidth = !width.equals(size.getWidth());
                        final IntegerValue counter = IntegerValue.create(0);
                        size.onChanged(counter::increment);
                        final MutableSize2D changedSize = size.setWidth(width);
                        test.assertSame(size, changedSize);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(differentWidth ? 1 : 0, counter.get());
                    });
                };

                setWidthTest.run(creator.run(Distance.zero, Distance.feet(2)), Distance.zero);
                setWidthTest.run(creator.run(Distance.feet(1), Distance.zero), Distance.inches(12));
                setWidthTest.run(creator.run(Distance.zero, Distance.zero), Distance.inches(2));
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                final Action3<MutableSize2D,Distance,Throwable> setHeightErrorTest = (MutableSize2D size, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, height), (Test test) ->
                    {
                        test.assertThrows(() -> size.setHeight(height), expected);
                    });
                };

                setHeightErrorTest.run(creator.run(Distance.inches(1), Distance.feet(2)), null, new PreConditionFailure("height cannot be null."));
                setHeightErrorTest.run(creator.run(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<MutableSize2D,Distance> setHeightTest = (MutableSize2D size, Distance height) ->
                {
                    runner.test("with " + English.andList(size, height), (Test test) ->
                    {
                        final boolean differentHeight = !height.equals(size.getHeight());
                        final IntegerValue counter = IntegerValue.create(0);
                        size.onChanged(counter::increment);
                        final MutableSize2D changedSize = size.setHeight(height);
                        test.assertSame(size, changedSize);
                        test.assertEqual(height, size.getHeight());
                        test.assertEqual(differentHeight ? 1 : 0, counter.get());
                    });
                };

                setHeightTest.run(creator.run(Distance.zero, Distance.zero), Distance.zero);
                setHeightTest.run(creator.run(Distance.feet(1), Distance.feet(1.5)), Distance.inches(18));
                setHeightTest.run(creator.run(Distance.zero, Distance.zero), Distance.inches(2));
            });

            runner.testGroup("set(Distance,Distance)", () ->
            {
                final Action4<MutableSize2D,Distance,Distance,Throwable> setErrorTest = (MutableSize2D size, Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, width, height), (Test test) ->
                    {
                        test.assertThrows(() -> size.set(width, height), expected);
                    });
                };

                setErrorTest.run(creator.run(Distance.inches(1), Distance.feet(2)), null, Distance.zero, new PreConditionFailure("width cannot be null."));
                setErrorTest.run(creator.run(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), Distance.zero, new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                setErrorTest.run(creator.run(Distance.inches(1), Distance.feet(2)), Distance.zero, null, new PreConditionFailure("height cannot be null."));
                setErrorTest.run(creator.run(Distance.feet(1), Distance.feet(2)), Distance.zero, Distance.feet(-1), new PreConditionFailure("height (-1.0 Feet) must be greater than or equal to 0.0 Inches."));

                final Action3<MutableSize2D,Distance,Distance> setTest = (MutableSize2D size, Distance width, Distance height) ->
                {
                    runner.test("with " + English.andList(size, width, height), (Test test) ->
                    {
                        final boolean different = !width.equals(size.getWidth()) || !height.equals(size.getHeight());
                        final IntegerValue counter = IntegerValue.create(0);
                        size.onChanged(counter::increment);
                        final MutableSize2D changedSize = size.set(width, height);
                        test.assertSame(size, changedSize);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                        test.assertEqual(different ? 1 : 0, counter.get());
                    });
                };

                setTest.run(creator.run(Distance.zero, Distance.zero), Distance.zero, Distance.zero);
                setTest.run(creator.run(Distance.feet(1), Distance.feet(1.5)), Distance.inches(12), Distance.inches(18));
                setTest.run(creator.run(Distance.zero, Distance.zero), Distance.inches(2), Distance.zero);
                setTest.run(creator.run(Distance.zero, Distance.zero), Distance.zero, Distance.feet(1));
                setTest.run(creator.run(Distance.zero, Distance.zero), Distance.miles(7), Distance.feet(1));
            });

            runner.testGroup("onChanged(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableSize2D size = creator.run(Distance.zero, Distance.zero);
                    test.assertThrows(() -> size.onChanged(null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final MutableSize2D size = creator.run(Distance.zero, Distance.zero);
                    final IntegerValue counter = IntegerValue.create(0);

                    final Disposable subscription = size.onChanged(counter::increment);
                    test.assertNotNull(subscription);
                    test.assertFalse(subscription.isDisposed());

                    size.set(Distance.inches(1), Distance.inches(2));
                    test.assertEqual(1, counter.get());

                    test.assertTrue(subscription.dispose().await());
                    test.assertTrue(subscription.isDisposed());

                    size.set(Distance.feet(1), Distance.feet(2));
                    test.assertEqual(1, counter.get());
                });
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<MutableSize2D,String> toStringTest = (MutableSize2D size, String expected) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        test.assertEqual(expected, size.toString());
                    });
                };

                toStringTest.run(creator.run(Distance.zero, Distance.zero), "{\"width\":\"0.0 Inches\",\"height\":\"0.0 Inches\"}");
                toStringTest.run(creator.run(Distance.feet(1), Distance.centimeters(2)), "{\"width\":\"1.0 Feet\",\"height\":\"2.0 Centimeters\"}");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<MutableSize2D,Object,Boolean> equalsTest = (MutableSize2D size, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(size, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, size.equals(rhs));
                    });
                };

                equalsTest.run(creator.run(Distance.zero, Distance.zero), null, false);
                equalsTest.run(creator.run(Distance.zero, Distance.zero), "hello", false);
                equalsTest.run(creator.run(Distance.zero, Distance.zero), creator.run(Distance.zero, Distance.zero), true);
                equalsTest.run(creator.run(Distance.feet(1), Distance.inches(2)), creator.run(Distance.zero, Distance.inches(2)), false);
                equalsTest.run(creator.run(Distance.feet(1), Distance.inches(2)), creator.run(Distance.feet(1), Distance.inches(3)), false);
            });

            runner.testGroup("equals(MutableSize2D)", () ->
            {
                final Action3<MutableSize2D,MutableSize2D,Boolean> equalsTest = (MutableSize2D size, MutableSize2D rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(size, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, size.equals(rhs));
                    });
                };

                equalsTest.run(creator.run(Distance.zero, Distance.zero), null, false);
                equalsTest.run(creator.run(Distance.zero, Distance.zero), creator.run(Distance.zero, Distance.zero), true);
                equalsTest.run(creator.run(Distance.feet(1), Distance.inches(2)), creator.run(Distance.zero, Distance.inches(2)), false);
                equalsTest.run(creator.run(Distance.feet(1), Distance.inches(2)), creator.run(Distance.feet(1), Distance.inches(3)), false);
            });

            runner.test("hashCode()", (Test test) ->
            {
                final MutableSize2D size = creator.run(Distance.zero, Distance.zero);
                test.assertEqual(size.hashCode(), size.hashCode());

                test.assertEqual(
                    creator.run(Distance.feet(1), Distance.feet(60)).hashCode(),
                    creator.run(Distance.feet(1), Distance.feet(60)).hashCode());

                test.assertNotEqual(
                    creator.run(Distance.feet(1), Distance.feet(60)).hashCode(),
                    creator.run(Distance.feet(2), Distance.feet(60)).hashCode());

                test.assertNotEqual(
                    creator.run(Distance.feet(1), Distance.feet(60)).hashCode(),
                    creator.run(Distance.feet(1), Distance.feet(61)).hashCode());

                test.assertNotEqual(
                    creator.run(Distance.feet(1), Distance.feet(60)).hashCode(),
                    creator.run(Distance.feet(2), Distance.feet(61)).hashCode());
            });
        });
    }
}
