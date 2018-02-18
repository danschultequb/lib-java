package qub;

/**
 * A node with two links to other DoubleLinkNodes.
 * @param <T> The type of value stored in the double-link node.
 */

public class DoubleLinkNode<T> implements Iterable<T>
{
    private DoubleLinkNode<T> previous;
    private DoubleLinkNode<T> next;
    private T value;

    /**
     * Create a new DoubleLinkNode with the provided value.
     * @param value The value to store in the new DoubleLinkNode.
     */
    public DoubleLinkNode(T value)
    {
        this.value = value;
    }

    /**
     * Get the value stored in this DoubleLinkNode.
     * @return The value stored in this DoubleLinkNode.
     */
    public T getValue()
    {
        return value;
    }

    /**
     * Set the value stored in this DoubleLinkNode.
     * @param value The value to set.
     */
    public void setValue(T value)
    {
        this.value = value;
    }

    /**
     * Get the previous DoubleLinkNode in this double-link chain.
     * @return The previous DoubleLinkNode in this double-link chain.
     */
    public DoubleLinkNode<T> getPrevious()
    {
        return previous;
    }

    /**
     * Set the previous DoubleLinkNode in this double-link chain.
     * @param previous The previous DoubleLinkNode to set.
     */
    public void setPrevious(DoubleLinkNode<T> previous)
    {
        this.previous = previous;
    }

    /**
     * Get the next DoubleLinkNode in this double-link chain.
     * @return The next DoubleLinkNode in this double-link chain.
     */
    public DoubleLinkNode<T> getNext()
    {
        return next;
    }

    /**
     * Set the next DoubleLinkNode in this double-link chain.
     * @param next The next DoubleLinkNode to set.
     */
    public void setNext(DoubleLinkNode<T> next)
    {
        this.next = next;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }

    @Override
    public Iterator<T> iterate()
    {
        return new DoubleLinkNodeIterator<>(this);
    }
}
