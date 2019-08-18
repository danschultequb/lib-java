package qub;

public interface CommandLineArgumentTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CommandLineArgument.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                 runner.test("with null name and null value", (Test test) ->
                 {
                     test.assertThrows(() -> new CommandLineArgument(null, null),
                         new PreConditionFailure("!Strings.isNullOrEmpty(name) || !Strings.isNullOrEmpty(value) cannot be false."));
                 });

                 runner.test("with null name and empty value", (Test test) ->
                 {
                     test.assertThrows(() -> new CommandLineArgument(null, ""),
                         new PreConditionFailure("!Strings.isNullOrEmpty(name) || !Strings.isNullOrEmpty(value) cannot be false."));
                 });

                 runner.test("with null name and non-empty value", (Test test) ->
                 {
                     final CommandLineArgument argument = new CommandLineArgument(null, "abc");
                     test.assertNull(argument.getName());
                     test.assertEqual("abc", argument.getValue());
                 });

                 runner.test("with empty name and null value", (Test test) ->
                 {
                     test.assertThrows(() -> new CommandLineArgument("", null),
                         new PreConditionFailure("!Strings.isNullOrEmpty(name) || !Strings.isNullOrEmpty(value) cannot be false."));
                 });

                 runner.test("with empty name and empty value", (Test test) ->
                 {
                     test.assertThrows(() -> new CommandLineArgument("", ""),
                         new PreConditionFailure("!Strings.isNullOrEmpty(name) || !Strings.isNullOrEmpty(value) cannot be false."));
                 });

                 runner.test("with empty name and non-empty value", (Test test) ->
                 {
                     final CommandLineArgument argument = new CommandLineArgument("", "abc");
                     test.assertEqual("", argument.getName());
                     test.assertEqual("abc", argument.getValue());
                 });

                 runner.test("with non-empty name and null value", (Test test) ->
                 {
                     final CommandLineArgument argument = new CommandLineArgument("xyz", null);
                     test.assertEqual("xyz", argument.getName());
                     test.assertEqual(null, argument.getValue());
                 });

                 runner.test("with non-empty name and empty value", (Test test) ->
                 {
                     final CommandLineArgument argument = new CommandLineArgument("xyz", "");
                     test.assertEqual("xyz", argument.getName());
                     test.assertEqual("", argument.getValue());
                 });

                 runner.test("with non-empty name and non-empty value", (Test test) ->
                 {
                     final CommandLineArgument argument = new CommandLineArgument("xyz", "abc");
                     test.assertEqual("xyz", argument.getName());
                     test.assertEqual("abc", argument.getValue());
                 });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with null name and non-empty value", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument(null, "b");
                    test.assertEqual("b", argument.toString());
                });

                runner.test("with empty name and non-empty value", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("", "b");
                    test.assertEqual("b", argument.toString());
                });

                runner.test("with non-empty name and null value", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", null);
                    test.assertEqual("--a", argument.toString());
                });

                runner.test("with non-empty name and empty value", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "");
                    test.assertEqual("--a", argument.toString());
                });

                runner.test("with non-empty name and non-empty value", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "b");
                    test.assertEqual("--a=b", argument.toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "b");
                    test.assertFalse(argument.equals((Object)null));
                });

                runner.test("with non-CommandLineArgument", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "b");
                    test.assertFalse(argument.equals((Object)"b"));
                });

                runner.test("with null lhs name", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument(null, "b");
                    test.assertFalse(argument.equals((Object)new CommandLineArgument("c", "b")));
                });

                runner.test("with null rhs name", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "b");
                    test.assertFalse(argument.equals((Object)new CommandLineArgument(null, "b")));
                });

                runner.test("with different name", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "b");
                    test.assertFalse(argument.equals((Object)new CommandLineArgument("c", "b")));
                });

                runner.test("with null lhs value", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", null);
                    test.assertFalse(argument.equals((Object)new CommandLineArgument("a", "c")));
                });

                runner.test("with null rhs value", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "b");
                    test.assertFalse(argument.equals((Object)new CommandLineArgument("a", null)));
                });

                runner.test("with different value", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "b");
                    test.assertFalse(argument.equals((Object)new CommandLineArgument("a", "c")));
                });

                runner.test("with equal name and value", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "b");
                    test.assertTrue(argument.equals((Object)new CommandLineArgument("a", "b")));
                });
            });

            runner.testGroup("equals(CommandLineArgument)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineArgument argument = new CommandLineArgument("a", "b");
                    test.assertFalse(argument.equals((CommandLineArgument)null));
                });
            });
        });
    }
}
