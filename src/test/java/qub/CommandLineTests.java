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
        final CommandLine commandLine = new CommandLine((String[])null);
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
    public void getValueWithNullArgumentsAndNullName()
    {
        final CommandLine commandLine = new CommandLine((String[])null);
        assertNull(commandLine.getValue(null));
    }

    @Test
    public void getValueWithNullArgumentsAndEmptyName()
    {
        final CommandLine commandLine = new CommandLine((String[])null);
        assertNull(commandLine.getValue(""));
    }

    @Test
    public void getValueWithNullArgumentsAndNonMatchingName()
    {
        final CommandLine commandLine = new CommandLine((String[])null);
        assertNull(commandLine.getValue("spud"));
    }

    @Test
    public void getValueWithEmptyArgumentsAndNullName()
    {
        final CommandLine commandLine = new CommandLine(new String[0]);
        assertNull(commandLine.getValue(null));
    }

    @Test
    public void getValueWithEmptyArgumentsAndEmptyName()
    {
        final CommandLine commandLine = new CommandLine(new String[0]);
        assertNull(commandLine.getValue(""));
    }

    @Test
    public void getValueWithEmptyArgumentsAndNonMatchingName()
    {
        final CommandLine commandLine = new CommandLine(new String[0]);
        assertNull(commandLine.getValue("spud"));
    }

    @Test
    public void getValueWithTextArgumentsAndNullName()
    {
        final CommandLine commandLine = new CommandLine("hello", "there");
        assertNull(commandLine.getValue(null));
    }

    @Test
    public void getValueWithTextArgumentsAndEmptyName()
    {
        final CommandLine commandLine = new CommandLine("hello", "there");
        assertNull(commandLine.getValue(""));
    }

    @Test
    public void getValueWithTextArgumentsAndNonMatchingName()
    {
        final CommandLine commandLine = new CommandLine("hello", "there");
        assertNull(commandLine.getValue("spud"));
    }

    @Test
    public void getValueWithSingleDashArgumentsAndNullName()
    {
        final CommandLine commandLine = new CommandLine("-hello", "-there");
        assertNull(commandLine.getValue(null));
    }

    @Test
    public void getValueWithSingleDashArgumentsAndEmptyName()
    {
        final CommandLine commandLine = new CommandLine("-hello", "-there");
        assertNull(commandLine.getValue(""));
    }

    @Test
    public void getValueWithSingleDashArgumentsAndNonMatchingName()
    {
        final CommandLine commandLine = new CommandLine("-hello", "-there");
        assertNull(commandLine.getValue("spud"));
    }

    @Test
    public void getValueWithSingleDashArgumentsAndMatchingName()
    {
        final CommandLine commandLine = new CommandLine("-hello", "there");
        final CommandLineArgument argument = commandLine.get("hello");
        assertNotNull(argument);
        assertEquals("-hello", argument.toString());
        assertEquals("hello", argument.getName());
        assertNull(argument.getValue());
    }

    @Test
    public void getValueWithSingleDashEqualsSignAndNullName()
    {
        final CommandLine commandLine = new CommandLine("-name=value");
        assertNull(commandLine.getValue(null));
    }

    @Test
    public void getValueWithSingleDashEqualsSignAndEmptyName()
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
