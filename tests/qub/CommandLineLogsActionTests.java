package qub;

public interface CommandLineLogsActionTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(CommandLineLogsAction.class, () ->
        {
            runner.testGroup("addAction(CommandLineActions<? extends DesktopProcess>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineLogsAction.addAction(null),
                        new PreConditionFailure("actions cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = CommandLineLogsAction.addAction(actions);
                    test.assertNotNull(action);
                    test.assertEqual(CommandLineLogsAction.actionName, action.getName());
                    test.assertEqual(CommandLineLogsAction.actionDescription, action.getDescription());
                    test.assertTrue(actions.containsActionName(action.getName()));
                });
            });

            runner.testGroup("run(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineLogsAction.run(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-existing logs folder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = process.getFileSystem();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("fake", "main-java", "7").await();
                        final File projectCompiledSourcesFile = projectVersionFolder.getCompiledSourcesFile().await();
                        projectCompiledSourcesFile.create().await();
                        final Folder dataFolder = projectVersionFolder.getProjectDataFolder().await();
                        final Folder logsFolder = dataFolder.getFolder("logs").await();

                        process.getTypeLoader()
                            .addTypeContainer("fake.MainClassFullName", projectCompiledSourcesFile);

                        CommandLineLogsAction.run(process);

                        test.assertEqual(0, process.getExitCode());
                        test.assertEqual(
                            Iterable.create(
                                "The logs folder (" + logsFolder + ") doesn't exist."),
                            Strings.getLines(process.getOutputWriteStream().getText().await()));
                        test.assertEqual("", process.getErrorWriteStream().getText().await());
                    }
                });

                runner.test("with existing logs folder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = process.getFileSystem();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("fake", "main-java", "7").await();
                        final File projectCompiledSourcesFile = projectVersionFolder.getCompiledSourcesFile().await();
                        projectCompiledSourcesFile.create().await();
                        final Folder dataFolder = projectVersionFolder.getProjectDataFolder().await();
                        final Folder logsFolder = dataFolder.getFolder("logs").await();
                        logsFolder.create().await();

                        process.getTypeLoader()
                            .addTypeContainer("fake.MainClassFullName", projectCompiledSourcesFile);

                        CommandLineLogsAction.run(process);

                        test.assertEqual(0, process.getExitCode());
                        test.assertEqual("", process.getOutputWriteStream().getText().await());
                        test.assertEqual("", process.getErrorWriteStream().getText().await());
                    }
                });
            });
        });
    }
}
