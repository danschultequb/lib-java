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
        return getFileSystem().getFolder(getPath().getParent()).throwErrorOrGetValue();
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
     * Create this File and return whether or not it was created as a result of this function.
     * @return Whether or not this function created the file.
     */
    public AsyncFunction<Result<Void>> createAsync()
    {
        return getFileSystem().createFileAsync(getPath())
            .then((Result<File> createResult) -> createResult.then(() -> {}));
    }

    /**
     * Get whether or not this File exists.
     */
    @Override
    public Result<Boolean> exists()
    {
        return getFileSystem().fileExists(getPath());
    }

    /**
     * Get whether or not this File exists.
     */
    @Override
    public AsyncFunction<Result<Boolean>> existsAsync()
    {
        return getFileSystem().fileExistsAsync(getPath());
    }

    @Override
    public Result<Void> delete()
    {
        return getFileSystem().deleteFile(getPath());
    }

    @Override
    public AsyncFunction<Result<Void>> deleteAsync()
    {
        return getFileSystem().deleteFileAsync(getPath());
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
     * Get the date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     * @return The date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     */
    public AsyncFunction<Result<DateTime>> getLastModifiedAsync()
    {
        return getFileSystem().getFileLastModifiedAsync(getPath());
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
     * Get a ByteReadStream to this file's contents.
     * @return A ByteReadStream to this file's contents.
     */
    public AsyncFunction<Result<ByteReadStream>> getContentByteReadStreamAsync()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentByteReadStreamAsync(path);
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
     * Get a CharacterReadStream to this file's contents.
     * @return A CharacterReadStream to this file's contents.
     */
    public AsyncFunction<Result<CharacterReadStream>> getContentCharacterReadStreamAsync()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentCharacterReadStreamAsync(path);
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
     * Get a ByteWriteStream to this file's contents.
     * @return A ByteWriteStream to this file's contents.
     */
    public AsyncFunction<Result<ByteWriteStream>> getContentByteWriteStreamAsync()
    {
        return getFileSystem().getFileContentByteWriteStreamAsync(getPath());
    }

    /**
     * Get a CharacterWriteStream to this file's contents.
     * @return A CharacterWriteStream to this file's contents.
     */
    public Result<CharacterWriteStream> getContentCharacterWriteStream()
    {
        return getFileSystem().getFileContentCharacterWriteStream(getPath());
    }

    /**
     * Get a CharacterWriteStream to this file's contents.
     * @return A CharacterWriteStream to this file's contents.
     */
    public AsyncFunction<Result<CharacterWriteStream>> getContentCharacterWriteStreamAsync()
    {
        return getFileSystem().getFileContentCharacterWriteStreamAsync(getPath());
    }

    public Result<byte[]> getContents()
    {
        return getFileSystem().getFileContent(getPath());
    }

    public AsyncFunction<Result<byte[]>> getContentsAsync()
    {
        return getFileSystem().getFileContentAsync(getPath());
    }

    public Result<String> getContentsAsString()
    {
        return getFileSystem().getFileContentAsString(getPath());
    }

    public AsyncFunction<Result<String>> getContentsAsStringAsync()
    {
        return getFileSystem().getFileContentAsStringAsync(getPath());
    }

    public Result<Void> setContents(byte[] content)
    {
        return getFileSystem().setFileContent(getPath(), content);
    }

    public AsyncFunction<Result<Void>> setContentsAsync(byte[] content)
    {
        return getFileSystem().setFileContentAsync(getPath(), content);
    }

    public Result<Void> setContentsAsString(String content)
    {
        return getFileSystem().setFileContentAsString(getPath(), content);
    }

    public Result<Void> copyTo(Path destinationPath)
    {
        return getFileSystem().copyFileTo(getPath(), destinationPath);
    }

    public AsyncFunction<Result<Void>> copyToAsync(Path destinationPath)
    {
        return getFileSystem().copyFileToAsync(getPath(), destinationPath);
    }

    public Result<Void> copyTo(File destinationFile)
    {
        return getFileSystem().copyFileTo(getPath(), destinationFile.getPath());
    }

    public AsyncFunction<Result<Void>> copyToAsync(File destinationFile)
    {
        return getFileSystem().copyFileToAsync(getPath(), destinationFile.getPath());
    }
}
