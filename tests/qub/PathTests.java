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
                
                concatenateTest.run("thing", "segment", "thingsegment");
                concatenateTest.run("thing", "a/b/c", "thinga/b/c");
                concatenateTest.run("thing", "a\\b\\c", "thinga\\b\\c");
                concatenateTest.run("thing", "C:/test/", null);
                
                concatenateTest.run("z/y", "segment", "z/ysegment");
                concatenateTest.run("z/y", "a/b/c", "z/ya/b/c");
                concatenateTest.run("z/y", "a\\b\\c", "z/ya\\b\\c");
                concatenateTest.run("z/y", "C:/test/", null);
                
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
                
                concatenateSegmentTest.run("thing", "segment", "thing/segment");
                concatenateSegmentTest.run("thing", "a/b/c", "thing/a/b/c");
                concatenateSegmentTest.run("thing", "a\\b\\c", "thing/a\\b\\c");
                concatenateSegmentTest.run("thing", "C:/test/", null);
                
                concatenateSegmentTest.run("z/y", "segment", "z/y/segment");
                concatenateSegmentTest.run("z/y", "a/b/c", "z/y/a/b/c");
                concatenateSegmentTest.run("z/y", "a\\b\\c", "z/y/a\\b\\c");
                concatenateSegmentTest.run("z/y", "C:/test/", null);
                
                concatenateSegmentTest.run("z\\y", "segment", "z\\y/segment");
                concatenateSegmentTest.run("z\\y", "a/b/c", "z\\y/a/b/c");
                concatenateSegmentTest.run("z\\y", "a\\b\\c", "z\\y/a\\b\\c");
                concatenateSegmentTest.run("z\\y", "C:/test/", null);

                concatenateSegmentTest.run("y/", "segment", "y/segment");
                concatenateSegmentTest.run("y/", "a/b/c", "y/a/b/c");
                concatenateSegmentTest.run("y/", "a\\b\\c", "y/a\\b\\c");
                concatenateSegmentTest.run("y/", "C:/test/", null);

                concatenateSegmentTest.run("y\\", "segment", "y\\segment");
                concatenateSegmentTest.run("y\\", "a/b/c", "y\\a/b/c");
                concatenateSegmentTest.run("y\\", "a\\b\\c", "y\\a\\b\\c");
                concatenateSegmentTest.run("y\\", "C:/test/", null);
            });

            runner.testGroup("endsWith(char)", () ->
            {
                runner.test("with \"apples\" and \'s\'", (Test test) ->
                {
                    test.assertTrue(Path.parse("apples").endsWith('s'));
                });

                runner.test("with \"apples\" and \'e\'", (Test test) ->
                {
                    test.assertFalse(Path.parse("apples").endsWith('e'));
                });
            });

            runner.testGroup("endsWith(char)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Path.parse("apples").endsWith((Character)null), new PreConditionFailure("suffix cannot be null."));
                });

                runner.test("with \"apples\" and \'s\'", (Test test) ->
                {
                    test.assertTrue(Path.parse("apples").endsWith(Character.valueOf('s')));
                });

                runner.test("with \"apples\" and \'e\'", (Test test) ->
                {
                    test.assertFalse(Path.parse("apples").endsWith(Character.valueOf('e')));
                });
            });
            
            runner.testGroup("endsWith(String)", () ->
            {
                final Action3<String,String,RuntimeException> endsWithErrorTest = (String pathString, String suffix, RuntimeException expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(suffix), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertThrows(() -> path.endsWith(suffix), expectedError);
                    });
                };

                endsWithErrorTest.run("apples", null, new PreConditionFailure("suffix cannot be null."));
                endsWithErrorTest.run("apples", "", new PreConditionFailure("suffix cannot be empty."));

                final Action3<String,String,Boolean> endsWithTest = (String pathString, String suffix, Boolean expectedResult) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(suffix), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(expectedResult, path.endsWith(suffix));
                    });
                };
                
                endsWithTest.run("apples", "sel", false);
                endsWithTest.run("apples", "les", true);
            });

            runner.testGroup("getRoot()", () ->
            {
                runner.test("with " + Strings.escapeAndQuote("blah"), (Test test) ->
                {
                    final Path path = Path.parse("blah");
                    test.assertThrows(() -> path.getRoot().await(),
                        new NotFoundException("Could not find a root on the path \"blah\"."));
                });

                final Action2<String,String> getRootTest = (String pathString, String expectedRoot) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(Path.parse(expectedRoot), path.getRoot().await());
                    });
                };

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
                    test.assertThrows(() -> Path.parse(null), new PreConditionFailure("pathString cannot be null."));
                });
                
                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> Path.parse(""), new PreConditionFailure("pathString cannot be empty."));
                });

                runner.test("with " + Strings.escapeAndQuote("/hello/there.txt"), (Test test) ->
                {
                    final Path path = Path.parse("/hello/there.txt");
                    test.assertNotNull(path);
                    test.assertEqual("/hello/there.txt", path.toString());
                    test.assertTrue(path.isRooted());
                    test.assertEqual(Path.parse("/"), path.getRoot().await());
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
                    test.assertEqual(Path.parse("/"), path.getRoot().await());
                    final Indexable<String> pathSegments = path.getSegments();
                    test.assertNotNull(pathSegments);
                    test.assertEqual(new String[] { "/", "test1" }, Array.toStringArray(pathSegments));

                    final Path normalizedPath = path.normalize();
                    test.assertEqual("/test1", normalizedPath.toString());
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
                    test.assertEqual(Path.parse("C:"), path.getRoot().await());
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

            runner.testGroup("changeFileExtension(String)", () ->
            {
                final Action3<String,String,RuntimeException> changeFileExtensionErrorTest = (String originalPathString, String fileExtension, RuntimeException expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(originalPathString) + " and " + Strings.escapeAndQuote(fileExtension), (Test test) ->
                    {
                        final Path path = Path.parse(originalPathString);
                        test.assertThrows(() -> path.changeFileExtension(fileExtension), expectedError);
                    });
                };

                changeFileExtensionErrorTest.run("/", ".gif", new PreConditionFailure("endsWith('/') cannot be true."));
                changeFileExtensionErrorTest.run("/", "gif", new PreConditionFailure("endsWith('/') cannot be true."));
                changeFileExtensionErrorTest.run("/", "", new PreConditionFailure("endsWith('/') cannot be true."));
                changeFileExtensionErrorTest.run("/", null, new PreConditionFailure("endsWith('/') cannot be true."));
                changeFileExtensionErrorTest.run("\\", ".gif", new PreConditionFailure("endsWith('\\') cannot be true."));
                changeFileExtensionErrorTest.run("\\", "gif", new PreConditionFailure("endsWith('\\') cannot be true."));
                changeFileExtensionErrorTest.run("\\", "", new PreConditionFailure("endsWith('\\') cannot be true."));
                changeFileExtensionErrorTest.run("\\", null, new PreConditionFailure("endsWith('\\') cannot be true."));

                final Action3<String,String,String> changeFileExtensionTest = (String originalPathString, String fileExtension, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(originalPathString) + " and " + Strings.escapeAndQuote(fileExtension), (Test test) ->
                    {
                        test.assertEqual(expectedPathString, Path.parse(originalPathString).changeFileExtension(fileExtension).toString());
                    });
                };

                changeFileExtensionTest.run("/a.txt", ".gif", "/a.gif");
                changeFileExtensionTest.run("/a.txt", "gif", "/a.gif");
                changeFileExtensionTest.run("/a.txt", "", "/a");
                changeFileExtensionTest.run("/a.txt", null, "/a");
                changeFileExtensionTest.run("/a", ".gif", "/a.gif");
                changeFileExtensionTest.run("/a", "gif", "/a.gif");
                changeFileExtensionTest.run("/a", "", "/a");
                changeFileExtensionTest.run("/a", null, "/a");
                changeFileExtensionTest.run("/b.c/a.txt", ".gif", "/b.c/a.gif");
                changeFileExtensionTest.run("/b.c/a.txt", "gif", "/b.c/a.gif");
                changeFileExtensionTest.run("/b.c/a.txt", "", "/b.c/a");
                changeFileExtensionTest.run("/b.c/a.txt", null, "/b.c/a");
                changeFileExtensionTest.run("/b.c/a", ".gif", "/b.c/a.gif");
                changeFileExtensionTest.run("/b.c/a", "gif", "/b.c/a.gif");
                changeFileExtensionTest.run("/b.c/a", "", "/b.c/a");
                changeFileExtensionTest.run("/b.c/a", null, "/b.c/a");
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

            runner.testGroup("withoutRoot()", () ->
            {
                final Action2<String,Throwable> withoutRootErrorTest = (String pathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertThrows(() -> path.withoutRoot().await(),
                            expectedError);
                    });
                };

                withoutRootErrorTest.run("/", new NotFoundException("The path \"/\" cannot create a path without its root because it only contains a root path."));
                withoutRootErrorTest.run("\\", new NotFoundException("The path \"\\\" cannot create a path without its root because it only contains a root path."));
                withoutRootErrorTest.run("C:", new NotFoundException("The path \"C:\" cannot create a path without its root because it only contains a root path."));
                withoutRootErrorTest.run("C:/", new NotFoundException("The path \"C:/\" cannot create a path without its root because it only contains a root path."));
                withoutRootErrorTest.run("C:\\", new NotFoundException("The path \"C:\\\" cannot create a path without its root because it only contains a root path."));

                final Action2<String,String> withoutRootTest = (String pathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        final Path pathWithoutRoot = path.withoutRoot().await();
                        test.assertEqual(expectedPathString, pathWithoutRoot == null ? null : pathWithoutRoot.toString());
                    });
                };

                withoutRootTest.run(":", ":");
                withoutRootTest.run("a.txt", "a.txt");
                withoutRootTest.run("a/b.txt", "a/b.txt");
                withoutRootTest.run("a\\b.txt", "a\\b.txt");
                withoutRootTest.run("/a.txt", "a.txt");
                withoutRootTest.run("\\a.txt", "a.txt");
                withoutRootTest.run("/a/b.txt", "a/b.txt");
                withoutRootTest.run("\\a\\b.txt", "a\\b.txt");
                withoutRootTest.run("C:/a.txt", "a.txt");
                withoutRootTest.run("C:\\a.txt", "a.txt");
                withoutRootTest.run("C:/a/b.txt", "a/b.txt");
                withoutRootTest.run("C:\\a\\b.txt", "a\\b.txt");
                withoutRootTest.run("F:/test/folder", "test/folder");
            });

            runner.testGroup("relativeTo(Path)", () ->
            {
                runner.test("with " + Strings.escapeAndQuote("C:/a/b/c.d") + " and null", (Test test) ->
                {
                    final Path path = Path.parse("C:/a/b/c.d");
                    test.assertThrows(() -> path.relativeTo((Path)null), new PreConditionFailure("basePath cannot be null."));
                });

                final Action3<String,String,String> relativeToTest = (String pathString, String basePathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        final Path base = Path.parse(basePathString);
                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(base));
                    });
                };

                relativeToTest.run("C:/a/b/c.d", "C:/a/b/c.d", ".");
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

                        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock(), test::getParallelAsyncRunner);
                        final Folder folder = fileSystem.getFolder(basePathString).await();
                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(folder));
                    });
                };

                relativeToTest.run("C:/a/b/c.d", "C:/a/b/c.d", ".");
                relativeToTest.run("C:/a/b/c.d", "/folder/", "C:/a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b", "c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z", "../b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z/y", "../../b/c.d");
            });

            runner.testGroup("resolve()", () ->
            {
                final Action3<String,String,Throwable> resolveTest = (String pathString, String expectedResolvedPathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> path.resolve().await(), expectedError);
                        }
                        else
                        {
                            test.assertEqual(Path.parse(expectedResolvedPathString), path.resolve().await());
                        }
                    });
                };

                resolveTest.run("/", "/", null);
                resolveTest.run("\\", "/", null);
                resolveTest.run("C:/", "C:/", null);
                resolveTest.run("C:\\", "C:/", null);
                resolveTest.run("/apples", "/apples", null);
                resolveTest.run("/apples/", "/apples/", null);
                resolveTest.run("/apples\\", "/apples/", null);
                resolveTest.run("C:/bananas/", "C:/bananas/", null);
                resolveTest.run("/a/b/c/../d/./e", "/a/b/d/e", null);
                resolveTest.run("/.", "/", null);
                resolveTest.run("/folder/../", "/", null);
                resolveTest.run("C:/a/../b/../c/..", "C:/", null);
                resolveTest.run("C:/a/../b/../c/../", "C:/", null);
                resolveTest.run("C:/a/../test.txt", "C:/test.txt", null);
                resolveTest.run("C:\\a\\../test.txt", "C:/test.txt", null);
                resolveTest.run("/..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveTest.run("\\..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveTest.run("C:/..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveTest.run("F:\\..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveTest.run("/a/../..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveTest.run("\\b/../..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveTest.run("C:/c/../..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveTest.run("F:\\d/../..", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
            });

            runner.testGroup("resolve(String)", () ->
            {
                final Action3<String,String,Throwable> resolveFailureTest = (String basePathString, String argumentPathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePathString) + " and " + Strings.escapeAndQuote(argumentPathString), (Test test) ->
                    {
                        final Path basePath = Path.parse(basePathString);
                        test.assertThrows(() -> basePath.resolve(argumentPathString), expectedError);
                    });
                };

                resolveFailureTest.run("/", null, new PreConditionFailure("pathString cannot be null."));
                resolveFailureTest.run("/", "", new PreConditionFailure("pathString cannot be empty."));
                resolveFailureTest.run("/", "C:/", new PreConditionFailure("relativePath.isRooted() cannot be true."));

                final Action4<String,String,String,Throwable> resolveTest = (String basePathString, String argumentPathString, String expectedPathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePathString) + " and " + Strings.escapeAndQuote(argumentPathString), (Test test) ->
                    {
                        final Path basePath = Path.parse(basePathString);
                        final Path expectedResolvedPath = Strings.isNullOrEmpty(expectedPathString) ? null : Path.parse(expectedPathString);
                        final Result<Path> result = basePath.resolve(argumentPathString);
                        if (expectedError != null)
                        {
                            test.assertThrows(result::await, expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedResolvedPath, result.await());
                        }
                    });
                };

                resolveTest.run("/", "a/b", "/a/b", null);
                resolveTest.run("D:\\z\\", "t/u.png", "D:/z/t/u.png", null);
                resolveTest.run("/a/b/c", "..", "/a/b", null);
                resolveTest.run("C:\\a\\b\\..\\c", "../../test.txt", "C:/test.txt", null);
                resolveTest.run("C:\\a\\b\\..\\c", "../../../test.txt", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
            });
        });
    }
}
