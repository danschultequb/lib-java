package qub;

public class MapIteratorTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("MapIterator<T>", new Action0()
        {
            @Override
            public void run()
            {
                IteratorTests.test(runner, new Function2<Integer, Boolean, Iterator<Integer>>()
                {
                    @Override
                    public Iterator<Integer> run(Integer count, Boolean started)
                    {
                        final Array<Long> array = new Array<>(count);
                        for (int i = 0; i < count; ++i)
                        {
                            array.set(i, (long)i);
                        }

                        final Iterator<Integer> iterator = array.iterate().map(new Function1<Long, Integer>()
                        {
                            @Override
                            public Integer run(Long value)
                            {
                                return value.intValue();
                            }
                        });

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
