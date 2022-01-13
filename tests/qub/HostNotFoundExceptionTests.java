package qub;

public interface HostNotFoundExceptionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(HostNotFoundException.class, () ->
        {
            runner.testGroup("constructor(String)", () ->
            {
                final Action2<String,Throwable> constructorErrorTest = (String host, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(host), (Test test) ->
                    {
                        test.assertThrows(() -> new HostNotFoundException(host), expected);
                    });
                };

                constructorErrorTest.run(null, new PreConditionFailure("host cannot be null."));
                constructorErrorTest.run("", new PreConditionFailure("host cannot be empty."));

                final Action1<String> constructorTest = (String host) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(host), (Test test) ->
                    {
                        final HostNotFoundException e = new HostNotFoundException(host);
                        test.assertNotNull(e);
                        test.assertEqual(host, e.getHost());
                        test.assertEqual("Could not resolve the IP address for the provided host: " + Strings.escapeAndQuote(host), e.getMessage());
                    });
                };

                constructorTest.run("hello");
                constructorTest.run("192.168.0.1");
            });
        });
    }
}
