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
                    test.assertEqual(Iterable.create(), console.getCommandLineArgumentStrings());
                });
                
                runner.test("with null String[]", (Test test) ->
                {
                    test.assertThrows(() -> new Console((String[])null), new PreConditionFailure("rawCommandLineArguments cannot be null."));
                });
                
                runner.test("with empty String[]", (Test test) ->
                {
                    final String[] commandLineArgumentStrings = new String[0];
                    final Console console = new Console(commandLineArgumentStrings);
                    test.assertEqual(Iterable.create(), console.getCommandLineArgumentStrings());
                    test.assertNotNull(console.getCommandLine());
                });
                
                runner.test("with non-empty String[]", (Test test) ->
                {
                    final String[] commandLineArgumentStrings = new String[] { "a", "b", "c" };
                    final Console console = new Console(commandLineArgumentStrings);
                    test.assertEqual(Iterable.create("a", "b", "c"), console.getCommandLineArgumentStrings());
                    test.assertNotNull(console.getCommandLine());
                });
            });

            runner.testGroup("writeByte(String,Object...)", () ->
            {
                final Action1<String> writeWithNoFormattedStringArgumentsFailureTest = (String toWrite) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and no formatted string arguments", (Test test) ->
                    {
                        final Console console = new Console();
                        final InMemoryLineStream output = new InMemoryLineStream();
                        console.setOutput(output);

                        test.assertThrows(() -> console.write(toWrite));

                        test.assertSuccess("", output.getText());
                    });
                };

                writeWithNoFormattedStringArgumentsFailureTest.run(null);
                writeWithNoFormattedStringArgumentsFailureTest.run("");

                final Action2<String,String> writeWithNoFormattedStringArgumentsTest = (String toWrite, String expectedText) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and no formatted string arguments", (Test test) ->
                    {
                        final Console console = new Console();
                        final InMemoryLineStream output = new InMemoryLineStream();
                        console.setOutput(output);

                        console.write(toWrite);

                        test.assertSuccess(expectedText, output.getText());
                    });
                };

                writeWithNoFormattedStringArgumentsTest.run("abcd", "abcd");
                writeWithNoFormattedStringArgumentsTest.run("Hello %s!", "Hello %s!");

                final Action2<String,Object[]> writeWithFormattedStringArgumentsFailureTest = (String toWrite, Object[] formattedStringArguments) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and " + (formattedStringArguments == null ? "null" : Array.create(formattedStringArguments).toString()) + " formatted string arguments", (Test test) ->
                    {
                        final Console console = new Console();
                        final InMemoryLineStream output = new InMemoryLineStream();
                        console.setOutput(output);

                        test.assertThrows(() -> console.write(toWrite, formattedStringArguments));

                        test.assertSuccess("", output.getText());
                    });
                };

                writeWithFormattedStringArgumentsFailureTest.run(null, null);
                writeWithFormattedStringArgumentsFailureTest.run(null, new Object[0]);
                writeWithFormattedStringArgumentsFailureTest.run(null, new Object[] { "Dan" });
                writeWithFormattedStringArgumentsFailureTest.run("", null);
                writeWithFormattedStringArgumentsFailureTest.run("", new Object[0]);
                writeWithFormattedStringArgumentsFailureTest.run("", new Object[] { "Dan" });

                final Action3<String,Object[],String> writeWithFormattedStringArgumentsTest = (String toWrite, Object[] formattedStringArguments, String expectedText) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and " + (formattedStringArguments == null ? "null" : Array.create(formattedStringArguments).toString()) + " formatted string arguments", (Test test) ->
                    {
                        final Console console = new Console();
                        final InMemoryLineStream output = new InMemoryLineStream();
                        console.setOutput(output);

                        console.write(toWrite, formattedStringArguments);

                        test.assertSuccess(expectedText, output.getText());
                    });
                };

                writeWithFormattedStringArgumentsTest.run("Hello!", null, "Hello!");
                writeWithFormattedStringArgumentsTest.run("Hello!", new Object[0], "Hello!");
                writeWithFormattedStringArgumentsTest.run("Hello!", new Object[] { "Dan" }, "Hello!");
                writeWithFormattedStringArgumentsTest.run("Hello, %s!", null, "Hello, %s!");
                writeWithFormattedStringArgumentsTest.run("Hello, %s!", new Object[0], "Hello, %s!");
                writeWithFormattedStringArgumentsTest.run("Hello, %s!", new Object[] { "Dan" }, "Hello, Dan!");
                writeWithFormattedStringArgumentsTest.run("Hello, %d!", null, "Hello, %d!");
                writeWithFormattedStringArgumentsTest.run("Hello, %d!", new Object[0], "Hello, %d!");

                final Action2<String,Object[]> writeWithWrongFormattedStringArgumentsTest = (String toWrite, Object[] formattedStringArguments) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and " + (formattedStringArguments == null ? "null" : Array.create(formattedStringArguments).toString()) + " formatted string arguments", (Test test) ->
                    {
                        final Console console = new Console();
                        final InMemoryLineStream output = new InMemoryLineStream();
                        console.setOutput(output);

                        test.assertThrows(() -> console.write(toWrite, formattedStringArguments),
                            new java.util.IllegalFormatConversionException('d', String.class));
                    });
                };

                writeWithWrongFormattedStringArgumentsTest.run("Hello, %d!", new Object[] { "Dan" });
            });
        });
    }
}
