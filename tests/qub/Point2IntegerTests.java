package qub;

public interface Point2IntegerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Point2Integer.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutablePoint2Integer point = Point2Integer.create();
                test.assertNotNull(point);
                test.assertEqual(0, point.getX());
                test.assertEqual(0, point.getY());
            });

            runner.testGroup("create(int,int)", () ->
            {
                final Action2<Integer,Integer> createTest = (Integer x, Integer y) ->
                {
                    runner.test("with " + English.andList(x, y), (Test test) ->
                    {
                        final MutablePoint2Integer point = Point2Integer.create(x, y);
                        test.assertNotNull(point);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                createTest.run(1, 2);
            });
        });
    }
}
