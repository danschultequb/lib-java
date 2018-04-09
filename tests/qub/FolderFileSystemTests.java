package qub;

public class FolderFileSystemTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FolderFileSystem.class, () ->
        {
            runner.testGroup("get(FileSystem,String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");
                    
                    final Result<FolderFileSystem> getResult = FolderFileSystem.get(fileSystem, (String)null);
                    test.assertError(new IllegalArgumentException("baseFolderPath cannot be null."), getResult);
                });

                runner.test("with empty path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    final Result<FolderFileSystem> getResult = FolderFileSystem.get(fileSystem, "");
                    test.assertError(new IllegalArgumentException("baseFolderPath cannot be null."), getResult);
                });

                runner.test("with relative path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    final Result<FolderFileSystem> folderFileSystem = FolderFileSystem.get(fileSystem, "basefolder");
                    test.assertSuccess(folderFileSystem);
                    test.assertEqual(Path.parse("basefolder"), folderFileSystem.getValue().getBaseFolderPath());
                });

                runner.test("with relative path that ends with backslash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    final Result<FolderFileSystem> folderFileSystem = FolderFileSystem.get(fileSystem, "basefolder\\");
                    test.assertEqual(Path.parse("basefolder"), folderFileSystem.getValue().getBaseFolderPath());
                });

                runner.test("with relative path that ends with forward slash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    final Result<FolderFileSystem> folderFileSystem = FolderFileSystem.get(fileSystem, "basefolder/");
                    test.assertEqual(Path.parse("basefolder"), folderFileSystem.getValue().getBaseFolderPath());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    final Result<FolderFileSystem> folderFileSystem = FolderFileSystem.get(fileSystem, "\\basefolder");
                    test.assertEqual(Path.parse("\\basefolder"), folderFileSystem.getValue().getBaseFolderPath());
                });

                runner.test("with rooted path that ends with backslash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    final Result<FolderFileSystem> folderFileSystem = FolderFileSystem.get(fileSystem, "/basefolder\\");
                    test.assertEqual(Path.parse("/basefolder"), folderFileSystem.getValue().getBaseFolderPath());
                });

                runner.test("with rooted path that ends with forward slash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    final Result<FolderFileSystem> folderFileSystem = FolderFileSystem.get(fileSystem, "/basefolder/");
                    test.assertEqual(Path.parse("/basefolder"), folderFileSystem.getValue().getBaseFolderPath());
                });
            });
        });
    }
}
