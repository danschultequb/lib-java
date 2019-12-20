package qub;

public class CommandLineParameterProfiler extends CommandLineParameterBoolean
{
    private final Process process;
    private final Class<?> classToAttachTo;

    public CommandLineParameterProfiler(Process process, Class<?> classToAttachTo)
    {
        this(process, classToAttachTo, false);
    }

    public CommandLineParameterProfiler(Process process, Class<?> classToAttachTo, boolean unspecifiedValue)
    {
        super("profiler", unspecifiedValue);

        PreCondition.assertNotNull(process, "process");
        PreCondition.assertNotNull(classToAttachTo, "classToAttachTo");

        this.process = process;
        this.classToAttachTo = classToAttachTo;

        setDescription("Whether or not this application should pause before it is run to allow a profiler to be attached.");
    }

    @Override
    public CommandLineParameterProfiler setDescription(String description)
    {
        super.setDescription(description);
        return this;
    }

    @Override
    public CommandLineParameterProfiler setAliases(Iterable<String> aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterProfiler setAliases(String... aliases)
    {
        super.setAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterProfiler addAlias(String alias)
    {
        super.addAlias(alias);
        return this;
    }

    @Override
    public CommandLineParameterProfiler addAliases(String... aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterProfiler addAliases(Iterable<String> aliases)
    {
        super.addAliases(aliases);
        return this;
    }

    @Override
    public CommandLineParameterProfiler setValueName(String valueName)
    {
        super.setValueName(valueName);
        return this;
    }

    @Override
    public CommandLineParameterProfiler setRequired(boolean required)
    {
        super.setRequired(required);
        return this;
    }

    @Override
    public CommandLineParameterProfiler setValueRequired(boolean valueRequired)
    {
        super.setValueRequired(valueRequired);
        return this;
    }

    @Override
    public CommandLineParameterProfiler setArguments(CommandLineArguments arguments)
    {
        super.setArguments(arguments);
        return this;
    }

    /**
     * Pause the application so that a Profiler can be attached.
     */
    public void await()
    {
        PreCondition.assertNotNull(this.process, "process");
        PreCondition.assertNotNull(this.classToAttachTo, "classToAttachTo");

        if (getValue().await())
        {
            this.process.getOutputCharacterWriteStream()
                .writeLine("Attach a profiler now to " + Types.getTypeName(this.classToAttachTo) + ". Press enter to continue...")
                .await();
            this.process.getInputCharacterReadStream()
                .readLine()
                .await();
        }
    }
}
