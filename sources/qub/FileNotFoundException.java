package qub;

public class FileNotFoundException extends RuntimeException
{
    private final Path filePath;

    public FileNotFoundException(String filePath)
    {
        this(Path.parse(filePath));
    }

    public FileNotFoundException(Path filePath)
    {
        super("The file at \"" + filePath + "\" doesn't exist.");

        this.filePath = filePath;
    }

    public Path getFilePath()
    {
        return filePath;
    }
}
