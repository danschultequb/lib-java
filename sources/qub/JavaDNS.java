package qub;

/**
 * A DNS implementation that resolves hosts by making real DNS queries against the network.
 */
public class JavaDNS implements DNS
{
    @Override
    public Result<IPv4Address> resolveHost(String host)
    {
        PreCondition.assertNotNullAndNotEmpty(host, "host");

        Result<IPv4Address> result;
        try
        {
            final java.net.InetAddress inetAddress = java.net.InetAddress.getByName(host);
            String inetAddressString = inetAddress.toString();
            if (inetAddressString != null)
            {
                final int forwardSlashIndex = inetAddressString.indexOf('/');
                if (0 <= forwardSlashIndex)
                {
                    inetAddressString = inetAddressString.substring(forwardSlashIndex + 1);
                }
            }
            final IPv4Address ipAddress = IPv4Address.parse(inetAddressString);
            result = Result.success(ipAddress);
        }
        catch (java.net.UnknownHostException e)
        {
            result = Result.error(new java.net.UnknownHostException(host));
        }
        return result;
    }
}
