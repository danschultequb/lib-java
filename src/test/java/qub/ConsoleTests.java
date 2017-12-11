package qub;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ConsoleTests
{
    @Test
    public void constructorWithNoArguments()
    {
        final Console console = new Console();
        assertArrayEquals(null, console.getCommandLineArgumentStrings());
    }

    @Test
    public void constructorWithNullStringArray()
    {
        final Console console = new Console(null);
        assertNull(console.getCommandLineArgumentStrings());
        assertSame(null, console.getCommandLineArgumentStrings());
        assertNotNull(console.getCommandLine());
    }

    @Test
    public void constructorWithEmptyStringArray()
    {
        final String[] commandLineArgumentStrings = new String[0];
        final Console console = new Console(commandLineArgumentStrings);
        assertArrayEquals(new String[0], console.getCommandLineArgumentStrings());
        assertSame(commandLineArgumentStrings, console.getCommandLineArgumentStrings());
        assertNotNull(console.getCommandLine());
    }

    @Test
    public void constructorWithNonEmptyStringArray()
    {
        final String[] commandLineArgumentStrings = Array.toStringArray("a", "b", "c");
        final Console console = new Console(commandLineArgumentStrings);
        assertArrayEquals(new String[] { "a", "b", "c" }, console.getCommandLineArgumentStrings());
        assertNotSame(commandLineArgumentStrings, console.getCommandLineArgumentStrings());
        assertNotNull(console.getCommandLine());
    }

    @Test
    public void getEncoding()
    {
        final Console console = new Console();
        assertEquals(CharacterEncoding.UTF_8, console.getCharacterEncoding());

        console.setCharacterEncoding(CharacterEncoding.US_ASCII);
        assertEquals(CharacterEncoding.US_ASCII, console.getCharacterEncoding());

        console.setCharacterEncoding(null);
        assertEquals(null, console.getCharacterEncoding());
    }

    @Test
    public void getLineSeparator()
    {
        final Console console = new Console();
        assertEquals("\n", console.getLineSeparator());

        console.setLineSeparator("\r\n");
        assertEquals("\r\n", console.getLineSeparator());

        console.setLineSeparator("abc");
        assertEquals("abc", console.getLineSeparator());

        console.setLineSeparator("");
        assertEquals("", console.getLineSeparator());

        console.setLineSeparator(null);
        assertEquals(null, console.getLineSeparator());
    }

    @Test
    public void getIncludeNewLines()
    {
        final Console console = new Console();
        assertFalse(console.getIncludeNewLines());

        console.setIncludeNewLines(true);
        assertTrue(console.getIncludeNewLines());
    }

    @Test
    public void getOutputAsByteWriteStream()
    {
        final Console console = new Console();
        final ByteWriteStream writeStream = console.getOutputAsByteWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void setOutputWithNullByteWriteStream()
    {
        final Console console = new Console();
        console.setOutput((ByteWriteStream)null);

        console.write((byte)50);
        console.write(new byte[]{51});
        console.write(new byte[]{52}, 0, 1);
        console.write("hello");
        console.writeLine("there!");
    }

    @Test
    public void setOutputWithNonNullBytewriteStream()
    {
        final Console console = new Console();
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        console.setOutput(byteWriteStream);

        console.write((byte)50);
        assertArrayEquals(new byte[]{50}, byteWriteStream.getBytes());

        console.write(new byte[]{51});
        assertArrayEquals(new byte[]{50, 51}, byteWriteStream.getBytes());

        console.write(new byte[]{52}, 0, 1);
        assertArrayEquals(new byte[]{50, 51, 52}, byteWriteStream.getBytes());

        console.write("hello");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, byteWriteStream.getBytes());

        console.writeLine();
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, byteWriteStream.getBytes());

        console.writeLine("there!");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, byteWriteStream.getBytes());
    }

    @Test
    public void getOutputAsCharacterWriteStream()
    {
        final Console console = new Console();
        final CharacterWriteStream writeStream = console.getOutputAsCharacterWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void setOutputWithNullCharacterWriteStream()
    {
        final Console console = new Console();
        console.setOutput((CharacterWriteStream)null);

        console.write((byte)50);
        console.write(new byte[]{51});
        console.write(new byte[]{52}, 0, 1);
        console.write("hello");
        console.writeLine("there!");
    }

    @Test
    public void setOutputWithNonNullCharacterWriteStream()
    {
        final Console console = new Console();
        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream();
        console.setOutput(characterWriteStream);

        console.write((byte)50);
        assertArrayEquals(new byte[]{50}, characterWriteStream.getBytes());

        console.write(new byte[]{51});
        assertArrayEquals(new byte[]{50, 51}, characterWriteStream.getBytes());

        console.write(new byte[]{52}, 0, 1);
        assertArrayEquals(new byte[]{50, 51, 52}, characterWriteStream.getBytes());

        console.write("hello");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, characterWriteStream.getBytes());

        console.writeLine();
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, characterWriteStream.getBytes());

        console.writeLine("there!");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, characterWriteStream.getBytes());
    }

    @Test
    public void getOutputAsLineWriteStream()
    {
        final Console console = new Console();
        final LineWriteStream writeStream = console.getOutputAsLineWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void setOutputWithNullLineWriteStream()
    {
        final Console console = new Console();
        console.setOutput((LineWriteStream)null);

        console.write((byte)50);
        console.write(new byte[]{51});
        console.write(new byte[]{52}, 0, 1);
        console.write("hello");
        console.writeLine("there!");
    }

    @Test
    public void setOutputWithNonNullLineWriteStream()
    {
        final Console console = new Console();
        final InMemoryLineWriteStream lineWriteStream = new InMemoryLineWriteStream();
        console.setOutput(lineWriteStream);

        console.write((byte)50);
        assertArrayEquals(new byte[]{50}, lineWriteStream.getBytes());

        console.write(new byte[]{51});
        assertArrayEquals(new byte[]{50, 51}, lineWriteStream.getBytes());

        console.write(new byte[]{52}, 0, 1);
        assertArrayEquals(new byte[]{50, 51, 52}, lineWriteStream.getBytes());

        console.write('a');
        assertArrayEquals(new byte[]{50, 51, 52, 97}, lineWriteStream.getBytes());

        console.write("hello");
        assertArrayEquals(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111}, lineWriteStream.getBytes());

        console.writeLine();
        assertArrayEquals(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111, 10 }, lineWriteStream.getBytes());

        console.writeLine("there!");
        assertArrayEquals(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, lineWriteStream.getBytes());
    }

    @Test
    public void getErrorAsByteWriteStream()
    {
        final Console console = new Console();
        final ByteWriteStream writeStream = console.getErrorAsByteWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void setErrorWithNullByteWriteStream()
    {
        final Console console = new Console();
        console.setError((ByteWriteStream)null);

        console.writeError((byte)50);
        console.writeError(new byte[]{51});
        console.writeError(new byte[]{52}, 0, 1);
        console.writeError("hello");
        console.writeErrorLine("there!");
    }

    @Test
    public void setErrorWithNonNullBytewriteStream()
    {
        final Console console = new Console();
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        console.setError(byteWriteStream);

        console.writeError((byte)50);
        assertArrayEquals(new byte[]{50}, byteWriteStream.getBytes());

        console.writeError(new byte[]{51});
        assertArrayEquals(new byte[]{50, 51}, byteWriteStream.getBytes());

        console.writeError(new byte[]{52}, 0, 1);
        assertArrayEquals(new byte[]{50, 51, 52}, byteWriteStream.getBytes());

        console.writeError("hello");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, byteWriteStream.getBytes());

        console.writeErrorLine();
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, byteWriteStream.getBytes());

        console.writeErrorLine("there!");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, byteWriteStream.getBytes());
    }

    @Test
    public void getErrorAsCharacterWriteStream()
    {
        final Console console = new Console();
        final CharacterWriteStream writeStream = console.getErrorAsCharacterWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void setErrorWithNullCharacterWriteStream()
    {
        final Console console = new Console();
        console.setError((CharacterWriteStream)null);

        console.writeError((byte)50);
        console.writeError(new byte[]{51});
        console.writeError(new byte[]{52}, 0, 1);
        console.writeError("hello");
        console.writeErrorLine("there!");
    }

    @Test
    public void setErrorWithNonNullCharacterWriteStream()
    {
        final Console console = new Console();
        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream();
        console.setError(characterWriteStream);

        console.writeError((byte)50);
        assertArrayEquals(new byte[]{50}, characterWriteStream.getBytes());

        console.writeError(new byte[]{51});
        assertArrayEquals(new byte[]{50, 51}, characterWriteStream.getBytes());

        console.writeError(new byte[]{52}, 0, 1);
        assertArrayEquals(new byte[]{50, 51, 52}, characterWriteStream.getBytes());

        console.writeError("hello");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, characterWriteStream.getBytes());

        console.writeErrorLine();
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, characterWriteStream.getBytes());

        console.writeErrorLine("there!");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, characterWriteStream.getBytes());
    }

    @Test
    public void getErrorAsLineWriteStream()
    {
        final Console console = new Console();
        final LineWriteStream writeStream = console.getErrorAsLineWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void setErrorWithNullLineWriteStream()
    {
        final Console console = new Console();
        console.setError((LineWriteStream)null);

        console.writeError((byte)50);
        console.writeError(new byte[]{51});
        console.writeError(new byte[]{52}, 0, 1);
        console.writeError("hello");
        console.writeErrorLine("there!");
    }

    @Test
    public void setErrorWithNonNullLineWriteStream()
    {
        final Console console = new Console();
        final InMemoryLineWriteStream lineWriteStream = new InMemoryLineWriteStream();
        console.setError(lineWriteStream);

        console.writeError((byte)50);
        assertArrayEquals(new byte[]{50}, lineWriteStream.getBytes());

        console.writeError(new byte[]{51});
        assertArrayEquals(new byte[]{50, 51}, lineWriteStream.getBytes());

        console.writeError(new byte[]{52}, 0, 1);
        assertArrayEquals(new byte[]{50, 51, 52}, lineWriteStream.getBytes());

        console.writeError('a');
        assertArrayEquals(new byte[]{50, 51, 52, 97}, lineWriteStream.getBytes());

        console.writeError("hello");
        assertArrayEquals(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111}, lineWriteStream.getBytes());

        console.writeErrorLine();
        assertArrayEquals(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111, 10 }, lineWriteStream.getBytes());

        console.writeErrorLine("there!");
        assertArrayEquals(new byte[]{50, 51, 52, 97, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, lineWriteStream.getBytes());
    }

    @Test
    public void asByteReadStream()
    {
        final Console console = new Console();

        final ByteReadStream readStream = console.asByteReadStream();
        assertNotNull(readStream);
    }

    @Test
    public void setByteReadStreamWithNull() throws IOException
    {
        final Console console = new Console();
        console.setByteReadStream(null);
        assertNull(console.asByteReadStream());
    }

    @Test
    public void setByteReadStreamWithNonNull() throws IOException
    {
        final Console console = new Console();
        final InMemoryCharacterReadStream readStream = new InMemoryCharacterReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
        console.setByteReadStream(readStream.asByteReadStream());

        final ByteReadStream byteReadStream = console.asByteReadStream();
        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

        final byte[] byteBuffer = new byte[2];
        assertEquals(2, byteReadStream.readBytes(byteBuffer));
        assertArrayEquals(new byte[] { 32, 116 }, byteBuffer);

        assertEquals(1, byteReadStream.readBytes(byteBuffer, 1, 1));
        assertArrayEquals(new byte[] { 32, 104 }, byteBuffer);

        final CharacterReadStream characterReadStream = console.asCharacterReadStream();
        assertArrayEquals(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

        final char[] characterBuffer = new char[4];
        assertEquals(4, characterReadStream.readCharacters(characterBuffer));
        assertArrayEquals(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

        assertEquals(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
        assertArrayEquals(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

        assertEquals("od fr", characterReadStream.readString(5));

        assertEquals("iend", console.readLine());

        assertEquals("How are you?\r\n", console.readLine(true));

        assertEquals("I'm alright.", console.readLine(false));
    }

    @Test
    public void asCharacterReadStream()
    {
        final Console console = new Console();

        final CharacterReadStream readStream = console.asCharacterReadStream();
        assertNotNull(readStream);
    }

    @Test
    public void setCharacterReadStreamWithNull() throws IOException
    {
        final Console console = new Console();
        console.setCharacterReadStream(null);
        assertNull(console.asCharacterReadStream());
    }

    @Test
    public void setCharacterReadStreamWithNonNull() throws IOException
    {
        final Console console = new Console();
        final InMemoryCharacterReadStream readStream = new InMemoryCharacterReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
        console.setCharacterReadStream(readStream);

        final ByteReadStream byteReadStream = console.asByteReadStream();
        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

        final byte[] byteBuffer = new byte[2];
        assertEquals(2, byteReadStream.readBytes(byteBuffer));
        assertArrayEquals(new byte[] { 32, 116 }, byteBuffer);

        assertEquals(1, byteReadStream.readBytes(byteBuffer, 1, 1));
        assertArrayEquals(new byte[] { 32, 104 }, byteBuffer);

        final CharacterReadStream characterReadStream = console.asCharacterReadStream();
        assertArrayEquals(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

        final char[] characterBuffer = new char[4];
        assertEquals(4, characterReadStream.readCharacters(characterBuffer));
        assertArrayEquals(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

        assertEquals(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
        assertArrayEquals(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

        assertEquals("od fr", characterReadStream.readString(5));

        assertEquals("iend", console.readLine());

        assertEquals("How are you?\r\n", console.readLine(true));

        assertEquals("I'm alright.", console.readLine(false));
    }

    @Test
    public void asLineReadStream()
    {
        final Console console = new Console();

        final LineReadStream readStream = console.asLineReadStream();
        assertNotNull(readStream);
    }

    @Test
    public void setLineReadStreamWithNull() throws IOException
    {
        final Console console = new Console();
        console.setLineReadStream(null);
        assertNull(console.asLineReadStream());
    }

    @Test
    public void setLineReadStreamWithNonNull() throws IOException
    {
        final Console console = new Console();
        final InMemoryLineReadStream readStream = new InMemoryLineReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
        console.setLineReadStream(readStream);

        final ByteReadStream byteReadStream = console.asByteReadStream();
        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

        final byte[] byteBuffer = new byte[2];
        assertEquals(2, byteReadStream.readBytes(byteBuffer));
        assertArrayEquals(new byte[] { 32, 116 }, byteBuffer);

        assertEquals(1, byteReadStream.readBytes(byteBuffer, 1, 1));
        assertArrayEquals(new byte[] { 32, 104 }, byteBuffer);

        final CharacterReadStream characterReadStream = console.asCharacterReadStream();
        assertArrayEquals(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

        final char[] characterBuffer = new char[4];
        assertEquals(4, characterReadStream.readCharacters(characterBuffer));
        assertArrayEquals(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

        assertEquals(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
        assertArrayEquals(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

        assertEquals("od fr", characterReadStream.readString(5));

        assertEquals("iend", console.readLine());

        assertEquals("How are you?\r\n", console.readLine(true));

        assertEquals("I'm alright.", console.readLine(false));
    }

    @Test
    public void getRandom()
    {
        final Console console = new Console();
        final Random random = console.getRandom();
        assertNotNull(random);
        assertTrue(random instanceof JavaRandom);
        assertSame(random, console.getRandom());
    }

    @Test
    public void setRandom()
    {
        final Console console = new Console();

        console.setRandom(null);
        assertNull(console.getRandom());

        final FixedRandom random = new FixedRandom(1);
        console.setRandom(random);
        assertSame(random, console.getRandom());
    }

    @Test
    public void getFileSystem()
    {
        final Console console = new Console();
        final FileSystem defaultFileSystem = console.getFileSystem();
        assertNotNull(defaultFileSystem);
        assertTrue(defaultFileSystem instanceof JavaFileSystem);
    }

    @Test
    public void setFileSystemWithNull()
    {
        final Console console = new Console();
        console.setFileSystem(null);
        assertNull(console.getFileSystem());
        assertNull(console.getCurrentFolderPathString());
    }

    @Test
    public void setFileSystemWithNonNull()
    {
        final Console console = new Console();
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        console.setFileSystem(fileSystem);
        assertSame(fileSystem, console.getFileSystem());
    }

    @Test
    public void getCurrentFolderPathString()
    {
        final Console console = new Console();
        final String currentFolderPathString = console.getCurrentFolderPathString();
        assertNotNull(currentFolderPathString);
        assertFalse(currentFolderPathString.isEmpty());
        assertTrue(console.getFileSystem().folderExists(currentFolderPathString));
    }

    @Test
    public void setCurrentFolderPathStringWithNull()
    {
        final Console console = new Console();
        console.setCurrentFolderPathString(null);
        assertNull(console.getCurrentFolderPathString());
    }

    @Test
    public void setCurrentFolderPathStringWithEmpty()
    {
        final Console console = new Console();
        console.setCurrentFolderPathString("");
        assertEquals("", console.getCurrentFolderPathString());
    }

    @Test
    public void setCurrentFolderPathStringWithRelativePath()
    {
        final Console console = new Console();
        console.setCurrentFolderPathString("hello there");
        assertEquals("hello there", console.getCurrentFolderPathString());
    }

    @Test
    public void getCurrentFolderPath()
    {
        final Console console = new Console();
        final Path currentFolderPath = console.getCurrentFolderPath();
        assertNotNull(currentFolderPath);
        assertFalse(currentFolderPath.isEmpty());
        assertTrue(currentFolderPath.isRooted());
        assertTrue(console.getFileSystem().folderExists(currentFolderPath));
    }

    @Test
    public void setCurrentFolderPathWithNull()
    {
        final Console console = new Console();
        console.setCurrentFolderPath(null);
        assertNull(console.getCurrentFolderPath());
    }

    @Test
    public void getCurrentFolder()
    {
        final Console console = new Console();
        final Folder currentFolder = console.getCurrentFolder();
        assertNotNull(currentFolder);
        assertTrue(currentFolder.exists());
    }

    @Test
    public void getEnvironmentVariableWithNull()
    {
        final Console console = new Console();
        assertNull(console.getEnvironmentVariable(null));
        assertNull(console.getEnvironmentVariable(null));
    }

    @Test
    public void getEnvironmentVariableWithEmpty()
    {
        final Console console = new Console();
        assertNull(console.getEnvironmentVariable(""));
    }

    @Test
    public void getEnvironmentVariableWithNotFound()
    {
        final Console console = new Console();
        assertNull(console.getEnvironmentVariable("Can't find me"));
    }

    @Test
    public void getEnvironmentVariableWithFound()
    {
        final Console console = new Console();
        final String path = console.getEnvironmentVariable("path");
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    @Test
    public void setEnvironmentVariables()
    {
        final Console console = new Console();
        final Map<String,String> envVars = new ListMap<>();
        console.setEnvironmentVariables(envVars);
        assertSame(envVars, console.getEnvironmentVariables());
    }

    @Test
    public void getProcessBuilderWithNullString()
    {
        final Console console = new Console();
        assertNull(console.getProcessBuilder((String)null));
    }

    @Test
    public void getProcessBuilderWithEmptyString()
    {
        final Console console = new Console();
        assertNull(console.getProcessBuilder(""));
    }

    @Test
    public void getProcessBuilderWithCurrentFolderRelativePathWithFileExtension()
    {
        final Console console = new Console();
        final ProcessBuilder builder = console.getProcessBuilder("pom.xml");
        assertNotNull(builder);
        assertEquals(console.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void getProcessBuilderWithCurrentFolderRelativePathWithoutFileExtension()
    {
        final Console console = new Console();
        final ProcessBuilder builder = console.getProcessBuilder("pom");
        assertNotNull(builder);
        assertEquals(console.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void getProcessBuilderWithRootedPathWithFileExtension()
    {
        final Console console = new Console();
        final Path executablePath = console.getCurrentFolder().getFile("pom.xml").getPath();
        final ProcessBuilder builder = console.getProcessBuilder(executablePath);
        assertNotNull(builder);
        assertEquals(console.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void getProcessBuilderWithRootedPathWithoutFileExtension()
    {
        final Console console = new Console();
        final Path executablePath = console.getCurrentFolder().getFile("pom").getPath();
        final ProcessBuilder builder = console.getProcessBuilder(executablePath);
        assertNotNull(builder);
        assertEquals(console.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void getProcessBuilderWithExactPathToFile()
    {
        final Console console = new Console();
        if (console.onWindows())
        {
            final ProcessBuilder builder = console.getProcessBuilder("C:/Program Files/Java/jdk-9/bin/javac.exe");
            assertEquals("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
            assertEquals(0, builder.getArgumentCount());
            assertEquals(2, builder.run().intValue());
        }
    }

    @Test
    public void getProcessBuilderWithFileNameWithExtension()
    {
        final Console console = new Console();
        if (console.onWindows())
        {
            final ProcessBuilder builder = console.getProcessBuilder("javac.exe");
            assertEquals("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
            assertEquals(0, builder.getArgumentCount());
            assertEquals(2, builder.run().intValue());
        }
    }

    @Test
    public void getProcessBuilderWithFileNameWithoutExtension()
    {
        final Console console = new Console();
        if (console.onWindows())
        {
            final ProcessBuilder builder = console.getProcessBuilder("javac");
            assertTrue(builder.getExecutableFile().getPath().getSegments().last().contains("javac"));
            assertEquals(0, builder.getArgumentCount());

            final StringBuilder output = builder.redirectOutput();

            assertEquals(2, builder.run().intValue());

            final String outputString = output.toString();
            assertTrue(outputString.contains("javac <options> <source files>"));
            assertTrue(outputString.contains("Terminate compilation if warnings occur"));
        }
    }

    @Test
    public void getProcessBuilderWithFileNameWithoutExtensionWithErrorLines()
    {
        final Console console = new Console();
        if (console.onWindows())
        {
            final ProcessBuilder builder = console.getProcessBuilder("javac");
            builder.addArgument("notfound.java");

            final StringBuilder error = builder.redirectError();

            assertEquals(2, builder.run().intValue());

            final String errorString = error.toString();
            assertTrue(errorString.contains("file not found: notfound.java"));
        }
    }

    @Test
    public void getProcessBuilderWithRedirectedOutput()
    {
        final Console console = new Console();
        if (console.onWindows())
        {
            final ProcessBuilder builder = console.getProcessBuilder("javac");
            assertTrue(builder.getExecutableFile().getPath().getSegments().last().contains("javac"));
            assertEquals(0, builder.getArgumentCount());

            final InMemoryByteWriteStream output = new InMemoryByteWriteStream();
            builder.redirectOutput(output);

            assertEquals(2, builder.run().intValue());

            final String outputString = new String(output.getBytes());
            assertTrue(outputString.contains("javac <options> <source files>"));
            assertTrue(outputString.contains("Terminate compilation if warnings occur"));
        }
    }

    @Test
    public void getProcessBuilderWithRedirectedError()
    {
        final Console console = new Console();
        if (console.onWindows())
        {
            final ProcessBuilder builder = console.getProcessBuilder("javac");
            builder.addArgument("notfound.java");

            final InMemoryByteWriteStream error = new InMemoryByteWriteStream();
            builder.redirectError(error);

            assertEquals(2, builder.run().intValue());

            final String errorString = new String(error.getBytes());
            assertTrue(errorString.contains("file not found: notfound.java"));
        }
    }
}
