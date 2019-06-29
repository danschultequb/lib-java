package qub;

/**
 * A parsed command-line argument.
 */
public class CommandLineArgument
{
    private final String name;
    private final String value;

    /**
     * Create a new CommandLineArgument object.
     * @param name The name of the command line argument.
     * @param value The String value of the command line argument.
     */
    public CommandLineArgument(String name, String value)
    {
        PreCondition.assertTrue(!Strings.isNullOrEmpty(name) || !Strings.isNullOrEmpty(value), "!Strings.isNullOrEmpty(name) || !Strings.isNullOrEmpty(value)");

        this.name = name;
        this.value = value;
    }

    /**
     * Get the name of this command line argument.
     * @return The name of this command line argument.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the String value of this command line argument.
     * @return The String value of this command line argument.
     */
    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        String result;

        final boolean hasName = !Strings.isNullOrEmpty(name);
        if (hasName)
        {
            result = "--" + name;
            if (!Strings.isNullOrEmpty(value))
            {
                result += "=" + value;
            }
        }
        else
        {
            result = value;
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof CommandLineArgument && equals((CommandLineArgument)rhs);
    }

    /**
     * Get whether this CommandLineArgument is equal to the provided CommandLineArgument.
     * @param rhs The CommandLineArgument to compare to this CommandLineArgument.
     * @return Whether or not the two CommandLineArguments are equal.
     */
    public boolean equals(CommandLineArgument rhs)
    {
        return rhs != null &&
            Comparer.equal(name, rhs.name) &&
            Comparer.equal(value, rhs.value);
    }
}
