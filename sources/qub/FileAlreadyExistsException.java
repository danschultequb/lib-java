package qub;

public class FileAlreadyExistsException extends RuntimeException
{
    private final Path filePath;

    public FileAlreadyExistsException(String filePath)
    {
        this(FileAlreadyExistsException.getPath(filePath));
    }

    public FileAlreadyExistsException(Path filePath)
    {
        super(FileAlreadyExistsException.createMessage(filePath));

        this.filePath = filePath;
    }

    public FileAlreadyExistsException(File file)
    {
        this(FileAlreadyExistsException.getPath(file));
    }

    private static Path getPath(String filePath)
    {
        PreCondition.assertNotNullAndNotEmpty(filePath, "filePath");

        return Path.parse(filePath);
    }

    private static Path getPath(File file)
    {
        PreCondition.assertNotNull(file, "file");

        return file.getPath();
    }

    private static String createMessage(Path filePath)
    {
        PreCondition.assertNotNull(filePath, "filePath");
        PreCondition.assertTrue(filePath.isRooted(), "filePath.isRooted()");

        return "The file at " + Strings.escapeAndQuote(filePath) + " already exists.";
    }

    public Path getFilePath()
    {
        return filePath;
    }
}
