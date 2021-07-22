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
                    final Folder folder = FolderTests.getFolder("/");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("\\"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder("\\");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("D:"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder("D:");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"D:/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("E:/"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder("E:/");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"E:/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("F:\\"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder("F:\\");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"F:/\" doesn't have a parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("/apples"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder("/apples");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder("/apples/");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/dates"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder("/apples/dates");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/apples/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/dates/"), (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder("/apples/dates/");
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
                        final Folder folder = FolderTests.getFolder(path);
                        test.assertThrows(() -> folder.relativeTo(basePath), expectedError);
                    });
                };

                relativeToErrorTests.run("/apples/grapes", "/apples/grapes", new PreConditionFailure("basePath (/apples/grapes) must not be /apples/grapes/."));
                relativeToErrorTests.run("/apples/grapes", "/apples/grapes/", new PreConditionFailure("basePath (/apples/grapes/) must not be /apples/grapes/."));

                final Action3<String,String,String> relativeToTests = (String path, String basePath, String expectedPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path) + " and " + Strings.escapeAndQuote(basePath), (Test test) ->
                    {
                        final Folder folder = FolderTests.getFolder(path);
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
                        final Folder folder = FolderTests.getFolder(path);
                        test.assertThrows(() -> folder.relativeTo(Path.parse(basePath)), expectedError);
                    });
                };

                relativeToErrorTests.run("/apples/grapes", "/apples/grapes", new PreConditionFailure("basePath (/apples/grapes) must not be /apples/grapes/."));
                relativeToErrorTests.run("/apples/grapes", "/apples/grapes/", new PreConditionFailure("basePath (/apples/grapes/) must not be /apples/grapes/."));

                final Action3<String,String,String> relativeToTests = (String path, String basePath, String expectedPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path) + " and " + Strings.escapeAndQuote(basePath), (Test test) ->
                    {
                        final Folder folder = FolderTests.getFolder(path);
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
                        final Folder folder = FolderTests.getFolder(path);
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
                        final Folder folder = FolderTests.getFolder(path);
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
                        final Folder folder = FolderTests.getFolder(path);
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
                    final Folder folder = FolderTests.getFolder();
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with folder exists", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    folder.create().await();
                    test.assertTrue(folder.exists().await());
                });
            });

            runner.testGroup("delete()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.delete().await(),
                        new FolderNotFoundException("/test/folder/"));
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    folder.create().await();

                    test.assertNull(folder.delete().await());
                    test.assertFalse(folder.exists().await());
                });
            });

            runner.testGroup("create()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertFalse(folder.exists().await());

                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
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
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.createFolder((String)null),
                        new PreConditionFailure("folderRelativePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.createFolder(""),
                        new PreConditionFailure("folderRelativePath cannot be empty."));
                });

                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();

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
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.createFolder((Path)null), new PreConditionFailure("relativeFolderPath cannot be null."));
                });

                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();

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
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.createFile((String)null), new PreConditionFailure("fileRelativePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.createFile(""), new PreConditionFailure("fileRelativePath cannot be empty."));
                });

                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();

                    final File file = folder.createFile("file.xml").await();
                    test.assertEqual("/test/folder/file.xml", file.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing relative path in subfolder with backslash separator", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();

                    final File file = folder.createFile("subfolder\\file.xml").await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing relative path in subfolder with forward slash separator", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();

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
                    final Folder folder = FolderTests.getFolder();

                    final File file = folder.createFile(Path.parse("file.xml")).await();
                    test.assertEqual("/test/folder/file.xml", file.toString());
                    test.assertTrue(folder.exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing subfolder relative path with backslash separator", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();

                    final File file = folder.createFile(Path.parse("subfolder\\file.xml")).await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());
                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing subfolder relative path with forward slash separator", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();

                    final File file = folder.createFile(Path.parse("subfolder/file.xml")).await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());
                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });
            });

            runner.testGroup("deleteFolder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFolder((String)null),
                        new PreConditionFailure("relativeFolderPath cannot be null."));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with empty", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFolder(""),
                        new PreConditionFailure("relativeFolderPath cannot be empty."));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with non-existing child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFolder("doesntExist").await(),
                        new FolderNotFoundException("/test/folder/doesntExist"));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with existing child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    final Folder childFolder = folder.createFolder("exists").await();

                    test.assertNull(folder.deleteFolder("exists").await());

                    test.assertTrue(folder.exists().await());
                    test.assertFalse(childFolder.exists().await());
                });
            });

            runner.testGroup("deleteFolder(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFolder((Path)null),
                        new PreConditionFailure("relativeFolderPath cannot be null."));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with non-existing child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFolder(Path.parse("doesntExist")).await(),
                        new FolderNotFoundException("/test/folder/doesntExist"));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with existing child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    final Folder childFolder = folder.createFolder("exists").await();

                    test.assertNull(folder.deleteFolder(Path.parse("exists")).await());

                    test.assertTrue(folder.exists().await());
                    test.assertFalse(childFolder.exists().await());
                });
            });

            runner.testGroup("deleteFile(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFile((String)null),
                        new PreConditionFailure("relativeFilePath cannot be null."));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with empty", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFile(""),
                        new PreConditionFailure("relativeFilePath cannot be empty."));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with non-existing child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFile("doesntExist").await(),
                        new FolderNotFoundException(folder));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with existing child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    final File childFile = folder.createFile("exists").await();

                    test.assertNull(folder.deleteFile("exists").await());

                    test.assertTrue(folder.exists().await());
                    test.assertFalse(childFile.exists().await());
                });
            });

            runner.testGroup("deleteFile(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFile((Path)null),
                        new PreConditionFailure("relativeFilePath cannot be null."));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with non-existing child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.deleteFile(Path.parse("doesntExist")).await(),
                        new FolderNotFoundException(folder));
                    test.assertFalse(folder.exists().await());
                });

                runner.test("with existing child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    final File childFile = folder.createFile("exists").await();

                    test.assertNull(folder.deleteFile(Path.parse("exists")).await());

                    test.assertTrue(folder.exists().await());
                    test.assertFalse(childFile.exists().await());
                });
            });

            runner.test("iterateFolders()", (Test test) ->
            {
                final Folder folder = FolderTests.getFolder();
                test.assertFalse(folder.exists().await());
                test.assertThrows(() -> folder.iterateFolders().toList(),
                    new FolderNotFoundException("/test/folder/"));

                test.assertNull(folder.create().await());
                test.assertTrue(folder.exists().await());
                test.assertEqual(Iterable.create(), folder.iterateFolders().toList());

                final Folder childFolder = folder.createFolder("childFolder1").await();
                test.assertEqual("/test/folder/childFolder1/", childFolder.toString());

                test.assertEqual(
                    Iterable.create(
                        "/test/folder/childFolder1/"),
                    folder.iterateFolders().map(Folder::toString).toList());

                test.assertEqual("/test/folder/childFolder1/grandchildFolder1/", childFolder.createFolder("grandchildFolder1").await().toString());
                test.assertEqual(
                    Iterable.create(
                        "/test/folder/childFolder1/"),
                    folder.iterateFolders().map(Folder::toString).toList());
            });

            runner.testGroup("iterateFiles()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertFalse(folder.exists().await());
                    test.assertThrows(() -> folder.iterateFiles().toList(),
                        new FolderNotFoundException("/test/folder/"));
                });

                runner.test("when folder exists but is empty", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                    test.assertEqual(Iterable.create(), folder.iterateFiles().toList());
                });

                runner.test("when folder exists and has one file", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                    test.assertEqual("/test/folder/data.txt", folder.createFile("data.txt").await().toString());
                    test.assertEqual(Iterable.create("/test/folder/data.txt"), folder.iterateFiles().map(File::toString).toList());
                });

                runner.test("when folder exists and has one grandchild file", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                    test.assertEqual("/test/folder/subfolder/data.txt", folder.createFile("subfolder/data.txt").await().toString());
                    test.assertEqual(Iterable.create(), folder.iterateFiles().toList());
                });
            });

            runner.testGroup("iterateEntries()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertFalse(folder.exists().await());
                    test.assertThrows(() -> folder.iterateEntries().toList(),
                        new FolderNotFoundException("/test/folder/"));
                });

                runner.test("when folder exists but is empty", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertNull(folder.create().await());
                    test.assertEqual(Iterable.create(), folder.iterateEntries().toList());
                });

                runner.test("when folder exists and has one child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertNull(folder.create().await());
                    test.assertEqual("/test/folder/subfolder/", folder.createFolder("subfolder").await().toString());
                    test.assertEqual(Iterable.create("/test/folder/subfolder/"), folder.iterateEntries().map(FileSystemEntry::toString).toList());
                });

                runner.test("when folder exists and has one child file", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertNull(folder.create().await());
                    folder.createFile("file.java").await();
                    test.assertEqual(Iterable.create("/test/folder/file.java"), folder.iterateEntries().map(FileSystemEntry::toString).toList());
                });

                runner.test("when folder exists and has one child file and one child folder", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertNull(folder.create().await());
                    folder.createFile("file.java").await();
                    folder.createFolder("childfolder").await();
                    test.assertEqual(
                        Iterable.create(
                            "/test/folder/childfolder/",
                            "/test/folder/file.java"),
                        folder.iterateEntries().map(FileSystemEntry::toString).toList());
                });
            });

            runner.testGroup("fileExists(String)", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertFalse(folder.fileExists("test.txt").await());
                });

                runner.test("when file exists", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    folder.createFile("test.txt").await();
                    test.assertTrue(folder.fileExists("test.txt").await());
                });
            });

            runner.testGroup("getFileContentReadStream(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.getFileContentReadStream((String)null),
                        new PreConditionFailure("relativeFilePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.getFileContentReadStream(""),
                        new PreConditionFailure("relativeFilePath cannot be empty."));
                });

                runner.test("with rooted file path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.getFileContentReadStream("/test.txt"),
                        new PreConditionFailure("relativeFilePath.isRooted() cannot be true."));
                });

                runner.test("with relative folder path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.getFileContentReadStream("test.txt/"),
                        new PreConditionFailure("relativeFilePath.endsWith(\"/\") || relativeFilePath.endsWith(\"\\\\\") cannot be true."));
                });

                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.getFileContentReadStream("test.txt").await(),
                        new FileNotFoundException("/test/folder/test.txt"));
                });

                runner.test("when file doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    folder.create().await();
                    test.assertThrows(() -> folder.getFileContentReadStream("test.txt").await(),
                        new FileNotFoundException("/test/folder/test.txt"));
                });

                runner.test("when file doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    folder.create().await();
                    folder.setFileContentsAsString("test.txt", "hello there").await();

                    try (final CharacterToByteReadStream stream = folder.getFileContentReadStream("test.txt").await())
                    {
                        test.assertEqual("hello there", stream.readEntireString().await());
                    }
                });
            });

            runner.testGroup("getFileContentReadStream(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.getFileContentReadStream((Path)null),
                        new PreConditionFailure("relativeFilePath cannot be null."));
                });

                runner.test("with rooted file path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.getFileContentReadStream(Path.parse("/test.txt")),
                        new PreConditionFailure("relativeFilePath.isRooted() cannot be true."));
                });

                runner.test("with relative folder path", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.getFileContentReadStream(Path.parse("test.txt/")),
                        new PreConditionFailure("relativeFilePath.endsWith(\"/\") || relativeFilePath.endsWith(\"\\\\\") cannot be true."));
                });

                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertThrows(() -> folder.getFileContentReadStream(Path.parse("test.txt")).await(),
                        new FileNotFoundException("/test/folder/test.txt"));
                });

                runner.test("when file doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    folder.create().await();
                    test.assertThrows(() -> folder.getFileContentReadStream(Path.parse("test.txt")).await(),
                        new FileNotFoundException("/test/folder/test.txt"));
                });

                runner.test("when file doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    folder.create().await();
                    folder.setFileContentsAsString("test.txt", "hello there").await();

                    try (final CharacterToByteReadStream stream = folder.getFileContentReadStream(Path.parse("test.txt")).await())
                    {
                        test.assertEqual("hello there", stream.readEntireString().await());
                    }
                });
            });

            runner.testGroup("folderExists(String)", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    test.assertFalse(folder.folderExists("test").await());
                });

                runner.test("when folder does exist", (Test test) ->
                {
                    final Folder folder = FolderTests.getFolder();
                    folder.createFolder("test").await();
                    test.assertTrue(folder.folderExists("test").await());
                });
            });
        });
    }

    static Folder getFolder()
    {
        return FolderTests.getFolder("/test/folder");
    }

    static Folder getFolder(String folderPath)
    {
        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
        fileSystem.createRoot("/").await();
        return fileSystem.getFolder(folderPath).await();
    }
}
