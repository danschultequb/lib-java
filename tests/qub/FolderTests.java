package qub;

public class FolderTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(Folder.class, () ->
        {
            runner.testGroup("getParentFolder()", () ->
            {
                runner.test("with " + Strings.escapeAndQuote("/"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "/");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"/\" has no parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("\\"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "\\");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"/\" has no parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("D:"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "D:");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"D:/\" has no parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("E:/"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "E:/");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"E:/\" has no parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("F:\\"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "F:\\");
                    test.assertThrows(() -> folder.getParentFolder().await(),
                        new NotFoundException("The path \"F:/\" has no parent folder."));
                });

                runner.test("with " + Strings.escapeAndQuote("/apples"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/dates"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/dates");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/apples", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/dates/"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/dates/");
                    final Folder parentFolder = folder.getParentFolder().await();
                    test.assertEqual("/apples", parentFolder.toString());
                });
            });

            runner.test("exists()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                
                test.assertFalse(folder.exists().await());
                
                folder.create().await();
                
                test.assertTrue(folder.exists().await());
            });


            runner.testGroup("delete()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.delete().await(),
                        new FolderNotFoundException("/test/folder"));
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    folder.create().await();

                    test.assertNull(folder.delete().await());
                    test.assertFalse(folder.exists().await());
                });
            });

            runner.testGroup("relativeTo(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.relativeTo((String)null), new PreConditionFailure("basePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.relativeTo(""), new PreConditionFailure("basePath cannot be empty."));
                });

                runner.test("with /apples/grapes relative to /apples", (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/grapes");
                    final Path relativePath = folder.relativeTo("/apples");
                    test.assertEqual(Path.parse("grapes"), relativePath);
                });

                runner.test("with /apples/grapes relative to /", (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/grapes");
                    final Path relativePath = folder.relativeTo("/");
                    test.assertEqual(Path.parse("apples/grapes"), relativePath);
                });
            });

            runner.testGroup("relativeTo(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.relativeTo((Path)null), new PreConditionFailure("basePath cannot be null."));
                });

                runner.test("with /apples/grapes relative to /apples", (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/grapes");
                    final Path relativePath = folder.relativeTo(Path.parse("/apples"));
                    test.assertEqual("grapes", relativePath.toString());
                });
            });

            runner.testGroup("relativeTo(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.relativeTo((Folder)null), new PreConditionFailure("folder cannot be null."));
                });

                runner.test("with /apples/grapes relative to /apples", (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/grapes");
                    final Folder baseFolder = getFolder(test, "/apples");
                    final Path relativePath = folder.relativeTo(baseFolder);
                    test.assertEqual("grapes", relativePath.toString());
                });
            });

            runner.testGroup("relativeTo(Root)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.relativeTo((Root)null), new PreConditionFailure("root cannot be null."));
                });

                runner.test("with /apples/grapes relative to /", (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/grapes");
                    final Root root = folder.getRoot();
                    final Path relativePath = folder.relativeTo(root);
                    test.assertEqual("apples/grapes", relativePath.toString());
                });
            });

            runner.test("create()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                test.assertFalse(folder.exists().await());

                test.assertNull(folder.create().await());
                test.assertTrue(folder.exists().await());

                test.assertThrows(() -> folder.create().await(),
                    new FolderAlreadyExistsException("/test/folder"));
                test.assertTrue(folder.exists().await());
            });

            runner.testGroup("createFolder(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.createFolder((String)null),
                        new PreConditionFailure("folderRelativePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.createFolder(""),
                        new PreConditionFailure("folderRelativePath cannot be empty."));
                });

                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Folder createdFolder = folder.createFolder("thing").await();
                    test.assertEqual("/test/folder/thing", createdFolder.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(createdFolder.exists().await());
                });
            });

            runner.testGroup("createFolder(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.createFolder((Path)null), new PreConditionFailure("relativeFolderPath cannot be null."));
                });

                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Folder createdFolder = folder.createFolder("place").await();
                    test.assertEqual("/test/folder/place", createdFolder.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(createdFolder.exists().await());
                });
            });

            runner.testGroup("createFile(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.createFile((String)null), new PreConditionFailure("fileRelativePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.createFile(""), new PreConditionFailure("fileRelativePath cannot be empty."));
                });

                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final File file = folder.createFile("file.xml").await();
                    test.assertEqual("/test/folder/file.xml", file.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing relative path in subfolder with backslash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final File file = folder.createFile("subfolder\\file.xml").await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());

                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing relative path in subfolder with forward slash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

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
                    final Folder folder = getFolder(test);

                    final File file = folder.createFile(Path.parse("file.xml")).await();
                    test.assertEqual("/test/folder/file.xml", file.toString());
                    test.assertTrue(folder.exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing subfolder relative path with backslash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final File file = folder.createFile(Path.parse("subfolder\\file.xml")).await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());
                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });

                runner.test("with non-existing subfolder relative path with forward slash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final File file = folder.createFile(Path.parse("subfolder/file.xml")).await();
                    test.assertEqual("/test/folder/subfolder/file.xml", file.toString());
                    test.assertTrue(folder.exists().await());
                    test.assertTrue(folder.getFolder("subfolder").await().exists().await());
                    test.assertTrue(file.exists().await());
                });
            });

            runner.test("getFolders()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                test.assertFalse(folder.exists().await());
                test.assertThrows(() -> folder.getFolders().await(),
                    new FolderNotFoundException("/test/folder"));

                test.assertNull(folder.create().await());
                test.assertTrue(folder.exists().await());
                test.assertEqual(Iterable.create(), folder.getFolders().await());

                final Folder childFolder = folder.createFolder("childFolder1").await();
                test.assertEqual("/test/folder/childFolder1", childFolder.toString());

                test.assertEqual(Iterable.create("/test/folder/childFolder1"), folder.getFolders().await().map(Folder::toString));

                test.assertEqual("/test/folder/childFolder1/grandchildFolder1", childFolder.createFolder("grandchildFolder1").await().toString());
                test.assertEqual(Iterable.create("/test/folder/childFolder1"), folder.getFolders().await().map(Folder::toString));
            });

            runner.testGroup("getFiles()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertFalse(folder.exists().await());
                    test.assertThrows(() -> folder.getFiles().await(), new FolderNotFoundException("/test/folder"));
                });

                runner.test("when folder exists but is empty", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                    test.assertEqual(Iterable.create(), folder.getFiles().await());
                });

                runner.test("when folder exists and has one file", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertNull(folder.create().await());
                    test.assertTrue(folder.exists().await());
                    test.assertEqual("/test/folder/data.txt", folder.createFile("data.txt").await().toString());
                    test.assertEqual(Iterable.create("/test/folder/data.txt"), folder.getFiles().await().map(File::toString));
                });

                runner.test("when folder exists and has one grandchild file", (Test test) ->
                {
                    final Folder folder = getFolder(test);
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
                    final Folder folder = getFolder(test);
                    test.assertFalse(folder.exists().await());
                    test.assertThrows(() -> folder.getFilesAndFolders().await(),
                        new FolderNotFoundException("/test/folder"));
                });

                runner.test("when folder exists but is empty", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertNull(folder.create().await());
                    test.assertEqual(Iterable.create(), folder.getFilesAndFolders().await());
                });

                runner.test("when folder exists and has one child folder", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertNull(folder.create().await());
                    test.assertEqual("/test/folder/subfolder", folder.createFolder("subfolder").await().toString());
                    test.assertEqual(Iterable.create("/test/folder/subfolder"), folder.getFilesAndFolders().await().map(FileSystemEntry::toString));
                });

                runner.test("when folder exists and has one child file", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertNull(folder.create().await());
                    folder.createFile("file.java").await();
                    test.assertEqual(Iterable.create("/test/folder/file.java"), folder.getFilesAndFolders().await().map(FileSystemEntry::toString));
                });

                runner.test("when folder exists and has one child file and one child folder", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertNull(folder.create().await());
                    folder.createFile("file.java").await();
                    folder.createFolder("childfolder").await();
                    test.assertEqual(
                        Iterable.create(
                            "/test/folder/childfolder",
                            "/test/folder/file.java"),
                        folder.getFilesAndFolders().await().map(FileSystemEntry::toString));
                });
            });

            runner.testGroup("fileExists(String)", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertFalse(folder.fileExists("test.txt").await());
                });

                runner.test("when file exists", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    folder.createFile("test.txt").await();
                    test.assertTrue(folder.fileExists("test.txt").await());
                });
            });

            runner.testGroup("folderExists(String)", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertFalse(folder.folderExists("test").await());
                });

                runner.test("when folder does exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    folder.createFolder("test").await();
                    test.assertTrue(folder.folderExists("test").await());
                });
            });
        });
    }

    private static Folder getFolder(Test test)
    {
        return getFolder(test, "/test/folder");
    }

    private static Folder getFolder(Test test, String folderPath)
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock(), test::getParallelAsyncRunner);
        fileSystem.createRoot("/").await();

        return fileSystem.getFolder(folderPath).await();
    }
}
