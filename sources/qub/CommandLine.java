package qub;

public class CommandLine implements Indexable<CommandLineArgument>
{
    private final String[] commandLineParts;

    public CommandLine(String... commandLineParts)
    {
        this.commandLineParts = Array.toStringArray(commandLineParts);
    }

    public String[] getArgumentStrings()
    {
        return commandLineParts;
    }

    public Indexable<CommandLineArgument> getArguments()
    {
        final ArrayList<CommandLineArgument> result = new ArrayList<>();
        if (commandLineParts != null && commandLineParts.length > 0)
        {
            for (final String commandLinePart : commandLineParts)
            {
                result.add(new CommandLineArgument(commandLinePart));
            }
        }
        return result;
    }

    @Override
    public Iterator<CommandLineArgument> iterate()
    {
        return getArguments().iterate();
    }

    @Override
    public CommandLineArgument get(int index)
    {
        return getArguments().get(index);
    }

    public CommandLineArgument get(String argumentName)
    {
        CommandLineArgument result = null;
        if (argumentName != null && !argumentName.isEmpty())
        {
            for (final CommandLineArgument argument : this)
            {
                if(argumentName.equalsIgnoreCase(argument.getName()))
                {
                    result = argument;
                    break;
                }
            }
        }
        return result;
    }

    public String getValue(String argumentName)
    {
        final CommandLineArgument argument = get(argumentName);
        return argument == null ? null : argument.getValue();
    }
}
