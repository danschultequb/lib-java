package qub;

public class LockedArrayListQueueTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("LockedArrayListQueue<T>", new Action0()
        {
            @Override
            public void run()
            {
                QueueTests.test(runner, new Function0<Queue<Integer>>()
                {
                    @Override
                    public Queue<Integer> run()
                    {
                        return new LockedArrayListQueue<>();
                    }
                });
            }
        });
    }
}
