package qub;

public class SingleLinkListQueue<T> extends QueueBase<T>
{
    /**
     * Create a new SingleLinkListQueue.
     */
    public SingleLinkListQueue()
    {
        super(new SingleLinkList<T>());
    }
}
