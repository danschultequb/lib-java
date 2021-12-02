package qub;

/**
 * A fake run of a process.
 */
public class BasicFakeChildProcessRun implements FakeChildProcessRun
{
    private final Path executablePath;
    private final List<String> arguments;
    private Path workingFolderPath;
    private Action1<FakeDesktopProcess> action;

    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    private BasicFakeChildProcessRun(Path executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executablePath, "executableFilePath");
        PreCondition.assertNotNull(arguments, "arguments");

        this.executablePath = executablePath;
        this.arguments = List.create(arguments);
    }

    public static BasicFakeChildProcessRun create(Path executablePath, Iterable<String> arguments)
    {
        return new BasicFakeChildProcessRun(executablePath, arguments);
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
    public FakeChildProcessRun addArgument(String argument)
    {
        PreCondition.assertNotNullAndNotEmpty(argument, "argument");

        this.arguments.add(argument);

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

    @Override
    public FakeChildProcessRun setWorkingFolder(Path workingFolderPath)
    {
        PreCondition.assertTrue(workingFolderPath == null || workingFolderPath.isRooted(), "workingFolderPath == null || workingFolderPath.isRooted()");

        this.workingFolderPath = workingFolderPath;

        return this;
    }

    @Override
    public Path getWorkingFolderPath()
    {
        return this.workingFolderPath;
    }

    @Override
    public FakeChildProcessRun setAction(Action1<FakeDesktopProcess> action)
    {
        PreCondition.assertNotNull(action, "action");

        this.action = action;

        return this;
    }

    @Override
    public Action1<FakeDesktopProcess> getAction()
    {
        return this.action;
    }
}
