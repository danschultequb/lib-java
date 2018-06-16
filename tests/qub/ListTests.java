package qub;

public abstract class ListTests
{
    public static void test(final TestRunner runner, final Function1<Integer,List<Integer>> createList)
    {
        runner.testGroup(List.class, () ->
        {
            IndexableTests.test(runner, createList::run);

            runner.testGroup("add()", () ->
            {
                runner.test("multiple values", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertEqual(0, list.getCount());
                    test.assertFalse(list.any());
                    test.assertEqual(null, list.get(0));

                    for (int i = 0; i < 100; ++i)
                    {
                        list.add(100 - i);
                        test.assertEqual(i + 1, list.getCount());
                        test.assertTrue(list.any());
                        test.assertEqual(100 - i, list.get(i));
                    }
                });

                runner.test("after removing the only value in the List", (Test test) ->
                {
                    final List<Integer> list = createList.run(1);
                    list.removeFirst(Math.isEven);

                    list.add(70);
                    test.assertEqual(new int[] { 70 }, Array.toIntArray(list));
                });
            });

            runner.testGroup("insert(int,T)", () ->
            {
                runner.test("with negative index when empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    if (list != null)
                    {
                        final Result<Boolean> result = list.insert(-1, 20);
                        test.assertError(new IllegalArgumentException("insertIndex (-1) must be 0."), result);
                        test.assertEqual(Array.fromValues(new Integer[0]), list);
                    }
                });

                runner.test("with 0 index when empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    if (list != null)
                    {
                        final Result<Boolean> result = list.insert(0, 20);
                        test.assertSuccess(true, result);
                        test.assertEqual(Array.fromValues(new Integer[] { 20 }), list);
                    }
                });

                runner.test("with positive index when empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    if (list != null)
                    {
                        final Result<Boolean> result = list.insert(1, 20);
                        test.assertError(new IllegalArgumentException("insertIndex (1) must be 0."), result);
                        test.assertEqual(Array.fromValues(new Integer[0]), list);
                    }
                });

                runner.test("with negative index when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    final Result<Boolean> result = list.insert(-1, 20);
                    test.assertError(new IllegalArgumentException("insertIndex (-1) must be between 0 and 4."), result);
                    test.assertEqual(Array.fromValues(new Integer[] { 0, 1, 2, 3 }), list);
                });

                runner.test("with 0 index when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(3);
                    final Result<Boolean> result = list.insert(0, 20);
                    test.assertSuccess(true, result);
                    test.assertEqual(Array.fromValues(new Integer[] { 20, 0, 1, 2 }), list);
                });

                runner.test("with positive index less than list count when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(5);
                    if (list != null)
                    {
                        final Result<Boolean> result = list.insert(2, 20);
                        test.assertSuccess(true, result);
                        test.assertEqual(Array.fromValues(new Integer[] { 0, 1, 20, 2, 3, 4 }), list);
                    }
                });

                runner.test("with positive index equal to list count when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    final Result<Boolean> result = list.insert(4, 20);
                    test.assertSuccess(true, result);
                    test.assertEqual(Array.fromValues(new Integer[] { 0, 1, 2, 3, 20 }), list);
                });

                runner.test("with positive index greater than list count when not empty", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    final Result<Boolean> result = list.insert(5, 20);
                    test.assertError(new IllegalArgumentException("insertIndex (5) must be between 0 and 4."), result);
                    test.assertEqual(Array.fromValues(new Integer[] { 0, 1, 2, 3 }), list);
                });
            });

            runner.test("addAll()", (Test test) ->
            {
                final List<Integer> list = createList.run(0);
                test.assertEqual(0, list.getCount());
                test.assertFalse(list.any());
                test.assertEqual(null, list.get(0));

                list.addAll(new Integer[0]);
                test.assertEqual(0, list.getCount());
                test.assertFalse(list.any());
                test.assertEqual(null, list.get(0));

                list.addAll(new Integer[] { 0 });
                test.assertEqual(1, list.getCount());
                test.assertTrue(list.any());
                test.assertEqual(0, list.get(0));

                list.addAll(new Integer[] { 1, 2, 3, 4, 5 });
                test.assertEqual(6, list.getCount());
                test.assertTrue(list.any());
                for (int i = 0; i < list.getCount(); ++i)
                {
                    test.assertEqual(i, list.get(i));
                }

                list.addAll((Iterator<Integer>)null);
                test.assertEqual(6, list.getCount());
                test.assertTrue(list.any());
                for (int i = 0; i < list.getCount(); ++i)
                {
                    test.assertEqual(i, list.get(i));
                }

                list.addAll(Array.fromValues(new Integer[0]).iterate());
                test.assertEqual(6, list.getCount());
                test.assertTrue(list.any());
                for (int i = 0; i < list.getCount(); ++i)
                {
                    test.assertEqual(i, list.get(i));
                }

                list.addAll(Array.fromValues(new Integer[] { 6, 7, 8, 9 }).iterate());
                test.assertEqual(10, list.getCount());
                test.assertTrue(list.any());
                for (int i = 0; i < list.getCount(); ++i)
                {
                    test.assertEqual(i, list.get(i));
                }

                list.addAll((Iterable<Integer>)null);
                test.assertEqual(10, list.getCount());
                test.assertTrue(list.any());
                for (int i = 0; i < list.getCount(); ++i)
                {
                    test.assertEqual(i, list.get(i));
                }

                list.addAll(Array.fromValues(new Integer[0]));
                test.assertEqual(10, list.getCount());
                test.assertTrue(list.any());
                for (int i = 0; i < list.getCount(); ++i)
                {
                    test.assertEqual(i, list.get(i));
                }

                list.addAll(Array.fromValues(new Integer[] { 10, 11 }));
                test.assertEqual(12, list.getCount());
                test.assertTrue(list.any());
                for (int i = 0; i < list.getCount(); ++i)
                {
                    test.assertEqual(i, list.get(i));
                }
            });

            runner.test("set()", (Test test) ->
            {
                final List<Integer> list = createList.run(0);
                for (int i = -1; i <= 1; ++i)
                {
                    list.set(i, i);
                    test.assertNull(list.get(i));
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

            runner.testGroup("remove()", () ->
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
                    test.assertEqual(Array.fromValues(new Integer[] { 0, 1, 2, 4 }), list);
                });
            });

            runner.test("removeAt()", (Test test) ->
            {
                final List<Integer> list = createList.run(0);
                for (int i = -1; i <= 1; ++i)
                {
                    test.assertNull(list.removeAt(i));
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
                    test.assertNull(list.removeFirst());
                });

                runner.test("with single value List", (Test test) ->
                {
                    final List<Integer> list = createList.run(1);
                    test.assertEqual(0, list.removeFirst());
                    test.assertNull(list.removeFirst());
                });

                runner.test("with multiple value List", (Test test) ->
                {
                    final List<Integer> list = createList.run(3);
                    test.assertEqual(0, list.removeFirst());
                    test.assertEqual(1, list.removeFirst());
                    test.assertEqual(2, list.removeFirst());
                    test.assertNull(list.removeFirst());
                });
            });

            runner.testGroup("removeFirst() with condition", () ->
            {
                runner.test("with null condition and empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertNull(list.removeFirst(null));
                });

                runner.test("with null condition and non-empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    test.assertNull(list.removeFirst(null));
                });

                runner.test("with non-matching condition and empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(0);
                    test.assertNull(list.removeFirst(Math.isOdd));
                });

                runner.test("with non-matching condition and non-empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(1);
                    test.assertNull(list.removeFirst(Math.isOdd));
                });

                runner.test("with matching condition and non-empty List", (Test test) ->
                {
                    final List<Integer> list = createList.run(4);
                    test.assertEqual(1, list.removeFirst(Math.isOdd));
                    test.assertEqual(new int[] { 0, 2, 3 }, Array.toIntArray(list));
                });

                runner.test("with matching condition and single-value List", (Test test) ->
                {
                    final List<Integer> list = createList.run(1);
                    test.assertEqual(0, list.removeFirst(Math.isEven));
                    test.assertFalse(list.any());
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
                    test.assertFalse(list.endsWith(Array.fromValues(new int[] { 7 })));
                });

                runner.test("with non-empty List and null", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.endsWith((Iterable<Integer>)null));
                });

                runner.test("with non-empty List and non-matching value", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.endsWith(Array.fromValues(new int[] { 3 })));
                });

                runner.test("with non-empty List and non-matching multiple values", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertFalse(list.endsWith(Array.fromValues(new int[] { 8, 9, 10 })));
                });

                runner.test("with non-empty List and matching single value", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertTrue(list.endsWith(Array.fromValues(new int[] { 9 })));
                });

                runner.test("with non-empty List and matching multiple values", (Test test) ->
                {
                    final List<Integer> list = createList.run(10);
                    test.assertTrue(list.endsWith(Array.fromValues(new int[] { 5, 6, 7, 8, 9 })));
                });
            });
        });
    }
}
