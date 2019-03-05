package qub;

public class DNSTests
{
    public static void test(TestRunner runner, Function0<DNS> creator)
    {
        runner.testGroup(DNS.class, () ->
        {
            runner.testGroup("resolveHost(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertThrows(() -> dns.resolveHost(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertThrows(() -> dns.resolveHost(""));
                });

                runner.test("with \"1.2.3.4\"", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertSuccess(IPv4Address.parse("1.2.3.4"), dns.resolveHost("1.2.3.4"));
                });

                runner.test("with \"www.example.com\"", runner.skip(!runner.hasNetworkConnection().await()), (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertSuccess(IPv4Address.parse("93.184.216.34"), dns.resolveHost("www.example.com"));
                });

                runner.test("with \"spam.example.com\"", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertError(new java.net.UnknownHostException("spam.example.com"), dns.resolveHost("spam.example.com"));
                });

                runner.test("with \"example.com\"", runner.skip(!runner.hasNetworkConnection().await()), (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertSuccess(IPv4Address.parse("93.184.216.34"), dns.resolveHost("example.com"));
                });

                runner.test("with \"com\"", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertError(new java.net.UnknownHostException("com"), dns.resolveHost("com"));
                });

                runner.test("with \"www.notavalidwebpageurlidontexist.com\"", (Test test) ->
                {
                    final DNS dns = creator.run();
                    dns.resolveHost("www.notavalidwebpageurlidontexist.com")
                        .then((IPv4Address resolvedAddress) ->
                        {
                            // Some internet service providers detect a failed DNS query and will redirect the query to
                            // a web search instead.
                            test.assertNull(resolvedAddress);
                        })
                        .catchError(java.net.UnknownHostException.class, (java.net.UnknownHostException error) ->
                        {
                            test.assertEqual("www.notavalidwebpageurlidontexist.com", error.getMessage());
                        });
                });
            });
        });
    }
}
