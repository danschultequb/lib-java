package qub;

public interface TCPServer extends AsyncDisposable
{
    /**
     * Get the Clock object that will be used to determine when an operation has timed out.
     * @return The Clock object that will be used to determine when an operation has timed out.
     */
    Clock getClock();

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
     * @param timeout The timeout for this operation.
     * @return The accepted incoming TCPClient.
     */
    default Result<TCPClient> accept(Duration timeout)
    {
        PreCondition.assertGreaterThan(timeout, Duration.zero, "timeout");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getClock(), "getClock()");

        final DateTime dateTimeTimeout = getClock().getCurrentDateTime().plus(timeout);
        return accept(dateTimeTimeout);
    }

    /**
     * Accept a single incoming TCPClient.
     * @param timeout The timeout for this operation.
     * @return The accepted incoming TCPClient.
     */
    Result<TCPClient> accept(DateTime timeout);

    /**
     * Accept a single incoming TCPClient.
     * @return The accepted incoming TCPClient.
     */
    default AsyncFunction<Result<TCPClient>> acceptAsync()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> accept());
    }
}
