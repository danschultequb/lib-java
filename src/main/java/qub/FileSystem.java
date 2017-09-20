package qub;

/**
 * The FileSystem class provides access to files, folders, and roots within a device's file system.
 */
public interface FileSystem
{
    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    boolean rootExists(String rootPath);

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    boolean rootExists(Path rootPath);

    /**
     * Get a reference to a Root with the provided path. The returned Root may or may not exist.
     * @param rootPath The path to the Root to return.
     * @return The Root with the provided path.
     */
    Root getRoot(String rootPath);

    /**
     * Get a reference to a Root with the provided Path. The returned Root may or may not exist.
     * @param rootPath The Path to the Root to return.
     * @return The Root with the provided Path.
     */
    Root getRoot(Path rootPath);

    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    Iterable<Root> getRoots();

    /**
     * Get the files and folders (entries) at the provided container path.
     * @param containerPath The path to the container (Root or Folder).
     * @return The files and folders (entries) at the provided container path.
     */
    Iterable<FileSystemEntry> getEntries(String containerPath);

    /**
     * Get the files and folders (entries) at the provided container Path.
     * @param containerPath The Path to the container (Root or Folder).
     * @return The files and folders (entries) at the provided container Path.
     */
    Iterable<FileSystemEntry> getEntries(Path containerPath);
}
