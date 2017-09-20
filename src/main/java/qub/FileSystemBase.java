package qub;

/**
 * An abstract base FileSystem class that contains common methods among FileSystem implementations.
 */
public abstract class FileSystemBase implements FileSystem
{
    @Override
    public boolean rootExists(String rootPath)
    {
        return rootExists(Path.parse(rootPath));
    }

    @Override
    public boolean rootExists(final Path rootPath)
    {
        return getRoots().any(new Function1<Root, Boolean>()
        {
            @Override
            public Boolean run(Root root)
            {
                return root.getPath().equals(rootPath);
            }
        });
    }

    @Override
    public Root getRoot(String rootPath)
    {
        return getRoot(Path.parse(rootPath));
    }

    @Override
    public Root getRoot(Path rootPath)
    {
        return new Root(this, rootPath);
    }

    @Override
    public Iterable<FileSystemEntry> getEntries(String containerPath)
    {
        return getEntries(Path.parse(containerPath));
    }
}
