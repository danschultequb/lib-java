package qub;

public interface CommandLineLogsActionTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(CommandLineLogsAction.class, () ->
        {
            runner.testGroup("getLogsFolderFromProcess(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineLogsAction.getLogsFolderFromProcess(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Folder logsFolder = CommandLineLogsAction.getLogsFolderFromProcess(process);
                        test.assertNotNull(logsFolder);
                        test.assertEqual("/qub/fake-publisher/fake-project/data/logs/", logsFolder.toString());
                    }
                });
            });

            runner.testGroup("getLogsFolderFromDataFolder(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineLogsAction.getLogsFolderFromDataFolder(null),
                        new PreConditionFailure("dataFolder cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder dataFolder = fileSystem.getFolder("/data/").await();

                    final Folder logsFolder = CommandLineLogsAction.getLogsFolderFromDataFolder(dataFolder);
                    test.assertNotNull(logsFolder);
                    test.assertEqual("/data/logs/", logsFolder.toString());
                });
            });

            runner.testGroup("addAction(CommandLineActions)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineLogsAction.addAction(null),
                        new PreConditionFailure("actions cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create(CommandLineLogsAction.actionName))
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        final CommandLineAction action = CommandLineLogsAction.addAction(actions);
                        test.assertNotNull(action);
                        test.assertEqual(CommandLineLogsAction.actionName, action.getName());
                        test.assertEqual(CommandLineLogsAction.actionDescription, action.getDescription());
                        test.assertTrue(actions.containsActionName(action.getName()));
                    }
                });
            });

            runner.testGroup("addAction(CommandLineActions,CommandLineLogsActionParameters)", () ->
            {
                runner.test("with null actions", (Test test) ->
                {
                    final CommandLineActions actions = null;
                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();

                    test.assertThrows(() -> CommandLineLogsAction.addAction(actions, parameters),
                        new PreConditionFailure("actions cannot be null."));
                });

                runner.test("with null parameters", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineLogsActionParameters parameters = null;

                    test.assertThrows(() -> CommandLineLogsAction.addAction(actions, parameters),
                        new PreConditionFailure("parameters cannot be null."));
                });

                runner.test("with non-null arguments", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineLogsActionParameters parameters = CommandLineLogsActionParameters.create();
                    final CommandLineAction action = CommandLineLogsAction.addAction(actions, parameters);
                    test.assertNotNull(action);
                    test.assertEqual(CommandLineLogsAction.actionName, action.getName());
                    test.assertEqual(CommandLineLogsAction.actionDescription, action.getDescription());
                    test.assertEqual(Iterable.create(), action.getAliases());
                    test.assertTrue(actions.containsActionName(CommandLineLogsAction.actionName));
                    test.assertNull(parameters.getLogsFolder());
                    test.assertNull(parameters.getOutput());
                    test.assertNull(parameters.getProcessFactory());
                    test.assertNull(parameters.getDefaultApplicationLauncher());
                    test.assertNull(parameters.getOpenWith());
                });
            });

//            runner.testGroup("run(DesktopProcess)", () ->
//            {
//                runner.test("with null", (Test test) ->
//                {
//                    test.assertThrows(() -> CommandLineLogsAction.run(null),
//                        new PreConditionFailure("process cannot be null."));
//                });
//
//                runner.test("with non-existing logs folder", (Test test) ->
//                {
//                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
//                    {
//                        final InMemoryFileSystem fileSystem = process.getFileSystem();
//                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
//                        final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("fake", "main-java", "7").await();
//                        final File projectCompiledSourcesFile = projectVersionFolder.getCompiledSourcesFile().await();
//                        projectCompiledSourcesFile.create().await();
//                        final Folder dataFolder = projectVersionFolder.getProjectDataFolder().await();
//                        final Folder logsFolder = dataFolder.getFolder("logs").await();
//
//                        process.getTypeLoader()
//                            .addTypeContainer("fake.MainClassFullName", projectCompiledSourcesFile);
//
//                        CommandLineLogsAction.run(process);
//
//                        test.assertEqual(0, process.getExitCode());
//                        test.assertEqual(
//                            Iterable.create(
//                                "The logs folder (" + logsFolder + ") doesn't exist."),
//                            Strings.getLines(process.getOutputWriteStream().getText().await()));
//                        test.assertEqual("", process.getErrorWriteStream().getText().await());
//                    }
//                });
//
//                runner.test("with existing logs folder", (Test test) ->
//                {
//                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
//                    {
//                        final InMemoryFileSystem fileSystem = process.getFileSystem();
//                        final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
//                        final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("fake", "main-java", "7").await();
//                        final File projectCompiledSourcesFile = projectVersionFolder.getCompiledSourcesFile().await();
//                        projectCompiledSourcesFile.create().await();
//                        final Folder dataFolder = projectVersionFolder.getProjectDataFolder().await();
//                        final Folder logsFolder = dataFolder.getFolder("logs").await();
//                        logsFolder.create().await();
//
//                        process.getTypeLoader()
//                            .addTypeContainer("fake.MainClassFullName", projectCompiledSourcesFile);
//
//                        CommandLineLogsAction.run(process);
//
//                        test.assertEqual(0, process.getExitCode());
//                        test.assertEqual("", process.getOutputWriteStream().getText().await());
//                        test.assertEqual("", process.getErrorWriteStream().getText().await());
//                    }
//                });
//            });
        });
    }
}
