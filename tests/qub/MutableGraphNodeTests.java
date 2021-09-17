package qub;

public interface MutableGraphNodeTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(MutableGraphNode.class, () ->
        {
            runner.testGroup("create(MutableGraph<T>,T)", () ->
            {
                runner.test("with null graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = null;
                    final Integer value = 20;
                    test.assertThrows(() -> MutableGraphNode.create(graph, value),
                            new PreConditionFailure("graph cannot be null."));
                });

                runner.test("with graph that doesn't already contain the provided value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final Integer value = 20;
                    final GraphNode<Integer> node = MutableGraphNode.create(graph, value);
                    test.assertNotNull(node);
                    test.assertFalse(graph.containsNode(node));
                    test.assertEqual(value, node.getValue());
                    test.assertEqual(Iterable.create(), node.getLinkedNodes());
                });

                runner.test("with graph that already contains the provided value", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final Integer value = 20;
                    graph.createNode(value);

                    test.assertThrows(() -> MutableGraphNode.create(graph, value),
                            new PreConditionFailure("graph.containsValue(value) cannot be true."));

                    test.assertEqual(Iterable.create(value), graph.getNodes().map(GraphNode::getValue));
                });
            });

            runner.testGroup("addLinkToValue(T)", () ->
            {
                runner.test("with value not in graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = MutableGraph.create();
                    final MutableGraphNode<Integer> node = graph.createNode(1);
                    test.assertThrows(() -> node.addLinkToValue(2),
                            new PreConditionFailure("this.graph.containsValue(value) cannot be false."));
                });

                runner.test("with not-yet-linked value in graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = MutableGraph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.createNode(2);

                    final MutableGraphNode<Integer> addLinkToValueResult = node1.addLinkToValue(2);
                    test.assertSame(node1, addLinkToValueResult);
                    test.assertEqual(Iterable.create(node2), node1.getLinkedNodes());
                    test.assertEqual(Iterable.create(), node2.getLinkedNodes());
                });

                runner.test("with already-linked value in graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = MutableGraph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.createNode(2);
                    node1.addLinkToValue(2);

                    test.assertThrows(() -> node1.addLinkToValue(2),
                        new PreConditionFailure("this.isLinkedTo(value) cannot be true."));
                    test.assertEqual(Iterable.create(node2), node1.getLinkedNodes());
                    test.assertEqual(Iterable.create(), node2.getLinkedNodes());
                });
            });

            runner.testGroup("addLinkToNode(GraphNode<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableGraph<Integer> graph = MutableGraph.create();
                    final MutableGraphNode<Integer> node = graph.createNode(1);
                    test.assertThrows(() -> node.addLinkToNode(null),
                            new PreConditionFailure("node cannot be null."));
                });

                runner.test("with node from a different graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph1 = MutableGraph.create();
                    final MutableGraphNode<Integer> node1 = graph1.createNode(1);

                    final MutableGraph<Integer> graph2 = MutableGraph.create();
                    final MutableGraphNode<Integer> node2 = graph2.createNode(2);

                    test.assertThrows(() -> node1.addLinkToNode(node2),
                        new PreConditionFailure("this.graph.containsNode(node) cannot be false."));
                    test.assertEqual(Iterable.create(), node1.getLinkedNodes());
                    test.assertEqual(Iterable.create(), node2.getLinkedNodes());
                });

                runner.test("with not-yet-linked node in graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = MutableGraph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.createNode(2);

                    final MutableGraphNode<Integer> addLinkToValueResult = node1.addLinkToNode(node2);
                    test.assertSame(node1, addLinkToValueResult);
                    test.assertEqual(Iterable.create(node2), node1.getLinkedNodes());
                    test.assertEqual(Iterable.create(), node2.getLinkedNodes());
                });

                runner.test("with already-linked node in graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = MutableGraph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.createNode(2);
                    node1.addLinkToValue(2);

                    test.assertThrows(() -> node1.addLinkToNode(node2),
                            new PreConditionFailure("this.isLinkedTo(node) cannot be true."));
                    test.assertEqual(Iterable.create(node2), node1.getLinkedNodes());
                    test.assertEqual(Iterable.create(), node2.getLinkedNodes());
                });
            });
        });
    }
}
