package qub;

public class InMemoryFileSystemTests extends FileSystemTests
{
    @Override
    protected FileSystem getFileSystem()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        return fileSystem;
    }
}
