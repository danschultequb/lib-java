package qub;

public class Folder extends FileSystemEntry
{
    private final FileSystem fileSystem;

    Folder(FileSystem fileSystem, Path path)
    {
        super(path);

        this.fileSystem = fileSystem;
    }

    @Override
    public String toString()
    {
        return path.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Folder && equals((File)rhs);
    }

    public boolean equals(Folder rhs)
    {
        return rhs != null && rhs.path.equals(this.path);
    }
}
