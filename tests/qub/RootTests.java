package qub;

public class RootTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Root", () ->
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
                    final Root root = getRoot();
                    test.assertFalse(root.equals(null));
                });
                
                runner.test("with String", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertFalse(root.equals(root.toString()));
                });
                
                runner.test("with same Root", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertTrue(root.equals((Object)root));
                });
                
                runner.test("with equal Root", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = getFileSystem();
                    final Root root = getRoot(fileSystem);
                    final Root root2 = getRoot(fileSystem);
                    test.assertTrue(root.equals((Object)root2));
                });
                
                runner.test("with different Root", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = getFileSystem();
                    final Root root = getRoot(fileSystem, "C:\\");
                    final Root root2 = getRoot(fileSystem, "D:\\");
                    test.assertFalse(root.equals((Object)root2));
                });
            });
            
            runner.testGroup("getFolder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertNull(root.getFolder((String)null));
                });
                
                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertNull(root.getFolder(""));
                });
                
                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot();
                    final Folder folder = root.getFolder("folderName");
                    test.assertNotNull(folder);
                    test.assertEqual("/folderName", folder.getPath().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("folderName");

                    final Folder folder = root.getFolder("folderName");
                    test.assertNotNull(folder);
                    test.assertEqual("/folderName", folder.getPath().toString());
                });
            });

            runner.testGroup("getFolder(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertNull(root.getFolder((Path)null));
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertNull(root.getFolder(Path.parse("")));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot();
                    final Folder folder = root.getFolder(Path.parse("folderName"));
                    test.assertNotNull(folder);
                    test.assertEqual("/folderName", folder.getPath().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("folderName");

                    final Folder folder = root.getFolder(Path.parse("folderName"));
                    test.assertNotNull(folder);
                    test.assertEqual("/folderName", folder.getPath().toString());
                });
            });

            runner.testGroup("getFile(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertNull(root.getFile((String)null));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertNull(root.getFile(""));
                });

                runner.test("with relative path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot();
                    final File file = root.getFile("fileName");
                    test.assertNotNull(file);
                    test.assertEqual("/fileName", file.getPath().toString());
                });

                runner.test("with relative path that exists", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("fileName");

                    final File file = root.getFile("fileName");
                    test.assertNotNull(file);
                    test.assertEqual("/fileName", file.getPath().toString());
                });
            });

            runner.testGroup("getFile(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertNull(root.getFile((Path)null));
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertNull(root.getFile(Path.parse("")));
                });

                runner.test("with relative Path that doesn't exist", (Test test) ->
                {
                    final Root root = getRoot();
                    final File file = root.getFile(Path.parse("fileName"));
                    test.assertNotNull(file);
                    test.assertEqual("/fileName", file.getPath().toString());
                });

                runner.test("with relative Path that exists", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("fileName");

                    final File file = root.getFile(Path.parse("fileName"));
                    test.assertNotNull(file);
                    test.assertEqual("/fileName", file.getPath().toString());
                });
            });

            runner.testGroup("createFolder(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertFalse(root.createFolder((String)null));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertFalse(root.createFolder(""));
                });

                runner.test("with relative Path with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertTrue(root.createFolder("folderName"));
                });

                runner.test("with relative Path when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertFalse(root.createFolder("folderName"));
                });

                runner.test("with relative Path with folder that exists", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("A");

                    test.assertFalse(root.createFolder("A"));
                });
            });

            runner.testGroup("createFolder(String,Value<Folder>)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<Folder> createdFolder = new Value<>();
                    test.assertFalse(root.createFolder((String)null, createdFolder));
                    test.assertFalse(createdFolder.hasValue());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<Folder> createdFolder = new Value<>();
                    test.assertFalse(root.createFolder(""));
                    test.assertFalse(createdFolder.hasValue());
                });

                runner.test("with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<Folder> createdFolder = new Value<>();
                    test.assertTrue(root.createFolder("folderName", createdFolder));
                    test.assertEqual(Path.parse("/folderName"), createdFolder.get().getPath());
                });

                runner.test("with non-existing folder when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    final Value<Folder> createdFolder = new Value<>();
                    test.assertFalse(root.createFolder("folderName", createdFolder));
                    test.assertFalse(createdFolder.hasValue());
                });

                runner.test("with existing folder", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("A");

                    final Value<Folder> createdFolder = new Value<>();
                    test.assertFalse(root.createFolder("A", createdFolder));
                    test.assertEqual(Path.parse("/A"), createdFolder.get().getPath());
                });
            });

            runner.testGroup("createFolder(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertFalse(root.createFolder((Path)null));
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertFalse(root.createFolder(Path.parse("")));
                });

                runner.test("with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertTrue(root.createFolder(Path.parse("folderName")));
                });

                runner.test("with non-existing folder when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertFalse(root.createFolder(Path.parse("folderName")));
                });

                runner.test("with existing folder", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("A");

                    test.assertFalse(root.createFolder(Path.parse("A")));
                });
            });

            runner.testGroup("createFolder(Path,Value<Folder>)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<Folder> createdFolder = new Value<>();
                    test.assertFalse(root.createFolder((Path)null, createdFolder));
                    test.assertFalse(createdFolder.hasValue());
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<Folder> createdFolder = new Value<>();
                    test.assertFalse(root.createFolder(Path.parse("")));
                    test.assertFalse(createdFolder.hasValue());
                });

                runner.test("with non-existing folder", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<Folder> createdFolder = new Value<>();
                    test.assertTrue(root.createFolder(Path.parse("folderName"), createdFolder));
                    test.assertEqual(Path.parse("/folderName"), createdFolder.get().getPath());
                });

                runner.test("with non-existing folder when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    final Value<Folder> createdFolder = new Value<>();
                    test.assertFalse(root.createFolder(Path.parse("folderName"), createdFolder));
                    test.assertFalse(createdFolder.hasValue());
                });

                runner.test("with existing folder", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("A");

                    final Value<Folder> createdFolder = new Value<>();
                    test.assertFalse(root.createFolder(Path.parse("A"), createdFolder));
                    test.assertEqual(Path.parse("/A"), createdFolder.get().getPath());
                });
            });

            runner.testGroup("createFile(String)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertFalse(root.createFile((String)null));
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertFalse(root.createFile(""));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertTrue(root.createFile("fileName"));
                });

                runner.test("with non-exisitng file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertFalse(root.createFile("fileName"));
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("A");

                    test.assertFalse(root.createFile("A"));
                });
            });

            runner.testGroup("createFile(String,Value<File>)", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<File> createdFile = new Value<>();
                    test.assertFalse(root.createFile((String)null, createdFile));
                    test.assertFalse(createdFile.hasValue());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<File> createdFile = new Value<>();
                    test.assertFalse(root.createFile(""));
                    test.assertFalse(createdFile.hasValue());
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<File> createdFile = new Value<>();
                    test.assertTrue(root.createFile("fileName", createdFile));
                    test.assertEqual(Path.parse("/fileName"), createdFile.get().getPath());
                });

                runner.test("with non-existing file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    final Value<File> createdFile = new Value<>();
                    test.assertFalse(root.createFile("fileName", createdFile));
                    test.assertFalse(createdFile.hasValue());
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("A");

                    final Value<File> createdFile = new Value<>();
                    test.assertFalse(root.createFile("A", createdFile));
                    test.assertEqual(Path.parse("/A"), createdFile.get().getPath());
                });
            });

            runner.testGroup("createFile(Path)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertFalse(root.createFile((Path)null));
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertFalse(root.createFile(Path.parse("")));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertTrue(root.createFile(Path.parse("fileName")));
                });

                runner.test("with non-existing file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertFalse(root.createFile(Path.parse("fileName")));
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("A");

                    test.assertFalse(root.createFile(Path.parse("A")));
                });
            });

            runner.testGroup("createFile(Path,Value<File>)", () ->
            {
                runner.test("with null Path", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<File> createdFile = new Value<>();
                    test.assertFalse(root.createFile((Path)null, createdFile));
                    test.assertFalse(createdFile.hasValue());
                });

                runner.test("with empty Path", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<File> createdFile = new Value<>();
                    test.assertFalse(root.createFile(Path.parse("")));
                    test.assertFalse(createdFile.hasValue());
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final Root root = getRoot();
                    final Value<File> createdFile = new Value<>();
                    test.assertTrue(root.createFile(Path.parse("fileName"), createdFile));
                    test.assertEqual(Path.parse("/fileName"), createdFile.get().getPath());
                });

                runner.test("with non-existing file when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    final Value<File> createdFile = new Value<>();
                    test.assertFalse(root.createFile(Path.parse("fileName"), createdFile));
                    test.assertFalse(createdFile.hasValue());
                });

                runner.test("with existing file", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("A");

                    final Value<File> createdFile = new Value<>();
                    test.assertFalse(root.createFile(Path.parse("A"), createdFile));
                    test.assertEqual(Path.parse("/A"), createdFile.get().getPath());
                });
            });

            runner.testGroup("getFolders()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertEqual(new Array<Folder>(0), root.getFolders());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertEqual(new Array<Folder>(0), root.getFolders());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("thing.txt");
                    test.assertEqual(new Array<Folder>(0), root.getFolders());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("things");
                    test.assertEqual(Array.fromValues(new Folder[] { root.getFolder("things") }), root.getFolders());
                });
            });

            runner.testGroup("getFiles()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertEqual(new Array<File>(0), root.getFiles());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertEqual(new Array<File>(0), root.getFiles());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("thing.txt");

                    final Iterable<File> files = root.getFiles();
                    test.assertNotNull(files);
                    test.assertEqual(1, files.getCount());
                    test.assertEqual(root.getFile("thing.txt"), files.first());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("things");

                    final Iterable<File> files = root.getFiles();
                    test.assertNotNull(files);
                    test.assertFalse(files.any());
                });
            });

            runner.testGroup("getFilesAndFolders()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertEqual(new Array<FileSystemEntry>(0), root.getFilesAndFolders());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot();
                    final Iterable<FileSystemEntry> entries = root.getFilesAndFolders();
                    test.assertNotNull(entries);
                    test.assertFalse(entries.any());
                });

                runner.test("when root has a file", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("thing.txt");

                    final Iterable<FileSystemEntry> entries = root.getFilesAndFolders();
                    test.assertNotNull(entries);
                    test.assertEqual(1, entries.getCount());
                    test.assertEqual(root.getFile("thing.txt"), entries.first());
                });

                runner.test("when root has a folder", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("things");

                    final Iterable<FileSystemEntry> entries = root.getFilesAndFolders();
                    test.assertNotNull(entries);
                    test.assertEqual(1, entries.getCount());
                    test.assertEqual(root.getFolder("things"), entries.first());
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertEqual(new Array<FileSystemEntry>(0), root.getFilesAndFoldersRecursively());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertEqual(new Array<FileSystemEntry>(0), root.getFilesAndFoldersRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertEqual(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            root.getFile("1.txt"),
                            root.getFile("2.txt")
                        }),
                        root.getFilesAndFoldersRecursively());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertEqual(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            root.getFolder("1.txt"),
                            root.getFolder("2.txt")
                        }),
                        root.getFilesAndFoldersRecursively());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    root.createFile("A/3.csv");
                    root.createFile("B/C/4.xml");
                    root.createFile("A/5.png");

                    final Iterable<FileSystemEntry> expectedEntries =
                        Array.fromValues(new FileSystemEntry[]
                        {
                            root.getFolder("A"),
                            root.getFolder("B"),
                            root.getFile("1.txt"),
                            root.getFile("2.txt"),
                            root.getFile("A/3.csv"),
                            root.getFile("A/5.png"),
                            root.getFolder("B/C"),
                            root.getFile("B/C/4.xml")
                        });
                    final Iterable<FileSystemEntry> actualEntries = root.getFilesAndFoldersRecursively();
                    test.assertEqual(expectedEntries, actualEntries);
                });
            });

            runner.testGroup("getFilesRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertEqual(new Array<File>(0), root.getFilesRecursively());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertEqual(new Array<File>(0), root.getFilesRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertEqual(
                        Array.fromValues(new File[]
                        {
                            root.getFile("1.txt"),
                            root.getFile("2.txt")
                        }),
                        root.getFilesRecursively());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertEqual(
                        new Array<File>(0),
                        root.getFilesRecursively());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    root.createFile("A/3.csv");
                    root.createFile("B/C/4.xml");
                    root.createFile("A/5.png");

                    final Iterable<File> expectedEntries =
                        Array.fromValues(new File[]
                        {
                            root.getFile("1.txt"),
                            root.getFile("2.txt"),
                            root.getFile("A/3.csv"),
                            root.getFile("A/5.png"),
                            root.getFile("B/C/4.xml")
                        });
                    final Iterable<File> actualEntries = root.getFilesRecursively();
                    test.assertEqual(expectedEntries, actualEntries);
                });
            });

            runner.testGroup("getFoldersRecursively()", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final Root root = getRoot("C:/");
                    test.assertEqual(new Array<Folder>(0), root.getFoldersRecursively());
                });

                runner.test("when root is empty", (Test test) ->
                {
                    final Root root = getRoot();
                    test.assertEqual(new Array<Folder>(0), root.getFoldersRecursively());
                });

                runner.test("when root has files", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    test.assertEqual(
                        new Array<Folder>(0),
                        root.getFoldersRecursively());
                });

                runner.test("when root has folders", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFolder("1.txt");
                    root.createFolder("2.txt");
                    test.assertEqual(
                        Array.fromValues(new Folder[]
                        {
                            root.getFolder("1.txt"),
                            root.getFolder("2.txt")
                        }),
                        root.getFoldersRecursively());
                });

                runner.test("when root has grandchild files and folders", (Test test) ->
                {
                    final Root root = getRoot();
                    root.createFile("1.txt");
                    root.createFile("2.txt");
                    root.createFile("A/3.csv");
                    root.createFile("B/C/4.xml");
                    root.createFile("A/5.png");

                    final Iterable<Folder> expectedEntries =
                        Array.fromValues(new Folder[]
                        {
                            root.getFolder("A"),
                            root.getFolder("B"),
                            root.getFolder("B/C")
                        });
                    final Iterable<Folder> actualEntries = root.getFoldersRecursively();
                    test.assertEqual(expectedEntries, actualEntries);
                });
            });
        });
    }

    private static InMemoryFileSystem getFileSystem()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        return fileSystem;
    }

    private static Root getRoot()
    {
        return getRoot("/");
    }

    private static Root getRoot(String rootPath)
    {
        final InMemoryFileSystem fileSystem = getFileSystem();
        return getRoot(fileSystem, rootPath);
    }

    private static Root getRoot(FileSystem fileSystem)
    {
        return getRoot(fileSystem, "/");
    }

    private static Root getRoot(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.getRoot(rootPath);
    }
}
