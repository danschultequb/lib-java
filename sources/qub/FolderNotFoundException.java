package qub;

public class FolderNotFoundException extends RuntimeException
{
    private final Path folderPath;

    public FolderNotFoundException(String folderPath)
    {
        this(Path.parse(folderPath));
    }

    public FolderNotFoundException(Path folderPath)
    {
        super("The folder at \"" + folderPath + "\" doesn't exist.");

        this.folderPath = folderPath;
    }

    public Path getFolderPath()
    {
        return folderPath;
    }
}
