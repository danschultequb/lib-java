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
                    final CommandLineActions<DesktopProcess> actions = CommandLineActions.create();
                    final CommandLineAction<DesktopProcess> action = CommandLineLogsAction.addAction(actions);
                    test.assertNotNull(action);
                    test.assertEqual(CommandLineLogsAction.actionName, action.getName());
                    test.assertEqual(CommandLineLogsAction.actionDescription, action.getDescription());
                    test.assertTrue(actions.containsActionName(action.getName()));
                });
            });

            runner.testGroup("run(QubProcess2)", () ->
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
                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        fileSystem.createRoot("/").await();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("fake", "main-java", "7").await();
                        final File projectCompiledSourcesFile = projectVersionFolder.getCompiledSourcesFile().await();
                        projectCompiledSourcesFile.create().await();
                        final Folder dataFolder = projectVersionFolder.getProjectDataFolder().await();
                        final Folder logsFolder = dataFolder.getFolder("logs").await();
                        process.setFileSystem(fileSystem, "/");

                        process.setTypeLoader(FakeTypeLoader.create()
                            .addTypeContainer("fake.MainClassFullName", projectCompiledSourcesFile));

                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        process.setErrorWriteStream(error);

                        CommandLineLogsAction.run(process);

                        test.assertEqual(0, process.getExitCode());
                        test.assertEqual(
                            Iterable.create(
                                "The logs folder (" + logsFolder + ") doesn't exist."),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual("", error.getText().await());
                    }
                });

                runner.test("with existing logs folder", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                        fileSystem.createRoot("/").await();
                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                        final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("fake", "main-java", "7").await();
                        final File projectCompiledSourcesFile = projectVersionFolder.getCompiledSourcesFile().await();
                        projectCompiledSourcesFile.create().await();
                        final Folder dataFolder = projectVersionFolder.getProjectDataFolder().await();
                        final Folder logsFolder = dataFolder.getFolder("logs").await();
                        logsFolder.create().await();
                        process.setFileSystem(fileSystem, "/");

                        process.setTypeLoader(FakeTypeLoader.create()
                            .addTypeContainer("fake.MainClassFullName", projectCompiledSourcesFile));

                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        process.setErrorWriteStream(error);

                        final FakeDefaultApplicationLauncher defaultApplicationLauncher = FakeDefaultApplicationLauncher.create();
                        process.setDefaultApplicationLauncher(defaultApplicationLauncher);

                        CommandLineLogsAction.run(process);

                        test.assertEqual(0, process.getExitCode());
                        test.assertEqual("", output.getText().await());
                        test.assertEqual("", error.getText().await());
                    }
                });
            });
        });
    }
}
