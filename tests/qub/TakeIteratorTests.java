package qub;

public interface TakeIteratorTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(TakeIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                final int additionalValues = 5;

                final Array<Integer> array = Array.createWithLength(count + additionalValues);
                for (int i = 0; i < count + additionalValues; ++i)
                {
                    array.set(i, i);
                }

                final Iterator<Integer> iterator = array.iterate().take(count);

                if (started)
                {
                    iterator.next();
                }

                return iterator;
            });

            runner.testGroup("create(Iterator<T>,int)", () ->
            {
                runner.test("with null innerIterator", (Test test) ->
                {
                    test.assertThrows(() -> TakeIterator.create(null, 5),
                        new PreConditionFailure("innerIterator cannot be null."));
                });

                runner.test("with negative toTake", (Test test) ->
                {
                    test.assertThrows(() -> TakeIterator.create(Iterator.create(1, 2, 3), -1),
                        new PreConditionFailure("toTake (-1) must be greater than or equal to 0."));
                });

                runner.test("with empty, non-started innerIterator and 0 toTake", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    final TakeIterator<Integer> iterator = TakeIterator.create(innerIterator, 0);
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty, non-started innerIterator and 10 toTake", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    final TakeIterator<Integer> iterator = TakeIterator.create(innerIterator, 10);
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with empty, started innerIterator and 0 toTake", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    innerIterator.next();
                    final TakeIterator<Integer> iterator = TakeIterator.create(innerIterator, 0);
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with empty, started innerIterator and 10 toTake", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create();
                    innerIterator.next();
                    final TakeIterator<Integer> iterator = TakeIterator.create(innerIterator, 10);
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty, non-started innerIterator and 0 toTake", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final TakeIterator<Integer> iterator = TakeIterator.create(innerIterator, 0);
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with non-empty, non-started innerIterator and 10 toTake", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    final TakeIterator<Integer> iterator = TakeIterator.create(innerIterator, 10);
                    IteratorTests.assertIterator(test, iterator, false, null);
                });

                runner.test("with non-empty, started innerIterator and 0 toTake", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    innerIterator.next();
                    final TakeIterator<Integer> iterator = TakeIterator.create(innerIterator, 0);
                    IteratorTests.assertIterator(test, iterator, true, null);
                });

                runner.test("with non-empty, non-started innerIterator and 10 toTake", (Test test) ->
                {
                    final Iterator<Integer> innerIterator = Iterator.create(1, 2, 3);
                    innerIterator.next();
                    final TakeIterator<Integer> iterator = TakeIterator.create(innerIterator, 10);
                    IteratorTests.assertIterator(test, iterator, true, 1);
                });
            });
        });
    }
}
