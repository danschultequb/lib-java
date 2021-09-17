package qub;

/**
 * A node that can connect to any number of other nodes.
 * @param <T> The type of value stored in this GraphNode.
 */
public interface GraphNode<T>
{
    /**
     * Create a new {@link GraphNode} in the provided {@link MutableGraph} with the provided value.
     * @param graph The {@link MutableGraph} to create a new {@link GraphNode} in.
     * @param value The value that the new {@link GraphNode} will contain.
     * @param <T> The type of value that the new {@link GraphNode} will contain.
     * @return The new {@link GraphNode}.
     */
    static <T> MutableGraphNode<T> create(MutableGraph<T> graph, T value)
    {
        return MutableGraphNode.create(graph, value);
    }

    /**
     * Get the value stored in this {@link GraphNode}.
     * @return The value stored in this {@link GraphNode}.
     */
    T getValue();

    /**
     * Get the nodes that this {@link GraphNode} is linked to.
     * @return The nodes that this {@link GraphNode} is linked to.
     */
    Iterable<GraphNode<T>> getLinkedNodes();

    /**
     * Get the {@link GraphNode} with the provided value that is linked to this {@link GraphNode}.
     * @param value The value to look for.
     * @return The {@link GraphNode} with the provided value that is linked to this
     * {@link GraphNode}.
     */
    default Result<? extends GraphNode<T>> getLinkedNode(T value)
    {
        return Result.create(() ->
        {
            final Iterable<? extends GraphNode<T>> linkedNodes = this.getLinkedNodes();
            final GraphNode<T> result = linkedNodes.first(node -> Comparer.equal(node.getValue(), value));
            if (result == null)
            {
                throw new NotFoundException("No GraphNode is linked to this GraphNode with the value " + value + ".");
            }
            return result;
        });
    }

    /**
     * Get whether or not this {@link GraphNode} is linked to a {@link GraphNode} with the provided
     * value.
     * @param value The value to look for in the {@link GraphNode}s that this {@link GraphNode} is
     *              linked to.
     * @return Whether or not this {@link GraphNode} is linked to a {@link GraphNode} with the
     * provided value.
     */
    default boolean isLinkedTo(T value)
    {
        return this.getLinkedNode(value)
            .then(() -> true)
            .catchError(NotFoundException.class, () -> false)
            .await();
    }

    /**
     * Get whether or not this {@link GraphNode} is linked to the provided {@link GraphNode}.
     * @param node The {@link GraphNode} to look for.
     * @return Whether or not this {@link GraphNode} is linked to the provided
     * {@link GraphNode}.
     */
    default boolean isLinkedTo(GraphNode<T> node)
    {
        PreCondition.assertNotNull(node, "node");

        return this.getLinkedNodes().contains(node);
    }
}
