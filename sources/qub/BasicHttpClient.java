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

        Result<HttpResponse> result = null;
        final URL requestUrl = request.getURL();
        final String requestHost = requestUrl.getHost();
        final Result<IPv4Address> requestIPAddressResult = network.getDNS().resolveHost(requestHost);
        if (requestIPAddressResult.hasError())
        {
            result = Result.error(requestIPAddressResult.getError());
        }
        else
        {
            final IPv4Address requestIPAddress = requestIPAddressResult.getValue();
            final Result<TCPClient> tcpClientResult = network.createTCPClient(requestIPAddress, 80);
            if (tcpClientResult.hasError())
            {
                result = Result.error(tcpClientResult.getError());
            }
            else
            {
                try (final TCPClient tcpClient = tcpClientResult.getValue())
                {
                    final InMemoryByteStream requestBeforeBodyByteStream = new InMemoryByteStream();
                    final LineWriteStream requestBeforeBodyLineStream = requestBeforeBodyByteStream.asLineWriteStream(CharacterEncoding.UTF_8, "\r\n");
                    requestBeforeBodyLineStream.writeLine("%s %s HTTP/1.1", request.getMethod(), request.getURL());
                    for (final HttpHeader header : request.getHeaders())
                    {
                        requestBeforeBodyLineStream.writeLine("%s:%s", header.getName(), header.getValue());
                    }
                    requestBeforeBodyLineStream.writeLine();
                    requestBeforeBodyByteStream.endOfStream();

                    Result<Boolean> writeResult = tcpClient.writeAll(requestBeforeBodyByteStream);
                    if (!writeResult.hasError())
                    {
                        final ByteReadStream requestBodyStream = request.getBody();
                        if (requestBodyStream != null)
                        {
                            writeResult = tcpClient.writeAll(requestBodyStream);
                        }
                    }

                    if (writeResult.hasError())
                    {
                        result = Result.error(writeResult.getError());
                    }
                    else
                    {
                        final MutableHttpResponse response = new MutableHttpResponse();

                        final BufferedByteReadStream bufferedByteReadStream = new BufferedByteReadStream(tcpClient);
                        final LineReadStream responseLineReadStream = bufferedByteReadStream.asLineReadStream(false);
                        String statusLine = responseLineReadStream.readLine().getValue();
                        final int httpVersionLength = statusLine.indexOf(' ');

                        response.setHTTPVersion(statusLine.substring(0, httpVersionLength));
                        statusLine = statusLine.substring(httpVersionLength + 1);

                        final int statusCodeStringLength = statusLine.indexOf(' ');
                        final String statusCodeString = statusLine.substring(0, statusCodeStringLength);
                        response.setStatusCode(Integer.parseInt(statusCodeString));
                        response.setReasonPhrase(statusLine.substring(statusCodeStringLength + 1));

                        String headerLine = responseLineReadStream.readLine().getValue();
                        while (!headerLine.isEmpty())
                        {
                            final int colonIndex = headerLine.indexOf(':');
                            final String headerName = headerLine.substring(0, colonIndex);
                            final String headerValue = headerLine.substring(colonIndex + 1).trim();
                            response.setHeader(headerName, headerValue);

                            headerLine = responseLineReadStream.readLine().getValue();
                        }

                        InMemoryByteStream responseBodyStream = null;

                        final Result<String> contentLengthHeaderValue = response.getHeaderValue(HttpHeader.ContentLengthName);
                        if (!contentLengthHeaderValue.hasError())
                        {
                            final int contentLength = Integer.parseInt(contentLengthHeaderValue.getValue());
                            if (0 < contentLength)
                            {
                                responseBodyStream = new InMemoryByteStream(network.getAsyncRunner());

                                int bytesToRead = contentLength;
                                while (result == null && 0 < bytesToRead)
                                {
                                    final Result<byte[]> responseBody = bufferedByteReadStream.readBytes(bytesToRead);
                                    if (responseBody.hasError())
                                    {
                                        result = Result.error(responseBody.getError());
                                    }
                                    else
                                    {
                                        final byte[] bytesRead = responseBody.getValue();
                                        if (bytesRead == null)
                                        {
                                            bytesToRead = 0;
                                        }
                                        else
                                        {
                                            responseBodyStream.write(bytesRead);
                                            bytesToRead -= bytesRead.length;
                                        }
                                    }
                                }
                                responseBodyStream.endOfStream();
                            }
                        }

                        if (result == null)
                        {
                            response.setBody(responseBodyStream);
                            result = Result.success(response);
                        }
                    }
                }
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
