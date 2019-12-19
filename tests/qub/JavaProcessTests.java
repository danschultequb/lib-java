package qub;

public interface JavaProcessTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaProcess.class, () ->
        {
            IConsoleTests.test(runner, JavaProcess::create);

            runner.testGroup("create(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final JavaProcess process = JavaProcess.create();
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaProcess.create((String[])null),
                        new PreConditionFailure("commandLineArgumentStrings cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final JavaProcess process = JavaProcess.create(new String[0]);
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JavaProcess process = JavaProcess.create("hello", "there");
                    test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                });
            });

            runner.testGroup("create(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaProcess.create((Iterable<String>)null),
                        new PreConditionFailure("commandLineArgumentStrings cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final JavaProcess process = JavaProcess.create(Iterable.create());
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JavaProcess process = JavaProcess.create(Iterable.create("hello", "there"));
                    test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                });
            });

            runner.testGroup("create(CommandLineArguments)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JavaProcess.create((CommandLineArguments)null),
                        new PreConditionFailure("commandLineArguments cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final JavaProcess process = JavaProcess.create(CommandLineArguments.create());
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JavaProcess process = JavaProcess.create(CommandLineArguments.create("hello", "there"));
                    test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                });
            });
        });
    }
}
