package qub;

public class CommandLineTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("CommandLine", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("constructor()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no arguments", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine();
                                test.assertEqual(new String[0], commandLine.getArgumentStrings());
                                test.assertFalse(commandLine.any());
                                test.assertEqual(0, commandLine.getCount());
                                test.assertNull(commandLine.get(0));
                            }
                        });
                        
                        runner.test("with null String[]", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine((String[])null);
                                test.assertNull(commandLine.getArgumentStrings());
                                test.assertFalse(commandLine.any());
                                test.assertEqual(0, commandLine.getCount());
                                test.assertNull(commandLine.get(0));
                            }
                        });
                        
                        runner.test("with empty String[]", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine(new String[0]);
                                test.assertEqual(new String[0], commandLine.getArgumentStrings());
                                test.assertFalse(commandLine.any());
                                test.assertEqual(0, commandLine.getCount());
                                test.assertNull(commandLine.get(0));
                            }
                        });

                        runner.test("with String argument list", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("a", "b", "c", "d");
                                test.assertEqual(new String[] { "a", "b", "c", "d" }, commandLine.getArgumentStrings());
                                test.assertTrue(commandLine.any());
                                test.assertEqual(4, commandLine.getCount());
                                test.assertNotNull(commandLine.get(0));
                                test.assertEqual(new String[] { "a", "b", "c", "d" }, Array.toStringArray(commandLine.map(new Function1<CommandLineArgument, String>()
                                {
                                    @Override
                                    public String run(CommandLineArgument arg1)
                                    {
                                        return arg1.toString();
                                    }
                                })));
                            }
                        });
                    }
                });

                runner.testGroup("getValue()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null arguments and null name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine((String[])null);
                                test.assertNull(commandLine.getValue(null));
                            }
                        });

                        runner.test("with null arguments and empty name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine((String[])null);
                                test.assertNull(commandLine.getValue(""));
                            }
                        });

                        runner.test("with null arguments and non-empty name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine((String[])null);
                                test.assertNull(commandLine.getValue("spud"));
                            }
                        });

                        runner.test("with empty arguments and null name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine(new String[0]);
                                test.assertNull(commandLine.getValue(null));
                            }
                        });

                        runner.test("with empty arguments and empty name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine(new String[0]);
                                test.assertNull(commandLine.getValue(""));
                            }
                        });

                        runner.test("with empty arguments and non-empty name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine(new String[0]);
                                test.assertNull(commandLine.getValue("spud"));
                            }
                        });

                        runner.test("with non-empty arguments and null name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("hello", "there");
                                test.assertNull(commandLine.getValue(null));
                            }
                        });

                        runner.test("with non-empty arguments and empty name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("hello", "there");
                                test.assertNull(commandLine.getValue(""));
                            }
                        });

                        runner.test("with non-empty arguments and non-matching name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("hello", "there");
                                test.assertNull(commandLine.getValue("spud"));
                            }
                        });

                        runner.test("with single-dash arguments and null name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("-hello", "-there");
                                test.assertNull(commandLine.getValue(null));
                            }
                        });

                        runner.test("with single-dash arguments and empty name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("-hello", "-there");
                                test.assertNull(commandLine.getValue(""));
                            }
                        });

                        runner.test("with single-dash arguments and non-matching name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("-hello", "-there");
                                test.assertNull(commandLine.getValue("spud"));
                            }
                        });

                        runner.test("with single-dash arguments and matching name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("-hello", "there");
                                final CommandLineArgument argument = commandLine.get("hello");
                                test.assertNotNull(argument);
                                test.assertEqual("-hello", argument.toString());
                                test.assertEqual("hello", argument.getName());
                                test.assertNull(argument.getValue());
                            }
                        });

                        runner.test("with single-dash and equals sign argument and null name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("-name=value");
                                test.assertNull(commandLine.getValue(null));
                            }
                        });

                        runner.test("with single-dash and equals sign argument and empty name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("-name=value");
                                test.assertNull(commandLine.getValue(""));
                            }
                        });

                        runner.test("with single-dash and equals sign argument and non-matching name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("-name=value");
                                test.assertNull(commandLine.getValue("spud"));
                            }
                        });

                        runner.test("with single-dash and equals sign argument and matching name", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CommandLine commandLine = new CommandLine("-name=value");
                                test.assertEqual("value", commandLine.getValue("name"));
                            }
                        });
                    }
                });
            }
        });
    }
}
