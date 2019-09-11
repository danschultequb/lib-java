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
     * Get the folder that contains this file.
     * @return The result of attempting to get the parent folder that contains this file.
     */
    public Folder getParentFolder()
    {
        return getFileSystem().getFolder(getPath().getParent().await()).await();
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

    public Path relativeTo(Path path)
    {
        return getPath().relativeTo(path);
    }

    public Path relativeTo(Folder folder)
    {
        return getPath().relativeTo(folder);
    }

    public Path relativeTo(Root root)
    {
        return getPath().relativeTo(root);
    }

    /**
     * Create this File and return whether or not it was created as a result of this function.
     * @return Whether or not this function created the file.
     */
    public Result<Void> create()
    {
        return getFileSystem().createFile(getPath()).then(() -> {});
    }

    /**
     * Get whether or not this File exists.
     */
    @Override
    public Result<Boolean> exists()
    {
        return getFileSystem().fileExists(getPath());
    }

    @Override
    public Result<Void> delete()
    {
        return getFileSystem().deleteFile(getPath());
    }

    /**
     * Get the date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     * @return The date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     */
    public Result<DateTime> getLastModified()
    {
        return getFileSystem().getFileLastModified(getPath());
    }

    /**
     * Get a ByteReadStream to this file's contents.
     * @return A ByteReadStream to this file's contents.
     */
    public Result<ByteReadStream> getContentByteReadStream()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentByteReadStream(path);
    }

    /**
     * Get a CharacterReadStream to this file's contents.
     * @return A CharacterReadStream to this file's contents.
     */
    public Result<CharacterReadStream> getContentCharacterReadStream()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentCharacterReadStream(path);
    }

    /**
     * Get a ByteWriteStream to this file's contents.
     * @return A ByteWriteStream to this file's contents.
     */
    public Result<ByteWriteStream> getContentByteWriteStream()
    {
        return getFileSystem().getFileContentByteWriteStream(getPath());
    }

    /**
     * Get a CharacterWriteStream to this file's contents.
     * @return A CharacterWriteStream to this file's contents.
     */
    public Result<CharacterWriteStream> getContentCharacterWriteStream()
    {
        return getFileSystem().getFileContentCharacterWriteStream(getPath());
    }

    public Result<byte[]> getContents()
    {
        return getFileSystem().getFileContent(getPath());
    }

    public Result<String> getContentsAsString()
    {
        return getFileSystem().getFileContentAsString(getPath());
    }

    public Result<Void> setContents(byte[] content)
    {
        return getFileSystem().setFileContent(getPath(), content);
    }

    public Result<Void> setContentsAsString(String content)
    {
        return getFileSystem().setFileContentAsString(getPath(), content);
    }

    public Result<Void> copyTo(Path destinationPath)
    {
        return getFileSystem().copyFileTo(this, destinationPath);
    }

    public Result<Void> copyTo(File destinationFile)
    {
        return getFileSystem().copyFileTo(this, destinationFile);
    }

    public Result<Void> copyToFolder(Path destinationFolderPath)
    {
        return getFileSystem().copyFileToFolder(this, destinationFolderPath);
    }

    public Result<Void> copyToFolder(Folder destinationFolder)
    {
        return getFileSystem().copyFileToFolder(this, destinationFolder);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof File && equals((File)rhs);
    }

    public boolean equals(File rhs)
    {
        return rhs != null &&
            Comparer.equal(getFileSystem(), rhs.getFileSystem()) &&
            Comparer.equal(getPath(), rhs.getPath());
    }
}
