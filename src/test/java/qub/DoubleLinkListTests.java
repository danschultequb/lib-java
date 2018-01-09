package qub;

public class DoubleLinkListTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("DoubleLinkList<T>", new Action0()
        {
            @Override
            public void run()
            {
                ListTests.test(runner, new Function1<Integer, List<Integer>>()
                {
                    @Override
                    public List<Integer> run(Integer count)
                    {
                        final DoubleLinkList<Integer> result = new DoubleLinkList<>();
                        for (int i = 0; i < count; ++i)
                        {
                            result.add(i);
                        }
                        return result;
                    }
                });
            }
        });
    }
}
