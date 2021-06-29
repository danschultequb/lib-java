package qub;

public interface CommandLineLogsActionParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineLogsActionParameters.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                test.assertNotNull(parameters);
                test.assertNull(parameters.getLogsFolder());
                test.assertNull(parameters.getOutput());
                test.assertNull(parameters.getDefaultApplicationLauncher());
                test.assertNull(parameters.getOpenWith());
            });

            runner.testGroup("setLogsFolder(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    test.assertThrows(() -> parameters.setLogsFolder(null),
                        new PreConditionFailure("logsFolder cannot be null."));
                    test.assertNull(parameters.getLogsFolder());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder logsFolder = fileSystem.getFolder("/logs/").await();

                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    final CommandLineLogsActionParameters setLogsFolderResult = parameters.setLogsFolder(logsFolder);
                    test.assertSame(parameters, setLogsFolderResult);
                    test.assertSame(logsFolder, parameters.getLogsFolder());
                });
            });

            runner.testGroup("setOutput(CharacterWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    test.assertThrows(() -> parameters.setOutput(null),
                        new PreConditionFailure("output cannot be null."));
                    test.assertNull(parameters.getOutput());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryCharacterStream output = InMemoryCharacterStream.create();

                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    final CommandLineLogsActionParameters setOutputResult = parameters.setOutput(output);
                    test.assertSame(parameters, setOutputResult);
                    test.assertSame(output, parameters.getOutput());
                });
            });

            runner.testGroup("setDefaultApplicationLauncher(DefaultApplicationLauncher)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    test.assertThrows(() -> parameters.setDefaultApplicationLauncher(null),
                        new PreConditionFailure("defaultApplicationLauncher cannot be null."));
                    test.assertNull(parameters.getDefaultApplicationLauncher());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create(fileSystem);

                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    final CommandLineLogsActionParameters setDefaultApplicationLauncherResult = parameters.setDefaultApplicationLauncher(defaultApplicationLauncher);
                    test.assertSame(parameters, setDefaultApplicationLauncherResult);
                    test.assertSame(defaultApplicationLauncher, parameters.getDefaultApplicationLauncher());
                });
            });

            runner.testGroup("setProcessFactory(ProcessFactory)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    test.assertThrows(() -> parameters.setProcessFactory(null),
                        new PreConditionFailure("processFactory cannot be null."));
                    test.assertNull(parameters.getProcessFactory());
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                        final CommandLineLogsActionParameters setDefaultApplicationLauncherResult = parameters.setProcessFactory(process.getChildProcessRunner());
                        test.assertSame(parameters, setDefaultApplicationLauncherResult);
                        test.assertSame(process.getChildProcessRunner(), parameters.getProcessFactory());
                    }
                });
            });

            runner.testGroup("setOpenWith(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    test.assertThrows(() -> parameters.setOpenWith(null),
                        new PreConditionFailure("openWith cannot be null."));
                    test.assertNull(parameters.getOpenWith());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    final CommandLineLogsActionParameters setOpenWithResult = parameters.setOpenWith(Path.parse("hello"));
                    test.assertSame(parameters, setOpenWithResult);
                    test.assertEqual(Path.parse("hello"), parameters.getOpenWith());
                });

                runner.test("with absolute path", (Test test) ->
                {
                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    final CommandLineLogsActionParameters setOpenWithResult = parameters.setOpenWith(Path.parse("/hello"));
                    test.assertSame(parameters, setOpenWithResult);
                    test.assertEqual(Path.parse("/hello"), parameters.getOpenWith());
                });
            });
        });
    }
}
