package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessBuilderTests
{
    @Test
    public void constructor()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        assertNull(builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void addArgumentWithNull()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.addArgument(null);
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void addArgumentWithEmpty()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.addArgument("");
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void addArgumentWithNonEmpty()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.addArgument("test");
        assertEquals(1, builder.getArgumentCount());
        assertEquals("test", builder.getArgument(0));
    }

    @Test
    public void addArgumentsWithNoValues()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.addArguments();
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void addArgumentsWithOneNullValue()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.addArguments((String)null);
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void setArgumentWithNegativeIndex()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.setArgument(-1, "test");
        assertEquals(0, builder.getArgumentCount());
    }

    @Test
    public void setArgumentWithNullValue()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.addArguments("a", "b", "c");
        builder.setArgument(0, null);
        assertEquals(Array.fromValues("b", "c"), builder.getArguments());
    }

    @Test
    public void setArgumentWithEmptyValue()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.addArguments("a", "b", "c");
        builder.setArgument(2, "");
        assertEquals(Array.fromValues("a", "b"), builder.getArguments());
    }

    @Test
    public void setArgumentWithNonEmptyValue()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.addArguments("a", "b", "c");
        builder.setArgument(1, "d");
        assertEquals(Array.fromValues("a", "d", "c"), builder.getArguments());
    }

    @Test
    public void removeArgumentAt()
    {
        final ProcessBuilder builder = new ProcessBuilder(null);
        builder.addArguments("a", "b", "c");
        builder.removeArgument(1);
        assertEquals(Array.fromValues("a", "c"), builder.getArguments());
    }

    @Test
    @Ignore
    public void runWithNoArguments()
    {
        final JavaFileSystem fileSystem = new JavaFileSystem();
        final File javacFile = fileSystem.getFile("C:/Program Files/Java/jdk-9/bin/javac.exe");
        final ProcessBuilder builder = new ProcessBuilder(javacFile);
        assertEquals(2, builder.run().intValue());
    }

    @Test
    @Ignore
    public void runWithOneArgument()
    {
        final JavaFileSystem fileSystem = new JavaFileSystem();
        final File javacFile = fileSystem.getFile("C:/Program Files/Java/jdk-9/bin/javac.exe");
        final ProcessBuilder builder = new ProcessBuilder(javacFile);
        builder.addArgument("-?");
        assertEquals(2, builder.run().intValue());
    }

    @Test
    public void runWithNotFoundExecutableFile()
    {
        final JavaFileSystem fileSystem = new JavaFileSystem();
        final File javacFile = fileSystem.getFile("C:/idontexist.exe");
        final ProcessBuilder builder = new ProcessBuilder(javacFile);
        builder.addArgument("won't matter");
        assertEquals(null, builder.run());
    }
}
