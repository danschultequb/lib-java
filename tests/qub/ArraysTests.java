package qub;

public class ArraysTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Arrays.class, () ->
        {
            runner.testGroup("createFrom(boolean...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Arrays.createFrom((boolean[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final Array<Boolean> array = Arrays.createFrom(new boolean[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final Array<Boolean> array = Arrays.createFrom(new boolean[] { true });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(true, array.get(0));
                });

                runner.test("with two value array", (Test test) ->
                {
                    final Array<Boolean> array = Arrays.createFrom(new boolean[] { true, false });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(true, array.get(0));
                    test.assertEqual(false, array.get(1));
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<Boolean> array = Arrays.createFrom(true);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(true, array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<Boolean> array = Arrays.createFrom(true, false);
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(true, array.get(0));
                    test.assertEqual(false, array.get(1));
                });
            });

            runner.testGroup("createFrom(byte...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Arrays.createFrom((byte[])null));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final ByteArray array = Arrays.createFrom(new byte[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final ByteArray array = Arrays.createFrom(new byte[] { 12 });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(12, array.get(0).intValue());
                });

                runner.test("with two value array", (Test test) ->
                {
                    final ByteArray array = Arrays.createFrom(new byte[] { 13, 14 });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(13, array.get(0).intValue());
                    test.assertEqual(14, array.get(1).intValue());
                });

                runner.test("with one value", (Test test) ->
                {
                    final ByteArray array = Arrays.createFrom((byte)12);
                    test.assertEqual(1, array.getCount());
                    test.assertEqual(12, array.get(0).intValue());
                });

                runner.test("with two values", (Test test) ->
                {
                    final ByteArray array = Arrays.createFrom((byte)13, (byte)14);
                    test.assertEqual(2, array.getCount());
                    test.assertEqual(13, array.get(0).intValue());
                    test.assertEqual(14, array.get(1).intValue());
                });
            });

            runner.testGroup("createFrom(char...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Arrays.createFrom((char[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final Array<Character> array = Arrays.createFrom(new char[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with one value array", (Test test) ->
                {
                    final Array<Character> array = Arrays.createFrom(new char[] { 'a' });
                    test.assertEqual(1, array.getCount());
                    test.assertEqual('a', array.get(0));
                });

                runner.test("with two value array", (Test test) ->
                {
                    final Array<Character> array = Arrays.createFrom(new char[] { 'b', 'c' });
                    test.assertEqual(2, array.getCount());
                    test.assertEqual('b', array.get(0));
                    test.assertEqual('c', array.get(1));
                });

                runner.test("with one value", (Test test) ->
                {
                    final Array<Character> array = Arrays.createFrom('a');
                    test.assertEqual(1, array.getCount());
                    test.assertEqual('a', array.get(0));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Array<Character> array = Arrays.createFrom('b', 'c');
                    test.assertEqual(2, array.getCount());
                    test.assertEqual('b', array.get(0));
                    test.assertEqual('c', array.get(1));
                });
            });
        });
    }
}
