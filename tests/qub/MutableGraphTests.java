package qub;

public interface MutableGraphTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(MutableGraph.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableGraph<Integer> graph = MutableGraph.create();
                test.assertNotNull(graph);
                final Iterable<? extends MutableGraphNode<Integer>> nodes = graph.getNodes();
                test.assertEqual(Iterable.create(), nodes);
            });

            runner.testGroup("createNode(T)", () ->
            {
                runner.test("with value that doesn't exist in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node = graph.createNode(1);
                    test.assertNotNull(node);
                    test.assertEqual(1, node.getValue());
                    test.assertEqual(Iterable.create(node), graph.getNodes());
                });

                runner.test("with value that already exists in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node = graph.createNode(1);
                    test.assertThrows(() -> graph.createNode(1),
                        new PreConditionFailure("this.containsValue(value) cannot be true."));
                    test.assertEqual(Iterable.create(node), graph.getNodes());
                });
            });

            runner.testGroup("addNode(T)", () ->
            {
                runner.test("with value that doesn't exist in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraph<Integer> createNodeResult = graph.addNode(1);
                    test.assertSame(graph, createNodeResult);
                    test.assertEqual(Iterable.create(graph.getNode(1).await()), graph.getNodes());
                });

                runner.test("with value that already exists in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node = graph.createNode(1);
                    test.assertThrows(() -> graph.addNode(1),
                        new PreConditionFailure("this.containsValue(value) cannot be true."));
                    test.assertEqual(Iterable.create(node), graph.getNodes());
                });
            });

            runner.testGroup("getNode(T)", () ->
            {
                runner.test("with value that doesn't exist in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    test.assertThrows(() -> graph.getNode(5).await(),
                        new NotFoundException("No node exists in the Graph with the value 5."));
                });

                runner.test("with value that exists in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final GraphNode<Integer> node = graph.createNode(1);
                    final MutableGraphNode<Integer> getNodeResult = graph.getNode(1).await();
                    test.assertSame(node, getNodeResult);
                });
            });

            runner.testGroup("getOrCreateNode(T)", () ->
            {
                runner.test("with value that doesn't exist in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node = graph.getOrCreateNode(1);
                    test.assertNotNull(node);
                    test.assertEqual(1, node.getValue());
                    test.assertSame(node, graph.getNode(1).await());
                    test.assertEqual(Iterable.create(node), graph.getNodes());
                });

                runner.test("with value that already exists in the graph", (Test test) ->
                {
                    final MutableGraph<Integer> graph = Graph.create();
                    final MutableGraphNode<Integer> node1 = graph.createNode(1);
                    final MutableGraphNode<Integer> node2 = graph.getOrCreateNode(1);
                    test.assertSame(node1, node2);
                    test.assertEqual(Iterable.create(node1), graph.getNodes());
                });
            });
        });
    }
}
