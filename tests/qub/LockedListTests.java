package qub;

public class LockedListTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(LockedList.class, () ->
        {
            ListTests.test(runner, (Integer count) ->
            {
                final List<Integer> result = LockedList.from(new ArrayList<>());
                for (int i = 0; i < count; ++i)
                {
                    result.add(i);
                }
                return result;
            });
        });
    }
}
