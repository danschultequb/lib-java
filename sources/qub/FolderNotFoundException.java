package qub;

public class FolderNotFoundException extends NotFoundException
{
    private final Path folderPath;

    public FolderNotFoundException(String folderPath)
    {
        this(FolderNotFoundException.parseFolderPath(folderPath));
    }

    public FolderNotFoundException(Path folderPath)
    {
        super("The folder at \"" + FolderNotFoundException.getFolderPath(folderPath) + "\" doesn't exist.");

        this.folderPath = folderPath;
    }

    public FolderNotFoundException(Folder folder)
    {
        this(FolderNotFoundException.getFolderPath(folder));
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
