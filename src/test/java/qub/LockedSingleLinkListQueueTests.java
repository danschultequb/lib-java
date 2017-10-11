package qub;

public class LockedSingleLinkListQueueTests extends QueueTests
{
    @Override
    protected Queue<Integer> createQueue()
    {
        return new LockedSingleLinkListQueue<>();
    }
}
