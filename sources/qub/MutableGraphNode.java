package qub;

/**
 * A graph node that can change its value and nodes.
 * @param <T> The type of value stored in the MutableGraphNode.
 */
public interface MutableGraphNode<T> extends GraphNode<T>
{
    /**
     * Create a new MutableGraphNode.
     * @param value The value stored in the new MutableGraphNode.
     * @param <T> The type of value stored in the new MutableGraphNode.
     * @return The new MutableGraphNode.
     */
    static <T> MutableGraphNode<T> create(T value)
    {
        return BasicGraphNode.create(value);
    }

    /**
     * Set the value stored in this MutableGraphNode.
     * @param value The value stored in this MutableGraphNode.
     * @return This object for method chaining.
     */
    MutableGraphNode<T> setValue(T value);

    /**
     * Add the provided node to the nodes that this MutableGraphNode is connected to.
     * @param node The node to connect to.
     * @return This object for method chaining.
     */
    MutableGraphNode<T> addNode(GraphNode<T> node);
}
