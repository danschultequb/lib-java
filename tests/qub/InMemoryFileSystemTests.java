package qub;

public class InMemoryFileSystemTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryFileSystem.class, () ->
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
                    test.assertThrows(() -> fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FileNotFoundException("C:\\folder\\file.bmp"));
                });
                
                runner.test("when parent folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    test.assertEqual("C:", fileSystem.createRoot("C:\\").await().toString());
                    test.assertThrows(() -> fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FileNotFoundException("C:\\folder\\file.bmp"));
                });
                
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    test.assertEqual("C:", fileSystem.createRoot("C:\\").await().toString());
                    test.assertEqual("C:/folder", fileSystem.createFolder("C:\\folder").await().toString());
                    test.assertThrows(() -> fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FileNotFoundException("C:/folder/file.bmp"));
                });
                
                runner.test("when file exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder").await();
                    fileSystem.createFile("C:\\folder\\file.bmp").await();
                    test.assertNull(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true).await());
                });
            });

            runner.testGroup("deleteFile(String)", () ->
            {
                runner.test("when file cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/").await();
                    fileSystem.createFile("Z:/file.png").await();
                    fileSystem.setFileCanDelete("Z:/file.png", false).await();
                    test.assertThrows(() -> fileSystem.deleteFile("Z:/file.png").await(),
                        new FileNotFoundException("Z:/file.png"));
                    test.assertTrue(fileSystem.fileExists("Z:/file.png").await());
                });
            });

            runner.testGroup("setFolderCanDelete(String,boolean)", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FolderNotFoundException("C:\\folder\\file.bmp"));
                });

                runner.test("when parent folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\").await();
                    test.assertThrows(() -> fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FolderNotFoundException("C:\\folder\\file.bmp"));
                });

                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder").await();
                    test.assertThrows(() -> fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FolderNotFoundException("C:\\folder\\file.bmp"));
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder\\file.bmp").await();
                    test.assertNull(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).await());
                });
            });

            runner.testGroup("deleteFolder(String)", () ->
            {
                runner.test("when folder cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/").await();
                    fileSystem.createFolder("Z:/file.png").await();
                    fileSystem.setFolderCanDelete("Z:/file.png", false).await();
                    test.assertThrows(() -> fileSystem.deleteFolder("Z:/file.png").await(),
                        new FolderNotFoundException("Z:/file.png"));
                    test.assertTrue(fileSystem.folderExists("Z:/file.png").await());
                });

                runner.test("when child file cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/").await();
                    fileSystem.createFolder("Z:/file.png").await();
                    fileSystem.createFile("Z:/file.png/notme").await();
                    fileSystem.setFileCanDelete("Z:/file.png/notme", false).await();
                    test.assertThrows(() -> fileSystem.deleteFolder("Z:/file.png").await(),
                        new FolderNotFoundException("Z:/file.png"));
                    test.assertTrue(fileSystem.folderExists("Z:/file.png").await());
                });

                runner.test("when child folder cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/").await();
                    fileSystem.createFolder("Z:/file.png").await();
                    fileSystem.createFolder("Z:/file.png/notme").await();
                    fileSystem.setFolderCanDelete("Z:/file.png/notme", false).await();
                    test.assertThrows(() -> fileSystem.deleteFolder("Z:/file.png").await(),
                        new FolderNotFoundException("Z:/file.png"));
                    test.assertTrue(fileSystem.folderExists("Z:/file.png").await());
                });
            });
        });
    }
}
