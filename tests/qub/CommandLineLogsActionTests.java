package qub;

public interface CommandLineLogsActionTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(CommandLineLogsAction.class, () ->
        {
            runner.testGroup("add(CommandLineActions<? extends QubProcess>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineLogsAction.add(null),
                        new PreConditionFailure("actions cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CommandLineActions<QubProcess> actions = CommandLineActions.create();
                    final CommandLineAction<QubProcess> action = CommandLineLogsAction.add(actions);
                    test.assertNotNull(action);
                    test.assertEqual(CommandLineLogsAction.actionName, action.getName());
                    test.assertEqual(CommandLineLogsAction.actionDescription, action.getDescription());
                    test.assertTrue(actions.containsActionName(action.getName()));
                });
            });

            runner.testGroup("run(QubProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineLogsAction.run(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-existing logs folder", (Test test) ->
                {
                    try (final QubProcess process = QubProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("C:/");
                        process.setFileSystem(fileSystem);

                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        process.setErrorWriteStream(error);

                        CommandLineLogsAction.run(process);

                        test.assertEqual(0, process.getExitCode());
                        test.assertEqual(
                            Iterable.create(
                                "The logs folder (C:/qub/qub/test-java/data/logs/) doesn't exist."),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual("", error.getText().await());
                    }
                });

                runner.test("with existing logs folder", (Test test) ->
                {
                    try (final QubProcess process = QubProcess.create())
                    {
                        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                        fileSystem.createRoot("C:/");
                        fileSystem.createFolder("C:/qub/qub/test-java/data/logs/").await();
                        process.setFileSystem(fileSystem);

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
