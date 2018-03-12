package qub;

public class SingleLinkListQueueTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SingleLinkListQueue.class, () ->
        {
            QueueTests.test(runner, SingleLinkListQueue::new);
        });
    }
}
