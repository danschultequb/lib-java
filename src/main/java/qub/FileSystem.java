package qub;

/**
 * The FileSystem class provides access to files, folders, and roots within a device's file system.
 */
public interface FileSystem
{
    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    Iterable<Root> getRoots();
}
