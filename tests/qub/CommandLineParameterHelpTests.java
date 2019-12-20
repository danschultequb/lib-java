package qub;

public interface CommandLineParameterHelpTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineParameterHelp.class, () ->
        {
            runner.testGroup("getApplicationUsageString()", () ->
            {
                runner.test("with no parameters", (Test test) ->
                {
                    test.assertEqual("my-app",
                        CommandLineParameterHelp.getApplicationUsageString(
                            "my-app",
                            Iterable.create()));
                });

                runner.test("with one optional String parameter", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.addString("test-param");
                    test.assertEqual("my-app [--test-param=<test-param-value>]",
                        CommandLineParameterHelp.getApplicationUsageString(
                            "my-app",
                            parameters.getOrderedParameters()));
                });

                runner.test("with one required boolean parameter", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.addBoolean("verbose").setRequired(true);
                    test.assertEqual("my-app --verbose",
                        CommandLineParameterHelp.getApplicationUsageString(
                            "my-app",
                            parameters.getOrderedParameters()));
                });

                runner.test("with multiple parameters", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.addPositionString("folder")
                        .setValueName("<folder-path-to-test>");
                    parameters.addString("pattern")
                        .setValueName("<test-name-pattern>");
                    parameters.addBoolean("coverage");
                    parameters.addBoolean("verbose");
                    parameters.addBoolean("help");
                    test.assertEqual(
                        "qub-test [[--folder=]<folder-path-to-test>] [--pattern=<test-name-pattern>] [--coverage] [--verbose] [--help]",
                        CommandLineParameterHelp.getApplicationUsageString(
                            "qub-test",
                            parameters.getOrderedParameters()));
                });

                runner.test("with position parameter added after non-position parameter", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.addString("pattern")
                        .setValueName("<test-name-pattern>");
                    parameters.addBoolean("coverage");
                    parameters.addPositionString("folder")
                        .setValueName("<folder-path-to-test>");
                    parameters.addBoolean("verbose");
                    parameters.addBoolean("help");
                    test.assertEqual(
                        "qub-test [[--folder=]<folder-path-to-test>] [--pattern=<test-name-pattern>] [--coverage] [--verbose] [--help]",
                        CommandLineParameterHelp.getApplicationUsageString(
                            "qub-test",
                            parameters.getOrderedParameters()));
                });

                runner.test("with qub-clean parameters", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        parameters.addPositionString("folder")
                            .setValueName("<folder-path-to-clean>")
                            .setDescription("The folder to clean. The current folder will be used if this isn't defined.");
                        parameters.addVerbose(process);
                        parameters.addHelp();
                        test.assertEqual(
                            "qub-clean [[--folder=]<folder-path-to-clean>] [--verbose] [--help]",
                            CommandLineParameterHelp.getApplicationUsageString(
                                "qub-clean",
                                parameters.getOrderedParameters()));
                    }
                });

                runner.test("with qub-build parameters", (Test test) ->
                {
                    try (final Process process = Process.create())
                    {
                        final CommandLineParameters parameters = new CommandLineParameters();
                        parameters.addPositionString("folder")
                            .setValueName("<folder-path-to-build>")
                            .setDescription("The folder to build. The current folder will be used if this isn't defined.");
                        parameters.addVerbose(process);
                        parameters.addHelp();
                        test.assertEqual(
                            "qub-build [[--folder=]<folder-path-to-build>] [--verbose] [--help]",
                            CommandLineParameterHelp.getApplicationUsageString(
                                "qub-build",
                                parameters.getOrderedParameters()));
                    }
                });
            });

            runner.testGroup("getHelpLines(String,String)", () ->
            {
                runner.test("with no parameters", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertEqual(
                        Iterable.create(
                            "Usage: fake-command",
                            "  fake command description"),
                        CommandLineParameterHelp.getApplicationHelpLines(
                            "fake-command",
                            "fake command description",
                            parameters.getOrderedParameters()));
                });

                runner.test("with position parameter added after non-position parameter", (Test test) ->
                {
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
                    test.assertEqual(
                        Iterable.create(
                            "Usage: qub-test [[--folder=]<folder-path-to-test>] [--pattern=<test-name-pattern>] [--coverage] [--verbose] [--help]",
                            "  Used to run tests in source code projects.",
                            "  --folder(f): The folder that contains the tests to run.",
                            "  --pattern(p): The pattern that test names must match to be run.",
                            "  --coverage(c): Whether or not to collect code coverage information.",
                            "  --verbose(v): Whether or not to output verbose logs.",
                            "  --help(?,h): Whether or not to display the help message for this application."),
                        CommandLineParameterHelp.getApplicationHelpLines(
                            "qub-test",
                            "Used to run tests in source code projects.",
                            parameters.getOrderedParameters()));
                });

                runner.test("with positional parameter list", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();

                    parameters.add("pattern", (String text) -> Result.success(PathPattern.parse(text)))
                        .setValueName("<pattern-value>")
                        .setValueRequired(true);
                    parameters.addBoolean("debug");
                    parameters.addBoolean("profiler");
                    parameters.addPositionStringList("test-class");

                    test.assertEqual(
                        Iterable.create(
                            "Usage: ConsoleTestRunner [[--test-class=]<test-class-value>] [--pattern=<pattern-value>] [--debug] [--profiler]",
                            "  Used to run test class files.",
                            "  --test-class: (No description provided)",
                            "  --pattern: (No description provided)",
                            "  --debug: (No description provided)",
                            "  --profiler: (No description provided)"),
                        CommandLineParameterHelp.getApplicationHelpLines(
                            "ConsoleTestRunner",
                            "Used to run test class files.",
                            parameters.getOrderedParameters()));
                });
            });
        });
    }
}
