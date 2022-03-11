package qub;

public interface MutablePoint2DistanceTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutablePoint2Distance.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutablePoint2Distance point = MutablePoint2Distance.create();
                test.assertNotNull(point);
                test.assertEqual(Distance.zero, point.getX());
                test.assertEqual(Distance.zero, point.getY());
            });

            runner.testGroup("create(Distance,Distance)", () ->
            {
                final Action2<Distance,Distance> createTest = (Distance x, Distance y) ->
                {
                    runner.test("with " + English.andList(x, y), (Test test) ->
                    {
                        final MutablePoint2Distance point = MutablePoint2Distance.create(x, y);
                        test.assertNotNull(point);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                createTest.run(Distance.inches(1), Distance.miles(2));
            });

            runner.testGroup("setX(Distance)", () ->
            {
                final Action2<Distance,Throwable> setXErrorTest = (Distance x, Throwable expected) ->
                {
                    runner.test("with " + x, (Test test) ->
                    {
                        final MutablePoint2Distance point = MutablePoint2Distance.create();
                        test.assertThrows(() -> point.setX(x),
                            expected);
                        test.assertEqual(Distance.zero, point.getX());
                        test.assertEqual(Distance.zero, point.getY());
                    });
                };

                setXErrorTest.run(null, new PreConditionFailure("x cannot be null."));

                final Action2<MutablePoint2Distance,Distance> setXTest = (MutablePoint2Distance point, Distance x) ->
                {
                    runner.test("with " + English.andList(point, x), (Test test) ->
                    {
                        final Distance y = point.getY();
                        final MutablePoint2Distance setXResult = point.setX(x);
                        test.assertSame(point, setXResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setXTest.run(MutablePoint2Distance.create(), Distance.centimeters(5));
                setXTest.run(MutablePoint2Distance.create(Distance.meters(1), Distance.kilometers(2)), Distance.millimeters(3));
            });

            runner.testGroup("setY(Distance)", () ->
            {
                final Action2<Distance,Throwable> setYErrorTest = (Distance y, Throwable expected) ->
                {
                    runner.test("with " + y, (Test test) ->
                    {
                        final MutablePoint2Distance point = MutablePoint2Distance.create();
                        test.assertThrows(() -> point.setY(y),
                            expected);
                        test.assertEqual(Distance.zero, point.getX());
                        test.assertEqual(Distance.zero, point.getY());
                    });
                };

                setYErrorTest.run(null, new PreConditionFailure("y cannot be null."));

                final Action2<MutablePoint2Distance,Distance> setYTest = (MutablePoint2Distance point, Distance y) ->
                {
                    runner.test("with " + English.andList(point, y), (Test test) ->
                    {
                        final Distance x = point.getX();
                        final MutablePoint2Distance setYResult = point.setY(y);
                        test.assertSame(point, setYResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setYTest.run(MutablePoint2Distance.create(), Distance.centimeters(5));
                setYTest.run(MutablePoint2Distance.create(Distance.meters(1), Distance.kilometers(2)), Distance.millimeters(3));
            });

            runner.testGroup("set(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> setErrorTest = (Distance x, Distance y, Throwable expected) ->
                {
                    runner.test("with " + English.andList(x, y), (Test test) ->
                    {
                        final MutablePoint2Distance point = Point2Distance.create();
                        test.assertThrows(() -> point.set(x, y), expected);
                        test.assertEqual(Distance.zero, point.getX());
                        test.assertEqual(Distance.zero, point.getY());
                    });
                };

                setErrorTest.run(null, Distance.zero, new PreConditionFailure("x cannot be null."));
                setErrorTest.run(Distance.zero, null, new PreConditionFailure("y cannot be null."));

                final Action2<Distance,Distance> setTest = (Distance x, Distance y) ->
                {
                    runner.test("with " + English.andList(x, y), (Test test) ->
                    {
                        final MutablePoint2Distance point = Point2Distance.create();
                        final MutablePoint2Distance setResult = point.set(x, y);
                        test.assertSame(point, setResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setTest.run(Distance.inches(1), Distance.miles(2));
            });
        });
    }
}
