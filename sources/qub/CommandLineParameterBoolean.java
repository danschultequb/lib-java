package qub;

public class CommandLineParameterBoolean extends CommandLineParameter<Boolean>
{
    public CommandLineParameterBoolean(String name)
    {
        this(name, false);
    }

    public CommandLineParameterBoolean(String name, boolean unspecifiedValue)
    {
        super(name, null, (String value) ->
        {
            Result<Boolean> argumentValue;
            if (value == null)
            {
                argumentValue = unspecifiedValue ? Result.successTrue() : Result.successFalse();
            }
            else if (value.equals(""))
            {
                argumentValue = Result.successTrue();
            }
            else
            {
                argumentValue = Booleans.parse(value);
            }
            return argumentValue;
        });
    }

    @Override
    public CommandLineParameterBoolean setDescription(String description)
    {
        super.setDescription(description);
        return this;
    }

    @Override
    public CommandLineParameterBoolean setAliases(Iterable<String> aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterBoolean setAliases(String... aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterBoolean addAlias(String alias)
    {
        super.addAlias(alias);
        return this;
    }

    @Override
    public CommandLineParameterBoolean addAliases(String... aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterBoolean addAliases(Iterable<String> aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterBoolean setValueName(String valueName)
    {
        super.setValueName(valueName);
        return this;
    }

    @Override
    public CommandLineParameterBoolean setRequired(boolean required)
    {
        super.setRequired(required);
        return this;
    }

    @Override
    public CommandLineParameterBoolean setValueRequired(boolean valueRequired)
    {
        super.setValueRequired(valueRequired);
        return this;
    }

    @Override
    public CommandLineParameterBoolean setArguments(CommandLineArguments arguments)
    {
        super.setArguments(arguments);
        return this;
    }
}
