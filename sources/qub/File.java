package qub;

public class File extends FileSystemEntry
{
    File(FileSystem fileSystem, String path)
    {
        this(fileSystem, Path.parse(path));
    }

    File(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);
    }

    /**
     * Get the file extension (including the '.' character) for this File.
     * @return
     */
    public String getFileExtension()
    {
        return getPath().getFileExtension();
    }

    /**
     * Get the name of this File without the file extension.
     * @return The name of this File without the file extension.
     */
    public String getNameWithoutFileExtension()
    {
        return getPath().withoutFileExtension().getSegments().last();
    }

    /**
     * Create this File and return whether or not it was created as a result of this function.
     * @return Whether or not this function created the file.
     */
    public boolean create()
    {
        return getFileSystem().createFile(getPath());
    }

    /**
     * Create this File with the provided contents and return whether or not it was created as a
     * result of this function.
     * @param contents The contents to create the file with if the file is created as a result of
     *                 this function.
     * @return Whether or not this function created the file.
     */
    public boolean create(byte[] contents)
    {
        return getFileSystem().createFile(getPath(), contents);
    }

    /**
     * Create this File with the provided contents and return whether or not it was created as a
     * result of this function.
     * @param contents The contents to create the file with if the file is created as a result of
     *                 this function.
     * @return Whether or not this function created the file.
     */
    public boolean create(String contents)
    {
        return getFileSystem().createFile(getPath(), contents);
    }

    /**
     * Create this File with the provided contents and return whether or not it was created as a
     * result of this function.
     * @param contents The contents to create the file with if the file is created as a result of
     *                 this function.
     * @param encoding The CharacterEncoding to use to convert the provided contents to bytes.
     * @return Whether or not this function created the file.
     */
    public boolean create(String contents, CharacterEncoding encoding)
    {
        return getFileSystem().createFile(getPath(), contents, encoding);
    }

    /**
     * Get whether or not this File exists.
     */
    @Override
    public boolean exists()
    {
        return getFileSystem().fileExists(getPath());
    }

    @Override
    public boolean delete()
    {
        return getFileSystem().deleteFile(getPath());
    }

    /**
     * Get the date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     * @return The date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     */
    public DateTime getLastModified()
    {
        return getFileSystem().getFileLastModified(getPath());
    }

    /**
     * Get the entire contents of this File as a single byte[]. If this file doesn't exist, then
     * null will be returned.
     * @return The entire contents of this File as a single byte[], or null if this file doesn't
     * exist.
     */
    public byte[] getContents()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContents(path);
    }

    /**
     * Get the entire contents of this File as a single ASCII-encoded String. If this file doesn't
     * exist, then null will be returned.
     * @return The entire contents of this File as a single ASCII-encoded String, or null if this
     * File doesn't exist.
     */
    public String getContentsAsString()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentsAsString(path);
    }

    /**
     * Get the entire contents of this File as a single String. If this file doesn't exist, then
     * null will be returned.
     * @return The entire contents of this File as a single String, or null if this File doesn't
     * exist.
     */
    public String getContentsAsString(CharacterEncoding encoding)
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentsAsString(path, encoding);
    }

    /**
     * Get the content lines of this File. If this file doesn't exist, then null will be returned.
     * @return The content lines of this File, or null if this File doesn't exist.
     */
    public Iterable<String> getContentLines()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentLines(path);
    }

    /**
     * Get the content lines of this File. If this file doesn't exist, then null will be returned.
     * @param encoding The CharacterEncoding to use to convert this File's content to Strings.
     * @return The content lines of this File, or null if this File doesn't exist.
     */
    public Iterable<String> getContentLines(CharacterEncoding encoding)
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentLines(path, encoding);
    }

    /**
     * Get the content lines of this File. If this file doesn't exist, then null will be returned.
     * @param includeNewLineCharacters Whether or not to include the newline characters at the end
     *                                 of each line.
     * @return The content lines of this File, or null if this File doesn't exist.
     */
    public Iterable<String> getContentLines(boolean includeNewLineCharacters)
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentLines(path, includeNewLineCharacters);
    }

    /**
     * Get the content lines of this File. If this file doesn't exist, then null will be returned.
     * @param includeNewLineCharacters Whether or not to include the newline characters at the end
     *                                 of each line.
     * @param encoding The CharacterEncoding to use to convert this File's contents to Strings.
     * @return The content lines of this File, or null if this File doesn't exist.
     */
    public Iterable<String> getContentLines(boolean includeNewLineCharacters, CharacterEncoding encoding)
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentLines(path, includeNewLineCharacters, encoding);
    }

    /**
     * Get a ByteReadStream to this file's contents.
     * @return A ByteReadStream to this file's contents.
     */
    public ByteReadStream getContentByteReadStream()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentByteReadStream(path);
    }

    /**
     * Get a CharacterReadStream to this file's contents.
     * @return A CharacterReadStream to this file's contents.
     */
    public CharacterReadStream getContentCharacterReadStream()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentCharacterReadStream(path);
    }

    /**
     * Set the contents of this File to be the provided fileContents and return whether or not the
     * file's contents were set.
     * @param fileContents The contents to set the file to.
     * @return Whether or not the file's contents were set.
     */
    public boolean setContents(byte[] fileContents)
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.setFileContents(path, fileContents);
    }

    /**
     * Set the contents of this File to be the provided fileContents and return whether or not the
     * file's contents were set.
     * @param fileContents The contents to set the file to.
     * @return Whether or not the file's contents were set.
     */
    public boolean setContents(String fileContents)
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.setFileContents(path, fileContents);
    }

    /**
     * Set the contents of this File to be the provided fileContents and return whether or not the
     * file's contents were set.
     * @param fileContents The contents to set the file to.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents into bytes.
     * @return Whether or not the file's contents were set.
     */
    public boolean setContents(String fileContents, CharacterEncoding encoding)
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.setFileContents(path, fileContents, encoding);
    }
}
