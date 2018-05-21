package qub;

public class URLTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(URL.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final URL url = new URL();
                test.assertNull(url.getScheme());
                test.assertNull(url.getHost());
                test.assertNull(url.getPort());
                test.assertNull(url.getPath());
                test.assertNull(url.getQuery());
                test.assertNull(url.getFragment());
                test.assertEqual("", url.toString());
            });

            runner.testGroup("setScheme(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final URL url = new URL();
                    url.setScheme(null);
                    test.assertNull(url.getScheme());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setScheme("");
                    test.assertNull(url.getScheme());
                });

                runner.test("with \"https\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setScheme("https");
                    test.assertEqual("https", url.getScheme());
                });
            });

            runner.testGroup("setHost(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                   final URL url = new URL();
                   url.setHost(null);
                   test.assertNull(url.getHost());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setHost("");
                    test.assertNull(url.getHost());
                });

                runner.test("with \"www.example.com\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setHost("www.example.com");
                    test.assertEqual("www.example.com", url.getHost());
                });
            });

            runner.testGroup("setPort(Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPort(null);
                    test.assertNull(url.getPort());
                });

                runner.test("with -20", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPort(-20);
                    test.assertEqual(-20, url.getPort());
                });

                runner.test("with 0", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPort(0);
                    test.assertEqual(0, url.getPort());
                });

                runner.test("with 1", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPort(1);
                    test.assertEqual(1, url.getPort());
                });

                runner.test("with 80", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPort(80);
                    test.assertEqual(80, url.getPort());
                });

                runner.test("with 65535", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPort(65535);
                    test.assertEqual(65535, url.getPort());
                });

                runner.test("with 65536", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPort(65536);
                    test.assertEqual(65536, url.getPort());
                });
            });

            runner.testGroup("setPath(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPath(null);
                    test.assertNull(url.getPath());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPath("");
                    test.assertNull(url.getPath());
                });

                runner.test("with \"/\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPath("/");
                    test.assertEqual("/", url.getPath());
                });

                runner.test("with \"/index.html\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPath("/index.html");
                    test.assertEqual("/index.html", url.getPath());
                });

                runner.test("with \"index.html\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPath("index.html");
                    test.assertEqual("index.html", url.getPath());
                });

                runner.test("with \"/my/index.html\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPath("/my/index.html");
                    test.assertEqual("/my/index.html", url.getPath());
                });

                runner.test("with \"my/index.html\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setPath("my/index.html");
                    test.assertEqual("my/index.html", url.getPath());
                });
            });

            runner.testGroup("setQuery(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery(null);
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("");
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"?\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("?");
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"a\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("a");
                    test.assertEqual("a", url.getQuery());
                    test.assertSuccess(null, url.getQueryParameterValue("a"));
                });

                runner.test("with \"a=\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("a=");
                    test.assertEqual("a=", url.getQuery());
                    test.assertSuccess("", url.getQueryParameterValue("a"));
                });

                runner.test("with \"a=b\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("a=b");
                    test.assertEqual("a=b", url.getQuery());
                    test.assertSuccess("b", url.getQueryParameterValue("a"));
                });

                runner.test("with \"&\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("&");
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"a&\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("a&");
                    test.assertEqual("a", url.getQuery());
                    test.assertSuccess(null, url.getQueryParameterValue("a"));
                });

                runner.test("with \"a=b&\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("a=b&");
                    test.assertEqual("a=b", url.getQuery());
                    test.assertSuccess("b", url.getQueryParameterValue("a"));
                });

                runner.test("with \"a=b&c\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("a=b&c");
                    test.assertEqual("a=b&c", url.getQuery());
                    test.assertSuccess("b", url.getQueryParameterValue("a"));
                    test.assertSuccess(null, url.getQueryParameterValue("c"));
                });

                runner.test("with \"a=b&c&\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("a=b&c&");
                    test.assertEqual("a=b&c", url.getQuery());
                    test.assertSuccess("b", url.getQueryParameterValue("a"));
                    test.assertSuccess(null, url.getQueryParameterValue("c"));
                });

                runner.test("with \"a=b&c=&\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("a=b&c=&");
                    test.assertEqual("a=b&c=", url.getQuery());
                    test.assertSuccess("b", url.getQueryParameterValue("a"));
                    test.assertSuccess("", url.getQueryParameterValue("c"));
                });

                runner.test("with \"=bad&\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQuery("=bad&");
                    test.assertNull(url.getQuery());
                });
            });

            runner.testGroup("setQueryParameter(String,String)", () ->
            {
                runner.test("with null and null", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter(null, null);
                    test.assertNull(url.getQuery());
                });

                runner.test("with null and \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter(null, "");
                    test.assertNull(url.getQuery());
                });

                runner.test("with null and \"b\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter(null, "b");
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"\" and null", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter("", null);
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"\" and \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter("", "");
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"\" and \"b\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter("", "b");
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"a\" and null", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter("a", null);
                    test.assertEqual("a", url.getQuery());
                    test.assertSuccess(null, url.getQueryParameterValue("a"));
                    test.assertError(new NotFoundException("A"), url.getQueryParameterValue("A"));
                });

                runner.test("with \"a\" and \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter("a", "");
                    test.assertEqual("a=", url.getQuery());
                    test.assertSuccess("", url.getQueryParameterValue("a"));
                    test.assertError(new NotFoundException("A"), url.getQueryParameterValue("A"));
                });

                runner.test("with \"a\" and \"b\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter("a", "b");
                    test.assertEqual("a=b", url.getQuery());
                    test.assertSuccess("b", url.getQueryParameterValue("a"));
                    test.assertError(new NotFoundException("A"), url.getQueryParameterValue("A"));
                });
            });

            runner.testGroup("setFragment(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final URL url = new URL();
                    url.setFragment(null);
                    test.assertNull(url.getFragment());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setFragment("");
                    test.assertNull(url.getFragment());
                });

                runner.test("with \"#\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setFragment("#");
                    test.assertEqual("#", url.getFragment());
                });

                runner.test("with \"part1\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setFragment("part1");
                    test.assertEqual("part1", url.getFragment());
                });

                runner.test("with \"#part1\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setFragment("#part1");
                    test.assertEqual("#part1", url.getFragment());
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with scheme", (Test test) ->
                {
                    final URL url = new URL();
                    url.setScheme("ftp");
                    test.assertEqual("ftp://", url.toString());
                });

                runner.test("with scheme and host", (Test test) ->
                {
                    final URL url = new URL();
                    url.setScheme("ftp");
                    url.setHost("www.example.com");
                    test.assertEqual("ftp://www.example.com", url.toString());
                });

                runner.test("with scheme, host, and port", (Test test) ->
                {
                    final URL url = new URL();
                    url.setScheme("ftp");
                    url.setHost("www.example.com");
                    url.setPort(8080);
                    test.assertEqual("ftp://www.example.com:8080", url.toString());
                });

                runner.test("with host and port", (Test test) ->
                {
                    final URL url = new URL();
                    url.setHost("www.example.com");
                    url.setPort(8080);
                    test.assertEqual("www.example.com:8080", url.toString());
                });

                runner.test("with scheme, host, port, and path", (Test test) ->
                {
                    final URL url = new URL();
                    url.setScheme("ftp");
                    url.setHost("www.example.com");
                    url.setPort(8080);
                    url.setPath("my/index.html");
                    test.assertEqual("ftp://www.example.com:8080/my/index.html", url.toString());
                });

                runner.test("with host, port, and path", (Test test) ->
                {
                    final URL url = new URL();
                    url.setHost("www.example.com");
                    url.setPort(8080);
                    url.setPath("your/index.html");
                    test.assertEqual("www.example.com:8080/your/index.html", url.toString());
                });

                runner.test("with host and path", (Test test) ->
                {
                    final URL url = new URL();
                    url.setHost("www.example.com");
                    url.setPath("/");
                    test.assertEqual("www.example.com/", url.toString());
                });

                runner.test("with host and query", (Test test) ->
                {
                    final URL url = new URL();
                    url.setHost("www.example.com");
                    url.setQuery("?a=b&c=d&e&f=");
                    test.assertEqual("www.example.com?a=b&c=d&e&f=", url.toString());
                });

                runner.test("with host and fragment that starts with #", (Test test) ->
                {
                    final URL url = new URL();
                    url.setHost("www.example.com");
                    url.setFragment("#firstHeading");
                    test.assertEqual("www.example.com#firstHeading", url.toString());
                });

                runner.test("with host and fragment that doesn't start with #", (Test test) ->
                {
                    final URL url = new URL();
                    url.setHost("www.example.com");
                    url.setFragment("firstHeading");
                    test.assertEqual("www.example.com#firstHeading", url.toString());
                });
            });
        });
    }
}
