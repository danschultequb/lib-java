package qub;

public interface FixedDynamicDistanceTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FixedDynamicDistance.class, () ->
        {
            DynamicDistanceTests.test(runner, () -> FixedDynamicDistance.create(Distance.inches(1)));
        });
    }
}
