package qub;

/**
 * A command-line parameter for an application.
 * @param <T> The type of value that the command-line parameter will create from the application
 *           arguments.
 */
public abstract class CommandLineParameterBase<T>
{
    private final String name;
    private String description;
    protected final Integer index;
    protected final Function1<String,Result<T>> parseArgumentValue;
    private List<String> aliases;
    private String valueName;
    private boolean required;
    private boolean valueRequired;
    private CommandLineArguments arguments;

    protected CommandLineParameterBase(String name, Integer index, Function1<String,Result<T>> parseArgumentValue)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");
        PreCondition.assertNullOrGreaterThanOrEqualTo(index, 0, "index");
        PreCondition.assertNotNull(parseArgumentValue, "parseArgumentValue");

        this.name = name;
        this.index = index;
        this.parseArgumentValue = parseArgumentValue;
        this.aliases = List.create();
        setValueRequired(index != null);
    }
    
    /**
     * Get the name of the parameter.
     * @return The name of the parameter.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get whether or not this parameter has an assigned position on the command-line..
     * @return Whether or not this parameter has an assigned position on the command-line.
     */
    public boolean hasIndex()
    {
        return getIndex() != null;
    }

    /**
     * Get the position that the parameter appears in on the command-line. If the parameter doesn't
     * have a designated position and must always be named to be referenced, then this will be null.
     * @return The position that the parameter appears in on the command-line.
     */
    public Integer getIndex()
    {
        return index;
    }

    /**
     * Get the description for this parameter.
     * @return The description for this parameter.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description for this parameter.
     * @param description The description for this parameter.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> setDescription(String description)
    {
        this.description = description;

        return this;
    }

    /**
     * Get the other names/shortcuts that this parameter can be referenced as.
     * @return The other names/shortcuts that this parameter can be referenced as.
     */
    public Iterable<String> getAliases()
    {
        return aliases;
    }

    /**
     * Set the aliases that this parameter can be referenced by.
     * @param aliases The aliases that this parameter can be referenced by.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> setAliases(Iterable<String> aliases)
    {
        PreCondition.assertNotNull(aliases, "aliases");

        this.aliases.clear();
        if (aliases.any())
        {
            addAliases(aliases);
        }

        return this;
    }

    /**
     * Set the aliases that this parameter can be referenced by.
     * @param aliases The aliases that this parameter can be referenced by.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> setAliases(String... aliases)
    {
        PreCondition.assertNotNull(aliases, "aliases");

        return setAliases(Iterable.create(aliases));
    }

    /**
     * Add the provided alias to this CommandLineParameter.
     * @param alias The alias to add to this CommandLineParameter.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> addAlias(String alias)
    {
        PreCondition.assertNotNullAndNotEmpty(alias, "alias");

        aliases.add(alias);

        return this;
    }

    /**
     * Add the provided alias to this CommandLineParameter.
     * @param aliases The aliases to add to this CommandLineParameter.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> addAliases(String... aliases)
    {
        PreCondition.assertNotNullAndNotEmpty(aliases, "aliases");

        for (final String alias : aliases)
        {
            addAlias(alias);
        }

        return this;
    }

    /**
     * Add the provided alias to this CommandLineParameter.
     * @param aliases The aliases to add to this CommandLineParameter.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> addAliases(Iterable<String> aliases)
    {
        PreCondition.assertNotNullAndNotEmpty(aliases, "aliases");

        for (final String alias : aliases)
        {
            addAlias(alias);
        }

        return this;
    }

    /**
     * Get the name of this parameter's value that will be used in this parameter's usage string.
     * @return The name of this parameter's value that will be used in this parameter's usage
     * string.
     */
    public String getValueName()
    {
        return valueName;
    }

    /**
     * Set the name of this parameter's value that will be used in this parameter's usage string.
     * @param valueName The name of this parameter's value that will be used in this parameter's
     *                  usage string.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> setValueName(String valueName)
    {
        PreCondition.assertTrue(!Strings.isNullOrEmpty(valueName) || index == null, "!Strings.isNullOrEmpty(valueName) || index == null");

        this.valueName = valueName;

        return this;
    }

    /**
     * Get whether or not this parameter is required.
     * @return Whether or not this parameter is required.
     */
    public boolean isRequired()
    {
        return required;
    }

    /**
     * Set whether or not this parameter is required.
     * @param required Whether or not this parameter is required.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> setRequired(boolean required)
    {
        this.required = required;

        return this;
    }

    /**
     * Get whether or not this parameter can be provided without an explicit value.
     * @return Whether or not this parameter can be provided without an explicit value.
     */
    public boolean isValueRequired()
    {
        return valueRequired;
    }

    /**
     * Set whether or not this parameter can be provided without an explicit value.
     * @param valueRequired Whether or not this parameter can be provided without an explicit value.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> setValueRequired(boolean valueRequired)
    {
        PreCondition.assertTrue(valueRequired || index == null, "valueRequired || index == null");

        this.valueRequired = valueRequired;
        if (valueRequired && Strings.isNullOrEmpty(valueName))
        {
            setValueName('<' + getName() + "-value>");
        }

        return this;
    }

    /**
     * Set the arguments that this CommandLineParameter will get its value from.
     * @param arguments The arguments that this CommandLineParameter will get its value from.
     * @return This object for method chaining.
     */
    public CommandLineParameterBase<T> setArguments(CommandLineArguments arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        this.arguments = arguments;

        return this;
    }
    
    protected CommandLineArguments getArguments()
    {
        return arguments;
    }

    /**
     * Get the usage string that demonstrates how this parameter can be used.
     * @return The usage string that demonstrates how this parameter can be used.
     */
    public String getUsageString()
    {
        final boolean required = isRequired();
        final boolean valueRequired = isValueRequired();
        final boolean hasIndex = hasIndex();
        final String valueName = getValueName();
        final boolean hasValueName = !Strings.isNullOrEmpty(valueName);

        String result = "";

        if (!required)
        {
            result += '[';
        }

        if (hasIndex)
        {
            result += '[';
        }

        result += "--" + getName();

        if (hasIndex)
        {
            result += "=]";
        }
        else if (hasValueName)
        {
            if (!valueRequired)
            {
                result += '[';
            }
            result += '=';
        }

        if (hasValueName)
        {
            result += getValueName();
            if (!valueRequired)
            {
                result += ']';
            }
        }

        if (!required)
        {
            result += ']';
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get the line of a help message that describes this parameter.
     * @return The line of a help message that describes this parameter.
     */
    public String getHelpLine()
    {
        String result = "--" + getName();

        final Iterable<String> aliases = getAliases();
        if (!Iterable.isNullOrEmpty(aliases))
        {
            result += "(" + Strings.join(',', aliases) + ")";
        }
        result += ": ";

        String description = getDescription();
        result += !Strings.isNullOrEmpty(description)
            ? description
            : "(No description provided)";

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }
}
