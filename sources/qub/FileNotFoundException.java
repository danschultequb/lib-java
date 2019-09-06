package qub;

/**
 * An exception that is thrown when an attempt is made to open a file that doesn't exist.
 */
public class FileNotFoundException extends NotFoundException
{
    private final Path filePath;

    public FileNotFoundException(String filePath)
    {
        this(Path.parse(filePath));
    }

    public FileNotFoundException(Path filePath)
    {
        super("The file at \"" + filePath + "\" doesn't exist.");

        PreCondition.assertNotNull(filePath, "filePath");

        this.filePath = filePath;
    }

    /**
     * Create a new FileNotFoundException using the path of the provided file.
     * @param file The file that was not found.
     */
    public FileNotFoundException(File file)
    {
        this(file.getPath());
    }

    /**
     * Get the path of the file that was not found.
     * @return The path of the file that was not found.
     */
    public Path getFilePath()
    {
        return filePath;
    }
}
