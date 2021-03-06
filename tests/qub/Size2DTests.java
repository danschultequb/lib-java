package qub;

public interface Size2DTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Size2D.class, () ->
        {
            runner.testGroup("create(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> createErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + width + " width and " + height + " height", (Test test) ->
                    {
                        test.assertThrows(() -> Size2D.create(width, height),
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
                        final Size2D size = Size2D.create(width, height);
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

    static void test(TestRunner runner, Function2<Distance,Distance,? extends Size2D> creator)
    {
        runner.testGroup(Size2D.class, () ->
        {
            runner.testGroup("changeWidth(Distance)", () ->
            {
                final Action3<Size2D,Distance,Throwable> changeWidthErrorTest = (Size2D size, Distance width, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, width), (Test test) ->
                    {
                        test.assertThrows(() -> size.changeWidth(width), expected);
                    });
                };

                changeWidthErrorTest.run(creator.run(Distance.inches(1), Distance.feet(2)), null, new PreConditionFailure("width cannot be null."));
                changeWidthErrorTest.run(creator.run(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Size2D,Distance> changeWidthTest = (Size2D size, Distance width) ->
                {
                    runner.test("with " + English.andList(size, width), (Test test) ->
                    {
                        final Distance previousWidth = size.getWidth();
                        final boolean differentWidth = !width.equals(previousWidth);
                        final Size2D changedSize = size.changeWidth(width);
                        test.assertEqual(width, changedSize.getWidth());
                        test.assertEqual(previousWidth, size.getWidth());
                        if (differentWidth)
                        {
                            test.assertNotSame(size, changedSize);
                        }
                        else
                        {
                            test.assertSame(size, changedSize);
                        }
                    });
                };

                changeWidthTest.run(creator.run(Distance.zero, Distance.feet(2)), Distance.zero);
                changeWidthTest.run(creator.run(Distance.feet(1), Distance.zero), Distance.inches(12));
                changeWidthTest.run(creator.run(Distance.zero, Distance.zero), Distance.inches(2));
            });

            runner.testGroup("changeHeight(Distance)", () ->
            {
                final Action3<Size2D,Distance,Throwable> changeHeightErrorTest = (Size2D size, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, height), (Test test) ->
                    {
                        test.assertThrows(() -> size.changeHeight(height), expected);
                    });
                };

                changeHeightErrorTest.run(creator.run(Distance.inches(1), Distance.feet(2)), null, new PreConditionFailure("height cannot be null."));
                changeHeightErrorTest.run(creator.run(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Size2D,Distance> changeHeightTest = (Size2D size, Distance height) ->
                {
                    runner.test("with " + English.andList(size, height), (Test test) ->
                    {
                        final Distance previousHeight = size.getHeight();
                        final boolean differentHeight = !height.equals(previousHeight);
                        final Size2D changedSize = size.changeHeight(height);
                        test.assertEqual(height, changedSize.getHeight());
                        test.assertEqual(previousHeight, size.getHeight());
                        if (differentHeight)
                        {
                            test.assertNotSame(size, changedSize);
                        }
                        else
                        {
                            test.assertSame(size, changedSize);
                        }
                    });
                };

                changeHeightTest.run(creator.run(Distance.zero, Distance.zero), Distance.zero);
                changeHeightTest.run(creator.run(Distance.feet(1), Distance.feet(1.5)), Distance.inches(18));
                changeHeightTest.run(creator.run(Distance.zero, Distance.zero), Distance.inches(2));
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Size2D,String> toStringTest = (Size2D size, String expected) ->
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
                final Action3<Size2D,Object,Boolean> equalsTest = (Size2D size, Object rhs, Boolean expected) ->
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

            runner.testGroup("equals(Size2D)", () ->
            {
                final Action3<Size2D,Size2D,Boolean> equalsTest = (Size2D size, Size2D rhs, Boolean expected) ->
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
                final Size2D size = creator.run(Distance.zero, Distance.zero);
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
