package qub;

public interface TCPClient extends ByteWriteStream, ByteReadStream, AsyncDisposable
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
     * Get the remote IP address that this client is connected to.
     * @return The remote IP address that this client is connected to.
     */
    IPv4Address getRemoteIPAddress();

    /**
     * Get the remote port that this client is connected to.
     * @return The remote port that this client is connected to.
     */
    int getRemotePort();
}
