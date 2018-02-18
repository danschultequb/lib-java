package qub;

public class IterableTests
{
    public static void test(final TestRunner runner, final Function1<Integer,Iterable<Integer>> createIterable)
    {
        runner.testGroup("Iterable<T>", () ->
        {
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
                        test.assertNull(iterator.getCurrent());

                        test.assertFalse(iterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                        test.assertNull(iterator.getCurrent());
                    }
                });
                
                runner.test("with non-empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    final Iterator<Integer> iterator = iterable.iterate();
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertNull(iterator.getCurrent());

                    for (int i = 0; i < iterable.getCount(); ++i) {
                        test.assertTrue(iterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertTrue(iterator.hasCurrent());
                        test.assertEqual(i, iterator.getCurrent());
                    }

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertNull(iterator.getCurrent());
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
                        test.assertNull(iterable.first());
                    }
                });
                
                runner.test("with non-empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(3);
                    test.assertEqual(0, iterable.first());
                });
                
                runner.test("with empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertNull(iterable.first(null));
                    }
                });
                
                runner.test("with non-empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertNull(iterable.first(null));
                });
                
                runner.test("with empty Iterable and non-null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertNull(iterable.first(Math.isOdd));
                    }
                });
                
                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertNull(iterable.first(Math.isOdd));
                });
                
                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(0, iterable.first(Math.isEven));
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
                        test.assertNull(iterable.first());
                    }
                });
                
                runner.test("with non-empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(3);
                    test.assertEqual(2, iterable.last());
                });
                
                runner.test("with empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertNull(iterable.last(null));
                    }
                });
                
                runner.test("with non-empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    test.assertNull(iterable.last(null));
                });
                
                runner.test("with empty Iterable and non-null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertNull(iterable.last(Math.isOdd));
                    }
                });
                
                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertNull(iterable.last(Math.isOdd));
                });
                
                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(3, iterable.last(Math.isOdd));
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
                        final Iterable<Integer> takeIterable = iterable.take(-1);
                        test.assertFalse(takeIterable.any());
                        test.assertEqual(0, takeIterable.getCount());

                        final Iterator<Integer> takeIterator = takeIterable.iterate();
                        test.assertFalse(takeIterator.any());
                        test.assertEqual(0, takeIterator.getCount());
                    }
                });
                
                runner.test("with empty Iterable and zero toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Integer> takeIterable = iterable.take(0);
                        test.assertFalse(takeIterable.any());
                        test.assertEqual(0, takeIterable.getCount());

                        final Iterator<Integer> takeIterator = takeIterable.iterate();
                        test.assertFalse(takeIterator.any());
                        test.assertEqual(0, takeIterator.getCount());
                    }
                });
                
                runner.test("with empty Iterable and positive toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Integer> takeIterable = iterable.take(3);
                        test.assertFalse(takeIterable.any());
                        test.assertEqual(0, takeIterable.getCount());

                        final Iterator<Integer> takeIterator = takeIterable.iterate();
                        test.assertFalse(takeIterator.any());
                        test.assertEqual(0, takeIterator.getCount());
                    }
                });
                
                runner.test("with non-empty Iterable and negative toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    final Iterable<Integer> takeIterable = iterable.take(-1);
                    test.assertFalse(takeIterable.any());
                    test.assertEqual(0, takeIterable.getCount());

                    final Iterator<Integer> takeIterator = takeIterable.iterate();
                    test.assertFalse(takeIterator.any());
                    test.assertEqual(0, takeIterator.getCount());
                });
                
                runner.test("with non-empty Iterable and zero toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> takeIterable = iterable.take(0);
                    test.assertFalse(takeIterable.any());
                    test.assertEqual(0, takeIterable.getCount());

                    final Iterator<Integer> takeIterator = takeIterable.iterate();
                    test.assertFalse(takeIterator.any());
                    test.assertEqual(0, takeIterator.getCount());
                });
                
                runner.test("with non-empty Iterable and positive less than Iterable count toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> takeIterable = iterable.take(3);
                    test.assertTrue(takeIterable.any());
                    test.assertEqual(3, takeIterable.getCount());

                    final Iterator<Integer> takeIterator = takeIterable.iterate();
                    test.assertTrue(takeIterator.any());
                    test.assertEqual(3, takeIterator.getCount());
                });
                
                runner.test("with non-empty Iterable and positive equal to Iterable count toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> takeIterable = iterable.take(iterable.getCount());
                    test.assertTrue(takeIterable.any());
                    test.assertEqual(4, takeIterable.getCount());

                    final Iterator<Integer> takeIterator = takeIterable.iterate();
                    test.assertTrue(takeIterator.any());
                    test.assertEqual(4, takeIterator.getCount());
                });
                
                runner.test("with non-empty Iterable and positive greater than Iterable count toTake value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> takeIterable = iterable.take(14);
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
                        test.assertEqual(new int[0], Array.toIntArray(iterable));

                        test.assertFalse(iterable.contains(3));
                    }
                });
                
                runner.test("with non-empty Iterable and not found value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    test.assertEqual(new int[] { 0, 1 }, Array.toIntArray(iterable));

                    test.assertFalse(iterable.contains(3));
                });
                
                runner.test("with non-empty Iterable and found value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(new int[] { 0, 1, 2, 3, 4 }, Array.toIntArray(iterable));

                    test.assertTrue(iterable.contains(3));
                });
                
                runner.test("with empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        test.assertEqual(new int[0], Array.toIntArray(iterable));

                        test.assertFalse(iterable.contains(Math.isOdd));
                    }
                });
                
                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    test.assertEqual(new int[] { 0 }, Array.toIntArray(iterable));

                    test.assertFalse(iterable.contains(Math.isOdd));
                });
                
                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    test.assertEqual(new int[] { 0, 1, 2, 3, 4 }, Array.toIntArray(iterable));

                    test.assertTrue(iterable.contains(Math.isOdd));
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
                        final Iterable<Integer> skipIterable = iterable.skip(-1);
                        test.assertSame(iterable, skipIterable);
                    }
                });
                
                runner.test("with empty Iterable and zero toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Integer> skipIterable = iterable.skip(0);
                        test.assertSame(iterable, skipIterable);
                    }
                });
                
                runner.test("with empty Iterable and positive toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Integer> skipIterable = iterable.skip(3);
                        test.assertFalse(skipIterable.any());
                        test.assertEqual(0, skipIterable.getCount());

                        final Iterator<Integer> skipIterator = skipIterable.iterate();
                        test.assertFalse(skipIterator.any());
                        test.assertEqual(0, skipIterator.getCount());
                    }
                });
                
                runner.test("with non-empty Iterable and negative toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);
                    final Iterable<Integer> skipIterable = iterable.skip(-1);
                    test.assertSame(iterable, skipIterable);
                });
                
                runner.test("with non-empty Iterable and zero toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> skipIterable = iterable.skip(0);
                    test.assertSame(iterable, skipIterable);
                });
                
                runner.test("with non-empty Iterable and positive less than Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> skipIterable = iterable.skip(3);
                    test.assertTrue(skipIterable.any());
                    test.assertEqual(1, skipIterable.getCount());

                    final Iterator<Integer> skipIterator = skipIterable.iterate();
                    test.assertTrue(skipIterator.any());
                    test.assertEqual(1, skipIterator.getCount());
                });
                
                runner.test("with non-empty Iterable and positive equal to Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> skipIterable = iterable.skip(iterable.getCount());
                    test.assertFalse(skipIterable.any());
                    test.assertEqual(0, skipIterable.getCount());

                    final Iterator<Integer> skipIterator = skipIterable.iterate();
                    test.assertFalse(skipIterator.any());
                    test.assertEqual(0, skipIterator.getCount());
                });
                
                runner.test("with non-empty Iterable and positive greater than Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> skipIterable = iterable.skip(14);
                    test.assertFalse(skipIterable.any());
                    test.assertEqual(0, skipIterable.getCount());

                    final Iterator<Integer> skipIterator = skipIterable.iterate();
                    test.assertFalse(skipIterator.any());
                    test.assertEqual(0, skipIterator.getCount());
                });
            });
            
            runner.testGroup("skipLast()", () ->
            {
                runner.test("with empty Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final Iterable<Integer> skipIterable = iterable.skipLast();
                        test.assertFalse(skipIterable.any());
                    }
                });
                
                runner.test("with one-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    final Iterable<Integer> skipIterable = iterable.skipLast();
                    test.assertFalse(skipIterable.any());
                });
                
                runner.test("with more than one valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    final Iterable<Integer> skipIterable = iterable.skipLast();
                    test.assertTrue(skipIterable.any());
                    test.assertEqual(1, skipIterable.getCount());
                    test.assertEqual(0, skipIterable.first());
                });
                
                runner.test("with empty Iterable and negative toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final Iterable<Integer> skipIterable = iterable.skipLast(-3);
                        test.assertSame(iterable, skipIterable);
                    }
                });
                
                runner.test("with empty Iterable and zero toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final Iterable<Integer> skipIterable = iterable.skipLast(0);
                        test.assertSame(iterable, skipIterable);
                    }
                });
                
                runner.test("with empty Iterable and positive toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final Iterable<Integer> skipIterable = iterable.skipLast(10);
                        test.assertNotSame(iterable, skipIterable);
                        test.assertFalse(skipIterable.any());
                    }
                });

                runner.test("with non-empty Iterable and negative toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    final Iterable<Integer> skipIterable = iterable.skipLast(-5);
                    test.assertSame(iterable, skipIterable);
                });

                runner.test("with non-empty Iterable and zero toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    final Iterable<Integer> skipIterable = iterable.skipLast(0);
                    test.assertSame(iterable, skipIterable);
                });

                runner.test("with non-empty Iterable and positive less than Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    final Iterable<Integer> skipIterable = iterable.skipLast(4);
                    test.assertTrue(skipIterable.any());
                    test.assertEqual(1, skipIterable.getCount());
                    test.assertEqual(0, skipIterable.first());
                });

                runner.test("with non-empty Iterable and positive equal to Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    final Iterable<Integer> skipIterable = iterable.skipLast(5);
                    test.assertFalse(skipIterable.any());
                    test.assertEqual(0, skipIterable.getCount());
                });

                runner.test("with non-empty Iterable and positive greater than Iterable count toSkip value", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(5);
                    final Iterable<Integer> skipIterable = iterable.skipLast(6);
                    test.assertFalse(skipIterable.any());
                    test.assertEqual(0, skipIterable.getCount());
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
                        final Iterable<Integer> skipUntilIterable = iterable.skipUntil(null);
                        test.assertFalse(skipUntilIterable.any());
                        test.assertEqual(0, skipUntilIterable.getCount());

                        final Iterator<Integer> skipUntilIterator = skipUntilIterable.iterate();
                        test.assertFalse(skipUntilIterator.any());
                        test.assertEqual(0, skipUntilIterator.getCount());
                    }
                });

                runner.test("with empty Iterable and non-null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Integer> skipUntilIterable = iterable.skipUntil(Math.isOdd);
                        test.assertFalse(skipUntilIterable.any());
                        test.assertEqual(0, skipUntilIterable.getCount());

                        final Iterator<Integer> skipUntilIterator = skipUntilIterable.iterate();
                        test.assertFalse(skipUntilIterator.any());
                        test.assertEqual(0, skipUntilIterator.getCount());
                    }
                });

                runner.test("with non-empty Iterable and null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> skipUntilIterable = iterable.skipUntil(null);
                    test.assertFalse(skipUntilIterable.any());
                    test.assertEqual(0, skipUntilIterable.getCount());

                    final Iterator<Integer> skipUntilIterator = skipUntilIterable.iterate();
                    test.assertFalse(skipUntilIterator.any());
                    test.assertEqual(0, skipUntilIterator.getCount());
                });

                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> skipUntilIterable = iterable.skipUntil(value -> value != null && value > 10);
                    test.assertFalse(skipUntilIterable.any());
                    test.assertEqual(0, skipUntilIterable.getCount());

                    final Iterator<Integer> skipUntilIterator = skipUntilIterable.iterate();
                    test.assertFalse(skipUntilIterator.any());
                    test.assertEqual(0, skipUntilIterator.getCount());
                });

                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> skipUntilIterable = iterable.skipUntil(Math.isOdd);
                    test.assertTrue(skipUntilIterable.any());
                    test.assertEqual(3, skipUntilIterable.getCount());

                    final Iterator<Integer> skipUntilIterator = skipUntilIterable.iterate();
                    test.assertTrue(skipUntilIterator.any());
                    test.assertEqual(3, skipUntilIterator.getCount());
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
                        final Iterable<Integer> takeIterable = iterable.where(null);
                        test.assertSame(iterable, takeIterable);
                    }
                });

                runner.test("with empty Iterable and non-null condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Integer> whereIterable = iterable.where(Math.isOdd);
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

                    final Iterable<Integer> whereIterable = iterable.where(null);
                    test.assertSame(iterable, whereIterable);
                });

                runner.test("with non-empty Iterable and non-matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> takeIterable = iterable.where(value -> value != null && value > 10);
                    test.assertFalse(takeIterable.any());
                    test.assertEqual(0, takeIterable.getCount());

                    final Iterator<Integer> takeIterator = takeIterable.iterate();
                    test.assertFalse(takeIterator.any());
                    test.assertEqual(0, takeIterator.getCount());
                });

                runner.test("with non-empty Iterable and matching condition", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> whereIterable = iterable.where(Math.isOdd);
                    test.assertTrue(whereIterable.any());
                    test.assertEqual(2, whereIterable.getCount());

                    final Iterator<Integer> whereIterator = whereIterable.iterate();
                    test.assertTrue(whereIterator.any());
                    test.assertEqual(2, whereIterator.getCount());
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
                        final Iterable<Boolean> mapIterable = iterable.map(null);
                        test.assertFalse(mapIterable.any());
                        test.assertEqual(0, mapIterable.getCount());

                        final Iterator<Boolean> mapIterator = mapIterable.iterate();
                        test.assertFalse(mapIterator.any());
                        test.assertEqual(0, mapIterator.getCount());
                    }
                });

                runner.test("with empty Iterable and non-null conversion", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Boolean> mapIterable = iterable.map(Math.isOdd);
                        test.assertFalse(mapIterable.any());
                        test.assertEqual(0, mapIterable.getCount());

                        final Iterator<Boolean> mapIterator = mapIterable.iterate();
                        test.assertFalse(mapIterator.any());
                        test.assertEqual(0, mapIterator.getCount());
                    }
                });

                runner.test("with non-empty Iterable and null conversion", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Boolean> mapIterable = iterable.map(null);
                    test.assertFalse(mapIterable.any());
                    test.assertEqual(0, mapIterable.getCount());

                    final Iterator<Boolean> mapIterator = mapIterable.iterate();
                    test.assertFalse(mapIterator.any());
                    test.assertEqual(0, mapIterator.getCount());
                });

                runner.test("with non-empty Iterable and non-null conversion", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Boolean> mapIterable = iterable.map(Math.isOdd);
                    test.assertTrue(mapIterable.any());
                    test.assertEqual(4, mapIterable.getCount());

                    final Iterator<Boolean> mapIterator = mapIterable.iterate();
                    test.assertTrue(mapIterator.any());
                    test.assertEqual(4, mapIterator.getCount());
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
                        final Iterable<Boolean> instanceOfIterable = iterable.instanceOf(Boolean.class);
                        test.assertFalse(instanceOfIterable.any());
                        test.assertEqual(0, instanceOfIterable.getCount());

                        final Iterator<Boolean> instanceOfIterator = instanceOfIterable.iterate();
                        test.assertFalse(instanceOfIterator.any());
                        test.assertEqual(0, instanceOfIterator.getCount());
                    }
                });

                runner.test("with empty Iterable and correct type", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Number> instanceOfIterable = iterable.instanceOf(Number.class);
                        test.assertFalse(instanceOfIterable.any());
                        test.assertEqual(0, instanceOfIterable.getCount());

                        final Iterator<Number> instanceOfIterator = instanceOfIterable.iterate();
                        test.assertFalse(instanceOfIterator.any());
                        test.assertEqual(0, instanceOfIterator.getCount());
                    }
                });

                runner.test("with non-empty Iterable and no matches", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    // Some iterables cannot be empty (such as SingleLinkNodes), so they will return null when
                    // an Iterable with 0 elements is requested.
                    if (iterable != null)
                    {
                        final Iterable<Number> instanceOfIterable = iterable.instanceOf(Number.class);
                        test.assertFalse(instanceOfIterable.any());
                        test.assertEqual(0, instanceOfIterable.getCount());

                        final Iterator<Number> instanceOfIterator = instanceOfIterable.iterate();
                        test.assertFalse(instanceOfIterator.any());
                        test.assertEqual(0, instanceOfIterator.getCount());
                    }
                });

                runner.test("with non-empty Iterable and matches", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(4);

                    final Iterable<Integer> instanceOfIterable = iterable.instanceOf(Integer.class);
                    test.assertTrue(instanceOfIterable.any());
                    test.assertEqual(4, instanceOfIterable.getCount());

                    final Iterator<Integer> instanceOfIterator = instanceOfIterable.iterate();
                    test.assertTrue(instanceOfIterator.any());
                    test.assertEqual(4, instanceOfIterator.getCount());
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
                        final Iterable<Integer> rhs = new Array<>(0);
                        test.assertTrue(iterable.equals((Object) rhs));
                        test.assertTrue(iterable.equals(rhs));
                    }
                });

                runner.test("with empty Iterable and one-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Array.fromValues(new Integer[] { 0 });
                        test.assertFalse(iterable.equals((Object)rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with empty Iterable and two-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(0);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Array.fromValues(new Integer[] { 0, 1 });
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
                        final Iterable<Integer> rhs = new Array<>(0);
                        test.assertFalse(iterable.equals((Object) rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with one-valued Iterable and equal one-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Array.fromValues(new Integer[] { 0 });
                        test.assertTrue(iterable.equals((Object)rhs));
                        test.assertTrue(iterable.equals(rhs));
                    }
                });

                runner.test("with one-valued Iterable and two-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(1);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Array.fromValues(new Integer[] { 0, 1 });
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
                        final Iterable<Integer> rhs = new Array<>(0);
                        test.assertFalse(iterable.equals((Object) rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with two-valued Iterable and one-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Array.fromValues(new Integer[] { 0 });
                        test.assertFalse(iterable.equals((Object)rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });

                runner.test("with two-valued Iterable and equal two-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Array.fromValues(new Integer[] { 0, 1 });
                        test.assertTrue(iterable.equals((Object)rhs));
                        test.assertTrue(iterable.equals(rhs));
                    }
                });

                runner.test("with two-valued Iterable and different two-valued Iterable", (Test test) ->
                {
                    final Iterable<Integer> iterable = createIterable.run(2);
                    if (iterable != null)
                    {
                        final Iterable<Integer> rhs = Array.fromValues(new Integer[] { 2, 3 });
                        test.assertFalse(iterable.equals((Object)rhs));
                        test.assertFalse(iterable.equals(rhs));
                    }
                });
            });
        });
    }
}
