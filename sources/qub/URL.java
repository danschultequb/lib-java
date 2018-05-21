package qub;

/**
 * A class that represents a URL (uniform resource locator).
 */
public class URL
{
    private String scheme;
    private String host;
    private Integer port;
    private String path;
    private Map<String,String> query;
    private String fragment;

    URL()
    {
    }

    /**
     * Get the scheme (or protocol) of this URL.
     * @return The scheme (or protocol) of this URL.
     */
    public String getScheme()
    {
        return scheme;
    }

    /**
     * Set the scheme (or protocol) of this URL.
     * @param scheme The scheme (or protocol) of this URL.
     */
    public void setScheme(String scheme)
    {
        this.scheme = Strings.isNullOrEmpty(scheme) ? null : scheme;
    }

    /**
     * Get the host of this URL.
     * @return The host of this URL.
     */
    public String getHost()
    {
        return host;
    }

    /**
     * Set the host of this URL.
     * @param host The host of this URL.
     */
    public void setHost(String host)
    {
        this.host = Strings.isNullOrEmpty(host) ? null : host;
    }

    /**
     * Get the port of this URL.
     * @return The port of this URL.
     */
    public Integer getPort()
    {
        return port;
    }

    /**
     * Set the port of this URL.
     * @param port The port of this URL.
     */
    public void setPort(Integer port)
    {
        this.port = port;
    }

    /**
     * Get the path of this URL.
     * @return The path of this URL.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Set the path of this URL.
     * @param path The path of this URL.
     */
    public void setPath(String path)
    {
        this.path = Strings.isNullOrEmpty(path) ? null : path;
    }

    /**
     * Get the query string of this URL.
     * @return The query string of this URL.
     */
    public String getQuery()
    {
        String result;
        if (query == null || !query.any())
        {
            result = null;
        }
        else
        {
            final StringBuilder builder = new StringBuilder();
            for (final MapEntry<String,String> queryParameter : query)
            {
                if (builder.length() > 0)
                {
                    builder.append('&');
                }

                final String queryParameterName = queryParameter.getKey();
                builder.append(queryParameterName);

                final String queryParameterValue = queryParameter.getValue();
                if (queryParameterValue != null)
                {
                    builder.append('=');
                    if (!queryParameterValue.isEmpty())
                    {
                        builder.append(queryParameterValue);
                    }
                }
            }
            result = builder.toString();
        }
        return result;
    }

    /**
     * Set the query string of this URL.
     * @param queryString The query string of this URL.
     */
    public void setQuery(String queryString)
    {
        if (Strings.isNullOrEmpty(queryString))
        {
            this.query = null;
        }
        else
        {
            final String queryParameterName = "queryParameterName";
            final String queryParameterEqualsSign = "queryParameterEqualsSign";
            final String queryParameterValue = "queryParameterValue";
            String currentState = queryParameterName;
            for (final char character : Strings.iterate(queryString))
            {
                switch ()
            }
        }
    }

    /**
     * Get the value for the query parameter with the provided name. If the no query parameter
     * exists with the provided name, then null will be returned.
     * @param queryParameterName The name of the query parameter.
     * @return The value for the query parameter with the provided name, or null if no query
     * parameter exists with the provided name.
     */
    public Result<String> getQueryParameterValue(String queryParameterName)
    {
        return query == null || !query.containsKey(queryParameterName)
            ? Result.<String>error(new NotFoundException(queryParameterName))
            : Result.success(query.get(queryParameterName));
    }

    /**
     * Set the provided query parameter in this URLs query string.
     * @param queryParameterName The name of the query parameter.
     * @param queryParameterValue The value of the query parameter.
     */
    public void setQueryParameter(String queryParameterName, String queryParameterValue)
    {
        if (query == null)
        {
            query = new ListMap<String,String>();
        }
        if (!Strings.isNullOrEmpty(queryParameterName))
        {
            query.set(queryParameterName, queryParameterValue);
        }
    }

    /**
     * Get the fragment of this URL.
     * @return The fragment of this URL.
     */
    public String getFragment()
    {
        return fragment;
    }

    /**
     * Set the fragment of this URL.
     * @param fragment The fragment of this URL.
     */
    public void setFragment(String fragment)
    {
        this.fragment = Strings.isNullOrEmpty(fragment) ? null : fragment;
    }
}
