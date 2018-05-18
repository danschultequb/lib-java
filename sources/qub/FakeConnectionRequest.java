package qub;

public class FakeConnectionRequest
{
    private final IPv4Address remoteIPAddress;
    private final int remotePort;

    public FakeConnectionRequest(IPv4Address remoteIPAddress, int remotePort)
    {
        this.remoteIPAddress = remoteIPAddress;
        this.remotePort = remotePort;
    }

    public IPv4Address getRemoteIPAddress()
    {
        return remoteIPAddress;
    }

    public int getRemotePort()
    {
        return remotePort;
    }
}
