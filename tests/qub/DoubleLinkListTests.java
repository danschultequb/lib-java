package qub;

public class DoubleLinkListTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(DoubleLinkList.class, () ->
        {
            ListTests.test(runner, (Integer count) ->
            {
                final DoubleLinkList<Integer> result = new DoubleLinkList<>();
                for (int i = 0; i < count; ++i)
                {
                    result.add(i);
                }
                return result;
            });
        });
    }
}
