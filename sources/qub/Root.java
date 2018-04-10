package qub;

/**
 * A root (also known as Volume or Drive) reference within a file system.
 */
public class Root
{
    private final Folder folder;

    /**
     * Create a new Root reference object.
     * @param fileSystem The file system that this root exists in.
     * @param path The path to this root.
     */
    Root(FileSystem fileSystem, Path path)
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
     * Get whether or not this Folder exists.
     */
    public AsyncFunction<Result<Boolean>> exists()
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
     * @param folderRelativePath The relative path from this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public AsyncFunction<Result<Folder>> createFolder(String folderRelativePath)
    {
        return folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child folder of this Root with the provided relative path.
     * @param folderRelativePath The relative path from this Root to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public AsyncFunction<Result<Folder>> createFolder(Path folderRelativePath)
    {
        return folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child file of this Root with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(String fileRelativePath)
    {
        return folder.createFile(fileRelativePath);
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public Result<File> createFile(Path fileRelativePath)
    {
        return folder.createFile(fileRelativePath);
    }

    /**
     * Create a child file of this Root with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public AsyncFunction<Result<File>> createFileAsync(String fileRelativePath)
    {
        return folder.createFileAsync(fileRelativePath);
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public AsyncFunction<Result<File>> createFileAsync(Path fileRelativePath)
    {
        return folder.createFileAsync(fileRelativePath);
    }

    public AsyncFunction<Result<Iterable<Folder>>> getFolders()
    {
        return folder.getFolders();
    }

    public AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursively()
    {
        return folder.getFoldersRecursively();
    }

    public AsyncFunction<Result<Iterable<File>>> getFiles()
    {
        return folder.getFiles();
    }

    public AsyncFunction<Result<Iterable<File>>> getFilesRecursively()
    {
        return folder.getFilesRecursively();
    }

    public Result<Iterable<FileSystemEntry>> getFilesAndFolders()
    {
        return folder.getFilesAndFolders();
    }

    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync()
    {
        return folder.getFilesAndFoldersAsync();
    }

    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursively()
    {
        return folder.getFilesAndFoldersRecursively();
    }
}
