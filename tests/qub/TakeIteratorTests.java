package qub;

public class TakeIteratorTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("TakeIterator<T>", new Action0()
        {
            @Override
            public void run()
            {
                IteratorTests.test(runner, new Function2<Integer, Boolean, Iterator<Integer>>()
                {
                    @Override
                    public Iterator<Integer> run(Integer count, Boolean started)
                    {
                        final int additionalValues = 5;

                        final Array<Integer> array = new Array<>(count + additionalValues);
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
                    }
                });
            }
        });
    }
}
