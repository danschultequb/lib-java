package qub;

public class ProcessBuilderTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("ProcessBuilder", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final ProcessBuilder builder = new ProcessBuilder(null, null);
                        test.assertNull(builder.getExecutableFile());
                        test.assertEqual(0, builder.getArgumentCount());
                        test.assertEqual("", builder.getCommand());
                    }
                });

                runner.testGroup("addArgument()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ProcessBuilder builder = new ProcessBuilder(null, null);
                                builder.addArgument(null);
                                test.assertEqual(0, builder.getArgumentCount());
                                test.assertEqual("", builder.getCommand());
                            }
                        });

                        runner.test("with empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ProcessBuilder builder = new ProcessBuilder(null, null);
                                builder.addArgument("");
                                test.assertEqual(0, builder.getArgumentCount());
                                test.assertEqual("", builder.getCommand());
                            }
                        });

                        runner.test("with non-empty", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ProcessBuilder builder = new ProcessBuilder(null, null);
                                builder.addArgument("test");
                                test.assertEqual(1, builder.getArgumentCount());
                                test.assertEqual("test", builder.getArgument(0));
                                test.assertEqual("test", builder.getCommand());
                            }
                        });
                    }
                });

                runner.testGroup("addArguments()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no arguments", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ProcessBuilder builder = new ProcessBuilder(null, null);
                                builder.addArguments();
                                test.assertEqual(0, builder.getArgumentCount());
                                test.assertEqual("", builder.getCommand());
                            }
                        });

                        runner.test("with one null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ProcessBuilder builder = new ProcessBuilder(null, null);
                                builder.addArguments((String)null);
                                test.assertEqual(0, builder.getArgumentCount());
                                test.assertEqual("", builder.getCommand());
                            }
                        });
                    }
                });

                runner.testGroup("setArgument()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with negative index", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ProcessBuilder builder = new ProcessBuilder(null, null);
                                builder.setArgument(-1, "test");
                                test.assertEqual(0, builder.getArgumentCount());
                                test.assertEqual("", builder.getCommand());
                            }
                        });

                        runner.test("with null value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ProcessBuilder builder = new ProcessBuilder(null, null);
                                builder.addArguments("a", "b", "c");
                                builder.setArgument(0, null);
                                test.assertEqual(Array.fromValues(new String[] { "b", "c" }), builder.getArguments());
                                test.assertEqual("b c", builder.getCommand());
                            }
                        });

                        runner.test("with emtpy value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ProcessBuilder builder = new ProcessBuilder(null, null);
                                builder.addArguments("a", "b", "c");
                                builder.setArgument(2, "");
                                test.assertEqual(Array.fromValues(new String[] { "a", "b", "" }), builder.getArguments());
                                test.assertEqual("a b \"\"", builder.getCommand());
                            }
                        });

                        runner.test("with non-empty value", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ProcessBuilder builder = new ProcessBuilder(null, null);
                                builder.addArguments("a", "b", "c");
                                builder.setArgument(1, "\"d\"");
                                test.assertEqual(Array.fromValues(new String[] { "a", "\"d\"", "c" }), builder.getArguments());
                                test.assertEqual("a \"d\" c", builder.getCommand());
                            }
                        });
                    }
                });

                runner.test("removeArgumentAt()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final ProcessBuilder builder = new ProcessBuilder(null, null);
                        builder.addArguments("a", "b", "c");
                        builder.removeArgument(1);
                        test.assertEqual(Array.fromValues(new String[] { "a", "c" }), builder.getArguments());
                        test.assertEqual("a c", builder.getCommand());
                    }
                });

                runner.test("run() with not found executable file", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final JavaFileSystem fileSystem = new JavaFileSystem();
                        final File javacFile = fileSystem.getFile("C:/idontexist.exe");
                        final ProcessBuilder builder = new ProcessBuilder(null, javacFile);
                        builder.addArgument("won't matter");
                        test.assertEqual(null, builder.run());
                        test.assertEqual("C:/idontexist.exe \"won't matter\"", builder.getCommand());
                    }
                });

                runner.test("escapeArgument()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        test.assertEqual("\"Then he said, \\\"Hey there!\\\"\"", ProcessBuilder.escapeArgument("Then he said, \"Hey there!\""));
                        test.assertEqual("-argument=\"value\"", ProcessBuilder.escapeArgument("-argument=\"value\""));
                        test.assertEqual("\"\"", ProcessBuilder.escapeArgument(""));
                    }
                });
            }
        });
    }
}
