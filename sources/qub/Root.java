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
    public boolean exists()
    {
        return folder.exists();
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param relativeFolderPath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Folder getFolder(String relativeFolderPath)
    {
        return folder.getFolder(relativeFolderPath);
    }

    /**
     * Get a reference to the Folder at the provided relative folderPath.
     * @param relativeFolderPath The path to the folder relative to this folder.
     * @return A reference to the Folder at the provided relative folderPath.
     */
    public Folder getFolder(Path relativeFolderPath)
    {
        return folder.getFolder(relativeFolderPath);
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the file relative to this Root.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public File getFile(String relativeFilePath)
    {
        return folder.getFile(relativeFilePath);
    }

    /**
     * Get a reference to the File at the provided relativeFilePath.
     * @param relativeFilePath The path to the file relative to this Root.
     * @return A reference to the File at the provided relativeFilePath.
     */
    public File getFile(Path relativeFilePath)
    {
        return folder.getFile(relativeFilePath);
    }

    /**
     * Create a child folder of this Root with the provided relative path.
     * @param folderRelativePath The relative path from this folder to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public boolean createFolder(String folderRelativePath)
    {
        return folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child folder of this Root with the provided relative path.
     * @param folderRelativePath The relative path from this Root to the child folder to create.
     * @param outputFolder The Value that will contain the child folder if it was created.
     * @return Whether or not this function created the child folder.
     */
    public boolean createFolder(String folderRelativePath, Value<Folder> outputFolder)
    {
        return folder.createFolder(folderRelativePath, outputFolder);
    }

    /**
     * Create a child folder of this Root with the provided relative path.
     * @param folderRelativePath The relative path from this Root to the child folder to create.
     * @return Whether or not this function created the child folder.
     */
    public boolean createFolder(Path folderRelativePath)
    {
        return folder.createFolder(folderRelativePath);
    }

    /**
     * Create a child folder of this Root with the provided relative path.
     * @param folderRelativePath The relative path from this Root to the child folder to create.
     * @param outputFolder The Value that will contain the child folder whether it was created or not.
     * @return Whether or not this function created the child folder.
     */
    public boolean createFolder(Path folderRelativePath, Value<Folder> outputFolder)
    {
        return folder.createFolder(folderRelativePath, outputFolder);
    }

    /**
     * Create a child file of this Root with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public boolean createFile(String fileRelativePath)
    {
        return folder.createFile(fileRelativePath);
    }

    /**
     * Create a child file of this Root with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @param outputFile The Value that will contain the child file whether it was created or not.
     * @return Whether or not this function created the child file.
     */
    public boolean createFile(String fileRelativePath, Value<File> outputFile)
    {
        return folder.createFile(fileRelativePath, outputFile);
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @return Whether or not this function created the child file.
     */
    public boolean createFile(Path fileRelativePath)
    {
        return folder.createFile(fileRelativePath);
    }

    /**
     * Create a child file of this folder with the provided relative path.
     * @param fileRelativePath The relative path from this folder to the child file to create.
     * @param outputFile The Value that will contain the child file whether it was created or not.
     * @return Whether or not this function created the child file.
     */
    public boolean createFile(Path fileRelativePath, Value<File> outputFile)
    {
        return folder.createFile(fileRelativePath, outputFile);
    }

    public Iterable<Folder> getFolders()
    {
        return folder.getFolders();
    }

    public Iterable<Folder> getFoldersRecursively()
    {
        return folder.getFoldersRecursively();
    }

    public Iterable<File> getFiles()
    {
        return folder.getFiles();
    }

    public Iterable<File> getFilesRecursively()
    {
        return folder.getFilesRecursively();
    }

    public Iterable<FileSystemEntry> getFilesAndFolders()
    {
        return folder.getFilesAndFolders();
    }

    public Iterable<FileSystemEntry> getFilesAndFoldersRecursively()
    {
        return folder.getFilesAndFoldersRecursively();
    }
}
