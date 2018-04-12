package qub;

public class FolderAlreadyExistsException extends RuntimeException
{
    private final Path folderPath;

    public FolderAlreadyExistsException(String folderPath)
    {
        this(Path.parse(folderPath));
    }

    public FolderAlreadyExistsException(Path folderPath)
    {
        super("The folder at \"" + folderPath + "\" already exists.");

        this.folderPath = folderPath;
    }

    public Path getFolderPath()
    {
        return folderPath;
    }
}
