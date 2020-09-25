package qub;

public interface URLTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(URL.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableURL url = URL.create();
                test.assertNull(url.getScheme());
                test.assertNull(url.getHost());
                test.assertNull(url.getPort());
                test.assertNull(url.getPath());
                test.assertNull(url.getQuery());
                test.assertNull(url.getFragment());
                test.assertEqual("", url.toString());
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<URL,String> toStringTest = (URL url, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(url), (Test test) ->
                    {
                        test.assertEqual(expected, url.toString());
                    });
                };

                toStringTest.run(URL.create(), "");
                toStringTest.run(
                    URL.create().setScheme("ftp"),
                    "ftp://");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com"),
                    "ftp://www.example.com");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com").setPort(8080),
                    "ftp://www.example.com:8080");
                toStringTest.run(
                    URL.create().setHost("www.example.com").setPort(8080),
                    "www.example.com:8080");
                toStringTest.run(
                    URL.create()
                        .setScheme("ftp")
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("my/index.html"),
                    "ftp://www.example.com:8080/my/index.html");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("your/index.html"),
                    "www.example.com:8080/your/index.html");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPath("/"),
                    "www.example.com/");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setQuery("?a=b&c=d&e&f="),
                    "www.example.com?a=b&c=d&e&f=");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("#firstHeading"),
                    "www.example.com#firstHeading");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("firstHeading"),
                    "www.example.com#firstHeading");
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String urlString, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(urlString), (Test test) ->
                    {
                        test.assertThrows(() -> URL.parse(urlString).await(), expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("urlString cannot be null."));
                parseErrorTest.run("", new PreConditionFailure("urlString cannot be empty."));
                parseErrorTest.run("http:", new IllegalArgumentException("Could not parse \"http:\" into a URL because it is missing a port number."));
                parseErrorTest.run("http:/", new IllegalArgumentException("Could not parse \"http:/\" into a URL because it is missing the second forward slash after the scheme."));
                parseErrorTest.run("http://:", new IllegalArgumentException("Could not parse \"http://:\" into a URL because no host was specified before the port."));
                parseErrorTest.run("http:///", new IllegalArgumentException("Could not parse \"http:///\" into a URL because no host was specified before the path."));
                parseErrorTest.run("http://?", new IllegalArgumentException("Could not parse \"http://?\" into a URL because no host was specified before the query."));
                parseErrorTest.run("http://#", new IllegalArgumentException("Could not parse \"http://#\" into a URL because no host was specified before the fragment."));
                parseErrorTest.run("http://www.example.com:", new IllegalArgumentException("Could not parse \"http://www.example.com:\" into a URL because it is missing its port number."));
                parseErrorTest.run("https://example.com:whoops", new IllegalArgumentException("Could not parse \"https://example.com:whoops\" into a URL because the port specified was not a number."));
                parseErrorTest.run("https://www.example.com:20^", new IllegalArgumentException("Expected \"/\", \"?\", or \"#\", but found \"^\" instead."));
                parseErrorTest.run(".", new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \".\"."));
                parseErrorTest.run("%", new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"%\"."));
                parseErrorTest.run("http:*", new IllegalArgumentException("After the scheme or host (http) and a colon, the following text must be either a forward slash or a port number."));
                parseErrorTest.run("http:/*", new IllegalArgumentException("Could not parse \"http:/*\" into a URL because the scheme (http) must be followed by \"://\"."));

                final Action2<String,MutableURL> parseTest = (String urlString, MutableURL expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(urlString), (Test test) ->
                    {
                        final MutableURL url = URL.parse(urlString).await();
                        test.assertNotNull(url);
                        test.assertEqual(expected, url);
                    });
                };

                parseTest.run("http", URL.create().setHost("http"));
                parseTest.run("www.example.com", URL.create().setHost("www.example.com"));
                parseTest.run("http:81", URL.create().setHost("http").setPort(81));
                parseTest.run("http://", URL.create().setScheme("http"));
                parseTest.run("https://www", URL.create().setScheme("https").setHost("www"));
                parseTest.run("https://www.example.com", URL.create().setScheme("https").setHost("www.example.com"));
                parseTest.run("http://www.example.com:20", URL.create().setScheme("http").setHost("www.example.com").setPort(20));
                parseTest.run("http://www.example.com:20/", URL.create().setScheme("http").setHost("www.example.com").setPort(20).setPath("/"));
                parseTest.run("http://www.example.com/", URL.create().setScheme("http").setHost("www.example.com").setPath("/"));
                parseTest.run(
                    "https://www.example.com:20/a/index.html",
                    URL.create()
                        .setScheme("https")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setPath("/a/index.html"));
                parseTest.run(
                    "http://www.example.com/b/page.htm",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPath("/b/page.htm"));
                parseTest.run(
                    "http://www.example.com?",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com"));
                parseTest.run(
                    "http://www.example.com#",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setFragment("#"));
                parseTest.run(
                    "http://www.example.com:20?",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20));
                parseTest.run(
                    "http://www.example.com:20?q1",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setQuery("?q1"));
                parseTest.run(
                    "http://www.example.com:20?q1=",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setQuery("?q1="));
                parseTest.run(
                    "http://www.example.com:20?q1=v1",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setQuery("?q1=v1"));
                parseTest.run(
                    "http://www.example.com:20/?",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setPath("/"));
                parseTest.run(
                    "http://www.example.com:20/?q2",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setPath("/")
                        .setQuery("?q2"));
                parseTest.run(
                    "http://www.example.com/?q3=v3&q3.1=v3.1",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPath("/")
                        .setQuery("q3=v3&q3.1=v3.1"));
                parseTest.run(
                    "http://www.example.com:20#",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setFragment("#"));
                parseTest.run(
                    "http://www.example.com:20#fragment",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setFragment("#fragment"));
                parseTest.run(
                    "http://www.example.com:20/#",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setPath("/")
                        .setFragment("#"));
                parseTest.run(
                    "http://www.example.com:20/#fragment",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setPath("/")
                        .setFragment("#fragment"));
                parseTest.run(
                    "http://www.example.com/#fragment",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPath("/")
                        .setFragment("#fragment"));
                parseTest.run(
                    "http://www.example.com:8080/index.html?a=b&c=d#fragmentthing",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("/index.html")
                        .setQuery("?a=b&c=d")
                        .setFragment("#fragmentthing"));
            });
        });
    }
}
