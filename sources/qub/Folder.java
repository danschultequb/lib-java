package qub;

/**
 * A Folder within a FileSystem.
 */
public class Folder extends FileSystemEntry
{
    /**
     * Create a new Folder reference.
     * @param fileSystem The FileSystem that contains this Folder.
     * @param folderPath The Path to this Folder.
     */
    public Folder(FileSystem fileSystem, Path folderPath)
    {
        super(fileSystem, Folder.normalizeFolderPath(folderPath));
    }

    private static Path normalizeFolderPath(Path folderPath)
    {
        PreCondition.assertNotNull(folderPath, "folderPath");
        PreCondition.assertTrue(folderPath.isRooted(), "folderPath.isRooted()");

        folderPath = folderPath.normalize();
        if (!folderPath.endsWith('/'))
        {
            folderPath = Path.parse(folderPath.toString() + '/');
        }
        return folderPath;
    }

    /**
     * Get whether or not this Folder exists.
     */
    @Override
    public Result<Boolean> exists()
    {
        return this.getFileSystem().folderExists(this.getPath());
    }

    @Override
    public Result<Void> delete()
    {
        return this.getFileSystem().deleteFolder(this.getPath());
    }

    /**
     * Try to create this folder and return whether or not this function created the folder.
     * @return Whether or not this function created the folder.
     */
    public Result<Void> create()
    {
        return getFileSystem().createFolder(getPath()).then(() -> {});
    }

    /**
     * Get whether or not the folder at the provided relativeFolderPath exists.
     * @param relativeFolderPath The relative path to the folder.
     * @return Whether or not the folder at the provided relativeFolderPath exists.
     */
    public Result<Boolean> folderExists(String relativeFolderPath)
    {
        return folderExists(Path.parse(relativeFolderPath));
    }

    /**
     * Get whether or not the folder at the provided relativeFolderPath exists.
     * @param relativeFolderPath The relative path to the folder.
     * @return Whether or not the folder at the provided relativeFolderPath exists.
     */
    public Result<Boolean> folderExists(Path relativeFolderPath)
    {
        PreCondition.assertNotNull(relativeFolderPath, "relativeFolderPath");
        PreCondition.assertFalse(relativeFolderPath.isRooted(), "relativeFolderPath.isRooted()");

        final Path childFolderPath = getChildPath(relativeFolderPath);
        final FileSystem fileSystem = getFileSystem();
        return fileSystem.folderExists(childFolderPath);
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param folderRelativePath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Result<Folder> getFolder(String folderRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(folderRelativePath, "folderRelativePath");

        return getFolder(Path.parse(folderRelativePath));
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param folderRelativePath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Result<Folder> getFolder(Path folderRelativePath)
    {
        PreCondition.assertNotNull(folderRelativePath, "folderRelativePath");
        PreCondition.assertFalse(folderRelativePath.isRooted(), "folderRelativePath.isRooted()");

        final Path childFolderPath = getChildPath(folderRelativePath);
        final FileSystem fileSystem = getFileSystem();
        return fileSystem.getFolder(childFolderPath);
    }

    /**
     * Get whether or not the file at the provided relativeFilePath exists.
     * @param relativeFilePath The relative path to the file.
     * @return Whether or not the file at the provided relativeFilePath exists.
     */
    public Result<Boolean> fileExists(String relativeFilePath)
    {
        return fileExists(Path.parse(relativeFilePath));
    }

    /**
     * Get whether or not the file at the provided relativeFilePath exists.
     * @param relativeFilePath The relative path to the file.
     * @return Whether or not the file at the provided relativeFilePath exists.
     */
    public Result<Boolean> fileExists(Path relativeFilePath)
    {
        validateRelativeFilePath(relativeFilePath);

        final Path childFilePath = getChildPath(relativeFilePath);
        final FileSystem fileSystem = getFileSystem();
        return fileSystem.fileExists(childFilePath);
    }

    /**
     * Get a reference to the File at the provided fileRelativePath.
     * @param fileRelativePath The path to the file relative to this folder.
     * @return A reference to the File at the provided fileRelativePath.
     */
    public Result<File> getFile(String fileRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(fileRelativePath, "fileRelativePath");

        return this.getFile(Path.parse(fileRelativePath));
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the File relative to this folder.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public Result<File> getFile(Path relativeFilePath)
    {
        PreCondition.assertNotNull(relativeFilePath, "relativeFilePath");
        PreCondition.assertFalse(relativeFilePath.isRooted(), "relativeFilePath.isRooted() (" + relativeFilePath + ")");

        final Path childFilePath = getChildPath(relativeFilePath);
        final FileSystem fileSystem = getFileSystem();
        return fileSystem.getFile(childFilePath);
    }

    /**
     * Get the contents of the file at the provided relativeFilePath.
     * @param relativeFilePath The relative file path to the file.
     * @return The String contents of the file at the provided relativeFilePath.
     */
    public Result<String> getFileContentsAsString(String relativeFilePath)
    {
        PreCondition.assertNotNullAndNotEmpty(relativeFilePath, "relativeFilePath");

        return getFileContentsAsString(Path.parse(relativeFilePath));
    }

    /**
     * Get the contents of the file at the provided relativeFilePath.
     * @param relativeFilePath The relative file path to the file.
     * @return The String contents of the file at the provided relativeFilePath.
     */
    public Result<String> getFileContentsAsString(Path relativeFilePath)
    {
        PreCondition.assertNotNull(relativeFilePath, "relativeFilePath");
        PreCondition.assertFalse(relativeFilePath.isRooted(), "relativeFilePath.isRooted()");

        final Path childFilePath = getChildPath(relativeFilePath);
        final FileSystem fileSystem = getFileSystem();
        return fileSystem.getFileContentsAsString(childFilePath);
    }

    /**
     * Set the contents of the file at the provided relativeFilePath.
     * @param relativeFilePath The relative path to the file.
     * @param contents The String contents to set.
     * @return The result of setting the file's contents.
     */
    public Result<File> setFileContentsAsString(String relativeFilePath, String contents)
    {
        PreCondition.assertNotNullAndNotEmpty(relativeFilePath, "relativeFilePath");

        return setFileContentsAsString(Path.parse(relativeFilePath), contents);
    }

    /**
     * Set the contents of the file at the provided relativeFilePath.
     * @param relativeFilePath The relative path to the file.
     * @param contents The String contents to set.
     * @return The result of setting the file's contents.
     */
    public Result<File> setFileContentsAsString(Path relativeFilePath, String contents)
    {
        PreCondition.assertNotNull(relativeFilePath, "relativeFilePath");
        PreCondition.assertFalse(relativeFilePath.isRooted(), "relativeFilePath.isRooted()");
        PreCondition.assertNotNull(contents, "contents");

        final Path childFilePath = this.getChildPath(relativeFilePath);
        final FileSystem fileSystem = this.getFileSystem();
        return fileSystem.setFileContentsAsString(childFilePath, contents);
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param folderRelativePath The relative path create this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public Result<Folder> createFolder(String folderRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(folderRelativePath, "folderRelativePath");

        return this.createFolder(Path.parse(folderRelativePath));
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param relativeFolderPath The relative path create this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public Result<Folder> createFolder(Path relativeFolderPath)
    {
        PreCondition.assertNotNull(relativeFolderPath, "relativeFolderPath");
        PreCondition.assertFalse(relativeFolderPath.isRooted(), "relativeFolderPath.isRooted()");
        
        final Path childFolderPath = this.getChildPath(relativeFolderPath);
        final FileSystem fileSystem = this.getFileSystem();
        return fileSystem.createFolder(childFolderPath);
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path create this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(String fileRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(fileRelativePath, "fileRelativePath");

        return this.createFile(Path.parse(fileRelativePath));
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param relativeFilePath The relative path create this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(Path relativeFilePath)
    {
        Folder.validateRelativeFilePath(relativeFilePath);

        final Path childFilePath = this.getChildPath(relativeFilePath);
        final FileSystem fileSystem = this.getFileSystem();
        return fileSystem.createFile(childFilePath);
    }

    public Result<Iterable<Folder>> getFolders()
    {
        return this.getFileSystem().getFolders(getPath());
    }

    public Result<Iterable<Folder>> getFoldersRecursively()
    {
        return this.getFileSystem().getFoldersRecursively(getPath());
    }

    public Result<Iterable<File>> getFiles()
    {
        return this.getFileSystem().getFiles(getPath());
    }

    public Result<Iterable<File>> getFilesRecursively()
    {
        return this.getFileSystem().getFilesRecursively(getPath());
    }

    public Result<Iterable<FileSystemEntry>> getFilesAndFolders()
    {
        return this.getFileSystem().getFilesAndFolders(getPath());
    }

    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively()
    {
        return this.getFileSystem().getFilesAndFoldersRecursively(getPath());
    }

    /**
     * Get whether or not this folder is an ancestor of the provided path.
     * @param possibleDescendantPathString The path that may be a descendant of this folder.
     * @return Whether or not this folder is an ancestor of the provided path.
     */
    public Result<Boolean> isAncestorOf(String possibleDescendantPathString)
    {
        PreCondition.assertNotNullAndNotEmpty(possibleDescendantPathString, "possibleDescendantPathString");

        return this.isAncestorOf(Path.parse(possibleDescendantPathString));
    }

    /**
     * Get whether or not this folder is an ancestor of the provided path.
     * @param possibleDescendantPath The path that may be a descendant of this folder.
     * @return Whether or not this folder is an ancestor of the provided path.
     */
    public Result<Boolean> isAncestorOf(Path possibleDescendantPath)
    {
        PreCondition.assertNotNull(possibleDescendantPath, "possibleDescendantPath");

        return this.getPath().isAncestorOf(possibleDescendantPath);
    }

    /**
     * Get whether or not this folder is an ancestor of the provided file system entry.
     * @param entry The file system entry that may be a descendant of this folder.
     * @return Whether or not this folder is an ancestor of the provided file system entry.
     */
    public Result<Boolean> isAncestorOf(FileSystemEntry entry)
    {
        PreCondition.assertNotNull(entry, "entry");

        return this.isAncestorOf(entry.getPath());
    }

    private Path getChildPath(Path relativePath)
    {
        return this.getPath().concatenateSegment(relativePath);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Folder && this.equals((Folder)rhs);
    }

    public boolean equals(Folder rhs)
    {
        return rhs != null &&
            Comparer.equal(this.getFileSystem(), rhs.getFileSystem()) &&
            this.getPath().equals(rhs.getPath(), false);
    }

    private static void validateRelativeFilePath(Path relativeFilePath)
    {
        PreCondition.assertNotNull(relativeFilePath, "relativeFilePath");
        PreCondition.assertFalse(relativeFilePath.isRooted(), "relativeFilePath.isRooted()");
        PreCondition.assertFalse(relativeFilePath.endsWith("/") || relativeFilePath.endsWith("\\"), "relativeFilePath.endsWith(\"/\") || relativeFilePath.endsWith(\"\\\\\")");
    }
}
