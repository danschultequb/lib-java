package qub;

public class JavaFileSystemTests
{
    public static void test(TestRunner runner)
    {
        String tempFolderPathString = System.getProperty("java.io.tmpdir");
        final Path tempFolderPath = Path.parse(tempFolderPathString).concatenateSegment("qub-tests");
        final java.util.concurrent.atomic.AtomicInteger testNumber = new java.util.concurrent.atomic.AtomicInteger(0);

        final Value<FolderFileSystem> folderFileSystem = new Value<>();

        runner.afterTest((Test test) ->
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
                final Path testFolderPath = tempFolderPath.concatenateSegment(Integer.toString(testNumber.incrementAndGet()));
                folderFileSystem.set(FolderFileSystem.get(new JavaFileSystem(asyncRunner), testFolderPath).getValue());
                if (folderFileSystem.get().exists().getValue())
                {
                    folderFileSystem.get().delete();
                }
                folderFileSystem.get().create();
                return folderFileSystem.get();
            });
        });
    }
}
