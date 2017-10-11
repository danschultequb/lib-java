package qub;

public class SingleLinkListQueueTests extends QueueTests
{
    @Override
    protected Queue<Integer> createQueue()
    {
        return new SingleLinkListQueue<>();
    }
}
