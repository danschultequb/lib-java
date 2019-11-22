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
                    test.assertThrows(() -> new Point2D(null, Length.inches(5)), new PreConditionFailure("x cannot be null."));
                });

                runner.test("with null y", (Test test) ->
                {
                    test.assertThrows(() -> new Point2D(Length.meters(2.3), null), new PreConditionFailure("y cannot be null."));
                });

                runner.test("with non-null coordinates", (Test test) ->
                {
                    final Point2D point = new Point2D(Length.inches(1), Length.centimeters(2));
                    test.assertEqual(Length.inches(1), point.getX());
                    test.assertEqual(Length.centimeters(2), point.getY());
                });
            });

            runner.test("toString()", (Test test) ->
            {
                test.assertEqual("(5.0 Inches,3.0 Meters)", new Point2D(Length.inches(5), Length.meters(3)).toString());
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(new Point2D(Length.feet(1), Length.feet(2)).equals((Object)null));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertFalse(new Point2D(Length.feet(1), Length.feet(2)).equals((Object)"test"));
                });

                runner.test("with same", (Test test) ->
                {
                    final Point2D point = new Point2D(Length.inches(1), Length.centimeters(2));
                    test.assertTrue(point.equals((Object)point));
                });

                runner.test("with equal", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Length.miles(5), Length.kilometers(2));
                    final Point2D p2 = new Point2D(Length.miles(5), Length.kilometers(2));
                    test.assertTrue(p1.equals((Object)p2));
                });

                runner.test("with different", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Length.miles(5), Length.kilometers(2));
                    final Point2D p2 = new Point2D(Length.millimeters(5), Length.meters(2));
                    test.assertFalse(p1.equals((Object)p2));
                });
            });

            runner.testGroup("equals(Point2D)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(new Point2D(Length.feet(1), Length.feet(2)).equals((Point2D)null));
                });

                runner.test("with same", (Test test) ->
                {
                    final Point2D point = new Point2D(Length.inches(1), Length.centimeters(2));
                    test.assertTrue(point.equals(point));
                });

                runner.test("with equal", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Length.miles(5), Length.kilometers(2));
                    final Point2D p2 = new Point2D(Length.miles(5), Length.kilometers(2));
                    test.assertTrue(p1.equals(p2));
                });

                runner.test("with different x", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Length.miles(5), Length.kilometers(2));
                    final Point2D p2 = new Point2D(Length.millimeters(5), Length.kilometers(2));
                    test.assertFalse(p1.equals(p2));
                });

                runner.test("with different y", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Length.miles(5), Length.kilometers(2));
                    final Point2D p2 = new Point2D(Length.miles(5), Length.meters(2));
                    test.assertFalse(p1.equals(p2));
                });

                runner.test("with different x and y", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Length.miles(5), Length.kilometers(2));
                    final Point2D p2 = new Point2D(Length.millimeters(5), Length.meters(2));
                    test.assertFalse(p1.equals(p2));
                });
            });

            runner.testGroup("hashCode()", () ->
            {
                runner.test("with same", (Test test) ->
                {
                    final Point2D point = new Point2D(Length.feet(1), Length.feet(-3));
                    test.assertEqual(point.hashCode(), point.hashCode());
                });

                runner.test("with equal", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Length.feet(1), Length.feet(-3));
                    final Point2D p2 = new Point2D(Length.feet(1), Length.feet(-3));
                    test.assertEqual(p1.hashCode(), p2.hashCode());
                });

                runner.test("with different", (Test test) ->
                {
                    final Point2D p1 = new Point2D(Length.feet(1), Length.feet(-3));
                    final Point2D p2 = new Point2D(Length.feet(7), Length.feet(-3));
                    test.assertNotEqual(p1.hashCode(), p2.hashCode());
                });
            });
        });
    }
}
