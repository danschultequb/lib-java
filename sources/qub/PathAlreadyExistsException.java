package qub;

public class PathAlreadyExistsException extends RuntimeException
{
    private final Path filePath;

    public PathAlreadyExistsException(String filePath)
    {
        this(Path.parse(filePath));
    }

    public PathAlreadyExistsException(Path filePath)
    {
        super("The path \"" + filePath + "\" already exists.");

        this.filePath = filePath;
    }

    public Path getPath()
    {
        return filePath;
    }
}
