package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTests
{
    @Test
    public void constructorWithNoArguments()
    {
        final CommandLine commandLine = new CommandLine();
        assertArrayEquals(new String[0], commandLine.getArgumentStrings());
        assertFalse(commandLine.any());
        assertEquals(0, commandLine.getCount());
        assertNull(commandLine.get(0));
    }

    @Test
    public void constructorWithNull()
    {
        final CommandLine commandLine = new CommandLine(null);
        assertNull(commandLine.getArgumentStrings());
        assertFalse(commandLine.any());
        assertEquals(0, commandLine.getCount());
        assertNull(commandLine.get(0));
    }

    @Test
    public void constructorWithEmpty()
    {
        final CommandLine commandLine = new CommandLine(new String[0]);
        assertArrayEquals(new String[0], commandLine.getArgumentStrings());
        assertFalse(commandLine.any());
        assertEquals(0, commandLine.getCount());
        assertNull(commandLine.get(0));
    }

    @Test
    public void constructorWithStringsNotInArray()
    {
        final CommandLine commandLine = new CommandLine("a", "b", "c", "d");
        assertArrayEquals(new String[] { "a", "b", "c", "d" }, commandLine.getArgumentStrings());
        assertTrue(commandLine.any());
        assertEquals(4, commandLine.getCount());
        assertNotNull(commandLine.get(0));
        assertArrayEquals(new String[] { "a", "b", "c", "d" }, Array.toStringArray(commandLine.map(new Function1<CommandLineArgument, String>()
        {
            @Override
            public String run(CommandLineArgument arg1)
            {
                return arg1.toString();
            }
        })));
    }

    @Test
    public void getValueWithNullName()
    {
        final CommandLine commandLine = new CommandLine("-name=value");
        assertNull(commandLine.getValue(null));
    }

    @Test
    public void getValueWithEmptyName()
    {
        final CommandLine commandLine = new CommandLine("-name=value");
        assertNull(commandLine.getValue(""));
    }

    @Test
    public void getValueWithSingleDashEqualsSignAndNonMatchingName()
    {
        final CommandLine commandLine = new CommandLine("-name=value");
        assertNull(commandLine.getValue("spud"));
    }

    @Test
    public void getValueWithSingleDashEqualsSignAndMatchingName()
    {
        final CommandLine commandLine = new CommandLine("-name=value");
        assertEquals("value", commandLine.getValue("name"));
    }
}
