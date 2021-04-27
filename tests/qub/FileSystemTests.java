package qub;

public interface FileSystemTests
{
    static void test(TestRunner runner, Function1<Clock,FileSystem> creator)
    {
        runner.testGroup(FileSystem.class, () ->
        {
            runner.testGroup("rootExists(String)", () ->
            {
                final Action2<String,Throwable> rootExistsFailureTest = (String rootPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.rootExists(rootPath), expectedError);
                    });
                };

                rootExistsFailureTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                rootExistsFailureTest.run("", new PreConditionFailure("rootPath cannot be empty."));

                final Action2<String,Boolean> rootExistsTest = (String rootPath, Boolean expectedValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.rootExists(rootPath), expectedError);
                    });
                };

                rootExistsFailureTest.run(null, new PreConditionFailure("rootPath cannot be null."));

                final Action2<String,Boolean> rootExistsTest = (String rootPath, Boolean expectedValue) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertEqual(expectedValue, fileSystem.rootExists(Path.parse(rootPath)).await());
                    });
                };

                rootExistsTest.run("C@:/", false);
                rootExistsTest.run("notme:\\", false);
                rootExistsTest.run("/", true);
            });

            runner.test("getRoot()", (Test test) ->
            {
                final FileSystem fileSystem = creator.run(null);
                final Root root = fileSystem.getRoot("/daffy/").await();
                test.assertNotNull(root);
                test.assertEqual("/", root.toString());
            });

            runner.test("getRoots()", (Test test) ->
            {
                final FileSystem fileSystem = creator.run(null);
                test.assertNotNullAndNotEmpty(fileSystem.getRoots().await());
            });

            runner.testGroup("getRootTotalDataSize(String)", () ->
            {
                final Action2<String,Throwable> getRootTotalDataSizeErrorTest = (String rootPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.getRootUsedDataSize(rootPath).await(), expected);
                    });
                };

                getRootUsedDataSizeErrorTest.run(null, new PreConditionFailure("rootPath cannot be null."));
                getRootUsedDataSizeErrorTest.run(Path.parse("hello"), new RootNotFoundException("hello"));
                getRootUsedDataSizeErrorTest.run(Path.parse("/hello/there"), new RootNotFoundException("/hello/there"));
                getRootUsedDataSizeErrorTest.run(Path.parse("p:/"), new RootNotFoundException("p:/"));
            });

            runner.testGroup("iterateEntries(String)", () ->
            {
                final Action2<String,Throwable> iterateEntriesFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.iterateEntries(folderPath).toList(), expectedError);
                    });
                };

                iterateEntriesFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateEntriesFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateEntriesFailureTest.run("/..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,Action1<FileSystem>,Iterable<String>> iterateEntriesTest = (String folderPath, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<FileSystemEntry> result = fileSystem.iterateEntries(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expected, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateEntriesTest.run(
                    "/",
                    null,
                    Iterable.create());
                iterateEntriesTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateEntriesTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                    },
                    Iterable.create("/folderA/", "/file1.txt"));
                iterateEntriesTest.run(
                    "/a/..",
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateEntries(Path)", () ->
            {
                final Action3<Path,Action1<FileSystem>,Throwable> iterateEntriesFailureTest = (Path folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateEntries(folderPath).toList(), expectedError);
                    });
                };

                iterateEntriesFailureTest.run(
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateEntriesFailureTest.run(
                    Path.parse("/.."),
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                iterateEntriesFailureTest.run(
                    Path.parse("/file"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/file").await();
                    },
                    new FolderNotFoundException("/file"));
                iterateEntriesFailureTest.run(
                    Path.parse("/folderThatDoesntExist/"),
                    null,
                    new FolderNotFoundException("/folderThatDoesntExist/"));

                final Action3<Path,Action1<FileSystem>,Iterable<String>> iterateEntriesTest = (Path folderPath, Action1<FileSystem> setup, Iterable<String> expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<FileSystemEntry> result = fileSystem.iterateEntries(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expectedEntryPaths, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateEntriesTest.run(
                    Path.parse("/folderA"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateEntriesTest.run(
                    Path.parse("/folderA/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateEntriesTest.run(
                    Path.parse("/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                    },
                    Iterable.create("/folderA/", "/file1.txt"));
                iterateEntriesTest.run(
                    Path.parse("/a/.."),
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateEntriesRecursively(String)", () ->
            {
                final Action2<String,Throwable> iterateEntriesRecursivelyFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.iterateEntriesRecursively(folderPath), expectedError);
                    });
                };

                iterateEntriesRecursivelyFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateEntriesRecursivelyFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateEntriesRecursivelyFailureTest.run("/..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,Action1<FileSystem>,Iterable<String>> iterateEntriesRecursivelyTest = (String folderPath, Action1<FileSystem> setup, Iterable<String> expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<FileSystemEntry> result = fileSystem.iterateEntriesRecursively(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expectedEntryPaths, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateEntriesRecursivelyTest.run(
                    "/",
                    null,
                    Iterable.create());
                iterateEntriesRecursivelyTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateEntriesRecursivelyTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                        fileSystem.createFile("/folderA/folderB/file3.jpg").await();
                    },
                    Iterable.create("/folderA/", "/file1.txt", "/folderA/folderB/", "/folderA/file2.csv", "/folderA/folderB/file3.jpg"));

                iterateEntriesRecursivelyTest.run(
                    "/a/..",
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateEntriesRecursively(Path)", () ->
            {
                final Action3<Path,Action1<FileSystem>,Throwable> iterateEntriesRecursivelyFailureTest = (Path folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateEntriesRecursively(folderPath).toList(), expectedError);
                    });
                };

                iterateEntriesRecursivelyFailureTest.run(
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateEntriesRecursivelyFailureTest.run(
                    Path.parse("/.."),
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                iterateEntriesRecursivelyFailureTest.run(
                    Path.parse("/file"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/file").await();
                    },
                    new FolderNotFoundException("/file/"));
                iterateEntriesRecursivelyFailureTest.run(
                    Path.parse("/folderThatDoesntExist/"),
                    null,
                    new FolderNotFoundException("/folderThatDoesntExist/"));

                final Action3<Path,Action1<FileSystem>,Iterable<String>> iterateEntriesRecursivelyTest = (Path folderPath, Action1<FileSystem> setup, Iterable<String> expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<FileSystemEntry> result = fileSystem.iterateEntriesRecursively(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expectedEntryPaths, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateEntriesRecursivelyTest.run(
                    Path.parse("/folderA"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateEntriesRecursivelyTest.run(
                    Path.parse("/folderA/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateEntriesRecursivelyTest.run(
                    Path.parse("/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                        fileSystem.createFile("/folderA/folderB/file3.jpg").await();
                    },
                    Iterable.create("/folderA/", "/file1.txt", "/folderA/folderB/", "/folderA/file2.csv", "/folderA/folderB/file3.jpg"));
                iterateEntriesRecursivelyTest.run(
                    Path.parse("/a/.."),
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateEntries(String,Traversal<Folder,FileSystemEntry>)", () ->
            {
                final Action5<String,String,Traversal<Folder,FileSystemEntry>,Action1<FileSystem>,Throwable> iterateEntriesErrorTest = (String testName, String path, Traversal<Folder,FileSystemEntry> traversal, Action1<FileSystem> setup, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateEntries(path, traversal).toList(), expected);
                    });
                };

                iterateEntriesErrorTest.run("with null path",
                    null,
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateEntriesErrorTest.run("with empty path",
                    "",
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateEntriesErrorTest.run("with null traversal",
                    "/rooted/folder/",
                    null,
                    null,
                    new PreConditionFailure("traversal cannot be null."));
                iterateEntriesErrorTest.run("with relative path",
                    "hello",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    null,
                    new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                iterateEntriesErrorTest.run("with path to non-existant folder with non-empty traversal path",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    null,
                    new FolderNotFoundException("/hello/"));

                final Action5<String,String,Traversal<Folder,FileSystemEntry>,Action1<FileSystem>,Iterable<String>> iterateEntriesTest = (String testName, String path, Traversal<Folder,FileSystemEntry> traversal, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expected, fileSystem.iterateEntries(path, traversal).map(FileSystemEntry::toString).toList());
                    });
                };

                iterateEntriesTest.run("with path to non-existant folder with empty traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch((TraversalActions<Folder,FileSystemEntry> actions, Folder currentFolder) ->
                    {
                    }),
                    null,
                    Iterable.create());
                iterateEntriesTest.run("with path to empty folder with empty traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateEntriesTest.run("with path to empty folder",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateEntriesTest.run("with file and folder tree with pre-order traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/again/",
                        "/hello/is/",
                        "/hello/there.mp3",
                        "/hello/is/it/",
                        "/hello/is/it/me/",
                        "/hello/is/it/you.txt"));
                iterateEntriesTest.run("with file and folder tree with post-order traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::postOrderEntriesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/is/it/me/",
                        "/hello/is/it/you.txt",
                        "/hello/is/it/",
                        "/hello/again/",
                        "/hello/is/",
                        "/hello/there.mp3"));
            });

            runner.testGroup("iterateEntries(Path,Traversal<Folder,FileSystemEntry>)", () ->
            {
                final Action5<String,Path,Traversal<Folder,FileSystemEntry>,Action1<FileSystem>,Throwable> iterateEntriesErrorTest = (String testName, Path path, Traversal<Folder,FileSystemEntry> traversal, Action1<FileSystem> setup, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateEntries(path, traversal).toList(), expected);
                    });
                };

                iterateEntriesErrorTest.run("with null path",
                    null,
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateEntriesErrorTest.run("with null traversal",
                    Path.parse("/rooted/folder/"),
                    null,
                    null,
                    new PreConditionFailure("traversal cannot be null."));
                iterateEntriesErrorTest.run("with relative path",
                    Path.parse("hello"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    null,
                    new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                iterateEntriesErrorTest.run("with path to non-existant folder with non-empty traversal path",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    null,
                    new FolderNotFoundException("/hello/"));

                final Action5<String,Path,Traversal<Folder,FileSystemEntry>,Action1<FileSystem>,Iterable<String>> iterateEntriesTest = (String testName, Path path, Traversal<Folder,FileSystemEntry> traversal, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expected, fileSystem.iterateEntries(path, traversal).map(FileSystemEntry::toString).toList());
                    });
                };

                iterateEntriesTest.run("with path to non-existant folder with empty traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch((TraversalActions<Folder,FileSystemEntry> actions, Folder currentFolder) ->
                    {
                    }),
                    null,
                    Iterable.create());
                iterateEntriesTest.run("with path to empty folder with empty traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateEntriesTest.run("with path to empty folder",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateEntriesTest.run("with file and folder tree with pre-order traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderEntriesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/again/",
                        "/hello/is/",
                        "/hello/there.mp3",
                        "/hello/is/it/",
                        "/hello/is/it/me/",
                        "/hello/is/it/you.txt"));
                iterateEntriesTest.run("with file and folder tree with post-order traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::postOrderEntriesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/is/it/me/",
                        "/hello/is/it/you.txt",
                        "/hello/is/it/",
                        "/hello/again/",
                        "/hello/is/",
                        "/hello/there.mp3"));
            });

            runner.testGroup("iterateFolders(String)", () ->
            {
                final Action2<String,Throwable> iterateFoldersFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.iterateFolders(folderPath).toList(), expectedError);
                    });
                };

                iterateFoldersFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFoldersFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateFoldersFailureTest.run("/..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,Action1<FileSystem>,Iterable<String>> iterateFoldersTest = (String folderPath, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<Folder> result = fileSystem.iterateFolders(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expected, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFoldersTest.run(
                    "/",
                    null,
                    Iterable.create());
                iterateFoldersTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFoldersTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                    },
                    Iterable.create("/folderA/"));
                iterateFoldersTest.run(
                    "/a/..",
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFolders(Path)", () ->
            {
                final Action3<Path,Action1<FileSystem>,Throwable> iterateFoldersFailureTest = (Path folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateFolders(folderPath).toList(), expectedError);
                    });
                };

                iterateFoldersFailureTest.run(
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFoldersFailureTest.run(
                    Path.parse("/.."),
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                iterateFoldersFailureTest.run(
                    Path.parse("/file"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/file").await();
                    },
                    new FolderNotFoundException("/file"));
                iterateFoldersFailureTest.run(
                    Path.parse("/folderThatDoesntExist/"),
                    null,
                    new FolderNotFoundException("/folderThatDoesntExist/"));

                final Action3<Path,Action1<FileSystem>,Iterable<String>> iterateFoldersTest = (Path folderPath, Action1<FileSystem> setup, Iterable<String> expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<Folder> result = fileSystem.iterateFolders(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expectedEntryPaths, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFoldersTest.run(
                    Path.parse("/folderA"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFoldersTest.run(
                    Path.parse("/folderA/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFoldersTest.run(
                    Path.parse("/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                    },
                    Iterable.create("/folderA/"));
                iterateFoldersTest.run(
                    Path.parse("/a/.."),
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFoldersRecursively(String)", () ->
            {
                final Action2<String,Throwable> iterateFoldersRecursivelyFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.iterateFoldersRecursively(folderPath), expectedError);
                    });
                };

                iterateFoldersRecursivelyFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFoldersRecursivelyFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateFoldersRecursivelyFailureTest.run("/..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,Action1<FileSystem>,Iterable<String>> iterateFoldersRecursivelyTest = (String folderPath, Action1<FileSystem> setup, Iterable<String> expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<Folder> result = fileSystem.iterateFoldersRecursively(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expectedEntryPaths, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFoldersRecursivelyTest.run(
                    "/",
                    null,
                    Iterable.create());
                iterateFoldersRecursivelyTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFoldersRecursivelyTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                        fileSystem.createFile("/folderA/folderB/file3.jpg").await();
                    },
                    Iterable.create("/folderA/", "/folderA/folderB/"));

                iterateFoldersRecursivelyTest.run(
                    "/a/..",
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFoldersRecursively(Path)", () ->
            {
                final Action3<Path,Action1<FileSystem>,Throwable> iterateFoldersRecursivelyFailureTest = (Path folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateFoldersRecursively(folderPath).toList(), expectedError);
                    });
                };

                iterateFoldersRecursivelyFailureTest.run(
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFoldersRecursivelyFailureTest.run(
                    Path.parse("/.."),
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                iterateFoldersRecursivelyFailureTest.run(
                    Path.parse("/file"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/file").await();
                    },
                    new FolderNotFoundException("/file/"));
                iterateFoldersRecursivelyFailureTest.run(
                    Path.parse("/folderThatDoesntExist/"),
                    null,
                    new FolderNotFoundException("/folderThatDoesntExist/"));

                final Action3<Path,Action1<FileSystem>,Iterable<String>> iterateFoldersRecursivelyTest = (Path folderPath, Action1<FileSystem> setup, Iterable<String> expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<Folder> result = fileSystem.iterateFoldersRecursively(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expectedEntryPaths, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFoldersRecursivelyTest.run(
                    Path.parse("/folderA"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFoldersRecursivelyTest.run(
                    Path.parse("/folderA/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFoldersRecursivelyTest.run(
                    Path.parse("/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                        fileSystem.createFile("/folderA/folderB/file3.jpg").await();
                    },
                    Iterable.create("/folderA/", "/folderA/folderB/"));
                iterateFoldersRecursivelyTest.run(
                    Path.parse("/a/.."),
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFolders(String,Traversal<Folder,Folder>)", () ->
            {
                final Action5<String,String,Traversal<Folder,Folder>,Action1<FileSystem>,Throwable> iterateFoldersErrorTest = (String testName, String path, Traversal<Folder,Folder> traversal, Action1<FileSystem> setup, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateFolders(path, traversal).toList(), expected);
                    });
                };

                iterateFoldersErrorTest.run("with null path",
                    null,
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFoldersErrorTest.run("with empty path",
                    "",
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateFoldersErrorTest.run("with null traversal",
                    "/rooted/folder/",
                    null,
                    null,
                    new PreConditionFailure("traversal cannot be null."));
                iterateFoldersErrorTest.run("with relative path",
                    "hello",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    null,
                    new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                iterateFoldersErrorTest.run("with path to non-existant folder with non-empty traversal path",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    null,
                    new FolderNotFoundException("/hello/"));

                final Action5<String,String,Traversal<Folder,Folder>,Action1<FileSystem>,Iterable<String>> iterateFoldersTest = (String testName, String path, Traversal<Folder,Folder> traversal, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expected, fileSystem.iterateFolders(path, traversal).map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFoldersTest.run("with path to non-existant folder with empty traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch((TraversalActions<Folder,Folder> actions, Folder currentFolder) ->
                    {
                    }),
                    null,
                    Iterable.create());
                iterateFoldersTest.run("with path to empty folder with empty traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateFoldersTest.run("with path to empty folder",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateFoldersTest.run("with file and folder tree with pre-order traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/again/",
                        "/hello/is/",
                        "/hello/is/it/",
                        "/hello/is/it/me/"));
                iterateFoldersTest.run("with file and folder tree with post-order traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::postOrderFoldersTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/is/it/me/",
                        "/hello/is/it/",
                        "/hello/again/",
                        "/hello/is/"));
            });

            runner.testGroup("iterateFolders(Path,Traversal<Folder,Folder>)", () ->
            {
                final Action5<String,Path,Traversal<Folder,Folder>,Action1<FileSystem>,Throwable> iterateFoldersErrorTest = (String testName, Path path, Traversal<Folder,Folder> traversal, Action1<FileSystem> setup, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateFolders(path, traversal).toList(), expected);
                    });
                };

                iterateFoldersErrorTest.run("with null path",
                    null,
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFoldersErrorTest.run("with null traversal",
                    Path.parse("/rooted/folder/"),
                    null,
                    null,
                    new PreConditionFailure("traversal cannot be null."));
                iterateFoldersErrorTest.run("with relative path",
                    Path.parse("hello"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    null,
                    new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                iterateFoldersErrorTest.run("with path to non-existant folder with non-empty traversal path",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    null,
                    new FolderNotFoundException("/hello/"));

                final Action5<String,Path,Traversal<Folder,Folder>,Action1<FileSystem>,Iterable<String>> iterateFoldersTest = (String testName, Path path, Traversal<Folder,Folder> traversal, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expected, fileSystem.iterateFolders(path, traversal).map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFoldersTest.run("with path to non-existant folder with empty traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch((TraversalActions<Folder,Folder> actions, Folder currentFolder) ->
                    {
                    }),
                    null,
                    Iterable.create());
                iterateFoldersTest.run("with path to empty folder with empty traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateFoldersTest.run("with path to empty folder",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateFoldersTest.run("with file and folder tree with pre-order traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFoldersTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/again/",
                        "/hello/is/",
                        "/hello/is/it/",
                        "/hello/is/it/me/"));
                iterateFoldersTest.run("with file and folder tree with post-order traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::postOrderFoldersTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/is/it/me/",
                        "/hello/is/it/",
                        "/hello/again/",
                        "/hello/is/"));
            });

            runner.testGroup("iterateFiles(String)", () ->
            {
                final Action2<String,Throwable> iterateFilesFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.iterateFiles(folderPath).toList(), expectedError);
                    });
                };

                iterateFilesFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFilesFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateFilesFailureTest.run("/..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,Action1<FileSystem>,Iterable<String>> iterateFilesTest = (String folderPath, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<File> result = fileSystem.iterateFiles(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expected, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFilesTest.run(
                    "/",
                    null,
                    Iterable.create());
                iterateFilesTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFilesTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                    },
                    Iterable.create("/file1.txt"));
                iterateFilesTest.run(
                    "/a/..",
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFiles(Path)", () ->
            {
                final Action3<Path,Action1<FileSystem>,Throwable> iterateFilesFailureTest = (Path folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateFiles(folderPath).toList(), expectedError);
                    });
                };

                iterateFilesFailureTest.run(
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFilesFailureTest.run(
                    Path.parse("/.."),
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                iterateFilesFailureTest.run(
                    Path.parse("/file"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/file").await();
                    },
                    new FolderNotFoundException("/file"));
                iterateFilesFailureTest.run(
                    Path.parse("/folderThatDoesntExist/"),
                    null,
                    new FolderNotFoundException("/folderThatDoesntExist/"));

                final Action3<Path,Action1<FileSystem>,Iterable<String>> iterateFilesTest = (Path folderPath, Action1<FileSystem> setup, Iterable<String> expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<File> result = fileSystem.iterateFiles(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expectedEntryPaths, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFilesTest.run(
                    Path.parse("/folderA"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFilesTest.run(
                    Path.parse("/folderA/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFilesTest.run(
                    Path.parse("/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                    },
                    Iterable.create("/file1.txt"));
                iterateFilesTest.run(
                    Path.parse("/a/.."),
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFilesRecursively(String)", () ->
            {
                final Action2<String,Throwable> iterateFilesRecursivelyFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.iterateFilesRecursively(folderPath), expectedError);
                    });
                };

                iterateFilesRecursivelyFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFilesRecursivelyFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateFilesRecursivelyFailureTest.run("/..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,Action1<FileSystem>,Iterable<String>> iterateFilesRecursivelyTest = (String folderPath, Action1<FileSystem> setup, Iterable<String> expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<File> result = fileSystem.iterateFilesRecursively(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expectedEntryPaths, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFilesRecursivelyTest.run(
                    "/",
                    null,
                    Iterable.create());
                iterateFilesRecursivelyTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFilesRecursivelyTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                        fileSystem.createFile("/folderA/folderB/file3.jpg").await();
                    },
                    Iterable.create(
                        "/file1.txt",
                        "/folderA/file2.csv",
                        "/folderA/folderB/file3.jpg"));

                iterateFilesRecursivelyTest.run(
                    "/a/..",
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFilesRecursively(Path)", () ->
            {
                final Action3<Path,Action1<FileSystem>,Throwable> iterateFilesRecursivelyFailureTest = (Path folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateFilesRecursively(folderPath).toList(), expectedError);
                    });
                };

                iterateFilesRecursivelyFailureTest.run(
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFilesRecursivelyFailureTest.run(
                    Path.parse("/.."),
                    null,
                    new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                iterateFilesRecursivelyFailureTest.run(
                    Path.parse("/file"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/file").await();
                    },
                    new FolderNotFoundException("/file/"));
                iterateFilesRecursivelyFailureTest.run(
                    Path.parse("/folderThatDoesntExist/"),
                    null,
                    new FolderNotFoundException("/folderThatDoesntExist/"));

                final Action3<Path,Action1<FileSystem>,Iterable<String>> iterateFilesRecursivelyTest = (Path folderPath, Action1<FileSystem> setup, Iterable<String> expectedEntryPaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Iterator<File> result = fileSystem.iterateFilesRecursively(folderPath);
                        test.assertNotNull(result);
                        test.assertEqual(expectedEntryPaths, result.map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFilesRecursivelyTest.run(
                    Path.parse("/folderA"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFilesRecursivelyTest.run(
                    Path.parse("/folderA/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA").await();
                    },
                    Iterable.create());
                iterateFilesRecursivelyTest.run(
                    Path.parse("/"),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB").await();
                        fileSystem.createFile("/file1.txt").await();
                        fileSystem.createFile("/folderA/file2.csv").await();
                        fileSystem.createFile("/folderA/folderB/file3.jpg").await();
                    },
                    Iterable.create(
                        "/file1.txt",
                        "/folderA/file2.csv",
                        "/folderA/folderB/file3.jpg"));
                iterateFilesRecursivelyTest.run(
                    Path.parse("/a/.."),
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFiles(String,Traversal<Folder,File>)", () ->
            {
                final Action5<String,String,Traversal<Folder,File>,Action1<FileSystem>,Throwable> iterateFilesErrorTest = (String testName, String path, Traversal<Folder,File> traversal, Action1<FileSystem> setup, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateFiles(path, traversal).toList(), expected);
                    });
                };

                iterateFilesErrorTest.run("with null path",
                    null,
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFilesErrorTest.run("with empty path",
                    "",
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateFilesErrorTest.run("with null traversal",
                    "/rooted/folder/",
                    null,
                    null,
                    new PreConditionFailure("traversal cannot be null."));
                iterateFilesErrorTest.run("with relative path",
                    "hello",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    null,
                    new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                iterateFilesErrorTest.run("with path to non-existant folder with non-empty traversal path",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    null,
                    new FolderNotFoundException("/hello/"));

                final Action5<String,String,Traversal<Folder,File>,Action1<FileSystem>,Iterable<String>> iterateFilesTest = (String testName, String path, Traversal<Folder,File> traversal, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expected, fileSystem.iterateFiles(path, traversal).map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFilesTest.run("with path to non-existant folder with empty traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch((TraversalActions<Folder,File> actions, Folder currentFolder) ->
                    {
                    }),
                    null,
                    Iterable.create());
                iterateFilesTest.run("with path to empty folder with empty traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateFilesTest.run("with path to empty folder",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateFilesTest.run("with file and folder tree with pre-order traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/there.mp3",
                        "/hello/is/it/you.txt"));
                iterateFilesTest.run("with file and folder tree with post-order traversal",
                    "/hello/",
                    Traversal.createDepthFirstSearch(FileSystem::postOrderFilesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/is/it/you.txt",
                        "/hello/there.mp3"));
            });

            runner.testGroup("iterateFiles(Path,Traversal<Folder,File>)", () ->
            {
                final Action5<String,Path,Traversal<Folder,File>,Action1<FileSystem>,Throwable> iterateFilesErrorTest = (String testName, Path path, Traversal<Folder,File> traversal, Action1<FileSystem> setup, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateFiles(path, traversal).toList(), expected);
                    });
                };

                iterateFilesErrorTest.run("with null path",
                    null,
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFilesErrorTest.run("with null traversal",
                    Path.parse("/rooted/folder/"),
                    null,
                    null,
                    new PreConditionFailure("traversal cannot be null."));
                iterateFilesErrorTest.run("with relative path",
                    Path.parse("hello"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    null,
                    new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                iterateFilesErrorTest.run("with path to non-existant folder with non-empty traversal path",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    null,
                    new FolderNotFoundException("/hello/"));

                final Action5<String,Path,Traversal<Folder,File>,Action1<FileSystem>,Iterable<String>> iterateFilesTest = (String testName, Path path, Traversal<Folder,File> traversal, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expected, fileSystem.iterateFiles(path, traversal).map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFilesTest.run("with path to non-existant folder with empty traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch((TraversalActions<Folder,File> actions, Folder currentFolder) ->
                    {
                    }),
                    null,
                    Iterable.create());
                iterateFilesTest.run("with path to empty folder with empty traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateFilesTest.run("with path to empty folder",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                    },
                    Iterable.create());
                iterateFilesTest.run("with file and folder tree with pre-order traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::preOrderFilesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/there.mp3",
                        "/hello/is/it/you.txt"));
                iterateFilesTest.run("with file and folder tree with post-order traversal",
                    Path.parse("/hello/"),
                    Traversal.createDepthFirstSearch(FileSystem::postOrderFilesTraversal),
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/hello/").await();
                        fileSystem.createFile("/1.txt").await();
                        fileSystem.createFile("/hello/there.mp3").await();
                        fileSystem.createFolder("/hello/again/").await();
                        fileSystem.createFolder("/hello/is/it/me/").await();
                        fileSystem.createFile("/hello/is/it/you.txt").await();
                    },
                    Iterable.create(
                        "/hello/is/it/you.txt",
                        "/hello/there.mp3"));
            });

            runner.testGroup("iterateFiles(String)", () ->
            {
                final Action2<String,Throwable> iterateFilesFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.iterateFiles(folderPath).toList(), expectedError);
                    });
                };

                iterateFilesFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFilesFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateFilesFailureTest.run("/..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,Action1<FileSystem>,Iterable<String>> iterateFilesTest = (String folderPath, Action1<FileSystem> setup, Iterable<String> expectedFilePaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expectedFilePaths, fileSystem.iterateFiles(folderPath).map(FileSystemEntry::toString).toList());
                    });
                };

                iterateFilesTest.run(
                    "/a/..",
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFiles(Path)", () ->
            {
                final Action2<Path,Throwable> iterateFilesFailureTest = (Path folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.iterateFiles(folderPath).toList(), expectedError);
                    });
                };

                iterateFilesFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFilesFailureTest.run(Path.parse("/.."), new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<Path,Action1<FileSystem>,Iterable<String>> iterateFilesTest = (Path folderPath, Action1<FileSystem> setup, Iterable<String> expectedFilePaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expectedFilePaths, fileSystem.iterateFiles(folderPath).map(File::toString).toList());
                    });
                };

                iterateFilesTest.run(
                    Path.parse("/a/.."),
                    null,
                    Iterable.create());
            });

            runner.testGroup("iterateFilesRecursively(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,Throwable> iterateFilesRecursivelyFailureTest = (String folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateFilesRecursively(folderPath).toList(), expectedError);
                    });
                };

                iterateFilesRecursivelyFailureTest.run(null, null, new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateFilesRecursivelyFailureTest.run("", null, new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateFilesRecursivelyFailureTest.run("/..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                iterateFilesRecursivelyFailureTest.run("test/folder", null, new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                iterateFilesRecursivelyFailureTest.run("F:/test/folder", null, new RootNotFoundException("F:"));
                iterateFilesRecursivelyFailureTest.run("/test/folder", null, new FolderNotFoundException("/test/folder/"));
                iterateFilesRecursivelyFailureTest.run("/test/folder",
                    (FileSystem fileSystem) -> { fileSystem.createFolder("/test/").await(); },
                    new FolderNotFoundException("/test/folder/"));

                final Action3<String,Action1<FileSystem>,Iterable<String>> iterateFilesRecursivelyTest = (String folderPath, Action1<FileSystem> setup, Iterable<String> expectedFilePaths) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expectedFilePaths, fileSystem.iterateFilesRecursively(folderPath).map(File::toString).toList());
                    });
                };

                iterateFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/test/folder").await(),
                    Iterable.create());
                iterateFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder").await();
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                    },
                    Iterable.create("/test/folder/1.txt", "/test/folder/2.txt"));
                iterateFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder").await();
                        fileSystem.createFolder("/test/folder/1.txt").await();
                        fileSystem.createFolder("/test/folder/2.txt").await();
                    },
                    Iterable.create());
                iterateFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                        fileSystem.createFile("/test/folder/A/3.csv").await();
                        fileSystem.createFile("/test/folder/B/C/4.xml").await();
                        fileSystem.createFile("/test/folder/A/5.png").await();
                    },
                    Iterable.create(
                        "/test/folder/1.txt",
                        "/test/folder/2.txt",
                        "/test/folder/A/3.csv",
                        "/test/folder/A/5.png",
                        "/test/folder/B/C/4.xml"));
                iterateFilesRecursivelyTest.run(
                    "/a/..",
                    null,
                    Iterable.create());
            });

            runner.testGroup("getFolder(String)", () ->
            {
                final Action2<String,Throwable> getFolderFailureTest = (String folderPathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPathString), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                getFolderTest.run("/a/b", "/a/b/", null);
                getFolderTest.run("/..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                getFolderTest.run("/a/..", "/", null);
            });

            runner.testGroup("folderExists(String)", () ->
            {
                final Action2<String,Throwable> folderExistsFailureTest = (String folderPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.folderExists(folderPath), expectedError);
                    });
                };

                folderExistsFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                folderExistsFailureTest.run("", new PreConditionFailure("rootedFolderPath cannot be empty."));

                final Action4<String,Action1<FileSystem>,Boolean,Throwable> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.folderExists(folderPath), expectedError);
                    });
                };

                folderExistsFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));

                final Action4<String,Action1<FileSystem>,Boolean,Throwable> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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

                createFolderTest.run(
                    "with root",
                    "/",
                    null,
                    null,
                    new FolderAlreadyExistsException("/"));
                createFolderTest.run(
                    "with root that doesn't exist",
                    "C:/",
                    null,
                    null,
                    new RootNotFoundException("C:"));
                createFolderTest.run(
                    "with rooted path with root that doesn't exist",
                    "C:/folder/",
                    null,
                    null,
                    new RootNotFoundException("C:"));
                createFolderTest.run(
                    "with rooted path that doesn't exist",
                    "/folder",
                    null,
                    "/folder/",
                    null);
                createFolderTest.run(
                    "with rooted path that already exists",
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder").await(),
                    "/folder/",
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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

                createFolderTest.run("with rooted path that doesn't exist", "/folder", null, "/folder/", null);
                createFolderTest.run(
                    "with rooted path that already exists",
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder").await(),
                    "/folder/",
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.deleteFolder(folderPath), expectedError);
                    });
                };

                deleteFolderFailureTest.run(null, new PreConditionFailure("rootedFolderPath cannot be null."));
                deleteFolderFailureTest.run(Path.parse("folder"), new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));

                final Action3<String,Action1<FileSystem>,Throwable> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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
                        final FileSystem fileSystem = creator.run(null);
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

                runner.test("with existing rooted path",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final FileSystem fileSystem = creator.run(clock);
                    final DateTime beforeFileCreated = clock.getCurrentDateTime().toUTC();
                    fileSystem.createFile("/thing.txt").await();
                    final DateTime afterFileCreated = clock.getCurrentDateTime().toUTC();

                    final DateTime lastModified = fileSystem.getFileLastModified("/thing.txt").await().toUTC();
                    test.assertNotNull(lastModified);
                    test.assertGreaterThanOrEqualTo(lastModified, beforeFileCreated);
                    test.assertEqual(afterFileCreated.getDurationSinceEpoch(), lastModified.getDurationSinceEpoch(), Duration.milliseconds(5));
                    test.assertEqual(fileSystem.getFileLastModified("/thing.txt").await().toUTC(), lastModified);
                });
            });

            runner.testGroup("getFileLastModified(Path)", () ->
            {
                final Action2<Path,Throwable> getFileLastModifiedFailureTest = (Path filePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        test.assertThrows(() -> fileSystem.getFileLastModified(filePath), expectedError);
                    });
                };

                getFileLastModifiedFailureTest.run(null, new PreConditionFailure("rootedFilePath cannot be null."));
                getFileLastModifiedFailureTest.run(Path.parse("relativeFile.txt"), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));

                final Action4<String,Action1<FileSystem>,DateTime,Throwable> getFileLastModifiedTest = (String filePath, Action1<FileSystem> setup, DateTime expectedLastModified, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
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

                runner.test("with existing rooted path",
                    (TestResources resources) -> Tuple.create(resources.getClock()),
                    (Test test, Clock clock) ->
                {
                    final FileSystem fileSystem = creator.run(clock);
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
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize((String)null),
                        new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize(""),
                        new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize("file.txt"),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize("/file.txt").await(),
                        new FileNotFoundException("/file.txt"));
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/file.txt").await();

                    final DataSize fileContentDataSize = fileSystem.getFileContentDataSize("/file.txt").await();
                    test.assertNotNull(fileContentDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, fileContentDataSize.getUnits());
                    test.assertEqual(0, fileContentDataSize.getValue());
                });

                runner.test("with existing non-empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/file.txt", "hello").await();

                    final DataSize fileContentDataSize = fileSystem.getFileContentDataSize("/file.txt").await();
                    test.assertNotNull(fileContentDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, fileContentDataSize.getUnits());
                    test.assertEqual(5, fileContentDataSize.getValue());
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/folder/").await();

                    test.assertThrows(() -> fileSystem.getFileContentDataSize("/folder").await(),
                        new FileNotFoundException("/folder"));
                });
            });

            runner.testGroup("getFileContentDataSize(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize((Path)null),
                        new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize(Path.parse("file.txt")),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.getFileContentDataSize(Path.parse("/file.txt")).await(),
                        new FileNotFoundException("/file.txt"));
                });

                runner.test("with existing empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/file.txt").await();

                    final DataSize fileContentDataSize = fileSystem.getFileContentDataSize(Path.parse("/file.txt")).await();
                    test.assertNotNull(fileContentDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, fileContentDataSize.getUnits());
                    test.assertEqual(0, fileContentDataSize.getValue());
                });

                runner.test("with existing non-empty file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/file.txt", "hello").await();

                    final DataSize fileContentDataSize = fileSystem.getFileContentDataSize(Path.parse("/file.txt")).await();
                    test.assertNotNull(fileContentDataSize);
                    test.assertEqual(DataSizeUnit.Bytes, fileContentDataSize.getUnits());
                    test.assertEqual(5, fileContentDataSize.getValue());
                });

                runner.test("with existing folder path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFolder("/folder/").await();

                    test.assertThrows(() -> fileSystem.getFileContentDataSize(Path.parse("/folder")).await(),
                        new FileNotFoundException("/folder"));
                });
            });

            runner.testGroup("getFileContent(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent((String)null), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent(""), new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent("thing.txt"), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent("/thing.txt").await(), new FileNotFoundException("/thing.txt"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/thing.txt").await();

                    test.assertEqual(new byte[0], fileSystem.getFileContent("/thing.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent("/../thing.txt").await(), new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent("/a/../thing.txt").await(), new FileNotFoundException("/thing.txt"));
                });
            });

            runner.testGroup("getFileContent(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent((Path)null), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent(Path.parse("thing.txt")), new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent(Path.parse("/thing.txt")).await(),
                        new FileNotFoundException("/thing.txt"));
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertEqual("/thing.txt", fileSystem.createFile("/thing.txt").await().toString());

                    test.assertEqual(new byte[0], fileSystem.getFileContent(Path.parse("/thing.txt")).await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent("/../thing.txt").await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContent("/a/../thing.txt").await(),
                        new FileNotFoundException("/thing.txt"));
                });
            });

            runner.testGroup("getFileContentReadStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContentReadStream("/i/dont/exist.txt").await(),
                        new FileNotFoundException("/i/dont/exist.txt"));
                });
            });

            runner.testGroup("getFileContentReadStream(Path)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.getFileContentReadStream(Path.parse("/i/dont/exist.txt")).await(),
                        new FileNotFoundException("/i/dont/exist.txt"));
                });
            });

            runner.testGroup("getFileContentByteWriteStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });
            });

            runner.testGroup("getFileContentsByteWriteStream(String,OpenWriteType)", () ->
            {
                runner.test("with null OpenWriteType", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    test.assertThrows(() -> fileSystem.getFileContentsByteWriteStream(filePath, null),
                        new PreConditionFailure("openWriteType cannot be null."));
                    test.assertFalse(fileSystem.fileExists(filePath).await());
                });

                runner.test("with non-existing file and CreateOrOverwrite", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrOverwrite).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to non-existing file and CreateOrOverwrite", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrOverwrite).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 0, 1, 2 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with non-existing file and CreateOrAppend", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrAppend).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to non-existing file and CreateOrAppend", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrAppend).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 0, 1, 2 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with existing file and CreateOrOverwrite", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/exist.txt";
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrOverwrite).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to existing file and CreateOrOverwrite", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/exist.txt";
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrOverwrite).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 3, 4 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 3, 4 }, fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with existing file and CreateOrAppend", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/exist.txt";
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrAppend).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to existing file and CreateOrAppend", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/exist.txt";
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrAppend).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 3, 4 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, fileSystem.getFileContent(filePath).await());
                    }
                });
            });

            runner.testGroup("getFileContentsByteWriteStream(Path)", () ->
            {
                runner.test("with null OpenWriteType", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    test.assertThrows(() -> fileSystem.getFileContentsByteWriteStream(filePath, null),
                        new PreConditionFailure("openWriteType cannot be null."));
                    test.assertFalse(fileSystem.fileExists(filePath).await());
                });

                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 0, 1, 2 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/exist.txt";
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/exist.txt";
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 3, 4 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 3, 4 }, fileSystem.getFileContent(filePath).await());
                    }
                });
            });

            runner.testGroup("getFileContentsByteWriteStream(Path,OpenWriteType)", () ->
            {
                runner.test("with null OpenWriteType", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    test.assertThrows(() -> fileSystem.getFileContentsByteWriteStream(filePath, null),
                        new PreConditionFailure("openWriteType cannot be null."));
                    test.assertFalse(fileSystem.fileExists(filePath).await());
                });

                runner.test("with non-existing file and CreateOrOverwrite", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrOverwrite).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to non-existing file and CreateOrOverwrite", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrOverwrite).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 0, 1, 2 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with non-existing file and CreateOrAppend", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrAppend).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to non-existing file and CreateOrAppend", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final Path filePath = Path.parse("/i/dont/exist.txt");
                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrAppend).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 0, 1, 2 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with existing file and CreateOrOverwrite", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final Path filePath = Path.parse("/i/dont/exist.txt");
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrOverwrite).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[0], fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to existing file and CreateOrOverwrite", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final Path filePath = Path.parse("/i/dont/exist.txt");
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrOverwrite).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 3, 4 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 3, 4 }, fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with existing file and CreateOrAppend", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final Path filePath = Path.parse("/i/dont/exist.txt");
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrAppend).await())
                    {
                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent(filePath).await());
                    }
                });

                runner.test("with writing to existing file and CreateOrAppend", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final Path filePath = Path.parse("/i/dont/exist.txt");
                    fileSystem.setFileContents(filePath, new byte[] { 0, 1, 2 }).await();

                    try (final ByteWriteStream byteWriteStream = fileSystem.getFileContentsByteWriteStream(filePath, OpenWriteType.CreateOrAppend).await())
                    {
                        byteWriteStream.writeAll(new byte[] { 3, 4 }).await();

                        test.assertTrue(byteWriteStream.dispose().await());
                        test.assertTrue(fileSystem.fileExists(filePath).await());
                        test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, fileSystem.getFileContent(filePath).await());
                    }
                });
            });

            runner.testGroup("getFileContentCharacterWriteStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final CharacterWriteStream characterWriteStream = fileSystem.getFileContentsCharacterWriteStream(filePath).await())
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
                    final FileSystem fileSystem = creator.run(null);
                    final String filePath = "/i/dont/exist.txt";
                    try (final CharacterWriteStream characterWriteStream = fileSystem.getFileContentsCharacterWriteStream(Path.parse(filePath)).await())
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
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContents((String)null, new byte[] { 0, 1, 2 }), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContents("", new byte[] { 0, 1, 2 }),
                        new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContents("relative.file", new byte[] { 0, 1, 2 }),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path with null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.setFileContents("/A.txt", null).await(),
                        new PreConditionFailure("content cannot be null."));
                    
                    test.assertFalse(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/A.txt", new byte[] { 0, 1 }).await();
                    
                    test.assertThrows(() -> fileSystem.setFileContents("/A.txt", null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    
                    fileSystem.setFileContents("/A.txt", new byte[0]).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/A.txt", new byte[] { 0, 1 }).await();

                    fileSystem.setFileContents("/A.txt", new byte[0]).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    
                    fileSystem.setFileContents("/folder/A.txt", new byte[] { 0, 1, 2 }).await();
                    
                    test.assertTrue(fileSystem.folderExists("/folder").await());
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/folder/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt");

                    fileSystem.setFileContents("/A.txt", new byte[] { 0, 1, 2 }).await();
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContents("/../thing.txt", new byte[] { 0, 1, 2 }).await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/a/../thing.txt", new byte[] { 0, 1, 2 }).await();
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/thing.txt").await());
                });
            });

            runner.testGroup("setFileContent(Path,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContents((Path)null, new byte[] { 0, 1, 2 }), new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContents(Path.parse("relative.file"), new byte[] { 0, 1, 2 }),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    
                    test.assertThrows(() -> fileSystem.setFileContents(Path.parse("/A.txt"), null).await(),
                        new PreConditionFailure("content cannot be null."));
                    
                    test.assertFalse(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/A.txt", new byte[] { 0, 1 }).await();

                    test.assertThrows(() -> fileSystem.setFileContents(Path.parse("/A.txt"), (byte[])null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/A.txt", new byte[] { 0, 1 }).await();

                    fileSystem.setFileContents(Path.parse("/A.txt"), new byte[0]).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt").await();

                    fileSystem.setFileContents(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }).await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContents(Path.parse("/../thing.txt"), new byte[] { 0, 1, 2 }).await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents(Path.parse("/a/../thing.txt"), new byte[] { 0, 1, 2 }).await();
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/thing.txt").await());
                });
            });

            runner.testGroup("setFileContentAsString(String,String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContentsAsString((String)null, "fake-content"),
                        new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContentsAsString("", "fake-content"),
                        new PreConditionFailure("rootedFilePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContentsAsString("relative.file", "fake-content"),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path with null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.setFileContentsAsString("/A.txt", null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertFalse(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/A.txt", "fake-content").await();

                    test.assertThrows(() -> fileSystem.setFileContentsAsString("/A.txt", null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    fileSystem.setFileContentsAsString("/A.txt", "").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/A.txt", "fake-content").await();

                    fileSystem.setFileContentsAsString("/A.txt", "").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    fileSystem.setFileContentsAsString("/A.txt", "fake-content").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    fileSystem.setFileContentsAsString("/folder/A.txt", "fake-content").await();

                    test.assertTrue(fileSystem.folderExists("/folder").await());
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentsAsString("/folder/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt");

                    fileSystem.setFileContentsAsString("/A.txt", "fake-content").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContentsAsString("/../thing.txt", "fake-content").await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/a/../thing.txt", "fake-content").await();
                    test.assertEqual("fake-content", fileSystem.getFileContentsAsString("/thing.txt").await());
                });
            });

            runner.testGroup("setFileContentAsString(Path,String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContentsAsString((Path)null, "fake-content"),
                        new PreConditionFailure("rootedFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContentsAsString(Path.parse("relative.file"), "fake-content"),
                        new PreConditionFailure("rootedFilePath.isRooted() cannot be false."));
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    test.assertThrows(() -> fileSystem.setFileContentsAsString(Path.parse("/A.txt"), null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertFalse(fileSystem.fileExists("/A.txt").await());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/A.txt", "fake-content").await();

                    test.assertThrows(() -> fileSystem.setFileContentsAsString(Path.parse("/A.txt"), (String)null).await(),
                        new PreConditionFailure("content cannot be null."));

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    fileSystem.setFileContentsAsString(Path.parse("/A.txt"), "").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/A.txt", "fake-content").await();

                    fileSystem.setFileContentsAsString(Path.parse("/A.txt"), "").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);

                    fileSystem.setFileContentsAsString(Path.parse("/A.txt"), "fake-content").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/A.txt").await();

                    fileSystem.setFileContentsAsString(Path.parse("/A.txt"), "fake-content").await();

                    test.assertTrue(fileSystem.fileExists("/A.txt").await());
                    test.assertEqual("fake-content", fileSystem.getFileContentsAsString("/A.txt").await());
                });

                runner.test("with rooted path resolved outside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.setFileContentsAsString(Path.parse("/../thing.txt"), "fake-content").await(),
                        new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                });

                runner.test("with rooted path resolved inside the root", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString(Path.parse("/a/../thing.txt"), "fake-content").await();
                    test.assertEqual("fake-content", fileSystem.getFileContentsAsString("/thing.txt").await());
                });
            });

            runner.testGroup("iterateEntriesRecursively(String)", () ->
            {
                final Action4<String,String,Action1<FileSystem>,Throwable> iterateEntriesRecursivelyErrorTest = (String testName, String rootedFolderPath, Action1<FileSystem> setup, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertThrows(() -> fileSystem.iterateEntriesRecursively(rootedFolderPath).toList(), expected);
                    });
                };

                iterateEntriesRecursivelyErrorTest.run("with null path",
                    null,
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be null."));
                iterateEntriesRecursivelyErrorTest.run("with empty path",
                    "",
                    null,
                    new PreConditionFailure("rootedFolderPath cannot be empty."));
                iterateEntriesRecursivelyErrorTest.run("with relative path",
                    "test/folder",
                    null,
                    new PreConditionFailure("rootedFolderPath.isRooted() cannot be false."));
                iterateEntriesRecursivelyErrorTest.run("with rooted path when root doesn't exist",
                    "F:/test/folder",
                    null,
                    new RootNotFoundException("F:"));
                iterateEntriesRecursivelyErrorTest.run("with rooted path when parent folder doesn't exist",
                    "/test/folder",
                    null,
                    new FolderNotFoundException("/test/folder/"));
                iterateEntriesRecursivelyErrorTest.run("with rooted path when folder doesn't exist",
                    "/test/folder",
                    (FileSystem fileSystem) -> { fileSystem.createFolder("/test/").await(); },
                    new FolderNotFoundException("/test/folder/"));

                final Action4<String,String,Action1<FileSystem>,Iterable<String>> iterateEntriesRecursivelyTest = (String testName, String rootedFolderPath, Action1<FileSystem> setup, Iterable<String> expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(null);
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expected, fileSystem.iterateEntriesRecursively(rootedFolderPath).map(FileSystemEntry::toString).toList());
                    });
                };

                iterateEntriesRecursivelyTest.run("with rooted path when folder is empty",
                    "/test/folder",
                    (FileSystem fileSystem) -> { fileSystem.createFolder("/test/folder/").await(); },
                    Iterable.create());
                iterateEntriesRecursivelyTest.run("with rooted path when folder has files",
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                    },
                    Iterable.create(
                        "/test/folder/1.txt",
                        "/test/folder/2.txt"));
                iterateEntriesRecursivelyTest.run("with rooted path when folder has folders",
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder/1/").await();
                        fileSystem.createFolder("/test/folder/2/").await();
                    },
                    Iterable.create(
                        "/test/folder/1/",
                        "/test/folder/2/"));
                iterateEntriesRecursivelyTest.run("with rooted path when folder has grandchild files and folders",
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt").await();
                        fileSystem.createFile("/test/folder/2.txt").await();
                        fileSystem.createFile("/test/folder/A/3.csv").await();
                        fileSystem.createFile("/test/folder/B/C/4.xml").await();
                        fileSystem.createFile("/test/folder/A/5.png").await();
                    },
                    Iterable.create(
                        "/test/folder/A/",
                        "/test/folder/B/",
                        "/test/folder/1.txt",
                        "/test/folder/2.txt",
                        "/test/folder/A/3.csv",
                        "/test/folder/A/5.png",
                        "/test/folder/B/C/",
                        "/test/folder/B/C/4.xml"));
            });

            runner.testGroup("copyFileTo(File,File)", () ->
            {
                runner.test("with null sourceFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo((File)null, fileSystem.getFile("/dest.txt").await()),
                        new PreConditionFailure("sourceFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), (File)null),
                        new PreConditionFailure("destinationFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), fileSystem.getFile("/dest.txt").await()).await(),
                        new FileNotFoundException("/source.txt"));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.setFileContents("/dest.txt", new byte[] { 10, 11 }).await();
                    test.assertNull(fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), fileSystem.getFile("/dest.txt").await()).await());
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.setFileContents("/dest.txt", new byte[] { 10, 11 }).await();
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
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo((File)null, Path.parse("/dest.txt")),
                        new PreConditionFailure("sourceFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), (Path)null),
                        new PreConditionFailure("destinationFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), Path.parse("/dest.txt")).await(),
                        new FileNotFoundException("/source.txt"));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.setFileContents("/dest.txt", new byte[] { 10, 11 }).await();
                    test.assertNull(fileSystem.copyFileTo(fileSystem.getFile("/source.txt").await(), Path.parse("/dest.txt")).await());
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.setFileContents("/dest.txt", new byte[] { 10, 11 }).await();
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
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo((Path)null, fileSystem.getFile("/dest.txt").await()),
                        new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), (File)null),
                        new PreConditionFailure("destinationFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), fileSystem.getFile("/dest.txt").await()).await(),
                        new FileNotFoundException("/source.txt"));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), fileSystem.getFile("/dest.txt").await()).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.setFileContents("/dest.txt", new byte[] { 10, 11 }).await();
                    test.assertNull(fileSystem.copyFileTo(Path.parse("/source.txt"), fileSystem.getFile("/dest.txt").await()).await());
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.setFileContents("/dest.txt", new byte[] { 10, 11 }).await();
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
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo((Path)null, Path.parse("/dest.txt")), new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), (Path)null), new PreConditionFailure("destinationFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("when source file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await(),
                        new FileNotFoundException("/source.txt"));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await();
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.createFile("/source.txt").await();
                    fileSystem.setFileContents("/dest.txt", new byte[] { 10, 11 }).await();
                    test.assertNull(fileSystem.copyFileTo(Path.parse("/source.txt"), Path.parse("/dest.txt")).await());
                    test.assertTrue(fileSystem.fileExists("/source.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/source.txt").await());
                    test.assertTrue(fileSystem.fileExists("/dest.txt").await());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/dest.txt").await());
                });

                runner.test("when source file is not empty and destination file already exists", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContents("/source.txt", new byte[] { 0, 1, 2, 3 }).await();
                    fileSystem.setFileContents("/dest.txt", new byte[] { 10, 11 }).await();
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
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileToFolder((File)null, fileSystem.getFolder("/dest.txt").await()),
                        new PreConditionFailure("sourceFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(new PreConditionFailure("destinationFolder cannot be null."),
                        () -> fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), (Folder)null));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("with non-existing sourceFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(new FileNotFoundException("/source.txt"),
                        () -> fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), fileSystem.getFolder("/destination/").await()).await());
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.folderExists("/destination/").await());
                });

                runner.test("with non-existing destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });

                runner.test("with existing destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.createFolder("/destination/").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });

                runner.test("with existing file at destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.setFileContentsAsString("/destination/source.txt", "oops").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });
            });

            runner.testGroup("copyFileToFolder(Path,Folder)", () ->
            {
                runner.test("with null sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileToFolder((Path)null, fileSystem.getFolder("/dest.txt").await()),
                        new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with non-file sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileToFolder(Path.parse("/"), fileSystem.getFolder("/dest.txt").await()),
                        new PreConditionFailure("sourceFilePath.endsWith(\"/\") cannot be true."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(new PreConditionFailure("destinationFolder cannot be null."),
                        () -> fileSystem.copyFileToFolder(Path.parse("/source.txt"), (Folder)null));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("with non-existing sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(new FileNotFoundException("/source.txt"),
                        () -> fileSystem.copyFileToFolder(Path.parse("/source.txt"), fileSystem.getFolder("/destination/").await()).await());
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.folderExists("/destination/").await());
                });

                runner.test("with non-existing destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });

                runner.test("with existing destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.createFolder("/destination/").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });

                runner.test("with existing file at destinationFolder", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.setFileContentsAsString("/destination/source.txt", "oops").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), fileSystem.getFolder("/destination/").await()).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });
            });

            runner.testGroup("copyFileToFolder(File,Path)", () ->
            {
                runner.test("with null sourceFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileToFolder((File)null, Path.parse("/dest.txt")),
                        new PreConditionFailure("sourceFile cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(new PreConditionFailure("destinationFolderPath cannot be null."),
                        () -> fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), (Path)null));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("with non-existing sourceFile", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(new FileNotFoundException("/source.txt"),
                        () -> fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), Path.parse("/destination/")).await());
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.folderExists("/destination/").await());
                });

                runner.test("with non-existing destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });

                runner.test("with existing destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.createFolder("/destination/").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });

                runner.test("with existing file at destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.setFileContentsAsString("/destination/source.txt", "oops").await();
                    fileSystem.copyFileToFolder(fileSystem.getFile("/source.txt").await(), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });
            });

            runner.testGroup("copyFileToFolder(Path,Path)", () ->
            {
                runner.test("with null sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileToFolder((Path)null, Path.parse("/dest.txt")),
                        new PreConditionFailure("sourceFilePath cannot be null."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with non-file sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(() -> fileSystem.copyFileToFolder(Path.parse("/"), Path.parse("/dest.txt")),
                        new PreConditionFailure("sourceFilePath.endsWith(\"/\") cannot be true."));
                    test.assertFalse(fileSystem.fileExists("/dest.txt").await());
                });

                runner.test("with null destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(new PreConditionFailure("destinationFolderPath cannot be null."),
                        () -> fileSystem.copyFileToFolder(Path.parse("/source.txt"), (Path)null));
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                });

                runner.test("with non-existing sourceFilePath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    test.assertThrows(new FileNotFoundException("/source.txt"),
                        () -> fileSystem.copyFileToFolder(Path.parse("/source.txt"), Path.parse("/destination/")).await());
                    test.assertFalse(fileSystem.fileExists("/source.txt").await());
                    test.assertFalse(fileSystem.folderExists("/destination/").await());
                });

                runner.test("with non-existing destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });

                runner.test("with existing destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.createFolder("/destination/").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
                });

                runner.test("with existing file at destinationFolderPath", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(null);
                    fileSystem.setFileContentsAsString("/source.txt", "hello").await();
                    fileSystem.setFileContentsAsString("/destination/source.txt", "oops").await();
                    fileSystem.copyFileToFolder(Path.parse("/source.txt"), Path.parse("/destination/")).await();
                    test.assertTrue(fileSystem.folderExists("/destination/").await());
                    test.assertTrue(fileSystem.fileExists("/destination/source.txt").await());
                    test.assertEqual("hello", fileSystem.getFileContentsAsString("/destination/source.txt").await());
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
