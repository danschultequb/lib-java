package qub;

public class FolderNotFoundException extends NotFoundException
{
    private final Path folderPath;

    public FolderNotFoundException(String folderPath)
    {
        this(Path.parse(folderPath));
    }

    public FolderNotFoundException(Path folderPath)
    {
        super("The folder at \"" + folderPath.normalize() + "\" doesn't exist.");

        this.folderPath = folderPath.normalize();
    }

    public Path getFolderPath()
    {
        return folderPath;
    }
}
