package qub;

public class HttpServerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(HttpServer.class, () ->
        {
            AsyncDisposableTests.test(runner, HttpServerTests::createServer);

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
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertSuccess(true, server.dispose());
                    }
                });
            });

            runner.testGroup("addPath(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertThrows(() -> server.addPath(null, (HttpRequest request) -> null));
                        test.assertEqual(Iterable.empty(), server.getPaths());
                    }
                });

                runner.test("with empty path", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertThrows(() -> server.addPath("", (HttpRequest request) -> null));
                        test.assertEqual(Iterable.empty(), server.getPaths());
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertSuccess(true, server.addPath("/", (HttpRequest request) ->
                            new MutableHttpResponse()
                                .setStatusCode(200)
                                .setBody("Hello!")));
                        test.assertEqual(Array.fromValues(new String[] { "/" }), server.getPaths().map(Path::toString));

                        final AsyncTask serverTask = server.startAsync();
                        try
                        {
                            final HttpClient client = createClient(test);
                            final HttpResponse response = client.get("http://" + server.getLocalIPAddress() + ":" + server.getLocalPort() + "/").throwErrorOrGetValue();
                            test.assertNotNull(response);
                            test.assertEqual(200, response.getStatusCode());
                        }
                        finally
                        {
                            test.assertSuccess(true, server.dispose());
                            serverTask.await();
                        }
                    }
                });

                runner.test("with an already existing path", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        server.addPath("/", (HttpRequest request) -> null);
                        test.assertError(new PathAlreadyExistsException("/"), server.addPath("/", (HttpRequest request) -> null));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/redfish"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertSuccess(server.addPath("/redfish", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/redfish" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("onefish"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertSuccess(server.addPath("onefish", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/onefish" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/a\\nice/path"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertSuccess(server.addPath("/a\\nice/path", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/a/nice/path" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/a\\nice/"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertSuccess(server.addPath("/a\\nice/", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/a/nice" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/a\\nice//"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertSuccess(server.addPath("/a\\nice//", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/a/nice" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("////"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertSuccess(server.addPath("////", (HttpRequest request) -> null));
                        test.assertEqual(Array.fromValues(new String[] { "/" }), server.getPaths().map(Path::toString));
                    }
                });

                runner.test("with multiple paths", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        server.addPath("/onefish", (HttpRequest request) -> new MutableHttpResponse()
                                                                                .setStatusCode(200)
                                                                                .setBody("Two Fish"));
                        server.addPath("/redfish", (HttpRequest request) -> new MutableHttpResponse()
                                                                                .setStatusCode(201)
                                                                                .setBody("Blue Fish"));
                        final AsyncAction serverTask = server.startAsync();
                        try
                        {
                            final HttpClient client = createClient(test);
                            final HttpResponse response1 = client.send(HttpRequest.get("http://" + server.getLocalIPAddress() + ":" + server.getLocalPort() + "/onefish").throwErrorOrGetValue()).throwErrorOrGetValue();
                            test.assertNotNull(response1);
                            test.assertEqual(200, response1.getStatusCode());
                            test.assertEqual("OK", response1.getReasonPhrase());
                            test.assertNotNull(response1.getBody());
                            test.assertSuccess("Two Fish", response1.getBody().asCharacterReadStream().readEntireString());
                            test.assertSuccess("", response1.getBody().asCharacterReadStream().readEntireString());

                            final HttpResponse response2 = client.send(HttpRequest.get("http://" + server.getLocalIPAddress() + ":" + server.getLocalPort() + "/redfish").throwErrorOrGetValue()).throwErrorOrGetValue();
                            test.assertNotNull(response2);
                            test.assertEqual(201, response2.getStatusCode());
                            test.assertEqual("Created", response2.getReasonPhrase());
                            test.assertNotNull(response2.getBody());
                            test.assertSuccess("Blue Fish", response2.getBody().asCharacterReadStream().readEntireString());
                            test.assertSuccess("", response2.getBody().asCharacterReadStream().readEntireString());
                        }
                        finally
                        {
                            test.assertSuccess(true, server.dispose());
                            serverTask.await();
                        }
                    }
                });
            });

            runner.testGroup("setNotFound()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertThrows(() -> server.setNotFound(null));
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        server.setNotFound((HttpRequest request) ->
                        {
                            final MutableHttpResponse response = new MutableHttpResponse();
                            response.setStatusCode(456);
                            return response;
                        });

                        final AsyncTask serverTask = server.startAsync();
                        try
                        {
                            final HttpClient client = createClient(test);
                            final HttpResponse response = client.get("http://" + server.getLocalIPAddress() + ":" + server.getLocalPort() + "/notfound").throwErrorOrGetValue();
                            test.assertNotNull(response);
                            test.assertEqual(456, response.getStatusCode());
                        }
                        finally
                        {
                            test.assertSuccess(true, server.dispose());
                            serverTask.await();
                        }
                    }
                });
            });
        });
    }

    private static HttpServer createServer(Test test)
    {
        return new HttpServer(test.getNetwork().createTCPServer(IPv4Address.localhost, 18084).throwErrorOrGetValue());
    }

    private static HttpClient createClient(Test test)
    {
        return new BasicHttpClient(test.getNetwork());
    }
}