package qub;

public class ByteListTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ByteList.class, () ->
        {

            runner.testGroup("constructor(byte...)", () ->
            {
                runner.test("with null byte array", (Test test) ->
                {
                    final ByteList list = new ByteList((byte[])null);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final ByteList list = new ByteList();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final byte[] values = new byte[0];
                    final ByteList list = new ByteList(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final byte[] values = new byte[] { 1 };
                    final ByteList list = new ByteList(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(1, list.get(0));
                    list.set(0, 5);
                    test.assertEqual(1, values[0]);
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with non-empty byte arguments", (Test test) ->
                {
                    final ByteList list = new ByteList((byte)1, (byte)2, (byte)3);
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCount());
                    test.assertEqual(1, list.get(0));
                    test.assertEqual(2, list.get(1));
                    test.assertEqual(3, list.get(2));
                });
            });

            runner.testGroup("constructor(int...)", () ->
            {
                runner.test("with null int array", (Test test) ->
                {
                    final ByteList list = new ByteList((int[])null);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final ByteList list = new ByteList();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final int[] values = new int[0];
                    final ByteList list = new ByteList(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final int[] values = new int[] { 1 };
                    final ByteList list = new ByteList(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(1, list.get(0));
                    list.set(0, 5);
                    test.assertEqual(1, values[0]);
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with non-empty int arguments", (Test test) ->
                {
                    final ByteList list = new ByteList(1, 2, 3);
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCount());
                    test.assertEqual(1, list.get(0));
                    test.assertEqual(2, list.get(1));
                    test.assertEqual(3, list.get(2));
                });
            });

            runner.testGroup("create(byte...)", () ->
            {
                runner.test("with null byte array", (Test test) ->
                {
                    final ByteList list = ByteList.create((byte[])null);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final byte[] values = new byte[0];
                    final ByteList list = ByteList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final byte[] values = new byte[] { 1 };
                    final ByteList list = ByteList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(1, list.get(0));
                    list.set(0, 5);
                    test.assertEqual(1, values[0]);
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with non-empty byte arguments", (Test test) ->
                {
                    final ByteList list = ByteList.create((byte)1, (byte)2, (byte)3);
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCount());
                    test.assertEqual(1, list.get(0));
                    test.assertEqual(2, list.get(1));
                    test.assertEqual(3, list.get(2));
                });
            });

            runner.testGroup("create(int...)", () ->
            {
                runner.test("with null int array", (Test test) ->
                {
                    final ByteList list = ByteList.create((int[])null);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final int[] values = new int[0];
                    final ByteList list = ByteList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final int[] values = new int[] { 1 };
                    final ByteList list = ByteList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(1, list.get(0));
                    list.set(0, 5);
                    test.assertEqual(1, values[0]);
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with non-empty byte arguments", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCount());
                    test.assertEqual(1, list.get(0));
                    test.assertEqual(2, list.get(1));
                    test.assertEqual(3, list.get(2));
                });
            });

            runner.testGroup("insert(int,int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertThrows(() -> list.insert(-1, 5), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with value less than minimum byte value", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertThrows(() -> list.insert(0, Bytes.minimum - 1), new PreConditionFailure("value (-129) must be between -128 and 127."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with value greater than maximum byte value", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertThrows(() -> list.insert(0, Bytes.maximum + 1), new PreConditionFailure("value (128) must be between -128 and 127."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    list.insert(0, 5);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertThrows(() -> list.insert(1, 5), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.insert(0, 7);
                    test.assertEqual(ByteList.create(7, 1, 2, 3), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.insert(2, (byte)7);
                    test.assertEqual(ByteList.create(1, 2, 7, 3), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.insert(3, (byte)7);
                    test.assertEqual(ByteList.create(1, 2, 3, 7), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.insert(4, (byte)7), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });
            });

            runner.testGroup("insert(int,byte)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertThrows(() -> list.insert(-1, (byte)5), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    list.insert(0, (byte)5);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertThrows(() -> list.insert(1, (byte)5), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.insert(0, (byte)7);
                    test.assertEqual(ByteList.create(7, 1, 2, 3), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.insert(2, (byte)7);
                    test.assertEqual(ByteList.create(1, 2, 7, 3), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.insert(3, (byte)7);
                    test.assertEqual(ByteList.create(1, 2, 3, 7), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.insert(4, (byte)7), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });
            });
        });
    }
}
