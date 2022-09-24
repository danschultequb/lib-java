package qub;

/**
 * A {@link Root} (also known as a Volume or Drive) reference within a {@link FileSystem}.
 */
public class Root
{
    private final Folder folder;

    private Root(FileSystem fileSystem, Path rootPath)
    {
        PreCondition.assertNotNull(fileSystem, "fileSystem");
        PreCondition.assertNotNull(rootPath, "rootPath");
        PreCondition.assertTrue(rootPath.isRooted(), "rootPath.isRooted()");

        this.folder = fileSystem.getFolder(rootPath).await();
    }

    /**
     * Create a new {@link Root} reference object.
     * @param fileSystem The {@link FileSystem} that this {@link Root} exists in.
     * @param rootPath The {@link Path} to this {@link Root}.
     */
    public static Root create(FileSystem fileSystem, Path rootPath)
    {
        return new Root(fileSystem, rootPath);
    }

    /**
     * Get the {@link Path} to this {@link Root}.
     */
    public Path getPath()
    {
        return this.folder.getPath();
    }

    /**
     * Get whether this {@link Root} is an ancestor of the provided path {@link String}.
     * @param possibleDescendantPathString The path {@link String} that may be a descendant of this
     * {@link Root}.
     */
    public Result<Boolean> isAncestorOf(String possibleDescendantPathString)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleDescendantPathString, "possibleDescendantPathString");

        return this.isAncestorOf(Path.parse(possibleDescendantPathString));
    }

    /**
     * Get whether this {@link Root} is an ancestor of the provided {@link Path}.
     * @param possibleDescendantPath The {@link Path} that may be a descendant of this {@link Root}.
     */
    public Result<Boolean> isAncestorOf(Path possibleDescendantPath)
    {
        PreCondition.assertNotNull(possibleDescendantPath, "possibleDescendantPath");

        return this.getPath().isAncestorOf(possibleDescendantPath);
    }

    /**
     * Get whether this {@link Root} is an ancestor of the provided {@link FileSystemEntry}.
     * @param entry The {@link FileSystemEntry} that may be a descendant of this {@link Root}.
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
        return rhs instanceof Root &&
            this.equals((Root)rhs);
    }

    /**
     * Get whether this {@link Root} is equal to the provided {@link Root}.
     * @param rhs The {@link Root} to compare against this {@link Root}.
     */
    public boolean equals(Root rhs)
    {
        return rhs != null && this.getPath().equals(rhs.getPath());
    }

    /**
     * Get the total {@link DataSize}/capacity of this {@link Root}.
     */
    public Result<DataSize> getTotalDataSize()
    {
        return this.folder.getFileSystem().getRootTotalDataSize(this.getPath());
    }

    /**
     * Get the unused/free {@link DataSize} of this {@link Root}.
     */
    public Result<DataSize> getUnusedDataSize()
    {
        return this.folder.getFileSystem().getRootUnusedDataSize(this.getPath());
    }

    /**
     * Get the used/occupied {@link DataSize} of this {@link Root}.
     */
    public Result<DataSize> getUsedDataSize()
    {
        return this.folder.getFileSystem().getRootUsedDataSize(this.getPath());
    }

    /**
     * Get whether this {@link Root} exists.
     */
    public Result<Boolean> exists()
    {
        return this.folder.exists();
    }

    /**
     * Get a reference to the {@link Folder} at the provided relative folderPath.
     * @param relativeFolderPath The path {@link String} to the folder relative to this folder.
     */
    public Result<Folder> getFolder(String relativeFolderPath)
    {
        return this.folder.getFolder(relativeFolderPath);
    }

    /**
     * Get a reference to the {@link Folder} at the provided relative folderPath.
     * @param relativeFolderPath The {@link Path} to the folder relative to this folder.
     */
    public Result<Folder> getFolder(Path relativeFolderPath)
    {
        return this.folder.getFolder(relativeFolderPath);
    }

    /**
     * Get a reference to the {@link File} at the provided relativeFilePath.
     * @param relativeFilePath The path {@link String} to the file relative to this {@link Root}.
     */
    public Result<File> getFile(String relativeFilePath)
    {
        return this.folder.getFile(relativeFilePath);
    }

    /**
     * Get a reference to the {@link File} at the provided relativeFilePath.
     * @param relativeFilePath The {@link Path} to the {@link File} relative to this {@link Root}.
     */
    public Result<File> getFile(Path relativeFilePath)
    {
        return this.folder.getFile(relativeFilePath);
    }

    /**
     * Create a child {@link Folder} of this {@link Root} with the provided relative path
     * {@link String}.
     * @param folderRelativePath The relative path from this {@link Root} to the child
     * {@link Folder} to create.
     */
    public Result<Folder> createFolder(String folderRelativePath)
    {
        return this.folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child {@link Folder} of this {@link Root} with the provided relative {@link Path}.
     * @param folderRelativePath The relative {@link Path} from this {@link Root} to the child
     * {@link Folder} to create.
     */
    public Result<Folder> createFolder(Path folderRelativePath)
    {
        return this.folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child {@link File} of this {@link Root} with the provided relative path
     * {@link String}.
     * @param fileRelativePath The relative path {@link String} from this {@link Root} to the child
     *                         {@link File} to create.
     */
    public Result<File> createFile(String fileRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(fileRelativePath, "fileRelativePath");

        return this.folder.createFile(fileRelativePath);
    }

    /**
     * Create a child {@link File} of this {@link Root} with the provided relative {@link Path}.
     * @param fileRelativePath The relative {@link Path} from this {@link Root} to the child
     * {@link File} to create.
     */
    public Result<File> createFile(Path fileRelativePath)
    {
        PreCondition.assertNotNull(fileRelativePath, "fileRelativePath");

        return this.folder.createFile(fileRelativePath);
    }

    /**
     * Get an {@link Iterator} that iterates through the {@link FileSystemEntry}s ({@link File}s and
     * {@link Folder}s) of this {@link Root}.
     */
    public Iterator<FileSystemEntry> iterateEntries()
    {
        return this.folder.iterateEntries();
    }

    /**
     * Get an {@link Iterator} that iterates through the {@link File}s of this {@link Root}.
     */
    public Iterator<File> iterateFiles()
    {
        return this.folder.iterateFiles();
    }

    /**
     * Get an {@link Iterator} that iterates through the {@link Folder}s of this {@link Root}.
     */
    public Iterator<Folder> iterateFolders()
    {
        return this.folder.iterateFolders();
    }

    /**
     * Get an {@link Iterator} that iterates through the {@link FileSystemEntry}s ({@link File}s
     * and {@link Folder}s) of this {@link Root} recursively.
     */
    public Iterator<FileSystemEntry> iterateEntriesRecursively()
    {
        return this.folder.iterateEntriesRecursively();
    }

    /**
     * Get an {@link Iterator} that iterates through the {@link File}s of this {@link Root}
     * recursively.
     */
    public Iterator<File> iterateFilesRecursively()
    {
        return this.folder.iterateFilesRecursively();
    }

    /**
     * Get an {@link Iterator} that iterates through the {@link Folder}s of this {@link Root}
     * recursively.
     */
    public Iterator<Folder> iterateFoldersRecursively()
    {
        return this.folder.iterateFoldersRecursively();
    }

    /**
     * Get an {@link Iterator} that iterates through the {@link FileSystemEntry}s ({@link File}s and
     * {@link Folder}s) of this {@link Root} using the provided {@link Traversal}.
     * @param traversal The {@link Traversal} to use to iterate through the {@link FileSystemEntry}s
     *                  ({@link File}s and {@link Folder}s) of this {@link Root}.
     */
    public Iterator<FileSystemEntry> iterateEntries(Traversal<Folder,FileSystemEntry> traversal)
    {
        PreCondition.assertNotNull(traversal, "traversal");

        return this.folder.iterateEntries(traversal);
    }

    /**
     * Get an {@link Iterator} that iterates through the {@link File}s of this {@link Root} using
     * the provided {@link Traversal}.
     * @param traversal The {@link Traversal} to use to iterate through the {@link File}s of this
     * {@link Root}.
     */
    public Iterator<File> iterateFiles(Traversal<Folder,File> traversal)
    {
        PreCondition.assertNotNull(traversal, "traversal");

        return this.folder.iterateFiles(traversal);
    }

    /**
     * Get an {@link Iterator} that iterates through the {@link Folder}s of this {@link Root} using
     * the provided {@link Traversal}.
     * @param traversal The {@link Traversal} to use to iterate through the {@link Folder}s of this
     * {@link Root}.
     */
    public Iterator<Folder> iterateFolders(Traversal<Folder,Folder> traversal)
    {
        PreCondition.assertNotNull(traversal, "traversal");

        return this.folder.iterateFolders(traversal);
    }
}
