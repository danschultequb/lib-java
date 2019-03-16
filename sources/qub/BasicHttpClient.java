package qub;

public class BasicHttpClient implements HttpClient
{
    private final Network network;

    BasicHttpClient(Network network)
    {
        this.network = network;
    }

    @Override
    public Result<HttpResponse> send(HttpRequest request)
    {
        PreCondition.assertNotNull(request, "request");
        PreCondition.assertNotNull(request.getURL(), "request.getURL()");
        PreCondition.assertNotNullAndNotEmpty(request.getURL().getHost(), "request.getURL().getHost()");

        return Result.create(() ->
        {
            final URL requestUrl = request.getURL();
            final String requestHost = requestUrl.getHost();
            final IPv4Address requestIPAddress = network.getDNS().resolveHost(requestHost).awaitError();

            Integer requestPort = requestUrl.getPort();
            if (requestPort == null)
            {
                requestPort = 80;
            }

            final MutableHttpResponse result = new MutableHttpResponse();

            try (final TCPClient tcpClient = network.createTCPClient(requestIPAddress, requestPort).awaitError())
            {
                final InMemoryByteStream requestBeforeBodyByteStream = new InMemoryByteStream();
                final CharacterWriteStream requestBeforeBodyLineStream = requestBeforeBodyByteStream.asCharacterWriteStream(CharacterEncoding.UTF_8, "\r\n");
                String httpVersion = request.getHttpVersion();
                if (Strings.isNullOrEmpty(httpVersion))
                {
                    httpVersion = "HTTP/1.1";
                }
                requestBeforeBodyLineStream.writeLine("%s %s %s", request.getMethod(), request.getURL(), httpVersion);
                for (final HttpHeader header : request.getHeaders())
                {
                    requestBeforeBodyLineStream.writeLine("%s:%s", header.getName(), header.getValue());
                }
                requestBeforeBodyLineStream.writeLine();
                requestBeforeBodyByteStream.endOfStream();

                tcpClient.writeAllBytes(requestBeforeBodyByteStream).awaitError();
                final ByteReadStream requestBodyStream = request.getBody();
                if (requestBodyStream != null)
                {
                    tcpClient.writeAllBytes(requestBodyStream).awaitError();
                }

                final BufferedByteReadStream bufferedByteReadStream = new BufferedByteReadStream(tcpClient);
                final LineReadStream responseLineReadStream = bufferedByteReadStream.asLineReadStream(false);
                String statusLine = responseLineReadStream.readLine().awaitError();
                final int httpVersionLength = statusLine.indexOf(' ');

                result.setHTTPVersion(statusLine.substring(0, httpVersionLength));
                statusLine = statusLine.substring(httpVersionLength + 1);

                final int statusCodeStringLength = statusLine.indexOf(' ');
                final String statusCodeString = statusLine.substring(0, statusCodeStringLength);
                result.setStatusCode(Integer.parseInt(statusCodeString));
                result.setReasonPhrase(statusLine.substring(statusCodeStringLength + 1));

                String headerLine = responseLineReadStream.readLine().awaitError();
                while (!headerLine.isEmpty())
                {
                    final int colonIndex = headerLine.indexOf(':');
                    final String headerName = headerLine.substring(0, colonIndex);
                    final String headerValue = headerLine.substring(colonIndex + 1).trim();
                    result.setHeader(headerName, headerValue);

                    headerLine = responseLineReadStream.readLine().awaitError();
                }

                result.getContentLength()
                    .catchError(NotFoundException.class, () -> 0L)
                    .then((Long contentLength) ->
                    {
                        if (0 < contentLength)
                        {
                            final InMemoryByteStream responseBodyStream = new InMemoryByteStream(network.getAsyncRunner());

                            long bytesToRead = contentLength;
                            while (0 < bytesToRead)
                            {
                                final byte[] bytesRead = bufferedByteReadStream.readBytes((int)Math.minimum(bytesToRead, Integers.maximum)).awaitError();
                                if (bytesRead == null)
                                {
                                    bytesToRead = 0;
                                }
                                else
                                {
                                    responseBodyStream.writeAllBytes(bytesRead).awaitError();
                                    bytesToRead -= bytesRead.length;
                                }
                            }
                            responseBodyStream.endOfStream();
                            result.setBody(responseBodyStream);
                        }
                    })
                    .awaitError();
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }
}
