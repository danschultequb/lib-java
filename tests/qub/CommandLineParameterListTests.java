package qub;

public interface CommandLineParameterListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineParameter.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> new CommandLineParameterList<>(null, null, Result::success),
                        new PreConditionFailure("name cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> new CommandLineParameterList<>("", null, Result::success),
                        new PreConditionFailure("name cannot be empty."));
                });

                runner.test("with non-empty name", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertNotNull(parameter);
                    test.assertEqual("fakeName", parameter.getName());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                    test.assertNull(parameter.getDescription());
                    test.assertNull(parameter.getValueName());
                    test.assertNull(parameter.getIndex());
                    test.assertFalse(parameter.isRequired());
                    test.assertFalse(parameter.isValueRequired());
                    test.assertEqual("[--fakeName]", parameter.getUsageString());
                });

                runner.test("with negative index", (Test test) ->
                {
                    test.assertThrows(() -> new CommandLineParameterList<>("fakeName", -1, Result::success),
                        new PreConditionFailure("index (-1) must be null or greater than or equal to 0."));
                });

                runner.test("with zero index", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success);
                    test.assertNotNull(parameter);
                    test.assertEqual("fakeName", parameter.getName());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                    test.assertNull(parameter.getDescription());
                    test.assertEqual("<fakeName-value>", parameter.getValueName());
                    test.assertEqual(0, parameter.getIndex());
                    test.assertFalse(parameter.isRequired());
                    test.assertTrue(parameter.isValueRequired());
                    test.assertEqual("[[--fakeName=]<fakeName-value>]", parameter.getUsageString());
                });

                runner.test("with positive index", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 3, Result::success);
                    test.assertNotNull(parameter);
                    test.assertEqual("fakeName", parameter.getName());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                    test.assertNull(parameter.getDescription());
                    test.assertEqual("<fakeName-value>", parameter.getValueName());
                    test.assertEqual(3, parameter.getIndex());
                    test.assertFalse(parameter.isRequired());
                    test.assertTrue(parameter.isValueRequired());
                    test.assertEqual("[[--fakeName=]<fakeName-value>]", parameter.getUsageString());
                });
            });

            runner.testGroup("setDescription(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    final CommandLineParameterList<String> next = parameter.setDescription(null);
                    test.assertSame(parameter, next);
                    test.assertNull(parameter.getDescription());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setDescription(""));
                    test.assertEqual("", parameter.getDescription());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setDescription("hello"));
                    test.assertEqual("hello", parameter.getDescription());
                });
            });

            runner.testGroup("setAliases(String...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertThrows(() -> parameter.setAliases((String[])null),
                        new PreConditionFailure("aliases cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setAliases());
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setAliases(new String[0]));
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setAliases(new String[] { "a", "b" }));
                    test.assertEqual(Iterable.create("a", "b"), parameter.getAliases());
                });

                runner.test("with non-empty when aliases are already set", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setAliases(new String[] { "a", "b" }));
                    test.assertEqual(Iterable.create("a", "b"), parameter.getAliases());

                    test.assertSame(parameter, parameter.setAliases(new String[] { "c" }));
                    test.assertEqual(Iterable.create("c"), parameter.getAliases());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setAliases("a"));
                    test.assertEqual(Iterable.create("a"), parameter.getAliases());
                });

                runner.test("with two arguments", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setAliases("a", "b"));
                    test.assertEqual(Iterable.create("a", "b"), parameter.getAliases());
                });
            });

            runner.testGroup("setAliases(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertThrows(() -> parameter.setAliases((Iterable<String>)null),
                        new PreConditionFailure("aliases cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setAliases(Iterable.create()));
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setAliases(Iterable.create("a", "b")));
                    test.assertEqual(Iterable.create("a", "b"), parameter.getAliases());
                });

                runner.test("with non-empty when aliases are already set", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setAliases(Iterable.create("a", "b")));
                    test.assertEqual(Iterable.create("a", "b"), parameter.getAliases());

                    test.assertSame(parameter, parameter.setAliases(Iterable.create("c")));
                    test.assertEqual(Iterable.create("c"), parameter.getAliases());
                });
            });

            runner.testGroup("addAlias(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertThrows(() -> parameter.addAlias(null),
                        new PreConditionFailure("alias cannot be null."));
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertThrows(() -> parameter.addAlias(""),
                        new PreConditionFailure("alias cannot be empty."));
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.addAlias("abc"));
                    test.assertEqual(Iterable.create("abc"), parameter.getAliases());
                });

                runner.test("with non-empty when aliases already are set", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.addAlias("abc"));
                    test.assertEqual(Iterable.create("abc"), parameter.getAliases());

                    test.assertSame(parameter, parameter.addAlias("def"));
                    test.assertEqual(Iterable.create("abc", "def"), parameter.getAliases());
                });
            });

            runner.testGroup("addAliases(String...)", () ->
            {
                runner.test("with null String[]", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertThrows(() -> parameter.addAliases((String[])null),
                        new PreConditionFailure("aliases cannot be null."));
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                });

                runner.test("with null String[]", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertThrows(() -> parameter.addAliases((String)null),
                        new PreConditionFailure("alias cannot be null."));
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertThrows(() -> parameter.addAliases(""),
                        new PreConditionFailure("alias cannot be empty."));
                    test.assertEqual(Iterable.create(), parameter.getAliases());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.addAliases("abc"));
                    test.assertEqual(Iterable.create("abc"), parameter.getAliases());
                });

                runner.test("with non-empty when aliases already are set", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.addAliases("abc"));
                    test.assertEqual(Iterable.create("abc"), parameter.getAliases());

                    test.assertSame(parameter, parameter.addAliases("def"));
                    test.assertEqual(Iterable.create("abc", "def"), parameter.getAliases());
                });
            });

            runner.testGroup("setValueName(String)", () ->
            {
                runner.test("with null with null index", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setValueName(null));
                    test.assertNull(parameter.getValueName());
                });

                runner.test("with empty with null index", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setValueName(""));
                    test.assertEqual("", parameter.getValueName());
                });

                runner.test("with non-empty with null index", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setValueName("hello"));
                    test.assertEqual("hello", parameter.getValueName());
                });

                runner.test("with null with non-null index", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success);
                    test.assertThrows(() -> parameter.setValueName(null),
                        new PreConditionFailure("!Strings.isNullOrEmpty(valueName) || index == null cannot be false."));
                    test.assertEqual("<fakeName-value>", parameter.getValueName());
                });

                runner.test("with empty with non-null index", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success);
                    test.assertThrows(() -> parameter.setValueName(""),
                        new PreConditionFailure("!Strings.isNullOrEmpty(valueName) || index == null cannot be false."));
                    test.assertEqual("<fakeName-value>", parameter.getValueName());
                });

                runner.test("with non-empty with non-null index", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success);
                    test.assertSame(parameter, parameter.setValueName("hello"));
                    test.assertEqual("hello", parameter.getValueName());
                });
            });

            runner.testGroup("setRequired(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setRequired(false));
                    test.assertFalse(parameter.isRequired());
                });

                runner.test("with true", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setRequired(true));
                    test.assertTrue(parameter.isRequired());
                });
            });

            runner.testGroup("setValueRequired(boolean)", () ->
            {
                runner.test("with false when index is null", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setValueRequired(false));
                    test.assertFalse(parameter.isValueRequired());
                    test.assertFalse(parameter.isRequired());
                    test.assertNull(parameter.getValueName());
                });

                runner.test("with true when index is null", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setValueRequired(true));
                    test.assertTrue(parameter.isValueRequired());
                    test.assertFalse(parameter.isRequired());
                    test.assertEqual("<fakeName-value>", parameter.getValueName());
                });

                runner.test("with false when index is not null", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success);
                    test.assertThrows(() -> parameter.setValueRequired(false),
                        new PreConditionFailure("valueRequired || index == null cannot be false."));
                    test.assertEqual(0, parameter.getIndex());
                    test.assertTrue(parameter.isValueRequired());
                    test.assertEqual("<fakeName-value>", parameter.getValueName());
                });

                runner.test("with true when index is not null", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertSame(parameter, parameter.setValueRequired(true));
                    test.assertTrue(parameter.isValueRequired());
                    test.assertFalse(parameter.isRequired());
                    test.assertEqual("<fakeName-value>", parameter.getValueName());
                });

                runner.test("with false when valueName is empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    parameter.setValueName("");
                    parameter.setValueRequired(false);
                    test.assertFalse(parameter.isValueRequired());
                    test.assertEqual("", parameter.getValueName());
                });

                runner.test("with true when valueName is empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    parameter.setValueName("");
                    test.assertSame(parameter, parameter.setValueRequired(true));
                    test.assertTrue(parameter.isValueRequired());
                    test.assertFalse(parameter.isRequired());
                    test.assertEqual("<fakeName-value>", parameter.getValueName());
                });

                runner.test("with false when valueName is non-empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    parameter.setValueName("abc");
                    parameter.setValueRequired(false);
                    test.assertFalse(parameter.isValueRequired());
                    test.assertEqual("abc", parameter.getValueName());
                });

                runner.test("with true when valueName is non-empty", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    parameter.setValueName("abc");
                    test.assertSame(parameter, parameter.setValueRequired(true));
                    test.assertTrue(parameter.isValueRequired());
                    test.assertFalse(parameter.isRequired());
                    test.assertEqual("abc", parameter.getValueName());
                });
            });

            runner.testGroup("getValues()", () ->
            {
                runner.test("with no arguments set", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertThrows(parameter::getValues,
                        new PreConditionFailure("getArguments() cannot be null."));
                });

                runner.test("with null index and no argument with matching name", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    parameter.setArguments(new CommandLineArguments());
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                });

                runner.test("with null index and argument with matching name and empty value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    parameter.setArguments(new CommandLineArguments()
                        .addNamedArgument("fakeName", ""));
                    test.assertEqual(Iterable.create(""), parameter.getValues().await());
                });

                runner.test("with null index and argument with matching name and non-empty value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    parameter.setArguments(new CommandLineArguments()
                        .addNamedArgument("fakeName", "abc"));
                    test.assertEqual(Iterable.create("abc"), parameter.getValues().await());
                });

                runner.test("with non-null index and no argument with matching name or matching position", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success);
                    parameter.setArguments(new CommandLineArguments());
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                });

                runner.test("with non-null index and argument with matching position", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success);
                    parameter.setArguments(new CommandLineArguments()
                        .addAnonymousArgument("spices"));
                    test.assertEqual(Iterable.create("spices"), parameter.getValues().await());
                });

                runner.test("with non-null index and argument with matching name and empty value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success);
                    parameter.setArguments(new CommandLineArguments()
                        .addNamedArgument("fakeName", ""));
                    test.assertEqual(Iterable.create(""), parameter.getValues().await());
                });

                runner.test("with non-null index and argument with matching name and non-empty value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success);
                    parameter.setArguments(new CommandLineArguments()
                        .addNamedArgument("fakeName", "abc"));
                    test.assertEqual(Iterable.create("abc"), parameter.getValues().await());
                });
            });

            runner.testGroup("removeValues()", () ->
            {
                runner.test("with no arguments set", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertThrows(parameter::removeValues,
                        new PreConditionFailure("getArguments() cannot be null."));
                });

                runner.test("with null index and no argument with matching name", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setArguments(arguments);
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                });

                runner.test("with null index and argument with matching name and empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--fakeName");
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setArguments(arguments);
                    test.assertEqual(Iterable.create(""), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                });

                runner.test("with null index and argument with matching name and non-empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--fakeName=abc");
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setArguments(arguments);
                    test.assertEqual(Iterable.create("abc"), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                });

                runner.test("with non-null index and no argument with matching name or matching position", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create();
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success)
                        .setArguments(arguments);
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                });

                runner.test("with non-null index and argument with matching position", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("spices");
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success)
                        .setArguments(arguments);
                    test.assertEqual(Iterable.create("spices"), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                });

                runner.test("with non-null index and argument with matching name and empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--fakeName");
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success)
                        .setArguments(arguments);
                    test.assertEqual(Iterable.create(""), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                });

                runner.test("with non-null index and argument with matching name and non-empty value", (Test test) ->
                {
                    final CommandLineArguments arguments = CommandLineArguments.create("--fakeName=abc");
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", 0, Result::success)
                        .setArguments(arguments);
                    test.assertEqual(Iterable.create("abc"), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                    test.assertEqual(Iterable.create(), parameter.removeValues().await());
                    test.assertEqual("[]", arguments.toString());
                });
            });

            runner.testGroup("getUsageString()", () ->
            {
                runner.test("with null valueName, not required, and not required value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setValueName(null)
                        .setRequired(false)
                        .setValueRequired(false);
                    test.assertEqual("[--fakeName]", parameter.getUsageString());
                });

                runner.test("with null valueName, not required, and required value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setValueName(null)
                        .setRequired(false)
                        .setValueRequired(true);
                    test.assertEqual("[--fakeName=<fakeName-value>]", parameter.getUsageString());
                });

                runner.test("with empty valueName, not required, and not required value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setValueName("")
                        .setRequired(false)
                        .setValueRequired(false);
                    test.assertEqual("[--fakeName]", parameter.getUsageString());
                });

                runner.test("with empty valueName, not required, and required value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setValueName("")
                        .setRequired(false)
                        .setValueRequired(true);
                    test.assertEqual("[--fakeName=<fakeName-value>]", parameter.getUsageString());
                });

                runner.test("with non-empty valueName, not required, and required value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setValueName("<true|false>")
                        .setRequired(false)
                        .setValueRequired(true);
                    test.assertEqual("[--fakeName=<true|false>]", parameter.getUsageString());
                });

                runner.test("with non-empty valueName, not required, and not required value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setValueName("<true|false>")
                        .setRequired(false)
                        .setValueRequired(false);
                    test.assertEqual("[--fakeName[=<true|false>]]", parameter.getUsageString());
                });

                runner.test("with non-empty valueName, required, and required value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setValueName("<true|false>")
                        .setRequired(true)
                        .setValueRequired(true);
                    test.assertEqual("--fakeName=<true|false>", parameter.getUsageString());
                });

                runner.test("with non-empty valueName, required, and not required value", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setValueName("<true|false>")
                        .setRequired(true)
                        .setValueRequired(false);
                    test.assertEqual("--fakeName[=<true|false>]", parameter.getUsageString());
                });
            });

            runner.testGroup("getHelpLine()", () ->
            {
                runner.test("with no description or aliases", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success);
                    test.assertEqual("--fakeName: (No description provided)", parameter.getHelpLine());
                });

                runner.test("with empty description", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setDescription("");
                    test.assertEqual("--fakeName: (No description provided)", parameter.getHelpLine());
                });

                runner.test("with non-empty description", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setDescription("hello");
                    test.assertEqual("--fakeName: hello", parameter.getHelpLine());
                });

                runner.test("with empty aliases", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .setAliases(Iterable.create());
                    test.assertEqual("--fakeName: (No description provided)", parameter.getHelpLine());
                });

                runner.test("with one alias", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .addAlias("fn");
                    test.assertEqual("--fakeName(fn): (No description provided)", parameter.getHelpLine());
                });

                runner.test("with two aliases", (Test test) ->
                {
                    final CommandLineParameterList<String> parameter = new CommandLineParameterList<>("fakeName", null, Result::success)
                        .addAlias("fn")
                        .addAlias("faken");
                    test.assertEqual("--fakeName(fn,faken): (No description provided)", parameter.getHelpLine());
                });
            });
        });
    }
}
