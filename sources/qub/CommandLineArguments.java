package qub;

/**
 * A collection of String arguments that are passed to an application on the command line.
 */
public class CommandLineArguments implements Indexable<CommandLineArgument>
{
    private final List<CommandLineArgument> arguments;

    private CommandLineArguments(List<CommandLineArgument> arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        this.arguments = arguments;
    }

    public CommandLineArguments()
    {
        this(List.create());
    }

    @Override
    public CommandLineArgument get(int index)
    {
        return this.arguments.get(index);
    }

    @Override
    public Iterator<CommandLineArgument> iterate()
    {
        return this.arguments.iterate();
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    public CommandLineArguments addNamedArgument(String argumentName)
    {
        PreCondition.assertNotNullAndNotEmpty(argumentName, "argumentName");

        return this.addNamedArgument(argumentName, "");
    }

    public CommandLineArguments addNamedArgument(String argumentName, String argumentValue)
    {
        PreCondition.assertNotNullAndNotEmpty(argumentName, "argumentName");
        PreCondition.assertNotNull(argumentValue, "argumentValue");

        this.arguments.add(new CommandLineArgument(argumentName, argumentValue));

        return this;
    }

    public CommandLineArguments addAnonymousArgument(String argumentValue)
    {
        PreCondition.assertNotNullAndNotEmpty(argumentValue, "argumentValue");

        this.arguments.add(new CommandLineArgument("", argumentValue));

        return this;
    }

    @Override
    public int getCount()
    {
        return this.arguments.getCount();
    }

    /**
     * Get the first value of the command line argument with the provided name.
     * @param argumentName The name of the argument to get the value of.
     * @return The value of the command line argument or a NotFoundException if no matching argument
     * is found.
     */
    public Result<String> getNamedValue(String argumentName)
    {
        PreCondition.assertNotNullAndNotEmpty(argumentName, "argumentName");

        return Result.create(() ->
        {
            final CommandLineArgument argument = this.arguments.first((CommandLineArgument arg) -> Comparer.equalIgnoreCase(arg.getName(), argumentName));
            if (argument == null)
            {
                throw new NotFoundException("Could not find command-line arguments with the name " + Strings.escapeAndQuote(argumentName) + ".");
            }
            return argument.getValue();
        });
    }

    /**
     * Get the values of the command line argument with the provided name.
     * @param argumentName The name of the argument to get the values of.
     * @return The values of the command line argument or a NotFoundException if no matching
     * arguments are found.
     */
    public Result<Indexable<String>> getNamedValues(String argumentName)
    {
        PreCondition.assertNotNullAndNotEmpty(argumentName, "argumentName");

        return Result.create(() ->
        {
            final Indexable<String> values = this.arguments
                .where((CommandLineArgument arg) -> Comparer.equalIgnoreCase(arg.getName(), argumentName))
                .map(CommandLineArgument::getValue)
                .toList();
            if (Iterable.isNullOrEmpty(values))
            {
                throw new NotFoundException("Could not find command-line arguments with the name " + Strings.escapeAndQuote(argumentName) + ".");
            }
            return values;
        });
    }

    /**
     * Remove the first value of the command line argument with the provided name.
     * @param argumentName The name of the argument to remove and get the first value of.
     * @return The value of the command line argument or a NotFoundException if no matching
     * arguments are found.
     */
    public Result<String> removeNamedValue(String argumentName)
    {
        PreCondition.assertNotNullAndNotEmpty(argumentName, "argumentName");

        return Result.create(() ->
        {
            final int index = this.arguments.indexOf((CommandLineArgument argument) -> Comparer.equalIgnoreCase(argument.getName(), argumentName));
            if (index < 0)
            {
                throw new NotFoundException("Could not find a command-line argument with the name " + Strings.escapeAndQuote(argumentName) + ".");
            }
            return this.arguments.removeAt(index).getValue();
        });
    }

    /**
     * Remove the values of the command line argument with the provided name.
     * @param argumentName The name of the argument to remove and get the values of.
     * @return The values of the command line arguments or a NotFoundException if no matching
     * arguments are found.
     */
    public Result<Indexable<String>> removeNamedValues(String argumentName)
    {
        PreCondition.assertNotNullAndNotEmpty(argumentName, "argumentName");

        final List<String> values = List.create();

        int index = 0;
        while (index < arguments.getCount())
        {
            final CommandLineArgument argument = arguments.get(index);
            if (Comparer.equalIgnoreCase(argument.getName(), argumentName))
            {
                arguments.removeAt(index);
                values.add(argument.getValue());
            }
            else
            {
                ++index;
            }
        }

        return values.any()
            ? Result.success(values)
            : Result.error(new NotFoundException("Could not find a command-line argument with the name " + Strings.escapeAndQuote(argumentName) + "."));
    }

    /**
     * Get the value of the anonymous command line argument at the provided anonymous value index.
     * @param anonymousValueIndex The index of the anonymous value to return.
     * @return The value of the anonymous command line argument in the provided index or
     * NotFoundException if no argument exists in the provided index.
     */
    public Result<String> getAnonymousValue(int anonymousValueIndex)
    {
        PreCondition.assertGreaterThanOrEqualTo(anonymousValueIndex, 0, "anonymousValueIndex");

        final Indexable<CommandLineArgument> anonymousArguments = getAnonymousArguments().toList();
        return anonymousArguments.getCount() > anonymousValueIndex
            ? Result.success(anonymousArguments.get(anonymousValueIndex).getValue())
            : Result.error(new NotFoundException("Could not find an anonymous command-line argument in the index " + anonymousValueIndex + "."));
    }

    /**
     * Get the values of the anonymous command line arguments at and after the provided anonymous
     * value index.
     * @param anonymousValueIndex The index of the anonymous values to begin returning from.
     * @return The value of the anonymous command line argument at and after the provided anonymous
     * value index or NotFoundException if no argument exists in the provided index.
     */
    public Result<Indexable<String>> getAnonymousValues(int anonymousValueIndex)
    {
        PreCondition.assertGreaterThanOrEqualTo(anonymousValueIndex, 0, "anonymousValueIndex");

        final Indexable<String> anonymousArguments = getAnonymousArguments()
            .skip(anonymousValueIndex)
            .map(CommandLineArgument::getValue)
            .toList();
        return anonymousArguments.any()
            ? Result.success(anonymousArguments)
            : Result.error(new NotFoundException("Could not find an anonymous command-line argument at the index " + anonymousValueIndex + "."));
    }

    /**
     * Remove the value of the anonymous command line argument in the provided index.
     * @param anonymousValueIndex The index of the anonymous value to return.
     * @return The value of the anonymous command line argument in the provided index or
     * NotFoundException if no argument exists in the provided index.
     */
    public Result<String> removeAnonymousValue(int anonymousValueIndex)
    {
        PreCondition.assertGreaterThanOrEqualTo(anonymousValueIndex, 0, "anonymousValueIndex");

        Result<String> result = null;
        int anonymousIndex = 0;
        for (int i = 0; i < arguments.getCount(); ++i)
        {
            final CommandLineArgument argument = arguments.get(i);
            if (Strings.isNullOrEmpty(argument.getName()))
            {
                if (anonymousIndex == anonymousValueIndex)
                {
                    arguments.removeAt(i);
                    result = Result.success(argument.getValue());
                    break;
                }
                else
                {
                    ++anonymousIndex;
                }
            }
        }
        if (result == null)
        {
            result = Result.error(new NotFoundException("Could not find an anonymous command-line argument in the index " + anonymousValueIndex + "."));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Remove the values of the anonymous command line argument at and after the provided index.
     * @param anonymousValueIndex The index of the anonymous values to return.
     * @return The values of the anonymous command line argument at and after the provided index or
     * NotFoundException if no argument exists in the provided index.
     */
    public Result<Indexable<String>> removeAnonymousValues(int anonymousValueIndex)
    {
        PreCondition.assertGreaterThanOrEqualTo(anonymousValueIndex, 0, "anonymousValueIndex");

        final List<String> anonymousValues = List.create();
        int anonymousIndex = 0;
        int index = 0;
        while (index < arguments.getCount())
        {
            final CommandLineArgument argument = arguments.get(index);
            if (Strings.isNullOrEmpty(argument.getName()))
            {
                if (anonymousIndex >= anonymousValueIndex)
                {
                    anonymousValues.add(arguments.removeAt(index).getValue());
                }
                else
                {
                    ++anonymousIndex;
                    ++index;
                }
            }
            else
            {
                ++index;
            }
        }

        return anonymousValues.any()
            ? Result.success(anonymousValues)
            : Result.error(new NotFoundException("Could not find an anonymous command-line argument in the index " + anonymousValueIndex + "."));
    }

    /**
     * Parse a CommandLineArguments object from the partially parsed command line arguments.
     * @param commandLineArguments The partially parsed entire command line arguments.
     * @return The CommandLineArguments that were parsed from the provided command line String.
     */
    public static CommandLineArguments create(String... commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        final CommandLineArguments result = create(Iterable.create(commandLineArguments));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Parse a CommandLineArguments object from the partially parsed command line arguments.
     * @param commandLineArguments The partially parsed entire command line arguments.
     * @return The CommandLineArguments that were parsed from the provided command line String.
     */
    public static CommandLineArguments create(Iterable<String> commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        final List<CommandLineArgument> arguments = List.create();
        final CharacterList argumentName = CharacterList.create();
        final CharacterList argumentValue = CharacterList.create();
        for (final String commandLineArgument : commandLineArguments)
        {
            final Iterator<Character> characters = Strings.iterate(commandLineArgument);
            if (characters.next())
            {
                boolean isValueArgument = true;
                if (characters.getCurrent() == '-')
                {
                    isValueArgument = !characters.next() || (characters.getCurrent() == '-' && !characters.next());
                }

                if (isValueArgument)
                {
                    argumentValue.addAll(commandLineArgument);
                }
                else
                {
                    while (characters.hasCurrent() && characters.getCurrent() != '=')
                    {
                        argumentName.add(characters.takeCurrent());
                    }

                    if (characters.next())
                    {
                        argumentValue.addAll(characters);
                    }
                }
            }

            if (argumentName.any() || argumentValue.any())
            {
                arguments.add(new CommandLineArgument(argumentName.toString(), argumentValue.toString()));
                argumentName.clear();
                argumentValue.clear();
            }
        }
        final CommandLineArguments result = new CommandLineArguments(arguments);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    private Iterable<CommandLineArgument> getAnonymousArguments()
    {
        return arguments.where((CommandLineArgument argument) -> Strings.isNullOrEmpty(argument.getName()));
    }
}
