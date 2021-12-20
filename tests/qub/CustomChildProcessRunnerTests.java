package qub;

public interface CustomChildProcessRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CustomChildProcessRunner.class, () ->
        {
            runner.testGroup("create(ChildProcessRunner)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CustomChildProcessRunner.create(null),
                        new PreConditionFailure("innerRunner cannot be null."));
                });
            });
        });
    }
}
