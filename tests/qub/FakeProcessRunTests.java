package qub;

public interface FakeProcessRunTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeProcessRun.class, () ->
        {
            runner.testGroup("get(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> FakeProcessRun.get((String)null),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> FakeProcessRun.get(""),
                        new PreConditionFailure("executablePath cannot be empty."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = FakeProcessRun.get("testFile.exe");
                    test.assertInstanceOf(fakeProcessRun, BasicFakeProcessRun.class);
                    test.assertEqual(Path.parse("testFile.exe"), fakeProcessRun.getExecutablePath());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = FakeProcessRun.get("/testFile.exe");
                    test.assertInstanceOf(fakeProcessRun, BasicFakeProcessRun.class);
                    test.assertEqual(Path.parse("/testFile.exe"), fakeProcessRun.getExecutablePath());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                    test.assertNull(fakeProcessRun.getFunction());
                });
            });

            runner.testGroup("get(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> FakeProcessRun.get((Path)null),
                        new PreConditionFailure("executableFilePath cannot be null."));
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = FakeProcessRun.get(Path.parse("testFile.exe"));
                    test.assertInstanceOf(fakeProcessRun, BasicFakeProcessRun.class);
                    test.assertEqual(Path.parse("testFile.exe"), fakeProcessRun.getExecutablePath());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = FakeProcessRun.get(Path.parse("/testFile.exe"));
                    test.assertInstanceOf(fakeProcessRun, BasicFakeProcessRun.class);
                    test.assertEqual(Path.parse("/testFile.exe"), fakeProcessRun.getExecutablePath());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                    test.assertNull(fakeProcessRun.getFunction());
                });
            });

            runner.testGroup("get(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> FakeProcessRun.get((File)null),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    final File file = fileSystem.getFile("/testFile2.exe").await();
                    final FakeProcessRun fakeProcessRun = FakeProcessRun.get(file);
                    test.assertInstanceOf(fakeProcessRun, BasicFakeProcessRun.class);
                    test.assertEqual(Path.parse("/testFile2.exe"), fakeProcessRun.getExecutablePath());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                    test.assertNull(fakeProcessRun.getFunction());
                });
            });
        });
    }

    static void test(TestRunner runner, Function1<String,FakeProcessRun> creator)
    {
        runner.testGroup(FakeProcessRun.class, () ->
        {
            runner.testGroup("addArgument(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.addArgument(null),
                        new PreConditionFailure("argument cannot be null."));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with empty", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.addArgument(""),
                        new PreConditionFailure("argument cannot be empty."));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArgument("apples"));
                    test.assertEqual(Iterable.create("apples"), fakeProcessRun.getArguments());
                });

                runner.test("with non-empty with spaces", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArgument("ap pl es"));
                    test.assertEqual(Iterable.create("ap pl es"), fakeProcessRun.getArguments());
                });
            });

            runner.testGroup("addArguments(String...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.addArguments((String[])null),
                        new PreConditionFailure("arguments cannot be null."));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(new String[0]));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with one-element array", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(new String[] { "a" }));
                    test.assertEqual(Iterable.create("a"), fakeProcessRun.getArguments());
                });

                runner.test("with two-element array", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(new String[] { "a", "b" }));
                    test.assertEqual(Iterable.create("a", "b"), fakeProcessRun.getArguments());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments());
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with one value", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments("a"));
                    test.assertEqual(Iterable.create("a"), fakeProcessRun.getArguments());
                });

                runner.test("with two values", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments("a", "b"));
                    test.assertEqual(Iterable.create("a", "b"), fakeProcessRun.getArguments());
                });
            });

            runner.testGroup("addArguments(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.addArguments((Iterable<String>)null),
                        new PreConditionFailure("arguments cannot be null."));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with empty", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(Iterable.create()));
                    test.assertEqual(Iterable.create(), fakeProcessRun.getArguments());
                });

                runner.test("with one value", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(Iterable.create("a")));
                    test.assertEqual(Iterable.create("a"), fakeProcessRun.getArguments());
                });

                runner.test("with two values", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.addArguments(Iterable.create("a", "b")));
                    test.assertEqual(Iterable.create("a", "b"), fakeProcessRun.getArguments());
                });
            });

            runner.testGroup("setWorkingFolder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder((String)null));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with empty", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setWorkingFolder(""),
                        new PreConditionFailure("workingFolderPath (\"\") must be either null or not empty."));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setWorkingFolder("folder"),
                        new PreConditionFailure("workingFolderPath == null || workingFolderPath.isRooted() cannot be false."));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder("/folder"));
                    test.assertEqual(Path.parse("/folder"), fakeProcessRun.getWorkingFolderPath());
                });
            });

            runner.testGroup("setWorkingFolder(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder((Path)null));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setWorkingFolder(Path.parse("folder")),
                        new PreConditionFailure("workingFolderPath == null || workingFolderPath.isRooted() cannot be false."));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with rooted path", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder(Path.parse("/folder")));
                    test.assertEqual(Path.parse("/folder"), fakeProcessRun.getWorkingFolderPath());
                });
            });

            runner.testGroup("setWorkingFolder(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder((Folder)null));
                    test.assertNull(fakeProcessRun.getWorkingFolderPath());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    final Folder folder = fileSystem.getFolder("/folder").await();
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setWorkingFolder(folder));
                    test.assertEqual(Path.parse("/folder"), fakeProcessRun.getWorkingFolderPath());
                });
            });

            runner.test("setFunction(int)", (Test test) ->
            {
                final FakeProcessRun fakeProcessRun = creator.run("exe");
                test.assertSame(fakeProcessRun, fakeProcessRun.setFunction(17));
                test.assertNotNull(fakeProcessRun.getFunction());
                final InMemoryByteStream input = new InMemoryByteStream();
                final InMemoryByteStream output = new InMemoryByteStream();
                final InMemoryByteStream error = new InMemoryByteStream();
                test.assertEqual(17, fakeProcessRun.getFunction().run(input, output, error));
                test.assertEqual(new byte[0], output.getBytes());
                test.assertEqual(new byte[0], error.getBytes());
            });

            runner.testGroup("setFunction(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setFunction((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    final IntegerValue value = new IntegerValue(5);
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction(() -> { value.set(20); }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertEqual(0, fakeProcessRun.getFunction().run(input, output, error));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                    test.assertEqual(20, value.get());
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction(() -> { throw new ParseException("blah"); }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertThrows(() -> fakeProcessRun.getFunction().run(input, output, error),
                        new ParseException("blah"));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });
            });

            runner.testGroup("setFunction(Function0<Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setFunction((Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    final IntegerValue value = new IntegerValue(5);
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction(() -> { value.set(20); return 7; }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertEqual(7, fakeProcessRun.getFunction().run(input, output, error));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                    test.assertEqual(20, value.get());
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((Function0<Integer>)() -> { throw new ParseException("blah"); }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertThrows(() -> fakeProcessRun.getFunction().run(input, output, error),
                        new ParseException("blah"));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });
            });

            runner.testGroup("setFunction(Action1<ByteWriteStream>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setFunction((Action1<ByteWriteStream>)null),
                        new PreConditionFailure("action cannot be null."));
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((ByteWriteStream output) -> { output.writeBytes(new byte[] { 1, 2, 3 }).await(); }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertEqual(0, fakeProcessRun.getFunction().run(input, output, error));
                    test.assertEqual(new byte[] { 1, 2, 3 }, output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((Action1<ByteWriteStream>)(ByteWriteStream output) -> { throw new ParseException("blah"); }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertThrows(() -> fakeProcessRun.getFunction().run(input, output, error),
                        new ParseException("blah"));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });
            });

            runner.testGroup("setFunction(Function1<ByteWriteStream,Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setFunction((Function1<ByteWriteStream,Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((ByteWriteStream output) -> { output.writeBytes(new byte[] { 1, 2 }).await(); return 100; }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertEqual(100, fakeProcessRun.getFunction().run(input, output, error));
                    test.assertEqual(new byte[] { 1, 2 }, output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((Function1<ByteWriteStream,Integer>)(ByteWriteStream output) -> { throw new ParseException("blah"); }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertThrows(() -> fakeProcessRun.getFunction().run(input, output, error),
                        new ParseException("blah"));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });
            });

            runner.testGroup("setFunction(Action2<ByteWriteStream,ByteWriteStream>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setFunction((Action2<ByteWriteStream,ByteWriteStream>)null),
                        new PreConditionFailure("action cannot be null."));
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((ByteWriteStream output, ByteWriteStream error) ->
                    {
                        output.writeBytes(new byte[] { 1, 2, 3 }).await();
                        error.writeBytes(new byte[] { 4, 5, 6 }).await();
                    }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertEqual(0, fakeProcessRun.getFunction().run(input, output, error));
                    test.assertEqual(new byte[] { 1, 2, 3 }, output.getBytes());
                    test.assertEqual(new byte[] { 4, 5, 6 }, error.getBytes());
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((Action2<ByteWriteStream,ByteWriteStream>)(ByteWriteStream output, ByteWriteStream error) ->
                    {
                        throw new ParseException("blah");
                    }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertThrows(() -> fakeProcessRun.getFunction().run(input, output, error),
                        new ParseException("blah"));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });
            });

            runner.testGroup("setFunction(Function2<ByteWriteStream,ByteWriteStream,Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setFunction((Function2<ByteWriteStream,ByteWriteStream,Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((ByteWriteStream output, ByteWriteStream error) ->
                    {
                        output.writeBytes(new byte[] { 1, 2, 3 }).await();
                        error.writeBytes(new byte[] { 4, 5, 6 }).await();
                        return 6;
                    }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertEqual(6, fakeProcessRun.getFunction().run(input, output, error));
                    test.assertEqual(new byte[] { 1, 2, 3 }, output.getBytes());
                    test.assertEqual(new byte[] { 4, 5, 6 }, error.getBytes());
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((Function2<ByteWriteStream,ByteWriteStream,Integer>)(ByteWriteStream output, ByteWriteStream error) ->
                    {
                        throw new ParseException("blah");
                    }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertThrows(() -> fakeProcessRun.getFunction().run(input, output, error),
                        new ParseException("blah"));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });
            });

            runner.testGroup("setFunction(Action3<ByteReadStream,ByteWriteStream,ByteWriteStream>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setFunction((Action3<ByteReadStream,ByteWriteStream,ByteWriteStream>)null),
                        new PreConditionFailure("action cannot be null."));
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) ->
                    {
                        output.writeAllBytes(input).await();
                        error.writeBytes(new byte[] { 4, 5, 6 }).await();
                    }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream(new byte[] { 10, 20, 30 }).endOfStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertEqual(0, fakeProcessRun.getFunction().run(input, output, error));
                    test.assertEqual(new byte[] { 10, 20, 30 }, output.getBytes());
                    test.assertEqual(new byte[] { 4, 5, 6 }, error.getBytes());
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((Action3<ByteReadStream,ByteWriteStream,ByteWriteStream>)(ByteReadStream input, ByteWriteStream output, ByteWriteStream error) ->
                    {
                        throw new ParseException("blah");
                    }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertThrows(() -> fakeProcessRun.getFunction().run(input, output, error),
                        new ParseException("blah"));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });
            });

            runner.testGroup("setFunction(Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertThrows(() -> fakeProcessRun.setFunction((Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                    test.assertNull(fakeProcessRun.getFunction());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) ->
                    {
                        output.writeAllBytes(input).await();
                        error.writeBytes(new byte[] { 4, 5, 6 }).await();
                        return 6;
                    }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream(new byte[] { 10, 20, 30 }).endOfStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertEqual(6, fakeProcessRun.getFunction().run(input, output, error));
                    test.assertEqual(new byte[] { 10, 20, 30 }, output.getBytes());
                    test.assertEqual(new byte[] { 4, 5, 6 }, error.getBytes());
                });

                runner.test("with non-null that throws", (Test test) ->
                {
                    final FakeProcessRun fakeProcessRun = creator.run("exe");
                    test.assertSame(fakeProcessRun, fakeProcessRun.setFunction((Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer>)(ByteReadStream input, ByteWriteStream output, ByteWriteStream error) ->
                    {
                        throw new ParseException("blah");
                    }));
                    test.assertNotNull(fakeProcessRun.getFunction());
                    final InMemoryByteStream input = new InMemoryByteStream();
                    final InMemoryByteStream output = new InMemoryByteStream();
                    final InMemoryByteStream error = new InMemoryByteStream();
                    test.assertThrows(() -> fakeProcessRun.getFunction().run(input, output, error),
                        new ParseException("blah"));
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });
            });
        });
    }
}
