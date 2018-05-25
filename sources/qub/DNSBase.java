package qub;

/**
 * A base implementation of DNS that provides implementations for common methods.
 */
public abstract class DNSBase implements DNS
{
    @Override
    public abstract Result<IPv4Address> resolveHost(String host);

    @Override
    public Result<URL> resolveURL(URL url)
    {
        Result<URL> result = Result.notNull(url, "url");
        if (result == null)
        {
            final String host = url.getHost();
            result = Result.notNullAndNotEmpty(host, "url.getHost()");
            if (result == null)
            {
                final Result<IPv4Address> resolvedHost = resolveHost(host);
                if (resolvedHost.hasError())
                {
                    result = Result.error(resolvedHost.getError());
                }
                else
                {
                    final URL resolvedURL = url.clone();
                    resolvedURL.setHost(resolvedHost.getValue().toString());
                    result = Result.success(resolvedURL);
                }
            }
        }
        return result;
    }
}
