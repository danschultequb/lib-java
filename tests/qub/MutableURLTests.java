package qub;

public interface MutableURLTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(MutableURL.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableURL url = MutableURL.create();
                test.assertNull(url.getScheme());
                test.assertNull(url.getHost());
                test.assertNull(url.getPort());
                test.assertNull(url.getPath());
                test.assertNull(url.getQueryString());
                test.assertNull(url.getFragment());
                test.assertEqual("", url.toString());
            });

            runner.testGroup("setScheme(String)", () ->
            {
                final Action1<String> setSchemeTest = (String scheme) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(scheme), (Test test) ->
                    {
                        final MutableURL url = MutableURL.create();
                        final MutableURL setSchemeResult = url.setScheme(scheme);
                        test.assertSame(url, setSchemeResult);
                        test.assertEqual(Strings.isNullOrEmpty(scheme) ? null : scheme, url.getScheme());
                    });
                };

                setSchemeTest.run(null);
                setSchemeTest.run("");
                setSchemeTest.run("https");
            });

            runner.testGroup("setHost(String)", () ->
            {
                final Action1<String> setHostTests = (String host) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(host), (Test test) ->
                    {
                        final MutableURL url = MutableURL.create();
                        final MutableURL setHostResult = url.setHost(host);
                        test.assertSame(url, setHostResult);
                        test.assertEqual(Strings.isNullOrEmpty(host) ? null : host, url.getHost());
                    });
                };

                setHostTests.run(null);
                setHostTests.run("");
                setHostTests.run("www.example.com");
            });

            runner.testGroup("setPort(Integer)", () ->
            {
                final Action1<Integer> setPortTests = (Integer port) ->
                {
                    runner.test("with " + port, (Test test) ->
                    {
                        final MutableURL url = MutableURL.create();
                        final MutableURL setPortResult = url.setPort(port);
                        test.assertSame(url, setPortResult);
                        test.assertEqual(port, url.getPort());
                    });
                };

                setPortTests.run(null);
                setPortTests.run(-20);
                setPortTests.run(0);
                setPortTests.run(1);
                setPortTests.run(80);
                setPortTests.run(65535);
                setPortTests.run(65536);
            });

            runner.testGroup("setPath(String)", () ->
            {
                final Action1<String> setPathTests = (String path) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        final MutableURL url = MutableURL.create();
                        final MutableURL setPathResult = url.setPath(path);
                        test.assertSame(url, setPathResult);
                        test.assertEqual(Strings.isNullOrEmpty(path) ? null : path, url.getPath());
                    });
                };

                setPathTests.run(null);
                setPathTests.run("");
                setPathTests.run("/");
                setPathTests.run("/index.html");
                setPathTests.run("index.html");
                setPathTests.run("/my/index.html");
                setPathTests.run("my/index.html");
            });

            runner.testGroup("setQuery(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString(null);
                    test.assertSame(url, setQueryResult);
                    test.assertNull(url.getQueryString());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("");
                    test.assertSame(url, setQueryResult);
                    test.assertNull(url.getQueryString());
                });

                runner.test("with \"?\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("?");
                    test.assertSame(url, setQueryResult);
                    test.assertNull(url.getQueryString());
                });

                runner.test("with \"a\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a", url.getQueryString());
                    test.assertNull(url.getQueryParameter("a").await());
                });

                runner.test("with \"a=\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=", url.getQueryString());
                    test.assertEqual("", url.getQueryParameter("a").await());
                });

                runner.test("with \"a=b\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b", url.getQueryString());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                });

                runner.test("with \"&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("&");
                    test.assertSame(url, setQueryResult);
                    test.assertNull(url.getQueryString());
                });

                runner.test("with \"a&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a&");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a", url.getQueryString());
                    test.assertNull(url.getQueryParameter("a").await());
                });

                runner.test("with \"a=b&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b&");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b", url.getQueryString());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                });

                runner.test("with \"a=b&c\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b&c");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b&c", url.getQueryString());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                    test.assertNull(url.getQueryParameter("c").await());
                });

                runner.test("with \"a=b&c&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b&c&");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b&c", url.getQueryString());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                    test.assertNull(url.getQueryParameter("c").await());
                });

                runner.test("with \"a=b&c=&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b&c=&");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b&c=", url.getQueryString());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                    test.assertEqual("", url.getQueryParameter("c").await());
                });

                runner.test("with \"=bad&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("=bad&");
                    test.assertSame(url, setQueryResult);
                    test.assertNull(url.getQueryString());
                });
            });

            runner.testGroup("setQueryParameter(String,String)", () ->
            {
                final Action3<String,String,Throwable> setQueryParameterErrorTest = (String queryParameterName, String queryParameterValue, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(queryParameterName, queryParameterValue).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final MutableURL url = MutableURL.create();
                        test.assertThrows(() -> url.setQueryParameter(queryParameterName, queryParameterValue),
                            expected);
                        test.assertNull(url.getQueryString());
                    });
                };

                setQueryParameterErrorTest.run(null, null, new PreConditionFailure("queryParameterName cannot be null."));
                setQueryParameterErrorTest.run(null, "", new PreConditionFailure("queryParameterName cannot be null."));
                setQueryParameterErrorTest.run(null, "b", new PreConditionFailure("queryParameterName cannot be null."));
                setQueryParameterErrorTest.run("", null, new PreConditionFailure("queryParameterName cannot be empty."));
                setQueryParameterErrorTest.run("", "", new PreConditionFailure("queryParameterName cannot be empty."));
                setQueryParameterErrorTest.run("", "b", new PreConditionFailure("queryParameterName cannot be empty."));

                final Action2<String,String> setQueryParameterTest = (String queryParameterName, String queryParameterValue) ->
                {
                    runner.test("with " + English.andList(Iterable.create(queryParameterName, queryParameterValue).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final MutableURL url = MutableURL.create();
                        final MutableURL setQueryParameterResult = url.setQueryParameter(queryParameterName, queryParameterValue);
                        test.assertSame(url, setQueryParameterResult);
                        test.assertEqual(queryParameterValue, url.getQueryParameter(queryParameterName).await());
                    });
                };

                setQueryParameterTest.run("a", null);
                setQueryParameterTest.run("a", "");
                setQueryParameterTest.run("a", "b");
            });

            runner.testGroup("setFragment(String)", () ->
            {
                final Action1<String> setFragmentTests = (String fragment) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(fragment), (Test test) ->
                    {
                        final MutableURL url = MutableURL.create();
                        final MutableURL setFragmentResult = url.setFragment(fragment);
                        test.assertSame(url, setFragmentResult);
                        test.assertEqual(Strings.isNullOrEmpty(fragment) ? null : fragment, url.getFragment());
                    });
                };

                setFragmentTests.run(null);
                setFragmentTests.run("");
                setFragmentTests.run("#");
                setFragmentTests.run("part1");
                setFragmentTests.run("#part1");
            });
        });
    }
}
