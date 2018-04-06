package qub;

public class FileSystemTests
{
    public static void test(TestRunner runner, Function1<AsyncRunner,FileSystem> creator)
    {
        runner.testGroup(FileSystem.class, () ->
        {
            runner.testGroup("rootExists(String)", () ->
            {
                final Action3<String,Boolean,Throwable> rootExistsAsyncTest = (String rootPath, Boolean expectedValue, Throwable expectedError) ->
                {
                    runner.test("with " + (rootPath == null ? null : "\"" + rootPath + "\""), (Test test) ->
                    {
                        final AsyncRunner mainAsyncRunner = test.getMainAsyncRunner();
                        test.assertEqual(0, mainAsyncRunner.getScheduledTaskCount());
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertEqual(0, mainAsyncRunner.getScheduledTaskCount());
                        final Result<Boolean> result = fileSystem.rootExists(rootPath).awaitReturn();
                        test.assertEqual(0, mainAsyncRunner.getScheduledTaskCount());
                        test.assertNotNull(result);
                        test.assertEqual(expectedValue, result.getValue());
                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertEqual(expectedError.getClass(), result.getErrorType());
                            test.assertEqual(expectedError.getMessage(), result.getErrorMessage());
                        }
                    });
                };

                rootExistsAsyncTest.run(null, null, new IllegalArgumentException("rootPath cannot be null."));
                rootExistsAsyncTest.run("", null, new IllegalArgumentException("rootPath cannot be null."));
                rootExistsAsyncTest.run("notme:\\", false, null);
                rootExistsAsyncTest.run("/", true, null);
            });

            runner.testGroup("rootExists(Path)", () ->
            {
                final Action3<String,Boolean,Throwable> rootExistsTest = (String rootPath, Boolean expectedValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<Boolean> result = fileSystem.rootExists(Path.parse(rootPath)).awaitReturn();
                        test.assertNotNull(result);
                        test.assertEqual(expectedValue, result.getValue());
                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertEqual(expectedError.getClass(), result.getErrorType());
                            test.assertEqual(expectedError.getMessage(), result.getErrorMessage());
                        }
                    });
                };

                rootExistsTest.run(null, null, new IllegalArgumentException("rootPath cannot be null."));
                rootExistsTest.run("", null, new IllegalArgumentException("rootPath cannot be null."));
                rootExistsTest.run("notme:\\", false, null);
                rootExistsTest.run("/", true, null);
            });

            runner.test("getRoot()", (Test test) ->
            {
                FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                final Root root1 = fileSystem.getRoot("/daffy/").getValue();
                test.assertFalse(root1.exists().awaitReturn().getValue());
                test.assertEqual("/daffy/", root1.toString());
            });

            runner.test("getRoots()", (Test test) ->
            {
                FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                final Iterable<Root> roots = fileSystem.getRoots().awaitReturn().getValue();
                test.assertNotNull(roots);
                test.assertTrue(1 <= roots.getCount());
            });

            runner.testGroup("getFilesAndFolders(String)", () ->
            {
                final Action4<String, Action1<FileSystem>, String[], Throwable> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFolders(folderPath).awaitReturn();
                        test.assertNotNull(result);

                        if (expectedEntryPaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Array.fromValues(expectedEntryPaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertEqual(expectedError.getClass(), result.getErrorType());
                            test.assertEqual(expectedError.getMessage(), result.getErrorMessage());
                        }
                    });
                };

                getFilesAndFoldersTest.run(null, null, null, new IllegalArgumentException("folderPath cannot be null."));
                getFilesAndFoldersTest.run("", null, null, new IllegalArgumentException("folderPath cannot be null."));
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
            });

            runner.testGroup("getFilesAndFolders(Path)", () ->
            {
                final Action4<String, Action1<FileSystem>, String[], Throwable> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFolders(Path.parse(folderPath)).awaitReturn();
                        test.assertNotNull(result);

                        if (expectedEntryPaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Array.fromValues(expectedEntryPaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertEqual(expectedError.getClass(), result.getErrorType());
                            test.assertEqual(expectedError.getMessage(), result.getErrorMessage());
                        }
                    });
                };

                getFilesAndFoldersTest.run(null, null, null, new IllegalArgumentException("folderPath cannot be null."));
                getFilesAndFoldersTest.run("", null, null, new IllegalArgumentException("folderPath cannot be null."));
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
            });

            runner.testGroup("getFolders(String)", () ->
            {
                final Action1<String> getFoldersTest = (String path) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertEqual(new Array<Folder>(0), fileSystem.getFolders(path).awaitReturn().getValue());
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

                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(Array.fromValues(expectedFolderPaths), fileSystem.getFoldersRecursively(folderPath).awaitReturn().getValue().map(Folder::toString));
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

            runner.testGroup("getFiles(String)", () ->
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
                        test.assertEqual(Array.fromValues(expectedFilePaths), fileSystem.getFiles(folderPath).awaitReturn().getValue().map(File::toString));
                    });
                };

                getFilesAsyncTest.run(null, null, new String[0]);
                getFilesAsyncTest.run("", null, new String[0]);
            });

            runner.testGroup("getFiles(Path)", () ->
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
                        test.assertEqual(Array.fromValues(expectedFilePaths), fileSystem.getFiles(Path.parse(folderPath)).awaitReturn().getValue().map(File::toString));
                    });
                };

                getFilesAsyncTest.run(null, null, new String[0]);
                getFilesAsyncTest.run("", null, new String[0]);
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
                        test.assertEqual(Array.fromValues(expectedFilePaths), fileSystem.getFilesRecursively(folderPath).awaitReturn().getValue().map(File::toString));
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
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Folder folder = fileSystem.getFolder(folderPath).getValue();
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
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expectedFolderExists, fileSystem.folderExists(folderPath).awaitReturn().getValue());
                    });
                };

                folderExistsTest.run(null, null, false);
                folderExistsTest.run("", null, false);
                folderExistsTest.run("/", null, true);
                folderExistsTest.run("/folderName", null, false);
                folderExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    true);
            });

            runner.testGroup("folderExists(String)", () ->
            {
                final Action3<String,Action1<FileSystem>,Boolean> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        test.assertEqual(expectedFolderExists, fileSystem.folderExists(Path.parse(folderPath)).awaitReturn().getValue());
                    });
                };

                folderExistsTest.run(null, null, false);
                folderExistsTest.run("", null, false);
                folderExistsTest.run("/", null, true);
                folderExistsTest.run("/folderName", null, false);
                folderExistsTest.run(
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
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
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

            runner.testGroup("createFolder(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Folder> result = fileSystem.createFolder((Path)null).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFolderPath cannot be null.", result.getErrorMessage());
                });
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
                        final Result<Boolean> result = fileSystem.deleteFolder(folderPath).awaitReturn();
                        test.assertNotNull(result);
                        test.assertEqual(expectedDeleteResult, result.getValue());
                        test.assertEqual(!expectedDeleteResult, result.hasError());
                    });
                };

                deleteFolderTest.run(null, null, false);
                deleteFolderTest.run("", null, false);
                deleteFolderTest.run("folder", null, false);
                deleteFolderTest.run("/folder", null, false);
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder").await(),
                    true);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a").await();
                        fileSystem.createFolder("/folder/b").await();
                        fileSystem.createFolder("/folder/c").await();
                    },
                    true);
            });

            runner.testGroup("deleteFolder(Path)", () ->
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
                        final Result<Boolean> result = fileSystem.deleteFolder(Path.parse(folderPath)).awaitReturn();
                        test.assertNotNull(result);
                        test.assertEqual(expectedDeleteResult, result.getValue());
                        test.assertEqual(!expectedDeleteResult, result.hasError());
                    });
                };

                deleteFolderTest.run(null, null, false);
                deleteFolderTest.run("", null, false);
                deleteFolderTest.run("folder", null, false);
                deleteFolderTest.run("/folder", null, false);
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder").await(),
                    true);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a").await();
                        fileSystem.createFolder("/folder/b").await();
                        fileSystem.createFolder("/folder/c").await();
                    },
                    true);
            });

            runner.testGroup("getFile(String)", () ->
            {
                final Action2<String,Boolean> getFileTest = (String filePath, Boolean fileExpected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<File> file = fileSystem.getFile(filePath);
                        test.assertNotNull(file);
                        test.assertEqual(fileExpected, file.getValue() != null);
                        test.assertEqual(!fileExpected, file.hasError());
                        if (fileExpected)
                        {
                            test.assertEqual(filePath, file.getValue().toString());
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
                        test.assertEqual(expectedFileExists, fileSystem.fileExists(filePath).awaitReturn().getValue());
                    });
                };

                fileExistsTest.run("/", null, false);
                fileExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName").await(),
                    false);
                fileExistsTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName").await(),
                    true);
            });

            runner.testGroup("fileAsync(Path)", () ->
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
                        test.assertEqual(expectedFileExists, fileSystem.fileExists(Path.parse(filePath)).awaitReturn().getValue());
                    });
                };

                fileExistsAsyncTest.run("/", null, false);
                fileExistsAsyncTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName").await(),
                    false);
                fileExistsAsyncTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName").await(),
                    true);
            });

            runner.testGroup("createFile(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<File> result = fileSystem.createFile((String)null).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<File> result = fileSystem.createFile("").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<File> result = fileSystem.createFile("things.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath must be rooted.", result.getErrorMessage());
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<File> result = fileSystem.createFile("/things.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/things.txt", result.getValue().toString());
                    test.assertFalse(result.hasError());
                    test.assertEqual(new byte[0], result.getValue().getContents().awaitReturn().getValue());
                });

                runner.test("with existing rooted path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/things.txt").await();

                    final Result<File> result = fileSystem.createFile("/things.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertEqual("/things.txt", result.getValue().toString());
                    test.assertTrue(result.hasError());
                    test.assertEqual(FileAlreadyExistsException.class, result.getErrorType());
                    test.assertEqual("The file at \"/things.txt\" already exists.", result.getErrorMessage());
                    test.assertEqual(new byte[0], result.getValue().getContents().awaitReturn().getValue());
                });

                runner.test("with invalid path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<File> result = fileSystem.createFile("/\u0000?#!.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                });
            });

            runner.testGroup("createFile(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<File> result = fileSystem.createFile((Path)null).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<File> result = fileSystem.createFile(Path.parse("")).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });
            });

            runner.testGroup("deleteFile(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.deleteFile((String)null).awaitReturn();
                    test.assertNotNull(result);
                    test.assertFalse(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null or empty.", result.getErrorMessage());
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.deleteFile("").awaitReturn();
                    test.assertNotNull(result);
                    test.assertFalse(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null or empty.", result.getErrorMessage());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.deleteFile("relativeFile.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertFalse(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath must be rooted.", result.getErrorMessage());
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.deleteFile("/idontexist.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertFalse(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(FileNotFoundException.class, result.getErrorType());
                    test.assertEqual("The file \"/idontexist.txt\" does not exist.", result.getErrorMessage());
                });

                runner.test("with existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/iexist.txt").await();

                    final Result<Boolean> result1 = fileSystem.deleteFile("/iexist.txt").awaitReturn();
                    test.assertNotNull(result1);
                    test.assertTrue(result1.getValue());
                    test.assertFalse(result1.hasError());
                    test.assertFalse(fileSystem.fileExists("/iexist.txt").awaitReturn().getValue());

                    final Result<Boolean> result2 = fileSystem.deleteFile("/iexist.txt").awaitReturn();
                    test.assertNotNull(result2);
                    test.assertFalse(result2.getValue());
                    test.assertTrue(result2.hasError());
                    test.assertEqual(FileNotFoundException.class, result2.getErrorType());
                    test.assertEqual("The file \"/iexist.txt\" does not exist.", result2.getErrorMessage());
                });
            });

            runner.testGroup("getFileLastModified(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<DateTime> result = fileSystem.getFileLastModified((String)null).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<DateTime> result = fileSystem.getFileLastModified("").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be empty.", result.getErrorMessage());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<DateTime> result = fileSystem.getFileLastModified("thing.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath must be rooted.", result.getErrorMessage());
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<DateTime> result = fileSystem.getFileLastModified("/thing.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(FileNotFoundException.class, result.getErrorType());
                    test.assertEqual("The file \"/thing.txt\" does not exist.", result.getErrorMessage());
                });

                runner.test("with existing rooted path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt").await();

                    final Result<DateTime> result = fileSystem.getFileLastModified("/thing.txt").awaitReturn();
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
                    final Result<byte[]> result = fileSystem.getFileContent((String)null).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("thing.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath must be rooted.", result.getErrorMessage());
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("/thing.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(FileNotFoundException.class, result.getErrorType());
                    test.assertEqual("The file \"/thing.txt\" does not exist.", result.getErrorMessage());
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt").await();

                    final Result<byte[]> result = fileSystem.getFileContent("/thing.txt").awaitReturn();
                    test.assertNotNull(result);
                    test.assertEqual(new byte[0], result.getValue());
                    test.assertFalse(result.hasError());
                });
            });

            runner.testGroup("getFileContent(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent((Path)null).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse("")).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse("thing.txt")).awaitReturn();
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath must be rooted.", result.getErrorMessage());
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse("/thing.txt")).awaitReturn();
                    test.assertError(new FileNotFoundException("/thing.txt"), result);
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt").await();

                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse("/thing.txt")).awaitReturn();
                    test.assertSuccess(new byte[0], result);
                });
            });

            runner.testGroup("getFileContentByteReadStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<ByteReadStream> result = fileSystem.getFileContentByteReadStream("C:/i/dont/exist.txt").awaitReturn();
                    test.assertError(new FileNotFoundException("C:/i/dont/exist.txt"), result);
                });
            });

            runner.testGroup("setFileContent(String,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.setFileContent((String)null, new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertError(new IllegalArgumentException("rootedFilePath cannot be null."), result);
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.setFileContent("", new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertError(new IllegalArgumentException("rootedFilePath cannot be empty."), result);
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.setFileContent("relative.file", new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertError(new IllegalArgumentException("rootedFilePath must be rooted."), result);
                });

                runner.test("with non-existing rooted path with null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", null).awaitReturn();
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }).await();
                    
                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", null).awaitReturn();
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", new byte[0]).awaitReturn();
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt"));
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });

                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", new byte[0]).awaitReturn();
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent("/folder/A.txt", new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.folderExists("/folder").awaitReturn().getValue());
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/folder/A.txt").awaitReturn().getValue());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/A.txt");

                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });
            });

            runner.testGroup("setFileContent(Path,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent((Path)null, new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertError(new IllegalArgumentException("rootedFilePath cannot be null."), result);
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse(""), new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertError(new IllegalArgumentException("rootedFilePath cannot be empty."), result);
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("relative.file"), new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertError(new IllegalArgumentException("rootedFilePath must be rooted."), result);
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), null).awaitReturn();
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 }).await();

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), (byte[])null).awaitReturn();
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]).awaitReturn();
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]).awaitReturn();
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/A.txt").await();

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 }).awaitReturn();
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").awaitReturn().getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").awaitReturn().getValue());
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively((String)null).awaitReturn();
                    test.assertError(new IllegalArgumentException("rootedFolderPath cannot be null."), result);
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("").awaitReturn();
                    test.assertError(new IllegalArgumentException("rootedFolderPath cannot be empty."), result);
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("test/folder").awaitReturn();
                    test.assertError(new IllegalArgumentException("rootedFolderPath must be rooted."), result);
                });

                runner.test("with rooted path when root doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("F:/test/folder").awaitReturn();
                    test.assertError(new RootNotFoundException("F:/"), result);
                });

                runner.test("with rooted path when parent folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder").awaitReturn();
                    test.assertError(new FolderNotFoundException("/test/folder"), result);
                });

                runner.test("with rooted path when folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/").await();

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder").awaitReturn();
                    test.assertError(new FolderNotFoundException("/test/folder"), result);
                });

                runner.test("with rooted path when folder is empty", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder").await();

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder").awaitReturn();
                    test.assertSuccess(new Array<>(0), result);
                });

                runner.test("with rooted path when folder has files", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder").await();
                    fileSystem.createFile("/test/folder/1.txt").await();
                    fileSystem.createFile("/test/folder/2.txt").await();

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder").awaitReturn();
                    test.assertSuccess(Array.fromValues(new FileSystemEntry[]
                        {
                            fileSystem.getFile("/test/folder/1.txt").getValue(),
                            fileSystem.getFile("/test/folder/2.txt").getValue()
                        }),
                        result);
                });

                runner.test("with rooted path when folder has folders", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder/1.txt").await();
                    fileSystem.createFolder("/test/folder/2.txt").await();

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder").awaitReturn();
                    test.assertSuccess(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            fileSystem.getFolder("/test/folder/1.txt").getValue(),
                            fileSystem.getFolder("/test/folder/2.txt").getValue()
                        }),
                        result);
                });

                runner.test("with rooted path when folder has grandchild files and folders", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/test/folder/1.txt").await();
                    fileSystem.createFile("/test/folder/2.txt").await();
                    fileSystem.createFile("/test/folder/A/3.csv").await();
                    fileSystem.createFile("/test/folder/B/C/4.xml").await();
                    fileSystem.createFile("/test/folder/A/5.png").await();

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder").awaitReturn();
                    test.assertSuccess(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            fileSystem.getFolder("/test/folder/A").getValue(),
                            fileSystem.getFolder("/test/folder/B").getValue(),
                            fileSystem.getFile("/test/folder/1.txt").getValue(),
                            fileSystem.getFile("/test/folder/2.txt").getValue(),
                            fileSystem.getFile("/test/folder/A/3.csv").getValue(),
                            fileSystem.getFile("/test/folder/A/5.png").getValue(),
                            fileSystem.getFolder("/test/folder/B/C").getValue(),
                            fileSystem.getFile("/test/folder/B/C/4.xml").getValue()
                        }),
                        result);
                });
            });
        });
    }
}
