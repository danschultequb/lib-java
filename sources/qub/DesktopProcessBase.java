package qub;

public abstract class DesktopProcessBase extends ProcessBase implements DesktopProcess
{
    private final CommandLineArguments commandLineArguments;
    private int exitCode;
    private final LongValue processId;
    private final Value<ChildProcessRunner> childProcessRunner;
    private final Value<String> mainClassFullName;

    protected DesktopProcessBase(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        super(mainAsyncRunner);

        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        this.commandLineArguments = commandLineArguments;
        this.processId = LongValue.create();
        this.childProcessRunner = Value.create();
        this.mainClassFullName = Value.create();
    }

    /**
     * Get the id of this process.
     * @return The id of this process.
     */
    public long getProcessId()
    {
        return this.processId.getOrSet(this.getProcessIdValue());
    }

    /**
     * Get the ID of this process.
     * @return The ID of this process.
     */
    protected abstract long getProcessIdValue();

    @Override
    public int getExitCode()
    {
        return this.exitCode;
    }

    @Override
    public DesktopProcessBase setExitCode(int exitCode)
    {
        this.exitCode = exitCode;
        return this;
    }

    @Override
    public CommandLineArguments getCommandLineArguments()
    {
        return this.commandLineArguments;
    }

    @Override
    public ChildProcessRunner getChildProcessRunner()
    {
        return this.childProcessRunner.getOrSet(this::createDefaultChildProcessRunner);
    }

    protected abstract ChildProcessRunner createDefaultChildProcessRunner();

    /**
     * Get the full name of the main class of this application.
     * @return The full name of the main class of this application.
     */
    public String getMainClassFullName()
    {
        return this.mainClassFullName.getOrSet(this::createDefaultMainClassFullName);
    }

    protected abstract String createDefaultMainClassFullName();
}
