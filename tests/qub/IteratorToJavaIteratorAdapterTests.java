package qub;

public interface IteratorToJavaIteratorAdapterTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IteratorToJavaIteratorAdapter.class, () ->
        {
            runner.testGroup("create(Iterator<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> IteratorToJavaIteratorAdapter.create(null),
                        new PreConditionFailure("iterator cannot be null."));
                });

                runner.test("with empty not-started", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create();

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertNotNull(javaIterator);
                    test.assertFalse(iterator.hasStarted());

                    test.assertFalse(javaIterator.hasNext());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());

                    test.assertThrows(() -> javaIterator.next(),
                        new PreConditionFailure("this.hasNext() cannot be false."));
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());

                    test.assertFalse(javaIterator.hasNext());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });

                runner.test("with empty started", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create();
                    iterator.next();

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertNotNull(javaIterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());

                    test.assertFalse(javaIterator.hasNext());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());

                    test.assertThrows(() -> javaIterator.next(),
                        new PreConditionFailure("this.hasNext() cannot be false."));
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());

                    test.assertFalse(javaIterator.hasNext());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });

                runner.test("with not-empty not-started", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create(1);

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertNotNull(javaIterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());

                    test.assertTrue(javaIterator.hasNext());
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertEqual(1, iterator.getCurrent());

                    test.assertEqual(1, javaIterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertEqual(1, iterator.getCurrent());

                    test.assertFalse(javaIterator.hasNext());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });

                runner.test("with not-empty started", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create(1);
                    iterator.next();

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertNotNull(javaIterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertEqual(1, iterator.getCurrent());

                    test.assertTrue(javaIterator.hasNext());
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertEqual(1, iterator.getCurrent());

                    test.assertEqual(1, javaIterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertEqual(1, iterator.getCurrent());

                    test.assertFalse(javaIterator.hasNext());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });
            });
            
            runner.testGroup("hasNext()", () ->
            {
                runner.test("with non-started empty iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create();
                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertFalse(iterator.hasStarted());

                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertFalse(javaIterator.hasNext());
                        test.assertTrue(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                    }
                });
                
                runner.test("with started empty iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create();
                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertTrue(iterator.hasStarted());

                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertFalse(javaIterator.hasNext());
                        test.assertTrue(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                    }
                });
                
                runner.test("with non-started non-empty iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create(1, 2, 3);
                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertFalse(iterator.hasStarted());

                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertTrue(javaIterator.hasNext());
                        test.assertTrue(iterator.hasStarted());
                        test.assertTrue(iterator.hasCurrent());
                        test.assertEqual(1, iterator.getCurrent());
                    }
                });
                
                runner.test("with started non-empty iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create(1, 2);
                    test.assertTrue(iterator.next());
                    test.assertTrue(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertTrue(iterator.hasStarted());

                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertTrue(javaIterator.hasNext());
                        test.assertTrue(iterator.hasStarted());
                        test.assertTrue(iterator.hasCurrent());
                        test.assertEqual(1, iterator.getCurrent());
                    }
                });
            });
            
            runner.testGroup("next()", () ->
            {
                runner.test("with non-started empty iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create();
                    test.assertFalse(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertFalse(iterator.hasStarted());

                    test.assertThrows(javaIterator::next,
                        new PreConditionFailure("this.hasNext() cannot be false."));
                });
                
                runner.test("with started empty iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create();
                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertTrue(iterator.hasStarted());

                    test.assertThrows(javaIterator::next,
                        new PreConditionFailure("this.hasNext() cannot be false."));
                });
                
                runner.test("with non-started non-empty iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create(0, 1, 2, 3, 4);
                    test.assertFalse(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertFalse(iterator.hasStarted());

                    for (int i = 0; i < 5; ++i)
                    {
                        test.assertEqual(i, javaIterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertTrue(iterator.hasCurrent());
                        test.assertEqual(i, iterator.getCurrent());
                    }

                    test.assertThrows(javaIterator::next,
                        new PreConditionFailure("this.hasNext() cannot be false."));
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });

                runner.test("with started non-empty iterator", (Test test) ->
                {
                    final Iterator<Integer> iterator = Iterator.create(0, 1, 2, 3, 4);
                    test.assertTrue(iterator.next());
                    test.assertTrue(iterator.hasStarted());

                    final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                    test.assertTrue(iterator.hasStarted());

                    for (int i = 0; i < 5; ++i)
                    {
                        test.assertEqual(i, javaIterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertTrue(iterator.hasCurrent());
                        test.assertEqual(i, iterator.getCurrent());
                    }

                    test.assertThrows(javaIterator::next,
                        new PreConditionFailure("this.hasNext() cannot be false."));
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });
            });

            runner.test("remove()", (Test test) ->
            {
                final Array<Integer> array = Array.createWithLength(5);
                for (int i = 0; i < array.getCount(); ++i)
                {
                    array.set(i, i);
                }

                final Iterator<Integer> iterator = ArrayIterator.create(array);
                final IteratorToJavaIteratorAdapter<Integer> javaIterator = IteratorToJavaIteratorAdapter.create(iterator);
                javaIterator.remove();
            });
        });
    }
}
