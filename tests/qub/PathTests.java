package qub;

public interface PathTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Path.class, () ->
        {
            runner.testGroup("concatenate(String)", () ->
            {
                final Action2<String,Throwable> concatenateErrorTest = (String rhs, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rhs), (Test test) ->
                    {
                        final Path path = Path.parse("thing");
                        test.assertThrows(expectedError, () -> path.concatenate(rhs));
                    });
                };

                concatenateErrorTest.run(null, new PreConditionFailure("toConcatenate cannot be null."));
                concatenateErrorTest.run("", new PreConditionFailure("toConcatenate cannot be empty."));
                concatenateErrorTest.run("/", new PreConditionFailure("toConcatenate.isRooted() cannot be true."));

                final Action3<String,String,String> concatenateTest = (String basePath, String argumentPath, String expectedResultPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePath) + " and " + Strings.escapeAndQuote(argumentPath), (Test test) ->
                    {
                        final Path path = Path.parse(basePath);
                        final Path result = path.concatenate(argumentPath);
                        test.assertEqual(basePath, path.toString());
                        test.assertEqual(expectedResultPath, result.toString());
                    });
                };

                concatenateTest.run("thing", "segment", "thingsegment");
                concatenateTest.run("thing", "a/b/c", "thinga/b/c");
                concatenateTest.run("thing", "a\\b\\c", "thinga\\b\\c");

                concatenateTest.run("z/y", "segment", "z/ysegment");
                concatenateTest.run("z/y", "a/b/c", "z/ya/b/c");
                concatenateTest.run("z/y", "a\\b\\c", "z/ya\\b\\c");

                concatenateTest.run("z\\y", "segment", "z\\ysegment");
                concatenateTest.run("z\\y", "a/b/c", "z\\ya/b/c");
                concatenateTest.run("z\\y", "a\\b\\c", "z\\ya\\b\\c");
            });

            runner.testGroup("concatenate(Path)", () ->
            {
                final Action2<Path,Throwable> concatenateErrorTest = (Path rhs, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rhs), (Test test) ->
                    {
                        final Path path = Path.parse("thing");
                        test.assertThrows(expectedError, () -> path.concatenate(rhs));
                    });
                };

                concatenateErrorTest.run(null, new PreConditionFailure("toConcatenate cannot be null."));
                concatenateErrorTest.run(Path.parse("/"), new PreConditionFailure("toConcatenate.isRooted() cannot be true."));


                final Action3<String,String,String> concatenateTest = (String basePath, String argumentPath, String expectedResultPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePath) + " and " + Strings.escapeAndQuote(argumentPath), (Test test) ->
                    {
                        final Path path = Path.parse(basePath);
                        final Path result = path.concatenate(Path.parse(argumentPath));
                        test.assertEqual(basePath, path.toString());
                        test.assertEqual(expectedResultPath, result.toString());
                    });
                };
                
                concatenateTest.run("thing", "segment", "thingsegment");
                concatenateTest.run("thing", "a/b/c", "thinga/b/c");
                concatenateTest.run("thing", "a\\b\\c", "thinga\\b\\c");
                
                concatenateTest.run("z/y", "segment", "z/ysegment");
                concatenateTest.run("z/y", "a/b/c", "z/ya/b/c");
                concatenateTest.run("z/y", "a\\b\\c", "z/ya\\b\\c");
                
                concatenateTest.run("z\\y", "segment", "z\\ysegment");
                concatenateTest.run("z\\y", "a/b/c", "z\\ya/b/c");
                concatenateTest.run("z\\y", "a\\b\\c", "z\\ya\\b\\c");
            });
            
            runner.testGroup("concatenateSegment(String)", () ->
            {
                final Action2<String,Throwable> concatenateSegmentErrorTest = (String rhs, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rhs), (Test test) ->
                    {
                        final Path path = Path.parse("thing");
                        test.assertThrows(expectedError, () -> path.concatenateSegment(rhs));
                    });
                };

                concatenateSegmentErrorTest.run(null, new PreConditionFailure("segmentToConcatenate cannot be null."));
                concatenateSegmentErrorTest.run("", new PreConditionFailure("segmentToConcatenate cannot be empty."));
                concatenateSegmentErrorTest.run("/", new PreConditionFailure("segmentToConcatenate.isRooted() cannot be true."));

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
                
                concatenateSegmentTest.run("z/y", "segment", "z/y/segment");
                concatenateSegmentTest.run("z/y", "a/b/c", "z/y/a/b/c");
                concatenateSegmentTest.run("z/y", "a\\b\\c", "z/y/a\\b\\c");
                
                concatenateSegmentTest.run("z\\y", "segment", "z\\y/segment");
                concatenateSegmentTest.run("z\\y", "a/b/c", "z\\y/a/b/c");
                concatenateSegmentTest.run("z\\y", "a\\b\\c", "z\\y/a\\b\\c");

                concatenateSegmentTest.run("y/", "segment", "y/segment");
                concatenateSegmentTest.run("y/", "a/b/c", "y/a/b/c");
                concatenateSegmentTest.run("y/", "a\\b\\c", "y/a\\b\\c");

                concatenateSegmentTest.run("y\\", "segment", "y\\segment");
                concatenateSegmentTest.run("y\\", "a/b/c", "y\\a/b/c");
                concatenateSegmentTest.run("y\\", "a\\b\\c", "y\\a\\b\\c");
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

            runner.testGroup("getParent()", () ->
            {
                final Action2<String,Throwable> getParentFailureTest = (String pathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertThrows(expectedError, () -> path.getParent().await());
                    });
                };

                getParentFailureTest.run("/", new NotFoundException("The path \"/\" doesn't have a parent folder."));
                getParentFailureTest.run("\\", new NotFoundException("The path \"\\\" doesn't have a parent folder."));
                getParentFailureTest.run("C:/", new NotFoundException("The path \"C:/\" doesn't have a parent folder."));
                getParentFailureTest.run("D:", new NotFoundException("The path \"D:\" doesn't have a parent folder."));
                getParentFailureTest.run("/a/../", new NotFoundException("The path \"/a/../\" doesn't have a parent folder."));
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
                    test.assertEqual(Iterable.create("/", "hello", "there.txt"), pathSegments);

                    final Path normalizedPath = path.normalize();
                    test.assertEqual("/hello/there.txt", normalizedPath.toString());
                    test.assertSame(normalizedPath, normalizedPath.normalize());
                    final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
                    test.assertNotNull(normalizedPathSegments);
                    test.assertEqual(Iterable.create("/", "hello", "there.txt"), normalizedPathSegments);
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
                    test.assertEqual(Iterable.create("/", "test1"), pathSegments);

                    final Path normalizedPath = path.normalize();
                    test.assertEqual("/test1/", normalizedPath.toString());
                    final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
                    test.assertNotNull(normalizedPathSegments);
                    test.assertEqual(Iterable.create("/", "test1"), normalizedPathSegments);
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
                    test.assertEqual(Iterable.create("C:", "Windows", "System32", "cmd.exe"), pathSegments);

                    final Path normalizedPath = path.normalize();
                    test.assertEqual("C:/Windows/System32/cmd.exe", normalizedPath.toString());
                    final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
                    test.assertNotNull(normalizedPathSegments);
                    test.assertEqual(Iterable.create("C:", "Windows", "System32", "cmd.exe"), normalizedPathSegments);
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

                changeFileExtensionTest.run("\\a.txt", ".txt", "\\a.txt");
                changeFileExtensionTest.run("/a.txt", ".txt", "/a.txt");
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
                final Action3<String,String,Throwable> relativeToErrorTest = (String pathString, String basePathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        final Path base = basePathString == null ? null : Path.parse(basePathString);
                        test.assertThrows(() -> path.relativeTo(base), expectedError);
                    });
                };

                relativeToErrorTest.run("C:/a/b/c.d", null, new PreConditionFailure("basePath cannot be null."));
                relativeToErrorTest.run("C:/a/b/c.d", "/folder/", new PreConditionFailure("basePath.getRoot().await() (/) must be C:."));
                relativeToErrorTest.run("C:/a/b/c.d", "C:/a/b/c.d", new PreConditionFailure("basePath (C:/a/b/c.d) must not be C:/a/b/c.d."));
                relativeToErrorTest.run("/", "/", new PreConditionFailure("basePath (/) must not be /."));

                final Action3<String,String,String> relativeToTest = (String pathString, String basePathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        final Path base = Path.parse(basePathString);
                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(base));
                    });
                };

                relativeToTest.run("C:/a/b/c.d", "C:/", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b", "c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z", "../b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z/y", "../../b/c.d");
                relativeToTest.run("/outputs/classes/A.class", "/outputs/", "classes/A.class");
                relativeToTest.run("/outputs/", "/outputs/classes/A.class", "../..");
                relativeToTest.run("/outputs/classes/A.class", "/", "outputs/classes/A.class");
                relativeToTest.run("/", "/outputs/classes/A.class", "../../..");
            });

            runner.testGroup("relativeTo(Folder)", () ->
            {
                final Action3<String,String,Throwable> relativeToErrorTest = (String pathString, String basePathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        final Folder folder = basePathString == null ? null : fileSystem.getFolder(basePathString).await();
                        test.assertThrows(() -> path.relativeTo(folder),
                            expectedError);
                    });
                };

                relativeToErrorTest.run("C:/a/b/c.d", null, new PreConditionFailure("folder cannot be null."));
                relativeToErrorTest.run("C:/a/b/c.d", "/folder/", new PreConditionFailure("basePath.getRoot().await() (/) must be C:."));
                relativeToErrorTest.run("C:/a/b/c.d", "C:/a/b/c.d", new PreConditionFailure("basePath (C:/a/b/c.d/) must not be C:/a/b/c.d."));
                relativeToErrorTest.run("/", "/", new PreConditionFailure("basePath (/) must not be /."));

                final Action3<String,String,String> relativeToTest = (String pathString, String basePathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        final Folder folder = fileSystem.getFolder(basePathString).await();
                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(folder));
                    });
                };

                relativeToTest.run("C:/a/b/c.d", "C:/", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b", "c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z", "../b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z/y", "../../b/c.d");
                relativeToTest.run("/outputs/classes/A.class", "/outputs/", "classes/A.class");
                relativeToTest.run("/outputs/", "/outputs/classes/A.class", "../..");
                relativeToTest.run("/outputs/classes/A.class", "/", "outputs/classes/A.class");
                relativeToTest.run("/", "/outputs/classes/A.class", "../../..");
            });

            runner.testGroup("normalize()", () ->
            {
                final Action2<String,String> normalizeTest = (String pathString, String expectedNormalizedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        final Path normalizedPath = path.normalize();
                        test.assertEqual(Path.parse(expectedNormalizedPathString), normalizedPath);
                        test.assertEqual(expectedNormalizedPathString, normalizedPath.toString());
                    });
                };

                normalizeTest.run("C:", "C:/");
                normalizeTest.run("D:/", "D:/");
                normalizeTest.run("E:\\", "E:/");
                normalizeTest.run("/", "/");
                normalizeTest.run("\\", "/");
                normalizeTest.run("F:/a", "F:/a");
                normalizeTest.run("G:\\b", "G:/b");
                normalizeTest.run("/c", "/c");
                normalizeTest.run("\\d", "/d");
                normalizeTest.run("H:/e/", "H:/e/");
                normalizeTest.run("I:\\f\\", "I:/f/");
                normalizeTest.run("J:/g\\", "J:/g/");
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
                            final Path resolvedPath = path.resolve().await();
                            test.assertEqual(Path.parse(expectedResolvedPathString), resolvedPath);
                            test.assertEqual(expectedResolvedPathString, resolvedPath.toString());
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
                            test.assertEqual(expectedPathString, result.await().toString());
                        }
                    });
                };

                resolveTest.run("/", "a/b", "/a/b", null);
                resolveTest.run("D:\\z\\", "t/u.png", "D:/z/t/u.png", null);
                resolveTest.run("/a/b/c", "..", "/a/b", null);
                resolveTest.run("C:\\a\\b\\..\\c", "../../test.txt", "C:/test.txt", null);
                resolveTest.run("C:\\a\\b\\..\\c", "../../../test.txt", null, new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<String,Object,Boolean> equalsTest = (String lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhs) + " and " + Strings.escapeAndQuote(Objects.toString(rhs)), (Test test) ->
                    {
                        final Path lhsPath = Path.parse(lhs);
                        test.assertEqual(lhsPath.equals(rhs), expected);
                    });
                };

                equalsTest.run("/", null, false);
                equalsTest.run("/", "", false);
                equalsTest.run("/", "/", true);
                equalsTest.run("/", Path.parse("/"), true);
                equalsTest.run("/", "/a/..", true);
                equalsTest.run("/", "/a/b/../../", true);
                equalsTest.run("/a", "/a/", false);
                equalsTest.run("/a", "/b", false);
                equalsTest.run("/a", "\\a", true);
            });

            runner.testGroup("isAncestorOf(String)", () ->
            {
                final Action3<String,String,Throwable> isAncestorOfErrorTest = (String lhsPathString, String rhsPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhsPathString) + " and " + Strings.escapeAndQuote(rhsPath), (Test test) ->
                    {
                        final Path lhsPath = Path.parse(lhsPathString);
                        test.assertThrows(expectedError, () -> lhsPath.isAncestorOf(rhsPath).await());
                    });
                };

                isAncestorOfErrorTest.run("/", null, new PreConditionFailure("possibleDescendantPathString cannot be null."));
                isAncestorOfErrorTest.run("/../hello", "/im/here", new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                isAncestorOfErrorTest.run("/a/b/c/", "/a/b/c/d/../../../../../f", new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,String,Boolean> isAncestorOfTest = (String lhsPathString, String rhsPathString, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhsPathString) + " and " + Strings.escapeAndQuote(rhsPathString), (Test test) ->
                    {
                        final Path lhsPath = Path.parse(lhsPathString);
                        test.assertEqual(expected, lhsPath.isAncestorOf(rhsPathString).await());
                    });
                };

                isAncestorOfTest.run("/", "/", false);
                isAncestorOfTest.run("/", "/hello", true);
                isAncestorOfTest.run("/hello", "/", false);
                isAncestorOfTest.run("/hello/there/", "C:/hello/there/you", false);
                isAncestorOfTest.run("/hello/", "/HELLO/there", false);
                isAncestorOfTest.run("/a/b/c/", "/a/b/c/d/e/f", true);
                isAncestorOfTest.run("/a/b/c/", "/a/b/c/d/../f", true);
                isAncestorOfTest.run("/a/b/c/", "/a/b/c/d/./f", true);
                isAncestorOfTest.run("/a/b/c/", "/a/b/c/d/../../../f", false);
            });

            runner.testGroup("isAncestorOf(Path)", () ->
            {
                final Action3<String,Path,Throwable> isAncestorOfErrorTest = (String lhsPathString, Path rhsPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhsPathString) + " and " + Strings.escapeAndQuote(Objects.toString(rhsPath)), (Test test) ->
                    {
                        final Path lhsPath = Path.parse(lhsPathString);
                        test.assertThrows(expectedError, () -> lhsPath.isAncestorOf(rhsPath).await());
                    });
                };

                isAncestorOfErrorTest.run("/", null, new PreConditionFailure("possibleDescendantPath cannot be null."));
                isAncestorOfErrorTest.run("/../hello", Path.parse("/im/here"), new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                isAncestorOfErrorTest.run("/a/b/c/", Path.parse("/a/b/c/d/../../../../../f"), new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,String,Boolean> isAncestorOfTest = (String lhsPathString, String rhsPathString, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhsPathString) + " and " + Strings.escapeAndQuote(rhsPathString), (Test test) ->
                    {
                        final Path lhsPath = Path.parse(lhsPathString);
                        final Path rhsPath = Path.parse(rhsPathString);
                        test.assertEqual(expected, lhsPath.isAncestorOf(rhsPath).await());
                    });
                };

                isAncestorOfTest.run("/", "/", false);
                isAncestorOfTest.run("/", "/hello", true);
                isAncestorOfTest.run("/hello", "/", false);
                isAncestorOfTest.run("/hello/there/", "C:/hello/there/you", false);
                isAncestorOfTest.run("/hello/", "/HELLO/there", false);
                isAncestorOfTest.run("/a/b/c/", "/a/b/c/d/e/f", true);
                isAncestorOfTest.run("/a/b/c/", "/a/b/c/d/../f", true);
                isAncestorOfTest.run("/a/b/c/", "/a/b/c/d/./f", true);
                isAncestorOfTest.run("/a/b/c/", "/a/b/c/d/../../../f", false);
            });

            runner.testGroup("isDescendantOf(String)", () ->
            {
                final Action3<String,String,Throwable> isDescendantOfErrorTest = (String lhsPathString, String rhsPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhsPathString) + " and " + Strings.escapeAndQuote(rhsPath), (Test test) ->
                    {
                        final Path lhsPath = Path.parse(lhsPathString);
                        test.assertThrows(expectedError, () -> lhsPath.isDescendantOf(rhsPath).await());
                    });
                };

                isDescendantOfErrorTest.run("/", null, new PreConditionFailure("possibleAncestorPathString cannot be null."));
                isDescendantOfErrorTest.run("/../hello", "/im/here", new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                isDescendantOfErrorTest.run("/a/b/c/", "/a/b/c/d/../../../../../f", new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,String,Boolean> isDescendantOfTest = (String lhsPathString, String rhsPathString, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhsPathString) + " and " + Strings.escapeAndQuote(rhsPathString), (Test test) ->
                    {
                        final Path lhsPath = Path.parse(lhsPathString);
                        test.assertEqual(expected, lhsPath.isDescendantOf(rhsPathString).await());
                    });
                };

                isDescendantOfTest.run("/", "/", false);
                isDescendantOfTest.run("/", "/hello", false);
                isDescendantOfTest.run("/hello", "/", true);
                isDescendantOfTest.run("/hello/there/", "C:/hello/there/you", false);
                isDescendantOfTest.run("/hello/", "/HELLO/there", false);
                isDescendantOfTest.run("/a/b/c/", "/a/b/c/d/e/f", false);
                isDescendantOfTest.run("/a/b/c/", "/a/b/c/d/../f", false);
                isDescendantOfTest.run("/a/b/c/", "/a/b/c/d/./f", false);
                isDescendantOfTest.run("/a/b/c/", "/a/b/c/d/../../../f", false);
            });

            runner.testGroup("isDescendantOf(Path)", () ->
            {
                final Action3<String,Path,Throwable> isDescendantOfErrorTest = (String lhsPathString, Path rhsPath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhsPathString) + " and " + Strings.escapeAndQuote(Objects.toString(rhsPath)), (Test test) ->
                    {
                        final Path lhsPath = Path.parse(lhsPathString);
                        test.assertThrows(expectedError, () -> lhsPath.isDescendantOf(rhsPath).await());
                    });
                };

                isDescendantOfErrorTest.run("/", null, new PreConditionFailure("possibleAncestorPath cannot be null."));
                isDescendantOfErrorTest.run("/../hello", Path.parse("/im/here"), new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                isDescendantOfErrorTest.run("/a/b/c/", Path.parse("/a/b/c/d/../../../../../f"), new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,String,Boolean> isDescendantOfTest = (String lhsPathString, String rhsPathString, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhsPathString) + " and " + Strings.escapeAndQuote(rhsPathString), (Test test) ->
                    {
                        final Path lhsPath = Path.parse(lhsPathString);
                        final Path rhsPath = Path.parse(rhsPathString);
                        test.assertEqual(expected, lhsPath.isDescendantOf(rhsPath).await());
                    });
                };

                isDescendantOfTest.run("/", "/", false);
                isDescendantOfTest.run("/", "/hello", false);
                isDescendantOfTest.run("/hello", "/", true);
                isDescendantOfTest.run("/hello/there/", "C:/hello/there/you", false);
                isDescendantOfTest.run("/hello/", "/HELLO/there", false);
                isDescendantOfTest.run("/a/b/c/", "/a/b/c/d/e/f", false);
                isDescendantOfTest.run("/a/b/c/", "/a/b/c/d/../f", false);
                isDescendantOfTest.run("/a/b/c/", "/a/b/c/d/./f", false);
                isDescendantOfTest.run("/a/b/c/", "/a/b/c/d/../../../f", false);
            });
        });
    }
}
