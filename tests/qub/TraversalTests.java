package qub;

public interface TraversalTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Traversal.class, () ->
        {
            runner.testGroup("createDepthFirstSearch(Action2<TraversalActions<TNode,TValue>,TNode>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Traversal.createDepthFirstSearch(null),
                        new PreConditionFailure("visitNodeFunction cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = Traversal.createDepthFirstSearch((TraversalActions<Node2<Integer>,Integer> actions, Node2<Integer> currentNode) -> {});
                    test.assertNotNull(traversal);
                    test.assertInstanceOf(traversal, DepthFirstSearchTraversal.class);
                });
            });
        });
    }

    static void test(TestRunner runner, Function0<Traversal<Node2<Integer>,Integer>> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(Traversal.class, () ->
        {
            runner.testGroup("iterate(Iterator<TNode>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = creator.run();
                    test.assertThrows(() -> traversal.iterate((Iterator<Node2<Integer>>)null),
                        new PreConditionFailure("startNodes cannot be null."));
                });

                runner.test("with empty iterator", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = creator.run();
                    final Iterator<Integer> iterator = traversal.iterate(Iterator.create());
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(), iterator.toList());
                });
            });

            runner.testGroup("iterate(Iterable<TNode>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = creator.run();
                    test.assertThrows(() -> traversal.iterate((Iterable<Node2<Integer>>)null),
                        new PreConditionFailure("startNodes cannot be null."));
                });

                runner.test("with empty iterator", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = creator.run();
                    final Iterator<Integer> iterator = traversal.iterate(Iterable.create());
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(), iterator.toList());
                });
            });
        });
    }
}
