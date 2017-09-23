package qub;

/**
 * An entry (Root, Folder, or File) within a FileSystem.
 */
public abstract class FileSystemEntry
{
    private final FileSystem fileSystem;
    private final Path path;

    /**
     * Create a new FileSystemEntry.
     * @param fileSystem The FileSystem in which this FileSystemEntry is stored.
     * @param path The Path to this FileSystemEntry.
     */
    FileSystemEntry(FileSystem fileSystem, String path)
    {
        this(fileSystem, Path.parse(path));
    }

    /**
     * Create a new FileSystemEntry.
     * @param fileSystem The FileSystem in which this FileSystemEntry is stored.
     * @param path The Path to this FileSystemEntry.
     */
    FileSystemEntry(FileSystem fileSystem, Path path)
    {
        this.fileSystem = fileSystem;
        this.path = path;
    }

    /**
     * Get the FileSystem that contains this FileSystemEntry.
     */
    protected FileSystem getFileSystem()
    {
        return fileSystem;
    }

    /**
     * Get the Path to this FileSystemEntry.
     */
    public Path getPath()
    {
        return path;
    }

    /**
     * Get whether or not this FileSystemEntry exists.
     */
    public abstract boolean exists();

    @Override
    public String toString()
    {
        return path.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs != null &&
                getClass().equals(rhs.getClass()) &&
                getPath().equals(((FileSystemEntry)rhs).getPath());
    }
}
