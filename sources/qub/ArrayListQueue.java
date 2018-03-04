package qub;

public class ArrayListQueue<T> extends ListQueue<T>
{
    /**
     * Create a new ArrayList-based Queue.
     */
    public ArrayListQueue()
    {
        super(new ArrayList<T>());
    }
}
