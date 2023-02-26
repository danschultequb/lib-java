package qub;

public interface IterableTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Iterable.class, () ->
        {
            runner.testGroup("create(T...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final Iterable<Integer> iterable = Iterable.create();
                    test.assertNotNull(iterable);
                    test.assertEqual(0, iterable.getCount());
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterable<Integer> iterable = Iterable.create(5);
                    test.assertNotNull(iterable);
                    test.assertEqual(1, iterable.getCount());
                    test.assertEqual(5, iterable.first().await());
                    test.assertEqual(5, iterable.last().await());
                });

                runner.test("with two values", (Test test) ->
                {
                    final Iterable<Integer> iterable = Iterable.create(5, 10);
                    test.assertNotNull(iterable);
                    test.assertEqual(2, iterable.getCount());
                    test.assertEqual(5, iterable.first().await());
                    test.assertEqual(10, iterable.last().await());
                });

                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Iterable.create((Integer[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final Iterable<Integer> iterable = Iterable.create(new Integer[0]);
                    test.assertNotNull(iterable);
                    test.assertEqual(0, iterable.getCount());
                });
            });

            runner.testGroup("toString(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual("null", Iterable.toString(null));
                });
            });

            runner.testGroup("isNullOrEmpty(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertTrue(Iterable.isNullOrEmpty(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertTrue(Iterable.isNullOrEmpty(Iterable.create()));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertFalse(Iterable.isNullOrEmpty(Iterable.create(1, 2, 3)));
                });
            });

            runner.testGroup("traverse(T,Function1<T,Iterable<T>>)", () ->
            {
                final Function1<Integer,Iterable<Integer>> getNextValues = (Integer value) ->
                {
                    final List<Integer> nextValues = List.create();
                    if (value + 2 <= 20)
                    {
                        nextValues.add(value + 2);
                    }
                    if (value * 2 <= 20)
                    {
                        nextValues.add(value * 2);
                    }
                    return nextValues;
                };

                runner.test("with non-empty Iterable", (Test test) ->
                {
                    test.assertEqual(Array.create(2, 4, 8, 16, 18, 20, 10, 12, 14, 6), Iterable.traverse(2, getNextValues));
                });

                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Iterable.traverse(2, null), new PreConditionFailure("getNextValues cannot be null."));
                });
            });

            runner.testGroup("traverse(Iterable<T>,Function1<T,Iterable<T>>)", () ->
            {
                final Function1<Integer,Iterable<Integer>> getNextValues = (Integer value) ->
                {
                    final List<Integer> nextValues = List.create();
                    if (value + 2 <= 20)
                    {
                        nextValues.add(value + 2);
                    }
                    if (value * 2 <= 20)
                    {
                        nextValues.add(value * 2);
                    }
                    return nextValues;
                };

                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertThrows(() -> Iterable.traverse((Iterable<Integer>)null, getNextValues), new PreConditionFailure("startValues cannot be null."));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    test.assertEqual(Iterable.create(), Iterable.traverse(Iterable.create(), getNextValues));
                });

                runner.test("with non-empty Iterable", (Test test) ->
                {
                    test.assertEqual(Array.create(2, 4, 8, 16, 18, 20, 10, 12, 14, 6), Iterable.traverse(Iterable.create(2), getNextValues));
                });

                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Iterable.traverse(Iterable.create(2), (Function1<Integer,Iterable<Integer>>)null), new PreConditionFailure("getNextValues cannot be null."));
                });
            });
        });
    }

    static void test(TestRunner runner, Function1<Integer,? extends Iterable<Integer>> createIterable)
    {
        runner.testGroup(Iterable.class, () ->
        {
            runner.testGroup("toArray()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertEqual(Array.create(), iterable.toArray());
                    }
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(Array.create(0), iterable.toArray());
                });

                runner.test("with two values", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    test.assertEqual(Array.create(0, 1), iterable.toArray());
                });
            });

            runner.testGroup("toList()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertEqual(List.create(), iterable.toList());
                    }
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(List.create(0), iterable.toList());
                });

                runner.test("with two values", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    test.assertEqual(List.create(0, 1), iterable.toList());
                });
            });

            runner.testGroup("toSet()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertEqual(Set.create(), iterable.toSet());
                    }
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(Set.create(0), iterable.toSet());
                });

                runner.test("with two values", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    test.assertEqual(Set.create(0, 1), iterable.toSet());
                });

                runner.test("with repeated values", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(10)
                        .map((Integer value) -> value % 3);
                    test.assertEqual(Set.create(0, 1, 2), iterable.toSet());
                });
            });

            runner.testGroup("toMap(Function1<T,K>,Function1<T,V>)", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertEqual(
                            Map.<Integer,String>create(),
                            iterable.toMap(i -> i, Integers::toString));
                    }
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(
                        ListMap.create(MapEntry.create(0, "0")),
                        iterable.toMap(i -> i, Integers::toString));
                });

                runner.test("with two values", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    test.assertEqual(
                        ListMap.create(
                            MapEntry.create("0", 0),
                            MapEntry.create("1", 1)),
                        iterable.toMap(Integers::toString, i -> i));
                });

                runner.test("with repeated values", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(10);
                    test.assertEqual(
                        ListMap.create(
                            MapEntry.create(0, 9),
                            MapEntry.create(1, 7),
                            MapEntry.create(2, 8)),
                        iterable.toMap(i -> i % 3, i -> i));
                });
            });

            runner.testGroup("iterate()", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterator<Integer> iterator = iterable.iterate();
                        test.assertNotNull(iterator);
                        test.assertFalse(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                        test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));

                        test.assertFalse(iterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                        test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                    }
                });

                runner.test("with non-empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    final Iterator<Integer> iterator = iterable.iterate();
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));

                    for (int i = 0; i < iterable.getCount(); ++i) {
                        test.assertTrue(iterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertTrue(iterator.hasCurrent());
                        test.assertEqual(i, iterator.getCurrent());
                    }

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent, new PreConditionFailure("this.hasCurrent() cannot be false."));
                });
            });

            runner.testGroup("for each", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        int elementCount = 0;
                        for (final Integer element : iterable)
                        {
                            test.assertEqual(elementCount, element);
                            ++elementCount;
                        }

                        test.assertEqual(0, elementCount);
                    }
                });

                runner.test("with non-empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(10);

                    int i = 0;
                    for (final Integer element : iterable)
                    {
                        test.assertEqual(i, element);
                        ++i;
                    }

                    test.assertEqual(iterable.getCount(), i);
                });
            });

            runner.testGroup("any()", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertFalse(iterable.any());
                    }
                });

                runner.test("with non-empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(110);
                    test.assertTrue(iterable.any());
                });
            });

            runner.testGroup("first()", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.first().await(),
                            new EmptyException());
                    }
                });

                runner.test("with non-empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(3);
                    test.assertEqual(0, iterable.first().await());
                });

                runner.test("with empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.first(null),
                            new PreConditionFailure("condition cannot be null."));
                    }
                });

                runner.test("with non-empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertThrows(() -> iterable.first(null),
                        new PreConditionFailure("condition cannot be null."));
                });

                runner.test("with empty Iterable and non-null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.first(Math::isOdd).await(),
                            new NotFoundException("Could not find a value in this Iterable that matches the provided condition."));
                    }
                });

                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertThrows(() -> iterable.first(Math::isOdd).await(),
                        new NotFoundException("Could not find a value in this Iterable that matches the provided condition."));
                });

                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(0, iterable.first(Math::isEven).await());
                });
            });

            runner.testGroup("last()", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.last().await(),
                            new EmptyException());
                    }
                });

                runner.test("with non-empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(3);
                    test.assertEqual(2, iterable.last().await());
                });

                runner.test("with empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.last(null), new PreConditionFailure("condition cannot be null."));
                    }
                });

                runner.test("with non-empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertThrows(() -> iterable.last(null), new PreConditionFailure("condition cannot be null."));
                });

                runner.test("with empty Iterable and non-null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.last(Math::isOdd).await(),
                            new NotFoundException("Could not find a value in this Iterable that matches the provided condition."));
                    }
                });

                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertThrows(() -> iterable.last(Math::isOdd).await(),
                        new NotFoundException("Could not find a value in this Iterable that matches the provided condition."));
                });

                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(3, iterable.last(Math::isOdd).await());
                });
            });

            runner.testGroup("take()", () ->
            {
                runner.test("with empty Iterable and negative toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.take(-1), new PreConditionFailure("toTake (-1) must be greater than or equal to 0."));
                    }
                });

                runner.test("with empty Iterable and zero toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.take(0));
                    }
                });

                runner.test("with empty Iterable and positive toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.take(3));
                    }
                });

                runner.test("with non-empty Iterable and negative toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertThrows(() -> iterable.take(-1), new PreConditionFailure("toTake (-1) must be greater than or equal to 0."));
                });

                runner.test("with non-empty Iterable and zero toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(), iterable.take(0));
                });

                runner.test("with non-empty Iterable and positive less than Iterable count toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(0, 1, 2), iterable.take(3));
                });

                runner.test("with non-empty Iterable and positive equal to Iterable count toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(0, 1, 2, 3), iterable.take(iterable.getCount()));
                });

                runner.test("with non-empty Iterable and positive greater than Iterable count toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(0, 1, 2, 3), iterable.take(14));
                });
            });

            runner.testGroup("takeLast()", () ->
            {
                runner.test("with empty Iterable and negative toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.takeLast(-1), new PreConditionFailure("toTake (-1) must be greater than or equal to 0."));
                    }
                });

                runner.test("with empty Iterable and zero toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.takeLast(0));
                    }
                });

                runner.test("with empty Iterable and positive toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.takeLast(3));
                    }
                });

                runner.test("with non-empty Iterable and negative toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertThrows(() -> iterable.takeLast(-1), new PreConditionFailure("toTake (-1) must be greater than or equal to 0."));
                });

                runner.test("with non-empty Iterable and zero toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(), iterable.takeLast(0));
                });

                runner.test("with non-empty Iterable and positive less than Iterable count toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(1, 2, 3), iterable.takeLast(3));
                });

                runner.test("with non-empty Iterable and positive equal to Iterable count toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> takeIterable = iterable.takeLast(iterable.getCount());
                    test.assertTrue(takeIterable.any());
                    test.assertEqual(4, takeIterable.getCount());

                    final Iterator<Integer> takeIterator = takeIterable.iterate();
                    test.assertTrue(takeIterator.any());
                    test.assertEqual(4, takeIterator.getCount());
                });

                runner.test("with non-empty Iterable and positive greater than Iterable count toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> takeIterable = iterable.takeLast(14);
                    test.assertTrue(takeIterable.any());
                    test.assertEqual(4, takeIterable.getCount());

                    final Iterator<Integer> takeIterator = takeIterable.iterate();
                    test.assertTrue(takeIterator.any());
                    test.assertEqual(4, takeIterator.getCount());
                });
            });

            runner.testGroup("contains()", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable);

                        test.assertFalse(iterable.contains(3));
                    }
                });

                runner.test("with non-empty Iterable and not found value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    test.assertEqual(Iterable.create(0, 1), iterable);

                    test.assertFalse(iterable.contains(3));
                });

                runner.test("with non-empty Iterable and found value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), iterable);

                    test.assertTrue(iterable.contains(3));
                });

                runner.test("with empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable);
                        test.assertFalse(iterable.contains(Math::isOdd));
                    }
                });

                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(Iterable.create(0), iterable);
                    test.assertFalse(iterable.contains(Math::isOdd));
                });

                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), iterable);
                    test.assertTrue(iterable.contains(Math::isOdd));
                });
            });

            runner.testGroup("skip()", () ->
            {
                runner.test("with empty Iterable and negative toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.skip(-1), new PreConditionFailure("toSkip (-1) must be greater than or equal to 0."));
                    }
                });

                runner.test("with empty Iterable and zero toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.skip(0));
                    }
                });

                runner.test("with empty Iterable and positive toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.skip(3));
                    }
                });

                runner.test("with non-empty Iterable and negative toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertThrows(() -> iterable.skip(-1), new PreConditionFailure("toSkip (-1) must be greater than or equal to 0."));
                });

                runner.test("with non-empty Iterable and zero toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(0, 1, 2, 3), iterable.skip(0));
                });

                runner.test("with non-empty Iterable and positive less than Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(3), iterable.skip(3));
                });

                runner.test("with non-empty Iterable and positive equal to Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(), iterable.skip(iterable.getCount()));
                });

                runner.test("with non-empty Iterable and positive greater than Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(), iterable.skip(14));
                });
            });

            runner.testGroup("skipFirst()", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.skipFirst());
                    }
                });

                runner.test("with one-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(Iterable.create(), iterable.skipFirst());
                });

                runner.test("with more than one valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    test.assertEqual(Iterable.create(1), iterable.skipFirst());
                });
            });

            runner.testGroup("skipLast()", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.skipLast());
                    }
                });

                runner.test("with one-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(Iterable.create(), iterable.skipLast());
                });

                runner.test("with more than one valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    test.assertEqual(Iterable.create(0), iterable.skipLast());
                });

                runner.test("with empty Iterable and negative toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.skipLast(-3), new PreConditionFailure("toSkip (-3) must be greater than or equal to 0."));
                    }
                });

                runner.test("with empty Iterable and zero toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.skipLast(0));
                    }
                });

                runner.test("with empty Iterable and positive toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.skipLast(10));
                    }
                });

                runner.test("with non-empty Iterable and negative toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertThrows(() -> iterable.skipLast(-5), new PreConditionFailure("toSkip (-5) must be greater than or equal to 0."));
                });

                runner.test("with non-empty Iterable and zero toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(Iterable.create(0, 1, 2, 3, 4), iterable.skipLast(0));
                });

                runner.test("with non-empty Iterable and positive less than Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(Iterable.create(0), iterable.skipLast(4));
                });

                runner.test("with non-empty Iterable and positive equal to Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(Iterable.create(), iterable.skipLast(5));
                });

                runner.test("with non-empty Iterable and positive greater than Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(Iterable.create(), iterable.skipLast(6));
                });
            });

            runner.testGroup("skipUntil()", () ->
            {
                runner.test("with empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.skipUntil(null), new PreConditionFailure("condition cannot be null."));
                    }
                });

                runner.test("with empty Iterable and non-null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.skipUntil(Math::isOdd));
                    }
                });

                runner.test("with non-empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertThrows(() -> iterable.skipUntil(null), new PreConditionFailure("condition cannot be null."));
                });

                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(), iterable.skipUntil(value -> value > 10));
                });

                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(1, 2, 3), iterable.skipUntil(Math::isOdd));
                });
            });

            runner.testGroup("where()", () ->
            {
                runner.test("with empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.where(null), new PreConditionFailure("condition cannot be null."));
                    }
                });

                runner.test("with empty Iterable and non-null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Integer> whereIterable = iterable.where(Math::isOdd);
                        test.assertFalse(whereIterable.any());
                        test.assertEqual(0, whereIterable.getCount());

                        final Iterator<Integer> whereIterator = whereIterable.iterate();
                        test.assertFalse(whereIterator.any());
                        test.assertEqual(0, whereIterator.getCount());
                    }
                });

                runner.test("with non-empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    test.assertThrows(() -> iterable.where(null), new PreConditionFailure("condition cannot be null."));
                });

                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> takeIterable = iterable.where(value -> value != null && value > 10);
                    test.assertEqual(Iterable.create(), takeIterable);
                });

                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> whereIterable = iterable.where(Math::isOdd);
                    test.assertEqual(Iterable.create(1, 3), whereIterable);
                });
            });

            runner.testGroup("map()", () ->
            {
                runner.test("with empty Iterable and null conversion", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertThrows(() -> iterable.map(null), new PreConditionFailure("conversion cannot be null."));
                    }
                });

                runner.test("with empty Iterable and non-null conversion", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.map(Math::isOdd));
                    }
                });

                runner.test("with non-empty Iterable and null conversion", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertThrows(() -> iterable.map(null), new PreConditionFailure("conversion cannot be null."));
                });

                runner.test("with non-empty Iterable and non-null conversion", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertEqual(Iterable.create(false, true, false, true), iterable.map(Math::isOdd));
                });
            });

            runner.testGroup("instanceOf()", () ->
            {
                runner.test("with empty Iterable and wrong type", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.instanceOf(Boolean.class));
                    }
                });

                runner.test("with empty Iterable and correct type", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.instanceOf(Number.class));
                    }
                });

                runner.test("with non-empty Iterable and no isMatch", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(Iterable.create(), iterable.instanceOf(Number.class));
                    }
                });

                runner.test("with non-empty Iterable and isMatch", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    test.assertEqual(Iterable.create(0, 1, 2, 3), iterable.instanceOf(Integer.class));
                });
            });

            runner.testGroup("equals()", () ->
            {
                runner.test("with empty Iterable and null", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        test.assertFalse(iterable.equals((Object) null));
                        test.assertFalse(iterable.equals((Iterable<Integer>) null));
                    }
                });

                runner.test("with empty Iterable and empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create();
                        test.assertTrue(iterable.equals((Object) rhs));
                        test.assertTrue(iterable.equals(rhs));
                    }
                });

                runner.test("with empty Iterable and one-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create(0);
                        test.assertFalse(iterable.equals((Object)rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with empty Iterable and two-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create(0, 1);
                        test.assertFalse(iterable.equals((Object)rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with one-valued iterable and null", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    if (iterable != null)
                    {
                        test.assertFalse(iterable.equals((Object) null));
                        test.assertFalse(iterable.equals((Iterable<Integer>) null));
                    }
                });

                runner.test("with one-valued Iterable and empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create();
                        test.assertFalse(iterable.equals((Object) rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with one-valued Iterable and equal one-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create(0);
                        test.assertTrue(iterable.equals((Object)rhs));
                        test.assertTrue(iterable.equals(rhs));
                    }
                });

                runner.test("with one-valued Iterable and two-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create(0, 1);
                        test.assertFalse(iterable.equals((Object)rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with two-valued Iterable and null", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    if (iterable != null)
                    {
                        test.assertFalse(iterable.equals((Object)null));
                        test.assertFalse(iterable.equals((Iterable<Integer>) null));
                    }
                });

                runner.test("with two-valued Iterable and empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create();
                        test.assertFalse(iterable.equals((Object) rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with two-valued Iterable and one-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create(0);
                        test.assertFalse(iterable.equals((Object)rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with two-valued Iterable and equal two-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create(0, 1);
                        test.assertTrue(iterable.equals((Object)rhs));
                        test.assertTrue(iterable.equals(rhs));
                    }
                });

                runner.test("with two-valued Iterable and different two-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Iterable.create(2, 3);
                        test.assertFalse(iterable.equals((Object)rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final String value = iterable.toString();
                        test.assertNotNullAndNotEmpty(value);
                        test.assertEqual("", value.substring(1, value.length() - 1));
                    }
                });

                runner.test("with single-value Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    final String value = iterable.toString();
                    test.assertEqual("0", value.substring(1, value.length() - 1));
                });

                runner.test("with multiple-value Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    final String value = iterable.toString();
                    test.assertEqual("0,1,2,3", value.substring(1, value.length() - 1));
                });
            });

            runner.testGroup("order(Function2<T,T,Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Iterable<Integer> values = Iterable.create();
                    test.assertThrows(() -> values.order((Function2<Integer,Integer,Integer>)null),
                        new PreConditionFailure("comparer cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Iterable<Integer> values = Iterable.create();
                    final Iterable<Integer> orderedValues = values.order(Integer::compare);
                    test.assertNotSame(values, orderedValues);
                    test.assertEqual(values, orderedValues);
                });

                runner.test("with one value", (Test test) ->
                {
                    final Iterable<Integer> values = Iterable.create(1);
                    final Iterable<Integer> orderedValues = values.order(Integer::compare);
                    test.assertNotSame(values, orderedValues);
                    test.assertEqual(values, orderedValues);
                });

                runner.test("with two values in sorted order", (Test test) ->
                {
                    final Iterable<Integer> values = Iterable.create(1, 13);
                    final Iterable<Integer> orderedValues = values.order(Integer::compare);
                    test.assertNotSame(values, orderedValues);
                    test.assertEqual(values, orderedValues);
                });

                runner.test("with two values in reverse-sorted order", (Test test) ->
                {
                    final Iterable<Integer> values = Iterable.create(10, 1);
                    final Iterable<Integer> orderedValues = values.order(Integer::compare);
                    test.assertNotSame(values, orderedValues);
                    test.assertEqual(Iterable.create(10, 1), values);
                    test.assertEqual(Iterable.create(1, 10), orderedValues);
                });

                runner.test("with three values in sorted order", (Test test) ->
                {
                    final Iterable<Integer> values = Iterable.create(12, 13, 528);
                    final Iterable<Integer> orderedValues = values.order(Integer::compare);
                    test.assertNotSame(values, orderedValues);
                    test.assertEqual(values, orderedValues);
                });

                runner.test("with three values in reverse-sorted order", (Test test) ->
                {
                    final Iterable<Integer> values = Iterable.create(2640, 10, 1);
                    final Iterable<Integer> orderedValues = values.order(Integer::compare);
                    test.assertNotSame(values, orderedValues);
                    test.assertEqual(Iterable.create(2640, 10, 1), values);
                    test.assertEqual(Iterable.create(1, 10, 2640), orderedValues);
                });

                runner.test("with three values in mixed-sorted order", (Test test) ->
                {
                    final Iterable<Integer> values = Iterable.create(120, 10560, 1);
                    final Iterable<Integer> orderedValues = values.order(Integer::compare);
                    test.assertNotSame(values, orderedValues);
                    test.assertEqual(Iterable.create(120, 10560, 1), values);
                    test.assertEqual(Iterable.create(1, 120, 10560), orderedValues);
                });
            });
        });
    }
}
