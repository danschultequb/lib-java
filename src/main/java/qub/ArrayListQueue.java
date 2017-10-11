package qub;

public class ArrayListQueue<T> extends QueueBase<T>
{
    /**
     * Create a new ArrayList-based Queue.
     */
    protected ArrayListQueue()
    {
        super(new ArrayList<T>());
    }
}
