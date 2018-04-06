package qub;

public class FolderTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(Folder.class, runner.skip(), () ->
        {
            runner.test("exists()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                
                final Result<Boolean> folderExistsResult1 = folder.exists().awaitReturn();
                test.assertFalse(folderExistsResult1.hasError());
                test.assertFalse(folderExistsResult1.getValue());
                
                folder.create().await();
                
                final Result<Boolean> folderExistsResult2 = folder.exists().awaitReturn();
                test.assertFalse(folderExistsResult2.hasError());
                test.assertTrue(folderExistsResult2.getValue());
            });

            runner.testGroup("delete()", () ->
            {
                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final Folder folder = getFolder(test);

                    final Result<Boolean> deleteFolderResult = folder.delete().awaitReturn();
                    test.assertNull(deleteFolderResult.getValue());
                    test.assertTrue(deleteFolderResult.getError() instanceof FolderNotFoundException);
                    test.assertFalse(folder.exists().awaitReturn().getValue());
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final Folder folder = getFolder(test);
                    folder.create().await();

                    test.assertTrue(folder.delete().awaitReturn().getValue());
                    test.assertFalse(folder.exists().awaitReturn().getValue());
                });
            });

            runner.test("create()", (Test test) ->
            {
                final Folder folder = getFolder(test);
                test.assertFalse(folder.exists().awaitReturn().getValue());

                final Result<Boolean> createFolderResult1 = folder.create().awaitReturn();
                test.assertTrue(createFolderResult1.getValue());
                test.assertNull(createFolderResult1.getError());
                test.assertTrue(folder.exists().awaitReturn().getValue());

                final Result<Boolean> createFolderResult2 = folder.create().awaitReturn();
                test.assertNull(createFolderResult2.getValue());
                test.assertTrue(createFolderResult2.getError() instanceof FolderAlreadyExistsException);
                test.assertTrue(folder.exists().awaitReturn().getValue());
            });

//            runner.testGroup("createFolder(String)", () ->
//            {
//                runner.test("with null path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFolder((String)null));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with empty path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFolder(""));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with non-existing relative path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFolder("thing"));
//                    test.assertTrue(folder.exists());
//                    test.assertTrue(folder.getFolder("thing").exists());
//                });
//            });
//
//            runner.testGroup("createFolder(Path)", () ->
//            {
//                runner.test("with null path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFolder((Path)null));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with empty path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFolder(Path.parse("")));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with non-existing relative path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFolder(Path.parse("place")));
//                    test.assertTrue(folder.exists());
//                    test.assertTrue(folder.getFolder("place").exists());
//                });
//            });
//
//            runner.testGroup("createFolder(Path,Value<Folder>)", () ->
//            {
//                runner.test("with relative path and output folder", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    final Value<Folder> childFolder = new Value<>();
//                    test.assertTrue(folder.createFolder(Path.parse("place"), childFolder));
//                    test.assertTrue(folder.exists());
//                    test.assertNotNull(childFolder.get());
//                    test.assertTrue(childFolder.get().exists());
//                });
//            });
//
//            runner.testGroup("createFile(String)", () ->
//            {
//                runner.test("with null path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFile((String)null));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with empty path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFile(""));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with non-existing relative path", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFile("file.xml"));
//                    test.assertTrue(folder.exists());
//                    test.assertTrue(folder.getFile("file.xml").exists());
//                });
//
//                runner.test("with non-existing relative path in subfolder with backslash separator", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFile("subfolder\\file.xml"));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                    test.assertTrue(folder.getFolder("subfolder").exists());
//                    test.assertTrue(folder.getFolder("subfolder").getFile("file.xml").exists());
//                });
//
//                runner.test("with non-existing relative path in subfolder with forward slash separator", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFile("subfolder/file.xml"));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                    test.assertTrue(folder.getFolder("subfolder").exists());
//                    test.assertTrue(folder.getFolder("subfolder").getFile("file.xml").exists());
//                });
//            });
//
//            runner.testGroup("createFile(String,Value<File>)", () ->
//            {
//                runner.test("with null path and null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFile((String)null, null));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with empty path and null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertFalse(folder.createFile("", null));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with non-existing relative path and null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFile("file.xml", null));
//                    test.assertTrue(folder.exists());
//                });
//
//                runner.test("with subfolder relative path with backslash separator and null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFile("subfolder\\file.xml", null));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                });
//
//                runner.test("with subfolder relative path with forward slash separator and null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    test.assertTrue(folder.createFile("subfolder/file.xml", null));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                });
//
//                runner.test("with null path and non-null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    final Value<File> file = new Value<>();
//                    test.assertFalse(folder.createFile((String)null, file));
//                    test.assertFalse(folder.exists());
//                    test.assertFalse(file.hasValue());
//                });
//
//                runner.test("with empty path and non-null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    final Value<File> file = new Value<>();
//                    test.assertFalse(folder.createFile("", file));
//                    test.assertFalse(folder.exists());
//                    test.assertFalse(file.hasValue());
//                });
//
//                runner.test("with non-existing relative path and non-null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    final Value<File> file = new Value<>();
//                    test.assertTrue(folder.createFile("file.xml", file));
//                    test.assertTrue(folder.exists());
//                    test.assertNotNull(file.get());
//                    test.assertEqual(Path.parse("/test/folder/file.xml"), file.get().getPath());
//                    test.assertTrue(file.get().exists());
//                });
//
//                runner.test("with non-existing subfolder relative path with backslash separator and non-null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    final Value<File> file = new Value<>();
//                    test.assertTrue(folder.createFile("subfolder\\file.xml", file));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                    test.assertNotNull(file.get());
//                    test.assertEqual(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
//                    test.assertTrue(file.get().exists());
//                });
//
//                runner.test("with non-existing subfolder relative path with forward slash separator and non-null value", (Test test) ->
//                {
//                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
//                    fileSystem.createRoot("/");
//
//                    final Folder folder = fileSystem.getFolder("/test/folder");
//                    final Value<File> file = new Value<>();
//                    test.assertTrue(folder.createFile("subfolder/file.xml", file));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                    test.assertNotNull(file.get());
//                    test.assertEqual(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
//                    test.assertTrue(file.get().exists());
//                });
//            });
//
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
//            runner.testGroup("createFile(Path,Value<File>)", () ->
//            {
//                runner.test("with null path and null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertFalse(folder.createFile((Path)null, null));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with empty path and null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertFalse(folder.createFile(Path.parse(""), null));
//                    test.assertFalse(folder.exists());
//                });
//
//                runner.test("with non-existing relative path and null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.createFile(Path.parse("file.xml"), null));
//                    test.assertTrue(folder.exists());
//                });
//
//                runner.test("with non-existing subfolder relative path with backslash separator and null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.createFile(Path.parse("subfolder\\file.xml"), null));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                });
//
//                runner.test("with non-existing subfolder relative path with forward slash separator and null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    test.assertTrue(folder.createFile(Path.parse("subfolder/file.xml"), null));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                });
//
//                runner.test("with null path and non-null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    final Value<File> file = new Value<>();
//                    test.assertFalse(folder.createFile((Path)null, file));
//                    test.assertFalse(folder.exists());
//                    test.assertFalse(file.hasValue());
//                });
//
//                runner.test("with empty path and non-null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    final Value<File> file = new Value<>();
//                    test.assertFalse(folder.createFile(Path.parse(""), file));
//                    test.assertFalse(folder.exists());
//                    test.assertFalse(file.hasValue());
//                });
//
//                runner.test("with non-existing relative path and non-null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    final Value<File> file = new Value<>();
//                    test.assertTrue(folder.createFile(Path.parse("file.xml"), file));
//                    test.assertTrue(folder.exists());
//                    test.assertNotNull(file.get());
//                    test.assertEqual(Path.parse("/test/folder/file.xml"), file.get().getPath());
//                    test.assertTrue(file.get().exists());
//                });
//
//                runner.test("with non-existing subfolder relative path with backslash separator and non-null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    final Value<File> file = new Value<>();
//                    test.assertTrue(folder.createFile(Path.parse("subfolder\\file.xml"), file));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                    test.assertNotNull(file.get());
//                    test.assertEqual(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
//                    test.assertTrue(file.get().exists());
//                });
//
//                runner.test("with non-existing subfolder relative path with forward slash separator and non-null value", (Test test) ->
//                {
//                    final Folder folder = getFolder(test);
//                    final Value<File> file = new Value<>();
//                    test.assertTrue(folder.createFile(Path.parse("subfolder/file.xml"), file));
//                    test.assertTrue(folder.exists());
//                    test.assertFalse(folder.getFiles().any());
//                    test.assertNotNull(file.get());
//                    test.assertEqual(Path.parse("/test/folder/subfolder/file.xml"), file.get().getPath());
//                    test.assertTrue(file.get().exists());
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
