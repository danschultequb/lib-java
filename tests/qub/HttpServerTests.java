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
                    test.assertThrows(() -> new HttpServer(null), new PreConditionFailure("tcpServer cannot be null."));
                });

                runner.test("with disposed TCPServer", (Test test) ->
                {
                    try (final TCPServer tcpServer = test.getNetwork().createTCPServer(23211).await())
                    {
                        test.assertTrue(tcpServer.dispose().await());
                        test.assertThrows(() -> new HttpServer(tcpServer), new PreConditionFailure("tcpServer.isDisposed() cannot be true."));
                    }
                });

                runner.test("with non-disposed TCPServer", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertTrue(server.dispose().await());
                    }
                });
            });

            runner.testGroup("addPath(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertThrows(() -> server.addPath(null, (HttpRequest request) -> null), new PreConditionFailure("pathString cannot be null."));
                        test.assertEqual(Iterable.create(), server.getPaths());
                    }
                });

                runner.test("with empty path", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertThrows(() -> server.addPath("", (HttpRequest request) -> null), new PreConditionFailure("pathString cannot be empty."));
                        test.assertEqual(Iterable.create(), server.getPaths());
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertTrue(server.addPath("/", (HttpRequest request) ->
                            new MutableHttpResponse()
                                .setStatusCode(200)
                                .setBody("Hello!")).await());
                        test.assertEqual(Array.create("/"), server.getPaths().map(PathPattern::toString));

                        final AsyncTask serverTask = server.startAsync();
                        try
                        {
                            final HttpClient client = createClient(test);
                            final HttpResponse response = client.get("http://" + server.getLocalIPAddress() + ":" + server.getLocalPort() + "/").await();
                            test.assertNotNull(response);
                            test.assertEqual(200, response.getStatusCode());
                        }
                        finally
                        {
                            test.assertTrue(server.dispose().await());
                            serverTask.await();
                        }
                    }
                });

                runner.test("with an already existing path", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertTrue(server.addPath("/", (HttpRequest request) -> null).await());
                        test.assertThrows(() -> server.addPath("/", (HttpRequest request) -> null).await(),
                            new AlreadyExistsException("The path \"/\" already exists."));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/redfish"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertTrue(server.addPath("/redfish", (HttpRequest request) -> null).await());
                        test.assertEqual(Array.create("/redfish"), server.getPaths().map(PathPattern::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("onefish"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        server.addPath("onefish", (HttpRequest request) -> null).await();
                        test.assertEqual(Array.create("/onefish"), server.getPaths().map(PathPattern::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/a\\nice/path"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        server.addPath("/a\\nice/path", (HttpRequest request) -> null).await();
                        test.assertEqual(Array.create("/a/nice/path"), server.getPaths().map(PathPattern::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/a\\nice/"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        server.addPath("/a\\nice/", (HttpRequest request) -> null).await();
                        test.assertEqual(Array.create("/a/nice"), server.getPaths().map(PathPattern::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/a\\nice//"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        server.addPath("/a\\nice//", (HttpRequest request) -> null).await();
                        test.assertEqual(Array.create("/a/nice"), server.getPaths().map(PathPattern::toString));
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("////"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        server.addPath("////", (HttpRequest request) -> null).await();
                        test.assertEqual(Array.create("/"), server.getPaths().map(PathPattern::toString));
                    }
                });

                runner.test("with multiple paths", (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        server.addPath("/onefish", (HttpRequest request) -> new MutableHttpResponse()
                                                                                .setStatusCode(200)
                                                                                .setBody("Two Fish")).await();
                        server.addPath("/redfish", (HttpRequest request) -> new MutableHttpResponse()
                                                                                .setStatusCode(201)
                                                                                .setBody("Blue Fish")).await();
                        final AsyncAction serverTask = server.startAsync();
                        try
                        {
                            final HttpClient client = createClient(test);
                            final HttpResponse response1 = client.send(HttpRequest.get("http://" + server.getLocalIPAddress() + ":" + server.getLocalPort() + "/onefish").await()).await();
                            test.assertNotNull(response1);
                            test.assertEqual(200, response1.getStatusCode());
                            test.assertEqual("OK", response1.getReasonPhrase());
                            test.assertNotNull(response1.getBody());
                            test.assertEqual("Two Fish", response1.getBody().asCharacterReadStream().readEntireString().await());
                            test.assertEqual("", response1.getBody().asCharacterReadStream().readEntireString().await());

                            final HttpResponse response2 = client.send(HttpRequest.get("http://" + server.getLocalIPAddress() + ":" + server.getLocalPort() + "/redfish").await()).await();
                            test.assertNotNull(response2);
                            test.assertEqual(201, response2.getStatusCode());
                            test.assertEqual("Created", response2.getReasonPhrase());
                            test.assertNotNull(response2.getBody());
                            test.assertEqual("Blue Fish", response2.getBody().asCharacterReadStream().readEntireString().await());
                            test.assertEqual("", response2.getBody().asCharacterReadStream().readEntireString().await());
                        }
                        finally
                        {
                            test.assertTrue(server.dispose().await());
                            serverTask.await();
                        }
                    }
                });

                runner.test("with " + Strings.escapeAndQuote("/things/*"), (Test test) ->
                {
                    try (final HttpServer server = createServer(test))
                    {
                        test.assertTrue(server.addPath("/things/*", (Indexable<String> trackedValues, HttpRequest request) ->
                             new MutableHttpResponse()
                                 .setStatusCode(200)
                                 .setBody("Hello, " + trackedValues.first() + "!")).await());
                        test.assertEqual(Array.create("/things/*"), server.getPaths().map(PathPattern::toString));

                        final AsyncTask serverTask = server.startAsync();
                        try
                        {
                            final HttpClient client = createClient(test);
                            final HttpResponse response = client.get("http://" + server.getLocalIPAddress() + ":" + server.getLocalPort() + "/things/catsanddogs").await();
                            test.assertNotNull(response);
                            test.assertEqual(200, response.getStatusCode());
                            test.assertEqual("Hello, catsanddogs!", response.getBody().asCharacterReadStream().readEntireString().await());
                        }
                        finally
                        {
                            test.assertTrue(server.dispose().await());
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
                        test.assertThrows(() -> server.setNotFound(null), new PreConditionFailure("notFoundAction cannot be null."));
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
                            final HttpResponse response = client.get("http://" + server.getLocalIPAddress() + ":" + server.getLocalPort() + "/notfound").await();
                            test.assertNotNull(response);
                            test.assertEqual(456, response.getStatusCode());
                        }
                        finally
                        {
                            test.assertTrue(server.dispose().await());
                            serverTask.await();
                        }
                    }
                });
            });
        });
    }

    private static HttpServer createServer(Test test)
    {
        return new HttpServer(test.getNetwork().createTCPServer(IPv4Address.localhost, 18084).await());
    }

    private static HttpClient createClient(Test test)
    {
        return new BasicHttpClient(test.getNetwork());
    }
}
