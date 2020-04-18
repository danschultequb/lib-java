package qub;

public interface FileSystemTests
{
    static void test(TestRunner runner, Function1<Test,FileSystem> creator)
    {
        runner.testGroup(FileSystem.class, () ->
        {
            runner.testGroup("rootExists(String)", () ->
            {
                final Action2<String,Throwable> rootExistsFailureTest = (String rootPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.rootExists(rootPath), expectedError);
                    });
                };

                rootExistsFailureTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                rootExistsFailureTest.run("", new PreConditionFailure("rootPath cannot be empty."));

                final Action2<String,Boolean> rootExistsTest = (String rootPath, Boolean expectedValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.rootExists(rootPath), expectedError);
                    });
                };

                rootExistsFailureTest.run(null, new PreConditionFailure("rootPath cannot be null."));

                final Action2<String,Boolean> rootExistsTest = (String rootPath, Boolean expectedValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertEqual(expectedValue, fileSystem.rootExists(Path.parse(rootPath)).await());
                    });
                };

                rootExistsTest.run("C@:/", false);
                rootExistsTest.run("notme:\\", false);
                rootExistsTest.run("/", true);
            });

            runner.test("getRoot()", (Test test) ->
            {
                final FileSystem fileSystem = creator.run(test);
                final Root root = fileSystem.getRoot("/daffy/").await();
                test.assertNotNull(root);
                test.assertEqual("/", root.toString());
            });

            runner.test("getRoots()", (Test test) ->
            {
                final FileSystem fileSystem = creator.run(test);
                test.assertNotNullAndNotEmpty(fileSystem.getRoots().await());
            });

            runner.testGroup("getRootTotalDataSize(String)", () ->
            {
                final Action2<String,Throwable> getRootTotalDataSizeErrorTest = (String rootPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getRootTotalDataSize(rootPath).await(), expected);
                    });
                };

                getRootTotalDataSizeErrorTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                getRootTotalDataSizeErrorTest.run("", new PreConditionFailure("rootPath cannot be empty."));
                getRootTotalDataSizeErrorTest.run("hello", new RootNotFoundException("hello"));
                getRootTotalDataSizeErrorTest.run("/hello/there", new RootNotFoundException("/hello/there"));
                getRootTotalDataSizeErrorTest.run("p:/", new RootNotFoundException("p:/"));
            });

            runner.testGroup("getRootTotalDataSize(Path)", () ->
            {
                final Action2<Path,Throwable> getRootTotalDataSizeErrorTest = (Path rootPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getRootTotalDataSize(rootPath).await(), expected);
                    });
                };

                getRootTotalDataSizeErrorTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                getRootTotalDataSizeErrorTest.run(Path.parse("hello"), new RootNotFoundException("hello"));
                getRootTotalDataSizeErrorTest.run(Path.parse("/hello/there"), new RootNotFoundException("/hello/there"));
                getRootTotalDataSizeErrorTest.run(Path.parse("p:/"), new RootNotFoundException("p:/"));
            });

            runner.testGroup("getRootUnusedDataSize(String)", () ->
            {
                final Action2<String,Throwable> getRootUnusedDataSizeErrorTest = (String rootPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getRootUnusedDataSize(rootPath).await(), expected);
                    });
                };

                getRootUnusedDataSizeErrorTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                getRootUnusedDataSizeErrorTest.run("", new PreConditionFailure("rootPath cannot be empty."));
                getRootUnusedDataSizeErrorTest.run("hello", new RootNotFoundException("hello"));
                getRootUnusedDataSizeErrorTest.run("/hello/there", new RootNotFoundException("/hello/there"));
                getRootUnusedDataSizeErrorTest.run("p:/", new RootNotFoundException("p:/"));
            });

            runner.testGroup("getRootUnusedDataSize(Path)", () ->
            {
                final Action2<Path,Throwable> getRootUnusedDataSizeErrorTest = (Path rootPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getRootUnusedDataSize(rootPath).await(), expected);
                    });
                };

                getRootUnusedDataSizeErrorTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                getRootUnusedDataSizeErrorTest.run(Path.parse("hello"), new RootNotFoundException("hello"));
                getRootUnusedDataSizeErrorTest.run(Path.parse("/hello/there"), new RootNotFoundException("/hello/there"));
                getRootUnusedDataSizeErrorTest.run(Path.parse("p:/"), new RootNotFoundException("p:/"));
            });

            runner.testGroup("getRootUsedDataSize(String)", () ->
            {
                final Action2<String,Throwable> getRootUsedDataSizeErrorTest = (String rootPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getRootUsedDataSize(rootPath).await(), expected);
                    });
                };

                getRootUsedDataSizeErrorTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                getRootUsedDataSizeErrorTest.run("", new PreConditionFailure("rootPath cannot be empty."));
                getRootUsedDataSizeErrorTest.run("hello", new RootNotFoundException("hello"));
                getRootUsedDataSizeErrorTest.run("/hello/there", new RootNotFoundException("/hello/there"));
                getRootUsedDataSizeErrorTest.run("p:/", new RootNotFoundException("p:/"));
            });

            runner.testGroup("getRootUsedDataSize(Path)", () ->
            {
                final Action2<Path,Throwable> getRootUsedDataSizeErrorTest = (Path rootPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getRootUsedDataSize(rootPath).await(), expected);
                    });
                };

                getRootUsedDataSizeErrorTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                getRootUsedDataSizeErrorTest.run(Path.parse("hello"), new RootNotFoundException("hello"));
                getRootUsedDataSizeErrorTest.run(Path.parse("/hello/there"), new RootNotFoundException("/hello/there"));
                getRootUsedDataSizeErrorTest.run(Path.parse("p:/"), new RootNotFoundException("p:/"));
            });

            runner.testGroup("getFilesAndFolders(String)", () ->
            {
                final Action2<String,Throwable> getFilesAndFoldersFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getFilesAndFolders(folderPath), expectedError);
                    });
                };

                getFilesAndFoldersFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFilesAndFoldersFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));

                final Action4<String, Action1<FileSystem>, String[], Throwable> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFolders(folderPath);
                        test.assertNotNull(result);

                        if (expectedEntryPaths == null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(Iterable.create(expectedEntryPaths), result.await().map(FileSystemEntry::toString));
                        }
                    });
                };

                getFilesAndFoldersTest.run("/", null, new String[0], null);
                getFilesAndFoldersTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    new String[0],
                    null);
                getFilesAndFoldersTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
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
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getFilesAndFolders(folderPath), expectedError);
                    });
                };

                getFilesAndFoldersFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));

                final Action4<String, Action1<FileSystem>, String[], Throwable> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFolders(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedEntryPaths == null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(Iterable.create(expectedEntryPaths), result.await().map(FileSystemEntry::toString));
                        }
                    });
                };

                getFilesAndFoldersTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    new String[0],
                    null);
                getFilesAndFoldersTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
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
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getFolders(path), expectedError);
                    });
                };

                getFoldersFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFoldersFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));

                runner.test("with " + Strings.escapeAndQuote("/.."), (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFolders("/..").await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });
            });

            runner.testGroup("getFoldersRecursively(String)", () ->
            {
                final Action2<String,Throwable> getFoldersRecursivelyFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<Folder>> result = fileSystem.getFoldersRecursively(folderPath);
                        test.assertNotNull(result);

                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            final Iterable<Folder> folders = result.await();
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

                getFoldersRecursivelyTest.run("F:/test/folder", null, null, new RootNotFoundException("F:"));
                getFoldersRecursivelyTest.run("/test/folder", null, null, new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/").await();
                    },
                    null,
                    new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder").await();
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder/1.txt").await();
                        fileSystem.createFolder("/test/folder/2.txt").await();
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" },
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                        fileSystem.createFile("/test/folder/A/3.csv").await();
                        fileSystem.createFile("/test/folder/B/C/4.xml").await();
                        fileSystem.createFile("/test/folder/A/5.png").await();
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
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getFoldersRecursively(folderPath), expectedError);
                    });
                };

                getFoldersRecursivelyFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFoldersRecursivelyFailureTest.run(Path.parse("test/folder"), new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

                final Action4<String,Action1<FileSystem>,String[],Throwable> getFoldersRecursivelyTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFolderPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<Folder>> result = fileSystem.getFoldersRecursively(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            final Iterable<Folder> folders = result.await();
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

                getFoldersRecursivelyTest.run("F:/test/folder", null, null, new RootNotFoundException("F:"));
                getFoldersRecursivelyTest.run("/test/folder", null, null, new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/").await();
                    },
                    null,
                    new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder").await();
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder/1.txt").await();
                        fileSystem.createFolder("/test/folder/2.txt").await();
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" },
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                        fileSystem.createFile("/test/folder/A/3.csv").await();
                        fileSystem.createFile("/test/folder/B/C/4.xml").await();
                        fileSystem.createFile("/test/folder/A/5.png").await();
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
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getFiles(folderPath), expectedError);
                    });
                };

                getFilesFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                getFilesFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));

                final Action4<String,Action1<FileSystem>,String[],Throwable> getFilesTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<File>> result = fileSystem.getFiles(folderPath);
                        test.assertNotNull(result);

                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            final Iterable<File> files = result.await();
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
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getFiles(folderPath), expectedError);
                    });
                };

                getFilesFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));

                final Action4<String,Action1<FileSystem>,String[],Throwable> getFilesTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<File>> result = fileSystem.getFiles(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            final Iterable<File> files = result.await();
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<File>> result = fileSystem.getFilesRecursively(folderPath);
                        test.assertNotNull(result);

                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            final Iterable<File> files = result.await();
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

                getFilesRecursivelyTest.run("F:/test/folder", null, null, new RootNotFoundException("F:"));
                getFilesRecursivelyTest.run("/test/folder", null, null, new FolderNotFoundException("/test/folder"));
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/test").await(),
                    null,
                    new FolderNotFoundException("/test/folder"));
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/test/folder").await(),
                    new String[0],
                    null);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder").await();
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" },
                    null);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder").await();
                        fileSystem.createFolder("/test/folder/1.txt").await();
                        fileSystem.createFolder("/test/folder/2.txt").await();
                    },
                    new String[0],
                    null);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                        fileSystem.createFile("/test/folder/A/3.csv").await();
                        fileSystem.createFile("/test/folder/B/C/4.xml").await();
                        fileSystem.createFile("/test/folder/A/5.png").await();
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        final Result<Folder> folder = fileSystem.getFolder(folderPathString);
                        if (expectedError != null)
                        {
                            test.assertThrows(folder::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFolderPathString, folder.await().toString());
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
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.folderExists(folderPath), expectedError);
                    });
                };

                folderExistsFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                folderExistsFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));

                final Action4<String,Action1<FileSystem>,Boolean,Throwable> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Boolean> result = fileSystem.folderExists(folderPath);
                        test.assertNotNull(result);

                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFolderExists, result.await());
                        }
                    });
                };

                folderExistsTest.run("/", null, true, null);
                folderExistsTest.run("/folderName", null, false, null);
                folderExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName").await(),
                    true,
                    null);
            });

            runner.testGroup("folderExists(Path)", () ->
            {
                final Action2<Path,Throwable> folderExistsFailureTest = (Path folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.folderExists(folderPath), expectedError);
                    });
                };

                folderExistsFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));

                final Action4<String,Action1<FileSystem>,Boolean,Throwable> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Boolean> result = fileSystem.folderExists(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFolderExists, result.await());
                        }
                    });
                };

                folderExistsTest.run("/", null, true, null);
                folderExistsTest.run("/folderName", null, false, null);
                folderExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName").await(),
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Folder> createFolderResult = fileSystem.createFolder(folderPath);
                        if (expectedError != null)
                        {
                            test.assertThrows(createFolderResult::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedCreatedFolderPath, createFolderResult.await().toString());
                        }
                    });
                };

                createFolderTest.run("with root", "/", null, null, new FolderAlreadyExistsException("/"));
                createFolderTest.run("with root that doesn't exist", "C:/", null, null, new RootNotFoundException("C:"));
                createFolderTest.run(
                    "with rooted path with root that doesn't exist",
                    "C:/folder/",
                    null,
                    null,
                    new RootNotFoundException("C:"));
                createFolderTest.run("with rooted path that doesn't exist", "/folder", null, "/folder", null);
                createFolderTest.run(
                    "with rooted path that already exists",
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder").await(),
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
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.createFolder(folderPath), expectedError);
                    });
                };

                createFolderFailureTest.run(null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                createFolderFailureTest.run(Path.parse("folder"),
                    new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

                final Action5<String,String,Action1<FileSystem>,String,Throwable> createFolderTest = (String testName, String folderPath, Action1<FileSystem> setup, String expectedCreatedFolderPath, Throwable expectedError) ->
                {
                    runner.test(testName + " (" + folderPath + ")", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Folder> createFolderResult = fileSystem.createFolder(Path.parse(folderPath));
                        if (expectedError != null)
                        {
                            test.assertThrows(createFolderResult::await, expectedError);
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
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder").await(),
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        if (expectedError == null)
                        {
                            fileSystem.deleteFolder(folderPath).await();
                        }
                        else
                        {
                            test.assertThrows(() -> fileSystem.deleteFolder(folderPath).await(), expectedError);
                        }
                    });
                };

                deleteFolderTest.run("/folder", null, new FolderNotFoundException("/folder"));
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder").await(),
                    null);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a").await();
                        fileSystem.createFolder("/folder/b").await();
                        fileSystem.createFolder("/folder/c").await();
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
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.deleteFolder(folderPath), expectedError);
                    });
                };

                deleteFolderFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                deleteFolderFailureTest.run(Path.parse("folder"), new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

                final Action3<String,Action1<FileSystem>,Throwable> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        if (expectedError == null)
                        {
                            fileSystem.deleteFolder(Path.parse(folderPath)).await();
                        }
                        else
                        {
                            test.assertThrows(() -> fileSystem.deleteFolder(Path.parse(folderPath)).await(), expectedError);
                        }
                    });
                };

                deleteFolderTest.run("/folder", null, new FolderNotFoundException("/folder"));
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder").await(),
                    null);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a").await();
                        fileSystem.createFolder("/folder/b").await();
                        fileSystem.createFolder("/folder/c").await();
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        final Result<File> result = fileSystem.getFile(filePath);
                        test.assertNotNull(result);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            final File file = result.await();
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        final Result<File> result = fileSystem.getFile(Path.parse(filePath));
                        test.assertNotNull(result);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            final File file = result.await();
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Boolean> result = fileSystem.fileExists(filePath);
                        test.assertNotNull(result);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFileExists, result.await());
                        }
                    });
                };

                fileExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName").await(),
                    false,
                    null);
                fileExistsTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName").await(),
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Boolean> result = fileSystem.fileExists(Path.parse(filePath));
                        test.assertNotNull(result);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedFileExists, result.await());
                        }
                    });
                };

                fileExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName").await(),
                    false,
                    null);
                fileExistsTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName").await(),
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<File> createFileResult = fileSystem.createFile(filePath);
                        if (expectedError != null)
                        {
                            test.assertThrows(createFileResult::await, expectedError);
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
                    (FileSystem fileSystem) -> fileSystem.createFile("/things.txt").await(),
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<File> createFileResult = fileSystem.createFile(Path.parse(filePath));
                        if (expectedError != null)
                        {
                            test.assertThrows(createFileResult::await, expectedError);
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
                    (FileSystem fileSystem) -> fileSystem.createFile("/things.txt").await(),
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        if (expectedError == null)
                        {
                            fileSystem.deleteFile(filePath).await();
                        }
                        else
                        {
                            test.assertThrows(() -> fileSystem.deleteFile(filePath).await(), expectedError);
                        }
                    });
                };

                deleteFileTest.run("/idontexist.txt", null, new FileNotFoundException("/idontexist.txt"));
                deleteFileTest.run(
                    "/iexist.txt",
                    (FileSystem fileSystem) -> fileSystem.createFile("/iexist.txt").await(),
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
                        final FileSystem fileSystem = creator.run(test);
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
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<DateTime> lastModifiedResult = fileSystem.getFileLastModified(filePath);
                        if (expectedError != null)
                        {
                            test.assertThrows(lastModifiedResult::await, expectedError);
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
                    final FileSystem fileSystem = creator.run(test);
                    final DateTime beforeFileCreated = test.getClock().getCurrentDateTime().toUTC();
                    fileSystem.createFile("/thing.txt").await();
                    final DateTime afterFileCreated = test.getClock().getCurrentDateTime().toUTC();

                    final DateTime result = fileSystem.getFileLastModified("/thing.txt").await().toUTC();
                    test.assertNotNull(result);
                    test.assertGreaterThanOrEqualTo(result, beforeFileCreated);
                    test.assertEqual(afterFileCreated.getDurationSinceEpoch(), result.getDurationSinceEpoch(), Duration.milliseconds(5));
                    test.assertEqual(fileSystem.getFileLastModified("/thing.txt").await().toUTC(), result);
                });
            });

            runner.testGroup("getFileLastModified(Path)", () ->
            {
                final Action2<Path,Throwable> getFileLastModifiedFailureTest = (Path filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        test.assertThrows(() -> fileSystem.getFileLastModified(filePath), expectedError);
                    });
                };

                getFileLastModifiedFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                getFileLastModifiedFailureTest.run(Path.parse("relativeFile.txt"), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));

                final Action4<String,Action1<FileSystem>,DateTime,Throwable> getFileLastModifiedTest = (String filePath, Action1<FileSystem> setup, DateTime expectedLastModified, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<DateTime> result = fileSystem.getFileLastModified(Path.parse(filePath));
                        test.assertNotNull(result);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedLastModified, result.await());
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
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/thing.txt").await();

                    final DateTime result = fileSystem.getFileLastModified(Path.parse("/thing.txt")).await();
                    test.assertNotNull(result);
                    test.assertGreaterThan(result, DateTime.create(2018, 1, 1, 0, 0, 0, 0));
                });
            });

            runner.testGroup("getFileContentDataSize(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize((String)null),
                        new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize(""),
                        new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize("file.txt"),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize("/file.txt").await(),
                        new FileNotFoundException("/file.txt"));
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/file.txt").await();

                    final DataSize fileContentDataSize = fileSystem.getFileContentDataSize("/file.txt").await();
                    test.assertNotNull(fileContentDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, fileContentDataSize.getUnits());
                    test.assertEqual(0, fileContentDataSize.getValue());
                });

                runner.test("with existing non-empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/file.txt", "hello").await();

                    final DataSize fileContentDataSize = fileSystem.getFileContentDataSize("/file.txt").await();
                    test.assertNotNull(fileContentDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, fileContentDataSize.getUnits());
                    test.assertEqual(5, fileContentDataSize.getValue());
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFolder("/folder/").await();

                    test.assertThrows(() -> fileSystem.getFileContentDataSize("/folder").await(),
                        new FileNotFoundException("/folder"));
                });
            });

            runner.testGroup("getFileContentDataSize(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize((Path)null),
                        new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize(Path.parse("file.txt")),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize(Path.parse("/file.txt")).await(),
                        new FileNotFoundException("/file.txt"));
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/file.txt").await();

                    final DataSize fileContentDataSize = fileSystem.getFileContentDataSize(Path.parse("/file.txt")).await();
                    test.assertNotNull(fileContentDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, fileContentDataSize.getUnits());
                    test.assertEqual(0, fileContentDataSize.getValue());
                });

                runner.test("with existing non-empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/file.txt", "hello").await();

                    final DataSize fileContentDataSize = fileSystem.getFileContentDataSize(Path.parse("/file.txt")).await();
                    test.assertNotNull(fileContentDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, fileContentDataSize.getUnits());
                    test.assertEqual(5, fileContentDataSize.getValue());
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFolder("/folder/").await();

                    test.assertThrows(() -> fileSystem.getFileContentDataSize(Path.parse("/folder")).await(),
                        new FileNotFoundException("/folder"));
                });
            });

            runner.testGroup("getFileContent(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent((String)null), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent(""), new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent("thing.txt"), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent("/thing.txt").await(), new FileNotFoundException("/thing.txt"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/thing.txt").await();

                    test.assertEqual(new byte[0], fileSystem.getFileContent("/thing.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent("/../thing.txt").await(), new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent("/a/../thing.txt").await(), new FileNotFoundException("/thing.txt"));
                });
            });

            runner.testGroup("getFileContent(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent((Path)null), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent(Path.parse("thing.txt")), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent(Path.parse("/thing.txt")).await(),
                        new FileNotFoundException("/thing.txt"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertEqual("/thing.txt", fileSystem.createFile("/thing.txt").await().toString());

                    test.assertEqual(new byte[0], fileSystem.getFileContent(Path.parse("/thing.txt")).await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent("/../thing.txt").await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContent("/a/../thing.txt").await(),
                        new FileNotFoundException("/thing.txt"));
                });
            });

            runner.testGroup("getFileContentByteReadStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContentByteReadStream("/i/dont/exist.txt").await(),
                        new FileNotFoundException("/i/dont/exist.txt"));
                });
            });

            runner.testGroup("getFileContentByteReadStream(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContentByteReadStream(Path.parse("/i/dont/exist.txt")).await(),
                        new FileNotFoundException("/i/dont/exist.txt"));
                });
            });

            runner.testGroup("getFileContentCharacterReadStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContentCharacterReadStream("/i/dont/exist.txt").await(),
                        new FileNotFoundException("/i/dont/exist.txt"));
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    final String filePath = "/i/exist.txt";
                    test.assertEqual(filePath, fileSystem.createFile(filePath).await().toString());
                    try (final CharacterReadStream content = fileSystem.getFileContentCharacterReadStream(filePath).await())
                    {
                        test.assertThrows(() -> content.readEntireString().await(),
                            new EndOfStreamException());
                    }
                });
            });

            runner.testGroup("getFileContentCharacterReadStream(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFileContentCharacterReadStream(Path.parse("/i/dont/exist.txt")).await(),
                        new FileNotFoundException("/i/dont/exist.txt"));
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    final String filePath = "/i/exist.txt";
                    test.assertEqual(filePath, fileSystem.createFile(filePath).await().toString());
                    try (final CharacterReadStream content = fileSystem.getFileContentCharacterReadStream(Path.parse(filePath)).await())
                    {
                        test.assertThrows(() -> content.readEntireString().await(),
                            new EndOfStreamException());
                    }
                });
            });

            runner.testGroup("getFileContentByteWriteStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentByteWriteStream(filePath).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });
            });

            runner.testGroup("getFileContentByteWriteStream(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentByteWriteStream(Path.parse(filePath)).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });
            });

            runner.testGroup("getFileContentCharacterWriteStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    final String filePath = "/i/dont/exist.txt";
                    try (final CharacterWriteStream characterWriteStream = fileSystem.getFileContentCharacterWriteStream(filePath).await())
                    {
                        test.assertTrue(characterWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });
            });

            runner.testGroup("getFileContentCharacterWriteStream(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    final String filePath = "/i/dont/exist.txt";
                    try (final CharacterWriteStream characterWriteStream = fileSystem.getFileContentCharacterWriteStream(Path.parse(filePath)).await())
                    {
                        test.assertTrue(characterWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });
            });

            runner.testGroup("setFileContent(String,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContent((String)null, new byte[] { 0, 1, 2 }), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContent("", new byte[] { 0, 1, 2 }),
                        new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContent("relative.file", new byte[] { 0, 1, 2 }),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path with null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.setFileContent("/A.txt", null).await(),
                        new PreConditionFailure("content cannot be null."));
                    
                    test.assertFalse(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }).await();
                    
                    test.assertThrows(() -> fileSystem.setFileContent("/A.txt", null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    
                    fileSystem.setFileContent("/A.txt", new byte[0]).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }).await();

                    fileSystem.setFileContent("/A.txt", new byte[0]).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 }).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    
                    fileSystem.setFileContent("/folder/A.txt", new byte[] { 0, 1, 2 }).await();
                    
                    test.assertTrue(fileSystem.folderExists("/folder").await());
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/folder/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/A.txt");

                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 }).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContent("/../thing.txt", new byte[] { 0, 1, 2 }).await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/a/../thing.txt", new byte[] { 0, 1, 2 }).await();
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/thing.txt").await());
                });
            });

            runner.testGroup("setFileContent(Path,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContent((Path)null, new byte[] { 0, 1, 2 }), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContent(Path.parse("relative.file"), new byte[] { 0, 1, 2 }),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    
                    test.assertThrows(() -> fileSystem.setFileContent(Path.parse("/A.txt"), null).await(),
                        new PreConditionFailure("content cannot be null."));
                    
                    test.assertFalse(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }).await();

                    test.assertThrows(() -> fileSystem.setFileContent(Path.parse("/A.txt"), (byte[])null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }).await();

                    fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/A.txt").await();

                    fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContent(Path.parse("/../thing.txt"), new byte[] { 0, 1, 2 }).await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent(Path.parse("/a/../thing.txt"), new byte[] { 0, 1, 2 }).await();
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/thing.txt").await());
                });
            });

            runner.testGroup("setFileContentAsString(String,String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContentAsString((String)null, "fake-content"),
                        new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContentAsString("", "fake-content"),
                        new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContentAsString("relative.file", "fake-content"),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path with null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.setFileContentAsString("/A.txt", null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertFalse(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/A.txt", "fake-content").await();

                    test.assertThrows(() -> fileSystem.setFileContentAsString("/A.txt", null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    fileSystem.setFileContentAsString("/A.txt", "").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/A.txt", "fake-content").await();

                    fileSystem.setFileContentAsString("/A.txt", "").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    fileSystem.setFileContentAsString("/A.txt", "fake-content").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    fileSystem.setFileContentAsString("/folder/A.txt", "fake-content").await();

                    test.assertTrue(fileSystem.folderExists("/folder").await());
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentAsString("/folder/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/A.txt");

                    fileSystem.setFileContentAsString("/A.txt", "fake-content").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContentAsString("/../thing.txt", "fake-content").await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/a/../thing.txt", "fake-content").await();
                    test.assertEqual("fake-content", fileSystem.getFileContentAsString("/thing.txt").await());
                });
            });

            runner.testGroup("setFileContentAsString(Path,String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContentAsString((Path)null, "fake-content"),
                        new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContentAsString(Path.parse("relative.file"), "fake-content"),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    test.assertThrows(() -> fileSystem.setFileContentAsString(Path.parse("/A.txt"), null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertFalse(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/A.txt", "fake-content").await();

                    test.assertThrows(() -> fileSystem.setFileContentAsString(Path.parse("/A.txt"), (String)null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    fileSystem.setFileContentAsString(Path.parse("/A.txt"), "").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/A.txt", "fake-content").await();

                    fileSystem.setFileContentAsString(Path.parse("/A.txt"), "").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);

                    fileSystem.setFileContentAsString(Path.parse("/A.txt"), "fake-content").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/A.txt").await();

                    fileSystem.setFileContentAsString(Path.parse("/A.txt"), "fake-content").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentAsString("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.setFileContentAsString(Path.parse("/../thing.txt"), "fake-content").await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString(Path.parse("/a/../thing.txt"), "fake-content").await();
                    test.assertEqual("fake-content", fileSystem.getFileContentAsString("/thing.txt").await());
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively((String)null), new PreConditionFailure("rootedFolderPath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively(""), new PreConditionFailure("rootedFolderPath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively("test/folder"), new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                });

                runner.test("with rooted path when root doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively("F:/test/folder").await(),
                        new RootNotFoundException("F:"));
                });

                runner.test("with rooted path when parent folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively("/test/folder").await(),
                        new FolderNotFoundException("/test/folder"));
                });

                runner.test("with rooted path when folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFolder("/test/").await();

                    test.assertThrows(() -> fileSystem.getFilesAndFoldersRecursively("/test/folder").await(),
                        new FolderNotFoundException("/test/folder"));
                });

                runner.test("with rooted path when folder is empty", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFolder("/test/folder").await();

                    test.assertEqual(Iterable.create(), fileSystem.getFilesAndFoldersRecursively("/test/folder").await());
                });

                runner.test("with rooted path when folder has files", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
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
                    final FileSystem fileSystem = creator.run(test);
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
                    final FileSystem fileSystem = creator.run(test);
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

            runner.testGroup("copyFileTo(File,File)", () ->
            {
                runner.test("with null sourceFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo((File)null, fileSystem.getFile("/dest.txt").await()),
                        new PreConditionFailure("sourceFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), (File)null),
                        new PreConditionFailure("destinationFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), fileSystem.getFile("/dest.txt").await()).await(),
                        new FileNotFoundException("/source.txt"));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 }).await();
                    test.assertNull(fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), fileSystem.getFile("/dest.txt").await()).await());
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 }).await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });
            });

            runner.testGroup("copyFileTo(File,Path)", () ->
            {
                runner.test("with null rootedFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo((File)null, Path.parse("/dest.txt")),
                        new PreConditionFailure("sourceFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), (Path)null),
                        new PreConditionFailure("destinationFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), Path.parse("/dest.txt")).await(),
                        new FileNotFoundException("/source.txt"));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 }).await();
                    test.assertNull(fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), Path.parse("/dest.txt")).await());
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 }).await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });
            });

            runner.testGroup("copyFileTo(Path,File)", () ->
            {
                runner.test("with null rootedFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo((Path)null, fileSystem.getFile("/dest.txt").await()),
                        new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), (File)null),
                        new PreConditionFailure("destinationFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), fileSystem.getFile("/dest.txt").await()).await(),
                        new FileNotFoundException("/source.txt"));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 }).await();
                    test.assertNull(fileSystem.copyFileTo(Path.parse("/source.txt"), fileSystem.getFile("/dest.txt").await()).await());
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 }).await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });
            });

            runner.testGroup("copyFileTo(Path,Path)", () ->
            {
                runner.test("with null rootedFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo((Path)null, Path.parse("/dest.txt")), new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), (Path)null), new PreConditionFailure("destinationFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await(),
                        new FileNotFoundException("/source.txt"));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 }).await();
                    test.assertNull(fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await());
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContent("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.setFileContent("/dest.txt", new byte[] { 10, 11 }).await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });
            });

            runner.testGroup("copyFileToFolder(File,Folder)", () ->
            {
                runner.test("with null sourceFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileToFolder((File)null, fileSystem.getFolder("/dest.txt").await()),
                        new PreConditionFailure("sourceFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(new PreConditionFailure("destinationFolder cannot be null."),
                        () -> fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), (Folder)null));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("with non-existing sourceFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(new FileNotFoundException("/source.txt"),
                        () -> fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), fileSystem.getFolder("/destination/").await()).await());
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.folderExists("/destination/").await());
                });

                runner.test("with non-existing destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });

                runner.test("with existing destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.createFolder("/destination/").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });

                runner.test("with existing file at destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.setFileContentAsString("/destination/source.txt", "oops").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });
            });

            runner.testGroup("copyFileToFolder(Path,Folder)", () ->
            {
                runner.test("with null sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileToFolder((Path)null, fileSystem.getFolder("/dest.txt").await()),
                        new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with non-file sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileToFolder(Path.parse("/"), fileSystem.getFolder("/dest.txt").await()),
                        new PreConditionFailure("sourceFilePath.endsWith(\"/\") cannot be true."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(new PreConditionFailure("destinationFolder cannot be null."),
                        () -> fileSystem.copyFileToFolder(Path.parse("/source.txt"), (Folder)null));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("with non-existing sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(new FileNotFoundException("/source.txt"),
                        () -> fileSystem.copyFileToFolder(Path.parse("/source.txt"), fileSystem.getFolder("/destination/").await()).await());
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.folderExists("/destination/").await());
                });

                runner.test("with non-existing destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });

                runner.test("with existing destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.createFolder("/destination/").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });

                runner.test("with existing file at destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.setFileContentAsString("/destination/source.txt", "oops").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });
            });

            runner.testGroup("copyFileToFolder(File,Path)", () ->
            {
                runner.test("with null sourceFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileToFolder((File)null, Path.parse("/dest.txt")),
                        new PreConditionFailure("sourceFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(new PreConditionFailure("destinationFolderPath cannot be null."),
                        () -> fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), (Path)null));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("with non-existing sourceFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(new FileNotFoundException("/source.txt"),
                        () -> fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), Path.parse("/destination/")).await());
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.folderExists("/destination/").await());
                });

                runner.test("with non-existing destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });

                runner.test("with existing destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.createFolder("/destination/").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });

                runner.test("with existing file at destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.setFileContentAsString("/destination/source.txt", "oops").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });
            });

            runner.testGroup("copyFileToFolder(Path,Path)", () ->
            {
                runner.test("with null sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileToFolder((Path)null, Path.parse("/dest.txt")),
                        new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with non-file sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(() -> fileSystem.copyFileToFolder(Path.parse("/"), Path.parse("/dest.txt")),
                        new PreConditionFailure("sourceFilePath.endsWith(\"/\") cannot be true."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(new PreConditionFailure("destinationFolderPath cannot be null."),
                        () -> fileSystem.copyFileToFolder(Path.parse("/source.txt"), (Path)null));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("with non-existing sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    test.assertThrows(new FileNotFoundException("/source.txt"),
                        () -> fileSystem.copyFileToFolder(Path.parse("/source.txt"), Path.parse("/destination/")).await());
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.folderExists("/destination/").await());
                });

                runner.test("with non-existing destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });

                runner.test("with existing destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.createFolder("/destination/").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
                });

                runner.test("with existing file at destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test);
                    fileSystem.setFileContentAsString("/source.txt", "hello").await();
                    fileSystem.setFileContentAsString("/destination/source.txt", "oops").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentAsString("/destination/source.txt").await());
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
