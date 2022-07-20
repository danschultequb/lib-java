package qub;

public interface PathTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Path.class, () ->
        {
            runner.testGroup("parse()", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String pathString, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        test.assertThrows(() -> Path.parse(pathString),
                            expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("pathString cannot be null."));
                parseErrorTest.run("", new PreConditionFailure("pathString cannot be empty."));

                final Action1<String> parseTest = (String pathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertNotNull(path);
                        test.assertEqual(pathString, path.toString());
                        test.assertEqual(pathString.length(), path.length());
                    });
                };

                parseTest.run("/");
                parseTest.run("\\");
                parseTest.run("C:\\");
                parseTest.run("/hello/there.txt");
                parseTest.run("/\\/test1//");
                parseTest.run("/\\/test1.notafileextension//");
                parseTest.run("C:\\Windows\\System32\\cmd.exe");
                parseTest.run("relative/folder/path/");
                parseTest.run("relative\\folder\\path\\");
                parseTest.run("relative/file/path.txt");
                parseTest.run("relative\\file\\path.txt");
            });

            runner.testGroup("getName()", () ->
            {
                final Action2<String,String> getNameTest = (String pathString, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(expected, path.getName());
                    });
                };

                getNameTest.run("/", "/");
                getNameTest.run("\\", "/");
                getNameTest.run("C:\\", "C:");
                getNameTest.run("C:/", "C:");
                getNameTest.run("/hello", "hello");
                getNameTest.run("\\hello", "hello");
                getNameTest.run("/hello/", "hello");
                getNameTest.run("\\hello\\", "hello");
                getNameTest.run("/hello/there.txt", "there.txt");
                getNameTest.run("\\hello\\there.txt", "there.txt");
                getNameTest.run("/hello/there.txt/", "there.txt");
                getNameTest.run("\\hello\\there.txt\\", "there.txt");
            });

            runner.testGroup("getNameWithoutFileExtension()", () ->
            {
                final Action2<String,String> getNameWithoutFileExtensionTest = (String pathString, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(expected, path.getNameWithoutFileExtension());
                    });
                };

                getNameWithoutFileExtensionTest.run("/", "/");
                getNameWithoutFileExtensionTest.run("\\", "/");
                getNameWithoutFileExtensionTest.run("C:\\", "C:");
                getNameWithoutFileExtensionTest.run("C:/", "C:");
                getNameWithoutFileExtensionTest.run("/hello", "hello");
                getNameWithoutFileExtensionTest.run("\\hello", "hello");
                getNameWithoutFileExtensionTest.run("/hello/", "hello");
                getNameWithoutFileExtensionTest.run("\\hello\\", "hello");
                getNameWithoutFileExtensionTest.run("/hello/there.txt", "there");
                getNameWithoutFileExtensionTest.run("\\hello\\there.txt", "there");
                getNameWithoutFileExtensionTest.run("/hello/there.txt/", "there.txt");
                getNameWithoutFileExtensionTest.run("\\hello\\there.txt\\", "there.txt");
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

                hasFileExtensionTest.run("/", false);
                hasFileExtensionTest.run("\\", false);
                hasFileExtensionTest.run("C:/", false);
                hasFileExtensionTest.run("C:\\", false);
                hasFileExtensionTest.run("/hello", false);
                hasFileExtensionTest.run("\\hello", false);
                hasFileExtensionTest.run("C:/hello", false);
                hasFileExtensionTest.run("C:\\hello", false);
                hasFileExtensionTest.run("/hello.jpg", true);
                hasFileExtensionTest.run("\\hello.jpg", true);
                hasFileExtensionTest.run("C:/hello.jpg", true);
                hasFileExtensionTest.run("C:\\hello.jpg", true);
                hasFileExtensionTest.run("/hello/", false);
                hasFileExtensionTest.run("\\hello\\", false);
                hasFileExtensionTest.run("C:/hello/", false);
                hasFileExtensionTest.run("C:\\hello\\", false);
                hasFileExtensionTest.run("/hello.txt/", false);
                hasFileExtensionTest.run("\\hello.txt\\", false);
                hasFileExtensionTest.run("C:/hello.txt/", false);
                hasFileExtensionTest.run("C:\\hello.txt\\", false);
                hasFileExtensionTest.run("/hello/there", false);
                hasFileExtensionTest.run("\\hello\\there", false);
                hasFileExtensionTest.run("C:/hello/there", false);
                hasFileExtensionTest.run("C:\\hello\\there", false);
                hasFileExtensionTest.run("/hello.txt/there", false);
                hasFileExtensionTest.run("\\hello.txt\\there", false);
                hasFileExtensionTest.run("C:/hello.txt/there", false);
                hasFileExtensionTest.run("C:\\hello.txt\\there", false);
            });

            runner.testGroup("getFileExtension()", () ->
            {
                final Action2<String,String> getFileExtensionTest = (String pathString, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(expected, path.getFileExtension());
                    });
                };

                getFileExtensionTest.run("/", null);
                getFileExtensionTest.run("\\", null);
                getFileExtensionTest.run("C:/", null);
                getFileExtensionTest.run("C:\\", null);
                getFileExtensionTest.run("/hello", null);
                getFileExtensionTest.run("\\hello", null);
                getFileExtensionTest.run("C:/hello", null);
                getFileExtensionTest.run("C:\\hello", null);
                getFileExtensionTest.run("/hello.jpg", ".jpg");
                getFileExtensionTest.run("\\hello.jpg", ".jpg");
                getFileExtensionTest.run("C:/hello.jpg", ".jpg");
                getFileExtensionTest.run("C:\\hello.jpg", ".jpg");
                getFileExtensionTest.run("/hello/", null);
                getFileExtensionTest.run("\\hello\\", null);
                getFileExtensionTest.run("C:/hello/", null);
                getFileExtensionTest.run("C:\\hello\\", null);
                getFileExtensionTest.run("/hello.txt/", null);
                getFileExtensionTest.run("\\hello.txt\\", null);
                getFileExtensionTest.run("C:/hello.txt/", null);
                getFileExtensionTest.run("C:\\hello.txt\\", null);
                getFileExtensionTest.run("/hello/there", null);
                getFileExtensionTest.run("\\hello\\there", null);
                getFileExtensionTest.run("C:/hello/there", null);
                getFileExtensionTest.run("C:\\hello\\there", null);
                getFileExtensionTest.run("/hello.txt/there", null);
                getFileExtensionTest.run("\\hello.txt\\there", null);
                getFileExtensionTest.run("C:/hello.txt/there", null);
                getFileExtensionTest.run("C:\\hello.txt\\there", null);
                getFileExtensionTest.run("C:\\hello.txt\\there.my.friend", ".friend");
            });

            runner.testGroup("changeFileExtension(String)", () ->
            {
                final Action3<String,String,Throwable> changeFileExtensionErrorTest = (String pathString, String fileExtension, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(fileExtension), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertThrows(() -> path.changeFileExtension(fileExtension),
                            expectedError);
                    });
                };

                changeFileExtensionErrorTest.run("/", ".gif", new PreConditionFailure("this.endsWith('/') cannot be true."));
                changeFileExtensionErrorTest.run("/", "gif", new PreConditionFailure("this.endsWith('/') cannot be true."));
                changeFileExtensionErrorTest.run("/", "", new PreConditionFailure("this.endsWith('/') cannot be true."));
                changeFileExtensionErrorTest.run("/", null, new PreConditionFailure("this.endsWith('/') cannot be true."));
                changeFileExtensionErrorTest.run("\\", ".gif", new PreConditionFailure("this.endsWith('\\') cannot be true."));
                changeFileExtensionErrorTest.run("\\", "gif", new PreConditionFailure("this.endsWith('\\') cannot be true."));
                changeFileExtensionErrorTest.run("\\", "", new PreConditionFailure("this.endsWith('\\') cannot be true."));
                changeFileExtensionErrorTest.run("\\", null, new PreConditionFailure("this.endsWith('\\') cannot be true."));

                final Action3<String,String,String> changeFileExtensionTest = (String pathString, String fileExtension, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(fileExtension), (Test test) ->
                    {
                        final Path originalPath = Path.parse(pathString);
                        final String originalPathFileExtension = originalPath.getFileExtension();

                        final Path changeFileExtensionResult = originalPath.changeFileExtension(fileExtension);

                        test.assertEqual(originalPathFileExtension, originalPath.getFileExtension());
                        final String expectedNewFileExtension;
                        if (Strings.isNullOrEmpty(fileExtension))
                        {
                            expectedNewFileExtension = null;
                        }
                        else if (fileExtension.startsWith("."))
                        {
                            expectedNewFileExtension = fileExtension;
                        }
                        else
                        {
                            expectedNewFileExtension = "." + fileExtension;
                        }
                        test.assertEqual(expectedNewFileExtension, changeFileExtensionResult.getFileExtension());
                        if (Comparer.equal(originalPathFileExtension, expectedNewFileExtension))
                        {
                            test.assertSame(originalPath, changeFileExtensionResult);
                        }
                        else
                        {
                            test.assertNotSame(originalPath, changeFileExtensionResult);
                        }
                        test.assertEqual(pathString, originalPath.toString());
                        test.assertEqual(expectedPathString, changeFileExtensionResult.toString());
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
                final Action2<String,String> withoutFileExtensionTest = (String originalPathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(originalPathString), (Test test) ->
                    {
                        final Path originalPath = Path.parse(originalPathString);
                        final String originalPathFileExtension = originalPath.getFileExtension();

                        final Path pathWithoutFileExtension = originalPath.withoutFileExtension();

                        test.assertEqual(originalPathString, originalPath.toString());
                        test.assertEqual(originalPathFileExtension, originalPath.getFileExtension());
                        test.assertNotNull(pathWithoutFileExtension);
                        test.assertEqual(expectedPathString, pathWithoutFileExtension.toString());
                        if (originalPath.hasFileExtension())
                        {
                            test.assertNotSame(originalPath, pathWithoutFileExtension);
                        }
                        else
                        {
                            test.assertSame(originalPath, pathWithoutFileExtension);
                        }
                    });
                };

                withoutFileExtensionTest.run("/a/b/c/", "/a/b/c/");
                withoutFileExtensionTest.run("/a/b/c.d/", "/a/b/c.d/");
                withoutFileExtensionTest.run("folder/file.txt", "folder/file");
                withoutFileExtensionTest.run("a.b/c/d", "a.b/c/d");

                withoutFileExtensionTest.run("/", "/");
                withoutFileExtensionTest.run("\\", "\\");
                withoutFileExtensionTest.run("C:/", "C:/");
                withoutFileExtensionTest.run("C:\\", "C:\\");
                withoutFileExtensionTest.run("/hello", "/hello");
                withoutFileExtensionTest.run("\\hello", "\\hello");
                withoutFileExtensionTest.run("C:/hello", "C:/hello");
                withoutFileExtensionTest.run("C:\\hello", "C:\\hello");
                withoutFileExtensionTest.run("/hello.jpg", "/hello");
                withoutFileExtensionTest.run("\\hello.jpg", "/hello");
                withoutFileExtensionTest.run("C:/hello.jpg", "C:/hello");
                withoutFileExtensionTest.run("C:\\hello.jpg", "C:/hello");
                withoutFileExtensionTest.run("/hello/", "/hello/");
                withoutFileExtensionTest.run("\\hello\\", "\\hello\\");
                withoutFileExtensionTest.run("C:/hello/", "C:/hello/");
                withoutFileExtensionTest.run("C:\\hello\\", "C:\\hello\\");
                withoutFileExtensionTest.run("/hello.txt/", "/hello.txt/");
                withoutFileExtensionTest.run("\\hello.txt\\", "\\hello.txt\\");
                withoutFileExtensionTest.run("C:/hello.txt/", "C:/hello.txt/");
                withoutFileExtensionTest.run("C:\\hello.txt\\", "C:\\hello.txt\\");
                withoutFileExtensionTest.run("/hello/there", "/hello/there");
                withoutFileExtensionTest.run("\\hello\\there", "\\hello\\there");
                withoutFileExtensionTest.run("C:/hello/there", "C:/hello/there");
                withoutFileExtensionTest.run("C:\\hello\\there", "C:\\hello\\there");
                withoutFileExtensionTest.run("/hello.txt/there", "/hello.txt/there");
                withoutFileExtensionTest.run("\\hello.txt\\there", "\\hello.txt\\there");
                withoutFileExtensionTest.run("C:/hello.txt/there", "C:/hello.txt/there");
                withoutFileExtensionTest.run("C:\\hello.txt\\there", "C:\\hello.txt\\there");
                withoutFileExtensionTest.run("C:\\hello.txt\\there.my.friend", "C:/hello.txt/there.my");
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
                        test.assertNotNull(pathWithoutRoot);
                        test.assertFalse(pathWithoutRoot.isRooted());
                        test.assertEqual(expectedPathString, pathWithoutRoot.toString());
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

                final Action3<String,String,String> concatenateTest = (String basePath, String argumentPath, String expectedResultPath) ->
                {
                    runner.test("with " + English.andList(Iterable.create(basePath, argumentPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final Path path = Path.parse(basePath);
                        final Path result = path.concatenate(argumentPath);
                        test.assertEqual(basePath, path.toString());
                        test.assertEqual(expectedResultPath, result.toString());
                    });
                };

                concatenateTest.run("thing", "/", "thing/");
                concatenateTest.run("thing", "\\", "thing\\");
                concatenateTest.run("thing", "C:/", "thingC:/");
                concatenateTest.run("thing", "C:\\", "thingC:\\");
                concatenateTest.run("thing", "segment", "thingsegment");
                concatenateTest.run("thing", "a/b/c", "thinga/b/c");
                concatenateTest.run("thing", "a\\b\\c", "thinga\\b\\c");

                concatenateTest.run("z/y", "/", "z/y/");
                concatenateTest.run("z/y", "\\", "z/y\\");
                concatenateTest.run("z/y", "C:/", "z/yC:/");
                concatenateTest.run("z/y", "C:\\", "z/yC:\\");
                concatenateTest.run("z/y", "segment", "z/ysegment");
                concatenateTest.run("z/y", "a/b/c", "z/ya/b/c");
                concatenateTest.run("z/y", "a\\b\\c", "z/ya\\b\\c");

                concatenateTest.run("z\\y", "/", "z\\y/");
                concatenateTest.run("z\\y", "\\", "z\\y\\");
                concatenateTest.run("z\\y", "C:/", "z\\yC:/");
                concatenateTest.run("z\\y", "C:\\", "z\\yC:\\");
                concatenateTest.run("z\\y", "segment", "z\\ysegment");
                concatenateTest.run("z\\y", "a/b/c", "z\\ya/b/c");
                concatenateTest.run("z\\y", "a\\b\\c", "z\\ya\\b\\c");

                concatenateTest.run("/hello", "/", "/hello/");
                concatenateTest.run("/hello", "\\", "/hello\\");
                concatenateTest.run("/hello", "C:/", "/helloC:/");
                concatenateTest.run("/hello", "C:\\", "/helloC:\\");
                concatenateTest.run("/hello", "segment", "/hellosegment");
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

                concatenateTest.run("thing", "/", "thing/");
                concatenateTest.run("thing", "\\", "thing\\");
                concatenateTest.run("thing", "C:/", "thingC:/");
                concatenateTest.run("thing", "C:\\", "thingC:\\");
                concatenateTest.run("thing", "segment", "thingsegment");
                concatenateTest.run("thing", "a/b/c", "thinga/b/c");
                concatenateTest.run("thing", "a\\b\\c", "thinga\\b\\c");

                concatenateTest.run("z/y", "/", "z/y/");
                concatenateTest.run("z/y", "\\", "z/y\\");
                concatenateTest.run("z/y", "C:/", "z/yC:/");
                concatenateTest.run("z/y", "C:\\", "z/yC:\\");
                concatenateTest.run("z/y", "segment", "z/ysegment");
                concatenateTest.run("z/y", "a/b/c", "z/ya/b/c");
                concatenateTest.run("z/y", "a\\b\\c", "z/ya\\b\\c");

                concatenateTest.run("z\\y", "/", "z\\y/");
                concatenateTest.run("z\\y", "\\", "z\\y\\");
                concatenateTest.run("z\\y", "C:/", "z\\yC:/");
                concatenateTest.run("z\\y", "C:\\", "z\\yC:\\");
                concatenateTest.run("z\\y", "segment", "z\\ysegment");
                concatenateTest.run("z\\y", "a/b/c", "z\\ya/b/c");
                concatenateTest.run("z\\y", "a\\b\\c", "z\\ya\\b\\c");

                concatenateTest.run("/hello", "/", "/hello/");
                concatenateTest.run("/hello", "\\", "/hello\\");
                concatenateTest.run("/hello", "C:/", "/helloC:/");
                concatenateTest.run("/hello", "C:\\", "/helloC:\\");
                concatenateTest.run("/hello", "segment", "/hellosegment");
            });

            runner.testGroup("concatenateSegments(String)", () ->
            {
                final Action2<String,Throwable> concatenateSegmentsErrorTest = (String rhs, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rhs), (Test test) ->
                    {
                        final Path path = Path.parse("thing");
                        test.assertThrows(expectedError, () -> path.concatenateSegments(rhs));
                    });
                };

                concatenateSegmentsErrorTest.run(null, new PreConditionFailure("segmentsToConcatenate cannot be null."));
                concatenateSegmentsErrorTest.run("", new PreConditionFailure("segmentsToConcatenate cannot be empty."));
                concatenateSegmentsErrorTest.run("/", new PreConditionFailure("segmentsToConcatenate.isRooted() cannot be true."));
                concatenateSegmentsErrorTest.run("\\", new PreConditionFailure("segmentsToConcatenate.isRooted() cannot be true."));
                concatenateSegmentsErrorTest.run("C:/", new PreConditionFailure("segmentsToConcatenate.isRooted() cannot be true."));
                concatenateSegmentsErrorTest.run("C:\\", new PreConditionFailure("segmentsToConcatenate.isRooted() cannot be true."));

                final Action3<String,String,String> concatenateSegmentsTest = (String basePath, String argumentPath, String expectedResultPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePath) + " and " + Strings.escapeAndQuote(argumentPath), (Test test) ->
                    {
                        final Path path = Path.parse(basePath);
                        final Path result = path.concatenateSegments(argumentPath);
                        test.assertEqual(basePath, path.toString());
                        test.assertEqual(expectedResultPath, result == null ? null : result.toString());
                    });
                };

                concatenateSegmentsTest.run("thing", "segment", "thing/segment");
                concatenateSegmentsTest.run("thing", "a/b/c", "thing/a/b/c");
                concatenateSegmentsTest.run("thing", "a\\b\\c", "thing/a\\b\\c");

                concatenateSegmentsTest.run("z/y", "segment", "z/y/segment");
                concatenateSegmentsTest.run("z/y", "a/b/c", "z/y/a/b/c");
                concatenateSegmentsTest.run("z/y", "a\\b\\c", "z/y/a\\b\\c");

                concatenateSegmentsTest.run("z\\y", "segment", "z\\y/segment");
                concatenateSegmentsTest.run("z\\y", "a/b/c", "z\\y/a/b/c");
                concatenateSegmentsTest.run("z\\y", "a\\b\\c", "z\\y/a\\b\\c");

                concatenateSegmentsTest.run("y/", "segment", "y/segment");
                concatenateSegmentsTest.run("y/", "a/b/c", "y/a/b/c");
                concatenateSegmentsTest.run("y/", "a\\b\\c", "y/a\\b\\c");

                concatenateSegmentsTest.run("y\\", "segment", "y\\segment");
                concatenateSegmentsTest.run("y\\", "a/b/c", "y\\a/b/c");
                concatenateSegmentsTest.run("y\\", "a\\b\\c", "y\\a\\b\\c");

                concatenateSegmentsTest.run("/y", "segment", "/y/segment");
                concatenateSegmentsTest.run("/y", "a/b/c", "/y/a/b/c");
                concatenateSegmentsTest.run("/y", "a\\b\\c", "/y/a\\b\\c");
            });

            runner.testGroup("startsWith(char)", () ->
            {
                final Action3<Path,Character,Boolean> startsWithTest = (Path path, Character prefix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(path, prefix), (Test test) ->
                    {
                        test.assertEqual(expected, path.startsWith(prefix.charValue()));
                    });
                };

                startsWithTest.run(Path.parse("apples"), 'a', true);
                startsWithTest.run(Path.parse("apples"), 'e', false);

                startsWithTest.run(Path.parse("/apples"), 'a', false);
                startsWithTest.run(Path.parse("/apples"), '/', true);
                startsWithTest.run(Path.parse("/apples"), '\\', false);

                startsWithTest.run(Path.parse("\\apples"), 'a', false);
                startsWithTest.run(Path.parse("\\apples"), '/', false);
                startsWithTest.run(Path.parse("\\apples"), '\\', true);

                startsWithTest.run(Path.parse("C:\\apples"), 'a', false);
                startsWithTest.run(Path.parse("C:\\apples"), '/', false);
                startsWithTest.run(Path.parse("C:\\apples"), '\\', false);
                startsWithTest.run(Path.parse("C:\\apples"), 'c', false);
                startsWithTest.run(Path.parse("C:\\apples"), 'C', true);
            });

            runner.testGroup("startsWith(Character)", () ->
            {
                final Action3<Path,Character,Throwable> startsWithErrorTest = (Path path, Character prefix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(path, prefix), (Test test) ->
                    {
                        test.assertThrows(() -> path.startsWith(prefix),
                            expected);
                    });
                };

                startsWithErrorTest.run(Path.parse("apples"), null, new PreConditionFailure("prefix cannot be null."));

                final Action3<Path,Character,Boolean> startsWithTest = (Path path, Character prefix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(path, prefix), (Test test) ->
                    {
                        test.assertEqual(expected, path.startsWith(prefix));
                    });
                };

                startsWithTest.run(Path.parse("apples"), 'a', true);
                startsWithTest.run(Path.parse("apples"), 'e', false);

                startsWithTest.run(Path.parse("/apples"), 'a', false);
                startsWithTest.run(Path.parse("/apples"), '/', true);
                startsWithTest.run(Path.parse("/apples"), '\\', false);

                startsWithTest.run(Path.parse("\\apples"), 'a', false);
                startsWithTest.run(Path.parse("\\apples"), '/', false);
                startsWithTest.run(Path.parse("\\apples"), '\\', true);

                startsWithTest.run(Path.parse("C:\\apples"), 'a', false);
                startsWithTest.run(Path.parse("C:\\apples"), '/', false);
                startsWithTest.run(Path.parse("C:\\apples"), '\\', false);
                startsWithTest.run(Path.parse("C:\\apples"), 'c', false);
                startsWithTest.run(Path.parse("C:\\apples"), 'C', true);
            });

            runner.testGroup("startsWith(String)", () ->
            {
                final Action3<Path,String,Throwable> startsWithErrorTest = (Path path, String prefix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(path, prefix), (Test test) ->
                    {
                        test.assertThrows(() -> path.startsWith(prefix),
                            expected);
                    });
                };

                startsWithErrorTest.run(Path.parse("apples"), null, new PreConditionFailure("prefix cannot be null."));
                startsWithErrorTest.run(Path.parse("apples"), "", new PreConditionFailure("prefix cannot be empty."));

                final Action3<Path,String,Boolean> startsWithTest = (Path path, String prefix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(path, prefix), (Test test) ->
                    {
                        test.assertEqual(expected, path.startsWith(prefix));
                    });
                };

                startsWithTest.run(Path.parse("apples"), "a", true);
                startsWithTest.run(Path.parse("apples"), "ap", true);
                startsWithTest.run(Path.parse("apples"), "app", true);
                startsWithTest.run(Path.parse("apples"), "appl", true);
                startsWithTest.run(Path.parse("apples"), "apple", true);
                startsWithTest.run(Path.parse("apples"), "apples", true);
                startsWithTest.run(Path.parse("apples"), "applesa", false);
                startsWithTest.run(Path.parse("apples"), "e", false);

                startsWithTest.run(Path.parse("/apples"), "a", false);
                startsWithTest.run(Path.parse("/apples"), "/", true);
                startsWithTest.run(Path.parse("/apples"), "/a", true);
                startsWithTest.run(Path.parse("/apples"), "\\", false);
                startsWithTest.run(Path.parse("/apples"), "\\a", false);

                startsWithTest.run(Path.parse("\\apples"), "a", false);
                startsWithTest.run(Path.parse("\\apples"), "/", false);
                startsWithTest.run(Path.parse("\\apples"), "/a", false);
                startsWithTest.run(Path.parse("\\apples"), "\\", true);
                startsWithTest.run(Path.parse("\\apples"), "\\a", true);

                startsWithTest.run(Path.parse("C:\\apples"), "a", false);
                startsWithTest.run(Path.parse("C:\\apples"), "/", false);
                startsWithTest.run(Path.parse("C:\\apples"), "\\", false);
                startsWithTest.run(Path.parse("C:\\apples"), "c", false);
                startsWithTest.run(Path.parse("C:\\apples"), "C", true);
                startsWithTest.run(Path.parse("C:\\apples"), "C:", true);
                startsWithTest.run(Path.parse("C:\\apples"), "C:\\", true);
                startsWithTest.run(Path.parse("C:\\apples"), "C:\\a", true);
            });

            runner.testGroup("startsWith(Path)", () ->
            {
                final Action3<Path,Path,Throwable> startsWithErrorTest = (Path path, Path prefix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(path, prefix), (Test test) ->
                    {
                        test.assertThrows(() -> path.startsWith(prefix),
                            expected);
                    });
                };

                startsWithErrorTest.run(Path.parse("apples"), null, new PreConditionFailure("prefix cannot be null."));

                final Action3<Path,Path,Boolean> startsWithTest = (Path path, Path prefix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(path, prefix), (Test test) ->
                    {
                        test.assertEqual(expected, path.startsWith(prefix));
                    });
                };

                startsWithTest.run(Path.parse("apples"), Path.parse("a"), true);
                startsWithTest.run(Path.parse("apples"), Path.parse("ap"), true);
                startsWithTest.run(Path.parse("apples"), Path.parse("app"), true);
                startsWithTest.run(Path.parse("apples"), Path.parse("appl"), true);
                startsWithTest.run(Path.parse("apples"), Path.parse("apple"), true);
                startsWithTest.run(Path.parse("apples"), Path.parse("apples"), true);
                startsWithTest.run(Path.parse("apples"), Path.parse("applesa"), false);
                startsWithTest.run(Path.parse("apples"), Path.parse("e"), false);

                startsWithTest.run(Path.parse("/apples"), Path.parse("a"), false);
                startsWithTest.run(Path.parse("/apples"), Path.parse("/"), true);
                startsWithTest.run(Path.parse("/apples"), Path.parse("/a"), true);
                startsWithTest.run(Path.parse("/apples"), Path.parse("\\"), false);
                startsWithTest.run(Path.parse("/apples"), Path.parse("\\a"), false);

                startsWithTest.run(Path.parse("\\apples"), Path.parse("a"), false);
                startsWithTest.run(Path.parse("\\apples"), Path.parse("/"), false);
                startsWithTest.run(Path.parse("\\apples"), Path.parse("/a"), false);
                startsWithTest.run(Path.parse("\\apples"), Path.parse("\\"), true);
                startsWithTest.run(Path.parse("\\apples"), Path.parse("\\a"), true);

                startsWithTest.run(Path.parse("C:\\apples"), Path.parse("a"), false);
                startsWithTest.run(Path.parse("C:\\apples"), Path.parse("/"), false);
                startsWithTest.run(Path.parse("C:\\apples"), Path.parse("\\"), false);
                startsWithTest.run(Path.parse("C:\\apples"), Path.parse("c"), false);
                startsWithTest.run(Path.parse("C:\\apples"), Path.parse("C"), true);
                startsWithTest.run(Path.parse("C:\\apples"), Path.parse("C:"), true);
                startsWithTest.run(Path.parse("C:\\apples"), Path.parse("C:\\"), true);
                startsWithTest.run(Path.parse("C:\\apples"), Path.parse("C:\\a"), true);
            });

            runner.testGroup("endsWith(char)", () ->
            {
                final Action3<Path,Character,Boolean> endsWithTest = (Path path, Character suffix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(path, suffix), (Test test) ->
                    {
                        test.assertEqual(expected, path.endsWith(suffix.charValue()));
                    });
                };

                endsWithTest.run(Path.parse("apples"), 's', true);
                endsWithTest.run(Path.parse("apples"), 'e', false);

                endsWithTest.run(Path.parse("apples/"), '/', true);
                endsWithTest.run(Path.parse("apples/"), '\\', false);

                endsWithTest.run(Path.parse("apples\\"), '/', false);
                endsWithTest.run(Path.parse("apples\\"), '\\', true);
            });

            runner.testGroup("endsWith(Character)", () ->
            {
                final Action3<Path,Character,Throwable> endsWithErrorTest = (Path path, Character suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(path, suffix), (Test test) ->
                    {
                        test.assertThrows(() -> path.endsWith(suffix),
                            expected);
                    });
                };

                endsWithErrorTest.run(Path.parse("apples"), null, new PreConditionFailure("suffix cannot be null."));

                final Action3<Path,Character,Boolean> endsWithTest = (Path path, Character suffix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(path, suffix), (Test test) ->
                    {
                        test.assertEqual(expected, path.endsWith(suffix));
                    });
                };

                endsWithTest.run(Path.parse("apples"), 's', true);
                endsWithTest.run(Path.parse("apples"), 'e', false);

                endsWithTest.run(Path.parse("apples/"), '/', true);
                endsWithTest.run(Path.parse("apples/"), '\\', false);

                endsWithTest.run(Path.parse("apples\\"), '/', false);
                endsWithTest.run(Path.parse("apples\\"), '\\', true);
            });

            runner.testGroup("endsWith(String)", () ->
            {
                final Action3<Path,String,RuntimeException> endsWithErrorTest = (Path path, String suffix, RuntimeException expectedError) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> path.endsWith(suffix), expectedError);
                    });
                };

                endsWithErrorTest.run(Path.parse("apples"), null, new PreConditionFailure("suffix cannot be null."));
                endsWithErrorTest.run(Path.parse("apples"), "", new PreConditionFailure("suffix cannot be empty."));

                final Action3<Path,String,Boolean> endsWithTest = (Path path, String suffix, Boolean expectedResult) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expectedResult, path.endsWith(suffix));
                    });
                };

                endsWithTest.run(Path.parse("apples"), "s", true);
                endsWithTest.run(Path.parse("apples"), "es", true);
                endsWithTest.run(Path.parse("apples"), "les", true);
                endsWithTest.run(Path.parse("apples"), "ples", true);
                endsWithTest.run(Path.parse("apples"), "pples", true);
                endsWithTest.run(Path.parse("apples"), "apples", true);
                endsWithTest.run(Path.parse("apples"), "sapples", false);
                endsWithTest.run(Path.parse("apples"), "e", false);

                endsWithTest.run(Path.parse("apples/"), "/", true);
                endsWithTest.run(Path.parse("apples/"), "\\", false);

                endsWithTest.run(Path.parse("apples\\"), "/", false);
                endsWithTest.run(Path.parse("apples\\"), "\\", true);
            });

            runner.testGroup("endsWith(Path)", () ->
            {
                final Action3<Path,Path,Throwable> endsWithErrorTest = (Path path, Path suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> path.endsWith(suffix), expected);
                    });
                };

                endsWithErrorTest.run(Path.parse("apples"), null, new PreConditionFailure("suffix cannot be null."));

                final Action3<Path,Path,Boolean> endsWithTest = (Path path, Path suffix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, path.endsWith(suffix));
                    });
                };

                endsWithTest.run(Path.parse("apples"), Path.parse("s"), true);
                endsWithTest.run(Path.parse("apples"), Path.parse("es"), true);
                endsWithTest.run(Path.parse("apples"), Path.parse("les"), true);
                endsWithTest.run(Path.parse("apples"), Path.parse("ples"), true);
                endsWithTest.run(Path.parse("apples"), Path.parse("pples"), true);
                endsWithTest.run(Path.parse("apples"), Path.parse("apples"), true);
                endsWithTest.run(Path.parse("apples"), Path.parse("sapples"), false);
                endsWithTest.run(Path.parse("apples"), Path.parse("e"), false);

                endsWithTest.run(Path.parse("apples/"), Path.parse("/"), true);
                endsWithTest.run(Path.parse("apples/"), Path.parse("\\"), false);

                endsWithTest.run(Path.parse("apples\\"), Path.parse("/"), false);
                endsWithTest.run(Path.parse("apples\\"), Path.parse("\\"), true);
            });

            runner.testGroup("contains(char)", () ->
            {
                final Action3<Path,Character,Boolean> containsTest = (Path path, Character value, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, value).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, path.contains(value.charValue()));
                    });
                };

                containsTest.run(Path.parse("apples"), 's', true);
                containsTest.run(Path.parse("apples"), 'e', true);
                containsTest.run(Path.parse("apples"), 'b', false);
                containsTest.run(Path.parse("apples"), 'E', false);

                containsTest.run(Path.parse("apples/"), '/', true);
                containsTest.run(Path.parse("apples/"), '\\', false);

                containsTest.run(Path.parse("apples\\"), '/', false);
                containsTest.run(Path.parse("apples\\"), '\\', true);
            });

            runner.testGroup("contains(Character)", () ->
            {
                final Action3<Path,Character,Throwable> containsErrorTest = (Path path, Character value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, value).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> path.contains(value),
                            expected);
                    });
                };

                containsErrorTest.run(Path.parse("apples"), null, new PreConditionFailure("value cannot be null."));

                final Action3<Path,Character,Boolean> containsTest = (Path path, Character value, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, value).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, path.contains(value));
                    });
                };

                containsTest.run(Path.parse("apples"), 's', true);
                containsTest.run(Path.parse("apples"), 'e', true);
                containsTest.run(Path.parse("apples"), 'b', false);
                containsTest.run(Path.parse("apples"), 'E', false);

                containsTest.run(Path.parse("apples/"), '/', true);
                containsTest.run(Path.parse("apples/"), '\\', false);

                containsTest.run(Path.parse("apples\\"), '/', false);
                containsTest.run(Path.parse("apples\\"), '\\', true);
            });

            runner.testGroup("contains(String)", () ->
            {
                final Action3<Path,String,Throwable> containsErrorTest = (Path path, String value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, value).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> path.contains(value),
                            expected);
                    });
                };

                containsErrorTest.run(Path.parse("apples"), null, new PreConditionFailure("value cannot be null."));
                containsErrorTest.run(Path.parse("apples"), "", new PreConditionFailure("value cannot be empty."));

                final Action3<Path,String,Boolean> containsTest = (Path path, String substring, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, substring).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, path.contains(substring));
                    });
                };

                containsTest.run(Path.parse("apples"), "s", true);
                containsTest.run(Path.parse("apples"), "es", true);
                containsTest.run(Path.parse("apples"), "les", true);
                containsTest.run(Path.parse("apples"), "ples", true);
                containsTest.run(Path.parse("apples"), "pples", true);
                containsTest.run(Path.parse("apples"), "apples", true);
                containsTest.run(Path.parse("apples"), "sapples", false);

                containsTest.run(Path.parse("apples"), "p", true);
                containsTest.run(Path.parse("apples"), "pl", true);
                containsTest.run(Path.parse("apples"), "ppl", true);
                containsTest.run(Path.parse("apples"), "pple", true);
                containsTest.run(Path.parse("apples"), "apple", true);

                containsTest.run(Path.parse("apples/"), "/", true);
                containsTest.run(Path.parse("apples/"), "es/", true);
                containsTest.run(Path.parse("apples/"), "\\", false);
                containsTest.run(Path.parse("apples/"), "es\\", false);

                containsTest.run(Path.parse("apples\\"), "/", false);
                containsTest.run(Path.parse("apples\\"), "es/", false);
                containsTest.run(Path.parse("apples\\"), "\\", true);
                containsTest.run(Path.parse("apples\\"), "es\\", true);
            });

            runner.testGroup("contains(Path)", () ->
            {
                final Action3<Path,Path,Throwable> containsErrorTest = (Path path, Path value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, value).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> path.contains(value), expected);
                    });
                };

                containsErrorTest.run(Path.parse("apples"), null, new PreConditionFailure("value cannot be null."));

                final Action3<Path,Path,Boolean> containsTest = (Path path, Path subPath, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(path, subPath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, path.contains(subPath));
                    });
                };

                containsTest.run(Path.parse("apples"), Path.parse("s"), true);
                containsTest.run(Path.parse("apples"), Path.parse("es"), true);
                containsTest.run(Path.parse("apples"), Path.parse("les"), true);
                containsTest.run(Path.parse("apples"), Path.parse("ples"), true);
                containsTest.run(Path.parse("apples"), Path.parse("pples"), true);
                containsTest.run(Path.parse("apples"), Path.parse("apples"), true);
                containsTest.run(Path.parse("apples"), Path.parse("sapples"), false);

                containsTest.run(Path.parse("apples"), Path.parse("p"), true);
                containsTest.run(Path.parse("apples"), Path.parse("pl"), true);
                containsTest.run(Path.parse("apples"), Path.parse("ppl"), true);
                containsTest.run(Path.parse("apples"), Path.parse("pple"), true);
                containsTest.run(Path.parse("apples"), Path.parse("apple"), true);

                containsTest.run(Path.parse("apples/"), Path.parse("/"), true);
                containsTest.run(Path.parse("apples/"), Path.parse("es/"), true);
                containsTest.run(Path.parse("apples/"), Path.parse("\\"), false);
                containsTest.run(Path.parse("apples/"), Path.parse("es\\"), false);

                containsTest.run(Path.parse("apples\\"), Path.parse("/"), false);
                containsTest.run(Path.parse("apples\\"), Path.parse("es/"), false);
                containsTest.run(Path.parse("apples\\"), Path.parse("\\"), true);
                containsTest.run(Path.parse("apples\\"), Path.parse("es\\"), true);
            });

            runner.testGroup("isRooted()", () ->
            {
                final Action2<Path,Boolean> isRootedTest = (Path path, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path.toString()), (Test test) ->
                    {
                        test.assertEqual(expected, path.isRooted());
                    });
                };

                isRootedTest.run(Path.parse("apples"), false);
                isRootedTest.run(Path.parse("apples/"), false);
                isRootedTest.run(Path.parse("apples\\"), false);

                isRootedTest.run(Path.parse("/apples"), true);
                isRootedTest.run(Path.parse("\\apples"), true);

                isRootedTest.run(Path.parse("C:/apples"), true);
                isRootedTest.run(Path.parse("C:\\apples"), true);

                isRootedTest.run(Path.parse("\\\\apples"), true);
            });

            runner.testGroup("getRoot()", () ->
            {
                final Action2<Path,Throwable> getRootErrorTest = (Path path, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path.toString()), (Test test) ->
                    {
                        test.assertThrows(() -> path.getRoot().await(),
                            expected);
                    });
                };

                getRootErrorTest.run(Path.parse("blah"), new NotFoundException("Could not find a root on the path \"blah\"."));
                getRootErrorTest.run(Path.parse("blah/hello"), new NotFoundException("Could not find a root on the path \"blah/hello\"."));
                getRootErrorTest.run(Path.parse("blah\\hello"), new NotFoundException("Could not find a root on the path \"blah\\hello\"."));

                final Action2<Path,Path> getRootTest = (Path path, Path expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        test.assertEqual(expected, path.getRoot().await());
                    });
                };

                getRootTest.run(Path.parse("/"), Path.parse("/"));
                getRootTest.run(Path.parse("\\"), Path.parse("/"));
                getRootTest.run(Path.parse("C:/"), Path.parse("C:"));
                getRootTest.run(Path.parse("C:\\"), Path.parse("C:"));
                getRootTest.run(Path.parse("/folder/file.txt"), Path.parse("/"));
                getRootTest.run(Path.parse("\\folder\\file.txt"), Path.parse("/"));
            });

            runner.testGroup("getParent()", () ->
            {
                final Action2<Path,Throwable> getParentFailureTest = (Path path, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        test.assertThrows(() -> path.getParent().await(),
                            expected);
                    });
                };

                getParentFailureTest.run(Path.parse("/"), new NotFoundException("The path \"/\" doesn't have a parent folder."));
                getParentFailureTest.run(Path.parse("\\"), new NotFoundException("The path \"\\\" doesn't have a parent folder."));
                getParentFailureTest.run(Path.parse("C:/"), new NotFoundException("The path \"C:/\" doesn't have a parent folder."));
                getParentFailureTest.run(Path.parse("D:"), new NotFoundException("The path \"D:\" doesn't have a parent folder."));
                getParentFailureTest.run(Path.parse("/a/../"), new NotFoundException("The path \"/a/../\" doesn't have a parent folder."));
                getParentFailureTest.run(Path.parse("a"), new NotFoundException("The path \"a\" doesn't have a parent folder."));
                getParentFailureTest.run(Path.parse("."), new NotFoundException("The path \".\" doesn't have a parent folder."));
                getParentFailureTest.run(Path.parse(".."), new NotFoundException("The path \"..\" doesn't have a parent folder."));

                final Action2<Path,Path> getParentTest = (Path path, Path expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        test.assertEqual(expected, path.getParent().await());
                    });
                };

                getParentTest.run(Path.parse("a/b"), Path.parse("a/"));
                getParentTest.run(Path.parse("/a/b"), Path.parse("/a/"));
                getParentTest.run(Path.parse("a\\b"), Path.parse("a\\"));
                getParentTest.run(Path.parse("\\a\\b"), Path.parse("\\a\\"));
            });

            runner.testGroup("relativeTo(Folder)", () ->
            {
                final Action3<String,String,Throwable> relativeToErrorTest = (String pathString, String basePathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        final Folder folder = basePathString == null ? null : fileSystem.getFolder(basePathString).await();
                        test.assertThrows(() -> path.relativeTo(folder),
                            expectedError);
                    });
                };

                relativeToErrorTest.run("C:/a/b/c.d", null, new PreConditionFailure("folder cannot be null."));
                relativeToErrorTest.run("C:/a/b/c.d", "/folder/", new PreConditionFailure("basePath.getRoot().await() (/) must be C:."));
                relativeToErrorTest.run("C:/a/b/c.d", "C:/a/b/c.d", new PreConditionFailure("basePath (C:/a/b/c.d/) must not be C:/a/b/c.d."));
                relativeToErrorTest.run("C:/a/b/c.d", "C:/a/b/c.d/", new PreConditionFailure("basePath (C:/a/b/c.d/) must not be C:/a/b/c.d."));
                relativeToErrorTest.run("C:/a/b/c.d/", "C:/a/b/c.d", new PreConditionFailure("basePath (C:/a/b/c.d/) must not be C:/a/b/c.d/."));
                relativeToErrorTest.run("/", "/", new PreConditionFailure("basePath (/) must not be /."));

                final Action3<String,String,String> relativeToTest = (String pathString, String basePathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        final Folder folder = fileSystem.getFolder(basePathString).await();
                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(folder));
                    });
                };

                relativeToTest.run("C:/a/b/c.d", "C:/", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b", "c.d");
                relativeToTest.run("C:/a/b/c.d/", "C:/a/b", "c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b/", "c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z", "../b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z/y", "../../b/c.d");
                relativeToTest.run("/outputs/classes/A.class", "/outputs/", "classes/A.class");
                relativeToTest.run("/outputs/", "/outputs/classes/A.class", "../..");
                relativeToTest.run("/outputs/classes/A.class", "/", "outputs/classes/A.class");
                relativeToTest.run("/", "/outputs/classes/A.class", "../../..");
            });

            runner.testGroup("relativeTo(Root)", () ->
            {
                final Action3<String,String,Throwable> relativeToErrorTest = (String pathString, String basePathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        final Root root = basePathString == null ? null : fileSystem.getRoot(basePathString).await();
                        test.assertThrows(() -> path.relativeTo(root),
                            expectedError);
                    });
                };

                relativeToErrorTest.run("C:/a/b/c.d", null, new PreConditionFailure("root cannot be null."));
                relativeToErrorTest.run("C:/a/b/c.d", "/folder/", new PreConditionFailure("basePath.getRoot().await() (/) must be C:."));
                relativeToErrorTest.run("/", "/", new PreConditionFailure("basePath (/) must not be /."));
                relativeToErrorTest.run("/", "/outputs/classes/A.class", new PreConditionFailure("basePath (/) must not be /."));

                final Action3<String,String,String> relativeToTest = (String pathString, String basePathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);

                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        final Root root = fileSystem.getRoot(basePathString).await();
                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(root));
                    });
                };

                relativeToTest.run("C:/a/b/c.d", "C:/", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d/", "C:/a/b", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b/", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/z/y", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b/c.d", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d", "C:/a/b/c.d/", "a/b/c.d");
                relativeToTest.run("C:/a/b/c.d/", "C:/a/b/c.d", "a/b/c.d");
                relativeToTest.run("/outputs/classes/A.class", "/outputs/", "outputs/classes/A.class");
                relativeToTest.run("/outputs/", "/outputs/classes/A.class", "outputs");
                relativeToTest.run("/outputs/classes/A.class", "/", "outputs/classes/A.class");
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
                relativeToErrorTest.run("C:/a/b/c.d", "hello", new PreConditionFailure("basePath.isRooted() cannot be false."));
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

            runner.testGroup("relativeTo(String)", () ->
            {
                final Action3<String,String,Throwable> relativeToErrorTest = (String pathString, String basePathString, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertThrows(() -> path.relativeTo(basePathString), expectedError);
                    });
                };

                relativeToErrorTest.run("C:/a/b/c.d", null, new PreConditionFailure("basePath cannot be null."));
                relativeToErrorTest.run("C:/a/b/c.d", "", new PreConditionFailure("basePath cannot be empty."));
                relativeToErrorTest.run("C:/a/b/c.d", "hello", new PreConditionFailure("basePath.isRooted() cannot be false."));
                relativeToErrorTest.run("C:/a/b/c.d", "/folder/", new PreConditionFailure("basePath.getRoot().await() (/) must be C:."));
                relativeToErrorTest.run("C:/a/b/c.d", "C:/a/b/c.d", new PreConditionFailure("basePath (C:/a/b/c.d) must not be C:/a/b/c.d."));
                relativeToErrorTest.run("/", "/", new PreConditionFailure("basePath (/) must not be /."));

                final Action3<String,String,String> relativeToTest = (String pathString, String basePathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString) + " and " + Strings.escapeAndQuote(basePathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(basePathString));
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
                normalizeTest.run("relative/path", "relative/path");
                normalizeTest.run("/rooted/path/with/../parent/and/./self/references", "/rooted/path/with/../parent/and/./self/references");
                normalizeTest.run("\\\\this\\is\\a\\UNC\\path", "/this/is/a/UNC/path");
            });

            runner.testGroup("resolve()", () ->
            {
                final Action2<String,Throwable> resolveErrorTest = (String pathString, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        test.assertThrows(() -> path.resolve().await(),
                            expected);
                    });
                };

                resolveErrorTest.run("/..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveErrorTest.run("\\..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveErrorTest.run("C:/..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveErrorTest.run("F:\\..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveErrorTest.run("/a/../..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveErrorTest.run("\\b/../..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveErrorTest.run("C:/c/../..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveErrorTest.run("F:\\d/../..", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                resolveErrorTest.run("a/../../b", new PreConditionFailure("index (-1) must be between 0 and 1."));


                final Action2<String,String> resolveTest = (String pathString, String expectedResolvedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(pathString), (Test test) ->
                    {
                        final Path path = Path.parse(pathString);
                        final Path resolvedPath = path.resolve().await();
                        test.assertEqual(Path.parse(expectedResolvedPathString), resolvedPath);
                        test.assertEqual(expectedResolvedPathString, resolvedPath.toString());
                    });
                };

                resolveTest.run("/", "/");
                resolveTest.run("\\", "/");
                resolveTest.run("C:/", "C:/");
                resolveTest.run("C:\\", "C:/");
                resolveTest.run("/apples", "/apples");
                resolveTest.run("/apples/", "/apples/");
                resolveTest.run("/apples\\", "/apples/");
                resolveTest.run("C:/bananas/", "C:/bananas/");
                resolveTest.run("/a/b/c/../d/./e", "/a/b/d/e");
                resolveTest.run("/.", "/");
                resolveTest.run("/folder/../", "/");
                resolveTest.run("C:/a/../b/../c/..", "C:/");
                resolveTest.run("C:/a/../b/../c/../", "C:/");
                resolveTest.run("C:/a/../test.txt", "C:/test.txt");
                resolveTest.run("C:\\a\\../test.txt", "C:/test.txt");
                resolveTest.run("hello", "hello");
                resolveTest.run("hello/there", "hello/there");
                resolveTest.run("a/../b", "b");
            });

            runner.testGroup("resolve(String)", () ->
            {
                final Action3<String,String,Throwable> resolveErrorTest = (String basePathString, String argumentPathString, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePathString) + " and " + Strings.escapeAndQuote(argumentPathString), (Test test) ->
                    {
                        final Path basePath = Path.parse(basePathString);
                        test.assertThrows(() -> basePath.resolve(argumentPathString).await(),
                            expected);
                    });
                };

                resolveErrorTest.run("/", null, new PreConditionFailure("relativePath cannot be null."));
                resolveErrorTest.run("/", "", new PreConditionFailure("relativePath cannot be empty."));
                resolveErrorTest.run("/", "C:/", new PreConditionFailure("relativePath.isRooted() cannot be true."));
                resolveErrorTest.run("C:\\a\\b\\..\\c", "../../../test.txt", new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<String,String,String> resolveTest = (String basePathString, String argumentPathString, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePathString) + " and " + Strings.escapeAndQuote(argumentPathString), (Test test) ->
                    {
                        final Path basePath = Path.parse(basePathString);
                        final Path expectedResolvedPath = Strings.isNullOrEmpty(expectedPathString) ? null : Path.parse(expectedPathString);
                        final Result<Path> result = basePath.resolve(argumentPathString);
                        test.assertEqual(expectedResolvedPath, result.await());
                        test.assertEqual(expectedPathString, result.await().toString());
                    });
                };

                resolveTest.run("/", "a/b", "/a/b");
                resolveTest.run("D:\\z\\", "t/u.png", "D:/z/t/u.png");
                resolveTest.run("/a/b/c", "..", "/a/b");
                resolveTest.run("C:\\a\\b\\..\\c", "../../test.txt", "C:/test.txt");

            });

            runner.testGroup("resolve(Path)", () ->
            {
                final Action3<Path,Path,Throwable> resolveErrorTest = (Path basePath, Path relativePath, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(basePath, relativePath).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> basePath.resolve(relativePath).await(),
                            expected);
                    });
                };

                resolveErrorTest.run(Path.parse("/"), null, new PreConditionFailure("relativePath cannot be null."));
                resolveErrorTest.run(Path.parse("/"), Path.parse("C:/"), new PreConditionFailure("relativePath.isRooted() cannot be true."));
                resolveErrorTest.run(Path.parse("C:\\a\\b\\..\\c"), Path.parse("../../../test.txt"), new IllegalArgumentException("Cannot resolve a rooted path outside of its root."));

                final Action3<Path,Path,String> resolveTest = (Path basePath, Path relativePath, String expectedPathString) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(basePath) + " and " + Strings.escapeAndQuote(relativePath), (Test test) ->
                    {
                        final Result<Path> result = basePath.resolve(relativePath);
                        test.assertEqual(Path.parse(expectedPathString), result.await());
                        test.assertEqual(expectedPathString, result.await().toString());
                    });
                };

                resolveTest.run(Path.parse("/"), Path.parse("a/b"), "/a/b");
                resolveTest.run(Path.parse("D:\\z\\"), Path.parse("t/u.png"), "D:/z/t/u.png");
                resolveTest.run(Path.parse("/a/b/c"), Path.parse(".."), "/a/b");
                resolveTest.run(Path.parse("C:\\a\\b\\..\\c"), Path.parse("../../test.txt"), "C:/test.txt");

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
                isAncestorOfErrorTest.run("/", "", new PreConditionFailure("possibleDescendantPathString cannot be empty."));
                isAncestorOfErrorTest.run("/../hello", "/im/here", new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                isAncestorOfErrorTest.run("/a/b/c/", "/a/b/c/d/../../../../../f", new java.lang.IllegalArgumentException("Cannot resolve a rooted path outside of its root."));
                isAncestorOfErrorTest.run("hello", "/im/here", new PreConditionFailure("this.isRooted() cannot be false."));

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
                isAncestorOfErrorTest.run("hello", Path.parse("/im/here"), new PreConditionFailure("this.isRooted() cannot be false."));

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

            runner.testGroup("getSegments()", () ->
            {
                final Action2<Path,Iterable<String>> getSegmentsTest = (Path path, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        final Indexable<String> segments = path.getSegments();
                        test.assertEqual(expected, segments);
                        test.assertSame(segments, path.getSegments());
                    });
                };

                getSegmentsTest.run(Path.parse("/"), Iterable.create("/"));
                getSegmentsTest.run(Path.parse("\\"), Iterable.create("/"));
                getSegmentsTest.run(Path.parse("c:"), Iterable.create("c:"));
                getSegmentsTest.run(Path.parse("C:"), Iterable.create("C:"));
                getSegmentsTest.run(Path.parse("mydrive:"), Iterable.create("mydrive:"));
                getSegmentsTest.run(Path.parse("mydrive:/"), Iterable.create("mydrive:"));
                getSegmentsTest.run(Path.parse("mydrive:\\"), Iterable.create("mydrive:"));
                getSegmentsTest.run(Path.parse("/a"), Iterable.create("/", "a"));
                getSegmentsTest.run(Path.parse("C:/a"), Iterable.create("C:", "a"));
                getSegmentsTest.run(Path.parse("/a/b/c.txt"), Iterable.create("/", "a", "b", "c.txt"));
                getSegmentsTest.run(Path.parse("b/c.txt"), Iterable.create("b", "c.txt"));
                getSegmentsTest.run(Path.parse("/a/.././../c.txt"), Iterable.create("/", "a", "..", ".", "..", "c.txt"));
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
        });
    }
}
