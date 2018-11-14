package qub;

public class FakeDNS implements DNS
{
    private final Map<String,IPv4Address> hostToIPAddressMap = new ListMap<>();

    public Result<Boolean> set(String host, IPv4Address ipAddress)
    {
        PreCondition.assertNotNullAndNotEmpty(host, "host");
        PreCondition.assertNotNull(ipAddress, "ipAddress");

        hostToIPAddressMap.set(host, ipAddress);
        return Result.successTrue();
    }

    public Result<Boolean> remove(String host)
    {
        PreCondition.assertNotNullAndNotEmpty(host, "host");

        return hostToIPAddressMap.remove(host).convertError();
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
