package qub;

public class ProcessTests
{
    public static void test(TestRunner runner, Function0<Process> creator)
    {
        runner.testGroup("Process", () ->
        {
            runner.test("getEncoding()", (Test test) ->
            {
                final Process process = creator.run();
                test.assertEqual(CharacterEncoding.UTF_8, process.getCharacterEncoding());

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

            runner.test("getOutputAsByteWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final ByteWriteStream writeStream = process.getOutputAsByteWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getOutputAsCharacterWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final CharacterWriteStream writeStream = process.getOutputAsCharacterWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getOutputAsLineWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final LineWriteStream writeStream = process.getOutputAsLineWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getErrorAsByteWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final ByteWriteStream writeStream = process.getErrorAsByteWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getErrorAsCharacterWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final CharacterWriteStream writeStream = process.getErrorAsCharacterWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getErrorAsLineWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final LineWriteStream writeStream = process.getErrorAsLineWriteStream();
                test.assertNotNull(writeStream);
            });

            runner.test("getInputAsByteWriteStream()", (Test test) ->
            {
                final Process process = creator.run();
                final ByteReadStream readStream = process.getInputAsByteReadStream();
                test.assertNotNull(readStream);
            });

            runner.testGroup("setInput(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setInput((ByteReadStream)null);
                    test.assertNull(process.getInputAsByteReadStream());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    final InMemoryCharacterReadStream readStream = new InMemoryCharacterReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
                    process.setInput(readStream.asByteReadStream());

                    final ByteReadStream byteReadStream = process.getInputAsByteReadStream();
                    test.assertEqual(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

                    final byte[] byteBuffer = new byte[2];
                    test.assertEqual(2, byteReadStream.readBytes(byteBuffer));
                    test.assertEqual(new byte[] { 32, 116 }, byteBuffer);

                    test.assertEqual(1, byteReadStream.readBytes(byteBuffer, 1, 1));
                    test.assertEqual(new byte[] { 32, 104 }, byteBuffer);

                    final CharacterReadStream characterReadStream = process.getInputAsCharacterReadStream();
                    test.assertEqual(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, characterReadStream.readCharacters(characterBuffer));
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", characterReadStream.readString(5));
                });
            });

            runner.test("getInputAsCharacterReadStream()", (Test test) ->
            {
                final Process process = creator.run();
                final CharacterReadStream readStream = process.getInputAsCharacterReadStream();
                test.assertNotNull(readStream);
            });

            runner.testGroup("setInput(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setInput((CharacterReadStream)null);
                    test.assertNull(process.getInputAsCharacterReadStream());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    final InMemoryCharacterReadStream readStream = new InMemoryCharacterReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
                    process.setInput(readStream);

                    final ByteReadStream byteReadStream = process.getInputAsByteReadStream();
                    test.assertEqual(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

                    final byte[] byteBuffer = new byte[2];
                    test.assertEqual(2, byteReadStream.readBytes(byteBuffer));
                    test.assertEqual(new byte[] { 32, 116 }, byteBuffer);

                    test.assertEqual(1, byteReadStream.readBytes(byteBuffer, 1, 1));
                    test.assertEqual(new byte[] { 32, 104 }, byteBuffer);

                    final CharacterReadStream characterReadStream = process.getInputAsCharacterReadStream();
                    test.assertEqual(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, characterReadStream.readCharacters(characterBuffer));
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", characterReadStream.readString(5));
                });
            });

            runner.test("getInputAsLineReadStream()", (Test test) ->
            {
                final Process process = creator.run();
                final LineReadStream readStream = process.getInputAsLineReadStream();
                test.assertNotNull(readStream);
            });

            runner.testGroup("setInput(LineReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run();
                    process.setInput((LineReadStream)null);
                    test.assertNull(process.getInputAsLineReadStream());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    final InMemoryLineReadStream readStream = new InMemoryLineReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
                    process.setInput(readStream);

                    final ByteReadStream byteReadStream = process.getInputAsByteReadStream();
                    test.assertEqual(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

                    final byte[] byteBuffer = new byte[2];
                    test.assertEqual(2, byteReadStream.readBytes(byteBuffer));
                    test.assertEqual(new byte[] { 32, 116 }, byteBuffer);

                    test.assertEqual(1, byteReadStream.readBytes(byteBuffer, 1, 1));
                    test.assertEqual(new byte[] { 32, 104 }, byteBuffer);

                    final CharacterReadStream characterReadStream = process.getInputAsCharacterReadStream();
                    test.assertEqual(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, characterReadStream.readCharacters(characterBuffer));
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", characterReadStream.readString(5));
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
                    process.setFileSystem((FileSystem)null);
                    test.assertNull(process.getFileSystem());
                    test.assertNull(process.getCurrentFolderPathString());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Process process = creator.run();
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    process.setFileSystem(fileSystem);
                    test.assertSame(fileSystem, process.getFileSystem());
                });
            });

            runner.test("getCurrentFolderPathString()", (Test test) ->
            {
                final Process process = creator.run();
                final String currentFolderPathString = process.getCurrentFolderPathString();
                test.assertNotNull(currentFolderPathString);
                test.assertFalse(currentFolderPathString.isEmpty());
                test.assertTrue(process.getFileSystem().folderExists(currentFolderPathString));
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
                test.assertTrue(process.getFileSystem().folderExists(currentFolderPath));
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
                final Folder currentFolder = process.getCurrentFolder();
                test.assertNotNull(currentFolder);
                test.assertTrue(currentFolder.exists());
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
                process.setEnvironmentVariables(envVars);
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

            runner.testGroup("getProcessBuilder(String)", () ->
            {
                runner.test("with null string", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertNull(process.getProcessBuilder((String)null));
                    }
                });

                runner.test("with empty string", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        test.assertNull(process.getProcessBuilder(""));
                    }
                });

                runner.test("with path with file extension relative to the current folder", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("pom.xml");
                        test.assertNotNull(builder);
                        test.assertEqual(process.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
                        test.assertEqual(0, builder.getArgumentCount());
                    }
                });

                runner.test("with path without file extension relative to the current folder", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final ProcessBuilder builder = process.getProcessBuilder("pom");
                        test.assertNotNull(builder);
                        test.assertEqual(process.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
                        test.assertEqual(0, builder.getArgumentCount());
                    }
                });

                runner.test("with rooted path with file extension", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final Path executablePath = process.getCurrentFolder().getFile("pom.xml").getPath();
                        final ProcessBuilder builder = process.getProcessBuilder(executablePath);
                        test.assertNotNull(builder);
                        test.assertEqual(process.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
                        test.assertEqual(0, builder.getArgumentCount());
                    }
                });

                runner.test("with rooted path without file extension", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        final Path executablePath = process.getCurrentFolder().getFile("pom").getPath();
                        final ProcessBuilder builder = process.getProcessBuilder(executablePath);
                        test.assertNotNull(builder);
                        test.assertEqual(process.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
                        test.assertEqual(0, builder.getArgumentCount());
                    }
                });

                runner.test("with rooted path to executable", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("C:/qub/oracle/jdk/1.9/bin/javac.exe");
                            test.assertEqual("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
                            test.assertEqual(0, builder.getArgumentCount());
                            test.assertEqual(2, builder.run());
                        }
                    }
                });

                runner.test("with path with file extension relative to PATH environment variable", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac.exe");
                            test.assertEqual("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
                            test.assertEqual(0, builder.getArgumentCount());
                            test.assertEqual(2, builder.run());
                        }
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac");
                            test.assertTrue(builder.getExecutableFile().getPath().getSegments().last().contains("javac"));
                            test.assertEqual(0, builder.getArgumentCount());

                            final StringBuilder output = builder.redirectOutput();

                            test.assertEqual(2, builder.run());

                            final String outputString = output.toString();
                            test.assertTrue(outputString.contains("javac <options> <source files>"));
                            test.assertTrue(outputString.contains("Terminate compilation if warnings occur"));
                        }
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable with redirected output", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac");
                            test.assertTrue(builder.getExecutableFile().getPath().getSegments().last().contains("javac"));
                            test.assertEqual(0, builder.getArgumentCount());

                            final InMemoryByteWriteStream output = new InMemoryByteWriteStream();
                            builder.redirectOutput(output);

                            test.assertEqual(2, builder.run());

                            final String outputString = new String(output.getBytes());
                            test.assertTrue(outputString.contains("javac <options> <source files>"));
                            test.assertTrue(outputString.contains("Terminate compilation if warnings occur"));
                        }
                    }
                });

                runner.test("with path without file extension relative to PATH environment variable with redirected error", (Test test) ->
                {
                    try (final Process process = creator.run())
                    {
                        if (process.onWindows())
                        {
                            final ProcessBuilder builder = process.getProcessBuilder("javac");
                            builder.addArgument("notfound.java");

                            final InMemoryByteWriteStream error = new InMemoryByteWriteStream();
                            builder.redirectError(error);

                            test.assertEqual(2, builder.run());

                            final String errorString = new String(error.getBytes());
                            test.assertTrue(errorString.contains("file not found: notfound.java"));
                        }
                    }
                });
            });
        });
    }
}
