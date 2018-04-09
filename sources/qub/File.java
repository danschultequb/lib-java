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
    public AsyncFunction<Result<Boolean>> create()
    {
        return getFileSystem().createFile(getPath()).then(new Function1<Result<File>, Result<Boolean>>()
        {
            @Override
            public Result<Boolean> run(Result<File> createResult)
            {
                return Result.done(!createResult.hasError(), createResult.getError());
            }
        });
    }

    /**
     * Get whether or not this File exists.
     */
    @Override
    public AsyncFunction<Result<Boolean>> exists()
    {
        return getFileSystem().fileExists(getPath());
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteAsync()
    {
        return getFileSystem().deleteFile(getPath());
    }

    /**
     * Get the date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     * @return The date and time of the most recent modification of this file, or null if this file
     * doesn't exist.
     */
    public AsyncFunction<Result<DateTime>> getLastModified()
    {
        return getFileSystem().getFileLastModified(getPath());
    }

    /**
     * Get a ByteReadStream to this file's contents.
     * @return A ByteReadStream to this file's contents.
     */
    public AsyncFunction<Result<ByteReadStream>>getContentByteReadStream()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContentByteReadStream(path);
    }

    /**
     * Get a ByteWriteStream to this file's contents.
     * @return A ByteWriteStrema to this file's contents.
     */
    public AsyncFunction<Result<ByteWriteStream>> getContentByteWriteStream()
    {
        return getFileSystem().getFileContentByteWriteStream(getPath());
    }

    public AsyncFunction<Result<byte[]>> getContents()
    {
        return getFileSystem().getFileContent(getPath());
    }

    public AsyncFunction<Result<Boolean>> setContents(byte[] content)
    {
        return getFileSystem().setFileContent(getPath(), content);
    }
}
