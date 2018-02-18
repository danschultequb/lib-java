package qub;

public class JavaFileSystemTests
{
    public static void test(final TestRunner runner)
    {
        final JavaFileSystem javaFileSystem = new JavaFileSystem();
        String tempFolderPathString = System.getProperty("java.io.tmpdir");
        final Path tempFolderPath = Path.parse(tempFolderPathString).concatenateSegment("qub-tests");

        final Value<FolderFileSystem> folderFileSystem = new Value<>();
        runner.beforeTest(() ->
        {
            final Path testFolderPath = tempFolderPath.concatenate(Integer.toString((int)(java.lang.Math.random() * 1000)));
            folderFileSystem.set(FolderFileSystem.create(javaFileSystem, testFolderPath));
            folderFileSystem.get().create();
        });

        runner.afterTest(() ->
        {
            folderFileSystem.get().delete();
        });

        runner.testGroup("JavaFileSystem", () ->
        {
            FileSystemTests.test(runner, folderFileSystem::get);
        });
    }
}
