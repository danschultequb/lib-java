package qub;

public class ArrayListQueueTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("ArrayListQueue<T>", () ->
        {
            QueueTests.test(runner, ArrayListQueue::new);
        });
    }
}
