package qub;

public interface ListTests
{
    static void test(final TestRunner runner, final Function1<Integer,List<Integer>> createList)
    {
        runner.testGroup(List.class, () ->
        {
            MutableIndexableTests.test(runner, createList::run);

            runner.testGroup("add()", () ->
            {
                runner.test("multiple values", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertEqual(Iterable.create(), list);

                    for (int i = 0; i < 100; ++i)
                    {
                        list.add(100 - i);
                        test.assertEqual(i + 1, list.getCount());
                        test.assertEqual(100 - i, list.get(i));
                    }
                });

                runner.test("after removing the only value in the List", (Test test) ->
                {
                    final List<Integer> list = createList.run(1);
                    list.removeFirst(Math::isEven);

                    list.add(70);
                    test.assertEqual(Iterable.create(70), list);
                });

                runner.test("after inserting a value at index 0", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);

                    list.insert(0, 10);

                    final List<Integer> addResult = list.add(20);
                    test.assertSame(list, addResult);
                    test.assertEqual(Iterable.create(10, 20), list);
                });

                runner.test("after inserting values at index 0 and 1", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);

                    list.insert(0, 20);
                    list.insert(0, 10);

                    final List<Integer> addResult = list.add(30);
                    test.assertSame(list, addResult);
                    test.assertEqual(Iterable.create(10, 20, 30), list);
                });
            });

            runner.testGroup("insert(int,T)", () ->
            {
                runner.test("with negative index when empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    if (list != null)
                    {
                        test.assertThrows(() -> list.insert(-1, 20), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                        test.assertEqual(Iterable.create(), list);
                    }
                });

                runner.test("with 0 index when empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    if (list != null)
                    {
                        list.insert(0, 20);
                        test.assertEqual(Iterable.create(20), list);
                    }
                });

                runner.test("with positive index when empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    if (list != null)
                    {
                        test.assertThrows(() -> list.insert(1, 20), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                        test.assertEqual(Iterable.create(), list);
                    }
                });

                runner.test("with negative index when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    test.assertThrows(() -> list.insert(-1, 20), new PreConditionFailure("insertIndex (-1) must be between 0 and 4."));
                    test.assertEqual(Iterable.create(0, 1, 2, 3), list);
                });

                runner.test("with 0 index when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(3);
                    list.insert(0, 20);
                    test.assertEqual(Iterable.create(20, 0, 1, 2), list);
                });

                runner.test("with positive index less than list count when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    if (list != null)
                    {
                        list.insert(2, 20);
                        test.assertEqual(Iterable.create(0, 1, 20, 2, 3, 4), list);
                    }
                });

                runner.test("with positive index equal to list count when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    list.insert(4, 20);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 20), list);
                });

                runner.test("with positive index greater than list count when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    test.assertThrows(() -> list.insert(5, 20), new PreConditionFailure("insertIndex (5) must be between 0 and 4."));
                    test.assertEqual(Iterable.create(0, 1, 2, 3), list);
                });
            });

            runner.test("addAll()", (Test test) ->
            {
                final List<Integer> list = createList.run(0);
                test.assertEqual(Iterable.create(), list);

                test.assertThrows(() -> list.addAll((Integer[])null),
                    new PreConditionFailure("values cannot be null."));

                list.addAll();
                test.assertEqual(Iterable.create(), list);

                list.addAll(new Integer[0]);
                test.assertEqual(Iterable.create(), list);

                list.addAll(new Integer[] { 0 });
                test.assertEqual(Iterable.create(0), list);

                list.addAll(1, 2, 3, 4, 5);
                test.assertEqual(Iterable.create(0, 1, 2, 3, 4, 5), list);

                test.assertThrows(() -> list.addAll((Iterator<Integer>)null),
                    new PreConditionFailure("values cannot be null."));
                test.assertEqual(Iterable.create(0, 1, 2, 3, 4, 5), list);

                list.addAll(Iterator.create());
                test.assertEqual(Iterable.create(0, 1, 2, 3, 4, 5), list);

                list.addAll(Iterator.create(6, 7, 8, 9));
                test.assertEqual(Iterable.create(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), list);

                test.assertThrows(() -> list.addAll((Iterable<Integer>)null),
                    new PreConditionFailure("values cannot be null."));
                test.assertEqual(Iterable.create(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), list);

                list.addAll(Iterable.create());
                test.assertEqual(Iterable.create(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), list);

                list.addAll(Iterable.create(10, 11));
                test.assertEqual(Iterable.create(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), list);
            });

            runner.test("set()", (Test test) ->
            {
                final List<Integer> list = createList.run(0);
                for (int i = -1; i <= 1; ++i)
                {
                    final int index = i;
                    test.assertThrows(() -> list.set(index, index), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                }

                for (int i = 0; i < 10; ++i)
                {
                    list.add(i);
                }

                for (int i = 0; i < list.getCount(); ++i)
                {
                    list.set(i, -list.get(i));
                }

                for (int i = 0; i < list.getCount(); ++i)
                {
                    test.assertEqual(-i, list.get(i));
                }
            });

            runner.testGroup("remove(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.remove(null));
                    test.assertEqual(10, list.getCount());
                });

                runner.test("with not found", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.remove(20));
                    test.assertEqual(10, list.getCount());
                });

                runner.test("with found", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    test.assertTrue(list.remove(3));
                    test.assertEqual(4, list.getCount());
                    test.assertEqual(Array.create(0, 1, 2, 4), list);
                });
            });

            runner.test("removeAt()", (Test test) ->
            {
                final List<Integer> list = createList.run(0);
                for (int i = -1; i <= 1; ++i)
                {
                    final int removeIndex = i;
                    test.assertThrows(() -> list.removeAt(removeIndex), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                }
                test.assertEqual(0, list.getCount());

                for (int i = 0; i < 10; ++i)
                {
                    list.add(i);
                }
                test.assertEqual(10, list.getCount());

                test.assertEqual(0, list.removeAt(0));
                test.assertEqual(9, list.getCount());

                test.assertEqual(9, list.removeAt(8));
                test.assertEqual(8, list.getCount());

                test.assertEqual(5, list.removeAt(4));
                test.assertEqual(7, list.getCount());
            });

            runner.testGroup("removeFirst()", () ->
            {
                runner.test("with empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertThrows(() -> list.removeFirst().await(),
                        new EmptyException());
                });

                runner.test("with single value List", (Test test) ->
                {
                    final List<Integer> list = createList.run(1);
                    test.assertEqual(0, list.removeFirst().await());
                    test.assertThrows(() -> list.removeFirst().await(),
                        new EmptyException());
                });

                runner.test("with multiple value List", (Test test) ->
                {
                    final List<Integer> list = createList.run(3);
                    test.assertEqual(0, list.removeFirst().await());
                    test.assertEqual(1, list.removeFirst().await());
                    test.assertEqual(2, list.removeFirst().await());
                    test.assertThrows(() -> list.removeFirst().await(),
                        new EmptyException());
                });
            });

            runner.testGroup("removeFirst(Function1<T,Boolean>)", () ->
            {
                runner.test("with null condition and empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertThrows(() -> list.removeFirst(null), new PreConditionFailure("condition cannot be null."));
                });

                runner.test("with null condition and non-empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    test.assertThrows(() -> list.removeFirst(null), new PreConditionFailure("condition cannot be null."));
                });

                runner.test("with non-matching condition and empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertNull(list.removeFirst(Math::isOdd));
                });

                runner.test("with non-matching condition and non-empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(1);
                    test.assertNull(list.removeFirst(Math::isOdd));
                });

                runner.test("with matching condition and non-empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    test.assertEqual(1, list.removeFirst(Math::isOdd));
                    test.assertEqual(Iterable.create(0, 2, 3), list);
                });

                runner.test("with matching condition and single-value List", (Test test) ->
                {
                    final List<Integer> list = createList.run(1);
                    test.assertEqual(0, list.removeFirst(Math::isEven));
                    test.assertFalse(list.any());
                });
            });

            runner.testGroup("removeFirst(int)", () ->
            {
                runner.test("with empty list and zero values to remove", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    if (list != null)
                    {
                        test.assertEqual(Iterable.create(), list.removeFirst(0).await());
                        test.assertEqual(Iterable.create(), list);
                    }
                });

                runner.test("with empty list and non-zero values to remove", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    if (list != null)
                    {
                        test.assertThrows(() -> list.removeFirst(1).await(),
                            new EmptyException());
                        test.assertEqual(Iterable.create(), list);
                    }
                });

                runner.test("with negative", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    test.assertThrows(() -> list.removeFirst(-1),
                        new PreConditionFailure("valuesToRemove (-1) must be greater than or equal to 0."));
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), list);
                });

                runner.test("with zero", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removeFirstResult = list.removeFirst(0).await();
                    test.assertEqual(Iterable.create(), removeFirstResult);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), list);
                });

                runner.test("with fewer than list count", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    test.assertEqual(Iterable.create(0, 1, 2), list.removeFirst(3).await());
                    test.assertEqual(Iterable.create(3, 4), list);
                });

                runner.test("with list count", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), list.removeFirst(5).await());
                    test.assertEqual(Iterable.create(), list);
                });

                runner.test("with more than list count", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removeFirstResult = list.removeFirst(6).await();
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), removeFirstResult);
                    test.assertEqual(Iterable.create(), list);
                });
            });

            runner.testGroup("removeLast()", () ->
            {
                runner.test("with empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertThrows(list::removeLast,
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with single value List", (Test test) ->
                {
                    final List<Integer> list = createList.run(1);
                    test.assertEqual(0, list.removeLast());
                    test.assertThrows(list::removeLast,
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });

                runner.test("with multiple value List", (Test test) ->
                {
                    final List<Integer> list = createList.run(3);
                    test.assertEqual(2, list.removeLast());
                    test.assertEqual(1, list.removeLast());
                    test.assertEqual(0, list.removeLast());
                    test.assertThrows(list::removeLast,
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                });
            });

            runner.testGroup("removeAll(T...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    test.assertThrows(() -> list.removeAll((Integer[])null),
                        new PreConditionFailure("toRemove cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll();
                    test.assertEqual(Iterable.create(), removed);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), list);
                });

                runner.test("with one argument that doesn't exist", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(7);
                    test.assertEqual(Iterable.create(), removed);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), list);
                });

                runner.test("with one argument that exists", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(3);
                    test.assertEqual(Iterable.create(3), removed);
                    test.assertEqual(Iterable.create(0, 1, 2, 4), list);
                });

                runner.test("with multiple arguments that exists", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(3, 0);
                    test.assertEqual(Iterable.create(0, 3), removed);
                    test.assertEqual(Iterable.create(1, 2, 4), list);
                });

                runner.test("with null argument", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(3, null, 0);
                    test.assertEqual(Iterable.create(0, 3), removed);
                    test.assertEqual(Iterable.create(1, 2, 4), list);
                });
            });

            runner.testGroup("removeAll(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    test.assertThrows(() -> list.removeAll((Iterable<Integer>)null),
                        new PreConditionFailure("toRemove cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(Iterable.create());
                    test.assertEqual(Iterable.create(), removed);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), list);
                });

                runner.test("with one value that doesn't exist", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(Iterable.create(7));
                    test.assertEqual(Iterable.create(), removed);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), list);
                });

                runner.test("with one value that exists", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(Iterable.create(3));
                    test.assertEqual(Iterable.create(3), removed);
                    test.assertEqual(Iterable.create(0, 1, 2, 4), list);
                });

                runner.test("with multiple values that exists", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(Iterable.create(3, 0));
                    test.assertEqual(Iterable.create(0, 3), removed);
                    test.assertEqual(Iterable.create(1, 2, 4), list);
                });

                runner.test("with all values in list", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(list);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), removed);
                    test.assertEqual(Iterable.create(), list);
                });

                runner.test("with null value", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(Iterable.create(3, null, 0));
                    test.assertEqual(Iterable.create(0, 3), removed);
                    test.assertEqual(Iterable.create(1, 2, 4), list);
                });
            });

            runner.testGroup("removeAll(Function1<T,Boolean>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    test.assertThrows(() -> list.removeAll((Function1<Integer,Boolean>)null),
                        new PreConditionFailure("condition cannot be null."));
                });

                runner.test("with no matches", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll((Integer value) -> value > 100);
                    test.assertEqual(Iterable.create(), removed);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), list);
                });

                runner.test("with some matches", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    final Iterable<Integer> removed = list.removeAll(Math::isOdd);
                    test.assertEqual(Iterable.create(1, 3), removed);
                    test.assertEqual(Iterable.create(0, 2, 4), list);
                });
            });

            runner.test("clear()", (Test test) ->
            {
                final List<Integer> list = createList.run(0);
                list.clear();
                test.assertEqual(0, list.getCount());

                for (int i = 0; i < 5; ++i)
                {
                    list.add(i);
                }
                test.assertEqual(5, list.getCount());

                list.clear();
                test.assertEqual(0, list.getCount());
            });

            runner.testGroup("endsWith(T)", () ->
            {
                runner.test("with empty List and null", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertFalse(list.endsWith((Integer)null));
                });

                runner.test("with empty List and 7", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertFalse(list.endsWith((Integer)null));
                });

                runner.test("with non-empty List and null", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.endsWith((Integer)null));
                });

                runner.test("with non-empty List and non-matching value", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.endsWith(3));
                });

                runner.test("with non-empty List and matching value", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertTrue(list.endsWith(9));
                });
            });

            runner.testGroup("endsWith(Iterable<T>)", () ->
            {
                runner.test("with empty List and null Iterable", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertFalse(list.endsWith((Iterable<Integer>)null));
                });

                runner.test("with empty List and [7]", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertFalse(list.endsWith(Array.create(7)));
                });

                runner.test("with non-empty List and null", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.endsWith((Iterable<Integer>)null));
                });

                runner.test("with non-empty List and non-matching value", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.endsWith(Array.create(3)));
                });

                runner.test("with non-empty List and non-matching multiple values", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.endsWith(Array.create(8, 9, 10)));
                });

                runner.test("with non-empty List and matching single value", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertTrue(list.endsWith(Array.create(9)));
                });

                runner.test("with non-empty List and matching multiple values", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertTrue(list.endsWith(Array.create(5, 6, 7, 8, 9)));
                });
            });
        });
    }
}
