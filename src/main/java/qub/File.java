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

    public boolean create()
    {
        return getFileSystem().createFile(getPath());
    }

    /**
     * Get whether or not this File exists.
     */
    @Override
    public boolean exists()
    {
        return getFileSystem().fileExists(getPath());
    }
}
