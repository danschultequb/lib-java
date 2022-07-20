package qub;

public interface JavaFileSystemTests
{
    static void test(TestRunner runner)
    {
        final String tempFolderPathString = System.getProperty("java.io.tmpdir");
        final Path tempFolderPath = Path.parse(tempFolderPathString).concatenateSegments("qub-tests");
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
            FileSystemTests.test(runner, (Clock clock) ->
            {
                final Path testFolderPath = tempFolderPath.concatenateSegments(testNumber.increment().toString());
                folderFileSystem.set(FolderFileSystem.get(JavaFileSystem.create(), testFolderPath));
                if (folderFileSystem.get().exists().await())
                {
                    folderFileSystem.get().delete().await();
                }
                folderFileSystem.get().create().await();
                return folderFileSystem.get();
            });

            runner.test("getRoots()", (Test test) ->
            {
                final JavaFileSystem fileSystem = JavaFileSystem.create();
                final Iterable<Root> roots = fileSystem.getRoots().await();
                test.assertNotNullAndNotEmpty(roots);

                final Array<Root> rootsArray = roots.toArray();
                test.assertNotNullAndNotEmpty(rootsArray);
            });

            runner.testGroup("getRootTotalDataSize(Path)", () ->
            {
                runner.test("with non-existing root path", (Test test) ->
                {
                    final JavaFileSystem fileSystem = JavaFileSystem.create();
                    test.assertThrows(() -> fileSystem.getRootTotalDataSize(Path.parse("p:/")).await(),
                        new RootNotFoundException("p:/"));
                });

                runner.test("with existing root path", (Test test) ->
                {
                    final JavaFileSystem fileSystem = JavaFileSystem.create();
                    final Root root = fileSystem.getRoots().await().first();
                    final DataSize rootTotalDataSize = fileSystem.getRootTotalDataSize(root.getPath()).await();
                    test.assertNotNull(rootTotalDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, rootTotalDataSize.getUnits());
                    test.assertGreaterThan(rootTotalDataSize.getValue(), 0);
                });
            });

            runner.testGroup("getRootUnusedDataSize(Path)", () ->
            {
                runner.test("with non-existing root path", (Test test) ->
                {
                    final JavaFileSystem fileSystem = JavaFileSystem.create();
                    test.assertThrows(() -> fileSystem.getRootUnusedDataSize(Path.parse("p:/")).await(),
                        new RootNotFoundException("p:/"));
                });

                runner.test("with existing root path", (Test test) ->
                {
                    final JavaFileSystem fileSystem = JavaFileSystem.create();
                    final Root root = fileSystem.getRoots().await().first();
                    final DataSize rootUnusedDataSize = fileSystem.getRootUnusedDataSize(root.getPath()).await();
                    test.assertNotNull(rootUnusedDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, rootUnusedDataSize.getUnits());
                    test.assertGreaterThan(rootUnusedDataSize.getValue(), 0);
                });
            });

            runner.testGroup("getRootUsedDataSize(Path)", () ->
            {
                runner.test("with non-existing root path", (Test test) ->
                {
                    final JavaFileSystem fileSystem = JavaFileSystem.create();
                    test.assertThrows(() -> fileSystem.getRootUsedDataSize(Path.parse("p:/")).await(),
                        new RootNotFoundException("p:/"));
                });

                runner.test("with existing root path", (Test test) ->
                {
                    final JavaFileSystem fileSystem = JavaFileSystem.create();
                    final Root root = fileSystem.getRoots().await().first();
                    final DataSize rootUnusedDataSize = fileSystem.getRootUsedDataSize(root.getPath()).await();
                    test.assertNotNull(rootUnusedDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, rootUnusedDataSize.getUnits());
                    test.assertGreaterThan(rootUnusedDataSize.getValue(), 0);
                });
            });
        });
    }
}
