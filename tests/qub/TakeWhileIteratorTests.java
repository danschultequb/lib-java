package qub;

public interface TakeWhileIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(TakeWhileIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                final int additionalValues = 5;

                final Array<Integer> array = Array.createWithLength(count + additionalValues);
                for (int i = 0; i < array.getCount(); ++i)
                {
                    array.set(i, i);
                }

                final Iterator<Integer> iterator = array.iterate().takeWhile((Integer value) -> value < count);

                if (started)
                {
                    iterator.start();
                }

                return iterator;
            });
        });
    }
}
