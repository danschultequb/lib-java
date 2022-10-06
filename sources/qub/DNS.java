package qub;

/**
 * An interface that allows hosts to be resolved to IP addresses.
 */
public interface DNS
{
    /**
     * Create a new instance of the default {@link DNS} type.
     */
    public static DNS create()
    {
        return JavaDNS.create();
    }

    /**
     * Lookup the provided host and return one of the {@link IPv4Address}es associated with it.
     * @param host The host to lookup.
     */
    public Result<IPv4Address> resolveHost(String host);
}
