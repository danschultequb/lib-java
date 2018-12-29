package qub;

public class SingleLinkListTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SingleLinkList.class, () ->
        {
            ListTests.test(runner, (Integer count) ->
            {
                final SingleLinkList<Integer> result = new SingleLinkList<>();
                for (int i = 0; i < count; ++i)
                {
                    result.add(i);
                }
                return result;
            });
        });
    }
}
