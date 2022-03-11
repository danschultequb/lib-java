package qub;

public interface Size2Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Size2.class, () ->
        {
            runner.testGroup("create(int,int)", () ->
            {
                final Action2<Integer,Integer> createTest = (Integer width, Integer height) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final MutableSize2Integer size = Size2.create(width, height);
                        test.assertNotNull(size);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                createTest.run(1, 2);
            });

            runner.testGroup("create(Distance,Distance)", () ->
            {
                final Action2<Distance,Distance> createTest = (Distance width, Distance height) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final MutableSize2Distance size = Size2.create(width, height);
                        test.assertNotNull(size);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                createTest.run(Distance.inches(1), Distance.centimeters(2));
            });
        });
    }
}
