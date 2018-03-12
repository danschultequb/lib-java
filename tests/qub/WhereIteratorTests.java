package qub;

public class WhereIteratorTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(WhereIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                final int additionalValues = 5;

                final Array<Integer> array = new Array<>(count + additionalValues);
                for (int i = 0; i < count + additionalValues; ++i)
                {
                    array.set(i, i - additionalValues);
                }

                final Iterator<Integer> iterator = array.iterate().where((Integer value) -> value >= 0);

                if (started)
                {
                    iterator.next();
                }

                return iterator;
            });
        });
    }
}
