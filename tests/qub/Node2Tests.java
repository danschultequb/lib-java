package qub;

public interface Node2Tests
{
    static void test(final TestRunner runner)
    {
        runner.testGroup(Node2.class, () ->
        {
            runner.test("create(T)", (Test test) ->
            {
                final Node2<Integer> node = Node2.create(20);
                test.assertEqual(20, node.getValue());
                test.assertNull(node.getNode1());
                test.assertNull(node.getNode2());
            });
            
            runner.testGroup("setValue()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Node2<Integer> node = Node2.create(10);
                    final Node2<Integer> setValueResult = node.setValue(null);
                    test.assertSame(node, setValueResult);
                    test.assertNull(node.getValue());
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Node2<Integer> node = Node2.create(10);
                    final Node2<Integer> setValueResult = node.setValue(20);
                    test.assertSame(node, setValueResult);
                    test.assertEqual(20, node.getValue());
                });
            });
            
            runner.test("setNode1(Node2<T>)", (Test test) ->
            {
                final Node2<Integer> node = Node2.create(11);
                final Node2<Integer> setNode1Result = node.setNode1(node);
                test.assertSame(node, setNode1Result);
                test.assertSame(node, node.getNode1());
            });

            runner.test("setNode2(Node2<T>)", (Test test) ->
            {
                final Node2<Integer> node = Node2.create(11);
                final Node2<Integer> setNode2Result = node.setNode2(node);
                test.assertSame(node, setNode2Result);
                test.assertSame(node, node.getNode2());
            });
        });
    }
}
