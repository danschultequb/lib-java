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
                    test.assertThrows(() -> url.setQueryParameter(null, null), new PreConditionFailure("queryParameterName cannot be null."));
                    test.assertNull(url.getQuery());
                });

                runner.test("with null and \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    test.assertThrows(() -> url.setQueryParameter(null, ""), new PreConditionFailure("queryParameterName cannot be null."));
                    test.assertNull(url.getQuery());
                });

                runner.test("with null and \"b\"", (Test test) ->
                {
                    final URL url = new URL();
                    test.assertThrows(() -> url.setQueryParameter(null, "b"), new PreConditionFailure("queryParameterName cannot be null."));
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"\" and null", (Test test) ->
                {
                    final URL url = new URL();
                    test.assertThrows(() -> url.setQueryParameter("", null), new PreConditionFailure("queryParameterName cannot be empty."));
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"\" and \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    test.assertThrows(() -> url.setQueryParameter("", ""), new PreConditionFailure("queryParameterName cannot be empty."));
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"\" and \"b\"", (Test test) ->
                {
                    final URL url = new URL();
                    test.assertThrows(() -> url.setQueryParameter("", "b"), new PreConditionFailure("queryParameterName cannot be empty."));
                    test.assertNull(url.getQuery());
                });

                runner.test("with \"a\" and null", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter("a", null);
                    test.assertEqual("a", url.getQuery());
                    test.assertNull(url.getQueryParameterValue("a").await());
                    test.assertThrows(() -> url.getQueryParameterValue("A").awaitError(), new NotFoundException("Could not find the provided key (A) in this Map."));
                });

                runner.test("with \"a\" and \"\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter("a", "");
                    test.assertEqual("a=", url.getQuery());
                    test.assertEqual("", url.getQueryParameterValue("a").await());
                    test.assertThrows(() -> url.getQueryParameterValue("A").awaitError(), new NotFoundException("Could not find the provided key (A) in this Map."));
                });

                runner.test("with \"a\" and \"b\"", (Test test) ->
                {
                    final URL url = new URL();
                    url.setQueryParameter("a", "b");
                    test.assertEqual("a=b", url.getQuery());
                    test.assertEqual("b", url.getQueryParameterValue("a").await());
                    test.assertThrows(() -> url.getQueryParameterValue("A").awaitError(), new NotFoundException("Could not find the provided key (A) in this Map."));
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

            runner.testGroup("parse(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> URL.parse(null), new PreConditionFailure("urlString cannot be null."));
                });

                runner.test("with \"\"", (Test test) ->
                {
                    test.assertThrows(() -> URL.parse(""), new PreConditionFailure("urlString cannot be empty."));
                });

                runner.test("with \"http\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.toString());
                            test.assertEqual(null, url.getScheme());
                            test.assertEqual("http", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                        });
                });

                runner.test("with \"http:\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http:");
                    test.assertError(new IllegalArgumentException("Could not parse \"http:\" into a URL because it is missing a port number."), urlResult);
                });

                runner.test("with \"http:81\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http:81"),
                        (URL url) ->
                        {
                            test.assertEqual("http:81", url.toString());
                            test.assertEqual(null, url.getScheme());
                            test.assertEqual("http", url.getHost());
                            test.assertEqual(81, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                        });
                });

                runner.test("with \"http:/\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http:/");
                    test.assertError(new IllegalArgumentException("Could not parse \"http:/\" into a URL because it is missing the second forward slash after the scheme."), urlResult);
                });

                runner.test("with \"http://\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://"),
                        (URL url) ->
                        {
                            test.assertEqual("http://", url.toString());
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual(null, url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                        });
                });

                runner.test("with \"http://:\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http://:");
                    test.assertError(new IllegalArgumentException("Could not parse \"http://:\" into a URL because no host was specified before the port."), urlResult);
                });

                runner.test("with \"http:///\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http:///");
                    test.assertError(new IllegalArgumentException("Could not parse \"http:///\" into a URL because no host was specified before the path."), urlResult);
                });

                runner.test("with \"http://?\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http://?");
                    test.assertError(new IllegalArgumentException("Could not parse \"http://?\" into a URL because no host was specified before the query."), urlResult);
                });

                runner.test("with \"http://#\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http://#");
                    test.assertError(new IllegalArgumentException("Could not parse \"http://#\" into a URL because no host was specified before the fragment."), urlResult);
                });

                runner.test("with \"http://www\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www"),
                        (URL url) ->
                        {
                            test.assertEqual("http://www", url.toString());
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                        });
                });

                runner.test("with \"http://www.example.com\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com"),
                        (URL url) ->
                        {
                            test.assertEqual("http://www.example.com", url.toString());
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                        });
                });

                runner.test("with \"http://www.example.com:\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http://www.example.com:");
                    test.assertError(new IllegalArgumentException("Could not parse \"http://www.example.com:\" into a URL because it is missing its port number."), urlResult);
                });

                runner.test("with \"http://www.example.com:whoops\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http://www.example.com:whoops");
                    test.assertError(new IllegalArgumentException("Could not parse \"http://www.example.com:whoops\" into a URL because the port specified was not a number."), urlResult);
                });

                runner.test("with \"http://www.example.com:20\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com:20", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20^\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http://www.example.com:20^");
                    test.assertError(new IllegalArgumentException("Expected \"/\", \"?\", or \"#\", but found \"^\" instead."), urlResult);
                });

                runner.test("with \"http://www.example.com:20/\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20/"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual("/", url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com:20/", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com/\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com/"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual("/", url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com/", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20/a/index.html\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20/a/index.html"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual("/a/index.html", url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com:20/a/index.html", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com/b/page.htm\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com/b/page.htm"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual("/b/page.htm", url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com/b/page.htm", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com?\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com?"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com#\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com#"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual("#", url.getFragment());
                            test.assertEqual("http://www.example.com#", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20?\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20?"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com:20", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20?q1\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20?q1"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual("q1", url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com:20?q1", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20?q1=\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20?q1="),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual("q1=", url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com:20?q1=", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20?q1=v1\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20?q1=v1"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual("q1=v1", url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com:20?q1=v1", url.toString());
                        });
                });

                runner.test("with \"https://www.example.com:20/?\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("https://www.example.com:20/?"),
                        (URL url) ->
                        {
                            test.assertEqual("https", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual("/", url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("https://www.example.com:20/", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20/?q2\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20/?q2"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual("/", url.getPath());
                            test.assertEqual("q2", url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com:20/?q2", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com/?q3=v3&q3.1=v3.1\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com/?q3=v3&q3.1=v3.1"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual("/", url.getPath());
                            test.assertEqual("q3=v3&q3.1=v3.1", url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("http://www.example.com/?q3=v3&q3.1=v3.1", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20#\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20#"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual("#", url.getFragment());
                            test.assertEqual("http://www.example.com:20#", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20#fragment\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20#fragment"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual("#fragment", url.getFragment());
                            test.assertEqual("http://www.example.com:20#fragment", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20/#\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20/#"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual("/", url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual("#", url.getFragment());
                            test.assertEqual("http://www.example.com:20/#", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:20/#fragment\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:20/#fragment"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(20, url.getPort());
                            test.assertEqual("/", url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual("#fragment", url.getFragment());
                            test.assertEqual("http://www.example.com:20/#fragment", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com/#fragment\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com/#fragment"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual("/", url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual("#fragment", url.getFragment());
                            test.assertEqual("http://www.example.com/#fragment", url.toString());
                        });
                });

                runner.test("with \"http://www.example.com:8080/index.html?a=b&c=d#fragmentthing\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("http://www.example.com:8080/index.html?a=b&c=d#fragmentthing"),
                        (URL url) ->
                        {
                            test.assertEqual("http", url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(8080, url.getPort());
                            test.assertEqual("/index.html", url.getPath());
                            test.assertEqual("a=b&c=d", url.getQuery());
                            test.assertEqual("#fragmentthing", url.getFragment());
                            test.assertEqual("http://www.example.com:8080/index.html?a=b&c=d#fragmentthing", url.toString());
                        });
                });

                runner.test("with \".\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse(".");
                    test.assertError(new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \".\"."), urlResult);
                });

                runner.test("with \"%\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("%");
                    test.assertError(new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"%\"."), urlResult);
                });

                runner.test("with \"www.example.com\"", (Test test) ->
                {
                    test.assertSuccess(URL.parse("www.example.com"),
                        (URL url) ->
                        {
                            test.assertEqual(null, url.getScheme());
                            test.assertEqual("www.example.com", url.getHost());
                            test.assertEqual(null, url.getPort());
                            test.assertEqual(null, url.getPath());
                            test.assertEqual(null, url.getQuery());
                            test.assertEqual(null, url.getFragment());
                            test.assertEqual("www.example.com", url.toString());
                        });
                });

                runner.test("with \"http:*\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http:*");
                    test.assertError(new IllegalArgumentException("After the scheme or host (http) and a colon, the following text must be either a forward slash or a port number."), urlResult);
                });

                runner.test("with \"http:/*\"", (Test test) ->
                {
                    final Result<URL> urlResult = URL.parse("http:/*");
                    test.assertError(new IllegalArgumentException("Could not parse \"http:/*\" into a URL because the scheme (http) must be followed by \"://\"."), urlResult);
                });
            });
        });
    }
}
