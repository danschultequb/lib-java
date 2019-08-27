package qub;

public interface Action0Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Action0.class, () ->
        {
            runner.test("empty", (Test test) ->
            {
                test.assertNotNull(Action0.empty);
                Action0.empty.run();
            });
        });
    }
}
