package qub;

public class JavaTCPClientTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaTCPClient.class, () ->
        {
            runner.testGroup("create(Socket,AsyncRunner)", () ->
            {
                runner.test("with null Socket", (Test test) ->
                {
                    test.assertThrows(() -> JavaTCPClient.create(null),
                        new PreConditionFailure("socket cannot be null."));
                });
            });
        });
    }
}
