package qub;

/**
 * A basic implementation of a {@link MutableGraph}.
 * @param <T> The type of value stored in the {@link MutableGraphNode}s of this {@link BasicGraph}.
 */
public class BasicGraph<T> implements MutableGraph<T>
{
    private final List<MutableGraphNode<T>> nodes;

    private BasicGraph()
    {
        this.nodes = List.create();
    }

    /**
     * Create a new {@link BasicGraph}.
     * @param <T> The type of values to store in the new {@link BasicGraph}.
     * @return The new {@link BasicGraph}.
     */
    public static <T> BasicGraph<T> create()
    {
        return new BasicGraph<>();
    }

    @Override
    public MutableGraphNode<T> createNode(T value)
    {
        PreCondition.assertFalse(this.containsValue(value), "this.containsValue(value)");

        final MutableGraphNode<T> result = MutableGraphNode.create(this, value);
        this.nodes.add(result);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertTrue(this.containsValue(value), "this.containsValue(value)");
        PostCondition.assertTrue(this.containsNode(result), "this.containsNode(result)");

        return result;
    }

    @Override
    public Iterable<MutableGraphNode<T>> getNodes()
    {
        return this.nodes;
    }
}
