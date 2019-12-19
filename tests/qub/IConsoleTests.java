package qub;

public interface IConsoleTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IConsole.class, () ->
        {
            runner.testGroup("create(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final IProcess process = IProcess.create();
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> IProcess.create((String[])null),
                        new PreConditionFailure("commandLineArgumentStrings cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final IProcess process = IProcess.create(new String[0]);
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final IProcess process = IProcess.create("hello", "there");
                    test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                });
            });

            runner.testGroup("create(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> IProcess.create((Iterable<String>)null),
                        new PreConditionFailure("commandLineArgumentStrings cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final IProcess process = IProcess.create(Iterable.create());
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final IProcess process = IProcess.create(Iterable.create("hello", "there"));
                    test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                });
            });

            runner.testGroup("create(CommandLineArguments)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> IProcess.create((CommandLineArguments)null),
                        new PreConditionFailure("commandLineArguments cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final IProcess process = IProcess.create(CommandLineArguments.create());
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final IProcess process = IProcess.create(CommandLineArguments.create("hello", "there"));
                    test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                });
            });
        });
    }
    
    static void test(TestRunner runner, Function0<? extends IConsole> creator)
    {
        runner.testGroup(IConsole.class, () ->
        {
            IProcessTests.test(runner, creator);

            runner.testGroup("writeByte(String,Object...)", () ->
            {
                final Action2<String,Throwable> writeWithNoFormattedStringArgumentsFailureTest = (String toWrite, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(toWrite) + " and no formatted string arguments", (Test test) ->
                    {
                        final IConsole console = creator.run();
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
                        final IConsole console = creator.run();
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
                        final IConsole console = creator.run();
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
                        final IConsole console = creator.run();
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
                        final IConsole console = creator.run();
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
