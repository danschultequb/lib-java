package qub;

public class FolderTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(Folder.class, () ->
        {
            runner.test("exists()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                
                test.assertSuccess(false, folder.exists());
                
                folder.create();
                
                test.assertSuccess(true, folder.exists());
            });

            runner.testGroup("delete()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertDone(false, new FolderNotFoundException("/test/folder"), folder.delete());
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    folder.create();

                    test.assertSuccess(true, folder.delete());
                    test.assertSuccess(false, folder.exists());
                });
            });

            runner.test("create()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                test.assertSuccess(false, folder.exists());

                test.assertSuccess(true, folder.create());
                test.assertSuccess(true, folder.exists());

                final Result<Boolean> createResult2 = folder.create();
                test.assertFalse(createResult2.getValue());
                test.assertEqual(new FolderAlreadyExistsException("/test/folder"), createResult2.getError());
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

                    final Result<Folder> result = folder.createFolder("thing");
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/thing", result.getValue().toString());

                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, result.getValue().exists());
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

                    final Result<Folder> result = folder.createFolder("place");
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/place", result.getValue().toString());

                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, result.getValue().exists());
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
                    test.assertEqual("/test/folder/file.xml", result.getValue().toString());

                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, result.getValue().exists());
                });

                runner.test("with non-existing relative path in subfolder with backslash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile("subfolder\\file.xml");
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/subfolder/file.xml", result.getValue().toString());

                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, folder.getFolder("subfolder").getValue().exists());
                    test.assertSuccess(true, result.getValue().exists());
                });

                runner.test("with non-existing relative path in subfolder with forward slash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile("subfolder/file.xml");
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/subfolder/file.xml", result.getValue().toString());

                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, folder.getFolder("subfolder").getValue().exists());
                    test.assertSuccess(true, result.getValue().exists());
                });
            });

            runner.testGroup("createFile(Path)", () ->
            {
                runner.test("with non-existing relative path", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile(Path.parse("file.xml"));
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/file.xml", result.getValue().toString());
                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, result.getValue().exists());
                });

                runner.test("with non-existing subfolder relative path with backslash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile(Path.parse("subfolder\\file.xml"));
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/subfolder/file.xml", result.getValue().toString());
                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, folder.getFolder("subfolder").getValue().exists());
                    test.assertSuccess(true, result.getValue().exists());
                });

                runner.test("with non-existing subfolder relative path with forward slash separator", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<File> result = folder.createFile(Path.parse("subfolder/file.xml"));
                    test.assertSuccess(result);
                    test.assertEqual("/test/folder/subfolder/file.xml", result.getValue().toString());
                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(true, folder.getFolder("subfolder").getValue().exists());
                    test.assertSuccess(true, result.getValue().exists());
                });
            });

            runner.test("getFolders()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                test.assertSuccess(false, folder.exists());
                test.assertError(new FolderNotFoundException("/test/folder"), folder.getFolders());

                folder.create();
                test.assertSuccess(true, folder.exists());
                test.assertSuccess(new Array<Folder>(0), folder.getFolders());

                final Result<Folder> childFolder = folder.createFolder("childFolder1");
                test.assertSuccess(childFolder);
                test.assertEqual("/test/folder/childFolder1", childFolder.getValue().toString());

                test.assertEqual(Array.fromValues(new String[] { "/test/folder/childFolder1" }), folder.getFolders().getValue().map(FileSystemEntry::toString));

                test.assertSuccess(childFolder.getValue().createFolder("grandchildFolder1"));
                test.assertEqual(Array.fromValues(new String[] { "/test/folder/childFolder1" }), folder.getFolders().getValue().map(FileSystemEntry::toString));
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
                    test.assertSuccess(true, folder.create());
                    test.assertSuccess(folder.exists());
                    test.assertSuccess(new Array<File>(0), folder.getFiles());
                });

                runner.test("when folder exists and has one file", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(true, folder.create());
                    test.assertSuccess(true, folder.exists());
                    test.assertSuccess(folder.createFile("data.txt"));
                    final Result<Iterable<File>> files = folder.getFiles();
                    test.assertSuccess(files);
                    test.assertEqual(Array.fromValues(new String[] { "/test/folder/data.txt" }), files.getValue().map(FileSystemEntry::toString));
                });

                runner.test("when folder exists and has one grandchild file", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(true, folder.create());
                    test.assertSuccess(folder.exists());
                    test.assertSuccess(folder.createFile("subfolder/data.txt"));
                    final Result<Iterable<File>> files = folder.getFiles();
                    test.assertSuccess(new Array<File>(0), files);
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
                    test.assertSuccess(true, folder.create());
                    test.assertSuccess(new Array<FileSystemEntry>(0), folder.getFilesAndFolders());
                });

                runner.test("when folder exists and has one child folder", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(true, folder.create());
                    test.assertSuccess(folder.createFolder("subfolder"));

                    final Result<Iterable<FileSystemEntry>> filesAndFolders = folder.getFilesAndFolders();
                    test.assertSuccess(filesAndFolders);
                    test.assertEqual(Array.fromValues(new String[] { "/test/folder/subfolder" }), filesAndFolders.getValue().map(FileSystemEntry::toString));
                });

                runner.test("when folder exists and has one child file", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(true, folder.create());
                    test.assertSuccess(folder.createFile("file.java"));

                    final Result<Iterable<FileSystemEntry>> filesAndFolders = folder.getFilesAndFolders();
                    test.assertSuccess(filesAndFolders);
                    test.assertEqual(Array.fromValues(new String[] { "/test/folder/file.java" }), filesAndFolders.getValue().map(FileSystemEntry::toString));
                });

                runner.test("when folder exists and has one child file and one child folder", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertSuccess(true, folder.create());
                    test.assertSuccess(folder.createFile("file.java"));
                    test.assertSuccess(folder.createFolder("childfolder"));

                    final Result<Iterable<FileSystemEntry>> filesAndFolders = folder.getFilesAndFolders();
                    test.assertSuccess(filesAndFolders);
                    test.assertEqual(Array.fromValues(new String[] { "/test/folder/childfolder", "/test/folder/file.java" }), filesAndFolders.getValue().map(FileSystemEntry::toString));
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
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
        fileSystem.createRoot("/");

        return fileSystem.getFolder("/test/folder").getValue();
    }
}
