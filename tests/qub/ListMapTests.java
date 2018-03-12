package qub;

public class ListMapTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ListMap.class, () ->
        {
            MapTests.test(runner, ListMap::new);
        });
    }
}
