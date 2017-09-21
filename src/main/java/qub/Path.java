package qub;

/**
 * The path to a file system entry (Root, File, or Folder).
 */
public class Path
{
    private final String value;
    private Path normalizedPath;

    /**
     * Create a new Path object from the provided value.
     * @param value The String representation of this Path.
     * @param normalized Whether or not this Path is normalized.
     */
    private Path(String value, boolean normalized)
    {
        this.value = value;
        this.normalizedPath = normalized ? this : null;
    }

    /**
     * Get the normalized version of this Path. In this context, a normalized path uses only forward
     * slashes ('/') as path separators and doesn't have multiple slashes in a row.
     * @return The normalized version of this Path.
     */
    public Path normalize()
    {
        if (normalizedPath ==  null)
        {
            final StringBuilder normalizedPathStringBuilder = new StringBuilder();

            final int valueLength = value.length();
            boolean previousCharacterWasSlash = false;
            for (int i = 0; i < valueLength; ++i)
            {
                final char c = value.charAt(i);
                if (c == '/' || c == '\\')
                {
                    if (!previousCharacterWasSlash)
                    {
                        previousCharacterWasSlash = true;
                        normalizedPathStringBuilder.append('/');
                    }
                }
                else
                {
                    previousCharacterWasSlash = false;
                    normalizedPathStringBuilder.append(c);
                }
            }

            final String normalizedPathString = normalizedPathStringBuilder.toString();
            if (normalizedPathString.equals(value))
            {
                normalizedPath = this;
            }
            else
            {
                normalizedPath = new Path(normalizedPathString, true);
            }
        }
        return normalizedPath;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Path && equals((Path)rhs);
    }

    public boolean equals(Path rhs)
    {
        boolean result = false;

        if (rhs != null)
        {
            final Path normalizedLhs = this.normalize();
            final Path normalizedRhs = rhs.normalize();
            result = normalizedLhs.value.equals(normalizedRhs.value);
        }

        return result;
    }

    /**
     * Get the String representation of this Path.
     * @return The String representation of this Path.
     */
    public String toString()
    {
        return value;
    }

    /**
     * Get the segments (root, folders, and/or file) of this Path.
     * @return The segments (root, folders, and/or file) of this Path.
     */
    public Indexable<String> getSegments()
    {
        final ArrayList<String> result = new ArrayList<>();
        final Path normalizedPath = normalize();
        final String normalizedPathString = normalizedPath.toString();
        final int normalizedPathStringLength = normalizedPathString.length();

        int currentSlashIndex = -1;
        if (normalizedPathStringLength > 0 && normalizedPathString.charAt(0) == '/')
        {
            result.add("/");
            currentSlashIndex = 0;
        }

        while(true)
        {
            final int segmentStartIndex = currentSlashIndex + 1;
            currentSlashIndex = normalizedPathString.indexOf('/', segmentStartIndex);
            if (currentSlashIndex == -1)
            {
                if (segmentStartIndex < normalizedPathStringLength)
                {
                    result.add(normalizedPathString.substring(segmentStartIndex));
                }
                break;
            }
            else
            {
                result.add(normalizedPathString.substring(segmentStartIndex, currentSlashIndex));
            }
        }

        return result;
    }

    /**
     * Parse a Path object from the provided pathString.
     * @param pathString The String representation of a Path.
     * @return The parsed Path object, or null if the provided pathString couldn't be parsed.
     */
    public static Path parse(String pathString)
    {
        return pathString == null ? null : new Path(pathString, false);
    }
}
