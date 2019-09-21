package qub;

public interface ProcessRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ProcessRunner.class, () ->
        {
            runner.test("escapeArgument()", (Test test) ->
            {
                test.assertEqual("\"Then he said, \\\"Hey there!\\\"\"", ProcessRunner.escapeArgument("Then he said, \"Hey there!\""));
                test.assertEqual("-argument=\"value\"", ProcessRunner.escapeArgument("-argument=\"value\""));
                test.assertEqual("\"\"", ProcessRunner.escapeArgument(""));
            });
        });
    }

    static void test(TestRunner runner, Function1<Test,ProcessRunner> creator)
    {
        runner.testGroup(ProcessRunner.class, () ->
        {
            runner.testGroup("run()", () ->
            {
                runner.test("with null executableFile", (Test test) ->
                {
                    final Iterable<String> arguments = Iterable.create();
                    final Folder workingFolder = test.getProcess().getCurrentFolder().await();
                    final ProcessRunner processRunner = creator.run(test);
                    test.assertThrows(() -> processRunner.run(null, arguments, workingFolder, null, null, null),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with null arguments", (Test test) ->
                {
                    final Iterable<String> arguments = null;
                    final Folder workingFolder = test.getProcess().getCurrentFolder().await();
                    final File executableFile = workingFolder.getFile("spam").await();
                    final ProcessRunner processRunner = creator.run(test);
                    test.assertThrows(() -> processRunner.run(executableFile, arguments, workingFolder, null, null, null),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with null workingFolder", (Test test) ->
                {
                    final File executableFile = test.getProcess().getCurrentFolder().await().getFile("spam").await();
                    final Iterable<String> arguments = Iterable.create();
                    final ProcessRunner processRunner = creator.run(test);
                    test.assertThrows(() -> processRunner.run(executableFile, arguments, null, null, null, null),
                        new PreConditionFailure("workingFolder cannot be null."));
                });
            });
        });
    }
}
