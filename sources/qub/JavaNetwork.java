package qub;

class JavaNetwork implements Network
{
    @Override
    public Result<TCPClient> createTCPClient(IPv4Address targetIPAddress, int targetPort)
    {
        return JavaTCPClient.create(targetIPAddress, targetPort);
    }
}
