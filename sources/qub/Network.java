package qub;

public interface Network
{
    Result<TCPClient> createTCPClient(IPv4Address targetIPAddress, int targetPort);
}
