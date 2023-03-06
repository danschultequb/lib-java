package qub;

public interface Point2Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Point2.class, () ->
        {
            runner.test("create(int,int)", (Test test) ->
            {
                final MutablePoint2Integer point = Point2.create(1, 2);
                test.assertNotNull(point);
                test.assertEqual(1, point.getX());
                test.assertEqual(2, point.getY());

                test.assertEqual("{\"x\":1,\"y\":2}", point.toString());

                test.assertEqual(7, point.hashCode());

                test.assertFalse(point.equals((Object)null));
                test.assertFalse(point.equals((Object)"hello"));
                test.assertTrue(point.equals((Object)Point2.create(1, 2)));
                test.assertFalse(point.equals((Object)Point2.create(2, 2)));
                test.assertFalse(point.equals((Object)Point2.create(1, 3)));
                test.assertFalse(point.equals((Object)Point2.create(4, 5)));

                test.assertFalse(point.equals((Point2<Integer>)null));
                test.assertTrue(point.equals(Point2.create(1, 2)));
                test.assertFalse(point.equals(Point2.create(2, 2)));
                test.assertFalse(point.equals(Point2.create(1, 3)));
                test.assertFalse(point.equals(Point2.create(4, 5)));
            });

            runner.test("create(Point2<Integer>)", (Test test) ->
            {
                final MutablePoint2Integer point = Point2.create((Point2<Integer>)Point2.create(1, 2));
                test.assertNotNull(point);
                test.assertEqual(1, point.getX());
                test.assertEqual(2, point.getY());

                test.assertEqual("{\"x\":1,\"y\":2}", point.toString());

                test.assertEqual(7, point.hashCode());

                test.assertFalse(point.equals((Object)null));
                test.assertFalse(point.equals((Object)"hello"));
                test.assertTrue(point.equals((Object)Point2.create(1, 2)));
                test.assertFalse(point.equals((Object)Point2.create(2, 2)));
                test.assertFalse(point.equals((Object)Point2.create(1, 3)));
                test.assertFalse(point.equals((Object)Point2.create(4, 5)));

                test.assertFalse(point.equals((Point2<Integer>)null));
                test.assertTrue(point.equals(Point2.create(1, 2)));
                test.assertFalse(point.equals(Point2.create(2, 2)));
                test.assertFalse(point.equals(Point2.create(1, 3)));
                test.assertFalse(point.equals(Point2.create(4, 5)));
            });

            runner.test("create(Point2Integer)", (Test test) ->
            {
                final MutablePoint2Integer point = Point2.create((Point2Integer)Point2.create(1, 2));
                test.assertNotNull(point);
                test.assertEqual(1, point.getX());
                test.assertEqual(2, point.getY());

                test.assertEqual("{\"x\":1,\"y\":2}", point.toString());

                test.assertEqual(7, point.hashCode());

                test.assertFalse(point.equals((Object)null));
                test.assertFalse(point.equals((Object)"hello"));
                test.assertTrue(point.equals((Object)Point2.create(1, 2)));
                test.assertFalse(point.equals((Object)Point2.create(2, 2)));
                test.assertFalse(point.equals((Object)Point2.create(1, 3)));
                test.assertFalse(point.equals((Object)Point2.create(4, 5)));

                test.assertFalse(point.equals((Point2<Integer>)null));
                test.assertTrue(point.equals(Point2.create(1, 2)));
                test.assertFalse(point.equals(Point2.create(2, 2)));
                test.assertFalse(point.equals(Point2.create(1, 3)));
                test.assertFalse(point.equals(Point2.create(4, 5)));
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
                createErrorTest.run(Distance.zero, null, new PreConditionFailure("y cannot be null."));

                runner.test("with valid x and y", (Test test) ->
                {
                    final MutablePoint2Distance point = Point2.create(Distance.inches(1), Distance.miles(2));
                    test.assertNotNull(point);
                    test.assertEqual(Distance.inches(1), point.getX());
                    test.assertEqual(Distance.miles(2), point.getY());

                    test.assertEqual("{\"x\":\"1.0 Inches\",\"y\":\"2.0 Miles\"}", point.toString());

                    test.assertEqual(412888199, point.hashCode());

                    test.assertFalse(point.equals((Object)null));
                    test.assertFalse(point.equals((Object)"hello"));
                    test.assertTrue(point.equals((Object)Point2.create(Distance.inches(1), Distance.miles(2))));
                    test.assertFalse(point.equals((Object)Point2.create(Distance.inches(2), Distance.miles(2))));
                    test.assertFalse(point.equals((Object)Point2.create(Distance.inches(1), Distance.miles(3))));
                    test.assertFalse(point.equals((Object)Point2.create(Distance.inches(4), Distance.miles(5))));

                    test.assertFalse(point.equals((Point2<Distance>)null));
                    test.assertTrue(point.equals(Point2.create(Distance.inches(1), Distance.miles(2))));
                    test.assertFalse(point.equals(Point2.create(Distance.inches(2), Distance.miles(2))));
                    test.assertFalse(point.equals(Point2.create(Distance.inches(1), Distance.miles(3))));
                    test.assertFalse(point.equals(Point2.create(Distance.inches(4), Distance.miles(5))));
                });
            });
        });
    }
}
