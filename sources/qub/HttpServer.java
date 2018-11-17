package qub;

/**
 * A type that can receive incoming HTTP requests.
 */
public class HttpServer implements AsyncDisposable
{
    private final TCPServer tcpServer;
    private boolean disposed;
    private final Map<Path,Function1<HttpRequest,HttpResponse>> paths;
    private Function1<HttpRequest,HttpResponse> defaultPathAction;

    /**
     * Create a new HTTP server based on the provided TCPServer.
     * @param tcpServer The TCPServer that will accept incoming HTTP requests.
     */
    public HttpServer(TCPServer tcpServer)
    {
        PreCondition.assertNotNull(tcpServer, "tcpServer");
        PreCondition.assertFalse(tcpServer.isDisposed(), "tcpServer.isDisposed()");

        this.tcpServer = tcpServer;
        this.paths = new ListMap<>();
        defaultPathAction = (HttpRequest request) ->
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

        final Path path = Path.parse(pathString);

        Result<Boolean> result;
        if (paths.containsKey(path))
        {
            result = Result.error(new PathAlreadyExistsException(path));
        }
        else
        {
            paths.set(path, pathAction);
            result = Result.successTrue();
        }

        return result;
    }

    /**
     * Set the action that will be invoked when this HttpServer receives a request for a path that
     * isn't recognized.
     * @param defaultPathAction The action that will be invoked when this HttpServer receives a
     *                          request for a path that isn't recognized.
     */
    public void setDefaultPath(Function1<HttpRequest,HttpResponse> defaultPathAction)
    {
        PreCondition.assertNotNull(defaultPathAction, "defaultPathAction");

        this.defaultPathAction = defaultPathAction;
    }

    /**
     * Start listening on the current thread for incoming requests. This method will block until the
     * HttpServer is disposed.
     */
    public Result<Boolean> start()
    {
        Result<Boolean> result = null;
        while (result == null && !isDisposed())
        {
            final Result<TCPClient> acceptedClientResult = tcpServer.accept();
            result = acceptedClientResult.convertError();
            if (result == null)
            {
                try (final TCPClient acceptedClient = acceptedClientResult.getValue())
                {
                    final MutableHttpRequest request = new MutableHttpRequest();
                    final LineReadStream acceptedClientLineReadStream = acceptedClient.asLineReadStream();

                    final String firstLine = acceptedClientLineReadStream.readLine().throwErrorOrGetValue();
                    final String[] firstLineParts = firstLine.split(" ");
                    request.setMethod(HttpMethod.valueOf(firstLineParts[0]));
                    request.setUrl(URL.parse(firstLineParts[1]).throwErrorOrGetValue());
                    request.setHttpVersion(firstLineParts[2]);

                    String headerLine = acceptedClientLineReadStream.readLine().throwErrorOrGetValue();
                    while (!Strings.isNullOrEmpty(headerLine))
                    {
                        final int firstColonIndex = headerLine.indexOf(':');
                        final String headerName = headerLine.substring(0, firstColonIndex);
                        final String headerValue = headerLine.substring(firstColonIndex + 1);
                        request.setHeader(headerName, headerValue);

                        headerLine = acceptedClientLineReadStream.readLine().throwErrorOrGetValue();
                    }

                    final Result<Long> contentLengthResult = request.getContentLength();
                    if (!contentLengthResult.hasError())
                    {
                        request.setBody(contentLengthResult.getValue(), acceptedClient);
                    }

                    HttpResponse response;
                    final String pathString = request.getURL().getPath();
                    final Path path = Path.parse(Strings.isNullOrEmpty(pathString) ? "/" : pathString);
                    final Result<Function1<HttpRequest,HttpResponse>> pathAction = paths.get(path);
                    if (pathAction.hasError())
                    {
                        response = defaultPathAction.run(request);
                    }
                    else
                    {
                        response = pathAction.getValue().run(request);
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

                    final ByteReadStream responseBody = response.getBody();
                    if (responseBody != null)
                    {
                        acceptedClient.writeAll(responseBody).throwError();
                        responseBody.dispose().throwError();
                    }
                }
                catch (Throwable error)
                {
                    result = Result.error(error);
                }
            }
        }
        return Result.successTrue();
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
    public Iterable<Path> getPaths()
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

    public static String getReasonPhrase(int statusCode)
    {
        String result = null;

        switch (statusCode)
        {
            case 200:
                result = "OK";
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
