package qub;

public class FakeDNSTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FakeDNS.class, () ->
        {
            DNSTests.test(runner, () ->
            {
                final FakeDNS dns = new FakeDNS();
                dns.set("www.example.com", IPv4Address.parse("93.184.216.34"));
                dns.set("example.com", IPv4Address.parse("93.184.216.34"));
                return dns;
            });
        });
    }
}
