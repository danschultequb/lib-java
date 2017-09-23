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
     * Get whether or not this is an empty path.
     * @return Whether or not this is an empty path.
     */
    public boolean isEmpty()
    {
        return value.isEmpty();
    }

    /**
     * Return a Path that is the result of concatenating the provided String onto the end of this
     * Path.
     * @param toConcatenate The String to concatenate onto the end of this Path.
     * @return A Path that is the result of concatenating the provided String onto the end of this
     * Path.
     */
    public Path concatenate(String toConcatenate)
    {
        return concatenate(Path.parse(toConcatenate));
    }

    /**
     * Return a Path that is the result of concatenating the provided String onto the end of this
     * Path.
     * @param toConcatenate The String to concatenate onto the end of this Path.
     * @return A Path that is the result of concatenating the provided String onto the end of this
     * Path.
     */
    public Path concatenate(Path toConcatenate)
    {
        Path result;
        if (toConcatenate == null || toConcatenate.isEmpty())
        {
            result = this;
        }
        else if (toConcatenate.isRooted())
        {
            if (this.isEmpty())
            {
                result = toConcatenate;
            }
            else
            {
                result = null;
            }
        }
        else
        {
            result = new Path(value + toConcatenate, false);
        }
        return result;
    }

    /**
     * Return a Path that is the result of concatenating the provided String onto the end of this
     * Path, after ensuring that the this Path ends with a slash.
     * @param segmentToConcatenate The String to concatenate onto the end of this Path.
     * @return A Path that is the result of concatenating the provided String onto the end of this
     * Path.
     */
    public Path concatenateSegment(String segmentToConcatenate)
    {
        return concatenateSegment(Path.parse(segmentToConcatenate));
    }

    /**
     * Return a Path that is the result of concatenating the provided String onto the end of this
     * Path, after ensuring that the this Path ends with a slash.
     * @param segmentToConcatenate The String to concatenate onto the end of this Path.
     * @return A Path that is the result of concatenating the provided String onto the end of this
     * Path.
     */
    public Path concatenateSegment(Path segmentToConcatenate)
    {
        Path result;
        if (segmentToConcatenate == null || segmentToConcatenate.isEmpty())
        {
            result = this;
        }
        else if (segmentToConcatenate.isRooted())
        {
            if (this.isEmpty())
            {
                result = segmentToConcatenate;
            }
            else
            {
                result = null;
            }
        }
        else
        {
            String resultPathString = value;
            if (!resultPathString.isEmpty() && !resultPathString.endsWith("/") && !resultPathString.endsWith("\\"))
            {
                resultPathString += "/";
            }
            resultPathString += segmentToConcatenate;
            result = new Path(resultPathString, false);
        }
        return result;
    }

    /**
     * Get whether or not this Path ends with the provided suffix.
     * @param suffix The suffix to check against this Path.
     * @return Whether or not this Path ends with the provided suffix.
     */
    public boolean endsWith(String suffix)
    {
        return !value.isEmpty() && suffix != null && !suffix.isEmpty() && value.endsWith(suffix);
    }

    /**
     * Get whether or not this Path begins with a root.
     * @return Whether or not this Path begins with a root.
     */
    public boolean isRooted()
    {
        boolean result = false;

        final Path normalizedPath = normalize();
        final Indexable<String> segments = normalizedPath.getSegments();
        if (segments.any())
        {
            final String firstSegment = segments.first();
            result = firstSegment.equals("/") || firstSegment.endsWith(":");
        }

        return result;
    }

    /**
     * If this Path is rooted, get the name of this Path's root. If this Path is not rooted, then
     * return null.
     * @return The name of this Path's root if the Path is rooted, null otherwise.
     */
    public String getRoot()
    {
        return isRooted() ? getSegments().first() : null;
    }

    /**
     * If this Path is rooted, get the Path to this Path's root. If this Path is not rooted, then
     * return null.
     * @return The Path to this Path's root if the Path is rooted, null otherwise.
     */
    public Path getRootPath()
    {
        return Path.parse(getRoot());
    }

    public String getParent()
    {
        String result = null;

        final Iterable<String> segments = getSegments();
        final int segmentCount = segments.getCount();
        if (segmentCount >= 2)
        {
            final Iterator<String> segmentIterator = segments.skipLast().iterate();
            final StringBuilder builder = new StringBuilder();
            if (segmentIterator.first().equals("/"))
            {
                builder.append(segmentIterator.first());
                segmentIterator.next();
            }

            for (final String segment : segmentIterator)
            {
                builder.append(segment);
                builder.append('/');
            }

            result = builder.toString();
        }

        return result;
    }

    public Path getParentPath()
    {
        return Path.parse(getParent());
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
    @Override
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
        return pathString == null || pathString.isEmpty() ? null : new Path(pathString, false);
    }
}
