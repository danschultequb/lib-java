package qub;

public interface ColorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Color.class, () ->
        {
            runner.testGroup("create(int,int,int)", () ->
            {
                final Action4<Integer,Integer,Integer,Throwable> createErrorTest = (Integer redComponent, Integer greenComponent, Integer blueComponent, Throwable expectedError) ->
                {
                    runner.test("with " + redComponent + ", " + greenComponent + ", and " + blueComponent, (Test test) ->
                    {
                        test.assertThrows(() -> Color.create(redComponent, greenComponent, blueComponent), expectedError);
                    });
                };

                createErrorTest.run(-1, 0, 0, new PreConditionFailure("redComponent (-1) must be between 0 and 255."));
                createErrorTest.run(256, 0, 0, new PreConditionFailure("redComponent (256) must be between 0 and 255."));
                createErrorTest.run(0, -1, 0, new PreConditionFailure("greenComponent (-1) must be between 0 and 255."));
                createErrorTest.run(0, 256, 0, new PreConditionFailure("greenComponent (256) must be between 0 and 255."));
                createErrorTest.run(0, 0, -1, new PreConditionFailure("blueComponent (-1) must be between 0 and 255."));
                createErrorTest.run(0, 0, 256, new PreConditionFailure("blueComponent (256) must be between 0 and 255."));

                final Action3<Integer,Integer,Integer> createTest = (Integer redComponent, Integer greenComponent, Integer blueComponent) ->
                {
                    runner.test("with " + redComponent + ", " + greenComponent + ", and " + blueComponent, (Test test) ->
                    {
                        final Color color = Color.create(redComponent, greenComponent, blueComponent);
                        test.assertNotNull(color);
                        test.assertEqual(redComponent, color.getRedComponent());
                        test.assertEqual(greenComponent, color.getGreenComponent());
                        test.assertEqual(blueComponent, color.getBlueComponent());
                        test.assertEqual(255, color.getAlphaComponent());
                    });
                };

                createTest.run(1, 2, 3);
                createTest.run(255, 0, 128);
            });

            runner.testGroup("create(int,int,int,int)", () ->
            {
                final Action5<Integer,Integer,Integer,Integer,Throwable> createErrorTest = (Integer red, Integer green, Integer blue, Integer alpha, Throwable expectedError) ->
                {
                    runner.test("with " + red + ", " + green + ", " + blue + ", and " + alpha, (Test test) ->
                    {
                        test.assertThrows(() -> Color.create(red, green, blue, alpha), expectedError);
                    });
                };

                createErrorTest.run(-1, 0, 0, 0, new PreConditionFailure("redComponent (-1) must be between 0 and 255."));
                createErrorTest.run(256, 0, 0, 0, new PreConditionFailure("redComponent (256) must be between 0 and 255."));
                createErrorTest.run(0, -1, 0, 0, new PreConditionFailure("greenComponent (-1) must be between 0 and 255."));
                createErrorTest.run(0, 256, 0, 0, new PreConditionFailure("greenComponent (256) must be between 0 and 255."));
                createErrorTest.run(0, 0, -1, 0, new PreConditionFailure("blueComponent (-1) must be between 0 and 255."));
                createErrorTest.run(0, 0, 256, 0, new PreConditionFailure("blueComponent (256) must be between 0 and 255."));
                createErrorTest.run(0, 0, 0, -1, new PreConditionFailure("alphaComponent (-1) must be between 0 and 255."));
                createErrorTest.run(0, 0, 0, 256, new PreConditionFailure("alphaComponent (256) must be between 0 and 255."));

                final Action4<Integer,Integer,Integer,Integer> createTest = (Integer redComponent, Integer greenComponent, Integer blueComponent, Integer alphaComponent) ->
                {
                    runner.test("with " + redComponent + ", " + greenComponent + ", " + blueComponent + ", and " + alphaComponent, (Test test) ->
                    {
                        final Color color = Color.create(redComponent, greenComponent, blueComponent, alphaComponent);
                        test.assertNotNull(color);
                        test.assertEqual(redComponent, color.getRedComponent());
                        test.assertEqual(greenComponent, color.getGreenComponent());
                        test.assertEqual(blueComponent, color.getBlueComponent());
                        test.assertEqual(alphaComponent, color.getAlphaComponent());
                    });
                };

                createTest.run(1, 2, 3, 4);
                createTest.run(255, 0, 128, 184);
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with " + English.andList(0, 1, 2), (Test test) ->
                {
                    final Color color = Color.create(0, 1, 2);
                    test.assertEqual("{\"redComponent\":\"0\",\"greenComponent\":\"1\",\"blueComponent\":\"2\",\"alphaComponent\":\"255\"}", color.toString());
                });

                runner.test("with " + English.andList(40, 50, 60, 70), (Test test) ->
                {
                    final Color color = Color.create(40, 50, 60, 70);
                    test.assertEqual("{\"redComponent\":\"40\",\"greenComponent\":\"50\",\"blueComponent\":\"60\",\"alphaComponent\":\"70\"}", color.toString());
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
                    test.assertTrue(Color.red.equals((Object)Color.create(255, 0, 0)));
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
                    test.assertTrue(Color.red.equals(Color.create(255, 0, 0)));
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
                    test.assertEqual(Color.create(1, 2, 3).hashCode(), Color.create(1, 2, 3).hashCode());
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
                test.assertEqual(255, white.getRedComponent());
                test.assertEqual(255, white.getGreenComponent());
                test.assertEqual(255, white.getBlueComponent());
                test.assertEqual(255, white.getAlphaComponent());
            });

            runner.test("black", (Test test) ->
            {
                final Color black = Color.black;
                test.assertNotNull(black);
                test.assertEqual(0, black.getRedComponent());
                test.assertEqual(0, black.getGreenComponent());
                test.assertEqual(0, black.getBlueComponent());
                test.assertEqual(255, black.getAlphaComponent());
            });

            runner.test("red", (Test test) ->
            {
                final Color red = Color.red;
                test.assertNotNull(red);
                test.assertEqual(255, red.getRedComponent());
                test.assertEqual(0, red.getGreenComponent());
                test.assertEqual(0, red.getBlueComponent());
                test.assertEqual(255, red.getAlphaComponent());
            });

            runner.test("green", (Test test) ->
            {
                final Color green = Color.green;
                test.assertNotNull(green);
                test.assertEqual(0, green.getRedComponent());
                test.assertEqual(255, green.getGreenComponent());
                test.assertEqual(0, green.getBlueComponent());
                test.assertEqual(255, green.getAlphaComponent());
            });

            runner.test("blue", (Test test) ->
            {
                final Color blue = Color.blue;
                test.assertNotNull(blue);
                test.assertEqual(0, blue.getRedComponent());
                test.assertEqual(0, blue.getGreenComponent());
                test.assertEqual(255, blue.getBlueComponent());
                test.assertEqual(255, blue.getAlphaComponent());
            });
        });
    }
}
