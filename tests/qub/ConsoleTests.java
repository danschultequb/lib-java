package qub;

public class ConsoleTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Console", () ->
        {
            ProcessTests.test(runner, Console::new);

            runner.testGroup("constructor()", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final Console console = new Console();
                    test.assertEqual(new Array<String>(0), console.getCommandLineArgumentStrings());
                });
                
                runner.test("with null String[]", (Test test) ->
                {
                    final Console console = new Console((String[])null);
                    test.assertEqual(new Array<String>(0), console.getCommandLineArgumentStrings());
                    test.assertNotNull(console.getCommandLine());
                });
                
                runner.test("with empty String[]", (Test test) ->
                {
                    final String[] commandLineArgumentStrings = new String[0];
                    final Console console = new Console(commandLineArgumentStrings);
                    test.assertEqual(new Array<String>(0), console.getCommandLineArgumentStrings());
                    test.assertNotNull(console.getCommandLine());
                });
                
                runner.test("with non-empty String[]", (Test test) ->
                {
                    final String[] commandLineArgumentStrings = Array.toStringArray("a", "b", "c");
                    final Console console = new Console(commandLineArgumentStrings);
                    test.assertEqual(Array.fromValues(new String[] { "a", "b", "c" }), console.getCommandLineArgumentStrings());
                    test.assertNotNull(console.getCommandLine());
                });
            });
        });
    }
}
