package qub;

public class PathTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Path", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("concatenate()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<String,String,String> concatenateTest = new Action3<String, String, String>()
                        {
                            @Override
                            public void run(final String basePath, final String argumentPath, final String expectedResultPath)
                            {
                                runner.test("with " + runner.escapeAndQuote(basePath) + " and " + runner.escapeAndQuote(argumentPath), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Path path = Path.parse(basePath);
                                        final Path result = path.concatenate(argumentPath);
                                        test.assertEqual(basePath, path.toString());
                                        test.assertEqual(expectedResultPath, result == null ? null : result.toString());
                                    }
                                });
                            }
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
                    }
                });
                
                runner.testGroup("concatenateSegment()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<String,String,String> concatenateSegmentTest = new Action3<String, String, String>()
                        {
                            @Override
                            public void run(final String basePath, final String argumentPath, final String expectedResultPath)
                            {
                                runner.test("with " + runner.escapeAndQuote(basePath) + " and " + runner.escapeAndQuote(argumentPath), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Path path = Path.parse(basePath);
                                        final Path result = path.concatenateSegment(argumentPath);
                                        test.assertEqual(basePath, path.toString());
                                        test.assertEqual(expectedResultPath, result == null ? null : result.toString());
                                    }
                                });
                            }
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
                    }
                });
                
                runner.testGroup("endsWith()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<String,String,Boolean> endsWithTest = new Action3<String, String, Boolean>()
                        {
                            @Override
                            public void run(final String pathString, final String suffix, final Boolean expectedResult)
                            {
                                runner.test("with " + runner.escapeAndQuote(pathString) + " and " + runner.escapeAndQuote(suffix), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Path path = Path.parse(pathString);
                                        test.assertEqual(expectedResult, path.endsWith(suffix));
                                    }
                                });
                            }
                        };
                        
                        endsWithTest.run("apples", null, false);
                        endsWithTest.run("apples", "", false);
                        endsWithTest.run("apples", "sel", false);
                        endsWithTest.run("apples", "les", true);
                    }
                });
                
                runner.testGroup("parse()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Path.parse(null));
                            }
                        });
                        
                        runner.test("with empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                test.assertNull(Path.parse(""));
                            }
                        });

                        runner.test("with " + runner.escapeAndQuote("/hello/there.txt"), new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Path path = Path.parse("/hello/there.txt");
                                test.assertNotNull(path);
                                test.assertEqual("/hello/there.txt", path.toString());
                                test.assertTrue(path.isRooted());
                                test.assertEqual("/", path.getRoot());
                                test.assertEqual(Path.parse("/"), path.getRootPath());
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
                            }
                        });

                        runner.test("with " + runner.escapeAndQuote("/\\/test1//"), new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Path path = Path.parse("/\\/test1//");
                                test.assertNotNull(path);
                                test.assertEqual("/\\/test1//", path.toString());
                                test.assertTrue(path.isRooted());
                                test.assertEqual("/", path.getRoot());
                                test.assertEqual(Path.parse("/"), path.getRootPath());
                                final Indexable<String> pathSegments = path.getSegments();
                                test.assertNotNull(pathSegments);
                                test.assertEqual(new String[] { "/", "test1" }, Array.toStringArray(pathSegments));

                                final Path normalizedPath = path.normalize();
                                test.assertEqual("/test1/", normalizedPath.toString());
                                final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
                                test.assertNotNull(normalizedPathSegments);
                                test.assertEqual(new String[] { "/", "test1" }, Array.toStringArray(normalizedPathSegments));
                            }
                        });

                        runner.test("with " + runner.escapeAndQuote("C:\\Windows\\System32\\cmd.exe"), new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final Path path = Path.parse("C:\\Windows\\System32\\cmd.exe");
                                test.assertNotNull(path);
                                test.assertEqual("C:\\Windows\\System32\\cmd.exe", path.toString());
                                test.assertTrue(path.isRooted());
                                test.assertEqual("C:", path.getRoot());
                                test.assertEqual(Path.parse("C:"), path.getRootPath());
                                final Indexable<String> pathSegments = path.getSegments();
                                test.assertNotNull(pathSegments);
                                test.assertEqual(new String[] { "C:", "Windows", "System32", "cmd.exe" }, Array.toStringArray(pathSegments));

                                final Path normalizedPath = path.normalize();
                                test.assertEqual("C:/Windows/System32/cmd.exe", normalizedPath.toString());
                                final Indexable<String> normalizedPathSegments = normalizedPath.getSegments();
                                test.assertNotNull(normalizedPathSegments);
                                test.assertEqual(new String[] { "C:", "Windows", "System32", "cmd.exe" }, Array.toStringArray(normalizedPathSegments));
                            }
                        });
                    }
                });

                runner.testGroup("hasFileExtension()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action2<String,Boolean> hasFileExtensionTest = new Action2<String, Boolean>()
                        {
                            @Override
                            public void run(final String pathString, final Boolean expectedResult)
                            {
                                runner.test("with " + runner.escapeAndQuote(pathString), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Path path = Path.parse(pathString);
                                        test.assertEqual(expectedResult, path.hasFileExtension());
                                    }
                                });
                            }
                        };

                        hasFileExtensionTest.run("/a/b/c/", false);
                        hasFileExtensionTest.run("folder/file.txt", true);
                        hasFileExtensionTest.run("a.b/c/d", false);
                    }
                });

                runner.testGroup("getFileExtension()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action2<String,String> getFileExtensionTest = new Action2<String, String>()
                        {
                            @Override
                            public void run(final String pathString, final String expectedFileExtension)
                            {
                                runner.test("with " + runner.escapeAndQuote(pathString), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Path path = Path.parse(pathString);
                                        test.assertEqual(expectedFileExtension, path.getFileExtension());
                                    }
                                });
                            }
                        };

                        getFileExtensionTest.run("/a/b/c/", null);
                        getFileExtensionTest.run("folder/file.txt", ".txt");
                        getFileExtensionTest.run("a.b/c/d", null);
                        getFileExtensionTest.run("test.bmp", ".bmp");
                        getFileExtensionTest.run("cats.and.dogs", ".dogs");
                    }
                });

                runner.testGroup("withoutFileExtension()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action2<String,String> withoutFileExtensionTest = new Action2<String, String>()
                        {
                            @Override
                            public void run(final String pathString, final String expectedPathString)
                            {
                                runner.test("with " + runner.escapeAndQuote(pathString), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Path path = Path.parse(pathString);
                                        final Path pathWithoutFileExtension = path.withoutFileExtension();
                                        test.assertEqual(expectedPathString, pathWithoutFileExtension == null ? null : pathWithoutFileExtension.toString());
                                    }
                                });
                            }
                        };

                        withoutFileExtensionTest.run("/a/b/c/", "/a/b/c/");
                        withoutFileExtensionTest.run("folder/file.txt", "folder/file");
                        withoutFileExtensionTest.run("a.b/c/d", "a.b/c/d");
                    }
                });

                runner.testGroup("relativeTo()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<String,String,String> relativeToTest = new Action3<String, String, String>()
                        {
                            @Override
                            public void run(final String pathString, final String basePathString, final String expectedPathString)
                            {
                                runner.test("with " + runner.escapeAndQuote(pathString) + " and " + runner.escapeAndQuote(basePathString), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Path path = Path.parse(pathString);
                                        final Path base = Path.parse(basePathString);
                                        test.assertEqual(Path.parse(expectedPathString), path.relativeTo(base));
                                    }
                                });
                            }
                        };

                        relativeToTest.run("C:/a/b/c.d", null, "C:/a/b/c.d");
                        relativeToTest.run("C:/a/b/c.d", "", "C:/a/b/c.d");
                        relativeToTest.run("C:/a/b/c.d", "C:/a/b/c.d", "C:/a/b/c.d");
                        relativeToTest.run("C:/a/b/c.d", "/folder/", "C:/a/b/c.d");
                        relativeToTest.run("C:/a/b/c.d", "C:/", "a/b/c.d");
                        relativeToTest.run("C:/a/b/c.d", "C:/a/b", "c.d");
                        relativeToTest.run("C:/a/b/c.d", "C:/a/z", "../b/c.d");
                        relativeToTest.run("C:/a/b/c.d", "C:/a/z/y", "../../b/c.d");
                    }
                });
            }
        });
    }
}
