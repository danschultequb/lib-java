package qub;

public class RootAlreadyExistsException extends RuntimeException
{
    private final Path rootPath;

    public RootAlreadyExistsException(Path rootPath)
    {
        super("The root at \"" + rootPath + "\" already exists.");

        this.rootPath = rootPath;
    }

    public Path getRootPath()
    {
        return rootPath;
    }
}
