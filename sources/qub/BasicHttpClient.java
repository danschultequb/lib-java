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
        Result<HttpResponse> result = Result.notNull(request, "request");
        if (result == null)
        {
            final URL requestUrl = request.getUrl();
            String requestHost = requestUrl.getHost();
            result = Result.notNull(requestHost, "request.getUrl().getHost()");
            if (result == null)
            {
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
                            final InMemoryByteStream requestByteStream = new InMemoryByteStream();
                            final LineWriteStream requestStream = requestByteStream.asLineWriteStream(CharacterEncoding.US_ASCII, "\r\n");
                            requestStream.writeLine("%s %s HTTP/1.1", request.getMethod(), request.getUrl());
                            for (final HttpHeader header : request.getHeaders())
                            {
                                requestStream.writeLine("%s:%s", header.getName(), header.getValue());
                            }
                            requestStream.writeLine();
                            requestByteStream.endOfStream();

                            Result<Boolean> writeResult = tcpClient.writeAll(requestByteStream);
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
                                final LineReadStream responseLineReadStream = tcpClient.asLineReadStream(false).getValue();
                                String statusLine = responseLineReadStream.readLine().getValue();
                                final int httpVersionLength = statusLine.indexOf(' ');

                                final String httpVersion = statusLine.substring(0, httpVersionLength);
                                statusLine = statusLine.substring(httpVersionLength + 1);

                                final int statusCodeStringLength = statusLine.indexOf(' ');
                                final String statusCodeString = statusLine.substring(0, statusCodeStringLength);
                                final int statusCode = Integer.parseInt(statusCodeString);
                                statusLine = statusLine.substring(statusCodeStringLength + 1);

                                final String reasonPhrase = statusLine;

                                final MutableHttpHeaders responseHeaders = new MutableHttpHeaders();

                                String headerLine = responseLineReadStream.readLine().getValue();
                                while (!headerLine.isEmpty())
                                {
                                    final int colonIndex = headerLine.indexOf(':');
                                    final String headerName = headerLine.substring(0, colonIndex);
                                    final String headerValue = headerLine.substring(colonIndex + 1).trim();
                                    responseHeaders.set(headerName, headerValue);

                                    headerLine = responseLineReadStream.readLine().getValue();
                                }

                                InMemoryByteStream responseBodyStream = null;

                                final Result<String> contentLengthHeaderValue = responseHeaders.getValue("content-length");
                                if (!contentLengthHeaderValue.hasError())
                                {
                                    final int contentLength = Integer.parseInt(contentLengthHeaderValue.getValue());
                                    if (0 < contentLength)
                                    {
                                        responseBodyStream = new InMemoryByteStream(network.getAsyncRunner());

                                        int bytesToRead = contentLength;
                                        while (result == null && 0 < bytesToRead)
                                        {
                                            final Result<byte[]> responseBody = tcpClient.readBytes(bytesToRead);
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
                                    result = Result.success(new HttpResponse(httpVersion, statusCode, reasonPhrase, responseHeaders, responseBodyStream));
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private static String readLine(ByteReadStream byteReadStream)
    {
        final List<Byte> buffer = new ArrayList<>();
        while (byteReadStream.next())
        {
            buffer.add(byteReadStream.getCurrent());
            if (byteReadStream.getCurrent() == '\n')
            {
                break;
            }
        }

        if (buffer.last() == (byte)'\n')
        {
            buffer.removeLast();
            if (buffer.last() == (byte)'\r')
            {
                buffer.removeLast();
            }
        }

        return !buffer.any() ? "" : CharacterEncoding.US_ASCII.decodeAsString(Array.toByteArray(buffer)).getValue();
    }
}
