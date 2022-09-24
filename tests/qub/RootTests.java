package qub;

public interface RootTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(Root.class, () ->
        {
            runner.testGroup("create(FileSystem,Path)", () ->
            {
                runner.test("with null fileSystem", (Test test) ->
                {
                    test.assertThrows(() -> Root.create(null, Path.parse("/path/to/root/")),
                        new PreConditionFailure("fileSystem cannot be null."));
                });

                runner.test("with null path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertThrows(() -> Root.create(fileSystem, null),
                        new PreConditionFailure("rootPath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    test.assertThrows(() -> Root.create(fileSystem, Path.parse("hello")),
                        new PreConditionFailure("rootPath.isRooted() cannot be false."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Path path = Path.parse("/path/to/root/");
                    final Root root = Root.create(fileSystem, path);
                    test.assertNotNull(root);
                    test.assertEqual(path, root.getPath());
                });
            });

            runner.testGroup("equals()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertFalse(root.equals(null));
                });

                runner.test("with String", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertFalse(root.equals(root.toString()));
                });

                runner.test("with same Root", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertTrue(root.equals((Object)root));
                });

                runner.test("with equal Root", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = getFileSystem(test);
                    final Root root = RootTests.getRoot(fileSystem);
                    final Root root2 = RootTests.getRoot(fileSystem);
                    test.assertTrue(root.equals((Object)root2));
                });

                runner.test("with different Root", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = getFileSystem(test);
                    final Root root = RootTests.getRoot(fileSystem, "C:\\");
                    final Root root2 = RootTests.getRoot(fileSystem, "D:\\");
                    test.assertFalse(root.equals((Object)root2));
                });
            });

            runner.testGroup("getTotalDataSize()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.getRoot("/").await();
                    test.assertThrows(() -> root.getTotalDataSize().await(),
                        new RootNotFoundException("/"));
                });

                runner.test("when root exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("/", DataSize.gigabytes(256)).await();
                    test.assertEqual(DataSize.gigabytes(256), root.getTotalDataSize().await());
                });
            });

            runner.testGroup("getUnusedDataSize()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.getRoot("/").await();
                    test.assertThrows(() -> root.getUnusedDataSize().await(),
                        new RootNotFoundException("/"));
                });

                runner.test("when root exists but is empty", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("/", DataSize.gigabytes(256)).await();
                    test.assertEqual(DataSize.gigabytes(256), root.getUnusedDataSize().await());
                });

                runner.test("when root exists but has a file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("/", DataSize.kilobytes(1)).await();
                    root.createFile("hello.txt").await().setContentsAsString("hello").await();
                    test.assertEqual(DataSize.bytes(995), root.getUnusedDataSize().await());
                });

                runner.test("when root exists but has a file in a subfolder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("/", DataSize.kilobytes(1)).await();
                    root.createFolder("folder").await()
                        .createFile("hello.txt").await()
                        .setContentsAsString("hello").await();
                    test.assertEqual(DataSize.bytes(995), root.getUnusedDataSize().await());
                });
            });

            runner.testGroup("getUsedDataSize()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.getRoot("/").await();
                    test.assertThrows(() -> root.getUsedDataSize().await(),
                        new RootNotFoundException("/"));
                });

                runner.test("when root exists but is empty", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("/", DataSize.gigabytes(256)).await();
                    test.assertEqual(DataSize.zero, root.getUsedDataSize().await());
                });

                runner.test("when root exists but has a file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("/", DataSize.kilobytes(1)).await();
                    root.createFile("hello.txt").await().setContentsAsString("hello").await();
                    test.assertEqual(DataSize.bytes(5), root.getUsedDataSize().await(), DataSize.bytes(0.0001));
                });

                runner.test("when root exists but has a file in a subfolder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Root root = fileSystem.createRoot("/", DataSize.kilobytes(1)).await();
                    root.createFolder("folder").await()
                        .createFile("hello.txt").await()
                        .setContentsAsString("hello").await();
                    test.assertEqual(DataSize.bytes(5), root.getUsedDataSize().await(), DataSize.bytes(0.0001));
                });
            });

            runner.testGroup("getFolder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.getFolder((String)null), new PreConditionFailure("folderRelativePath cannot be null."));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.getFolder(""), new PreConditionFailure("folderRelativePath cannot be empty."));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    final Folder folder = root.getFolder("folderName").await();
                    test.assertEqual("/folderName/", folder.toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);

                    root.createFolder("folderName").await();

                    final Folder folder = root.getFolder("folderName").await();
                    test.assertEqual("/folderName/", folder.toString());
                });
            });

            runner.testGroup("getFolder(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.getFolder((Path)null), new PreConditionFailure("folderRelativePath cannot be null."));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual("/folderName/", root.getFolder(Path.parse("folderName")).await().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFolder("folderName").await();

                    test.assertEqual("/folderName/", root.getFolder(Path.parse("folderName")).await().toString());
                });
            });

            runner.testGroup("getFile(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.getFile((String)null), new PreConditionFailure("fileRelativePath cannot be null."));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.getFile(""), new PreConditionFailure("fileRelativePath cannot be empty."));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual("/fileName", root.getFile("fileName").await().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("fileName").await();

                    test.assertEqual("/fileName", root.getFile("fileName").await().toString());
                });
            });

            runner.testGroup("getFile(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.getFile((Path)null), new PreConditionFailure("relativeFilePath cannot be null."));
                });

                runner.test("with relative Path that doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual("/fileName", root.getFile(Path.parse("fileName")).await().toString());
                });

                runner.test("with relative Path that exists", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("fileName").await();

                    test.assertEqual("/fileName", root.getFile(Path.parse("fileName")).await().toString());
                });
            });

            runner.testGroup("createFolder(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.createFolder((String)null), new PreConditionFailure("folderRelativePath cannot be null."));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.createFolder(""), new PreConditionFailure("folderRelativePath cannot be empty."));
                });

                runner.test("with relative Path with non-existing folder", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    final Folder folder = root.createFolder("folderName").await();
                    test.assertEqual("/folderName/", folder.toString());
                });

                runner.test("with relative Path when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    test.assertThrows(() -> root.createFolder("folderName").await(),
                        new RootNotFoundException("C:"));
                });

                runner.test("with relative Path with folder that exists", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFolder("A").await();

                    test.assertThrows(() -> root.createFolder("A").await(),
                        new FolderAlreadyExistsException("/A"));
                });
            });

            runner.testGroup("createFolder(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.createFolder((Path)null), new PreConditionFailure("relativeFolderPath cannot be null."));
                });

                runner.test("with non-existing folder", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    final Folder result = root.createFolder(Path.parse("folderName")).await();
                    test.assertNotNull(result);
                    test.assertEqual("/folderName/", result.toString());
                });

                runner.test("with non-existing folder when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    final Result<Folder> result = root.createFolder(Path.parse("folderName"));
                    test.assertThrows(result::await, new RootNotFoundException("C:"));
                });

                runner.test("with existing folder", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFolder("A").await();
                    test.assertThrows(() -> root.createFolder(Path.parse("A")).await(),
                        new FolderAlreadyExistsException("/A"));
                });
            });

            runner.testGroup("createFile(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.createFile((String)null), new PreConditionFailure("fileRelativePath cannot be null."));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.createFile(""), new PreConditionFailure("fileRelativePath cannot be empty."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual("/fileName", root.createFile("fileName").await().toString());
                });

                runner.test("with non-exisitng file when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    final Result<File> result = root.createFile("fileName");
                    test.assertThrows(result::await, new RootNotFoundException("C:"));
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("A").await();
                    test.assertThrows(() -> root.createFile("A").await(),
                        new FileAlreadyExistsException("/A"));
                });
            });

            runner.testGroup("createFile(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertThrows(() -> root.createFile((Path)null), new PreConditionFailure("fileRelativePath cannot be null."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual("/fileName", root.createFile(Path.parse("fileName")).await().toString());
                });

                runner.test("with non-existing file when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    test.assertThrows(() -> root.createFile(Path.parse("fileName")).await(),
                        new RootNotFoundException("C:"));
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("A").await();
                    test.assertThrows(() -> root.createFile(Path.parse("A")).await(),
                        new FileAlreadyExistsException("/A"));
                });
            });

            runner.testGroup("iterateFolders()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    test.assertThrows(() -> root.iterateFolders().toList(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual(Iterable.create(), root.iterateFolders().toList());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("thing.txt").await();
                    test.assertEqual(Iterable.create(), root.iterateFolders().toList());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFolder("things").await();
                    test.assertEqual(Iterable.create(root.getFolder("things").await()), root.iterateFolders().toList());
                });
            });

            runner.testGroup("iterateFiles()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    test.assertThrows(() -> root.iterateFiles().toList(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual(Iterable.create(), root.iterateFiles().toList());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("thing.txt").await();

                    test.assertEqual(
                        Iterable.create(root.getFile("thing.txt").await()),
                        root.iterateFiles().toList());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFolder("things").await();

                    test.assertEqual(Iterable.create(), root.iterateFiles().toList());
                });
            });

            runner.testGroup("iterateEntries()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    test.assertThrows(() -> root.iterateEntries().toList(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual(Iterable.create(), root.iterateEntries().toList());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("thing.txt").await();

                    test.assertEqual(
                        Iterable.create(root.getFile("thing.txt").await()),
                        root.iterateEntries().toList());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFolder("things").await();

                    test.assertEqual(
                        Iterable.create(root.getFolder("things").await()),
                        root.iterateEntries().toList());
                });
            });

            runner.testGroup("iterateEntriesRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    test.assertThrows(() -> root.iterateEntriesRecursively().toList(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual(Iterable.create(), root.iterateEntriesRecursively().toList());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("1.txt").await();
                    root.createFile("2.txt").await();
                    test.assertEqual(
                        Iterable.create(
                            root.getFile("1.txt").await(),
                            root.getFile("2.txt").await()),
                        root.iterateEntriesRecursively().toList());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFolder("1.txt").await();
                    root.createFolder("2.txt").await();
                    test.assertEqual(
                        Iterable.create(
                            root.getFolder("1.txt").await(),
                            root.getFolder("2.txt").await()),
                        root.iterateEntriesRecursively().toList());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("1.txt").await();
                    root.createFile("2.txt").await();
                    root.createFile("A/3.csv").await();
                    root.createFile("B/C/4.xml").await();
                    root.createFile("A/5.png").await();

                    test.assertEqual(
                        Iterable.create(
                            root.getFolder("A").await(),
                            root.getFolder("B").await(),
                            root.getFile("1.txt").await(),
                            root.getFile("2.txt").await(),
                            root.getFile("A/3.csv").await(),
                            root.getFile("A/5.png").await(),
                            root.getFolder("B/C").await(),
                            root.getFile("B/C/4.xml").await()),
                        root.iterateEntriesRecursively().toList());
                });
            });

            runner.testGroup("iterateFilesRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    test.assertThrows(() -> root.iterateFilesRecursively().toList(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual(Iterable.create(), root.iterateFilesRecursively().toList());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("1.txt").await();
                    root.createFile("2.txt").await();
                    test.assertEqual(
                        Iterable.create(
                            root.getFile("1.txt").await(),
                            root.getFile("2.txt").await()),
                        root.iterateFilesRecursively().toList());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFolder("1.txt").await();
                    root.createFolder("2.txt").await();
                    test.assertEqual(
                        Iterable.create(),
                        root.iterateFilesRecursively().toList());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
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
                        root.iterateFilesRecursively().toList());
                });
            });

            runner.testGroup("iterateFoldersRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test, "C:/");
                    test.assertThrows(() -> root.iterateFoldersRecursively().toList(),
                        new RootNotFoundException("C:"));
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    test.assertEqual(Iterable.create(), root.iterateFoldersRecursively().toList());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFile("1.txt").await();
                    root.createFile("2.txt").await();
                    test.assertEqual(
                        Iterable.create(),
                        root.iterateFoldersRecursively().toList());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
                    root.createFolder("1.txt").await();
                    root.createFolder("2.txt").await();
                    test.assertEqual(
                        Iterable.create(
                            root.getFolder("1.txt").await(),
                            root.getFolder("2.txt").await()),
                        root.iterateFoldersRecursively().toList());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = RootTests.getRoot(test);
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
                        root.iterateFoldersRecursively().toList());
                });
            });
        });
    }

    static InMemoryFileSystem getFileSystem(Test test)
    {
        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
        fileSystem.createRoot("/").await();
        return fileSystem;
    }

    static Root getRoot(Test test)
    {
        return RootTests.getRoot(test, "/");
    }

    static Root getRoot(Test test, String rootPath)
    {
        final InMemoryFileSystem fileSystem = getFileSystem(test);
        return RootTests.getRoot(fileSystem, rootPath);
    }

    static Root getRoot(FileSystem fileSystem)
    {
        return RootTests.getRoot(fileSystem, "/");
    }

    static Root getRoot(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.getRoot(rootPath).await();
    }
}
