package qub;

/**
 * An immutable graph data structure.
 * @param <T> The type of values stored in each of this {@link Graph}'s {@link GraphNode}s.
 */
public interface Graph<T>
{
    /**
     * Create a new {@link MutableGraph}.
     * @param <T> The type of values to store in the new {@link MutableGraph}.
     * @return The new {@link MutableGraph}.
     */
    public static <T> MutableGraph<T> create()
    {
        return MutableGraph.create();
    }

    /**
     * Get the {@link GraphNode}s in this {@link Graph}.
     * @return The {@link GraphNode}s in this {@link Graph}.
     */
    public Iterable<? extends GraphNode<T>> getNodes();

    /**
     * Get the {@link GraphNode} in this {@link Graph} with the provided value.
     * @param value The {@code value} to look for.
     * @return The {@link GraphNode} in this {@link Graph} with the provided value.
     */
    public default Result<? extends GraphNode<T>> getNode(T value)
    {
        final Iterable<? extends GraphNode<T>> nodes = this.getNodes();
        return nodes.first(node -> Comparer.equal(node.getValue(), value))
            .convertError(NotFoundException.class, () -> new NotFoundException("No node exists in the Graph with the value " + value + "."));
    }

    /**
     * Get whether this {@link Graph} contains the provided value.
     * @param value The value to look for.
     */
    public default boolean containsValue(T value)
    {
        return this.getNode(value)
            .then(() -> true)
            .catchError(NotFoundException.class, () -> false)
            .await();
    }

    /**
     * Get whether or not this {@link Graph} contains the provided {@link GraphNode}.
     * @param node The node to look for.
     * @return Whether or not this {@link Graph} contains the provided {@link GraphNode}.
     */
    default boolean containsNode(GraphNode<T> node)
    {
        PreCondition.assertNotNull(node, "node");

        return this.getNodes()
            .contains(existingNode -> Comparer.same(existingNode, node));
    }
}
