package qub;

public interface BasicRunnableEvent1Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicRunnableEvent1.class, () ->
        {
            RunnableEvent1Tests.test(runner, BasicRunnableEvent1::create);
        });
    }
}
