package qub;

public class ProcessTests
{
    public static void test(TestRunner runner, Function0<Process> creator)
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

            runner.test("getCharacterEncoding()", (Test test) ->
            {
                final Process process = creator.run();
                test.assertEqual(CharacterEncoding.US_ASCII, process.getCharacterEncoding());

                process.setCharacterEncoding(CharacterEncoding.US_ASCII);
                test.assertEqual(CharacterEncoding.US_ASCII, process.getCharacterEncoding());

                process.setCharacterEncoding(null);
                test.assertEqual(null, process.getCharacterEncoding());
            });

            runner.test("getLineSeparator()", (Test test) ->
            {
                final Process process = creator.run();
                test.assertEqual(process.onWindows() ? "\r\n" : "\n", process.getLineSeparator());

                process.setLineSeparator("\r\n");
                test.assertEqual("\r\n", process.getLineSeparator());

                process.setLineSeparator("abc");
                test.assertEqual("abc", process.getLineSeparator());

                process.setLineSeparator("");
                test.assertEqual("", process.getLineSeparator());

                process.setLineSeparator(null);
                test.assertEqual(null, process.getLineSeparator());
            });

            runner.test("getIncludeNewLines()", (Test test) ->
            {
                final Process process = creator.run();
                test.assertFalse(process.getIncludeNewLines());

                process.setIncludeNewLines(true);
                test.assertTrue(process.getIncludeNewLines());
            });

            runner.test("getOutputByteWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final ByteWriteStream writeStream = process.getOutputByteWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getOutputCharacterWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final CharacterWriteStream writeStream = process.getOutputCharacterWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getErrorByteWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final ByteWriteStream writeStream = process.getErrorByteWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getErrorCharacterWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final CharacterWriteStream writeStream = process.getErrorCharacterWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getInputByteWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final ByteReadStream readStream = process.getInputByteReadStream();
                test.assertNotNull(readStream);
            });

            runner.testGroup("setInputByteReadStream(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setInputByteReadStream(null);
                    test.assertNull(process.getInputByteReadStream());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    final InMemoryCharacterStream readStream = new InMemoryCharacterStream("hello there my good friend\nHow are you?\r\nI'm alright.");
                    process.setInputByteReadStream(readStream.asByteReadStream());

                    final ByteReadStream byteReadStream = process.getInputByteReadStream();
                    test.assertEqual(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5).awaitError());

                    final byte[] byteBuffer = new byte[2];
                    test.assertEqual(2, byteReadStream.readBytes(byteBuffer).awaitError());
                    test.assertEqual(new byte[] { 32, 116 }, byteBuffer);

                    test.assertEqual(1, byteReadStream.readBytes(byteBuffer, 1, 1).awaitError());
                    test.assertEqual(new byte[] { 32, 104 }, byteBuffer);

                    final CharacterReadStream characterReadStream = process.getInputCharacterReadStream();
                    test.assertEqual(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3).awaitError());

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, characterReadStream.readCharacters(characterBuffer).awaitError());
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, characterReadStream.readCharacters(characterBuffer, 0, 2).awaitError());
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", characterReadStream.readString(5).awaitError());
                });
            });

            runner.test("getInputCharacterReadStream()", (Test test) ->
            {
                final Process process = creator.run();
                final CharacterReadStream readStream = process.getInputCharacterReadStream();
                test.assertNotNull(readStream);
            });

            runner.testGroup("setInputCharacterReadStream(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setInputCharacterReadStream(null);
                    test.assertNull(process.getInputCharacterReadStream());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    final InMemoryCharacterStream readStream = new InMemoryCharacterStream("ere my good friend\nHow are you?\r\nI'm alright.").endOfStream();
                    process.setInputCharacterReadStream(readStream);

                    test.assertNull(process.getInputByteReadStream());

                    final CharacterReadStream characterReadStream = process.getInputCharacterReadStream();
                    test.assertEqual(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3).awaitError());

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, characterReadStream.readCharacters(characterBuffer).awaitError());
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, characterReadStream.readCharacters(characterBuffer, 0, 2).awaitError());
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", characterReadStream.readString(5).awaitError());
                });
            });

            runner.test("getInputLineReadStream()", (Test test) ->
            {
                final Process process = creator.run();
                final LineReadStream readStream = process.getInputLineReadStream();
                test.assertNotNull(readStream);
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
                    process.setFileSystem((FileSystem)null);
                    test.assertNull(process.getFileSystem());
                    test.assertNull(process.getCurrentFolderPathString());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
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
                    final Network network = new JavaNetwork(test.getMainAsyncRunner());
                    process.setNetwork(network);
                    test.assertSame(network, process.getNetwork());
                });
            });

            runner.testGroup("setNetwork(Function1<AsyncRunner,Network>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setNetwork((Function1<AsyncRunner,Network>)null);
                    test.assertNull(process.getNetwork());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setNetwork(JavaNetwork::new);
                    final Network network = process.getNetwork();
                    test.assertTrue(network instanceof JavaNetwork);
                    test.assertSame(network, process.getNetwork());
                });
            });

            runner.test("getCurrentFolderPathString()", (Test test) ->
            {
                final Process process = creator.run();
                final String currentFolderPathString = process.getCurrentFolderPathString();
                test.assertNotNull(currentFolderPathString);
                test.assertFalse(currentFolderPathString.isEmpty());
                test.assertTrue(process.getFileSystem().folderExists(currentFolderPathString).awaitError());
            });

            runner.testGroup("setCurrentFolderPathString(String)", () ->
            {
                runner.test("with null string", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setCurrentFolderPathString(null);
                    test.assertNull(process.getCurrentFolderPathString());
                });

                runner.test("with empty string", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setCurrentFolderPathString("");
                    test.assertEqual("", process.getCurrentFolderPathString());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setCurrentFolderPathString("hello there");
                    test.assertEqual("hello there", process.getCurrentFolderPathString());
                });
            });

            runner.test("getCurrentFolderPath()", (Test test) ->
            {
                final Process process = creator.run();
                final Path currentFolderPath = process.getCurrentFolderPath();
                test.assertNotNull(currentFolderPath);
                test.assertTrue(currentFolderPath.isRooted());
                test.assertTrue(process.getFileSystem().folderExists(currentFolderPath).awaitError());
            });

            runner.test("setCurrentFolderPath(Path) with null", (Test test) ->
            {
                final Process process = creator.run();
                process.setCurrentFolderPath(null);
                test.assertNull(process.getCurrentFolderPath());
            });

            runner.test("getCurrentFolder()", (Test test) ->
            {
                final Process process = creator.run();
                final Folder currentFolder = process.getCurrentFolder().awaitError();
                test.assertNotNull(currentFolder);
                test.assertTrue(currentFolder.exists().awaitError());
            });

            runner.testGroup("getEnvironmentVariable()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    test.assertNull(process.getEnvironmentVariable(null));
                    test.assertNull(process.getEnvironmentVariable(null));
                });

                runner.test("with empty string", (Test test) ->
                {
                    final Process process = creator.run();
                    test.assertNull(process.getEnvironmentVariable(""));
                });

                runner.test("with non-existing variable name", (Test test) ->
                {
                    final Process process = creator.run();
                    test.assertNull(process.getEnvironmentVariable("Can't find me"));
                });

                runner.test("with existing variable name", (Test test) ->
                {
                    final Process process = creator.run();
                    final String path = process.getEnvironmentVariable("path");
                    test.assertNotNull(path);
                    test.assertFalse(path.isEmpty());
                });
            });

            runner.test("setEnvironmentVariables()", (Test test) ->
            {
                final Process process = creator.run();
                final Map<String,String> envVars = new ListMap<>();
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
                    process.setClock(new ManualClock(test.getMainAsyncRunner(), DateTime.date(123, 4, 5)));
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
                        test.assertThrows(() -> process.getProcessBuilder((String)null));
                    }
                });

                runner.test("with empty string", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertThrows(() -> process.getProcessBuilder(""));
                    }
                });

                runner.test("with path with file extension relative to the current folder", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("pom.xml").awaitError();
                        test.assertNotNull(builder);
                        test.assertEqual(process.getCurrentFolder().awaitError().getFile("pom.xml").awaitError(), builder.getExecutableFile());
                        test.assertEqual(0, builder.getArgumentCount());
                    }
                });

                runner.test("with path without file extension relative to the current folder", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("pom").awaitError();
                        test.assertNotNull(builder, "The builder not have been null.");
                        test.assertEqual(process.getCurrentFolder().awaitError().getFile("pom.xml").awaitError(), builder.getExecutableFile());
                        test.assertEqual(0, builder.getArgumentCount());
                    }
                });

                runner.test("with rooted path with file extension", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final Path executablePath = process.getCurrentFolder().awaitError().getFile("pom.xml").awaitError().getPath();
                        final ProcessBuilder builder = process.getProcessBuilder(executablePath).awaitError();
                        test.assertNotNull(builder);
                        test.assertEqual(process.getCurrentFolder().awaitError().getFile("pom.xml").awaitError(), builder.getExecutableFile());
                        test.assertEqual(0, builder.getArgumentCount());
                    }
                });

                runner.test("with rooted path without file extension", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final Path executablePath = process.getCurrentFolder().awaitError().getFile("pom").awaitError().getPath();
                        final ProcessBuilder builder = process.getProcessBuilder(executablePath).awaitError();
                        test.assertNotNull(builder);
                        test.assertEqual(process.getCurrentFolder().awaitError().getFile("pom.xml").awaitError(), builder.getExecutableFile());
                        test.assertEqual(0, builder.getArgumentCount());
                    }
                });

                runner.test("with rooted path to executable", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("C:/Program Files/Java/jdk1.8.0_192/bin/javac.exe").awaitError();
                            test.assertEqual("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
                            test.assertEqual(0, builder.getArgumentCount());
                            test.assertEqual(2, builder.run().awaitError());
                        }
                    }
                });

                runner.test("with path with file extension relative to PATH environment variable", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac.exe").awaitError();
                            test.assertEqual("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
                            test.assertEqual(0, builder.getArgumentCount());
                            test.assertEqual(2, builder.run().awaitError());
                        }
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac").awaitError();
                            test.assertTrue(builder.getExecutableFile().getPath().getSegments().last().contains("javac"));
                            test.assertEqual(0, builder.getArgumentCount());

                            final StringBuilder output = new StringBuilder();
                            builder.redirectOutputTo(output);
                            builder.redirectErrorTo(output);

                            test.assertEqual(2, builder.run().awaitError());

                            final String outputString = output.toString();
                            test.assertNotNullAndNotEmpty(outputString);
                            test.assertTrue(outputString.contains("javac <options> <source files>"), "Process output (" + outputString + ") should have contained \"javac <options> <source files>\".");
                            test.assertTrue(outputString.contains("Terminate compilation if warnings occur"), "Process output (" + outputString + ") should have contained \"Terminate compilation if warnings occur\".");
                        }
                    }
                });

                runner.test("with non-existing working folder", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("javac").awaitError();
                        final StringBuilder output = new StringBuilder();
                        builder.redirectOutputTo(output);
                        builder.redirectErrorTo(output);

                        final Folder workingFolder = test.getProcess().getCurrentFolder()
                            .thenResult((Folder currentFolder) -> currentFolder.getFolder("I don't exist"))
                            .awaitError();
                        builder.setWorkingFolder(workingFolder);
                        test.assertThrows(() -> builder.run().awaitError(),
                            new RuntimeException(
                                new java.io.IOException("Cannot run program \"C:/Program Files/Java/jdk-11.0.1/bin/javac.exe\" (in directory \"C:\\Users\\dansc\\Sources\\qub-java\\I don't exist\"): CreateProcess error=267, The directory name is invalid",
                                    new java.io.IOException("CreateProcess error=267, The directory name is invalid"))));
                        test.assertEqual("", output.toString());
                    }
                });

                runner.test("with existing working folder (a child folder of the current folder)", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("javac").awaitError();
                        final StringBuilder output = new StringBuilder();
                        builder.redirectOutputTo(output);
                        builder.redirectErrorTo(output);

                        final Folder workingFolder = test.getProcess().getCurrentFolder()
                            .thenResult((Folder currentFolder) -> currentFolder.createFolder("temp"))
                            .awaitError();
                        try
                        {
                            builder.setWorkingFolder(workingFolder);
                            test.assertEqual(2, builder.run().awaitError());

                            final String outputString = output.toString();
                            test.assertNotNullAndNotEmpty(outputString);
                            test.assertContains(outputString, "javac <options> <source files>");
                            test.assertContains(outputString, "Terminate compilation if warnings occur");
                        }
                        finally
                        {
                            workingFolder.delete().awaitError();
                        }
                    }
                });

                runner.test("with existing working folder (the current folder)", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("javac").awaitError();

                        final StringBuilder output = new StringBuilder();
                        builder.redirectOutputTo(output);
                        builder.redirectErrorTo(output);

                        final Folder workingFolder = test.getProcess().getCurrentFolder().awaitError();
                        builder.setWorkingFolder(workingFolder);
                        test.assertEqual(2, builder.run().awaitError());

                        final String outputString = output.toString();
                        test.assertNotNullAndNotEmpty(outputString);
                        test.assertContains(outputString, "javac <options> <source files>");
                        test.assertContains(outputString, "Terminate compilation if warnings occur");
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable with redirected output", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("qub").awaitError();
                            test.assertTrue(builder.getExecutableFile().getPath().getSegments().last().contains("qub."));
                            test.assertEqual(0, builder.getArgumentCount());

                            final InMemoryByteStream output = new InMemoryByteStream();
                            builder.redirectOutput(output);

                            test.assertEqual(0, builder.run().awaitError());

                            final String outputString = new String(output.getBytes());
                            test.assertNotNullAndNotEmpty(outputString);
                            test.assertTrue(outputString.contains("qub <action> [<action-options>]"), "Process output (" + outputString + ") should have contained \"qub <action> [<action-options>]\".");
                        }
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable with redirected error", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac").awaitError();
                            builder.addArgument("notfound.java");

                            final InMemoryByteStream error = new InMemoryByteStream();
                            builder.redirectError(error);

                            test.assertEqual(2, builder.run().awaitError());

                            final String errorString = new String(error.getBytes());
                            test.assertContains(errorString, "file not found: notfound.java");
                        }
                    }
                });
            });

            runner.test("createWindow()", (Test test) ->
            {
                Window window;
                try (final Process process = creator.run())
                {
                    window = process.createWindow();
                    test.assertNotNull(window);
                    test.assertFalse(window.isDisposed());
                }
                test.assertTrue(window.isDisposed());
            });
        });
    }
}
