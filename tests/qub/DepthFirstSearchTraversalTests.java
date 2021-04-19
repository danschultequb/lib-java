package qub;

public interface DepthFirstSearchTraversalTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(DepthFirstSearchTraversal.class, () ->
        {
            TraversalTests.test(runner, () -> DepthFirstSearchTraversal.create(Node2::inOrderTraversal));

            runner.testGroup("iterate(TNode)", () ->
            {
                runner.test("with single-node graph", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Node2.create(50));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(50), iterator.toList());
                });

                runner.test("with multiple-node graph", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Node2.create(50)
                            .setNode1(Node2.create(25)
                                .setNode1(Node2.create(10))
                                .setNode2(Node2.create(35)))
                            .setNode2(Node2.create(55)));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(10, 25, 35, 50, 55), iterator.toList());
                });
            });

            runner.testGroup("iterate(Iterator<TNode>)", () ->
            {
                runner.test("with empty iterator", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(Iterator.create());
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(), iterator.toList());
                });

                runner.test("with single-node graph", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Iterator.create(
                            Node2.create(50)));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(50), iterator.toList());
                });

                runner.test("with multiple-node graph", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Iterator.create(
                            Node2.create(50)
                                .setNode1(Node2.create(25)
                                    .setNode1(Node2.create(10))
                                    .setNode2(Node2.create(35)))
                                .setNode2(Node2.create(55))));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(10, 25, 35, 50, 55), iterator.toList());
                });

                runner.test("with multiple single-node graphs", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Iterator.create(
                            Node2.create(50),
                            Node2.create(10),
                            Node2.create(30)));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(50, 10, 30), iterator.toList());
                });

                runner.test("with multiple multiple-node graph", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Iterator.create(
                            Node2.create(50)
                                .setNode1(Node2.create(25)
                                    .setNode1(Node2.create(10))
                                    .setNode2(Node2.create(35)))
                                .setNode2(Node2.create(55)),
                            Node2.create(5)
                                .setNode1(Node2.create(3)
                                    .setNode1(Node2.create(1))
                                    .setNode2(Node2.create(4)))
                                .setNode2(Node2.create(7)),
                            Node2.create(1000)
                                .setNode1(Node2.create(999))));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(10,25,35,50,55,1,3,4,5,7,999,1000), iterator.toList());
                });
            });

            runner.testGroup("iterate(Iterable<TNode>)", () ->
            {
                runner.test("with empty iterable", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(Iterable.create());
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(), iterator.toList());
                });

                runner.test("with single-node graph", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Iterable.create(
                            Node2.create(50)));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(50), iterator.toList());
                });

                runner.test("with multiple-node graph", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Iterable.create(
                            Node2.create(50)
                                .setNode1(Node2.create(25)
                                    .setNode1(Node2.create(10))
                                    .setNode2(Node2.create(35)))
                                .setNode2(Node2.create(55))));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(10, 25, 35, 50, 55), iterator.toList());
                });

                runner.test("with multiple single-node graphs", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Iterable.create(
                            Node2.create(50),
                            Node2.create(10),
                            Node2.create(30)));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(50, 10, 30), iterator.toList());
                });

                runner.test("with multiple multiple-node graph", (Test test) ->
                {
                    final Traversal<Node2<Integer>,Integer> traversal = DepthFirstSearchTraversal.create(Node2::inOrderTraversal);
                    final Iterator<Integer> iterator = traversal.iterate(
                        Iterable.create(
                            Node2.create(50)
                                .setNode1(Node2.create(25)
                                    .setNode1(Node2.create(10))
                                    .setNode2(Node2.create(35)))
                                .setNode2(Node2.create(55)),
                            Node2.create(5)
                                .setNode1(Node2.create(3)
                                    .setNode1(Node2.create(1))
                                    .setNode2(Node2.create(4)))
                                .setNode2(Node2.create(7)),
                            Node2.create(1000)
                                .setNode1(Node2.create(999))));
                    test.assertNotNull(iterator);
                    test.assertEqual(Iterable.create(10,25,35,50,55,1,3,4,5,7,999,1000), iterator.toList());
                });
            });
        });
    }
}
