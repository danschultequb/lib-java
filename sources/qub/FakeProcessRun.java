package qub;

/**
 * A fake run of a process.
 */
public interface FakeProcessRun
{
    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    static FakeProcessRun get(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return FakeProcessRun.get(Path.parse(executablePath));
    }

    /**
     * Create a new FakeProcessRun from the provided executablePath.
     * @param executablePath The path to the executable.
     */
    static FakeProcessRun get(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executableFilePath");

        return new BasicFakeProcessRun(executablePath);
    }

    /**
     * Create a new FakeProcessRun from the provided executableFile.
     * @param executableFile The file to execute.
     */
    static FakeProcessRun get(File executableFile)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");

        return FakeProcessRun.get(executableFile.getPath());
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
    FakeProcessRun addArgument(String argument);

    /**
     * Add the provided arguments to this FakeProcessRun.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    FakeProcessRun addArguments(String... arguments);

    /**
     * Add the provided arguments to this FakeProcessRun.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    FakeProcessRun addArguments(Iterable<String> arguments);

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
    FakeProcessRun setWorkingFolder(String workingFolderPath);

    /**
     * Set the folder path that the process will be run from. If null is provided, then this will
     * match any invoked process regardless of where it was run from.
     * @param workingFolderPath The folder path that the process was run from.
     * @return This object for method chaining.
     */
    FakeProcessRun setWorkingFolder(Path workingFolderPath);

    /**
     * Set the folder that the process will be run from. If null is provided, then this will match
     * any invoked process regardless of where it was run from.
     * @param workingFolder The folder that the process was run from.
     * @return This object for method chaining.
     */
    FakeProcessRun setWorkingFolder(Folder workingFolder);

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
    FakeProcessRun setFunction(int exitCode);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeProcessRun setFunction(Action0 action);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeProcessRun setFunction(Function0<Integer> function);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeProcessRun setFunction(Action1<ByteWriteStream> action);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeProcessRun setFunction(Function1<ByteWriteStream,Integer> function);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeProcessRun setFunction(Action2<ByteWriteStream,ByteWriteStream> action);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeProcessRun setFunction(Function2<ByteWriteStream,ByteWriteStream,Integer> function);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param action The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeProcessRun setFunction(Action3<ByteReadStream,ByteWriteStream,ByteWriteStream> action);

    /**
     * Set the action that will be run when this FakeProcessRun is invoked.
     * @param function The action that will be run when this FakeProcessRun is invoked.
     * @return This object for method chaining.
     */
    FakeProcessRun setFunction(Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> function);

    /**
     * The action that will be run when this FakeProcessRun is invoked.
     * @return The action that will be run when this FakeProcessRun is invoked.
     */
    Function3<ByteReadStream,ByteWriteStream,ByteWriteStream,Integer> getFunction();
}
