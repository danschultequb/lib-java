package qub;

public class LockedSingleLinkListQueue<T> extends LockedQueueBase<T>
{
    public LockedSingleLinkListQueue()
    {
        super(new SingleLinkListQueue<T>());
    }
}
