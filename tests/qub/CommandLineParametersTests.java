package qub;

public interface CommandLineParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineParameters.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final CommandLineParameters parameters = new CommandLineParameters();
                test.assertNotNull(parameters);
            });

            runner.testGroup("setArguments(CommandLineArguments)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.setArguments(null),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with no parameters", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertSame(parameters, parameters.setArguments(arguments));
                });

                runner.test("with existing parameters", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final CommandLineParameter<String> parameter = parameters.addString("hello");
                    test.assertNull(parameter.getArguments());
                    test.assertSame(parameters, parameters.setArguments(arguments));
                    test.assertSame(arguments, parameter.getArguments());
                });
            });

            runner.testGroup("addString(String,String)", () ->
            {
                runner.test("with null parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addString(null),
                        new PreConditionFailure("parameterName cannot be null."));
                });

                runner.test("with empty parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addString(""),
                        new PreConditionFailure("parameterName cannot be empty."));
                });

                runner.test("with non-empty parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final CommandLineParameter<String> parameter = parameters.addString("fakeName");
                    test.assertNotNull(parameter);
                    test.assertEqual("fakeName", parameter.getName());
                    test.assertNull(parameter.getDescription());
                    test.assertNull(parameter.getIndex());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                    test.assertFalse(parameter.isRequired());
                    test.assertTrue(parameter.isValueRequired());
                    test.assertNull(parameter.getArguments());
                });
            });

            runner.testGroup("addPositionString(String,String)", () ->
            {
                runner.test("with null parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addPositionString(null),
                        new PreConditionFailure("parameterName cannot be null."));
                });

                runner.test("with empty parameterName", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addPositionString(""),
                        new PreConditionFailure("parameterName cannot be empty."));
                });

                runner.test("with non-empty parameterName and non-empty parameterDescription", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    final CommandLineParameter<String> parameter = parameters.addPositionString("fakeName");
                    test.assertNotNull(parameter);
                    test.assertEqual("fakeName", parameter.getName());
                    test.assertNull(parameter.getDescription());
                    test.assertEqual(0, parameter.getIndex());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                    test.assertFalse(parameter.isRequired());
                });

                runner.test("with multiple parameters", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();

                    final CommandLineParameter<String> parameter1 = parameters.addPositionString("fakeName1");
                    test.assertEqual("fakeName1", parameter1.getName());
                    test.assertEqual(0, parameter1.getIndex());

                    final CommandLineParameter<String> parameter2 = parameters.addPositionString("fakeName2");
                    test.assertEqual("fakeName2", parameter2.getName());
                    test.assertEqual(1, parameter2.getIndex());
                });
            });

            runner.testGroup("addBoolean(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addBoolean(null),
                        new PreConditionFailure("parameterName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertThrows(() -> parameters.addBoolean(""),
                        new PreConditionFailure("parameterName cannot be empty."));
                });

                runner.test("with non-empty name that doesn't exist in arguments", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Boolean> parameter = parameters.addBoolean("a");
                    test.assertFalse(parameter.getValue().await());
                });

                runner.test("with non-empty name that doesn't have a value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Boolean> parameter = parameters.addBoolean("a");
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that has an empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Boolean> parameter = parameters.addBoolean("a");
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that has a \"false\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=false");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Boolean> parameter = parameters.addBoolean("a");
                    test.assertFalse(parameter.getValue().await());
                });

                runner.test("with non-empty name that has a \"FALSE\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=FALSE");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Boolean> parameter = parameters.addBoolean("a");
                    test.assertThrows(() -> parameter.getValue().await(),
                        new ParseException("Expected the value (\"FALSE\") to be either \"true\" or \"false\"."));
                });

                runner.test("with non-empty name that has a \"true\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=true");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Boolean> parameter = parameters.addBoolean("a");
                    test.assertTrue(parameter.getValue().await());
                });

                runner.test("with non-empty name that has a \"TRUE\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=TRUE");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Boolean> parameter = parameters.addBoolean("a");
                    test.assertThrows(() -> parameter.getValue().await(),
                        new ParseException("Expected the value (\"TRUE\") to be either \"true\" or \"false\"."));
                });

                runner.test("with non-empty name that has a \"10\" value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--a=10");
                    final CommandLineParameters parameters = new CommandLineParameters().setArguments(arguments);
                    final CommandLineParameter<Boolean> parameter = parameters.addBoolean("a");
                    test.assertThrows(() -> parameter.getValue().await(),
                        new ParseException("Expected the value (\"10\") to be either \"true\" or \"false\"."));
                });
            });

            runner.test("addVerbose()", (Test test) ->
            {
                final CommandLineParameters parameters = new CommandLineParameters();
                final CommandLineParameter<Boolean> parameter = parameters.addVerbose();
                test.assertEqual("verbose", parameter.getName());
                test.assertEqual(Iterable.create(), parameter.getAliases());
                test.assertEqual("Whether or not to show verbose logs.", parameter.getDescription());
                test.assertNull(parameter.getValueName());
            });

            runner.test("addDebug()", (Test test) ->
            {
                final CommandLineParameters parameters = new CommandLineParameters();
                final CommandLineParameter<Boolean> parameter = parameters.addDebug();
                test.assertEqual("debug", parameter.getName());
                test.assertEqual(Iterable.create(), parameter.getAliases());
                test.assertEqual("Whether or not to run this application in debug mode.", parameter.getDescription());
                test.assertNull(parameter.getValueName());
            });

            runner.test("addProfiler()", (Test test) ->
            {
                final CommandLineParameters parameters = new CommandLineParameters();
                final CommandLineParameter<Boolean> parameter = parameters.addProfiler();
                test.assertEqual("profiler", parameter.getName());
                test.assertEqual(Iterable.create(), parameter.getAliases());
                test.assertEqual("Whether or not this application should pause before it is run to allow a profiler to be attached.", parameter.getDescription());
                test.assertNull(parameter.getValueName());
            });

            runner.test("addHelp()", (Test test) ->
            {
                final CommandLineParameters parameters = new CommandLineParameters();
                final CommandLineParameter<Boolean> parameter = parameters.addHelp();
                test.assertEqual("help", parameter.getName());
                test.assertEqual(Iterable.create("?"), parameter.getAliases());
                test.assertEqual("Show the help message for this application.", parameter.getDescription());
                test.assertNull(parameter.getValueName());
            });

            runner.testGroup("getUsageString()", () ->
            {
                runner.test("with no parameters", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    test.assertEqual("my-app", parameters.getUsageString("my-app"));
                });

                runner.test("with one optional String parameter", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.addString("test-param");
                    test.assertEqual("my-app [--test-param=<test-param-value>]", parameters.getUsageString("my-app"));
                });

                runner.test("with one required boolean parameter", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.addBoolean("verbose").setRequired(true);
                    test.assertEqual("my-app --verbose", parameters.getUsageString("my-app"));
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
                        parameters.getUsageString("qub-test"));
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
                        parameters.getUsageString("qub-test"));
                });

                runner.test("with qub-clean parameters", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.addPositionString("folder")
                        .setValueName("<folder-path-to-clean>")
                        .setDescription("The folder to clean. The current folder will be used if this isn't defined.");
                    parameters.addVerbose();
                    parameters.addHelp();
                    test.assertEqual(
                        "qub-clean [[--folder=]<folder-path-to-clean>] [--verbose] [--help]",
                        parameters.getUsageString("qub-clean"));
                });

                runner.test("with qub-build parameters", (Test test) ->
                {
                    final CommandLineParameters parameters = new CommandLineParameters();
                    parameters.addPositionString("folder")
                        .setValueName("<folder-path-to-build>")
                        .setDescription("The folder to build. The current folder will be used if this isn't defined.");
                    parameters.addVerbose();
                    parameters.addHelp();
                    test.assertEqual(
                        "qub-build [[--folder=]<folder-path-to-build>] [--verbose] [--help]",
                        parameters.getUsageString("qub-build"));
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
                        parameters.getHelpLines("fake-command", "fake command description"));
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
                        parameters.getHelpLines("qub-test", "Used to run tests in source code projects."));
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
                        parameters.getHelpLines("ConsoleTestRunner", "Used to run test class files."));
                });
            });
        });
    }
}
