package qub;

public interface CommandLineActionsTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(CommandLineActions.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final CommandLineActions<Process> actions = CommandLineActions.create();
                test.assertNotNull(actions);
                test.assertNull(actions.getApplicationName());
                test.assertNull(actions.getApplicationDescription());
            });

            runner.testGroup("setApplicationName(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationName(null));
                    test.assertEqual(null, actions.getApplicationName());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationName(""));
                    test.assertEqual("", actions.getApplicationName());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationName("hello there"));
                    test.assertEqual("hello there", actions.getApplicationName());
                });
            });

            runner.testGroup("setApplicationDescription(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationDescription(null));
                    test.assertEqual(null, actions.getApplicationDescription());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationDescription(""));
                    test.assertEqual("", actions.getApplicationDescription());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationDescription("hello there"));
                    test.assertEqual("hello there", actions.getApplicationDescription());
                });
            });

            runner.testGroup("containsActionName(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.containsActionName(null),
                        new PreConditionFailure("actionName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.containsActionName(""),
                        new PreConditionFailure("actionName cannot be empty."));
                });

                runner.test("with non-existing actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertFalse(actions.containsActionName("test"));
                });

                runner.test("with existing actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    actions.addAction("hello", (Process process) -> {});
                    test.assertTrue(actions.containsActionName("hello"));
                });

                runner.test("with existing alias actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    actions.addAction("hello", (Process process) -> {})
                        .addAlias("there");
                    test.assertTrue(actions.containsActionName("there"));
                });
            });

            runner.testGroup("addAction(String,Action1<Process>)", () ->
            {
                runner.test("with null actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction(null, (Process process) -> {}),
                        new PreConditionFailure("actionName cannot be null."));
                });

                runner.test("with empty actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction("", (Process process) -> {}),
                        new PreConditionFailure("actionName cannot be empty."));
                });

                runner.test("with null mainAction", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction("hello", (Action1<Process>)null),
                        new PreConditionFailure("mainAction cannot be null."));
                });

                runner.test("with empty actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    final CommandLineAction action = actions.addAction("hello", (Process process) -> {});
                    test.assertEqual("hello", action.getName());
                    test.assertEqual(Iterable.create(), action.getAliases());
                    test.assertNull(action.getDescription());
                });

                runner.test("with existing actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    actions.addAction("hello", (Process process) -> {});
                    test.assertThrows(() -> actions.addAction("hello", (Process process) -> {}),
                        new PreConditionFailure("this.containsActionName(actionName) cannot be true."));
                    test.assertTrue(actions.containsActionName("hello"));
                });

                runner.test("with existing actionName alias", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    actions.addAction("hello", (Process process) -> {})
                        .addAlias("there");
                    test.assertThrows(() -> actions.addAction("there", (Process process) -> {}),
                        new PreConditionFailure("this.containsActionName(actionName) cannot be true."));
                    test.assertTrue(actions.containsActionName("hello"));
                    test.assertTrue(actions.containsActionName("there"));
                });
            });

            runner.testGroup("addAction(String,Function1<Process,Integer>)", () ->
            {
                runner.test("with null actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction(null, (Process process) -> 1),
                        new PreConditionFailure("actionName cannot be null."));
                });

                runner.test("with empty actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction("", (Process process) -> 2),
                        new PreConditionFailure("actionName cannot be empty."));
                });

                runner.test("with null mainAction", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction("hello", (Function1<Process,Integer>)null),
                        new PreConditionFailure("mainFunction cannot be null."));
                });

                runner.test("with empty actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    final CommandLineAction action = actions.addAction("hello", (Process process) -> 3);
                    test.assertEqual("hello", action.getName());
                    test.assertEqual(Iterable.create(), action.getAliases());
                    test.assertNull(action.getDescription());
                });

                runner.test("with existing actionName", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    actions.addAction("hello", (Process process) -> {});
                    test.assertThrows(() -> actions.addAction("hello", (Process process) -> 4),
                        new PreConditionFailure("this.containsActionName(actionName) cannot be true."));
                    test.assertTrue(actions.containsActionName("hello"));
                });

                runner.test("with existing actionName alias", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    actions.addAction("hello", (Process process) -> {})
                        .addAlias("there");
                    test.assertThrows(() -> actions.addAction("there", (Process process) -> 5),
                        new PreConditionFailure("this.containsActionName(actionName) cannot be true."));
                    test.assertTrue(actions.containsActionName("hello"));
                    test.assertTrue(actions.containsActionName("there"));
                });
            });

            runner.testGroup("run(Process)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.run(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with no actions and no arguments", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create()
                        .setApplicationName("hello")
                        .setApplicationDescription("there");
                    try (Process process = Process.create())
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        actions.run(process);

                        test.assertEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?): Show the help message for this application."),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with no actions and empty action argument", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create()
                        .setApplicationName("hello")
                        .setApplicationDescription("there");
                    try (Process process = Process.create("--action="))
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        actions.run(process);

                        test.assertEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?): Show the help message for this application."),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with no actions and non-empty action argument", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create()
                        .setApplicationName("hello")
                        .setApplicationDescription("there");
                    try (Process process = Process.create("--action=stuff"))
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        actions.run(process);

                        test.assertEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?): Show the help message for this application."),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with one action and empty action argument", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create()
                        .setApplicationName("hello")
                        .setApplicationDescription("there");
                    actions.addAction("update", (Process process) -> {})
                        .setDescription("Perform the update action");
                    try (Process process = Process.create("--action="))
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        actions.run(process);

                        test.assertEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?): Show the help message for this application.",
                                "",
                                "Actions:",
                                "  update: Perform the update action"),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with one action and empty action argument", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create()
                        .setApplicationName("hello")
                        .setApplicationDescription("there");
                    actions.addAction("update", (Process process) -> {})
                        .setDescription("Update the thing.");
                    try (Process process = Process.create("--action="))
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        actions.run(process);

                        test.assertEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?): Show the help message for this application.",
                                "",
                                "Actions:",
                                "  update: Update the thing."),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with one action and non-matching action argument", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create()
                        .setApplicationName("hello")
                        .setApplicationDescription("there");
                    actions.addAction("update", (Process process) -> {});
                    try (Process process = Process.create("--action=foo"))
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        actions.run(process);

                        test.assertEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?): Show the help message for this application.",
                                "",
                                "Actions:",
                                "  update"),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with two actions and non-matching action argument", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create()
                        .setApplicationName("hello")
                        .setApplicationDescription("there");
                    actions.addAction("update", (Process process) -> {});
                    actions.addAction("list", (Process process) -> {})
                        .setDescription("Do the list action.");
                    try (Process process = Process.create("--action=foo"))
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        actions.run(process);

                        test.assertEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?): Show the help message for this application.",
                                "",
                                "Actions:",
                                "  list: Do the list action.",
                                "  update"),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with one action and name-matching action argument", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final CommandLineActions<Process> actions = CommandLineActions.create()
                        .setApplicationName("hello")
                        .setApplicationDescription("there");
                    actions.addAction("update", (Process process) -> { value.increment(); });
                    try (Process process = Process.create("--action=update"))
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        actions.run(process);

                        test.assertEqual(
                            Iterable.create(),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual(0, process.getExitCode());
                        test.assertEqual(1, value.get());
                    }
                });

                runner.test("with one action and alias-matching action argument", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final CommandLineActions<Process> actions = CommandLineActions.create()
                        .setApplicationName("hello")
                        .setApplicationDescription("there");
                    actions.addAction("update", (Process process) -> { value.increment(); })
                        .addAlias("u");
                    try (Process process = Process.create("--action=u"))
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        process.setOutputWriteStream(output);

                        actions.run(process);

                        test.assertEqual(
                            Iterable.create(),
                            Strings.getLines(output.getText().await()));
                        test.assertEqual(0, process.getExitCode());
                        test.assertEqual(1, value.get());
                    }
                });
            });
        });
    }
}
