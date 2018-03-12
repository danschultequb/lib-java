package qub;

public class IteratorToJavaIteratorAdapterTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(IteratorToJavaIteratorAdapter.class, () ->
        {
            runner.testGroup("hasNext()", () ->
            {
                runner.test("with non-started empty iterator", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(0);
                    final Iterator<Integer> iterator = new ArrayIterator<>(array);
                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
                    test.assertFalse(iterator.hasStarted());

                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertFalse(javaIterator.hasNext());
                    }
                });
                
                runner.test("with started empty iterator", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(0);
                    final Iterator<Integer> iterator = new ArrayIterator<>(array);
                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
                    test.assertTrue(iterator.hasStarted());

                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertFalse(javaIterator.hasNext());
                        test.assertTrue(iterator.hasStarted());
                    }
                });
                
                runner.test("with non-started non-empty iterator", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(5);
                    final Iterator<Integer> iterator = new ArrayIterator<>(array);
                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
                    test.assertFalse(iterator.hasStarted());

                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertTrue(javaIterator.hasNext());
                        test.assertTrue(iterator.hasStarted());
                    }
                });
                
                runner.test("with started non-empty iterator", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(5);
                    final Iterator<Integer> iterator = new ArrayIterator<>(array);
                    test.assertTrue(iterator.next());
                    test.assertTrue(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
                    test.assertTrue(iterator.hasStarted());

                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertTrue(javaIterator.hasNext());
                        test.assertTrue(iterator.hasStarted());
                    }
                });
            });
            
            runner.testGroup("next()", () ->
            {
                runner.test("with non-started empty iterator", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(0);
                    final Iterator<Integer> iterator = new ArrayIterator<>(array);
                    test.assertFalse(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
                    test.assertFalse(iterator.hasStarted());

                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertNull(javaIterator.next());
                    }
                });
                
                runner.test("with started empty iterator", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(0);
                    final Iterator<Integer> iterator = new ArrayIterator<>(array);
                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
                    test.assertTrue(iterator.hasStarted());

                    for (int i = 0; i < 10; ++i)
                    {
                        test.assertNull(javaIterator.next());
                    }
                });
                
                runner.test("with non-started non-empty iterator", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(5);
                    for (int i = 0; i < array.getCount(); ++i)
                    {
                        array.set(i, i);
                    }

                    final Iterator<Integer> iterator = new ArrayIterator<>(array);
                    test.assertFalse(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
                    test.assertFalse(iterator.hasStarted());

                    for (int i = 0; i < 5; ++i)
                    {
                        test.assertEqual(i, javaIterator.next());
                    }

                    test.assertNull(javaIterator.next());
                });

                runner.test("with started non-empty iterator", (Test test) ->
                {
                    final Array<Integer> array = new Array<>(5);
                    for (int i = 0; i < array.getCount(); ++i)
                    {
                        array.set(i, i);
                    }

                    final Iterator<Integer> iterator = new ArrayIterator<>(array);
                    test.assertTrue(iterator.next());
                    test.assertTrue(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
                    test.assertTrue(iterator.hasStarted());

                    for (int i = 0; i < 5; ++i)
                    {
                        test.assertEqual(i, javaIterator.next());
                    }

                    test.assertNull(javaIterator.next());
                });
            });

            runner.test("remove()", (Test test) ->
            {
                final Array<Integer> array = new Array<>(5);
                for (int i = 0; i < array.getCount(); ++i)
                {
                    array.set(i, i);
                }

                final Iterator<Integer> iterator = new ArrayIterator<>(array);
                final IteratorToJavaIteratorAdapter<Integer> javaIterator = new IteratorToJavaIteratorAdapter<>(iterator);
                javaIterator.remove();
            });
        });
    }
}
