package qub;

public class ConcurrentHashMapTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ConcurrentHashMap.class, () ->
        {
            MapTests.test(runner, ConcurrentHashMap::new, false, false);
        });
    }
}
