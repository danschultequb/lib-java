package qub;

public class FolderAlreadyExistsException extends RuntimeException
{
    private final Path folderPath;

    public FolderAlreadyExistsException(String folderPath)
    {
        this(FolderAlreadyExistsException.parseFolderPath(folderPath));
    }

    public FolderAlreadyExistsException(Path folderPath)
    {
        super("The folder at \"" + FolderAlreadyExistsException.getFolderPath(folderPath) + "\" already exists.");

        this.folderPath = FolderAlreadyExistsException.getFolderPath(folderPath);
    }

    public FolderAlreadyExistsException(Folder folder)
    {
        this(FolderAlreadyExistsException.getFolderPath(folder));
    }

    private static Path parseFolderPath(String folderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        return Path.parse(folderPath);
    }

    private static Path getFolderPath(Path folderPath)
    {
        PreCondition.assertNotNull(folderPath, "folderPath");
        PreCondition.assertTrue(folderPath.isRooted(), "folderPath.isRooted()");

        return folderPath;
    }

    private static Path getFolderPath(Folder folder)
    {
        PreCondition.assertNotNull(folder, "folder");

        return folder.getPath();
    }

    public Path getFolderPath()
    {
        return folderPath;
    }
}
