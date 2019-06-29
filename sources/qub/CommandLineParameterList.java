package qub;

public class CommandLineParameterList<T> extends CommandLineParameterBase<T>
{
    public CommandLineParameterList(String name, Integer index, Function1<String,Result<T>> parseArgumentValue)
    {
        super(name, index, parseArgumentValue);
    }

    @Override
    public CommandLineParameterList<T> setDescription(String description)
    {
        super.setDescription(description);
        return this;
    }

    @Override
    public CommandLineParameterList<T> setAliases(Iterable<String> aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterList<T> setAliases(String... aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterList<T> addAlias(String alias)
    {
        super.addAlias(alias);
        return this;
    }

    @Override
    public CommandLineParameterList<T> addAliases(String... aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterList<T> addAliases(Iterable<String> aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterList<T> setValueName(String valueName)
    {
        super.setValueName(valueName);
        return this;
    }

    @Override
    public CommandLineParameterList<T> setRequired(boolean required)
    {
        super.setRequired(required);
        return this;
    }

    @Override
    public CommandLineParameterList<T> setValueRequired(boolean valueRequired)
    {
        super.setValueRequired(valueRequired);
        return this;
    }

    @Override
    public CommandLineParameterList<T> setArguments(CommandLineArguments arguments)
    {
        super.setArguments(arguments);
        return this;
    }

    /**
     * Get the parsed argument value for this CommandLineParameter.
     * @return The parsed argument value for this CommandLineParameter.
     */
    public Result<Indexable<T>> getValues()
    {
        PreCondition.assertNotNull(getArguments(), "getArguments()");

        return Result.create(() ->
        {
            final CommandLineArguments arguments = getArguments();
            Indexable<String> argumentStringValues = arguments.getNamedValues(getName())
                .catchError(NotFoundException.class)
                .await();
            if (argumentStringValues == null && index != null)
            {
                argumentStringValues = arguments.getAnonymousValues(index)
                    .catchError(NotFoundException.class)
                    .await();
            }

            final List<T> parsedValues = List.create();
            if (!Iterable.isNullOrEmpty(argumentStringValues))
            {
                parsedValues.addAll(argumentStringValues
                    .map((String argumentValue) -> parseArgumentValue.run(argumentValue).await()));
            }
            return parsedValues;
        });
    }

    /**
     * Get the parsed argument value for this CommandLineParameter.
     * @return The parsed argument value for this CommandLineParameter.
     */
    public Result<Indexable<T>> removeValues()
    {
        PreCondition.assertNotNull(getArguments(), "getArguments()");

        return Result.create(() ->
        {
            final CommandLineArguments arguments = getArguments();
            Indexable<String> argumentStringValues = arguments.removeNamedValues(getName())
                .catchError(NotFoundException.class)
                .await();
            if (argumentStringValues == null && index != null)
            {
                argumentStringValues = arguments.removeAnonymousValues(index)
                    .catchError(NotFoundException.class)
                    .await();
            }

            final List<T> parsedValues = List.create();
            if (!Iterable.isNullOrEmpty(argumentStringValues))
            {
                parsedValues.addAll(argumentStringValues
                    .map((String argumentValue) -> parseArgumentValue.run(argumentValue).await()));
            }
            return parsedValues;
        });
    }
}
