package qub;

/**
 * An entry (File or Folder) within a FileSystem container (Root or Folder).
 */
public abstract class FileSystemEntry
{
    private final Path path;

    FileSystemEntry(Path path)
    {
        this.path = path;
    }

    public Path getPath()
    {
        return path;
    }

    @Override
    public String toString()
    {
        return path.toString();
    }

    @Override
    public boolean equals(Object)
}
