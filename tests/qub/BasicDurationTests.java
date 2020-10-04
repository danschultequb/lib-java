package qub;

public interface BasicDurationTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicDuration.class, () ->
        {
            DurationTests.test(runner, BasicDuration::create);
        });
    }
}
