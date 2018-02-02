package qub;

public class LockedSingleLinkListQueueTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("LockedSingleLinkListQueue<T>", new Action0()
        {
            @Override
            public void run()
            {
                QueueTests.test(runner, new Function0<Queue<Integer>>()
                {
                    @Override
                    public Queue<Integer> run()
                    {
                        return new LockedSingleLinkListQueue<>();
                    }
                });
            }
        });
    }
}
