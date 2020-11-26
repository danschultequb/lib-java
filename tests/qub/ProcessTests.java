package qub;

public interface ProcessTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Process.class, () ->
        {
            runner.testGroup("create(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final Process process = Process.create();
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Process.create((String[])null),
                        new PreConditionFailure("commandLineArgumentStrings cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Process process = Process.create(new String[0]);
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Process process = Process.create("hello", "there");
                    test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                });
            });

            runner.testGroup("create(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Process.create((Iterable<String>)null),
                        new PreConditionFailure("commandLineArgumentStrings cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Process process = Process.create(Iterable.create());
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Process process = Process.create(Iterable.create("hello", "there"));
                    test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                });
            });

            runner.testGroup("create(CommandLineArguments)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Process.create((CommandLineArguments)null),
                        new PreConditionFailure("commandLineArguments cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Process process = Process.create(CommandLineArguments.create());
                    test.assertEqual(Iterable.create(), process.getCommandLineArguments());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Process process = Process.create(CommandLineArguments.create("hello", "there"));
                    test.assertEqual(Iterable.create("hello", "there"), process.getCommandLineArguments().map(CommandLineArgument::toString));
                });
            });
        });
    }
    
    static void test(TestRunner runner, Function0<? extends Process> creator)
    {
        runner.testGroup(Process.class, () ->
        {
            runner.test("getExitCode()", (Test test) ->
            {
                final Process process = creator.run();
                test.assertEqual(0, process.getExitCode());
            });

            runner.testGroup("setExitCode(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    final Process process = creator.run();
                    test.assertSame(process, process.setExitCode(-1));
                    test.assertEqual(-1, process.getExitCode());
                });

                runner.test("with zero", (Test test) ->
                {
                    final Process process = creator.run();
                    test.assertSame(process, process.setExitCode(0));
                    test.assertEqual(0, process.getExitCode());
                });

                runner.test("with positive", (Test test) ->
                {
                    final Process process = creator.run();
                    test.assertSame(process, process.setExitCode(2));
                    test.assertEqual(2, process.getExitCode());
                });
            });

            runner.test("incrementExitCode()", (Test test) ->
            {
                final Process process = creator.run();
                test.assertEqual(0, process.getExitCode());
                test.assertSame(process, process.incrementExitCode());
                test.assertEqual(1, process.getExitCode());
            });

            runner.test("getMainAsyncRunner()", (Test test) ->
            {
                final Process process = creator.run();
                final AsyncScheduler mainAsyncRunner = process.getMainAsyncRunner();
                test.assertNotNull(mainAsyncRunner);
                test.assertSame(mainAsyncRunner, process.getMainAsyncRunner());
                test.assertSame(mainAsyncRunner, CurrentThread.getAsyncRunner().await());
            });

            runner.test("getParallelAsyncRunner()", (Test test) ->
            {
                try (final Process process = creator.run())
                {
                    final AsyncScheduler parallelAsyncRunner = process.getParallelAsyncRunner();
                    test.assertNotNull(parallelAsyncRunner);
                    test.assertSame(parallelAsyncRunner, process.getParallelAsyncRunner());
                    test.assertNotSame(parallelAsyncRunner, CurrentThread.getAsyncRunner().await());
                }
            });

            runner.test("getCommandLineArguments()", (Test test) ->
            {
                try (final Process process = creator.run())
                {
                    final CommandLineArguments arguments = process.getCommandLineArguments();
                    test.assertNotNull(arguments);
                    test.assertSame(arguments, process.getCommandLineArguments());
                }
            });

            runner.test("getCharacterEncoding()", (Test test) ->
            {
                final Process process = creator.run();
                test.assertEqual(CharacterEncoding.UTF_8, process.getCharacterEncoding());

                process.setCharacterEncoding(CharacterEncoding.US_ASCII);
                test.assertEqual(CharacterEncoding.US_ASCII, process.getCharacterEncoding());

                test.assertThrows(() -> process.setCharacterEncoding(null),
                    new PreConditionFailure("characterEncoding cannot be null."));
                test.assertEqual(CharacterEncoding.US_ASCII, process.getCharacterEncoding());
            });

            runner.test("getLineSeparator()", (Test test) ->
            {
                final Process process = creator.run();
                test.assertEqual(process.onWindows().await() ? "\r\n" : "\n", process.getLineSeparator());

                process.setLineSeparator("\r\n");
                test.assertEqual("\r\n", process.getLineSeparator());

                process.setLineSeparator("abc");
                test.assertEqual("abc", process.getLineSeparator());

                process.setLineSeparator("");
                test.assertEqual("", process.getLineSeparator());

                process.setLineSeparator(null);
                test.assertEqual(null, process.getLineSeparator());
            });

            runner.test("getOutputWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final CharacterToByteWriteStream writeStream = process.getOutputWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getErrorWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final ByteWriteStream writeStream = process.getErrorWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getInputByteWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final ByteReadStream readStream = process.getInputReadStream();
                test.assertNotNull(readStream);
            });

            runner.testGroup("setInputReadStream(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setInputReadStream(null);
                    test.assertNull(process.getInputReadStream());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setInputReadStream(ByteReadStream.create(CharacterEncoding.UTF_8.encodeCharacters("hello there my good friend\nHow are you?\r\nI'm alright.").await()));

                    final CharacterToByteReadStream readStream = process.getInputReadStream();
                    test.assertEqual(new byte[] { 104, 101, 108, 108, 111 }, readStream.readBytes(5).await());

                    final byte[] byteBuffer = new byte[2];
                    test.assertEqual(2, readStream.readBytes(byteBuffer).await());
                    test.assertEqual(new byte[] { 32, 116 }, byteBuffer);

                    test.assertEqual(1, readStream.readBytes(byteBuffer, 1, 1).await());
                    test.assertEqual(new byte[] { 32, 104 }, byteBuffer);

                    test.assertEqual(new char[] { 'e', 'r', 'e' }, readStream.readCharacters(3).await());

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, readStream.readCharacters(characterBuffer).await());
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, readStream.readCharacters(characterBuffer, 0, 2).await());
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", readStream.readString(5).await());
                });
            });

            runner.test("getInputReadStream()", (Test test) ->
            {
                final Process process = creator.run();
                final CharacterToByteReadStream readStream = process.getInputReadStream();
                test.assertNotNull(readStream);
            });

            runner.testGroup("setInputReadStream(CharacterToByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setInputReadStream(null);
                    test.assertNull(process.getInputReadStream());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setInputReadStream(CharacterToByteReadStream.create("ere my good friend\nHow are you?\r\nI'm alright."));

                    final CharacterToByteReadStream readStream = process.getInputReadStream();
                    test.assertEqual(new char[] { 'e', 'r', 'e' }, readStream.readCharacters(3).await());

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, readStream.readCharacters(characterBuffer).await());
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, readStream.readCharacters(characterBuffer, 0, 2).await());
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", readStream.readString(5).await());
                });
            });

            runner.test("getRandom()", (Test test) ->
            {
                final Process process = creator.run();
                final Random random = process.getRandom();
                test.assertNotNull(random);
                test.assertTrue(random instanceof JavaRandom);
                test.assertSame(random, process.getRandom());
            });

            runner.test("setRandom()", (Test test) ->
            {
                final Process process = creator.run();

                process.setRandom(null);
                test.assertNull(process.getRandom());

                final FixedRandom random = new FixedRandom(1);
                process.setRandom(random);
                test.assertSame(random, process.getRandom());
            });

            runner.test("getFileSystem()", (Test test) ->
            {
                final Process process = creator.run();
                final FileSystem defaultFileSystem = process.getFileSystem();
                test.assertNotNull(defaultFileSystem);
                test.assertTrue(defaultFileSystem instanceof JavaFileSystem);
            });

            runner.testGroup("setFileSystem(FileSystem)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    final FileSystem oldFileSystem = process.getFileSystem();
                    final Folder oldCurrentFolder = process.getCurrentFolder();
                    test.assertThrows(() -> process.setFileSystem(null),
                        new PreConditionFailure("fileSystem cannot be null."));
                    test.assertSame(oldFileSystem, process.getFileSystem());
                    test.assertSame(oldCurrentFolder, process.getCurrentFolder());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
                    process.setFileSystem(fileSystem);
                    test.assertSame(fileSystem, process.getFileSystem());
                });
            });

            runner.test("getNetwork()", (Test test) ->
            {
                final Process process = creator.run();
                final Network defaultNetwork = process.getNetwork();
                test.assertNotNull(defaultNetwork);
                test.assertTrue(defaultNetwork instanceof JavaNetwork);
                test.assertSame(defaultNetwork, process.getNetwork());
            });

            runner.testGroup("setNetwork(Network)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setNetwork((Network)null);
                    test.assertNull(process.getNetwork());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    final Network network = JavaNetwork.create(test.getClock());
                    process.setNetwork(network);
                    test.assertSame(network, process.getNetwork());
                });
            });

            runner.test("getCurrentFolderPathString()", (Test test) ->
            {
                final Process process = creator.run();
                final String currentFolderPathString = process.getCurrentFolderPathString();
                test.assertNotNull(currentFolderPathString);
                test.assertFalse(currentFolderPathString.isEmpty());
                test.assertEndsWith(currentFolderPathString, "/");
                test.assertTrue(process.getFileSystem().folderExists(currentFolderPathString).await());
            });

            runner.testGroup("setCurrentFolderPathString(String)", () ->
            {
                runner.test("with null string", (Test test) ->
                {
                    final Process process = creator.run();
                    final Folder old = process.getCurrentFolder();
                    test.assertThrows(() -> process.setCurrentFolderPathString(null),
                        new PreConditionFailure("currentFolderPathString cannot be null."));
                    test.assertEqual(old, process.getCurrentFolder());
                });

                runner.test("with empty string", (Test test) ->
                {
                    final Process process = creator.run();
                    final Folder old = process.getCurrentFolder();
                    test.assertThrows(() -> process.setCurrentFolderPathString(""),
                        new PreConditionFailure("currentFolderPathString cannot be empty."));
                    test.assertEqual(old, process.getCurrentFolder());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final Process process = creator.run();
                    final Folder old = process.getCurrentFolder();
                    test.assertThrows(() -> process.setCurrentFolderPathString("hello there"),
                        new PreConditionFailure("currentFolderPath.isRooted() cannot be false."));
                    test.assertEqual(old, process.getCurrentFolder());
                });
            });

            runner.test("getCurrentFolderPath()", (Test test) ->
            {
                final Process process = creator.run();
                final Path currentFolderPath = process.getCurrentFolderPath();
                test.assertNotNull(currentFolderPath);
                test.assertTrue(currentFolderPath.isRooted());
                test.assertEndsWith(currentFolderPath.toString(), "/");
                test.assertTrue(process.getFileSystem().folderExists(currentFolderPath).await());
            });

            runner.test("setCurrentFolderPath(Path) with null", (Test test) ->
            {
                final Process process = creator.run();
                final Path old = process.getCurrentFolderPath();
                test.assertThrows(() -> process.setCurrentFolderPath(null),
                    new PreConditionFailure("currentFolderPath cannot be null."));
                test.assertEqual(old, process.getCurrentFolderPath());
            });

            runner.test("getCurrentFolder()", (Test test) ->
            {
                final Process process = creator.run();
                final Folder currentFolder = process.getCurrentFolder();
                test.assertNotNull(currentFolder);
                test.assertTrue(currentFolder.exists().await());
            });

            runner.testGroup("getEnvironmentVariable()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    test.assertThrows(() -> process.getEnvironmentVariable(null),
                        new PreConditionFailure("variableName cannot be null."));
                });

                runner.test("with empty string", (Test test) ->
                {
                    final Process process = creator.run();
                    test.assertThrows(() -> process.getEnvironmentVariable(""),
                        new PreConditionFailure("variableName cannot be empty."));
                });

                runner.test("with non-existing variable name", (Test test) ->
                {
                    final Process process = creator.run();
                    test.assertThrows(() -> process.getEnvironmentVariable("Can't find me").await(),
                        new NotFoundException("No environment variable named \"Can't find me\" found."));
                });

                runner.test("with existing variable name", (Test test) ->
                {
                    final Process process = creator.run();
                    final String path = process.getEnvironmentVariable("path").await();
                    test.assertNotNull(path);
                    test.assertFalse(path.isEmpty());
                });
            });

            runner.test("setEnvironmentVariables()", (Test test) ->
            {
                final Process process = creator.run();
                final EnvironmentVariables envVars = new EnvironmentVariables();
                test.assertSame(process, process.setEnvironmentVariables(envVars));
                test.assertSame(envVars, process.getEnvironmentVariables());
            });

            runner.testGroup("getStopwatch()", () ->
            {
                runner.test("with default creator", (Test test) ->
                {
                    final Process process = creator.run();
                    final Stopwatch stopwatch = process.getStopwatch();
                    test.assertNotNull(stopwatch);
                    test.assertTrue(stopwatch instanceof JavaStopwatch);
                });

                runner.test("with null creator", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setStopwatchCreator(null);
                    test.assertNull(process.getStopwatch());
                });
            });

            runner.testGroup("getClock()", () ->
            {
                runner.test("with default", (Test test) ->
                {
                    final Process process = creator.run();
                    final Clock clock = process.getClock();
                    test.assertNotNull(clock);
                    test.assertTrue(clock instanceof JavaClock);
                });

                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setClock(null);
                    test.assertNull(process.getClock());
                });

                runner.test("with manual", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setClock(new ManualClock(DateTime.create(123, 4, 5), test.getMainAsyncRunner()));
                    final Clock clock = process.getClock();
                    test.assertNotNull(clock);
                    test.assertTrue(clock instanceof ManualClock);
                });
            });

            runner.testGroup("getDisplays()", () ->
            {
                runner.test("with default", (Test test) ->
                {
                    final Process process = creator.run();
                    final Iterable<Display> displays = process.getDisplays();
                    test.assertNotNull(displays);
                    test.assertGreaterThanOrEqualTo(displays.getCount(), 1);
                    for (final Display display : displays)
                    {
                        test.assertNotNull(display);
                        test.assertGreaterThanOrEqualTo(display.getWidthInPixels(), 1);
                        test.assertGreaterThanOrEqualTo(display.getHeightInPixels(), 1);
                        test.assertGreaterThanOrEqualTo(display.getHorizontalDpi(), 1);
                        test.assertGreaterThanOrEqualTo(display.getVerticalDpi(), 1);
                    }
                });
            });

            runner.testGroup("getProcessBuilder(String)", () ->
            {
                runner.test("with null string", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertThrows(() -> process.getProcessBuilder((String)null), new PreConditionFailure("executablePath cannot be null."));
                    }
                });

                runner.test("with empty string", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertThrows(() -> process.getProcessBuilder(""), new PreConditionFailure("executablePath cannot be empty."));
                    }
                });

                runner.test("with path with file extension relative to the current folder", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("project.json").await();
                        test.assertNotNull(builder);
                        test.assertEqual(Path.parse("project.json"), builder.getExecutablePath());
                        test.assertEqual(Iterable.create(), builder.getArguments());
                    }
                });

                runner.test("with path without file extension relative to the current folder", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("project").await();
                        test.assertNotNull(builder);
                        test.assertEqual(Path.parse("project"), builder.getExecutablePath());
                        test.assertEqual(Iterable.create(), builder.getArguments());
                    }
                });

                runner.test("with rooted path with file extension", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final Path executablePath = process.getCurrentFolder().getFile("project.json").await().getPath();
                        final ProcessBuilder builder = process.getProcessBuilder(executablePath).await();
                        test.assertNotNull(builder);
                        test.assertEqual(executablePath, builder.getExecutablePath());
                        test.assertEqual(Iterable.create(), builder.getArguments());
                    }
                });

                runner.test("with rooted path without file extension", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final Path executablePath = process.getCurrentFolder().getFile("project").await().getPath();
                        final ProcessBuilder builder = process.getProcessBuilder(executablePath).await();
                        test.assertNotNull(builder);
                        test.assertEqual(executablePath, builder.getExecutablePath());
                        test.assertEqual(Iterable.create(), builder.getArguments());
                    }
                });

                runner.test("with rooted path to executable", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("C:/qub/openjdk/jdk/versions/15.0.1/bin/javac.exe").await();
                            test.assertEqual("javac.exe", builder.getExecutablePath().getSegments().last());
                            test.assertEqual(Iterable.create(), builder.getArguments());
                            test.assertEqual(2, builder.run().await());
                        }
                    }
                });

                runner.test("with rooted path to executable (with verbose logging)", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final InMemoryCharacterStream verbose = InMemoryCharacterStream.create();
                            final ProcessBuilder builder = process.getProcessBuilder("C:/qub/openjdk/jdk/versions/15.0.1/bin/javac.exe").await()
                                .setVerbose(verbose);
                            test.assertEqual("", verbose.getText().await());
                            test.assertEqual("javac.exe", builder.getExecutablePath().getSegments().last());
                            test.assertEqual(Iterable.create(), builder.getArguments());
                            test.assertEqual(2, builder.run().await());
                            test.assertEqual(
                                Iterable.create(
                                    "Looking for executable: \"C:/qub/openjdk/jdk/versions/15.0.1/bin/javac.exe\" (check extensions: true)",
                                    "Checking \"C:/qub/openjdk/jdk/versions/15.0.1/bin/javac.exe\"... Yes!",
                                    "C:/Users/dansc/Sources/lib-java/: C:/qub/openjdk/jdk/versions/15.0.1/bin/javac.exe"),
                                Strings.getLines(verbose.getText().await()));
                        }
                    }
                });

                runner.test("with rooted path to executable with fake process", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final FileSystem fileSystem = process.getFileSystem();
                            final File executableFile = fileSystem.getFile("C:/Program Files/Java/jdk1.8.0_192/bin/javac.exe").await();
                            process.setProcessFactory(new FakeProcessFactory(test.getParallelAsyncRunner(), process.getCurrentFolderPath())
                                .add(FakeProcessRun.get(executableFile)
                                    .setFunction(8)));
                            final ProcessBuilder builder = process.getProcessBuilder(executableFile.getPath()).await();
                            test.assertEqual("javac.exe", builder.getExecutablePath().getSegments().last());
                            test.assertEqual(Iterable.create(), builder.getArguments());
                            test.assertEqual(8, builder.run().await());
                        }
                    }
                });

                runner.test("with path with file extension relative to PATH environment variable", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac.exe").await();
                            test.assertEqual("javac.exe", builder.getExecutablePath().getSegments().last());
                            test.assertEqual(Iterable.create(), builder.getArguments());
                            test.assertEqual(2, builder.run().await());
                        }
                    }
                });

                runner.test("with path with file extension relative to PATH environment variable (with verbose logging)", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final InMemoryCharacterStream verbose = InMemoryCharacterStream.create();
                            final ProcessBuilder builder = process.getProcessBuilder("javac.exe").await()
                                .setVerbose(verbose);
                            test.assertEqual("javac.exe", builder.getExecutablePath().getSegments().last());
                            test.assertEqual(Iterable.create(), builder.getArguments());
                            test.assertEqual(2, builder.run().await());
                            test.assertEqual(
                                Iterable.create(
                                    "Looking for executable: \"javac.exe\" (check extensions: true)",
                                    "Checking \"C:/Users/dansc/Sources/lib-java/javac.exe\"... No.",
                                    "Checking \"C:/Users/dansc/cmder/bin/javac.exe\"... No.",
                                    "Checking \"C:/Users/dansc/cmder/vendor/bin/javac.exe\"... No.",
                                    "Checking \"C:/Users/dansc/cmder/vendor/conemu-maximus5/ConEmu/Scripts/javac.exe\"... No.",
                                    "Checking \"C:/Users/dansc/cmder/vendor/conemu-maximus5/javac.exe\"... No.",
                                    "Checking \"C:/Users/dansc/cmder/vendor/conemu-maximus5/ConEmu/javac.exe\"... No.",
                                    "Checking \"C:/Program Files (x86)/Python38-32/Scripts/javac.exe\"... No.",
                                    "Checking \"C:/Program Files (x86)/Python38-32/javac.exe\"... No.",
                                    "Checking \"C:/Program Files (x86)/Python37-32/Scripts/javac.exe\"... No.",
                                    "Checking \"C:/Program Files (x86)/Python37-32/javac.exe\"... No.",
                                    "Checking \"C:/WINDOWS/system32/javac.exe\"... No.",
                                    "Checking \"C:/WINDOWS/javac.exe\"... No.",
                                    "Checking \"C:/WINDOWS/System32/Wbem/javac.exe\"... No.",
                                    "Checking \"C:/WINDOWS/System32/WindowsPowerShell/v1.0/javac.exe\"... No.",
                                    "Checking \"C:/WINDOWS/System32/OpenSSH/javac.exe\"... No.",
                                    "Checking \"C:/qub/openjdk/jdk/versions/15.0.1/bin/javac.exe\"... Yes!",
                                    "C:/Users/dansc/Sources/lib-java/: C:/qub/openjdk/jdk/versions/15.0.1/bin/javac.exe"),
                                Strings.getLines(verbose.getText().await()));
                        }
                    }
                });

                runner.test("with path with file extension relative to PATH environment variable with fake process runner", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            process.setProcessFactory(new FakeProcessFactory(test.getParallelAsyncRunner(), process.getCurrentFolderPath())
                                .add(FakeProcessRun.get("javac.exe")
                                    .setFunction(2)));
                            final ProcessBuilder builder = process.getProcessBuilder("javac.exe").await();
                            test.assertEqual("javac.exe", builder.getExecutablePath().getSegments().last());
                            test.assertEqual(Iterable.create(), builder.getArguments());
                            test.assertEqual(2, builder.run().await());
                        }
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac").await();
                            test.assertTrue(builder.getExecutablePath().getSegments().last().startsWith("javac"));
                            test.assertEqual(Iterable.create(), builder.getArguments());

                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            builder.redirectOutput(output);

                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            builder.redirectError(error);

                            test.assertEqual(2, builder.run().await());

                            final String outputString = output.getText().await();
                            test.assertNotNullAndNotEmpty(outputString);
                            test.assertTrue(outputString.contains("javac <options> <source files>"), "Process output (" + outputString + ") should have contained \"javac <options> <source files>\".");
                            test.assertTrue(outputString.contains("Terminate compilation if warnings occur"), "Process output (" + outputString + ") should have contained \"Terminate compilation if warnings occur\".");

                            test.assertEqual("", error.getText().await());
                        }
                    }
                });

                runner.test("with non-existing working folder", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("javac").await();

                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        builder.redirectOutput(output);

                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        builder.redirectError(error);

                        final Folder workingFolder = test.getProcess().getCurrentFolder().getFolder("I don't exist").await();
                        builder.setWorkingFolder(workingFolder);
                        test.assertThrows(() -> builder.run().await(),
                            new FolderNotFoundException(workingFolder));
                        test.assertEqual("", output.getText().await());
                        test.assertEqual("", error.getText().await());
                    }
                });

                runner.test("with existing working folder (a child folder of the current folder)", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("javac").await();

                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        builder.redirectOutput(output);

                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        builder.redirectError(error);

                        final Folder currentFolder = test.getProcess().getCurrentFolder();
                        final Folder workingFolder = currentFolder.createFolder("temp2").await();
                        try
                        {
                            builder.setWorkingFolder(workingFolder);
                            test.assertEqual(2, builder.run().await());

                            final String outputString = output.getText().await();
                            test.assertNotNullAndNotEmpty(outputString);
                            test.assertContains(outputString, "javac <options> <source files>");
                            test.assertContains(outputString, "Terminate compilation if warnings occur");

                            final String errorString = error.getText().await();
                            test.assertEqual("", errorString);
                        }
                        finally
                        {
                            workingFolder.delete().await();
                        }
                    }
                });

                runner.test("with existing working folder (the current folder)", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("javac").await();

                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        builder.redirectOutput(output);

                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        builder.redirectError(error);

                        final Folder workingFolder = test.getProcess().getCurrentFolder();
                        builder.setWorkingFolder(workingFolder);
                        test.assertEqual(2, builder.run().await());

                        final String outputString = output.getText().await();
                        test.assertNotNullAndNotEmpty(outputString);
                        test.assertContains(outputString, "javac <options> <source files>");
                        test.assertContains(outputString, "Terminate compilation if warnings occur");

                        final String errorString = error.getText().await();
                        test.assertEqual("", errorString);
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable with redirected error", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows().await())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac").await();
                            builder.addArgument("notfound.java");

                            final InMemoryByteStream error = InMemoryByteStream.create();
                            builder.redirectError(error);

                            test.assertEqual(2, builder.run().await());

                            final String errorString = new String(error.getBytes());
                            test.assertContains(errorString, "file not found: notfound.java");
                        }
                    }
                });
            });

            runner.testGroup("getSystemProperties()", () ->
            {
                runner.test("with no modifications", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final Map<String,String> systemProperties = process.getSystemProperties();
                        test.assertNotNull(systemProperties);
                        test.assertTrue(systemProperties.containsKey("os.name"));
                        test.assertTrue(systemProperties.containsKey("sun.java.command"));
                    }
                });

                runner.test("with modifications", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertFalse(process.getSystemProperties().containsKey("apples"));

                        process.setSystemProperty("apples", "bananas");

                        final Map<String,String> systemProperties = process.getSystemProperties();
                        test.assertNotNull(systemProperties);
                        test.assertEqual("bananas", systemProperties.get("apples").await());
                    }
                });
            });

            runner.testGroup("getSystemProperty()", () ->
            {
                runner.test("with null systemPropertyName", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertThrows(() -> process.getSystemProperty(null),
                            new PreConditionFailure("systemPropertyName cannot be null."));
                    }
                });

                runner.test("with empty systemPropertyName", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertThrows(() -> process.getSystemProperty(""),
                            new PreConditionFailure("systemPropertyName cannot be empty."));
                    }
                });

                runner.test("with not-found systemPropertyName", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertThrows(() -> process.getSystemProperty("apples-and-bananas").await(),
                            new NotFoundException("No system property found with the name \"apples-and-bananas\"."));
                    }
                });

                runner.test("with found systemPropertyName", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertNotNullAndNotEmpty(process.getSystemProperty("os.name").await());
                    }
                });
            });

            runner.test("onWindows()", (Test test) ->
            {
                try (final Process process = creator.run())
                {
                    test.assertNotNull(process.onWindows().await());
                }
            });

            runner.test("getJVMClasspath()", (Test test) ->
            {
                try (final Process process = creator.run())
                {
                    test.assertNotNullAndNotEmpty(process.getJVMClasspath().await());
                }
            });

            runner.test("setJVMClasspath()", (Test test) ->
            {
                final String originalJvmClassPath = test.getProcess().getJVMClasspath().await();
                try (final Process process = creator.run())
                {
                    test.assertEqual(originalJvmClassPath, process.getJVMClasspath().await());

                    test.assertSame(process, process.setJVMClasspath("hello"));

                    test.assertEqual("hello", process.getJVMClasspath().await());
                    test.assertEqual(originalJvmClassPath, test.getProcess().getJVMClasspath().await());
                }
            });

            runner.test("getJavaVersion()", (Test test) ->
            {
                final VersionNumber version = test.getProcess().getJavaVersion();
                test.assertNotNull(version);
            });
        });
    }
}
