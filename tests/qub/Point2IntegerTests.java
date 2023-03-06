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

            runner.testGroup("create(Point2<Integer>)", () ->
            {
                final Action1<Point2<Integer>> createTest = (Point2<Integer> toCopy) ->
                {
                    runner.test("with " + toCopy, (Test test) ->
                    {
                        final MutablePoint2Integer point = Point2Integer.create(toCopy);
                        test.assertNotNull(point);
                        test.assertEqual(toCopy.getX(), point.getX());
                        test.assertEqual(toCopy.getY(), point.getY());
                    });
                };

                createTest.run(Point2.create(1, 2));
            });

            runner.testGroup("create(Point2Integer)", () ->
            {
                final Action1<Point2Integer> createTest = (Point2Integer toCopy) ->
                {
                    runner.test("with " + toCopy, (Test test) ->
                    {
                        final MutablePoint2Integer point = Point2Integer.create(toCopy);
                        test.assertNotNull(point);
                        test.assertEqual(toCopy.getX(), point.getX());
                        test.assertEqual(toCopy.getY(), point.getY());
                    });
                };

                createTest.run(Point2.create(1, 2));
                createTest.run(Point2.create(3, 4));
            });
        });
    }
}
