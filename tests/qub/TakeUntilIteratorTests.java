package qub;

public interface TakeUntilIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(TakeUntilIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                final int additionalValues = 5;

                final Array<Integer> array = Array.createWithLength(count + additionalValues);
                for (int i = 0; i < array.getCount(); ++i)
                {
                    array.set(i, i);
                }

                final Iterator<Integer> iterator = array.iterate().takeUntil((Integer value) -> value < count);

                if (started)
                {
                    iterator.start();
                }

                return iterator;
            });
        });
    }
}
