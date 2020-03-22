package qub;

public interface JavaFileSystemTests
{
    static void test(TestRunner runner)
    {
        final String tempFolderPathString = System.getProperty("java.io.tmpdir");
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

            runner.test("getRoots()", (Test test) ->
            {
                final JavaFileSystem fileSystem = new JavaFileSystem();
                final Iterable<Root> roots = fileSystem.getRoots().await();
                test.assertNotNullAndNotEmpty(roots);

                final Array<Root> rootsArray = roots.toArray();
                test.assertNotNullAndNotEmpty(rootsArray);
            });

            runner.testGroup("getRootTotalDataSize(Path)", () ->
            {
                runner.test("with existing path", (Test test) ->
                {
                    final JavaFileSystem fileSystem = new JavaFileSystem();
                    final Root root = fileSystem.getRoots().await().first();
                    final DataSize rootTotalDataSize = fileSystem.getRootTotalDataSize(root.getPath()).await();
                    test.assertNotNull(rootTotalDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, rootTotalDataSize.getUnits());
                    test.assertGreaterThan(rootTotalDataSize.getValue(), 0);
                });
            });
        });
    }
}
