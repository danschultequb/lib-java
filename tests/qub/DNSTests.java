package qub;

public interface DNSTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(DNS.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final DNS dns = DNS.create();
                test.assertNotNull(dns);
                test.assertInstanceOf(dns, JavaDNS.class);
            });
        });
    }

    static void test(TestRunner runner, Function0<DNS> creator)
    {
        runner.testGroup(DNS.class, () ->
        {
            runner.testGroup("resolveHost(String)", () ->
            {
                final Action2<String,Throwable> resolveHostErrorTest = (String host, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(host), (Test test) ->
                    {
                        final DNS dns = creator.run();
                        test.assertThrows(() -> dns.resolveHost(host).await(),
                            expected);
                    });
                };

                resolveHostErrorTest.run(null, new PreConditionFailure("host cannot be null."));
                resolveHostErrorTest.run("", new PreConditionFailure("host cannot be empty."));
                resolveHostErrorTest.run("spam.example.com", new HostNotFoundException("spam.example.com"));
                resolveHostErrorTest.run("com", new HostNotFoundException("com"));
                resolveHostErrorTest.run("www.notavalidwebpageurlidontexist.com", new HostNotFoundException("www.notavalidwebpageurlidontexist.com"));

                final Action2<String,IPv4Address> resolveHostTest = (String host, IPv4Address expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(host), (Test test) ->
                    {
                        final DNS dns = creator.run();
                        test.assertEqual(expected, dns.resolveHost(host).await());
                    });
                };

                resolveHostTest.run("1.2.3.4", IPv4Address.parse("1.2.3.4").await());
                resolveHostTest.run("www.example.com", IPv4Address.parse("93.184.216.34").await());
                resolveHostTest.run("example.com", IPv4Address.parse("93.184.216.34").await());
            });
        });
    }
}
