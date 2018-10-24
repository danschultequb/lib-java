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
                    
                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, (String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, ""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, "basefolder"));
                });

                runner.test("with relative path that ends with backslash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, "basefolder\\"));
                });

                runner.test("with relative path that ends with forward slash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("/");

                    test.assertThrows(() -> FolderFileSystem.get(fileSystem, "basefolder/"));
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
