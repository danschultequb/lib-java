package qub;

public interface MutableSize2IntegerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutableSize2Integer.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableSize2Integer size = MutableSize2Integer.create();
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
                        test.assertThrows(() -> MutableSize2Integer.create(width, height),
                            expected);
                    });
                };

                createErrorTest.run(-1, 0, new PreConditionFailure("width (-1) must be greater than or equal to 0."));
                createErrorTest.run(1, -2, new PreConditionFailure("height (-2) must be greater than or equal to 0."));

                final Action2<Integer,Integer> createTest = (Integer width, Integer height) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create(width.intValue(), height.intValue());
                        test.assertNotNull(size);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                createTest.run(0, 0);
                createTest.run(1, 2);
            });

            runner.testGroup("setWidth(Integer)", () ->
            {
                final Action2<Integer,Throwable> setWidthErrorTest = (Integer width, Throwable expected) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        test.assertThrows(() -> size.setWidth(width),
                            expected);
                        test.assertEqual(0, size.getWidth());
                        test.assertEqual(0, size.getHeight());
                    });
                };

                setWidthErrorTest.run(null, new PreConditionFailure("width cannot be null."));
                setWidthErrorTest.run(-1, new PreConditionFailure("width (-1) must be greater than or equal to 0."));

                final Action1<Integer> setWidthTest = (Integer width) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        final MutableSize2Integer setWidthResult = size.setWidth(width);
                        test.assertSame(size, setWidthResult);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(0, size.getHeight());
                    });
                };

                setWidthTest.run(1);
                setWidthTest.run(2);
            });

            runner.testGroup("setWidth(int)", () ->
            {
                final Action2<Integer,Throwable> setWidthErrorTest = (Integer width, Throwable expected) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        test.assertThrows(() -> size.setWidth(width.intValue()),
                            expected);
                        test.assertEqual(0, size.getWidth());
                        test.assertEqual(0, size.getHeight());
                    });
                };

                setWidthErrorTest.run(-1, new PreConditionFailure("width (-1) must be greater than or equal to 0."));

                final Action1<Integer> setWidthTest = (Integer width) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        final MutableSize2Integer setWidthResult = size.setWidth(width.intValue());
                        test.assertSame(size, setWidthResult);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(0, size.getHeight());
                    });
                };

                setWidthTest.run(1);
                setWidthTest.run(2);
            });

            runner.testGroup("setHeight(Integer)", () ->
            {
                final Action2<Integer,Throwable> setHeightErrorTest = (Integer height, Throwable expected) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        test.assertThrows(() -> size.setHeight(height),
                            expected);
                        test.assertEqual(0, size.getWidth());
                        test.assertEqual(0, size.getHeight());
                    });
                };

                setHeightErrorTest.run(null, new PreConditionFailure("height cannot be null."));
                setHeightErrorTest.run(-1, new PreConditionFailure("height (-1) must be greater than or equal to 0."));

                final Action1<Integer> setHeightTest = (Integer height) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        final MutableSize2Integer setHeightResult = size.setHeight(height);
                        test.assertSame(size, setHeightResult);
                        test.assertEqual(0, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                setHeightTest.run(1);
                setHeightTest.run(2);
            });

            runner.testGroup("setHeight(int)", () ->
            {
                final Action2<Integer,Throwable> setHeightErrorTest = (Integer height, Throwable expected) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        test.assertThrows(() -> size.setHeight(height.intValue()),
                            expected);
                        test.assertEqual(0, size.getWidth());
                        test.assertEqual(0, size.getHeight());
                    });
                };

                setHeightErrorTest.run(-1, new PreConditionFailure("height (-1) must be greater than or equal to 0."));

                final Action1<Integer> setHeightTest = (Integer height) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        final MutableSize2Integer setHeightResult = size.setHeight(height.intValue());
                        test.assertSame(size, setHeightResult);
                        test.assertEqual(0, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                setHeightTest.run(1);
                setHeightTest.run(2);
            });

            runner.testGroup("set(Integer,Integer)", () ->
            {
                final Action3<Integer,Integer,Throwable> setErrorTest = (Integer width, Integer height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        test.assertThrows(() -> size.set(width, height),
                            expected);
                        test.assertEqual(0, size.getWidth());
                        test.assertEqual(0, size.getHeight());
                    });
                };

                setErrorTest.run(null, 0, new PreConditionFailure("width cannot be null."));
                setErrorTest.run(-1, 0, new PreConditionFailure("width (-1) must be greater than or equal to 0."));
                setErrorTest.run(0, null, new PreConditionFailure("height cannot be null."));
                setErrorTest.run(0, -2, new PreConditionFailure("height (-2) must be greater than or equal to 0."));

                final Action2<Integer,Integer> setTest = (Integer width, Integer height) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        final MutableSize2Integer size = MutableSize2Integer.create();
                        final MutableSize2Integer setHeightResult = size.set(width, height);
                        test.assertSame(size, setHeightResult);
                        test.assertEqual(width, size.getWidth());
                        test.assertEqual(height, size.getHeight());
                    });
                };

                setTest.run(1, 2);
                setTest.run(3, 4);
            });
        });
    }
}
