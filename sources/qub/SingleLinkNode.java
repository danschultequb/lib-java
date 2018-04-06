package qub;

/**
 * A node with a single link to another SingleLinkNode.
 * @param <T> The type of value stored in the single-link node.
 */
public class SingleLinkNode<T> extends IterableBase<T>
{
    private volatile T value;
    private volatile SingleLinkNode<T> next;

    /**
     * Create a new single-link node with the provided value.
     * @param value The value to store in this single-link node.
     */
    public SingleLinkNode(T value)
    {
        this.value = value;
        this.next = null;
    }

    /**
     * Get the value stored in this single-link node.
     * @return The value stored in this single-link node.
     */
    public T getValue()
    {
        return value;
    }

    /**
     * Set the value stored in this single-link node.
     * @param value The value to store in this single-link node.
     */
    public void setValue(T value)
    {
        this.value = value;
    }

    /**
     * Get the next node in this single-link node chain/list.
     * @return The next node in this single-link node chain/list.
     */
    public SingleLinkNode<T> getNext()
    {
        return next;
    }

    /**
     * Set the next node in this single-link node chain/list.
     * @param next The next node in this single-link node chain/list.
     */
    public void setNext(SingleLinkNode<T> next)
    {
        this.next = next;
    }

    @Override
    public Iterator<T> iterate()
    {
        return new SingleLinkNodeIterator<>(this);
    }
}
