package qub;

public class FileTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(File.class, () ->
        {
            runner.testGroup("getParentFolder()", () ->
            {
                runner.test("with file at root", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File file = fileSystem.getFile("/file.txt").await();
                    test.assertEqual("/", file.getParentFolder().toString());
                });

                runner.test("with file in folder", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File file = fileSystem.getFile("/folder/file.txt").await();
                    test.assertEqual("/folder", file.getParentFolder().toString());
                });
            });

            runner.test("getFileExtension()", (Test test) ->
            {
                final FileSystem fileSystem = getFileSystem(test);

                final File fileWithoutExtension = fileSystem.getFile("/folder/file").await();
                test.assertNull(fileWithoutExtension.getFileExtension());

                final File fileWithExtension = fileSystem.getFile("/file.csv").await();
                test.assertEqual(".csv", fileWithExtension.getFileExtension());
            });

            runner.testGroup("getNameWithoutFileExtension()", () ->
            {
                final Action2<String,String> getNameWithoutFileExtensionTest = (String filePath, String expectedNameWithoutFileExtension) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = getFileSystem(test);
                        test.assertSuccess(expectedNameWithoutFileExtension, fileSystem.getFile(filePath).then(File::getNameWithoutFileExtension));
                    });
                };

                getNameWithoutFileExtensionTest.run("/folder/file", "file");
                getNameWithoutFileExtensionTest.run("/dogs.txt", "dogs");
            });
            
            runner.test("create()", (Test test) ->
            {
                final File file = getFile(test);

                test.assertSuccess(null, file.create());

                test.assertTrue(file.exists().await());

                test.assertEqual(new byte[0], file.getContents().await());

                test.assertError(new FileAlreadyExistsException(file.toString()), file.create());

                test.assertTrue(file.exists().await());
            });
            
            runner.testGroup("exists()", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.exists().await());
                });

                runner.test("when file does exist", (Test test) ->
                {
                    final File file = getFile(test);
                    file.create();
                    test.assertTrue(file.exists().await());
                });
            });

            runner.testGroup("delete()", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertError(new FileNotFoundException(file.toString()), file.delete());
                    test.assertSuccess(false, file.exists());
                });

                runner.test("when file does exist", (Test test) ->
                {
                    final File file = getFile(test);
                    file.create();

                    test.assertSuccess(null, file.delete());
                    test.assertFalse(file.exists().await());
                });
            });

            runner.testGroup("equals()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.equals(null));
                });

                runner.test("with String", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.equals(file.getPath().toString()));
                });

                runner.test("with Path", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.equals(file.getPath()));
                });

                runner.test("with different file create same file system", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File lhs = getFile(fileSystem, "/a/path.txt");
                    final File rhs = getFile(fileSystem, "/not/the/same/path.txt");
                    test.assertFalse(lhs.equals(rhs));
                });

                runner.test("with same", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertTrue(file.equals(file));
                });

                runner.test("with equal path and same file system", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem(test);
                    final File lhs = getFile(fileSystem);
                    final File rhs = getFile(fileSystem);
                    test.assertTrue(lhs.equals(rhs));
                });
            });

            runner.testGroup("getContents()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile(test);
                    final Result<byte[]> contents = file.getContents();
                    test.assertError(new FileNotFoundException("/A"), contents);
                });

                runner.test("with existing file with no contents", (Test test) ->
                {
                    final File file = getFile(test);
                    file.create();
                    final Result<byte[]> contents = file.getContents();
                    test.assertSuccess(new byte[0], contents);
                });
            });

            runner.testGroup("getContentByteReadStream()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertError(new FileNotFoundException("/A"), file.getContentByteReadStream());
                });
            });

            runner.testGroup("getContentByteReadStreamAsync()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertError(new FileNotFoundException("/A"), file.getContentByteReadStreamAsync().awaitReturn());
                });
            });

            runner.testGroup("getContentCharacterReadStream()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertError(new FileNotFoundException("/A"), file.getContentCharacterReadStream());
                });
            });

            runner.testGroup("getContentCharacterReadStreamAsync()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertError(new FileNotFoundException("/A"), file.getContentCharacterReadStreamAsync().awaitReturn());
                });
            });
        });
    }

    private static FileSystem getFileSystem(Test test)
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
        fileSystem.createRoot("/");
        return fileSystem;
    }

    private static File getFile(Test test)
    {
        final FileSystem fileSystem = getFileSystem(test);
        return getFile(fileSystem);
    }

    private static File getFile(FileSystem fileSystem)
    {
        return getFile(fileSystem, "/A");
    }

    private static File getFile(FileSystem fileSystem, String filePath)
    {
        return new File(fileSystem, filePath);
    }
}
