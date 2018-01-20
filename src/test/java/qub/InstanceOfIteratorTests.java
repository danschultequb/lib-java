package qub;

public class InstanceOfIteratorTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InstanceOfIterator<T>", new Action0()
        {
            @Override
            public void run()
            {
                IteratorTests.test(runner, new Function2<Integer, Boolean, Iterator<Integer>>()
                {
                    @Override
                    public Iterator<Integer> run(Integer count, Boolean started)
                    {
                        return ArrayIteratorTests.createIterator(count, started)
                            .instanceOf(Integer.class);
                    }
                });
            }
        });
    }
}
