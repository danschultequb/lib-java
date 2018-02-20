package qub;

public class CommandLineTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("CommandLine", () ->
        {
            runner.testGroup("parse()", () ->
            {
                runner.test("with null String[]", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(null);
                    test.assertEqual(new Array<String>(0), commandLine.getArgumentStrings());
                    test.assertFalse(commandLine.any());
                    test.assertEqual(0, commandLine.getCount());
                    test.assertNull(commandLine.get(0));
                });
                
                runner.test("with empty String[]", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[0]);
                    test.assertEqual(new Array<String>(0), commandLine.getArgumentStrings());
                    test.assertFalse(commandLine.any());
                    test.assertEqual(0, commandLine.getCount());
                    test.assertNull(commandLine.get(0));
                });

                runner.test("with String argument list", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "a", "b", "c", "d" });
                    test.assertEqual(Array.fromValues(new String[] { "a", "b", "c", "d" }), commandLine.getArgumentStrings());
                    test.assertTrue(commandLine.any());
                    test.assertEqual(4, commandLine.getCount());
                    test.assertNotNull(commandLine.get(0));
                });
            });

            runner.testGroup("getValue()", () ->
            {
                runner.test("with null arguments and null name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(null);
                    test.assertNull(commandLine.getValue(null));
                });

                runner.test("with null arguments and empty name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(null);
                    test.assertNull(commandLine.getValue(""));
                });

                runner.test("with null arguments and non-empty name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(null);
                    test.assertNull(commandLine.getValue("spud"));
                });

                runner.test("with empty arguments and null name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[0]);
                    test.assertNull(commandLine.getValue(null));
                });

                runner.test("with empty arguments and empty name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[0]);
                    test.assertNull(commandLine.getValue(""));
                });

                runner.test("with empty arguments and non-empty name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[0]);
                    test.assertNull(commandLine.getValue("spud"));
                });

                runner.test("with non-empty arguments and null name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "hello", "there" });
                    test.assertNull(commandLine.getValue(null));
                });

                runner.test("with non-empty arguments and empty name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "hello", "there" });
                    test.assertNull(commandLine.getValue(""));
                });

                runner.test("with non-empty arguments and non-matching name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "hello", "there" });
                    test.assertNull(commandLine.getValue("spud"));
                });

                runner.test("with single-dash arguments and null name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "-hello", "-there" });
                    test.assertNull(commandLine.getValue(null));
                });

                runner.test("with single-dash arguments and empty name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "-hello", "-there" });
                    test.assertNull(commandLine.getValue(""));
                });

                runner.test("with single-dash arguments and non-matching name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "-hello", "-there" });
                    test.assertNull(commandLine.getValue("spud"));
                });

                runner.test("with single-dash arguments and matching name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "-hello", "there" });
                    final CommandLineArgument argument = commandLine.get("hello");
                    test.assertNotNull(argument);
                    test.assertEqual("-hello", argument.toString());
                    test.assertEqual("hello", argument.getName());
                    test.assertNull(argument.getValue());
                });

                runner.test("with single-dash and equals sign argument and null name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "-name=value" });
                    test.assertNull(commandLine.getValue(null));
                });

                runner.test("with single-dash and equals sign argument and empty name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "-name=value" });
                    test.assertNull(commandLine.getValue(""));
                });

                runner.test("with single-dash and equals sign argument and non-matching name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "-name=value" });
                    test.assertNull(commandLine.getValue("spud"));
                });

                runner.test("with single-dash and equals sign argument and matching name", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "-name=value" });
                    test.assertEqual("value", commandLine.getValue("name"));
                });
            });
            
            runner.testGroup("removeAt()", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "a", "b", "c" });
                    test.assertEqual(null, commandLine.removeAt(-1));
                    test.assertEqual(Array.fromValues(new String[] { "a", "b", "c" }), commandLine.getArgumentStrings());
                });

                runner.test("with zero index with empty command line", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[0]);
                    test.assertEqual(null, commandLine.removeAt(0));
                    test.assertEqual(new Array<String>(0), commandLine.getArgumentStrings());
                });

                runner.test("with zero index with non-empty command line", (Test test) ->
                {
                    final CommandLine commandLine = CommandLine.parse(new String[] { "a", "b", "c" });
                    test.assertEqual(new CommandLineArgument("a"), commandLine.removeAt(0));
                    test.assertEqual(CommandLine.parse(new String[] { "b", "c" }), commandLine);
                });
            });
        });
    }
}
