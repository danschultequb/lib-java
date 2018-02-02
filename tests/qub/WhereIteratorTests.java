package qub;

public class WhereIteratorTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("WhereIterator", new Action0()
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
                            array.set(i, i - additionalValues);
                        }

                        final Iterator<Integer> iterator = array.iterate().where(new Function1<Integer, Boolean>()
                        {
                            @Override
                            public Boolean run(Integer arg1)
                            {
                                return arg1 >= 0;
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
