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

                    final Result<Boolean> deleteResult = folder.delete();
                    test.assertFalse(deleteResult.getValue());
                    test.assertEqual(new FolderNotFoundException("/test/folder"), deleteResult.getError());
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
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), folder.createFolder((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), folder.createFolder(""));
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
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), folder.createFolder((Path)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertError(new IllegalArgumentException("relativeFolderPath cannot be null."), folder.createFolder(Path.parse("")));
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
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), folder.createFile((String)null));
                });

                runner.test("with empty path", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    test.assertError(new IllegalArgumentException("relativeFilePath cannot be null."), folder.createFile(""));
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
                    test.assertEqual("/test/folder/subfolder\\file.xml", result.getValue().toString());

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

//            runner.testGroup("createFile(Path)", () ->
//            {
//                runner.test("with null path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFile((Path)null));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with empty path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFile(Path.parse("")));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with non-existing relative path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFile(Path.parse("file.xml")));
//                    test.assertTrue(folder.exists());
//                });
//
//                runner.test("with non-existing subfolder relative path with backslash separator", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFile(Path.parse("subfolder\\file.xml")));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                });
//
//                runner.test("with non-existing subfolder relative path with forward slash separator", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFile(Path.parse("subfolder/file.xml")));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                });
//            });
//
//            runner.test("getFolders()", (Test test) ->
//            {
//                final Folder folder = getFolder(test);
//                test.assertFalse(folder.exists());
//                test.assertEqual(new Array<Folder>(0), folder.getFolders());
//
//                folder.create();
//                test.assertEqual(new Array<Folder>(0), folder.getFolders());
//
//                final Value<Folder> childFolder = new Value<>();
//                test.assertTrue(folder.createFolder("childFolder1", childFolder));
//                test.assertEqual(new String[] { "/test/folder/childFolder1" }, Array.toStringArray(folder.getFolders().map(FileSystemEntry::toString)));
//
//                test.assertTrue(childFolder.get().createFolder("grandchildFolder1"));
//                test.assertEqual(new String[] { "/test/folder/childFolder1" }, Array.toStringArray(folder.getFolders().map(FileSystemEntry::toString)));
//            });
//
//            runner.testGroup("getFiles()", () ->
//            {
//                runner.test("when folder doesn't exist", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertFalse(folder.exists());
//                    test.assertEqual(new Array<File>(0), folder.getFiles());
//                });
//
//                runner.test("when folder exists but is empty", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.create());
//                    test.assertTrue(folder.exists());
//                    final Iterable<File> files = folder.getFiles();
//                    test.assertEqual(new String[0], Array.toStringArray(files.map(FileSystemEntry::toString)));
//                });
//
//                runner.test("when folder exists and has one file", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.create());
//                    test.assertTrue(folder.exists());
//                    test.assertTrue(folder.createFile("data.txt"));
//                    final Iterable<File> files = folder.getFiles();
//                    test.assertEqual(new String[] { "/test/folder/data.txt" }, Array.toStringArray(files.map(FileSystemEntry::toString)));
//                });
//
//                runner.test("when folder exists and has one grandchild file", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.create());
//                    test.assertTrue(folder.exists());
//                    test.assertTrue(folder.createFile("subfolder/data.txt"));
//                    final Iterable<File> files = folder.getFiles();
//                    test.assertEqual(new String[0], Array.toStringArray(files.map(FileSystemEntry::toString)));
//                });
//            });
//
//            runner.testGroup("getFilesAndFolders()", () ->
//            {
//                runner.test("when folder doesn't exist", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertFalse(folder.exists());
//
//                    final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
//                    test.assertEqual(new Array<FileSystemEntry>(0), filesAndFolders);
//                });
//
//                runner.test("when folder exists but is empty", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.create());
//
//                    final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
//                    test.assertNotNull(filesAndFolders);
//                    test.assertFalse(filesAndFolders.any());
//                });
//
//                runner.test("when folder exists and has one child folder", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.create());
//                    test.assertTrue(folder.createFolder("subfolder"));
//
//                    final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
//                    test.assertNotNull(filesAndFolders);
//                    test.assertTrue(filesAndFolders.any());
//                    test.assertEqual(new String[] { "/test/folder/subfolder" }, Array.toStringArray(filesAndFolders.map(FileSystemEntry::toString)));
//                });
//
//                runner.test("when folder exists and has one child file", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.create());
//                    test.assertTrue(folder.createFile("file.java"));
//
//                    final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
//                    test.assertNotNull(filesAndFolders);
//                    test.assertTrue(filesAndFolders.any());
//                    test.assertEqual(new String[] { "/test/folder/file.java" }, Array.toStringArray(filesAndFolders.map(FileSystemEntry::toString)));
//                });
//
//                runner.test("when folder exists and has one child file and one child folder", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.create());
//                    test.assertTrue(folder.createFile("file.java"));
//                    test.assertTrue(folder.createFolder("childfolder"));
//
//                    final Iterable<FileSystemEntry> filesAndFolders = folder.getFilesAndFolders();
//                    test.assertNotNull(filesAndFolders);
//                    test.assertTrue(filesAndFolders.any());
//                    test.assertEqual(new String[] { "/test/folder/childfolder", "/test/folder/file.java" }, Array.toStringArray(filesAndFolders.map(FileSystemEntry::toString)));
//                });
//            });
//
//            runner.testGroup("fileExists(String)", () ->
//            {
//                runner.test("when file doesn't exist", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertFalse(folder.fileExists("test.txt"));
//                });
//
//                runner.test("when file exists", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    folder.createFile("test.txt");
//                    test.assertTrue(folder.fileExists("test.txt"));
//                });
//            });
//
//            runner.testGroup("folderExists(String)", () ->
//            {
//                runner.test("when folder doesn't exist", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertFalse(folder.folderExists("test"));
//                });
//
//                runner.test("when folder does exist", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    folder.createFolder("test");
//                    test.assertTrue(folder.folderExists("test"));
//                });
//            });
        });
    }

    private static Folder getFolder(Test test)
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
        fileSystem.createRoot("/");

        return fileSystem.getFolder("/test/folder").getValue();
    }
}
