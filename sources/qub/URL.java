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
     * Create a clone of this {@link URL}.
     */
    public MutableURL clone();

    /**
     * Get whether this {@link URL} has a scheme/protocol.
     */
    public boolean hasScheme();

    /**
     * Get the scheme (or protocol) of this {@link URL}.
     */
    public Result<String> getScheme();

    /**
     * Get whether this {@link URL} has a host.
     */
    public boolean hasHost();

    /**
     * Get the host of this {@link URL}.
     */
    public Result<String> getHost();

    /**
     * Get whether this {@link URL} has a port.
     */
    public boolean hasPort();

    /**
     * Get the port of this {@link URL}.
     */
    public Result<Integer> getPort();

    /**
     * Get whether this {@link URL} has a path.
     */
    public boolean hasPath();

    /**
     * Get the path of this {@link URL}.
     */
    public Result<String> getPath();

    /**
     * Get whether this {@link URL} has a query {@link String}.
     */
    public boolean hasQueryString();

    /**
     * Get the query string of this {@link URL}.
     */
    public Result<String> getQueryString();

    /**
     * Get the query parameters of this {@link URL}.
     */
    public Map<String,String> getQueryParameters();

    /**
     * Get the value for the query parameter with the provided name.
     * @param queryParameterName The name of the query parameter.
     */
    public Result<String> getQueryParameter(String queryParameterName);

    /**
     * Get whether this {@link URL} has a fragment.
     */
    public boolean hasFragment();

    /**
     * Get the fragment of this {@link URL}.
     */
    public Result<String> getFragment();

    /**
     * Get whether the provided {@link URL} is equal to the provided {@link Object}.
     * @param url The {@link URL} to compare against the {@link Object}.
     * @param rhs The {@link Object} to compare against the {@link URL}.
     */
    public static boolean equals(URL url, Object rhs)
    {
        return url == rhs || (url != null && rhs instanceof URL && url.equals((URL)rhs));
    }

    /**
     * Get whether this {@link URL} is equal to the provided {@link URL}.
     * @param rhs The {@link URL} to compare against this {@link URL}.
     */
    public default boolean equals(URL rhs)
    {
        boolean result = false;
        if (rhs != null)
        {
            result =
                Comparer.equal(
                    this.getScheme().catchError(NotFoundException.class).await(),
                    rhs.getScheme().catchError(NotFoundException.class).await()) &&
                Comparer.equal(
                    this.getHost().catchError(NotFoundException.class).await(),
                    rhs.getHost().catchError(NotFoundException.class).await()) &&
                Comparer.equal(
                    this.getPort().catchError(NotFoundException.class).await(),
                    rhs.getPort().catchError(NotFoundException.class).await()) &&
                Comparer.equal(
                    this.getPath().catchError(NotFoundException.class).await(),
                    rhs.getPath().catchError(NotFoundException.class).await()) &&
                Comparer.equal(this.getQueryParameters(), rhs.getQueryParameters()) &&
                Comparer.equal(
                    this.getFragment().catchError(NotFoundException.class).await(),
                    rhs.getFragment().catchError(NotFoundException.class).await());
        }
        return result;
    }

    /**
     * Encode the provided path {@link String} and add the result to the provided
     * {@link CharacterList}.
     * @param path The path {@link String} to encode.
     * @param output The {@link CharacterList} where the encoded path {@link String} will be added
     *               to.
     */
    public static void encodePath(String path, CharacterList output)
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

    /**
     * Encode the provided path {@link String}
     * @param path The path {@link String} to encode.
     */
    public static String encodePath(String path)
    {
        final CharacterList list = CharacterList.create();
        URL.encodePath(path, list);
        final String result = list.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Encode the provided query {@link String} and add the result to the provided
     * {@link CharacterList}.
     * @param query The query {@link String} to encode.
     * @param output The {@link CharacterList} where the encoded query {@link String} will be added
     *               to.
     */
    public static void encodeQuery(String query, CharacterList output)
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

    /**
     * Encode the provided query {@link String}
     * @param query The query {@link String} to encode.
     */
    public static String encodeQuery(String query)
    {
        final CharacterList list = CharacterList.create();
        URL.encodeQuery(query, list);
        final String result = list.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Encode the provided fragment {@link String} and add the result to the provided
     * {@link CharacterList}.
     * @param fragment The fragment {@link String} to encode.
     * @param output The {@link CharacterList} where the encoded fragment {@link String} will be
     *               added to.
     */
    public static void encodeFragment(String fragment, CharacterList output)
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

    /**
     * Encode the provided fragment {@link String}
     * @param fragment The fragment {@link String} to encode.
     */
    public static String encodeFragment(String fragment)
    {
        final CharacterList list = CharacterList.create();
        URL.encodeFragment(fragment, list);
        final String result = list.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public default String toString(boolean percentEncode)
    {
        return URL.toString(this, percentEncode);
    }

    public static String toString(URL url)
    {
        return URL.toString(url, false);
    }

    public static String toString(URL url, boolean percentEncode)
    {
        PreCondition.assertNotNull(url, "url");

        final CharacterList list = CharacterList.create();

        final String scheme = url.getScheme()
            .catchError(NotFoundException.class)
            .await();
        if (!Strings.isNullOrEmpty(scheme))
        {
            list.addAll(scheme);
            list.addAll("://");
        }

        final String host = url.getHost()
            .catchError(NotFoundException.class)
            .await();
        if (!Strings.isNullOrEmpty(host))
        {
            list.addAll(host);
        }

        final Integer port = url.getPort()
            .catchError(NotFoundException.class)
            .await();
        if (port != null)
        {
            list.add(':');
            list.addAll(Integers.toString(port));
        }

        final String path = url.getPath()
            .catchError(NotFoundException.class)
            .await();
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

        final String query = url.getQueryString()
            .catchError(NotFoundException.class)
            .await();
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

        final String fragment = url.getFragment()
            .catchError(NotFoundException.class)
            .await();
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
     * Parse the provided url {@link String} into a {@link URL}.
     * @param urlString The url {@link String} to parse into a {@link URL}.
     */
    public static Result<MutableURL> parse(String urlString)
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
                                throw new IllegalArgumentException("Could not parse \"" + urlString + "\" into a URL because the scheme (" + result.getScheme().await() + ") must be followed by \"://\".");
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
    public enum URLParseState
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
