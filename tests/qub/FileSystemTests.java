package qub;

public class FileSystemTests
{
    public static void test(TestRunner runner, Function1<AsyncRunner,FileSystem> creator)
    {
        runner.testGroup(FileSystem.class, () ->
        {
            runner.testGroup("rootExists(String)", () ->
            {
                final Action1<String> rootExistsFailureTest = (String rootPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.rootExists(Path.parse(rootPath)));
                    });
                };

                rootExistsFailureTest.run(null);
                rootExistsFailureTest.run("");

                final Action2<String,Boolean> rootExistsTest = (String rootPath, Boolean expectedValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertSuccess(expectedValue, fileSystem.rootExists(rootPath));
                    });
                };

                rootExistsTest.run("C@:/", false);
                rootExistsTest.run("notme:\\", false);
                rootExistsTest.run("/", true);
            });

            runner.testGroup("rootExists(Path)", () ->
            {
                final Action1<String> rootExistsFailureTest = (String rootPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.rootExists(Path.parse(rootPath)));
                    });
                };

                rootExistsFailureTest.run(null);
                rootExistsFailureTest.run("");

                final Action2<String,Boolean> rootExistsTest = (String rootPath, Boolean expectedValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertSuccess(expectedValue, fileSystem.rootExists(Path.parse(rootPath)));
                    });
                };

                rootExistsTest.run("C@:/", false);
                rootExistsTest.run("notme:\\", false);
                rootExistsTest.run("/", true);
            });

            runner.test("getRoot()", (Test test) ->
            {
                FileSystem fileSystem = creator.run(test.getParallelAsyncRunner());
                final Result<Root> rootResult = fileSystem.getRoot("/daffy/");
                test.assertNotNull(rootResult);

                final Root root = rootResult.getValue();
                test.assertNotNull(root);
                test.assertEqual("/", root.toString());
            });

            runner.test("getRoots()", (Test test) ->
            {
                final FileSystem fileSystem = creator.run(test.getParallelAsyncRunner());
                final Result<Iterable<Root>> rootsResult = fileSystem.getRoots();
                test.assertSuccess(rootsResult);
                test.assertTrue(rootsResult.getValue().any());
            });

            runner.testGroup("getFilesAndFolders(String)", () ->
            {
                final Action1<String> getFilesAndFoldersFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFilesAndFolders(folderPath));
                    });
                };

                getFilesAndFoldersFailureTest.run(null);
                getFilesAndFoldersFailureTest.run("");

                final Action4<String, Action1<FileSystem>, String[], Throwable> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFolders(folderPath);
                        test.assertNotNull(result);

                        if (expectedEntryPaths == null)
                        {
                            test.assertError(expectedError, result);
                        }
                        else
                        {
                            test.assertSuccess(result);
                            test.assertEqual(Iterable.create(expectedEntryPaths), result.getValue().map(FileSystemEntry::toString));
                        }
                    });
                };

                getFilesAndFoldersTest.run("/", null, new String[0], null);
                getFilesAndFoldersTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                    },
                    new String[0],
                    null);
                getFilesAndFoldersTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB");
                        fileSystem.createFile("/file1.txt");
                        fileSystem.createFile("/folderA/file2.csv");
                    },
                    new String[] { "/folderA", "/file1.txt" },
                    null);
                getFilesAndFoldersTest.run(
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFilesAndFoldersTest.run(
                    "/a/..",
                    null,
                    new String[0],
                    null);
            });

            runner.testGroup("getFilesAndFolders(Path)", () ->
            {
                final Action1<String> getFilesAndFoldersFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFilesAndFolders(Path.parse(folderPath)));
                    });
                };

                getFilesAndFoldersFailureTest.run(null);
                getFilesAndFoldersFailureTest.run("");

                final Action4<String, Action1<FileSystem>, String[], Throwable> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFolders(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedEntryPaths == null)
                        {
                            test.assertError(expectedError, result);
                        }
                        else
                        {
                            test.assertSuccess(result);
                            test.assertEqual(Iterable.create(expectedEntryPaths), result.getValue().map(FileSystemEntry::toString));
                        }
                    });
                };

                getFilesAndFoldersTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                    },
                    new String[0],
                    null);
                getFilesAndFoldersTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB");
                        fileSystem.createFile("/file1.txt");
                        fileSystem.createFile("/folderA/file2.csv");
                    },
                    new String[] { "/folderA", "/file1.txt" },
                    null);
                getFilesAndFoldersTest.run(
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFilesAndFoldersTest.run(
                    "/a/..",
                    null,
                    new String[0],
                    null);
            });

            runner.testGroup("getFolders(String)", () ->
            {
                final Action1<String> getFoldersFailureTest = (String path) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFolders(path));
                    });
                };

                getFoldersFailureTest.run(null);
                getFoldersFailureTest.run("");

                runner.test("with " + Strings.escapeAndQuote("/.."), (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertError(new IllegalArgumentException("Cannot resolve a rooted path outside of its root."), fileSystem.getFolders("/.."));
                });
            });

            runner.testGroup("getFoldersRecursively(String)", () ->
            {
                final Action1<String> getFoldersRecursivelyFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFoldersRecursively(folderPath));
                    });
                };

                getFoldersRecursivelyFailureTest.run(null);
                getFoldersRecursivelyFailureTest.run("");
                getFoldersRecursivelyFailureTest.run("test/folder");

                final Action4<String,Action1<FileSystem>,String[],Throwable> getFoldersRecursivelyTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFolderPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<Folder>> result = fileSystem.getFoldersRecursively(folderPath);
                        test.assertNotNull(result);

                        if (expectedFolderPaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Iterable.create(expectedFolderPaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFoldersRecursivelyTest.run("F:/test/folder", null, null, new FolderNotFoundException("F:/test/folder"));
                getFoldersRecursivelyTest.run("/test/folder", null, null, new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/");
                    },
                    null,
                    new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder/1.txt");
                        fileSystem.createFolder("/test/folder/2.txt");
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" },
                    null);
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
                    new String[] { "/test/folder/A", "/test/folder/B", "/test/folder/B/C" },
                    null);
                getFoldersRecursivelyTest.run(
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFoldersRecursivelyTest.run(
                    "/a/..",
                    null,
                    new String[0],
                    null);
            });

            runner.testGroup("getFoldersRecursively(Path)", () ->
            {
                final Action1<String> getFoldersRecursivelyFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFoldersRecursively(Path.parse(folderPath)));
                    });
                };

                getFoldersRecursivelyFailureTest.run(null);
                getFoldersRecursivelyFailureTest.run("");
                getFoldersRecursivelyFailureTest.run("test/folder");

                final Action4<String,Action1<FileSystem>,String[],Throwable> getFoldersRecursivelyTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFolderPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<Folder>> result = fileSystem.getFoldersRecursively(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedFolderPaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Iterable.create(expectedFolderPaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFoldersRecursivelyTest.run("F:/test/folder", null, null, new FolderNotFoundException("F:/test/folder"));
                getFoldersRecursivelyTest.run("/test/folder", null, null, new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/");
                    },
                    null,
                    new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder/1.txt");
                        fileSystem.createFolder("/test/folder/2.txt");
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" },
                    null);
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
                    new String[] { "/test/folder/A", "/test/folder/B", "/test/folder/B/C" },
                    null);
                getFoldersRecursivelyTest.run(
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFoldersRecursivelyTest.run(
                    "/a/..",
                    null,
                    new String[0],
                    null);
            });

            runner.testGroup("getFiles(String)", () ->
            {
                final Action1<String> getFilesFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFiles(folderPath));
                    });
                };

                getFilesFailureTest.run(null);
                getFilesFailureTest.run("");

                final Action4<String,Action1<FileSystem>,String[],Throwable> getFilesTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<File>> result = fileSystem.getFiles(folderPath);
                        test.assertNotNull(result);

                        if (expectedFilePaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Iterable.create(expectedFilePaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFilesTest.run(
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFilesTest.run(
                    "/a/..",
                    null,
                    new String[0],
                    null);
            });

            runner.testGroup("getFiles(Path)", () ->
            {
                final Action1<String> getFilesFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFiles(Path.parse(folderPath)));
                    });
                };

                getFilesFailureTest.run(null);
                getFilesFailureTest.run("");

                final Action4<String,Action1<FileSystem>,String[],Throwable> getFilesTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<File>> result = fileSystem.getFiles(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedFilePaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Iterable.create(expectedFilePaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFilesTest.run(
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFilesTest.run(
                    "/a/..",
                    null,
                    new String[0],
                    null);
            });

            runner.testGroup("getFilesRecursively(String)", () ->
            {
                final Action1<String> getFilesRecursivelyFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFilesRecursively(folderPath));
                    });
                };

                getFilesRecursivelyFailureTest.run(null);
                getFilesRecursivelyFailureTest.run("");
                getFilesRecursivelyFailureTest.run("test/folder");

                final Action4<String,Action1<FileSystem>,String[],Throwable> getFilesRecursivelyTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<File>> result = fileSystem.getFilesRecursively(folderPath);
                        test.assertNotNull(result);

                        if (expectedFilePaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Iterable.create(expectedFilePaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFilesRecursivelyTest.run("F:/test/folder", null, null, new FolderNotFoundException("F:/test/folder"));
                getFilesRecursivelyTest.run("/test/folder", null, null, new FolderNotFoundException("/test/folder"));
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/test"),
                    null,
                    new FolderNotFoundException("/test/folder"));
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/test/folder"),
                    new String[0],
                    null);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" },
                    null);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                        fileSystem.createFolder("/test/folder/1.txt");
                        fileSystem.createFolder("/test/folder/2.txt");
                    },
                    new String[0],
                    null);
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
                    },
                    null);
                getFilesRecursivelyTest.run(
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFilesRecursivelyTest.run(
                    "/a/..",
                    null,
                    new String[0],
                    null);
            });

            runner.testGroup("getFolder(String)", () ->
            {
                final Action1<String> getFolderFailureTest = (String folderPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPathString), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFolder(folderPathString));
                    });
                };

                getFolderFailureTest.run(null);
                getFolderFailureTest.run("");
                getFolderFailureTest.run("a/b/c");

                final Action3<String,String,Throwable> getFolderTest = (String folderPathString, String expectedFolderPathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPathString), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<Folder> folder = fileSystem.getFolder(folderPathString);
                        if (expectedError == null)
                        {
                            test.assertSuccess(folder);
                            test.assertEqual(expectedFolderPathString, folder.getValue().toString());
                        }
                        else
                        {
                            test.assertError(expectedError, folder);
                        }
                    });
                };

                getFolderTest.run("/", "/", null);
                getFolderTest.run("\\", "/", null);
                getFolderTest.run("Z:\\", "Z:/", null);
                getFolderTest.run("/a/b", "/a/b", null);
                getFolderTest.run("/..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFolderTest.run("/a/..", "/", null);
            });

            runner.testGroup("folderExists(String)", () ->
            {
                final Action1<String> folderExistsFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.folderExists(folderPath));
                    });
                };

                folderExistsFailureTest.run(null);
                folderExistsFailureTest.run("");

                final Action4<String,Action1<FileSystem>,Boolean,Throwable> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Boolean> result = fileSystem.folderExists(folderPath);
                        test.assertNotNull(result);

                        if (expectedFolderExists == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(expectedFolderExists, result.getValue());
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                folderExistsTest.run("/", null, true, null);
                folderExistsTest.run("/folderName", null, false, null);
                folderExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    true,
                    null);
            });

            runner.testGroup("folderExists(Path)", () ->
            {
                final Action1<String> folderExistsFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.folderExists(Path.parse(folderPath)));
                    });
                };

                folderExistsFailureTest.run(null);
                folderExistsFailureTest.run("");

                final Action4<String,Action1<FileSystem>,Boolean,Throwable> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Boolean> result = fileSystem.folderExists(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedFolderExists == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(expectedFolderExists, result.getValue());
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                folderExistsTest.run("/", null, true, null);
                folderExistsTest.run("/folderName", null, false, null);
                folderExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    true,
                    null);
                folderExistsTest.run(
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                folderExistsTest.run(
                    "/a/..",
                    null,
                    true,
                    null);
            });

            runner.testGroup("createFolder(String)", () ->
            {
                final Action2<String,String> createFolderFailureTest = (String testName, String folderPath) ->
                {
                    runner.test(testName + " (" + folderPath + ")", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.createFolder(folderPath));
                    });
                };

                createFolderFailureTest.run("with null", null);
                createFolderFailureTest.run("with empty string", "");
                createFolderFailureTest.run("with relative path", "folder");

                final Action5<String,String,Action1<FileSystem>,String,Throwable> createFolderTest = (String testName, String folderPath, Action1<FileSystem> setup, String expectedCreatedFolderPath, Throwable expectedError) ->
                {
                    runner.test(testName + " (" + folderPath + ")", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertDone(expectedCreatedFolderPath, expectedError, fileSystem.createFolder(folderPath).then(Folder::toString));
                    });
                };

                createFolderTest.run("with rooted path that doesn't exist", "/folder", null, "/folder", null);
                createFolderTest.run(
                    "with rooted path that already exists",
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    "/folder",
                    new FolderAlreadyExistsException("/folder"));
                createFolderTest.run(
                    "with rooted path that resolves outside of its root",
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                createFolderTest.run(
                    "with rooted path that resolves back to the root",
                    "/a/..",
                    null,
                    "/",
                    new FolderAlreadyExistsException("/"));
            });

            runner.testGroup("createFolder(Path)", () ->
            {
                final Action2<String,String> createFolderFailureTest = (String testName, String folderPath) ->
                {
                    runner.test(testName + " (" + folderPath + ")", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.createFolder(Path.parse(folderPath)));
                    });
                };

                createFolderFailureTest.run("with null", null);
                createFolderFailureTest.run("with empty string", "");
                createFolderFailureTest.run("with relative path", "folder");

                final Action5<String,String,Action1<FileSystem>,String,Throwable> createFolderTest = (String testName, String folderPath, Action1<FileSystem> setup, String expectedCreatedFolderPath, Throwable expectedError) ->
                {
                    runner.test(testName + " (" + folderPath + ")", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertDone(expectedCreatedFolderPath, expectedError, fileSystem.createFolder(Path.parse(folderPath)).then(Folder::toString));
                    });
                };

                createFolderTest.run("with rooted path that doesn't exist", "/folder", null, "/folder", null);
                createFolderTest.run(
                    "with rooted path that already exists",
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    "/folder",
                    new FolderAlreadyExistsException("/folder"));
                createFolderTest.run(
                    "with rooted path that resolves outside of its root",
                    "/..",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                createFolderTest.run(
                    "with rooted path that resolves back to the root",
                    "/a/..",
                    null,
                    "/",
                    new FolderAlreadyExistsException("/"));
            });

            runner.testGroup("deleteFolder(String)", () ->
            {
                final Action1<String> deleteFolderFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.deleteFolder(folderPath));
                    });
                };

                deleteFolderFailureTest.run(null);
                deleteFolderFailureTest.run("");
                deleteFolderFailureTest.run("folder");

                final Action3<String,Action1<FileSystem>,Throwable> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertError(expectedError, fileSystem.deleteFolder(folderPath));
                    });
                };

                deleteFolderTest.run("/folder", null, new FolderNotFoundException("/folder"));
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    null);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a");
                        fileSystem.createFolder("/folder/b");
                        fileSystem.createFolder("/folder/c");
                    },
                    null);
                deleteFolderTest.run(
                    "/..",
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                deleteFolderTest.run(
                    "/a/..",
                    null,
                    new IllegalArgumentException("Cannot delete a root folder (/)."));
            });

            runner.testGroup("deleteFolder(Path)", () ->
            {
                final Action1<String> deleteFolderFailureTest = (String folderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() ->
                        {
                            fileSystem.deleteFolder(Path.parse(folderPath));
                        });
                    });
                };

                deleteFolderFailureTest.run(null);
                deleteFolderFailureTest.run("");
                deleteFolderFailureTest.run("folder");

                final Action3<String,Action1<FileSystem>,Throwable> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertError(expectedError, fileSystem.deleteFolder(Path.parse(folderPath)));
                    });
                };

                deleteFolderTest.run("/folder", null, new FolderNotFoundException("/folder"));
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    null);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a");
                        fileSystem.createFolder("/folder/b");
                        fileSystem.createFolder("/folder/c");
                    },
                    null);
                deleteFolderTest.run(
                    "/..",
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                deleteFolderTest.run(
                    "/a/..",
                    null,
                    new IllegalArgumentException("Cannot delete a root folder (/)."));
            });

            runner.testGroup("getFile(String)", () ->
            {
                final Action1<String> getFileFailureTest = (String filePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFile(filePath));
                    });
                };

                getFileFailureTest.run(null);
                getFileFailureTest.run("");
                getFileFailureTest.run("a/b/c");
                getFileFailureTest.run("/");
                getFileFailureTest.run("\\");
                getFileFailureTest.run("Z:\\");

                final Action3<String,String,Throwable> getFileTest = (String filePath, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<File> file = fileSystem.getFile(filePath);
                        test.assertNotNull(file);
                        test.assertEqual(expectedFilePath, file.getValue() == null ? null : file.getValue().toString());
                        test.assertEqual(expectedError, file.getError());
                    });
                };

                getFileTest.run("/a/b", "/a/b", null);
                getFileTest.run("/../test.txt", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFileTest.run("/a/../test.txt", "/test.txt", null);
            });

            runner.testGroup("getFile(Path)", () ->
            {
                final Action1<String> getFileFailureTest = (String filePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFile(Path.parse(filePath)));
                    });
                };

                getFileFailureTest.run(null);
                getFileFailureTest.run("");
                getFileFailureTest.run("a/b/c");
                getFileFailureTest.run("/");
                getFileFailureTest.run("\\");
                getFileFailureTest.run("Z:\\");

                final Action3<String,String,Throwable> getFileTest = (String filePath, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<File> file = fileSystem.getFile(Path.parse(filePath));
                        test.assertNotNull(file);
                        test.assertEqual(expectedFilePath, file.getValue() == null ? null : file.getValue().toString());
                        test.assertEqual(expectedError, file.getError());
                    });
                };

                getFileTest.run("/a/b", "/a/b", null);
                getFileTest.run("/../test.txt", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFileTest.run("/a/../test.txt", "/test.txt", null);
            });

            runner.testGroup("fileExists(String)", () ->
            {
                final Action1<String> fileExistsFailureTest = (String filePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.fileExists(filePath));
                    });
                };

                fileExistsFailureTest.run(null);
                fileExistsFailureTest.run("");
                fileExistsFailureTest.run("blah");
                fileExistsFailureTest.run("/");
                fileExistsFailureTest.run("\\");

                final Action4<String,Action1<FileSystem>,Boolean,Throwable> fileExistsTest = (String filePath, Action1<FileSystem> setup, Boolean expectedFileExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Boolean> result = fileSystem.fileExists(filePath);
                        test.assertNotNull(result);

                        test.assertEqual(expectedFileExists, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                fileExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    false,
                    null);
                fileExistsTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName"),
                    true,
                    null);
                fileExistsTest.run(
                    "/../file.txt",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                fileExistsTest.run(
                    "/a/../file.txt",
                    null,
                    false,
                    null);
            });

            runner.testGroup("fileExists(Path)", () ->
            {
                final Action1<String> fileExistsFailureTest = (String filePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.fileExists(Path.parse(filePath)));
                    });
                };

                fileExistsFailureTest.run(null);
                fileExistsFailureTest.run("");
                fileExistsFailureTest.run("blah");
                fileExistsFailureTest.run("/");
                fileExistsFailureTest.run("\\");

                final Action4<String,Action1<FileSystem>,Boolean,Throwable> fileExistsTest = (String filePath, Action1<FileSystem> setup, Boolean expectedFileExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Boolean> result = fileSystem.fileExists(Path.parse(filePath));
                        test.assertNotNull(result);

                        test.assertEqual(expectedFileExists, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                fileExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    false,
                    null);
                fileExistsTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName"),
                    true,
                    null);
                fileExistsTest.run(
                    "/../file.txt",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                fileExistsTest.run(
                    "/a/../file.txt",
                    null,
                    false,
                    null);
            });

            runner.testGroup("createFile(String)", () ->
            {
                final Action1<String> createFileFailureTest = (String filePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.createFile(filePath));
                    });
                };

                createFileFailureTest.run(null);
                createFileFailureTest.run("");
                createFileFailureTest.run("things.txt");
                createFileFailureTest.run("/\u0000.txt");
                createFileFailureTest.run("/?.txt");
                createFileFailureTest.run("/*.txt");
                createFileFailureTest.run("/<.txt");
                createFileFailureTest.run("/>.txt");
                createFileFailureTest.run("/|.txt");
                createFileFailureTest.run("/:.txt");
                createFileFailureTest.run("/\".txt");

                final Action4<String,Action1<FileSystem>,String,Throwable> createFileTest = (String filePath, Action1<FileSystem> setup, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertDone(expectedFilePath, expectedError, fileSystem.createFile(filePath).then(File::toString));
                    });
                };

                createFileTest.run("/`~!@#$%^&()-_=+[]{};',.txt", null, "/`~!@#$%^&()-_=+[]{};',.txt", null);
                createFileTest.run("/things.txt", null, "/things.txt", null);
                createFileTest.run(
                    "/things.txt",
                    (FileSystem fileSystem) -> fileSystem.createFile("/things.txt"),
                    "/things.txt",
                    new FileAlreadyExistsException("/things.txt"));
                createFileTest.run(
                    "/../file.txt",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                createFileTest.run(
                    "/a/../file.txt",
                    null,
                    "/file.txt",
                    null);
            });

            runner.testGroup("createFile(Path)", () ->
            {
                final Action1<String> createFileFailureTest = (String filePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.createFile(Path.parse(filePath)));
                    });
                };

                createFileFailureTest.run(null);
                createFileFailureTest.run("");
                createFileFailureTest.run("things.txt");
                createFileFailureTest.run("/\u0000.txt");
                createFileFailureTest.run("/?.txt");
                createFileFailureTest.run("/*.txt");
                createFileFailureTest.run("/<.txt");
                createFileFailureTest.run("/>.txt");
                createFileFailureTest.run("/|.txt");
                createFileFailureTest.run("/:.txt");
                createFileFailureTest.run("/\".txt");

                final Action4<String,Action1<FileSystem>,String,Throwable> createFileTest = (String filePath, Action1<FileSystem> setup, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertDone(expectedFilePath, expectedError, fileSystem.createFile(Path.parse(filePath)).then(File::toString));
                    });
                };

                createFileTest.run("/`~!@#$%^&()-_=+[]{};',.txt", null, "/`~!@#$%^&()-_=+[]{};',.txt", null);
                createFileTest.run("/things.txt", null, "/things.txt", null);
                createFileTest.run(
                    "/things.txt",
                    (FileSystem fileSystem) -> fileSystem.createFile("/things.txt"),
                    "/things.txt",
                    new FileAlreadyExistsException("/things.txt"));
                createFileTest.run(
                    "/../file.txt",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                createFileTest.run(
                    "/a/../file.txt",
                    null,
                    "/file.txt",
                    null);
            });

            runner.testGroup("deleteFile(String)", () ->
            {
                final Action1<String> deleteFileFailureTest = (String filePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.deleteFile(filePath));
                    });
                };

                deleteFileFailureTest.run(null);
                deleteFileFailureTest.run("");
                deleteFileFailureTest.run("relativeFile.txt");

                final Action3<String,Action1<FileSystem>,Throwable> deleteFileTest = (String filePath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertError(expectedError, fileSystem.deleteFile(filePath));
                    });
                };

                deleteFileTest.run("/idontexist.txt", null, new FileNotFoundException("/idontexist.txt"));
                deleteFileTest.run(
                    "/iexist.txt",
                    (FileSystem fileSystem) -> fileSystem.createFile("/iexist.txt"),
                    null);
                deleteFileTest.run(
                    "/../file.txt",
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                deleteFileTest.run(
                    "/a/../file.txt",
                    null,
                    new FileNotFoundException("/file.txt"));
            });

            runner.testGroup("getFileLastModified(String)", () ->
            {
                final Action1<String> getFileLastModifiedFailureTest = (String filePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFileLastModified(filePath));
                    });
                };

                getFileLastModifiedFailureTest.run(null);
                getFileLastModifiedFailureTest.run("");
                getFileLastModifiedFailureTest.run("relativeFile.txt");

                final Action4<String,Action1<FileSystem>,DateTime,Throwable> getFileLastModifiedTest = (String filePath, Action1<FileSystem> setup, DateTime expectedLastModified, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertDone(expectedLastModified, expectedError, fileSystem.getFileLastModified(filePath));
                    });
                };

                getFileLastModifiedTest.run("/idontexist.txt", null, null, new FileNotFoundException("/idontexist.txt"));
                getFileLastModifiedTest.run(
                    "/../file.txt",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFileLastModifiedTest.run(
                    "/a/../file.txt",
                    null,
                    null,
                    new FileNotFoundException("/file.txt"));

                runner.test("with existing rooted path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt");

                    final Result<DateTime> result = fileSystem.getFileLastModified("/thing.txt");
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertTrue(result.getValue().greaterThan(DateTime.local(2018, 1, 1, 0, 0, 0, 0)));
                    test.assertFalse(result.hasError());
                });
            });

            runner.testGroup("getFileLastModified(Path)", () ->
            {
                final Action1<String> getFileLastModifiedFailureTest = (String filePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFileLastModified(Path.parse(filePath)));
                    });
                };

                getFileLastModifiedFailureTest.run(null);
                getFileLastModifiedFailureTest.run("");
                getFileLastModifiedFailureTest.run("relativeFile.txt");

                final Action4<String,Action1<FileSystem>,DateTime,Throwable> getFileLastModifiedTest = (String filePath, Action1<FileSystem> setup, DateTime expectedLastModified, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<DateTime> result = fileSystem.getFileLastModified(Path.parse(filePath));
                        test.assertNotNull(result);

                        test.assertEqual(expectedLastModified, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                getFileLastModifiedTest.run("/idontexist.txt", null, null, new FileNotFoundException("/idontexist.txt"));
                getFileLastModifiedTest.run(
                    "/../file.txt",
                    null,
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFileLastModifiedTest.run(
                    "/a/../file.txt",
                    null,
                    null,
                    new FileNotFoundException("/file.txt"));

                runner.test("with existing rooted path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt");

                    final Result<DateTime> result = fileSystem.getFileLastModified(Path.parse("/thing.txt"));
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertTrue(result.getValue().greaterThan(DateTime.local(2018, 1, 1, 0, 0, 0, 0)));
                    test.assertFalse(result.hasError());
                });
            });

            runner.testGroup("getFileContent(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent("thing.txt"));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("/thing.txt");
                    test.assertError(new FileNotFoundException("/thing.txt"), result);
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt");

                    final Result<byte[]> result = fileSystem.getFileContent("/thing.txt");
                    test.assertSuccess(new byte[0], result);
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("/../thing.txt");
                    test.assertError(new IllegalArgumentException("Cannot resolve a rooted path outside of its root."), result);
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("/a/../thing.txt");
                    test.assertError(new FileNotFoundException("/thing.txt"), result);
                });
            });

            runner.testGroup("getFileContent(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent((Path)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent(Path.parse("")));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent(Path.parse("thing.txt")));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse("/thing.txt"));
                    test.assertError(new FileNotFoundException("/thing.txt"), result);
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt");

                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse("/thing.txt"));
                    test.assertSuccess(new byte[0], result);
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("/../thing.txt");
                    test.assertError(new IllegalArgumentException("Cannot resolve a rooted path outside of its root."), result);
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("/a/../thing.txt");
                    test.assertError(new FileNotFoundException("/thing.txt"), result);
                });
            });

            runner.testGroup("getFileContentByteReadStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<ByteReadStream> result = fileSystem.getFileContentByteReadStream("/i/dont/exist.txt");
                    test.assertError(new FileNotFoundException("/i/dont/exist.txt"), result);
                });
            });

            runner.testGroup("getFileContentByteReadStream(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<ByteReadStream> result = fileSystem.getFileContentByteReadStream(Path.parse("/i/dont/exist.txt"));
                    test.assertError(new FileNotFoundException("/i/dont/exist.txt"), result);
                });
            });

            runner.testGroup("getFileContentByteReadStreamAsync(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<ByteReadStream> result = fileSystem.getFileContentByteReadStreamAsync("/i/dont/exist.txt").awaitReturn();
                    test.assertError(new FileNotFoundException("/i/dont/exist.txt"), result);
                });
            });

            runner.testGroup("getFileContentByteReadStreamAsync(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<ByteReadStream> result = fileSystem.getFileContentByteReadStreamAsync(Path.parse("/i/dont/exist.txt")).awaitReturn();
                    test.assertError(new FileNotFoundException("/i/dont/exist.txt"), result);
                });
            });

            runner.testGroup("getFileContentCharacterReadStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<CharacterReadStream> result = fileSystem.getFileContentCharacterReadStream("/i/dont/exist.txt");
                    test.assertError(new FileNotFoundException("/i/dont/exist.txt"), result);
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/exist.txt";
                    fileSystem.createFile(filePath);
                    test.assertSuccess("", fileSystem.getFileContentCharacterReadStream(filePath)
                        .thenResult((CharacterReadStream readStream) ->
                        {
                            try
                            {
                                return readStream.readEntireString();
                            }
                            finally
                            {
                                readStream.dispose();
                            }
                        }));
                });
            });

            runner.testGroup("getFileContentCharacterReadStream(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<CharacterReadStream> result = fileSystem.getFileContentCharacterReadStream(Path.parse("/i/dont/exist.txt"));
                    test.assertError(new FileNotFoundException("/i/dont/exist.txt"), result);
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/exist.txt";
                    fileSystem.createFile(filePath);
                    test.assertSuccess("", fileSystem.getFileContentCharacterReadStream(Path.parse(filePath))
                        .thenResult((CharacterReadStream readStream) ->
                        {
                            try
                            {
                                return readStream.readEntireString();
                            }
                            finally
                            {
                                readStream.dispose();
                            }
                        }));
                });
            });

            runner.testGroup("getFileContentCharacterReadStreamAsync(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<CharacterReadStream> result = fileSystem.getFileContentCharacterReadStreamAsync("/i/dont/exist.txt").awaitReturn();
                    test.assertError(new FileNotFoundException("/i/dont/exist.txt"), result);
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/exist.txt";
                    fileSystem.createFile(filePath);
                    test.assertSuccess("", fileSystem.getFileContentCharacterReadStreamAsync(filePath).awaitReturn()
                        .thenResult((CharacterReadStream readStream) ->
                        {
                            try
                            {
                                return readStream.readEntireString();
                            }
                            finally
                            {
                                readStream.dispose();
                            }
                        }));
                });
            });

            runner.testGroup("getFileContentCharacterReadStreamAsync(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/dont/exist.txt";
                    final Result<CharacterReadStream> result = fileSystem.getFileContentCharacterReadStreamAsync(Path.parse(filePath)).awaitReturn();
                    test.assertError(new FileNotFoundException(filePath), result);
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/exist.txt";
                    fileSystem.createFile(filePath);
                    test.assertSuccess("", fileSystem.getFileContentCharacterReadStreamAsync(Path.parse(filePath)).awaitReturn()
                        .thenResult((CharacterReadStream readStream) ->
                        {
                            try
                            {
                                return readStream.readEntireString();
                            }
                            finally
                            {
                                readStream.dispose();
                            }
                        }));
                });
            });

            runner.testGroup("getFileContentByteWriteStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/dont/exist.txt";
                    test.assertSuccess(fileSystem.getFileContentByteWriteStream(filePath),
                        (ByteWriteStream byteWriteStream) ->
                        {
                            test.assertSuccess(true, byteWriteStream.dispose());
                            test.assertSuccess(true, fileSystem.fileExists(filePath));
                            test.assertSuccess(new byte[0], fileSystem.getFileContent(filePath));
                        });
                });
            });

            runner.testGroup("getFileContentByteWriteStream(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/dont/exist.txt";
                    test.assertSuccess(fileSystem.getFileContentByteWriteStream(Path.parse(filePath)),
                        (ByteWriteStream byteWriteStream) ->
                        {
                            test.assertSuccess(true, byteWriteStream.dispose());
                            test.assertSuccess(true, fileSystem.fileExists(filePath));
                            test.assertSuccess(new byte[0], fileSystem.getFileContent(filePath));
                        });
                });
            });

            runner.testGroup("getFileContentByteWriteStreamAsync(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/dont/exist.txt";
                    test.assertSuccess(fileSystem.getFileContentByteWriteStreamAsync(filePath).awaitReturn(),
                        (ByteWriteStream byteWriteStream) ->
                        {
                            test.assertSuccess(true, byteWriteStream.dispose());
                            test.assertSuccess(true, fileSystem.fileExists(filePath));
                            test.assertSuccess(new byte[0], fileSystem.getFileContent(filePath));
                        });
                });
            });

            runner.testGroup("getFileContentByteWriteStreamAsync(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/dont/exist.txt";
                    test.assertSuccess(fileSystem.getFileContentByteWriteStreamAsync(Path.parse(filePath)).awaitReturn(),
                        (ByteWriteStream byteWriteStream) ->
                        {
                            test.assertSuccess(true, byteWriteStream.dispose());
                            test.assertSuccess(true, fileSystem.fileExists(filePath));
                            test.assertSuccess(new byte[0], fileSystem.getFileContent(filePath));
                        });
                });
            });

            runner.testGroup("getFileContentCharacterWriteStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/dont/exist.txt";
                    test.assertSuccess(fileSystem.getFileContentCharacterWriteStream(filePath),
                        (CharacterWriteStream characterWriteStream) ->
                        {
                            test.assertSuccess(true, characterWriteStream.dispose());
                            test.assertSuccess(true, fileSystem.fileExists(filePath));
                            test.assertSuccess(new byte[0], fileSystem.getFileContent(filePath));
                        });
                });
            });

            runner.testGroup("getFileContentCharacterWriteStream(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/dont/exist.txt";
                    test.assertSuccess(fileSystem.getFileContentCharacterWriteStream(Path.parse(filePath)),
                        (CharacterWriteStream characterWriteStream) ->
                        {
                            test.assertSuccess(true, characterWriteStream.dispose());
                            test.assertSuccess(true, fileSystem.fileExists(filePath));
                            test.assertSuccess(new byte[0], fileSystem.getFileContent(filePath));
                        });
                });
            });

            runner.testGroup("getFileContentCharacterWriteStreamAsync(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/dont/exist.txt";
                    test.assertSuccess(fileSystem.getFileContentCharacterWriteStreamAsync(filePath).awaitReturn(),
                        (CharacterWriteStream characterWriteStream) ->
                        {
                            test.assertSuccess(true, characterWriteStream.dispose());
                            test.assertSuccess(true, fileSystem.fileExists(filePath));
                            test.assertSuccess(new byte[0], fileSystem.getFileContent(filePath));
                        });
                });
            });

            runner.testGroup("getFileContentCharacterWriteStreamAsync(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final String filePath = "/i/dont/exist.txt";
                    test.assertSuccess(fileSystem.getFileContentCharacterWriteStreamAsync(Path.parse(filePath)).awaitReturn(),
                        (CharacterWriteStream characterWriteStream) ->
                        {
                            test.assertSuccess(true, characterWriteStream.dispose());
                            test.assertSuccess(true, fileSystem.fileExists(filePath));
                            test.assertSuccess(new byte[0], fileSystem.getFileContent(filePath));
                        });
                });
            });

            runner.testGroup("setFileContent(String,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent((String)null, new byte[] { 0, 1, 2 }));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent("", new byte[] { 0, 1, 2 }));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent("relative.file", new byte[] { 0, 1, 2 }));
                });

                runner.test("with non-existing rooted path with null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertSuccess(null, fileSystem.setFileContent("/A.txt", null));
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });
                    
                    test.assertSuccess(null, fileSystem.setFileContent("/A.txt", null));

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    test.assertSuccess(null, fileSystem.setFileContent("/A.txt", new byte[0]));
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });

                    test.assertSuccess(null, fileSystem.setFileContent("/A.txt", new byte[0]));
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    test.assertSuccess(null, fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 }));
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    test.assertSuccess(null, fileSystem.setFileContent("/folder/A.txt", new byte[] { 0, 1, 2 }));
                    
                    test.assertTrue(fileSystem.folderExists("/folder").getValue());
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt").getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/folder/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/A.txt");

                    test.assertSuccess(null, fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 }));
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Void> result = fileSystem.setFileContent("/../thing.txt", new byte[] { 0, 1, 2 });
                    test.assertError(new IllegalArgumentException("Cannot resolve a rooted path outside of its root."), result);
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertSuccess(null, fileSystem.setFileContent("/a/../thing.txt", new byte[] { 0, 1, 2 }));
                    test.assertSuccess(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/thing.txt"));
                });
            });

            runner.testGroup("setFileContent(Path,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent((Path)null, new byte[] { 0, 1, 2 }));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent(Path.parse(""), new byte[] { 0, 1, 2 }));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent(Path.parse("relative.file"), new byte[] { 0, 1, 2 }));
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    test.assertSuccess(null, fileSystem.setFileContent(Path.parse("/A.txt"), null));
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });

                    test.assertSuccess(null, fileSystem.setFileContent(Path.parse("/A.txt"), (byte[])null));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    test.assertSuccess(null, fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]));

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertSuccess(null, fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }));

                    test.assertSuccess(null, fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]));

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    test.assertSuccess(null, fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/A.txt").await();

                    test.assertSuccess(null, fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Void> result = fileSystem.setFileContent(Path.parse("/../thing.txt"), new byte[] { 0, 1, 2 });
                    test.assertError(new IllegalArgumentException("Cannot resolve a rooted path outside of its root."), result);
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertSuccess(null, fileSystem.setFileContent(Path.parse("/a/../thing.txt"), new byte[] { 0, 1, 2 }));
                    test.assertSuccess(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/thing.txt"));
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively(""));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively("test/folder"));
                });

                runner.test("with rooted path when root doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("F:/test/folder");
                    test.assertError(new FolderNotFoundException("F:/test/folder"), result);
                });

                runner.test("with rooted path when parent folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertError(new FolderNotFoundException("/test/folder"), result);
                });

                runner.test("with rooted path when folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertError(new FolderNotFoundException("/test/folder"), result);
                });

                runner.test("with rooted path when folder is empty", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertSuccess(Iterable.create(), result);
                });

                runner.test("with rooted path when folder has files", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder");
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertSuccess(
                        Iterable.create(
                            fileSystem.getFile("/test/folder/1.txt").getValue(),
                            fileSystem.getFile("/test/folder/2.txt").getValue()),
                        result);
                });

                runner.test("with rooted path when folder has folders", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder/1.txt");
                    fileSystem.createFolder("/test/folder/2.txt");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertSuccess(
                        Iterable.create(
                            fileSystem.getFolder("/test/folder/1.txt").getValue(),
                            fileSystem.getFolder("/test/folder/2.txt").getValue()),
                        result);
                });

                runner.test("with rooted path when folder has grandchild files and folders", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");
                    fileSystem.createFile("/test/folder/A/3.csv");
                    fileSystem.createFile("/test/folder/B/C/4.xml");
                    fileSystem.createFile("/test/folder/A/5.png");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertSuccess(
                        Iterable.create(
                            fileSystem.getFolder("/test/folder/A").getValue(),
                            fileSystem.getFolder("/test/folder/B").getValue(),
                            fileSystem.getFile("/test/folder/1.txt").getValue(),
                            fileSystem.getFile("/test/folder/2.txt").getValue(),
                            fileSystem.getFile("/test/folder/A/3.csv").getValue(),
                            fileSystem.getFile("/test/folder/A/5.png").getValue(),
                            fileSystem.getFolder("/test/folder/B/C").getValue(),
                            fileSystem.getFile("/test/folder/B/C/4.xml").getValue()),
                        result);
                });
            });

            runner.testGroup("copyFileTo(Path,Path)", () ->
            {
                runner.test("with null rootedFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.copyFileTo((Path)null, Path.parse("/dest.txt")), new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertSuccess(false, fileSystem.fileExists("/dest.txt"));
                });

                runner.test("with null destinationFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), (Path)null), new PreConditionFailure("destinationFilePath cannot be null."));
                    test.assertSuccess(false, fileSystem.fileExists("/source.txt"));
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertError(new FileNotFoundException("/source.txt"), fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")));
                    test.assertSuccess(false, fileSystem.fileExists("/source.txt"));
                    test.assertSuccess(false, fileSystem.fileExists("/dest.txt"));
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/source.txt");
                    test.assertSuccess(null, fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")));
                    test.assertSuccess(true, fileSystem.fileExists("/source.txt"));
                    test.assertSuccess(new byte[0], fileSystem.getFileContent("/source.txt"));
                    test.assertSuccess(true, fileSystem.fileExists("/dest.txt"));
                    test.assertSuccess(new byte[0], fileSystem.getFileContent("/dest.txt"));
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 });
                    test.assertSuccess(null, fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")));
                    test.assertSuccess(true, fileSystem.fileExists("/source.txt"));
                    test.assertSuccess(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt"));
                    test.assertSuccess(true, fileSystem.fileExists("/dest.txt"));
                    test.assertSuccess(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt"));
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/source.txt");
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 });
                    test.assertSuccess(null, fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")));
                    test.assertSuccess(true, fileSystem.fileExists("/source.txt"));
                    test.assertSuccess(new byte[0], fileSystem.getFileContent("/source.txt"));
                    test.assertSuccess(true, fileSystem.fileExists("/dest.txt"));
                    test.assertSuccess(new byte[0], fileSystem.getFileContent("/dest.txt"));
                });

                runner.test("when source file is not empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 });
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 });
                    test.assertNull(fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).awaitError());
                    test.assertTrue(fileSystem.fileExists("/source.txt").awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").awaitError());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").awaitError());
                });
            });

            runner.testGroup("containsInvalidCharacters(Path)", () ->
            {
                final Action2<String,Boolean> containsInvalidCharactersTest = (String pathString, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        test.assertEqual(expected, FileSystem.containsInvalidCharacters(Path.parse(pathString)));
                    });
                };

                containsInvalidCharactersTest.run("abc", false);
                containsInvalidCharactersTest.run(":", true);
                containsInvalidCharactersTest.run("?", true);
                containsInvalidCharactersTest.run("C:", false);
                containsInvalidCharactersTest.run("C:/", false);
                containsInvalidCharactersTest.run("C:\\", false);
                containsInvalidCharactersTest.run("F:", false);
                containsInvalidCharactersTest.run("F:/", false);
                containsInvalidCharactersTest.run("F:\\", false);
                containsInvalidCharactersTest.run("C:/?", true);
                containsInvalidCharactersTest.run("F:/:", true);
            });
        });
    }
}
