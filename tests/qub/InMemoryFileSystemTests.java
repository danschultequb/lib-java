package qub;

public interface InMemoryFileSystemTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryFileSystem.class, () ->
        {
            FileSystemTests.test(runner, (Clock clock) ->
            {
                final InMemoryFileSystem fileSystem = (clock == null ? InMemoryFileSystem.create() : InMemoryFileSystem.create(clock));
                fileSystem.createRoot("/");
                return fileSystem;
            });

            runner.testGroup("getRootTotalDataSize(String)", () ->
            {
                runner.test("with root where no data size was specified", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("Z:/").await();
                    test.assertNull(fileSystem.getRootTotalDataSize("Z:/").await());
                });

                runner.test("with root where data size was specified", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("Z:/", DataSize.megabytes(100)).await();
                    test.assertEqual(DataSize.megabytes(100), fileSystem.getRootTotalDataSize("Z:/").await());
                });
            });

            runner.testGroup("setFileCanDelete(String,boolean)", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertThrows(() -> fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true).await(),
                        new RootNotFoundException("C:"));
                });
                
                runner.test("when parent folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertEqual("C:/", fileSystem.createRoot("C:\\").await().toString());
                    test.assertThrows(() -> fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FileNotFoundException("C:\\folder\\file.bmp"));
                });
                
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertEqual("C:/", fileSystem.createRoot("C:\\").await().toString());
                    test.assertEqual("C:/folder/", fileSystem.createFolder("C:\\folder").await().toString());
                    test.assertThrows(() -> fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FileNotFoundException("C:/folder/file.bmp"));
                });
                
                runner.test("when file exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
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
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
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
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertThrows(() -> fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).await(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when parent folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:\\").await();
                    test.assertThrows(() -> fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FolderNotFoundException("C:\\folder\\file.bmp"));
                });

                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder").await();
                    test.assertThrows(() -> fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).await(),
                        new FolderNotFoundException("C:\\folder\\file.bmp"));
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder\\file.bmp").await();
                    test.assertNull(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).await());
                });
            });

            runner.testGroup("deleteFolder(String)", () ->
            {
                runner.test("when folder cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("Z:/").await();
                    fileSystem.createFolder("Z:/file.png").await();
                    fileSystem.setFolderCanDelete("Z:/file.png", false).await();
                    test.assertThrows(() -> fileSystem.deleteFolder("Z:/file.png").await(),
                        new FolderNotFoundException("Z:/file.png"));
                    test.assertTrue(fileSystem.folderExists("Z:/file.png").await());
                });

                runner.test("when child file cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
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
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
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
