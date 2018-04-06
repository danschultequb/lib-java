package qub;

public class RootNotFoundException extends RuntimeException
{
    private final Path rootPath;

    public RootNotFoundException(String rootPath)
    {
        this(Path.parse(rootPath));
    }

    public RootNotFoundException(Path rootPath)
    {
        super("The root at \"" + rootPath + "\" doesn't exist.");

        this.rootPath = rootPath;
    }

    public Path getRootPath()
    {
        return rootPath;
    }
}
