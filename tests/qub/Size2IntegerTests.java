package qub;

public interface Size2IntegerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Size2Integer.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableSize2Integer size = Size2Integer.create();
                test.assertNotNull(size);
                test.assertEqual(0, size.getWidth());
                test.assertEqual(0, size.getHeight());
            });

            runner.testGroup("create(int,int)", () ->
            {
                final Action3<Integer,Integer,Throwable> createErrorTest = (Integer width, Integer height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        test.assertThrows(() -> Size2Integer.create(width, height),
                            expected);
                    });
                };

                createErrorTest.run(-1, 0, new PreConditionFailure("width (-1) must be greater than or equal to 0."));
                createErrorTest.run(1, -2, new PreConditionFailure("height (-2) must be greater than or equal to 0."));

                final Action2<Integer,Integer> createTest = (Integer width, Integer height) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final MutableSize2Integer size = Size2Integer.create(width.intValue(), height.intValue());
                        test.assertNotNull(size);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                createTest.run(0, 0);
                createTest.run(1, 2);
            });
        });
    }
}
