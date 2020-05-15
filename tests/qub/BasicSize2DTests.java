package qub;

public interface BasicSize2DTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicSize2D.class, () ->
        {
            runner.testGroup("create(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> createErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + width + " width and " + height + " height", (Test test) ->
                    {
                        test.assertThrows(() -> BasicSize2D.create(width, height),
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
                        final BasicSize2D size = BasicSize2D.create(width, height);
                        test.assertNotNull(size);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                createTest.run(Distance.zero, Distance.zero);
                createTest.run(Distance.inches(5), Distance.inches(6));
            });

            runner.testGroup("changeWidth(Distance)", () ->
            {
                final Action3<BasicSize2D,Distance,Throwable> changeWidthErrorTest = (BasicSize2D size, Distance width, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, width), (Test test) ->
                    {
                        test.assertThrows(() -> size.changeWidth(width), expected);
                    });
                };

                changeWidthErrorTest.run(BasicSize2D.create(Distance.inches(1), Distance.feet(2)), null, new PreConditionFailure("width cannot be null."));
                changeWidthErrorTest.run(BasicSize2D.create(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<BasicSize2D,Distance> changeWidthTest = (BasicSize2D size, Distance width) ->
                {
                    runner.test("with " + English.andList(size, width), (Test test) ->
                    {
                        final Distance previousWidth = size.getWidth();
                        final boolean differentWidth = !width.equals(previousWidth);
                        final BasicSize2D changedSize = size.changeWidth(width);
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

                changeWidthTest.run(BasicSize2D.create(Distance.zero, Distance.feet(2)), Distance.zero);
                changeWidthTest.run(BasicSize2D.create(Distance.feet(1), Distance.zero), Distance.inches(12));
                changeWidthTest.run(BasicSize2D.create(Distance.zero, Distance.zero), Distance.inches(2));
            });

            runner.testGroup("changeHeight(Distance)", () ->
            {
                final Action3<BasicSize2D,Distance,Throwable> changeHeightErrorTest = (BasicSize2D size, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, height), (Test test) ->
                    {
                        test.assertThrows(() -> size.changeHeight(height), expected);
                    });
                };

                changeHeightErrorTest.run(BasicSize2D.create(Distance.inches(1), Distance.feet(2)), null, new PreConditionFailure("height cannot be null."));
                changeHeightErrorTest.run(BasicSize2D.create(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<BasicSize2D,Distance> changeHeightTest = (BasicSize2D size, Distance height) ->
                {
                    runner.test("with " + English.andList(size, height), (Test test) ->
                    {
                        final Distance previousHeight = size.getHeight();
                        final boolean differentHeight = !height.equals(previousHeight);
                        final BasicSize2D changedSize = size.changeHeight(height);
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

                changeHeightTest.run(BasicSize2D.create(Distance.zero, Distance.zero), Distance.zero);
                changeHeightTest.run(BasicSize2D.create(Distance.feet(1), Distance.feet(1.5)), Distance.inches(18));
                changeHeightTest.run(BasicSize2D.create(Distance.zero, Distance.zero), Distance.inches(2));
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<BasicSize2D,String> toStringTest = (BasicSize2D size, String expected) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        test.assertEqual(expected, size.toString());
                    });
                };

                toStringTest.run(BasicSize2D.create(Distance.zero, Distance.zero), "{\"width\":\"0.0 Inches\",\"height\":\"0.0 Inches\"}");
                toStringTest.run(BasicSize2D.create(Distance.feet(1), Distance.centimeters(2)), "{\"width\":\"1.0 Feet\",\"height\":\"2.0 Centimeters\"}");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<BasicSize2D,Object,Boolean> equalsTest = (BasicSize2D size, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(size, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, size.equals(rhs));
                    });
                };

                equalsTest.run(BasicSize2D.create(Distance.zero, Distance.zero), null, false);
                equalsTest.run(BasicSize2D.create(Distance.zero, Distance.zero), "hello", false);
                equalsTest.run(BasicSize2D.create(Distance.zero, Distance.zero), BasicSize2D.create(Distance.zero, Distance.zero), true);
                equalsTest.run(BasicSize2D.create(Distance.feet(1), Distance.inches(2)), BasicSize2D.create(Distance.zero, Distance.inches(2)), false);
                equalsTest.run(BasicSize2D.create(Distance.feet(1), Distance.inches(2)), BasicSize2D.create(Distance.feet(1), Distance.inches(3)), false);
            });

            runner.testGroup("equals(BasicSize2D)", () ->
            {
                final Action3<BasicSize2D,BasicSize2D,Boolean> equalsTest = (BasicSize2D size, BasicSize2D rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(size, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, size.equals(rhs));
                    });
                };

                equalsTest.run(BasicSize2D.create(Distance.zero, Distance.zero), null, false);
                equalsTest.run(BasicSize2D.create(Distance.zero, Distance.zero), BasicSize2D.create(Distance.zero, Distance.zero), true);
                equalsTest.run(BasicSize2D.create(Distance.feet(1), Distance.inches(2)), BasicSize2D.create(Distance.zero, Distance.inches(2)), false);
                equalsTest.run(BasicSize2D.create(Distance.feet(1), Distance.inches(2)), BasicSize2D.create(Distance.feet(1), Distance.inches(3)), false);
            });

            runner.test("hashCode()", (Test test) ->
            {
                final BasicSize2D size = BasicSize2D.create(Distance.zero, Distance.zero);
                test.assertEqual(size.hashCode(), size.hashCode());

                test.assertEqual(
                    BasicSize2D.create(Distance.feet(1), Distance.feet(60)).hashCode(),
                    BasicSize2D.create(Distance.feet(1), Distance.feet(60)).hashCode());

                test.assertNotEqual(
                    BasicSize2D.create(Distance.feet(1), Distance.feet(60)).hashCode(),
                    BasicSize2D.create(Distance.feet(2), Distance.feet(60)).hashCode());

                test.assertNotEqual(
                    BasicSize2D.create(Distance.feet(1), Distance.feet(60)).hashCode(),
                    BasicSize2D.create(Distance.feet(1), Distance.feet(61)).hashCode());

                test.assertNotEqual(
                    BasicSize2D.create(Distance.feet(1), Distance.feet(60)).hashCode(),
                    BasicSize2D.create(Distance.feet(2), Distance.feet(61)).hashCode());
            });
        });
    }
}
