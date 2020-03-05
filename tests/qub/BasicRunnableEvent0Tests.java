package qub;

public interface BasicRunnableEvent0Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicRunnableEvent0.class, () ->
        {
            RunnableEvent0Tests.test(runner, BasicRunnableEvent0::create);
        });
    }
}
