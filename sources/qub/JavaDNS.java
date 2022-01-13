package qub;

/**
 * A DNS implementation that resolves hosts by making real DNS queries against the network.
 */
public class JavaDNS implements DNS
{
    private JavaDNS()
    {
    }

    public static JavaDNS create()
    {
        return new JavaDNS();
    }

    @Override
    public Result<IPv4Address> resolveHost(String host)
    {
        PreCondition.assertNotNullAndNotEmpty(host, "host");

        return Result.create(() ->
        {
            IPv4Address result;
            try
            {
                final java.net.InetAddress inetAddress = java.net.InetAddress.getByName(host);
                String inetAddressString = inetAddress.toString();
                final int forwardSlashIndex = inetAddressString.indexOf('/');
                inetAddressString = inetAddressString.substring(forwardSlashIndex + 1);
                result = IPv4Address.parse(inetAddressString).await();
            }
            catch (java.net.UnknownHostException e)
            {
                throw new HostNotFoundException(host);
            }
            return result;
        });
    }
}
