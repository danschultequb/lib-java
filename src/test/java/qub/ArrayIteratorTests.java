package qub;

public class ArrayIteratorTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("ArrayIterator<T>", new Action0()
        {
            @Override
            public void run()
            {
                IteratorTests.test(runner, new Function2<Integer, Boolean, Iterator<Integer>>()
                {
                    @Override
                    public Iterator<Integer> run(Integer count, Boolean started)
                    {
                        return createIterator(count, started);
                    }
                });
            }
        });
    }

    public static ArrayIterator<Integer> createIterator(int count, boolean started)
    {
        final Array<Integer> a = new Array<>(count);
        for (int i = 0; i < count; ++i) {
            a.set(i, i);
        }

        final ArrayIterator<Integer> iterator = new ArrayIterator<>(a);
        if (started)
        {
            iterator.next();
        }

        return iterator;
    }
}
