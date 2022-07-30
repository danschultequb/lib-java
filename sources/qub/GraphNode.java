package qub;

/**
 * A node that can connect to any number of other nodes.
 * @param <T> The type of value stored in this {@link GraphNode}.
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
    public static <T> MutableGraphNode<T> create(MutableGraph<T> graph, T value)
    {
        return MutableGraphNode.create(graph, value);
    }

    /**
     * Get the value stored in this {@link GraphNode}.
     * @return The value stored in this {@link GraphNode}.
     */
    public T getValue();

    /**
     * Iterate through the {@link GraphNode}s that have been linked to this {@link GraphNode}.
     */
    public Iterator<GraphNode<T>> iterateLinkedNodes();

    /**
     * Get the {@link GraphNode} with the provided value that is linked to this {@link GraphNode}.
     * @param value The value to look for.
     */
    public default Result<? extends GraphNode<T>> getLinkedNode(T value)
    {
        final Iterator<GraphNode<T>> linkedNodes = this.iterateLinkedNodes();
        return linkedNodes.first(node -> Comparer.equal(node.getValue(), value))
            .convertError(NotFoundException.class, () -> new NotFoundException("No " + Types.getTypeName(GraphNode.class) + " is linked to this " + Types.getTypeName(GraphNode.class) + " with the value " + value + "."));
    }

    /**
     * Get whether this {@link GraphNode} is linked to a {@link GraphNode} with the provided value.
     * @param value The value to look for in the {@link GraphNode}s that this {@link GraphNode} is
     *              linked to.
     */
    public default boolean isLinkedTo(T value)
    {
        return this.getLinkedNode(value)
            .then(() -> true)
            .catchError(NotFoundException.class, () -> false)
            .await();
    }

    /**
     * Get whether this {@link GraphNode} is linked to the provided {@link GraphNode}.
     * @param node The {@link GraphNode} to look for.
     */
    public default boolean isLinkedTo(GraphNode<T> node)
    {
        PreCondition.assertNotNull(node, "node");

        return this.iterateLinkedNodes().contains(node).await();
    }
}
