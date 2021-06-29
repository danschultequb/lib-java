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
        PreCondition.assertNotNull(executablePath, "executableFilePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return FakeChildProcessRun.create(executablePath, Iterable.create(arguments));
    }

    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    static FakeChildProcessRun create(Path executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executablePath, "executableFilePath");

        return BasicFakeChildProcessRun.create(executablePath, arguments);
    }

    /**
     * Create a new FakeProcessRun from the provided executableFile.
     * @param executableFile The file to execute.
     */
    static FakeChildProcessRun create(File executableFile)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");

        return FakeChildProcessRun.create(executableFile.getPath());
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
    FakeChildProcessRun addArguments(String... arguments);

    /**
     * Add the provided arguments to this FakeProcessRun.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    FakeChildProcessRun addArguments(Iterable<String> arguments);

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
    FakeChildProcessRun setWorkingFolder(String workingFolderPath);

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
    FakeChildProcessRun setWorkingFolder(Folder workingFolder);

    /**
     * Get the folder path that a matching process will run in. If this is null, then this will
     * match a process regardless of where it is run from.
     * @return The folder that the process will run in.
     */
    Path getWorkingFolderPath();

    /**
     * Set the exit code that will be returned when this FakeProcessRun is invoked.
     * @param exitCode The exit code that will be returned by the fake process.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setFunction(int exitCode);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setFunction(Action0 action);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setFunction(Function0<Integer> function);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setFunction(Action1<ByteWriteStream> action);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setFunction(Function1<ByteWriteStream,Integer> function);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setFunction(Action2<ByteWriteStream,ByteWriteStream> action);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setFunction(Function2<ByteWriteStream,ByteWriteStream,Integer> function);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setFunction(Action3<ByteReadStream,ByteWriteStream,ByteWriteStream> action);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeChildProcessRun setFunction(Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> function);

    /**
     * The action that will be run when this FakeProcessRun is invoked.
     * @return The action that will be run when this FakeProcessRun is invoked.
     */
    Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> getFunction();
}
