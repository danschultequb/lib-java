package qub;

public interface Node2ListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Node2List.class, () ->
        {
            ListTests.test(runner, (Integer count) ->
            {
                final Node2List<Integer> result = Node2List.create();
                for (int i = 0; i < count; ++i)
                {
                    result.add(i);
                }
                return result;
            });
        });
    }
}
