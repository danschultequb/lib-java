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
    Folder(FileSystem fileSystem, String path)
    {
        super(fileSystem, path);
    }

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
    public boolean exists()
    {
        return getFileSystem().folderExists(getPath());
    }

    /**
     * Try to create this folder and return whether or not this function created the folder.
     * @return Whether or not this function created the folder.
     */
    public boolean create()
    {
        return getFileSystem().createFolder(getPath());
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param folderRelativePath The relative path from this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public boolean createFolder(String folderRelativePath)
    {
        boolean result = false;
        final Path childFolderPath = getChildPath(folderRelativePath);
        if (!getPath().equals(childFolderPath))
        {
            result = getFileSystem().createFolder(childFolderPath);
        }
        return result;
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param folderRelativePath The relative path from this folder to the child folder to create.
     * @param outputFolder The Value that will contain the child folder whether it was created or not.
     * @return Whether or not this function created the child folder.
     */
    public boolean createFolder(String folderRelativePath, Value<Folder> outputFolder)
    {
        boolean result = false;
        final Path childFolderPath = getChildPath(folderRelativePath);
        if (!getPath().equals(childFolderPath))
        {
            result = getFileSystem().createFolder(childFolderPath, outputFolder);
        }
        return result;
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param folderRelativePath The relative path from this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public boolean createFolder(Path folderRelativePath)
    {
        boolean result = false;
        final Path childFolderPath = getChildPath(folderRelativePath);
        if (!getPath().equals(childFolderPath))
        {
            result = getFileSystem().createFolder(childFolderPath);
        }
        return result;
    }

    /**
     * Create a child folder of this folder with the provided relative path.
     * @param folderRelativePath The relative path from this folder to the child folder to create.
     * @param outputFolder The Value that will contain the child folder whether it was created or not.
     * @return Whether or not this function created the child folder.
     */
    public boolean createFolder(Path folderRelativePath, Value<Folder> outputFolder)
    {
        boolean result = false;
        final Path childFolderPath = getChildPath(folderRelativePath);
        if (!getPath().equals(childFolderPath))
        {
            result = getFileSystem().createFolder(childFolderPath, outputFolder);
        }
        return result;
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public boolean createFile(String fileRelativePath)
    {
        boolean result = false;
        final Path childFilePath = getChildPath(fileRelativePath);
        if (!getPath().equals(childFilePath))
        {
            result = getFileSystem().createFile(childFilePath);
        }
        return result;
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @param outputFile The Value that will contain the child file whether it was created or not.
     * @return Whether or not this function created the child file.
     */
    public boolean createFile(String fileRelativePath, Value<File> outputFile)
    {
        boolean result = false;
        final Path childFilePath = getChildPath(fileRelativePath);
        if (!getPath().equals(childFilePath))
        {
            result = getFileSystem().createFile(childFilePath, outputFile);
        }
        return result;
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public boolean createFile(Path fileRelativePath)
    {
        boolean result = false;
        final Path childFilePath = getChildPath(fileRelativePath);
        if (!getPath().equals(childFilePath))
        {
            result = getFileSystem().createFile(childFilePath);
        }
        return result;
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @param outputFile The Value that will contain the child file whether it was created or not.
     * @return Whether or not this function created the child file.
     */
    public boolean createFile(Path fileRelativePath, Value<File> outputFile)
    {
        boolean result = false;
        final Path childFilePath = getChildPath(fileRelativePath);
        if (!getPath().equals(childFilePath))
        {
            result = getFileSystem().createFile(childFilePath, outputFile);
        }
        return result;
    }

    public Iterable<Folder> getFolders()
    {
        return getFileSystem().getFolders(getPath());
    }

    public Iterable<File> getFiles()
    {
        return getFileSystem().getFiles(getPath());
    }

    public Iterable<FileSystemEntry> getFilesAndFolders()
    {
        return getFileSystem().getFilesAndFolders(getPath());
    }

    private Path getChildPath(String relativePath)
    {
        return getChildPath(Path.parse(relativePath));
    }

    private Path getChildPath(Path relativePath)
    {
        return getPath().concatenateSegment(relativePath);
    }
}
