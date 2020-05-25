package qub;

/**
 * An interface that is responsible for creating and running ProcessBuilder objects.
 */
public interface ProcessFactory
{
    /**
     * Get the path to the folder that executables will be "run" from.
     * @return The path to the folder that executables will be "run" from.
     */
    Path getWorkingFolderPath();

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    default Result<ProcessBuilder> getProcessBuilder(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return this.getProcessBuilder(Path.parse(executablePath));
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    Result<ProcessBuilder> getProcessBuilder(Path executablePath);

    /**
     * Get the ProcessBuilder for the provided executable file.
     * @param executableFile The file to execute.
     * @return The ProcessBuilder for the provided executable file.
     */
    default Result<ProcessBuilder> getProcessBuilder(File executableFile)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");

        return this.getProcessBuilder(executableFile.getPath());
    }

    /**
     * Run the provided executable with the provided arguments. This method will wait for the
     * started child process to complete before returning.
     * @param executablePath The executable to run.
     * @param arguments The arguments to provide to the executable.
     * @param workingFolderPath The folder that the process will be executed from.
     * @param redirectedInputStream The input stream that the new process will use.
     * @param redirectedOutputStream The action that will be invoked each time the process writes to its output stream.
     * @param redirectedErrorStream The action that will be invoked each time the process writes to its error stream.
     * @param verbose The CharacterWriteStream that verbose logs will be written to.
     * @return The exit code of the created child process.
     */
    default Result<Integer> run(Path executablePath, Iterable<String> arguments, Path workingFolderPath, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectedOutputStream, Action1<ByteReadStream> redirectedErrorStream, CharacterWriteStream verbose)
    {
        return this.start(executablePath, arguments, workingFolderPath, redirectedInputStream, redirectedOutputStream, redirectedErrorStream, verbose)
            .then(ChildProcess::await);
    }

    /**
     * Start the provided executable with the provided arguments. This method will not wait for the
     * started child process to complete before returning.
     * @param executablePath The executable to run.
     * @param arguments The arguments to provide to the executable.
     * @param workingFolderPath The folder that the process will be executed from.
     * @param redirectedInputStream The input stream that the new process will use.
     * @param redirectedOutputStream The action that will be invoked each time the process writes to its output stream.
     * @param redirectedErrorStream The action that will be invoked each time the process writes to its error stream.
     * @param verbose The CharacterWriteStream that verbose logs will be written to.
     * @return A reference to the created child process.
     */
    Result<ChildProcess> start(Path executablePath, Iterable<String> arguments, Path workingFolderPath, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectedOutputStream, Action1<ByteReadStream> redirectedErrorStream, CharacterWriteStream verbose);

    /**
     * Get the full command line string that will be run.
     * @param executablePath The path to the executable.
     * @param arguments The arguments to provide to the executable file.
     * @param workingFolderPath The folder path that the executable file will be run in.
     * @return The full command line string that will be run.
     */
    static String getCommand(Path executablePath, Iterable<String> arguments, Path workingFolderPath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        final StringBuilder builder = new StringBuilder();

        if (workingFolderPath != null)
        {
            builder.append(escapeArgument(workingFolderPath.toString()) + ": ");
        }

        builder.append(escapeArgument(executablePath.toString()));

        for (final String argument : arguments)
        {
            builder.append(" " + escapeArgument(argument));
        }

        return builder.toString();
    }

    /**
     * Escape the provided argument so that it will be understood as a single argument.
     * @param argument The argument to escape.
     * @return The escaped argument.
     */
    static String escapeArgument(String argument)
    {
        PreCondition.assertNotNullAndNotEmpty(argument, "argument");

        final StringBuilder builder = new StringBuilder();
        final int argumentLength = argument.length();

        boolean originalArgumentIsQuoted = false;
        boolean addQuoteToEnd = false;
        for (int i = 0; i < argumentLength; ++i)
        {
            final char c = argument.charAt(i);
            switch (c)
            {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    builder.append(c);
                    if (!originalArgumentIsQuoted && !addQuoteToEnd)
                    {
                        builder.insert(0, '\"');
                        addQuoteToEnd = true;
                    }
                    break;

                case '\"':
                    if (i == 0)
                    {
                        originalArgumentIsQuoted = true;
                        builder.append(c);
                    }
                    else if (i == argumentLength - 1 && originalArgumentIsQuoted)
                    {
                        builder.append(c);
                    }
                    else
                    {
                        if (!originalArgumentIsQuoted && !addQuoteToEnd)
                        {
                            builder.insert(0, '\"');
                            addQuoteToEnd = true;
                        }
                        builder.append("\\\"");
                    }
                    break;

                default:
                    builder.append(c);
                    break;
            }
        }

        if (addQuoteToEnd)
        {
            builder.append('\"');
        }

        return builder.toString();
    }
}
