package qub;

/**
 * A Folder within a FileSystem.
 */
public class Folder extends FileSystemEntry
{
    /**
     * Create a new Folder reference.
     * @param fileSystem The FileSystem that contains this Folder.
     * @param path The Path to this Folder.
     */
    Folder(FileSystem fileSystem, String path)
    {
        super(fileSystem, path);
    }

    /**
     * Create a new Folder reference.
     * @param fileSystem The FileSystem that contains this Folder.
     * @param path The Path to this Folder.
     */
    Folder(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);
    }

    /**
     * Get whether or not this Folder exists.
     */
    @Override
    public boolean exists()
    {
        return getFileSystem().folderExists(getPath());
    }

    public Iterable<Folder> getFolders()
    {
        return getFileSystem().getFolders(getPath());
    }

    public Iterable<File> getFiles()
    {
        return getFileSystem().getFiles(getPath());
    }
}
