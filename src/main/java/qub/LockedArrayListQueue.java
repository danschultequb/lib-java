package qub;

public class LockedArrayListQueue<T> extends LockedQueueBase<T>
{
    public LockedArrayListQueue()
    {
        super(new ArrayListQueue<T>());
    }
}
