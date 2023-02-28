package qub;

public interface ArraysTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Arrays.class, () ->
        {
            runner.testGroup("any(byte[])", () ->
            {
                final Action2<byte[],Boolean> anyTest = (byte[] values, Boolean expected) ->
                {
                    runner.test("with " + Array.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, Arrays.any(values));
                    });
                };

                anyTest.run(null, false);
                anyTest.run(new byte[0], false);
                anyTest.run(new byte[1], true);
            });

            runner.testGroup("any(T[])", () ->
            {
                final Action2<String[],Boolean> anyTest = (String[] values, Boolean expected) ->
                {
                    runner.test("with " + Array.toString(values), (Test test) ->
                    {
                        test.assertEqual(expected, Arrays.any(values));
                    });
                };

                anyTest.run(null, false);
                anyTest.run(new String[0], false);
                anyTest.run(new String[1], true);
                anyTest.run(new String[] { "a", "b", "c" }, true);
            });

            runner.testGroup("clone(byte[])", () ->
            {
                final Action1<byte[]> cloneTest = (byte[] values) ->
                {
                    runner.test("with " + Array.toString(values), (Test test) ->
                    {
                        final byte[] result = Arrays.clone(values);
                        test.assertEqual(values, result);
                        test.assertNotSame(values, result);
                    });
                };

                cloneTest.run(new byte[] {});
                cloneTest.run(new byte[] { 1 });
                cloneTest.run(new byte[] { 1, 2, 3 });
            });

            runner.testGroup("clone(byte[],int,int)", () ->
            {
                final Action4<byte[],Integer,Integer,byte[]> cloneTest = (byte[] values, Integer startIndex, Integer length, byte[] expected) ->
                {
                    runner.test("with " + English.andList(Array.toString(values), startIndex, length), (Test test) ->
                    {
                        final byte[] result = Arrays.clone(values, startIndex, length);
                        test.assertEqual(expected, result);
                        test.assertNotSame(values, result);
                    });
                };

                cloneTest.run(new byte[] {}, 0, 0, new byte[] {});
                cloneTest.run(new byte[] { 1, 2, 3 }, 0, 0, new byte[] {});
                cloneTest.run(new byte[] { 1, 2, 3 }, 1, 0, new byte[] {});
                cloneTest.run(new byte[] { 1, 2, 3 }, 2, 0, new byte[] {});
                cloneTest.run(new byte[] { 1, 2, 3 }, 0, 1, new byte[] { 1 });
                cloneTest.run(new byte[] { 1, 2, 3 }, 0, 2, new byte[] { 1, 2 });
                cloneTest.run(new byte[] { 1, 2, 3 }, 0, 3, new byte[] { 1, 2, 3 });
                cloneTest.run(new byte[] { 1, 2, 3 }, 1, 1, new byte[] { 2 });
                cloneTest.run(new byte[] { 1, 2, 3 }, 1, 2, new byte[] { 2, 3 });
                cloneTest.run(new byte[] { 1, 2, 3 }, 2, 1, new byte[] { 3 });
            });
        });
    }
}
