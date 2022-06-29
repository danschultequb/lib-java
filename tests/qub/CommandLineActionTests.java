package qub;

public interface CommandLineActionTests
{
    public static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(CommandLineAction.class, () ->
        {
            runner.testGroup("create(String,Action1<DesktopProcess>)", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineAction.create(null, (DesktopProcess process) -> {}),
                        new PreConditionFailure("name cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineAction.create("", (DesktopProcess process) -> {}),
                        new PreConditionFailure("name cannot be empty."));
                });

                runner.test("with non-empty name", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertEqual("hello", action.getName());
                    test.assertEqual(Iterable.create(), action.getAliases());
                    test.assertNull(action.getDescription());
                    test.assertFalse(action.isDefaultAction());
                });

                runner.test("with null mainAction", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineAction.create("hello", (Action1<DesktopProcess>)null),
                        new PreConditionFailure("mainAction cannot be null."));
                });
            });

            runner.testGroup("create(String,Action2<DesktopProcess,CommandLineAction>)", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineAction.create(null, (DesktopProcess process, CommandLineAction action) -> {}),
                        new PreConditionFailure("name cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineAction.create("", (DesktopProcess process, CommandLineAction action) -> {}),
                        new PreConditionFailure("name cannot be empty."));
                });

                runner.test("with non-empty name", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process, CommandLineAction currentAction) -> {});
                    test.assertEqual("hello", action.getName());
                    test.assertEqual(Iterable.create(), action.getAliases());
                    test.assertNull(action.getDescription());
                    test.assertFalse(action.isDefaultAction());
                });

                runner.test("with null mainAction", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineAction.create("hello", (Action2<DesktopProcess,CommandLineAction>)null),
                        new PreConditionFailure("mainAction cannot be null."));
                });
            });

            runner.testGroup("getFullName()", () ->
            {
                runner.test("with no parentActions set", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertEqual("hello", action.getName());
                    test.assertEqual("hello", action.getFullName());
                });

                runner.test("with parentActions set with null applicationName", (Test test) ->
                {
                    final CommandLineActions parentActions = CommandLineActions.create();
                    final CommandLineAction action = parentActions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertEqual("hello", action.getName());
                    test.assertEqual("hello", action.getFullName());
                });

                runner.test("with parentActions set with empty applicationName", (Test test) ->
                {
                    final CommandLineActions parentActions = CommandLineActions.create();
                    parentActions.setApplicationName("");
                    final CommandLineAction action = parentActions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertEqual("hello", action.getName());
                    test.assertEqual("hello", action.getFullName());
                });

                runner.test("with parentActions set with non-empty applicationName", (Test test) ->
                {
                    final CommandLineActions parentActions = CommandLineActions.create();
                    parentActions.setApplicationName("abc");
                    final CommandLineAction action = parentActions.addAction("hello", (DesktopProcess process) -> {});
                    test.assertEqual("hello", action.getName());
                    test.assertEqual("abc hello", action.getFullName());
                });
            });

            runner.testGroup("addAlias(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAlias(null),
                        new PreConditionFailure("alias cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAlias(""),
                        new PreConditionFailure("alias cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.addAlias("h"));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with alias equal to action's name", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAlias("hello"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with alias already added to action", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.addAlias("h"));
                    test.assertThrows(() -> action.addAlias("h"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with alias already in use as a different action's name", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action1 = actions.addAction("hello", (DesktopProcess process) -> {});
                    final CommandLineAction action2 = actions.addAction("there", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action1.addAlias("there"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create(), action2.getAliases());
                });

                runner.test("with alias already in use as a different action's alias", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action1 = actions.addAction("hello", (DesktopProcess process) -> {});
                    final CommandLineAction action2 = actions.addAction("there", (DesktopProcess process) -> {})
                        .addAlias("h");
                    test.assertThrows(() -> action1.addAlias("h"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create("h"), action2.getAliases());
                });
            });

            runner.testGroup("addAliases(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases(),
                        new PreConditionFailure("aliases cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with null array", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases((String[])null),
                        new PreConditionFailure("aliases cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases(new String[0]),
                        new PreConditionFailure("aliases cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with null String", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases((String)null),
                        new PreConditionFailure("alias cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases(""),
                        new PreConditionFailure("alias cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with one String", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.addAliases("h"));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with two Strings", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.addAliases("h", "i"));
                    test.assertEqual(Iterable.create("h", "i"), action.getAliases());
                });

                runner.test("with alias equal to action's name", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases("hello"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with alias already added to action", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.addAliases("h"));
                    test.assertThrows(() -> action.addAliases("h"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with alias already in use as a different action's name", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action1 = actions.addAction("hello", (DesktopProcess process) -> {});
                    final CommandLineAction action2 = actions.addAction("there", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action1.addAliases("there"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create(), action2.getAliases());
                });

                runner.test("with alias already in use as a different action's alias", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action1 = actions.addAction("hello", (DesktopProcess process) -> {});
                    final CommandLineAction action2 = actions.addAction("there", (DesktopProcess process) -> {})
                        .addAliases("h");
                    test.assertThrows(() -> action1.addAliases("h"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create("h"), action2.getAliases());
                });
            });

            runner.testGroup("addAliases(Iterable<String>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases((Iterable<String>)null),
                        new PreConditionFailure("aliases cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases(Iterable.create()),
                        new PreConditionFailure("aliases cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with null String", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases(Iterable.create((String)null)),
                        new PreConditionFailure("alias cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases(Iterable.create("")),
                        new PreConditionFailure("alias cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with one String", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.addAliases(Iterable.create("h")));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with two Strings", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.addAliases(Iterable.create("h", "i")));
                    test.assertEqual(Iterable.create("h", "i"), action.getAliases());
                });

                runner.test("with alias equal to action's name", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.addAliases(Iterable.create("hello")),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with alias already added to action", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.addAliases(Iterable.create("h")));
                    test.assertThrows(() -> action.addAliases(Iterable.create("h")),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with alias already in use as a different action's name", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action1 = actions.addAction("hello", (DesktopProcess process) -> {});
                    final CommandLineAction action2 = actions.addAction("there", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action1.addAliases(Iterable.create("there")),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create(), action2.getAliases());
                });

                runner.test("with alias already in use as a different action's alias", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action1 = actions.addAction("hello", (DesktopProcess process) -> {});
                    final CommandLineAction action2 = actions.addAction("there", (DesktopProcess process) -> {})
                        .addAliases(Iterable.create("h"));
                    test.assertThrows(() -> action1.addAliases(Iterable.create("h")),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create("h"), action2.getAliases());
                });
            });

            runner.testGroup("setDescription(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.setDescription(null));
                    test.assertNull(action.getDescription());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.setDescription(""));
                    test.assertEqual("", action.getDescription());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.setDescription("there"));
                    test.assertEqual("there", action.getDescription());
                });
            });

            runner.testGroup("setDefaultAction()", () ->
            {
                runner.test("with no parent actions", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});

                    final CommandLineAction setDefaultActionResult = action.setDefaultAction();
                    test.assertSame(action, setDefaultActionResult);
                    test.assertTrue(action.isDefaultAction());

                    final CommandLineAction setDefaultActionResult2 = action.setDefaultAction();
                    test.assertSame(action, setDefaultActionResult2);
                    test.assertTrue(action.isDefaultAction());
                });

                runner.test("with parent actions with no other actions", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {})
                        .setParentActions(actions);

                    final CommandLineAction setDefaultActionResult = action.setDefaultAction();
                    test.assertSame(action, setDefaultActionResult);
                    test.assertTrue(action.isDefaultAction());

                    final CommandLineAction setDefaultActionResult2 = action.setDefaultAction();
                    test.assertSame(action, setDefaultActionResult2);
                    test.assertTrue(action.isDefaultAction());
                });

                runner.test("with parent actions with a different default action", (Test test) ->
                {
                    final CommandLineActions actions = CommandLineActions.create();
                    final CommandLineAction action1 = actions.addAction("hello", (DesktopProcess process) -> {});
                    final CommandLineAction action2 = actions.addAction("there", (DesktopProcess process) -> {});

                    final CommandLineAction setDefaultActionResult = action1.setDefaultAction();
                    test.assertSame(action1, setDefaultActionResult);
                    test.assertTrue(action1.isDefaultAction());
                    test.assertTrue(actions.hasDefaultAction());
                    test.assertSame(action1, actions.getDefaultAction().await());

                    test.assertThrows(() -> action2.setDefaultAction(),
                        new PreConditionFailure("this.isDefaultAction() || this.parentActions == null || !this.parentActions.hasDefaultAction() cannot be false."));
                    test.assertFalse(action2.isDefaultAction());
                    test.assertTrue(action1.isDefaultAction());
                    test.assertTrue(actions.hasDefaultAction());
                    test.assertSame(action1, actions.getDefaultAction().await());
                });
            });

            runner.testGroup("setParentActions(CommandLineActions)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertSame(action, action.setParentActions(null));
                });
            });

            runner.testGroup("createCommandLineParameters(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DesktopProcess process = null;
                    final CommandLineAction action = CommandLineAction.create("fake-action-name", (DesktopProcess actionProcess) -> {});
                    test.assertThrows(() -> action.createCommandLineParameters(process),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with no parentActions and no description", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineAction action = CommandLineAction.create("fake-action-name", (DesktopProcess actionProcess) -> {});
                        final CommandLineParameters parameters = action.createCommandLineParameters(process);
                        test.assertNotNull(parameters);
                        test.assertEqual("fake-action-name", parameters.getApplicationName());
                        test.assertNull(parameters.getApplicationDescription());
                    }
                });

                runner.test("with parentActions and no description", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineActions actions = process.createCommandLineActions()
                            .setApplicationName("fake-application-name")
                            .setApplicationDescription("fake-application-description");
                        final CommandLineAction action = actions.addAction("fake-action-name", (DesktopProcess actionProcess) -> {});
                        final CommandLineParameters parameters = action.createCommandLineParameters(process);
                        test.assertNotNull(parameters);
                        test.assertEqual("fake-application-name fake-action-name", parameters.getApplicationName());
                        test.assertNull(parameters.getApplicationDescription());
                    }
                });

                runner.test("with parentActions and description", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final CommandLineActions actions = process.createCommandLineActions()
                            .setApplicationName("fake-application-name")
                            .setApplicationDescription("fake-application-description");
                        final CommandLineAction action = actions.addAction("fake-action-name", (DesktopProcess actionProcess) -> {})
                            .setDescription("fake-action-description");
                        final CommandLineParameters parameters = action.createCommandLineParameters(process);
                        test.assertNotNull(parameters);
                        test.assertEqual("fake-application-name fake-action-name", parameters.getApplicationName());
                        test.assertEqual("fake-action-description", parameters.getApplicationDescription());
                    }
                });
            });

            runner.testGroup("createCommandLineParameters(CommandLineArguments)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineArguments arguments = null;
                    final CommandLineAction action = CommandLineAction.create("fake-action-name", (DesktopProcess actionProcess) -> {});
                    test.assertThrows(() -> action.createCommandLineParameters(arguments),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with no parentActions and no description", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineAction action = CommandLineAction.create("fake-action-name", (DesktopProcess actionProcess) -> {});
                    final CommandLineParameters parameters = action.createCommandLineParameters(arguments);
                    test.assertNotNull(parameters);
                    test.assertEqual("fake-action-name", parameters.getApplicationName());
                    test.assertNull(parameters.getApplicationDescription());
                });

                runner.test("with parentActions and no description", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineActions actions = CommandLineActions.create()
                        .setApplicationName("fake-application-name")
                        .setApplicationDescription("fake-application-description");
                    final CommandLineAction action = actions.addAction("fake-action-name", (DesktopProcess actionProcess) -> {});
                    final CommandLineParameters parameters = action.createCommandLineParameters(arguments);
                    test.assertNotNull(parameters);
                    test.assertEqual("fake-application-name fake-action-name", parameters.getApplicationName());
                    test.assertNull(parameters.getApplicationDescription());
                });

                runner.test("with parentActions and description", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineActions actions = CommandLineActions.create()
                        .setApplicationName("fake-application-name")
                        .setApplicationDescription("fake-application-description");
                    final CommandLineAction action = actions.addAction("fake-action-name", (DesktopProcess actionProcess) -> {})
                        .setDescription("fake-action-description");
                    final CommandLineParameters parameters = action.createCommandLineParameters(arguments);
                    test.assertNotNull(parameters);
                    test.assertEqual("fake-application-name fake-action-name", parameters.getApplicationName());
                    test.assertEqual("fake-action-description", parameters.getApplicationDescription());
                });
            });

            runner.testGroup("createCommandLineActions()", () ->
            {
                runner.test("with no parentActions and no description", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("fake-action-name", (DesktopProcess process) -> {});
                    final CommandLineActions actions = action.createCommandLineActions();
                    test.assertNotNull(actions);
                    test.assertEqual("fake-action-name", actions.getApplicationName());
                    test.assertNull(actions.getApplicationDescription());
                });

                runner.test("with parentActions and no description", (Test test) ->
                {
                    final CommandLineActions parentActions = CommandLineActions.create()
                        .setApplicationName("fake-application-name")
                        .setApplicationDescription("fake-application-description");
                    final CommandLineAction action = parentActions.addAction("fake-action-name", (DesktopProcess process) -> {});
                    final CommandLineActions actions = action.createCommandLineActions();
                    test.assertNotNull(actions);
                    test.assertEqual("fake-application-name fake-action-name", actions.getApplicationName());
                    test.assertNull(actions.getApplicationDescription());
                });

                runner.test("with parentActions and description", (Test test) ->
                {
                    final CommandLineActions parentActions = CommandLineActions.create()
                        .setApplicationName("fake-application-name")
                        .setApplicationDescription("fake-application-description");
                    final CommandLineAction action = parentActions.addAction("fake-action-name", (DesktopProcess process) -> {})
                        .setDescription("fake-action-description");
                    final CommandLineActions actions = action.createCommandLineActions();
                    test.assertNotNull(actions);
                    test.assertEqual("fake-application-name fake-action-name", actions.getApplicationName());
                    test.assertEqual("fake-action-description", actions.getApplicationDescription());
                });
            });

            runner.testGroup("run(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> {});
                    test.assertThrows(() -> action.run(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final CommandLineAction action = CommandLineAction.create("hello", (DesktopProcess process) -> { value.increment(); });
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        action.run(process);
                        test.assertEqual(1, value.get());
                    }
                });
            });
        });
    }
}
