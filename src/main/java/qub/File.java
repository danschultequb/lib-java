package qub;

public class File implements FileSystemEntry
{
    private final FileSystem fileSystem;
    private final Path path;

    File(FileSystem fileSystem, Path path)
    {
        this.fileSystem = fileSystem;
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
    public boolean equals(Object rhs)
    {
        return rhs instanceof File && equals((File)rhs);
    }

    public boolean equals(File rhs)
    {
        return rhs != null && rhs.path.equals(this.path);
    }
}
