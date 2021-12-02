package qub;

/**
 * A fake run of a process.
 */
public interface FakeChildProcessRun
{
    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    static FakeChildProcessRun create(String executablePath, String... arguments)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return FakeChildProcessRun.create(Path.parse(executablePath), arguments);
    }

    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    static FakeChildProcessRun create(String executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return FakeChildProcessRun.create(Path.parse(executablePath), arguments);
    }

    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    static FakeChildProcessRun create(Path executablePath, String... arguments)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return FakeChildProcessRun.create(executablePath, Iterable.create(arguments));
    }

    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    static FakeChildProcessRun create(Path executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return BasicFakeChildProcessRun.create(executablePath, arguments);
    }

    /**
     * Create a new FakeProcessRun from the provided executableFile.
     * @param executableFile The file to execute.
     */
    static FakeChildProcessRun create(File executableFile, String... arguments)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");

        return FakeChildProcessRun.create(executableFile, Iterable.create(arguments));
    }

    /**
     * Create a new FakeProcessRun from the provided executableFile.
     * @param executableFile The file to execute.
     */
    static FakeChildProcessRun create(File executableFile, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");

        return FakeChildProcessRun.create(executableFile.getPath(), arguments);
    }

    /**
     * Get the path to the executable.
     * @return The path to the executable.
     */
    Path getExecutablePath();

    /**
     * Add an argument to this FakeProcessRun.
     * @param argument The argument to add.
     * @return This object for method chaining.
     */
    FakeChildProcessRun addArgument(String argument);

    /**
     * Add the provided arguments to this FakeProcessRun.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    default FakeChildProcessRun addArguments(String... arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        for (final String argument : arguments)
        {
            this.addArgument(argument);
        }

        return this;
    }

    /**
     * Add the provided arguments to this FakeProcessRun.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    default FakeChildProcessRun addArguments(Iterable<String> arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        for (final String argument : arguments)
        {
            this.addArgument(argument);
        }

        return this;
    }

    /**
     * Get the arguments that have been added to this process.
     * @return The arguments that have been added to this process.
     */
    Iterable<String> getArguments();

    /**
     * Set the folder path that the process will be run from. If null is provided, then this will
     * match any invoked process regardless of where it was run from.
     * @param workingFolderPath The folder path that the process was run from.
     * @return This object for method chaining.
     */
    default FakeChildProcessRun setWorkingFolder(String workingFolderPath)
    {
        PreCondition.assertNullOrNotEmpty(workingFolderPath, "workingFolderPath");

        return this.setWorkingFolder(workingFolderPath == null ? null : Path.parse(workingFolderPath));
    }

    /**
     * Set the folder path that the process will be run from. If null is provided, then this will
     * match any invoked process regardless of where it was run from.
     * @param workingFolderPath The folder path that the process was run from.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setWorkingFolder(Path workingFolderPath);

    /**
     * Set the folder that the process will be run from. If null is provided, then this will match
     * any invoked process regardless of where it was run from.
     * @param workingFolder The folder that the process was run from.
     * @return This object for method chaining.
     */
    default FakeChildProcessRun setWorkingFolder(Folder workingFolder)
    {
        return this.setWorkingFolder(workingFolder == null ? null : workingFolder.getPath());
    }

    /**
     * Get the folder path that a matching process will run in. If this is null, then this will
     * match a process regardless of where it is run from.
     * @return The folder that the process will run in.
     */
    Path getWorkingFolderPath();

    /**
     * Set the {@link Action1} that will be run when this {@link FakeChildProcessRun} is invoked.
     * @param action The {@link Action1} that will be run when this {@link FakeChildProcessRun} is
     *               invoked.
     * @return This object for method chaining.
     */
    default FakeChildProcessRun setAction(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.setAction((FakeDesktopProcess process) -> action.run());
    }

    /**
     * Set the {@link Action1} that will be run when this {@link FakeChildProcessRun} is invoked.
     * @param action The {@link Action1} that will be run when this {@link FakeChildProcessRun} is
     *               invoked.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setAction(Action1<FakeDesktopProcess> action);

    /**
     * The {@link Action1} will be run when this {@link FakeChildProcessRun} is invoked.
     * @return The {@link Action1} that will be run when this {@link FakeChildProcessRun} is
     * invoked.
     */
    Action1<FakeDesktopProcess> getAction();
}
