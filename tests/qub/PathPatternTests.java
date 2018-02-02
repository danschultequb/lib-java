package qub;

public class PathPatternTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("PathPattern", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("parse(Path)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action1<String> parsePathTest = new Action1<String>()
                        {
                            @Override
                            public void run(final String pathString)
                            {
                                runner.test("with " + runner.escapeAndQuote(pathString), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Path path = Path.parse(pathString);
                                        
                                        final PathPattern pattern = PathPattern.parse(path);
                                        test.assertTrue(pattern instanceof SimplePathPattern);
                                        test.assertNotNull(pattern);

                                        final String pathString = (path == null ? null : path.toString());
                                        test.assertEqual(pathString, pattern.toString());
                                        test.assertTrue(pattern.isMatch(path));
                                        test.assertTrue(pattern.isMatch(pathString));
                                    }
                                });
                            }
                        };
                        
                        parsePathTest.run(null);
                        parsePathTest.run("");
                        parsePathTest.run("/folder/subfolder");
                    }
                });
                
                runner.testGroup("parse(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action1<String> parseStringTest = new Action1<String>()
                        {
                            @Override
                            public void run(final String pathString)
                            {
                                runner.test("with " + runner.escapeAndQuote(pathString), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final PathPattern pattern = PathPattern.parse(pathString);
                                        test.assertTrue(pattern instanceof SimplePathPattern);
                                        test.assertNotNull(pattern);

                                        test.assertEqual(pathString, pattern.toString());
                                        test.assertTrue(pattern.isMatch(pathString));
                                    }
                                });
                            }
                        };
                        
                        parseStringTest.run(null);
                        parseStringTest.run("");
                        parseStringTest.run("abcd");
                        parseStringTest.run("\\");
                        parseStringTest.run("/");
                        parseStringTest.run("*");
                        parseStringTest.run("*abc");
                        parseStringTest.run("**");
                        parseStringTest.run("**test");
                    }
                });
                
                runner.testGroup("isMatch(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null pattern", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final PathPattern pattern = PathPattern.parse((String)null);

                                test.assertTrue(pattern.isMatch((String)null));
                                test.assertTrue(pattern.isMatch(""));
                                test.assertFalse(pattern.isMatch("a"));

                                test.assertTrue(pattern.isMatch((Path)null));
                                test.assertTrue(pattern.isMatch(Path.parse("")));
                                test.assertFalse(pattern.isMatch(Path.parse("a")));
                            }
                        });

                        runner.test("with " + runner.escapeAndQuote("abc") + " pattern", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final PathPattern pattern = PathPattern.parse("abc");

                                test.assertTrue(pattern.isMatch("abc"));
                                test.assertTrue(pattern.isMatch(Path.parse("abc")));

                                test.assertFalse(pattern.isMatch("ab"));
                                test.assertFalse(pattern.isMatch("abcd"));
                            }
                        });

                        runner.test("with " + runner.escapeAndQuote("a/b") + " pattern", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final PathPattern pattern = PathPattern.parse("a/b");

                                test.assertTrue(pattern.isMatch("a/b"));
                                test.assertTrue(pattern.isMatch(Path.parse("a/b")));
                                test.assertTrue(pattern.isMatch("a\\b"));
                                test.assertTrue(pattern.isMatch(Path.parse("a\\b")));

                                test.assertFalse(pattern.isMatch("a/b/"));
                                test.assertFalse(pattern.isMatch("a\\b\\"));
                            }
                        });

                        runner.test("with " + runner.escapeAndQuote("*") + " pattern", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final PathPattern pattern = PathPattern.parse("*");

                                test.assertTrue(pattern.isMatch(""));
                                test.assertTrue(pattern.isMatch(".java"));
                                test.assertTrue(pattern.isMatch("Test.java"));
                                test.assertTrue(pattern.isMatch("..java"));
                                test.assertTrue(pattern.isMatch("Test.jav"));
                                test.assertTrue(pattern.isMatch("Test.javas"));

                                test.assertFalse(pattern.isMatch("/"));
                                test.assertFalse(pattern.isMatch("\\"));
                            }
                        });

                        runner.test("with " + runner.escapeAndQuote("*.java") + " pattern", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final PathPattern pattern = PathPattern.parse("*.java");

                                test.assertTrue(pattern.isMatch(".java"));
                                test.assertTrue(pattern.isMatch("Test.java"));
                                test.assertTrue(pattern.isMatch("..java"));

                                test.assertFalse(pattern.isMatch(""));
                                test.assertFalse(pattern.isMatch("Test.jav"));
                                test.assertFalse(pattern.isMatch("Test.javas"));
                                test.assertFalse(pattern.isMatch("/Test.java"));
                                test.assertFalse(pattern.isMatch("\\Test.java"));
                            }
                        });

                        runner.test("with " + runner.escapeAndQuote("sources/**.java") + " pattern", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final PathPattern pattern = PathPattern.parse("sources/**.java");

                                test.assertTrue(pattern.isMatch("sources/Test.java"));
                                test.assertTrue(pattern.isMatch("sources/qub/Test.java"));
                                test.assertTrue(pattern.isMatch("sources\\qub\\package\\Class.java"));

                                test.assertFalse(pattern.isMatch("output/Test.java"));
                            }
                        });
                    }
                });

                runner.test("equals()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final PathPattern pattern = PathPattern.parse("a/b/c");

                        test.assertFalse(pattern.equals((Object)null));
                        test.assertFalse(pattern.equals((PathPattern)null));
                        test.assertFalse(pattern.equals("path pattern"));
                        test.assertFalse(pattern.equals(PathPattern.parse("a/b")));

                        test.assertTrue(pattern.equals(pattern));
                        test.assertTrue(pattern.equals(PathPattern.parse(pattern.toString())));
                    }
                });
            }
        });
    }
}
