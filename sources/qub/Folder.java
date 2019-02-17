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
        return getFileSystem().getRoot(getPath()).throwErrorOrGetValue();
    }

    /**
     * Get the folder that contains this folder.
     * @return The result of attempting to get the parent folder that contains this folder.
     */
    public Result<Folder> getParentFolder()
    {
        final Path parentFolderPath = getPath().getParent();
        return parentFolderPath == null
            ? Result.error(new NotFoundException("The path " + Strings.escapeAndQuote(this.toString()) + " has no parent folder."))
            : getFileSystem().getFolder(parentFolderPath);
    }

    /**
     * Get whether or not this Folder exists.
     */
    @Override
    public Result<Boolean> exists()
    {
        return getFileSystem().folderExists(getPath());
    }

    /**
     * Get whether or not this Folder exists.
     */
    @Override
    public AsyncFunction<Result<Boolean>> existsAsync()
    {
        return getFileSystem().folderExistsAsync(getPath());
    }

    @Override
    public Result<Void> delete()
    {
        return getFileSystem().deleteFolder(getPath());
    }

    @Override
    public AsyncFunction<Result<Void>> deleteAsync()
    {
        return getFileSystem().deleteFolderAsync(getPath());
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
     * Try to create this folder and return whether or not this function created the folder.
     * @return Whether or not this function created the folder.
     */
    public AsyncFunction<Result<Void>> createAsync()
    {
        return getFileSystem().createFolderAsync(getPath())
            .then((Result<Folder> result) -> result.then(() -> {}));
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
     * Get whether or not the folder at the provided relativeFolderPath exists.
     * @param relativeFolderPath The relative path to the folder.
     * @return Whether or not the folder at the provided relativeFolderPath exists.
     */
    public AsyncFunction<Result<Boolean>> folderExistsAsync(String relativeFolderPath)
    {
        return folderExistsAsync(Path.parse(relativeFolderPath));
    }

    /**
     * Get whether or not the folder at the provided relativeFolderPath exists.
     * @param relativeFolderPath The relative path to the folder.
     * @return Whether or not the folder at the provided relativeFolderPath exists.
     */
    public AsyncFunction<Result<Boolean>> folderExistsAsync(Path relativeFolderPath)
    {
        AsyncFunction<Result<Boolean>> result = validateRelativeFolderPathAsync(relativeFolderPath);
        if (result == null)
        {
            final Path childFolderPath = getChildPath(relativeFolderPath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.folderExistsAsync(childFolderPath);
        }
        return result;
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
        Result<Boolean> result = validateRelativeFilePath(relativeFilePath);
        if (result == null)
        {
            final Path childFilePath = getChildPath(relativeFilePath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.fileExists(childFilePath);
        }

        return result;
    }

    /**
     * Get whether or not the file at the provided relativeFilePath exists.
     * @param relativeFilePath The relative path to the file.
     * @return Whether or not the file at the provided relativeFilePath exists.
     */
    public AsyncFunction<Result<Boolean>> fileExistsAsync(String relativeFilePath)
    {
        return fileExistsAsync(Path.parse(relativeFilePath));
    }

    /**
     * Get whether or not the file at the provided relativeFilePath exists.
     * @param relativeFilePath The relative path to the file.
     * @return Whether or not the file at the provided relativeFilePath exists.
     */
    public AsyncFunction<Result<Boolean>> fileExistsAsync(Path relativeFilePath)
    {
        AsyncFunction<Result<Boolean>> result = validateRelativeFilePathAsync(relativeFilePath);
        if (result == null)
        {
            final Path childFilePath = getChildPath(relativeFilePath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.fileExistsAsync(childFilePath);
        }

        return result;
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
     * Create a child folder of this folder with the provided relative path.
     * @param folderRelativePath The relative path create this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public AsyncFunction<Result<Folder>> createFolderAsync(String folderRelativePath)
    {
        PreCondition.assertNotNullAndNotEmpty(folderRelativePath, "folderRelativePath");

        return createFolderAsync(Path.parse(folderRelativePath));
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param relativeFolderPath The relative path create this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public AsyncFunction<Result<Folder>> createFolderAsync(Path relativeFolderPath)
    {
        AsyncFunction<Result<Folder>> result = validateRelativeFolderPathAsync(relativeFolderPath);
        if (result == null)
        {
            final Path childFolderPath = getChildPath(relativeFolderPath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.createFolderAsync(childFolderPath);
        }
        return result;
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
        Result<File> result = validateRelativeFilePath(relativeFilePath);
        if (result == null)
        {
            final Path childFilePath = getChildPath(relativeFilePath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.createFile(childFilePath);
        }
        return result;
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param relativeFilePath The relative path create this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public AsyncFunction<Result<File>> createFileAsync(String relativeFilePath)
    {
        return createFileAsync(Path.parse(relativeFilePath));
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param relativeFilePath The relative path create this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public AsyncFunction<Result<File>> createFileAsync(Path relativeFilePath)
    {
        AsyncFunction<Result<File>> result = validateRelativeFilePathAsync(relativeFilePath);
        if (result == null)
        {
            final Path childFilePath = getChildPath(relativeFilePath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.createFileAsync(childFilePath);
        }
        return result;
    }

    public Result<Iterable<Folder>> getFolders()
    {
        return getFileSystem().getFolders(getPath());
    }

    public AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync()
    {
        return getFileSystem().getFoldersAsync(getPath());
    }

    public Result<Iterable<Folder>> getFoldersRecursively()
    {
        return getFileSystem().getFoldersRecursively(getPath());
    }

    public AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync()
    {
        return getFileSystem().getFoldersRecursivelyAsync(getPath());
    }

    public Result<Iterable<File>> getFiles()
    {
        return getFileSystem().getFiles(getPath());
    }

    public AsyncFunction<Result<Iterable<File>>> getFilesAsync()
    {
        return getFileSystem().getFilesAsync(getPath());
    }

    public Result<Iterable<File>> getFilesRecursively()
    {
        return getFileSystem().getFilesRecursively(getPath());
    }

    public AsyncFunction<Result<Iterable<File>>> getFilesRecursivelyAsync()
    {
        return getFileSystem().getFilesRecursivelyAsync(getPath());
    }

    public Result<Iterable<FileSystemEntry>> getFilesAndFolders()
    {
        return getFileSystem().getFilesAndFolders(getPath());
    }

    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync()
    {
        return getFileSystem().getFilesAndFoldersAsync(getPath());
    }

    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively()
    {
        return getFileSystem().getFilesAndFoldersRecursively(getPath());
    }

    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync()
    {
        return getFileSystem().getFilesAndFoldersRecursivelyAsync(getPath());
    }

    private Path getChildPath(Path relativePath)
    {
        return getPath().concatenateSegment(relativePath);
    }

    private static <T> Result<T> validateRelativeFolderPath(Path relativeFolderPath)
    {
        Result<T> result = Result.notNull(relativeFolderPath, "relativeFolderPath");
        if (result == null)
        {
            if (relativeFolderPath.isRooted())
            {
                result = Result.error(new IllegalArgumentException("relativeFolderPath cannot be rooted."));
            }
        };
        return result;
    }

    private static <T> AsyncFunction<Result<T>> validateRelativeFolderPathAsync(Path relativeFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = validateRelativeFolderPath(relativeFolderPath);
        return result == null ? null : currentAsyncRunner.done(result);
    }

    private static <T> Result<T> validateRelativeFilePath(Path relativeFilePath)
    {
        Result<T> result = Result.notNull(relativeFilePath, "relativeFilePath");
        if (result == null)
        {
            if (relativeFilePath.isRooted())
            {
                result = Result.error(new IllegalArgumentException("relativeFilePath cannot be rooted."));
            }
            else if (relativeFilePath.endsWith("/") || relativeFilePath.endsWith("\\"))
            {
                result = Result.error(new IllegalArgumentException("relativeFilePath cannot end with '/' or '\\'."));
            }
        };
        return result;
    }

    private static <T> AsyncFunction<Result<T>> validateRelativeFilePathAsync(Path relativeFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = validateRelativeFilePath(relativeFilePath);
        return result == null ? null : currentAsyncRunner.done(result);
    }
}
