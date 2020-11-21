package qub;

public interface FolderTests
{
    static void test(final TestRunner runner)
    {
        runner.testGroup(Folder.class, () ->
        {
            FileSystemEntryTests.test(runner, FolderTests::getFolder);

            runner.testGroup("getParentFolder()", () ->
            {
                runner.test("with " + Strings.escapeAndQuote("/"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test, "/");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("\\"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test, "\\");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("D:"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test, "D:");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"D:/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("E:/"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test, "E:/");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"E:/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("F:\\"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test, "F:\\");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"F:/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("/apples"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test, "/apples");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test, "/apples/");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/dates"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test, "/apples/dates");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/apples/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/dates/"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test, "/apples/dates/");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/apples/", parentFolder.toString());
                });
            });

            runner.testGroup("relativeTo(String)", () ->
            {
                final Action3<String,String,Throwable> relativeToErrorTests = (String path, String basePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path) + " and " + Strings.escapeAndQuote(basePath), (Test test) ->
                    {
                        final Folder folder = FolderTests.getFolder(test, path);
                        test.assertThrows(() -> folder.relativeTo(basePath), expectedError);
                    });
                };

                relativeToErrorTests.run("/apples/grapes", "/apples/grapes", new PreConditionFailure("basePath (/apples/grapes) must not be /apples/grapes/."));
                relativeToErrorTests.run("/apples/grapes", "/apples/grapes/", new PreConditionFailure("basePath (/apples/grapes/) must not be /apples/grapes/."));

                final Action3<String,String,String> relativeToTests = (String path, String basePath, String expectedPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path) + " and " + Strings.escapeAndQuote(basePath), (Test test) ->
                    {
                        final Folder folder = FolderTests.getFolder(test, path);
                        test.assertEqual(expectedPath, folder.relativeTo(basePath).toString());
                    });
                };

                relativeToTests.run("/apples/grapes", "/apples", "grapes");
                relativeToTests.run("/apples/grapes", "/", "apples/grapes");
                relativeToTests.run("/apples/grapes", "/apples/grapes/cherries", "..");
                relativeToTests.run("/apples/grapes", "/apples/grapes/cherries/", "..");
                relativeToTests.run("/apples/grapes", "/apples/grapes/cherries/bananas", "../..");
                relativeToTests.run("/apples/grapes", "/apples/cherries/bananas", "../../grapes");
            });

            runner.testGroup("relativeTo(Path)", () ->
            {
                final Action3<String,String,Throwable> relativeToErrorTests = (String path, String basePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path) + " and " + Strings.escapeAndQuote(basePath), (Test test) ->
                    {
                        final Folder folder = FolderTests.getFolder(test, path);
                        test.assertThrows(() -> folder.relativeTo(Path.parse(basePath)), expectedError);
                    });
                };

                relativeToErrorTests.run("/apples/grapes", "/apples/grapes", new PreConditionFailure("basePath (/apples/grapes) must not be /apples/grapes/."));
                relativeToErrorTests.run("/apples/grapes", "/apples/grapes/", new PreConditionFailure("basePath (/apples/grapes/) must not be /apples/grapes/."));

                final Action3<String,String,String> relativeToTests = (String path, String basePath, String expectedPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path) + " and " + Strings.escapeAndQuote(basePath), (Test test) ->
                    {
                        final Folder folder = FolderTests.getFolder(test, path);
                        test.assertEqual(expectedPath, folder.relativeTo(Path.parse(basePath)).toString());
                    });
                };

                relativeToTests.run("/apples/grapes", "/apples", "grapes");
                relativeToTests.run("/apples/grapes", "/", "apples/grapes");
                relativeToTests.run("/apples/grapes", "/apples/grapes/cherries", "..");
                relativeToTests.run("/apples/grapes", "/apples/grapes/cherries/", "..");
                relativeToTests.run("/apples/grapes", "/apples/grapes/cherries/bananas", "../..");
                relativeToTests.run("/apples/grapes", "/apples/cherries/bananas", "../../grapes");
            });

            runner.testGroup("relativeTo(Folder)", () ->
            {
                final Action3<String,String,Throwable> relativeToErrorTests = (String path, String basePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path) + " and " + Strings.escapeAndQuote(basePath), (Test test) ->
                    {
                        final Folder folder = FolderTests.getFolder(test, path);
                        final FileSystem fileSystem = folder.getFileSystem();
                        final Folder baseFolder = fileSystem.getFolder(basePath).await();
                        test.assertThrows(() -> folder.relativeTo(baseFolder), expectedError);
                    });
                };

                relativeToErrorTests.run("/apples/grapes", "/apples/grapes", new PreConditionFailure("basePath (/apples/grapes/) must not be /apples/grapes/."));
                relativeToErrorTests.run("/apples/grapes", "/apples/grapes/", new PreConditionFailure("basePath (/apples/grapes/) must not be /apples/grapes/."));

                final Action3<String,String,String> relativeToTests = (String path, String basePath, String expectedPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path) + " and " + Strings.escapeAndQuote(basePath), (Test test) ->
                    {
                        final Folder folder = FolderTests.getFolder(test, path);
                        final FileSystem fileSystem = folder.getFileSystem();
                        final Folder baseFolder = fileSystem.getFolder(basePath).await();
                        test.assertEqual(expectedPath, folder.relativeTo(baseFolder).toString());
                    });
                };

                relativeToTests.run("/apples/grapes", "/apples", "grapes");
                relativeToTests.run("/apples/grapes", "/", "apples/grapes");
                relativeToTests.run("/apples/grapes", "/apples/grapes/cherries", "..");
                relativeToTests.run("/apples/grapes", "/apples/grapes/cherries/", "..");
                relativeToTests.run("/apples/grapes", "/apples/grapes/cherries/bananas", "../..");
                relativeToTests.run("/apples/grapes", "/apples/cherries/bananas", "../../grapes");
            });

            runner.testGroup("relativeTo(Root)", () ->
            {
                final Action3<String,String,String> relativeToTests = (String path, String basePath, String expectedPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path) + " and " + Strings.escapeAndQuote(basePath), (Test test) ->
                    {
                        final Folder folder = FolderTests.getFolder(test, path);
                        final Root root = folder.getFileSystem().getRoot(basePath).await();
                        test.assertEqual(expectedPath, folder.relativeTo(root).toString());
                    });
                };

                relativeToTests.run("/apples/grapes", "/", "apples/grapes");
            });

            runner.testGroup("exists()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with folder exists", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    folder.create().await();
                    test.assertTrue(folder.exists().await());
                });
            });

            runner.testGroup("delete()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertThrows(() -> folder.delete().await(),
                        new FolderNotFoundException("/test/folder/"));
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    folder.create().await();

                    test.assertNull(folder.delete().await());
                    test.assertFalse(folder.exists().await());
                });
            });

            runner.testGroup("create()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertFalse(folder.exists().await());

                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    folder.create().await();

                    test.assertThrows(() -> folder.create().await(),
                        new FolderAlreadyExistsException(folder.getPath()));
                    test.assertTrue(folder.exists().await());
                });
            });

            runner.testGroup("createFolder(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertThrows(() -> folder.createFolder((String)null),
                        new PreConditionFailure("folderRelativePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertThrows(() -> folder.createFolder(""),
                        new PreConditionFailure("folderRelativePath cannot be empty."));
                });

                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);

                    final Folder createdFolder = folder.createFolder("thing").await();
                    test.assertEqual("/test/folder/thing/", createdFolder.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(createdFolder.exists().await());
                });
            });

            runner.testGroup("createFolder(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertThrows(() -> folder.createFolder((Path)null), new PreConditionFailure("relativeFolderPath cannot be null."));
                });

                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);

                    final Folder createdFolder = folder.createFolder("place").await();
                    test.assertEqual("/test/folder/place/", createdFolder.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(createdFolder.exists().await());
                });
            });

            runner.testGroup("createFile(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertThrows(() -> folder.createFile((String)null), new PreConditionFailure("fileRelativePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertThrows(() -> folder.createFile(""), new PreConditionFailure("fileRelativePath cannot be empty."));
                });

                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);

                    final File file = folder.createFile("file.xml").await();
                    test.assertEqual("/test/folder/file.xml", file.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing relative path in subfolder with backslash separator", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);

                    final File file = folder.createFile("subfolder\\file.xml").await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing relative path in subfolder with forward slash separator", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);

                    final File file = folder.createFile("subfolder/file.xml").await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });
            });

            runner.testGroup("createFile(Path)", () ->
            {
                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);

                    final File file = folder.createFile(Path.parse("file.xml")).await();
                    test.assertEqual("/test/folder/file.xml", file.toString());
                    test.assertTrue(folder.exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing subfolder relative path with backslash separator", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);

                    final File file = folder.createFile(Path.parse("subfolder\\file.xml")).await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());
                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing subfolder relative path with forward slash separator", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);

                    final File file = folder.createFile(Path.parse("subfolder/file.xml")).await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());
                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });
            });

            runner.test("getFolders()", (Test test) ->
            {
                final Folder folder = FolderTests.getFolder(test);
                test.assertFalse(folder.exists().await());
                test.assertThrows(() -> folder.getFolders().await(),
                    new FolderNotFoundException("/test/folder/"));

                test.assertNull(folder.create().await());
                test.assertTrue(folder.exists().await());
                test.assertEqual(Iterable.create(), folder.getFolders().await());

                final Folder childFolder = folder.createFolder("childFolder1").await();
                test.assertEqual("/test/folder/childFolder1/", childFolder.toString());

                test.assertEqual(Iterable.create("/test/folder/childFolder1/"), folder.getFolders().await().map(Folder::toString));

                test.assertEqual("/test/folder/childFolder1/grandchildFolder1/", childFolder.createFolder("grandchildFolder1").await().toString());
                test.assertEqual(Iterable.create("/test/folder/childFolder1/"), folder.getFolders().await().map(Folder::toString));
            });

            runner.testGroup("getFiles()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertFalse(folder.exists().await());
                    test.assertThrows(() -> folder.getFiles().await(), new FolderNotFoundException("/test/folder/"));
                });

                runner.test("when folder exists but is empty", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                    test.assertEqual(Iterable.create(), folder.getFiles().await());
                });

                runner.test("when folder exists and has one file", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                    test.assertEqual("/test/folder/data.txt", folder.createFile("data.txt").await().toString());
                    test.assertEqual(Iterable.create("/test/folder/data.txt"), folder.getFiles().await().map(File::toString));
                });

                runner.test("when folder exists and has one grandchild file", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                    test.assertEqual("/test/folder/subfolder/data.txt", folder.createFile("subfolder/data.txt").await().toString());
                    test.assertEqual(Iterable.create(), folder.getFiles().await());
                });
            });

            runner.testGroup("getFilesAndFolders()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertFalse(folder.exists().await());
                    test.assertThrows(() -> folder.getFilesAndFolders().await(),
                        new FolderNotFoundException("/test/folder/"));
                });

                runner.test("when folder exists but is empty", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertNull(folder.create().await());
                    test.assertEqual(Iterable.create(), folder.getFilesAndFolders().await());
                });

                runner.test("when folder exists and has one child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertNull(folder.create().await());
                    test.assertEqual("/test/folder/subfolder/", folder.createFolder("subfolder").await().toString());
                    test.assertEqual(Iterable.create("/test/folder/subfolder/"), folder.getFilesAndFolders().await().map(FileSystemEntry::toString));
                });

                runner.test("when folder exists and has one child file", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertNull(folder.create().await());
                    folder.createFile("file.java").await();
                    test.assertEqual(Iterable.create("/test/folder/file.java"), folder.getFilesAndFolders().await().map(FileSystemEntry::toString));
                });

                runner.test("when folder exists and has one child file and one child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertNull(folder.create().await());
                    folder.createFile("file.java").await();
                    folder.createFolder("childfolder").await();
                    test.assertEqual(
                        Iterable.create(
                            "/test/folder/childfolder/",
                            "/test/folder/file.java"),
                        folder.getFilesAndFolders().await().map(FileSystemEntry::toString));
                });
            });

            runner.testGroup("fileExists(String)", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertFalse(folder.fileExists("test.txt").await());
                });

                runner.test("when file exists", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    folder.createFile("test.txt").await();
                    test.assertTrue(folder.fileExists("test.txt").await());
                });
            });

            runner.testGroup("folderExists(String)", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    test.assertFalse(folder.folderExists("test").await());
                });

                runner.test("when folder does exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder(test);
                    folder.createFolder("test").await();
                    test.assertTrue(folder.folderExists("test").await());
                });
            });
        });
    }

    static Folder getFolder(Test test)
    {
        return FolderTests.getFolder(test, "/test/folder");
    }

    static Folder getFolder(Test test, String folderPath)
    {
        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
        fileSystem.createRoot("/").await();

        return fileSystem.getFolder(folderPath).await();
    }
}
