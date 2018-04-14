package qub;

public class FileTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(File.class, runner.skip(), () ->
        {
            runner.test("getFileExtension()", (Test test) ->
            {
                final FileSystem fileSystem = getFileSystem(test);

                final File fileWithoutExtension = fileSystem.getFile("/folder/file").getValue();
                test.assertNull(fileWithoutExtension.getFileExtension());

                final File fileWithExtension = fileSystem.getFile("/file.csv").getValue();
                test.assertEqual(".csv", fileWithExtension.getFileExtension());
            });

            runner.testGroup("getNameWithoutFileExtension()", () ->
            {
                final Action2<String,String> getNameWithoutFileExtensionTest = (String filePath, String expectedNameWithoutFileExtension) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = getFileSystem(test);
                        final File file = fileSystem.getFile(filePath).getValue();
                        final String nameWithoutFileExtension = file.getNameWithoutFileExtension();
                        test.assertEqual(expectedNameWithoutFileExtension, nameWithoutFileExtension);
                    });
                };

                getNameWithoutFileExtensionTest.run("/folder/file", "file");
                getNameWithoutFileExtensionTest.run("/dogs.txt", "dogs");
            });
            
            runner.test("create()", (Test test) ->
            {
                final File file = getFile(test);

                test.assertTrue(file.create().getValue());

                test.assertTrue(file.exists().getValue());

                test.assertEqual(new byte[0], file.getContents().getValue());

                test.assertFalse(file.create().getValue());

                test.assertTrue(file.exists().getValue());
            });
            
            runner.testGroup("exists()", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.exists().getValue());
                });

                runner.test("when file does exist", (Test test) ->
                {
                    final File file = getFile(test);
                    file.create();
                    test.assertTrue(file.exists().getValue());
                });
            });

            runner.testGroup("delete()", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertFalse(file.delete().getValue());
                    test.assertFalse(file.exists().getValue());
                });

                runner.test("when file does exist", (Test test) ->
                {
                    final File file = getFile(test);
                    file.create();

                    test.assertTrue(file.delete().getValue());
                    test.assertFalse(file.exists().getValue());
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

                runner.test("with different file from same file system", (Test test) ->
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
                    test.assertNull(file.getContents());
                });

                runner.test("with existing file with no contents", (Test test) ->
                {
                    final File file = getFile(test);
                    file.create();
                    test.assertEqual(new byte[0], file.getContents());
                });
            });

            runner.testGroup("getContentByteReadStream()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile(test);
                    test.assertNull(file.getContentByteReadStream());
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
