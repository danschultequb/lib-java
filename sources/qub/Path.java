package qub;

/**
 * The {@link Path} to a {@link FileSystemEntry} (such as {@link Root}, {@link File}, or
 * {@link Folder}).
 */
public class Path implements Comparable<Path>
{
    private final String value;
    private Path normalizedPath;
    private Indexable<String> segments;

    /**
     * Create a new {@link Path} object from the provided value.
     * @param value The {@link String} representation of this {@link Path}.
     * @param normalized Whether this {@link Path} is normalized.
     */
    private Path(String value, boolean normalized)
    {
        this.value = value;
        this.normalizedPath = normalized ? this : null;
    }

    /**
     * Parse a {@link Path} object from the provided pathString.
     * @param pathString The {@link String} representation of the {@link Path} to return.
     */
    public static Path parse(String pathString)
    {
        PreCondition.assertNotNullAndNotEmpty(pathString, "pathString");

        return new Path(pathString, false);
    }

    /**
     * Get the number of characters in this {@link Path}.
     */
    public int length()
    {
        return value.length();
    }

    /**
     * Get the value of the last segment of this {@link Path}.
     */
    public String getName()
    {
        return this.getSegments().last().await();
    }

    /**
     * Get the value of the last segment of this {@link Path} without a file extension. If this
     * {@link Path} is definitely a folder path (ends with a '/' or '\'), then just the name of this
     * {@link Path} will be returned since it doesn't have a "file extension".
     */
    public String getNameWithoutFileExtension()
    {
        return this.withoutFileExtension().getName();
    }

    /**
     * Get whether this {@link Path} has a file extension.
     */
    public boolean hasFileExtension()
    {
        return this.getFileExtension() != null;
    }

    /**
     * Get this {@link Path}'s file extension (including the period). If no file extension exists on
     * this {@link Path}, then null will be returned.
     */
    public String getFileExtension()
    {
        String result;
        if (this.endsWith('/') || this.endsWith('\\'))
        {
            result = null;
        }
        else
        {
            final Path normalizedPath = this.normalize();
            final String lastSegment = normalizedPath.getSegments().last().await();
            final int lastPeriodIndex = lastSegment.lastIndexOf('.');
            if (lastPeriodIndex == -1)
            {
                result = null;
            }
            else
            {
                result = lastSegment.substring(lastPeriodIndex);
            }
        }
        return result;
    }

    /**
     * Get a {@link Path} that is equal to this {@link Path} except the file extension has been
     * changed to the provided file extension. This function will fail if this {@link Path} ends
     * with '/' or '\'. If the provided fileExtension is null or empty, then the returned
     * {@link Path} will not have a file extension.
     * @param fileExtension The file extension for the returned {@link Path}.
     */
    public Path changeFileExtension(String fileExtension)
    {
        PreCondition.assertFalse(this.endsWith('/'), "this.endsWith('/')");
        PreCondition.assertFalse(this.endsWith('\\'), "this.endsWith('\\')");

        if (fileExtension != null)
        {
            if (fileExtension.isEmpty())
            {
                fileExtension = null;
            }
            else if (!fileExtension.startsWith("."))
            {
                fileExtension = '.' + fileExtension;
            }
        }

        Path result;

        final String currentFileExtension = this.getFileExtension();
        if (Comparer.equal(currentFileExtension, fileExtension))
        {
            result = this;
        }
        else
        {
            final int currentFileExtensionLength = Strings.getLength(currentFileExtension);
            final int currentPathLength = this.length();
            final String pathWithoutFileExtension = toString().substring(0, currentPathLength - currentFileExtensionLength);
            final String pathWithNewFileExtension = pathWithoutFileExtension + (fileExtension == null ? "" : fileExtension);
            result = Path.parse(pathWithNewFileExtension);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get a {@link Path} that is this {@link Path} without a file extension. If this {@link Path}
     * doesn't have a file extension, then this {@link Path} will be returned.
     */
    public Path withoutFileExtension()
    {
        Path result = this;
        final Path normalizedPath = this.normalize();
        if (!normalizedPath.endsWith('/'))
        {
            final String name = normalizedPath.getName();
            final int lastPeriodIndex = name.lastIndexOf('.');
            if (lastPeriodIndex != -1)
            {
                final int nameLength = name.length();

                final String normalizedPathString = normalizedPath.toString();
                final int normalizedPathStringLength = normalizedPathString.length();

                final int fileExtensionStartIndex = normalizedPathStringLength - (nameLength - lastPeriodIndex);

                result = new Path(normalizedPathString.substring(0, fileExtensionStartIndex), true);
            }
        }
        return result;
    }

    /**
     * Get a {@link Path} that is this {@link Path} without a {@link Root}. If this {@link Path}
     * doesn't have a {@link Root}, then this {@link Path} will be returned.
     */
    public Result<Path> withoutRoot()
    {
        return this.getRoot()
            .catchError(NotFoundException.class, () -> this)
            .then((Path root) ->
            {
                Path result;
                if (root == this)
                {
                    result = this;
                }
                else
                {
                    String pathStringWithoutRoot = this.toString().substring(root.length());
                    if (pathStringWithoutRoot.startsWith("/") || pathStringWithoutRoot.startsWith("\\"))
                    {
                        pathStringWithoutRoot = pathStringWithoutRoot.substring(1);
                    }

                    if (Strings.isNullOrEmpty(pathStringWithoutRoot))
                    {
                        throw new NotFoundException("The path " + Strings.escapeAndQuote(this) + " cannot create a path without its root because it only contains a root path.");
                    }
                    result = Path.parse(pathStringWithoutRoot);
                }
                return result;
            });
    }

    /**
     * Return a {@link Path} that is the result of concatenating the provided {@link String} onto
     * the end of this {@link Path}.
     * @param toConcatenate The {@link String} to concatenate onto the end of this {@link Path}.
     */
    public Path concatenate(String toConcatenate)
    {
        PreCondition.assertNotNullAndNotEmpty(toConcatenate, "toConcatenate");

        return Path.parse(this.value + toConcatenate);
    }

    /**
     * Return a {@link Path} that is the result of concatenating the provided {@link Path}'s
     * {@link String} representation onto the end of this {@link Path}.
     * @param toConcatenate The {@link Path} to concatenate onto the end of this {@link Path}.
     */
    public Path concatenate(Path toConcatenate)
    {
        PreCondition.assertNotNull(toConcatenate, "toConcatenate");

        return this.concatenate(toConcatenate.toString());
    }

    /**
     * Return a {@link Path} that is the result of concatenating the provided {@link Path}'s
     * {@link String} representation onto the end of this {@link Path}. If this {@link Path} doesn't
     * end with a '/' or '\' character, then a '/' character will be inserted between this
     * {@link Path} and the provided {@link Path} in the resulting {@link Path}.
     * @param segmentsToConcatenate The {@link Path} to concatenate onto the end of this
     * {@link Path}.
     */
    public Path concatenateSegments(String segmentsToConcatenate)
    {
        PreCondition.assertNotNullAndNotEmpty(segmentsToConcatenate, "segmentsToConcatenate");

        return concatenateSegments(Path.parse(segmentsToConcatenate));
    }

    /**
     * Return a Path that is the result of concatenating the provided String onto the end of this
     * Path, after ensuring that the this Path ends with a slash.
     * @param segmentsToConcatenate The String to concatenate onto the end of this Path.
     * @return A Path that is the result of concatenating the provided String onto the end of this
     * Path.
     */
    public Path concatenateSegments(Path segmentsToConcatenate)
    {
        PreCondition.assertNotNull(segmentsToConcatenate, "segmentsToConcatenate");
        PreCondition.assertFalse(segmentsToConcatenate.isRooted(), "segmentsToConcatenate.isRooted()");

        String resultPathString = this.value;
        if (!resultPathString.endsWith("/") && !resultPathString.endsWith("\\"))
        {
            resultPathString += "/";
        }
        resultPathString += segmentsToConcatenate;
        return new Path(resultPathString, false);
    }

    /**
     * Get whether this {@link Path} starts with the provided prefix.
     * @param prefix The prefix to check against this {@link Path}.
     */
    public boolean startsWith(char prefix)
    {
        return this.value.startsWith(Characters.toString(prefix));
    }

    /**
     * Get whether this {@link Path} starts with the provided prefix.
     * @param prefix The prefix to check against this {@link Path}.
     */
    public boolean startsWith(Character prefix)
    {
        PreCondition.assertNotNull(prefix, "prefix");

        return this.value.startsWith(Characters.toString(prefix));
    }

    /**
     * Get whether this {@link Path} starts with the provided prefix.
     * @param prefix The prefix to check against this {@link Path}.
     */
    public boolean startsWith(String prefix)
    {
        PreCondition.assertNotNullAndNotEmpty(prefix, "prefix");

        return this.value.startsWith(prefix);
    }

    /**
     * Get whether this {@link Path} starts with the provided prefix.
     * @param prefix The prefix to check against this {@link Path}.
     */
    public boolean startsWith(Path prefix)
    {
        PreCondition.assertNotNull(prefix, "prefix");

        return this.startsWith(prefix.toString());
    }

    /**
     * Get whether this {@link Path} ends with the provided suffix.
     * @param suffix The suffix to check against this {@link Path}.
     */
    public boolean endsWith(char suffix)
    {
        return this.endsWith(Characters.toString(suffix));
    }

    /**
     * Get whether this {@link Path} ends with the provided suffix.
     * @param suffix The suffix to check against this {@link Path}.
     */
    public boolean endsWith(Character suffix)
    {
        PreCondition.assertNotNull(suffix, "suffix");

        return this.endsWith(Characters.toString(suffix));
    }

    /**
     * Get whether this {@link Path} ends with the provided suffix.
     * @param suffix The suffix to check against this {@link Path}.
     */
    public boolean endsWith(String suffix)
    {
        PreCondition.assertNotNullAndNotEmpty(suffix, "suffix");

        return this.value.endsWith(suffix);
    }

    /**
     * Get whether this {@link Path} ends with the provided suffix.
     * @param suffix The suffix to check against this {@link Path}.
     */
    public boolean endsWith(Path suffix)
    {
        PreCondition.assertNotNull(suffix, "suffix");

        return this.endsWith(suffix.toString());
    }

    /**
     * Get whether this {@link Path} contains the provided value.
     * @param value The value to look for in this {@link Path}.
     */
    public boolean contains(char value)
    {
        return this.contains(Characters.toString(value));
    }

    /**
     * Get whether this {@link Path} contains the provided value.
     * @param value The value to look for in this {@link Path}.
     */
    public boolean contains(Character value)
    {
        PreCondition.assertNotNull(value, "value");

        return this.contains(Characters.toString(value));
    }

    /**
     * Get whether this {@link Path} contains the provided value.
     * @param value The value to look for in this {@link Path}.
     */
    public boolean contains(String value)
    {
        PreCondition.assertNotNullAndNotEmpty(value, "value");

        return this.value.contains(value);
    }

    /**
     * Get whether this {@link Path} contains the provided value.
     * @param value The value to look for in this {@link Path}.
     */
    public boolean contains(Path value)
    {
        PreCondition.assertNotNull(value, "value");

        return this.contains(value.toString());
    }

    /**
     * Get whether this {@link Path} begins with a root.
     */
    public boolean isRooted()
    {
        final Indexable<String> segments = this.getSegments();
        final String firstSegment = segments.first().catchError().await();
        return firstSegment != null && (firstSegment.equals("/") || (firstSegment.endsWith(":") && !firstSegment.equals(":")));
    }

    /**
     * Get the {@link Path} to this {@link Path}'s root.
     */
    public Result<Path> getRoot()
    {
        return this.isRooted() ?
           Result.success(Path.parse(this.getSegments().first().await())) :
           Result.error(new NotFoundException("Could not find a root on the path " + Strings.escapeAndQuote(this) + "."));
    }

    /**
     * Get the {@link Path} to this {@link Path}'s parent folder.
     */
    public Result<Path> getParent()
    {
        return Result.create(() ->
        {
            final Path resolvedPath = this.resolve().await();
            final Iterable<String> segments = resolvedPath.getSegments();
            final int segmentCount = segments.getCount();
            if (segmentCount <= 1)
            {
                throw new NotFoundException("The path " + Strings.escapeAndQuote(value) + " doesn't have a parent folder.");
            }

            final Iterator<String> segmentIterator = segments.skipLast().iterate();
            final StringBuilder builder = new StringBuilder();
            if (segmentIterator.first().await().equals("/"))
            {
                builder.append(segmentIterator.first().await());
                segmentIterator.next();
            }

            for (final String segment : segmentIterator)
            {
                builder.append(segment);
                builder.append('/');
            }

            final Path result = Path.parse(builder.toString());

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    /**
     * Get this {@link Path} relative to the provided {@link Folder}'s {@link Path}.
     * @param folder The {@link Folder} to make a relative version of this {@link Path} against.
     */
    public Path relativeTo(Folder folder)
    {
        PreCondition.assertNotNull(folder, "folder");

        return this.relativeTo(folder.getPath());
    }

    /**
     * Get this {@link Path} relative to the provided {@link Root}'s {@link Path}.
     * @param root The {@link Root} to make a relative version of this {@link Path} against.
     */
    public Path relativeTo(Root root)
    {
        PreCondition.assertNotNull(root, "root");

        return this.relativeTo(root.getPath());
    }

    /**
     * Get this {@link Path} relative to the provided {@link String} path.
     * @param basePath The {@link String} path to make a relative version of this {@link Path}
     *                 against.
     */
    public Path relativeTo(String basePath)
    {
        PreCondition.assertNotNullAndNotEmpty(basePath, "basePath");

        return this.relativeTo(Path.parse(basePath));
    }

    /**
     * Get this {@link Path} relative to the provided {@link Path}.
     * @param basePath The {@link Path} to make a relative version of this {@link Path} against.
     */
    public Path relativeTo(Path basePath)
    {
        PreCondition.assertNotNull(basePath, "basePath");
        PreCondition.assertTrue(basePath.isRooted(), "basePath.isRooted()");
        PreCondition.assertTrue(this.isRooted(), "this.isRooted()");
        PreCondition.assertEqual(this.getRoot().await(), basePath.getRoot().await(), "basePath.getRoot().await()");
        PreCondition.check(!this.equals(basePath, false), AssertionMessages.notEqual(this, basePath, "basePath"));

        Path result;
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
            final int minimumSegmentCount = Math.minimum(thisSegmentsCount, basePathSegmentsCount);

            int segmentIndex = 0;
            for (; segmentIndex < minimumSegmentCount; ++segmentIndex)
            {
                if (!thisSegments.get(segmentIndex).equalsIgnoreCase(basePathSegments.get(segmentIndex)))
                {
                    break;
                }
            }

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
            result = Path.parse(relativePathStringBuilder.toString());
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
        if (this.normalizedPath == null)
        {
            final CharacterList normalizedPathString = CharacterList.create();

            final int valueLength = this.value.length();
            boolean previousCharacterWasSlash = false;
            for (int i = 0; i < valueLength; ++i)
            {
                final char c = this.value.charAt(i);
                if (c == '/' || c == '\\')
                {
                    if (!previousCharacterWasSlash)
                    {
                        previousCharacterWasSlash = true;
                        normalizedPathString.add('/');
                    }
                }
                else
                {
                    previousCharacterWasSlash = false;
                    normalizedPathString.add(c);
                }
            }

            if (normalizedPathString.endsWith(":") && !normalizedPathString.contains('/'))
            {
                normalizedPathString.add('/');
            }

            final String normalizedPathStringString = normalizedPathString.toString();
            if (normalizedPathStringString.equals(this.value))
            {
                this.normalizedPath = this;
            }
            else
            {
                this.normalizedPath = new Path(normalizedPathStringString, true);
            }
        }
        return this.normalizedPath;
    }

    /**
     * Return a copy of this {@link Path} where any current path (.) or parent path (..) segments
     * have been resolved. If this {@link Path} doesn't contain any current path (.) or parent path
     * (..) segments, then this {@link Path} will be returned.
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
            if (!resultPathString.equals("/") && (normalizedPath.endsWith("/") || ((resultPathString.endsWith(":") && resolvedSegments.getCount() == 1))))
            {
                builder.append('/');
            }
            result = Result.success(new Path(builder.toString(), true));
        }
        return result;
    }

    /**
     * Get a resolved {@link Path} that is the result of resolving the provided {@link String}
     * relativePath against this {@link Path}.
     * @param relativePath The relative path to resolve against this {@link Path}.
     */
    public Result<Path> resolve(String relativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(relativePath, "relativePath");

        return resolve(Path.parse(relativePath));
    }

    /**
     * Get a resolved {@link Path} that is the result of resolving the provided relative
     * {@link Path} against this {@link Path}.
     * @param relativePath The relative {@link Path} to resolve against this {@link Path}.
     */
    public Result<Path> resolve(Path relativePath)
    {
        PreCondition.assertNotNull(relativePath, "relativePath");
        PreCondition.assertFalse(relativePath.isRooted(), "relativePath.isRooted()");

        final Path concatenatedPath = this.concatenateSegments(relativePath);
        return concatenatedPath.resolve();
    }

    /**
     * Get whether or not this path is an ancestor of the provided path.
     * @param possibleDescendantPathString The path that may be a descendant of this path.
     * @return Whether or not this path is an ancestor of the provided path.
     */
    public Result<Boolean> isAncestorOf(String possibleDescendantPathString)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleDescendantPathString, "possibleDescendantPathString");
        PreCondition.assertTrue(this.isRooted(), "this.isRooted()");

        return this.isAncestorOf(Path.parse(possibleDescendantPathString));
    }

    /**
     * Get whether or not this path is an ancestor of the provided path.
     * @param possibleDescendantPath The path that may be a descendant of this path.
     * @return Whether or not this path is an ancestor of the provided path.
     */
    public Result<Boolean> isAncestorOf(Path possibleDescendantPath)
    {
        PreCondition.assertNotNull(possibleDescendantPath, "possibleDescendantPath");
        PreCondition.assertTrue(possibleDescendantPath.isRooted(), "possibleDescendantPath.isRooted()");
        PreCondition.assertTrue(this.isRooted(), "this.isRooted()");

        return Result.create(() ->
        {
            final Indexable<String> thisSegments = this.resolve().await().getSegments();
            final int thisSegmentsCount = thisSegments.getCount();

            final Indexable<String> possibleDescendantSegments = possibleDescendantPath.resolve().await().getSegments();
            final int possibleDescendantSegmentsCount = possibleDescendantSegments.getCount();

            boolean result = thisSegmentsCount < possibleDescendantSegmentsCount;
            if (result)
            {
                for (int i = 0; i < thisSegmentsCount; ++i)
                {
                    if (!Comparer.equal(thisSegments.get(i), possibleDescendantSegments.get(i)))
                    {
                        result = false;
                        break;
                    }
                }
            }

            return result;
        });
    }

    /**
     * Get whether this {@link Path} is a descendant of the provided path String.
     * @param possibleAncestorPathString The path string that may be an ancestor of this
     * {@link Path}.
     * @return Whether this {@link Path} is a descendant of the provided path string.
     */
    public Result<Boolean> isDescendantOf(String possibleAncestorPathString)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleAncestorPathString, "possibleAncestorPathString");
        PreCondition.assertTrue(this.isRooted(), "this.isRooted()");

        return this.isDescendantOf(Path.parse(possibleAncestorPathString));
    }

    /**
     * Get whether this {@link Path} is a descendant of the provided {@link Path}.
     * @param possibleAncestorPath The {@link Path} that may be an ancestor of this {@link Path}.
     * @return Whether this {@link Path} is a descendant of the provided {@link Path}.
     */
    public Result<Boolean> isDescendantOf(Path possibleAncestorPath)
    {
        PreCondition.assertNotNull(possibleAncestorPath, "possibleAncestorPath");
        PreCondition.assertTrue(possibleAncestorPath.isRooted(), "possibleAncestorPath.isRooted()");
        PreCondition.assertTrue(this.isRooted(), "this.isRooted()");

        return possibleAncestorPath.isAncestorOf(this);
    }

    /**
     * Get the segments (root, folders, and/or file) of this {@link Path}.
     */
    public Indexable<String> getSegments()
    {
        if (this.segments == null)
        {
            final List<String> result = List.create();
            final Path normalizedPath = this.normalize();
            final String normalizedPathString = normalizedPath.toString();
            final int normalizedPathStringLength = normalizedPathString.length();

            int currentSlashIndex = -1;
            if (normalizedPathStringLength > 0 && normalizedPathString.charAt(0) == '/')
            {
                result.add("/");
                currentSlashIndex = 0;
            }

            while (true)
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

            this.segments = result;
        }
        return this.segments;
    }

    @Override
    public Comparison compareWith(Path value)
    {
        return value == null
            ? Comparison.GreaterThan
            : Strings.compare(this.normalize().toString(), value.normalize().toString());
    }

    @Override
    public boolean equals(Object rhs)
    {
        return (rhs instanceof Path && this.equals((Path)rhs)) ||
            (rhs instanceof String && this.equals((String)rhs));
    }

    public boolean equals(String rhs)
    {
        return !Strings.isNullOrEmpty(rhs) && this.equals(Path.parse(rhs));
    }

    public boolean equals(Path rhs)
    {
        return this.equals(rhs, true);
    }

    public boolean equals(Path rhs, boolean checkTrailingSlash)
    {
        boolean result = false;

        if (rhs != null)
        {
            final Path resolvedLhs = this.resolve().await();
            String resolvedLhsString = resolvedLhs.toString();
            final Path resolvedRhs = rhs.resolve().await();
            String resolvedRhsString = resolvedRhs.toString();
            result = resolvedLhsString.equals(resolvedRhsString);
            if (!result && !checkTrailingSlash)
            {
                if (resolvedLhs.endsWith('/'))
                {
                    resolvedLhsString = resolvedLhsString.substring(0, resolvedLhsString.length() - 1);
                }

                if (resolvedRhs.endsWith('/'))
                {
                    resolvedRhsString = resolvedRhsString.substring(0, resolvedRhsString.length() - 1);
                }

                result = resolvedLhsString.equals(resolvedRhsString);
            }
        }

        return result;
    }

    @Override
    public int hashCode()
    {
        return this.normalize().toString().hashCode();
    }

    /**
     * Get the String representation of this Path.
     * @return The String representation of this Path.
     */
    @Override
    public String toString()
    {
        return this.value;
    }
}
