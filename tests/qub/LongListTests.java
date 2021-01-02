package qub;

public interface LongListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(LongList.class, () ->
        {
            runner.testGroup("createWithCapacity(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> LongList.createWithCapacity(-1),
                        new PreConditionFailure("initialCapacity (-1) must be greater than or equal to 0."));
                });

                final Action1<Integer> createWithCapacityTest = (Integer initialCapacity) ->
                {
                    runner.test("with " + initialCapacity, (Test test) ->
                    {
                        final LongList list = LongList.createWithCapacity(initialCapacity);
                        test.assertNotNull(list);
                        test.assertEqual(0, list.getCount());
                    });
                };

                createWithCapacityTest.run(0);
                createWithCapacityTest.run(1);
                createWithCapacityTest.run(10);
            });

            runner.testGroup("create(long...)", () ->
            {
                runner.test("with null int array", (Test test) ->
                {
                    test.assertThrows(() -> LongList.create((long[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final LongList list = LongList.create();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final long[] values = new long[0];
                    final LongList list = LongList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final long[] values = new long[] { 1 };
                    final LongList list = LongList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(1, list.get(0));
                    list.set(0, 5);
                    test.assertEqual(1, values[0]);
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with non-empty int arguments", (Test test) ->
                {
                    final LongList list = LongList.create(1, 2, 3);
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCount());
                    test.assertEqual(1, list.get(0));
                    test.assertEqual(2, list.get(1));
                    test.assertEqual(3, list.get(2));
                });
            });

            runner.testGroup("insert(int,long)", () ->
            {
                final Action4<LongList,Integer,Long,Throwable> insertErrorTest = (LongList list, Integer insertIndex, Long value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, insertIndex, value), (Test test) ->
                    {
                        test.assertThrows(() -> list.insert(insertIndex.intValue(), value.longValue()), expected);
                    });
                };

                insertErrorTest.run(LongList.create(), -1, 5L, new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                insertErrorTest.run(LongList.create(), 1, 5L, new PreConditionFailure("insertIndex (1) must be equal to 0."));
                insertErrorTest.run(LongList.create(1, 2, 3), 4, 7L, new PreConditionFailure("insertIndex (4) must be between 0 and 3."));

                final Action4<LongList,Integer,Long,LongList> insertTest = (LongList list, Integer insertIndex, Long value, LongList expected) ->
                {
                    runner.test("with " + English.andList(list, insertIndex, value), (Test test) ->
                    {
                        final LongList insertResult = list.insert(insertIndex.intValue(), value.longValue());
                        test.assertSame(list, insertResult);
                        test.assertEqual(expected, list);
                    });
                };

                insertTest.run(LongList.create(), 0, 5L, LongList.create(5));
                insertTest.run(LongList.create(1, 2, 3), 0, 7L, LongList.create(7, 1, 2, 3));
                insertTest.run(LongList.create(1, 2, 3), 1, 7L, LongList.create(1, 7, 2, 3));
                insertTest.run(LongList.create(1, 2, 3), 2, 7L, LongList.create(1, 2, 7, 3));
                insertTest.run(LongList.create(1, 2, 3), 3, 7L, LongList.create(1, 2, 3, 7));

                runner.test("with list that won't grow as a result of insertion", (Test test) ->
                {
                    final LongList list = LongList.create();

                    list.insert(0, 0); // Grow to capacity: 1
                    test.assertEqual(LongList.create(0), list);

                    list.insert(1, 1); // Grow to capacity: 3
                    test.assertEqual(LongList.create(0, 1), list);

                    list.insert(2, 2); // No growth
                    test.assertEqual(LongList.create(0, 1, 2), list);

                    list.insert(0, 3); // Grow to capacity: 7
                    test.assertEqual(LongList.create(3, 0, 1, 2), list);

                    list.insert(2, 4);
                    test.assertEqual(LongList.create(3, 0, 4, 1, 2), list);
                });
            });

            runner.testGroup("insert(int,Long)", () ->
            {
                final Action4<LongList,Integer,Long,Throwable> insertErrorTest = (LongList list, Integer insertIndex, Long value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, insertIndex, value), (Test test) ->
                    {
                        test.assertThrows(() -> list.insert(insertIndex.intValue(), value), expected);
                    });
                };

                insertErrorTest.run(LongList.create(), -1, 5L, new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                insertErrorTest.run(LongList.create(), 1, 5L, new PreConditionFailure("insertIndex (1) must be equal to 0."));
                insertErrorTest.run(LongList.create(1, 2, 3), 4, 7L, new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                insertErrorTest.run(LongList.create(), 0, null, new PreConditionFailure("value cannot be null."));

                final Action4<LongList,Integer,Long,LongList> insertTest = (LongList list, Integer insertIndex, Long value, LongList expected) ->
                {
                    runner.test("with " + English.andList(list, insertIndex, value), (Test test) ->
                    {
                        final LongList insertResult = list.insert(insertIndex.intValue(), value);
                        test.assertSame(list, insertResult);
                        test.assertEqual(expected, list);
                    });
                };

                insertTest.run(LongList.create(), 0, 5L, LongList.create(5));
                insertTest.run(LongList.create(1, 2, 3), 0, 7L, LongList.create(7, 1, 2, 3));
                insertTest.run(LongList.create(1, 2, 3), 2, 7L, LongList.create(1, 2, 7, 3));
                insertTest.run(LongList.create(1, 2, 3), 3, 7L, LongList.create(1, 2, 3, 7));
            });

            runner.testGroup("add(long)", () ->
            {
                final Action3<LongList,Long,LongList> addTest = (LongList list, Long value, LongList expected) ->
                {
                    runner.test("with " + English.andList(list, value), (Test test) ->
                    {
                        final LongList addResult = list.add(value.longValue());
                        test.assertSame(list, addResult);
                        test.assertEqual(expected, list);
                    });
                };

                addTest.run(LongList.create(), 0L, LongList.create(0));
                addTest.run(LongList.create(1), 2L, LongList.create(1, 2));
                addTest.run(LongList.create(3, 4), 5L, LongList.create(3, 4, 5));
            });

            runner.testGroup("add(Long)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LongList list = LongList.create();
                    test.assertThrows(() -> list.add(null),
                        new PreConditionFailure("value cannot be null."));
                    test.assertEqual(LongList.create(), list);
                });

                final Action3<LongList,Long,LongList> addTest = (LongList list, Long value, LongList expected) ->
                {
                    runner.test("with " + English.andList(list, value), (Test test) ->
                    {
                        final LongList addResult = list.add(value);
                        test.assertSame(list, addResult);
                        test.assertEqual(expected, list);
                    });
                };

                addTest.run(LongList.create(), 0L, LongList.create(0));
                addTest.run(LongList.create(1), 2L, LongList.create(1, 2));
                addTest.run(LongList.create(3, 4), 5L, LongList.create(3, 4, 5));
            });

            runner.testGroup("addAll(Long...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final LongList list = LongList.create();
                    test.assertThrows(() -> list.addAll((Long[])null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no values", (Test test) ->
                {
                    final LongList list = LongList.create();
                    final LongList addAllResult = list.addAll();
                    test.assertSame(list, addAllResult);
                    test.assertEqual(LongList.create(), list);
                });

                runner.test("with one value", (Test test) ->
                {
                    final LongList list = LongList.create();
                    final LongList addAllResult = list.addAll(Long.valueOf(5));
                    test.assertSame(list, addAllResult);
                    test.assertEqual(LongList.create(5), list);
                });

                runner.test("with two values", (Test test) ->
                {
                    final LongList list = LongList.create();
                    final LongList addAllResult = list.addAll(Long.valueOf(5), Long.valueOf(6));
                    test.assertSame(list, addAllResult);
                    test.assertEqual(LongList.create(5, 6), list);
                });
            });

            runner.testGroup("removeAt(int)", () ->
            {
                final Action3<LongList,Integer,Throwable> removeAtErrorTest = (LongList list, Integer index, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, index), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeAt(index), expected);
                    });
                };

                removeAtErrorTest.run(LongList.create(), -1, new PreConditionFailure("this cannot be empty."));
                removeAtErrorTest.run(LongList.create(), 0, new PreConditionFailure("this cannot be empty."));
                removeAtErrorTest.run(LongList.create(), 1, new PreConditionFailure("this cannot be empty."));
                removeAtErrorTest.run(LongList.create(1, 2, 3), -1, new PreConditionFailure("index (-1) must be between 0 and 2."));
                removeAtErrorTest.run(LongList.create(1, 2, 3), 3, new PreConditionFailure("index (3) must be between 0 and 2."));
                removeAtErrorTest.run(LongList.create(1, 2, 3), 4, new PreConditionFailure("index (4) must be between 0 and 2."));

                final Action4<LongList,Integer,Long,LongList> removeAtTest = (LongList list, Integer index, Long expectedResult, LongList expectedList) ->
                {
                    runner.test("with " + English.andList(list, index), (Test test) ->
                    {
                        test.assertEqual(expectedResult, list.removeAt(index));
                        test.assertEqual(expectedList, list);
                    });
                };

                removeAtTest.run(LongList.create(1, 2, 3), 0, 1L, LongList.create(2, 3));
                removeAtTest.run(LongList.create(1, 2, 3, 4, 5, 6), 1, 2L, LongList.create(1, 3, 4, 5, 6));
                removeAtTest.run(LongList.create(1, 2, 3), 2, 3L, LongList.create(1, 2));
            });

            runner.testGroup("removeFirst(int)", () ->
            {
                final Action3<LongList,Integer,Throwable> removeFirstErrorTest = (LongList list, Integer valuesToRemove, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, valuesToRemove), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(valuesToRemove), expected);
                    });
                };

                removeFirstErrorTest.run(LongList.create(), -1, new PreConditionFailure("valuesToRemove (-1) must be greater than or equal to 0."));
                removeFirstErrorTest.run(LongList.create(1, 2, 3), -1, new PreConditionFailure("valuesToRemove (-1) must be greater than or equal to 0."));

                final Action4<LongList,Integer,LongArray,LongList> removeFirstTest = (LongList list, Integer valuesToRemove, LongArray expectedResult, LongList expectedList) ->
                {
                    runner.test("with " + English.andList(list, valuesToRemove), (Test test) ->
                    {
                        test.assertEqual(expectedResult, list.removeFirst(valuesToRemove));
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(LongList.create(), 0, LongArray.create(), LongList.create());
                removeFirstTest.run(LongList.create(), 1, LongArray.create(), LongList.create());
                removeFirstTest.run(LongList.create(), 2, LongArray.create(), LongList.create());
            });

            runner.testGroup("removeFirst(long[])", () ->
            {
                final Action3<LongList,long[],Throwable> removeFirstErrorTest = (LongList list, long[] outputLongs, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, outputLongs), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(outputLongs), expected);
                    });
                };

                removeFirstErrorTest.run(LongList.create(), null, new PreConditionFailure("outputLongs cannot be null."));

                final Action5<LongList,long[],Integer,long[],LongList> removeFirstTest = (LongList list, long[] outputLongs, Integer expectedResult, long[] expectedOutputLongs, LongList expectedList) ->
                {
                    runner.test("with " + English.andList(list, outputLongs), (Test test) ->
                    {
                        test.assertEqual(expectedResult, list.removeFirst(outputLongs));
                        test.assertEqual(expectedOutputLongs, outputLongs);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(LongList.create(), new long[] {}, 0, new long[] {}, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10 }, 0, new long[] { 10 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11 }, 0, new long[] { 10, 11 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11, 12 }, 0, new long[] { 10, 11, 12 }, LongList.create());
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] {}, 0, new long[] {}, LongList.create(0, 1, 2));
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] { 10 }, 1, new long[] { 0 }, LongList.create(1, 2));
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] { 10, 11 }, 2, new long[] { 0, 1 }, LongList.create(2));
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] { 10, 11, 12 }, 3, new long[] { 0, 1, 2 }, LongList.create());
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] { 10, 11, 12, 13 }, 3, new long[] { 0, 1, 2, 13 }, LongList.create());
            });

            runner.testGroup("removeFirst(long[],int,int)", () ->
            {
                final Action5<LongList,long[],Integer,Integer,Throwable> removeFirstErrorTest = (LongList list, long[] outputLongs, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, outputLongs, startIndex, length), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(outputLongs, startIndex, length), expected);
                    });
                };

                removeFirstErrorTest.run(LongList.create(), null, 0, 0, new PreConditionFailure("outputLongs cannot be null."));
                removeFirstErrorTest.run(LongList.create(), new long[0], -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                removeFirstErrorTest.run(LongList.create(), new long[0], 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                removeFirstErrorTest.run(LongList.create(), new long[0], 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                removeFirstErrorTest.run(LongList.create(), new long[0], 0, 1, new PreConditionFailure("length (1) must be equal to 0."));

                final Action7<LongList,long[],Integer,Integer,Integer,long[],LongList> removeFirstTest = (LongList list, long[] outputLongs, Integer startIndex, Integer length, Integer expectedResult, long[] expectedOutputLongs, LongList expectedList) ->
                {
                    runner.test("with " + English.andList(list, outputLongs, startIndex, length), (Test test) ->
                    {
                        test.assertEqual(expectedResult, list.removeFirst(outputLongs, startIndex, length));
                        test.assertEqual(expectedOutputLongs, outputLongs);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(LongList.create(), new long[] {}, 0, 0, 0, new long[] {}, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10 }, 0, 0, 0, new long[] { 10 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10 }, 0, 1, 0, new long[] { 10 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11 }, 0, 0, 0, new long[] { 10, 11 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11 }, 0, 1, 0, new long[] { 10, 11 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11 }, 0, 2, 0, new long[] { 10, 11 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11 }, 1, 0, 0, new long[] { 10, 11 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11 }, 1, 1, 0, new long[] { 10, 11 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11, 12 }, 0, 0, 0, new long[] { 10, 11, 12 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11, 12 }, 0, 1, 0, new long[] { 10, 11, 12 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11, 12 }, 0, 2, 0, new long[] { 10, 11, 12 }, LongList.create());
                removeFirstTest.run(LongList.create(), new long[] { 10, 11, 12 }, 0, 3, 0, new long[] { 10, 11, 12 }, LongList.create());
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] {}, 0, 0, 0, new long[] {}, LongList.create(0, 1, 2));
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] { 10 }, 0, 0, 0, new long[] { 10 }, LongList.create(0, 1, 2));
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] { 10 }, 0, 1, 1, new long[] { 0 }, LongList.create(1, 2));
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] { 10, 11 }, 0, 0, 0, new long[] { 10, 11 }, LongList.create(0, 1, 2));
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] { 10, 11, 12 }, 0, 3, 3, new long[] { 0, 1, 2 }, LongList.create());
                removeFirstTest.run(LongList.create(0, 1, 2), new long[] { 10, 11, 12, 13 }, 0, 4, 3, new long[] { 0, 1, 2, 13 }, LongList.create());
            });

            runner.testGroup("set(int,Integer)", () ->
            {
                final Action4<LongList,Integer,Integer,Throwable> setErrorTest = (LongList list, Integer index, Integer value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, index, value), (Test test) ->
                    {
                        test.assertThrows(() -> list.set(index, value), expected);
                    });
                };

                setErrorTest.run(LongList.create(1, 2, 3), -1, 5, new PreConditionFailure("index (-1) must be between 0 and 2."));
                setErrorTest.run(LongList.create(1, 2, 3), 3, 5, new PreConditionFailure("index (3) must be between 0 and 2."));
                setErrorTest.run(LongList.create(1, 2, 3), 4, 5, new PreConditionFailure("index (4) must be between 0 and 2."));
                setErrorTest.run(LongList.create(1, 2, 3), 1, null, new PreConditionFailure("value cannot be null."));

                final Action4<LongList,Integer,Integer,LongList> setTest = (LongList list, Integer index, Integer value, LongList expected) ->
                {
                    runner.test("with " + English.andList(list, index, value), (Test test) ->
                    {
                        final LongList setResult = list.set(index, value);
                        test.assertSame(list, setResult);
                        test.assertEqual(expected, list);
                    });
                };

                setTest.run(LongList.create(1, 2, 3), 0, 5, LongList.create(5, 2, 3));
                setTest.run(LongList.create(1, 2, 3), 2, 5, LongList.create(1, 2, 5));
            });

            runner.testGroup("set(int,Long)", () ->
            {
                final Action4<LongList,Integer,Long,Throwable> setErrorTest = (LongList list, Integer index, Long value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, index, value), (Test test) ->
                    {
                        test.assertThrows(() -> list.set(index, value), expected);
                    });
                };

                setErrorTest.run(LongList.create(1, 2, 3), -1, 5L, new PreConditionFailure("index (-1) must be between 0 and 2."));
                setErrorTest.run(LongList.create(1, 2, 3), 3, 5L, new PreConditionFailure("index (3) must be between 0 and 2."));
                setErrorTest.run(LongList.create(1, 2, 3), 4, 5L, new PreConditionFailure("index (4) must be between 0 and 2."));
                setErrorTest.run(LongList.create(1, 2, 3), 1, null, new PreConditionFailure("value cannot be null."));

                final Action4<LongList,Integer,Long,LongList> setTest = (LongList list, Integer index, Long value, LongList expected) ->
                {
                    runner.test("with " + English.andList(list, index, value), (Test test) ->
                    {
                        final LongList setResult = list.set(index, value);
                        test.assertSame(list, setResult);
                        test.assertEqual(expected, list);
                    });
                };

                setTest.run(LongList.create(1, 2, 3), 0, 5L, LongList.create(5, 2, 3));
                setTest.run(LongList.create(1, 2, 3), 2, 5L, LongList.create(1, 2, 5));
            });

            runner.testGroup("get(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final LongList list = LongList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                });

                runner.test("with zero index", (Test test) ->
                {
                    final LongList list = LongList.create(1, 2, 3);
                    test.assertEqual(1, list.get(0));
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final LongList list = LongList.create(1, 2, 3);
                    test.assertEqual(3, list.get(2));
                });

                runner.test("with count index", (Test test) ->
                {
                    final LongList list = LongList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(3), new PreConditionFailure("index (3) must be between 0 and 2."));
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final LongList list = LongList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(4), new PreConditionFailure("index (4) must be between 0 and 2."));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("[]", LongList.create().toString());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual("[11]", LongList.create(11).toString());
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual("[11,20]", LongList.create(11, 20).toString());
                });
            });
        });
    }
}
