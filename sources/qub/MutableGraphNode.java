
package qub;

/**
 * A {@link MutableGraphNode} that can change which other {@link MutableGraphNode}s that it is
 * connected to.
 * @param <T> The type of value stored in this {@link MutableGraphNode}.
 */
public interface MutableGraphNode<T> extends GraphNode<T>
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
        return BasicGraphNode.create(graph, value);
    }

    /**
     * Link this {@link MutableGraphNode} to the {@link MutableGraphNode} that contains the provided value.
     * @param value The value to find and link to.
     * @return This object for method chaining.
     */
    MutableGraphNode<T> addLinkToValue(T value);

    /**
     * Add the provided node to the nodes that this MutableGraphNode is connected to.
     * @param node The node to connect to.
     * @return This object for method chaining.
     */
    MutableGraphNode<T> addLinkToNode(GraphNode<T> node);
}
