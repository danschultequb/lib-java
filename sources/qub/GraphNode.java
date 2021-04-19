package qub;

/**
 * A node that can connect to any number of other nodes.
 * @param <T> The type of value stored in this GraphNode.
 */
public interface GraphNode<T> extends Traversable<GraphNode<T>,T>
{
    /**
     * Create a new MutableGraphNode.
     * @param value The value stored in the new MutableGraphNode.
     * @param <T> The type of value stored in the new MutableGraphNode.
     * @return The new MutableGraphNode.
     */
    static <T> MutableGraphNode<T> create(T value)
    {
        return MutableGraphNode.create(value);
    }

    /**
     * Get the value stored in this GraphNode.
     * @return The value stored in this GraphNode.
     */
    T getValue();

    /**
     * Get whether or not this GraphNode contains the provided node.
     * @param node The node to look for in this GraphNode.
     * @return Whether or not this GraphNode contains the provided node.
     */
    default boolean containsNode(GraphNode<T> node)
    {
        PreCondition.assertNotNull(node, "node");

        return this.getNodes().contains(node);
    }

    /**
     * Get the nodes that this GraphNode is connected to.
     * @return The nodes that this GraphNode is connected to.
     */
    Iterable<GraphNode<T>> getNodes();

    @Override
    default Iterator<T> iterate(Traversal<GraphNode<T>, T> traversal)
    {
        PreCondition.assertNotNull(traversal, "traversal");

        return traversal.iterate(this);
    }
}
