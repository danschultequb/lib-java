package qub;

public interface TCPServer extends AsyncDisposable
{
    /**
     * Get the local IP address that this client is connected to.
     * @return The local IP address that this client is connected to.
     */
    IPv4Address getLocalIPAddress();

    /**
     * Get the local port that this client is connected to.
     * @return The local port that this client is connected to.
     */
    int getLocalPort();

    /**
     * Accept a single incoming TCPClient.
     * @return The accepted incoming TCPClient.
     */
    Result<TCPClient> accept();

    /**
     * Accept a single incoming TCPClient.
     * @return The accepted incoming TCPClient.
     */
    AsyncFunction<Result<TCPClient>> acceptAsync();
}
