package qub;

public interface CommandLineParameterHelpTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineParameterHelp.class, () ->
        {
            runner.testGroup("constructor(CommandLineParameters)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new CommandLineParameterHelp(null),
                        new PreConditionFailure("parameters cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CommandLineParameters parameters = CommandLineParameters.create();
                    final CommandLineParameterHelp helpParameter = new CommandLineParameterHelp(parameters);
                    test.assertNotNull(helpParameter);
                    test.assertEqual("help", helpParameter.getName());
                    test.assertEqual(Iterable.create("?"), helpParameter.getAliases());
                    test.assertEqual("Show the help message for this application.", helpParameter.getDescription());
                    test.assertFalse(helpParameter.getForceShowApplicationHelpLines());
                });
            });

            runner.testGroup("setForceShowApplicationHelpLines(boolean)", () ->
            {
                final Action1<Boolean> setForceShowApplicationHelpLinesTest = (Boolean forceShowApplicationHelpLines) ->
                {
                    runner.test("with " + forceShowApplicationHelpLines, (Test test) ->
                    {
                        final CommandLineParameters parameters = CommandLineParameters.create();
                        final CommandLineParameterHelp helpParameter = new CommandLineParameterHelp(parameters);
                        final CommandLineParameterHelp setForceShowApplicationHelpLinesResult = helpParameter.setForceShowApplicationHelpLines(forceShowApplicationHelpLines);
                        test.assertSame(helpParameter, setForceShowApplicationHelpLinesResult);
                        test.assertEqual(forceShowApplicationHelpLines, helpParameter.getForceShowApplicationHelpLines());
                    });
                };

                setForceShowApplicationHelpLinesTest.run(false);
                setForceShowApplicationHelpLinesTest.run(true);
            });

            runner.testGroup("showApplicationHelpLines(DesktopProcess)", () ->
            {
                runner.test("with null process", (Test test) ->
                {
                    final DesktopProcess process = null;
                    final CommandLineParameters parameters = CommandLineParameters.create();
                    final CommandLineParameterHelp helpParameter = new CommandLineParameterHelp(parameters);

                    test.assertThrows(() -> helpParameter.showApplicationHelpLines(process),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with no command line arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineParameters parameters = process.createCommandLineParameters();
                        final CommandLineParameterHelp helpParameter = parameters.addHelp();

                        test.assertFalse(helpParameter.showApplicationHelpLines(process).await());

                        test.assertEqual("", process.getOutputWriteStream().getText().await());
                        test.assertEqual(0, process.getExitCode());
                    }
                });

                runner.test("with --help", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create("--help"))
                    {
                        final CommandLineParameters parameters = process.createCommandLineParameters()
                            .setApplicationName("fake-application-name")
                            .setApplicationDescription("fake-application-description");
                        final CommandLineParameterHelp helpParameter = parameters.addHelp();

                        test.assertTrue(helpParameter.showApplicationHelpLines(process).await());

                        test.assertEqual(
                            Iterable.create(
                                "Usage: fake-application-name [--help]",
                                "  fake-application-description",
                                "  --help(?): Show the help message for this application."),
                            Strings.getLines(process.getOutputWriteStream().getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with --?", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create("--?"))
                    {
                        final CommandLineParameters parameters = process.createCommandLineParameters()
                            .setApplicationName("fake-application-name")
                            .setApplicationDescription("fake-application-description");
                        final CommandLineParameterHelp helpParameter = parameters.addHelp();

                        test.assertTrue(helpParameter.showApplicationHelpLines(process).await());

                        test.assertEqual(
                            Iterable.create(
                                "Usage: fake-application-name [--help]",
                                "  fake-application-description",
                                "  --help(?): Show the help message for this application."),
                            Strings.getLines(process.getOutputWriteStream().getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with forceShowApplicationHelpLines set to true", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineParameters parameters = process.createCommandLineParameters()
                            .setApplicationName("fake-application-name")
                            .setApplicationDescription("fake-application-description");
                        final CommandLineParameterHelp helpParameter = parameters.addHelp();

                        helpParameter.setForceShowApplicationHelpLines(true);

                        test.assertTrue(helpParameter.showApplicationHelpLines(process).await());

                        test.assertEqual(
                            Iterable.create(
                                "Usage: fake-application-name [--help]",
                                "  fake-application-description",
                                "  --help(?): Show the help message for this application."),
                            Strings.getLines(process.getOutputWriteStream().getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });
            });
        });
    }
}
