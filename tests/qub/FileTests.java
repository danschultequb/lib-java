package qub;

public class FileTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(File.class, () ->
        {
            runner.test("getFileExtension()", (Test test) ->
            {
                final FileSystem fileSystem = getFileSystem();

                final File fileWithoutExtension = fileSystem.getFile("/folder/file");
                test.assertNull(fileWithoutExtension.getFileExtension());

                final File fileWithExtension = fileSystem.getFile("/file.csv");
                test.assertEqual(".csv", fileWithExtension.getFileExtension());
            });

            runner.testGroup("getNameWithoutFileExtension()", () ->
            {
                final Action2<String,String> getNameWithoutFileExtensionTest = (String filePath, String expectedNameWithoutFileExtension) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = getFileSystem();
                        final File file = fileSystem.getFile(filePath);
                        final String nameWithoutFileExtension = file.getNameWithoutFileExtension();
                        test.assertEqual(expectedNameWithoutFileExtension, nameWithoutFileExtension);
                    });
                };

                getNameWithoutFileExtensionTest.run("/folder/file", "file");
                getNameWithoutFileExtensionTest.run("/dogs.txt", "dogs");
            });
            
            runner.test("create()", (Test test) ->
            {
                final File file = getFile();

                test.assertTrue(file.create());

                test.assertTrue(file.exists());
                test.assertEqual(new byte[0], file.getContents());
            });
            
            runner.testGroup("create(byte[])", () ->
            {
                runner.test("with null contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.create((byte[])null));

                    test.assertTrue(file.exists());
                    test.assertEqual(new byte[0], file.getContents());
                });
                
                runner.test("with empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.create(new byte[0]));

                    test.assertTrue(file.exists());
                    test.assertEqual(new byte[0], file.getContents());
                });
                
                runner.test("with non-empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.create(new byte[] { 0, 1, 2, 3, 4 }));

                    test.assertTrue(file.exists());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, file.getContents());
                });
            });
            
            runner.testGroup("create(String)", () ->
            {
                runner.test("with null contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.create((String)null));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });
                
                runner.test("with empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.create(""));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });
                
                runner.test("with non-empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.create("hello"));

                    test.assertTrue(file.exists());
                    test.assertEqual("hello", file.getContentsAsString());
                });
            });
            
            runner.testGroup("create(String,CharacterEncoding)", () ->
            {
                runner.test("with null contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.create(null, CharacterEncoding.UTF_8));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });
                
                runner.test("with empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.create("", CharacterEncoding.UTF_8));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });
                
                runner.test("with non-empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.create("hello", CharacterEncoding.UTF_8));

                    test.assertTrue(file.exists());
                    test.assertEqual("hello", file.getContentsAsString());
                });
            });
            
            runner.testGroup("exists()", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final File file = getFile();
                    test.assertFalse(file.exists());
                });

                runner.test("when file does exist", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertTrue(file.exists());
                });
            });

            runner.testGroup("delete()", () ->
            {
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final File file = getFile();
                    test.assertFalse(file.delete());
                    test.assertFalse(file.exists());
                });

                runner.test("when file does exist", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.delete());
                    test.assertFalse(file.exists());
                });
            });

            runner.testGroup("equals()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final File file = getFile();
                    test.assertFalse(file.equals(null));
                });

                runner.test("with String", (Test test) ->
                {
                    final File file = getFile();
                    test.assertFalse(file.equals(file.getPath().toString()));
                });

                runner.test("with Path", (Test test) ->
                {
                    final File file = getFile();
                    test.assertFalse(file.equals(file.getPath()));
                });

                runner.test("with different file from same file system", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem();
                    final File lhs = getFile(fileSystem, "/a/path.txt");
                    final File rhs = getFile(fileSystem, "/not/the/same/path.txt");
                    test.assertFalse(lhs.equals(rhs));
                });

                runner.test("with same", (Test test) ->
                {
                    final File file = getFile();
                    test.assertTrue(file.equals(file));
                });

                runner.test("with equal path and same file system", (Test test) ->
                {
                    final FileSystem fileSystem = getFileSystem();
                    final File lhs = getFile(fileSystem);
                    final File rhs = getFile(fileSystem);
                    test.assertTrue(lhs.equals(rhs));
                });
            });

            runner.testGroup("getContents()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContents());
                });

                runner.test("with existing file with no contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertEqual(new byte[0], file.getContents());
                });
            });

            runner.testGroup("getContentsAsString()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentsAsString());
                });

                runner.test("with existing file with no contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertEqual("", file.getContentsAsString());
                });

                runner.test("with existing file with contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create("Hello".getBytes());
                    test.assertEqual("Hello", file.getContentsAsString());
                });
            });

            runner.testGroup("getContentsAsString(CharacterEncoding)", () ->
            {
                runner.test("with non-existing file and null encoding", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentsAsString(null));
                });

                runner.test("with existing file with no contents and null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertNull(file.getContentsAsString(null));
                });

                runner.test("with existing file with contents and null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create("Hello".getBytes());
                    test.assertNull(file.getContentsAsString(null));
                });

                runner.test("with non-existing file and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentsAsString(CharacterEncoding.UTF_8));
                });

                runner.test("with existing file with no contents and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertEqual("", file.getContentsAsString(CharacterEncoding.UTF_8));
                });

                runner.test("with existing file with contents and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create("Hello".getBytes());
                    test.assertEqual("Hello", file.getContentsAsString(CharacterEncoding.UTF_8));
                });
            });

            runner.testGroup("getContentLines()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentLines());
                });

                runner.test("with existing file with no contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertEqual(new String[0], Array.toStringArray(file.getContentLines()));
                });

                runner.test("with existing file with single line content", (Test test) ->
                {
                    final File file = getFile();
                    file.create("hello");
                    test.assertEqual(new String[] { "hello" }, Array.toStringArray(file.getContentLines()));
                });

                runner.test("with existing file with multiple lines of content", (Test test) ->
                {
                    final File file = getFile();
                    file.create("a\nb\r\nc");
                    test.assertEqual(new String[] { "a\n", "b\r\n", "c" }, Array.toStringArray(file.getContentLines()));
                });
            });

            runner.testGroup("getContentLines(CharacterEncoding)", () ->
            {
                runner.test("with non-existing file and null encoding", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentLines(null));
                });

                runner.test("with existing file with no contents and null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertEqual(null, Array.toStringArray(file.getContentLines(null)));
                });

                runner.test("with existing file with single line content and null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create("hello");
                    test.assertEqual(null, Array.toStringArray(file.getContentLines(null)));
                });

                runner.test("with existing file with multiple lines of content and null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create("a\nb\r\nc");
                    test.assertEqual(null, Array.toStringArray(file.getContentLines(null)));
                });

                runner.test("with non-existing file and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentLines(CharacterEncoding.UTF_8));
                });

                runner.test("with existing file with no contents and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertEqual(new String[0], Array.toStringArray(file.getContentLines(CharacterEncoding.UTF_8)));
                });

                runner.test("with existing file with single line content and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create("hello");
                    test.assertEqual(new String[] { "hello" }, Array.toStringArray(file.getContentLines(CharacterEncoding.UTF_8)));
                });

                runner.test("with existing file with multiple lines of content and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create("a\nb\r\nc");
                    test.assertEqual(new String[] { "a\n", "b\r\n", "c" }, Array.toStringArray(file.getContentLines(CharacterEncoding.UTF_8)));
                });
            });

            runner.testGroup("getContentLines(boolean)", () ->
            {
                runner.test("with non-existing file and don't include new lines", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentLines(false));
                });

                runner.test("with existing file with no contents and don't include new lines", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertEqual(new String[0], Array.toStringArray(file.getContentLines(false)));
                });

                runner.test("with existing file with single line of content and don't include new lines", (Test test) ->
                {
                    final File file = getFile();
                    file.create("hello");
                    test.assertEqual(new String[] { "hello" }, Array.toStringArray(file.getContentLines(false)));
                });

                runner.test("with existing file with multiple lines of content and don't include new lines", (Test test) ->
                {
                    final File file = getFile();
                    file.create("a\nb\r\nc");
                    test.assertEqual(new String[] { "a", "b", "c" }, Array.toStringArray(file.getContentLines(false)));
                });

                runner.test("with non-existing file and include new lines", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentLines(true));
                });

                runner.test("with existing file with no contents and include new lines", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertEqual(new String[0], Array.toStringArray(file.getContentLines(true)));
                });

                runner.test("with existing file with single line content and include new lines", (Test test) ->
                {
                    final File file = getFile();
                    file.create("hello");
                    test.assertEqual(new String[] { "hello" }, Array.toStringArray(file.getContentLines(true)));
                });

                runner.test("with existing file with multiple lines of content and include new lines", (Test test) ->
                {
                    final File file = getFile();
                    file.create("a\nb\r\nc");
                    test.assertEqual(new String[] { "a\n", "b\r\n", "c" }, Array.toStringArray(file.getContentLines(true)));
                });
            });

            runner.testGroup("getContentLines(boolean,CharacterEncoding)", () ->
            {
                runner.test("with non-existing file, include new lines, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentLines(true, CharacterEncoding.UTF_8));
                });

                runner.test("with existing file with no contents, include new lines, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create();
                    test.assertEqual(new String[0], Array.toStringArray(file.getContentLines(true, CharacterEncoding.UTF_8)));
                });

                runner.test("with existing file with single line content, include new lines, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create("hello");
                    test.assertEqual(new String[] { "hello" }, Array.toStringArray(file.getContentLines(true, CharacterEncoding.UTF_8)));
                });

                runner.test("with existing file with multiple lines of content, include new lines, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create("a\nb\r\nc");
                    test.assertEqual(new String[] { "a\n", "b\r\n", "c" }, Array.toStringArray(file.getContentLines(true, CharacterEncoding.UTF_8)));
                });
            });

            runner.testGroup("getContentByteReadStream()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentByteReadStream());
                });
            });

            runner.testGroup("getContentCharacterReadStream()", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final File file = getFile();
                    test.assertNull(file.getContentCharacterReadStream());
                });
            });

            runner.testGroup("setContents(byte[])", () ->
            {
                runner.test("with non-existing file and null contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.setContents((byte[])null));

                    test.assertTrue(file.exists());
                    test.assertEqual(new byte[0], file.getContents());
                });

                runner.test("with existing file and null contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.setContents((byte[])null));

                    test.assertTrue(file.exists());
                    test.assertEqual(new byte[0], file.getContents());
                });

                runner.test("with non-existing file and empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.setContents(new byte[0]));

                    test.assertTrue(file.exists());
                    test.assertEqual(new byte[0], file.getContents());
                });

                runner.test("with existing file and empty contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.setContents(new byte[0]));

                    test.assertTrue(file.exists());
                    test.assertEqual(new byte[0], file.getContents());
                });

                runner.test("with non-existing file and non-empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.setContents(new byte[] { 0, 1, 2, 3 }));

                    test.assertTrue(file.exists());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, file.getContents());
                });

                runner.test("with existing file and non-empty contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.setContents(new byte[] { 0, 1, 2, 3 }));

                    test.assertTrue(file.exists());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, file.getContents());
                });
            });

            runner.testGroup("setContents(String)", () ->
            {
                runner.test("with non-existing file and null contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.setContents((String)null));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });

                runner.test("with existing file and null contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.setContents((String)null));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });

                runner.test("with non-existing file and empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.setContents(""));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });

                runner.test("with existing file and empty contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.setContents(""));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });

                runner.test("with non-existing flie and non-empty contents", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.setContents("XYZ"));

                    test.assertTrue(file.exists());
                    test.assertEqual("XYZ", file.getContentsAsString());
                });

                runner.test("with existing file and non-empty contents", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.setContents("XYZ"));

                    test.assertTrue(file.exists());
                    test.assertEqual("XYZ", file.getContentsAsString());
                });
            });

            runner.testGroup("setContents(String,CharacterEncoding)", () ->
            {
                runner.test("with non-existing file, null contents, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.setContents(null, CharacterEncoding.UTF_8));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });

                runner.test("with existing file, null contents, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.setContents(null, CharacterEncoding.UTF_8));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });

                runner.test("with non-existing file, empty contents, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.setContents("", CharacterEncoding.UTF_8));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });

                runner.test("with existing file, empty contents, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.setContents("", CharacterEncoding.UTF_8));

                    test.assertTrue(file.exists());
                    test.assertEqual("", file.getContentsAsString());
                });

                runner.test("with non-existing file, non-empty contents, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();

                    test.assertTrue(file.setContents("ABC", CharacterEncoding.UTF_8));

                    test.assertTrue(file.exists());
                    test.assertEqual("ABC", file.getContentsAsString());
                });

                runner.test("with existing file, non-empty contents, and non-null encoding", (Test test) ->
                {
                    final File file = getFile();
                    file.create();

                    test.assertTrue(file.setContents("ABC", CharacterEncoding.UTF_8));

                    test.assertTrue(file.exists());
                    test.assertEqual("ABC", file.getContentsAsString());
                });
            });
        });
    }

    private static FileSystem getFileSystem()
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        fileSystem.createRoot("/");
        return fileSystem;
    }

    private static File getFile()
    {
        final FileSystem fileSystem = getFileSystem();
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
