package qub;

public interface Node1Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Node1.class, () ->
        {
            runner.testGroup("create(T)", () ->
            {
                runner.test("with null", test ->
                {
                    final Node1<Integer> node = Node1.create(null);
                    test.assertNull(node.getValue());
                    test.assertNull(node.getNode1());
                });

                runner.test("with non-null", test ->
                {
                    final Node1<Integer> node = Node1.create(55);
                    test.assertEqual(55, node.getValue());
                    test.assertNull(node.getNode1());
                });
            });

            runner.testGroup("setValue()", () ->
            {
                runner.test("with null", test ->
                {
                    final Node1<Integer> node = Node1.create(33);
                    node.setValue(null);
                    test.assertNull(node.getValue());
                });

                runner.test("with non-null", test ->
                {
                    final Node1<Integer> node = Node1.create(33);
                    node.setValue(44);
                    test.assertEqual(44, node.getValue());
                });
            });

            runner.testGroup("setNode1(Node1)", () ->
            {
                runner.test("with null", test ->
                {
                    final Node1<Integer> node = Node1.create(66);
                    final Node1<Integer> setNode1Result = node.setNode1(null);
                    test.assertSame(node, setNode1Result);
                    test.assertNull(node.getNode1());
                });

                runner.test("with non-null", test ->
                {
                    final Node1<Integer> node = Node1.create(77);
                    final Node1<Integer> setNode1Result = node.setNode1(node);
                    test.assertSame(node, setNode1Result);
                    test.assertSame(node, node.getNode1());
                });
            });
        });
    }
}
