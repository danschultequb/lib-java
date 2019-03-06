package qub;

public class FakeDNS implements DNS
{
    private final MutableMap<String,IPv4Address> hostToIPAddressMap = Map.create();

    public Result<Void> set(String host, IPv4Address ipAddress)
    {
        PreCondition.assertNotNullAndNotEmpty(host, "host");
        PreCondition.assertNotNull(ipAddress, "ipAddress");

        hostToIPAddressMap.set(host, ipAddress);
        return Result.success();
    }

    public Result<IPv4Address> remove(String host)
    {
        PreCondition.assertNotNullAndNotEmpty(host, "host");

        return hostToIPAddressMap.remove(host);
    }

    @Override
    public Result<IPv4Address> resolveHost(String host)
    {
        PreCondition.assertNotNullAndNotEmpty(host, "host");

        Result<IPv4Address> result;
        final IPv4Address hostAsIpAddress = IPv4Address.parse(host);
        if (hostAsIpAddress != null)
        {
            result = Result.success(hostAsIpAddress);
        }
        else if (!hostToIPAddressMap.containsKey(host))
        {
            result = Result.error(new java.net.UnknownHostException(host));
        }
        else
        {
            result = hostToIPAddressMap.get(host);
        }
        return result;
    }
}
