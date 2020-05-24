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

        if (this.getValue().await())
        {
            final String classtoAttachToName = Types.getTypeName(this.classToAttachTo);
            final long processId = this.process.getProcessId();

            final CharacterWriteStream output = this.process.getOutputWriteStream();
            output.write("Attaching a profiler now to " + classtoAttachToName + " (" + processId + ")...").await();

            final VisualVMProcessBuilder visualVMProcessBuilder = VisualVMProcessBuilder.get(this.process).await()
                .setOpenPid(processId)
                .setShowSplashScreen(false);

            final EnvironmentVariables environmentVariables = this.process.getEnvironmentVariables();
            final String javaHomeString = environmentVariables.get("JAVA_HOME")
                .catchError(NotFoundException.class)
                .await();
            if (!Strings.isNullOrEmpty(javaHomeString))
            {
                final String resolvedJavaHomeString = environmentVariables.resolve(javaHomeString);
                if (!Strings.isNullOrEmpty(resolvedJavaHomeString))
                {
                    final Path javaHomePath = Path.parse(resolvedJavaHomeString);
                    if (javaHomePath.isRooted())
                    {
                        visualVMProcessBuilder.setJDKHome(javaHomePath);
                    }
                }
            }

            visualVMProcessBuilder.run().await();

            output.writeLine(" Press enter to continue...").await();

            final CharacterReadStream input = this.process.getInputReadStream();
            input.readLine().await();
        }
    }
}
