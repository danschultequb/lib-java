package qub;

/**
 * A builder that can be used to initialize the parameters needed to invoke an external process.
 */
public interface ProcessBuilder
{
    /**
     * Run the executable path with the provided arguments. Return the process's exit code.
     * @return The exit code from the process's execution.
     */
    Result<Integer> run();

    /**
     * Start the executable path with the configured arguments. Return a ChildProcess object that
     * can be used to monitor the started process. This process will not wait for the child process
     * to complete before returning.
     * @return The ChildProcess that can be used to monitor the started process.
     */
    Result<? extends ChildProcess> start();

    /**
     * Get the path to the executable that this ProcessBuilder will invoke.
     * @return The path to the executable that this ProcessBuilder will invoke.
     */
    Path getExecutablePath();

    /**
     * Get the full command string of the process for this builder.
     * @return The full command string of the process for this builder.
     */
    String getCommand();

    /**
     * Add the provided argument to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param argument The argument to add.
     * @return This object for method chaining.
     */
    ProcessBuilder addArgument(String argument);

    /**
     * Add the provided arguments to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    ProcessBuilder addArguments(String... arguments);

    /**
     * Add the provided arguments to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    ProcessBuilder addArguments(Iterable<String> arguments);

    /**
     * Get the arguments that this ProcessBuilder will provide to the executable.
     * @return The arguments that this ProcessBuilder will provide to the executable.
     */
    Iterable<String> getArguments();

    /**
     * Set the path to the folder that this process will be executed in.
     * @param workingFolderPath The path to the folder that this process will be executed in.
     * @return This object for method chaining.
     */
    ProcessBuilder setWorkingFolder(String workingFolderPath);

    /**
     * Set the path to the folder that this process will be executed in.
     * @param workingFolderPath The path to the folder that this process will be executed in.
     * @return This object for method chaining.
     */
    ProcessBuilder setWorkingFolder(Path workingFolderPath);

    /**
     * Set the folder that this process will be executed in.
     * @param workingFolder The folder that this process will be executed in.
     * @return This object for method chaining.
     */
    ProcessBuilder setWorkingFolder(Folder workingFolder);

    /**
     * Get the path to the folder that this ProcessBuilder will run the executable in.
     * @return The path to the folder that this ProcessBuilder will run the executable in.
     */
    Path getWorkingFolderPath();

    /**
     * Redirect the input stream of the invoked process to use the provided ByteReadStream instead.
     * @param redirectedInputStream The ByteReadStream the invoked process should use instead of its
     *                              default input stream.
     * @return This object for method chaining.
     */
    ProcessBuilder redirectInput(ByteReadStream redirectedInputStream);

    /**
     * Redirect the output stream of the invoked process to the provided action when the process is
     * started.
     * @param redirectOutputAction The action that will be invoked when the process is started.
     * @return This object for method chaining.
     */
    ProcessBuilder redirectOutput(Action1<ByteReadStream> redirectOutputAction);

    /**
     * Redirect the output stream create the created processes to the provided ByteWriteStream.
     * @param redirectedOutputStream The ByteWriteStream to redirect process output to.
     * @return This object for method chaining.
     */
    ProcessBuilder redirectOutput(ByteWriteStream redirectedOutputStream);

    /**
     * Redirect the output stream lines create the processes that are created by this ProcessBuilder.
     * @param onOutputLine The function to call when a process writes a line to its output stream.
     * @return This object for method chaining.
     */
    ProcessBuilder redirectOutputLines(Action1<String> onOutputLine);

    /**
     * Redirect the error stream of the invoked process to the provided action when the process is
     * started.
     * @param redirectErrorAction The action that will be invoked when the process is started.
     * @return This object for method chaining.
     */
    ProcessBuilder redirectError(Action1<ByteReadStream> redirectErrorAction);

    /**
     * Redirect the error stream create the created processes to the provided ByteWriteStream.
     * @param redirectedErrorStream The ByteWriteStream to redirect process error to.
     * @return This object for method chaining.
     */
    ProcessBuilder redirectError(ByteWriteStream redirectedErrorStream);

    /**
     * Redirect the error stream lines create the processes that are created by this ProcessBuilder.
     * @param onErrorLine The function to call when a process writes a line to its error stream.
     * @return This object for method chaining.
     */
    ProcessBuilder redirectErrorLines(Action1<String> onErrorLine);

    /**
     * Set the CharacterWriteStream that this ProcessBuilder will write verbose logs to.
     * @param verbose The CharacterWriteStream that this ProcessBuilder will write verbose logs to.
     * @return This object for method chaining.
     */
    ProcessBuilder setVerbose(CharacterWriteStream verbose);
}
