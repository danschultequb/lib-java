package qub;

public interface TraversalActionsTests
{
    static void test(TestRunner runner, Function1<Iterable<Node2<Integer>>,? extends TraversalActions<Node2<Integer>,Integer>> creator)
    {
        runner.testGroup(TraversalActions.class, () ->
        {
            runner.testGroup("visitNodes(Iterator<TNode>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final TraversalActions<Node2<Integer>,Integer> actions = creator.run(Iterable.create());
                    test.assertThrows(() -> actions.visitNodes((Iterator<Node2<Integer>>)null),
                        new PreConditionFailure("nodes cannot be null."));
                });
            });

            runner.testGroup("visitNodes(Iterable<TNode>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final TraversalActions<Node2<Integer>,Integer> actions = creator.run(Iterable.create());
                    test.assertThrows(() -> actions.visitNodes((Iterable<Node2<Integer>>)null),
                        new PreConditionFailure("nodes cannot be null."));
                });
            });

            runner.testGroup("returnValues(Iterator<TValue>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final TraversalActions<Node2<Integer>,Integer> actions = creator.run(Iterable.create());
                    test.assertThrows(() -> actions.returnValues((Iterator<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                });
            });

            runner.testGroup("returnValues(Iterable<TValue>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final TraversalActions<Node2<Integer>,Integer> actions = creator.run(Iterable.create());
                    test.assertThrows(() -> actions.returnValues((Iterable<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                });
            });
        });
    }
}
