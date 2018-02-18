package qub;

public class SingleLinkListQueue<T> extends ListQueue<T>
{
    /**
     * Create a new SingleLinkListQueue.
     */
    public SingleLinkListQueue()
    {
        super(new SingleLinkList<T>());
    }
}
