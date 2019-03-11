package qub;

/**
 * A type that can receive incoming HTTP requests.
 */
public class HttpServer implements AsyncDisposable
{
    private final TCPServer tcpServer;
    private boolean disposed;
    private final MutableMap<PathPattern,Function2<Indexable<String>,HttpRequest,HttpResponse>> paths;
    private Function1<HttpRequest,HttpResponse> notFoundAction;

    /**
     * Create a new HTTP server based on the provided TCPServer.
     * @param tcpServer The TCPServer that will accept incoming HTTP requests.
     */
    public HttpServer(TCPServer tcpServer)
    {
        PreCondition.assertNotNull(tcpServer, "tcpServer");
        PreCondition.assertFalse(tcpServer.isDisposed(), "tcpServer.isDisposed()");

        this.tcpServer = tcpServer;
        this.paths = Map.create();
        notFoundAction = (HttpRequest request) ->
        {
            final MutableHttpResponse response = new MutableHttpResponse();
            response.setHTTPVersion(request.getHttpVersion());
            response.setStatusCode(404);
            response.setReasonPhrase("Not Found");
            response.setBody("<html><body>404: Not Found</body></html>");
            return response;
        };
    }

    /**
     * Get the local IP address that this HttpServer is bound to.
     * @return The local IP address that this HttpServer is bound to.
     */
    public IPv4Address getLocalIPAddress()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return tcpServer.getLocalIPAddress();
    }

    /**
     * Get the local port that this HttpServer is bound to.
     * @return The local port that this HttpServer is bound to.
     */
    public int getLocalPort()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return tcpServer.getLocalPort();
    }

    /**
     * Add a new pathString that this HTTP server will respond to.
     * @param pathString The pathString that this HTTP server will respond to.
     * @return Whether or not the pathString was successfully added.
     */
    public Result<Boolean> addPath(String pathString, Function1<HttpRequest,HttpResponse> pathAction)
    {
        PreCondition.assertNotNullAndNotEmpty(pathString, "pathString");
        PreCondition.assertNotNull(pathAction, "pathAction");

        return addPath(pathString, (Indexable<String> pathMatches, HttpRequest request) -> pathAction.run(request));
    }

    /**
     * Add a new pathString that this HTTP server will respond to.
     * @param pathString The pathString that this HTTP server will respond to.
     * @return Whether or not the pathString was successfully added.
     */
    public Result<Boolean> addPath(String pathString, Function2<Indexable<String>,HttpRequest,HttpResponse> pathAction)
    {
        PreCondition.assertNotNullAndNotEmpty(pathString, "pathString");
        PreCondition.assertNotNull(pathAction, "pathAction");

        if (pathString.contains("\\"))
        {
            pathString = pathString.replaceAll("\\\\", "/");
        }
        if (!pathString.startsWith("/"))
        {
            pathString = '/' + pathString;
        }

        int endIndex = pathString.length();
        while (1 < endIndex && pathString.charAt(endIndex - 1) == '/')
        {
            --endIndex;
        }
        pathString = pathString.substring(0, endIndex);

        final PathPattern pathPattern = PathPattern.parse(pathString);

        Result<Boolean> result;
        if (paths.containsKey(pathPattern))
        {
            result = Result.error(new AlreadyExistsException("The path " + Strings.escapeAndQuote(pathPattern) + " already exists."));
        }
        else
        {
            paths.set(pathPattern, pathAction);
            result = Result.successTrue();
        }

        return result;
    }

    /**
     * Set the action that will be invoked when this HttpServer receives a request for a path that
     * isn't recognized.
     * @param notFoundAction The action that will be invoked when this HttpServer receives a request
     *                       for a path that isn't recognized.
     */
    public void setNotFound(Function1<HttpRequest,HttpResponse> notFoundAction)
    {
        PreCondition.assertNotNull(notFoundAction, "notFoundAction");

        this.notFoundAction = notFoundAction;
    }

    /**
     * Start listening on the current thread for incoming requests. This method will block until the
     * HttpServer is disposed.
     */
    public Result<Void> start()
    {
        return Result.create(() ->
        {
            while(!isDisposed())
            {
                try (final TCPClient acceptedClient = tcpServer.accept().awaitError())
                {
                    final MutableHttpRequest request = new MutableHttpRequest();
                    final LineReadStream acceptedClientLineReadStream = acceptedClient.asLineReadStream();

                    final String firstLine = acceptedClientLineReadStream.readLine().awaitError();
                    final String[] firstLineParts = firstLine.split(" ");
                    request.setMethod(HttpMethod.valueOf(firstLineParts[0]));
                    request.setUrl(URL.parse(firstLineParts[1]).awaitError());
                    request.setHttpVersion(firstLineParts[2]);

                    String headerLine = acceptedClientLineReadStream.readLine().awaitError();
                    while (!Strings.isNullOrEmpty(headerLine))
                    {
                        final int firstColonIndex = headerLine.indexOf(':');
                        final String headerName = headerLine.substring(0, firstColonIndex);
                        final String headerValue = headerLine.substring(firstColonIndex + 1);
                        request.setHeader(headerName, headerValue);

                        headerLine = acceptedClientLineReadStream.readLine().awaitError();
                    }

                    request.getContentLength()
                        .then((Long contentLength) -> request.setBody(contentLength, acceptedClient))
                        .catchError()
                        .awaitError();

                    HttpResponse response;
                    final String pathString = request.getURL().getPath();
                    final Path path = Path.parse(Strings.isNullOrEmpty(pathString) ? "/" : pathString);
                    Indexable<String> pathTrackedValues = null;
                    Function2<Indexable<String>,HttpRequest,HttpResponse> pathAction = null;
                    for (final MapEntry<PathPattern,Function2<Indexable<String>,HttpRequest,HttpResponse>> entry : paths)
                    {
                        final PathPattern pathPattern = entry.getKey();
                        final Iterable<Match> pathMatches = pathPattern.getMatches(path);
                        if (pathMatches.any())
                        {
                            final Match firstMatch = pathMatches.first();
                            final Iterable<Iterable<Character>> trackedCharacters = firstMatch.getTrackedValues();
                            final Iterable<String> trackedStrings = trackedCharacters.map(Strings::join);
                            pathTrackedValues = List.create(trackedStrings);
                            pathAction = entry.getValue();
                            break;
                        }
                    }

                    if (pathAction == null)
                    {
                        response = notFoundAction.run(request);
                    }
                    else
                    {
                        response = pathAction.run(pathTrackedValues, request);
                    }

                    if (response == null)
                    {
                        final MutableHttpResponse mutableResponse = new MutableHttpResponse();
                        mutableResponse.setHTTPVersion(request.getHttpVersion());
                        mutableResponse.setStatusCode(500);
                        mutableResponse.setBody("<html><body>" + mutableResponse.getStatusCode() + ": " + getReasonPhrase(mutableResponse.getStatusCode()) + "</body></html>");
                        response = mutableResponse;
                    }

                    String httpVersion = response.getHTTPVersion();
                    if (Strings.isNullOrEmpty(httpVersion))
                    {
                        httpVersion = "HTTP/1.1";
                    }

                    String reasonPhrase = response.getReasonPhrase();
                    if (Strings.isNullOrEmpty(reasonPhrase))
                    {
                        reasonPhrase = getReasonPhrase(response.getStatusCode());
                    }

                    final LineWriteStream clientLineWriteStream = acceptedClient.asLineWriteStream("\r\n");
                    clientLineWriteStream.writeLine("%s %s %s", httpVersion, response.getStatusCode(), reasonPhrase);
                    for (final HttpHeader header : response.getHeaders())
                    {
                        clientLineWriteStream.writeLine("%s:%s", header.getName(), header.getValue());
                    }
                    clientLineWriteStream.writeLine();

                    try (final ByteReadStream responseBody = response.getBody())
                    {
                        acceptedClient.writeAllBytes(responseBody).awaitError();
                    }
                }
            }
        });
    }

    /**
     * Start listening on a separate thread for incoming requests.
     */
    public AsyncAction startAsync()
    {
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");
        PreCondition.assertFalse(getAsyncRunner().isDisposed(), "getAsyncRunner().isDisposed()");

        return getAsyncRunner().scheduleSingle(this::start);
    }

    /**
     * Get all of the paths that have been registered with this server.
     * @return All of the paths that have been registered with this server.
     */
    public Iterable<PathPattern> getPaths()
    {
        return paths.getKeys();
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return tcpServer.getAsyncRunner();
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (disposed)
        {
            result = Result.successFalse();
        }
        else
        {
            disposed = true;

            result = tcpServer.dispose();
        }
        return result;
    }

    /**
     * Get the default reason phrase for the provided status code. These default phrases come create
     * https://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html.
     * @param statusCode The status code to get the default reason phrase for.
     * @return The default reason phrase for the provided status code.
     */
    public static String getReasonPhrase(int statusCode)
    {
        String result = null;

        switch (statusCode)
        {
            case 100:
                result = "Continue";
                break;

            case 101:
                result = "Switching Protocols";
                break;

            case 200:
                result = "OK";
                break;

            case 201:
                result = "Created";
                break;

            case 202:
                result = "Accepted";
                break;

            case 400:
                result = "Bad Request";
                break;

            case 404:
                result = "Not Found";
                break;

            case 500:
                result = "Internal Server Error";
                break;
        }

        return result;
    }
}
