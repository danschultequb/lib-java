package qub;

public interface Node1ListQueueTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Node1ListQueue.class, () ->
        {
            QueueTests.test(runner, Node1ListQueue::create);
        });
    }
}
