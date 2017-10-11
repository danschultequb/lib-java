package qub;

public class ArrayListQueueTests extends QueueTests
{
    @Override
    protected Queue<Integer> createQueue()
    {
        return new ArrayListQueue<>();
    }
}
