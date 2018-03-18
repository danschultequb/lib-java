package qub;

public class JavaFileSystemTests
{
    public static void test(TestRunner runner)
    {
        String tempFolderPathString = System.getProperty("java.io.tmpdir");
        final Path tempFolderPath = Path.parse(tempFolderPathString).concatenateSegment("qub-tests");

        final Value<FolderFileSystem> folderFileSystem = new Value<>();

        runner.afterTest(() ->
        {
            if (folderFileSystem.hasValue())
            {
                folderFileSystem.get().delete();
                folderFileSystem.clear();
            }
        });

        runner.testGroup(JavaFileSystem.class, () ->
        {
            FileSystemTests.test(runner, (AsyncRunner asyncRunner) ->
            {
                final Value<Path> testFolderPath = new Value<>(tempFolderPath.concatenate(Integer.toString((int)(java.lang.Math.random() * 1000))));
                folderFileSystem.set(FolderFileSystem.create(new JavaFileSystem(asyncRunner), testFolderPath.get()));
                folderFileSystem.get().create();
                return folderFileSystem.get();
            });
        });
    }
}
