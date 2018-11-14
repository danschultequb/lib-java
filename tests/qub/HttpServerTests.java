package qub;

public class HttpServerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(HttpServer.class, () ->
        {
            AsyncDisposableTests.test(runner, HttpServerTests::create);

            runner.testGroup("constructor(TCPServer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new HttpServer(null));
                });

                runner.test("with disposed TCPServer", (Test test) ->
                {
                    try (final TCPServer tcpServer = test.getNetwork().createTCPServer(23211).throwErrorOrGetValue())
                    {
                        test.assertSuccess(true, tcpServer.dispose());
                        test.assertThrows(() -> new HttpServer(tcpServer));
                    }
                });

                runner.test("with non-disposed TCPServer", (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertSuccess(true, server.dispose());
                    }
                });
            });

            runner.testGroup("addPath(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertThrows(() -> server.addPath(null, (HttpRequest request) -> null));
                        test.assertEqual(Iterable.empty(), server.getPaths());
                    }
                });

                runner.test("with empty path", (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertThrows(() -> server.addPath("", (HttpRequest request) -> null));
                        test.assertEqual(Iterable.empty(), server.getPaths());
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/"), (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertSuccess(true, server.addPath("/", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with an already existing path", (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        server.addPath("/", (HttpRequest request) -> null);
                        test.assertError(new PathAlreadyExistsException("/"), server.addPath("/", (HttpRequest request) -> null));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/redfish"), (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertSuccess(server.addPath("/redfish", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/redfish" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("onefish"), (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertSuccess(server.addPath("onefish", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/onefish" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/a\\nice/path"), (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertSuccess(server.addPath("/a\\nice/path", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/a/nice/path" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/a\\nice/"), (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertSuccess(server.addPath("/a\\nice/", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/a/nice" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/a\\nice//"), (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertSuccess(server.addPath("/a\\nice//", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/a/nice" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("////"), (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertSuccess(server.addPath("////", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/" }), server.getPaths().map(Path::toString));
                    }
                });
            });

            runner.testGroup("setDefaultPath()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final HttpServer server = create(test))
                    {
                        test.assertThrows(() -> server.setDefaultPath(null));
                    }
                });
            });
        });
    }

    private static HttpServer create(Test test)
    {
        return new HttpServer(test.getNetwork().createTCPServer(18083).throwErrorOrGetValue());
    }
}
