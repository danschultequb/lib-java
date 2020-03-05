package qub;

public interface BasicRunnableEvent2Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicRunnableEvent2.class, () ->
        {
            RunnableEvent2Tests.test(runner, BasicRunnableEvent2::create);
        });
    }
}
