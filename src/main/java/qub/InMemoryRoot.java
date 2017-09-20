package qub;

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
