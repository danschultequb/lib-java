package qub;

public class Point2DTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Point2D.class, () ->
        {
            runner.testGroup("constructor(Distance,Distance)", () ->
            {
                runner.test("with null x", (Test test) ->
                {
                    test.assertThrows(() -> new Point2D(null, Distance.inches(5)));
                });

                runner.test("with null y", (Test test) ->
                {
                    test.assertThrows(() -> new Point2D(Distance.meters(2.3), null));
                });

                runner.test("with non-null coordinates", (Test test) ->
                {
                    final Point2D point = new Point2D(Distance.inches(1), Distance.centimeters(2));
                    test.assertEqual(Distance.inches(1), point.getX());
                    test.assertEqual(Distance.centimeters(2), point.getY());
                });
            });

            runner.test("toString()", (Test test) ->
            {
                test.assertEqual("(5.0 Inches,3.0 Meters)", new Point2D(Distance.inches(5), Distance.meters(3)).toString());
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(new Point2D(Distance.feet(1), Distance.feet(2)).equals((Object)null));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertFalse(new Point2D(Distance.feet(1), Distance.feet(2)).equals((Object)"test"));
                });

                runner.test("with same", (Test test) ->
                {
                    final Point2D point = new Point2D(Distance.inches(1), Distance.centimeters(2));
                    test.assertTrue(point.equals((Object)point));
                });

                runner.test("with equal", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Distance.miles(5), Distance.kilometers(2));
                    final Point2D p2 = new Point2D(Distance.miles(5), Distance.kilometers(2));
                    test.assertTrue(p1.equals((Object)p2));
                });

                runner.test("with different", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Distance.miles(5), Distance.kilometers(2));
                    final Point2D p2 = new Point2D(Distance.millimeters(5), Distance.meters(2));
                    test.assertFalse(p1.equals((Object)p2));
                });
            });

            runner.testGroup("equals(Point2D)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(new Point2D(Distance.feet(1), Distance.feet(2)).equals((Point2D)null));
                });

                runner.test("with same", (Test test) ->
                {
                    final Point2D point = new Point2D(Distance.inches(1), Distance.centimeters(2));
                    test.assertTrue(point.equals(point));
                });

                runner.test("with equal", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Distance.miles(5), Distance.kilometers(2));
                    final Point2D p2 = new Point2D(Distance.miles(5), Distance.kilometers(2));
                    test.assertTrue(p1.equals(p2));
                });

                runner.test("with different x", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Distance.miles(5), Distance.kilometers(2));
                    final Point2D p2 = new Point2D(Distance.millimeters(5), Distance.kilometers(2));
                    test.assertFalse(p1.equals(p2));
                });

                runner.test("with different y", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Distance.miles(5), Distance.kilometers(2));
                    final Point2D p2 = new Point2D(Distance.miles(5), Distance.meters(2));
                    test.assertFalse(p1.equals(p2));
                });

                runner.test("with different x and y", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Distance.miles(5), Distance.kilometers(2));
                    final Point2D p2 = new Point2D(Distance.millimeters(5), Distance.meters(2));
                    test.assertFalse(p1.equals(p2));
                });
            });

            runner.testGroup("hashCode()", () ->
            {
                runner.test("with same", (Test test) ->
                {
                    final Point2D point = new Point2D(Distance.feet(1), Distance.feet(-3));
                    test.assertEqual(point.hashCode(), point.hashCode());
                });

                runner.test("with equal", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Distance.feet(1), Distance.feet(-3));
                    final Point2D p2 = new Point2D(Distance.feet(1), Distance.feet(-3));
                    test.assertEqual(p1.hashCode(), p2.hashCode());
                });

                runner.test("with different", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Distance.feet(1), Distance.feet(-3));
                    final Point2D p2 = new Point2D(Distance.feet(7), Distance.feet(-3));
                    test.assertNotEqual(p1.hashCode(), p2.hashCode());
                });
            });
        });
    }
}
