package qub;

public class ConsoleTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Console", () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final Console console = new Console();
                    test.assertEqual(new Array<String>(0), console.getCommandLineArgumentStrings());
                });
                
                runner.test("with null String[]", (Test test) ->
                {
                    final Console console = new Console(null);
                    test.assertEqual(new Array<String>(0), console.getCommandLineArgumentStrings());
                    test.assertNotNull(console.getCommandLine());
                });
                
                runner.test("with empty String[]", (Test test) ->
                {
                    final String[] commandLineArgumentStrings = new String[0];
                    final Console console = new Console(commandLineArgumentStrings);
                    test.assertEqual(new Array<String>(0), console.getCommandLineArgumentStrings());
                    test.assertNotNull(console.getCommandLine());
                });
                
                runner.test("with non-empty String[]", (Test test) ->
                {
                    final String[] commandLineArgumentStrings = Array.toStringArray("a", "b", "c");
                    final Console console = new Console(commandLineArgumentStrings);
                    test.assertEqual(Array.fromValues(new String[] { "a", "b", "c" }), console.getCommandLineArgumentStrings());
                    test.assertNotNull(console.getCommandLine());
                });
            });
            
            runner.test("getEncoding()", (Test test) ->
            {
                final Console console = new Console();
                test.assertEqual(CharacterEncoding.UTF_8, console.getCharacterEncoding());

                console.setCharacterEncoding(CharacterEncoding.US_ASCII);
                test.assertEqual(CharacterEncoding.US_ASCII, console.getCharacterEncoding());

                console.setCharacterEncoding(null);
                test.assertEqual(null, console.getCharacterEncoding());
            });

            runner.test("getLineSeparator()", (Test test) ->
            {
                final Console console = new Console();
                test.assertEqual("\n", console.getLineSeparator());

                console.setLineSeparator("\r\n");
                test.assertEqual("\r\n", console.getLineSeparator());

                console.setLineSeparator("abc");
                test.assertEqual("abc", console.getLineSeparator());

                console.setLineSeparator("");
                test.assertEqual("", console.getLineSeparator());

                console.setLineSeparator(null);
                test.assertEqual(null, console.getLineSeparator());
            });
            
            runner.test("getIncludeNewLines()", (Test test) ->
            {
                final Console console = new Console();
                test.assertFalse(console.getIncludeNewLines());

                console.setIncludeNewLines(true);
                test.assertTrue(console.getIncludeNewLines());
            });
            
            runner.test("getOutputAsByteWriteStream()", (Test test) ->
            {
                final Console console = new Console();
                final ByteWriteStream writeStream = console.getOutputAsByteWriteStream();
                test.assertNotNull(writeStream);
            });
            
            runner.testGroup("setOutput(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setOutput((ByteWriteStream)null);

                    console.write((byte)50);
                    console.write(new byte[]{51});
                    console.write(new byte[]{52}, 0, 1);
                    console.write("hello");
                    console.writeLine("there!");
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                    console.setOutput(byteWriteStream);

                    console.write((byte)50);
                    test.assertEqual(new byte[]{50}, byteWriteStream.getBytes());

                    console.write(new byte[]{51});
                    test.assertEqual(new byte[]{50, 51}, byteWriteStream.getBytes());

                    console.write(new byte[]{52}, 0, 1);
                    test.assertEqual(new byte[]{50, 51, 52}, byteWriteStream.getBytes());

                    console.write("hello");
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, byteWriteStream.getBytes());

                    console.writeLine();
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, byteWriteStream.getBytes());

                    console.writeLine("there!");
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, byteWriteStream.getBytes());
                });
            });
            
            runner.test("getOutputAsCharacterWriteStream()", (Test test) ->
            {
                final Console console = new Console();
                final CharacterWriteStream writeStream = console.getOutputAsCharacterWriteStream();
                test.assertNotNull(writeStream);
            });
            
            runner.testGroup("setOutput(CharacterWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setOutput((CharacterWriteStream)null);

                    console.write((byte)50);
                    console.write(new byte[]{51});
                    console.write(new byte[]{52}, 0, 1);
                    console.write("hello");
                    console.writeLine("there!");
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream();
                    console.setOutput(characterWriteStream);

                    console.write((byte)50);
                    test.assertEqual(new byte[]{50}, characterWriteStream.getBytes());

                    console.write(new byte[]{51});
                    test.assertEqual(new byte[]{50, 51}, characterWriteStream.getBytes());

                    console.write(new byte[]{52}, 0, 1);
                    test.assertEqual(new byte[]{50, 51, 52}, characterWriteStream.getBytes());

                    console.write("hello");
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, characterWriteStream.getBytes());

                    console.writeLine();
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, characterWriteStream.getBytes());

                    console.writeLine("there!");
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, characterWriteStream.getBytes());
                });
            });
            
            runner.test("getOutputAsLineWriteStream()", (Test test) ->
            {
                final Console console = new Console();
                final LineWriteStream writeStream = console.getOutputAsLineWriteStream();
                test.assertNotNull(writeStream);
            });
            
            runner.testGroup("setOutput(LineWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setOutput((LineWriteStream)null);

                    console.write((byte)50);
                    console.write(new byte[]{51});
                    console.write(new byte[]{52}, 0, 1);
                    console.write("hello");
                    console.writeLine("there!");
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryLineWriteStream lineWriteStream = new InMemoryLineWriteStream();
                    console.setOutput(lineWriteStream);

                    console.write((byte)50);
                    test.assertEqual(new byte[]{50}, lineWriteStream.getBytes());

                    console.write(new byte[]{51});
                    test.assertEqual(new byte[]{50, 51}, lineWriteStream.getBytes());

                    console.write(new byte[]{52}, 0, 1);
                    test.assertEqual(new byte[]{50, 51, 52}, lineWriteStream.getBytes());

                    console.write('a');
                    test.assertEqual(new byte[]{50, 51, 52, 97}, lineWriteStream.getBytes());

                    console.write("hello");
                    test.assertEqual(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111}, lineWriteStream.getBytes());

                    console.writeLine();
                    test.assertEqual(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111, 10 }, lineWriteStream.getBytes());

                    console.writeLine("there!");
                    test.assertEqual(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, lineWriteStream.getBytes());
                });
            });
            
            runner.test("getErrorAsByteWriteStream()", (Test test) ->
            {
                final Console console = new Console();
                final ByteWriteStream writeStream = console.getErrorAsByteWriteStream();
                test.assertNotNull(writeStream);
            });
            
            runner.testGroup("setError(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setError((ByteWriteStream)null);

                    console.writeError((byte)50);
                    console.writeError(new byte[]{51});
                    console.writeError(new byte[]{52}, 0, 1);
                    console.writeError("hello");
                    console.writeErrorLine("there!");
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                    console.setError(byteWriteStream);

                    console.writeError((byte)50);
                    test.assertEqual(new byte[]{50}, byteWriteStream.getBytes());

                    console.writeError(new byte[]{51});
                    test.assertEqual(new byte[]{50, 51}, byteWriteStream.getBytes());

                    console.writeError(new byte[]{52}, 0, 1);
                    test.assertEqual(new byte[]{50, 51, 52}, byteWriteStream.getBytes());

                    console.writeError("hello");
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, byteWriteStream.getBytes());

                    console.writeErrorLine();
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, byteWriteStream.getBytes());

                    console.writeErrorLine("there!");
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, byteWriteStream.getBytes());
                });
            });
            
            runner.test("getErrorAsCharacterWriteStream()", (Test test) ->
            {
                final Console console = new Console();
                final CharacterWriteStream writeStream = console.getErrorAsCharacterWriteStream();
                test.assertNotNull(writeStream);
            });
            
            runner.testGroup("setError(CharacterWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setError((CharacterWriteStream)null);

                    console.writeError((byte)50);
                    console.writeError(new byte[]{51});
                    console.writeError(new byte[]{52}, 0, 1);
                    console.writeError("hello");
                    console.writeErrorLine("there!");
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream();
                    console.setError(characterWriteStream);

                    console.writeError((byte)50);
                    test.assertEqual(new byte[]{50}, characterWriteStream.getBytes());

                    console.writeError(new byte[]{51});
                    test.assertEqual(new byte[]{50, 51}, characterWriteStream.getBytes());

                    console.writeError(new byte[]{52}, 0, 1);
                    test.assertEqual(new byte[]{50, 51, 52}, characterWriteStream.getBytes());

                    console.writeError("hello");
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, characterWriteStream.getBytes());

                    console.writeErrorLine();
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, characterWriteStream.getBytes());

                    console.writeErrorLine("there!");
                    test.assertEqual(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, characterWriteStream.getBytes());
                });
            });
            
            runner.test("getErrorAsLineWriteStream()", (Test test) ->
            {
                final Console console = new Console();
                final LineWriteStream writeStream = console.getErrorAsLineWriteStream();
                test.assertNotNull(writeStream);
            });
            
            runner.testGroup("setError(LineWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setError((LineWriteStream)null);

                    console.writeError((byte)50);
                    console.writeError(new byte[]{51});
                    console.writeError(new byte[]{52}, 0, 1);
                    console.writeError("hello");
                    console.writeErrorLine("there!");
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryLineWriteStream lineWriteStream = new InMemoryLineWriteStream();
                    console.setError(lineWriteStream);

                    console.writeError((byte)50);
                    test.assertEqual(new byte[]{50}, lineWriteStream.getBytes());

                    console.writeError(new byte[]{51});
                    test.assertEqual(new byte[]{50, 51}, lineWriteStream.getBytes());

                    console.writeError(new byte[]{52}, 0, 1);
                    test.assertEqual(new byte[]{50, 51, 52}, lineWriteStream.getBytes());

                    console.writeError('a');
                    test.assertEqual(new byte[]{50, 51, 52, 97}, lineWriteStream.getBytes());

                    console.writeError("hello");
                    test.assertEqual(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111}, lineWriteStream.getBytes());

                    console.writeErrorLine();
                    test.assertEqual(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111, 10 }, lineWriteStream.getBytes());

                    console.writeErrorLine("there!");
                    test.assertEqual(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, lineWriteStream.getBytes());
                });
            });
            
            runner.test("getInputAsByteReadStream()", (Test test) ->
            {
                final Console console = new Console();

                final ByteReadStream readStream = console.getInputAsByteReadStream();
                test.assertNotNull(readStream);
            });
            
            runner.testGroup("setInput(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setInput((ByteReadStream)null);
                    test.assertNull(console.getInputAsByteReadStream());
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryCharacterReadStream readStream = new InMemoryCharacterReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
                    console.setInput(readStream.asByteReadStream());

                    final ByteReadStream byteReadStream = console.getInputAsByteReadStream();
                    test.assertEqual(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

                    final byte[] byteBuffer = new byte[2];
                    test.assertEqual(2, byteReadStream.readBytes(byteBuffer));
                    test.assertEqual(new byte[] { 32, 116 }, byteBuffer);

                    test.assertEqual(1, byteReadStream.readBytes(byteBuffer, 1, 1));
                    test.assertEqual(new byte[] { 32, 104 }, byteBuffer);

                    final CharacterReadStream characterReadStream = console.getInputAsCharacterReadStream();
                    test.assertEqual(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, characterReadStream.readCharacters(characterBuffer));
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", characterReadStream.readString(5));

                    test.assertEqual("iend", console.readLine());

                    test.assertEqual("How are you?\r\n", console.readLine(true));

                    test.assertEqual("I'm alright.", console.readLine(false));
                });
            });
            
            runner.test("getInputAsCharacterReadStream()", (Test test) ->
            {
                final Console console = new Console();
                final CharacterReadStream readStream = console.getInputAsCharacterReadStream();
                test.assertNotNull(readStream);
            });
            
            runner.testGroup("setInput(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setInput((CharacterReadStream)null);
                    test.assertNull(console.getInputAsCharacterReadStream());
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryCharacterReadStream readStream = new InMemoryCharacterReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
                    console.setInput(readStream);

                    final ByteReadStream byteReadStream = console.getInputAsByteReadStream();
                    test.assertEqual(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

                    final byte[] byteBuffer = new byte[2];
                    test.assertEqual(2, byteReadStream.readBytes(byteBuffer));
                    test.assertEqual(new byte[] { 32, 116 }, byteBuffer);

                    test.assertEqual(1, byteReadStream.readBytes(byteBuffer, 1, 1));
                    test.assertEqual(new byte[] { 32, 104 }, byteBuffer);

                    final CharacterReadStream characterReadStream = console.getInputAsCharacterReadStream();
                    test.assertEqual(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, characterReadStream.readCharacters(characterBuffer));
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", characterReadStream.readString(5));

                    test.assertEqual("iend", console.readLine());

                    test.assertEqual("How are you?\r\n", console.readLine(true));

                    test.assertEqual("I'm alright.", console.readLine(false));
                });
            });
            
            runner.test("getInputAsLineReadStream()", (Test test) ->
            {
                final Console console = new Console();
                final LineReadStream readStream = console.getInputAsLineReadStream();
                test.assertNotNull(readStream);
            });
            
            runner.testGroup("setInput(LineReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setInput((LineReadStream)null);
                    test.assertNull(console.getInputAsLineReadStream());
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryLineReadStream readStream = new InMemoryLineReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
                    console.setInput(readStream);

                    final ByteReadStream byteReadStream = console.getInputAsByteReadStream();
                    test.assertEqual(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

                    final byte[] byteBuffer = new byte[2];
                    test.assertEqual(2, byteReadStream.readBytes(byteBuffer));
                    test.assertEqual(new byte[] { 32, 116 }, byteBuffer);

                    test.assertEqual(1, byteReadStream.readBytes(byteBuffer, 1, 1));
                    test.assertEqual(new byte[] { 32, 104 }, byteBuffer);

                    final CharacterReadStream characterReadStream = console.getInputAsCharacterReadStream();
                    test.assertEqual(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

                    final char[] characterBuffer = new char[4];
                    test.assertEqual(4, characterReadStream.readCharacters(characterBuffer));
                    test.assertEqual(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

                    test.assertEqual(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
                    test.assertEqual(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

                    test.assertEqual("od fr", characterReadStream.readString(5));

                    test.assertEqual("iend", console.readLine());

                    test.assertEqual("How are you?\r\n", console.readLine(true));

                    test.assertEqual("I'm alright.", console.readLine(false));
                });
            });
            
            runner.test("getRandom()", (Test test) ->
            {
                final Console console = new Console();
                final Random random = console.getRandom();
                test.assertNotNull(random);
                test.assertTrue(random instanceof JavaRandom);
                test.assertSame(random, console.getRandom());
            });
            
            runner.test("setRandom()", (Test test) ->
            {
                final Console console = new Console();

                console.setRandom(null);
                test.assertNull(console.getRandom());

                final FixedRandom random = new FixedRandom(1);
                console.setRandom(random);
                test.assertSame(random, console.getRandom());
            });
            
            runner.test("getFileSystem()", (Test test) ->
            {
                final Console console = new Console();
                final FileSystem defaultFileSystem = console.getFileSystem();
                test.assertNotNull(defaultFileSystem);
                test.assertTrue(defaultFileSystem instanceof JavaFileSystem);
            });
            
            runner.testGroup("setFileSystem()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setFileSystem(null);
                    test.assertNull(console.getFileSystem());
                    test.assertNull(console.getCurrentFolderPathString());
                });
                
                runner.test("with non-null", (Test test) ->
                {
                    final Console console = new Console();
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
                    console.setFileSystem(fileSystem);
                    test.assertSame(fileSystem, console.getFileSystem());
                });
            });
            
            runner.test("getCurrentFolderPathString()", (Test test) ->
            {
                final Console console = new Console();
                final String currentFolderPathString = console.getCurrentFolderPathString();
                test.assertNotNull(currentFolderPathString);
                test.assertFalse(currentFolderPathString.isEmpty());
                test.assertTrue(console.getFileSystem().folderExists(currentFolderPathString));
            });
            
            runner.testGroup("setCurrentFolderPathString()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setCurrentFolderPathString(null);
                    test.assertNull(console.getCurrentFolderPathString());
                });
                
                runner.test("with empty", (Test test) ->
                {
                    final Console console = new Console();
                    console.setCurrentFolderPathString("");
                    test.assertEqual("", console.getCurrentFolderPathString());
                });
                
                runner.test("with relative path", (Test test) ->
                {
                    final Console console = new Console();
                    console.setCurrentFolderPathString("hello there");
                    test.assertEqual("hello there", console.getCurrentFolderPathString());
                });
            });
            
            runner.test("getCurrentFolderPath()", (Test test) ->
            {
                final Console console = new Console();
                final Path currentFolderPath = console.getCurrentFolderPath();
                test.assertNotNull(currentFolderPath);
                test.assertFalse(currentFolderPath.isEmpty());
                test.assertTrue(currentFolderPath.isRooted());
                test.assertTrue(console.getFileSystem().folderExists(currentFolderPath));
            });
            
            runner.testGroup("setCurrentFolderPath()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    console.setCurrentFolderPath(null);
                    test.assertNull(console.getCurrentFolderPath());
                });
            });
            
            runner.test("getCurrentFolder()", (Test test) ->
            {
                final Console console = new Console();
                final Folder currentFolder = console.getCurrentFolder();
                test.assertNotNull(currentFolder);
                test.assertTrue(currentFolder.exists());
            });
            
            runner.testGroup("getEnvironmentVariable()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Console console = new Console();
                    test.assertNull(console.getEnvironmentVariable(null));
                    test.assertNull(console.getEnvironmentVariable(null));
                });
                
                runner.test("with empty", (Test test) ->
                {
                    final Console console = new Console();
                    test.assertNull(console.getEnvironmentVariable(""));
                });
                
                runner.test("with not found variable name", (Test test) ->
                {
                    final Console console = new Console();
                    test.assertNull(console.getEnvironmentVariable("Can't find me"));
                });
                
                runner.test("with found variable name", (Test test) ->
                {
                    final Console console = new Console();
                    final String path = console.getEnvironmentVariable("path");
                    test.assertNotNull(path);
                    test.assertNotEqual("", path);
                });
            });
            
            runner.test("setEnvironmentVariables()", (Test test) ->
            {
                final Console console = new Console();
                final Map<String,String> envVars = new ListMap<>();
                console.setEnvironmentVariables(envVars);
                test.assertSame(envVars, console.getEnvironmentVariables());
            });
            
            runner.testGroup("getProcessBuilder()", () ->
            {
                runner.test("with null String", (Test test) ->
                {
                    final Console console = new Console();
                    test.assertNull(console.getProcessBuilder((String)null));
                });
                
                runner.test("with empty String", (Test test) ->
                {
                    final Console console = new Console();
                    test.assertNull(console.getProcessBuilder(""));
                });
                
                runner.test("with relative path with file extension", (Test test) ->
                {
                    final Console console = new Console();
                    final ProcessBuilder builder = console.getProcessBuilder("pom.xml");
                    test.assertNotNull(builder);
                    test.assertEqual(console.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
                    test.assertEqual(0, builder.getArgumentCount());
                });
                
                runner.test("with relative path without file extension", (Test test) ->
                {
                    final Console console = new Console();
                    final ProcessBuilder builder = console.getProcessBuilder("pom");
                    test.assertNotNull(builder);
                    test.assertEqual(console.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
                    test.assertEqual(0, builder.getArgumentCount());
                });
                
                runner.test("with rooted path with file extension", (Test test) ->
                {
                    final Console console = new Console();
                    final Path executablePath = console.getCurrentFolder().getFile("pom.xml").getPath();
                    final ProcessBuilder builder = console.getProcessBuilder(executablePath);
                    test.assertNotNull(builder);
                    test.assertEqual(console.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
                    test.assertEqual(0, builder.getArgumentCount());
                });
                
                runner.test("with rooted path without file extension", (Test test) ->
                {
                    final Console console = new Console();
                    final Path executablePath = console.getCurrentFolder().getFile("pom").getPath();
                    final ProcessBuilder builder = console.getProcessBuilder(executablePath);
                    test.assertNotNull(builder);
                    test.assertEqual(console.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
                    test.assertEqual(0, builder.getArgumentCount());
                });
                
                runner.test("with rooted path to file and run()", (Test test) ->
                {
                    final Console console = new Console();
                    if (console.onWindows())
                    {
                        final ProcessBuilder builder = console.getProcessBuilder("C:/qub/oracle/jdk/1.9/bin/javac.exe");
                        test.assertEqual("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
                        test.assertEqual(0, builder.getArgumentCount());
                        test.assertEqual(2, builder.run());
                    }
                });

                runner.test("with file name with extension and run()", (Test test) ->
                {
                    final Console console = new Console();
                    if (console.onWindows())
                    {
                        final ProcessBuilder builder = console.getProcessBuilder("javac.exe");
                        test.assertEqual("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
                        test.assertEqual(0, builder.getArgumentCount());
                        test.assertEqual(2, builder.run());
                    }
                });

                runner.test("with file name without extension and run()", (Test test) ->
                {
                    final Console console = new Console();
                    if (console.onWindows())
                    {
                        final ProcessBuilder builder = console.getProcessBuilder("javac");
                        test.assertTrue(builder.getExecutableFile().getPath().getSegments().last().contains("javac"));
                        test.assertEqual(0, builder.getArgumentCount());

                        final StringBuilder output = builder.redirectOutput();

                        test.assertEqual(2, builder.run());

                        final String outputString = output.toString();
                        test.assertTrue(outputString.contains("javac <options> <source files>"));
                        test.assertTrue(outputString.contains("Terminate compilation if warnings occur"));
                    }
                });

                runner.test("with file name without extension and run() with error lines", (Test test) ->
                {
                    final Console console = new Console();
                    if (console.onWindows())
                    {
                        final ProcessBuilder builder = console.getProcessBuilder("javac");
                        builder.addArgument("notfound.java");

                        final StringBuilder error = builder.redirectError();

                        test.assertEqual(2, builder.run());

                        final String errorString = error.toString();
                        test.assertTrue(errorString.contains("file not found: notfound.java"));
                    }
                });

                runner.test("with redirected output", (Test test) ->
                {
                    final Console console = new Console();
                    if (console.onWindows())
                    {
                        final ProcessBuilder builder = console.getProcessBuilder("javac");
                        test.assertTrue(builder.getExecutableFile().getPath().getSegments().last().contains("javac"));
                        test.assertEqual(0, builder.getArgumentCount());

                        final InMemoryByteWriteStream output = new InMemoryByteWriteStream();
                        builder.redirectOutput(output);

                        test.assertEqual(2, builder.run());

                        final String outputString = new String(output.getBytes());
                        test.assertTrue(outputString.contains("javac <options> <source files>"));
                        test.assertTrue(outputString.contains("Terminate compilation if warnings occur"));
                    }
                });

                runner.test("with redirected error", (Test test) ->
                {
                    final Console console = new Console();
                    if (console.onWindows())
                    {
                        final ProcessBuilder builder = console.getProcessBuilder("javac");
                        builder.addArgument("notfound.java");

                        final InMemoryByteWriteStream error = new InMemoryByteWriteStream();
                        builder.redirectError(error);

                        test.assertEqual(2, builder.run());

                        final String errorString = new String(error.getBytes());
                        test.assertTrue(errorString.contains("file not found: notfound.java"));
                    }
                });
            });
        });
    }
}
