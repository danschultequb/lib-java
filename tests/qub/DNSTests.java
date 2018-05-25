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
                    test.assertError(new IllegalArgumentException("host cannot be null."), dns.resolveHost(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertError(new IllegalArgumentException("host cannot be empty."), dns.resolveHost(""));
                });

                runner.test("with \"1.2.3.4\"", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertSuccess(IPv4Address.parse("1.2.3.4"), dns.resolveHost("1.2.3.4"));
                });

                runner.test("with \"www.example.com\"", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertSuccess(IPv4Address.parse("93.184.216.34"), dns.resolveHost("www.example.com"));
                });

                runner.test("with \"example.com\"", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertSuccess(IPv4Address.parse("93.184.216.34"), dns.resolveHost("example.com"));
                });

                runner.test("with \"com\"", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertError(new java.net.UnknownHostException("No such host is known (com)"), dns.resolveHost("com"));
                });

                runner.test("with \"www.notavalidwebpageurlidontexist.com\"", (Test test) ->
                {
                    final DNS dns = creator.run();
                    test.assertError(new java.net.UnknownHostException("No such host is known (www.notavalidwebpageurlidontexist.com)"), dns.resolveHost("www.notavalidwebpageurlidontexist.com"));
                });
            });
        });
    }
}
