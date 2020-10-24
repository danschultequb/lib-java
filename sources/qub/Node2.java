package qub;

/**
 * A node with two links to other Node2s.
 * @param <T> The type of value stored in the double-link node.
 */

public class Node2<T>
{
    private Node2<T> node1;
    private Node2<T> node2;
    private T value;

    /**
     * Create a new Node2 with the provided value.
     * @param value The value to store in the new Node2.
     */
    private Node2(T value)
    {
        this.value = value;
    }

    public static <T> Node2<T> create(T value)
    {
        return new Node2<>(value);
    }

    /**
     * Get the value stored in this Node2.
     * @return The value stored in this Node2.
     */
    public T getValue()
    {
        return this.value;
    }

    /**
     * Set the value stored in this Node2.
     * @param value The value to set.
     */
    public Node2<T> setValue(T value)
    {
        this.value = value;
        return this;
    }

    /**
     * Get the first node reference in this Node2.
     * @return The first node reference in this Node2.
     */
    public Node2<T> getNode1()
    {
        return this.node1;
    }

    /**
     * Set the first node reference in this Node2.
     * @param node1 The first node reference in this Node2.
     */
    public Node2<T> setNode1(Node2<T> node1)
    {
        this.node1 = node1;
        return this;
    }

    /**
     * Get the second node reference in this Node2.
     * @return The second node reference in this Node2.
     */
    public Node2<T> getNode2()
    {
        return this.node2;
    }

    /**
     * Set the second node reference in this Node2.
     * @param node2 The second node reference in this Node2.
     */
    public Node2<T> setNode2(Node2<T> node2)
    {
        this.node2 = node2;
        return this;
    }
}
