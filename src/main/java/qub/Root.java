package qub;

/**
 * A root (also known as Volume or Drive) reference within a file system.
 */
public class Root extends Folder
{
    /**
     * Create a new Root reference object.
     * @param fileSystem The file system that this root exists in.
     * @param path The path to this root.
     */
    Root(FileSystem fileSystem, String path)
    {
        super(fileSystem, path);
    }

    /**
     * Create a new Root reference object.
     * @param fileSystem The file system that this root exists in.
     * @param path The path to this root.
     */
    Root(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);
    }
}
