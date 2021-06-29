package qub;

public class CommandLineParameterProfiler extends CommandLineParameterBoolean
{
    private final DesktopProcess process;
    private final Class<?> classToAttachTo;

    public CommandLineParameterProfiler(DesktopProcess process, Class<?> classToAttachTo)
    {
        this(process, classToAttachTo, false);
    }

    public CommandLineParameterProfiler(DesktopProcess process, Class<?> classToAttachTo, boolean unspecifiedValue)
    {
        super("profiler", unspecifiedValue);

        PreCondition.assertNotNull(process, "process");
        PreCondition.assertNotNull(classToAttachTo, "classToAttachTo");

        this.process = process;
        this.classToAttachTo = classToAttachTo;

        this.setDescription("Whether or not this application should pause before it is run to allow a profiler to be attached.");
    }

    @Override
    public CommandLineParameterProfiler setDescription(String description)
    {
        return (CommandLineParameterProfiler)super.setDescription(description);
    }

    @Override
    public CommandLineParameterProfiler setAliases(Iterable<String> aliases)
    {
        return (CommandLineParameterProfiler)super.setAliases(aliases);
    }

    @Override
    public CommandLineParameterProfiler setAliases(String... aliases)
    {
        return (CommandLineParameterProfiler)super.setAliases(aliases);
    }

    @Override
    public CommandLineParameterProfiler addAlias(String alias)
    {
        return (CommandLineParameterProfiler)super.addAlias(alias);
    }

    @Override
    public CommandLineParameterProfiler addAliases(String... aliases)
    {
        return (CommandLineParameterProfiler)super.addAliases(aliases);
    }

    @Override
    public CommandLineParameterProfiler addAliases(Iterable<String> aliases)
    {
        return (CommandLineParameterProfiler)super.addAliases(aliases);
    }

    @Override
    public CommandLineParameterProfiler setValueName(String valueName)
    {
        return (CommandLineParameterProfiler)super.setValueName(valueName);
    }

    @Override
    public CommandLineParameterProfiler setRequired(boolean required)
    {
        return (CommandLineParameterProfiler)super.setRequired(required);
    }

    @Override
    public CommandLineParameterProfiler setValueRequired(boolean valueRequired)
    {
        return (CommandLineParameterProfiler)super.setValueRequired(valueRequired);
    }

    @Override
    public CommandLineParameterProfiler setArguments(CommandLineArguments arguments)
    {
        return (CommandLineParameterProfiler)super.setArguments(arguments);
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
            final String classToAttachToName = Types.getTypeName(this.classToAttachTo);
            final long processId = this.process.getProcessId();

            final CharacterWriteStream output = this.process.getOutputWriteStream();
            output.write("Attaching a profiler now to " + classToAttachToName + " (" + processId + ")...").await();

            final QubFolder qubFolder = this.process.getQubFolder().await();
            final QubProjectVersionFolder jdkProjectVersionFolder = qubFolder.getProjectFolder("openjdk", "jdk").await()
                .getLatestProjectVersionFolder().await();
            final VisualVMParameters parameters = VisualVMParameters.create(qubFolder).await()
                .setOpenPid(processId)
                .setSuppressConsoleOutput(true)
                .setJDKHome(jdkProjectVersionFolder);

            final ChildProcessRunner processRunner = this.process.getChildProcessRunner();
            processRunner.start(parameters).await();

            output.writeLine(" Press enter to continue...").await();

            final CharacterReadStream input = this.process.getInputReadStream();
            input.readLine().await();
        }
    }
}
