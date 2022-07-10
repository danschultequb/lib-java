package qub;

/**
 * An entry (Root, Folder, or File) within a FileSystem.
 */
public abstract class FileSystemEntry
{
    private final FileSystem fileSystem;
    private final Path path;

    /**
     * Create a new FileSystemEntry.
     * @param fileSystem The FileSystem in which this FileSystemEntry is stored.
     * @param path The Path to this FileSystemEntry.
     */
    FileSystemEntry(FileSystem fileSystem, Path path)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNull(path, "path");
        PreCondition.assertTrue(path.isRooted(), "path.isRooted()");

        this.fileSystem = fileSystem;
        this.path = path;
    }

    /**
     * Get the FileSystem that contains this FileSystemEntry.
     */
    protected FileSystem getFileSystem()
    {
        return this.fileSystem;
    }

    /**
     * Get the name of this FileSystemEntry.
     * @return The name of this FileSystemEntry.
     */
    public String getName()
    {
        return this.path.getName();
    }

    /**
     * Get the Path to this FileSystemEntry.
     */
    public Path getPath()
    {
        return this.path;
    }

    /**
     * Get whether or not this entry is a descendant of the provided path.
     * @param possibleAncestorPathString The path that may be an ancestor of this entry.
     * @return Whether or not this entry is a descendant of the provided path.
     */
    public Result<Boolean> isDescendantOf(String possibleAncestorPathString)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleAncestorPathString, "possibleAncestorPathString");

        return this.isDescendantOf(Path.parse(possibleAncestorPathString));
    }

    /**
     * Get whether or not this entry is a descendant of the provided path.
     * @param possibleAncestorPath The path that may be an ancestor of this entry.
     * @return Whether or not this entry is a descendant of the provided path.
     */
    public Result<Boolean> isDescendantOf(Path possibleAncestorPath)
    {
        PreCondition.assertNotNull(possibleAncestorPath, "possibleAncestorPath");

        return this.getPath().isDescendantOf(possibleAncestorPath);
    }

    /**
     * Get whether or not this entry is a descendant of the provided folder.
     * @param possibleAncestorFolder The folder that may be an ancestor of this entry.
     * @return Whether or not this entry is a descendant of the provided folder.
     */
    public Result<Boolean> isDescendantOf(Folder possibleAncestorFolder)
    {
        PreCondition.assertNotNull(possibleAncestorFolder, "possibleAncestorFolder");

        return this.isDescendantOf(possibleAncestorFolder.getPath());
    }

    /**
     * Get whether or not this entry is a descendant of the provided root.
     * @param possibleAncestorRoot The root that may be an ancestor of this entry.
     * @return Whether or not this entry is a descendant of the provided root.
     */
    public Result<Boolean> isDescendantOf(Root possibleAncestorRoot)
    {
        PreCondition.assertNotNull(possibleAncestorRoot, "possibleAncestorRoot");

        return this.isDescendantOf(possibleAncestorRoot.getPath());
    }

    /**
     * Get the folder that contains this entry.
     * @return The result of attempting to get the parent folder that contains this entry.
     */
    public Result<Folder> getParentFolder()
    {
        return this.getPath().getParent()
            .then((Path parentFolderPath) -> this.getFileSystem().getFolder(parentFolderPath).await());
    }

    /**
     * Get the root that contains this folder.
     * @return The root that contains this entry.
     */
    public Result<Root> getRoot()
    {
        return this.getPath().getRoot()
            .then((Path rootPath) -> this.getFileSystem().getRoot(rootPath).await());
    }

    /**
     * Get whether or not this FileSystemEntry exists.
     */
    public abstract Result<Boolean> exists();

    /**
     * Attempt to delete this FileSystemEntry. Return whether or not this function deleted the
     * entry.
     * @return Whether or not this function deleted the entry.
     */
    public abstract Result<Void> delete();

    /**
     * Get the path to this FileSystemEntry relative to the provided base path.
     * @param basePath The path to use as a base path when creating the relative path to this
     *                 FileSystemEntry.
     * @return The relative path to this FileSystemEntry from the provided base path.
     */
    public Path relativeTo(String basePath)
    {
        PreCondition.assertNotNullAndNotEmpty(basePath, "basePath");

        return this.getPath().relativeTo(basePath);
    }

    /**
     * Get the path to this FileSystemEntry relative to the provided base path.
     * @param basePath The path to use as a base path when creating the relative path to this
     *                 FileSystemEntry.
     * @return The relative path to this FileSystemEntry from the provided base path.
     */
    public Path relativeTo(Path basePath)
    {
        PreCondition.assertNotNull(basePath, "basePath");

        return this.getPath().relativeTo(basePath);
    }

    /**
     * Get the path to this FileSystemEntry relative to the provided folder.
     * @param folder The folder to use as a base when creating the relative path to this
     *                 FileSystemEntry.
     * @return The relative path to this FileSystemEntry from the provided folder.
     */
    public Path relativeTo(Folder folder)
    {
        PreCondition.assertNotNull(folder, "folder");

        return this.getPath().relativeTo(folder);
    }

    /**
     * Get the path to this FileSystemEntry relative to the provided root.
     * @param root The root to use as a base when creating the relative path to this
     *                 FileSystemEntry.
     * @return The relative path to this FileSystemEntry from the provided root.
     */
    public Path relativeTo(Root root)
    {
        PreCondition.assertNotNull(root, "root");

        return this.getPath().relativeTo(root);
    }

    @Override
    public String toString()
    {
        return this.path.toString();
    }

    @Override
    public int hashCode()
    {
        return this.path.hashCode();
    }
}
