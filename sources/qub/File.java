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
    public Result<Boolean> create()
    {
        final Result<File> createResult = getFileSystem().createFile(getPath());
        final Throwable error = createResult.getError();
        final boolean created = !(error instanceof FileAlreadyExistsException || error instanceof RootNotFoundException);
        return Result.done(created, error);
    }

    /**
     * Create this File and return whether or not it was created as a result of this function.
     * @return Whether or not this function created the file.
     */
    public AsyncFunction<Result<Boolean>> createAsync()
    {
        return getFileSystem().createFileAsync(getPath()).then(new Function1<Result<File>, Result<Boolean>>()
        {
            @Override
            public Result<Boolean> run(Result<File> createResult)
            {
                final Throwable error = createResult.getError();
                final boolean created = !(error instanceof FileAlreadyExistsException || error instanceof RootNotFoundException);
                return Result.done(created, error);
            }
        });
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
    public Result<Boolean> delete()
    {
        return getFileSystem().deleteFile(getPath());
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteAsync()
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
     * Get a ByteWriteStream to this file's contents.
     * @return A ByteWriteStrema to this file's contents.
     */
    public Result<ByteWriteStream> getContentByteWriteStream()
    {
        return getFileSystem().getFileContentByteWriteStream(getPath());
    }

    /**
     * Get a ByteWriteStream to this file's contents.
     * @return A ByteWriteStrema to this file's contents.
     */
    public AsyncFunction<Result<ByteWriteStream>> getContentByteWriteStreamAsync()
    {
        return getFileSystem().getFileContentByteWriteStreamAsync(getPath());
    }

    public Result<byte[]> getContents()
    {
        return getFileSystem().getFileContent(getPath());
    }

    public AsyncFunction<Result<byte[]>> getContentsAsync()
    {
        return getFileSystem().getFileContentAsync(getPath());
    }

    public Result<Boolean> setContents(byte[] content)
    {
        return getFileSystem().setFileContent(getPath(), content);
    }

    public AsyncFunction<Result<Boolean>> setContentsAsync(byte[] content)
    {
        return getFileSystem().setFileContentAsync(getPath(), content);
    }

    public Result<Void> copyTo(Path destinationPath)
    {
        return getFileSystem().copyFileTo(getPath(), destinationPath);
    }

    public AsyncFunction<Result<Void>> copyToAsync(Path destinationPath)
    {
        return getFileSystem().copyFileToAsync(getPath(), destinationPath);
    }
}
