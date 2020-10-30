package qub;

public interface BinaryTreeTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BinaryTree.class, () ->
        {
            runner.testGroup("create(Function2<T,T,Comparison>,T...)", () ->
            {
                runner.test("with null comparer and no values", (Test test) ->
                {
                    test.assertThrows(() -> BinaryTree.create((Function2<Integer,Integer,Comparison>)null),
                        new PreConditionFailure("comparer cannot be null."));
                });

                runner.test("with non-null comparer and no values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BinaryTree<String> tree = BinaryTree.create(comparer);
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with non-null comparer and null values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    test.assertThrows(() -> BinaryTree.create(comparer, (String[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with non-null comparer and one value", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BinaryTree<String> tree = BinaryTree.create(comparer, "apples");
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples"), tree);
                });

                runner.test("with non-null comparer and two values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BinaryTree<String> tree = BinaryTree.create(comparer, "apples", "bananas");
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples", "bananas"), tree);
                });
            });

            runner.testGroup("create(Function2<T,T,Comparison>,Iterable<T>)", () ->
            {
                runner.test("with null comparer", (Test test) ->
                {
                    test.assertThrows(() -> BinaryTree.create((Function2<Integer,Integer,Comparison>)null, Iterable.create()),
                        new PreConditionFailure("comparer cannot be null."));
                });

                runner.test("with null values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    test.assertThrows(() -> BinaryTree.create(comparer, (Iterable<String>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with non-null comparer and one value", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BinaryTree<String> tree = BinaryTree.create(comparer, Iterable.create("apples"));
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples"), tree);
                });

                runner.test("with non-null comparer and two values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BinaryTree<String> tree = BinaryTree.create(comparer, Iterable.create("apples", "bananas"));
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples", "bananas"), tree);
                });
            });

            runner.testGroup("create(T...)", () ->
            {
                runner.test("no values", (Test test) ->
                {
                    final BinaryTree<String> tree = BinaryTree.create();
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> BinaryTree.create((String[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with one value", (Test test) ->
                {
                    final BinaryTree<String> tree = BinaryTree.create("apples");
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples"), tree);
                });

                runner.test("with two values", (Test test) ->
                {
                    final BinaryTree<String> tree = BinaryTree.create("apples", "bananas");
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples", "bananas"), tree);
                });
            });

            runner.testGroup("create(Iterable<T>)", () ->
            {
                runner.test("no values", (Test test) ->
                {
                    final BinaryTree<String> tree = BinaryTree.create(Iterable.<String>create());
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> BinaryTree.create((Iterable<String>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with one value", (Test test) ->
                {
                    final BinaryTree<String> tree = BinaryTree.create(Iterable.create("apples"));
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples"), tree);
                });

                runner.test("with two values", (Test test) ->
                {
                    final BinaryTree<String> tree = BinaryTree.create(Iterable.create("apples", "bananas"));
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples", "bananas"), tree);
                });
            });
        });
    }

    static void test(TestRunner runner, Function0<? extends BinaryTree<Integer>> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(BinaryTree.class, () ->
        {
            runner.testGroup("add(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addResult = tree.add(null);
                    test.assertSame(tree, addResult);
                    test.assertEqual(Iterable.create((String)null), tree);
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addResult = tree.add(30);
                    test.assertSame(tree, addResult);
                    test.assertEqual(Iterable.create(30), tree);
                });

                runner.test("with less than existing value with no left child node", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    tree.add(30);

                    final BinaryTree<Integer> addResult = tree.add(20);
                    test.assertSame(tree, addResult);
                    test.assertEqual(Iterable.create(20, 30), tree);
                });

                runner.test("with less than existing value with left child node", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    tree.addAll(30, 25);

                    final BinaryTree<Integer> addResult = tree.add(20);
                    test.assertSame(tree, addResult);
                    test.assertEqual(Iterable.create(20, 25, 30), tree);
                });

                runner.test("with equal to existing value with no left child node", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    tree.add(30);

                    final BinaryTree<Integer> addResult = tree.add(30);
                    test.assertSame(tree, addResult);
                    test.assertEqual(Iterable.create(30, 30), tree);
                });

                runner.test("with equal to existing value with left child node", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    tree.addAll(30, 30);

                    final BinaryTree<Integer> addResult = tree.add(30);
                    test.assertSame(tree, addResult);
                    test.assertEqual(Iterable.create(30, 30, 30), tree);
                });

                runner.test("with greater than existing value with no right child node", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    tree.add(30);

                    final BinaryTree<Integer> addResult = tree.add(40);
                    test.assertSame(tree, addResult);
                    test.assertEqual(Iterable.create(30, 40), tree);
                });

                runner.test("with greater than existing value with right child node", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    tree.addAll(30, 35);

                    final BinaryTree<Integer> addResult = tree.add(40);
                    test.assertSame(tree, addResult);
                    test.assertEqual(Iterable.create(30, 35, 40), tree);
                });
            });

            runner.testGroup("addAll(T...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    test.assertThrows(() -> tree.addAll((Integer[])null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with no values", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addAllResult = tree.addAll();
                    test.assertSame(tree, addAllResult);
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with one value", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addAllResult = tree.addAll(30);
                    test.assertSame(tree, addAllResult);
                    test.assertEqual(Iterable.create(30), tree);
                });

                runner.test("with multiple values", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addAllResult = tree.addAll(30, 10, 40, 50, 60);
                    test.assertSame(tree, addAllResult);
                    test.assertEqual(Iterable.create(10, 30, 40, 50, 60), tree);
                });
            });

            runner.testGroup("addAll(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    test.assertThrows(() -> tree.addAll((Iterable<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with no values", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addAllResult = tree.addAll(Iterable.create());
                    test.assertSame(tree, addAllResult);
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with one value", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addAllResult = tree.addAll(Iterable.create(30));
                    test.assertSame(tree, addAllResult);
                    test.assertEqual(Iterable.create(30), tree);
                });

                runner.test("with multiple values", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addAllResult = tree.addAll(Iterable.create(30, 10, 40, 50, 60));
                    test.assertSame(tree, addAllResult);
                    test.assertEqual(Iterable.create(10, 30, 40, 50, 60), tree);
                });
            });

            runner.testGroup("addAll(Iterator<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    test.assertThrows(() -> tree.addAll((Iterator<Integer>)null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with no values", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addAllResult = tree.addAll(Iterator.create());
                    test.assertSame(tree, addAllResult);
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with one value", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addAllResult = tree.addAll(Iterator.create(30));
                    test.assertSame(tree, addAllResult);
                    test.assertEqual(Iterable.create(30), tree);
                });

                runner.test("with multiple values", (Test test) ->
                {
                    final BinaryTree<Integer> tree = creator.run();
                    final BinaryTree<Integer> addAllResult = tree.addAll(Iterator.create(30, 10, 40, 50, 60));
                    test.assertSame(tree, addAllResult);
                    test.assertEqual(Iterable.create(10, 30, 40, 50, 60), tree);
                });
            });

            runner.testGroup("remove(T)", () ->
            {
                final Action4<Iterable<Integer>,Integer,Boolean,Iterable<Integer>> removeTest = (Iterable<Integer> values, Integer toRemove, Boolean expectedResult, Iterable<Integer> expectedValues) ->
                {
                    runner.test("with " + English.andList(values, toRemove), (Test test) ->
                    {
                        final BinaryTree<Integer> tree = creator.run()
                            .addAll(values);
                        test.assertEqual(expectedResult, tree.remove(toRemove));
                        test.assertEqual(expectedValues, tree);
                    });
                };

                removeTest.run(
                    Iterable.create(),
                    null,
                    false,
                    Iterable.create());
                removeTest.run(
                    Iterable.create(),
                    1,
                    false,
                    Iterable.create());
                removeTest.run(
                    Iterable.create(10),
                    5,
                    false,
                    Iterable.create(10));
                removeTest.run(
                    Iterable.create(10),
                    10,
                    true,
                    Iterable.create());
                removeTest.run(
                    Iterable.create(2, 1),
                    1,
                    true,
                    Iterable.create(2));
                removeTest.run(
                    Iterable.create(1, 2),
                    2,
                    true,
                    Iterable.create(1));
                removeTest.run(
                    Iterable.create(1, 2),
                    1,
                    true,
                    Iterable.create(2));
                removeTest.run(
                    Iterable.create(1, 2, 3),
                    2,
                    true,
                    Iterable.create(1, 3));
                removeTest.run(
                    Iterable.create(3, 1, 2),
                    1,
                    true,
                    Iterable.create(2, 3));
                removeTest.run(
                    Iterable.create(3, 2, 1),
                    2,
                    true,
                    Iterable.create(1, 3));
                removeTest.run(
                    Iterable.create(1, 3, 2),
                    3,
                    true,
                    Iterable.create(1, 2));
                removeTest.run(
                    Iterable.create(1, 3, 2, 4),
                    3,
                    true,
                    Iterable.create(1, 2, 4));
                removeTest.run(
                    Iterable.create(4, 2, 3, 1),
                    4,
                    true,
                    Iterable.create(1, 2, 3));
                removeTest.run(
                    Iterable.create(4, 2, 3, 1),
                    2,
                    true,
                    Iterable.create(1, 3, 4));
                removeTest.run(
                    Iterable.create(4, 2, 3, 9, 8, 7),
                    4,
                    true,
                    Iterable.create(2, 3, 7, 8, 9));
                removeTest.run(
                    Iterable.create(10, 4, 2, 3, 9, 8, 7),
                    4,
                    true,
                    Iterable.create(2, 3, 7, 8, 9, 10));
            });

            runner.testGroup("contains(T)", () ->
            {
                final Action3<Iterable<Integer>,Integer,Boolean> containsTest = (Iterable<Integer> values, Integer value, Boolean expected) ->
                {
                    runner.test("with " + English.andList(values, value), (Test test) ->
                    {
                        final BinaryTree<Integer> tree = creator.run()
                            .addAll(values);
                        test.assertEqual(expected, tree.contains(value));
                    });
                };

                containsTest.run(Iterable.create(), null, false);
                containsTest.run(Iterable.create(), 1, false);
                containsTest.run(Iterable.create(1), null, false);
                containsTest.run(Iterable.create(1), 0, false);
                containsTest.run(Iterable.create(1), 2, false);
                containsTest.run(Iterable.create(1), 1, true);
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Iterable<Integer>,String> toStringTest = (Iterable<Integer> values, String expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        final BinaryTree<Integer> tree = creator.run()
                            .addAll(values);
                        test.assertEqual(expected, tree.toString());
                    });
                };

                toStringTest.run(Iterable.create(), "[]");
                toStringTest.run(Iterable.create(1), "[1]");
                toStringTest.run(Iterable.create(1, 2), "[1,2]");
                toStringTest.run(Iterable.create(2, 1), "[1,2]");
                toStringTest.run(Iterable.create(5, 7, 1, 4, 10), "[1,4,5,7,10]");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Iterable<Integer>,Object,Boolean> equalsTest = (Iterable<Integer> values, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(values, rhs), (Test test) ->
                    {
                        final BinaryTree<Integer> tree = creator.run()
                            .addAll(values);
                        test.assertEqual(expected, tree.equals(rhs));
                    });
                };

                equalsTest.run(Iterable.create(), null, false);
                equalsTest.run(Iterable.create(), Iterable.create(), true);
                equalsTest.run(Iterable.create(1, 2, 3), Iterable.create(1, 2, 3), true);
                equalsTest.run(Iterable.create(3, 2, 1), Iterable.create(1, 2, 3), true);
                equalsTest.run(Iterable.create(1, 2, 3), Iterable.create(3, 2, 1), false);
                equalsTest.run(Iterable.create(3, 2, 1), Iterable.create(3, 2, 1), false);
            });
        });
    }
}
