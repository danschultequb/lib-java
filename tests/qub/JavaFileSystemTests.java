package qub;

public class JavaFileSystemTests
{
    public static void test(TestRunner runner)
    {
        String tempFolderPathString = System.getProperty("java.io.tmpdir");
        final Path tempFolderPath = Path.parse(tempFolderPathString).concatenateSegment("qub-tests");
        final IntegerValue testNumber = new IntegerValue(0);

        final Value<FolderFileSystem> folderFileSystem = Value.create();

        runner.afterTest((Test test) ->
        {
            if (folderFileSystem.hasValue())
            {
                folderFileSystem.get().delete().await();
                folderFileSystem.clear();
            }
        });

        runner.testGroup(JavaFileSystem.class, () ->
        {
            FileSystemTests.test(runner, (Test test) ->
            {
                final Path testFolderPath = tempFolderPath.concatenateSegment(testNumber.increment().toString());
                folderFileSystem.set(FolderFileSystem.get(new JavaFileSystem(), testFolderPath));
                if (folderFileSystem.get().exists().await())
                {
                    folderFileSystem.get().delete().await();
                }
                folderFileSystem.get().create().await();
                return folderFileSystem.get();
            });
        });
    }
}
