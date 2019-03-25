package qub;

public class FileSystemTests
{
    public static void test(TestRunner runner, Function1<AsyncRunner,FileSystem> creator)
    {
        runner.testGroup(FileSystem.class, () ->
        {
            runner.testGroup("rootExists(String)", () ->
            {
                final Action2<String,Throwable> rootExistsFailureTest = (String rootPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.rootExists(rootPath), expectedError);
                    });
                };

                rootExistsFailureTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                rootExistsFailureTest.run("", new PreConditionFailure("rootPath cannot be empty."));

                final Action2<String,Boolean> rootExistsTest = (String rootPath, Boolean expectedValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertEqual(expectedValue, fileSystem.rootExists(rootPath).await());
                    });
                };

                rootExistsTest.run("C@:/", false);
                rootExistsTest.run("notme:\\", false);
                rootExistsTest.run("/", true);
            });

            runner.testGroup("rootExists(Path)", () ->
            {
                final Action2<Path,Throwable> rootExistsFailureTest = (Path rootPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.rootExists(rootPath), expectedError);
                    });
                };

                rootExistsFailureTest.run(null, new PreConditionFailure("rootPath cannot be null."));

                final Action2<String,Boolean> rootExistsTest = (String rootPath, Boolean expectedValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertEqual(expectedValue, fileSystem.rootExists(Path.parse(rootPath)).await());
                    });
                };

                rootExistsTest.run("C@:/", false);
                rootExistsTest.run("notme:\\", false);
                rootExistsTest.run("/", true);
            });

            runner.test("getRoot()", (Test test) ->
            {
                final FileSystem fileSystem = creator.run(test.getParallelAsyncRunner());
                final Root root = fileSystem.getRoot("/daffy/").awaitError();
                test.assertNotNull(root);
                test.assertEqual("/", root.toString());
            });

            runner.test("getRoots()", (Test test) ->
            {
                final FileSystem fileSystem = creator.run(test.getParallelAsyncRunner());
                test.assertNotNullAndNotEmpty(fileSystem.getRoots().awaitError());
            });

            runner.testGroup("getFilesAndFolders(String)", () ->
            {
                final Action2<String,Throwable> getFilesAndFoldersFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFilesAndFolders(folderPath), expectedError);
                    });
                };

                getFilesAndFoldersFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFilesAndFoldersFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));

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
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(Iterable.create(expectedEntryPaths), result.awaitError().map(FileSystemEntry::toString));
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
                final Action2<Path,Throwable> getFilesAndFoldersFailureTest = (Path folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFilesAndFolders(folderPath), expectedError);
                    });
                };

                getFilesAndFoldersFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));

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
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(Iterable.create(expectedEntryPaths), result.awaitError().map(FileSystemEntry::toString));
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
                final Action2<String,Throwable> getFoldersFailureTest = (String path, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFolders(path), expectedError);
                    });
                };

                getFoldersFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFoldersFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));

                runner.test("with " + Strings.escapeAndQuote("/.."), (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFolders("/..").awaitError(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });
            });

            runner.testGroup("getFoldersRecursively(String)", () ->
            {
                final Action2<String,Throwable> getFoldersRecursivelyFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFoldersRecursively(folderPath), expectedError);
                    });
                };

                getFoldersRecursivelyFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFoldersRecursivelyFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                getFoldersRecursivelyFailureTest.run("test/folder", new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

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

                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            final Iterable<Folder> folders = result.awaitError();
                            if (expectedFolderPaths == null)
                            {
                                test.assertNull(folders);
                            }
                            else
                            {
                                test.assertEqual(Iterable.create(expectedFolderPaths), folders.map(FileSystemEntry::toString));
                            }
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
                final Action2<Path,Throwable> getFoldersRecursivelyFailureTest = (Path folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFoldersRecursively(folderPath), expectedError);
                    });
                };

                getFoldersRecursivelyFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFoldersRecursivelyFailureTest.run(Path.parse("test/folder"), new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

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

                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            final Iterable<Folder> folders = result.awaitError();
                            if (expectedFolderPaths == null)
                            {
                                test.assertEqual(null, folders);
                            }
                            else
                            {
                                test.assertEqual(Iterable.create(expectedFolderPaths), folders.map(FileSystemEntry::toString));
                            }
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
                final Action2<String,Throwable> getFilesFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFiles(folderPath), expectedError);
                    });
                };

                getFilesFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFilesFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));

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

                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            final Iterable<File> files = result.awaitError();
                            if (expectedFilePaths == null)
                            {
                                test.assertNull(files);
                            }
                            else
                            {
                                test.assertEqual(Iterable.create(expectedFilePaths), files.map(FileSystemEntry::toString));
                            }
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
                final Action2<Path,Throwable> getFilesFailureTest = (Path folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFiles(folderPath), expectedError);
                    });
                };

                getFilesFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));

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

                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            final Iterable<File> files = result.awaitError();
                            if (expectedFilePaths == null)
                            {
                                test.assertNull(files);
                            }
                            else
                            {
                                test.assertEqual(Iterable.create(expectedFilePaths), files.map(FileSystemEntry::toString));
                            }
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
                final Action2<String,Throwable> getFilesRecursivelyFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFilesRecursively(folderPath), expectedError);
                    });
                };

                getFilesRecursivelyFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFilesRecursivelyFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                getFilesRecursivelyFailureTest.run("test/folder", new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

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

                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            final Iterable<File> files = result.awaitError();
                            if (expectedFilePaths == null)
                            {
                                test.assertNull(files);
                            }
                            else
                            {
                                test.assertEqual(Iterable.create(expectedFilePaths), files.map(FileSystemEntry::toString));
                            }
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
                final Action2<String,Throwable> getFolderFailureTest = (String folderPathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPathString), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFolder(folderPathString), expectedError);
                    });
                };

                getFolderFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFolderFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                getFolderFailureTest.run("a/b/c", new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

                final Action3<String,String,Throwable> getFolderTest = (String folderPathString, String expectedFolderPathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPathString), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<Folder> folder = fileSystem.getFolder(folderPathString);
                        if (expectedError != null)
                        {
                            test.assertThrows(folder::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFolderPathString, folder.awaitError().toString());
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
                final Action2<String,Throwable> folderExistsFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.folderExists(folderPath), expectedError);
                    });
                };

                folderExistsFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                folderExistsFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));

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

                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFolderExists, result.awaitError());
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
                final Action2<Path,Throwable> folderExistsFailureTest = (Path folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.folderExists(folderPath), expectedError);
                    });
                };

                folderExistsFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));

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

                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFolderExists, result.awaitError());
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
                final Action2<String,Throwable> createFolderFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.createFolder(folderPath), expectedError);
                    });
                };

                createFolderFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                createFolderFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                createFolderFailureTest.run("folder", new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

                final Action5<String,String,Action1<FileSystem>,String,Throwable> createFolderTest = (String testName, String folderPath, Action1<FileSystem> setup, String expectedCreatedFolderPath, Throwable expectedError) ->
                {
                    runner.test(testName + " (" + folderPath + ")", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Folder> createFolderResult = fileSystem.createFolder(folderPath);
                        if (expectedError != null)
                        {
                            test.assertThrows(createFolderResult::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedCreatedFolderPath, createFolderResult.await().toString());
                        }
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
                final Action2<Path,Throwable> createFolderFailureTest = (Path folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.createFolder(folderPath), expectedError);
                    });
                };

                createFolderFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                createFolderFailureTest.run(Path.parse("folder"), new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

                final Action5<String,String,Action1<FileSystem>,String,Throwable> createFolderTest = (String testName, String folderPath, Action1<FileSystem> setup, String expectedCreatedFolderPath, Throwable expectedError) ->
                {
                    runner.test(testName + " (" + folderPath + ")", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Folder> createFolderResult = fileSystem.createFolder(Path.parse(folderPath));
                        if (expectedError != null)
                        {
                            test.assertThrows(createFolderResult::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedCreatedFolderPath, createFolderResult.await().toString());
                        }
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
                final Action2<String,Throwable> deleteFolderFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.deleteFolder(folderPath), expectedError);
                    });
                };

                deleteFolderFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                deleteFolderFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                deleteFolderFailureTest.run("folder", new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

                final Action3<String,Action1<FileSystem>,Throwable> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        if (expectedError == null)
                        {
                            fileSystem.deleteFolder(folderPath).awaitError();
                        }
                        else
                        {
                            test.assertThrows(() -> fileSystem.deleteFolder(folderPath).awaitError(), expectedError);
                        }
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
                final Action2<Path,Throwable> deleteFolderFailureTest = (Path folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.deleteFolder(folderPath), expectedError);
                    });
                };

                deleteFolderFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                deleteFolderFailureTest.run(Path.parse("folder"), new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

                final Action3<String,Action1<FileSystem>,Throwable> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        if (expectedError == null)
                        {
                            fileSystem.deleteFolder(Path.parse(folderPath)).awaitError();
                        }
                        else
                        {
                            test.assertThrows(() -> fileSystem.deleteFolder(Path.parse(folderPath)).awaitError(), expectedError);
                        }
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
                final Action2<String,Throwable> getFileFailureTest = (String filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFile(filePath), expectedError);
                    });
                };

                getFileFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                getFileFailureTest.run("", new PreConditionFailure("rootedFilePath cannot be empty."));
                getFileFailureTest.run("a/b/c", new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                getFileFailureTest.run("/", new PreConditionFailure("rootedFilePath.endsWith(\"/\") cannot be true."));
                getFileFailureTest.run("\\", new PreConditionFailure("rootedFilePath.endsWith(\"\\\") cannot be true."));
                getFileFailureTest.run("Z:\\", new PreConditionFailure("rootedFilePath.endsWith(\"\\\") cannot be true."));

                final Action3<String,String,Throwable> getFileTest = (String filePath, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<File> result = fileSystem.getFile(filePath);
                        test.assertNotNull(result);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            final File file = result.awaitError();
                            test.assertEqual(expectedFilePath, file == null ? null : file.toString());
                        }
                    });
                };

                getFileTest.run("/a/b", "/a/b", null);
                getFileTest.run("/../test.txt", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFileTest.run("/a/../test.txt", "/test.txt", null);
            });

            runner.testGroup("getFile(Path)", () ->
            {
                final Action2<Path,Throwable> getFileFailureTest = (Path filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFile(filePath), expectedError);
                    });
                };

                getFileFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                getFileFailureTest.run(Path.parse("a/b/c"), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                getFileFailureTest.run(Path.parse("/"), new PreConditionFailure("rootedFilePath.endsWith(\"/\") cannot be true."));
                getFileFailureTest.run(Path.parse("\\"), new PreConditionFailure("rootedFilePath.endsWith(\"\\\") cannot be true."));
                getFileFailureTest.run(Path.parse("Z:\\"), new PreConditionFailure("rootedFilePath.endsWith(\"\\\") cannot be true."));

                final Action3<String,String,Throwable> getFileTest = (String filePath, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<File> result = fileSystem.getFile(Path.parse(filePath));
                        test.assertNotNull(result);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            final File file = result.awaitError();
                            test.assertEqual(expectedFilePath, file == null ? null : file.toString());
                        }
                    });
                };

                getFileTest.run("/a/b", "/a/b", null);
                getFileTest.run("/../test.txt", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFileTest.run("/a/../test.txt", "/test.txt", null);
            });

            runner.testGroup("fileExists(String)", () ->
            {
                final Action2<String,Throwable> fileExistsFailureTest = (String filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.fileExists(filePath), expectedError);
                    });
                };

                fileExistsFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                fileExistsFailureTest.run("", new PreConditionFailure("rootedFilePath cannot be empty."));
                fileExistsFailureTest.run("blah", new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                fileExistsFailureTest.run("/", new PreConditionFailure("rootedFilePath.endsWith(\"/\") cannot be true."));
                fileExistsFailureTest.run("\\", new PreConditionFailure("rootedFilePath.endsWith(\"\\\") cannot be true."));

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
                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFileExists, result.awaitError());
                        }
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
                final Action2<Path,Throwable> fileExistsFailureTest = (Path filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.fileExists(filePath), expectedError);
                    });
                };

                fileExistsFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                fileExistsFailureTest.run(Path.parse("blah"), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                fileExistsFailureTest.run(Path.parse("/"), new PreConditionFailure("rootedFilePath.endsWith(\"/\") cannot be true."));
                fileExistsFailureTest.run(Path.parse("\\"), new PreConditionFailure("rootedFilePath.endsWith(\"\\\") cannot be true."));

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
                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFileExists, result.awaitError());
                        }
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
                final Action2<String,Throwable> createFileFailureTest = (String filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.createFile(filePath), expectedError);
                    });
                };

                createFileFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                createFileFailureTest.run("", new PreConditionFailure("rootedFilePath cannot be empty."));
                createFileFailureTest.run("things.txt", new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                createFileFailureTest.run("/\u0000.txt", new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/\u0000.txt\")) cannot be true."));
                createFileFailureTest.run("/?.txt", new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/?.txt\")) cannot be true."));
                createFileFailureTest.run("/*.txt", new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/*.txt\")) cannot be true."));
                createFileFailureTest.run("/<.txt", new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/<.txt\")) cannot be true."));
                createFileFailureTest.run("/>.txt", new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/>.txt\")) cannot be true."));
                createFileFailureTest.run("/|.txt", new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/|.txt\")) cannot be true."));
                createFileFailureTest.run("/:.txt", new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/:.txt\")) cannot be true."));
                createFileFailureTest.run("/\".txt", new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/\\\".txt\")) cannot be true."));

                final Action4<String,Action1<FileSystem>,String,Throwable> createFileTest = (String filePath, Action1<FileSystem> setup, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<File> createFileResult = fileSystem.createFile(filePath);
                        if (expectedError != null)
                        {
                            test.assertThrows(createFileResult::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFilePath, createFileResult.await().toString());
                        }
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
                final Action2<Path,Throwable> createFileFailureTest = (Path filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.createFile(filePath), expectedError);
                    });
                };

                createFileFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                createFileFailureTest.run(Path.parse("things.txt"), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                createFileFailureTest.run(Path.parse("/\u0000.txt"), new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/\u0000.txt\")) cannot be true."));
                createFileFailureTest.run(Path.parse("/?.txt"), new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/?.txt\")) cannot be true."));
                createFileFailureTest.run(Path.parse("/*.txt"), new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/*.txt\")) cannot be true."));
                createFileFailureTest.run(Path.parse("/<.txt"), new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/<.txt\")) cannot be true."));
                createFileFailureTest.run(Path.parse("/>.txt"), new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/>.txt\")) cannot be true."));
                createFileFailureTest.run(Path.parse("/|.txt"), new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/|.txt\")) cannot be true."));
                createFileFailureTest.run(Path.parse("/:.txt"), new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/:.txt\")) cannot be true."));
                createFileFailureTest.run(Path.parse("/\".txt"), new PreConditionFailure("containsInvalidCharacters(rootedFilePath(\"/\\\".txt\")) cannot be true."));

                final Action4<String,Action1<FileSystem>,String,Throwable> createFileTest = (String filePath, Action1<FileSystem> setup, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<File> createFileResult = fileSystem.createFile(Path.parse(filePath));
                        if (expectedError != null)
                        {
                            test.assertThrows(createFileResult::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFilePath, createFileResult.await().toString());
                        }
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
                final Action2<String,Throwable> deleteFileFailureTest = (String filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.deleteFile(filePath), expectedError);
                    });
                };

                deleteFileFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                deleteFileFailureTest.run("", new PreConditionFailure("rootedFilePath cannot be empty."));
                deleteFileFailureTest.run("relativeFile.txt", new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));

                final Action3<String,Action1<FileSystem>,Throwable> deleteFileTest = (String filePath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        if (expectedError == null)
                        {
                            fileSystem.deleteFile(filePath).awaitError();
                        }
                        else
                        {
                            test.assertThrows(() -> fileSystem.deleteFile(filePath).awaitError(), expectedError);
                        }
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
                final Action2<String,Throwable> getFileLastModifiedFailureTest = (String filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFileLastModified(filePath), expectedError);
                    });
                };

                getFileLastModifiedFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                getFileLastModifiedFailureTest.run("", new PreConditionFailure("rootedFilePath cannot be empty."));
                getFileLastModifiedFailureTest.run("relativeFile.txt", new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));

                final Action4<String,Action1<FileSystem>,DateTime,Throwable> getFileLastModifiedTest = (String filePath, Action1<FileSystem> setup, DateTime expectedLastModified, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<DateTime> lastModifiedResult = fileSystem.getFileLastModified(filePath);
                        if (expectedError != null)
                        {
                            test.assertThrows(lastModifiedResult::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedLastModified, lastModifiedResult.await());
                        }
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
                    test.assertGreaterThan(result.awaitError(), DateTime.local(2018, 1, 1, 0, 0, 0, 0));
                });
            });

            runner.testGroup("getFileLastModified(Path)", () ->
            {
                final Action2<Path,Throwable> getFileLastModifiedFailureTest = (Path filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertThrows(() -> fileSystem.getFileLastModified(filePath), expectedError);
                    });
                };

                getFileLastModifiedFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                getFileLastModifiedFailureTest.run(Path.parse("relativeFile.txt"), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));

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
                        if (expectedError != null)
                        {
                            test.assertThrows(result::awaitError, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedLastModified, result.awaitError());
                        }
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
                    test.assertGreaterThan(result.awaitError(), DateTime.local(2018, 1, 1, 0, 0, 0, 0));
                });
            });

            runner.testGroup("getFileContent(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent((String)null), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent(""), new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent("thing.txt"), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent("/thing.txt").awaitError(), new FileNotFoundException("/thing.txt"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt").await();

                    test.assertEqual(new byte[0], fileSystem.getFileContent("/thing.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent("/../thing.txt").awaitError(), new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent("/a/../thing.txt").awaitError(), new FileNotFoundException("/thing.txt"));
                });
            });

            runner.testGroup("getFileContent(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent((Path)null), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFileContent(Path.parse("thing.txt")), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
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
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentByteWriteStreamAsync(filePath).awaitReturn().await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
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
                    test.assertThrows(() -> fileSystem.setFileContent((String)null, new byte[] { 0, 1, 2 }), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent("", new byte[] { 0, 1, 2 }), new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent("relative.file", new byte[] { 0, 1, 2 }), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path with null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", null).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }).await();
                    
                    fileSystem.setFileContent("/A.txt", null).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    fileSystem.setFileContent("/A.txt", new byte[0]).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }).await();

                    fileSystem.setFileContent("/A.txt", new byte[0]).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 }).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    fileSystem.setFileContent("/folder/A.txt", new byte[] { 0, 1, 2 }).await();
                    
                    test.assertTrue(fileSystem.folderExists("/folder").await());
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/folder/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/A.txt");

                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 }).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent("/../thing.txt", new byte[] { 0, 1, 2 }).awaitError(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/a/../thing.txt", new byte[] { 0, 1, 2 }).await();
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/thing.txt").await());
                });
            });

            runner.testGroup("setFileContent(Path,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent((Path)null, new byte[] { 0, 1, 2 }), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent(Path.parse("relative.file"), new byte[] { 0, 1, 2 }), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    fileSystem.setFileContent(Path.parse("/A.txt"), null).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });

                    fileSystem.setFileContent(Path.parse("/A.txt"), (byte[])null).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }).await();

                    fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/A.txt").await();

                    fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.setFileContent(Path.parse("/../thing.txt"), new byte[] { 0, 1, 2 }).awaitError(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent(Path.parse("/a/../thing.txt"), new byte[] { 0, 1, 2 }).await();
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/thing.txt").await());
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively((String)null), new PreConditionFailure("rootedFolderPath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively(""), new PreConditionFailure("rootedFolderPath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively("test/folder"), new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                });

                runner.test("with rooted path when root doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively("F:/test/folder").awaitError(),
                        new FolderNotFoundException("F:/test/folder"));
                });

                runner.test("with rooted path when parent folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively("/test/folder").awaitError(),
                        new FolderNotFoundException("/test/folder"));
                });

                runner.test("with rooted path when folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/").await();

                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively("/test/folder").awaitError(),
                        new FolderNotFoundException("/test/folder"));
                });

                runner.test("with rooted path when folder is empty", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder").await();

                    test.assertEqual(Iterable.create(), fileSystem.getFilesAndFoldersRecursively("/test/folder").await());
                });

                runner.test("with rooted path when folder has files", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder").await();
                    fileSystem.createFile("/test/folder/1.txt").await();
                    fileSystem.createFile("/test/folder/2.txt").await();

                    test.assertEqual(
                        Iterable.create(
                            fileSystem.getFile("/test/folder/1.txt").await(),
                            fileSystem.getFile("/test/folder/2.txt").await()),
                        fileSystem.getFilesAndFoldersRecursively("/test/folder").await());
                });

                runner.test("with rooted path when folder has folders", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder/1.txt").await();
                    fileSystem.createFolder("/test/folder/2.txt").await();

                    test.assertEqual(
                        Iterable.create(
                            fileSystem.getFolder("/test/folder/1.txt").await(),
                            fileSystem.getFolder("/test/folder/2.txt").await()),
                        fileSystem.getFilesAndFoldersRecursively("/test/folder").await());
                });

                runner.test("with rooted path when folder has grandchild files and folders", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/test/folder/1.txt").await();
                    fileSystem.createFile("/test/folder/2.txt").await();
                    fileSystem.createFile("/test/folder/A/3.csv").await();
                    fileSystem.createFile("/test/folder/B/C/4.xml").await();
                    fileSystem.createFile("/test/folder/A/5.png").await();

                    test.assertEqual(
                        Iterable.create(
                            fileSystem.getFolder("/test/folder/A").await(),
                            fileSystem.getFolder("/test/folder/B").await(),
                            fileSystem.getFile("/test/folder/1.txt").await(),
                            fileSystem.getFile("/test/folder/2.txt").await(),
                            fileSystem.getFile("/test/folder/A/3.csv").await(),
                            fileSystem.getFile("/test/folder/A/5.png").await(),
                            fileSystem.getFolder("/test/folder/B/C").await(),
                            fileSystem.getFile("/test/folder/B/C/4.xml").await()),
                        fileSystem.getFilesAndFoldersRecursively("/test/folder").await());
                });
            });

            runner.testGroup("copyFileTo(Path,Path)", () ->
            {
                runner.test("with null rootedFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.copyFileTo((Path)null, Path.parse("/dest.txt")), new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), (Path)null), new PreConditionFailure("destinationFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).awaitError(),
                        new FileNotFoundException("/source.txt"));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 }).await();
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
                    fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
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
