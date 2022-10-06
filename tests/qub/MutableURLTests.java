package qub;

public interface MutableURLTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutableURL.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableURL url = MutableURL.create();
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

            runner.testGroup("setScheme(String)", () ->
            {
                final Action1<String> setSchemeTest = (String scheme) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(scheme), (Test test) ->
                    {
                        final MutableURL url = MutableURL.create();
                        final MutableURL setSchemeResult = url.setScheme(scheme);
                        test.assertSame(url, setSchemeResult);
                        final Result<String> getSchemeResult = url.getScheme();
                        if (Strings.isNullOrEmpty(scheme))
                        {
                            test.assertThrows(() -> getSchemeResult.await(),
                                new NotFoundException("No scheme/protocol was found."));
                        }
                        else
                        {
                            test.assertEqual(scheme, getSchemeResult.await());
                        }
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
                        if (Strings.isNullOrEmpty(host))
                        {
                            test.assertThrows(() -> url.getHost().await(),
                                new NotFoundException("No host was found."));
                        }
                        else
                        {
                            test.assertEqual(host, url.getHost().await());
                        }
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
                        final Result<Integer> getPortResult = url.getPort();
                        if (port == null)
                        {
                            test.assertThrows(() -> getPortResult.await(),
                                new NotFoundException("No port was found."));
                        }
                        else
                        {
                            test.assertEqual(port, getPortResult.await());
                        }
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

                        final Result<String> getPathResult = url.getPath();
                        if (Strings.isNullOrEmpty(path))
                        {
                            test.assertThrows(() -> getPathResult.await(),
                                new NotFoundException("No path was found."));
                        }
                        else
                        {
                            test.assertEqual(path, getPathResult.await());
                        }
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
                    test.assertThrows(() -> url.getQueryString().await(),
                        new NotFoundException("No query string was found."));
                    test.assertEqual(Map.create(), url.getQueryParameters());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("");
                    test.assertSame(url, setQueryResult);
                    test.assertThrows(() -> url.getQueryString().await(),
                        new NotFoundException("No query string was found."));
                    test.assertEqual(Map.create(), url.getQueryParameters());
                });

                runner.test("with \"?\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("?");
                    test.assertSame(url, setQueryResult);
                    test.assertThrows(() -> url.getQueryString().await(),
                        new NotFoundException("No query string was found."));
                    test.assertEqual(Map.create(), url.getQueryParameters());
                });

                runner.test("with \"a\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a", url.getQueryString().await());
                    test.assertNull(url.getQueryParameter("a").await());
                });

                runner.test("with \"a=\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=", url.getQueryString().await());
                    test.assertEqual("", url.getQueryParameter("a").await());
                });

                runner.test("with \"a=b\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b", url.getQueryString().await());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                });

                runner.test("with \"&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("&");
                    test.assertSame(url, setQueryResult);
                    test.assertThrows(() -> url.getQueryString().await(),
                        new NotFoundException("No query string was found."));
                });

                runner.test("with \"a&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a&");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a", url.getQueryString().await());
                    test.assertNull(url.getQueryParameter("a").await());
                });

                runner.test("with \"a=b&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b&");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b", url.getQueryString().await());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                });

                runner.test("with \"a=b&c\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b&c");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b&c", url.getQueryString().await());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                    test.assertNull(url.getQueryParameter("c").await());
                });

                runner.test("with \"a=b&c&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b&c&");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b&c", url.getQueryString().await());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                    test.assertNull(url.getQueryParameter("c").await());
                });

                runner.test("with \"a=b&c=&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("a=b&c=&");
                    test.assertSame(url, setQueryResult);
                    test.assertEqual("a=b&c=", url.getQueryString().await());
                    test.assertEqual("b", url.getQueryParameter("a").await());
                    test.assertEqual("", url.getQueryParameter("c").await());
                });

                runner.test("with \"=bad&\"", (Test test) ->
                {
                    final MutableURL url = MutableURL.create();
                    final MutableURL setQueryResult = url.setQueryString("=bad&");
                    test.assertSame(url, setQueryResult);
                    test.assertThrows(() -> url.getQueryString().await(),
                        new NotFoundException("No query string was found."));
                    test.assertEqual(Map.create(), url.getQueryParameters());
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
                        test.assertThrows(() -> url.getQueryString().await(),
                            new NotFoundException("No query string was found."));
                        test.assertEqual(Map.create(), url.getQueryParameters());
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

                        final Result<String> getFragmentResult = url.getFragment();
                        if (Strings.isNullOrEmpty(fragment))
                        {
                            test.assertThrows(() -> getFragmentResult.await(),
                                new NotFoundException("No fragment was found."));
                        }
                        else
                        {
                            test.assertEqual(fragment, getFragmentResult.await());
                        }
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
