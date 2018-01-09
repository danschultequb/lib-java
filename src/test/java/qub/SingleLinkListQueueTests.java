package qub;

public class SingleLinkListQueueTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("SingleLinkListQueue<T>", new Action0()
        {
            @Override
            public void run()
            {
                QueueTests.test(runner, new Function0<Queue<Integer>>()
                {
                    @Override
                    public Queue<Integer> run()
                    {
                        return new SingleLinkListQueue<>();
                    }
                });
            }
        });
    }
}
