package qub;

public interface Node1ListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Node1List.class, () ->
        {
            ListTests.test(runner, (Integer count) ->
            {
                final Node1List<Integer> result = Node1List.create();
                for (int i = 0; i < count; ++i)
                {
                    result.add(i);
                }
                return result;
            });
        });
    }
}
