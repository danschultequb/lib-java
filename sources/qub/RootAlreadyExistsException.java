package qub;

public class RootAlreadyExistsException extends RuntimeException
{
    private final Path rootPath;

    public RootAlreadyExistsException(Path rootPath)
    {
        super(RootAlreadyExistsException.createMessage(rootPath));

        this.rootPath = rootPath;
    }

    private static String createMessage(Path rootPath)
    {
        PreCondition.assertNotNull(rootPath, "rootPath");
        PreCondition.assertTrue(rootPath.isRooted(), "rootPath.isRooted()");

        return "The root at " + Strings.escapeAndQuote(rootPath.toString()) + " already exists.";
    }

    public Path getRootPath()
    {
        return this.rootPath;
    }
}
