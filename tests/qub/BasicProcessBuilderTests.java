package qub;

public interface BasicProcessBuilderTests
{
    static BasicProcessBuilder createBuilder(FakeDesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");
        
        final InMemoryFileSystem fileSystem = process.getFileSystem();
        final Folder workingFolder = fileSystem.createFolder("/working/folder/").await();
        final ProcessFactory factory = FakeProcessFactory.create(process.getParallelAsyncRunner(), workingFolder)
            .add(FakeProcessRun.get("/files/executable.exe")
                .setFunction((ByteWriteStream output, ByteWriteStream error) ->
                {
                    CharacterWriteStream.create(output)
                        .writeLines(
                            "I'm output!",
                            "I'm more output!",
                            "I'm the last output.")
                        .await();
                    CharacterWriteStream.create(error)
                        .writeLines(
                            "I'm error!",
                            "I'm more error!",
                            "Still a little more error.",
                            "It's over?",
                            "")
                        .await();
                }));
        return new BasicProcessBuilder(factory, Path.parse("/files/executable.exe"), Path.parse("/working/"));
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(BasicProcessBuilder.class, () ->
        {
            runner.test("constructor()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                test.assertEqual(Path.parse("/files/executable.exe"), builder.getExecutablePath());
                test.assertEqual(Iterable.create(), builder.getArguments());
                test.assertEqual(Path.parse("/working/"), builder.getWorkingFolderPath());
                test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
            });

            runner.testGroup("addArgument(String)", () ->
            {
                runner.test("with null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    test.assertThrows(() -> builder.addArgument(null),
                        new PreConditionFailure("argument cannot be null."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with empty",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    test.assertThrows(() -> builder.addArgument(""),
                        new PreConditionFailure("argument cannot be empty."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with non-empty",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder addArgumentResult = builder.addArgument("test");
                    test.assertSame(builder, addArgumentResult);
                    test.assertEqual(Iterable.create("test"), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe test", builder.getCommand());
                });
            });

            runner.testGroup("addArguments(String...)", () ->
            {
                runner.test("with no arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder addArgumentsResult = builder.addArguments();
                    test.assertSame(builder, addArgumentsResult);
                    test.assertEqual(Iterable.create(), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with one null value",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    test.assertThrows(() -> builder.addArguments((String)null),
                        new PreConditionFailure("argument cannot be null."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with one empty value",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    test.assertThrows(() -> builder.addArguments(""),
                        new PreConditionFailure("argument cannot be empty."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with one non-empty value",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder addArgumentsResult = builder.addArguments("test");
                    test.assertSame(builder, addArgumentsResult);
                    test.assertEqual(Iterable.create("test"), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe test", builder.getCommand());
                });

                runner.test("with multiple non-empty value",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder addArgumentsResult = builder.addArguments("test", "ing", "stuff");
                    test.assertSame(builder, addArgumentsResult);
                    test.assertEqual(Iterable.create("test", "ing", "stuff"), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe test ing stuff", builder.getCommand());
                });
            });

            runner.testGroup("addArguments(Iterable<String>)", () ->
            {
                runner.test("with null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    test.assertThrows(() -> builder.addArguments((Iterable<String>)null),
                        new PreConditionFailure("arguments cannot be null."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with no arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder addArgumentsResult = builder.addArguments(Iterable.create());
                    test.assertSame(builder, addArgumentsResult);
                    test.assertEqual(Iterable.create(), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with one null value",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    test.assertThrows(() -> builder.addArguments(Iterable.create((String)null)),
                        new PreConditionFailure("argument cannot be null."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with one empty value",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    test.assertThrows(() -> builder.addArguments(Iterable.create("")),
                        new PreConditionFailure("argument cannot be empty."));
                    test.assertEqual(Iterable.create(), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with one non-empty value",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder addArgumentsResult = builder.addArguments(Iterable.create("test"));
                    test.assertSame(builder, addArgumentsResult);
                    test.assertEqual(Iterable.create("test"), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe test", builder.getCommand());
                });

                runner.test("with multiple non-empty value",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder addArgumentsResult = builder.addArguments(Iterable.create("test", "ing", "stuff"));
                    test.assertSame(builder, addArgumentsResult);
                    test.assertEqual(Iterable.create("test", "ing", "stuff"), builder.getArguments());
                    test.assertEqual("/working/: /files/executable.exe test ing stuff", builder.getCommand());
                });
            });

            runner.testGroup("setWorkingFolder(String)", () ->
            {
                runner.test("with null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final Path workingFolderPath = builder.getWorkingFolderPath();
                    test.assertThrows(() -> builder.setWorkingFolder((String)null),
                        new PreConditionFailure("workingFolderPath cannot be null."));
                    test.assertEqual(workingFolderPath, builder.getWorkingFolderPath());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with empty",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final Path workingFolderPath = builder.getWorkingFolderPath();
                    test.assertThrows(() -> builder.setWorkingFolder(""),
                        new PreConditionFailure("workingFolderPath cannot be empty."));
                    test.assertEqual(workingFolderPath, builder.getWorkingFolderPath());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with relative",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final Path workingFolderPath = builder.getWorkingFolderPath();
                    test.assertThrows(() -> builder.setWorkingFolder("hello"),
                        new PreConditionFailure("workingFolderPath.isRooted() cannot be false."));
                    test.assertEqual(workingFolderPath, builder.getWorkingFolderPath());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with rooted",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder setWorkingFolderResult = builder.setWorkingFolder("/hello");
                    test.assertSame(builder, setWorkingFolderResult);
                    test.assertEqual(Path.parse("/hello"), builder.getWorkingFolderPath());
                    test.assertEqual("/hello: /files/executable.exe", builder.getCommand());
                });
            });

            runner.testGroup("setWorkingFolder(Path)", () ->
            {
                runner.test("with null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final Path workingFolderPath = builder.getWorkingFolderPath();
                    test.assertThrows(() -> builder.setWorkingFolder((Path)null),
                        new PreConditionFailure("workingFolderPath cannot be null."));
                    test.assertEqual(workingFolderPath, builder.getWorkingFolderPath());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with relative",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final Path workingFolderPath = builder.getWorkingFolderPath();
                    test.assertThrows(() -> builder.setWorkingFolder(Path.parse("hello")),
                        new PreConditionFailure("workingFolderPath.isRooted() cannot be false."));
                    test.assertEqual(workingFolderPath, builder.getWorkingFolderPath());
                    test.assertEqual("/working/: /files/executable.exe", builder.getCommand());
                });

                runner.test("with rooted",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder setWorkingFolderResult = builder.setWorkingFolder(Path.parse("/hello"));
                    test.assertSame(builder, setWorkingFolderResult);
                    test.assertEqual(Path.parse("/hello"), builder.getWorkingFolderPath());
                    test.assertEqual("/hello: /files/executable.exe", builder.getCommand());
                });
            });

            runner.testGroup("redirectOutputLines(Action1<String>)", () ->
            {
                runner.test("with null action",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    test.assertThrows(() -> builder.redirectOutputLines(null),
                        new PreConditionFailure("onOutputLine cannot be null."));
                    test.assertEqual(0, builder.run().await());
                });

                runner.test("with empty action",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder redirectOutputLinesResult = builder.redirectOutputLines((String outputLine) -> {});
                    test.assertSame(builder, redirectOutputLinesResult);
                    test.assertEqual(0, builder.run().await());
                });

                runner.test("with non-empty action",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final List<String> outputLines = List.create();
                    final BasicProcessBuilder redirectOutputLinesResult = builder.redirectOutputLines(outputLines::add);
                    test.assertSame(builder, redirectOutputLinesResult);
                    test.assertEqual(0, builder.run().await());
                    test.assertEqual(
                        Iterable.create(
                            "I'm output!\n",
                            "I'm more output!\n",
                            "I'm the last output.\n"
                        ),
                        outputLines);
                });
            });

            runner.testGroup("redirectErrorLines(Action1<String>)", () ->
            {
                runner.test("with null action",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    test.assertThrows(() -> builder.redirectErrorLines(null),
                        new PreConditionFailure("onErrorLine cannot be null."));
                    test.assertEqual(0, builder.run().await());
                });

                runner.test("with empty action",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final BasicProcessBuilder redirectErrorLinesResult = builder.redirectErrorLines((String outputLine) -> {});
                    test.assertSame(builder, redirectErrorLinesResult);
                    test.assertEqual(0, builder.run().await());
                });

                runner.test("with non-empty action",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final BasicProcessBuilder builder = BasicProcessBuilderTests.createBuilder(process);
                    final List<String> outputLines = List.create();
                    final BasicProcessBuilder redirectErrorLinesResult = builder.redirectErrorLines(outputLines::add);
                    test.assertSame(builder, redirectErrorLinesResult);
                    test.assertEqual(0, builder.run().await());
                    test.assertEqual(
                        Iterable.create(
                            "I'm error!\n",
                            "I'm more error!\n",
                            "Still a little more error.\n",
                            "It's over?\n",
                            "\n"),
                        outputLines);
                });
            });
        });
    }
}
