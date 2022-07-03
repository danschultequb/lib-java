package qub;

public interface IteratorTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Iterator.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final Iterator<Boolean> iterator = Iterator.create();
                IteratorTests.assertIterator(test, iterator, false, null);
                test.assertInstanceOf(iterator, EmptyIterator.class);
            });

            runner.testGroup("createFromBytes(byte...)", () ->
            {
                runner.test("with null byte[]", (Test test) ->
                {
                    test.assertThrows(() -> Iterator.createFromBytes((byte[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                final Action1<byte[]> createFromBytesTest = (byte[] values) ->
                {
                    runner.test("with " + Array.toString(values), (Test test) ->
                    {
                        final Iterator<Byte> iterator = Iterator.createFromBytes(values);
                        IteratorTests.assertIterator(test, iterator, false, null);

                        for (final byte value : values)
                        {
                            test.assertTrue(iterator.next());
                            IteratorTests.assertIterator(test, iterator, true, value);
                        }

                        for (int i = 0; i < 2; ++i)
                        {
                            test.assertFalse(iterator.next());
                            IteratorTests.assertIterator(test, iterator, true, null);
                        }
                    });
                };

                createFromBytesTest.run(new byte[0]);
                createFromBytesTest.run(new byte[] { 0 });
                createFromBytesTest.run(new byte[] { 0, 1, 2, 3 });

                runner.test("with " + English.andList(4, 5, 6), (Test test) ->
                {
                    final Iterator<Byte> iterator = Iterator.createFromBytes((byte)4, (byte)5, (byte)6);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, (byte)4);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, (byte)5);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, (byte)6);

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                    }
                });
            });

            runner.testGroup("create(byte[],int,int)", () ->
            {
                final Action4<byte[],Integer,Integer,Throwable> createErrorTest = (byte[] values, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Array.toString(values), startIndex, length), (Test test) ->
                    {
                        test.assertThrows(() -> Iterator.create(values, startIndex, length),
                            expected);
                    });
                };

                createErrorTest.run(null, 0, 0, new PreConditionFailure("values cannot be null."));
                createErrorTest.run(new byte[0], -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                createErrorTest.run(new byte[0], 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                createErrorTest.run(new byte[] { 0 }, -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                createErrorTest.run(new byte[] { 0 }, 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                createErrorTest.run(new byte[] { 0, 1 }, -1, 0, new PreConditionFailure("startIndex (-1) must be between 0 and 1."));
                createErrorTest.run(new byte[] { 0, 1 }, 2, 0, new PreConditionFailure("startIndex (2) must be between 0 and 1."));

                final Action3<byte[],Integer,Integer> createTest = (byte[] values, Integer startIndex, Integer length) ->
                {
                    runner.test("with " + English.andList(Array.toString(values), startIndex, length), (Test test) ->
                    {
                        final Iterator<Byte> iterator = Iterator.create(values, startIndex, length);
                    });
                };
            });

            runner.testGroup("create(char[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Iterator.create((char[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                final Action1<char[]> createTest = (char[] values) ->
                {
                    runner.test("with " + Array.toString(values), (Test test) ->
                    {
                        final Iterator<Character> iterator = Iterator.create(values);
                        IteratorTests.assertIterator(test, iterator, false, null);

                        for (final char value : values)
                        {
                            test.assertTrue(iterator.next());
                            IteratorTests.assertIterator(test, iterator, true, value);
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(iterator.next());
                            IteratorTests.assertIterator(test, iterator, true, null);
                        }
                    });
                };

                createTest.run(new char[0]);
                createTest.run(new char[] { 'a' });
                createTest.run(new char[] { 'a', 'b', 'c' });
            });

            runner.testGroup("create(int[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Iterator.create((int[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                final Action1<int[]> createTest = (int[] values) ->
                {
                    runner.test("with " + Array.toString(values), (Test test) ->
                    {
                        final Iterator<Integer> iterator = Iterator.create(values);
                        IteratorTests.assertIterator(test, iterator, false, null);

                        for (final int value : values)
                        {
                            test.assertTrue(iterator.next());
                            IteratorTests.assertIterator(test, iterator, true, value);
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(iterator.next());
                            IteratorTests.assertIterator(test, iterator, true, null);
                        }
                    });
                };

                createTest.run(new int[0]);
                createTest.run(new int[] { 10 });
                createTest.run(new int[] { 1990, 134, 23508 });
            });

            runner.testGroup("create(T...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Iterator.create((String[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                final Action1<String[]> createTest = (String[] values) ->
                {
                    runner.test("with " + Array.toString(values), (Test test) ->
                    {
                        final Iterator<String> iterator = Iterator.create(values);
                        IteratorTests.assertIterator(test, iterator, false, null);

                        for (final String value : values)
                        {
                            test.assertTrue(iterator.next());
                            IteratorTests.assertIterator(test, iterator, true, value);
                        }

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertFalse(iterator.next());
                            IteratorTests.assertIterator(test, iterator, true, null);
                        }
                    });
                };

                createTest.run(new String[0]);
                createTest.run(new String[] { "10" });
                createTest.run(new String[] { "1990", "134", "23508" });
            });

            runner.testGroup("create(Action1<IteratorActions<T>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Iterator.create((Action1<IteratorActions<Integer>>)null),
                        new PreConditionFailure("getNextValuesAction cannot be null."));
                });

                runner.test("with empty action", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions) -> {});
                    IteratorTests.assertIterator(test, iterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                    }
                });

                runner.test("with action that returns one value once", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(10);
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions) ->
                    {
                        if (value.hasValue())
                        {
                            actions.returnValue(value.get());
                            value.clear();
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(10, value.get());

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 10);
                    test.assertFalse(value.hasValue());

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertFalse(value.hasValue());
                    }
                });

                runner.test("with action that returns one value multiple times", (Test test) ->
                {
                    final List<Integer> values = List.create(1, 2, 3);
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions) ->
                    {
                        if (values.any())
                        {
                            actions.returnValue(values.removeFirst().await());
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(Iterable.create(1, 2, 3), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    test.assertEqual(Iterable.create(2, 3), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    test.assertEqual(Iterable.create(3), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    test.assertEqual(Iterable.create(), values);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertEqual(Iterable.create(), values);
                    }
                });

                runner.test("with action that returns multiple values once", (Test test) ->
                {
                    final List<Integer> values = List.create(1, 2, 3);
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions) ->
                    {
                        if (values.any())
                        {
                            actions.returnValues(values);
                            values.clear();
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(Iterable.create(1, 2, 3), values);

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        test.assertEqual(Iterable.create(), values);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertEqual(Iterable.create(), values);
                    }
                });

                runner.test("with action that returns multiple values multiple times", (Test test) ->
                {
                    final List<Integer> values = List.create(1, 2, 3, 4, 5, 6, 7);
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions) ->
                    {
                        if (values.any())
                        {
                            actions.returnValues(values.removeFirst(3).await());
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(Iterable.create(1, 2, 3, 4, 5, 6, 7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    test.assertEqual(Iterable.create(4, 5, 6, 7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    test.assertEqual(Iterable.create(4, 5, 6, 7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    test.assertEqual(Iterable.create(4, 5, 6, 7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 4);
                    test.assertEqual(Iterable.create(7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 5);
                    test.assertEqual(Iterable.create(7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 6);
                    test.assertEqual(Iterable.create(7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 7);
                    test.assertEqual(Iterable.create(), values);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertEqual(Iterable.create(), values);
                    }
                });
            });

            runner.testGroup("create(Action2<IteratorActions<T>,Getter<T>>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Iterator.create((Action2<IteratorActions<Integer>,Getter<Integer>>)null),
                        new PreConditionFailure("getNextValuesAction cannot be null."));
                });

                runner.test("with empty action", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions, Getter<Integer> currentValue) -> {});
                    IteratorTests.assertIterator(test, iterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                    }
                });

                runner.test("with action that returns one value once", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(10);
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions, Getter<Integer> currentValue) ->
                    {
                        test.assertNotNull(actions);
                        test.assertNotNull(currentValue);

                        if (value.hasValue())
                        {
                            test.assertFalse(currentValue.hasValue());
                            actions.returnValue(value.get());
                            value.clear();
                        }
                        else
                        {
                            test.assertTrue(currentValue.hasValue());
                            test.assertEqual(10, currentValue.get());
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(10, value.get());

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 10);
                    test.assertFalse(value.hasValue());

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertFalse(value.hasValue());
                    }
                });

                runner.test("with action that returns one value multiple times", (Test test) ->
                {
                    final List<Integer> values = List.create(1, 2, 3);
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions, Getter<Integer> currentValue) ->
                    {
                        test.assertNotNull(actions);
                        test.assertNotNull(currentValue);

                        if (values.any())
                        {
                            if (values.getCount() == 3)
                            {
                                test.assertFalse(currentValue.hasValue());
                            }
                            else
                            {
                                test.assertTrue(currentValue.hasValue());
                                test.assertEqual(values.first() - 1, currentValue.get());
                            }

                            actions.returnValue(values.removeFirst().await());
                        }
                        else
                        {
                            test.assertTrue(currentValue.hasValue());
                            test.assertEqual(3, currentValue.get());
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(Iterable.create(1, 2, 3), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    test.assertEqual(Iterable.create(2, 3), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    test.assertEqual(Iterable.create(3), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    test.assertEqual(Iterable.create(), values);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertEqual(Iterable.create(), values);
                    }
                });

                runner.test("with action that returns multiple values once", (Test test) ->
                {
                    final List<Integer> values = List.create(1, 2, 3);
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions, Getter<Integer> currentValue) ->
                    {
                        test.assertNotNull(actions);
                        test.assertNotNull(currentValue);

                        if (values.any())
                        {
                            if (values.getCount() == 3)
                            {
                                test.assertFalse(currentValue.hasValue());
                            }
                            else
                            {
                                test.assertTrue(currentValue.hasValue());
                                test.assertEqual(values.first() - 1, currentValue.get());
                            }
                            actions.returnValues(values);
                            values.clear();
                        }
                        else
                        {
                            test.assertTrue(currentValue.hasValue());
                            test.assertEqual(3, currentValue.get());
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(Iterable.create(1, 2, 3), values);

                    for (int i = 1; i <= 3; i++)
                    {
                        test.assertTrue(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        test.assertEqual(Iterable.create(), values);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertEqual(Iterable.create(), values);
                    }
                });

                runner.test("with action that returns multiple values multiple times", (Test test) ->
                {
                    final List<Integer> values = List.create(1, 2, 3, 4, 5, 6, 7);
                    final Iterator<Integer> iterator = Iterator.create((IteratorActions<Integer> actions, Getter<Integer> currentValue) ->
                    {
                        test.assertNotNull(actions);
                        test.assertNotNull(currentValue);

                        if (values.any())
                        {
                            if (values.getCount() == 7)
                            {
                                test.assertFalse(currentValue.hasValue());
                            }
                            else
                            {
                                test.assertTrue(currentValue.hasValue());
                                test.assertEqual(values.first() - 1, currentValue.get());
                            }
                            actions.returnValues(values.removeFirst(3).await());
                        }
                        else
                        {
                            test.assertTrue(currentValue.hasValue());
                            test.assertEqual(7, currentValue.get());
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(Iterable.create(1, 2, 3, 4, 5, 6, 7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    test.assertEqual(Iterable.create(4, 5, 6, 7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    test.assertEqual(Iterable.create(4, 5, 6, 7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    test.assertEqual(Iterable.create(4, 5, 6, 7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 4);
                    test.assertEqual(Iterable.create(7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 5);
                    test.assertEqual(Iterable.create(7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 6);
                    test.assertEqual(Iterable.create(7), values);

                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 7);
                    test.assertEqual(Iterable.create(), values);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(iterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertEqual(Iterable.create(), values);
                    }
                });
            });
        });
    }

    public static void test(TestRunner runner, Function2<Integer,Boolean,Iterator<Integer>> createIterator)
    {
        runner.testGroup(Iterator.class, () ->
        {
            runner.testGroup("start()", () ->
            {
                runner.test("with empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    final Iterator<Integer> startResult = iterator.start();
                    test.assertSame(iterator, startResult);
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    final Iterator<Integer> startResult = iterator.start();
                    test.assertSame(iterator, startResult);
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    final Iterator<Integer> startResult = iterator.start();
                    test.assertSame(iterator, startResult);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with non-empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    final Iterator<Integer> startResult = iterator.start();
                    test.assertSame(iterator, startResult);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });
            });

            runner.testGroup("takeCurrent()", () ->
            {
                runner.test("with empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    test.assertThrows(iterator::takeCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    test.assertThrows(iterator::takeCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    test.assertThrows(iterator::takeCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with non-empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, true);
                    final Integer value = iterator.takeCurrent();
                    test.assertEqual(0, value);
                    IteratorTests.assertIterator(test, iterator, true, 1);
                });
            });

            runner.testGroup("any()", () ->
            {
                runner.test("with empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertFalse(iterator.any());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertFalse(iterator.any());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertTrue(iterator.any());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with non-empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertTrue(iterator.any());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with non-empty Iterator at second element", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    test.assertTrue(iterator.any());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                });
            });

            runner.testGroup("getCount()", () ->
            {
                runner.test("with empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(0, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertEqual(0, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertEqual(0, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertEqual(0, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(3, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertEqual(0, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty Iterator at first value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertEqual(3, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertEqual(0, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty Iterator at second value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    test.assertEqual(2, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertEqual(0, iterator.getCount());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });
            });

            runner.testGroup("first()", () ->
            {
                runner.test("with empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.first().await(),
                        new EmptyException());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.first().await(),
                        new EmptyException());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(0, iterator.first().await());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertEqual(0, iterator.first().await());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with non-empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertEqual(0, iterator.getCurrent());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertEqual(0, iterator.first().await());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });
            });

            runner.testGroup("first(Function1<T,Boolean>)", () ->
            {
                runner.test("with empty non-started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.first(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.first(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.first(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with non-empty started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertThrows(() -> iterator.first(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with empty non-started Iterator and non-null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.first(Math::isOdd).await(),
                        new NotFoundException("Could not find a value in this Iterator that matches the provided condition."));
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty started Iterator and non-null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.first(Math::isOdd).await(),
                        new NotFoundException("Could not find a value in this Iterator that matches the provided condition."));
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(1, iterator.first(Math::isOdd).await());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    test.assertEqual(1, iterator.first(Math::isOdd).await());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                });

                runner.test("with non-empty started Iterator and matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertEqual(0, iterator.first(Math::isEven).await());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertEqual(0, iterator.first(Math::isEven).await());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });
            });

            runner.testGroup("last()", () ->
            {
                runner.test("with empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.last().await(),
                        new EmptyException());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.last().await(),
                        new EmptyException());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(2, iterator.last().await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.last().await(),
                        new EmptyException());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertEqual(2, iterator.last().await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.last().await(),
                        new EmptyException());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });
            });

            runner.testGroup("last(Function1<T,Boolean>)", () ->
            {
                runner.test("with empty non-started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.last(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.last(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.last(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with non-empty started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertThrows(() -> iterator.last(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with empty non-started Iterator and condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.last(Math::isOdd).await(),
                        new NotFoundException("Could not find a value in this Iterator that matches the provided condition."));
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty started Iterator and condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.last(Math::isOdd).await(),
                        new NotFoundException("Could not find a value in this Iterator that matches the provided condition."));
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertEqual(1, iterator.last(Math::isOdd).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.last(Math::isOdd).await(),
                        new NotFoundException("Could not find a value in this Iterator that matches the provided condition."));
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty started Iterator and matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertEqual(2, iterator.last(Math::isEven).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertThrows(() -> iterator.last(Math::isEven).await(),
                        new NotFoundException("Could not find a value in this Iterator that matches the provided condition."));
                    IteratorTests.assertIterator(test, iterator, true, null);
                });
            });

            runner.testGroup("contains(T)", () ->
            {
                runner.test("with empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertFalse(iterator.contains(3).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertFalse(iterator.contains(3).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and not found value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertFalse(iterator.contains(3).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and found value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertTrue(iterator.contains(3).await());
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    test.assertTrue(iterator.contains(3).await());
                    IteratorTests.assertIterator(test, iterator, true, 3);
                });

                runner.test("with non-empty started Iterator and not found value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertFalse(iterator.contains(3).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty started Iterator and found value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(20, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertTrue(iterator.contains(12).await());
                    IteratorTests.assertIterator(test, iterator, true, 12);
                    test.assertTrue(iterator.contains(12).await());
                    IteratorTests.assertIterator(test, iterator, true, 12);
                });
            });

            runner.testGroup("contains(Function1<T,Boolean>)", () ->
            {
                runner.test("with null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.contains((Function1<Integer,Boolean>)null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertFalse(iterator.contains((Integer value) -> value > 5).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertFalse(iterator.contains((Integer value) -> value > 5).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and not found value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertFalse(iterator.contains((Integer value) -> value > 5).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and found value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertTrue(iterator.contains((Integer value) -> value > 3).await());
                    IteratorTests.assertIterator(test, iterator, true, 4);
                    test.assertTrue(iterator.contains((Integer value) -> value > 3).await());
                    IteratorTests.assertIterator(test, iterator, true, 4);
                });

                runner.test("with non-empty started Iterator and not found value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertFalse(iterator.contains((Integer value) -> value > 5).await());
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty started Iterator and found value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(20, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertTrue(iterator.contains((Integer value) -> value > 5).await());
                    IteratorTests.assertIterator(test, iterator, true, 6);
                    test.assertTrue(iterator.contains((Integer value) -> value > 5).await());
                    IteratorTests.assertIterator(test, iterator, true, 6);
                });
            });

            runner.testGroup("take(int)", () ->
            {
                runner.test("with empty non-started Iterator and negative toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.take(-3),
                        new PreConditionFailure("toTake (-3) must be greater than or equal to 0."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty non-started Iterator and zero toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> takeIterator = iterator.take(0);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeIterator, false, null);

                    test.assertFalse(takeIterator.next());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeIterator, true, null);
                });

                runner.test("with empty non-started Iterator and positive toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> takeIterator = iterator.take(2);
                    IteratorTests.assertIterator(test, takeIterator, false, null);

                    test.assertFalse(takeIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, takeIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and negative toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    test.assertThrows(() -> iterator.take(-3),
                        new PreConditionFailure("toTake (-3) must be greater than or equal to 0."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with non-empty non-started Iterator and zero toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> takeIterator = iterator.take(0);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeIterator, false, null);

                    test.assertFalse(takeIterator.next());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and positive toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> takeIterator = iterator.take(2);
                    IteratorTests.assertIterator(test, takeIterator, false, null);

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertTrue(takeIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, takeIterator, true, i);
                    }

                    test.assertFalse(takeIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, takeIterator, true, null);
                });

                runner.test("with non-empty started Iterator and negative toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    test.assertThrows(() -> iterator.take(-3),
                        new PreConditionFailure("toTake (-3) must be greater than or equal to 0."));
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with non-empty started Iterator and zero toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> takeIterator = iterator.take(0);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeIterator, true, null);

                    test.assertFalse(takeIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeIterator, true, null);
                });

                runner.test("with non-empty started Iterator and positive but less than Iterator count toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> takeIterator = iterator.take(2);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeIterator, true, 0);

                    for (int i = 1; i < 2; ++i) {
                        test.assertTrue(takeIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, takeIterator, true, i);
                    }

                    test.assertFalse(takeIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, takeIterator, true, null);
                });

                runner.test("with non-empty started Iterator and positive equal to Iterator count toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> takeIterator = iterator.take(5);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeIterator, true, 0);

                    for (int i = 1; i < 5; ++i) {
                        test.assertTrue(takeIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, takeIterator, true, i);
                    }

                    test.assertFalse(takeIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 4);
                    IteratorTests.assertIterator(test, takeIterator, true, null);
                });

                runner.test("with non-empty started Iterator and positive greater than Iterator count toTake value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> takeIterator = iterator.take(100);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeIterator, true, 0);

                    for (int i = 1; i < 5; ++i) {
                        test.assertTrue(takeIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, takeIterator, true, i);
                    }

                    test.assertFalse(takeIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, takeIterator, true, null);
                });
            });

            runner.testGroup("takeUntil(Function1<T,Boolean>)", () ->
            {
                runner.test("with null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.takeUntil(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with condition that is always true with non-started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeUntil((Integer value) ->
                    {
                        conditionCounter.increment();
                        return true;
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeUntilResult, false, null);
                    test.assertEqual(0, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(1, conditionCounter.get());
                });

                runner.test("with condition that is always true with started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeUntil((Integer value) ->
                    {
                        conditionCounter.increment();
                        return true;
                    });
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(1, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(1, conditionCounter.get());
                });

                runner.test("with condition that is always false with non-started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeUntil((Integer value) ->
                    {
                        conditionCounter.increment();
                        return false;
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeUntilResult, false, null);
                    test.assertEqual(0, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(5, conditionCounter.get());
                });

                runner.test("with condition that is always false with started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeUntil((Integer value) ->
                    {
                        conditionCounter.increment();
                        return false;
                    });
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, 0);
                    test.assertEqual(1, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(5, conditionCounter.get());
                });

                runner.test("with real condition with non-started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeUntil((Integer value) ->
                    {
                        conditionCounter.increment();
                        return value >= 3;
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeUntilResult, false, null);
                    test.assertEqual(0, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(0, 1, 2), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(4, conditionCounter.get());
                });

                runner.test("with real condition with started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeUntil((Integer value) ->
                    {
                        conditionCounter.increment();
                        return value >= 3;
                    });
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, 0);
                    test.assertEqual(1, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(0, 1, 2), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(4, conditionCounter.get());
                });
            });

            runner.testGroup("takeWhile(Function1<T,Boolean>)", () ->
            {
                runner.test("with null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.takeUntil(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with condition that is always true with non-started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeWhile((Integer value) ->
                    {
                        conditionCounter.increment();
                        return true;
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeUntilResult, false, null);
                    test.assertEqual(0, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(5, conditionCounter.get());
                });

                runner.test("with condition that is always true with started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeWhile((Integer value) ->
                    {
                        conditionCounter.increment();
                        return true;
                    });
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, 0);
                    test.assertEqual(1, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(5, conditionCounter.get());
                });

                runner.test("with condition that is always false with non-started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeWhile((Integer value) ->
                    {
                        conditionCounter.increment();
                        return false;
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeUntilResult, false, null);
                    test.assertEqual(0, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(1, conditionCounter.get());
                });

                runner.test("with condition that is always false with started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeWhile((Integer value) ->
                    {
                        conditionCounter.increment();
                        return false;
                    });
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(1, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(1, conditionCounter.get());
                });

                runner.test("with real condition with non-started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeWhile((Integer value) ->
                    {
                        conditionCounter.increment();
                        return value < 3;
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, takeUntilResult, false, null);
                    test.assertEqual(0, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(0, 1, 2), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(4, conditionCounter.get());
                });

                runner.test("with real condition with started inner iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);

                    final IntegerValue conditionCounter = IntegerValue.create(0);
                    final Iterator<Integer> takeUntilResult = iterator.takeWhile((Integer value) ->
                    {
                        conditionCounter.increment();
                        return value < 3;
                    });
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, takeUntilResult, true, 0);
                    test.assertEqual(1, conditionCounter.get());

                    final Iterable<Integer> returnedValues = takeUntilResult.toList();
                    test.assertEqual(Iterable.create(0, 1, 2), returnedValues);
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    IteratorTests.assertIterator(test, takeUntilResult, true, null);
                    test.assertEqual(4, conditionCounter.get());
                });
            });

            runner.testGroup("skip(int)", () ->
            {
                runner.test("with empty non-started Iterator and negative toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.skip(-3),
                        new PreConditionFailure("toSkip (-3) must be greater than or equal to 0."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty non-started Iterator and zero toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> skipIterator = iterator.skip(0);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, skipIterator, false, null);

                    test.assertFalse(skipIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                });

                runner.test("with empty non-started Iterator and positive toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> skipIterator = iterator.skip(2);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, skipIterator, false, null);

                    test.assertFalse(skipIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and negative toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.skip(-3),
                        new PreConditionFailure("toSkip (-3) must be greater than or equal to 0."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with non-empty non-started Iterator and zero toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> skipIterator = iterator.skip(0);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, skipIterator, false, null);

                    for (int i = 0; i < 5; ++i)
                    {
                        test.assertTrue(skipIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, skipIterator, true, i);
                    }

                    test.assertFalse(skipIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and positive toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> skipIterator = iterator.skip(2);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, skipIterator, false, null);

                    for (int i = 2; i < 5; ++i)
                    {
                        test.assertTrue(skipIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, skipIterator, true, i);
                    }

                    test.assertFalse(skipIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                });

                runner.test("with non-empty started Iterator and negative toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    test.assertThrows(() -> iterator.skip(-3),
                        new PreConditionFailure("toSkip (-3) must be greater than or equal to 0."));
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with non-empty started Iterator and zero toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> skipIterator = iterator.skip(0);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, skipIterator, true, 0);

                    for (int i = 1; i < 5; ++i)
                    {
                        test.assertTrue(skipIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, skipIterator, true, i);
                    }

                    test.assertFalse(skipIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                });

                runner.test("with non-empty started Iterator and positive less than Iterator count toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> skipIterator = iterator.skip(2);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, skipIterator, true, 2);
                    IteratorTests.assertIterator(test, iterator, true, 2);

                    for (int i = 3; i < 5; ++i)
                    {
                        test.assertTrue(skipIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, skipIterator, true, i);
                    }

                    test.assertFalse(skipIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                });

                runner.test("with non-empty started Iterator and positive equal to Iterator count toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> skipIterator = iterator.skip(5);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                    IteratorTests.assertIterator(test, iterator, true, null);

                    test.assertFalse(skipIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                });

                runner.test("with non-empty started Iterator and positive greater than Iterator count toSkip value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> skipIterator = iterator.skip(100);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                    IteratorTests.assertIterator(test, iterator, true, null);

                    test.assertFalse(skipIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipIterator, true, null);
                });
            });

            runner.testGroup("skipUntil(Function1<T,Boolean>)", () ->
            {
                runner.test("with empty non-started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.skipUntil(null),
                        new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty non-started Iterator and condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, skipUntilIterator, false, null);

                    test.assertFalse(skipUntilIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipUntilIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.skipUntil(null), new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with non-empty non-started Iterator and non-matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(6, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Comparer.equal(20));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, skipUntilIterator, false, null);

                    test.assertFalse(skipUntilIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipUntilIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, skipUntilIterator, false, null);

                    for (int i = 1; i < 5; ++i)
                    {
                        test.assertTrue(skipUntilIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, skipUntilIterator, true, i);
                    }

                    test.assertFalse(skipUntilIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipUntilIterator, true, null);
                });

                runner.test("with non-empty started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    test.assertThrows(() -> iterator.skipUntil(null), new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with non-empty started Iterator and non-matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, skipUntilIterator, true, 1);
                    IteratorTests.assertIterator(test, iterator, true, 1);

                    test.assertTrue(skipUntilIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, skipUntilIterator, true, 2);

                    test.assertFalse(skipUntilIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipUntilIterator, true, null);
                });

                runner.test("with non-empty started Iterator and matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> skipUntilIterator = iterator.skipUntil(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, skipUntilIterator, true, 1);
                    IteratorTests.assertIterator(test, iterator, true, 1);

                    for (int i = 2; i < 5; ++i)
                    {
                        test.assertTrue(skipUntilIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, skipUntilIterator, true, i);
                    }

                    test.assertFalse(skipUntilIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, skipUntilIterator, true, null);
                });
            });

            runner.testGroup("where()", () ->
            {
                runner.test("with empty non-started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.where(null), new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty non-started Iterator and condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> whereIterator = iterator.where(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, whereIterator, false, null);

                    test.assertFalse(whereIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, whereIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.where(null), new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with non-empty non-started Iterator and non-matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(6, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> whereIterator = iterator.where(Comparer.equal(20));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, whereIterator, false, null);

                    test.assertFalse(whereIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, whereIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> whereIterator = iterator.where(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, whereIterator, false, null);

                    for (int i = 1; i < 5; i += 2)
                    {
                        test.assertTrue(whereIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, whereIterator, true, i);
                    }

                    test.assertFalse(whereIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, whereIterator, true, null);
                });

                runner.test("with non-empty started Iterator and null condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    test.assertThrows(() -> iterator.where(null), new PreConditionFailure("condition cannot be null."));
                    IteratorTests.assertIterator(test, iterator, true, 0);
                });

                runner.test("with non-empty started Iterator and non-matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> whereIterator = iterator.where(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, whereIterator, true, 1);
                    IteratorTests.assertIterator(test, iterator, true, 1);

                    test.assertFalse(whereIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, whereIterator, true, null);
                });

                runner.test("with started non-empty Iterator and matching condition", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> whereIterator = iterator.where(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, whereIterator, true, 1);
                    IteratorTests.assertIterator(test, iterator, true, 1);

                    test.assertTrue(whereIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 3);
                    IteratorTests.assertIterator(test, whereIterator, true, 3);

                    test.assertFalse(whereIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, whereIterator, true, null);
                });
            });

            runner.testGroup("map()", () ->
            {
                runner.test("with empty non-started iterator and null conversion", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.map(null), new PreConditionFailure("conversion cannot be null."));
                });

                runner.test("with empty non-started Iterator and conversion", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Boolean> mapIterator = iterator.map(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, mapIterator, false, null);

                    test.assertFalse(mapIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, mapIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and null conversion", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.map(null), new PreConditionFailure("conversion cannot be null."));
                });

                runner.test("with non-empty non-started Iterator and conversion", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Boolean> mapIterator = iterator.map(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, mapIterator, false, null);

                    for (int i = 0; i < 5; ++i)
                    {
                        test.assertTrue(mapIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, mapIterator, true, i % 2 == 1);
                    }

                    test.assertFalse(mapIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, mapIterator, true, null);
                });

                runner.test("with non-empty started Iterator and null conversion", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    test.assertThrows(() -> iterator.map(null), new PreConditionFailure("conversion cannot be null."));
                });

                runner.test("with non-empty started Iterator and conversion", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Boolean> mapIterator = iterator.map(Math::isOdd);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, mapIterator, true, false);

                    for (int i = 1; i < 5; ++i)
                    {
                        test.assertTrue(mapIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, mapIterator, true, i % 2 == 1);
                    }

                    test.assertFalse(mapIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, mapIterator, true, null);
                });
            });

            runner.testGroup("instanceOf()", () ->
            {
                runner.test("with empty non-started Iterator and null type", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.instanceOf(null), new PreConditionFailure("type cannot be null."));
                });

                runner.test("with empty non-started Iterator and type", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Boolean> instanceOfIterator = iterator.instanceOf(Boolean.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, instanceOfIterator, false, null);

                    test.assertFalse(instanceOfIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, instanceOfIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and null type", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    test.assertThrows(() -> iterator.instanceOf(null), new PreConditionFailure("type cannot be null."));
                });

                runner.test("with non-empty non-started Iterator and no isMatch", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Double> instanceOfIterator = iterator.instanceOf(Double.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, instanceOfIterator, false, null);

                    test.assertFalse(instanceOfIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, instanceOfIterator, true, null);
                });

                runner.test("with non-empty non-started Iterator and isMatch", (Test test) ->
                {
                    final Iterator<Number> iterator = createIterator.run(5, false).map((Integer value) -> (Number)value);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    final Iterator<Integer> instanceOfIterator = iterator.instanceOf(Integer.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, instanceOfIterator, false, null);

                    for (int i = 0; i < 5; ++i)
                    {
                        test.assertTrue(instanceOfIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, instanceOfIterator, true, i);
                    }

                    test.assertFalse(instanceOfIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, instanceOfIterator, true, null);
                });

                runner.test("with non-empty started Iterator and null type", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    test.assertThrows(() -> iterator.instanceOf(null), new PreConditionFailure("type cannot be null."));
                });

                runner.test("with non-empty started Iterator and no isMatch", (Test test) ->
                {
                    final Iterator<Number> iterator = createIterator.run(5, true).map((Integer value) -> (Number)value);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Float> instanceOfIterator = iterator.instanceOf(Float.class);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, instanceOfIterator, true, null);
                    IteratorTests.assertIterator(test, iterator, true, null);

                    test.assertFalse(instanceOfIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, instanceOfIterator, true, null);
                });

                runner.test("with non-empty started Iterator and isMatch", (Test test) ->
                {
                    final Iterator<Number> iterator = createIterator.run(5, true).map((Integer value) -> (Number)value);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    final Iterator<Integer> instanceOfIterator = iterator.instanceOf(Integer.class);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, instanceOfIterator, true, 0);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    for (int i = 1; i < 5; ++i)
                    {
                        test.assertTrue(instanceOfIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, instanceOfIterator, true, i);
                    }

                    test.assertFalse(instanceOfIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, instanceOfIterator, true, null);
                });
            });

            runner.testGroup("for each", () ->
            {
                runner.test("with empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    int elementCount = 0;
                    for (final Integer ignored : iterator)
                    {
                        ++elementCount;
                    }
                    IteratorTests.assertIterator(test, iterator, true, null);

                    test.assertEqual(0, elementCount);
                });

                runner.test("with empty started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    IteratorTests.assertIterator(test, iterator, true, null);

                    int elementCount = 0;
                    for (final Integer ignored : iterator)
                    {
                        ++elementCount;
                    }
                    IteratorTests.assertIterator(test, iterator, true, null);

                    test.assertEqual(0, elementCount);
                });

                runner.test("with non-empty non-started Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, false);
                    IteratorTests.assertIterator(test, iterator, false, null);

                    int i = 0;
                    for (final Integer element : iterator)
                    {
                        test.assertEqual(i, element);
                        ++i;
                    }
                    IteratorTests.assertIterator(test, iterator, true, null);

                    test.assertEqual(10, i);
                });

                runner.test("with non-empty started Iterator at first value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);

                    int i = 0;
                    for (final Integer element : iterator)
                    {
                        test.assertEqual(i, element);
                        ++i;
                    }
                    IteratorTests.assertIterator(test, iterator, true, null);

                    test.assertEqual(10, i);
                });

                runner.test("with non-empty started Iterator at second value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, true);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    test.assertTrue(iterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 1);

                    int i = 0;
                    for (final Integer element : iterator)
                    {
                        test.assertEqual(i + 1, element);
                        ++i;
                    }
                    IteratorTests.assertIterator(test, iterator, true, null);

                    test.assertEqual(9, i);
                });
            });

            runner.testGroup("toArray()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        test.assertEqual(Array.create(), iterator.toArray());
                    }
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    test.assertEqual(Array.create(0), iterator.toArray());
                });

                runner.test("with two values", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    test.assertEqual(Array.create(0, 1), iterator.toArray());
                });
            });

            runner.testGroup("toList()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        test.assertEqual(List.create(), iterator.toList());
                    }
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    test.assertEqual(List.create(0), iterator.toList());
                });

                runner.test("with two values", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    test.assertEqual(List.create(0, 1), iterator.toList());
                });
            });

            runner.testGroup("toSet()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        test.assertEqual(Set.create(), iterator.toSet());
                    }
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    test.assertEqual(Set.create(0), iterator.toSet());
                });

                runner.test("with two values", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    test.assertEqual(Set.create(0, 1), iterator.toSet());
                });

                runner.test("with repeated values", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, false)
                        .map((Integer value) -> value % 3);
                    test.assertEqual(Set.create(0, 1, 2), iterator.toSet());
                });
            });

            runner.testGroup("toMap(Function1<T,K>,Function1<T,V>)", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        test.assertEqual(
                            Map.<Integer,String>create(),
                            iterator.toMap(i -> i, Integers::toString));
                    }
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    test.assertEqual(
                        ListMap.create(MapEntry.create(0, "0")),
                        iterator.toMap(i -> i, Integers::toString));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    test.assertEqual(
                        ListMap.create(
                            MapEntry.create("0", 0),
                            MapEntry.create("1", 1)),
                        iterator.toMap(Integers::toString, i -> i));
                });

                runner.test("with repeated values", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, false);
                    test.assertEqual(
                        ListMap.create(
                            MapEntry.create(0, 9),
                            MapEntry.create(1, 7),
                            MapEntry.create(2, 8)),
                        iterator.toMap(i -> i % 3, i -> i));
                });
            });

            runner.test("customize()", (Test test) ->
            {
                final Iterator<Integer> iterator = createIterator.run(10, false);
                final CustomIterator<Integer> customIterator = iterator.customize();
                test.assertNotSame(iterator, customIterator);
                IteratorTests.assertIterator(test, iterator, false, null);
                IteratorTests.assertIterator(test, customIterator, false, null);

                for (int i = 0; i < 10; i++)
                {
                    test.assertTrue(customIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, i);
                    IteratorTests.assertIterator(test, customIterator, true, i);
                }

                for (int i = 0; i < 2; i++)
                {
                    test.assertFalse(customIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, customIterator, true, null);
                }
            });

            runner.testGroup("customize(Function1<Iterator<T>,Boolean>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, false);
                    test.assertThrows(() -> iterator.customize(null),
                        new PreConditionFailure("nextFunction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with function that doesn't invoke innerIterator.next() and returns false", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, false);
                    final CustomIterator<Integer> customIterator = iterator.customize((Iterator<Integer> innerIterator) ->
                    {
                        return false;
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, customIterator, false, null);

                    test.assertFalse(customIterator.next());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, customIterator, false, null);
                });

                runner.test("with function that doesn't invoke innerIterator.next() and returns true", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, false);
                    final CustomIterator<Integer> customIterator = iterator.customize((Iterator<Integer> innerIterator) ->
                    {
                        return true;
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, customIterator, false, null);

                    test.assertTrue(customIterator.next());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, customIterator, false, null);
                });

                runner.test("with function that invokes innerIterator.next() and returns the result", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, false);
                    final CustomIterator<Integer> customIterator = iterator.customize((Iterator<Integer> innerIterator) ->
                    {
                        return innerIterator.next();
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, customIterator, false, null);

                    for (int i = 0; i < 10; i++)
                    {
                        test.assertTrue(customIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, customIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(customIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, customIterator, true, null);
                    }
                });

                runner.test("with function that invokes innerIterator.next() and then throws an exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(10, false);
                    final CustomIterator<Integer> customIterator = iterator.customize((Iterator<Integer> innerIterator) ->
                    {
                        innerIterator.next();
                        throw new ParseException("hello");
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, customIterator, false, null);

                    for (int i = 0; i < 10; i++)
                    {
                        test.assertThrows(() -> customIterator.next(),
                            new ParseException("hello"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, customIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> customIterator.next(),
                            new ParseException("hello"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, customIterator, true, null);
                    }
                });
            });

            runner.testGroup("onValue(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.onValue((Action0)null),
                        new PreConditionFailure("onValueAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final IntegerValue counter = IntegerValue.create(0);
                        final Iterator<Integer> onValueIterator = iterator.onValue(() -> { counter.increment(); });
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, onValueIterator, false, null);

                        test.assertFalse(onValueIterator.next());

                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onValueIterator = iterator.onValue(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    test.assertTrue(onValueIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onValueIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onValueIterator = iterator.onValue(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(onValueIterator.next());
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(2, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with action that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onValueIterator = iterator.onValue(() ->
                    {
                        counter.increment();
                        throw new ParseException("hello");
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onValueIterator.next(),
                            new ParseException("hello"));
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(3, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with action that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onValueIterator = iterator.onValue(() ->
                    {
                        counter.increment();
                        if (Math.isOdd(counter.get()))
                        {
                            throw new ParseException("hello");
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        if (Math.isEven(i))
                        {
                            test.assertThrows(() -> onValueIterator.next(),
                                new ParseException("hello"));
                        }
                        else
                        {
                            test.assertTrue(onValueIterator.next());
                        }

                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(3, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false)
                        .customize((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onValueIterator = iterator.onValue(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onValueIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onValueIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onValueIterator = iterator.onValue(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        if (Math.isOdd(i))
                        {
                            test.assertThrows(() -> onValueIterator.next(),
                                new ParseException("hello there"));
                        }
                        else
                        {
                            test.assertTrue(onValueIterator.next());
                        }

                        test.assertEqual((i / 2) + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(2, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });
            });

            runner.testGroup("onValue(Action1<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.onValue((Action1<Integer>)null),
                        new PreConditionFailure("onValueAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final List<Integer> values = List.create();
                        final Iterator<Integer> onValueIterator = iterator.onValue((Integer currentValue) -> values.add(currentValue));
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, onValueIterator, false, null);

                        test.assertFalse(onValueIterator.next());

                        test.assertEqual(Iterable.create(), values);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final List<Integer> values = List.create();
                    final Iterator<Integer> onValueIterator = iterator.onValue((Integer currentValue) -> { values.add(currentValue); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    test.assertTrue(onValueIterator.next());
                    test.assertEqual(Iterable.create(0), values);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onValueIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(Iterable.create(0), values);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final List<Integer> values = List.create();
                    final Iterator<Integer> onValueIterator = iterator.onValue((Integer currentValue) -> { values.add(currentValue); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(onValueIterator.next());
                        final List<Integer> expectedValues = List.create();
                        for (int j = 0; j <= i; j++)
                        {
                            expectedValues.add(j);
                        }
                        test.assertEqual(expectedValues, values);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(Iterable.create(0, 1), values);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with action that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final List<Integer> values = List.create();
                    final Iterator<Integer> onValueIterator = iterator.onValue((Integer currentValue) ->
                    {
                        values.add(currentValue);
                        throw new ParseException("hello");
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onValueIterator.next(),
                            new ParseException("hello"));
                        final List<Integer> expectedValues = List.create();
                        for (int j = 0; j <= i; j++)
                        {
                            expectedValues.add(j);
                        }
                        test.assertEqual(expectedValues, values);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(Iterable.create(0, 1, 2), values);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with action that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final List<Integer> values = List.create();
                    final Iterator<Integer> onValueIterator = iterator.onValue((Integer currentValue) ->
                    {
                        values.add(currentValue);
                        if (Math.isOdd(values.getCount()))
                        {
                            throw new ParseException("hello");
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        if (Math.isEven(i))
                        {
                            test.assertThrows(() -> onValueIterator.next(),
                                new ParseException("hello"));
                        }
                        else
                        {
                            test.assertTrue(onValueIterator.next());
                        }

                        final List<Integer> expectedValues = List.create();
                        for (int j = 0; j <= i; j++)
                        {
                            expectedValues.add(j);
                        }
                        test.assertEqual(expectedValues, values);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(Iterable.create(0, 1, 2), values);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final List<Integer> values = List.create();
                    final Iterator<Integer> onValueIterator = iterator.onValue((Integer currentValue) -> { values.add(currentValue); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onValueIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(Iterable.create(), values);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onValueIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(Iterable.create(), values);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final List<Integer> values = List.create();
                    final Iterator<Integer> onValueIterator = iterator.onValue((Integer currentValue) -> { values.add(currentValue); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onValueIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        if (Math.isOdd(i))
                        {
                            test.assertThrows(() -> onValueIterator.next(),
                                new ParseException("hello there"));
                        }
                        else
                        {
                            test.assertTrue(onValueIterator.next());
                        }

                        final List<Integer> expectedValues = List.create();
                        for (int j = 0; j <= i; j++)
                        {
                            if (Math.isEven(j))
                            {
                                expectedValues.add(j);
                            }
                        }
                        test.assertEqual(expectedValues, values);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onValueIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onValueIterator.next());
                        test.assertEqual(Iterable.create(0, 2), values);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onValueIterator, true, null);
                    }
                });
            });

            runner.testGroup("catchError()", () ->
            {
                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final Iterator<Integer> catchErrorIterator = iterator.catchError();
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                        test.assertFalse(catchErrorIterator.next());

                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError();
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError();
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError();
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError();
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("catchError(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.catchError((Action0)null),
                        new PreConditionFailure("catchErrorAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final IntegerValue counter = IntegerValue.create(0);
                        final Iterator<Integer> catchErrorIterator = iterator.catchError(() -> { counter.increment(); });
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                        test.assertFalse(catchErrorIterator.next());

                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with action that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(() ->
                    {
                        counter.increment();
                        throw new ParseException("hello");
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with action that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(() ->
                    {
                        counter.increment();
                        if (Math.isOdd(counter.get()))
                        {
                            throw new ParseException("hello");
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(4 + i, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("catchError(Action1<Throwable>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.catchError((Action1<Throwable>)null),
                        new PreConditionFailure("catchErrorAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final List<Throwable> errors = List.create();
                        final Iterator<Integer> catchErrorIterator = iterator.catchError((Throwable error) -> { errors.add(error); });
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                        test.assertFalse(catchErrorIterator.next());

                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError((Throwable error) -> { errors.add(error); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError((Throwable error) -> { errors.add(error); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with action that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError((Throwable error) ->
                    {
                        errors.add(error);
                        throw new ParseException("hello");
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with action that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError((Throwable error) ->
                    {
                        errors.add(error);
                        if (Math.isOdd(errors.getCount()))
                        {
                            throw new ParseException("hello");
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError((Throwable error) -> { errors.add(error); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        final List<ParseException> expectedErrors = List.create();
                        for (int j = 0; j <= i; j++)
                        {
                            expectedErrors.add(new ParseException("hello there"));
                        }
                        test.assertEqual(expectedErrors, errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        final List<ParseException> expectedErrors = List.create(
                            new ParseException("hello there"),
                            new ParseException("hello there"),
                            new ParseException("hello there"));
                        for (int j = 0; j <= i; j++)
                        {
                            expectedErrors.add(new ParseException("hello there"));
                        }
                        test.assertEqual(expectedErrors, errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError((Throwable error) -> { errors.add(error); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(
                        Iterable.create(
                            new ParseException("hello there")),
                        errors);
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(
                        Iterable.create(
                            new ParseException("hello there")),
                        errors);
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(
                            Iterable.create(
                                new ParseException("hello there")),
                            errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("catchError(Class<TError>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    test.assertThrows(() -> iterator.catchError((Class<NotFoundException>)null),
                        new PreConditionFailure("errorType cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                        test.assertFalse(catchErrorIterator.next());

                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new ParseException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new ParseException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new NotFoundException("hello there");
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new EmptyException("hello there");
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new RuntimeException("hello there");
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new RuntimeException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    test.assertThrows(() -> catchErrorIterator.next(),
                        new RuntimeException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new RuntimeException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertThrows(() -> catchErrorIterator.next(),
                        new ParseException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new NotFoundException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new EmptyException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new RuntimeException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertThrows(() -> catchErrorIterator.next(),
                        new RuntimeException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("catchError(Class<TError>,Action0)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    test.assertThrows(() -> iterator.catchError((Class<NotFoundException>)null, Action0.empty),
                        new PreConditionFailure("errorType cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with null action", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    test.assertThrows(() -> iterator.catchError(NotFoundException.class, (Action0)null),
                        new PreConditionFailure("catchErrorAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final IntegerValue counter = IntegerValue.create(0);
                        final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new NotFoundException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(4 + i, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new EmptyException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(4 + i, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new RuntimeException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new RuntimeException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new RuntimeException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertThrows(() -> catchErrorIterator.next(),
                        new ParseException("hello there"));
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new NotFoundException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new EmptyException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new RuntimeException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertThrows(() -> catchErrorIterator.next(),
                        new RuntimeException("hello there"));
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("catchError(Class<TError>,Action1<TError>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    test.assertThrows(() -> iterator.catchError((Class<NotFoundException>)null, (NotFoundException error) -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with null action", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    test.assertThrows(() -> iterator.catchError(NotFoundException.class, (Action1<NotFoundException>)null),
                        new PreConditionFailure("catchErrorAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final List<NotFoundException> errors = List.create();
                        final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new NotFoundException("hello there");
                        });
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        final List<NotFoundException> expectedErrors = List.create();
                        for (int j = 0; j <= i; j++)
                        {
                            expectedErrors.add(new NotFoundException("hello there"));
                        }
                        test.assertEqual(expectedErrors, errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        final List<NotFoundException> expectedErrors = List.create(
                            new NotFoundException("hello there"),
                            new NotFoundException("hello there"),
                            new NotFoundException("hello there"));
                        for (int j = 0; j <= i; j++)
                        {
                            expectedErrors.add(new NotFoundException("hello there"));
                        }
                        test.assertEqual(expectedErrors, errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new EmptyException("hello there");
                        });
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(catchErrorIterator.next());
                        final List<NotFoundException> expectedErrors = List.create();
                        for (int j = 0; j <= i; j++)
                        {
                            expectedErrors.add(new EmptyException("hello there"));
                        }
                        test.assertEqual(expectedErrors, errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        final List<NotFoundException> expectedErrors = List.create(
                            new EmptyException("hello there"),
                            new EmptyException("hello there"),
                            new EmptyException("hello there"));
                        for (int j = 0; j <= i; j++)
                        {
                            expectedErrors.add(new EmptyException("hello there"));
                        }
                        test.assertEqual(expectedErrors, errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new RuntimeException("hello there");
                        });
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                            new RuntimeException("hello there"));
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> catchErrorIterator.next(),
                        new RuntimeException("hello there"));
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertThrows(() -> catchErrorIterator.next(),
                        new ParseException("hello there"));
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new NotFoundException("hello there");
                            }
                            return result;
                        });
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(
                        Iterable.create(
                            new NotFoundException("hello there")),
                        errors);
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(
                        Iterable.create(
                            new NotFoundException("hello there")),
                        errors);
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(
                            Iterable.create(
                                new NotFoundException("hello there")),
                            errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new EmptyException("hello there");
                            }
                            return result;
                        });
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(
                        Iterable.create(
                            new EmptyException("hello there")),
                        errors);
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(
                        Iterable.create(
                            new EmptyException("hello there")),
                        errors);
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(
                            Iterable.create(
                                new EmptyException("hello there")),
                            errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new RuntimeException("hello there");
                            }
                            return result;
                        });
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> catchErrorIterator = iterator.catchError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, catchErrorIterator, false, null);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 0);

                    test.assertThrows(() -> catchErrorIterator.next(),
                        new RuntimeException("hello there"));
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 1);

                    test.assertTrue(catchErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, catchErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(catchErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, catchErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("onError(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.onError((Action0)null),
                        new PreConditionFailure("onErrorAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final IntegerValue counter = IntegerValue.create(0);
                        final Iterator<Integer> onErrorIterator = iterator.onError(() -> { counter.increment(); });
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, onErrorIterator, false, null);

                        test.assertFalse(onErrorIterator.next());

                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(() -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with action that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(() ->
                    {
                        counter.increment();
                        throw new ParseException("hello");
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with action that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(() ->
                    {
                        counter.increment();
                        if (Math.isOdd(counter.get()))
                        {
                            throw new ParseException("hello");
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(() -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(4 + i, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(() -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new ParseException("hello there"));
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("onError(Action1<Throwable>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.onError((Action1<Throwable>)null),
                        new PreConditionFailure("onErrorAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final List<Throwable> errors = List.create();
                        final Iterator<Integer> onErrorIterator = iterator.onError((Throwable error) -> { errors.add(error); });
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, onErrorIterator, false, null);

                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError((Throwable error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError((Throwable error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with action that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError((Throwable error) ->
                    {
                        errors.add(error);
                        throw new ParseException("hello");
                    });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with action that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError((Throwable error) ->
                    {
                        errors.add(error);
                        if (Math.isOdd(errors.getCount()))
                        {
                            throw new ParseException("hello");
                        }
                    });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError((Throwable error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new ParseException("hello there"));
                        final List<Throwable> expectedErrors = List.create();
                        for (int j = 0; j <= i; j++)
                        {
                            expectedErrors.add(new ParseException("hello there"));
                        }
                        test.assertEqual(expectedErrors, errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new ParseException("hello there"));
                        final List<Throwable> expectedErrors = List.create(
                            new ParseException("hello there"),
                            new ParseException("hello there"),
                            new ParseException("hello there"));
                        for (int j = 0; j <= i; j++)
                        {
                            expectedErrors.add(new ParseException("hello there"));
                        }
                        test.assertEqual(expectedErrors, errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final List<Throwable> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError((Throwable error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new ParseException("hello there"));
                    test.assertEqual(
                        Iterable.create(
                            new ParseException("hello there")),
                        errors);
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(
                        Iterable.create(
                            new ParseException("hello there")),
                        errors);
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(
                            Iterable.create(
                                new ParseException("hello there")),
                            errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("onError(Class<TError>,Action0)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.onError((Class<NotFoundException>)null, Action0.empty),
                        new PreConditionFailure("errorType cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with null action", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.onError(NotFoundException.class, (Action0)null),
                        new PreConditionFailure("onErrorAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final IntegerValue counter = IntegerValue.create(0);
                        final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, onErrorIterator, false, null);

                        test.assertFalse(onErrorIterator.next());

                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with action that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () ->
                    {
                        counter.increment();
                        throw new ParseException("hello");
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with action that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () ->
                    {
                        counter.increment();
                        if (Math.isOdd(counter.get()))
                        {
                            throw new ParseException("hello");
                        }
                    });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new NotFoundException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new NotFoundException("hello there"));
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new NotFoundException("hello there"));
                        test.assertEqual(4 + i, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new EmptyException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new EmptyException("hello there"));
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new EmptyException("hello there"));
                        test.assertEqual(4 + i, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new RuntimeException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new RuntimeException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new RuntimeException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new ParseException("hello there"));
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new NotFoundException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new NotFoundException("hello there"));
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new EmptyException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new EmptyException("hello there"));
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new RuntimeException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new RuntimeException("hello there"));
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("onError(Class<TError>,Action1<TError>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.onError((Class<NotFoundException>)null, (NotFoundException error) -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with null action", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.onError(NotFoundException.class, (Action1<NotFoundException>)null),
                        new PreConditionFailure("onErrorAction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final List<NotFoundException> errors = List.create();
                        final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, onErrorIterator, false, null);

                        test.assertFalse(onErrorIterator.next());

                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with action that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, (NotFoundException error) ->
                    {
                        errors.add(error);
                        throw new ParseException("hello");
                    });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with action that sometimes throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, (NotFoundException error) ->
                    {
                        errors.add(error);
                        if (Math.isOdd(errors.getCount()))
                        {
                            throw new ParseException("hello");
                        }
                    });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final List<NotFoundException> errors = List.create();
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, (NotFoundException error) -> { errors.add(error); });
                    test.assertEqual(Iterable.create(), errors);
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new ParseException("hello there"));
                        test.assertEqual(Iterable.create(), errors);
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new NotFoundException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new NotFoundException("hello there"));
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new NotFoundException("hello there"));
                        test.assertEqual(4 + i, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new EmptyException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new EmptyException("hello there"));
                        test.assertEqual(i + 1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new EmptyException("hello there"));
                        test.assertEqual(4 + i, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new RuntimeException("hello there");
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new RuntimeException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, onErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> onErrorIterator.next(),
                            new RuntimeException("hello there"));
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new ParseException("hello there"));
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new NotFoundException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new NotFoundException("hello there"));
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new EmptyException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new EmptyException("hello there"));
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(1, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(1, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new RuntimeException("hello there");
                            }
                            return result;
                        });
                    final IntegerValue counter = IntegerValue.create(0);
                    final Iterator<Integer> onErrorIterator = iterator.onError(NotFoundException.class, () -> { counter.increment(); });
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, onErrorIterator, false, null);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 0);

                    test.assertThrows(() -> onErrorIterator.next(),
                        new RuntimeException("hello there"));
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 1);

                    test.assertTrue(onErrorIterator.next());
                    test.assertEqual(0, counter.get());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, onErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(onErrorIterator.next());
                        test.assertEqual(0, counter.get());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, onErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("convertError(Function0<? extends Throwable>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.convertError((Function0<? extends Throwable>)null),
                        new PreConditionFailure("convertErrorFunction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final Iterator<Integer> convertErrorIterator = iterator.convertError(() -> new NotFoundException("abc"));
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                        test.assertFalse(convertErrorIterator.next());

                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(() -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(() -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with function that always throws", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(() -> { throw new ParseException("hello"); });
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertTrue(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(() -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new NotFoundException("abc"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new NotFoundException("abc"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(() -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("convertError(Function1<Throwable,? extends Throwable>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.convertError((Function1<Throwable,? extends Throwable>)null),
                        new PreConditionFailure("convertErrorFunction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final Iterator<Integer> convertErrorIterator = iterator.convertError((Throwable error) -> new NotFoundException(error.getMessage()));
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                        test.assertFalse(convertErrorIterator.next());

                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final Iterator<Integer> convertErrorIterator = iterator.convertError((Throwable error) -> new NotFoundException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final Iterator<Integer> convertErrorIterator = iterator.convertError((Throwable error) -> new NotFoundException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError((Throwable error) -> new NotFoundException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new NotFoundException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new NotFoundException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError((Throwable error) -> new NotFoundException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new NotFoundException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("convertError(Class<? extends Throwable>,Function0<? extends Throwable>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.convertError((Class<? extends Throwable>)null, () -> new NotFoundException("hello there")),
                        new PreConditionFailure("errorType cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with null function", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.convertError(NotFoundException.class, (Function0<? extends Throwable>)null),
                        new PreConditionFailure("convertErrorFunction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                        test.assertFalse(convertErrorIterator.next());

                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new ParseException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new ParseException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new NotFoundException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new NotFoundException("abc"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new NotFoundException("abc"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new EmptyException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new NotFoundException("abc"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new NotFoundException("abc"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new RuntimeException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new RuntimeException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new RuntimeException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new ParseException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new NotFoundException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new EmptyException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new RuntimeException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, () -> new NotFoundException("abc"));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new RuntimeException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("convertError(Class<TError>,Function1<TError,? extends Throwable>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.convertError((Class<ParseException>)null, (ParseException error) -> new NotFoundException("hello there")),
                        new PreConditionFailure("errorType cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with null function", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(5, false);
                    test.assertThrows(() -> iterator.convertError(NotFoundException.class, (Function1<NotFoundException,? extends Throwable>)null),
                        new PreConditionFailure("convertErrorFunction cannot be null."));
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                        IteratorTests.assertIterator(test, iterator, false, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                        test.assertFalse(convertErrorIterator.next());

                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertTrue(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new ParseException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new ParseException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new ParseException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new NotFoundException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new SocketClosedException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new SocketClosedException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new SocketClosedException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new EmptyException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new SocketClosedException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new SocketClosedException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new SocketClosedException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that always throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new RuntimeException("hello there");
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new RuntimeException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, i);
                    }

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new RuntimeException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, null);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> convertErrorIterator.next(),
                            new RuntimeException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new ParseException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws same type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new NotFoundException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new SocketClosedException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws derived type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new EmptyException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new SocketClosedException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });

                runner.test("with Iterator that sometimes throws base type exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new RuntimeException("hello there");
                            }
                            return result;
                        });
                    final Iterator<Integer> convertErrorIterator = iterator.convertError(NotFoundException.class, (NotFoundException error) -> new SocketClosedException(error.getMessage()));
                    IteratorTests.assertIterator(test, iterator, false, null);
                    IteratorTests.assertIterator(test, convertErrorIterator, false, null);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 0);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 0);

                    test.assertThrows(() -> convertErrorIterator.next(),
                        new RuntimeException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 1);

                    test.assertTrue(convertErrorIterator.next());
                    IteratorTests.assertIterator(test, iterator, true, 2);
                    IteratorTests.assertIterator(test, convertErrorIterator, true, 2);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(convertErrorIterator.next());
                        IteratorTests.assertIterator(test, iterator, true, null);
                        IteratorTests.assertIterator(test, convertErrorIterator, true, null);
                    }
                });
            });

            runner.testGroup("await()", () ->
            {
                runner.test("with empty Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        final List<Object> results = List.create();

                        iterator.onValue(results::add).onError(results::add).await();

                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertEqual(
                            Iterable.create(),
                            results);
                    }
                });

                runner.test("with one value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(1, false);
                    final List<Object> results = List.create();

                    final Iterator<Integer> resultsIterator = iterator.onValue(results::add).onError(results::add);
                    resultsIterator.await();

                    IteratorTests.assertIterator(test, iterator, true, null);
                    IteratorTests.assertIterator(test, resultsIterator, true, null);
                    test.assertEqual(
                        Iterable.create(0),
                        results);
                });

                runner.test("with two value Iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(2, false);
                    final List<Object> results = List.create();

                    final Iterator<Integer> resultsIterator = iterator.onValue(results::add).onError(results::add);
                    resultsIterator.await();

                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertEqual(
                        Iterable.create(0, 1),
                        results);
                });

                runner.test("with Iterator that always throws exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            innerIterator.next();
                            throw new ParseException("hello there");
                        });
                    final List<Object> results = List.create();
                    final Iterator<Integer> resultsIterator = iterator.onValue(results::add).onError(results::add);

                    for (int i = 0; i < 3; i++)
                    {
                        test.assertThrows(() -> resultsIterator.await(),
                            new ParseException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, i);

                        final List<Object> expectedResults = List.create();
                        for (int j = 0; j <= i; j++)
                        {
                            expectedResults.add(new ParseException("hello there"));
                        }
                        test.assertEqual(expectedResults, results);
                    }

                    test.assertThrows(() -> resultsIterator.await(),
                        new ParseException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertEqual(
                        Iterable.create(
                            new ParseException("hello there"),
                            new ParseException("hello there"),
                            new ParseException("hello there"),
                            new ParseException("hello there")),
                        results);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertThrows(() -> resultsIterator.await(),
                            new ParseException("hello there"));
                        IteratorTests.assertIterator(test, iterator, true, null);

                        final List<Object> expectedResults = List.create(
                            new ParseException("hello there"),
                                new ParseException("hello there"),
                                new ParseException("hello there"),
                                new ParseException("hello there"));
                        for (int j = 0; j <= i; j++)
                        {
                            expectedResults.add(new ParseException("hello there"));
                        }
                        test.assertEqual(expectedResults, results);
                    }
                });

                runner.test("with Iterator that sometimes throws unrelated exception", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false).customize()
                        .setNextFunction((Iterator<Integer> innerIterator) ->
                        {
                            final boolean result = innerIterator.next();
                            if (result && Math.isOdd(innerIterator.getCurrent()))
                            {
                                throw new ParseException("hello there");
                            }
                            return result;
                        });
                    final List<Object> results = List.create();
                    final Iterator<Integer> resultsIterator = iterator.onValue(results::add).onError(results::add);

                    test.assertThrows(() -> resultsIterator.await(),
                        new ParseException("hello there"));
                    IteratorTests.assertIterator(test, iterator, true, 1);
                    test.assertEqual(
                        Iterable.create(
                            0,
                            new ParseException("hello there")),
                        results);

                    resultsIterator.await();
                    IteratorTests.assertIterator(test, iterator, true, null);
                    test.assertEqual(
                        Iterable.create(
                            0,
                            new ParseException("hello there"),
                            2),
                        results);

                    for (int i = 0; i < 2; i++)
                    {
                        resultsIterator.await();
                        IteratorTests.assertIterator(test, iterator, true, null);
                        test.assertEqual(
                            Iterable.create(
                                0,
                                new ParseException("hello there"),
                                2),
                            results);
                    }
                });
            });

            runner.testGroup("isNullOrEmpty(Iterator<?>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertTrue(Iterator.isNullOrEmpty(null));
                });

                runner.test("with non-started empty", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, false);
                    if (iterator != null)
                    {
                        IteratorTests.assertIterator(test, iterator, false, null);

                        for (int i = 0; i < 2; i++)
                        {
                            test.assertTrue(Iterator.isNullOrEmpty(iterator));
                            IteratorTests.assertIterator(test, iterator, true, null);
                        }
                    }
                });

                runner.test("with started empty", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(0, true);
                    if (iterator != null)
                    {
                        for (int i = 0; i < 2; i++)
                        {
                            test.assertTrue(Iterator.isNullOrEmpty(iterator));
                            IteratorTests.assertIterator(test, iterator, true, null);
                        }
                    }
                });

                runner.test("with non-started non-empty", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, false);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(Iterator.isNullOrEmpty(iterator));
                        IteratorTests.assertIterator(test, iterator, true, 0);
                    }
                });

                runner.test("with started non-empty", (Test test) ->
                {
                    final Iterator<Integer> iterator = createIterator.run(3, true);

                    for (int i = 0; i < 2; i++)
                    {
                        test.assertFalse(Iterator.isNullOrEmpty(iterator));
                        IteratorTests.assertIterator(test, iterator, true, 0);
                    }
                });
            });
        });
    }

    public static <T> void assertIterator(Test test, Iterator<T> iterator, boolean expectedHasStarted, T expectedCurrent)
    {
        PreCondition.assertNotNull(test, "test");

        test.assertNotNull(iterator);
        test.assertEqual(expectedHasStarted, iterator.hasStarted(), "Wrong hasStarted()");
        test.assertEqual(expectedCurrent != null, iterator.hasCurrent(), "Wrong hasCurrent()");
        if (expectedCurrent == null)
        {
            test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
        }
        else
        {
            test.assertEqual(expectedCurrent, iterator.getCurrent(), "Wrong getCurrent()");
        }
    }
}
