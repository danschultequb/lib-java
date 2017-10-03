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
        assertTrue(console.isOpen());
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
    public void close()
    {
        final Console console = new Console();
        assertFalse(console.close());
    }

    @Test
    public void getWriteStream()
    {
        final Console console = new Console();
        final TextWriteStream writeStream = console.getWriteStream();
        assertNotNull(writeStream);
        assertTrue(writeStream instanceof StandardOutputTextWriteStream);
    }

    @Test
    public void setWriteStreamWithNull()
    {
        final Console console = new Console();
        console.setWriteStream(null);

        console.write((byte)50);
        console.write(new byte[]{51});
        console.write(new byte[]{52}, 0, 1);
        console.write("hello");
        console.writeLine("there!");
    }

    @Test
    public void setWriteStreamWithNonNull()
    {
        final Console console = new Console();
        final InMemoryTextWriteStream writeStream = new InMemoryTextWriteStream("\n");
        console.setWriteStream(writeStream);

        console.write((byte)50);
        assertArrayEquals(new byte[]{50}, writeStream.getBytes());

        console.write(new byte[]{51});
        assertArrayEquals(new byte[]{50, 51}, writeStream.getBytes());

        console.write(new byte[]{52}, 0, 1);
        assertArrayEquals(new byte[]{50, 51, 52}, writeStream.getBytes());

        console.write("hello");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111}, writeStream.getBytes());

        console.writeLine();
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10 }, writeStream.getBytes());

        console.writeLine("there!");
        assertArrayEquals(new byte[]{50, 51, 52, 104, 101, 108, 108, 111, 10, 116, 104, 101, 114, 101, 33, 10}, writeStream.getBytes());
    }

    @Test
    public void getReadStream()
    {
        final Console console = new Console();

        final TextReadStream readStream = console.getReadStream();
        assertNotNull(readStream);
        assertTrue(readStream instanceof StandardInputTextReadStream);
    }

    @Test
    public void setReadStreamWithNull() throws IOException
    {
        final Console console = new Console();
        console.setReadStream(null);

        assertNull(console.readBytes(5));
        assertEquals(-1, console.readBytes(null));
        assertEquals(-1, console.readBytes(null, 0, 0));
        assertNull(console.readCharacters(7));
        assertEquals(-1, console.readCharacters(null));
        assertEquals(-1, console.readCharacters(null, 0, 0));
        assertNull(console.readString(10));
        assertNull(console.readLine());
        assertNull(console.readLine(false));
    }

    @Test
    public void setReadStreamWithNonNull() throws IOException
    {
        final Console console = new Console();
        final InMemoryTextReadStream readStream = new InMemoryTextReadStream();
        console.setReadStream(readStream);

        readStream.add("hello there my good friend\nHow are you?\r\nI'm alright.");

        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111 }, console.readBytes(5));

        final byte[] byteBuffer = new byte[2];
        assertEquals(2, console.readBytes(byteBuffer));
        assertArrayEquals(new byte[] { 32, 116 }, byteBuffer);

        assertEquals(1, console.readBytes(byteBuffer, 1, 1));
        assertArrayEquals(new byte[] { 32, 104 }, byteBuffer);

        assertArrayEquals(new char[] { 'e', 'r', 'e' }, console.readCharacters(3));

        final char[] characterBuffer = new char[4];
        assertEquals(4, console.readCharacters(characterBuffer));
        assertArrayEquals(new char[] { ' ', 'm', 'y', ' ' }, characterBuffer);

        assertEquals(2, console.readCharacters(characterBuffer, 0, 2));
        assertArrayEquals(new char[] { 'g', 'o', 'y', ' ' }, characterBuffer);

        assertEquals("od fr", console.readString(5));

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
}
