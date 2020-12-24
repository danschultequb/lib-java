package qub;

public interface ByteListTests
{
    static void test(TestRunner runner)
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
                    final ByteList list = ByteList.createFromBytes((byte[])null);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final byte[] values = new byte[0];
                    final ByteList list = ByteList.createFromBytes(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final byte[] values = new byte[] { 1 };
                    final ByteList list = ByteList.createFromBytes(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(1, list.get(0));
                    list.set(0, 5);
                    test.assertEqual(1, values[0]);
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with non-empty byte arguments", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes((byte)1, (byte)2, (byte)3);
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
                    final ByteList list = ByteList.createFromBytes((int[])null);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final int[] values = new int[0];
                    final ByteList list = ByteList.createFromBytes(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final int[] values = new int[] { 1 };
                    final ByteList list = ByteList.createFromBytes(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(1, list.get(0));
                    list.set(0, 5);
                    test.assertEqual(1, values[0]);
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with non-empty byte arguments", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
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
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.insert(-1, 5), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with value less than minimum byte value", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.insert(0, Bytes.minimum - 1), new PreConditionFailure("value (-129) must be between -128 and 127."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with value greater than maximum byte value", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.insert(0, Bytes.maximum + 1), new PreConditionFailure("value (128) must be between -128 and 127."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    list.insert(0, 5);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.insert(1, 5), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.insert(0, 7);
                    test.assertEqual(ByteList.createFromBytes(7, 1, 2, 3), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.insert(2, (byte)7);
                    test.assertEqual(ByteList.createFromBytes(1, 2, 7, 3), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.insert(3, (byte)7);
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3, 7), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.insert(4, (byte)7), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });
            });

            runner.testGroup("insert(int,byte)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.insert(-1, (byte)5), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    list.insert(0, (byte)5);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.insert(1, (byte)5), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.insert(0, (byte)7);
                    test.assertEqual(ByteList.createFromBytes(7, 1, 2, 3), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.insert(2, (byte)7);
                    test.assertEqual(ByteList.createFromBytes(1, 2, 7, 3), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.insert(3, (byte)7);
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3, 7), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.insert(4, (byte)7), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with index less than count when the list doesn't need to grow", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.add(4); // Initiate growth.
                    list.insert(0, 0);
                    test.assertEqual(ByteList.createFromBytes(0, 1, 2, 3, 4), list);
                });
            });

            runner.testGroup("insert(int,Byte)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.insert(-1, Byte.valueOf((byte)5)), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with null value", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.insert(0, null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    list.insert(0, Byte.valueOf((byte)5));
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.insert(1, Byte.valueOf((byte)5)), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.insert(0, Byte.valueOf((byte)7));
                    test.assertEqual(ByteList.createFromBytes(7, 1, 2, 3), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.insert(2, Byte.valueOf((byte)7));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 7, 3), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.insert(3, Byte.valueOf((byte)7));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3, 7), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.insert(4, Byte.valueOf((byte)7)), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with index less than count when the list doesn't need to grow", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.add(4); // Initiate growth.
                    list.insert(0, Byte.valueOf((byte)0));
                    test.assertEqual(ByteList.createFromBytes(0, 1, 2, 3, 4), list);
                });
            });

            runner.testGroup("removeAt(int)", () ->
            {
                runner.test("with negative index with an empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.removeAt(-1), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(ByteList.createFromBytes(), list);
                });

                runner.test("with 0 index with an empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.removeAt(0), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(ByteList.createFromBytes(), list);
                });

                runner.test("with 1 index with an empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes();
                    test.assertThrows(() -> list.removeAt(1), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(ByteList.createFromBytes(), list);
                });

                runner.test("with negative index with a non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.removeAt(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with 0 index with a non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertEqual(1, list.removeAt(0));
                    test.assertEqual(ByteList.createFromBytes(2, 3), list);
                });

                runner.test("with 1 index with a non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3, 4, 5, 6);
                    test.assertEqual(2, list.removeAt(1));
                    test.assertEqual(ByteList.createFromBytes(1, 3, 4, 5, 6), list);
                });

                runner.test("with count - 1 index with a non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertEqual(3, list.removeAt(list.getCount() - 1));
                    test.assertEqual(ByteList.createFromBytes(1, 2), list);
                });

                runner.test("with count index with a non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.removeAt(list.getCount()), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with count + 1 index with a non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.removeAt(list.getCount() + 1), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });
            });

            runner.testGroup("set(int,byte)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.set(-1, (byte)5), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.set(0, (byte)5);
                    test.assertEqual(ByteList.createFromBytes(5, 2, 3), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.set(2, (byte)5);
                    test.assertEqual(ByteList.createFromBytes(1, 2, 5), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.set(3, (byte)5), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.set(4, (byte)5), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });
            });

            runner.testGroup("set(int,int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.set(-1, 5), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.set(0, 5);
                    test.assertEqual(ByteList.createFromBytes(5, 2, 3), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.set(2, 5);
                    test.assertEqual(ByteList.createFromBytes(1, 2, 5), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.set(3, 5), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.set(4, 5), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });
            });

            runner.testGroup("set(int,Byte)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.set(-1, Byte.valueOf((byte)5)), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.set(0, Byte.valueOf((byte)5));
                    test.assertEqual(ByteList.createFromBytes(5, 2, 3), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    list.set(2, Byte.valueOf((byte)5));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 5), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.set(3, Byte.valueOf((byte)5)), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.set(4, Byte.valueOf((byte)5)), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(ByteList.createFromBytes(1, 2, 3), list);
                });
            });

            runner.testGroup("get(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.get(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                });

                runner.test("with zero index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertEqual(1, list.get(0));
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertEqual(3, list.get(2));
                });

                runner.test("with count index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.get(3), new PreConditionFailure("index (3) must be between 0 and 2."));
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.createFromBytes(1, 2, 3);
                    test.assertThrows(() -> list.get(4), new PreConditionFailure("index (4) must be between 0 and 2."));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("[]", ByteList.createFromBytes().toString());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual("[11]", ByteList.createFromBytes(11).toString());
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual("[11,20]", ByteList.createFromBytes(11, 20).toString());
                });
            });
        });
    }
}
