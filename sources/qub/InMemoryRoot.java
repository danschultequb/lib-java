package qub;

/**
 * A Root within an InMemoryFileSystem.
 */
class InMemoryRoot extends InMemoryFolder
{
    /**
     * Create a new InMemoryRoot with the provided name/path.
     * @param name The name/path for the new InMemoryRoot.
     */
    public InMemoryRoot(String name, Clock clock)
    {
        super(name, clock);
    }

    /**
     * Get the path to this InMemoryRoot.
     * @return The path to this InMemoryRoot.
     */
    public Path getPath()
    {
        return Path.parse(getName());
    }
}
