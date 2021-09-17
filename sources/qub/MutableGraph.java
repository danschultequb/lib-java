package qub;

/**
 * A mutable graph data structure.
 * @param <T> The type of values stored in each of this {@link MutableGraph}'s {@link MutableGraphNode}s.
 */
public interface MutableGraph<T> extends Graph<T>
{
    /**
     * Create a new {@link MutableGraph}.
     * @param <T> The type of values to store in the new {@link MutableGraph}.
     * @return The new {@link MutableGraph}.
     */
    static <T> MutableGraph<T> create()
    {
        return BasicGraph.create();
    }

    /**
     * Get the {@link MutableGraphNode}s in this {@link MutableGraph}.
     * @return The {@link MutableGraphNode}s in this {@link MutableGraph}.
     */
    Iterable<? extends MutableGraphNode<T>> getNodes();

    /**
     * Create and return a new {@link MutableGraphNode} with the provided value.
     * @param value The value to create a new {@link MutableGraphNode} around.
     * @return The newly created {@link MutableGraphNode}.
     */
    MutableGraphNode<T> createNode(T value);

    /**
     * Create a new {@link MutableGraphNode} with the provided value. This {@link MutableGraph}
     * will then be returned for method chaining.
     * @param value The value to create a new {@link MutableGraphNode} around.
     * @return This object for method chaining.
     */
    default MutableGraph<T> addNode(T value)
    {
        this.createNode(value);
        return this;
    }

    /**
     * Get the {@link MutableGraphNode} in this {@link MutableGraph} with the provided value.
     * @param value The value to look for.
     * @return The {@link MutableGraphNode} in this {@link MutableGraph} with the provided value.
     */
    @Override
    default Result<MutableGraphNode<T>> getNode(T value)
    {
        return Result.create(() ->
        {
            final GraphNode<T> node = Graph.super.getNode(value).await();
            return (MutableGraphNode<T>)node;
        });
    }

    /**
     * Get the {@link MutableGraphNode} in this {@link MutableGraph} with the provided value. If no
     * {@link MutableGraphNode} exists in this {@link MutableGraph} with the provided value, the
     * create a new {@link MutableGraphNode} with the provided value, add it to this
     * {@link MutableGraph}, and then return the new {@link MutableGraphNode}.
     * @param value The value to look for.
     * @return The existing or new {@link MutableGraphNode}.
     */
    default MutableGraphNode<T> getOrCreateNode(T value)
    {
        return this.getNode(value)
            .catchError(NotFoundException.class, () ->
            {
                return this.createNode(value);
            })
            .await();
    }
}
