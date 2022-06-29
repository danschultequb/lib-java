package qub;

public interface CommandLineActionsTests
{
    public static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(CommandLineActions.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final CommandLineActions actions = CommandLineActions.create();
                test.assertNotNull(actions);
                test.assertNull(actions.getProcess());
                test.assertNull(actions.getApplicationName());
                test.assertNull(actions.getApplicationDescription());
                test.assertThrows(() -> actions.getDefaultAction().await(),
                    new NotFoundException("No default action was found."));
            });

            runner.testGroup("setProcess(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.setProcess(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineActions setProcessResult = actions.setProcess(process);
                    test.assertSame(actions, setProcessResult);
                    test.assertSame(process, actions.getProcess());
                });
            });

            runner.testGroup("setApplicationName(String)", () ->
            {
                final Action1<String> setApplicationNameTest = (String applicationName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(applicationName), (Test test) ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        test.assertSame(actions, actions.setApplicationName(applicationName));
                        test.assertEqual(applicationName, actions.getApplicationName());
                    });
                };

                setApplicationNameTest.run(null);
                setApplicationNameTest.run("");
                setApplicationNameTest.run("hello");
                setApplicationNameTest.run("hello there");
            });

            runner.testGroup("setApplicationDescription(String)", () ->
            {
                final Action1<String> setApplicationDescriptionTest = (String applicationDescription) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(applicationDescription), (Test test) ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        test.assertSame(actions, actions.setApplicationDescription(applicationDescription));
                        test.assertEqual(applicationDescription, actions.getApplicationDescription());
                    });
                };

                setApplicationDescriptionTest.run(null);
                setApplicationDescriptionTest.run("");
                setApplicationDescriptionTest.run("hello");
                setApplicationDescriptionTest.run("hello there");
            });

            runner.testGroup("getFullActionName(String)", () ->
            {
                final Action2<String,Throwable> getFullActionNameErrorTest = (String actionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(actionName), (Test test) ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        test.assertThrows(() -> actions.getFullActionName(actionName),
                            expected);
                    });
                };

                getFullActionNameErrorTest.run(null, new PreConditionFailure("actionName cannot be null."));
                getFullActionNameErrorTest.run("", new PreConditionFailure("actionName cannot be empty."));

                final Action4<String,CommandLineActions,String,String> getFullActionNameTest = (String testName, CommandLineActions actions, String actionName, String expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        test.assertEqual(expected, actions.getFullActionName(actionName));
                    });
                };

                getFullActionNameTest.run("with no application name",
                    CommandLineActions.create(),
                    "hello",
                    "hello");
                getFullActionNameTest.run("with application name",
                    CommandLineActions.create().setApplicationName("hello"),
                    "there",
                    "hello there");
                getFullActionNameTest.run("with application name with spaces",
                    CommandLineActions.create().setApplicationName("hello there my"),
                    "friend",
                    "hello there my friend");
            });

            runner.testGroup("getDefaultAction()", () ->
            {
                runner.test("with no actions", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.getDefaultAction().await(),
                        new NotFoundException("No default action was found."));
                });

                runner.test("with one action that isn't the default action", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> actions.getDefaultAction().await(),
                        new NotFoundException("No default action was found."));
                });

                runner.test("with one action that is the default action", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = actions.addAction("hello", (DesktopProcess process) -> {})
                        .setDefaultAction();
                    test.assertSame(action, actions.getDefaultAction().await());
                });
            });

            runner.testGroup("hasDefaultAction()", () ->
            {
                runner.test("with no actions", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertFalse(actions.hasDefaultAction());
                });

                runner.test("with one action that isn't the default action", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertFalse(actions.hasDefaultAction());
                });

                runner.test("with one action that is the default action", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {})
                        .setDefaultAction();
                    test.assertTrue(actions.hasDefaultAction());
                });
            });

            runner.testGroup("getAction(String)", () ->
            {
                final Action2<String,Throwable> getActionErrorTest = (String actionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(actionName), (Test test) ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        test.assertThrows(() -> actions.getAction(actionName),
                            expected);
                    });
                };

                getActionErrorTest.run(null, new PreConditionFailure("actionName cannot be null."));
                getActionErrorTest.run("", new PreConditionFailure("actionName cannot be empty."));

                runner.test("with no actions", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    test.assertThrows(() -> actions.getAction("hello").await(),
                        new NotFoundException("No action was found with the name \"hello\"."));
                });

                runner.test("with non-existing action name", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    actions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> actions.getAction("test").await(),
                        new NotFoundException("No action was found with the name \"test\"."));
                });

                runner.test("with existing action name", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = actions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, actions.getAction("hello").await());
                });

                runner.test("with existing action name with different case", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = actions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, actions.getAction("heLLo").await());
                });

                runner.test("with existing action alias", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = actions.addAction("hello", (DesktopProcess process) -> {})
                        .addAlias("there");
                    test.assertSame(action, actions.getAction("there").await());
                });

                runner.test("with existing action alias with different case", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = actions.addAction("hello", (DesktopProcess process) -> {})
                        .addAlias("there");
                    test.assertSame(action, actions.getAction("TherE").await());
                });
            });

            runner.testGroup("containsActionName(String)", () ->
            {
                final Action2<String,Throwable> containsActionNameErrorTest = (String actionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(actionName), (Test test) ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        test.assertThrows(() -> actions.containsActionName(actionName),
                            expected);
                    });
                };

                containsActionNameErrorTest.run(null, new PreConditionFailure("actionName cannot be null."));
                containsActionNameErrorTest.run("", new PreConditionFailure("actionName cannot be empty."));

                final Action4<String,Function0<CommandLineActions>,String,Boolean> containsActionNameTest = (String testName, Function0<CommandLineActions> actionsCreator, String actionName, Boolean expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        final CommandLineActions actions = actionsCreator.run();
                        test.assertEqual(expected, actions.containsActionName(actionName));
                    });
                };

                containsActionNameTest.run("with no actions",
                    () -> { return CommandLineActions.create(); },
                    "test",
                    false);
                containsActionNameTest.run("with non-existing actionName",
                    () ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        actions.addAction("hello", (DesktopProcess process) -> {});
                        return actions;
                    },
                    "test",
                    false);
                containsActionNameTest.run("with existing actionName",
                    () ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        actions.addAction("hello", (DesktopProcess process) -> {});
                        return actions;
                    },
                    "hello",
                    true);
                containsActionNameTest.run("with existing actionName with different case",
                    () ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        actions.addAction("hello", (DesktopProcess process) -> {});
                        return actions;
                    },
                    "HELLO",
                    true);
                containsActionNameTest.run("with existing actionName alias",
                    () ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        actions.addAction("hello", (DesktopProcess process) -> {})
                            .addAlias("there");
                        return actions;
                    },
                    "there",
                    true);
                containsActionNameTest.run("with existing actionName alias with different case",
                    () ->
                    {
                        final CommandLineActions actions = CommandLineActions.create();
                        actions.addAction("hello", (DesktopProcess process) -> {})
                            .addAlias("there");
                        return actions;
                    },
                    "THERE",
                    true);
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
