package qub;

/**
 * An interface that allows hosts to be resolved to IP addresses.
 */
public interface DNS
{
    /**
     * Lookup the provided host and return one of the IPv4Addresses associated with it.
     * @param host The host to lookup.
     * @return An IPv4Address associated with the provided host.
     */
    Result<IPv4Address> resolveHost(String host);
}
