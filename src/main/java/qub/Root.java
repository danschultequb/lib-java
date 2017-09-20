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
     * @param fileSystem The file system that this root exists in.
     * @param path The path to this root.
     */
    public Root(FileSystem fileSystem, String path)
    {
        this(fileSystem, Path.parse(path));
    }

    /**
     * Create a new Root reference object.
     * @param fileSystem The file system that this root exists in.
     * @param path The path to this root.
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
    @Override
    public String toString()
    {
        return path.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Root && equals((Root)rhs);
    }

    /**
     * Get whether or not this Root references the same root as the provided Root.
     * @param rhs The Root to compare to this Root.
     * @return Whether or not this Root references the same root as the provided Root.
     */
    public boolean equals(Root rhs)
    {
        return rhs != null && rhs.fileSystem.equals(fileSystem) && rhs.path.equals(path);
    }

    /**
     * Get whether or not this Root exists in its file system.
     * @return Whether or not this Root exists in its file system.
     */
    public boolean exists()
    {
        return fileSystem.rootExists(path);
    }
}
