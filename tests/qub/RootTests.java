package qub;

public class RootTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(Root.class, () ->
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
                    final Result<Folder> folder = root.getFolder((String)null);
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), folder);
                });
                
                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> folder = root.getFolder("");
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), folder);
                });
                
                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> folder = root.getFolder("folderName");
                    test.assertSuccess(folder);
                    test.assertEqual("/folderName", folder.getValue().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);

                    root.createFolder("folderName");
                    test.assertEqual(0, test.getMainAsyncRunner().getScheduledTaskCount());

                    final Result<Folder> folder = root.getFolder("folderName");
                    test.assertSuccess(folder);
                    test.assertEqual("/folderName", folder.getValue().toString());
                });
            });

            runner.testGroup("getFolder(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), root.getFolder((Path)null));
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), root.getFolder(Path.parse("")));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> folder = root.getFolder(Path.parse("folderName"));
                    test.assertSuccess(folder);
                    test.assertEqual("/folderName", folder.getValue().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("folderName");

                    final Result<Folder> folder = root.getFolder(Path.parse("folderName"));
                    test.assertSuccess(folder);
                    test.assertEqual("/folderName", folder.getValue().toString());
                });
            });

            runner.testGroup("getFile(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> file = root.getFile((String)null);
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), file);
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> file = root.getFile("");
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), file);
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> file = root.getFile("fileName");
                    test.assertSuccess(file);
                    test.assertEqual("/fileName", file.getValue().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("fileName");

                    final Result<File> file = root.getFile("fileName");
                    test.assertSuccess(file);
                    test.assertEqual("/fileName", file.getValue().toString());
                });
            });

            runner.testGroup("getFile(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> file = root.getFile((Path)null);
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), file);
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> file = root.getFile(Path.parse(""));
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), file);
                });

                runner.test("with relative Path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> file = root.getFile(Path.parse("fileName"));
                    test.assertSuccess(file);
                    test.assertEqual("/fileName", file.getValue().toString());
                });

                runner.test("with relative Path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("fileName");

                    final Result<File> file = root.getFile(Path.parse("fileName"));
                    test.assertSuccess(file);
                    test.assertEqual("/fileName", file.getValue().toString());
                });
            });

            runner.testGroup("createFolder(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder((String)null);
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), result);
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder("");
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), result);
                });

                runner.test("with relative Path with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder("folderName");
                    test.assertSuccess(result);
                    test.assertEqual("/folderName", result.getValue().toString());
                });

                runner.test("with relative Path when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Folder> result = root.createFolder("folderName");
                    test.assertError(new RootNotFoundException("C:"), result);
                });

                runner.test("with relative Path with folder that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("A");

                    final Result<Folder> result = root.createFolder("A");
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
                    final Result<Folder> result = root.createFolder((Path)null);
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual("relativeFolderPath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder(Path.parse(""));
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual("relativeFolderPath cannot be null.", result.getErrorMessage());
                });

                runner.test("with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> result = root.createFolder(Path.parse("folderName"));
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/folderName", result.getValue().toString());
                    test.assertFalse(result.hasError());
                });

                runner.test("with non-existing folder when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Folder> result = root.createFolder(Path.parse("folderName"));
                    test.assertError(new RootNotFoundException("C:"), result);
                });

                runner.test("with existing folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("A");

                    final Result<Folder> result = root.createFolder(Path.parse("A"));
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
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), result);
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile("");
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), result);
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile("fileName");
                    test.assertSuccess(result);
                    test.assertEqual("/fileName", result.getValue().toString());
                });

                runner.test("with non-exisitng file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<File> result = root.createFile("fileName");
                    test.assertError(new RootNotFoundException("C:"), result);
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
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), result);
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile(Path.parse(""));
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), result);
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile(Path.parse("fileName"));
                    test.assertSuccess(result);
                    test.assertEqual("/fileName", result.getValue().toString());
                });

                runner.test("with non-existing file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<File> result = root.createFile(Path.parse("fileName"));
                    test.assertError(new RootNotFoundException("C:"), result);
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
                    final Result<Iterable<Folder>> result = root.getFolders();
                    test.assertError(new FolderNotFoundException("C:"), result);
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Iterable<Folder>> result = root.getFolders();
                    test.assertSuccess(new Array<Folder>(0), result);
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt");

                    final Result<Iterable<Folder>> result = root.getFolders();
                    test.assertSuccess(new Array<Folder>(0), result);
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things");

                    final Result<Iterable<Folder>> result = root.getFolders();
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
                    final Result<Iterable<File>> result = root.getFiles();
                    test.assertError(new FolderNotFoundException("C:"), result);
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Iterable<File>> result = root.getFiles();
                    test.assertSuccess(new Array<File>(0), result);
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt");

                    final Result<Iterable<File>> result = root.getFiles();
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

                    final Result<Iterable<File>> result = root.getFiles();
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
                    test.assertError(new FolderNotFoundException("C:"), result);
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFolders();
                    test.assertSuccess(new Array<FileSystemEntry>(0), result);
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt");

                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFolders();
                    test.assertSuccess(
                        Array.fromValues(new FileSystemEntry[]
                            {
                                root.getFile("thing.txt").getValue()
                            }),
                        result);
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things");

                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFolders();
                    test.assertSuccess(
                        Array.fromValues(new FileSystemEntry[]
                            {
                                root.getFolder("things").getValue()
                            }),
                        result);
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFoldersRecursively();
                    test.assertError(new FolderNotFoundException("C:/"), result);
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertSuccess(new Array<FileSystemEntry>(0), root.getFilesAndFoldersRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertSuccess(
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
                    test.assertSuccess(
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
                    test.assertSuccess(expectedEntries, root.getFilesAndFoldersRecursively());
                });
            });

            runner.testGroup("getFilesRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertError(new FolderNotFoundException("C:/"), root.getFilesRecursively());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertSuccess(new Array<File>(0), root.getFilesRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertSuccess(
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
                    test.assertSuccess(
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
                    test.assertSuccess(expectedEntries, root.getFilesRecursively());
                });
            });

            runner.testGroup("getFoldersRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertError(new FolderNotFoundException("C:/"), root.getFoldersRecursively());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertSuccess(new Array<>(0), root.getFoldersRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertSuccess(
                        new Array<Folder>(0),
                        root.getFoldersRecursively());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertSuccess(
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

                    final Result<Iterable<Folder>> folders = root.getFoldersRecursively();
                    test.assertSuccess(
                        Array.fromValues(new Folder[]
                        {
                            root.getFolder("A").getValue(),
                            root.getFolder("B").getValue(),
                            root.getFolder("B/C").getValue()
                        }),
                        folders);
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
