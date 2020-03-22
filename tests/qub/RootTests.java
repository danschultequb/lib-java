package qub;

public interface RootTests
{
    static void test(final TestRunner runner)
    {
        runner.testGroup(Root.class, () ->
        {
            runner.testGroup("constructor(FileSystem,Path)", () ->
            {
                runner.test("with null fileSystem", (Test test) ->
                {
                    test.assertThrows(() -> new Root(null, Path.parse("/path/to/root/")),
                        new PreConditionFailure("fileSystem cannot be null."));
                });

                runner.test("with null path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    test.assertThrows(() -> new Root(fileSystem, null),
                        new PreConditionFailure("path cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    final Path path = Path.parse("/path/to/root/");
                    final Root root = new Root(fileSystem, path);
                    test.assertNotNull(root);
                    test.assertEqual(path, root.getPath());
                });
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

            runner.testGroup("getTotalDataSize()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    final Root root = fileSystem.getRoot("/").await();
                    test.assertThrows(() -> root.getTotalDataSize().await(),
                        new RootNotFoundException("/"));
                });

                runner.test("when root exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    final Root root = fileSystem.createRoot("/", DataSize.gigabytes(256)).await();
                    test.assertEqual(DataSize.gigabytes(256), root.getTotalDataSize().await());
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
                    final Folder folder = root.getFolder("folderName").await();
                    test.assertEqual("/folderName", folder.toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);

                    root.createFolder("folderName").await();

                    final Folder folder = root.getFolder("folderName").await();
                    test.assertEqual("/folderName", folder.toString());
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
                    test.assertEqual("/folderName", root.getFolder(Path.parse("folderName")).await().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("folderName").await();

                    test.assertEqual("/folderName", root.getFolder(Path.parse("folderName")).await().toString());
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
                    test.assertEqual("/fileName", root.getFile("fileName").await().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("fileName").await();

                    test.assertEqual("/fileName", root.getFile("fileName").await().toString());
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
                    test.assertEqual("/fileName", root.getFile(Path.parse("fileName")).await().toString());
                });

                runner.test("with relative Path that exists", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("fileName").await();

                    test.assertEqual("/fileName", root.getFile(Path.parse("fileName")).await().toString());
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
                    final Folder folder = root.createFolder("folderName").await();
                    test.assertEqual("/folderName", folder.toString());
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
                    test.assertThrows(result::await, new RootNotFoundException("C:"));
                });

                runner.test("with existing folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("A").await();
                    test.assertThrows(() -> root.createFolder(Path.parse("A")).await(),
                        new FolderAlreadyExistsException("/A"));
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
                    test.assertEqual("/fileName", root.createFile("fileName").await().toString());
                });

                runner.test("with non-exisitng file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<File> result = root.createFile("fileName");
                    test.assertThrows(result::await, new RootNotFoundException("C:"));
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("A").await();
                    test.assertThrows(() -> root.createFile("A").await(),
                        new FileAlreadyExistsException("/A"));
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
                    test.assertEqual("/fileName", root.createFile(Path.parse("fileName")).await().toString());
                });

                runner.test("with non-existing file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertThrows(() -> root.createFile(Path.parse("fileName")).await(),
                        new RootNotFoundException("C:"));
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("A").await();
                    test.assertThrows(() -> root.createFile(Path.parse("A")).await(),
                        new FileAlreadyExistsException("/A"));
                });
            });

            runner.testGroup("getFolders()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertThrows(() -> root.getFolders().await(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertEqual(Iterable.create(), root.getFolders().await());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt").await();
                    test.assertEqual(Iterable.create(), root.getFolders().await());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things").await();
                    test.assertEqual(Iterable.create(root.getFolder("things").await()), root.getFolders().await());
                });
            });

            runner.testGroup("getFiles()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertThrows(() -> root.getFiles().await(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertEqual(Iterable.create(), root.getFiles().await());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("thing.txt").await();

                    test.assertEqual(
                        Iterable.create(root.getFile("thing.txt").await()),
                        root.getFiles().await());
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
                    test.assertThrows(result::await, new RootNotFoundException("C:"));
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
                    root.createFile("thing.txt").await();

                    test.assertEqual(
                        Iterable.create(root.getFile("thing.txt").await()),
                        root.getFilesAndFolders().await());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("things").await();

                    test.assertEqual(
                        Iterable.create(root.getFolder("things").await()),
                        root.getFilesAndFolders().await());
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    final Result<Iterable<FileSystemEntry>> result = root.getFilesAndFoldersRecursively();
                    test.assertThrows(result::await, new RootNotFoundException("C:"));
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
                    test.assertThrows(() -> root.getFilesRecursively().await(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertEqual(Iterable.create(), root.getFilesRecursively().await());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt").await();
                    root.createFile("2.txt").await();
                    test.assertEqual(
                        Iterable.create(
                            root.getFile("1.txt").await(),
                            root.getFile("2.txt").await()),
                        root.getFilesRecursively().await());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("1.txt").await();
                    root.createFolder("2.txt").await();
                    test.assertEqual(
                        Iterable.create(),
                        root.getFilesRecursively().await());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt").await();
                    root.createFile("2.txt").await();
                    root.createFile("A/3.csv").await();
                    root.createFile("B/C/4.xml").await();
                    root.createFile("A/5.png").await();

                    test.assertEqual(
                        Iterable.create(
                            root.getFile("1.txt").await(),
                            root.getFile("2.txt").await(),
                            root.getFile("A/3.csv").await(),
                            root.getFile("A/5.png").await(),
                            root.getFile("B/C/4.xml").await()),
                        root.getFilesRecursively().await());
                });
            });

            runner.testGroup("getFoldersRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot(test, "C:/");
                    test.assertThrows(() -> root.getFoldersRecursively().await(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot(test);
                    test.assertEqual(Iterable.create(), root.getFoldersRecursively().await());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt").await();
                    root.createFile("2.txt").await();
                    test.assertEqual(
                        Iterable.create(),
                        root.getFoldersRecursively().await());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFolder("1.txt").await();
                    root.createFolder("2.txt").await();
                    test.assertEqual(
                        Iterable.create(
                            root.getFolder("1.txt").await(),
                            root.getFolder("2.txt").await()),
                        root.getFoldersRecursively().await());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = getRoot(test);
                    root.createFile("1.txt").await();
                    root.createFile("2.txt").await();
                    root.createFile("A/3.csv").await();
                    root.createFile("B/C/4.xml").await();
                    root.createFile("A/5.png").await();

                    test.assertEqual(
                        Iterable.create(
                            root.getFolder("A").await(),
                            root.getFolder("B").await(),
                            root.getFolder("B/C").await()),
                        root.getFoldersRecursively().await());
                });
            });
        });
    }

    static InMemoryFileSystem getFileSystem(Test test)
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
        fileSystem.createRoot("/").await();
        return fileSystem;
    }

    static Root getRoot(Test test)
    {
        return getRoot(test, "/");
    }

    static Root getRoot(Test test, String rootPath)
    {
        final InMemoryFileSystem fileSystem = getFileSystem(test);
        return getRoot(fileSystem, rootPath);
    }

    static Root getRoot(FileSystem fileSystem)
    {
        return getRoot(fileSystem, "/");
    }

    static Root getRoot(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.getRoot(rootPath).await();
    }
}
