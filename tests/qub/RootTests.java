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
                    test.assertThrows(() -> root.getFolder((String)null), new PreConditionFailure("folderRelativePath cannot be null."));
                });
                
                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.getFolder(""), new PreConditionFailure("folderRelativePath cannot be empty."));
                });
                
                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertSuccess(root.getFolder("folderName"),
                        (Folder folder) ->
                        {
                            test.assertEqual("/folderName", folder.toString());
                        });
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);

                    root.createFolder("folderName");
                    test.assertEqual(0, test.getMainAsyncRunner().getScheduledTaskCount());

                    test.assertSuccess(root.getFolder("folderName"),
                        (Folder folder) ->
                        {
                            test.assertEqual("/folderName", folder.toString());
                        });
                });
            });

            runner.testGroup("getFolder(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.getFolder((Path)null), new PreConditionFailure("folderRelativePath cannot be null."));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<Folder> folder = root.getFolder(Path.parse("folderName"));
                    test.assertSuccess(folder);
                    test.assertEqual("/folderName", folder.await().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("folderName");

                    final Result<Folder> folder = root.getFolder(Path.parse("folderName"));
                    test.assertSuccess(folder);
                    test.assertEqual("/folderName", folder.await().toString());
                });
            });

            runner.testGroup("getFile(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.getFile((String)null), new PreConditionFailure("fileRelativePath cannot be null."));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.getFile(""), new PreConditionFailure("fileRelativePath cannot be empty."));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> file = root.getFile("fileName");
                    test.assertSuccess(file);
                    test.assertEqual("/fileName", file.await().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("fileName");

                    final Result<File> file = root.getFile("fileName");
                    test.assertSuccess(file);
                    test.assertEqual("/fileName", file.await().toString());
                });
            });

            runner.testGroup("getFile(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.getFile((Path)null), new PreConditionFailure("relativeFilePath cannot be null."));
                });

                runner.test("with relative Path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> file = root.getFile(Path.parse("fileName"));
                    test.assertSuccess(file);
                    test.assertEqual("/fileName", file.await().toString());
                });

                runner.test("with relative Path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("fileName");

                    test.assertSuccess(root.getFile(Path.parse("fileName")),
                        (File file) ->
                        {
                            test.assertEqual("/fileName", file.toString());
                        });
                });
            });

            runner.testGroup("createFolder(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.createFolder((String)null), new PreConditionFailure("folderRelativePath cannot be null."));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.createFolder(""), new PreConditionFailure("folderRelativePath cannot be empty."));
                });

                runner.test("with relative Path with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertSuccess(root.createFolder("folderName"),
                        (Folder folder) ->
                        {
                            test.assertEqual("/folderName", folder.toString());
                        });
                });

                runner.test("with relative Path when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertThrows(() -> root.createFolder("folderName").await(),
                        new RootNotFoundException("C:"));
                });

                runner.test("with relative Path with folder that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("A").await();

                    test.assertThrows(() -> root.createFolder("A").await(),
                        new FolderAlreadyExistsException("/A"));
                });
            });

            runner.testGroup("createFolder(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.createFolder((Path)null), new PreConditionFailure("relativeFolderPath cannot be null."));
                });

                runner.test("with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Folder result = root.createFolder(Path.parse("folderName")).await();
                    test.assertNotNull(result);
                    test.assertEqual("/folderName", result.toString());
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
                    test.assertError(new FolderAlreadyExistsException("/A"), root.createFolder(Path.parse("A")));
                });
            });

            runner.testGroup("createFile(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.createFile((String)null), new PreConditionFailure("fileRelativePath cannot be null."));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.createFile(""), new PreConditionFailure("fileRelativePath cannot be empty."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    final Result<File> result = root.createFile("fileName");
                    test.assertSuccess(result);
                    test.assertEqual("/fileName", result.await().toString());
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
                    test.assertError(new FileAlreadyExistsException("/A"), root.createFile("A"));
                });
            });

            runner.testGroup("createFile(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertThrows(() -> root.createFile((Path)null), new PreConditionFailure("fileRelativePath cannot be null."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertSuccess("/fileName", root.createFile(Path.parse("fileName")).then(File::toString));
                });

                runner.test("with non-existing file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertError(new RootNotFoundException("C:"), root.createFile(Path.parse("fileName")));
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("A");
                    test.assertError(new FileAlreadyExistsException("/A"), root.createFile(Path.parse("A")));
                });
            });

            runner.testGroup("getFolders()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertError(new FolderNotFoundException("C:"), root.getFolders());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertSuccess(Iterable.create(), root.getFolders());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt");
                    test.assertSuccess(Iterable.create(), root.getFolders());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things");
                    test.assertSuccess(Iterable.create(root.getFolder("things").await()), root.getFolders());
                });
            });

            runner.testGroup("getFiles()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertError(new FolderNotFoundException("C:"), root.getFiles());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertSuccess(Iterable.create(), root.getFiles());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt");

                    final Result<Iterable<File>> result = root.getFiles();
                    test.assertNotNull(result);
                    test.assertEqual(
                        Iterable.create(root.getFile("thing.txt").await()),
                        result.await());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things");

                    final Result<Iterable<File>> result = root.getFiles();
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(), result.await());
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
                    test.assertEqual(Iterable.create(), result.await());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt");

                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFolders();
                    test.assertEqual(
                        Iterable.create(root.getFile("thing.txt").await()),
                        result.await());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things");

                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFolders();
                    test.assertEqual(
                        Iterable.create(root.getFolder("things").await()),
                        result.await());
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
                    test.assertEqual(Iterable.create(), root.getFilesAndFoldersRecursively().await());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertEqual(
                        Iterable.<FileSystemEntry>create(
                            root.getFile("1.txt").await(),
                            root.getFile("2.txt").await()),
                        root.getFilesAndFoldersRecursively().await());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertEqual(
                        Iterable.create(
                            root.getFolder("1.txt").await(),
                            root.getFolder("2.txt").await()),
                        root.getFilesAndFoldersRecursively().await());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    root.createFile("A/3.csv");
                    root.createFile("B/C/4.xml");
                    root.createFile("A/5.png");

                    final Iterable<FileSystemEntry> expectedEntries = Iterable.create(
                        root.getFolder("A").await(),
                        root.getFolder("B").await(),
                        root.getFile("1.txt").await(),
                        root.getFile("2.txt").await(),
                        root.getFile("A/3.csv").await(),
                        root.getFile("A/5.png").await(),
                        root.getFolder("B/C").await(),
                        root.getFile("B/C/4.xml").await());
                    test.assertEqual(expectedEntries, root.getFilesAndFoldersRecursively().await());
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
                    test.assertSuccess(Iterable.create(), root.getFilesRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertSuccess(
                        Iterable.create(
                            root.getFile("1.txt").await(),
                            root.getFile("2.txt").await()),
                        root.getFilesRecursively());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertSuccess(
                        Iterable.create(),
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

                    final Iterable<File> expectedEntries = Iterable.create(
                        root.getFile("1.txt").await(),
                        root.getFile("2.txt").await(),
                        root.getFile("A/3.csv").await(),
                        root.getFile("A/5.png").await(),
                        root.getFile("B/C/4.xml").await());
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
                    test.assertSuccess(Iterable.create(), root.getFoldersRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertSuccess(
                        Iterable.create(),
                        root.getFoldersRecursively());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertSuccess(
                        Iterable.create(
                            root.getFolder("1.txt").await(),
                            root.getFolder("2.txt").await()),
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
                        Iterable.create(
                            root.getFolder("A").await(),
                            root.getFolder("B").await(),
                            root.getFolder("B/C").await()),
                        folders);
                });
            });
        });
    }

    private static InMemoryFileSystem getFileSystem(Test test)
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner(), test.getClock());
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
        return fileSystem.getRoot(rootPath).await();
    }
}
