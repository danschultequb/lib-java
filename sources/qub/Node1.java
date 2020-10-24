package qub;

/**
 * A node with a single link to another SingleLinkNode.
 * @param <T> The type of value stored in the single-link node.
 */
public class Node1<T>
{
    private volatile T value;
    private volatile Node1<T> node1;

    private Node1(T value)
    {
        this.value = value;
    }

    /**
     * Create a new single-link node with the provided value.
     * @param value The value to store in this single-link node.
     */
    public static <T> Node1<T> create(T value)
    {
        return new Node1<>(value);
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
    public Node1<T> setValue(T value)
    {
        this.value = value;
        return this;
    }

    /**
     * Get the next node in this single-link node chain/list.
     * @return The next node in this single-link node chain/list.
     */
    public Node1<T> getNode1()
    {
        return node1;
    }

    /**
     * Set the next node in this single-link node chain/list.
     * @param node1 The next node in this single-link node chain/list.
     */
    public Node1<T> setNode1(Node1<T> node1)
    {
        this.node1 = node1;
        return this;
    }
}
