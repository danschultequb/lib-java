package qub;

/**
 * A FileSystem implementation that is completely stored in the memory of the running application.
 */
public class InMemoryFileSystem extends FileSystemBase
{
    private final ArrayList<InMemoryRoot> roots;

    /**
     * Create a new InMemoryFileSystem.
     */
    public InMemoryFileSystem()
    {
        roots = new ArrayList<>();
    }

    @Override
    public Iterable<Root> getRoots()
    {
        return Array.fromValues(roots.map(new Function1<InMemoryRoot, Root>()
        {
            @Override
            public Root run(InMemoryRoot inMemoryRoot)
            {
                return new Root(InMemoryFileSystem.this, inMemoryRoot.getPath());
            }
        }));
    }

    @Override
    public Iterable<FileSystemEntry> getEntries(Path containerPath)
    {
        return null;
    }

    /**
     * Create a new Root in this FileSystem.
     * @param rootPath The String representation of the path to the Root to create in this
     *                 FileSystem.
     */
    public void createRoot(String rootPath)
    {
        createRoot(Path.parse(rootPath));
    }

    /**
     * Create a new Root in this FileSystem.
     * @param rootPath The path to the Root to create in this FileSystem.
     */
    public void createRoot(Path rootPath)
    {
        if (rootPath != null && !rootExists(rootPath))
        {
            roots.add(new InMemoryRoot(rootPath));
        }
    }
}