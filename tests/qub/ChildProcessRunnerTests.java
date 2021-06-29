package qub;

public interface ChildProcessRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ChildProcessRunner.class, () ->
        {
            runner.testGroup("getCommand()", () ->
            {
                runner.test("with null executablePath", (Test test) ->
                {
                    final Path executablePath = null;
                    final Iterable<String> arguments = Iterable.create("a", "b");
                    final Path workingFolderPath = Path.parse("/working/");
                    test.assertThrows(() -> ChildProcessRunner.getCommand(executablePath, arguments, workingFolderPath),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with null arguments", (Test test) ->
                {
                    final Path executablePath = Path.parse("/executable/file");
                    final Iterable<String> arguments = null;
                    final Path workingFolderPath = Path.parse("/working/");
                    test.assertThrows(() -> ChildProcessRunner.getCommand(executablePath, arguments, workingFolderPath),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with null workingFolderPath", (Test test) ->
                {
                    final Path executablePath = Path.parse("/executable/file");
                    final Iterable<String> arguments = Iterable.create("a", "b", "c");
                    final Path workingFolderPath = null;
                    test.assertEqual("/executable/file a b c",
                        ChildProcessRunner.getCommand(executablePath, arguments, workingFolderPath));
                });

                runner.test("with empty arguments and non-null workingFolderPath", (Test test) ->
                {
                    final Path executablePath = Path.parse("/executable/file");
                    final Iterable<String> arguments = Iterable.create();
                    final Path workingFolderPath = Path.parse("/working/");
                    test.assertEqual("/working/: /executable/file",
                        ChildProcessRunner.getCommand(executablePath, arguments, workingFolderPath));
                });

                runner.test("with non-empty arguments and non-null workingFolderPath", (Test test) ->
                {
                    final Path executablePath = Path.parse("/executable/file");
                    final Iterable<String> arguments = Iterable.create("apples", "bananas");
                    final Path workingFolderPath = Path.parse("/working/");
                    test.assertEqual("/working/: /executable/file apples bananas",
                        ChildProcessRunner.getCommand(executablePath, arguments, workingFolderPath));
                });
            });

            runner.testGroup("escapeArgument()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> ChildProcessRunner.escapeArgument(null),
                        new PreConditionFailure("argument cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> ChildProcessRunner.escapeArgument(""),
                        new PreConditionFailure("argument cannot be empty."));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("abc", ChildProcessRunner.escapeArgument("abc"));
                });

                runner.test("with non-empty that contains space", (Test test) ->
                {
                    test.assertEqual("\"a c\"", ChildProcessRunner.escapeArgument("a c"));
                });

                runner.test("with non-empty that contains multiple spaces", (Test test) ->
                {
                    test.assertEqual("\"a c e\"", ChildProcessRunner.escapeArgument("a c e"));
                });

                runner.test("with non-empty that contains tab", (Test test) ->
                {
                    test.assertEqual("\"a\tc\"", ChildProcessRunner.escapeArgument("a\tc"));
                });

                runner.test("with non-empty that contains newline", (Test test) ->
                {
                    test.assertEqual("\"a\nc\"", ChildProcessRunner.escapeArgument("a\nc"));
                });

                runner.test("with non-empty that contains carriage return", (Test test) ->
                {
                    test.assertEqual("\"a\rc\"", ChildProcessRunner.escapeArgument("a\rc"));
                });

                runner.test("with non-empty that contains single quote", (Test test) ->
                {
                    test.assertEqual("a\'c", ChildProcessRunner.escapeArgument("a\'c"));
                });

                runner.test("with non-empty that contains double quote", (Test test) ->
                {
                    test.assertEqual("\"a\\\"c\"", ChildProcessRunner.escapeArgument("a\"c"));
                });

                runner.test("with non-empty that contains multiple double quotes", (Test test) ->
                {
                    test.assertEqual("\"a\\\"\\\"c\"", ChildProcessRunner.escapeArgument("a\"\"c"));
                });

                runner.test("with non-empty that is missing closing double quote", (Test test) ->
                {
                    test.assertEqual("\"hello\\\"\"", ChildProcessRunner.escapeArgument("hello\""));
                });

                runner.test("with quoted empty", (Test test) ->
                {
                    test.assertEqual("\"\"", ChildProcessRunner.escapeArgument("\"\""));
                });

                runner.test("with quoted space", (Test test) ->
                {
                    test.assertEqual("\" \"", ChildProcessRunner.escapeArgument("\" \""));
                });

                runner.test("with quoted single quote", (Test test) ->
                {
                    test.assertEqual("\"\'\"", ChildProcessRunner.escapeArgument("\"\'\""));
                });

                runner.test("with quoted double quotes", (Test test) ->
                {
                    test.assertEqual("\"\\\"\\\"\"", ChildProcessRunner.escapeArgument("\"\"\"\""));
                });

                runner.test("with quoted tab", (Test test) ->
                {
                    test.assertEqual("\"\t\"", ChildProcessRunner.escapeArgument("\"\t\""));
                });

                runner.test("with quoted carriage return", (Test test) ->
                {
                    test.assertEqual("\"\r\"", ChildProcessRunner.escapeArgument("\"\r\""));
                });

                runner.test("with quoted newline", (Test test) ->
                {
                    test.assertEqual("\"\n\"", ChildProcessRunner.escapeArgument("\"\n\""));
                });
            });
        });
    }

    static void test(TestRunner runner, Function1<DesktopProcess, ChildProcessRunner> creator)
    {
        runner.testGroup(ChildProcessRunner.class, () ->
        {
            runner.testGroup("run(String,String...)", () ->
            {
                final Action2<String,Throwable> runErrorTest = (String executablePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath) + " and no arguments",
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final ChildProcessRunner processRunner = creator.run(process);
                        test.assertThrows(() -> processRunner.run(executablePath).await(), expected);
                    });
                };

                runErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));
                runErrorTest.run("", new PreConditionFailure("executablePath cannot be empty."));

                runner.test("with null arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.run("hello", (String[])null).await(),
                        new PreConditionFailure("arguments cannot be null."));
                });
            });

            runner.testGroup("run(String,Iterable<String>...)", () ->
            {
                final Action3<String,Iterable<String>,Throwable> runErrorTest = (String executablePath, Iterable<String> arguments, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(executablePath), arguments),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final ChildProcessRunner processRunner = creator.run(process);
                        test.assertThrows(() -> processRunner.run(executablePath, arguments).await(), expected);
                    });
                };

                runErrorTest.run(null, Iterable.create(), new PreConditionFailure("executablePath cannot be null."));
                runErrorTest.run("", Iterable.create(), new PreConditionFailure("executablePath cannot be empty."));
                runErrorTest.run("hello", null, new PreConditionFailure("arguments cannot be null."));
            });

            runner.testGroup("run(Path,String...)", () ->
            {
                runner.test("with null executablePath",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.run((Path)null),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with null arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.run(Path.parse("hello"), (String[])null).await(),
                        new PreConditionFailure("arguments cannot be null."));
                });
            });

            runner.testGroup("run(Path,Iterable<String>...)", () ->
            {
                final Action3<Path,Iterable<String>,Throwable> runErrorTest = (Path executablePath, Iterable<String> arguments, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath) + " and no arguments",
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final ChildProcessRunner processRunner = creator.run(process);
                        test.assertThrows(() -> processRunner.run(executablePath, arguments).await(), expected);
                    });
                };

                runErrorTest.run(null, Iterable.create(), new PreConditionFailure("executablePath cannot be null."));
                runErrorTest.run(Path.parse("hello"), null, new PreConditionFailure("arguments cannot be null."));
            });

            runner.testGroup("run(File,String...)", () ->
            {
                runner.test("with null executableFile",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.run((File)null),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with null arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    final File executableFile = process.getCurrentFolder().getFile("hello").await();
                    test.assertThrows(() -> processRunner.run(executableFile, (String[])null).await(),
                        new PreConditionFailure("arguments cannot be null."));
                });
            });

            runner.testGroup("run(File,Iterable<String>)", () ->
            {
                runner.test("with null executableFile",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.run((File)null, Iterable.create()),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with null arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    final File executableFile = process.getCurrentFolder().getFile("hello").await();
                    test.assertThrows(() -> processRunner.run(executableFile, (Iterable<String>)null).await(),
                        new PreConditionFailure("arguments cannot be null."));
                });
            });

            runner.testGroup("run(ExecutableParameters)", () ->
            {
                runner.test("with null parameters",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.run((ChildProcessParameters)null),
                        new PreConditionFailure("parameters cannot be null."));
                });
            });

            runner.testGroup("start(String,String...)", () ->
            {
                final Action2<String,Throwable> startErrorTest = (String executablePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final ChildProcessRunner processRunner = creator.run(process);
                        test.assertThrows(() -> processRunner.start(executablePath).await(), expected);
                    });
                };

                startErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));
                startErrorTest.run("", new PreConditionFailure("executablePath cannot be empty."));
                startErrorTest.run("bad/", new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run("bad\\", new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run("/bad/", new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run("\\bad\\", new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));

                runner.test("with null arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.start("hello", (String[])null).await(),
                        new PreConditionFailure("arguments cannot be null."));
                });
            });

            runner.testGroup("start(String,Iterable<String>)", () ->
            {
                final Action3<String,Iterable<String>,Throwable> startErrorTest = (String executablePath, Iterable<String> arguments, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(executablePath), arguments),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final ChildProcessRunner processRunner = creator.run(process);
                        test.assertThrows(() -> processRunner.start(executablePath, arguments).await(), expected);
                    });
                };

                startErrorTest.run(null, Iterable.create(), new PreConditionFailure("executablePath cannot be null."));
                startErrorTest.run("", Iterable.create(), new PreConditionFailure("executablePath cannot be empty."));
                startErrorTest.run("bad/", Iterable.create(), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run("bad\\", Iterable.create(), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run("/bad/", Iterable.create(), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run("\\bad\\", Iterable.create(), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run("hello", null, new PreConditionFailure("arguments cannot be null."));
            });

            runner.testGroup("start(Path,String...)", () ->
            {
                runner.test("with null executablePath",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.start((Path)null),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with null arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.start(Path.parse("hello"), (String[])null).await(),
                        new PreConditionFailure("arguments cannot be null."));
                });
            });

            runner.testGroup("start(Path,Iterable<String>)", () ->
            {
                final Action3<Path,Iterable<String>,Throwable> startErrorTest = (Path executablePath, Iterable<String> arguments, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(executablePath), arguments),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final ChildProcessRunner processRunner = creator.run(process);
                        test.assertThrows(() -> processRunner.start(executablePath, arguments).await(), expected);
                    });
                };

                startErrorTest.run(null, Iterable.create(), new PreConditionFailure("executablePath cannot be null."));
                startErrorTest.run(Path.parse("bad/"), Iterable.create(), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run(Path.parse("bad\\"), Iterable.create(), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run(Path.parse("/bad/"), Iterable.create(), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run(Path.parse("\\bad\\"), Iterable.create(), new PreConditionFailure("executablePath.endsWith('/') || executablePath.endsWith('\\') cannot be true."));
                startErrorTest.run(Path.parse("hello"), null, new PreConditionFailure("arguments cannot be null."));
            });

            runner.testGroup("start(File,String...)", () ->
            {
                runner.test("with null executableFile",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.start((File)null),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with null arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    final File executableFile = process.getCurrentFolder().getFile("hello").await();
                    test.assertThrows(() -> processRunner.start(executableFile, (String[])null).await(),
                        new PreConditionFailure("arguments cannot be null."));
                });
            });

            runner.testGroup("start(File,Iterable<String>)", () ->
            {
                runner.test("with null executableFile",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.start((File)null, Iterable.create()),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with null arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    final File executableFile = process.getCurrentFolder().getFile("hello").await();
                    test.assertThrows(() -> processRunner.start(executableFile, (Iterable<String>)null).await(),
                        new PreConditionFailure("arguments cannot be null."));
                });
            });

            runner.testGroup("start(ExecutableParameters)", () ->
            {
                runner.test("with null parameters",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner processRunner = creator.run(process);
                    test.assertThrows(() -> processRunner.run(null),
                        new PreConditionFailure("parameters cannot be null."));
                });
            });
        });
    }
}
