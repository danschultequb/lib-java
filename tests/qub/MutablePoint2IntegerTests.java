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

            runner.testGroup("setX(int)", () ->
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
            });

            runner.testGroup("setY(int)", () ->
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
            });
        });
    }
}
