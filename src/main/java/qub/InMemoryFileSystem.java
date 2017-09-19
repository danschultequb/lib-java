package qub;

/**
 * A FileSystem implementation that is completely stored in the memory of the running application.
 */
public class InMemoryFileSystem implements FileSystem
{
    private final ArrayList<InMemoryRoot> roots;

    /**
     * Create a new InMemoryFileSystem.
     */
    public InMemoryFileSystem()
    {
        roots = new ArrayList<>();
    }

    /**
     * Add a new Root to this FileSystem.
     * @param rootPath The String representation of the path to the Root to add to this FileSystem.
     */
    public void addRoot(String rootPath)
    {
        addRoot(Path.parse(rootPath));
    }

    /**
     * Add a new Root to this FileSystem.
     * @param rootPath The path to the Root to add to this FileSystem.
     */
    public void addRoot(final Path rootPath)
    {
        if (rootPath != null && !roots.any(new Function1<InMemoryRoot,Boolean>() {
            @Override
            public Boolean run(InMemoryRoot root)
            {
                return root.getPath().equals(rootPath);
            }
        }))
        {
            roots.add(new InMemoryRoot(rootPath));
        }
    }

    @Override
    public Iterable<Root> getRoots()
    {
        final Iterable<Root> mappedRoots = roots.map(new Function1<InMemoryRoot, Root>()
        {
            @Override
            public Root run(InMemoryRoot inMemoryRoot)
            {
                return new Root(InMemoryFileSystem.this, inMemoryRoot.getPath());
            }
        });
        return Array.fromValues(mappedRoots);
    }
}

interface InMemoryContainer
{

}

class InMemoryRoot implements InMemoryContainer
{
    private final Path path;

    public InMemoryRoot(Path path)
    {
        this.path = path;
    }

    public Path getPath()
    {
        return path;
    }
}

class InMemoryFolder implements InMemoryContainer
{

}

class InMemoryFile
{

}