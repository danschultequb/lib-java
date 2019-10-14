package qub;

public interface ProcessBuilderTests
{
    static BasicProcessBuilder createBuilder(Test test)
    {
        final ProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), Path.parse("/working/"));
        return new BasicProcessBuilder(factory, Path.parse("/files/executable.exe"), Path.parse("/working/"));
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(BasicProcessBuilder.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final BasicProcessBuilder builder = createBuilder(test);
                test.assertEqual(Path.parse("/files/executable.exe"), builder.getExecutablePath());
                test.assertEqual(Iterable.create(), builder.getArguments());
                test.assertEqual(Path.parse("/working/"), builder.getWorkingFolderPath());
            });

            runner.testGroup("addArgument()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicProcessBuilder builder = createBuilder(test);
                    test.assertThrows(() -> builder.addArgument(null),
                        new PreConditionFailure("argument cannot be null."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                });

                runner.test("with empty", (Test test) ->
                {
                    final BasicProcessBuilder builder = createBuilder(test);
                    test.assertThrows(() -> builder.addArgument(""),
                        new PreConditionFailure("argument cannot be empty."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final BasicProcessBuilder builder = createBuilder(test);
                    builder.addArgument("test");
                    test.assertEqual(Iterable.create("test"), builder.getArguments());
                });
            });

            runner.testGroup("addArguments()", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final BasicProcessBuilder builder = createBuilder(test);
                    builder.addArguments();
                    test.assertEqual(Iterable.create(), builder.getArguments());
                });

                runner.test("with one null value", (Test test) ->
                {
                    final BasicProcessBuilder builder = createBuilder(test);
                    test.assertThrows(() -> builder.addArguments((String)null),
                        new PreConditionFailure("argument cannot be null."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                });
            });
        });
    }
}
