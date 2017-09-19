package qub;

/**
 * A root (also known as Volume or Drive) reference within a file system.
 */
public class Root
{
    private final FileSystem fileSystem;
    private final Path path;

    /**
     * Create a new Root reference object.
     * @param fileSystem
     */
    public Root(FileSystem fileSystem, Path path)
    {
        this.fileSystem = fileSystem;
        this.path = path;
    }

    /**
     * Get the Path to this Root.
     * @return The Path to this Root.
     */
    public Path getPath()
    {
        return path;
    }

    /**
     * Get the String representation of this Root (this Root's path).
     * @return The String representation of this Root (this Root's path).
     */
    public String toString()
    {
        return path.toString();
    }
}
