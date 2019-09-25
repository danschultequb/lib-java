package qub;

/**
 * A fake run of a process.
 */
public class FakeProcessRun
{
    private final Path executablePath;
    private final List<String> arguments;
    private Path workingFolderPath;
    private Action0 action;
    private int exitCode;

    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    public FakeProcessRun(String executablePath)
    {
        this(Path.parse(executablePath));
    }

    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    public FakeProcessRun(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executableFilePath");

        this.executablePath = executablePath;
        this.arguments = List.create();
    }

    /**
     * Create a new FakeProcessRun from the provided executableFile.
     * @param executableFile The file to execute.
     */
    public FakeProcessRun(File executableFile)
    {
        this(executableFile.getPath());
    }

    /**
     * Get the path to the executable.
     * @return The path to the executable.
     */
    public Path getExecutablePath()
    {
        return this.executablePath;
    }

    /**
     * Add an argument to this FakeProcessRun.
     * @param argument The argument to add.
     * @return This object for method chaining.
     */
    public FakeProcessRun addArgument(String argument)
    {
        PreCondition.assertNotNullAndNotEmpty(argument, "argument");

        this.arguments.add(argument);

        return this;
    }

    /**
     * Add the provided arguments to this FakeProcessRun.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    public FakeProcessRun addArguments(String... arguments)
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
    public FakeProcessRun addArguments(Iterable<String> arguments)
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
    public Iterable<String> getArguments()
    {
        return this.arguments;
    }

    /**
     * Set the folder path that the process will be run from. If null is provided, then this will
     * match any invoked process regardless of where it was run from.
     * @param workingFolderPath The folder path that the process was run from.
     * @return This object for method chaining.
     */
    public FakeProcessRun setWorkingFolder(String workingFolderPath)
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
    public FakeProcessRun setWorkingFolder(Path workingFolderPath)
    {
        this.workingFolderPath = workingFolderPath;

        return this;
    }

    /**
     * Set the folder that the process will be run from. If null is provided, then this will match
     * any invoked process regardless of where it was run from.
     * @param workingFolder The folder that the process was run from.
     * @return This object for method chaining.
     */
    public FakeProcessRun setWorkingFolder(Folder workingFolder)
    {
        return this.setWorkingFolder(workingFolder == null ? null : workingFolder.getPath());
    }

    /**
     * Get the folder path that a matching process will run in. If this is null, then this will
     * match a process regardless of where it is run from.
     * @return
     */
    public Path getWorkingFolderPath()
    {
        return this.workingFolderPath;
    }

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    public FakeProcessRun setAction(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        this.action = action;

        return this;
    }

    /**
     * The action that will be run when this FakeProcessRun is invoked.
     * @return The action that will be run when this FakeProcessRun is invoked.
     */
    public Action0 getAction()
    {
        return this.action;
    }

    /**
     * Set the exit code that this process will return.
     * @param exitCode The exit code that this process will return.
     * @return This object for method chaining.
     */
    public FakeProcessRun setExitCode(int exitCode)
    {
        this.exitCode = exitCode;

        return this;
    }

    /**
     * Get the exit code that this process will return.
     * @return The exit code that this process will return.
     */
    public int getExitCode()
    {
        return this.exitCode;
    }
}
