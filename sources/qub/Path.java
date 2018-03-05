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
     * Get whether or not this Path has a file extension.
     * @return Whether or not this Path has a file extension.
     */
    public boolean hasFileExtension()
    {
        return getFileExtension() != null;
    }

    /**
     * Get the file extension (including the period) of this Path. If no file extension exists on
     * this Path, then null will be returned.
     * @return The file extension (including the period) of this Path.
     */
    public String getFileExtension()
    {
        String result;
        final Path normalizedPath = normalize();
        final String lastSegment = normalizedPath.getSegments().last();
        final int lastPeriodIndex = lastSegment.lastIndexOf('.');
        if (lastPeriodIndex == -1)
        {
            result = null;
        }
        else
        {
            result = lastSegment.substring(lastPeriodIndex);
        }
        return result;
    }

    /**
     * Get a Path that is this path without a file extension. If this path doesn't have a file
     * extension, then this path will be returned.
     * @return This path without a file extension.
     */
    public Path withoutFileExtension()
    {
        Path result;
        final Path normalizedPath = normalize();
        final String lastSegment = normalizedPath.getSegments().last();
        final int lastPeriodIndex = lastSegment.lastIndexOf('.');
        if (lastPeriodIndex == -1)
        {
            result = this;
        }
        else
        {
            final int lastSegmentLength = lastSegment.length();

            final String normalizedPathString = normalizedPath.toString();
            final int normalizedPathStringLength = normalizedPathString.length();

            final int fileExtensionStartIndex = normalizedPathStringLength - (lastSegmentLength - lastPeriodIndex);

            result = new Path(normalizedPathString.substring(0, fileExtensionStartIndex), true);
        }
        return result;
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
        if (toConcatenate == null)
        {
            result = this;
        }
        else if (toConcatenate.isRooted())
        {
            result = null;
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
        if (segmentToConcatenate == null)
        {
            result = this;
        }
        else if (segmentToConcatenate.isRooted())
        {
            result = null;
        }
        else
        {
            String resultPathString = value;
            if (!resultPathString.endsWith("/") && !resultPathString.endsWith("\\"))
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
        return suffix != null && !suffix.isEmpty() && value.endsWith(suffix);
    }

    /**
     * Get whether or not this Path begins with a root.
     * @return Whether or not this Path begins with a root.
     */
    public boolean isRooted()
    {
        final Path normalizedPath = normalize();
        final Indexable<String> segments = normalizedPath.getSegments();
        final String firstSegment = segments.first();
        return firstSegment.equals("/") || firstSegment.endsWith(":");
    }

    /**
     * If this Path is rooted, get the name of this Path's root. If this Path is not rooted, then
     * return null.
     * @return The name of this Path's root if the Path is rooted, null otherwise.
     */
    public Path getRoot()
    {
        return isRooted() ? Path.parse(getSegments().first()) : null;
    }

    public Path getParent()
    {
        Path result = null;

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

            result = Path.parse(builder.toString());
        }

        return result;
    }

    /**
     * Get this Path relative to the provided Folder's path. If this path does not begin with the
     * provided path, then this Path will be returned.
     * @param folder The folder to make a relative version of this Path against.
     * @return The relative version of this Path against the provided folder's path, or this Path if
     * this Path doesn't start with the provided basePath.
     */
    public Path relativeTo(Folder folder)
    {
        return relativeTo(folder == null ? null : folder.getPath());
    }

    /**
     * Get this Path relative to the provided Root's path. If this path does not begin with the
     * provided path, then this Path will be returned.
     * @param root The root to make a relative version of this Path against.
     * @return The relative version of this Path against the provided root's path, or this Path if
     * this Path doesn't start with the provided basePath.
     */
    public Path relativeTo(Root root)
    {
        return relativeTo(root == null ? null : root.getPath());
    }

    /**
     * Get this Path relative to the provided Path. If this path does not begin with the provided
     * path, then this Path will be returned.
     * @param basePath The path to make a relative version of this Path against.
     * @return The relative version of this Path against the provided basePath, or this Path if this
     * Path doesn't start with the provided basePath.
     */
    public Path relativeTo(Path basePath)
    {
        Path result = this;
        if (basePath != null && !equals(basePath))
        {
            final Indexable<String> thisSegments = getSegments();
            final int thisSegmentsCount = thisSegments.getCount();
            final Indexable<String> basePathSegments = basePath.getSegments();
            final int basePathSegmentsCount = basePathSegments.getCount();

            int segmentIndex = 0;
            for (; segmentIndex < basePathSegmentsCount; ++segmentIndex)
            {
                if (!thisSegments.get(segmentIndex).equalsIgnoreCase(basePathSegments.get(segmentIndex)))
                {
                    break;
                }
            }

            if (0 < segmentIndex && segmentIndex < thisSegmentsCount)
            {
                final StringBuilder relativePathStringBuilder = new StringBuilder();

                for (int i = segmentIndex; i < basePathSegmentsCount; ++i)
                {
                    if (relativePathStringBuilder.length() > 0)
                    {
                        relativePathStringBuilder.append('/');
                    }
                    relativePathStringBuilder.append("..");
                }

                final Iterable<String> relativePathSegments = thisSegments.skip(segmentIndex);
                for (final String segment : relativePathSegments)
                {
                    if (relativePathStringBuilder.length() > 0)
                    {
                        relativePathStringBuilder.append('/');
                    }
                    relativePathStringBuilder.append(segment);
                }
                result = parse(relativePathStringBuilder.toString());
            }
        }
        return result;
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
