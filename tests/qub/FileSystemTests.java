package qub;

public class FileSystemTests
{
    public static void test(TestRunner runner, Function1<AsyncRunner,FileSystem> creator)
    {
        runner.testGroup(FileSystem.class, () ->
        {
            runner.testGroup("rootExists()", () ->
            {
                final Action1<String> rootExistsTest = (String rootPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertFalse(fileSystem.rootExists(rootPath));
                    });
                };

                rootExistsTest.run(null);
                rootExistsTest.run("");
                rootExistsTest.run("notme:/");
            });

            runner.testGroup("rootExistsAsync(String)", () ->
            {
                final Action2<String,Boolean> rootExistsAsyncTest = (String rootPath, Boolean expectedToExist) ->
                {
                    runner.test("with " + (rootPath == null ? null : "\"" + rootPath + "\""), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        fileSystem.rootExistsAsync(rootPath)
                            .then((Boolean rootExists) ->
                            {
                                test.assertEqual(expectedToExist, rootExists);
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
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        fileSystem.rootExistsAsync(Path.parse(rootPath))
                            .then((Boolean rootExists) ->
                            {
                                test.assertEqual(expectedToExist, rootExists);
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
                FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                fileSystem.getRootsAsync()
                    .then((Iterable<Root> roots) ->
                    {
                        test.assertNotNull(roots);
                        test.assertTrue(1 <= roots.getCount());
                    });
            });

            runner.testGroup("getFilesAndFolders(String)", () ->
            {
                final Action3<String, Action1<FileSystem>, String[]> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(Array.fromValues(expectedEntryPaths), fileSystem.getFilesAndFolders(folderPath).map(FileSystemEntry::toString));
                    });
                };

                getFilesAndFoldersTest.run(null, null, new String[0]);
                getFilesAndFoldersTest.run("", null, new String[0]);
            });

            runner.testGroup("getFilesAndFolders(Path)", () ->
            {
                final Action3<String, Action1<FileSystem>, String[]> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(Array.fromValues(expectedEntryPaths), fileSystem.getFilesAndFolders(Path.parse(folderPath)).map(FileSystemEntry::toString));
                    });
                };

                getFilesAndFoldersTest.run(null, null, new String[0]);
                getFilesAndFoldersTest.run("", null, new String[0]);
                getFilesAndFoldersTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                    },
                    new String[0]);
                getFilesAndFoldersTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB");
                        fileSystem.createFile("/file1.txt");
                        fileSystem.createFile("/folderA/file2.csv");
                    },
                    new String[] { "/folderA", "/file1.txt" });
            });

            runner.testGroup("getFilesAndFoldersAsync(String)", () ->
            {
                final Action3<Action1<FileSystem>,String,String[]> getFilesAndFoldersAsyncTest = (Action1<FileSystem> setup, String folderPath, String[] expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.getFilesAndFoldersAsync(folderPath)
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertEqual(Array.fromValues(expectedEntryPaths), entries.map(FileSystemEntry::toString));
                            });
                    });
                };

                getFilesAndFoldersAsyncTest.run(null, null, new String[0]);
                getFilesAndFoldersAsyncTest.run(null, "", new String[0]);
                getFilesAndFoldersAsyncTest.run(null, "/i/dont/exist/", new String[0]);
                getFilesAndFoldersAsyncTest.run(
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderA"),
                    "/folderA",
                    new String[0]);
                getFilesAndFoldersAsyncTest.run(
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                        fileSystem.createFolder("/folderA/folderB");
                        fileSystem.createFile("/file1.txt");
                        fileSystem.createFile("/folderA/file2.csv");
                    },
                    "/",
                    new String[] { "/folderA", "/file1.txt" });
            });

            runner.testGroup("getFilesAndFoldersAsync(Path)", () ->
            {
                final Action3<Action1<FileSystem>,String,String[]> getFilesAndFoldersAsyncTest = (Action1<FileSystem> setup, String folderPath, String[] expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.getFilesAndFoldersAsync(Path.parse(folderPath))
                            .then((Iterable<FileSystemEntry> entries) ->
                            {
                                test.assertEqual(Array.fromValues(expectedEntryPaths), entries.map(FileSystemEntry::toString));
                            });
                    });
                };

                getFilesAndFoldersAsyncTest.run(null, null, null);
                getFilesAndFoldersAsyncTest.run(null, "", new String[0]);
                getFilesAndFoldersAsyncTest.run(null, "/i/dont/exist/", new String[0]);
                getFilesAndFoldersAsyncTest.run(
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderA"),
                    "/folderA",
                    new String[0]);
                getFilesAndFoldersAsyncTest.run(
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                        fileSystem.createFolder("/folderA/folderB");
                        fileSystem.createFile("/file1.txt");
                        fileSystem.createFile("/folderA/file2.csv");
                    },
                    "/",
                    new String[] { "/folderA", "/file1.txt" });
            });

            runner.testGroup("getFolders(String)", () ->
            {
                final Action1<String> getFoldersTest = (String path) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertEqual(new Array<Folder>(0), fileSystem.getFolders(path));
                    });
                };

                getFoldersTest.run(null);
                getFoldersTest.run("");
            });

            runner.testGroup("getFoldersRecursively(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,String[]> getFoldersRecursivelyTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFolderPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {

                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(Array.fromValues(expectedFolderPaths), fileSystem.getFoldersRecursively(folderPath).map(Folder::toString));
                    });
                };

                getFoldersRecursivelyTest.run(null, null, new String[0]);
                getFoldersRecursivelyTest.run("", null, new String[0]);
                getFoldersRecursivelyTest.run("test/folder", null, new String[0]);
                getFoldersRecursivelyTest.run("F:/test/folder", null, new String[0]);
                getFoldersRecursivelyTest.run("/test/folder", null, new String[0]);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/");
                    },
                    new String[0]);
                getFoldersRecursivelyTest.run(
                    "/test/folder/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                    },
                    new String[0]);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                    },
                    new String[0]);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder/1.txt");
                        fileSystem.createFolder("/test/folder/2.txt");
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" });
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                        fileSystem.createFile("/test/folder/A/3.csv");
                        fileSystem.createFile("/test/folder/B/C/4.xml");
                        fileSystem.createFile("/test/folder/A/5.png");
                    },
                    new String[] { "/test/folder/A", "/test/folder/B", "/test/folder/B/C" });
            });

            runner.testGroup("getFoldersAsync(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,String[]> getFoldersAsyncTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFolderPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.getFoldersAsync(folderPath)
                            .then((Iterable<Folder> folders) ->
                            {
                                test.assertEqual(Array.fromValues(expectedFolderPaths), folders.map(Folder::toString));
                            });
                    });
                };

                getFoldersAsyncTest.run(null, null, new String[0]);
                getFoldersAsyncTest.run("", null, new String[0]);
            });

            runner.testGroup("getFoldersAsync(Path)", () ->
            {
                final Action3<String,Action1<FileSystem>,String[]> getFoldersAsyncTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFolderPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.getFoldersAsync(Path.parse(folderPath))
                            .then((Iterable<Folder> folders) ->
                            {
                                test.assertEqual(Array.fromValues(expectedFolderPaths), folders.map(Folder::toString));
                            });
                    });
                };

                getFoldersAsyncTest.run(null, null, new String[0]);
                getFoldersAsyncTest.run("", null, new String[0]);
            });

            runner.testGroup("getFilesAsync(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,String[]> getFilesAsyncTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.getFilesAsync(folderPath)
                            .then((Iterable<File> files) ->
                            {
                                test.assertEqual(Array.fromValues(expectedFilePaths), files.map(File::toString));
                            });
                    });
                };

                getFilesAsyncTest.run(null, null, new String[0]);
                getFilesAsyncTest.run("", null, new String[0]);
            });

            runner.testGroup("getFilesAsync(Path)", () ->
            {
                final Action3<String,Action1<FileSystem>,String[]> getFilesAsyncTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.getFilesAsync(Path.parse(folderPath))
                            .then((Iterable<File> files) ->
                            {
                                test.assertEqual(Array.fromValues(expectedFilePaths), files.map(File::toString));
                            });
                    });
                };

                getFilesAsyncTest.run(null, null, new String[0]);
                getFilesAsyncTest.run("", null, new String[0]);
            });

            runner.testGroup("getFiles(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,String[]> getFilesTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(Array.fromValues(expectedFilePaths), fileSystem.getFiles(folderPath).map(File::toString));
                    });
                };

                getFilesTest.run(null, null, new String[0]);
                getFilesTest.run("", null, new String[0]);
            });

            runner.testGroup("getFilesRecursively(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,String[]> getFilesRecursivelyTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(Array.fromValues(expectedFilePaths), fileSystem.getFilesRecursively(folderPath).map(File::toString));
                    });
                };

                getFilesRecursivelyTest.run(null, null, new String[0]);
                getFilesRecursivelyTest.run("", null, new String[0]);
                getFilesRecursivelyTest.run("test/folder", null, new String[0]);
                getFilesRecursivelyTest.run("F:/test/folder", null, new String[0]);
                getFilesRecursivelyTest.run("/test/folder", null, new String[0]);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/test"),
                    new String[0]);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/test/folder"),
                    new String[0]);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" });
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                        fileSystem.createFolder("/test/folder/1.txt");
                        fileSystem.createFolder("/test/folder/2.txt");
                    },
                    new String[0]);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                        fileSystem.createFile("/test/folder/A/3.csv");
                        fileSystem.createFile("/test/folder/B/C/4.xml");
                        fileSystem.createFile("/test/folder/A/5.png");
                    },
                    new String[]
                    {
                        "/test/folder/1.txt",
                        "/test/folder/2.txt",
                        "/test/folder/A/3.csv",
                        "/test/folder/A/5.png",
                        "/test/folder/B/C/4.xml"
                    });
            });

            runner.testGroup("getFolder(String)", () ->
            {
                final Action2<String,Boolean> getFolderTest = (String folderPath, Boolean folderExpected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        final Folder folder = fileSystem.getFolder(folderPath);
                        if (folderExpected)
                        {
                            test.assertNotNull(folder);
                            test.assertEqual(folderPath, folder.toString());
                        }
                        else
                        {
                            test.assertNull(folder);
                        }
                    });
                };

                getFolderTest.run(null, false);
                getFolderTest.run("", false);
                getFolderTest.run("a/b/c", false);
                getFolderTest.run("/", true);
                getFolderTest.run("\\", true);
                getFolderTest.run("Z:\\", true);
                getFolderTest.run("/a/b", true);
            });

            runner.testGroup("folderExists(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expectedFolderExists, fileSystem.folderExists(folderPath));
                    });
                };

                folderExistsTest.run("/", null, true);
                folderExistsTest.run("/folderName", null, false);
                folderExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    true);
            });

            runner.testGroup("folderExistsAsync(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> folderExistsAsyncTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.folderExistsAsync(folderPath)
                            .then((Boolean folderExists) ->
                            {
                                test.assertEqual(expectedFolderExists, folderExists);
                            });
                    });
                };

                folderExistsAsyncTest.run(null, null, false);
                folderExistsAsyncTest.run("", null, false);
                folderExistsAsyncTest.run("/", null, true);
                folderExistsAsyncTest.run("/folderName", null, false);
                folderExistsAsyncTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    true);
            });

            runner.testGroup("folderExistsAsync(Path)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> folderExistsAsyncTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.folderExistsAsync(Path.parse(folderPath))
                            .then((Boolean folderExists) ->
                            {
                                test.assertEqual(expectedFolderExists, folderExists);
                            });
                    });
                };

                folderExistsAsyncTest.run(null, null, false);
                folderExistsAsyncTest.run("", null, false);
                folderExistsAsyncTest.run("/", null, true);
                folderExistsAsyncTest.run("/folderName", null, false);
                folderExistsAsyncTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    true);
            });

            runner.testGroup("createFolder(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,Boolean,Boolean> createFolderTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedCreateResult, Boolean expectedFolderExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expectedCreateResult, fileSystem.createFolder(folderPath));
                        test.assertEqual(expectedFolderExists, fileSystem.folderExists(folderPath));
                    });
                };

                createFolderTest.run(null, null, false, false);
                createFolderTest.run("", null, false, false);
                createFolderTest.run("folder", null, false, false);
                createFolderTest.run("/folder", null, true, true);
                createFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    false,
                    true);
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
                final Action4<String,Action1<FileSystem>,Boolean,Boolean> createFolderAsyncTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedCreateResult, Boolean expectedFolderExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.createFolderAsync(folderPath)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertEqual(expectedCreateResult, folderCreated);
                                test.assertEqual(expectedFolderExists, fileSystem.folderExists(folderPath));
                            });
                    });
                };

                createFolderAsyncTest.run(null, null, false, false);
                createFolderAsyncTest.run("", null, false, false);
                createFolderAsyncTest.run("folder", null, false, false);
                createFolderAsyncTest.run("/folder", null, true, true);
                createFolderAsyncTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    false,
                    true);
            });

            runner.testGroup("createFolderAsync(String,Out<Folder>)", () ->
            {
                final Action5<String,Action1<FileSystem>,Boolean,Boolean,Boolean> createFolderAsyncTest = (String folderPath, Action1<FileSystem> setup, Boolean provideOut, Boolean expectedCreateResult, Boolean expectedFolderExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath) + " and " + (provideOut ? "non-null" : "null") + " Out<Folder>", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        Value<Folder> out = provideOut ? new Value<>() : null;
                        fileSystem.createFolderAsync(folderPath, out)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertEqual(expectedCreateResult, folderCreated);
                                test.assertEqual(expectedFolderExists, fileSystem.folderExists(folderPath));
                                if (provideOut)
                                {
                                    test.assertEqual(expectedFolderExists, out.hasValue());
                                    if (expectedFolderExists)
                                    {
                                        test.assertEqual(folderPath, out.get().toString());
                                    }
                                }
                            });
                    });
                };

                createFolderAsyncTest.run(null, null, false, false, false);
                createFolderAsyncTest.run("", null, false, false, false);
                createFolderAsyncTest.run("folder", null, false, false, false);
                createFolderAsyncTest.run("/folder", null, false, true, true);
                createFolderAsyncTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    false,
                    false,
                    true);

                createFolderAsyncTest.run(null, null, true, false, false);
                createFolderAsyncTest.run("", null, true, false, false);
                createFolderAsyncTest.run("folder", null, true, false, false);
                createFolderAsyncTest.run("/folder", null, true, true, true);
                createFolderAsyncTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    false,
                    false,
                    true);
            });

            runner.testGroup("createFolderAsync(Path)", () ->
            {
                final Action4<String,Action1<FileSystem>,Boolean,Boolean> createFolderAsyncTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedCreateResult, Boolean expectedFolderExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.createFolderAsync(Path.parse(folderPath))
                            .then((Boolean folderCreated) ->
                            {
                                test.assertEqual(expectedCreateResult, folderCreated);
                                test.assertEqual(expectedFolderExists, fileSystem.folderExists(folderPath));
                            });
                    });
                };

                createFolderAsyncTest.run(null, null, false, false);
                createFolderAsyncTest.run("", null, false, false);
                createFolderAsyncTest.run("folder", null, false, false);
                createFolderAsyncTest.run("/folder", null, true, true);
                createFolderAsyncTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    false,
                    true);
            });

            runner.testGroup("createFolderAsync(Path,Out<Folder>)", () ->
            {
                final Action5<String,Action1<FileSystem>,Boolean,Boolean,Boolean> createFolderAsyncTest = (String folderPath, Action1<FileSystem> setup, Boolean provideOut, Boolean expectedCreateResult, Boolean expectedFolderExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath) + " and " + (provideOut ? "non-null" : "null") + " Out<Folder>", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        Value<Folder> out = provideOut ? new Value<>() : null;
                        fileSystem.createFolderAsync(Path.parse(folderPath), out)
                            .then((Boolean folderCreated) ->
                            {
                                test.assertEqual(expectedCreateResult, folderCreated);
                                test.assertEqual(expectedFolderExists, fileSystem.folderExists(folderPath));
                                if (provideOut)
                                {
                                    test.assertEqual(expectedFolderExists, out.hasValue());
                                    if (expectedFolderExists)
                                    {
                                        test.assertEqual(folderPath, out.get().toString());
                                    }
                                }
                            });
                    });
                };

                createFolderAsyncTest.run(null, null, false, false, false);
                createFolderAsyncTest.run("", null, false, false, false);
                createFolderAsyncTest.run("folder", null, false, false, false);
                createFolderAsyncTest.run("/folder", null, false, true, true);
                createFolderAsyncTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    false,
                    false,
                    true);

                createFolderAsyncTest.run(null, null, true, false, false);
                createFolderAsyncTest.run("", null, true, false, false);
                createFolderAsyncTest.run("folder", null, true, false, false);
                createFolderAsyncTest.run("/folder", null, true, true, true);
                createFolderAsyncTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    false,
                    false,
                    true);
            });

            runner.testGroup("deleteFolder(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedDeleteResult) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expectedDeleteResult, fileSystem.deleteFolder(folderPath));
                        test.assertFalse(fileSystem.deleteFolder(folderPath));
                    });
                };

                deleteFolderTest.run(null, null, false);
                deleteFolderTest.run("", null, false);
                deleteFolderTest.run("folder", null, false);
                deleteFolderTest.run("/folder", null, false);
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    true);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a");
                        fileSystem.createFolder("/folder/b");
                        fileSystem.createFolder("/folder/c");
                    },
                    true);
            });

            runner.testGroup("deleteFolderAsync(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedDeleteResult) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.deleteFolderAsync(folderPath)
                            .thenAsyncFunction((Boolean folderDeleted) ->
                            {
                                test.assertEqual(expectedDeleteResult, folderDeleted);
                                return fileSystem.folderExistsAsync(folderPath);
                            })
                            .thenAsyncFunction((Boolean folderExists) ->
                            {
                                test.assertFalse(folderExists);
                                return fileSystem.deleteFolderAsync(folderPath);
                            })
                            .then((Boolean folderDeletedAgain) ->
                            {
                                test.assertFalse(folderDeletedAgain);
                            });;
                    });
                };

                deleteFolderTest.run(null, null, false);
                deleteFolderTest.run("", null, false);
                deleteFolderTest.run("folder", null, false);
                deleteFolderTest.run("/folder", null, false);
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    true);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a");
                        fileSystem.createFolder("/folder/b");
                        fileSystem.createFolder("/folder/c");
                    },
                    true);
            });

            runner.testGroup("deleteFolderAsync(Path)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> deleteFolderAsyncTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedDeleteResult) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.deleteFolderAsync(Path.parse(folderPath))
                            .thenAsyncFunction((Boolean folderDeleted) ->
                            {
                                test.assertEqual(expectedDeleteResult, folderDeleted);
                                return fileSystem.folderExistsAsync(folderPath);
                            })
                            .thenAsyncFunction((Boolean folderExists) ->
                            {
                                test.assertFalse(folderExists);
                                return fileSystem.deleteFolderAsync(folderPath);
                            })
                            .then((Boolean folderDeletedAgain) ->
                            {
                                test.assertFalse(folderDeletedAgain);
                            });;
                    });
                };

                deleteFolderAsyncTest.run(null, null, false);
                deleteFolderAsyncTest.run("", null, false);
                deleteFolderAsyncTest.run("folder", null, false);
                deleteFolderAsyncTest.run("/folder", null, false);
                deleteFolderAsyncTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    true);
                deleteFolderAsyncTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a");
                        fileSystem.createFolder("/folder/b");
                        fileSystem.createFolder("/folder/c");
                    },
                    true);
            });

            runner.testGroup("getFile(String)", () ->
            {
                final Action2<String,Boolean> getFileTest = (String filePath, Boolean fileExpected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        final File file = fileSystem.getFile(filePath);
                        if (fileExpected)
                        {
                            test.assertNotNull(file);
                            test.assertEqual(filePath, file.toString());
                        }
                        else
                        {
                            test.assertNull(file);
                        }
                    });
                };

                getFileTest.run(null, false);
                getFileTest.run("", false);
                getFileTest.run("a/b/c", false);
                getFileTest.run("/", false);
                getFileTest.run("\\", false);
                getFileTest.run("Z:\\", false);
                getFileTest.run("/a/b", true);
            });

            runner.testGroup("fileExists(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> fileExistsTest = (String filePath, Action1<FileSystem> setup, Boolean expectedFileExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expectedFileExists, fileSystem.fileExists(filePath));
                    });
                };

                fileExistsTest.run("/", null, false);
                fileExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    false);
                fileExistsTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName"),
                    true);
            });

            runner.testGroup("fileExistsAsync(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> fileExistsAsyncTest = (String filePath, Action1<FileSystem> setup, Boolean expectedFileExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.fileExistsAsync(filePath)
                            .then((Boolean fileExists) ->
                            {
                                test.assertEqual(expectedFileExists, fileExists);
                            });
                    });
                };

                fileExistsAsyncTest.run("/", null, false);
                fileExistsAsyncTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    false);
                fileExistsAsyncTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName"),
                    true);
            });

            runner.testGroup("fileExistsAsync(Path)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> fileExistsAsyncTest = (String filePath, Action1<FileSystem> setup, Boolean expectedFileExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        fileSystem.fileExistsAsync(Path.parse(filePath))
                            .then((Boolean fileExists) ->
                            {
                                test.assertEqual(expectedFileExists, fileExists);
                            });
                    });
                };

                fileExistsAsyncTest.run("/", null, false);
                fileExistsAsyncTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    false);
                fileExistsAsyncTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName"),
                    true);
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

//            runner.testGroup("createFileAsync(String)", () ->
//            {
//                runner.test("with null path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync((String)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with empty path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync("")
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with relative path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync("things.txt")
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with non-existing rooted path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync("/things.txt")
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertTrue(fileCreated);
//                                test.assertTrue(fileSystem.fileExists("/things.txt"));
//                            });
//                    });
//                });
//
//                runner.test("with existing rooted path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFile("/things.txt");
//
//                        fileSystem.createFileAsync("/things.txt")
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertTrue(fileSystem.fileExists("/things.txt"));
//                            });
//                    });
//                });
//            });
//
//            runner.testGroup("createFileAsync(String,Out<File>)", () ->
//            {
//                runner.test("with null path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync((String)null, (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with empty path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync("", (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with relative path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync("things.txt", (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with non-existing rooted path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync("/things.txt", (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertTrue(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with existing rooted path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFile("/things.txt");
//
//                        fileSystem.createFileAsync("/things.txt", (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertTrue(fileSystem.fileExists("/things.txt"));
//                            });
//                    });
//                });
//
//                runner.test("with null path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync((String)null, file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertFalse(file.hasValue());
//                            });
//                    });
//                });
//
//                runner.test("with empty path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync("", file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertFalse(file.hasValue());
//                            });
//                    });
//                });
//
//                runner.test("with relative path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync("things.txt", file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertFalse(file.hasValue());
//                            });
//                    });
//                });
//
//                runner.test("with non-existing rooted path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync("/things.txt", file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertTrue(fileCreated);
//                                test.assertEqual("/things.txt", file.get().getPath().toString());
//                            });
//                    });
//                });
//
//                runner.test("with existing rooted path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        test.assertTrue(fileSystem.createFile("/things.txt", file));
//
//                        fileSystem.createFileAsync("/things.txt", file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated, "The createFileAsync() method should have failed.");
//                                test.assertTrue(file.hasValue(), "file should have had a value, even though the createFileAsync() method failed.");
//                                test.assertNotNull(file.get(), "file's value should have been not-null.");
//                                test.assertEqual(Path.parse("/things.txt"), file.get().getPath());
//                            });
//                    });
//                });
//
//                runner.test("with invalid path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync("/\u0000?#!.txt", file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertFalse(file.hasValue());
//                            });
//                    });
//                });
//            });
//
//            runner.testGroup("createFileAsync(Path)", () ->
//            {
//                runner.test("with null path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync((Path)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with empty path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync(Path.parse(""))
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with relative path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync(Path.parse("things.txt"))
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with non-existing rooted path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync(Path.parse("/things.txt"))
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertTrue(fileCreated);
//                                test.assertTrue(fileSystem.fileExists("/things.txt"));
//                            });
//                    });
//                });
//
//                runner.test("with existing rooted path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFile("/things.txt");
//
//                        fileSystem.createFileAsync(Path.parse("/things.txt"))
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertTrue(fileSystem.fileExists("/things.txt"));
//                            });
//                    });
//                });
//            });
//
//            runner.testGroup("createFileAsync(Path,Out<File>)", () ->
//            {
//                runner.test("with null path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync((Path)null, (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with empty path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync(Path.parse(""), (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with relative path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync(Path.parse("things.txt"), (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with non-existing rooted path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFileAsync(Path.parse("/things.txt"), (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertTrue(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with existing rooted path and null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFile("/things.txt");
//
//                        fileSystem.createFileAsync(Path.parse("/things.txt"), (Out<File>)null)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                            });
//                    });
//                });
//
//                runner.test("with null path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync((Path)null, file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertFalse(file.hasValue());
//                            });
//                    });
//                });
//
//                runner.test("with empty path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync(Path.parse(""), file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertFalse(file.hasValue());
//                            });
//                    });
//                });
//
//                runner.test("with relative path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync(Path.parse("things.txt"), file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertFalse(file.hasValue());
//                            });
//                    });
//                });
//
//                runner.test("with non-existing rooted path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync(Path.parse("/things.txt"), file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertTrue(fileCreated);
//                                test.assertEqual("/things.txt", file.get().getPath().toString());
//                            });
//                    });
//                });
//
//                runner.test("with existing rooted path and non-null value", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFile("/things.txt", file);
//
//                        fileSystem.createFileAsync(Path.parse("/things.txt"), file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertTrue(file.hasValue());
//                                test.assertNotNull(file.get());
//                                test.assertEqual(Path.parse("/things.txt"), file.get().getPath());
//                            });
//                    });
//                });
//
//                runner.test("with invalid path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        final Value<File> file = new Value<>();
//                        fileSystem.createFileAsync(Path.parse("/\u0000?#!.txt"), file)
//                            .then((Boolean fileCreated) ->
//                            {
//                                test.assertFalse(fileCreated);
//                                test.assertFalse(file.hasValue());
//                            });
//                    });
//                });
//            });

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

//            runner.testGroup("deleteFileAsync(String)", () ->
//            {
//                runner.test("with null path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.deleteFileAsync((String)null)
//                            .then((Boolean fileDeleted) ->
//                            {
//                                test.assertFalse(fileDeleted);
//                            });
//                    });
//                });
//
//                runner.test("with empty path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.deleteFileAsync("")
//                            .then((Boolean fileDeleted) ->
//                            {
//                                test.assertFalse(fileDeleted);
//                            });
//                    });
//                });
//
//                runner.test("with relative path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.deleteFileAsync("relativeFile.txt")
//                            .then((Boolean fileDeleted) ->
//                            {
//                                test.assertFalse(fileDeleted);
//                            });
//                    });
//                });
//
//                runner.test("with non-existing rooted path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.deleteFileAsync("/idontexist.txt")
//                            .then((Boolean fileDeleted) ->
//                            {
//                                test.assertFalse(fileDeleted);
//                            });
//                    });
//                });
//
//                runner.test("with existing rooted path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFile("/iexist.txt");
//                        fileSystem.deleteFileAsync("/iexist.txt")
//                            .then((Boolean fileDeleted) ->
//                            {
//                                test.assertTrue(fileDeleted);
//                                test.assertFalse(fileSystem.fileExists("/iexist.txt"));
//                            });
//                    });
//                });
//            });
//
//            runner.testGroup("deleteFileAsync(Path)", () ->
//            {
//                runner.test("with null path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.deleteFileAsync((Path)null)
//                            .then((Boolean fileDeleted) ->
//                            {
//                                test.assertFalse(fileDeleted);
//                            });
//                    });
//                });
//
//                runner.test("with empty path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.deleteFileAsync(Path.parse(""))
//                            .then((Boolean fileDeleted) ->
//                            {
//                                test.assertFalse(fileDeleted);
//                            });
//                    });
//                });
//
//                runner.test("with relative path", (Test test) ->
//                {
//                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
//                    fileSystem.deleteFileAsync(Path.parse("relativeFile.txt"))
//                        .then((Boolean fileDeleted) ->
//                        {
//                            test.assertFalse(fileDeleted);
//                        });
//                });
//
//                runner.test("with non-existing rooted path", (Test test) ->
//                {
//                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
//                    fileSystem.deleteFileAsync(Path.parse("/idontexist.txt"))
//                        .then((Boolean fileDeleted) ->
//                        {
//                            test.assertFalse(fileDeleted);
//                        });
//                });
//
//                runner.test("with existing rooted path", (Test test) ->
//                {
//                    asyncTest.run(test, (FileSystem fileSystem) ->
//                    {
//                        fileSystem.createFile("/iexist.txt");
//                        fileSystem.deleteFileAsync(Path.parse("/iexist.txt"))
//                            .then((Boolean fileDeleted) ->
//                            {
//                                test.assertTrue(fileDeleted);
//                                test.assertFalse(fileSystem.fileExists("/iexist.txt"));
//                            });
//                    });
//                });
//            });

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
