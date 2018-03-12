package qub;

public class SkipIteratorTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SkipIterator.class, () ->
        {
            IteratorTests.test(runner, (Integer count, Boolean started) ->
            {
                final int toSkip = 5;

                final Array<Integer> array = new Array<>(count + toSkip);
                for (int i = 0; i < count + toSkip; ++i)
                {
                    array.set(i, i - toSkip);
                }

                final Iterator<Integer> iterator = array.iterate().skip(toSkip);

                if (started)
                {
                    iterator.next();
                }

                return iterator;
            });
        });
    }
}
