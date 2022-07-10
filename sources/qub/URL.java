package qub;

/**
 * A class that represents a URL (uniform resource locator).
 */
public interface URL
{
    public static MutableURL create()
    {
        return MutableURL.create();
    }

    /**
     * Create a clone of this URL.
     * @return A new clone of this URL.
     */
    MutableURL clone();

    /**
     * Get the scheme (or protocol) of this URL.
     * @return The scheme (or protocol) of this URL.
     */
    String getScheme();

    /**
     * Get the host of this URL.
     * @return The host of this URL.
     */
    String getHost();

    /**
     * Get the port of this URL.
     * @return The port of this URL.
     */
    Integer getPort();

    /**
     * Get the path of this URL.
     * @return The path of this URL.
     */
    String getPath();

    /**
     * Get the query string of this URL.
     * @return The query string of this URL.
     */
    String getQueryString();

    /**
     * Get the query parameters of this URL.
     * @return The query parameters of this URL.
     */
    Map<String,String> getQueryParameters();

    /**
     * Get the value for the query parameter with the provided name. If the no query parameter
     * exists with the provided name, then null will be returned.
     * @param queryParameterName The name of the query parameter.
     * @return The value for the query parameter with the provided name, or null if no query
     * parameter exists with the provided name.
     */
    Result<String> getQueryParameter(String queryParameterName);

    /**
     * Get the fragment of this URL.
     * @return The fragment of this URL.
     */
    String getFragment();

    static boolean equals(URL url, Object rhs)
    {
        return url == rhs || (url != null && rhs instanceof URL && url.equals((URL)rhs));
    }

    default boolean equals(URL rhs)
    {
        return rhs != null &&
            Comparer.equal(this.getScheme(), rhs.getScheme()) &&
            Comparer.equal(this.getHost(), rhs.getHost()) &&
            Comparer.equal(this.getPort(), rhs.getPort()) &&
            Comparer.equal(this.getPath(), rhs.getPath()) &&
            Comparer.equal(this.getQueryParameters(), rhs.getQueryParameters()) &&
            Comparer.equal(this.getFragment(), rhs.getFragment());
    }

    static void encodePath(String path, CharacterList output)
    {
        PreCondition.assertNotNull(output, "output");

        if (!Strings.isNullOrEmpty(path))
        {
            for (final byte b : CharacterEncoding.UTF_8.encodeCharacters(path).await())
            {
                if (b <= 0x001F /* control characters */ ||
                    b >  0x007E /* ~ */ ||
                    b == 0x0020 /* SPACE */ ||
                    b == 0x0022 /* " */ ||
                    b == 0x0023 /* # */ ||
                    b == 0x003C /* < */ ||
                    b == 0x003E /* > */ ||
                    b == 0x003F /* ? */ ||
                    b == 0x0060 /* ` */ ||
                    b == 0x007B /* { */ ||
                    b == 0x007D /* } */)
                {
                    output.add('%');
                    final String hexString = Bytes.toHexString(b, false);
                    if (hexString.length() == 1)
                    {
                        output.add('0');
                    }
                    output.addAll(hexString);
                }
                else
                {
                    output.add((char)b);
                }
            }
        }
    }

    static void encodeQuery(String query, CharacterList output)
    {
        PreCondition.assertNotNull(output, "output");

        if (!Strings.isNullOrEmpty(query))
        {
            for (final byte b : CharacterEncoding.UTF_8.encodeCharacters(query).await())
            {
                if (b <= 0x001F /* control characters */ ||
                    b >  0x007E /* ~ */ ||
                    b == 0x0020 /* SPACE */ ||
                    b == 0x0022 /* " */ ||
                    b == 0x0023 /* # */ ||
                    b == 0x003C /* < */ ||
                    b == 0x003E /* > */)
                {
                    output.add('%');
                    final String hexString = Bytes.toHexString(b, false);
                    if (hexString.length() == 1)
                    {
                        output.add('0');
                    }
                    output.addAll(hexString);
                }
                else
                {
                    output.add((char)b);
                }
            }
        }
    }

    static void encodeFragment(String fragment, CharacterList output)
    {
        PreCondition.assertNotNull(output, "output");

        if (!Strings.isNullOrEmpty(fragment))
        {
            for (final byte b : CharacterEncoding.UTF_8.encodeCharacters(fragment).await())
            {
                if (b <= 0x001F /* control characters */ ||
                    b >  0x007E /* ~ */ ||
                    b == 0x0020 /* SPACE */ ||
                    b == 0x0022 /* " */ ||
                    b == 0x003C /* < */ ||
                    b == 0x003E /* > */ ||
                    b == 0x0060 /* ` */)
                {
                    output.add('%');
                    final String hexString = Bytes.toHexString(b, false);
                    if (hexString.length() == 1)
                    {
                        output.add('0');
                    }
                    output.addAll(hexString);
                }
                else
                {
                    output.add((char)b);
                }
            }
        }
    }

    default String toString(boolean percentEncode)
    {
        return URL.toString(this, percentEncode);
    }

    static String toString(URL url)
    {
        return URL.toString(url, false);
    }

    static String toString(URL url, boolean percentEncode)
    {
        PreCondition.assertNotNull(url, "url");

        final CharacterList list = CharacterList.create();

        final String scheme = url.getScheme();
        if (!Strings.isNullOrEmpty(scheme))
        {
            list.addAll(scheme);
            list.addAll("://");
        }

        final String host = url.getHost();
        if (!Strings.isNullOrEmpty(host))
        {
            list.addAll(host);
        }

        final Integer port = url.getPort();
        if (port != null)
        {
            list.add(':');
            list.addAll(Integers.toString(port));
        }

        final String path = url.getPath();
        if (!Strings.isNullOrEmpty(path))
        {
            if (!path.startsWith("/"))
            {
                list.add('/');
            }

            if (percentEncode)
            {
                URL.encodePath(path, list);
            }
            else
            {
                list.addAll(path);
            }
        }

        final String query = url.getQueryString();
        if (!Strings.isNullOrEmpty(query))
        {
            if (!query.startsWith("?"))
            {
                list.add('?');
            }

            if (percentEncode)
            {
                URL.encodeQuery(query, list);
            }
            else
            {
                list.addAll(query);
            }
        }

        final String fragment = url.getFragment();
        if (!Strings.isNullOrEmpty(fragment))
        {
            if (!fragment.startsWith("#"))
            {
                list.add('#');
            }

            if (percentEncode)
            {
                URL.encodeFragment(fragment, list);
            }
            else
            {
                list.addAll(fragment);
            }
        }

        final String result = list.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Parse the provided urlString into a URL object.
     * @param urlString The string to parse into a URL.
     * @return The parsed URL.
     */
    static Result<MutableURL> parse(String urlString)
    {
        PreCondition.assertNotNullAndNotEmpty(urlString, "urlString");

        return Result.create(() ->
        {
            final Lexer lexer = new Lexer(urlString);
            final MutableURL result = URL.create();
            URLParseState currentState = URLParseState.SchemeOrHost;
            final CharacterList list = CharacterList.create();
            while (lexer.next())
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
                                list.addAll(lexer.getCurrent().toString());
                                break;

                            case Colon:
                                currentState = URLParseState.SchemeOrHostWithColon;
                                break;

                            case Period:
                                if (!list.any())
                                {
                                    throw new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"" + lexer.getCurrent().toString() + "\".");
                                }
                                else
                                {
                                    list.addAll(lexer.getCurrent().toString());
                                    currentState = URLParseState.Host;
                                }
                                break;

                            default:
                                throw new IllegalArgumentException("A URL must begin with either a scheme (such as \"http\") or a host (such as \"www.example.com\"), not \"" + lexer.getCurrent().toString() + "\".");
                        }
                        break;

                    case SchemeOrHostWithColon:
                        switch (lexer.getCurrent().getType())
                        {
                            case ForwardSlash:
                                result.setScheme(URL.takeText(list));
                                currentState = URLParseState.SchemeWithColonAndForwardSlash;
                                break;

                            case Digits:
                                result.setHost(URL.takeText(list));
                                result.setPort(Integer.parseInt(lexer.getCurrent().toString()));
                                currentState = URLParseState.PathQueryOrFragment;
                                break;

                            default:
                                throw new IllegalArgumentException("After the scheme or host (" + list.toString(true) + ") and a colon, the following text must be either a forward slash or a port number.");
                        }
                        break;

                    case SchemeWithColonAndForwardSlash:
                        switch (lexer.getCurrent().getType())
                        {
                            case ForwardSlash:
                                currentState = URLParseState.Host;
                                break;

                            default:
                                throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because the scheme (" + result.getScheme() + ") must be followed by \"://\".");
                        }
                        break;

                    case Host:
                        switch (lexer.getCurrent().getType())
                        {
                            case Colon:
                                if (list.any())
                                {
                                    result.setHost(URL.takeText(list));
                                    currentState = URLParseState.Port;
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because no host was specified before the port.");
                                }
                                break;

                            case ForwardSlash:
                                if (list.any())
                                {
                                    result.setHost(URL.takeText(list));
                                    list.addAll(lexer.getCurrent().toString());
                                    currentState = URLParseState.Path;
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because no host was specified before the path.");
                                }
                                break;

                            case QuestionMark:
                                if (list.any())
                                {
                                    result.setHost(URL.takeText(list));
                                    currentState = URLParseState.Query;
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because no host was specified before the query.");
                                }
                                break;

                            case Hash:
                                if (list.any())
                                {
                                    result.setHost(URL.takeText(list));
                                    list.addAll(lexer.getCurrent().toString());
                                    currentState = URLParseState.Fragment;
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because no host was specified before the fragment.");
                                }
                                break;

                            default:
                                list.addAll(lexer.getCurrent().toString());
                                break;
                        }
                        break;

                    case Port:
                        switch (lexer.getCurrent().getType())
                        {
                            case Digits:
                                result.setPort(Integer.parseInt(lexer.getCurrent().toString()));
                                currentState = URLParseState.PathQueryOrFragment;
                                break;

                            default:
                                throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because the port specified was not a number.");
                        }
                        break;

                    case PathQueryOrFragment:
                        switch (lexer.getCurrent().getType())
                        {
                            case ForwardSlash:
                                list.addAll(lexer.getCurrent().toString());
                                currentState = URLParseState.Path;
                                break;

                            case QuestionMark:
                                currentState = URLParseState.Query;
                                break;

                            case Hash:
                                list.addAll(lexer.getCurrent().toString());
                                currentState = URLParseState.Fragment;
                                break;

                            default:
                                throw new IllegalArgumentException("Expected \"/\", \"?\", or \"#\", but found \"" + lexer.getCurrent().toString() + "\" instead.");
                        }
                        break;

                    case Path:
                        switch (lexer.getCurrent().getType())
                        {
                            case QuestionMark:
                                result.setPath(URL.takeText(list));
                                currentState = URLParseState.Query;
                                break;

                            case Hash:
                                result.setPath(URL.takeText(list));
                                list.addAll(lexer.getCurrent().toString());
                                currentState = URLParseState.Fragment;
                                break;

                            default:
                                list.addAll(lexer.getCurrent().toString());
                                break;
                        }
                        break;

                    case Query:
                        switch (lexer.getCurrent().getType())
                        {
                            case Hash:
                                result.setQueryString(URL.takeText(list));
                                list.addAll(lexer.getCurrent().toString());
                                currentState = URLParseState.Fragment;
                                break;

                            default:
                                list.addAll(lexer.getCurrent().toString());
                                break;
                        }
                        break;

                    case Fragment:
                        list.addAll(lexer.getCurrent().toString());
                        break;

                    default:
                        throw new IllegalStateException("Unrecognized URLParseState: " + currentState);
                }
            }

            switch (currentState)
            {
                case SchemeOrHost:
                case Host:
                    result.setHost(URL.takeText(list));
                    break;

                case SchemeOrHostWithColon:
                    throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because it is missing a port number.");

                case SchemeWithColonAndForwardSlash:
                    throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because it is missing the second forward slash after the scheme.");

                case Port:
                    throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because it is missing its port number.");

                case PathQueryOrFragment:
                    break;

                case Path:
                    result.setPath(URL.takeText(list));
                    break;

                case Query:
                    result.setQueryString(URL.takeText(list));
                    break;

                case Fragment:
                    result.setFragment(URL.takeText(list));
                    break;

                default:
                    throw new IllegalArgumentException("Unrecognized URL parse end state: " + currentState);
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    private static String takeText(CharacterList list)
    {
        PreCondition.assertNotNull(list, "list");

        final String result = list.toString(true);
        list.clear();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * The different states of parsing a URL.
     */
    enum URLParseState
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
}
