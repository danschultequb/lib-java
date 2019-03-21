package qub;

public class ColorTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Color.class, () ->
        {
            runner.testGroup("rgb(double,double,double)", () ->
            {
                final Action4<Double,Double,Double,Throwable> rgbThrowTest = (Double red, Double green, Double blue, Throwable expectedError) ->
                {
                    runner.test("with " + red + ", " + green + ", and " + blue, (Test test) ->
                    {
                        test.assertThrows(() -> Color.rgb(red, green, blue), expectedError);
                    });
                };

                rgbThrowTest.run(-0.1, 0.0, 0.0, new PreConditionFailure("red (-0.1) must be between 0.0 and 1.0."));
                rgbThrowTest.run(1.1, 0.0, 0.0, new PreConditionFailure("red (1.1) must be between 0.0 and 1.0."));
                rgbThrowTest.run(0.0, -1.0, 0.0, new PreConditionFailure("green (-1.0) must be between 0.0 and 1.0."));
                rgbThrowTest.run(0.0, 10.0, 0.0, new PreConditionFailure("green (10.0) must be between 0.0 and 1.0."));
                rgbThrowTest.run(0.0, 0.0, -20.0, new PreConditionFailure("blue (-20.0) must be between 0.0 and 1.0."));
                rgbThrowTest.run(0.0, 0.0, 1.001, new PreConditionFailure("blue (1.001) must be between 0.0 and 1.0."));

                final Action3<Double,Double,Double> rgbTest = (Double red, Double green, Double blue) ->
                {
                    runner.test("with " + red + ", " + green + ", and " + blue, (Test test) ->
                    {
                        final Color color = Color.rgb(red, green, blue);
                        test.assertNotNull(color);
                        test.assertEqual(red, color.getRed());
                        test.assertEqual(green, color.getGreen());
                        test.assertEqual(blue, color.getBlue());
                        test.assertEqual(1, color.getAlpha());
                    });
                };

                rgbTest.run(0.1, 0.2, 0.3);
                rgbTest.run(1.0, 0.0, 0.5);
            });

            runner.testGroup("rgba(double,double,double,double)", () ->
            {
                final Action5<Double,Double,Double,Double,Throwable> rgbaThrowTest = (Double red, Double green, Double blue, Double alpha, Throwable expectedError) ->
                {
                    runner.test("with " + red + ", " + green + ", " + blue + ", and " + alpha, (Test test) ->
                    {
                        test.assertThrows(() -> Color.rgba(red, green, blue, alpha), expectedError);
                    });
                };

                rgbaThrowTest.run(-0.1, 0.0, 0.0, 0.0, new PreConditionFailure("red (-0.1) must be between 0.0 and 1.0."));
                rgbaThrowTest.run(1.1, 0.0, 0.0, 0.0, new PreConditionFailure("red (1.1) must be between 0.0 and 1.0."));
                rgbaThrowTest.run(0.0, -1.0, 0.0, 0.0, new PreConditionFailure("green (-1.0) must be between 0.0 and 1.0."));
                rgbaThrowTest.run(0.0, 10.0, 0.0, 0.0, new PreConditionFailure("green (10.0) must be between 0.0 and 1.0."));
                rgbaThrowTest.run(0.0, 0.0, -20.0, 0.0, new PreConditionFailure("blue (-20.0) must be between 0.0 and 1.0."));
                rgbaThrowTest.run(0.0, 0.0, 1.001, 0.0, new PreConditionFailure("blue (1.001) must be between 0.0 and 1.0."));
                rgbaThrowTest.run(0.0, 0.0, 0.0, -20.0, new PreConditionFailure("alpha (-20.0) must be between 0.0 and 1.0."));
                rgbaThrowTest.run(0.0, 0.0, 0.0, 1.001, new PreConditionFailure("alpha (1.001) must be between 0.0 and 1.0."));

                final Action4<Double,Double,Double,Double> rgbaTest = (Double red, Double green, Double blue, Double alpha) ->
                {
                    runner.test("with " + red + ", " + green + ", " + blue + ", and " + alpha, (Test test) ->
                    {
                        final Color color = Color.rgba(red, green, blue, alpha);
                        test.assertNotNull(color);
                        test.assertEqual(red, color.getRed());
                        test.assertEqual(green, color.getGreen());
                        test.assertEqual(blue, color.getBlue());
                        test.assertEqual(alpha, color.getAlpha());
                    });
                };

                rgbaTest.run(0.1, 0.2, 0.3, 0.4);
                rgbaTest.run(1.0, 0.0, 0.5, 0.73);
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with 0.0, 0.1, 0.2, and 0.3", (Test test) ->
                {
                    final Color color = Color.rgba(0.0, 0.1, 0.2, 0.3);
                    test.assertEqual("{\"red\":\"0.0\",\"green\":\"0.1\",\"blue\":\"0.2\",\"alpha\":\"0.3\"}", color.toString());
                });

                runner.test("with 0.4, 0.5, 0.6, and 0.7", (Test test) ->
                {
                    final Color color = Color.rgba(0.4, 0.5, 0.6, 0.7);
                    test.assertEqual("{\"red\":\"0.4\",\"green\":\"0.5\",\"blue\":\"0.6\",\"alpha\":\"0.7\"}", color.toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Color.red.equals((Object)null));
                });

                runner.test("with non-Color", (Test test) ->
                {
                    test.assertFalse(Color.red.equals((Object)"test"));
                });

                runner.test("with same", (Test test) ->
                {
                    test.assertTrue(Color.red.equals((Object)Color.red));
                });

                runner.test("with equal", (Test test) ->
                {
                    test.assertTrue(Color.red.equals((Object)Color.rgb(1, 0, 0)));
                });

                runner.test("with different", (Test test) ->
                {
                    test.assertFalse(Color.red.equals((Object)Color.white));
                });
            });

            runner.testGroup("equals(Color)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Color.red.equals((Color)null));
                });

                runner.test("with same", (Test test) ->
                {
                    test.assertTrue(Color.red.equals(Color.red));
                });

                runner.test("with equal", (Test test) ->
                {
                    test.assertTrue(Color.red.equals(Color.rgb(1, 0, 0)));
                });

                runner.test("with different", (Test test) ->
                {
                    test.assertFalse(Color.red.equals(Color.white));
                });
            });

            runner.testGroup("hashCode()", () ->
            {
                runner.test("with same", (Test test) ->
                {
                    test.assertEqual(Color.red.hashCode(), Color.red.hashCode());
                });

                runner.test("with equal", (Test test) ->
                {
                    test.assertEqual(Color.rgb(0.1, 0.2, 0.3).hashCode(), Color.rgb(0.1, 0.2, 0.3).hashCode());
                });

                runner.test("with different", (Test test) ->
                {
                    test.assertNotEqual(Color.red.hashCode(), Color.blue.hashCode());
                });
            });

            runner.test("white", (Test test) ->
            {
                final Color white = Color.white;
                test.assertNotNull(white);
                test.assertEqual(1, white.getRed());
                test.assertEqual(1, white.getGreen());
                test.assertEqual(1, white.getBlue());
                test.assertEqual(1, white.getAlpha());
            });

            runner.test("black", (Test test) ->
            {
                final Color black = Color.black;
                test.assertNotNull(black);
                test.assertEqual(0, black.getRed());
                test.assertEqual(0, black.getGreen());
                test.assertEqual(0, black.getBlue());
                test.assertEqual(1, black.getAlpha());
            });

            runner.test("red", (Test test) ->
            {
                final Color red = Color.red;
                test.assertNotNull(red);
                test.assertEqual(1, red.getRed());
                test.assertEqual(0, red.getGreen());
                test.assertEqual(0, red.getBlue());
                test.assertEqual(1, red.getAlpha());
            });

            runner.test("green", (Test test) ->
            {
                final Color green = Color.green;
                test.assertNotNull(green);
                test.assertEqual(0, green.getRed());
                test.assertEqual(1, green.getGreen());
                test.assertEqual(0, green.getBlue());
                test.assertEqual(1, green.getAlpha());
            });

            runner.test("blue", (Test test) ->
            {
                final Color blue = Color.blue;
                test.assertNotNull(blue);
                test.assertEqual(0, blue.getRed());
                test.assertEqual(0, blue.getGreen());
                test.assertEqual(1, blue.getBlue());
                test.assertEqual(1, blue.getAlpha());
            });
        });
    }
}
