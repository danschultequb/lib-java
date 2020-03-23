package qub;

/**
 * A Root within an InMemoryFileSystem.
 */
class InMemoryRoot extends InMemoryFolder
{
    private final DataSize totalDataSize;

    /**
     * Create a new InMemoryRoot with the provided name/path.
     * @param name The name/path for the new InMemoryRoot.
     */
    public InMemoryRoot(String name, Clock clock)
    {
        this(name, clock, null);
    }

    /**
     * Create a new InMemoryRoot with the provided name/path.
     * @param name The name/path for the new InMemoryRoot.
     */
    public InMemoryRoot(String name, Clock clock, DataSize totalDataSize)
    {
        super(name, clock);

        this.totalDataSize = totalDataSize;
    }

    /**
     * Get the path to this InMemoryRoot.
     * @return The path to this InMemoryRoot.
     */
    public Path getPath()
    {
        return Path.parse(this.getName());
    }

    /**
     * Get the total data size of this InMemoryRoot object.
     * @return The total data size of this InMemoryRoot object.
     */
    public DataSize getTotalDataSize()
    {
        return this.totalDataSize;
    }

    /**
     * Get the total unused/free data size of this InMemoryRoot object.
     * @return The total unused/free data size of this InMemoryRoot object.
     */
    public DataSize getUnusedDataSize()
    {
        return this.getTotalDataSize().minus(this.getUsedDataSize());
    }
}
