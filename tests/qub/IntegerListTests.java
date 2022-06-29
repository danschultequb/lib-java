package qub;

public interface IntegerListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IntegerList.class, () ->
        {
            runner.testGroup("createWithCapacity(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> IntegerList.createWithCapacity(-1),
                        new PreConditionFailure("initialCapacity (-1) must be greater than or equal to 0."));
                });

                final Action1<Integer> createWithCapacityTest = (Integer initialCapacity) ->
                {
                    runner.test("with " + initialCapacity, (Test test) ->
                    {
                        final IntegerList list = IntegerList.createWithCapacity(initialCapacity);
                        test.assertNotNull(list);
                        test.assertEqual(0, list.getCount());
                    });
                };

                createWithCapacityTest.run(0);
                createWithCapacityTest.run(1);
                createWithCapacityTest.run(10);
            });

            runner.testGroup("create(int...)", () ->
            {
                runner.test("with null int array", (Test test) ->
                {
                    test.assertThrows(() -> IntegerList.create((int[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final int[] values = new int[0];
                    final IntegerList list = IntegerList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final int[] values = new int[] { 1 };
                    final IntegerList list = IntegerList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(1, list.get(0));
                    list.set(0, 5);
                    test.assertEqual(1, values[0]);
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with non-empty int arguments", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCount());
                    test.assertEqual(1, list.get(0));
                    test.assertEqual(2, list.get(1));
                    test.assertEqual(3, list.get(2));
                });
            });

            runner.testGroup("insert(int,int)", () ->
            {
                final Action4<IntegerList,Integer,Integer,Throwable> insertErrorTest = (IntegerList list, Integer insertIndex, Integer value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, insertIndex, value), (Test test) ->
                    {
                        test.assertThrows(() -> list.insert(insertIndex.intValue(), value.intValue()), expected);
                    });
                };

                insertErrorTest.run(IntegerList.create(), -1, 5, new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                insertErrorTest.run(IntegerList.create(), 1, 5, new PreConditionFailure("insertIndex (1) must be equal to 0."));
                insertErrorTest.run(IntegerList.create(1, 2, 3), 4, 7, new PreConditionFailure("insertIndex (4) must be between 0 and 3."));

                final Action4<IntegerList,Integer,Integer,IntegerList> insertTest = (IntegerList list, Integer insertIndex, Integer value, IntegerList expected) ->
                {
                    runner.test("with " + English.andList(list, insertIndex, value), (Test test) ->
                    {
                        final IntegerList insertResult = list.insert(insertIndex.intValue(), value.intValue());
                        test.assertSame(list, insertResult);
                        test.assertEqual(expected, list);
                    });
                };

                insertTest.run(IntegerList.create(), 0, 5, IntegerList.create(5));
                insertTest.run(IntegerList.create(1, 2, 3), 0, 7, IntegerList.create(7, 1, 2, 3));
                insertTest.run(IntegerList.create(1, 2, 3), 1, 7, IntegerList.create(1, 7, 2, 3));
                insertTest.run(IntegerList.create(1, 2, 3), 2, 7, IntegerList.create(1, 2, 7, 3));
                insertTest.run(IntegerList.create(1, 2, 3), 3, 7, IntegerList.create(1, 2, 3, 7));

                runner.test("with list that won't grow as a result of insertion", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();

                    list.insert(0, 0); // Grow to capacity: 1
                    test.assertEqual(IntegerList.create(0), list);

                    list.insert(1, 1); // Grow to capacity: 3
                    test.assertEqual(IntegerList.create(0, 1), list);

                    list.insert(2, 2); // No growth
                    test.assertEqual(IntegerList.create(0, 1, 2), list);

                    list.insert(0, 3); // Grow to capacity: 7
                    test.assertEqual(IntegerList.create(3, 0, 1, 2), list);

                    list.insert(2, 4);
                    test.assertEqual(IntegerList.create(3, 0, 4, 1, 2), list);
                });
            });

            runner.testGroup("insert(int,Integer)", () ->
            {
                final Action4<IntegerList,Integer,Integer,Throwable> insertErrorTest = (IntegerList list, Integer insertIndex, Integer value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, insertIndex, value), (Test test) ->
                    {
                        test.assertThrows(() -> list.insert(insertIndex.intValue(), value), expected);
                    });
                };

                insertErrorTest.run(IntegerList.create(), -1, 5, new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                insertErrorTest.run(IntegerList.create(), 1, 5, new PreConditionFailure("insertIndex (1) must be equal to 0."));
                insertErrorTest.run(IntegerList.create(1, 2, 3), 4, 7, new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                insertErrorTest.run(IntegerList.create(), 0, null, new PreConditionFailure("value cannot be null."));

                final Action4<IntegerList,Integer,Integer,IntegerList> insertTest = (IntegerList list, Integer insertIndex, Integer value, IntegerList expected) ->
                {
                    runner.test("with " + English.andList(list, insertIndex, value), (Test test) ->
                    {
                        final IntegerList insertResult = list.insert(insertIndex.intValue(), value);
                        test.assertSame(list, insertResult);
                        test.assertEqual(expected, list);
                    });
                };

                insertTest.run(IntegerList.create(), 0, 5, IntegerList.create(5));
                insertTest.run(IntegerList.create(1, 2, 3), 0, 7, IntegerList.create(7, 1, 2, 3));
                insertTest.run(IntegerList.create(1, 2, 3), 2, 7, IntegerList.create(1, 2, 7, 3));
                insertTest.run(IntegerList.create(1, 2, 3), 3, 7, IntegerList.create(1, 2, 3, 7));
            });

            runner.testGroup("add(int)", () ->
            {
                final Action3<IntegerList,Integer,IntegerList> addTest = (IntegerList list, Integer value, IntegerList expected) ->
                {
                    runner.test("with " + English.andList(list, value), (Test test) ->
                    {
                        final IntegerList addResult = list.add(value.intValue());
                        test.assertSame(list, addResult);
                        test.assertEqual(expected, list);
                    });
                };

                addTest.run(IntegerList.create(), 0, IntegerList.create(0));
                addTest.run(IntegerList.create(1), 2, IntegerList.create(1, 2));
                addTest.run(IntegerList.create(3, 4), 5, IntegerList.create(3, 4, 5));
            });

            runner.testGroup("add(Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.add(null),
                        new PreConditionFailure("value cannot be null."));
                    test.assertEqual(IntegerList.create(), list);
                });

                final Action3<IntegerList,Integer,IntegerList> addTest = (IntegerList list, Integer value, IntegerList expected) ->
                {
                    runner.test("with " + English.andList(list, value), (Test test) ->
                    {
                        final IntegerList addResult = list.add(value);
                        test.assertSame(list, addResult);
                        test.assertEqual(expected, list);
                    });
                };

                addTest.run(IntegerList.create(), 0, IntegerList.create(0));
                addTest.run(IntegerList.create(1), 2, IntegerList.create(1, 2));
                addTest.run(IntegerList.create(3, 4), 5, IntegerList.create(3, 4, 5));
            });

            runner.testGroup("addAll(Integer...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.addAll((Integer[])null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no values", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    final IntegerList addAllResult = list.addAll();
                    test.assertSame(list, addAllResult);
                    test.assertEqual(IntegerList.create(), list);
                });

                runner.test("with one value", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    final IntegerList addAllResult = list.addAll(Integer.valueOf(5));
                    test.assertSame(list, addAllResult);
                    test.assertEqual(IntegerList.create(5), list);
                });

                runner.test("with two values", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    final IntegerList addAllResult = list.addAll(Integer.valueOf(5), Integer.valueOf(6));
                    test.assertSame(list, addAllResult);
                    test.assertEqual(IntegerList.create(5, 6), list);
                });
            });

            runner.testGroup("removeAt(int)", () ->
            {
                final Action3<IntegerList,Integer,Throwable> removeAtErrorTest = (IntegerList list, Integer index, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, index), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeAt(index), expected);
                    });
                };

                removeAtErrorTest.run(IntegerList.create(), -1, new PreConditionFailure("this cannot be empty."));
                removeAtErrorTest.run(IntegerList.create(), 0, new PreConditionFailure("this cannot be empty."));
                removeAtErrorTest.run(IntegerList.create(), 1, new PreConditionFailure("this cannot be empty."));
                removeAtErrorTest.run(IntegerList.create(1, 2, 3), -1, new PreConditionFailure("index (-1) must be between 0 and 2."));
                removeAtErrorTest.run(IntegerList.create(1, 2, 3), 3, new PreConditionFailure("index (3) must be between 0 and 2."));
                removeAtErrorTest.run(IntegerList.create(1, 2, 3), 4, new PreConditionFailure("index (4) must be between 0 and 2."));

                final Action4<IntegerList,Integer,Integer,IntegerList> removeAtTest = (IntegerList list, Integer index, Integer expectedResult, IntegerList expectedList) ->
                {
                    runner.test("with " + English.andList(list, index), (Test test) ->
                    {
                        test.assertEqual(expectedResult, list.removeAt(index));
                        test.assertEqual(expectedList, list);
                    });
                };

                removeAtTest.run(IntegerList.create(1, 2, 3), 0, 1, IntegerList.create(2, 3));
                removeAtTest.run(IntegerList.create(1, 2, 3, 4, 5, 6), 1, 2, IntegerList.create(1, 3, 4, 5, 6));
                removeAtTest.run(IntegerList.create(1, 2, 3), 2, 3, IntegerList.create(1, 2));
            });

            runner.testGroup("removeFirst(int)", () ->
            {
                final Action3<IntegerList,Integer,Throwable> removeFirstErrorTest = (IntegerList list, Integer valuesToRemove, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, valuesToRemove), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(valuesToRemove), expected);
                    });
                };

                removeFirstErrorTest.run(IntegerList.create(), -1, new PreConditionFailure("valuesToRemove (-1) must be greater than or equal to 0."));
                removeFirstErrorTest.run(IntegerList.create(1, 2, 3), -1, new PreConditionFailure("valuesToRemove (-1) must be greater than or equal to 0."));

                final Action4<IntegerList,Integer,IntegerArray,IntegerList> removeFirstTest = (IntegerList list, Integer valuesToRemove, IntegerArray expectedResult, IntegerList expectedList) ->
                {
                    runner.test("with " + English.andList(list, valuesToRemove), (Test test) ->
                    {
                        test.assertEqual(expectedResult, list.removeFirst(valuesToRemove).await());
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(IntegerList.create(), 0, IntegerArray.create(), IntegerList.create());
                removeFirstTest.run(IntegerList.create(), 1, IntegerArray.create(), IntegerList.create());
                removeFirstTest.run(IntegerList.create(), 2, IntegerArray.create(), IntegerList.create());
            });

            runner.testGroup("removeFirst(int[])", () ->
            {
                final Action3<IntegerList,int[],Throwable> removeFirstErrorTest = (IntegerList list, int[] outputIntegers, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, outputIntegers), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(outputIntegers), expected);
                    });
                };

                removeFirstErrorTest.run(IntegerList.create(), null, new PreConditionFailure("outputIntegers cannot be null."));

                final Action5<IntegerList,int[],Integer,int[],IntegerList> removeFirstTest = (IntegerList list, int[] outputIntegers, Integer expectedResult, int[] expectedOutputLongs, IntegerList expectedList) ->
                {
                    runner.test("with " + English.andList(list, outputIntegers), (Test test) ->
                    {
                        test.assertEqual(expectedResult, list.removeFirst(outputIntegers).await());
                        test.assertEqual(expectedOutputLongs, outputIntegers);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(IntegerList.create(), new int[] {}, 0, new int[] {}, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10 }, 0, new int[] { 10 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11 }, 0, new int[] { 10, 11 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11, 12 }, 0, new int[] { 10, 11, 12 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] {}, 0, new int[] {}, IntegerList.create(0, 1, 2));
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] { 10 }, 1, new int[] { 0 }, IntegerList.create(1, 2));
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] { 10, 11 }, 2, new int[] { 0, 1 }, IntegerList.create(2));
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] { 10, 11, 12 }, 3, new int[] { 0, 1, 2 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] { 10, 11, 12, 13 }, 3, new int[] { 0, 1, 2, 13 }, IntegerList.create());
            });

            runner.testGroup("removeFirst(int[],int,int)", () ->
            {
                final Action5<IntegerList,int[],Integer,Integer,Throwable> removeFirstErrorTest = (IntegerList list, int[] outputIntegers, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, outputIntegers, startIndex, length), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(outputIntegers, startIndex, length), expected);
                    });
                };

                removeFirstErrorTest.run(IntegerList.create(), null, 0, 0, new PreConditionFailure("outputIntegers cannot be null."));
                removeFirstErrorTest.run(IntegerList.create(), new int[0], -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                removeFirstErrorTest.run(IntegerList.create(), new int[0], 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                removeFirstErrorTest.run(IntegerList.create(), new int[0], 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                removeFirstErrorTest.run(IntegerList.create(), new int[0], 0, 1, new PreConditionFailure("length (1) must be equal to 0."));

                final Action7<IntegerList,int[],Integer,Integer,Integer,int[],IntegerList> removeFirstTest = (IntegerList list, int[] outputIntegers, Integer startIndex, Integer length, Integer expectedResult, int[] expectedOutputLongs, IntegerList expectedList) ->
                {
                    runner.test("with " + English.andList(list, outputIntegers, startIndex, length), (Test test) ->
                    {
                        test.assertEqual(expectedResult, list.removeFirst(outputIntegers, startIndex, length).await());
                        test.assertEqual(expectedOutputLongs, outputIntegers);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(IntegerList.create(), new int[] {}, 0, 0, 0, new int[] {}, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10 }, 0, 0, 0, new int[] { 10 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10 }, 0, 1, 0, new int[] { 10 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11 }, 0, 0, 0, new int[] { 10, 11 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11 }, 0, 1, 0, new int[] { 10, 11 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11 }, 0, 2, 0, new int[] { 10, 11 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11 }, 1, 0, 0, new int[] { 10, 11 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11 }, 1, 1, 0, new int[] { 10, 11 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11, 12 }, 0, 0, 0, new int[] { 10, 11, 12 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11, 12 }, 0, 1, 0, new int[] { 10, 11, 12 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11, 12 }, 0, 2, 0, new int[] { 10, 11, 12 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(), new int[] { 10, 11, 12 }, 0, 3, 0, new int[] { 10, 11, 12 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] {}, 0, 0, 0, new int[] {}, IntegerList.create(0, 1, 2));
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] { 10 }, 0, 0, 0, new int[] { 10 }, IntegerList.create(0, 1, 2));
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] { 10 }, 0, 1, 1, new int[] { 0 }, IntegerList.create(1, 2));
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] { 10, 11 }, 0, 0, 0, new int[] { 10, 11 }, IntegerList.create(0, 1, 2));
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] { 10, 11, 12 }, 0, 3, 3, new int[] { 0, 1, 2 }, IntegerList.create());
                removeFirstTest.run(IntegerList.create(0, 1, 2), new int[] { 10, 11, 12, 13 }, 0, 4, 3, new int[] { 0, 1, 2, 13 }, IntegerList.create());
            });

            runner.testGroup("set(int,Integer)", () ->
            {
                final Action4<IntegerList,Integer,Integer,Throwable> setErrorTest = (IntegerList list, Integer index, Integer value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, index, value), (Test test) ->
                    {
                        test.assertThrows(() -> list.set(index, value), expected);
                    });
                };

                setErrorTest.run(IntegerList.create(1, 2, 3), -1, 5, new PreConditionFailure("index (-1) must be between 0 and 2."));
                setErrorTest.run(IntegerList.create(1, 2, 3), 3, 5, new PreConditionFailure("index (3) must be between 0 and 2."));
                setErrorTest.run(IntegerList.create(1, 2, 3), 4, 5, new PreConditionFailure("index (4) must be between 0 and 2."));
                setErrorTest.run(IntegerList.create(1, 2, 3), 1, null, new PreConditionFailure("value cannot be null."));

                final Action4<IntegerList,Integer,Integer,IntegerList> setTest = (IntegerList list, Integer index, Integer value, IntegerList expected) ->
                {
                    runner.test("with " + English.andList(list, index, value), (Test test) ->
                    {
                        final IntegerList setResult = list.set(index, value);
                        test.assertSame(list, setResult);
                        test.assertEqual(expected, list);
                    });
                };

                setTest.run(IntegerList.create(1, 2, 3), 0, 5, IntegerList.create(5, 2, 3));
                setTest.run(IntegerList.create(1, 2, 3), 2, 5, IntegerList.create(1, 2, 5));
            });

            runner.testGroup("get(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                });

                runner.test("with zero index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertEqual(1, list.get(0));
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertEqual(3, list.get(2));
                });

                runner.test("with count index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(3), new PreConditionFailure("index (3) must be between 0 and 2."));
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(4), new PreConditionFailure("index (4) must be between 0 and 2."));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("[]", IntegerList.create().toString());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual("[11]", IntegerList.create(11).toString());
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual("[11,20]", IntegerList.create(11, 20).toString());
                });
            });
        });
    }
}
