package qub;

public interface URLTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(URL.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableURL url = URL.create();
                test.assertThrows(() -> url.getScheme().await(),
                    new NotFoundException("No scheme/protocol was found."));
                test.assertThrows(() -> url.getHost().await(),
                    new NotFoundException("No host was found."));
                test.assertThrows(() -> url.getPort().await(),
                    new NotFoundException("No port was found."));
                test.assertThrows(() -> url.getPath().await(),
                    new NotFoundException("No path was found."));
                test.assertThrows(() -> url.getQueryString().await(),
                    new NotFoundException("No query string was found."));
                test.assertEqual(Map.create(), url.getQueryParameters());
                test.assertThrows(() -> url.getFragment().await(),
                    new NotFoundException("No fragment was found."));
                test.assertEqual("", url.toString());
            });

            runner.testGroup("encodePath(String,CharacterList)", () ->
            {
                final Action2<String,String> encodePathTest = (String path, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        final CharacterList output = CharacterList.create();
                        URL.encodePath(path, output);
                        test.assertEqual(expected, output.toString());
                    });
                };

                encodePathTest.run(null, "");
                encodePathTest.run("", "");
                encodePathTest.run("abc", "abc");
                encodePathTest.run("" + (char)0x00, "%00");
                encodePathTest.run("\t", "%09");
                encodePathTest.run("\r", "%0D");
                encodePathTest.run("\n", "%0A");
                encodePathTest.run("" + (char)0x10, "%10");
                encodePathTest.run("" + (char)0x1F, "%1F");
                encodePathTest.run(" ", "%20");
                encodePathTest.run("\"", "%22");
                encodePathTest.run("#", "%23");
                encodePathTest.run("<", "%3C");
                encodePathTest.run(">", "%3E");
                encodePathTest.run("?", "%3F");
                encodePathTest.run("`", "%60");
                encodePathTest.run("{", "%7B");
                encodePathTest.run("}", "%7D");
                encodePathTest.run("" + (char)0x7E, "~");
                encodePathTest.run("" + (char)0x7F, "%7F");
                encodePathTest.run("\"", "%22");
                encodePathTest.run("'", "'");
            });

            runner.testGroup("encodePath(String)", () ->
            {
                final Action2<String,String> encodePathTest = (String path, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        test.assertEqual(expected, URL.encodePath(path));
                    });
                };

                encodePathTest.run(null, "");
                encodePathTest.run("", "");
                encodePathTest.run("abc", "abc");
                encodePathTest.run("" + (char)0x00, "%00");
                encodePathTest.run("\t", "%09");
                encodePathTest.run("\r", "%0D");
                encodePathTest.run("\n", "%0A");
                encodePathTest.run("" + (char)0x10, "%10");
                encodePathTest.run("" + (char)0x1F, "%1F");
                encodePathTest.run(" ", "%20");
                encodePathTest.run("\"", "%22");
                encodePathTest.run("#", "%23");
                encodePathTest.run("<", "%3C");
                encodePathTest.run(">", "%3E");
                encodePathTest.run("?", "%3F");
                encodePathTest.run("`", "%60");
                encodePathTest.run("{", "%7B");
                encodePathTest.run("}", "%7D");
                encodePathTest.run("" + (char)0x7E, "~");
                encodePathTest.run("" + (char)0x7F, "%7F");
                encodePathTest.run("\"", "%22");
                encodePathTest.run("'", "'");
            });

            runner.testGroup("encodeQuery(String,CharacterList)", () ->
            {
                final Action2<String,String> encodeQueryTest = (String query, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(query), (Test test) ->
                    {
                        final CharacterList output = CharacterList.create();
                        URL.encodeQuery(query, output);
                        test.assertEqual(expected, output.toString());
                    });
                };

                encodeQueryTest.run(null, "");
                encodeQueryTest.run("", "");
                encodeQueryTest.run("abc", "abc");
                encodeQueryTest.run("" + (char)0x00, "%00");
                encodeQueryTest.run("\t", "%09");
                encodeQueryTest.run("\r", "%0D");
                encodeQueryTest.run("\n", "%0A");
                encodeQueryTest.run("" + (char)0x10, "%10");
                encodeQueryTest.run("" + (char)0x1F, "%1F");
                encodeQueryTest.run(" ", "%20");
                encodeQueryTest.run("\"", "%22");
                encodeQueryTest.run("#", "%23");
                encodeQueryTest.run("<", "%3C");
                encodeQueryTest.run(">", "%3E");
                encodeQueryTest.run("?", "?");
                encodeQueryTest.run("`", "`");
                encodeQueryTest.run("{", "{");
                encodeQueryTest.run("}", "}");
                encodeQueryTest.run("" + (char)0x7E, "~");
                encodeQueryTest.run("" + (char)0x7F, "%7F");
                encodeQueryTest.run("\"", "%22");
                encodeQueryTest.run("'", "'");
            });

            runner.testGroup("encodeQuery(String)", () ->
            {
                final Action2<String,String> encodeQueryTest = (String query, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(query), (Test test) ->
                    {
                        test.assertEqual(expected, URL.encodeQuery(query));
                    });
                };

                encodeQueryTest.run(null, "");
                encodeQueryTest.run("", "");
                encodeQueryTest.run("abc", "abc");
                encodeQueryTest.run("" + (char)0x00, "%00");
                encodeQueryTest.run("\t", "%09");
                encodeQueryTest.run("\r", "%0D");
                encodeQueryTest.run("\n", "%0A");
                encodeQueryTest.run("" + (char)0x10, "%10");
                encodeQueryTest.run("" + (char)0x1F, "%1F");
                encodeQueryTest.run(" ", "%20");
                encodeQueryTest.run("\"", "%22");
                encodeQueryTest.run("#", "%23");
                encodeQueryTest.run("<", "%3C");
                encodeQueryTest.run(">", "%3E");
                encodeQueryTest.run("?", "?");
                encodeQueryTest.run("`", "`");
                encodeQueryTest.run("{", "{");
                encodeQueryTest.run("}", "}");
                encodeQueryTest.run("" + (char)0x7E, "~");
                encodeQueryTest.run("" + (char)0x7F, "%7F");
                encodeQueryTest.run("\"", "%22");
                encodeQueryTest.run("'", "'");
            });

            runner.testGroup("encodeFragment(String,CharacterList)", () ->
            {
                final Action2<String,String> encodeFragmentTest = (String fragment, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(fragment), (Test test) ->
                    {
                        final CharacterList output = CharacterList.create();
                        URL.encodeFragment(fragment, output);
                        test.assertEqual(expected, output.toString());
                    });
                };

                encodeFragmentTest.run(null, "");
                encodeFragmentTest.run("", "");
                encodeFragmentTest.run("abc", "abc");
                encodeFragmentTest.run("" + (char)0x00, "%00");
                encodeFragmentTest.run("\t", "%09");
                encodeFragmentTest.run("\r", "%0D");
                encodeFragmentTest.run("\n", "%0A");
                encodeFragmentTest.run("" + (char)0x10, "%10");
                encodeFragmentTest.run("" + (char)0x1F, "%1F");
                encodeFragmentTest.run(" ", "%20");
                encodeFragmentTest.run("\"", "%22");
                encodeFragmentTest.run("#", "#");
                encodeFragmentTest.run("<", "%3C");
                encodeFragmentTest.run(">", "%3E");
                encodeFragmentTest.run("?", "?");
                encodeFragmentTest.run("`", "%60");
                encodeFragmentTest.run("{", "{");
                encodeFragmentTest.run("}", "}");
                encodeFragmentTest.run("" + (char)0x7E, "~");
                encodeFragmentTest.run("" + (char)0x7F, "%7F");
                encodeFragmentTest.run("\"", "%22");
                encodeFragmentTest.run("'", "'");
            });

            runner.testGroup("encodeFragment(String)", () ->
            {
                final Action2<String,String> encodeFragmentTest = (String fragment, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(fragment), (Test test) ->
                    {
                        test.assertEqual(expected, URL.encodeFragment(fragment));
                    });
                };

                encodeFragmentTest.run(null, "");
                encodeFragmentTest.run("", "");
                encodeFragmentTest.run("abc", "abc");
                encodeFragmentTest.run("" + (char)0x00, "%00");
                encodeFragmentTest.run("\t", "%09");
                encodeFragmentTest.run("\r", "%0D");
                encodeFragmentTest.run("\n", "%0A");
                encodeFragmentTest.run("" + (char)0x10, "%10");
                encodeFragmentTest.run("" + (char)0x1F, "%1F");
                encodeFragmentTest.run(" ", "%20");
                encodeFragmentTest.run("\"", "%22");
                encodeFragmentTest.run("#", "#");
                encodeFragmentTest.run("<", "%3C");
                encodeFragmentTest.run(">", "%3E");
                encodeFragmentTest.run("?", "?");
                encodeFragmentTest.run("`", "%60");
                encodeFragmentTest.run("{", "{");
                encodeFragmentTest.run("}", "}");
                encodeFragmentTest.run("" + (char)0x7E, "~");
                encodeFragmentTest.run("" + (char)0x7F, "%7F");
                encodeFragmentTest.run("\"", "%22");
                encodeFragmentTest.run("'", "'");
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
                        .setQueryString("a=b&c=d&e&f="),
                    "www.example.com?a=b&c=d&e&f=");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setQueryString("?a=b&c=d&e&f="),
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
                toStringTest.run(
                    URL.create()
                        .setScheme("https")
                        .setHost("www.example.com")
                        .setPath("/my path/?#")
                        .setQueryString("?hello there #<> friend")
                        .setFragment("#i`m a fragment"),
                    "https://www.example.com/my path/?#?hello there #<> friend#i`m a fragment");
            });

            runner.testGroup("toString(boolean)", () ->
            {
                final Action3<URL,Boolean,String> toStringTest = (URL url, Boolean percentEncode, String expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(url), percentEncode), (Test test) ->
                    {
                        test.assertEqual(expected, url.toString(percentEncode));
                    });
                };

                toStringTest.run(URL.create(), false, "");
                toStringTest.run(URL.create(), true, "");
                toStringTest.run(
                    URL.create().setScheme("ftp"),
                    false,
                    "ftp://");
                toStringTest.run(
                    URL.create().setScheme("ftp"),
                    true,
                    "ftp://");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com"),
                    false,
                    "ftp://www.example.com");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com"),
                    true,
                    "ftp://www.example.com");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com").setPort(8080),
                    false,
                    "ftp://www.example.com:8080");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com").setPort(8080),
                    true,
                    "ftp://www.example.com:8080");
                toStringTest.run(
                    URL.create().setHost("www.example.com").setPort(8080),
                    false,
                    "www.example.com:8080");
                toStringTest.run(
                    URL.create().setHost("www.example.com").setPort(8080),
                    true,
                    "www.example.com:8080");
                toStringTest.run(
                    URL.create()
                        .setScheme("ftp")
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("my/index.html"),
                    false,
                    "ftp://www.example.com:8080/my/index.html");
                toStringTest.run(
                    URL.create()
                        .setScheme("ftp")
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("my/index.html"),
                    true,
                    "ftp://www.example.com:8080/my/index.html");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("your/index.html"),
                    false,
                    "www.example.com:8080/your/index.html");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("your/index.html"),
                    true,
                    "www.example.com:8080/your/index.html");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPath("/"),
                    false,
                    "www.example.com/");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPath("/"),
                    true,
                    "www.example.com/");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setQueryString("?a=b&c=d&e&f="),
                    false,
                    "www.example.com?a=b&c=d&e&f=");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setQueryString("?a=b&c=d&e&f="),
                    true,
                    "www.example.com?a=b&c=d&e&f=");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("#firstHeading"),
                    false,
                    "www.example.com#firstHeading");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("#firstHeading"),
                    true,
                    "www.example.com#firstHeading");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("firstHeading"),
                    false,
                    "www.example.com#firstHeading");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("firstHeading"),
                    true,
                    "www.example.com#firstHeading");
                toStringTest.run(
                    URL.create()
                        .setScheme("https")
                        .setHost("www.example.com")
                        .setPath("/my path/?#")
                        .setQueryString("?hello there #<> friend")
                        .setFragment("#i`m a fragment"),
                    false,
                    "https://www.example.com/my path/?#?hello there #<> friend#i`m a fragment");
                toStringTest.run(
                    URL.create()
                        .setScheme("https")
                        .setHost("www.example.com")
                        .setPath("/my path/?#")
                        .setQueryString("?hello there #<> friend")
                        .setFragment("#i`m a fragment"),
                    true,
                    "https://www.example.com/my%20path/%3F%23?hello%20there%20%23%3C%3E%20friend#i%60m%20a%20fragment");
            });

            runner.testGroup("toString(URL,boolean)", () ->
            {
                final Action3<URL,Boolean,String> toStringTest = (URL url, Boolean percentEncode, String expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(url), percentEncode), (Test test) ->
                    {
                        test.assertEqual(expected, URL.toString(url, percentEncode));
                    });
                };

                toStringTest.run(URL.create(), false, "");
                toStringTest.run(URL.create(), true, "");
                toStringTest.run(
                    URL.create().setScheme("ftp"),
                    false,
                    "ftp://");
                toStringTest.run(
                    URL.create().setScheme("ftp"),
                    true,
                    "ftp://");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com"),
                    false,
                    "ftp://www.example.com");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com"),
                    true,
                    "ftp://www.example.com");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com").setPort(8080),
                    false,
                    "ftp://www.example.com:8080");
                toStringTest.run(
                    URL.create().setScheme("ftp").setHost("www.example.com").setPort(8080),
                    true,
                    "ftp://www.example.com:8080");
                toStringTest.run(
                    URL.create().setHost("www.example.com").setPort(8080),
                    false,
                    "www.example.com:8080");
                toStringTest.run(
                    URL.create().setHost("www.example.com").setPort(8080),
                    true,
                    "www.example.com:8080");
                toStringTest.run(
                    URL.create()
                        .setScheme("ftp")
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("my/index.html"),
                    false,
                    "ftp://www.example.com:8080/my/index.html");
                toStringTest.run(
                    URL.create()
                        .setScheme("ftp")
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("my/index.html"),
                    true,
                    "ftp://www.example.com:8080/my/index.html");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("your/index.html"),
                    false,
                    "www.example.com:8080/your/index.html");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPort(8080)
                        .setPath("your/index.html"),
                    true,
                    "www.example.com:8080/your/index.html");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPath("/"),
                    false,
                    "www.example.com/");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setPath("/"),
                    true,
                    "www.example.com/");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setQueryString("?a=b&c=d&e&f="),
                    false,
                    "www.example.com?a=b&c=d&e&f=");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setQueryString("?a=b&c=d&e&f="),
                    true,
                    "www.example.com?a=b&c=d&e&f=");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("#firstHeading"),
                    false,
                    "www.example.com#firstHeading");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("#firstHeading"),
                    true,
                    "www.example.com#firstHeading");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("firstHeading"),
                    false,
                    "www.example.com#firstHeading");
                toStringTest.run(
                    URL.create()
                        .setHost("www.example.com")
                        .setFragment("firstHeading"),
                    true,
                    "www.example.com#firstHeading");
                toStringTest.run(
                    URL.create()
                        .setScheme("https")
                        .setHost("www.example.com")
                        .setPath("/my path/?#")
                        .setQueryString("?hello there #<> friend")
                        .setFragment("#i`m a fragment"),
                    false,
                    "https://www.example.com/my path/?#?hello there #<> friend#i`m a fragment");
                toStringTest.run(
                    URL.create()
                        .setScheme("https")
                        .setHost("www.example.com")
                        .setPath("/my path/?#")
                        .setQueryString("?hello there #<> friend")
                        .setFragment("#i`m a fragment"),
                    true,
                    "https://www.example.com/my%20path/%3F%23?hello%20there%20%23%3C%3E%20friend#i%60m%20a%20fragment");
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
                        .setQueryString("?q1"));
                parseTest.run(
                    "http://www.example.com:20?q1=",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setQueryString("?q1="));
                parseTest.run(
                    "http://www.example.com:20?q1=v1",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPort(20)
                        .setQueryString("?q1=v1"));
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
                        .setQueryString("?q2"));
                parseTest.run(
                    "http://www.example.com/?q3=v3&q3.1=v3.1",
                    URL.create()
                        .setScheme("http")
                        .setHost("www.example.com")
                        .setPath("/")
                        .setQueryString("q3=v3&q3.1=v3.1"));
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
                        .setQueryString("?a=b&c=d")
                        .setFragment("#fragmentthing"));
            });
        });
    }
}
