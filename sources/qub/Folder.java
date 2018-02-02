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
    public boolean exists()
    {
        return getFileSystem().folderExists(getPath());
    }

    @Override
    public boolean delete()
    {
        return getFileSystem().deleteFolder(getPath());
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
     * Get whether or not the folder at the provided relativeFolderPath exists.
     * @param relativeFolderPath The relative path to the folder.
     * @return Whether or not the folder at the provided relativeFolderPath exists.
     */
    public boolean folderExists(String relativeFolderPath)
    {
        return folderExists(Path.parse(relativeFolderPath));
    }

    /**
     * Get whether or not the folder at the provided relativeFolderPath exists.
     * @param relativeFolderPath The relative path to the folder.
     * @return Whether or not the folder at the provided relativeFolderPath exists.
     */
    public boolean folderExists(Path relativeFolderPath)
    {
        boolean result = false;

        if (relativeFolderPath != null && !relativeFolderPath.isEmpty() && !relativeFolderPath.isRooted())
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
    public Folder getFolder(String relativeFolderPath)
    {
        Folder result = null;
        if (relativeFolderPath != null && !relativeFolderPath.isEmpty())
        {
            final Path childFolderPath = getChildPath(relativeFolderPath);
            final FileSystem fileSystem = getFileSystem();
            result = fileSystem.getFolder(childFolderPath);
        }
        return result;
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param relativeFolderPath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Folder getFolder(Path relativeFolderPath)
    {
        Folder result = null;
        if (relativeFolderPath != null)
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
    public boolean fileExists(String relativeFilePath)
    {
        return fileExists(Path.parse(relativeFilePath));
    }

    /**
     * Get whether or not the file at the provided relativeFilePath exists.
     * @param relativeFilePath The relative path to the file.
     * @return Whether or not the file at the provided relativeFilePath exists.
     */
    public boolean fileExists(Path relativeFilePath)
    {
        boolean result = false;

        if (relativeFilePath != null && !relativeFilePath.isEmpty() && !relativeFilePath.isRooted())
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
    public File getFile(String relativeFilePath)
    {
        final Path childFilePath = getChildPath(relativeFilePath);
        return getFileSystem().getFile(childFilePath);
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the File relative to this folder.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public File getFile(Path relativeFilePath)
    {
        final Path childFilePath = getChildPath(relativeFilePath);
        return getFileSystem().getFile(childFilePath);
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

    public Iterable<Folder> getFoldersRecursively()
    {
        return getFileSystem().getFoldersRecursively(getPath());
    }

    public Iterable<File> getFiles()
    {
        return getFileSystem().getFiles(getPath());
    }

    public Iterable<File> getFilesRecursively()
    {
        return getFileSystem().getFilesRecursively(getPath());
    }

    public Iterable<FileSystemEntry> getFilesAndFolders()
    {
        return getFileSystem().getFilesAndFolders(getPath());
    }

    public Iterable<FileSystemEntry> getFilesAndFolders(Action1<String> onError)
    {
        return getFileSystem().getFilesAndFolders(getPath(), onError);
    }

    public Iterable<FileSystemEntry> getFilesAndFoldersRecursively()
    {
        return getFileSystem().getFilesAndFoldersRecursively(getPath());
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
