package qub;

public interface FileTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(File.class, () ->
        {
            FileSystemEntryTests.test(runner, FileTests::getFile);

            runner.testGroup("getParentFolder()", () ->
            {
                runner.test("with file at root", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File file = fileSystem.getFile("/file.txt").await();
                    test.assertEqual("/", file.getParentFolder().await().toString());
                });

                runner.test("with file in folder", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File file = fileSystem.getFile("/folder/file.txt").await();
                    test.assertEqual("/folder/", file.getParentFolder().await().toString());
                });
            });

            runner.test("getFileExtension()", (Test test) ->
            {
                final FileSystem fileSystem = getFileSystem(test);

                final File fileWithoutExtension = fileSystem.getFile("/folder/file").await();
                test.assertNull(fileWithoutExtension.getFileExtension());

                final File fileWithExtension = fileSystem.getFile("/file.csv").await();
                test.assertEqual(".csv", fileWithExtension.getFileExtension());
            });

            runner.testGroup("getNameWithoutFileExtension()", () ->
            {
                final Action2<String,String> getNameWithoutFileExtensionTest = (String filePath, String expectedNameWithoutFileExtension) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = getFileSystem(test);
                        test.assertEqual(expectedNameWithoutFileExtension, fileSystem.getFile(filePath).await().getNameWithoutFileExtension());
                    });
                };

                getNameWithoutFileExtensionTest.run("/folder/file", "file");
                getNameWithoutFileExtensionTest.run("/dogs.txt", "dogs");
            });
            
            runner.test("create()", (Test test) ->
            {
                final File file = getFile(test);

                test.assertNull(file.create().await());

                test.assertTrue(file.exists().await());

                test.assertEqual(new byte[0], file.getContents().await());

                test.assertThrows(() -> file.create().await(),
                    new FileAlreadyExistsException(file.toString()));

                test.assertTrue(file.exists().await());
            });
            
            runner.testGroup("exists()", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.exists().await());
                });

                runner.test("when file does exist", (Test test) ->
                {
                    final File file = getFile(test);
                    file.create();
                    test.assertTrue(file.exists().await());
                });
            });

            runner.testGroup("delete()", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertThrows(() -> file.delete().await(), new FileNotFoundException(file.toString()));
                    test.assertFalse(file.exists().await());
                });

                runner.test("when file does exist", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertNull(file.create().await());

                    test.assertNull(file.delete().await());
                    test.assertFalse(file.exists().await());
                });
            });

            runner.testGroup("equals()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.equals(null));
                });

                runner.test("with String", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.equals(file.getPath().toString()));
                });

                runner.test("with Path", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.equals(file.getPath()));
                });

                runner.test("with different file create same file system", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File lhs = getFile(fileSystem, "/a/path.txt");
                    final File rhs = getFile(fileSystem, "/not/the/same/path.txt");
                    test.assertFalse(lhs.equals(rhs));
                });

                runner.test("with same", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertTrue(file.equals(file));
                });

                runner.test("with equal path and same file system", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File lhs = getFile(fileSystem);
                    final File rhs = getFile(fileSystem);
                    test.assertTrue(lhs.equals(rhs));
                });
            });

            runner.testGroup("getContentDataSize()", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final File file = FileTests.getFile(test);
                    test.assertThrows(() -> file.getContentsDataSize().await(),
                        new FileNotFoundException(file.getPath()));
                });

                runner.test("when file is empty", (Test test) ->
                {
                    final File file = FileTests.getFile(test);
                    file.setContentsAsString("").await();
                    test.assertEqual(DataSize.zero, file.getContentsDataSize().await());
                });

                runner.test("when file is not empty", (Test test) ->
                {
                    final File file = FileTests.getFile(test);
                    file.setContentsAsString("hello there").await();
                    test.assertEqual(DataSize.bytes(11), file.getContentsDataSize().await());
                });
            });

            runner.testGroup("getContents()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertThrows(() -> file.getContents().await(), new FileNotFoundException("/A"));
                });

                runner.test("with existing file with no contents", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertNull(file.create().await());
                    test.assertEqual(new byte[0], file.getContents().await());
                });
            });

            runner.testGroup("getContentReadStream()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertThrows(() -> file.getContentsReadStream().await(),
                        new FileNotFoundException("/A"));
                });
            });

            runner.testGroup("copyTo(Path)", () ->
            {
                runner.test("with null destinationFilePath", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertThrows(new PreConditionFailure("destinationFilePath cannot be null."),
                        () -> file.copyTo((Path)null));
                });

                runner.test("with non-existing source file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    final Path destinationFilePath = Path.parse("/B");
                    test.assertThrows(new FileNotFoundException("/A"),
                        () -> sourceFile.copyTo(destinationFilePath).await());
                });

                runner.test("with non-existing destination file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    final Path destinationFilePath = Path.parse("/B");
                    sourceFile.copyTo(destinationFilePath).await();
                    test.assertTrue(fileSystem.fileExists(destinationFilePath).await());
                    test.assertEqual(fileSystem.getFileContentsAsString(destinationFilePath).await(), "hello");
                });

                runner.test("with existing destination file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    final Path destinationFilePath = Path.parse("/B");
                    fileSystem.setFileContentAsString(destinationFilePath, "there").await();
                    sourceFile.copyTo(destinationFilePath).await();
                    test.assertTrue(fileSystem.fileExists(destinationFilePath).await());
                    test.assertEqual(fileSystem.getFileContentsAsString(destinationFilePath).await(), "hello");
                });
            });

            runner.testGroup("copyTo(File)", () ->
            {
                runner.test("with null destinationFile", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertThrows(new PreConditionFailure("destinationFile cannot be null."),
                        () -> file.copyTo((File)null));
                });

                runner.test("with non-existing source file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    final File destinationFile = getFile(fileSystem, "/B");
                    test.assertThrows(new FileNotFoundException("/A"),
                        () -> sourceFile.copyTo(destinationFile).await());
                });

                runner.test("with non-existing destination file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    final File destinationFile = getFile(fileSystem, "/B");
                    sourceFile.copyTo(destinationFile).await();
                    test.assertTrue(destinationFile.exists().await());
                    test.assertEqual(destinationFile.getContentsAsString().await(), "hello");
                });

                runner.test("with existing destination file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    final File destinationFile = getFile(fileSystem, "/B");
                    destinationFile.setContentsAsString("there").await();
                    sourceFile.copyTo(destinationFile).await();
                    test.assertTrue(destinationFile.exists().await());
                    test.assertEqual(destinationFile.getContentsAsString().await(), "hello");
                });
            });

            runner.testGroup("copyToFolder(Path)", () ->
            {
                runner.test("with null destinationFolderPath", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertThrows(new PreConditionFailure("destinationFolderPath cannot be null."),
                        () -> file.copyToFolder((Path)null));
                });

                runner.test("with non-existing source file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    final Path destinationFolderPath = Path.parse("/B/");
                    test.assertThrows(new FileNotFoundException("/A"),
                        () -> sourceFile.copyToFolder(destinationFolderPath).await());
                });

                runner.test("with non-existing destination folder", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    final Path destinationFolderPath = Path.parse("/B/");
                    sourceFile.copyToFolder(destinationFolderPath).await();
                    test.assertTrue(fileSystem.fileExists("/B/A").await());
                    test.assertEqual(fileSystem.getFileContentsAsString("/B/A").await(), "hello");
                });

                runner.test("with non-existing destination file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    final Path destinationFolderPath = Path.parse("/B");
                    fileSystem.createFolder(destinationFolderPath).await();
                    sourceFile.copyToFolder(destinationFolderPath).await();
                    test.assertTrue(fileSystem.fileExists("/B/A").await());
                    test.assertEqual(fileSystem.getFileContentsAsString("/B/A").await(), "hello");
                });

                runner.test("with existing destination file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    fileSystem.setFileContentAsString("/B", "there").await();
                    sourceFile.copyToFolder(Path.parse("/B")).await();
                    test.assertTrue(fileSystem.fileExists("/B/A").await());
                    test.assertEqual(fileSystem.getFileContentsAsString("/B/A").await(), "hello");
                });
            });

            runner.testGroup("copyToFolder(Folder)", () ->
            {
                runner.test("with null destinationFolder", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertThrows(new PreConditionFailure("destinationFolder cannot be null."),
                        () -> file.copyToFolder((Folder)null));
                });

                runner.test("with non-existing source file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    final Folder destinationFolder = fileSystem.getFolder("/B").await();
                    test.assertThrows(new FileNotFoundException("/A"),
                        () -> sourceFile.copyToFolder(destinationFolder).await());
                });

                runner.test("with non-existing destination folder", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    final Folder destinationFolder = fileSystem.getFolder("/B/").await();
                    sourceFile.copyToFolder(destinationFolder).await();
                    test.assertTrue(destinationFolder.fileExists("A").await());
                    test.assertEqual(destinationFolder.getFileContentsAsString("A").await(), "hello");
                });

                runner.test("with non-existing destination file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    final Folder destinationFolder = fileSystem.getFolder("/B").await();
                    destinationFolder.create().await();
                    sourceFile.copyToFolder(destinationFolder).await();
                    test.assertTrue(destinationFolder.fileExists("A").await());
                    test.assertEqual(destinationFolder.getFileContentsAsString("A").await(), "hello");
                });

                runner.test("with existing destination file", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File sourceFile = getFile(fileSystem, "/A");
                    sourceFile.setContentsAsString("hello").await();
                    final Folder destinationFolder = fileSystem.getFolder("/B").await();
                    destinationFolder.create().await();
                    destinationFolder.setFileContentsAsString("A", "there").await();
                    sourceFile.copyToFolder(destinationFolder).await();
                    test.assertTrue(destinationFolder.fileExists("A").await());
                    test.assertEqual(destinationFolder.getFileContentsAsString("A").await(), "hello");
                });
            });
        });
    }

    static FileSystem getFileSystem(Test test)
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
        fileSystem.createRoot("/").await();
        return fileSystem;
    }

    static File getFile(Test test)
    {
        final FileSystem fileSystem = getFileSystem(test);
        return FileTests.getFile(fileSystem);
    }

    static File getFile(FileSystem fileSystem)
    {
        return FileTests.getFile(fileSystem, "/A");
    }

    static File getFile(FileSystem fileSystem, String filePath)
    {
        return new File(fileSystem, filePath);
    }
}
