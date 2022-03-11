package qub;

public interface Size2DistanceTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Size2Distance.class, () ->
        {
            runner.test("zero", (Test test) ->
            {
                final Size2Distance zero = Size2Distance.zero;
                test.assertNotNull(zero);
                test.assertEqual(Distance.zero, zero.getWidth());
                test.assertEqual(Distance.zero, zero.getHeight());
            });

            runner.test("create()", (Test test) ->
            {
                final MutableSize2Distance zero = Size2Distance.create();
                test.assertNotNull(zero);
                test.assertEqual(Distance.zero, zero.getWidth());
                test.assertEqual(Distance.zero, zero.getHeight());
            });

            runner.testGroup("create(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> createErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        test.assertThrows(() -> Size2Distance.create(width, height),
                            expected);
                    });
                };

                createErrorTest.run(null, Distance.zero, new PreConditionFailure("width cannot be null."));
                createErrorTest.run(Distance.inches(-1), Distance.zero, new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                createErrorTest.run(Distance.zero, null, new PreConditionFailure("height cannot be null."));
                createErrorTest.run(Distance.zero, Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Distance,Distance> createTest = (Distance width, Distance height) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final MutableSize2Distance size = Size2Distance.create(width, height);
                        test.assertNotNull(size);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                createTest.run(Distance.inches(1), Distance.centimeters(5));
            });
        });
    }
}
