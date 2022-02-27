package qub;

public abstract class DesktopProcessBase<T extends MutableDesktopProcess> extends ProcessBase<T> implements MutableDesktopProcess
{
    private final CommandLineArguments commandLineArguments;
    private int exitCode;
    private final LazyValue<Long> processId;
    private final LazyValue<ChildProcessRunner> childProcessRunner;
    private final LazyValue<String> mainClassFullName;

    protected DesktopProcessBase(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        super(mainAsyncRunner);

        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        this.commandLineArguments = commandLineArguments;
        this.processId = LazyValue.create();
        this.childProcessRunner = LazyValue.create();
        this.mainClassFullName = LazyValue.create();
    }

    /**
     * Get the id of this process.
     * @return The id of this process.
     */
    public long getProcessId()
    {
        return this.processId.get();
    }

    public T setProcessId(long processId)
    {
        return this.setProcessId(() -> processId);
    }

    @SuppressWarnings("unchecked")
    public T setProcessId(Function0<Long> processId)
    {
        this.processId.set(processId);

        return (T)this;
    }

    @Override
    public int getExitCode()
    {
        return this.exitCode;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setExitCode(int exitCode)
    {
        this.exitCode = exitCode;

        return (T)this;
    }

    @Override
    public CommandLineArguments getCommandLineArguments()
    {
        return this.commandLineArguments;
    }

    @Override
    public ChildProcessRunner getChildProcessRunner()
    {
        return this.childProcessRunner.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setChildProcessRunner(ChildProcessRunner childProcessRunner)
    {
        return (T)MutableDesktopProcess.super.setChildProcessRunner(childProcessRunner);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setChildProcessRunner(Function0<ChildProcessRunner> childProcessRunner)
    {
        this.childProcessRunner.set(childProcessRunner);

        return (T)this;
    }

    /**
     * Get the full name of the main class of this application.
     * @return The full name of the main class of this application.
     */
    public String getMainClassFullName()
    {
        return this.mainClassFullName.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setMainClassFullName(String mainClassFullName)
    {
        return (T)MutableDesktopProcess.super.setMainClassFullName(mainClassFullName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setMainClassFullName(Function0<String> mainClassFullName)
    {
        this.mainClassFullName.set(mainClassFullName);

        return (T)this;
    }
}
