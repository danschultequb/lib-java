package qub;

/**
 * An exception that is thrown when there is no resolution for a provided host name.
 */
public class HostNotFoundException extends NotFoundException
{
    private final String host;

    public HostNotFoundException(String host)
    {
        super(HostNotFoundException.getMessage(host));

        this.host = host;
    }

    private static String getMessage(String host)
    {
        PreCondition.assertNotNullAndNotEmpty(host, "host");

        return "Could not resolve the IP address for the provided host: " + Strings.escapeAndQuote(host);
    }

    /**
     * Get the host that could not be resolved.
     * @return The host that could not be resolved.
     */
    public String getHost()
    {
        return this.host;
    }
}
