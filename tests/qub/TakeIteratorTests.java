package qub;

public class TakeIteratorTests
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
        });
    }
}
