package qub;

/**
 * A class that represents a mutable {@link URL} (uniform resource locator).
 */
public class MutableURL implements URL
{
    private String scheme;
    private String host;
    private Integer port;
    private String path;
    private final MutableMap<String,String> query;
    private String fragment;

    private MutableURL()
    {
        this.query = ListMap.create();
    }

    public static MutableURL create()
    {
        return new MutableURL();
    }

    @Override
    public MutableURL clone()
    {
        return MutableURL.create()
            .setScheme(this.scheme)
            .setHost(this.host)
            .setPort(this.port)
            .setPath(this.path)
            .setQueryParameters(this.query)
            .setFragment(this.fragment);
    }

    private static <T> Result<T> getValue(T value, String valueName)
    {
        return Result.create(() ->
        {
            if (value == null)
            {
                throw new NotFoundException("No " + valueName + " was found.");
            }
            return value;
        });
    }

    @Override
    public boolean hasScheme()
    {
        return this.scheme != null;
    }

    @Override
    public Result<String> getScheme()
    {
        return MutableURL.getValue(this.scheme, "scheme/protocol");
    }

    /**
     * Set the scheme (or protocol) of this {@link MutableURL}.
     * @param scheme The new scheme (or protocol) of this {@link MutableURL}.
     * @return This object for method chaining.
     */
    public MutableURL setScheme(String scheme)
    {
        this.scheme = Strings.isNullOrEmpty(scheme) ? null : scheme;
        return this;
    }

    @Override
    public boolean hasHost()
    {
        return this.host != null;
    }

    @Override
    public Result<String> getHost()
    {
        return MutableURL.getValue(this.host, "host");
    }

    /**
     * Set the host of this {@link MutableURL}.
     * @param host The new host of this {@link MutableURL}.
     * @return This object for method chaining.
     */
    public MutableURL setHost(String host)
    {
        this.host = Strings.isNullOrEmpty(host) ? null : host;
        return this;
    }

    @Override
    public boolean hasPort()
    {
        return this.port != null;
    }

    @Override
    public Result<Integer> getPort()
    {
        return MutableURL.getValue(this.port, "port");
    }

    /**
     * Set the port of this {@link MutableURL}.
     * @param port The new port of this {@link MutableURL}.
     * @return This object for method chaining.
     */
    public MutableURL setPort(Integer port)
    {
        this.port = port;
        return this;
    }

    @Override
    public boolean hasPath()
    {
        return this.path != null;
    }

    @Override
    public Result<String> getPath()
    {
        return MutableURL.getValue(this.path, "path");
    }

    /**
     * Set the path of this {@link MutableURL}.
     * @param path The new path of this {@link MutableURL}.
     * @return This object for method chaining.
     */
    public MutableURL setPath(String path)
    {
        this.path = Strings.isNullOrEmpty(path) ? null : path;
        return this;
    }

    @Override
    public boolean hasQueryString()
    {
        return this.query.any();
    }

    @Override
    public Result<String> getQueryString()
    {
        return Result.create(() ->
        {
            if (!this.query.any())
            {
                throw new NotFoundException("No query string was found.");
            }

            final CharacterList list = CharacterList.create();
            for (final MapEntry<String,String> queryParameter : this.query)
            {
                if (list.any())
                {
                    list.add('&');
                }

                final String queryParameterName = queryParameter.getKey();
                list.addAll(queryParameterName);

                final String queryParameterValue = queryParameter.getValue();
                if (queryParameterValue != null)
                {
                    list.add('=');
                    if (!queryParameterValue.isEmpty())
                    {
                        list.addAll(queryParameterValue);
                    }
                }
            }
            final String result = list.toString(true);

            PostCondition.assertNotNullAndNotEmpty(result, "result");

            return result;
        });
    }

    /**
     * Set the query {@link String} of this {@link MutableURL}.
     * @param queryString The query string of this {@link MutableURL}.
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
                            this.query.set(queryParameterName.toString(), null);
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
                            this.query.set(queryParameterName.toString(), queryParameterValue.toString());
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
                    this.query.set(queryParameterName.toString(), null);
                }
                else
                {
                    this.query.set(queryParameterName.toString(), queryParameterValue.toString());
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
        return this.query.get(queryParameterName);
    }

    /**
     * Set the provided query parameter in this {@link MutableURL}'s query string.
     * @param queryParameterName The name of the query parameter.
     * @param queryParameterValue The value of the query parameter.
     */
    public MutableURL setQueryParameter(String queryParameterName, String queryParameterValue)
    {
        PreCondition.assertNotNullAndNotEmpty(queryParameterName, "queryParameterName");

        this.query.set(queryParameterName, queryParameterValue);

        return this;
    }

    @Override
    public boolean hasFragment()
    {
        return this.fragment != null;
    }

    @Override
    public Result<String> getFragment()
    {
        return MutableURL.getValue(this.fragment, "fragment");
    }

    /**
     * Set the fragment of this {@link MutableURL}.
     * @param fragment The new fragment of this {@link MutableURL}.
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
