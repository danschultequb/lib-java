package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessBuilderTests
{
    @Test
    public void constructor()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        assertNull(builder.getExecutableFile());
        assertEquals(0, builder.getArgumentCount());
        assertEquals("", builder.getCommand());
    }

    @Test
    public void addArgumentWithNull()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.addArgument(null);
        assertEquals(0, builder.getArgumentCount());
        assertEquals("", builder.getCommand());
    }

    @Test
    public void addArgumentWithEmpty()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.addArgument("");
        assertEquals(0, builder.getArgumentCount());
        assertEquals("", builder.getCommand());
    }

    @Test
    public void addArgumentWithNonEmpty()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.addArgument("test");
        assertEquals(1, builder.getArgumentCount());
        assertEquals("test", builder.getArgument(0));
        assertEquals("test", builder.getCommand());
    }

    @Test
    public void addArgumentsWithNoValues()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.addArguments();
        assertEquals(0, builder.getArgumentCount());
        assertEquals("", builder.getCommand());
    }

    @Test
    public void addArgumentsWithOneNullValue()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.addArguments((String)null);
        assertEquals(0, builder.getArgumentCount());
        assertEquals("", builder.getCommand());
    }

    @Test
    public void setArgumentWithNegativeIndex()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.setArgument(-1, "test");
        assertEquals(0, builder.getArgumentCount());
        assertEquals("", builder.getCommand());
    }

    @Test
    public void setArgumentWithNullValue()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.addArguments("a", "b", "c");
        builder.setArgument(0, null);
        assertEquals(Array.fromValues("b", "c"), builder.getArguments());
        assertEquals("b c", builder.getCommand());
    }

    @Test
    public void setArgumentWithEmptyValue()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.addArguments("a", "b", "c");
        builder.setArgument(2, "");
        assertEquals(Array.fromValues("a", "b", ""), builder.getArguments());
        assertEquals("a b \"\"", builder.getCommand());
    }

    @Test
    public void setArgumentWithNonEmptyValue()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.addArguments("a", "b", "c");
        builder.setArgument(1, "\"d\"");
        assertEquals(Array.fromValues("a", "\"d\"", "c"), builder.getArguments());
        assertEquals("a \"d\" c", builder.getCommand());
    }

    @Test
    public void removeArgumentAt()
    {
        final ProcessBuilder builder = new ProcessBuilder(null, null);
        builder.addArguments("a", "b", "c");
        builder.removeArgument(1);
        assertEquals(Array.fromValues("a", "c"), builder.getArguments());
        assertEquals("a c", builder.getCommand());
    }

    @Test
    public void runWithNotFoundExecutableFile()
    {
        final JavaFileSystem fileSystem = new JavaFileSystem();
        final File javacFile = fileSystem.getFile("C:/idontexist.exe");
        final ProcessBuilder builder = new ProcessBuilder(null, javacFile);
        builder.addArgument("won't matter");
        assertEquals(null, builder.run());
        assertEquals("C:/idontexist.exe \"won't matter\"", builder.getCommand());
    }

    @Test
    public void escapeArgument()
    {
        assertEquals("\"Then he said, \\\"Hey there!\\\"\"", ProcessBuilder.escapeArgument("Then he said, \"Hey there!\""));
        assertEquals("-argument=\"value\"", ProcessBuilder.escapeArgument("-argument=\"value\""));
        assertEquals("\"\"", ProcessBuilder.escapeArgument(""));
    }
}
