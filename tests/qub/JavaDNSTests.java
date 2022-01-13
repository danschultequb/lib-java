package qub;

public interface JavaDNSTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaDNS.class, () ->
        {
            DNSTests.test(runner, JavaDNS::create);
        });
    }
}
