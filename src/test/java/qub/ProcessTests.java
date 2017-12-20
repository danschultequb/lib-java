package qub;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ProcessTests
{
    @Test
    public void constructorWithNoArguments()
    {
        final Process process = new Process();
        assertArrayEquals(null, process.getCommandLineArgumentStrings());
    }

    @Test
    public void constructorWithNullStringArray()
    {
        final Process process = new Process(null);
        assertNull(process.getCommandLineArgumentStrings());
        assertSame(null, process.getCommandLineArgumentStrings());
        assertNotNull(process.getCommandLine());
    }

    @Test
    public void constructorWithEmptyStringArray()
    {
        final String[] commandLineArgumentStrings = new String[0];
        final Process process = new Process(commandLineArgumentStrings);
        assertArrayEquals(new String[0], process.getCommandLineArgumentStrings());
        assertSame(commandLineArgumentStrings, process.getCommandLineArgumentStrings());
        assertNotNull(process.getCommandLine());
    }

    @Test
    public void constructorWithNonEmptyStringArray()
    {
        final String[] commandLineArgumentStrings = Array.toStringArray("a", "b", "c");
        final Process process = new Process(commandLineArgumentStrings);
        assertArrayEquals(new String[] { "a", "b", "c" }, process.getCommandLineArgumentStrings());
        assertNotSame(commandLineArgumentStrings, process.getCommandLineArgumentStrings());
        assertNotNull(process.getCommandLine());
    }

    @Test
    public void getEncoding()
    {
        final Process process = new Process();
        assertEquals(CharacterEncoding.UTF_8, process.getCharacterEncoding());

        process.setCharacterEncoding(CharacterEncoding.US_ASCII);
        assertEquals(CharacterEncoding.US_ASCII, process.getCharacterEncoding());

        process.setCharacterEncoding(null);
        assertEquals(null, process.getCharacterEncoding());
    }

    @Test
    public void getLineSeparator()
    {
        final Process process = new Process();
        assertEquals("\n", process.getLineSeparator());

        process.setLineSeparator("\r\n");
        assertEquals("\r\n", process.getLineSeparator());

        process.setLineSeparator("abc");
        assertEquals("abc", process.getLineSeparator());

        process.setLineSeparator("");
        assertEquals("", process.getLineSeparator());

        process.setLineSeparator(null);
        assertEquals(null, process.getLineSeparator());
    }

    @Test
    public void getIncludeNewLines()
    {
        final Process process = new Process();
        assertFalse(process.getIncludeNewLines());

        process.setIncludeNewLines(true);
        assertTrue(process.getIncludeNewLines());
    }

    @Test
    public void getOutputAsByteWriteStream()
    {
        final Process process = new Process();
        final ByteWriteStream writeStream = process.getOutputAsByteWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void getOutputAsCharacterWriteStream()
    {
        final Process process = new Process();
        final CharacterWriteStream writeStream = process.getOutputAsCharacterWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void getOutputAsLineWriteStream()
    {
        final Process process = new Process();
        final LineWriteStream writeStream = process.getOutputAsLineWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void getErrorAsByteWriteStream()
    {
        final Process process = new Process();
        final ByteWriteStream writeStream = process.getErrorAsByteWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void getErrorAsCharacterWriteStream()
    {
        final Process process = new Process();
        final CharacterWriteStream writeStream = process.getErrorAsCharacterWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void getErrorAsLineWriteStream()
    {
        final Process process = new Process();
        final LineWriteStream writeStream = process.getErrorAsLineWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void asByteReadStream()
    {
        final Process process = new Process();

        final ByteReadStream readStream = process.getInputAsByteReadStream();
        assertNotNull(readStream);
    }

    @Test
    public void setInputWithNullByteReadStream() throws IOException
    {
        final Process process = new Process();
        process.setInput((ByteReadStream)null);
        assertNull(process.getInputAsByteReadStream());
    }

    @Test
    public void setInputWithNonNullByteReadStream() throws IOException
    {
        final Process process = new Process();
        final InMemoryCharacterReadStream readStream = new InMemoryCharacterReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
        process.setInput(readStream.asByteReadStream());

        final ByteReadStream byteReadStream = process.getInputAsByteReadStream();
        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

        final byte[] byteBuffer = new byte[2];
        assertEquals(2, byteReadStream.readBytes(byteBuffer));
        assertArrayEquals(new byte[] { 32, 116 }, byteBuffer);

        assertEquals(1, byteReadStream.readBytes(byteBuffer, 1, 1));
        assertArrayEquals(new byte[] { 32, 104 }, byteBuffer);

        final CharacterReadStream characterReadStream = process.getInputAsCharacterReadStream();
        assertArrayEquals(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

        final char[] characterBuffer = new char[4];
        assertEquals(4, characterReadStream.readCharacters(characterBuffer));
        assertArrayEquals(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

        assertEquals(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
        assertArrayEquals(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

        assertEquals("od fr", characterReadStream.readString(5));
    }

    @Test
    public void getInputAsCharacterReadStream()
    {
        final Process process = new Process();

        final CharacterReadStream readStream = process.getInputAsCharacterReadStream();
        assertNotNull(readStream);
    }

    @Test
    public void setInputWithNullCharacterReadStream() throws IOException
    {
        final Process process = new Process();
        process.setInput((CharacterReadStream)null);
        assertNull(process.getInputAsCharacterReadStream());
    }

    @Test
    public void setInputWithNonNullCharacterReadStream() throws IOException
    {
        final Process process = new Process();
        final InMemoryCharacterReadStream readStream = new InMemoryCharacterReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
        process.setInput(readStream);

        final ByteReadStream byteReadStream = process.getInputAsByteReadStream();
        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

        final byte[] byteBuffer = new byte[2];
        assertEquals(2, byteReadStream.readBytes(byteBuffer));
        assertArrayEquals(new byte[] { 32, 116 }, byteBuffer);

        assertEquals(1, byteReadStream.readBytes(byteBuffer, 1, 1));
        assertArrayEquals(new byte[] { 32, 104 }, byteBuffer);

        final CharacterReadStream characterReadStream = process.getInputAsCharacterReadStream();
        assertArrayEquals(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

        final char[] characterBuffer = new char[4];
        assertEquals(4, characterReadStream.readCharacters(characterBuffer));
        assertArrayEquals(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

        assertEquals(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
        assertArrayEquals(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

        assertEquals("od fr", characterReadStream.readString(5));
    }

    @Test
    public void asLineReadStream()
    {
        final Process process = new Process();

        final LineReadStream readStream = process.getInputAsLineReadStream();
        assertNotNull(readStream);
    }

    @Test
    public void setInputWithNullLineReadStream() throws IOException
    {
        final Process process = new Process();
        process.setInput((LineReadStream)null);
        assertNull(process.getInputAsLineReadStream());
    }

    @Test
    public void setLineReadStreamWithNonNull() throws IOException
    {
        final Process process = new Process();
        final InMemoryLineReadStream readStream = new InMemoryLineReadStream("hello there my good friend\nHow are you?\r\nI'm alright.");
        process.setInput(readStream);

        final ByteReadStream byteReadStream = process.getInputAsByteReadStream();
        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111 }, byteReadStream.readBytes(5));

        final byte[] byteBuffer = new byte[2];
        assertEquals(2, byteReadStream.readBytes(byteBuffer));
        assertArrayEquals(new byte[] { 32, 116 }, byteBuffer);

        assertEquals(1, byteReadStream.readBytes(byteBuffer, 1, 1));
        assertArrayEquals(new byte[] { 32, 104 }, byteBuffer);

        final CharacterReadStream characterReadStream = process.getInputAsCharacterReadStream();
        assertArrayEquals(new char[] { 'e', 'r', 'e' }, characterReadStream.readCharacters(3));

        final char[] characterBuffer = new char[4];
        assertEquals(4, characterReadStream.readCharacters(characterBuffer));
        assertArrayEquals(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

        assertEquals(2, characterReadStream.readCharacters(characterBuffer, 0, 2));
        assertArrayEquals(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

        assertEquals("od fr", characterReadStream.readString(5));
    }

    @Test
    public void getRandom()
    {
        final Process process = new Process();
        final Random random = process.getRandom();
        assertNotNull(random);
        assertTrue(random instanceof JavaRandom);
        assertSame(random, process.getRandom());
    }

    @Test
    public void setRandom()
    {
        final Process process = new Process();

        process.setRandom(null);
        assertNull(process.getRandom());

        final FixedRandom random = new FixedRandom(1);
        process.setRandom(random);
        assertSame(random, process.getRandom());
    }

    @Test
    public void getFileSystem()
    {
        final Process process = new Process();
        final FileSystem defaultFileSystem = process.getFileSystem();
        assertNotNull(defaultFileSystem);
        assertTrue(defaultFileSystem instanceof JavaFileSystem);
    }

    @Test
    public void setFileSystemWithNull()
    {
        final Process process = new Process();
        process.setFileSystem(null);
        assertNull(process.getFileSystem());
        assertNull(process.getCurrentFolderPathString());
    }

    @Test
    public void setFileSystemWithNonNull()
    {
        final Process process = new Process();
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem();
        process.setFileSystem(fileSystem);
        assertSame(fileSystem, process.getFileSystem());
    }

    @Test
    public void getCurrentFolderPathString()
    {
        final Process process = new Process();
        final String currentFolderPathString = process.getCurrentFolderPathString();
        assertNotNull(currentFolderPathString);
        assertFalse(currentFolderPathString.isEmpty());
        assertTrue(process.getFileSystem().folderExists(currentFolderPathString));
    }

    @Test
    public void setCurrentFolderPathStringWithNull()
    {
        final Process process = new Process();
        process.setCurrentFolderPathString(null);
        assertNull(process.getCurrentFolderPathString());
    }

    @Test
    public void setCurrentFolderPathStringWithEmpty()
    {
        final Process process = new Process();
        process.setCurrentFolderPathString("");
        assertEquals("", process.getCurrentFolderPathString());
    }

    @Test
    public void setCurrentFolderPathStringWithRelativePath()
    {
        final Process process = new Process();
        process.setCurrentFolderPathString("hello there");
        assertEquals("hello there", process.getCurrentFolderPathString());
    }

    @Test
    public void getCurrentFolderPath()
    {
        final Process process = new Process();
        final Path currentFolderPath = process.getCurrentFolderPath();
        assertNotNull(currentFolderPath);
        assertFalse(currentFolderPath.isEmpty());
        assertTrue(currentFolderPath.isRooted());
        assertTrue(process.getFileSystem().folderExists(currentFolderPath));
    }

    @Test
    public void setCurrentFolderPathWithNull()
    {
        final Process process = new Process();
        process.setCurrentFolderPath(null);
        assertNull(process.getCurrentFolderPath());
    }

    @Test
    public void getCurrentFolder()
    {
        final Process process = new Process();
        final Folder currentFolder = process.getCurrentFolder();
        assertNotNull(currentFolder);
        assertTrue(currentFolder.exists());
    }

    @Test
    public void getEnvironmentVariableWithNull()
    {
        final Process process = new Process();
        assertNull(process.getEnvironmentVariable(null));
        assertNull(process.getEnvironmentVariable(null));
    }

    @Test
    public void getEnvironmentVariableWithEmpty()
    {
        final Process process = new Process();
        assertNull(process.getEnvironmentVariable(""));
    }

    @Test
    public void getEnvironmentVariableWithNotFound()
    {
        final Process process = new Process();
        assertNull(process.getEnvironmentVariable("Can't find me"));
    }

    @Test
    public void getEnvironmentVariableWithFound()
    {
        final Process process = new Process();
        final String path = process.getEnvironmentVariable("path");
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    @Test
    public void setEnvironmentVariables()
    {
        final Process process = new Process();
        final Map<String,String> envVars = new ListMap<>();
        process.setEnvironmentVariables(envVars);
        assertSame(envVars, process.getEnvironmentVariables());
    }

    @Test
    public void getProcessBuilderWithNullString()
    {
        final Process process = new Process();
        assertNull(process.getProcessBuilder((String)null));
    }

    @Test
    public void getProcessBuilderWithEmptyString()
    {
        final Process process = new Process();
        assertNull(process.getProcessBuilder(""));
    }

    @Test
    public void getProcessBuilderWithCurrentFolderRelativePathWithFileExtension()
    {
        final Process process = new Process();
        final ProcessBuilder builder = process.getProcessBuilder("pom.xml");
        assertNotNull(builder);
        assertEquals(process.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void getProcessBuilderWithCurrentFolderRelativePathWithoutFileExtension()
    {
        final Process process = new Process();
        final ProcessBuilder builder = process.getProcessBuilder("pom");
        assertNotNull(builder);
        assertEquals(process.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void getProcessBuilderWithRootedPathWithFileExtension()
    {
        final Process process = new Process();
        final Path executablePath = process.getCurrentFolder().getFile("pom.xml").getPath();
        final ProcessBuilder builder = process.getProcessBuilder(executablePath);
        assertNotNull(builder);
        assertEquals(process.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void getProcessBuilderWithRootedPathWithoutFileExtension()
    {
        final Process process = new Process();
        final Path executablePath = process.getCurrentFolder().getFile("pom").getPath();
        final ProcessBuilder builder = process.getProcessBuilder(executablePath);
        assertNotNull(builder);
        assertEquals(process.getCurrentFolder().getFile("pom.xml"), builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void getProcessBuilderWithExactPathToFile()
    {
        final Process process = new Process();
        if (process.onWindows())
        {
            final ProcessBuilder builder = process.getProcessBuilder("C:/Program Files/Java/jdk-9/bin/javac.exe");
            assertEquals("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
            assertEquals(0, builder.getArgumentCount());
            assertEquals(2, builder.run().intValue());
        }
    }

    @Test
    public void getProcessBuilderWithFileNameWithExtension()
    {
        final Process process = new Process();
        if (process.onWindows())
        {
            final ProcessBuilder builder = process.getProcessBuilder("javac.exe");
            assertEquals("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
            assertEquals(0, builder.getArgumentCount());
            assertEquals(2, builder.run().intValue());
        }
    }

    @Test
    public void getProcessBuilderWithFileNameWithoutExtension()
    {
        final Process process = new Process();
        if (process.onWindows())
        {
            final ProcessBuilder builder = process.getProcessBuilder("javac");
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
        final Process process = new Process();
        if (process.onWindows())
        {
            final ProcessBuilder builder = process.getProcessBuilder("javac");
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
        final Process process = new Process();
        if (process.onWindows())
        {
            final ProcessBuilder builder = process.getProcessBuilder("javac");
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
        final Process process = new Process();
        if (process.onWindows())
        {
            final ProcessBuilder builder = process.getProcessBuilder("javac");
            builder.addArgument("notfound.java");

            final InMemoryByteWriteStream error = new InMemoryByteWriteStream();
            builder.redirectError(error);

            assertEquals(2, builder.run().intValue());

            final String errorString = new String(error.getBytes());
            assertTrue(errorString.contains("file not found: notfound.java"));
        }
    }
}
