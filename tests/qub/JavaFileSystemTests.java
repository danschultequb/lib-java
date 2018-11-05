package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class JavaFileSystemTests
{
    public static void test(TestRunner runner)
    {
        String tempFolderPathString = System.getProperty("java.io.tmpdir");
        final Path tempFolderPath = Path.parse(tempFolderPathString).concatenateSegment("qub-tests");
        final AtomicInteger testNumber = new AtomicInteger(0);

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
