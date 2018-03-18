package qub;

public class FileSystemTests
{
    public static void test(TestRunner runner, Function1<AsyncRunner,FileSystem> creator)
    {
        final Action2<Test,Action1<FileSystem>> asyncTest = (Test test, Action1<FileSystem> testAction) ->
        {
            final Synchronization synchronization = new Synchronization();
            CurrentThreadAsyncRunner.withRegistered(synchronization, (CurrentThreadAsyncRunner mainRunner) ->
            {
                final CurrentThreadAsyncRunner backgroundRunner = new CurrentThreadAsyncRunner(synchronization);
                FileSystem fileSystem = creator.run(backgroundRunner);

                testAction.run(fileSystem);
                test.assertEqual(0, mainRunner.getScheduledTaskCount());
                test.assertEqual(1, backgroundRunner.getScheduledTaskCount());

                backgroundRunner.await();
                test.assertEqual(1, mainRunner.getScheduledTaskCount());
                test.assertEqual(0, backgroundRunner.getScheduledTaskCount());

                mainRunner.await();
                test.assertEqual(0, mainRunner.getScheduledTaskCount());
                test.assertEqual(0, backgroundRunner.getScheduledTaskCount());
            });
        };

        runner.testGroup("FileSystem", () ->
        {
            runner.testGroup("rootExists()", () ->
            {
                runner.test("with null String path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.rootExists((String)null));
                });

                runner.test("with empty String path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.rootExists(""));
                });

                runner.test("with non-existing String path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.rootExists("notme:/"));
                });
            });

            runner.testGroup("rootExistsAsync(String)", () ->
            {
                final Action2<String,Boolean> rootExistsAsyncTest = (String rootPath, Boolean expectedToExist) ->
                {
                    runner.test("with " + (rootPath == null ? null : "\"" + rootPath + "\""), (Test test) ->
                    {
                        asyncTest.run(test, (FileSystem fileSystem) ->
                        {
                            fileSystem.rootExistsAsync(rootPath)
                                .then((Boolean rootExists) ->
                                {
                                    test.assertEqual(expectedToExist, rootExists);
                                });
                        });
                    });
                };

                rootExistsAsyncTest.run(null, false);
                rootExistsAsyncTest.run("", false);
                rootExistsAsyncTest.run("notme:\\", false);
            });

            runner.testGroup("rootExistsAsync(Path)", () ->
            {
                final Action2<String,Boolean> rootExistsAsyncTest = (String rootPath, Boolean expectedToExist) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        asyncTest.run(test, (FileSystem fileSystem) ->
                        {
                            fileSystem.rootExistsAsync(Path.parse(rootPath))
                                .then((Boolean rootExists) ->
                                {
                                    test.assertEqual(expectedToExist, rootExists);
                                });
                        });
                    });
                };

                rootExistsAsyncTest.run(null, false);
                rootExistsAsyncTest.run("", false);
                rootExistsAsyncTest.run("notme:\\", false);
            });

            runner.test("getRoot()", (Test test) ->
            {
                FileSystem fileSystem = creator.run(null);
                final Root root1 = fileSystem.getRoot("/daffy/");
                test.assertFalse(root1.exists());
                test.assertEqual("/daffy/", root1.toString());
            });

            runner.test("getRoots()", (Test test) ->
            {
                FileSystem fileSystem = creator.run(null);
                final Iterable<Root> roots = fileSystem.getRoots();
                test.assertNotNull(roots);
                test.assertTrue(1 <= roots.getCount());
            });

            runner.test("getRootsAsync()", (Test test) ->
            {
                asyncTest.run(test, (FileSystem fileSystem) ->
                {
                    fileSystem.getRootsAsync()
                        .then((Iterable<Root> roots) ->
                        {
                            test.assertNotNull(roots);
                            test.assertTrue(1 <= roots.getCount());
                        });
                });
            });

            runner.testGroup("getFilesAndFolders(String)", () ->
            {
                runner.test("with null String path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders((String)null);
                    test.assertEqual(new Array<FileSystemEntry>(0), entries);
                });

                runner.test("with empty String path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders("");
                    test.assertEqual(new Array<FileSystemEntry>(0), entries);
                });

                runner.test("with null Path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders((Path)null);
                    test.assertEqual(new Array<FileSystemEntry>(0), entries);
                });

                runner.test("with non-existing path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders("/i/dont/exist/");
                    test.assertEqual(new Array<FileSystemEntry>(0), entries);
                });

                runner.test("with existing path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/folderA");
                    test.assertFalse(fileSystem.getFilesAndFolders("/folderA").any());
                });

                runner.test("with existing path with contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/folderA");
                    fileSystem.createFolder("/folderA/folderB");
                    fileSystem.createFile("/file1.txt");
                    fileSystem.createFile("/folderA/file2.csv");

                    final Iterable<FileSystemEntry> entries = fileSystem.getFilesAndFolders("/");
                    test.assertTrue(entries.any());
                    test.assertEqual(2, entries.getCount());
                    final String[] entryPathStrings = Array.toStringArray(entries.map(FileSystemEntry::toString));
                    test.assertEqual(new String[] { "/folderA", "/file1.txt" }, entryPathStrings);
                });
            });

            runner.testGroup("getFilesAndFoldersAsync(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFilesAndFoldersAsync((String)null)
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertEqual(new Array<FileSystemEntry>(0), entries);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFilesAndFoldersAsync("")
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertEqual(new Array<FileSystemEntry>(0), entries);
                            });
                    });
                });

                runner.test("with non-existing path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFilesAndFoldersAsync("/i/dont/exist/")
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertEqual(new Array<FileSystemEntry>(0), entries);
                            });
                    });
                });

                runner.test("with existing path with no content", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                        fileSystem.getFilesAndFoldersAsync("/folderA")
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertFalse(entries.any());
                            });
                    });
                });

                runner.test("with existing path with content", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                        fileSystem.createFolder("/folderA/folderB");
                        fileSystem.createFile("/file1.txt");
                        fileSystem.createFile("/folderA/file2.csv");

                        fileSystem.getFilesAndFoldersAsync("/")
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertTrue(entries.any());
                                test.assertEqual(2, entries.getCount());
                                final String[] entryPathStrings = Array.toStringArray(entries.map(FileSystemEntry::toString));
                                test.assertEqual(new String[] { "/folderA", "/file1.txt" }, entryPathStrings);
                            });
                    });
                });
            });

            runner.testGroup("getFilesAndFoldersAsync(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFilesAndFoldersAsync(Path.parse(""))
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertEqual(new Array<FileSystemEntry>(0), entries);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFilesAndFoldersAsync((Path)null)
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertEqual(new Array<FileSystemEntry>(0), entries);
                            });
                    });
                });

                runner.test("with non-existing path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFilesAndFoldersAsync(Path.parse("/i/dont/exist/"))
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertEqual(new Array<FileSystemEntry>(0), entries);
                            });
                    });
                });

                runner.test("with existing path with no contents", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                        fileSystem.getFilesAndFoldersAsync(Path.parse("/folderA"))
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertEqual(new Array<FileSystemEntry>(0), entries);
                            });
                    });
                });

                runner.test("with existing path with contents", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                        fileSystem.createFolder("/folderA/folderB");
                        fileSystem.createFile("/file1.txt");
                        fileSystem.createFile("/folderA/file2.csv");

                        fileSystem.getFilesAndFoldersAsync(Path.parse("/"))
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertTrue(entries.any());
                                test.assertEqual(2, entries.getCount());
                                final String[] entryPathStrings = Array.toStringArray(entries.map(FileSystemEntry::toString));
                                test.assertEqual(new String[] { "/folderA", "/file1.txt" }, entryPathStrings);
                            });
                    });
                });
            });

            runner.testGroup("getFolders(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Iterable<Folder> folders = fileSystem.getFolders((String)null);
                    test.assertEqual(new Array<Folder>(0), folders);
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Iterable<Folder> folders = fileSystem.getFolders("");
                    test.assertEqual(new Array<Folder>(0), folders);
                });
            });

            runner.testGroup("getFoldersRecursively(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<Folder>(0), fileSystem.getFoldersRecursively((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<Folder>(0), fileSystem.getFoldersRecursively(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<Folder>(0), fileSystem.getFoldersRecursively("test/folder"));
                });

                runner.test("with rooted path when root doesn't exist", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<Folder>(0), fileSystem.getFoldersRecursively("F:/test/folder"));
                });

                runner.test("with rooted path when parent folder doesn't exist", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<Folder>(0), fileSystem.getFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder doesn't exist", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/");
                    test.assertEqual(new Array<Folder>(0), fileSystem.getFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder is empty", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/folder");
                    test.assertEqual(new Array<Folder>(0), fileSystem.getFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder has files", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/folder");
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");
                    test.assertEqual(
                        new Array<Folder>(0),
                        fileSystem.getFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder has folders", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/folder");
                    fileSystem.createFolder("/test/folder/1.txt");
                    fileSystem.createFolder("/test/folder/2.txt");
                    test.assertEqual(
                        Array.fromValues(new Folder[]
                        {
                            fileSystem.getFolder("/test/folder/1.txt"),
                            fileSystem.getFolder("/test/folder/2.txt")
                        }),
                        fileSystem.getFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder has grandchild folders and files", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");
                    fileSystem.createFile("/test/folder/A/3.csv");
                    fileSystem.createFile("/test/folder/B/C/4.xml");
                    fileSystem.createFile("/test/folder/A/5.png");

                    final Iterable<Folder> expectedEntries =
                        Array.fromValues(new Folder[]
                        {
                            fileSystem.getFolder("/test/folder/A"),
                            fileSystem.getFolder("/test/folder/B"),
                            fileSystem.getFolder("/test/folder/B/C")
                        });
                    final Iterable<Folder> actualEntries = fileSystem.getFoldersRecursively("/test/folder");
                    test.assertEqual(expectedEntries, actualEntries);
                });
            });

            runner.testGroup("getFoldersAsync(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFoldersAsync((String)null)
                            .then((Iterable<Folder> folders) ->
                            {
                                test.assertEqual(new Array<Folder>(0), folders);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFoldersAsync("")
                            .then((Iterable<Folder> folders) ->
                            {
                                test.assertEqual(new Array<Folder>(0), folders);
                            });
                    });
                });
            });

            runner.testGroup("getFoldersAsync(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFoldersAsync((Path)null)
                            .then((Iterable<Folder> folders) ->
                            {
                                test.assertEqual(new Array<Folder>(0), folders);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFoldersAsync(Path.parse(""))
                            .then((Iterable<Folder> folders) ->
                            {
                                test.assertEqual(new Array<Folder>(0), folders);
                            });
                    });
                });
            });

            runner.testGroup("getFilesAsync(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final AsyncFunction<Iterable<File>> asyncFunction = fileSystem.getFilesAsync((String)null);
                        test.assertNotNull(asyncFunction, "The AsyncFunction returned from getFilesAsync() was null.");

                        asyncFunction.then((Iterable<File> files) ->
                        {
                            test.assertEqual(new Array<File>(0), files);
                        });
                    });
                });

                runner.test("with empty path", (Test test) ->
                    {
                        asyncTest.run(test, (FileSystem fileSystem) ->
                        {
                            final AsyncFunction<Iterable<File>> asyncFunction = fileSystem.getFilesAsync("");
                            test.assertNotNull(asyncFunction, "The AsyncFunction returned from getFilesAsync() was null.");

                            asyncFunction.then((Iterable<File> files) ->
                            {
                                test.assertEqual(new Array<File>(0), files);
                            });
                        });
                    });

            });

            runner.testGroup("getFilesAsync(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFilesAsync((Path)null)
                            .then((Iterable<File> files) ->
                            {
                                test.assertEqual(new Array<File>(0), files);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.getFilesAsync(Path.parse(""))
                            .then((Iterable<File> files) ->
                            {
                                test.assertEqual(new Array<File>(0), files);
                            });
                    });
                });
            });

            runner.testGroup("getFiles(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<File>(0), fileSystem.getFiles((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<File>(0), fileSystem.getFiles(""));
                });
            });

            runner.testGroup("getFilesRecursively(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesRecursively((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesRecursively(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesRecursively("test/folder"));
                });

                runner.test("with rooted path when root doesn't exist", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesRecursively("F:/test/folder"));
                });

                runner.test("with rooted path when parent folder doesn't exist", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder doesn't exist", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/");
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder is empty", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/folder");
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder has files", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/folder");
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");
                    test.assertEqual(
                        Array.fromValues(new File[]
                        {
                            fileSystem.getFile("/test/folder/1.txt"),
                            fileSystem.getFile("/test/folder/2.txt")
                        }),
                        fileSystem.getFilesRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder has folders", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/folder");
                    fileSystem.createFolder("/test/folder/1.txt");
                    fileSystem.createFolder("/test/folder/2.txt");
                    test.assertEqual(
                        new Array<File>(0),
                        fileSystem.getFilesRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder has grandchild folders and files", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");
                    fileSystem.createFile("/test/folder/A/3.csv");
                    fileSystem.createFile("/test/folder/B/C/4.xml");
                    fileSystem.createFile("/test/folder/A/5.png");

                    final Iterable<File> expectedEntries =
                        Array.fromValues(new File[]
                        {
                            fileSystem.getFile("/test/folder/1.txt"),
                            fileSystem.getFile("/test/folder/2.txt"),
                            fileSystem.getFile("/test/folder/A/3.csv"),
                            fileSystem.getFile("/test/folder/A/5.png"),
                            fileSystem.getFile("/test/folder/B/C/4.xml")
                        });
                    final Iterable<File> actualEntries = fileSystem.getFilesRecursively("/test/folder");
                    test.assertEqual(expectedEntries, actualEntries);
                });
            });

            runner.testGroup("getFolder(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Folder folder = fileSystem.getFolder((String)null);
                    test.assertNull(folder);
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Folder folder = fileSystem.getFolder("");
                    test.assertNull(folder);
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Folder folder = fileSystem.getFolder("a/b/c");
                    test.assertNull(folder);
                });

                runner.test("with forward-slash", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Folder folder = fileSystem.getFolder("/");
                    test.assertNotNull(folder);
                    test.assertEqual("/", folder.toString());
                });

                runner.test("with backslash", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Folder folder = fileSystem.getFolder("\\");
                    test.assertNotNull(folder);
                    test.assertEqual("\\", folder.toString());
                });

                runner.test("with Windows root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Folder folder = fileSystem.getFolder("Z:\\");
                    test.assertNotNull(folder);
                    test.assertEqual("Z:\\", folder.toString());
                });

                runner.test("with root and folder", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Folder folder = fileSystem.getFolder("/a/b");
                    test.assertNotNull(folder);
                    test.assertEqual("/a/b", folder.toString());
                });
            });

            runner.testGroup("folderExists(String)", () ->
            {
                runner.test("with root path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Root folder = fileSystem.getRoots().first();
                    test.assertTrue(fileSystem.folderExists(folder.getPath().toString()));
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/folderName");
                    test.assertTrue(fileSystem.folderExists("/folderName"));
                });
            });

            runner.testGroup("folderExistsAsync(String)", () ->
            {
                runner.test("with root path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.folderExistsAsync(fileSystem.getRoots().first().getPath().toString())
                            .then((Boolean folderExists) ->
                            {
                                test.assertTrue(folderExists);
                            });
                    });
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderName");
                        fileSystem.folderExistsAsync("/folderName")
                            .then((Boolean folderExists) ->
                            {
                                test.assertTrue(folderExists);
                            });
                    });
                });
            });

            runner.testGroup("folderExistsAsync(Path)", () ->
            {
                runner.test("with root path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.folderExistsAsync(fileSystem.getRoots().first().getPath())
                            .then((Boolean folderExists) ->
                            {
                                test.assertTrue(folderExists);
                            });
                    });
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderName");
                        fileSystem.folderExistsAsync(Path.parse("/folderName"))
                            .then((Boolean folderExists) ->
                            {
                                test.assertTrue(folderExists);
                            });
                    });
                });
            });

            runner.testGroup("createFolder(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFolder((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFolder(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFolder("folder"));
                });

                runner.test("with rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.createFolder("/folder"));
                    test.assertTrue(fileSystem.folderExists("/folder"));
                });
            });

            runner.testGroup("createFolder(String,Out<Folder>)", () ->
            {
                runner.test("with null path and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFolder((String)null, (Out<Folder>)null));
                });

                runner.test("with empty path and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFolder("", (Out<Folder>)null));
                });

                runner.test("with relative path and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFolder("folder", (Out<Folder>)null));
                });

                runner.test("with rooted path and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.createFolder("/folder", (Out<Folder>)null));
                    test.assertTrue(fileSystem.folderExists("/folder"));
                });

                runner.test("with null path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<Folder> folder = new Value<>();
                    test.assertFalse(fileSystem.createFolder((String)null, folder));
                    test.assertFalse(folder.hasValue());
                });

                runner.test("with empty path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<Folder> folder = new Value<>();
                    test.assertFalse(fileSystem.createFolder("", folder));
                    test.assertFalse(folder.hasValue());
                });

                runner.test("with relative path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<Folder> folder = new Value<>();
                    test.assertFalse(fileSystem.createFolder("folder", folder));
                    test.assertFalse(folder.hasValue());
                });

                runner.test("with rooted path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<Folder> folder = new Value<>();
                    test.assertTrue(fileSystem.createFolder("/folder", folder));
                    test.assertTrue(fileSystem.folderExists("/folder"));
                    test.assertTrue(folder.hasValue());
                    test.assertNotNull(folder.get());
                    test.assertEqual("/folder", folder.get().getPath().toString());
                });
            });

            runner.testGroup("createFolder(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFolder((Path)null));
                });
            });

            runner.testGroup("createFolder(Path,Value<Folder>)", () ->
            {
                runner.test("with null path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<Folder> folder = new Value<>();
                    test.assertFalse(fileSystem.createFolder((Path)null, folder));
                    test.assertFalse(folder.hasValue());
                    test.assertNull(folder.get());
                });
            });

            runner.testGroup("createFolderAsync(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync((String)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync("")
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with relative path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync("folder")
                            .then((Boolean arg1) ->
                            {
                                test.assertFalse(arg1);
                            });
                    });
                });

                runner.test("with rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync("/folder")
                            .then((Boolean folderCreated) ->
                            {
                                test.assertTrue(folderCreated);
                                test.assertTrue(fileSystem.folderExists("/folder"));
                            });
                    });
                });
            });

            runner.testGroup("createFolderAsync(String,Out<Folder>)", () ->
            {
                runner.test("with null path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync((String)null, (Out<Folder>)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with empty path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync("", (Out<Folder>)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with relative path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync("folder", (Out<Folder>)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with rooted path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync("/folder", (Out<Folder>)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertTrue(folderCreated);
                                test.assertTrue(fileSystem.folderExists("/folder"));
                            });
                    });
                });

                runner.test("with null path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<Folder> folder = new Value<>();
                        fileSystem.createFolderAsync((String)null, folder)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                                test.assertFalse(folder.hasValue());
                            });
                    });
                });

                runner.test("with empty path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<Folder> folder = new Value<>();
                        fileSystem.createFolderAsync("", folder)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                                test.assertFalse(folder.hasValue());
                            });
                    });
                });

                runner.test("with relative path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<Folder> folder = new Value<>();
                        fileSystem.createFolderAsync("folder", folder)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                                test.assertFalse(folder.hasValue());
                            });
                    });
                });

                runner.test("with rooted path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<Folder> folder = new Value<>();
                        fileSystem.createFolderAsync("/folder", folder)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertTrue(folderCreated);
                                test.assertTrue(fileSystem.folderExists("/folder"));

                                test.assertTrue(folder.hasValue());
                                test.assertNotNull(folder.get());
                                test.assertEqual("/folder", folder.get().getPath().toString());
                            });
                    });
                });
            });

            runner.testGroup("createFolderAsync(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync((Path)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync(Path.parse(""))
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with relative path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync(Path.parse("folder"))
                            .then((Boolean arg1) ->
                            {
                                test.assertFalse(arg1);
                            });
                    });
                });

                runner.test("with rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync(Path.parse("/folder"))
                            .then((Boolean folderCreated) ->
                            {
                                test.assertTrue(folderCreated);
                                test.assertTrue(fileSystem.folderExists("/folder"));
                            });
                    });
                });
            });

            runner.testGroup("createFolderAsync(Path,Out<Folder>)", () ->
            {
                runner.test("with null path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync((Path)null, (Out<Folder>)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with empty path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync(Path.parse(""), (Out<Folder>)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with relative path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync(Path.parse("folder"), (Out<Folder>)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                            });
                    });
                });

                runner.test("with rooted path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolderAsync(Path.parse("/folder"), (Out<Folder>)null)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertTrue(folderCreated);
                                test.assertTrue(fileSystem.folderExists("/folder"));
                            });
                    });
                });

                runner.test("with null path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<Folder> folder = new Value<>();
                        fileSystem.createFolderAsync((Path)null, folder)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                                test.assertFalse(folder.hasValue());
                            });
                    });
                });

                runner.test("with empty path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<Folder> folder = new Value<>();
                        fileSystem.createFolderAsync(Path.parse(""), folder)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                                test.assertFalse(folder.hasValue());
                            });
                    });
                });

                runner.test("with relative path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<Folder> folder = new Value<>();
                        fileSystem.createFolderAsync(Path.parse("folder"), folder)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertFalse(folderCreated);
                                test.assertFalse(folder.hasValue());
                            });
                    });
                });

                runner.test("with rooted path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<Folder> folder = new Value<>();
                        fileSystem.createFolderAsync(Path.parse("/folder"), folder)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertTrue(folderCreated);
                                test.assertTrue(fileSystem.folderExists("/folder"));

                                test.assertTrue(folder.hasValue());
                                test.assertNotNull(folder.get());
                                test.assertEqual("/folder", folder.get().getPath().toString());
                            });
                    });
                });
            });

            runner.testGroup("deleteFolder(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.deleteFolder((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.deleteFolder(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/folder");
                    test.assertFalse(fileSystem.deleteFolder("folder"));
                });

                runner.test("with non-existing path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.deleteFolder("/folder"));
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/folder/");
                    test.assertTrue(fileSystem.deleteFolder("/folder/"));
                    test.assertFalse(fileSystem.deleteFolder("/folder/"));
                });

                runner.test("with existing folder path with sibling folders", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/folder/a");
                    fileSystem.createFolder("/folder/b");
                    fileSystem.createFolder("/folder/c");
                    test.assertTrue(fileSystem.deleteFolder("/folder/c"));
                    test.assertFalse(fileSystem.deleteFolder("/folder/c"));
                });
            });

            runner.testGroup("deleteFolderAsync(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFolderAsync((String)null)
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertFalse(folderDeleted);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFolderAsync("")
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertFalse(folderDeleted);
                            });
                    });
                });

                runner.test("with relative path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder");
                        fileSystem.deleteFolderAsync("folder")
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertFalse(folderDeleted, "Expected the folder to fail to delete since a relative path was given.");
                                test.assertTrue(fileSystem.folderExists("/folder"), "Expected the folder to still exist.");
                            });
                    });
                });

                runner.test("with non-existing folder path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFolderAsync("/folder")
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertFalse(folderDeleted);
                            });
                    });
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/");

                        fileSystem.deleteFolderAsync("/folder/")
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertTrue(folderDeleted);
                                test.assertFalse(fileSystem.folderExists("/folder/"));
                            });
                    });
                });

                runner.test("with existing folder path and sibling folders", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a");
                        fileSystem.createFolder("/folder/b");
                        fileSystem.createFolder("/folder/c");

                        fileSystem.deleteFolderAsync("/folder/c")
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertTrue(folderDeleted);
                                test.assertFalse(fileSystem.folderExists("/folder/c"));
                                test.assertTrue(fileSystem.folderExists("/folder/a"));
                                test.assertTrue(fileSystem.folderExists("/folder/b"));
                            });
                    });
                });
            });

            runner.testGroup("deleteFolderAsync(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFolderAsync((Path)null)
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertFalse(folderDeleted);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFolderAsync(Path.parse(""))
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertFalse(folderDeleted);
                            });
                    });
                });

                runner.test("with relative path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder");
                        fileSystem.deleteFolderAsync(Path.parse("folder"))
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertFalse(folderDeleted);
                                test.assertTrue(fileSystem.folderExists("/folder"));
                            });
                    });
                });

                runner.test("with non-existing folder path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFolderAsync(Path.parse("/folder"))
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertFalse(folderDeleted);
                            });
                    });
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/");

                        fileSystem.deleteFolderAsync(Path.parse("/folder/"))
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertTrue(folderDeleted);
                                test.assertFalse(fileSystem.folderExists("/folder/"));
                            });
                    });
                });

                runner.test("with existing folder path with sibling folders", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a");
                        fileSystem.createFolder("/folder/b");
                        fileSystem.createFolder("/folder/c");

                        fileSystem.deleteFolderAsync(Path.parse("/folder/c"))
                            .then((Boolean folderDeleted) ->
                            {
                                test.assertTrue(folderDeleted);
                                test.assertFalse(fileSystem.folderExists("/folder/c"));
                                test.assertTrue(fileSystem.folderExists("/folder/a"));
                                test.assertTrue(fileSystem.folderExists("/folder/b"));
                            });
                    });
                });
            });

            runner.testGroup("getFile(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final File file = fileSystem.getFile((String)null);
                    test.assertNull(file);
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final File file = fileSystem.getFile("");
                    test.assertNull(file);
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final File file = fileSystem.getFile("a/b/c");
                    test.assertNull(file);
                });

                runner.test("with forward slash", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final File file = fileSystem.getFile("/");
                    test.assertNull(file);
                });

                runner.test("with backslash", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final File file = fileSystem.getFile("\\");
                    test.assertNull(file);
                });

                runner.test("with Windows root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final File file = fileSystem.getFile("Z:\\");
                    test.assertNull(file);
                });

                runner.test("with rooted file path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final File file = fileSystem.getFile("/a/b");
                    test.assertNotNull(file);
                    test.assertEqual("/a/b", file.toString());
                });
            });

            runner.testGroup("fileExists(String)", () ->
            {
                runner.test("with rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Root root = fileSystem.getRoots().first();
                    test.assertFalse(fileSystem.fileExists(root.getPath().toString()));
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/folderName");
                    test.assertFalse(fileSystem.fileExists("/folerName"));
                });

                runner.test("with existing file path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/file1.xml");
                    test.assertTrue(fileSystem.fileExists("/file1.xml"));
                });
            });

            runner.testGroup("fileExistsAsync(String)", () ->
            {
                runner.test("with root path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Root root = fileSystem.getRoots().first();
                        fileSystem.fileExistsAsync(root.getPath().toString())
                            .then((Boolean fileExists) ->
                            {
                                test.assertFalse(fileExists);
                            });
                    });
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderName");
                        fileSystem.fileExistsAsync("/folderName")
                            .then((Boolean fileExists) ->
                            {
                                test.assertFalse(fileExists);
                            });
                    });
                });

                runner.test("with existing file path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/file1.xml");
                        fileSystem.fileExistsAsync("/file1.xml")
                            .then((Boolean fileExists) ->
                            {
                                test.assertTrue(fileExists);
                            });
                    });
                });
            });

            runner.testGroup("fileExistsAsync(Path)", () ->
            {
                runner.test("with root path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Root root = fileSystem.getRoots().first();
                        fileSystem.fileExistsAsync(root.getPath())
                            .then((Boolean fileExists) ->
                            {
                                test.assertFalse(fileExists);
                            });
                    });
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderName");
                        fileSystem.fileExistsAsync(Path.parse("/folderName"))
                            .then((Boolean fileExists) ->
                            {
                                test.assertFalse(fileExists);
                            });
                    });
                });

                runner.test("with existing file path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/file1.xml");
                        fileSystem.fileExistsAsync(Path.parse("/file1.xml"))
                            .then((Boolean fileExists) ->
                            {
                                test.assertTrue(fileExists);
                            });
                    });
                });
            });

            runner.testGroup("createFile(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFile((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFile(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFile("things.txt"));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);

                    test.assertTrue(fileSystem.createFile("/things.txt"));

                    test.assertTrue(fileSystem.fileExists("/things.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                });

                runner.test("with existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/things.txt");

                    test.assertFalse(fileSystem.createFile("/things.txt"));

                    test.assertTrue(fileSystem.fileExists("/things.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                });

                runner.test("with non-existing rooted path and content", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);

                    test.assertTrue(fileSystem.createFile("/things.txt", new byte[] { 0, 1, 2, 3 }));

                    test.assertTrue(fileSystem.fileExists("/things.txt"));
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContents("/things.txt"));
                });

                runner.test("with existing rooted path and byte[] contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.createFile("/things.txt"), "Failed to create the file.");

                    test.assertFalse(fileSystem.createFile("/things.txt", new byte[] { 0, 1, 2, 3 }), "Expected file creation to return false when the file already exists.");

                    test.assertTrue(fileSystem.fileExists("/things.txt"), "The file should have existed after the failed creation attempt.");
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"), "The file contents should've been empty.");
                });

                runner.test("with existing rooted path and String contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.createFile("/things.txt"), "Failed to create the file.");

                    test.assertFalse(fileSystem.createFile("/things.txt", "ABC"), "Expected file creation to return false when the file already exists.");

                    test.assertTrue(fileSystem.fileExists("/things.txt"), "The file should've existed after the failed creation attempt.");
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"), "The file contents should've been empty.");
                });

                runner.test("with existing rooted path, String contents, and encoding", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.createFile("/things.txt"));

                    test.assertFalse(fileSystem.createFile("/things.txt", "ABC", CharacterEncoding.UTF_8));

                    test.assertTrue(fileSystem.fileExists("/things.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                });
            });

            runner.testGroup("createFile(String,Out<File>)", () ->
            {
                runner.test("with null path and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFile((String)null, (Out<File>)null));
                });

                runner.test("with empty path and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFile("", (Out<File>)null));
                });

                runner.test("with relative path and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFile("things.txt", (Out<File>)null));
                });

                runner.test("with non-existing rooted path and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.createFile("/things.txt", (Out<File>)null));
                    test.assertTrue(fileSystem.fileExists("/things.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                });

                runner.test("with non-existing rooted path, byte[] contents, and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.createFile("/things.txt", new byte[] { 10, 11, 12 }, (Out<File>)null));
                    test.assertTrue(fileSystem.fileExists("/things.txt"));
                    test.assertEqual(new byte[] { 10, 11, 12 }, fileSystem.getFileContents("/things.txt"));
                });

                runner.test("with non-existing rooted path, String contents, and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);

                    test.assertTrue(fileSystem.createFile("/things.txt", "ABC", (Out<File>)null));

                    test.assertTrue(fileSystem.fileExists("/things.txt"));
                    test.assertEqual("ABC", fileSystem.getFileContentsAsString("/things.txt"));
                });

                runner.test("with non-existing rooted path, String contents, encoding, and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);

                    test.assertTrue(fileSystem.createFile("/things.txt", "ABC", CharacterEncoding.UTF_8, (Out<File>)null));

                    test.assertTrue(fileSystem.fileExists("/things.txt"));
                    test.assertEqual("ABC", fileSystem.getFileContentsAsString("/things.txt"));
                });

                runner.test("with existing rooted path and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/things.txt");

                    test.assertFalse(fileSystem.createFile("/things.txt", (Out<File>)null));

                    test.assertTrue(fileSystem.fileExists("/things.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"));
                });

                runner.test("with existing rooted path, byte[] contents, and null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.createFile("/things.txt"), "Failed to create /things.txt.");
                    test.assertTrue(fileSystem.fileExists("/things.txt"), "/things.txt should have existed after it was created.");

                    test.assertFalse(fileSystem.createFile("/things.txt", new byte[] { 0, 1 }, (Out<File>)null), "Shouldn't have been able to create /things.txt a second time.");

                    test.assertTrue(fileSystem.fileExists("/things.txt"), "/things.txt should still have existed after the second createFile() method failed.");
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/things.txt"), "/things.txt file's content should be empty.");
                });

                runner.test("with null path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<File> file = new Value<>();
                    test.assertFalse(fileSystem.createFile((String)null, file));
                });

                runner.test("with empty path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<File> file = new Value<>();
                    test.assertFalse(fileSystem.createFile("", file));
                });

                runner.test("with relative path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<File> file = new Value<>();
                    final boolean fileCreated = fileSystem.createFile("things.txt", file);
                    test.assertFalse(fileCreated);
                    test.assertFalse(file.hasValue());
                });

                runner.test("with non-existing rooted path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<File> file = new Value<>();
                    test.assertTrue(fileSystem.createFile("/things.txt", file));
                    test.assertTrue(file.hasValue());
                    test.assertNotNull(file.get());
                    test.assertEqual("/things.txt", file.get().getPath().toString());
                });

                runner.test("with existing rooted path and non-null value", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/things.txt");

                    final Value<File> file = new Value<>();
                    test.assertFalse(fileSystem.createFile("/things.txt", file));
                    test.assertTrue(file.hasValue());
                    test.assertEqual(Path.parse("/things.txt"), file.get().getPath());
                });
            });

            runner.testGroup("createFile(Path)", () ->
            {
                runner.test("with null path and byte[] contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFile((Path)null, new byte[] { 0, 1, 2 }));
                });

                runner.test("with null path and String contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFile((Path)null, "ABC"));
                });

                runner.test("with null path, String contents, and encoding", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.createFile((Path)null, "ABC", CharacterEncoding.UTF_8));
                });

                runner.test("with invalid path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<File> file = new Value<>();
                    final boolean fileCreated = fileSystem.createFile("/\u0000?#!.txt", file);
                    test.assertFalse(fileCreated, "Wrong fileCreated");
                    test.assertFalse(file.hasValue(), "Wrong file.hasValue()");
                    test.assertNull(file.get(), "Wrong file.get()");
                });
            });

            runner.testGroup("createFileAsync(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync((String)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync("")
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with relative path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync("things.txt")
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync("/things.txt")
                            .then((Boolean fileCreated) ->
                            {
                                test.assertTrue(fileCreated);
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                            });
                    });
                });

                runner.test("with existing rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/things.txt");

                        fileSystem.createFileAsync("/things.txt")
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                            });
                    });
                });
            });

            runner.testGroup("createFileAsync(String,Out<File>)", () ->
            {
                runner.test("with null path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync((String)null, (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with empty path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync("", (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with relative path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync("things.txt", (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with non-existing rooted path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync("/things.txt", (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertTrue(fileCreated);
                            });
                    });
                });

                runner.test("with existing rooted path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/things.txt");

                        fileSystem.createFileAsync("/things.txt", (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                            });
                    });
                });

                runner.test("with null path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync((String)null, file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            });
                    });
                });

                runner.test("with empty path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync("", file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            });
                    });
                });

                runner.test("with relative path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync("things.txt", file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            });
                    });
                });

                runner.test("with non-existing rooted path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync("/things.txt", file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertTrue(fileCreated);
                                test.assertEqual("/things.txt", file.get().getPath().toString());
                            });
                    });
                });

                runner.test("with existing rooted path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        test.assertTrue(fileSystem.createFile("/things.txt", file));

                        fileSystem.createFileAsync("/things.txt", file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated, "The createFileAsync() method should have failed.");
                                test.assertTrue(file.hasValue(), "file should have had a value, even though the createFileAsync() method failed.");
                                test.assertNotNull(file.get(), "file's value should have been not-null.");
                                test.assertEqual(Path.parse("/things.txt"), file.get().getPath());
                            });
                    });
                });

                runner.test("with invalid path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync("/\u0000?#!.txt", file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            });
                    });
                });
            });

            runner.testGroup("createFileAsync(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync((Path)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync(Path.parse(""))
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with relative path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync(Path.parse("things.txt"))
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync(Path.parse("/things.txt"))
                            .then((Boolean fileCreated) ->
                            {
                                test.assertTrue(fileCreated);
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                            });
                    });
                });

                runner.test("with existing rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/things.txt");

                        fileSystem.createFileAsync(Path.parse("/things.txt"))
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertTrue(fileSystem.fileExists("/things.txt"));
                            });
                    });
                });
            });

            runner.testGroup("createFileAsync(Path,Out<File>)", () ->
            {
                runner.test("with null path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync((Path)null, (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with empty path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync(Path.parse(""), (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with relative path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync(Path.parse("things.txt"), (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with non-existing rooted path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFileAsync(Path.parse("/things.txt"), (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertTrue(fileCreated);
                            });
                    });
                });

                runner.test("with existing rooted path and null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/things.txt");

                        fileSystem.createFileAsync(Path.parse("/things.txt"), (Out<File>)null)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                            });
                    });
                });

                runner.test("with null path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync((Path)null, file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            });
                    });
                });

                runner.test("with empty path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync(Path.parse(""), file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            });
                    });
                });

                runner.test("with relative path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync(Path.parse("things.txt"), file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            });
                    });
                });

                runner.test("with non-existing rooted path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync(Path.parse("/things.txt"), file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertTrue(fileCreated);
                                test.assertEqual("/things.txt", file.get().getPath().toString());
                            });
                    });
                });

                runner.test("with existing rooted path and non-null value", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFile("/things.txt", file);

                        fileSystem.createFileAsync(Path.parse("/things.txt"), file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertTrue(file.hasValue());
                                test.assertNotNull(file.get());
                                test.assertEqual(Path.parse("/things.txt"), file.get().getPath());
                            });
                    });
                });

                runner.test("with invalid path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        final Value<File> file = new Value<>();
                        fileSystem.createFileAsync(Path.parse("/\u0000?#!.txt"), file)
                            .then((Boolean fileCreated) ->
                            {
                                test.assertFalse(fileCreated);
                                test.assertFalse(file.hasValue());
                            });
                    });
                });
            });

            runner.testGroup("deleteFile(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.deleteFile((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.deleteFile(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.deleteFile("relativeFile.txt"));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.deleteFile("/idontexist.txt"));
                });

                runner.test("with existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/iexist.txt");

                    test.assertTrue(fileSystem.deleteFile("/iexist.txt"));
                    test.assertFalse(fileSystem.fileExists("/iexist.txt"));

                    test.assertFalse(fileSystem.deleteFile("/iexist.txt"));
                    test.assertFalse(fileSystem.fileExists("/iexist.txt"));
                });
            });

            runner.testGroup("deleteFileAsync(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFileAsync((String)null)
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertFalse(fileDeleted);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFileAsync("")
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertFalse(fileDeleted);
                            });
                    });
                });

                runner.test("with relative path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFileAsync("relativeFile.txt")
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertFalse(fileDeleted);
                            });
                    });
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFileAsync("/idontexist.txt")
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertFalse(fileDeleted);
                            });
                    });
                });

                runner.test("with existing rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/iexist.txt");
                        fileSystem.deleteFileAsync("/iexist.txt")
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertTrue(fileDeleted);
                                test.assertFalse(fileSystem.fileExists("/iexist.txt"));
                            });
                    });
                });
            });

            runner.testGroup("deleteFileAsync(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFileAsync((Path)null)
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertFalse(fileDeleted);
                            });
                    });
                });

                runner.test("with empty path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFileAsync(Path.parse(""))
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertFalse(fileDeleted);
                            });
                    });
                });

                runner.test("with relative path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFileAsync(Path.parse("relativeFile.txt"))
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertFalse(fileDeleted);
                            });
                    });
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.deleteFileAsync(Path.parse("/idontexist.txt"))
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertFalse(fileDeleted);
                            });
                    });
                });

                runner.test("with existing rooted path", (Test test) ->
                {
                    asyncTest.run(test, (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/iexist.txt");
                        fileSystem.deleteFileAsync(Path.parse("/iexist.txt"))
                            .then((Boolean fileDeleted) ->
                            {
                                test.assertTrue(fileDeleted);
                                test.assertFalse(fileSystem.fileExists("/iexist.txt"));
                            });
                    });
                });
            });

            runner.testGroup("getFileLastModified(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileLastModified((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileLastModified(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileLastModified("thing.txt"));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileLastModified("/thing.txt"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/thing.txt");
                    final DateTime lastModified = fileSystem.getFileLastModified("/thing.txt");
                    test.assertNotNull(lastModified);
                });
            });

            runner.testGroup("getFileContents(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContents((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContents(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContents("thing.txt"));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContents("/thing.txt"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/thing.txt");
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/thing.txt"));
                });
            });

            runner.testGroup("getFileContents(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContents((Path)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContents(Path.parse("")));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContents(Path.parse("thing.txt")));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContents(Path.parse("/thing.txt")));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/thing.txt");
                    test.assertEqual(new byte[0], fileSystem.getFileContents(Path.parse("/thing.txt")));
                });
            });

            runner.testGroup("getFileContentsAsString(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentsAsString((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentsAsString(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentsAsString("file.txt"));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentsAsString("/file.txt"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/file.txt");
                    test.assertEqual("", fileSystem.getFileContentsAsString("/file.txt"));
                });

                runner.test("with existing rooted path with contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/file.txt", CharacterEncoding.UTF_8.encode("Hello there!"));
                    test.assertEqual("Hello there!", fileSystem.getFileContentsAsString("/file.txt"));
                });
            });

            runner.testGroup("getFileContentsAsString(String,CharacterEncoding)", () ->
            {
                runner.test("with null path and null encoding", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentsAsString((String)null, (CharacterEncoding)null));
                });

                runner.test("with empty path and null encoding", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentsAsString("", (CharacterEncoding)null));
                });

                runner.test("with relative path and null encoding", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentsAsString("file.txt", (CharacterEncoding)null));
                });

                runner.test("with non-existing rooted path and null encoding", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentsAsString("/file.txt", (CharacterEncoding)null));
                });

                runner.test("with existing rooted path with no contents and null encoding", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/file.txt");
                    test.assertNull(fileSystem.getFileContentsAsString("/file.txt", (CharacterEncoding)null));
                });

                runner.test("with existing rooted path with contents and null encoding", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/file.txt", CharacterEncoding.UTF_8.encode("Hello there!"));
                    test.assertNull(fileSystem.getFileContentsAsString("/file.txt", (CharacterEncoding)null));
                });
            });

            runner.testGroup("getFileContentByteReadStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentByteReadStream("C:/i/dont/exist.txt"));
                });
            });

            runner.testGroup("getFileContentCharacterReadStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentCharacterReadStream("C:/i/dont/exist.txt"));
                });
            });

            runner.testGroup("getFileContentBlocks(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentBlocks((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentBlocks(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentBlocks("B"));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentBlocks("/a.txt"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/a.txt");

                    final Iterable<byte[]> fileContentBlocks = fileSystem.getFileContentBlocks("/a.txt");
                    test.assertNotNull(fileContentBlocks);
                    test.assertEqual(0, fileContentBlocks.getCount());
                });

                runner.test("with existing rooted path with contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/a.txt", new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});

                    final Iterable<byte[]> fileContentBlocks = fileSystem.getFileContentBlocks("/a.txt");
                    test.assertNotNull(fileContentBlocks);

                    final byte[] fileContents = Array.merge(fileContentBlocks);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, fileContents);
                });
            });

            runner.testGroup("getFileContentLines(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("relative/file.txt"));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("/folder/file.csv"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv");
                    test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv")));
                });

                runner.test("with existing rooted path with single line", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "abcdef");
                    final Value<String> error = new Value<>();
                    final Iterable<String> fileContentLines = fileSystem.getFileContentLines("/folder/file.csv", error::set);
                    test.assertFalse(error.hasValue(), error.get());
                    test.assertEqual(Array.fromValues(new String[] { "abcdef" }), fileContentLines);
                });

                runner.test("with existing rooted path with multiple lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                    final Value<String> error = new Value<>();
                    final Iterable<String> fileContentLines = fileSystem.getFileContentLines("/folder/file.csv", error::set);
                    test.assertFalse(error.hasValue(), error.get());
                    test.assertEqual(Array.fromValues(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }), fileContentLines);
                });
            });

            runner.testGroup("getFileContentLines(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines((Path)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse("")));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt")));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv")));
                });

                runner.test("with existing rooted path with no content", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv");
                    test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"))));
                });

                runner.test("with existing rooted path with single line", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "abcdef");
                    test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"))));
                });

                runner.test("with existing rooted path with multiple lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                    test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"))));
                });
            });

            runner.testGroup("getFileContentLines(String,boolean)", () ->
            {
                runner.test("with null path and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines((String)null, true));
                });

                runner.test("with empty path and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("", true));
                });

                runner.test("with relative path and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("relative/file.txt", true));
                });

                runner.test("with non-existing rooted path and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("/folder/file.csv", true));
                });

                runner.test("with existing rooted path with no contents and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv");
                    test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", true)));
                });

                runner.test("with existing rooted path with single line and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "abcdef");
                    test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", true)));
                });

                runner.test("with existing rooted path with multiple lines and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                    test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", true)));
                });

                runner.test("with null path and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines((String)null, false));
                });

                runner.test("with empty path and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("", false));
                });

                runner.test("with relative path and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("relative/file.txt", false));
                });

                runner.test("with non-existing rooted path and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("/folder/file.csv", false));
                });

                runner.test("with existing rooted path with no contents and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv");

                    test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false)));
                });

                runner.test("with existing rooted path with single line and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "abcdef");
                    test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false)));
                });

                runner.test("with existing rooted path with multiple lines and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                    test.assertEqual(new String[] { "ab", "cd", "e", "f" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false)));
                });
            });

            runner.testGroup("getFileContentLines(String,boolean,CharacterEncoding)", () ->
            {
                runner.test("with existing rooted path with multiple lines, character encoding, and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                    test.assertEqual(new String[] { "ab", "cd", "e", "f" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", false, CharacterEncoding.UTF_8)));
                });
            });

            runner.testGroup("getFileContentLines(Path,boolean)", () ->
            {
                runner.test("with null path and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines((Path)null, true));
                });

                runner.test("with empty path and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse(""), true));
                });

                runner.test("with relative path and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt"), true));
                });

                runner.test("with non-existing rooted path and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true));
                });

                runner.test("with existing rooted path with no contents and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv");
                    test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true)));
                });

                runner.test("with existing rooted path with single line and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "abcdef");
                    test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true)));
                });

                runner.test("with existing rooted path with multiple lines and include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                    test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), true)));
                });

                runner.test("with null path and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines((Path)null, false));
                });

                runner.test("with empty path and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse(""), false));
                });

                runner.test("with relative path and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt"), false));
                });

                runner.test("with non-existing rooted path and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false));
                });

                runner.test("with existing rooted path with no contents and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv");
                    test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false)));
                });

                runner.test("with existing rooted path with single line and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "abcdef");
                    test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false)));
                });

                runner.test("with existing rooted path with multiple lines and don't include new lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                    test.assertEqual(new String[] { "ab", "cd", "e", "f" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), false)));
                });
            });

            runner.testGroup("getFileContentLines(String,CharacterEncoding)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines((String)null, CharacterEncoding.UTF_8));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("", CharacterEncoding.UTF_8));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("relative/file.txt", CharacterEncoding.UTF_8));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv");
                    test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8)));
                });

                runner.test("with existing rooted path with single line", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "abcdef");
                    test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8)));
                });

                runner.test("with existing rooted path with multiple lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");
                    test.assertEqual(new String[] { "ab\n", "cd\r\n", "e\n", "f\n" }, Array.toStringArray(fileSystem.getFileContentLines("/folder/file.csv", CharacterEncoding.UTF_8)));
                });
            });

            runner.testGroup("getFileContentLines(Path,CharacterEncoding)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines((Path)null, CharacterEncoding.UTF_8));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse(""), CharacterEncoding.UTF_8));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse("relative/file.txt"), CharacterEncoding.UTF_8));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertNull(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), CharacterEncoding.UTF_8));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv");
                    test.assertEqual(new String[0], Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), CharacterEncoding.UTF_8)));
                });

                runner.test("with existing rooted path with single line", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "abcdef");
                    test.assertEqual(new String[] { "abcdef" }, Array.toStringArray(fileSystem.getFileContentLines(Path.parse("/folder/file.csv"), CharacterEncoding.UTF_8)));
                });

                runner.test("with existing rooted path with multiple lines", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/folder/file.csv", "ab\ncd\r\ne\nf\n");

                    final String[] expected = new String[] { "ab\n", "cd\r\n", "e\n", "f\n" };
                    final Path filePath = Path.parse("/folder/file.csv");
                    final Iterable<String> fileLines = fileSystem.getFileContentLines(filePath, CharacterEncoding.UTF_8);
                    final String[] actual = Array.toStringArray(fileLines);
                    test.assertEqual(expected, actual);
                });
            });

            runner.testGroup("setFileContents(String,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents((String)null, new byte[] { 0, 1, 2 }));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents("", new byte[] { 0, 1, 2 }));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents("relative.file", new byte[] { 0, 1, 2 }));
                });

                runner.test("with non-existing rooted path with null contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents("/A.txt", (byte[])null));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

                    test.assertTrue(fileSystem.setFileContents("/A.txt", (byte[])null));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[0]));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt", new byte[] { 0, 1 });

                    test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[0]));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents("/folder/A.txt", new byte[] { 0, 1, 2 }));
                    test.assertTrue(fileSystem.folderExists("/folder"));
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt"));
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/folder/A.txt"));
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt");

                    test.assertTrue(fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
                });
            });

            runner.testGroup("setFileContents(Path,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents((Path)null, new byte[] { 0, 1, 2 }));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents(Path.parse(""), new byte[] { 0, 1, 2 }));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents(Path.parse("relative.file"), new byte[] { 0, 1, 2 }));
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (byte[])null));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (byte[])null));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[0], fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt");

                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));

                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContents("/A.txt"));
                });
            });

            runner.testGroup("setFileContents(String,String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents((String)null, "ABC"));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents("", "ABC"));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents("relative.file", "ABC"));
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents("/A.txt", (String)null));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                    test.assertTrue(fileSystem.setFileContents("/A.txt", (String)null));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents("/A.txt", ""));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                    test.assertTrue(fileSystem.setFileContents("/A.txt", ""));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with non-existing rooted path with non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents("/A.txt", "ABC"));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents("/folder/A.txt", "ABC"));
                    test.assertTrue(fileSystem.folderExists("/folder"));
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt"));
                    test.assertEqual("ABC", fileSystem.getFileContentsAsString("/folder/A.txt"));
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt");
                    test.assertTrue(fileSystem.setFileContents("/A.txt", "ABC"));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
                });
            });

            runner.testGroup("setFileContents(Path,String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents((Path)null, "ABC"));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents(Path.parse(""), "ABC"));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertFalse(fileSystem.setFileContents(Path.parse("relative.file"), "ABC"));
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (String)null));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt", "Test");
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), (String)null));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), ""));
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt", new byte[] { 0, 1 });
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), ""), "Unable to set the file's contents after creating the file");
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    final Value<String> error = new Value<>();
                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), "ABC", error::set), error.get());
                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt");

                    test.assertTrue(fileSystem.setFileContents(Path.parse("/A.txt"), "ABC"));

                    test.assertTrue(fileSystem.fileExists("/A.txt"));
                    test.assertEqual("ABC", fileSystem.getFileContentsAsString("/A.txt"));
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesAndFoldersRecursively((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesAndFoldersRecursively(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesAndFoldersRecursively("test/folder"));
                });

                runner.test("with rooted path when root doesn't exist", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesAndFoldersRecursively("F:/test/folder"));
                });

                runner.test("with rooted path when parent folder doesn't exist", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesAndFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder doesn't exist", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/");
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesAndFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder is empty", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/folder");
                    test.assertEqual(new Array<FileSystemEntry>(0), fileSystem.getFilesAndFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder has files", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/folder");
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");
                    test.assertEqual(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            fileSystem.getFile("/test/folder/1.txt"),
                            fileSystem.getFile("/test/folder/2.txt")
                        }),
                        fileSystem.getFilesAndFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder has folders", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/test/folder");
                    fileSystem.createFolder("/test/folder/1.txt");
                    fileSystem.createFolder("/test/folder/2.txt");
                    test.assertEqual(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            fileSystem.getFolder("/test/folder/1.txt"),
                            fileSystem.getFolder("/test/folder/2.txt")
                        }),
                        fileSystem.getFilesAndFoldersRecursively("/test/folder"));
                });

                runner.test("with rooted path when folder has grandchild files and folders", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");
                    fileSystem.createFile("/test/folder/A/3.csv");
                    fileSystem.createFile("/test/folder/B/C/4.xml");
                    fileSystem.createFile("/test/folder/A/5.png");

                    final Iterable<FileSystemEntry> expectedEntries =
                        Array.fromValues(new FileSystemEntry[]
                        {
                            fileSystem.getFolder("/test/folder/A"),
                            fileSystem.getFolder("/test/folder/B"),
                            fileSystem.getFile("/test/folder/1.txt"),
                            fileSystem.getFile("/test/folder/2.txt"),
                            fileSystem.getFile("/test/folder/A/3.csv"),
                            fileSystem.getFile("/test/folder/A/5.png"),
                            fileSystem.getFolder("/test/folder/B/C"),
                            fileSystem.getFile("/test/folder/B/C/4.xml")
                        });
                    final Value<String> error = new Value<>();
                    final Iterable<FileSystemEntry> actualEntries = fileSystem.getFilesAndFoldersRecursively("/test/folder", error::set);
                    test.assertFalse(error.hasValue(), error.toString());
                    test.assertEqual(expectedEntries, actualEntries);
                });
            });
        });
    }
}
