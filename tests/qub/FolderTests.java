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
                    test.assertError(new NotFoundException("The path \"/\" has no parent folder."), folder.getParentFolder());
                });

                runner.test("with " + Strings.escapeAndQuote("\\"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "\\");
                    test.assertError(new NotFoundException("The path \"/\" has no parent folder."), folder.getParentFolder());
                });

                runner.test("with " + Strings.escapeAndQuote("D:"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "D:");
                    test.assertError(new NotFoundException("The path \"D:/\" has no parent folder."), folder.getParentFolder());
                });

                runner.test("with " + Strings.escapeAndQuote("E:/"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "E:/");
                    test.assertError(new NotFoundException("The path \"E:/\" has no parent folder."), folder.getParentFolder());
                });

                runner.test("with " + Strings.escapeAndQuote("F:\\"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "F:\\");
                    test.assertError(new NotFoundException("The path \"F:/\" has no parent folder."), folder.getParentFolder());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples");
                    final Folder parentFolder = folder.getParentFolder().awaitError();
                    test.assertEqual("/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/");
                    final Folder parentFolder = folder.getParentFolder().awaitError();
                    test.assertEqual("/", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/dates"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/dates");
                    final Folder parentFolder = folder.getParentFolder().awaitError();
                    test.assertEqual("/apples", parentFolder.toString());
                });

                runner.test("with " + Strings.escapeAndQuote("/apples/dates/"), (Test test) ->
                {
                    final Folder folder = getFolder(test, "/apples/dates/");
                    final Folder parentFolder = folder.getParentFolder().awaitError();
                    test.assertEqual("/apples", parentFolder.toString());
                });
            });

            runner.test("exists()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                
                test.assertSuccess(false, folder.exists());
                
                folder.create();
                
                test.assertSuccess(true, folder.exists());
            });

            runner.test("existsAsync()", (Test test) ->
            {
                final Folder folder = getFolder(test);

                test.assertSuccess(false, folder.existsAsync().awaitReturn());

                folder.create();

                test.assertSuccess(true, folder.existsAsync().awaitReturn());
            });

            runner.testGroup("delete()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertError(new FolderNotFoundException("/test/folder"), folder.delete());
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    folder.create();

                    test.assertSuccess(null, folder.delete());
                    test.assertSuccess(false, folder.exists());
                });
            });

            runner.testGroup("deleteAsync()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertError(new FolderNotFoundException("/test/folder"), folder.deleteAsync().awaitReturn());
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    folder.create();

                    test.assertSuccess(null, folder.deleteAsync().awaitReturn());
                    test.assertSuccess(false, folder.exists());
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
                test.assertSuccess(false, folder.exists());

                test.assertSuccess(null, folder.create());
                test.assertSuccess(true, folder.exists());

                test.assertError(new FolderAlreadyExistsException("/test/folder"), folder.create());
                test.assertSuccess(true, folder.exists());
            });

            runner.testGroup("createFolder(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.createFolder((String)null), new PreConditionFailure("folderRelativePath cannot be null."));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertThrows(() -> folder.createFolder(""), new PreConditionFailure("folderRelativePath cannot be empty."));
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

                    final Result<File> result = folder.createFile("file.xml");
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/file.xml", result.awaitError().toString());

                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, result.awaitError().exists());
                });

                runner.test("with non-existing relative path in subfolder with backslash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile("subfolder\\file.xml");
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/subfolder/file.xml", result.awaitError().toString());

                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, folder.getFolder("subfolder").awaitError().exists());
                    test.assertSuccess(true, result.awaitError().exists());
                });

                runner.test("with non-existing relative path in subfolder with forward slash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile("subfolder/file.xml");
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/subfolder/file.xml", result.awaitError().toString());

                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, folder.getFolder("subfolder").awaitError().exists());
                    test.assertSuccess(true, result.awaitError().exists());
                });
            });

            runner.testGroup("createFile(Path)", () ->
            {
                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile(Path.parse("file.xml"));
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/file.xml", result.awaitError().toString());
                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, result.awaitError().exists());
                });

                runner.test("with non-existing subfolder relative path with backslash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile(Path.parse("subfolder\\file.xml"));
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/subfolder/file.xml", result.awaitError().toString());
                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, folder.getFolder("subfolder").awaitError().exists());
                    test.assertSuccess(true, result.awaitError().exists());
                });

                runner.test("with non-existing subfolder relative path with forward slash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile(Path.parse("subfolder/file.xml"));
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/subfolder/file.xml", result.awaitError().toString());
                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, folder.getFolder("subfolder").awaitError().exists());
                    test.assertSuccess(true, result.awaitError().exists());
                });
            });

            runner.test("getFolders()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                test.assertSuccess(false, folder.exists());
                test.assertError(new FolderNotFoundException("/test/folder"), folder.getFolders());

                test.assertSuccess(null, folder.create());
                test.assertSuccess(true, folder.exists());
                test.assertSuccess(Iterable.create(), folder.getFolders());

                test.assertSuccess(folder.createFolder("childFolder1"),
                    (Folder childFolder) ->
                    {
                        test.assertEqual("/test/folder/childFolder1", childFolder.toString());

                        test.assertSuccess(Iterable.create("/test/folder/childFolder1"), folder.getFolders().then((Iterable<Folder> folders) -> folders.map(FileSystemEntry::toString)));

                        test.assertSuccess("/test/folder/childFolder1/grandchildFolder1", childFolder.createFolder("grandchildFolder1").then(Folder::toString));
                        test.assertSuccess(Iterable.create("/test/folder/childFolder1"), folder.getFolders().then((Iterable<Folder> folders) -> folders.map(FileSystemEntry::toString)));
                    });
            });

            runner.testGroup("getFiles()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(false, folder.exists());
                    test.assertError(new FolderNotFoundException("/test/folder"), folder.getFiles());
                });

                runner.test("when folder exists but is empty", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(null, folder.create());
                    test.assertSuccess(folder.exists());
                    test.assertSuccess(Iterable.create(), folder.getFiles());
                });

                runner.test("when folder exists and has one file", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(null, folder.create());
                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(folder.createFile("data.txt"));
                    test.assertSuccess(Iterable.create("/test/folder/data.txt"), folder.getFiles().then((Iterable<File> files) -> files.map(FileSystemEntry::toString)));
                });

                runner.test("when folder exists and has one grandchild file", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(null, folder.create());
                    test.assertSuccess(folder.exists());
                    test.assertSuccess(folder.createFile("subfolder/data.txt"));
                    test.assertSuccess(Iterable.create(), folder.getFiles());
                });
            });

            runner.testGroup("getFilesAndFolders()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(false, folder.exists());
                    test.assertError(new FolderNotFoundException("/test/folder"), folder.getFilesAndFolders());
                });

                runner.test("when folder exists but is empty", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(null, folder.create());
                    test.assertSuccess(Iterable.create(), folder.getFilesAndFolders());
                });

                runner.test("when folder exists and has one child folder", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(null, folder.create());
                    test.assertSuccess(folder.createFolder("subfolder"));
                    test.assertSuccess(Iterable.create("/test/folder/subfolder"), folder.getFilesAndFolders().then((Iterable<FileSystemEntry> entries) -> entries.map(FileSystemEntry::toString)));
                });

                runner.test("when folder exists and has one child file", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(null, folder.create());
                    test.assertSuccess(folder.createFile("file.java"));
                    test.assertSuccess(Iterable.create("/test/folder/file.java"), folder.getFilesAndFolders().then((Iterable<FileSystemEntry> entries) -> entries.map(FileSystemEntry::toString)));
                });

                runner.test("when folder exists and has one child file and one child folder", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(null, folder.create());
                    test.assertSuccess(folder.createFile("file.java"));
                    test.assertSuccess(folder.createFolder("childfolder"));
                    test.assertSuccess(
                        Iterable.create(
                            "/test/folder/childfolder",
                            "/test/folder/file.java"),
                        folder.getFilesAndFolders().then((Iterable<FileSystemEntry> entries) -> entries.map(FileSystemEntry::toString)));
                });
            });

            runner.testGroup("fileExists(String)", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(false, folder.fileExists("test.txt"));
                });

                runner.test("when file exists", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(folder.createFile("test.txt"));
                    test.assertSuccess(true, folder.fileExists("test.txt"));
                });
            });

            runner.testGroup("folderExists(String)", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(false, folder.folderExists("test"));
                });

                runner.test("when folder does exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(folder.createFolder("test"));
                    test.assertSuccess(true, folder.folderExists("test"));
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
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
        fileSystem.createRoot("/");

        return fileSystem.getFolder(folderPath).awaitError();
    }
}
