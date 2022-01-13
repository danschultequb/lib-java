package qub;

public interface FakeDNSTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeDNS.class, () ->
        {
            DNSTests.test(runner, () ->
            {
                return FakeDNS.create()
                    .set("www.example.com", IPv4Address.parse("93.184.216.34").await())
                    .set("example.com", IPv4Address.parse("93.184.216.34").await())
                    .set("missie", IPv4Address.parse("192.168.0.175").await());
            });
        });
    }
}
