package qub;

public class InMemoryFileSystemTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryFileSystem.class, runner.skip(), () ->
        {
            FileSystemTests.test(runner, (AsyncRunner asyncRunner) ->
            {
                final InMemoryFileSystem fileSystem = new InMemoryFileSystem(asyncRunner);
                fileSystem.createRoot("/");
                return fileSystem;
            });

            runner.testGroup("setFileCanDelete(String,boolean)", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    test.assertFalse(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                });
                
                runner.test("when parent folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    test.assertFalse(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                });
                
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    fileSystem.createFolder("C:\\folder");
                    test.assertFalse(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                });
                
                runner.test("when file exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    fileSystem.createFolder("C:\\folder");
                    fileSystem.createFile("C:\\folder\\file.bmp");
                    test.assertTrue(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                });
            });

            runner.testGroup("deleteFile(String)", () ->
            {
                runner.test("when file cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/");
                    fileSystem.createFile("Z:/file.png");
                    test.assertTrue(fileSystem.setFileCanDelete("Z:/file.png", false));
                    test.assertFalse(fileSystem.deleteFile("Z:/file.png").awaitReturn().getValue());
                    test.assertTrue(fileSystem.fileExists("Z:/file.png").awaitReturn().getValue());
                });
            });

            runner.testGroup("setFolderCanDelete(String,boolean)", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    test.assertFalse(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                });

                runner.test("when parent folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    test.assertFalse(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                });

                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    fileSystem.createFolder("C:\\folder");
                    test.assertFalse(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    fileSystem.createFolder("C:\\folder\\file.bmp");
                    test.assertTrue(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                });
            });

            runner.testGroup("deleteFolder(String)", () ->
            {
                runner.test("when folder cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/");
                    fileSystem.createFolder("Z:/file.png");
                    test.assertTrue(fileSystem.setFolderCanDelete("Z:/file.png", false));
                    test.assertFalse(fileSystem.deleteFolder("Z:/file.png").awaitReturn().getValue());
                    test.assertTrue(fileSystem.folderExists("Z:/file.png").awaitReturn().getValue());
                });

                runner.test("when child file cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/");
                    fileSystem.createFolder("Z:/file.png");
                    fileSystem.createFile("Z:/file.png/notme");
                    fileSystem.setFileCanDelete("Z:/file.png/notme", false);
                    test.assertFalse(fileSystem.deleteFolder("Z:/file.png").awaitReturn().getValue());
                    test.assertTrue(fileSystem.folderExists("Z:/file.png").awaitReturn().getValue());
                });

                runner.test("when child folder cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/");
                    fileSystem.createFolder("Z:/file.png");
                    fileSystem.createFolder("Z:/file.png/notme");
                    fileSystem.setFolderCanDelete("Z:/file.png/notme", false);
                    test.assertFalse(fileSystem.deleteFolder("Z:/file.png").awaitReturn().getValue());
                    test.assertTrue(fileSystem.folderExists("Z:/file.png").awaitReturn().getValue());
                });
            });
        });
    }
}
