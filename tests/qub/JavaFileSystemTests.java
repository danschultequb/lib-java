package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class JavaFileSystemTests
{
    public static void test(TestRunner runner)
    {
        String tempFolderPathString = System.getProperty("java.io.tmpdir");
        final Path tempFolderPath = Path.parse(tempFolderPathString).concatenateSegment("qub-tests");
        final AtomicInteger testNumber = new AtomicInteger(0);

        final Value<Path> testFolderPath = new Value<>();
        final Value<FolderFileSystem> folderFileSystem = new Value<>();

        runner.afterTest(() ->
        {
            if (folderFileSystem.hasValue())
            {
                folderFileSystem.get().delete().await();
                folderFileSystem.clear();
            }
        });

        runner.testGroup(JavaFileSystem.class, runner.skip(), () ->
        {
            FileSystemTests.test(runner, (AsyncRunner asyncRunner) ->
            {
                testFolderPath.set(tempFolderPath.concatenateSegment(Integer.toString(testNumber.incrementAndGet())));
                folderFileSystem.set(FolderFileSystem.create(new JavaFileSystem(asyncRunner), testFolderPath.get()));
                folderFileSystem.get().create().await();
                return folderFileSystem.get();
            });
        });
    }
}
