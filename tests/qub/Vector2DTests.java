package qub;

public class Vector2DTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Vector2D.class, () ->
        {
            runner.test("zero", (Test test) ->
            {
                test.assertNotNull(Vector2D.zero);
                test.assertEqual(Distance.zero, Vector2D.zero.getX());
                test.assertEqual(Distance.zero, Vector2D.zero.getY());
            });

            runner.testGroup("constructor(Distance,Distance)", () ->
            {
                runner.test("with null x", (Test test) ->
                {
                    test.assertThrows(() -> new Vector2D(null, Distance.zero));
                });

                runner.test("with null y", (Test test) ->
                {
                    test.assertThrows(() -> new Vector2D(Distance.zero, null));
                });

                runner.test("with non-null x and y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(1), Distance.feet(7));
                    test.assertNotNull(vector);
                    test.assertEqual(Distance.inches(1), vector.getX());
                    test.assertEqual(Distance.feet(7), vector.getY());
                });
            });

            runner.testGroup("negate()", () ->
            {
                runner.test("with zero", (Test test) ->
                {
                    test.assertEqual(Vector2D.zero, Vector2D.zero.negate());
                });

                runner.test("with zero x and positive y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.zero, Distance.miles(2));
                    test.assertEqual(new Vector2D(Distance.zero, Distance.miles(-2)), vector.negate());
                });

                runner.test("with zero x and negative y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.zero, Distance.miles(-2));
                    test.assertEqual(new Vector2D(Distance.zero, Distance.miles(2)), vector.negate());
                });

                runner.test("with positive x and zero y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.miles(1), Distance.zero);
                    test.assertEqual(new Vector2D(Distance.miles(-1), Distance.zero), vector.negate());
                });

                runner.test("with negative x and zero y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.miles(-1), Distance.zero);
                    test.assertEqual(new Vector2D(Distance.miles(1), Distance.zero), vector.negate());
                });

                runner.test("with positive x and positive y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.miles(1), Distance.miles(2));
                    test.assertEqual(new Vector2D(Distance.miles(-1), Distance.miles(-2)), vector.negate());
                });

                runner.test("with negative x and positive y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.miles(-1), Distance.miles(2));
                    test.assertEqual(new Vector2D(Distance.miles(1), Distance.miles(-2)), vector.negate());
                });

                runner.test("with positive x and negative y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.miles(1), Distance.miles(-2));
                    test.assertEqual(new Vector2D(Distance.miles(-1), Distance.miles(2)), vector.negate());
                });

                runner.test("with negative x and negative y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.miles(-1), Distance.miles(-2));
                    test.assertEqual(new Vector2D(Distance.miles(1), Distance.miles(2)), vector.negate());
                });
            });

            runner.testGroup("plus(Point2D)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new Vector2D(Distance.inches(1), Distance.miles(2)).plus((Point2D)null));
                });

                runner.test("with zero vector", (Test test) ->
                {
                    final Vector2D vector = Vector2D.zero;
                    final Point2D point = new Point2D(Distance.inches(3), Distance.inches(0.5));
                    final Point2D result = vector.plus(point);
                    test.assertEqual(point, result);
                });

                runner.test("with zero point", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(0.25), Distance.inches(0.1));
                    final Point2D point = Point2D.zero;
                    final Point2D result = vector.plus(point);
                    test.assertEqual(new Point2D(vector.getX(), vector.getY()), result);
                });

                runner.test("with non-zero point and vector", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(6), Distance.inches(1));
                    final Point2D point = new Point2D(Distance.inches(3), Distance.inches(2));
                    final Point2D result = vector.plus(point);
                    test.assertEqual(new Point2D(Distance.inches(9), Distance.inches(3)), result);
                });
            });

            runner.testGroup("plusPoint(Distance,Distance)", () ->
            {
                runner.test("with null x", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(6), Distance.inches(1));
                    test.assertThrows(() -> vector.plusPoint(null, Distance.inches(1)));
                });

                runner.test("with null y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(6), Distance.inches(1));
                    test.assertThrows(() -> vector.plusPoint(Distance.inches(1), null));
                });

                runner.test("with null x and y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(6), Distance.inches(1));
                    test.assertThrows(() -> vector.plusPoint(null, null));
                });

                runner.test("with non-null x and y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(2), Distance.inches(5));
                    final Point2D result = vector.plusPoint(Distance.inches(4), Distance.inches(10));
                    test.assertEqual(new Point2D(Distance.inches(6), Distance.inches(15)), result);
                });
            });

            runner.testGroup("plusVector(Distance,Distance)", () ->
            {
                runner.test("with null x", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(6), Distance.inches(1));
                    test.assertThrows(() -> vector.plusVector(null, Distance.inches(1)));
                });

                runner.test("with null y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(6), Distance.inches(1));
                    test.assertThrows(() -> vector.plusVector(Distance.inches(1), null));
                });

                runner.test("with null x and y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(6), Distance.inches(1));
                    test.assertThrows(() -> vector.plusVector(null, null));
                });

                runner.test("with non-null x and y", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(2), Distance.inches(5));
                    final Vector2D result = vector.plusVector(Distance.inches(4), Distance.inches(10));
                    test.assertEqual(new Vector2D(Distance.inches(6), Distance.inches(15)), result);
                });
            });

            runner.testGroup("plus(Vector2D)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new Vector2D(Distance.inches(1), Distance.miles(2)).plus((Vector2D)null));
                });

                runner.test("with zero lhs", (Test test) ->
                {
                    final Vector2D lhs = Vector2D.zero;
                    final Vector2D rhs = new Vector2D(Distance.inches(3), Distance.inches(0.5));
                    final Vector2D result = lhs.plus(rhs);
                    test.assertEqual(rhs, result);
                });

                runner.test("with zero rhs", (Test test) ->
                {
                    final Vector2D lhs = new Vector2D(Distance.inches(0.25), Distance.inches(0.1));
                    final Vector2D rhs = Vector2D.zero;
                    final Vector2D result = lhs.plus(rhs);
                    test.assertEqual(new Vector2D(lhs.getX(), lhs.getY()), result);
                });

                runner.test("with non-zero point and vector", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.inches(6), Distance.inches(1));
                    final Vector2D point = new Vector2D(Distance.inches(3), Distance.inches(2));
                    final Vector2D result = vector.plus(point);
                    test.assertEqual(new Vector2D(Distance.inches(9), Distance.inches(3)), result);
                });
            });

            runner.test("toString()", (Test test) ->
            {
                test.assertEqual("[3.0 Miles,7.0 Kilometers]", new Vector2D(Distance.miles(3), Distance.kilometers(7)).toString());
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Vector2D.zero.equals((Object)null));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertFalse(Vector2D.zero.equals("test"));
                });

                runner.test("with same", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    test.assertTrue(vector.equals((Object)vector));
                });

                runner.test("with equal", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    final Vector2D v2 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    test.assertTrue(v1.equals((Object)v2));
                });

                runner.test("with different x", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    final Vector2D v2 = new Vector2D(Distance.miles(6), Distance.kilometers(6));
                    test.assertFalse(v1.equals((Object)v2));
                });

                runner.test("with different y", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    final Vector2D v2 = new Vector2D(Distance.miles(5), Distance.kilometers(7));
                    test.assertFalse(v1.equals((Object)v2));
                });

                runner.test("with different x and y", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    final Vector2D v2 = new Vector2D(Distance.miles(15), Distance.kilometers(7));
                    test.assertFalse(v1.equals((Object)v2));
                });
            });

            runner.testGroup("equals(Vector2D)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Vector2D.zero.equals((Vector2D)null));
                });

                runner.test("with same", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    test.assertTrue(vector.equals(vector));
                });

                runner.test("with equal", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    final Vector2D v2 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    test.assertTrue(v1.equals(v2));
                });

                runner.test("with different x", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    final Vector2D v2 = new Vector2D(Distance.miles(6), Distance.kilometers(6));
                    test.assertFalse(v1.equals(v2));
                });

                runner.test("with different y", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    final Vector2D v2 = new Vector2D(Distance.miles(5), Distance.kilometers(7));
                    test.assertFalse(v1.equals(v2));
                });

                runner.test("with different x and y", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.miles(5), Distance.kilometers(6));
                    final Vector2D v2 = new Vector2D(Distance.miles(15), Distance.kilometers(7));
                    test.assertFalse(v1.equals(v2));
                });
            });

            runner.testGroup("hashCode()", () ->
            {
                runner.test("with same", (Test test) ->
                {
                    final Vector2D vector = new Vector2D(Distance.centimeters(3), Distance.millimeters(10));
                    test.assertEqual(vector.hashCode(), vector.hashCode());
                });

                runner.test("with equal", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.centimeters(3), Distance.millimeters(10));
                    final Vector2D v2 = new Vector2D(Distance.centimeters(3), Distance.millimeters(10));
                    test.assertEqual(v1.hashCode(), v2.hashCode());
                });

                runner.test("with different", (Test test) ->
                {
                    final Vector2D v1 = new Vector2D(Distance.centimeters(3), Distance.kilometers(10));
                    final Vector2D v2 = new Vector2D(Distance.millimeters(3), Distance.millimeters(10));
                    test.assertNotEqual(v1.hashCode(), v2.hashCode());
                });
            });
        });
    }
}
