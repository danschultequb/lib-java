package qub;

public class JavaDNSTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(JavaDNS.class, () ->
        {
            DNSTests.test(runner, JavaDNS::new);
        });
    }
}
