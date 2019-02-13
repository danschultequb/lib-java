package qub;

public class Action0Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Action0.class, () ->
        {
            runner.test("empty", (Test test) ->
            {
                test.assertNotNull(Action0.empty);
            });
        });
    }
}
