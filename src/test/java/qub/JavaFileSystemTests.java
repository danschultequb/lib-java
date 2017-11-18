package qub;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public class JavaFileSystemTests extends FileSystemTests
{
    private static FolderFileSystem fileSystem;

    @BeforeClass
    public static void beforeClass()
    {
        final JavaFileSystem javaFileSystem = new JavaFileSystem();
        String tempFolderPathString = System.getProperty("java.io.tmpdir");
        final Path tempFolderPath = Path.parse(tempFolderPathString).concatenateSegment("qub-tests");
        fileSystem = new FolderFileSystem(javaFileSystem, tempFolderPath);
    }

    @Before
    public void beforeTest()
    {
        while (!fileSystem.exists())
        {
            fileSystem.create();
        }
    }

    @After
    public void afterTest()
    {
        while (fileSystem.exists())
        {
            fileSystem.delete();
        }
    }

    @Override
    protected FileSystem getFileSystem()
    {
        return fileSystem;
    }
}
