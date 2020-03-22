package qub;

/**
 * A root (also known as a Drive) reference within a file system.
 */
public class Root
{
    private final Folder folder;

    /**
     * Create a new Root reference object.
     * @param fileSystem The file system that this root exists in.
     * @param path The path to this root.
     */
    public Root(FileSystem fileSystem, Path path)
    {
        folder = new Folder(fileSystem, path);
    }

    /**
     * Get the Path to this FileSystemEntry.
     */
    public Path getPath()
    {
        return folder.getPath();
    }

    /**
     * Get whether or not this root is an ancestor of the provided path.
     * @param possibleDescendantPathString The path that may be a descendant of this root.
     * @return Whether or not this root is an ancestor of the provided path.
     */
    public Result<Boolean> isAncestorOf(String possibleDescendantPathString)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleDescendantPathString, "possibleDescendantPathString");

        return this.isAncestorOf(Path.parse(possibleDescendantPathString));
    }

    /**
     * Get whether or not this root is an ancestor of the provided path.
     * @param possibleDescendantPath The path that may be a descendant of this root.
     * @return Whether or not this root is an ancestor of the provided path.
     */
    public Result<Boolean> isAncestorOf(Path possibleDescendantPath)
    {
        PreCondition.assertNotNull(possibleDescendantPath, "possibleDescendantPath");

        return this.getPath().isAncestorOf(possibleDescendantPath);
    }

    /**
     * Get whether or not this root is an ancestor of the provided file system entry.
     * @param entry The file system entry that may be a descendant of this root.
     * @return Whether or not this root is an ancestor of the provided file system entry.
     */
    public Result<Boolean> isAncestorOf(FileSystemEntry entry)
    {
        PreCondition.assertNotNull(entry, "entry");

        return this.isAncestorOf(entry.getPath());
    }

    @Override
    public String toString()
    {
        return folder.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs != null && rhs instanceof Root && equals((Root)rhs);
    }

    public boolean equals(Root rhs)
    {
        return rhs != null && getPath().equals(rhs.getPath());
    }

    /**
     * Get the total data size/capacity of this Root.
     * @return The total data size/capacity of this Root.
     */
    public Result<DataSize> getTotalDataSize()
    {
        return this.folder.getFileSystem().getRootTotalDataSize(this.getPath());
    }

    /**
     * Get whether or not this Root exists.
     */
    public Result<Boolean> exists()
    {
        return folder.exists();
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param relativeFolderPath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Result<Folder> getFolder(String relativeFolderPath)
    {
        return folder.getFolder(relativeFolderPath);
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param relativeFolderPath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Result<Folder> getFolder(Path relativeFolderPath)
    {
        return folder.getFolder(relativeFolderPath);
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the file relative to this Root.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public Result<File> getFile(String relativeFilePath)
    {
        return folder.getFile(relativeFilePath);
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the file relative to this Root.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public Result<File> getFile(Path relativeFilePath)
    {
        return folder.getFile(relativeFilePath);
    }

    /**
     * Create a child folder of this Root with the provided relative path.
     * @param folderRelativePath The relative path create this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public Result<Folder> createFolder(String folderRelativePath)
    {
        return folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child folder of this Root with the provided relative path.
     * @param folderRelativePath The relative path create this Root to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public Result<Folder> createFolder(Path folderRelativePath)
    {
        return folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child file of this Root with the provided relative path.
     * @param fileRelativePath The relative path create this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(String fileRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(fileRelativePath, "fileRelativePath");

        return folder.createFile(fileRelativePath);
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path create this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(Path fileRelativePath)
    {
        PreCondition.assertNotNull(fileRelativePath, "fileRelativePath");

        return folder.createFile(fileRelativePath);
    }

    public Result<Iterable<Folder>> getFolders()
    {
        return folder.getFolders();
    }

    public Result<Iterable<Folder>> getFoldersRecursively()
    {
        return folder.getFoldersRecursively();
    }

    public Result<Iterable<File>> getFiles()
    {
        return folder.getFiles();
    }

    public Result<Iterable<File>> getFilesRecursively()
    {
        return folder.getFilesRecursively();
    }

    public Result<Iterable<FileSystemEntry>> getFilesAndFolders()
    {
        return folder.getFilesAndFolders();
    }

    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively()
    {
        return folder.getFilesAndFoldersRecursively();
    }
}
