package qub;

/**
 * A ProcessBuilder that builds up a process for invocation.
 */
public class ProcessBuilder
{
    private final ProcessFactory factory;
    private final Path executablePath;
    private final List<String> arguments;
    private Path workingFolderPath;
    private ByteReadStream redirectedInputStream;
    private Action1<ByteReadStream> redirectOutputAction;
    private Action1<ByteReadStream> redirectErrorAction;

    public ProcessBuilder(ProcessFactory factory, Path executablePath, Path workingFolderPath)
    {
        PreCondition.assertNotNull(factory, "factory");
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        this.factory = factory;
        this.executablePath = executablePath;
        this.arguments = List.create();
        this.workingFolderPath = workingFolderPath;
    }

    /**
     * Run the executable path with the provided arguments. Return the process's exit code.
     * @return The exit code from the process's execution.
     */
    public Result<Integer> run()
    {
        return this.factory.run(
            this.executablePath,
            this.arguments,
            this.workingFolderPath,
            this.redirectedInputStream,
            this.redirectOutputAction,
            this.redirectErrorAction);
    }

    /**
     * Get the path to the executable that this ProcessBuilder will invoke.
     * @return The path to the executable that this ProcessBuilder will invoke.
     */
    public Path getExecutablePath()
    {
        return this.executablePath;
    }

    /**
     * Add the provided argument to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param argument The argument to add.
     * @return This object for method chaining.
     */
    public ProcessBuilder addArgument(String argument)
    {
        PreCondition.assertNotNullAndNotEmpty(argument, "argument");

        this.arguments.add(argument);

        return this;
    }

    /**
     * Add the provided arguments to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    public ProcessBuilder addArguments(String... arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        for (final String argument : arguments)
        {
            this.addArgument(argument);
        }

        return this;
    }

    /**
     * Add the provided arguments to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    public ProcessBuilder addArguments(Iterable<String> arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        for (final String argument : arguments)
        {
            this.addArgument(argument);
        }

        return this;
    }

    /**
     * Get the arguments that this ProcessBuilder will provide to the executable.
     * @return The arguments that this ProcessBuilder will provide to the executable.
     */
    public Iterable<String> getArguments()
    {
        return this.arguments;
    }

    /**
     * Set the path to the folder that this process will be executed in.
     * @param workingFolderPath The path to the folder that this process will be executed in.
     * @return This object for method chaining.
     */
    public ProcessBuilder setWorkingFolder(String workingFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(workingFolderPath, "workingFolderPath");

        return this.setWorkingFolder(Path.parse(workingFolderPath));
    }

    /**
     * Set the path to the folder that this process will be executed in.
     * @param workingFolderPath The path to the folder that this process will be executed in.
     * @return This object for method chaining.
     */
    public ProcessBuilder setWorkingFolder(Path workingFolderPath)
    {
        PreCondition.assertNotNull(workingFolderPath, "workingFolderPath");

        this.workingFolderPath = workingFolderPath;

        return this;
    }

    /**
     * Set the folder that this process will be executed in.
     * @param workingFolder The folder that this process will be executed in.
     * @return This object for method chaining.
     */
    public ProcessBuilder setWorkingFolder(Folder workingFolder)
    {
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        return this.setWorkingFolder(workingFolder.getPath());
    }

    /**
     * Get the path to the folder that this ProcessBuilder will run the executable in.
     * @return The path to the folder that this ProcessBuilder will run the executable in.
     */
    public Path getWorkingFolderPath()
    {
        return this.workingFolderPath;
    }

    /**
     * Redirect the input stream of the invoked process to use the provided ByteReadStream instead.
     * @param redirectedInputStream The ByteReadStream the invoked process should use instead of its
     *                              default input stream.
     * @return This object for method chaining.
     */
    public ProcessBuilder redirectInput(ByteReadStream redirectedInputStream)
    {
        this.redirectedInputStream = redirectedInputStream;
        return this;
    }

    /**
     * Redirect the output stream of the invoked process to the provided action when the process is
     * started.
     * @param redirectOutputAction The action that will be invoked when the process is started.
     * @return This object for method chaining.
     */
    public ProcessBuilder redirectOutput(Action1<ByteReadStream> redirectOutputAction)
    {
        this.redirectOutputAction = redirectOutputAction;
        return this;
    }

    /**
     * Redirect the output stream create the created processes to the provided ByteWriteStream.
     * @param redirectedOutputStream The ByteWriteStream to redirect process output to.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectOutput(ByteWriteStream redirectedOutputStream)
    {
        return this.redirectOutput(redirectedOutputStream::writeAllBytes);
    }

    /**
     * Redirect the output stream lines create the processes that are created by this ProcessBuilder.
     * @param onOutputLine The function to call when a process writes a line to its output stream.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectOutputLines(Action1<String> onOutputLine)
    {
        return this.redirectOutput(ProcessBuilder.byteReadStreamToLineAction(onOutputLine));
    }

    /**
     * Redirect the output stream lines create the processes that are created by this ProcessBuilder
     * to the provided StringBuilder.
     * @param builder The StringBuilder to collect process output in.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectOutputTo(StringBuilder builder)
    {
        return this.redirectOutputLines(ProcessBuilder.appendLineToStringBuilder(builder));
    }

    /**
     * Redirect the output stream lines create the processes that are created by this ProcessBuilder
     * to the returned StringBuilder.
     * @return This StringBuilder that will collect the output stream..
     */
    public StringBuilder redirectOutput()
    {
        final StringBuilder result = new StringBuilder();
        this.redirectOutputTo(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Redirect the error stream of the invoked process to the provided action when the process is
     * started.
     * @param redirectErrorAction The action that will be invoked when the process is started.
     * @return This object for method chaining.
     */
    public ProcessBuilder redirectError(Action1<ByteReadStream> redirectErrorAction)
    {
        this.redirectErrorAction = redirectErrorAction;
        return this;
    }

    /**
     * Redirect the error stream create the created processes to the provided ByteWriteStream.
     * @param redirectedErrorStream The ByteWriteStream to redirect process error to.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectError(ByteWriteStream redirectedErrorStream)
    {
        return this.redirectError(redirectedErrorStream::writeAllBytes);
    }

    /**
     * Redirect the error stream lines create the processes that are created by this ProcessBuilder.
     * @param onErrorLine The function to call when a process writes a line to its error stream.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectErrorLines(Action1<String> onErrorLine)
    {
        return this.redirectError(ProcessBuilder.byteReadStreamToLineAction(onErrorLine));
    }

    /**
     * Redirect the error stream lines create the processes that are created by this ProcessBuilder to
     * the provided StringBuilder.
     * @param builder The StringBuilder to collect process error in.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectErrorTo(StringBuilder builder)
    {
        return this.redirectErrorLines(ProcessBuilder.appendLineToStringBuilder(builder));
    }

    /**
     * Redirect the error stream lines create the processes that are created by this ProcessBuilder to
     * the returned StringBuilder.
     * @return This StringBuilder that will collect the error stream..
     */
    public StringBuilder redirectError()
    {
        final StringBuilder result = new StringBuilder();
        this.redirectErrorTo(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get a function that will take in a ByteReadStream and will invoked the provided function on
     * each line.
     * @param onLineAction The function to invoke for each line of the ByteReadStream.
     * @return The function.
     */
    private static Action1<ByteReadStream> byteReadStreamToLineAction(Action1<String> onLineAction)
    {
        PreCondition.assertNotNull(onLineAction, "onLineAction");

        return (ByteReadStream byteReadStream) ->
        {
            final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream();
            String line;
            do
            {
                line = characterReadStream.readLine(true)
                    .catchError(EndOfStreamException.class)
                    .await();
                onLineAction.run(line);
            }
            while (line != null);
        };
    }

    private static Action1<String> appendLineToStringBuilder(StringBuilder builder)
    {
        PreCondition.assertNotNull(builder, "builder");

        return (String line) ->
        {
            if (line != null)
            {
                builder.append(line);
            }
        };
    }
}
