package qub;

public interface MutablePoint2IntegerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutablePoint2Integer.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutablePoint2Integer point = MutablePoint2Integer.create();
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
                        final MutablePoint2Integer point = MutablePoint2Integer.create(x, y);
                        test.assertNotNull(point);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                createTest.run(1, 2);
            });

            runner.testGroup("setX(Integer)", () ->
            {
                final Action2<MutablePoint2Integer,Integer> setXTest = (MutablePoint2Integer point, Integer x) ->
                {
                    runner.test("with " + English.andList(point, x), (Test test) ->
                    {
                        final int y = point.getY();
                        final MutablePoint2Integer setXResult = point.setX(x);
                        test.assertSame(point, setXResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setXTest.run(MutablePoint2Integer.create(), 1);
                setXTest.run(MutablePoint2Integer.create(1, 2), 3);
                setXTest.run(MutablePoint2Integer.create(), -4);
            });

            runner.testGroup("setX(int)", () ->
            {
                final Action2<MutablePoint2Integer,Integer> setXTest = (MutablePoint2Integer point, Integer x) ->
                {
                    runner.test("with " + English.andList(point, x), (Test test) ->
                    {
                        final int y = point.getY();
                        final MutablePoint2Integer setXResult = point.setX(x.intValue());
                        test.assertSame(point, setXResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setXTest.run(MutablePoint2Integer.create(), 1);
                setXTest.run(MutablePoint2Integer.create(1, 2), 3);
                setXTest.run(MutablePoint2Integer.create(1, 2), -4);
            });

            runner.testGroup("setY(int)", () ->
            {
                final Action2<MutablePoint2Integer,Integer> setYTest = (MutablePoint2Integer point, Integer y) ->
                {
                    runner.test("with " + English.andList(point, y), (Test test) ->
                    {
                        final int x = point.getX();
                        final MutablePoint2Integer setYResult = point.setY(y.intValue());
                        test.assertSame(point, setYResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setYTest.run(MutablePoint2Integer.create(), 1);
                setYTest.run(MutablePoint2Integer.create(1, 2), 3);
                setYTest.run(MutablePoint2Integer.create(1, 2), -4);
            });

            runner.testGroup("setY(Integer)", () ->
            {
                final Action2<MutablePoint2Integer,Integer> setYTest = (MutablePoint2Integer point, Integer y) ->
                {
                    runner.test("with " + English.andList(point, y), (Test test) ->
                    {
                        final int x = point.getX();
                        final MutablePoint2Integer setYResult = point.setY(y);
                        test.assertSame(point, setYResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setYTest.run(MutablePoint2Integer.create(), 1);
                setYTest.run(MutablePoint2Integer.create(1, 2), 3);
                setYTest.run(MutablePoint2Integer.create(1, 2), -4);
            });

            runner.testGroup("set(Integer,Integer)", () ->
            {
                final Action4<MutablePoint2Integer,Integer,Integer,Throwable> setErrorTest = (MutablePoint2Integer point, Integer x, Integer y, Throwable expected) ->
                {
                    runner.test("with " + English.andList(point, x, y), (Test test) ->
                    {
                        final int previousX = point.getX();
                        final int previousY = point.getY();

                        test.assertThrows(() -> point.set(x, y), expected);

                        test.assertEqual(previousX, point.getX());
                        test.assertEqual(previousY, point.getY());
                    });
                };

                setErrorTest.run(MutablePoint2Integer.create(), null, 2, new PreConditionFailure("x cannot be null."));
                setErrorTest.run(MutablePoint2Integer.create(), 1, null, new PreConditionFailure("y cannot be null."));

                final Action3<MutablePoint2Integer,Integer,Integer> setTest = (MutablePoint2Integer point, Integer x, Integer y) ->
                {
                    runner.test("with " + English.andList(point, x, y), (Test test) ->
                    {
                        final MutablePoint2Integer setYResult = point.set(x, y);
                        test.assertSame(point, setYResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setTest.run(MutablePoint2Integer.create(), 1, 2);
                setTest.run(MutablePoint2Integer.create(1, 2), 3, 4);
                setTest.run(MutablePoint2Integer.create(1, 2), -4, -5);
            });

            runner.testGroup("set(int,Integer)", () ->
            {
                final Action4<MutablePoint2Integer,Integer,Integer,Throwable> setErrorTest = (MutablePoint2Integer point, Integer x, Integer y, Throwable expected) ->
                {
                    runner.test("with " + English.andList(point, x, y), (Test test) ->
                    {
                        final int previousX = point.getX();
                        final int previousY = point.getY();

                        test.assertThrows(() -> point.set(x.intValue(), y), expected);

                        test.assertEqual(previousX, point.getX());
                        test.assertEqual(previousY, point.getY());
                    });
                };

                setErrorTest.run(MutablePoint2Integer.create(), 1, null, new PreConditionFailure("y cannot be null."));

                final Action3<MutablePoint2Integer,Integer,Integer> setTest = (MutablePoint2Integer point, Integer x, Integer y) ->
                {
                    runner.test("with " + English.andList(point, x, y), (Test test) ->
                    {
                        final MutablePoint2Integer setYResult = point.set(x.intValue(), y);
                        test.assertSame(point, setYResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setTest.run(MutablePoint2Integer.create(), 1, 2);
                setTest.run(MutablePoint2Integer.create(1, 2), 3, 4);
                setTest.run(MutablePoint2Integer.create(1, 2), -4, -5);
            });

            runner.testGroup("set(Integer,int)", () ->
            {
                final Action4<MutablePoint2Integer,Integer,Integer,Throwable> setErrorTest = (MutablePoint2Integer point, Integer x, Integer y, Throwable expected) ->
                {
                    runner.test("with " + English.andList(point, x, y), (Test test) ->
                    {
                        final int previousX = point.getX();
                        final int previousY = point.getY();

                        test.assertThrows(() -> point.set(x, y.intValue()), expected);

                        test.assertEqual(previousX, point.getX());
                        test.assertEqual(previousY, point.getY());
                    });
                };

                setErrorTest.run(MutablePoint2Integer.create(), null, 2, new PreConditionFailure("x cannot be null."));

                final Action3<MutablePoint2Integer,Integer,Integer> setTest = (MutablePoint2Integer point, Integer x, Integer y) ->
                {
                    runner.test("with " + English.andList(point, x, y), (Test test) ->
                    {
                        final MutablePoint2Integer setYResult = point.set(x, y.intValue());
                        test.assertSame(point, setYResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setTest.run(MutablePoint2Integer.create(), 1, 2);
                setTest.run(MutablePoint2Integer.create(1, 2), 3, 4);
                setTest.run(MutablePoint2Integer.create(1, 2), -4, -5);
            });

            runner.testGroup("set(int,int)", () ->
            {
                final Action3<MutablePoint2Integer,Integer,Integer> setTest = (MutablePoint2Integer point, Integer x, Integer y) ->
                {
                    runner.test("with " + English.andList(point, x, y), (Test test) ->
                    {
                        final MutablePoint2Integer setYResult = point.set(x.intValue(), y.intValue());
                        test.assertSame(point, setYResult);
                        test.assertEqual(x, point.getX());
                        test.assertEqual(y, point.getY());
                    });
                };

                setTest.run(MutablePoint2Integer.create(), 1, 2);
                setTest.run(MutablePoint2Integer.create(1, 2), 3, 4);
                setTest.run(MutablePoint2Integer.create(1, 2), -4, -5);
            });
        });
    }
}
