package qub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

class JavaNetwork extends NetworkBase
{
    private final AsyncRunner asyncRunner;
    private final BasicHttpClient httpClient;
    private final DNS dns;

    JavaNetwork(AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
        httpClient = new BasicHttpClient(this);
        dns = new JavaDNS();
    }

    @Override
    public Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort)
    {
        Result<TCPClient> result = NetworkBase.validateRemoteIPAddress(remoteIPAddress);
        if (result == null)
        {
            result = NetworkBase.validateRemotePort(remotePort);
            if (result == null)
            {
                result = NetworkBase.validateAsyncRunner(asyncRunner);
                if (result == null)
                {
                    try
                    {
                        final byte[] remoteIPAddressBytes = remoteIPAddress.toBytes();
                        final InetAddress remoteInetAddress = InetAddress.getByAddress(remoteIPAddressBytes);
                        final Socket socket = new Socket(remoteInetAddress, remotePort);
                        result = JavaTCPClient.create(socket, asyncRunner);
                    }
                    catch (IOException e)
                    {
                        result = Result.error(e);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Result<TCPServer> createTCPServer(int localPort)
    {
        return JavaTCPServer.create(localPort, getAsyncRunner());
    }

    @Override
    public Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort)
    {
        return JavaTCPServer.create(localIPAddress, localPort, getAsyncRunner());
    }

    @Override
    public HttpClient getHttpClient()
    {
        return httpClient;
    }

    @Override
    public DNS getDNS()
    {
        return dns;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }
}
