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

            final String queryParameterNameState = "queryParameterNameState";
            final String queryParameterValueState = "queryParameterValueState";
            String currentState = queryParameterNameState;
            final Map<String,String> query = new ListMap<String,String>();
            final StringBuilder queryParameterName = new StringBuilder();
            final StringBuilder queryParameterValue = new StringBuilder();
            final Iterator<Character> characters = Strings.iterate(queryString);
            characters.next();
            if (characters.getCurrent() == '?')
            {
                characters.next();
            }
            for (final char character : characters)
            {
                if (currentState == queryParameterNameState)
                {
                    if (character ==  '=')
                    {
                        currentState = queryParameterValueState;
                    }
                    else if (character == '&')
                    {
                        if (queryParameterName.length() > 0)
                        {
                            query.set(queryParameterName.toString(), null);
                            queryParameterName.setLength(0);
                            queryParameterValue.setLength(0);
                        }
                    }
                    else
                    {
                        queryParameterName.append(character);
                    }
                }
                else
                {
                    if (character == '&')
                    {
                        if (queryParameterName.length() > 0)
                        {
                            query.set(queryParameterName.toString(), queryParameterValue.toString());
                            queryParameterName.setLength(0);
                            queryParameterValue.setLength(0);
                        }
                        currentState = queryParameterNameState;
                    }
                    else
                    {
                        queryParameterValue.append(character);
                    }
                }
            }
            if (queryParameterName.length() > 0)
            {
                if (currentState == queryParameterNameState)
                {
                    query.set(queryParameterName.toString(), null);
                }
                else
                {
                    query.set(queryParameterName.toString(), queryParameterValue.toString());
                }
            }
            this.query = query;
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

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();

        if (scheme != null)
        {
            builder.append(scheme);
            builder.append("://");
        }

        if (host != null)
        {
            builder.append(host);
        }

        if (port != null)
        {
            builder.append(':');
            builder.append(port);
        }

        if (path != null)
        {
            if (!path.startsWith("/"))
            {
                builder.append('/');
            }
            builder.append(path);
        }

        if (query != null)
        {
            builder.append('?');
            boolean addedQueryParameter = false;
            for (final MapEntry<String,String> queryParameter : query)
            {
                if (!addedQueryParameter)
                {
                    addedQueryParameter = true;
                }
                else
                {
                    builder.append('&');
                }

                builder.append(queryParameter.getKey());
                final String queryParameterValue = queryParameter.getValue();
                if (queryParameterValue != null)
                {
                    builder.append('=');
                    builder.append(queryParameterValue);
                }
            }
        }

        if (fragment != null)
        {
            if (!fragment.startsWith("#"))
            {
                builder.append('#');
            }
            builder.append(fragment);
        }

        return builder.toString();
    }
}
