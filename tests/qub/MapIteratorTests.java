package qub;

public class MapIteratorTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(MapIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                final Array<Long> array = Array.createWithLength(count);
                for (int i = 0; i < count; ++i)
                {
                    array.set(i, (long)i);
                }

                final Iterator<Integer> iterator = array.iterate().map(Long::intValue);

                if (started)
                {
                    iterator.next();
                }

                return iterator;
            });
        });
    }
}
