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
     * Get whether or not this Folder exists.
     */
    @Override
    public AsyncFunction<Result<Boolean>> exists()
    {
        return getFileSystem().folderExists(getPath());
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteAsync()
    {
        return getFileSystem().deleteFolderAsync(getPath());
    }

    public Path relativeTo(Path path)
    {
        return getPath().relativeTo(path);
    }

    public Path relativeTo(Folder folder)
    {
        return getPath().relativeTo(folder);
    }

    public Path relativeTo(Root root)
    {
        return getPath().relativeTo(root);
    }

    /**
     * Try to create this folder and return whether or not this function created the folder.
     * @return Whether or not this function created the folder.
     */
    public Result<Boolean> create()
    {
        return createAsync().awaitReturn();
    }

    /**
     * Try to create this folder and return whether or not this function created the folder.
     * @return Whether or not this function created the folder.
     */
    public AsyncFunction<Result<Boolean>> createAsync()
    {
        return getFileSystem().createFolderAsync(getPath())
            .then(new Function1<Result<Folder>, Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run(Result<Folder> result)
                {
                    return Result.done(!result.hasError(), result.getError());
                }
            });
    }

    /**
     * Get whether or not the folder at the provided relativeFolderPath exists.
     * @param relativeFolderPath The relative path to the folder.
     * @return Whether or not the folder at the provided relativeFolderPath exists.
     */
    public AsyncFunction<Result<Boolean>> folderExists(String relativeFolderPath)
    {
        return folderExists(Path.parse(relativeFolderPath));
    }

    /**
     * Get whether or not the folder at the provided relativeFolderPath exists.
     * @param relativeFolderPath The relative path to the folder.
     * @return Whether or not the folder at the provided relativeFolderPath exists.
     */
    public AsyncFunction<Result<Boolean>> folderExists(Path relativeFolderPath)
    {
        AsyncFunction<Result<Boolean>> result;

        if (relativeFolderPath == null)
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("relativeFolderPath cannot be null."));
        }
        else if (relativeFolderPath.isRooted())
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("relativeFolderPath cannot be rooted."));
        }
        else
        {
            final Path childFolderPath = getChildPath(relativeFolderPath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.folderExists(childFolderPath);
        }

        return result;
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param relativeFolderPath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Result<Folder> getFolder(String relativeFolderPath)
    {
        return getFolder(Path.parse(relativeFolderPath));
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param relativeFolderPath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Result<Folder> getFolder(Path relativeFolderPath)
    {
        Result<Folder> result;

        if (relativeFolderPath == null)
        {
            result = Result.error(new IllegalArgumentException("relativeFolderPath cannot be null."));
        }
        else if (relativeFolderPath.isRooted())
        {
            result = Result.error(new IllegalArgumentException("relativeFolderPath cannot be rooted."));
        }
        else
        {
            final Path childFolderPath = getChildPath(relativeFolderPath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.getFolder(childFolderPath);
        }
        return result;
    }

    /**
     * Get whether or not the file at the provided relativeFilePath exists.
     * @param relativeFilePath The relative path to the file.
     * @return Whether or not the file at the provided relativeFilePath exists.
     */
    public AsyncFunction<Result<Boolean>> fileExists(String relativeFilePath)
    {
        return fileExists(Path.parse(relativeFilePath));
    }

    /**
     * Get whether or not the file at the provided relativeFilePath exists.
     * @param relativeFilePath The relative path to the file.
     * @return Whether or not the file at the provided relativeFilePath exists.
     */
    public AsyncFunction<Result<Boolean>> fileExists(Path relativeFilePath)
    {
        AsyncFunction<Result<Boolean>> result;

        if (relativeFilePath == null)
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("relativeFilePath cannot be null."));
        }
        else if (relativeFilePath.isRooted())
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("relativeFilePath cannot be rooted."));
        }
        else
        {
            final Path childFilePath = getChildPath(relativeFilePath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.fileExists(childFilePath);
        }

        return result;
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the file relative to this folder.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public Result<File> getFile(String relativeFilePath)
    {
        return getFile(Path.parse(relativeFilePath));
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the File relative to this folder.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public Result<File> getFile(Path relativeFilePath)
    {
        Result<File> result;

        if (relativeFilePath == null)
        {
            result = Result.error(new IllegalArgumentException("relativeFilePath cannot be null."));
        }
        else if (relativeFilePath.isRooted())
        {
            result = Result.error(new IllegalArgumentException("relativeFilePath cannot be rooted."));
        }
        else
        {
            final Path childFilePath = getChildPath(relativeFilePath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.getFile(childFilePath);
        }
        return result;
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param folderRelativePath The relative path from this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public AsyncFunction<Result<Folder>> createFolder(String folderRelativePath)
    {
        return createFolder(Path.parse(folderRelativePath));
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param relativeFolderPath The relative path from this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public AsyncFunction<Result<Folder>> createFolder(Path relativeFolderPath)
    {
        AsyncFunction<Result<Folder>> result;

        if (relativeFolderPath == null)
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("relativeFolderPath cannot be null."));
        }
        else if (relativeFolderPath.isRooted())
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("relativeFolderPath cannot be rooted."));
        }
        else
        {
            final Path childFolderPath = getChildPath(relativeFolderPath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.createFolderAsync(childFolderPath);
        }
        return result;
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param relativeFilePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(String relativeFilePath)
    {
        return createFile(Path.parse(relativeFilePath));
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param relativeFilePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(Path relativeFilePath)
    {
        return createFileAsync(relativeFilePath).awaitReturn();
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param relativeFilePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public AsyncFunction<Result<File>> createFileAsync(String relativeFilePath)
    {
        return createFileAsync(Path.parse(relativeFilePath));
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param relativeFilePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public AsyncFunction<Result<File>> createFileAsync(Path relativeFilePath)
    {
        AsyncFunction<Result<File>> result;

        if (relativeFilePath == null)
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("relativeFilePath cannot be null."));
        }
        else if (relativeFilePath.isRooted())
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("relativeFilePath cannot be rooted."));
        }
        else
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

    public AsyncFunction<Result<Iterable<File>>> getFiles()
    {
        return getFileSystem().getFiles(getPath());
    }

    public AsyncFunction<Result<Iterable<File>>> getFilesRecursively()
    {
        return getFileSystem().getFilesRecursively(getPath());
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
}
