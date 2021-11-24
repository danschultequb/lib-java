package qub;

public interface RealDesktopProcessTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RealDesktopProcess.class, () ->
        {
            DesktopProcessTests.test(runner, () -> RealDesktopProcessTests.ignoreDispose(RealDesktopProcess.create()));

            runner.testGroup("create(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    try (final RealDesktopProcess process = RealDesktopProcessTests.ignoreDispose(RealDesktopProcess.create()))
                    {
                        test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                    }
                });

                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> RealDesktopProcess.create((String[])null),
                        new PreConditionFailure("commandLineArgumentStrings cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final RealDesktopProcess process = RealDesktopProcessTests.ignoreDispose(RealDesktopProcess.create(new String[0])))
                    {
                        test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                    }
                });

                runner.test("with non-empty", (Test test) ->
                {
                    try (final RealDesktopProcess process = RealDesktopProcessTests.ignoreDispose(RealDesktopProcess.create("hello", "there")))
                    {
                        test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                    }
                });
            });

            runner.testGroup("create(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> RealDesktopProcess.create((Iterable<String>)null),
                        new PreConditionFailure("commandLineArgumentStrings cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final RealDesktopProcess process = RealDesktopProcessTests.ignoreDispose(RealDesktopProcess.create(Iterable.create())))
                    {
                        test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                    }
                });

                runner.test("with non-empty", (Test test) ->
                {
                    try (final RealDesktopProcess process = RealDesktopProcessTests.ignoreDispose(RealDesktopProcess.create(Iterable.create("hello", "there"))))
                    {
                        test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                    }
                });
            });

            runner.testGroup("create(CommandLineArguments)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> RealDesktopProcess.create((CommandLineArguments)null),
                        new PreConditionFailure("commandLineArguments cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final RealDesktopProcess process = RealDesktopProcessTests.ignoreDispose(RealDesktopProcess.create(CommandLineArguments.create())))
                    {
                        test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                    }
                });

                runner.test("with non-empty", (Test test) ->
                {
                    try (final RealDesktopProcess process = RealDesktopProcessTests.ignoreDispose(RealDesktopProcess.create(CommandLineArguments.create("hello", "there"))))
                    {
                        test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                    }
                });
            });
        });
    }

    private static RealDesktopProcess ignoreDispose(RealDesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        process.setOutputWriteStream(IgnoreDisposeCharacterToByteWriteStream.create(process.getOutputWriteStream()));
        process.setErrorWriteStream(IgnoreDisposeCharacterToByteWriteStream.create(process.getErrorWriteStream()));
        process.setInputReadStream(IgnoreDisposeCharacterToByteReadStream.create(process.getInputReadStream()));

        return process;
    }
}
