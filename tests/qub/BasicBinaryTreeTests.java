package qub;

public interface BasicBinaryTreeTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicBinaryTree.class, () ->
        {
            BinaryTreeTests.test(runner, BasicBinaryTree::create);

            runner.testGroup("create(Function2<T,T,Comparison>,T...)", () ->
            {
                runner.test("with null comparer and no values", (Test test) ->
                {
                    test.assertThrows(() -> BasicBinaryTree.create((Function2<Integer,Integer,Comparison>)null),
                        new PreConditionFailure("comparer cannot be null."));
                });

                runner.test("with non-null comparer and no values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create(comparer);
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with non-null comparer and null values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    test.assertThrows(() -> BasicBinaryTree.create(comparer, (String[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with non-null comparer and one value", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create(comparer, "apples");
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples"), tree);
                });

                runner.test("with non-null comparer and two values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create(comparer, "apples", "bananas");
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples", "bananas"), tree);
                });
            });

            runner.testGroup("create(Function2<T,T,Comparison>,Iterable<T>)", () ->
            {
                runner.test("with null comparer", (Test test) ->
                {
                    test.assertThrows(() -> BasicBinaryTree.create((Function2<Integer,Integer,Comparison>)null, Iterable.create()),
                        new PreConditionFailure("comparer cannot be null."));
                });

                runner.test("with null values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    test.assertThrows(() -> BasicBinaryTree.create(comparer, (Iterable<String>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with non-null comparer and one value", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create(comparer, Iterable.create("apples"));
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples"), tree);
                });

                runner.test("with non-null comparer and two values", (Test test) ->
                {
                    final Function2<String,String,Comparison> comparer = (String lhs, String rhs) -> Comparer.compare(lhs.toLowerCase(), rhs.toLowerCase());
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create(comparer, Iterable.create("apples", "bananas"));
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples", "bananas"), tree);
                });
            });

            runner.testGroup("create(T...)", () ->
            {
                runner.test("no values", (Test test) ->
                {
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create();
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> BasicBinaryTree.create((String[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with one value", (Test test) ->
                {
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create("apples");
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples"), tree);
                });

                runner.test("with two values", (Test test) ->
                {
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create("apples", "bananas");
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples", "bananas"), tree);
                });
            });

            runner.testGroup("create(Iterable<T>)", () ->
            {
                runner.test("no values", (Test test) ->
                {
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create(Iterable.<String>create());
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create(), tree);
                });

                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> BasicBinaryTree.create((Iterable<String>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with one value", (Test test) ->
                {
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create(Iterable.create("apples"));
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples"), tree);
                });

                runner.test("with two values", (Test test) ->
                {
                    final BasicBinaryTree<String> tree = BasicBinaryTree.create(Iterable.create("apples", "bananas"));
                    test.assertNotNull(tree);
                    test.assertEqual(Iterable.create("apples", "bananas"), tree);
                });
            });
        });
    }
}
