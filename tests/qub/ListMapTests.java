package qub;

public class ListMapTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ListMap.class, () ->
        {
            MutableMapTests.test(runner, ListMap::new, true, true);
        });
    }
}
