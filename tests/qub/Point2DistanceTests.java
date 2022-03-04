package qub;

public interface Point2DistanceTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Point2Distance.class, () ->
        {
            runner.test("zero", (Test test) ->
            {
                final Point2Distance point = Point2Distance.zero;
                test.assertNotNull(point);
                test.assertEqual(Distance.zero, point.getX());
                test.assertEqual(Distance.zero, point.getY());
            });

            runner.testGroup("create(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> createErrorTest = (Distance x, Distance y, Throwable expected) ->
                {
                    runner.test("with " + English.andList(x, y), (Test test) ->
                    {
                        test.assertThrows(() -> Point2Distance.create(x, y),
                            expected);
                    });
                };

                createErrorTest.run(null, Distance.zero, new PreConditionFailure("x cannot be null."));
            });
        });
    }
}
