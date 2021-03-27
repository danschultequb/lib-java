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
                final CommandLineActions actions = CommandLineActions.create();
                test.assertNotNull(actions);
                test.assertNull(actions.getApplicationName());
                test.assertNull(actions.getApplicationDescription());
            });

            runner.testGroup("setApplicationName(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationName(null));
                    test.assertEqual(null, actions.getApplicationName());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationName(""));
                    test.assertEqual("", actions.getApplicationName());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationName("hello there"));
                    test.assertEqual("hello there", actions.getApplicationName());
                });
            });

            runner.testGroup("setApplicationDescription(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationDescription(null));
                    test.assertEqual(null, actions.getApplicationDescription());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationDescription(""));
                    test.assertEqual("", actions.getApplicationDescription());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertSame(actions, actions.setApplicationDescription("hello there"));
                    test.assertEqual("hello there", actions.getApplicationDescription());
                });
            });

            runner.testGroup("getFullActionName(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.getFullActionName(null),
                        new PreConditionFailure("actionName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.getFullActionName(""),
                        new PreConditionFailure("actionName cannot be empty."));
                });

                runner.test("with no application name", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertEqual("hello", actions.getFullActionName("hello"));
                });

                runner.test("with application name", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create()
                        .setApplicationName("hello");
                    test.assertEqual("hello there", actions.getFullActionName("there"));
                });

                runner.test("with application name with spaces", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create()
                        .setApplicationName("hello there my");
                    test.assertEqual("hello there my friend", actions.getFullActionName("friend"));
                });
            });

            runner.testGroup("containsActionName(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.containsActionName(null),
                        new PreConditionFailure("actionName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.containsActionName(""),
                        new PreConditionFailure("actionName cannot be empty."));
                });

                runner.test("with non-existing actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertFalse(actions.containsActionName("test"));
                });

                runner.test("with existing actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertTrue(actions.containsActionName("hello"));
                });

                runner.test("with existing alias actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {})
                        .addAlias("there");
                    test.assertTrue(actions.containsActionName("there"));
                });
            });

            runner.testGroup("addAction(Action1<CommandLineActions>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction(null),
                        new PreConditionFailure("actionAdder cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineActions addActionResult = actions.addAction((CommandLineActions innerActions) ->
                    {
                        innerActions.addAction("hello", (DesktopProcess process) -> {});
                    });
                    test.assertSame(actions, addActionResult);
                    test.assertTrue(actions.containsActionName("hello"));
                });
            });

            runner.testGroup("addAction(String,Action1<DesktopProcess>)", () ->
            {
                runner.test("with null actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction(null, (DesktopProcess process) -> {}),
                        new PreConditionFailure("actionName cannot be null."));
                });

                runner.test("with empty actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction("", (DesktopProcess process) -> {}),
                        new PreConditionFailure("actionName cannot be empty."));
                });

                runner.test("with null mainAction", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction("hello", (Action1<DesktopProcess>)null),
                        new PreConditionFailure("mainAction cannot be null."));
                });

                runner.test("with empty actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = actions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertEqual("hello", action.getName());
                    test.assertEqual(Iterable.create(), action.getAliases());
                    test.assertNull(action.getDescription());
                });

                runner.test("with existing actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> actions.addAction("hello", (DesktopProcess process) -> {}),
                        new PreConditionFailure("this.containsActionName(actionName) cannot be true."));
                    test.assertTrue(actions.containsActionName("hello"));
                });

                runner.test("with existing actionName alias", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {})
                        .addAlias("there");
                    test.assertThrows(() -> actions.addAction("there", (DesktopProcess process) -> {}),
                        new PreConditionFailure("this.containsActionName(actionName) cannot be true."));
                    test.assertTrue(actions.containsActionName("hello"));
                    test.assertTrue(actions.containsActionName("there"));
                });
            });

            runner.testGroup("addAction(String,Function1<DesktopProcess,Integer>)", () ->
            {
                runner.test("with null actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction(null, (DesktopProcess process) -> 1),
                        new PreConditionFailure("actionName cannot be null."));
                });

                runner.test("with empty actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction("", (DesktopProcess process) -> 2),
                        new PreConditionFailure("actionName cannot be empty."));
                });

                runner.test("with null mainAction", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.addAction("hello", (Function1<DesktopProcess,Integer>)null),
                        new PreConditionFailure("mainFunction cannot be null."));
                });

                runner.test("with empty actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = actions.addAction("hello", (DesktopProcess process) -> 3);
                    test.assertEqual("hello", action.getName());
                    test.assertEqual(Iterable.create(), action.getAliases());
                    test.assertNull(action.getDescription());
                });

                runner.test("with existing actionName", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> actions.addAction("hello", (DesktopProcess process) -> 4),
                        new PreConditionFailure("this.containsActionName(actionName) cannot be true."));
                    test.assertTrue(actions.containsActionName("hello"));
                });

                runner.test("with existing actionName alias", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {})
                        .addAlias("there");
                    test.assertThrows(() -> actions.addAction("there", (DesktopProcess process) -> 5),
                        new PreConditionFailure("this.containsActionName(actionName) cannot be true."));
                    test.assertTrue(actions.containsActionName("hello"));
                    test.assertTrue(actions.containsActionName("there"));
                });
            });

            runner.testGroup("run(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.run(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with no actions and no arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");

                        actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?):   Show the help message for this application."),
                            process.getOutputWriteStream());
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with no actions and empty action argument", (Test test) ->
                {
                    try (FakeDesktopProcess process = FakeDesktopProcess.create("--action="))
                    {
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");

                        actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?):   Show the help message for this application."),
                            process.getOutputWriteStream());
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with no actions and non-empty action argument", (Test test) ->
                {
                    try (FakeDesktopProcess process = FakeDesktopProcess.create("--action=stuff"))
                    {
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");

                        actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(
                                "Unrecognized action: \"stuff\"",
                                "",
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?):   Show the help message for this application."),
                            process.getOutputWriteStream());
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with one action and empty action argument", (Test test) ->
                {
                    try (FakeDesktopProcess process = FakeDesktopProcess.create("--action="))
                    {
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");
                        actions.addAction("update", (DesktopProcess desktopProcess) -> {})
                            .setDescription("Perform the update action");

                        actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?):   Show the help message for this application.",
                                "",
                                "Actions:",
                                "  update: Perform the update action"),
                            process.getOutputWriteStream());
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with one action and empty action argument", (Test test) ->
                {
                    try (FakeDesktopProcess process = FakeDesktopProcess.create("--action="))
                    {
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");
                        actions.addAction("update", (DesktopProcess desktopProcess) -> {})
                            .setDescription("Update the thing.");

                        actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?):   Show the help message for this application.",
                                "",
                                "Actions:",
                                "  update: Update the thing."),
                            process.getOutputWriteStream());
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with one action and non-matching action argument", (Test test) ->
                {
                    try (FakeDesktopProcess process = FakeDesktopProcess.create("--action=foo"))
                    {
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");
                        actions.addAction("update", (DesktopProcess desktopProcess) -> {});

                        actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(
                                "Unrecognized action: \"foo\"",
                                "",
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?):   Show the help message for this application.",
                                "",
                                "Actions:",
                                "  update: (No description provided)"),
                            process.getOutputWriteStream());
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with two actions and non-matching action argument", (Test test) ->
                {
                    try (FakeDesktopProcess process = FakeDesktopProcess.create("--action=foo"))
                    {
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");
                        actions.addAction("update", (DesktopProcess desktopProcess) -> {});
                        actions.addAction("list", (DesktopProcess desktopProcess) -> {})
                            .setDescription("Do the list action.");

                        actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(
                                "Unrecognized action: \"foo\"",
                                "",
                                "Usage: hello [--action=]<action-name> [--help]",
                                "  there",
                                "  --action(a): The name of the action to invoke.",
                                "  --help(?):   Show the help message for this application.",
                                "",
                                "Actions:",
                                "  list:   Do the list action.",
                                "  update: (No description provided)"),
                            process.getOutputWriteStream());
                        test.assertEqual(-1, process.getExitCode());
                    }
                });

                runner.test("with two actions, matching action argument, and -?", (Test test) ->
                {
                    try (FakeDesktopProcess process = FakeDesktopProcess.create("--action=list", "-?"))
                    {
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");
                        actions.addAction("update", (DesktopProcess desktopProcess) -> {});
                        actions.addAction("list", (DesktopProcess desktopProcess) -> {})
                            .setDescription("Do the list action.");

                    actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(),
                            process.getOutputWriteStream());
                        test.assertLinesEqual(
                            Iterable.create(),
                            process.getErrorWriteStream());
                        test.assertEqual(0, process.getExitCode());
                    }
                });

                runner.test("with one action and name-matching action argument", (Test test) ->
                {
                    try (FakeDesktopProcess process = FakeDesktopProcess.create("--action=update"))
                    {
                        final IntegerValue value = IntegerValue.create(0);
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");
                        actions.addAction("update", (DesktopProcess desktopProcess) -> { value.increment(); });

                        actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(),
                            process.getOutputWriteStream());
                        test.assertEqual(0, process.getExitCode());
                        test.assertEqual(1, value.get());
                    }
                });

                runner.test("with one action and alias-matching action argument", (Test test) ->
                {
                    try (FakeDesktopProcess process = FakeDesktopProcess.create("--action=u"))
                    {
                        final IntegerValue value = IntegerValue.create(0);
                        final CommandLineActions actions = CommandLineActions.create()
                            .setApplicationName("hello")
                            .setApplicationDescription("there");
                        actions.addAction("update", (DesktopProcess desktopProcess) -> { value.increment(); })
                            .addAlias("u");

                        actions.run(process);

                        test.assertLinesEqual(
                            Iterable.create(),
                            process.getOutputWriteStream());
                        test.assertEqual(0, process.getExitCode());
                        test.assertEqual(1, value.get());
                    }
                });
            });
        });
    }
}
