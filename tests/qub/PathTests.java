package qub;

public class PathTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Path.class, () ->
        {
            runner.testGroup("concatenate()", () ->
            {
                final Action3<String,String,String> concatenateTest = (String basePath, String argumentPath, String expectedResultPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePath) + " and " + Strings.escapeAndQuote(argumentPath), (Test test) ->
                    {
                        final Path path = Path.parse(basePath);
                        final Path result = path.concatenate(argumentPath);
                        test.assertEqual(basePath, path.toString());
                        test.assertEqual(expectedResultPath, result == null ? null : result.toString());
                    });
                };
                
                concatenateTest.run("thing", null, "thing");
                concatenateTest.run("thing", "", "thing");
                concatenateTest.run("thing", "segment", "thingsegment");
                concatenateTest.run("thing", "a/b/c", "thinga/b/c");
                concatenateTest.run("thing", "a\\b\\c", "thinga\\b\\c");
                concatenateTest.run("thing", "C:/test/", null);
                
                concatenateTest.run("z/y", null, "z/y");
                concatenateTest.run("z/y", "", "z/y");
                concatenateTest.run("z/y", "segment", "z/ysegment");
                concatenateTest.run("z/y", "a/b/c", "z/ya/b/c");
                concatenateTest.run("z/y", "a\\b\\c", "z/ya\\b\\c");
                concatenateTest.run("z/y", "C:/test/", null);
                
                concatenateTest.run("z\\y", null, "z\\y");
                concatenateTest.run("z\\y", "", "z\\y");
                concatenateTest.run("z\\y", "segment", "z\\ysegment");
                concatenateTest.run("z\\y", "a/b/c", "z\\ya/b/c");
                concatenateTest.run("z\\y", "a\\b\\c", "z\\ya\\b\\c");
                concatenateTest.run("z\\y", "C:/test/", null);
            });
            
            runner.testGroup("concatenateSegment()", () ->
            {
                final Action3<String,String,String> concatenateSegmentTest = (String basePath, String argumentPath, String expectedResultPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePath) + " and " + Strings.escapeAndQuote(argumentPath), (Test test) ->
                    {
                        final Path path = Path.parse(basePath);
                        final Path result = path.concatenateSegment(argumentPath);
                        test.assertEqual(basePath, path.toString());
                        test.assertEqual(expectedResultPath, result == null ? null : result.toString());
                    });
                };
                
                concatenateSegmentTest.run("thing", null, "thing");
                concatenateSegmentTest.run("thing", "", "thing");
                concatenateSegmentTest.run("thing", "segment", "thing/segment");
                concatenateSegmentTest.run("thing", "a/b/c", "thing/a/b/c");
                concatenateSegmentTest.run("thing", "a\\b\\c", "thing/a\\b\\c");
                concatenateSegmentTest.run("thing", "C:/test/", null);
                
                concatenateSegmentTest.run("z/y", null, "z/y");
                concatenateSegmentTest.run("z/y", "", "z/y");
                concatenateSegmentTest.run("z/y", "segment", "z/y/segment");
                concatenateSegmentTest.run("z/y", "a/b/c", "z/y/a/b/c");
                concatenateSegmentTest.run("z/y", "a\\b\\c", "z/y/a\\b\\c");
                concatenateSegmentTest.run("z/y", "C:/test/", null);
                
                concatenateSegmentTest.run("z\\y", null, "z\\y");
                concatenateSegmentTest.run("z\\y", "", "z\\y");
                concatenateSegmentTest.run("z\\y", "segment", "z\\y/segment");
                concatenateSegmentTest.run("z\\y", "a/b/c", "z\\y/a/b/c");
                concatenateSegmentTest.run("z\\y", "a\\b\\c", "z\\y/a\\b\\c");
                concatenateSegmentTest.run("z\\y", "C:/test/", null);

                concatenateSegmentTest.run("y/", null, "y/");
                concatenateSegmentTest.run("y/", "", "y/");
                concatenateSegmentTest.run("y/", "segment", "y/segment");
                concatenateSegmentTest.run("y/", "a/b/c", "y/a/b/c");
                concatenateSegmentTest.run("y/", "a\\b\\c", "y/a\\b\\c");
                concatenateSegmentTest.run("y/", "C:/test/", null);

                concatenateSegmentTest.run("y\\", null, "y\\");
                concatenateSegmentTest.run("y\\", "", "y\\");
                concatenateSegmentTest.run("y\\", "segment", "y\\segment");
                concatenateSegmentTest.run("y\\", "a/b/c", "y\\a/b/c");
                concatenateSegmentTest.run("y\\", "a\\b\\c", "y\\a\\b\\c");
                concatenateSegmentTest.run("y\\", "C:/test/", null);
            });
            
            runner.testGroup("endsWith()", () ->
            {
                final Action3<String,String,Boolean> endsWithTest = (String pathString, String suffix, Boolean expectedResult) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(suffix), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(expectedResult, path.endsWith(suffix));
                    });
                };
                
                endsWithTest.run("apples", null, false);
                endsWithTest.run("apples", "", false);
                endsWithTest.run("apples", "sel", false);
                endsWithTest.run("apples", "les", true);
            });

            runner.testGroup("getRoot()", () ->
            {
                final Action2<String,String> getRootTest = (String pathString, String expectedRoot) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(Path.parse(expectedRoot), path.getRoot());
                    });
                };

                getRootTest.run("blah", null);
                getRootTest.run("/", "/");
                getRootTest.run("\\", "/");
                getRootTest.run("C:/", "C:");
                getRootTest.run("/folder/file.txt", "/");
                getRootTest.run("\\folder\\file.txt", "/");
            });
            
            runner.testGroup("parse()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertNull(Path.parse(null));
                });
                
                runner.test("with empty", (Test test) ->
                {
                    test.assertNull(Path.parse(""));
                });

                runner.test("with " + Strings.escapeAndQuote("/hello/there.txt"), (Test test) ->
                {
                    final Path path = Path.parse("/hello/there.txt");
                    test.assertNotNull(path);
                    test.assertEqual("/hello/there.txt", path.toString());
                    test.assertTrue(path.isRooted());
                    test.assertEqual(Path.parse("/"), path.getRoot());
                    test.assertTrue(path.equals(path));
                    test.assertTrue(path.equals((Object)path));
                    final Indexable<String> pathSegments = path.getSegments();
                    test.assertNotNull(pathSegments);
                    test.assertEqual(new String[] { "/", "hello", "there.txt" }, Array.toStringArray(pathSegments));

                    final Path normalizedPath = path.normalize();
                    test.assertEqual("/hello/there.txt", normalizedPath.toString());
                    test.assertSame(normalizedPath, normalizedPath.normalize());
                    final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
                    test.assertNotNull(normalizedPathSegments);
                    test.assertEqual(new String[] { "/", "hello", "there.txt" }, Array.toStringArray(normalizedPathSegments));
                });

                runner.test("with " + Strings.escapeAndQuote("/\\/test1//"), (Test test) ->
                {
                    final Path path = Path.parse("/\\/test1//");
                    test.assertNotNull(path);
                    test.assertEqual("/\\/test1//", path.toString());
                    test.assertTrue(path.isRooted());
                    test.assertEqual(Path.parse("/"), path.getRoot());
                    final Indexable<String> pathSegments = path.getSegments();
                    test.assertNotNull(pathSegments);
                    test.assertEqual(new String[] { "/", "test1" }, Array.toStringArray(pathSegments));

                    final Path normalizedPath = path.normalize();
                    test.assertEqual("/test1/", normalizedPath.toString());
                    final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
                    test.assertNotNull(normalizedPathSegments);
                    test.assertEqual(new String[] { "/", "test1" }, Array.toStringArray(normalizedPathSegments));
                });

                runner.test("with " + Strings.escapeAndQuote("C:\\Windows\\System32\\cmd.exe"), (Test test) ->
                {
                    final Path path = Path.parse("C:\\Windows\\System32\\cmd.exe");
                    test.assertNotNull(path);
                    test.assertEqual("C:\\Windows\\System32\\cmd.exe", path.toString());
                    test.assertTrue(path.isRooted());
                    test.assertEqual(Path.parse("C:"), path.getRoot());
                    final Indexable<String> pathSegments = path.getSegments();
                    test.assertNotNull(pathSegments);
                    test.assertEqual(new String[] { "C:", "Windows", "System32", "cmd.exe" }, Array.toStringArray(pathSegments));

                    final Path normalizedPath = path.normalize();
                    test.assertEqual("C:/Windows/System32/cmd.exe", normalizedPath.toString());
                    final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
                    test.assertNotNull(normalizedPathSegments);
                    test.assertEqual(new String[] { "C:", "Windows", "System32", "cmd.exe" }, Array.toStringArray(normalizedPathSegments));
                });
            });

            runner.testGroup("hasFileExtension()", () ->
            {
                final Action2<String,Boolean> hasFileExtensionTest = (String pathString, Boolean expectedResult) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(expectedResult, path.hasFileExtension());
                    });
                };

                hasFileExtensionTest.run("/a/b/c/", false);
                hasFileExtensionTest.run("folder/file.txt", true);
                hasFileExtensionTest.run("a.b/c/d", false);
            });

            runner.testGroup("getFileExtension()", () ->
            {
                final Action2<String,String> getFileExtensionTest = (String pathString, String expectedFileExtension) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(expectedFileExtension, path.getFileExtension());
                    });
                };

                getFileExtensionTest.run("/a/b/c/", null);
                getFileExtensionTest.run("folder/file.txt", ".txt");
                getFileExtensionTest.run("a.b/c/d", null);
                getFileExtensionTest.run("test.bmp", ".bmp");
                getFileExtensionTest.run("cats.and.dogs", ".dogs");
            });

            runner.testGroup("withoutFileExtension()", () ->
            {
                final Action2<String,String> withoutFileExtensionTest = (String pathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        final Path pathWithoutFileExtension = path.withoutFileExtension();
                        test.assertEqual(expectedPathString, pathWithoutFileExtension == null ? null : pathWithoutFileExtension.toString());
                    });
                };

                withoutFileExtensionTest.run("/a/b/c/", "/a/b/c/");
                withoutFileExtensionTest.run("folder/file.txt", "folder/file");
                withoutFileExtensionTest.run("a.b/c/d", "a.b/c/d");
            });

            runner.testGroup("relativeTo(Path)", () ->
            {
                final Action3<String,String,String> relativeToTest = (String pathString, String basePathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        final Path base = Path.parse(basePathString);
                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(base));
                    });
                };

                relativeToTest.run("C:/a/b/c.d", null, "C:/a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "", "C:/a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b/c.d", "C:/a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "/folder/", "C:/a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b", "c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z", "../b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z/y", "../../b/c.d");
            });

            runner.testGroup("relativeTo(Folder)", () ->
            {
                final Action3<String,String,String> relativeToTest = (String pathString, String basePathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);

                        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                        final Folder folder = fileSystem.getFolder(basePathString);

                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(folder));
                    });
                };

                relativeToTest.run("C:/a/b/c.d", null, "C:/a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "", "C:/a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b/c.d", "C:/a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "/folder/", "C:/a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b", "c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z", "../b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z/y", "../../b/c.d");
            });
        });
    }
}
