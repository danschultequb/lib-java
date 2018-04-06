package qub;

public class LockedMapTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(LockedMap.class, () ->
        {
            MapTests.test(runner, () -> new LockedMap<>(new ListMap<>(), new SpinMutex()), true, true);
        });
    }
}
