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
     * @param rootPath The path to this root.
     */
    public Root(FileSystem fileSystem, Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");
        PreCondition.assertTrue(rootPath.isRooted(), "rootPath.isRooted()");

        this.folder = new Folder(fileSystem, rootPath);
    }

    /**
     * Get the Path to this FileSystemEntry.
     */
    public Path getPath()
    {
        return this.folder.getPath();
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
        return this.folder.toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs != null && rhs instanceof Root && this.equals((Root)rhs);
    }

    public boolean equals(Root rhs)
    {
        return rhs != null && this.getPath().equals(rhs.getPath());
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
     * Get the unused/free data size of this Root.
     * @return The unused/free data size of this Root.
     */
    public Result<DataSize> getUnusedDataSize()
    {
        return this.folder.getFileSystem().getRootUnusedDataSize(this.getPath());
    }

    /**
     * Get the unused/free data size of this Root.
     * @return The unused/free data size of this Root.
     */
    public Result<DataSize> getUsedDataSize()
    {
        return this.folder.getFileSystem().getRootUsedDataSize(this.getPath());
    }

    /**
     * Get whether or not this Root exists.
     */
    public Result<Boolean> exists()
    {
        return this.folder.exists();
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param relativeFolderPath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Result<Folder> getFolder(String relativeFolderPath)
    {
        return this.folder.getFolder(relativeFolderPath);
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param relativeFolderPath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Result<Folder> getFolder(Path relativeFolderPath)
    {
        return this.folder.getFolder(relativeFolderPath);
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the file relative to this Root.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public Result<File> getFile(String relativeFilePath)
    {
        return this.folder.getFile(relativeFilePath);
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the file relative to this Root.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public Result<File> getFile(Path relativeFilePath)
    {
        return this.folder.getFile(relativeFilePath);
    }

    /**
     * Create a child folder of this Root with the provided relative path.
     * @param folderRelativePath The relative path create this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public Result<Folder> createFolder(String folderRelativePath)
    {
        return this.folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child folder of this Root with the provided relative path.
     * @param folderRelativePath The relative path create this Root to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public Result<Folder> createFolder(Path folderRelativePath)
    {
        return this.folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child file of this Root with the provided relative path.
     * @param fileRelativePath The relative path create this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(String fileRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(fileRelativePath, "fileRelativePath");

        return this.folder.createFile(fileRelativePath);
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path create this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(Path fileRelativePath)
    {
        PreCondition.assertNotNull(fileRelativePath, "fileRelativePath");

        return this.folder.createFile(fileRelativePath);
    }

    /**
     * Get an iterator that iterates through the entries (files and folders) of this folder.
     * @return An iterator that iterates through the entries (files and folders) of this folder.
     */
    public Iterator<FileSystemEntry> iterateEntries()
    {
        return this.folder.iterateEntries();
    }

    /**
     * Get an iterator that iterates through the files of this folder.
     * @return An iterator that iterates through the files of this folder.
     */
    public Iterator<File> iterateFiles()
    {
        return this.folder.iterateFiles();
    }

    /**
     * Get an iterator that iterates through the folders of this folder.
     * @return An iterator that iterates through the folders of this folder.
     */
    public Iterator<Folder> iterateFolders()
    {
        return this.folder.iterateFolders();
    }

    /**
     * Get an iterator that iterates through the entries (files and folders) of this folder
     * recursively.
     * @return An iterator that iterates through the entries (files and folders) of this folder
     * recursively.
     */
    public Iterator<FileSystemEntry> iterateEntriesRecursively()
    {
        return this.folder.iterateEntriesRecursively();
    }

    /**
     * Get an iterator that iterates through the files of this folder recursively.
     * @return An iterator that iterates through the files of this folder recursively.
     */
    public Iterator<File> iterateFilesRecursively()
    {
        return this.folder.iterateFilesRecursively();
    }

    /**
     * Get an iterator that iterates through the folders of this folder recursively.
     * @return An iterator that iterates through the folders of this folder recursively.
     */
    public Iterator<Folder> iterateFoldersRecursively()
    {
        return this.folder.iterateFoldersRecursively();
    }

    /**
     * Get an iterator that iterates through the entries (files and folders) of this folder using
     * the provided Traversal.
     * @param traversal The Traversal to use to iterate through the entries (files and folders) of
     *                  this folder.
     * @return An iterator that iterates through the entries (files and folders) of this folder.
     */
    public Iterator<FileSystemEntry> iterateEntries(Traversal<Folder,FileSystemEntry> traversal)
    {
        PreCondition.assertNotNull(traversal, "traversal");

        return this.folder.iterateEntries(traversal);
    }

    /**
     * Get an iterator that iterates through the files of this folder using the provided Traversal.
     * @param traversal The Traversal to use to iterate through the files of this folder.
     * @return An iterator that iterates through the files of this folder.
     */
    public Iterator<File> iterateFiles(Traversal<Folder,File> traversal)
    {
        PreCondition.assertNotNull(traversal, "traversal");

        return this.folder.iterateFiles(traversal);
    }

    /**
     * Get an iterator that iterates through the folders of this folder using the provided Traversal.
     * @param traversal The Traversal to use to iterate through the folders of this folder.
     * @return An iterator that iterates through the folders of this folder.
     */
    public Iterator<Folder> iterateFolders(Traversal<Folder,Folder> traversal)
    {
        PreCondition.assertNotNull(traversal, "traversal");

        return this.folder.iterateFolders(traversal);
    }
}
