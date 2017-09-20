package qub;

/**
 * The path to a file system entry (Root, File, or Folder).
 */
public class Path
{
    private final String value;
    private final boolean normalized;

    /**
     * Create a new Path object from the provided value.
     * @param value The String representation of this Path.
     * @param normalized Whether or not this Path is normalized.
     */
    private Path(String value, boolean normalized)
    {
        this.value = value;
        this.normalized = normalized;
    }

    /**
     * Get the normalized version of this Path. In this context, a normalized path uses only forward
     * slashes ('/') as path separators and doesn't have multiple slashes in a row.
     * @return The normalized version of this Path.
     */
    public Path normalize()
    {
        Path result;
        if (normalized)
        {
            result = this;
        }
        else
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
            result = new Path(normalizedPathString, true);
        }
        return result;
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
     * Parse a Path object from the provided pathString.
     * @param pathString The String representation of a Path.
     * @return The parsed Path object, or null if the provided pathString couldn't be parsed.
     */
    public static Path parse(String pathString)
    {
        return pathString == null ? null : new Path(pathString, false);
    }
}
