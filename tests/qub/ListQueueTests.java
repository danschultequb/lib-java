package qub;

public interface ListQueueTests
{
    static void test(final TestRunner runner)
    {
        runner.testGroup(ListQueue.class, () ->
        {
            QueueTests.test(runner, ListQueue::create);
        });
    }
}
