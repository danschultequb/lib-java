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
    public void asByteWriteStream()
    {
        final Console console = new Console();
        final ByteWriteStream writeStream = console.asByteWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void setByteWriteStreamWithNull()
    {
        final Console console = new Console();
        console.setByteWriteStream(null);

        console.write((byte)50);
        console.write(new byte[]{51});
        console.write(new byte[]{52}, 0, 1);
        console.write("hello");
        console.writeLine("there!");
    }

    @Test
    public void setByteWriteStreamWithNonNull()
    {
        final Console console = new Console();
        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
        console.setByteWriteStream(byteWriteStream);

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
    public void asCharacterWriteStream()
    {
        final Console console = new Console();
        final CharacterWriteStream writeStream = console.asCharacterWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void setCharacterWriteStreamWithNull()
    {
        final Console console = new Console();
        console.setCharacterWriteStream(null);

        console.write((byte)50);
        console.write(new byte[]{51});
        console.write(new byte[]{52}, 0, 1);
        console.write("hello");
        console.writeLine("there!");
    }

    @Test
    public void setCharacterWriteStreamWithNonNull()
    {
        final Console console = new Console();
        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream();
        console.setCharacterWriteStream(characterWriteStream);

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
    public void asLineWriteStream()
    {
        final Console console = new Console();
        final LineWriteStream writeStream = console.asLineWriteStream();
        assertNotNull(writeStream);
    }

    @Test
    public void setLineWriteStreamWithNull()
    {
        final Console console = new Console();
        console.setLineWriteStream(null);

        console.write((byte)50);
        console.write(new byte[]{51});
        console.write(new byte[]{52}, 0, 1);
        console.write("hello");
        console.writeLine("there!");
    }

    @Test
    public void setLineWriteStreamWithNonNull()
    {
        final Console console = new Console();
        final InMemoryLineWriteStream lineWriteStream = new InMemoryLineWriteStream();
        console.setLineWriteStream(lineWriteStream);

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
        final ProcessBuilder builder = console.getProcessBuilder("C:/Program Files/Java/jdk-9/bin/javac.exe");
        assertEquals("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
        assertEquals(0, builder.getArgumentCount());
        assertEquals(2, builder.run().intValue());
    }

    @Test
    public void getProcessBuilderWithFileNameWithExtension()
    {
        final Console console = new Console();
        final ProcessBuilder builder = console.getProcessBuilder("javac.exe");
        assertEquals("javac.exe", builder.getExecutableFile().getPath().getSegments().last());
        assertEquals(0, builder.getArgumentCount());
        assertEquals(2, builder.run().intValue());
    }

    @Test
    public void getProcessBuilderWithFileNameWithoutExtension()
    {
        final Console console = new Console();
        final ProcessBuilder builder = console.getProcessBuilder("javac");
        assertTrue(builder.getExecutableFile().getPath().getSegments().last().contains("javac"));
        assertEquals(0, builder.getArgumentCount());
        assertEquals(2, builder.run().intValue());
    }
}
