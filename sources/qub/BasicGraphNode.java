package qub;

/**
 * A basic implementation of MutableGraphNode.
 * @param <T> The type of value stored in this BasicGraphNode.
 */
public class BasicGraphNode<T> implements MutableGraphNode<T>
{
    private T value;
    private final List<GraphNode<T>> nodes;

    private BasicGraphNode(T value)
    {
        this.value = value;
        this.nodes = List.create();
    }

    /**
     * Create a new BasicGraphNode.
     * @param value The value stored in the new BasicGraphNode.
     * @param <T> The type of value stored in the new BasicGraphNode.
     * @return The new BasicGraphNode.
     */
    public static <T> BasicGraphNode<T> create(T value)
    {
        return new BasicGraphNode<>(value);
    }

    @Override
    public T getValue()
    {
        return this.value;
    }

    @Override
    public Iterable<GraphNode<T>> getNodes()
    {
        return this.nodes;
    }

    @Override
    public BasicGraphNode<T> setValue(T value)
    {
        this.value = value;

        return this;
    }

    @Override
    public BasicGraphNode<T> addNode(GraphNode<T> node)
    {
        PreCondition.assertNotNull(node, "node");
        PreCondition.assertFalse(this.containsNode(node), "this.containsNode(node)");

        this.nodes.add(node);

        return this;
    }
}
