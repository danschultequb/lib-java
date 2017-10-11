package qub;

public class LockedArrayListQueueTests extends QueueTests
{
    @Override
    protected Queue<Integer> createQueue()
    {
        return new LockedArrayListQueue<>();
    }
}
