package qub;

public class FolderFileSystemTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FolderFileSystem.class, () ->
        {
            runner.testGroup("create(FileSystem,String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    fileSystem.createRoot("/");

                    test.assertNull(FolderFileSystem.create(fileSystem, (String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    fileSystem.createRoot("/");

                    test.assertNull(FolderFileSystem.create(fileSystem, ""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    fileSystem.createRoot("/");

                    final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "basefolder");
                    test.assertEqual(Path.parse("basefolder"), folderFileSystem.getBaseFolderPath());
                });

                runner.test("with relative path that ends with backslash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    fileSystem.createRoot("/");

                    final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "basefolder\\");
                    test.assertEqual(Path.parse("basefolder"), folderFileSystem.getBaseFolderPath());
                });

                runner.test("with relative path that ends with forward slash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    fileSystem.createRoot("/");

                    final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "basefolder/");
                    test.assertEqual(Path.parse("basefolder"), folderFileSystem.getBaseFolderPath());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    fileSystem.createRoot("/");

                    final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "\\basefolder");
                    test.assertEqual(Path.parse("\\basefolder"), folderFileSystem.getBaseFolderPath());
                });

                runner.test("with rooted path that ends with backslash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    fileSystem.createRoot("/");

                    final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "/basefolder\\");
                    test.assertEqual(Path.parse("/basefolder"), folderFileSystem.getBaseFolderPath());
                });

                runner.test("with rooted path that ends with forward slash", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    fileSystem.createRoot("/");

                    final FolderFileSystem folderFileSystem = FolderFileSystem.create(fileSystem, "/basefolder/");
                    test.assertEqual(Path.parse("/basefolder"), folderFileSystem.getBaseFolderPath());
                });
            });
        });
    }
}
