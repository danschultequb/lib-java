package qub;

public class JavaFileSystemTests extends FileSystemTests
{
    @Override
    protected JavaFileSystem getFileSystem()
    {
        return new JavaFileSystem();
    }
}
