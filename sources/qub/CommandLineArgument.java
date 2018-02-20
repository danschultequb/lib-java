package qub;

public class CommandLineArgument
{
    private final String argumentString;
    private final String name;
    private final String value;

    public CommandLineArgument(String argumentString)
    {
        this.argumentString = argumentString;

        if (argumentString == null || argumentString.isEmpty() || !argumentString.startsWith("-"))
        {
            this.name = null;
            this.value = argumentString;
        }
        else
        {
            final int nameStartIndex = 1;
            final int equalsIndex = argumentString.indexOf('=');
            if (equalsIndex == -1)
            {
                this.name = argumentString.substring(nameStartIndex);
                this.value = null;
            }
            else
            {
                this.name = argumentString.substring(nameStartIndex, equalsIndex);
                this.value = argumentString.substring(equalsIndex + 1);
            }
        }
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return argumentString;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof CommandLineArgument && equals((CommandLineArgument)rhs);
    }

    public boolean equals(CommandLineArgument rhs)
    {
        return rhs != null && argumentString.equals(rhs.argumentString);
    }
}
