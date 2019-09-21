package qub;

/**
 * A factory that produces ProcessBuilder objects.
 */
public interface ProcessRunner
{
    /**
     * Run the provided executableFile with the provided arguments.
     * @param executableFile The executable file to run.
     * @param arguments The arguments to provide to the executable file.
     * @param workingFolder The folder that the process will be executed from.
     * @param redirectedInputStream The input stream that the new process will use.
     * @param redirectedOutputStream The action that will be invoked each time the process writes to its output stream.
     * @param redirectedErrorStream The action that will be invoked each time the process writes to its error stream.
     * @return The exit code of the process.
     */
    Result<Integer> run(File executableFile, Iterable<String> arguments, Folder workingFolder, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectedOutputStream, Action1<ByteReadStream> redirectedErrorStream);

    /**
     * Get the full command line string that will be run.
     * @param executableFile The file to run.
     * @param arguments The arguments to provide to the executable file.
     * @return The full command line string that will be run.
     */
    static String getCommand(File executableFile, Iterable<String> arguments)
    {
        return ProcessRunner.getCommand(executableFile, arguments, null);
    }

    /**
     * Get the full command line string that will be run.
     * @param executableFile The file to run.
     * @param arguments The arguments to provide to the executable file.
     * @param workingFolder The folder that the executable file will be run in.
     * @return The full command line string that will be run.
     */
    static String getCommand(File executableFile, Iterable<String> arguments, Folder workingFolder)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");

        final StringBuilder builder = new StringBuilder();

        if (workingFolder != null)
        {
            builder.append(workingFolder.toString() + ": ");
        }

        builder.append(escapeArgument(executableFile.getPath().toString()));

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
        final StringBuilder builder = new StringBuilder();
        final int argumentLength = argument.length();
        if (argumentLength == 0)
        {
            builder.append("\"\"");
        }
        else
        {
            boolean originalArgumentIsQuoted = false;
            boolean addQuoteToEnd = false;
            for (int i = 0; i < argumentLength; ++i)
            {
                final char c = argument.charAt(i);
                switch (c)
                {
                    case ' ':
                    case '\t':
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
                        else if ((i != argumentLength - 1 && originalArgumentIsQuoted) || addQuoteToEnd)
                        {
                            builder.append("\\\"");
                        }
                        else
                        {
                            builder.append(c);
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
        }

        return builder.toString();
    }
}
