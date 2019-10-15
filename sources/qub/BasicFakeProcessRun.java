package qub;

/**
 * A fake run of a process.
 */
public class BasicFakeProcessRun implements FakeProcessRun
{
    private final Path executablePath;
    private final List<String> arguments;
    private Path workingFolderPath;
    private Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> function;

    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    public BasicFakeProcessRun(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executableFilePath");

        this.executablePath = executablePath;
        this.arguments = List.create();
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
        PreCondition.assertTrue(workingFolderPath == null || workingFolderPath.isRooted(), "workingFolderPath == null || workingFolderPath.isRooted()");

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
     * @return The folder path that a matching process will run in.
     */
    public Path getWorkingFolderPath()
    {
        return this.workingFolderPath;
    }

    /**
     * Set the exit code that will be returned when this FakeProcessRun is invoked.
     * @param exitCode The exit code that will be returned by the fake process.
     * @return This object for method chaining.
     */
    public FakeProcessRun setFunction(int exitCode)
    {
        return this.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) -> exitCode);
    }

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    public FakeProcessRun setFunction(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) -> action.run());
    }

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    public FakeProcessRun setFunction(Function0<Integer> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) -> function.run());
    }

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    public FakeProcessRun setFunction(Action1<ByteWriteStream> action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) -> action.run(output));
    }

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    public FakeProcessRun setFunction(Function1<ByteWriteStream,Integer> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) -> function.run(output));
    }

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    public FakeProcessRun setFunction(Action2<ByteWriteStream,ByteWriteStream> action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) -> action.run(output, error));
    }

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    public FakeProcessRun setFunction(Function2<ByteWriteStream,ByteWriteStream,Integer> function)
    {
        PreCondition.assertNotNull(function, "function");

        return this.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) -> function.run(output, error));
    }

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    public FakeProcessRun setFunction(Action3<ByteReadStream,ByteWriteStream,ByteWriteStream> action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) ->
        {
            action.run(input, output, error);
            return 0;
        });
    }

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    public FakeProcessRun setFunction(Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> function)
    {
        PreCondition.assertNotNull(function, "function");

        this.function = function;

        return this;
    }

    /**
     * The action that will be run when this FakeProcessRun is invoked.
     * @return The action that will be run when this FakeProcessRun is invoked.
     */
    public Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> getFunction()
    {
        return this.function;
    }
}
