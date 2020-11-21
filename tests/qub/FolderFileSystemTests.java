package qub;

public interface FolderFileSystemTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FolderFileSystem.class, () ->
        {
            runner.testGroup("get(FileSystem,String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/");
                    
                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, (String)null),
                        new PreConditionFailure("baseFolderPath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/");

                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, ""),
                        new PreConditionFailure("baseFolderPath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/");

                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, "basefolder"),
                        new PreConditionFailure("baseFolderPath.isRooted() cannot be false."));
                });

                runner.test("with relative path that ends with backslash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();

                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, "basefolder\\"),
                        new PreConditionFailure("baseFolderPath.isRooted() cannot be false."));
                });

                runner.test("with relative path that ends with forward slash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/").await();

                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, "basefolder/"),
                        new PreConditionFailure("baseFolderPath.isRooted() cannot be false."));
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/");

                    final FolderFileSystem folderFileSystem = FolderFileSystem.get(fileSystem, "\\basefolder");
                    test.assertEqual(Path.parse("\\basefolder"), folderFileSystem.getBaseFolderPath());
                });

                runner.test("with rooted path that ends with backslash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/");

                    final FolderFileSystem folderFileSystem = FolderFileSystem.get(fileSystem, "/basefolder\\");
                    test.assertEqual(Path.parse("/basefolder/"), folderFileSystem.getBaseFolderPath());
                });

                runner.test("with rooted path that ends with forward slash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    fileSystem.createRoot("/");

                    final FolderFileSystem folderFileSystem = FolderFileSystem.get(fileSystem, "/basefolder/");
                    test.assertEqual(Path.parse("/basefolder/"), folderFileSystem.getBaseFolderPath());
                });
            });
        });
    }
}
