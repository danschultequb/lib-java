package qub;

public interface FakeChildProcessRunTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeChildProcessRun.class, () ->
        {
            runner.testGroup("create(String,String...)", () ->
            {
                runner.test("with null executablePath", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create((String)null),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with empty executablePath", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create(""),
                        new PreConditionFailure("executablePath cannot be empty."));
                });

                runner.test("with null arguments array", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create("fake.exe", (String[])null),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with relative executablePath", (Test test) ->
                {
                    final FakeChildProcessRun run = FakeChildProcessRun.create("fake.exe");
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create(), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });

                runner.test("with arguments", (Test test) ->
                {
                    final FakeChildProcessRun run = FakeChildProcessRun.create("fake.exe", "hello", "there");
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create("hello", "there"), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });
            });

            runner.testGroup("create(String,Iterable<String>)", () ->
            {
                runner.test("with null executablePath", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create((String)null, Iterable.create()),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with empty executablePath", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create("", Iterable.create()),
                        new PreConditionFailure("executablePath cannot be empty."));
                });

                runner.test("with null arguments Iterable", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create("fake.exe", (Iterable<String>)null),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with relative executablePath", (Test test) ->
                {
                    final FakeChildProcessRun run = FakeChildProcessRun.create("fake.exe", Iterable.create());
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create(), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });

                runner.test("with arguments", (Test test) ->
                {
                    final FakeChildProcessRun run = FakeChildProcessRun.create("fake.exe", Iterable.create("hello", "there"));
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create("hello", "there"), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });
            });

            runner.testGroup("create(Path,String...)", () ->
            {
                runner.test("with null executablePath", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create((Path)null),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with null arguments array", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create(Path.parse("fake.exe"), (String[])null),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with relative executablePath", (Test test) ->
                {
                    final FakeChildProcessRun run = FakeChildProcessRun.create(Path.parse("fake.exe"));
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create(), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });

                runner.test("with arguments", (Test test) ->
                {
                    final FakeChildProcessRun run = FakeChildProcessRun.create(Path.parse("fake.exe"), "hello", "there");
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create("hello", "there"), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });
            });

            runner.testGroup("create(Path,Iterable<String>)", () ->
            {
                runner.test("with null executablePath", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create((Path)null, Iterable.create()),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with null arguments Iterable", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create(Path.parse("fake.exe"), (Iterable<String>)null),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with relative executablePath", (Test test) ->
                {
                    final FakeChildProcessRun run = FakeChildProcessRun.create(Path.parse("fake.exe"), Iterable.create());
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create(), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });

                runner.test("with arguments", (Test test) ->
                {
                    final FakeChildProcessRun run = FakeChildProcessRun.create(Path.parse("fake.exe"), Iterable.create("hello", "there"));
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create("hello", "there"), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });
            });

            runner.testGroup("create(File,String...)", () ->
            {
                runner.test("with null executableFile", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create((File)null),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with null arguments array", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/fake.exe").await();
                    test.assertThrows(() -> FakeChildProcessRun.create(file, (String[])null),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with relative executablePath", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/fake.exe").await();
                    final FakeChildProcessRun run = FakeChildProcessRun.create(file);
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("/fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create(), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });

                runner.test("with arguments", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/fake.exe").await();
                    final FakeChildProcessRun run = FakeChildProcessRun.create(file, "hello", "there");
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("/fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create("hello", "there"), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });
            });

            runner.testGroup("create(File,Iterable<String>)", () ->
            {
                runner.test("with null executablePath", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRun.create((File)null, Iterable.create()),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with null arguments Iterable", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/fake.exe").await();
                    test.assertThrows(() -> FakeChildProcessRun.create(file, (Iterable<String>)null),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with relative executablePath", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/fake.exe").await();
                    final FakeChildProcessRun run = FakeChildProcessRun.create(file, Iterable.create());
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("/fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create(), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });

                runner.test("with arguments", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/fake.exe").await();
                    final FakeChildProcessRun run = FakeChildProcessRun.create(file, Iterable.create("hello", "there"));
                    test.assertNotNull(run);
                    test.assertEqual(Path.parse("/fake.exe"), run.getExecutablePath());
                    test.assertEqual(Iterable.create("hello", "there"), run.getArguments());
                    test.assertNull(run.getWorkingFolderPath());
                    test.assertNull(run.getAction());
                });
            });
        });
    }

    static void test(TestRunner runner, Function1<String, FakeChildProcessRun> creator)
    {
        runner.testGroup(FakeChildProcessRun.class, () ->
        {
            runner.testGroup("addArgument(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.addArgument(null),
                        new PreConditionFailure("argument cannot be null."));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with empty", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.addArgument(""),
                        new PreConditionFailure("argument cannot be empty."));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArgument("apples"));
                    test.assertEqual(Iterable.create("apples"), fakeProcessRun.getArguments());
                });

                runner.test("with non-empty with spaces", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArgument("ap pl es"));
                    test.assertEqual(Iterable.create("ap pl es"), fakeProcessRun.getArguments());
                });
            });

            runner.testGroup("addArguments(String...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.addArguments((String[])null),
                        new PreConditionFailure("arguments cannot be null."));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(new String[0]));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with one-element array", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(new String[] { "a" }));
                    test.assertEqual(Iterable.create("a"), fakeProcessRun.getArguments());
                });

                runner.test("with two-element array", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(new String[] { "a", "b" }));
                    test.assertEqual(Iterable.create("a", "b"), fakeProcessRun.getArguments());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with one value", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments("a"));
                    test.assertEqual(Iterable.create("a"), fakeProcessRun.getArguments());
                });

                runner.test("with two values", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments("a", "b"));
                    test.assertEqual(Iterable.create("a", "b"), fakeProcessRun.getArguments());
                });
            });

            runner.testGroup("addArguments(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.addArguments((Iterable<String>)null),
                        new PreConditionFailure("arguments cannot be null."));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with empty", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(Iterable.create()));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with one value", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(Iterable.create("a")));
                    test.assertEqual(Iterable.create("a"), fakeProcessRun.getArguments());
                });

                runner.test("with two values", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(Iterable.create("a", "b")));
                    test.assertEqual(Iterable.create("a", "b"), fakeProcessRun.getArguments());
                });
            });

            runner.testGroup("setWorkingFolder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder((String)null));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with empty", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setWorkingFolder(""),
                        new PreConditionFailure("workingFolderPath (\"\") must be either null or not empty."));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setWorkingFolder("folder"),
                        new PreConditionFailure("workingFolderPath == null || workingFolderPath.isRooted() cannot be false."));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder("/folder"));
                    test.assertEqual(Path.parse("/folder"), fakeProcessRun.getWorkingFolderPath());
                });
            });

            runner.testGroup("setWorkingFolder(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder((Path)null));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setWorkingFolder(Path.parse("folder")),
                        new PreConditionFailure("workingFolderPath == null || workingFolderPath.isRooted() cannot be false."));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder(Path.parse("/folder")));
                    test.assertEqual(Path.parse("/folder"), fakeProcessRun.getWorkingFolderPath());
                });
            });

            runner.testGroup("setWorkingFolder(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder((Folder)null));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Folder folder = fileSystem.getFolder("/folder").await();
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder(folder));
                    test.assertEqual(Path.parse("/folder/"), fakeProcessRun.getWorkingFolderPath());
                });
            });

            runner.testGroup("setAction(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setAction((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                    test.assertNull(fakeProcessRun.getAction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    final IntegerValue value = new IntegerValue(5);
                    test.assertSame(fakeProcessRun, fakeProcessRun.setAction(() -> { value.set(20); }));
                    test.assertNotNull(fakeProcessRun.getAction());
                    try (final FakeDesktopProcess childProcess = FakeDesktopProcess.create())
                    {
                        fakeProcessRun.getAction().run(childProcess);
                        test.assertEqual(new byte[0], childProcess.getOutputWriteStream().getBytes());
                        test.assertEqual(new byte[0], childProcess.getErrorWriteStream().getBytes());
                        test.assertEqual(20, value.get());
                        test.assertEqual(0, childProcess.getExitCode());
                    }
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setAction(() -> { throw new ParseException("blah"); }));
                    test.assertNotNull(fakeProcessRun.getAction());
                    try (final FakeDesktopProcess childProcess = FakeDesktopProcess.create())
                    {
                        test.assertThrows(() -> fakeProcessRun.getAction().run(childProcess),
                            new ParseException("blah"));
                        test.assertEqual(new byte[0], childProcess.getOutputWriteStream().getBytes());
                        test.assertEqual(new byte[0], childProcess.getErrorWriteStream().getBytes());
                        test.assertEqual(0, childProcess.getExitCode());
                    }
                });
            });

            runner.testGroup("setAction(Action1<FakeDesktopProcess>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setAction((Action1<FakeDesktopProcess>)null),
                        new PreConditionFailure("action cannot be null."));
                    test.assertNull(fakeProcessRun.getAction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    final IntegerValue value = new IntegerValue(5);
                    final FakeChildProcessRun setActionResult = fakeProcessRun.setAction((FakeDesktopProcess childProcess) ->
                    {
                        value.set(20);
                        childProcess.setExitCode(2);
                    });
                    test.assertSame(fakeProcessRun, setActionResult);
                    test.assertNotNull(fakeProcessRun.getAction());
                    try (final FakeDesktopProcess childProcess = FakeDesktopProcess.create())
                    {
                        fakeProcessRun.getAction().run(childProcess);
                        test.assertEqual(new byte[0], childProcess.getOutputWriteStream().getBytes());
                        test.assertEqual(new byte[0], childProcess.getErrorWriteStream().getBytes());
                        test.assertEqual(20, value.get());
                        test.assertEqual(2, childProcess.getExitCode());
                    }
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeChildProcessRun fakeProcessRun = creator.run("exe");
                    final FakeChildProcessRun setActionResult = fakeProcessRun.setAction((FakeDesktopProcess childProcess) -> { throw new ParseException("blah"); });
                    test.assertSame(fakeProcessRun, setActionResult);
                    test.assertNotNull(fakeProcessRun.getAction());
                    try (final FakeDesktopProcess childProcess = FakeDesktopProcess.create())
                    {
                        test.assertThrows(() -> fakeProcessRun.getAction().run(childProcess),
                            new ParseException("blah"));
                        test.assertEqual(new byte[0], childProcess.getOutputWriteStream().getBytes());
                        test.assertEqual(new byte[0], childProcess.getErrorWriteStream().getBytes());
                        test.assertEqual(0, childProcess.getExitCode());
                    }
                });
            });
        });
    }
}
