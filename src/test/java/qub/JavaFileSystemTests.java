package qub;

public class JavaFileSystemTests
{
    public static void test(final TestRunner runner)
    {
        final JavaFileSystem javaFileSystem = new JavaFileSystem();
        String tempFolderPathString = System.getProperty("java.io.tmpdir");
        final Path tempFolderPath = Path.parse(tempFolderPathString).concatenateSegment("qub-tests");
        final FolderFileSystem fileSystem = new FolderFileSystem(javaFileSystem, tempFolderPath);

        runner.beforeTest(new Action0()
        {
            @Override
            public void run()
            {
                fileSystem.delete();
                fileSystem.create();
            }
        });

        runner.afterTest(new Action0()
        {
            @Override
            public void run()
            {
                fileSystem.delete();
            }
        });

        runner.testGroup("JavaFileSystem", new Action0()
        {
            @Override
            public void run()
            {
                FileSystemTests.test(runner, new Function0<FileSystem>()
                {
                    @Override
                    public FileSystem run()
                    {
                        return fileSystem;
                    }
                });
            }
        });
    }
}
