package qub;

public class RootTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(Root.class, runner.skip(), () ->
        {
            runner.test("constructor", (Test test) ->
            {
                final Root root = new Root(null, Path.parse("/path/to/root/"));
                test.assertEqual("/path/to/root/", root.toString());
            });
            
            runner.testGroup("equals()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertFalse(root.equals(null));
                });
                
                runner.test("with String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertFalse(root.equals(root.toString()));
                });
                
                runner.test("with same Root", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertTrue(root.equals((Object)root));
                });
                
                runner.test("with equal Root", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = getFileSystem(test);
                    final Root root = getRoot(fileSystem);
                    final Root root2 = getRoot(fileSystem);
                    test.assertTrue(root.equals((Object)root2));
                });
                
                runner.test("with different Root", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = getFileSystem(test);
                    final Root root = getRoot(fileSystem, "C:\\");
                    final Root root2 = getRoot(fileSystem, "D:\\");
                    test.assertFalse(root.equals((Object)root2));
                });
            });
            
            runner.testGroup("getFolder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertNull(root.getFolder((String)null));
                });
                
                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertNull(root.getFolder(""));
                });
                
                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Folder folder = root.getFolder("folderName").getValue();
                    test.assertNotNull(folder);
                    test.assertEqual("/folderName", folder.getPath().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("folderName");

                    final Folder folder = root.getFolder("folderName").getValue();
                    test.assertNotNull(folder);
                    test.assertEqual("/folderName", folder.getPath().toString());
                });
            });

            runner.testGroup("getFolder(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertNull(root.getFolder((Path)null));
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertNull(root.getFolder(Path.parse("")));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Folder folder = root.getFolder(Path.parse("folderName")).getValue();
                    test.assertNotNull(folder);
                    test.assertEqual("/folderName", folder.getPath().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("folderName");

                    final Folder folder = root.getFolder(Path.parse("folderName")).getValue();
                    test.assertNotNull(folder);
                    test.assertEqual("/folderName", folder.getPath().toString());
                });
            });

            runner.testGroup("getFile(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertNull(root.getFile((String)null));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertNull(root.getFile(""));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final File file = root.getFile("fileName").getValue();
                    test.assertNotNull(file);
                    test.assertEqual("/fileName", file.getPath().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("fileName");

                    final File file = root.getFile("fileName").getValue();
                    test.assertNotNull(file);
                    test.assertEqual("/fileName", file.getPath().toString());
                });
            });

            runner.testGroup("getFile(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertNull(root.getFile((Path)null));
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertNull(root.getFile(Path.parse("")));
                });

                runner.test("with relative Path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final File file = root.getFile(Path.parse("fileName")).getValue();
                    test.assertNotNull(file);
                    test.assertEqual("/fileName", file.getPath().toString());
                });

                runner.test("with relative Path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("fileName");

                    final File file = root.getFile(Path.parse("fileName")).getValue();
                    test.assertNotNull(file);
                    test.assertEqual("/fileName", file.getPath().toString());
                });
            });

            runner.testGroup("createFolder(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder((String)null).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual("rootedFolderPath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder("").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual("rootedFolderPath cannot be null.", result.getErrorMessage());
                });

                runner.test("with relative Path with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder("folderName").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/folderName", result.getValue().toString());
                    test.assertFalse(result.hasError());
                });

                runner.test("with relative Path when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Folder> result = root.createFolder("folderName").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(RootNotFoundException.class, result.getError().getClass());
                    test.assertEqual("The root \"C:/\" doesn't exist.", result.getErrorMessage());
                });

                runner.test("with relative Path with folder that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("A").await();

                    final Result<Folder> result = root.createFolder("A").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/A", result.getValue().toString());
                    test.assertTrue(result.hasError());
                    test.assertEqual(FolderAlreadyExistsException.class, result.getError().getClass());
                    test.assertEqual("The folder at \"/A\" already exists.", result.getErrorMessage());
                });
            });

            runner.testGroup("createFolder(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder((Path)null).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual("rootedFolderPath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder(Path.parse("")).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual("rootedFolderPath cannot be null.", result.getErrorMessage());
                });

                runner.test("with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder(Path.parse("folderName")).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/folderName", result.getValue().toString());
                    test.assertFalse(result.hasError());
                });

                runner.test("with non-existing folder when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Folder> result = root.createFolder(Path.parse("folderName")).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(RootNotFoundException.class, result.getErrorType());
                    test.assertEqual("rootedFolderPath cannot be null.", result.getErrorMessage());
                });

                runner.test("with existing folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("A");

                    final Result<Folder> result = root.createFolder(Path.parse("A")).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/A", result.getValue().toString());
                    test.assertTrue(result.hasError());
                    test.assertEqual(FolderAlreadyExistsException.class, result.getErrorType());
                    test.assertEqual("The folder at \"/A\" already exists.", result.getErrorMessage());
                });
            });

            runner.testGroup("createFile(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile((String)null);
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null or empty.", result.getErrorMessage());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile("");
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null or empty.", result.getErrorMessage());
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile("fileName");
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/fileName", result.getValue().toString());
                    test.assertFalse(result.hasError());
                });

                runner.test("with non-exisitng file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<File> result = root.createFile("fileName");
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(RootNotFoundException.class, result.getErrorType());
                    test.assertEqual("The root \"C:/\" doesn't exist.", result.getErrorMessage());
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("A");

                    final Result<File> result = root.createFile("A");
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/A", result.getValue().toString());
                    test.assertTrue(result.hasError());
                    test.assertEqual(FileAlreadyExistsException.class, result.getErrorType());
                    test.assertEqual("The file at \"/A\" already exists.", result.getErrorMessage());
                });
            });

            runner.testGroup("createFile(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile((Path)null);
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null or empty.", result.getErrorMessage());
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile(Path.parse(""));
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null or empty.", result.getErrorMessage());
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile(Path.parse("fileName"));
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/fileName", result.getValue().toString());
                    test.assertFalse(result.hasError());
                });

                runner.test("with non-existing file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<File> result = root.createFile(Path.parse("fileName"));
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(RootNotFoundException.class, result.getErrorType());
                    test.assertEqual("The root at \"C:/\" doesn't exist.", result.getErrorMessage());
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("A");

                    final Result<File> result = root.createFile(Path.parse("A"));
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/A", result.getValue().toString());
                    test.assertTrue(result.hasError());
                    test.assertEqual(FileAlreadyExistsException.class, result.getErrorType());
                    test.assertEqual("The file at \"/A\" already exists.", result.getErrorMessage());
                });
            });

            runner.testGroup("getFolders()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Iterable<Folder>> result = root.getFolders().awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(RootNotFoundException.class, result.getErrorType());
                    test.assertEqual("The root at \"C:/\" doesn't exist.", result.getErrorMessage());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Iterable<Folder>> result = root.getFolders().awaitReturn();
                    test.assertNotNull(result);
                    test.assertEqual(new Array<Folder>(0), result.getValue());
                    test.assertFalse(result.hasError());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt");

                    final Result<Iterable<Folder>> result = root.getFolders().awaitReturn();
                    test.assertNotNull(result);
                    test.assertEqual(new Array<Folder>(0), result.getValue());
                    test.assertFalse(result.hasError());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things");

                    final Result<Iterable<Folder>> result = root.getFolders().awaitReturn();
                    test.assertNotNull(result);
                    test.assertEqual(
                        Array.fromValues(new Folder[]
                        {
                            root.getFolder("things").getValue()
                        }),
                        result.getValue());
                    test.assertFalse(result.hasError());
                });
            });

            runner.testGroup("getFiles()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Iterable<File>> result = root.getFiles().awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(RootNotFoundException.class, result.getErrorType());
                    test.assertEqual("The root at \"C:/\" doesn't exist.", result.getErrorMessage());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Iterable<File>> result = root.getFiles().awaitReturn();
                    test.assertNotNull(result);
                    test.assertEqual(new Array<File>(0), result.getValue());
                    test.assertFalse(result.hasError());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt");

                    final Result<Iterable<File>> result = root.getFiles().awaitReturn();
                    test.assertNotNull(result);
                    test.assertEqual(
                        Array.fromValues(new File[]
                        {
                            root.getFile("thing.txt").getValue()
                        }),
                        result.getValue());
                    test.assertFalse(result.hasError());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things");

                    final Result<Iterable<File>> result = root.getFiles().awaitReturn();
                    test.assertNotNull(result);
                    test.assertEqual(new Array<File>(0), result.getValue());
                    test.assertFalse(result.hasError());
                });
            });

            runner.testGroup("getFilesAndFolders()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFolders();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(RootNotFoundException.class, result.getErrorType());
                    test.assertEqual("The root at \"C:/\" doesn't exist.", result.getErrorMessage());

                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFolders();
                    test.assertNotNull(result);
                    test.assertEqual(new Array<FileSystemEntry>(0), result.getValue());
                    test.assertFalse(result.hasError());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt");

                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFolders();
                    test.assertNotNull(result);
                    test.assertEqual(
                        Array.fromValues(new FileSystemEntry[]
                            {
                                root.getFile("thing.txt").getValue()
                            }),
                        result.getValue());
                    test.assertFalse(result.hasError());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things");

                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFolders();
                    test.assertNotNull(result);
                    test.assertEqual(
                        Array.fromValues(new FileSystemEntry[]
                            {
                                root.getFolder("things").getValue()
                            }),
                        result.getValue());
                    test.assertFalse(result.hasError());
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFoldersRecursively().awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(RootNotFoundException.class, result.getErrorType());
                    test.assertEqual("The root at \"C:/\" doesn't exist.", result.getErrorMessage());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertEqual(new Array<FileSystemEntry>(0), root.getFilesAndFoldersRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertEqual(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            root.getFile("1.txt").getValue(),
                            root.getFile("2.txt").getValue()
                        }),
                        root.getFilesAndFoldersRecursively());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertEqual(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            root.getFolder("1.txt").getValue(),
                            root.getFolder("2.txt").getValue()
                        }),
                        root.getFilesAndFoldersRecursively());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    root.createFile("A/3.csv");
                    root.createFile("B/C/4.xml");
                    root.createFile("A/5.png");

                    final Iterable<FileSystemEntry> expectedEntries =
                        Array.fromValues(new FileSystemEntry[]
                        {
                            root.getFolder("A").getValue(),
                            root.getFolder("B").getValue(),
                            root.getFile("1.txt").getValue(),
                            root.getFile("2.txt").getValue(),
                            root.getFile("A/3.csv").getValue(),
                            root.getFile("A/5.png").getValue(),
                            root.getFolder("B/C").getValue(),
                            root.getFile("B/C/4.xml").getValue()
                        });
                    final Iterable<FileSystemEntry> actualEntries = root.getFilesAndFoldersRecursively().awaitReturn().getValue();
                    test.assertEqual(expectedEntries, actualEntries);
                });
            });

            runner.testGroup("getFilesRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertEqual(new Array<File>(0), root.getFilesRecursively());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertEqual(new Array<File>(0), root.getFilesRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertEqual(
                        Array.fromValues(new File[]
                        {
                            root.getFile("1.txt").getValue(),
                            root.getFile("2.txt").getValue()
                        }),
                        root.getFilesRecursively());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertEqual(
                        new Array<File>(0),
                        root.getFilesRecursively());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    root.createFile("A/3.csv");
                    root.createFile("B/C/4.xml");
                    root.createFile("A/5.png");

                    final Iterable<File> expectedEntries =
                        Array.fromValues(new File[]
                        {
                            root.getFile("1.txt").getValue(),
                            root.getFile("2.txt").getValue(),
                            root.getFile("A/3.csv").getValue(),
                            root.getFile("A/5.png").getValue(),
                            root.getFile("B/C/4.xml").getValue()
                        });
                    final Iterable<File> actualEntries = root.getFilesRecursively().awaitReturn().getValue();
                    test.assertEqual(expectedEntries, actualEntries);
                });
            });

            runner.testGroup("getFoldersRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertEqual(new Array<Folder>(0), root.getFoldersRecursively().awaitReturn().getValue());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertEqual(new Array<Folder>(0), root.getFoldersRecursively().awaitReturn().getValue());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertEqual(
                        new Array<Folder>(0),
                        root.getFoldersRecursively());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertEqual(
                        Array.fromValues(new Folder[]
                        {
                            root.getFolder("1.txt").getValue(),
                            root.getFolder("2.txt").getValue()
                        }),
                        root.getFoldersRecursively());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    root.createFile("A/3.csv");
                    root.createFile("B/C/4.xml");
                    root.createFile("A/5.png");

                    final Iterable<Folder> expectedEntries =
                        Array.fromValues(new Folder[]
                        {
                            root.getFolder("A").getValue(),
                            root.getFolder("B").getValue(),
                            root.getFolder("B/C").getValue()
                        });
                    final Iterable<Folder> actualEntries = root.getFoldersRecursively().awaitReturn().getValue();
                    test.assertEqual(expectedEntries, actualEntries);
                });
            });
        });
    }

    private static InMemoryFileSystem getFileSystem(Test test)
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
        fileSystem.createRoot("/");
        return fileSystem;
    }

    private static Root getRoot(Test test)
    {
        return getRoot(test, "/");
    }

    private static Root getRoot(Test test, String rootPath)
    {
        final InMemoryFileSystem fileSystem = getFileSystem(test);
        return getRoot(fileSystem, rootPath);
    }

    private static Root getRoot(FileSystem fileSystem)
    {
        return getRoot(fileSystem, "/");
    }

    private static Root getRoot(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.getRoot(rootPath).getValue();
    }
}
