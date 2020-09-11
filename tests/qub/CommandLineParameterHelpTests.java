package qub;

public interface CommandLineParameterHelpTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineParameterHelp.class, () ->
        {
            runner.testGroup("writeApplicationHelpLines(String,String)", () ->
            {
                runner.test("with no parameters", (Test test) ->
                {
                    final String applicationName = "fake-command";
                    final String applicationDescription = "fake command description";
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final InMemoryCharacterStream stream = InMemoryCharacterStream.create();
                    final int charactersWritten = CommandLineParameterHelp.writeApplicationHelpLines(stream, applicationName, applicationDescription, parameters.getOrderedParameters()).await();
                    test.assertEqual(
                        Iterable.create(
                            "Usage: fake-command",
                            "  fake command description",
                            ""),
                        Strings.getLines(stream.getText().await()));
                    test.assertEqual(48, charactersWritten);
                });

                runner.test("with position parameter added after non-position parameter", (Test test) ->
                {
                    final String applicationName = "qub-test";
                    final String applicationDescription = "Used to run tests in source code projects.";
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.addString("pattern")
                        .setValueName("<test-name-pattern>")
                        .setDescription("The pattern that test names must match to be run.")
                        .addAlias("p");
                    parameters.addBoolean("coverage")
                        .setDescription("Whether or not to collect code coverage information.")
                        .addAlias("c");
                    parameters.addPositionString("folder")
                        .setValueName("<folder-path-to-test>")
                        .setDescription("The folder that contains the tests to run.")
                        .addAlias("f");
                    parameters.addBoolean("verbose")
                        .setDescription("Whether or not to output verbose logs.")
                        .addAlias("v");
                    parameters.addBoolean("help")
                        .setDescription("Whether or not to display the help message for this application.")
                        .addAliases("?", "h");
                    final InMemoryCharacterStream stream = InMemoryCharacterStream.create();
                    final int charactersWritten = CommandLineParameterHelp.writeApplicationHelpLines(stream, applicationName, applicationDescription, parameters.getOrderedParameters()).await();
                    test.assertEqual(
                        Iterable.create(
                            "Usage: qub-test [[--folder=]<folder-path-to-test>] [--pattern=<test-name-pattern>] [--coverage] [--verbose] [--help]",
                            "  Used to run tests in source code projects.",
                            "  --folder(f):   The folder that contains the tests to run.                      ",
                            "  --pattern(p):  The pattern that test names must match to be run.               ",
                            "  --coverage(c): Whether or not to collect code coverage information.            ",
                            "  --verbose(v):  Whether or not to output verbose logs.                          ",
                            "  --help(?,h):   Whether or not to display the help message for this application."),
                        Strings.getLines(stream.getText().await()));
                    test.assertEqual(572, charactersWritten);
                });

                runner.test("with positional parameter list", (Test test) ->
                {
                    final String applicationName = "ConsoleTestRunner";
                    final String applicationDescription = "Used to run test class files.";
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.add("pattern", (String text) -> Result.success(PathPattern.parse(text)))
                        .setValueName("<pattern-value>")
                        .setValueRequired(true);
                    parameters.addBoolean("debug");
                    parameters.addBoolean("profiler");
                    parameters.addPositionStringList("test-class");

                    final InMemoryCharacterStream stream = InMemoryCharacterStream.create();
                    final int charactersWritten = CommandLineParameterHelp.writeApplicationHelpLines(stream, applicationName, applicationDescription, parameters.getOrderedParameters()).await();

                    test.assertEqual(
                        Iterable.create(
                            "Usage: ConsoleTestRunner [[--test-class=]<test-class-value>] [--pattern=<pattern-value>] [--debug] [--profiler]",
                            "  Used to run test class files.",
                            "  --test-class: (No description provided)",
                            "  --pattern:    (No description provided)",
                            "  --debug:      (No description provided)",
                            "  --profiler:   (No description provided)"),
                        Strings.getLines(stream.getText().await()));
                    test.assertEqual(312, charactersWritten);
                });
            });
        });
    }
}
