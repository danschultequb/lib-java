package qub;

public interface MutableSize2DistanceTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutableSize2Distance.class, () ->
        {
            MutableSize2Tests.test(runner, MutableSize2Distance::create);

            runner.testGroup("create(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> createErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + width + " width and " + height + " height", (Test test) ->
                    {
                        test.assertThrows(() -> MutableSize2Distance.create(width, height),
                            expected);
                    });
                };

                createErrorTest.run(Distance.inches(-1), Distance.inches(1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                createErrorTest.run(Distance.inches(0.5), null, new PreConditionFailure("height cannot be null."));
                createErrorTest.run(Distance.inches(0.5), Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Distance,Distance> createTest = (Distance width, Distance height) ->
                {
                    runner.test("with " + width + " width and " + height + " height", (Test test) ->
                    {
                        final MutableSize2Distance size = MutableSize2Distance.create(width, height);
                        test.assertNotNull(size);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                createTest.run(Distance.zero, Distance.zero);
                createTest.run(Distance.inches(5), Distance.inches(6));
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                final Action3<MutableSize2Distance,Distance,Throwable> setWidthErrorTest = (MutableSize2Distance size, Distance width, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, width), (Test test) ->
                    {
                        test.assertThrows(() -> size.setWidth(width), expected);
                    });
                };

                setWidthErrorTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<MutableSize2Distance,Distance> setWidthTest = (MutableSize2Distance size, Distance width) ->
                {
                    runner.test("with " + English.andList(size, width), (Test test) ->
                    {
                        final MutableSize2Distance setWidthResult = size.setWidth(width);
                        test.assertSame(size, setWidthResult);
                        test.assertEqual(width, size.getWidth());
                    });
                };

                setWidthTest.run(MutableSize2Distance.create(Distance.zero, Distance.feet(2)), Distance.zero);
                setWidthTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.zero), Distance.inches(12));
                setWidthTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Distance.inches(2));
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                final Action3<MutableSize2Distance,Distance,Throwable> setHeightErrorTest = (MutableSize2Distance size, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, height), (Test test) ->
                    {
                        test.assertThrows(() -> size.setHeight(height), expected);
                    });
                };

                setHeightErrorTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<MutableSize2Distance,Distance> setHeightTest = (MutableSize2Distance size, Distance height) ->
                {
                    runner.test("with " + English.andList(size, height), (Test test) ->
                    {
                        final MutableSize2Distance setHeightResult = size.setHeight(height);
                        test.assertSame(size, setHeightResult);
                        test.assertEqual(height, size.getHeight());
                    });
                };

                setHeightTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Distance.zero);
                setHeightTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.feet(1.5)), Distance.inches(18));
                setHeightTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Distance.inches(2));
            });

            runner.testGroup("set(Distance,Distance)", () ->
            {
                final Action4<MutableSize2Distance,Distance,Distance,Throwable> setErrorTest = (MutableSize2Distance size, Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(size, width, height), (Test test) ->
                    {
                        test.assertThrows(() -> size.set(width, height), expected);
                    });
                };

                setErrorTest.run(MutableSize2Distance.create(Distance.inches(1), Distance.feet(2)), null, Distance.zero, new PreConditionFailure("width cannot be null."));
                setErrorTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.feet(2)), Distance.inches(-1), Distance.zero, new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                setErrorTest.run(MutableSize2Distance.create(Distance.inches(1), Distance.feet(2)), Distance.zero, null, new PreConditionFailure("height cannot be null."));
                setErrorTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.feet(2)), Distance.zero, Distance.feet(-1), new PreConditionFailure("height (-1.0 Feet) must be greater than or equal to 0.0 Inches."));

                final Action3<MutableSize2Distance,Distance,Distance> setTest = (MutableSize2Distance size, Distance width, Distance height) ->
                {
                    runner.test("with " + English.andList(size, width, height), (Test test) ->
                    {
                        final MutableSize2Distance setResult = size.set(width, height);
                        test.assertSame(size, setResult);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                setTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Distance.zero, Distance.zero);
                setTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.feet(1.5)), Distance.inches(12), Distance.inches(18));
                setTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Distance.inches(2), Distance.zero);
                setTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Distance.zero, Distance.feet(1));
                setTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Distance.miles(7), Distance.feet(1));
            });

            runner.testGroup("set(Size2<Distance>)", () ->
            {
                final Action2<MutableSize2Distance,Size2<Distance>> setTest = (MutableSize2Distance size, Size2<Distance> newSize) ->
                {
                    runner.test("with " + English.andList(size, newSize), (Test test) ->
                    {
                        final MutableSize2Distance setResult = size.set(newSize);
                        test.assertSame(size, setResult);
                        test.assertEqual(newSize.getWidth(), size.getWidth());
                        test.assertEqual(newSize.getHeight(), size.getHeight());
                    });
                };

                setTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Size2Distance.zero);
                setTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.feet(1.5)), Size2.create(Distance.inches(12), Distance.inches(18)));
                setTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Size2.create(Distance.inches(2), Distance.zero));
                setTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Size2.create(Distance.zero, Distance.feet(1)));
                setTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), Size2.create(Distance.miles(7), Distance.feet(1)));
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<MutableSize2Distance,String> toStringTest = (MutableSize2Distance size, String expected) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        test.assertEqual(expected, size.toString());
                    });
                };

                toStringTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), "{\"width\":\"0.0 Inches\",\"height\":\"0.0 Inches\"}");
                toStringTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.centimeters(2)), "{\"width\":\"1.0 Feet\",\"height\":\"2.0 Centimeters\"}");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<MutableSize2Distance,Object,Boolean> equalsTest = (MutableSize2Distance size, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(size, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, size.equals(rhs));
                    });
                };

                equalsTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), null, false);
                equalsTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), "hello", false);
                equalsTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), MutableSize2Distance.create(Distance.zero, Distance.zero), true);
                equalsTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.inches(2)), MutableSize2Distance.create(Distance.zero, Distance.inches(2)), false);
                equalsTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.inches(2)), MutableSize2Distance.create(Distance.feet(1), Distance.inches(3)), false);
            });

            runner.testGroup("equals(MutableSize2Distance)", () ->
            {
                final Action3<MutableSize2Distance,MutableSize2Distance,Boolean> equalsTest = (MutableSize2Distance size, MutableSize2Distance rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(size, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, size.equals(rhs));
                    });
                };

                equalsTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), null, false);
                equalsTest.run(MutableSize2Distance.create(Distance.zero, Distance.zero), MutableSize2Distance.create(Distance.zero, Distance.zero), true);
                equalsTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.inches(2)), MutableSize2Distance.create(Distance.zero, Distance.inches(2)), false);
                equalsTest.run(MutableSize2Distance.create(Distance.feet(1), Distance.inches(2)), MutableSize2Distance.create(Distance.feet(1), Distance.inches(3)), false);
            });

            runner.test("hashCode()", (Test test) ->
            {
                final MutableSize2Distance size = MutableSize2Distance.create(Distance.zero, Distance.zero);
                test.assertEqual(size.hashCode(), size.hashCode());

                test.assertEqual(
                    MutableSize2Distance.create(Distance.feet(1), Distance.feet(60)).hashCode(),
                    MutableSize2Distance.create(Distance.feet(1), Distance.feet(60)).hashCode());

                test.assertNotEqual(
                    MutableSize2Distance.create(Distance.feet(1), Distance.feet(60)).hashCode(),
                    MutableSize2Distance.create(Distance.feet(2), Distance.feet(60)).hashCode());

                test.assertNotEqual(
                    MutableSize2Distance.create(Distance.feet(1), Distance.feet(60)).hashCode(),
                    MutableSize2Distance.create(Distance.feet(1), Distance.feet(61)).hashCode());

                test.assertNotEqual(
                    MutableSize2Distance.create(Distance.feet(1), Distance.feet(60)).hashCode(),
                    MutableSize2Distance.create(Distance.feet(2), Distance.feet(61)).hashCode());
            });
        });
    }
}
