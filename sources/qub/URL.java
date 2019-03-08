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
    private final ListMap<String,String> query = new ListMap<>();
    private String fragment;

    URL()
    {
    }

    /**
     * Create a clone of this URL.
     * @return A new clone of this URL.
     */
    public URL clone()
    {
        final URL result = new URL();
        result.scheme = this.scheme;
        result.host = this.host;
        result.port = this.port;
        result.path = this.path;
        result.query.setAll(this.query);
        result.fragment = this.fragment;
        return result;
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
        return query.get(queryParameterName);
    }

    /**
     * Set the provided query parameter in this URLs query string.
     * @param queryParameterName The name of the query parameter.
     * @param queryParameterValue The value of the query parameter.
     */
    public void setQueryParameter(String queryParameterName, String queryParameterValue)
    {
        PreCondition.assertNotNullAndNotEmpty(queryParameterName, "queryParameterName");

        query.set(queryParameterName, queryParameterValue);
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
    public boolean equals(Object rhs)
    {
        return rhs instanceof URL && equals((URL)rhs);
    }

    public boolean equals(URL rhs)
    {
        return rhs != null &&
            Comparer.equal(scheme, rhs.scheme) &&
            Comparer.equal(host, rhs.host) &&
            Comparer.equal(port, rhs.port) &&
            Comparer.equal(query, rhs.query) &&
            Comparer.equal(fragment, rhs.fragment);
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

        if (query.any())
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

    /**
     * Parse the provided urlString into a URL object.
     * @param urlString The string to parse into a URL.
     * @return The parsed URL.
     */
    public static Result<URL> parse(String urlString)
    {
        PreCondition.assertNotNullAndNotEmpty(urlString, "urlString");

        Result<URL> result = null;
        final Lexer lexer = new Lexer(urlString);
        final URL url = new URL();
        URLParseState currentState = URLParseState.SchemeOrHost;
        final StringBuilder builder = new StringBuilder();
        while (lexer.next() && result == null)
        {
            switch (currentState)
            {
                case SchemeOrHost:
                    switch (lexer.getCurrent().getType())
                    {
                        case Letters:
                        case Digits:
                        case Plus:
                        case Dash:
                            builder.append(lexer.getCurrent().toString());
                            break;

                        case Colon:
                            currentState = URLParseState.SchemeOrHostWithColon;
                            break;

                        case Period:
                            if (builder.length() == 0)
                            {
                                result = Result.error(new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"" + lexer.getCurrent().toString() + "\"."));
                            }
                            else
                            {
                                builder.append(lexer.getCurrent().toString());
                                currentState = URLParseState.Host;
                            }
                            break;

                        default:
                            result = Result.error(new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"" + lexer.getCurrent().toString() + "\"."));
                            break;
                    }
                    break;

                case SchemeOrHostWithColon:
                    switch (lexer.getCurrent().getType())
                    {
                        case ForwardSlash:
                            url.setScheme(takeText(builder));
                            currentState = URLParseState.SchemeWithColonAndForwardSlash;
                            break;

                        case Digits:
                            url.setHost(takeText(builder));
                            url.setPort(Integer.parseInt(lexer.getCurrent().toString()));
                            currentState = URLParseState.PathQueryOrFragment;
                            break;

                        default:
                            result = Result.error(new IllegalArgumentException("After the scheme or host (" + builder.toString() + ") and a colon, the following text must be either a forward slash or a port number."));
                            break;
                    }
                    break;

                case SchemeWithColonAndForwardSlash:
                    switch (lexer.getCurrent().getType())
                    {
                        case ForwardSlash:
                            currentState = URLParseState.Host;
                            break;

                        default:
                            result = Result.error(new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because the scheme (" + url.getScheme() + ") must be followed by \"://\"."));
                            break;
                    }
                    break;

                case Host:
                    switch (lexer.getCurrent().getType())
                    {
                        case Colon:
                            if (builder.length() > 0)
                            {
                                url.setHost(takeText(builder));
                                currentState = URLParseState.Port;
                            }
                            else
                            {
                                result = Result.error(new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because no host was specified before the port."));
                            }
                            break;

                        case ForwardSlash:
                            if (builder.length() > 0)
                            {
                                url.setHost(takeText(builder));
                                builder.append(lexer.getCurrent().toString());
                                currentState = URLParseState.Path;
                            }
                            else
                            {
                                result = Result.error(new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because no host was specified before the path."));
                            }
                            break;

                        case QuestionMark:
                            if (builder.length() > 0)
                            {
                                url.setHost(takeText(builder));
                                currentState = URLParseState.Query;
                            }
                            else
                            {
                                result = Result.error(new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because no host was specified before the query."));
                            }
                            break;

                        case Hash:
                            if (builder.length() > 0)
                            {
                                url.setHost(takeText(builder));
                                builder.append(lexer.getCurrent().toString());
                                currentState = URLParseState.Fragment;
                            }
                            else
                            {
                                result = Result.error(new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because no host was specified before the fragment."));
                            }
                            break;

                        default:
                            builder.append(lexer.getCurrent().toString());
                            break;
                    }
                    break;

                case Port:
                    switch (lexer.getCurrent().getType())
                    {
                        case Digits:
                            url.setPort(Integer.parseInt(lexer.getCurrent().toString()));
                            currentState = URLParseState.PathQueryOrFragment;
                            break;

                        default:
                            result = Result.error(new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because the port specified was not a number."));
                            break;
                    }
                    break;

                case PathQueryOrFragment:
                    switch (lexer.getCurrent().getType())
                    {
                        case ForwardSlash:
                            builder.append(lexer.getCurrent().toString());
                            currentState = URLParseState.Path;
                            break;

                        case QuestionMark:
                            currentState = URLParseState.Query;
                            break;

                        case Hash:
                            builder.append(lexer.getCurrent().toString());
                            currentState = URLParseState.Fragment;
                            break;

                        default:
                            result = Result.error(new IllegalArgumentException("Expected \"/\", \"?\", or \"#\", but found \"" + lexer.getCurrent().toString() + "\" instead."));
                            break;
                    }
                    break;

                case Path:
                    switch (lexer.getCurrent().getType())
                    {
                        case QuestionMark:
                            url.setPath(takeText(builder));
                            currentState = URLParseState.Query;
                            break;

                        case Hash:
                            url.setPath(takeText(builder));
                            builder.append(lexer.getCurrent().toString());
                            currentState = URLParseState.Fragment;
                            break;

                        default:
                            builder.append(lexer.getCurrent().toString());
                            break;
                    }
                    break;

                case Query:
                    switch (lexer.getCurrent().getType())
                    {
                        case Hash:
                            url.setQuery(takeText(builder));
                            builder.append(lexer.getCurrent().toString());
                            currentState = URLParseState.Fragment;
                            break;

                        default:
                            builder.append(lexer.getCurrent().toString());
                            break;
                    }
                    break;

                case Fragment:
                    builder.append(lexer.getCurrent().toString());
                    break;

                default:
                    result = Result.error(new IllegalStateException("Unrecognized URLParseState: " + currentState));
                    break;
            }
        }

        if (result == null)
        {
            switch (currentState)
            {
                case SchemeOrHost:
                case Host:
                    url.setHost(takeText(builder));
                    break;

                case SchemeOrHostWithColon:
                    result = Result.error(new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because it is missing a port number."));
                    break;

                case SchemeWithColonAndForwardSlash:
                    result = Result.error(new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because it is missing the second forward slash after the scheme."));
                    break;

                case Port:
                    result = Result.error(new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because it is missing its port number."));
                    break;

                case PathQueryOrFragment:
                    break;

                case Path:
                    url.setPath(takeText(builder));
                    break;

                case Query:
                    url.setQuery(takeText(builder));
                    break;

                case Fragment:
                    url.setFragment(takeText(builder));
                    break;

                default:
                    result = Result.error(new IllegalArgumentException("Unrecognized URL parse end state: " + currentState));
                    break;
            }

            if (result == null)
            {
                result = Result.success(url);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    private static String takeText(StringBuilder builder)
    {
        final String text = builder.toString();
        builder.setLength(0);
        return text;
    }

    /**
     * The different states of parsing a URL.
     */
    private enum URLParseState
    {
        SchemeOrHost,
        SchemeOrHostWithColon,
        SchemeWithColonAndForwardSlash,
        Host,
        Port,
        Path,
        Query,
        Fragment,
        PathQueryOrFragment
    }

    private enum QueryParseState
    {
        QueryParameterName,
        QueryParameterValue
    }
}
