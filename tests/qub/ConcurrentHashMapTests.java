package qub;

public interface ConcurrentHashMapTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ConcurrentHashMap.class, () ->
        {
            MutableMapTests.test(runner, ConcurrentHashMap::create, false, false);
        });
    }
}
