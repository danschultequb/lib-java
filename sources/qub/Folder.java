package qub;

/**
 * A Folder within a FileSystem.
 */
public class Folder extends FileSystemEntry
{
    /**
     * Create a new Folder reference.
     * @param fileSystem The FileSystem that contains this Folder.
     * @param path The Path to this Folder.
     */
    Folder(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);
    }

    /**
     * Get the root of this Folder.
     * @return The root of this Folder.
     */
    public Root getRoot()
    {
        return getFileSystem().getRoot(getPath()).await();
    }

    /**
     * Get the folder that contains this folder.
     * @return The result of attempting to get the parent folder that contains this folder.
     */
    public Result<Folder> getParentFolder()
    {
        return getPath().getParent()
            .thenResult((Path parentFolderPath) -> getFileSystem().getFolder(parentFolderPath));
    }

    /**
     * Get whether or not this Folder exists.
     */
    @Override
    public Result<Boolean> exists()
    {
        return getFileSystem().folderExists(getPath());
    }

    @Override
    public Result<Void> delete()
    {
        return getFileSystem().deleteFolder(getPath());
    }

    public Path relativeTo(String basePath)
    {
        PreCondition.assertNotNullAndNotEmpty(basePath, "basePath");

        return getPath().relativeTo(basePath);
    }

    public Path relativeTo(Path basePath)
    {
        PreCondition.assertNotNull(basePath, "basePath");

        return getPath().relativeTo(basePath);
    }

    public Path relativeTo(Folder folder)
    {
        PreCondition.assertNotNull(folder, "folder");

        return getPath().relativeTo(folder);
    }

    public Path relativeTo(Root root)
    {
        PreCondition.assertNotNull(root, "root");

        return getPath().relativeTo(root);
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

        return getFile(Path.parse(fileRelativePath));
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the File relative to this folder.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public Result<File> getFile(Path relativeFilePath)
    {
        PreCondition.assertNotNull(relativeFilePath, "relativeFilePath");
        PreCondition.assertFalse(relativeFilePath.isRooted(), "relativeFilePath.isRooted()");

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
        return fileSystem.getFileContentAsString(childFilePath);
    }

    /**
     * Set the contents of the file at the provided relativeFilePath.
     * @param relativeFilePath The relative path to the file.
     * @param content The String contents to set.
     * @return The result of setting the file's contents.
     */
    public Result<Void> setFileContentsAsString(String relativeFilePath, String content)
    {
        PreCondition.assertNotNullAndNotEmpty(relativeFilePath, "relativeFilePath");

        return setFileContentsAsString(Path.parse(relativeFilePath), content);
    }

    /**
     * Set the contents of the file at the provided relativeFilePath.
     * @param relativeFilePath The relative path to the file.
     * @param content The String contents to set.
     * @return The result of setting the file's contents.
     */
    public Result<Void> setFileContentsAsString(Path relativeFilePath, String content)
    {
        PreCondition.assertNotNull(relativeFilePath, "relativeFilePath");
        PreCondition.assertFalse(relativeFilePath.isRooted(), "relativeFilePath.isRooted()");
        PreCondition.assertNotNull(content, "content");

        final Path childFilePath = getChildPath(relativeFilePath);
        final FileSystem fileSystem = getFileSystem();
        return fileSystem.setFileContentAsString(childFilePath, content);
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param folderRelativePath The relative path create this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public Result<Folder> createFolder(String folderRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(folderRelativePath, "folderRelativePath");

        return createFolder(Path.parse(folderRelativePath));
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
        
        final Path childFolderPath = getChildPath(relativeFolderPath);
        final FileSystem fileSystem = getFileSystem();
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

        return createFile(Path.parse(fileRelativePath));
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param relativeFilePath The relative path create this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(Path relativeFilePath)
    {
        validateRelativeFilePath(relativeFilePath);

        final Path childFilePath = getChildPath(relativeFilePath);
        final FileSystem fileSystem = getFileSystem();
        return fileSystem.createFile(childFilePath);
    }

    public Result<Iterable<Folder>> getFolders()
    {
        return getFileSystem().getFolders(getPath());
    }

    public Result<Iterable<Folder>> getFoldersRecursively()
    {
        return getFileSystem().getFoldersRecursively(getPath());
    }

    public Result<Iterable<File>> getFiles()
    {
        return getFileSystem().getFiles(getPath());
    }

    public Result<Iterable<File>> getFilesRecursively()
    {
        return getFileSystem().getFilesRecursively(getPath());
    }

    public Result<Iterable<FileSystemEntry>> getFilesAndFolders()
    {
        return getFileSystem().getFilesAndFolders(getPath());
    }

    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively()
    {
        return getFileSystem().getFilesAndFoldersRecursively(getPath());
    }

    private Path getChildPath(Path relativePath)
    {
        return getPath().concatenateSegment(relativePath);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Folder && equals((Folder)rhs);
    }

    public boolean equals(Folder rhs)
    {
        return rhs != null &&
            Comparer.equal(getFileSystem(), rhs.getFileSystem()) &&
            getPath().equals(rhs.getPath(), false);
    }

    private static void validateRelativeFilePath(Path relativeFilePath)
    {
        PreCondition.assertNotNull(relativeFilePath, "relativeFilePath");
        PreCondition.assertFalse(relativeFilePath.isRooted(), "relativeFilePath.isRooted()");
        PreCondition.assertFalse(relativeFilePath.endsWith("/") || relativeFilePath.endsWith("\\"), "relativeFilePath.endsWith(\"/\") || relativeFilePath.endsWith(\"\\\\\")");
    }
}
