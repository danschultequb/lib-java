package qub;

public class FileAlreadyExistsException extends RuntimeException
{
    private final Path filePath;

    public FileAlreadyExistsException(String filePath)
    {
        this(Path.parse(filePath));
    }

    public FileAlreadyExistsException(Path filePath)
    {
        super("The file at \"" + filePath + "\" already exists.");

        this.filePath = filePath;
    }

    public Path getFilePath()
    {
        return filePath;
    }
}
