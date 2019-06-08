package qub;

/**
 * The path to a file system entry (Root, File, or Folder).
 */
public class Path
{
    private final String value;
    private Path normalizedPath;

    /**
     * Create a new Path object create the provided value.
     * @param value The String representation of this Path.
     * @param normalized Whether or not this Path is normalized.
     */
    private Path(String value, boolean normalized)
    {
        this.value = value;
        this.normalizedPath = normalized ? this : null;
    }

    /**
     * Get the number of characters in this Path.
     * @return The number of characters in this Path.
     */
    public int length()
    {
        return value.length();
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
     * Set the file extension for this Path.
     * @param fileExtension The new file extension for this Path.
     */
    public Path changeFileExtension(String fileExtension)
    {
        PreCondition.assertFalse(endsWith('/'), "endsWith('/')");
        PreCondition.assertFalse(endsWith('\\'), "endsWith('\\')");

        if (fileExtension == null)
        {
            fileExtension = "";
        }
        else if (fileExtension.length() > 0 && !fileExtension.startsWith("."))
        {
            fileExtension = '.' + fileExtension;
        }

        Path result;

        final String currentFileExtension = getFileExtension();
        if (Comparer.equal(currentFileExtension, fileExtension))
        {
            result = this;
        }
        else
        {
            final int currentFileExtensionLength = Strings.getLength(currentFileExtension);
            final int currentPathLength = length();
            final String pathWithoutFileExtension = toString().substring(0, currentPathLength - currentFileExtensionLength);
            final String pathWithNewFileExtension = pathWithoutFileExtension + fileExtension;
            result = Path.parse(pathWithNewFileExtension);
        }

        PostCondition.assertNotNull(result, "result");

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
     * Get a Path that is this path without a root. If this path doesn't have a root, then this path
     * will be returned.
     * @return This path without a root.
     */
    public Result<Path> withoutRoot()
    {
        return getRoot()
            .catchError(NotFoundException.class, () -> this)
            .thenResult((Path root) ->
            {
                Result<Path> withoutRootResult;
                if (root == this)
                {
                    withoutRootResult = Result.success(this);
                }
                else
                {
                    String pathStringWithoutRoot = toString().substring(root.length());
                    if (pathStringWithoutRoot.startsWith("/") || pathStringWithoutRoot.startsWith("\\"))
                    {
                        pathStringWithoutRoot = pathStringWithoutRoot.substring(1);
                    }

                    if (Strings.isNullOrEmpty(pathStringWithoutRoot))
                    {
                        withoutRootResult = Result.error(new NotFoundException("The path " + Strings.escapeAndQuote(this) + " cannot create a path without its root because it only contains a root path."));
                    }
                    else
                    {
                        withoutRootResult = Result.success(Path.parse(pathStringWithoutRoot));
                    }
                }
                return withoutRootResult;
            });
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
        PreCondition.assertNotNullAndNotEmpty(toConcatenate, "toConcatenate");

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
        PreCondition.assertNotNull(toConcatenate, "toConcatenate");
        PreCondition.assertFalse(toConcatenate.isRooted(), "toConcatenate.isRooted()");

        return new Path(value + toConcatenate.toString(), false);
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
        PreCondition.assertNotNullAndNotEmpty(segmentToConcatenate, "segmentToConcatenate");

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
        PreCondition.assertNotNull(segmentToConcatenate, "segmentToConcatenate");
        PreCondition.assertFalse(segmentToConcatenate.isRooted(), "segmentToConcatenate.isRooted()");

        String resultPathString = value;
        if (!resultPathString.endsWith("/") && !resultPathString.endsWith("\\"))
        {
            resultPathString += "/";
        }
        resultPathString += segmentToConcatenate;
        return new Path(resultPathString, false);
    }

    /**
     * Get whether or not this Path ends with the provided suffix.
     * @param suffix The suffix to check against this Path.
     * @return Whether or not this Path ends with the provided suffix.
     */
    public boolean endsWith(char suffix)
    {
        return value.endsWith(Characters.toString(suffix));
    }

    /**
     * Get whether or not this Path ends with the provided suffix.
     * @param suffix The suffix to check against this Path.
     * @return Whether or not this Path ends with the provided suffix.
     */
    public boolean endsWith(Character suffix)
    {
        PreCondition.assertNotNull(suffix, "suffix");

        return value.endsWith(Characters.toString(suffix));
    }

    /**
     * Get whether or not this Path ends with the provided suffix.
     * @param suffix The suffix to check against this Path.
     * @return Whether or not this Path ends with the provided suffix.
     */
    public boolean endsWith(String suffix)
    {
        PreCondition.assertNotNullAndNotEmpty(suffix, "suffix");

        return value.endsWith(suffix);
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
        return firstSegment.equals("/") || (firstSegment.endsWith(":") && !firstSegment.equals(":"));
    }

    /**
     * If this Path is rooted, get the name of this Path's root. If this Path is not rooted, then
     * return null.
     * @return The name of this Path's root if the Path is rooted, null otherwise.
     */
    public Result<Path> getRoot()
    {
        return isRooted() ?
           Result.success(Path.parse(getSegments().first())) :
           Result.error(new NotFoundException("Could not find a root on the path " + Strings.escapeAndQuote(this) + "."));
    }

    public Result<Path> getParent()
    {
        return Result.createResult(() ->
        {
            Result<Path> result;

            final Path resolvedPath = this.resolve().await();
            final Iterable<String> segments = resolvedPath.getSegments();
            final int segmentCount = segments.getCount();
            if (segmentCount <= 1)
            {
                result = Result.error(new NotFoundException("The path " + Strings.escapeAndQuote(value) + " doesn't have a parent folder."));
            }
            else
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

                result = Result.success(Path.parse(builder.toString()));
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
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
        PreCondition.assertNotNull(folder, "folder");

        return relativeTo(folder.getPath());
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
        PreCondition.assertNotNull(root, "root");

        return relativeTo(root.getPath());
    }

    /**
     * Get this Path relative to the provided Path. If this path does not begin with the provided
     * path, then this Path will be returned.
     * @param basePath The path to make a relative version of this Path against.
     * @return The relative version of this Path against the provided basePath, or this Path if this
     * Path doesn't start with the provided basePath.
     */
    public Path relativeTo(String basePath)
    {
        PreCondition.assertNotNullAndNotEmpty(basePath, "basePath");

        return relativeTo(Path.parse(basePath));
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
        PreCondition.assertNotNull(basePath, "basePath");
        PreCondition.assertTrue(basePath.isRooted(), "basePath.isRooted()");
        PreCondition.assertTrue(this.isRooted(), "this.isRooted()");

        Path result = this;
        if (this.equals(basePath))
        {
            result = Path.parse(".");
        }
        else
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
        if (normalizedPath == null)
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

            String normalizedPathString = normalizedPathStringBuilder.toString();
            if (normalizedPathString.endsWith("/") && !normalizedPathString.equals("/") && !normalizedPathString.endsWith(":/"))
            {
                normalizedPathString = normalizedPathString.substring(0, normalizedPathString.length() - 1);
            }

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
        return (rhs instanceof Path && equals((Path)rhs)) ||
            (rhs instanceof String && equals((String)rhs));
    }

    public boolean equals(String rhs)
    {
        return !Strings.isNullOrEmpty(rhs) && equals(Path.parse(rhs));
    }

    public boolean equals(Path rhs)
    {
        boolean result = false;

        if (rhs != null)
        {
            final Path resolvedLhs = this.resolve().await();
            final Path resolvedRhs = rhs.resolve().await();
            result = resolvedLhs.value.equals(resolvedRhs.value);
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
     * Resolve any current path (.) or parent path (..) segments in this Path.
     * @return
     */
    public Result<Path> resolve()
    {
        Result<Path> result = null;

        final Path normalizedPath = this.normalize();
        final boolean isRooted = normalizedPath.isRooted();
        final List<String> resolvedSegments = List.create(normalizedPath.getSegments());
        for (int i = 1; i < resolvedSegments.getCount(); ++i)
        {
            final String currentSegment = resolvedSegments.get(i);
            if (currentSegment.equals("."))
            {
                resolvedSegments.removeAt(i);
                i -= 1;
            }
            else if (currentSegment.equals(".."))
            {
                if (isRooted && i == 1)
                {
                    result = Result.error(new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                    break;
                }
                else if (!resolvedSegments.get(i - 1).equals(".."))
                {
                    resolvedSegments.removeAt(i);
                    resolvedSegments.removeAt(i - 1);
                    i -= 2;
                }
            }
        }

        if (result == null)
        {
            final StringBuilder builder = new StringBuilder();
            String previousResolvedSegment = null;
            for (final String resolvedSegment : resolvedSegments)
            {
                if (previousResolvedSegment != null && !previousResolvedSegment.equals("/"))
                {
                    builder.append('/');
                }
                builder.append(resolvedSegment);
                previousResolvedSegment = resolvedSegment;
            }
            String resultPathString = builder.toString();
            if (!resultPathString.equals("/") && (normalizedPath.endsWith("/") || (resultPathString.endsWith(":") && resolvedSegments.getCount() == 1)))
            {
                builder.append('/');
            }
            result = Result.success(new Path(builder.toString(), true));
        }
        return result;
    }

    public Result<Path> resolve(String relativePathString)
    {
        return resolve(Path.parse(relativePathString));
    }

    public Result<Path> resolve(Path relativePath)
    {
        PreCondition.assertNotNull(relativePath, "relativePath");
        PreCondition.assertFalse(relativePath.isRooted(), "relativePath.isRooted()");

        final Path concatenatedPath = this.concatenateSegment(relativePath);
        return concatenatedPath.resolve();
    }

    /**
     * Parse a Path object create the provided pathString.
     * @param pathString The String representation of a Path.
     * @return The parsed Path object, or null if the provided pathString couldn't be parsed.
     */
    public static Path parse(String pathString)
    {
        PreCondition.assertNotNullAndNotEmpty(pathString, "pathString");

        return new Path(pathString, false);
    }
}
