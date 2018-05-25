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

    /**
     * Lookup the host in the provided URL and return a URL with the host replaced by an
     * IPv4Address.
     * @param url The URL to resolve.
     * @return A resolved URL.
     */
    Result<URL> resolveURL(URL url);
}
