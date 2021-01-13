package qub;

/**
 * A class that represents a mutable URL (uniform resource locator).
 */
public class MutableURL implements URL
{
    private String scheme;
    private String host;
    private Integer port;
    private String path;
    private final MutableMap<String,String> query = Map.create();
    private String fragment;

    private MutableURL()
    {
    }

    public static MutableURL create()
    {
        return new MutableURL();
    }

    @Override
    public MutableURL clone()
    {
        final MutableURL result = new MutableURL();
        result.scheme = this.scheme;
        result.host = this.host;
        result.port = this.port;
        result.path = this.path;
        result.query.setAll(this.query);
        result.fragment = this.fragment;
        return result;
    }

    @Override
    public String getScheme()
    {
        return scheme;
    }

    /**
     * Set the scheme (or protocol) of this MutableURL.
     * @param scheme The scheme (or protocol) of this MutableURL.
     */
    public MutableURL setScheme(String scheme)
    {
        this.scheme = Strings.isNullOrEmpty(scheme) ? null : scheme;
        return this;
    }

    @Override
    public String getHost()
    {
        return host;
    }

    /**
     * Set the host of this MutableURL.
     * @param host The host of this MutableURL.
     */
    public MutableURL setHost(String host)
    {
        this.host = Strings.isNullOrEmpty(host) ? null : host;
        return this;
    }

    @Override
    public Integer getPort()
    {
        return port;
    }

    /**
     * Set the port of this MutableURL.
     * @param port The port of this MutableURL.
     */
    public MutableURL setPort(Integer port)
    {
        this.port = port;
        return this;
    }

    @Override
    public String getPath()
    {
        return path;
    }

    /**
     * Set the path of this MutableURL.
     * @param path The path of this MutableURL.
     */
    public MutableURL setPath(String path)
    {
        this.path = Strings.isNullOrEmpty(path) ? null : path;
        return this;
    }

    @Override
    public String getQueryString()
    {
        String result;
        if (!query.any())
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
     * Set the query string of this MutableURL.
     * @param queryString The query string of this MutableURL.
     */
    public MutableURL setQueryString(String queryString)
    {
        this.query.clear();
        if (!Strings.isNullOrEmpty(queryString))
        {
            QueryParseState currentState = QueryParseState.QueryParameterName;
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
                if (currentState == QueryParseState.QueryParameterName)
                {
                    if (character ==  '=')
                    {
                        currentState = QueryParseState.QueryParameterValue;
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
                        currentState = QueryParseState.QueryParameterName;
                    }
                    else
                    {
                        queryParameterValue.append(character);
                    }
                }
            }
            if (queryParameterName.length() > 0)
            {
                if (currentState == QueryParseState.QueryParameterName)
                {
                    query.set(queryParameterName.toString(), null);
                }
                else
                {
                    query.set(queryParameterName.toString(), queryParameterValue.toString());
                }
            }
        }
        return this;
    }

    @Override
    public Map<String,String> getQueryParameters()
    {
        return this.query;
    }

    public MutableURL setQueryParameters(Map<String,String> queryParameters)
    {
        PreCondition.assertNotNull(queryParameters, "queryParameters");

        this.query.clear();
        this.query.setAll(queryParameters);

        return this;
    }

    @Override
    public Result<String> getQueryParameter(String queryParameterName)
    {
        return query.get(queryParameterName);
    }

    /**
     * Set the provided query parameter in this MutableURLs query string.
     * @param queryParameterName The name of the query parameter.
     * @param queryParameterValue The value of the query parameter.
     */
    public MutableURL setQueryParameter(String queryParameterName, String queryParameterValue)
    {
        PreCondition.assertNotNullAndNotEmpty(queryParameterName, "queryParameterName");

        query.set(queryParameterName, queryParameterValue);

        return this;
    }

    @Override
    public String getFragment()
    {
        return fragment;
    }

    /**
     * Set the fragment of this MutableURL.
     * @param fragment The fragment of this MutableURL.
     */
    public MutableURL setFragment(String fragment)
    {
        this.fragment = Strings.isNullOrEmpty(fragment) ? null : fragment;
        return this;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return URL.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return URL.toString(this);
    }

    private enum QueryParseState
    {
        QueryParameterName,
        QueryParameterValue
    }
}
