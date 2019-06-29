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
                    test.assertEqual(Iterable.create(), console.getCommandLineArguments());
                });
                
                runner.test("with null CommandLineArguments", (Test test) ->
                {
                    test.assertThrows(() -> new Console((CommandLineArguments)null),
                        new PreConditionFailure("commandLineArguments cannot be null."));
                });
                
                runner.test("with empty CommandLineArguments", (Test test) ->
                {
                    final String[] commandLineArgumentStrings = new String[0];
                    final Console console = new Console(CommandLineArguments.create());
                    test.assertEqual(Iterable.create(), console.getCommandLineArguments());
                });
                
                runner.test("with non-empty CommandLineArguments", (Test test) ->
                {
                    final Console console = new Console(CommandLineArguments.create("a", "b", "c"));
                    test.assertEqual("[a,b,c]", console.getCommandLineArguments().toString());
                });
            });

            runner.testGroup("writeByte(String,Object...)", () ->
            {
                final Action2<String,Throwable> writeWithNoFormattedStringArgumentsFailureTest = (String toWrite, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and no formatted string arguments", (Test test) ->
                    {
                        final Console console = new Console();
                        final InMemoryCharacterStream output = new InMemoryCharacterStream();
                        console.setOutputCharacterWriteStream(output);

                        test.assertThrows(() -> console.write(toWrite), expectedError);

                        test.assertEqual("", output.getText().await());
                    });
                };

                writeWithNoFormattedStringArgumentsFailureTest.run(null, new PreConditionFailure("toWrite cannot be null."));
                writeWithNoFormattedStringArgumentsFailureTest.run("", new PreConditionFailure("text cannot be empty."));

                final Action1<String> writeWithNoFormattedStringArgumentsTest = (String toWrite) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and no formatted string arguments", (Test test) ->
                    {
                        final Console console = new Console();
                        final InMemoryCharacterStream output = new InMemoryCharacterStream();
                        console.setOutputCharacterWriteStream(output);

                        console.write(toWrite).await();

                        test.assertEqual(toWrite, output.getText().await());
                    });
                };

                writeWithNoFormattedStringArgumentsTest.run("abcd");
                writeWithNoFormattedStringArgumentsTest.run("Hello %s!");

                final Action3<String,Object[],Throwable> writeWithFormattedStringArgumentsFailureTest = (String toWrite, Object[] formattedStringArguments, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and " + (formattedStringArguments == null ? "null" : Array.create(formattedStringArguments).toString()) + " formatted string arguments", (Test test) ->
                    {
                        final Console console = new Console();
                        final InMemoryCharacterStream output = new InMemoryCharacterStream();
                        console.setOutputCharacterWriteStream(output);

                        test.assertThrows(() -> console.write(toWrite, formattedStringArguments), expectedError);

                        test.assertEqual("", output.getText().await());
                    });
                };

                writeWithFormattedStringArgumentsFailureTest.run(null, null, new PreConditionFailure("toWrite cannot be null."));
                writeWithFormattedStringArgumentsFailureTest.run(null, new Object[0], new PreConditionFailure("toWrite cannot be null."));
                writeWithFormattedStringArgumentsFailureTest.run(null, new Object[] { "Dan" }, new PreConditionFailure("toWrite cannot be null."));
                writeWithFormattedStringArgumentsFailureTest.run("", null, new PreConditionFailure("text cannot be empty."));
                writeWithFormattedStringArgumentsFailureTest.run("", new Object[0], new PreConditionFailure("text cannot be empty."));
                writeWithFormattedStringArgumentsFailureTest.run("", new Object[] { "Dan" }, new PreConditionFailure("text cannot be empty."));

                final Action3<String,Object[],String> writeWithFormattedStringArgumentsTest = (String toWrite, Object[] formattedStringArguments, String expectedText) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and " + (formattedStringArguments == null ? "null" : Array.create(formattedStringArguments).toString()) + " formatted string arguments", (Test test) ->
                    {
                        final Console console = new Console();
                        final InMemoryCharacterStream output = new InMemoryCharacterStream();
                        console.setOutputCharacterWriteStream(output);

                        console.write(toWrite, formattedStringArguments);

                        test.assertEqual(expectedText, output.getText().await());
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
                        final InMemoryCharacterStream output = new InMemoryCharacterStream();
                        console.setOutputCharacterWriteStream(output);

                        test.assertThrows(() -> console.write(toWrite, formattedStringArguments),
                            new java.util.IllegalFormatConversionException('d', String.class));
                    });
                };

                writeWithWrongFormattedStringArgumentsTest.run("Hello, %d!", new Object[] { "Dan" });
            });
        });
    }
}
