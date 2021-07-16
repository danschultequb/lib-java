package qub;

/**
 * Parameters that can be specified to alter how an executable will be run.
 */
public interface ChildProcessParameters
{
    /**
     * Create a new ExecutableParameters object.
     * @return The new ExecutableParameters object.
     */
    static ChildProcessParameters create(String executablePath, String... arguments)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return ChildProcessParameters.create(Path.parse(executablePath), arguments);
    }

    /**
     * Create a new ExecutableParameters object.
     * @return The new ExecutableParameters object.
     */
    static ChildProcessParameters create(String executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return ChildProcessParameters.create(Path.parse(executablePath), arguments);
    }

    /**
     * Create a new ExecutableParameters object.
     * @return The new ExecutableParameters object.
     */
    static ChildProcessParameters create(Path executablePath, String... arguments)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return ChildProcessParameters.create(executablePath, Iterable.create(arguments));
    }

    /**
     * Create a new ExecutableParameters object.
     * @return The new ExecutableParameters object.
     */
    static ChildProcessParameters create(Path executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return BasicChildProcessParameters.create(executablePath, arguments);
    }

    /**
     * Create a new ExecutableParameters object.
     * @return The new ExecutableParameters object.
     */
    static ChildProcessParameters create(File executableFile, String... arguments)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");

        return ChildProcessParameters.create(executableFile.getPath(), arguments);
    }

    /**
     * Create a new ExecutableParameters object.
     * @return The new ExecutableParameters object.
     */
    static ChildProcessParameters create(File executableFile, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");

        return ChildProcessParameters.create(executableFile.getPath(), arguments);
    }

    /**
     * Get the path to the executable to run.
     * @return The path to the executable to run.
     */
    Path getExecutablePath();

    /**
     * Get the command-line arguments that will be passed to the executable.
     * @return The command-line arguments that will be passed to the executable.
     */
    Iterable<String> getArguments();

    /**
     * Insert the provided argument into the list of command-line arguments at the provided index.
     * @param index The index at which to insert the provided argument.
     * @param argument The argument to insert.
     * @return This object for method chaining.
     */
    ChildProcessParameters insertArgument(int index, String argument);

    /**
     * Add the provided argument to the list of command-line arguments that will be passed to the
     * executable.
     * @param argument The argument to add.
     * @return This object for method chaining.
     */
    default ChildProcessParameters addArgument(String argument)
    {
        return this.insertArgument(this.getArguments().getCount(), argument);
    }

    /**
     * Add the provided arguments to the list of command-line arguments that will be passed to the executable.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    default ChildProcessParameters addArguments(String... arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        for (final String argument : arguments)
        {
            this.addArgument(argument);
        }

        return this;
    }

    /**
     * Add the provided arguments to the list of command-line arguments that will be passed to the executable.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    default ChildProcessParameters addArguments(Iterable<String> arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        for (final String argument : arguments)
        {
            this.addArgument(argument);
        }

        return this;
    }

    /**
     * Get the working folder path that the executable should be run in, or null if no working
     * folder path has been specified.
     * @return The working folder path that the executable should be run in, or null if no working
     * folder path has been specified.
     */
    Path getWorkingFolderPath();

    /**
     * Set the working folder path that the executable should be run in.
     * @param workingFolderPath The working folder path that the executable should be run in.
     * @return This object for method chaining.
     */
    default ChildProcessParameters setWorkingFolderPath(String workingFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(workingFolderPath, "workingFolderPath");

        return this.setWorkingFolderPath(Path.parse(workingFolderPath));
    }

    /**
     * Set the working folder path that the executable should be run in.
     * @param workingFolderPath The working folder path that the executable should be run in.
     * @return This object for method chaining.
     */
    ChildProcessParameters setWorkingFolderPath(Path workingFolderPath);

    /**
     * Set the working folder that the executable should be run in.
     * @param workingFolder The working folder that the executable should be run in.
     * @return This object for method chaining.
     */
    default ChildProcessParameters setWorkingFolder(Folder workingFolder)
    {
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        return this.setWorkingFolderPath(workingFolder.getPath());
    }

    /**
     * Get the input stream that the executable should read from.
     * @return The input stream that the executable should read from.
     */
    ByteReadStream getInputStream();

    /**
     * Set the input stream that the executable should read from.
     * @param inputStream The input stream that the executable should read from.
     * @return This object for method chaining.
     */
    ChildProcessParameters setInputStream(ByteReadStream inputStream);

    /**
     * Get the function that will be invoked to handle the output stream from the executable, or
     * null if no handler has been assigned.
     * @return The function that will be invoked to handle the output stream from the executable,
     * or null if no handler has been assigned.
     */
    Action1<ByteReadStream> getOutputStreamHandler();

    /**
     * Set the function that will be invoked to handle the output stream from the executable.
     * @param outputStreamHandler The function that will be invoked to handle the output stream
     *                            from the executable.
     * @return This object for method chaining.
     */
    ChildProcessParameters setOutputStreamHandler(Action1<ByteReadStream> outputStreamHandler);

    /**
     * Redirect any bytes written to the child process's output stream to the provided outputStream.
     * @param outputStream The output stream that the child process's output stream will be written
     *                     to.
     * @return This object for method chaining.
     */
    default ChildProcessParameters redirectOutputTo(ByteWriteStream outputStream)
    {
        PreCondition.assertNotNull(outputStream, "outputStream");

        return this.setOutputStreamHandler((ByteReadStream childProcessOutputStream) ->
        {
            outputStream.writeAll(childProcessOutputStream).await();
        });
    }

    /**
     * Get the function that will be invoked to handle the error stream from the executable, or
     * null if no handler has been assigned.
     * @return The function that will be invoked to handle the error stream from the executable, or
     * null if no handler has been assigned.
     */
    Action1<ByteReadStream> getErrorStreamHandler();

    /**
     * Set the function that will be invoked to handle the error stream from the executable.
     * @param errorStreamHandler The function that will be invoked to handle the error stream from
     *                            the executable.
     * @return This object for method chaining.
     */
    ChildProcessParameters setErrorStreamHandler(Action1<ByteReadStream> errorStreamHandler);

    /**
     * Redirect any bytes written to the child process's error stream to the provided errorStream.
     * @param errorStream The error stream that the child process's error stream will be written
     *                     to.
     * @return This object for method chaining.
     */
    default ChildProcessParameters redirectErrorTo(ByteWriteStream errorStream)
    {
        PreCondition.assertNotNull(errorStream, "errorStream");

        return this.setErrorStreamHandler((ByteReadStream childProcessErrorStream) ->
        {
            errorStream.writeAll(childProcessErrorStream).await();
        });
    }
}
