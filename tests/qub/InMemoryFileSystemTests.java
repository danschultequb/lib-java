package qub;

public interface InMemoryFileSystemTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryFileSystem.class, () ->
        {
            FileSystemTests.test(runner, (Clock clock) ->
            {
                final InMemoryFileSystem fileSystem = (clock == null
                    ? InMemoryFileSystem.create()
                    : InMemoryFileSystem.create(clock));
                fileSystem.createRoot("/").await();
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
                        new FileNotFoundException("C:\\folder\\file.bmp"));
                });

                runner.test("when file exists (canDelete - true)", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder").await();
                    fileSystem.createFile("C:\\folder\\file.bmp").await();

                    test.assertNull(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true).await());

                    test.assertNull(fileSystem.deleteFile("C:\\folder\\file.bmp").await());
                    test.assertFalse(fileSystem.fileExists("C:\\folder\\file.bmp").await());
                });

                runner.test("when file exists (canDelete - false)", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder").await();
                    fileSystem.createFile("C:\\folder\\file.bmp").await();

                    test.assertNull(fileSystem.setFileCanDelete("C:\\folder\\file.bmp", false).await());

                    test.assertThrows(() -> fileSystem.deleteFile("C:\\folder\\file.bmp").await(),
                        new FileNotFoundException("C:\\folder\\file.bmp"));
                    test.assertTrue(fileSystem.fileExists("C:\\folder\\file.bmp").await());
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

                runner.test("when folder exists (canDelete folder - true)", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder\\file.bmp").await();

                    test.assertNull(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).await());

                    test.assertNull(fileSystem.deleteFolder("C:\\folder\\file.bmp").await());
                    test.assertFalse(fileSystem.folderExists("C:\\folder\\file.bmp").await());
                });

                runner.test("when folder exists (canDelete folder - false)", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder\\file.bmp").await();

                    test.assertNull(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", false).await());

                    test.assertThrows(() -> fileSystem.deleteFolder("C:\\folder\\file.bmp").await(),
                        new FolderNotFoundException("C:\\folder\\file.bmp"));
                    test.assertTrue(fileSystem.folderExists("C:\\folder\\file.bmp").await());
                });

                runner.test("when folder exists (canDelete child file - false)", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder\\file.bmp").await();
                    fileSystem.createFile("C:\\folder\\file.bmp\\child.file").await();

                    test.assertNull(fileSystem.setFileCanDelete("C:\\folder\\file.bmp\\child.file", false).await());

                    test.assertThrows(() -> fileSystem.deleteFolder("C:\\folder\\file.bmp").await(),
                        new FolderNotFoundException("C:\\folder\\file.bmp"));
                    test.assertTrue(fileSystem.folderExists("C:\\folder\\file.bmp").await());
                    test.assertTrue(fileSystem.fileExists("C:\\folder\\file.bmp\\child.file").await());
                });

                runner.test("when folder exists (canDelete child folder - false)", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:\\").await();
                    fileSystem.createFolder("C:\\folder\\file.bmp").await();
                    fileSystem.createFolder("C:\\folder\\file.bmp\\child.file").await();

                    test.assertNull(fileSystem.setFolderCanDelete("C:\\folder\\file.bmp\\child.file", false).await());

                    test.assertThrows(() -> fileSystem.deleteFolder("C:\\folder\\file.bmp").await(),
                        new FolderNotFoundException("C:\\folder\\file.bmp"));
                    test.assertTrue(fileSystem.folderExists("C:\\folder\\file.bmp").await());
                    test.assertTrue(fileSystem.folderExists("C:\\folder\\file.bmp\\child.file").await());
                });
            });

            runner.testGroup("createRoot(String)", () ->
            {
                final Action2<String,Throwable> createRootErrorTest = (String rootPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        test.assertThrows(() -> fileSystem.createRoot(rootPath).await(),
                            expected);
                        test.assertEqual(Iterable.create(), fileSystem.getRoots().await());
                    });
                };

                createRootErrorTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                createRootErrorTest.run("", new PreConditionFailure("rootPath cannot be empty."));
                createRootErrorTest.run("hello", new PreConditionFailure("rootPath.isRooted() cannot be false."));

                runner.test("with \"C:\\\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("C:\\").await();
                    test.assertNotNull(root);
                    test.assertEqual("C:/", root.toString());
                    test.assertEqual(root, fileSystem.getRoot("C:\\").await());
                });

                runner.test("with \"C:/\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("C:/").await();
                    test.assertNotNull(root);
                    test.assertEqual("C:/", root.toString());
                    test.assertEqual(root, fileSystem.getRoot("C:/").await());
                });

                runner.test("with \"C:/cats\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("C:/cats").await();
                    test.assertNotNull(root);
                    test.assertEqual("C:/", root.toString());
                    test.assertEqual(root, fileSystem.getRoot("C:/cats").await());
                });

                runner.test("with \"C:/\" when it already exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:/").await();

                    test.assertThrows(() -> fileSystem.createRoot("C:/").await(),
                        new RootAlreadyExistsException("C:/"));

                    test.assertEqual(Iterable.create("C:/"), fileSystem.getRoots().await().map(Root::toString));
                });

                runner.test("with \"C:/cats\" when \"C:/\" already exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("C:/").await();

                    test.assertThrows(() -> fileSystem.createRoot("C:/cats").await(),
                        new RootAlreadyExistsException("C:/cats"));

                    test.assertEqual(Iterable.create("C:/"), fileSystem.getRoots().await().map(Root::toString));
                });
            });
        });
    }
}
