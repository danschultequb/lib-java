package qub;

public interface Network
{
    AsyncRunner getAsyncRunner();

    Result<TCPClient> createTCPClient(IPv4Address remoteIPAddress, int remotePort);

    AsyncFunction<Result<TCPClient>> createTCPClientAsync(IPv4Address remoteIPAddress, int remotePort);

    Result<TCPServer> createTCPServer(int localPort);

    AsyncFunction<Result<TCPServer>> createTCPServerAsync(int localPort);

    Result<TCPServer> createTCPServer(IPv4Address localIPAddress, int localPort);

    AsyncFunction<Result<TCPServer>> createTCPServerAsync(IPv4Address localIPAddress, int localPort);

    HttpClient getHttpClient();

    DNS getDNS();
}
