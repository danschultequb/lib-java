package qub;

public interface ByteListTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ByteList.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final ByteList list = ByteList.create();
                test.assertNotNull(list);
                test.assertEqual(64, list.getCapacity());
                test.assertEqual(0, list.getCount());
                test.assertEqual(Iterable.create(), list);
            });

            runner.testGroup("createWithCapacity(int)", () ->
            {
                final Action2<Integer,Throwable> createWithCapacityErrorTest = (Integer capacity, Throwable expected) ->
                {
                    runner.test("with " + capacity, (Test test) ->
                    {
                        test.assertThrows(() -> ByteList.createWithCapacity(capacity),
                            expected);
                    });
                };

                createWithCapacityErrorTest.run(-1, new PreConditionFailure("capacity (-1) must be greater than or equal to 0."));

                final Action1<Integer> createWithCapacityTest = (Integer capacity) ->
                {
                    runner.test("with " + capacity, (Test test) ->
                    {
                        final ByteList list = ByteList.createWithCapacity(capacity);
                        test.assertNotNull(list);
                        test.assertEqual(capacity, list.getCapacity());
                        test.assertEqual(0, list.getCount());
                        test.assertEqual(Iterable.create(), list);
                    });
                };

                createWithCapacityTest.run(0);
                createWithCapacityTest.run(1);
                createWithCapacityTest.run(5);
                createWithCapacityTest.run(10);
                createWithCapacityTest.run(100);
            });

            runner.testGroup("create(byte...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> ByteList.create((byte[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with one byte", (Test test) ->
                {
                    final ByteList list = ByteList.create((byte)1);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCapacity());
                    test.assertEqual(1, list.getCount());
                    test.assertEqual((byte)1, list.get(0));
                    test.assertEqual(Iterable.create((byte)1), list);
                });

                runner.test("with two bytes", (Test test) ->
                {
                    final ByteList list = ByteList.create((byte)1, (byte)2);
                    test.assertNotNull(list);
                    test.assertEqual(2, list.getCapacity());
                    test.assertEqual(2, list.getCount());
                    test.assertEqual((byte)1, list.get(0));
                    test.assertEqual((byte)2, list.get(1));
                    test.assertEqual(Iterable.create((byte)1, (byte)2), list);
                });

                runner.test("with three bytes", (Test test) ->
                {
                    final ByteList list = ByteList.create((byte)1, (byte)2, (byte)3);
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCapacity());
                    test.assertEqual(3, list.getCount());
                    test.assertEqual((byte)1, list.get(0));
                    test.assertEqual((byte)2, list.get(1));
                    test.assertEqual((byte)3, list.get(2));
                    test.assertEqual(Iterable.create((byte)1, (byte)2, (byte)3), list);
                });
            });

            runner.testGroup("create(int...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> ByteList.create((int[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with one int", (Test test) ->
                {
                    final ByteList list = ByteList.create(1);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCapacity());
                    test.assertEqual(1, list.getCount());
                    test.assertEqual((byte)1, list.get(0));
                    test.assertEqual(Iterable.create((byte)1), list);
                });

                runner.test("with two ints", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2);
                    test.assertNotNull(list);
                    test.assertEqual(2, list.getCapacity());
                    test.assertEqual(2, list.getCount());
                    test.assertEqual((byte)1, list.get(0));
                    test.assertEqual((byte)2, list.get(1));
                    test.assertEqual(Iterable.create((byte)1, (byte)2), list);
                });

                runner.test("with three ints", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCapacity());
                    test.assertEqual(3, list.getCount());
                    test.assertEqual((byte)1, list.get(0));
                    test.assertEqual((byte)2, list.get(1));
                    test.assertEqual((byte)3, list.get(2));
                    test.assertEqual(Iterable.create((byte)1, (byte)2, (byte)3), list);
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

                runner.test("with index less than count when the list doesn't need to grow", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.add(4); // Initiate growth.
                    list.insert(0, 0);
                    test.assertEqual(ByteList.create(0, 1, 2, 3, 4), list);
                });
            });

            runner.testGroup("insert(int,Byte)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertThrows(() -> list.insert(-1, Byte.valueOf((byte)5)), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with null value", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertThrows(() -> list.insert(0, null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    list.insert(0, Byte.valueOf((byte)5));
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create();
                    test.assertThrows(() -> list.insert(1, Byte.valueOf((byte)5)), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.insert(0, Byte.valueOf((byte)7));
                    test.assertEqual(ByteList.create(7, 1, 2, 3), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.insert(2, Byte.valueOf((byte)7));
                    test.assertEqual(ByteList.create(1, 2, 7, 3), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.insert(3, Byte.valueOf((byte)7));
                    test.assertEqual(ByteList.create(1, 2, 3, 7), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.insert(4, Byte.valueOf((byte)7)), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });

                runner.test("with index less than count when the list doesn't need to grow", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.add(4); // Initiate growth.
                    list.insert(0, Byte.valueOf((byte)0));
                    test.assertEqual(ByteList.create(0, 1, 2, 3, 4), list);
                });
            });

            runner.testGroup("removeAt(int)", () ->
            {
                final Action3<ByteList,Integer,Throwable> removeAtErrorTest = (ByteList list, Integer index, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, index), (Test test) ->
                    {
                        final ByteList backupList = ByteList.create().addAll(list);
                        test.assertThrows(() -> list.removeAt(index),
                            expected);
                        test.assertEqual(backupList, list);
                    });
                };

                removeAtErrorTest.run(ByteList.create(), -1, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtErrorTest.run(ByteList.create(), 0, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtErrorTest.run(ByteList.create(), 1, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtErrorTest.run(ByteList.create(1, 2, 3), -1, new PreConditionFailure("index (-1) must be between 0 and 2."));
                removeAtErrorTest.run(ByteList.create(1, 2, 3), 3, new PreConditionFailure("index (3) must be between 0 and 2."));
                removeAtErrorTest.run(ByteList.create(1, 2, 3), 4, new PreConditionFailure("index (4) must be between 0 and 2."));

                final Action4<ByteList,Integer,Byte,Iterable<Byte>> removeAtTest = (ByteList list, Integer index, Byte expectedResult, Iterable<Byte> expectedList) ->
                {
                    runner.test("with " + English.andList(list, index), (Test test) ->
                    {
                        final Byte removeAtResult = list.removeAt(index);
                        test.assertEqual(expectedResult, removeAtResult);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeAtTest.run(ByteList.create(1, 2, 3), 0, (byte)1, ByteList.create(2, 3));
                removeAtTest.run(ByteList.create(1, 2, 3, 4, 5, 6), 1, (byte)2, ByteList.create(1, 3, 4, 5, 6));
                removeAtTest.run(ByteList.create(1, 2, 3), 2, (byte)3, ByteList.create(1, 2));
            });

            runner.testGroup("removeFirst(int)", () ->
            {
                final Action3<ByteList,Integer,Throwable> removeFirstErrorTest = (ByteList list, Integer valuesToRemove, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, valuesToRemove), (Test test) ->
                    {
                        final Iterable<Byte> backupList = List.create(list);
                        test.assertThrows(() -> list.removeFirst(valuesToRemove).await(),
                            expected);
                        test.assertEqual(backupList, list);
                    });
                };

                removeFirstErrorTest.run(ByteList.create(), -2, new PreConditionFailure("valuesToRemove (-2) must be greater than or equal to 0."));
                removeFirstErrorTest.run(ByteList.create(), 1, new EmptyException());
                removeFirstErrorTest.run(ByteList.create(1, 2, 3), -2, new PreConditionFailure("valuesToRemove (-2) must be greater than or equal to 0."));

                final Action4<ByteList,Integer,ByteArray,ByteList> removeFirstTest = (ByteList list, Integer valuesToRemove, ByteArray expectedResult, ByteList expectedList) ->
                {
                    runner.test("with " + English.andList(list, valuesToRemove), (Test test) ->
                    {
                        final ByteArray removeFirstResult = list.removeFirst(valuesToRemove).await();
                        test.assertEqual(expectedResult, removeFirstResult);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(ByteList.create(), 0, ByteArray.create(), ByteList.create());
                removeFirstTest.run(ByteList.create(1, 2, 3), 0, ByteArray.create(), ByteList.create(1, 2, 3));
                removeFirstTest.run(ByteList.create(1, 2, 3), 1, ByteArray.create(1), ByteList.create(2, 3));
                removeFirstTest.run(ByteList.create(1, 2, 3), 2, ByteArray.create(1, 2), ByteList.create(3));
                removeFirstTest.run(ByteList.create(1, 2, 3), 3, ByteArray.create(1, 2, 3), ByteList.create());
                removeFirstTest.run(ByteList.create(1, 2, 3), 4, ByteArray.create(1, 2, 3), ByteList.create());
            });

            runner.testGroup("removeFirst(byte[])", () ->
            {
                final Action3<ByteList,byte[],Throwable> removeFirstErrorTest = (ByteList list, byte[] outputBytes, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, Array.toString(outputBytes)), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(outputBytes).await(),
                            expected);
                    });
                };

                removeFirstErrorTest.run(
                    ByteList.create(),
                    null,
                    new PreConditionFailure("outputBytes cannot be null."));
                removeFirstErrorTest.run(
                    ByteList.create(),
                    new byte[1],
                    new EmptyException());

                final Action5<ByteList,byte[],Integer,byte[],ByteList> removeFirstTest = (ByteList list, byte[] outputBytes, Integer expectedResult, byte[] expectedOutputBytes, ByteList expectedList) ->
                {
                    runner.test("with " + English.andList(list, Array.toString(outputBytes)), (Test test) ->
                    {
                        final Integer removeFirstResult = list.removeFirst(outputBytes).await();
                        test.assertEqual(expectedResult, removeFirstResult);
                        test.assertEqual(expectedOutputBytes, outputBytes);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(
                    ByteList.create(),
                    new byte[0],
                    0,
                    new byte[0],
                    ByteList.create());
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[0],
                    0,
                    new byte[0],
                    ByteList.create(1, 2, 3));
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[1],
                    1,
                    new byte[] { 1 },
                    ByteList.create(2, 3));
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[2],
                    2,
                    new byte[] { 1, 2 },
                    ByteList.create(3));
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[3],
                    3,
                    new byte[] { 1, 2, 3 },
                    ByteList.create());
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[4],
                    3,
                    new byte[] { 1, 2, 3, '\0' },
                    ByteList.create());
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[5],
                    3,
                    new byte[] { 1, 2, 3, '\0', '\0' },
                    ByteList.create());
            });

            runner.testGroup("removeFirst(byte[],int,int)", () ->
            {
                final Action5<ByteList,byte[],Integer,Integer,Throwable> removeFirstErrorTest = (ByteList list, byte[] outputBytes, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, Array.toString(outputBytes), startIndex, length), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(outputBytes, startIndex, length).await(),
                            expected);
                    });
                };

                removeFirstErrorTest.run(
                    ByteList.create(),
                    null,
                    0,
                    0,
                    new PreConditionFailure("outputBytes cannot be null."));
                removeFirstErrorTest.run(
                    ByteList.create(),
                    new byte[0],
                    -1,
                    0,
                    new PreConditionFailure("startIndex (-1) must be equal to 0."));
                removeFirstErrorTest.run(
                    ByteList.create(),
                    new byte[0],
                    1,
                    0,
                    new PreConditionFailure("startIndex (1) must be equal to 0."));
                removeFirstErrorTest.run(
                    ByteList.create(),
                    new byte[1],
                    1,
                    0,
                    new PreConditionFailure("startIndex (1) must be equal to 0."));
                removeFirstErrorTest.run(
                    ByteList.create(),
                    new byte[2],
                    2,
                    0,
                    new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                removeFirstErrorTest.run(
                    ByteList.create(),
                    new byte[0],
                    0,
                    -1,
                    new PreConditionFailure("length (-1) must be equal to 0."));
                removeFirstErrorTest.run(
                    ByteList.create(),
                    new byte[1],
                    0,
                    2,
                    new PreConditionFailure("length (2) must be between 0 and 1."));
                removeFirstErrorTest.run(
                    ByteList.create(),
                    new byte[1],
                    0,
                    1,
                    new EmptyException());

                final Action7<ByteList,byte[],Integer,Integer,Integer,byte[],ByteList> removeFirstTest = (ByteList list, byte[] outputBytes, Integer startIndex, Integer length, Integer expectedResult, byte[] expectedOutputBytes, ByteList expectedList) ->
                {
                    runner.test("with " + English.andList(list, Array.toString(outputBytes), startIndex, length), (Test test) ->
                    {
                        final Integer removeFirstResult = list.removeFirst(outputBytes, startIndex, length).await();
                        test.assertEqual(expectedResult, removeFirstResult);
                        test.assertEqual(expectedOutputBytes, outputBytes);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(
                    ByteList.create(),
                    new byte[0],
                    0,
                    0,
                    0,
                    new byte[0],
                    ByteList.create());
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[0],
                    0,
                    0,
                    0,
                    new byte[0],
                    ByteList.create(1, 2, 3));
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[1],
                    0,
                    1,
                    1,
                    new byte[] { 1 },
                    ByteList.create(2, 3));
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[2],
                    0,
                    2,
                    2,
                    new byte[] { 1, 2 },
                    ByteList.create(3));
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[3],
                    0,
                    3,
                    3,
                    new byte[] { 1, 2, 3 },
                    ByteList.create());
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[3],
                    1,
                    2,
                    2,
                    new byte[] { '\0', 1, 2 },
                    ByteList.create(3));
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[4],
                    0,
                    4,
                    3,
                    new byte[] { 1, 2, 3, '\0' },
                    ByteList.create());
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[4],
                    1,
                    3,
                    3,
                    new byte[] { '\0', 1, 2, 3 },
                    ByteList.create());
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[5],
                    0,
                    5,
                    3,
                    new byte[] { 1, 2, 3, '\0', '\0' },
                    ByteList.create());
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[5],
                    1,
                    4,
                    3,
                    new byte[] { '\0', 1, 2, 3, '\0' },
                    ByteList.create());
                removeFirstTest.run(
                    ByteList.create(1, 2, 3),
                    new byte[5],
                    2,
                    3,
                    3,
                    new byte[] { '\0', '\0', 1, 2, 3 },
                    ByteList.create());
            });

            runner.testGroup("set(int,byte)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(-1, (byte)5), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.set(0, (byte)5);
                    test.assertEqual(ByteList.create(5, 2, 3), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.set(2, (byte)5);
                    test.assertEqual(ByteList.create(1, 2, 5), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(3, (byte)5), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(4, (byte)5), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });
            });

            runner.testGroup("set(int,int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(-1, 5), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.set(0, 5);
                    test.assertEqual(ByteList.create(5, 2, 3), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.set(2, 5);
                    test.assertEqual(ByteList.create(1, 2, 5), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(3, 5), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(4, 5), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });
            });

            runner.testGroup("set(int,Byte)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(-1, Byte.valueOf((byte)5)), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.set(0, Byte.valueOf((byte)5));
                    test.assertEqual(ByteList.create(5, 2, 3), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    list.set(2, Byte.valueOf((byte)5));
                    test.assertEqual(ByteList.create(1, 2, 5), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(3, Byte.valueOf((byte)5)), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(4, Byte.valueOf((byte)5)), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(ByteList.create(1, 2, 3), list);
                });
            });

            runner.testGroup("get(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                });

                runner.test("with zero index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertEqual(1, list.get(0));
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertEqual(3, list.get(2));
                });

                runner.test("with count index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(3), new PreConditionFailure("index (3) must be between 0 and 2."));
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final ByteList list = ByteList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(4), new PreConditionFailure("index (4) must be between 0 and 2."));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("[]", ByteList.create().toString());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual("[11]", ByteList.create(11).toString());
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual("[11,20]", ByteList.create(11, 20).toString());
                });
            });
        });
    }
}
