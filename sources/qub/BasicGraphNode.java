package qub;

/**
 * A basic implementation of {@link MutableGraphNode}.
 * @param <T> The type of value stored in this {@link BasicGraphNode}.
 */
public class BasicGraphNode<T> implements MutableGraphNode<T>
{
    private final MutableGraph<T> graph;
    private final T value;
    private final List<GraphNode<T>> linkedNodes;

    private BasicGraphNode(MutableGraph<T> graph, T value)
    {
        PreCondition.assertNotNull(graph, "graph");

        this.graph = graph;
        this.value = value;
        this.linkedNodes = List.create();
    }

    /**
     * Create a new {@link BasicGraphNode}.
     * @param value The value stored in the new {@link BasicGraphNode}.
     * @param <T> The type of value stored in the new {@link BasicGraphNode}.
     * @return The new {@link BasicGraphNode}.
     */
    public static <T> BasicGraphNode<T> create(MutableGraph<T> graph, T value)
    {
        PreCondition.assertNotNull(graph, "graph");
        PreCondition.assertFalse(graph.containsValue(value), "graph.containsValue(value)");

        final BasicGraphNode<T> result = new BasicGraphNode<>(graph, value);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertFalse(graph.containsValue(value), "graph.containsValue(value)");

        return result;
    }

    @Override
    public T getValue()
    {
        return this.value;
    }

    @Override
    public Iterable<GraphNode<T>> getLinkedNodes()
    {
        return this.linkedNodes;
    }

    @Override
    public BasicGraphNode<T> addLinkToValue(T value)
    {
        PreCondition.assertFalse(this.isLinkedTo(value), "this.isLinkedTo(value)");
        PreCondition.assertTrue(this.graph.containsValue(value), "this.graph.containsValue(value)");

        return this.addLinkToNode(this.graph.getNode(value).await());
    }

    @Override
    public BasicGraphNode<T> addLinkToNode(GraphNode<T> node)
    {
        PreCondition.assertNotNull(node, "node");
        PreCondition.assertFalse(this.isLinkedTo(node), "this.isLinkedTo(node)");
        PreCondition.assertTrue(this.graph.containsNode(node), "this.graph.containsNode(node)");

        this.linkedNodes.add(node);

        return this;
    }
}
