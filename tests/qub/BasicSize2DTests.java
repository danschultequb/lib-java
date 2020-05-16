package qub;

public interface BasicSize2DTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicSize2D.class, () ->
        {
            Size2DTests.test(runner, BasicSize2D::create);

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
        });
    }
}
