package qub;

public class SpinMutexTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SpinMutex.class, () ->
        {
            MutexTests.test(runner, SpinMutex::create);
        });
    }
}
