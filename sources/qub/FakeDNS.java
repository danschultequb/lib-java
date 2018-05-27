package qub;

import java.net.UnknownHostException;

public class FakeDNS implements DNS
{
    private final Map<String,IPv4Address> hostToIPAddressMap = new ListMap<String,IPv4Address>();

    public Result<Boolean> set(String host, IPv4Address ipAddress)
    {
        Result<Boolean> result = Result.notNullAndNotEmpty(host, "host");
        if (result == null)
        {
            result = Result.notNull(ipAddress, "ipAddress");
            if (result == null)
            {
                hostToIPAddressMap.set(host, ipAddress);
                result = Result.successTrue();
            }
        }
        return result;
    }

    public Result<Boolean> remove(String host)
    {
        Result<Boolean> result = Result.notNullAndNotEmpty(host, "host");
        if (result == null)
        {
            if (hostToIPAddressMap.remove(host))
            {
                result = Result.successTrue();
            }
            else
            {
                result = Result.error(new KeyNotFoundException(host));
            }
        }
        return result;
    }

    @Override
    public Result<IPv4Address> resolveHost(String host)
    {
        Result<IPv4Address> result = Result.notNullAndNotEmpty(host, "host");
        if (result == null)
        {
            final IPv4Address hostAsIpAddress = IPv4Address.parse(host);
            if (hostAsIpAddress != null)
            {
                result = Result.success(hostAsIpAddress);
            }
            else
            {
                final IPv4Address ipAddress = hostToIPAddressMap.get(host);
                if (ipAddress == null)
                {
                    result = Result.error(new UnknownHostException(host));
                }
                else
                {
                    result = Result.success(ipAddress);
                }
            }
        }
        return result;
    }
}
