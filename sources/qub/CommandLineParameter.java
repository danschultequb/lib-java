package qub;

/**
 * A command-line parameter for an application.
 * @param <T> The type of value that the command-line parameter will create from the application
 *           arguments.
 */
public class CommandLineParameter<T> extends CommandLineParameterBase<T>
{
    private Result<T> valueResult;
    protected boolean removedValue;

    public CommandLineParameter(String name, Integer index, Function1<String,Result<T>> parseArgumentValue)
    {
        super(name, index, parseArgumentValue);
    }

    @Override
    public CommandLineParameter<T> setDescription(String description)
    {
        super.setDescription(description);
        return this;
    }

    @Override
    public CommandLineParameter<T> setAliases(Iterable<String> aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameter<T> setAliases(String... aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameter<T> addAlias(String alias)
    {
        super.addAlias(alias);
        return this;
    }

    @Override
    public CommandLineParameter<T> addAliases(String... aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameter<T> addAliases(Iterable<String> aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameter<T> setValueName(String valueName)
    {
        super.setValueName(valueName);
        return this;
    }

    @Override
    public CommandLineParameter<T> setRequired(boolean required)
    {
        super.setRequired(required);
        return this;
    }

    @Override
    public CommandLineParameter<T> setValueRequired(boolean valueRequired)
    {
        super.setValueRequired(valueRequired);
        return this;
    }

    @Override
    public CommandLineParameter<T> setArguments(CommandLineArguments arguments)
    {
        super.setArguments(arguments);
        return this;
    }

    /**
     * Get the parsed argument value for this CommandLineParameter.
     * @return The parsed argument value for this CommandLineParameter.
     */
    public Result<T> getValue()
    {
        PreCondition.assertNotNull(getArguments(), "getArguments()");

        if (valueResult == null)
        {
            final CommandLineArguments arguments = this.getArguments();
            String argumentStringValue = arguments.getNamedValue(this.getName())
                .catchError(NotFoundException.class)
                .await();
            if (argumentStringValue == null && !Iterable.isNullOrEmpty(aliases))
            {
                for (final String alias : aliases)
                {
                    argumentStringValue = arguments.getNamedValue(alias)
                        .catchError(NotFoundException.class)
                        .await();
                    if (argumentStringValue != null)
                    {
                        break;
                    }
                }
            }
            if (argumentStringValue == null && index != null)
            {
                argumentStringValue = arguments.getAnonymousValue(index)
                    .catchError(NotFoundException.class)
                    .await();
            }
            valueResult = parseArgumentValue.run(argumentStringValue);
        }
        final Result<T> result = valueResult;

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the parsed argument value for this CommandLineParameter.
     * @return The parsed argument value for this CommandLineParameter.
     */
    public Result<T> removeValue()
    {
        PreCondition.assertNotNull(getArguments(), "getArguments()");

        if (!removedValue)
        {
            final CommandLineArguments arguments = getArguments();
            String argumentStringValue = arguments.removeNamedValue(getName())
                .catchError(NotFoundException.class)
                .await();
            if (argumentStringValue == null && !Iterable.isNullOrEmpty(aliases))
            {
                for (final String alias : aliases)
                {
                    argumentStringValue = arguments.removeNamedValue(alias)
                        .catchError(NotFoundException.class)
                        .await();
                    if (argumentStringValue != null)
                    {
                        break;
                    }
                }
            }
            if (argumentStringValue == null && index != null)
            {
                argumentStringValue = arguments.removeAnonymousValue(index)
                    .catchError(NotFoundException.class)
                    .await();
            }
            removedValue = true;

            if (valueResult == null)
            {
                valueResult = parseArgumentValue.run(argumentStringValue);
            }
        }
        final Result<T> result = valueResult;

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
