package qub;

/**
 * An interface that is responsible for creating and running child processes.
 */
public interface ChildProcessRunner
{
    /**
     * Run a new child process using the provided executablePath. This method will wait for the
     * child process to exit before it returns.
     * @param executablePath The path to the executable to run.
     * @return The exit code of the child process.
     */
    default Result<Integer> run(String executablePath, String... arguments)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.run(Path.parse(executablePath), arguments);
    }

    /**
     * Run a new child process using the provided executablePath. This method will wait for the
     * child process to exit before it returns.
     * @param executablePath The path to the executable to run.
     * @return The exit code of the child process.
     */
    default Result<Integer> run(String executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.run(Path.parse(executablePath), arguments);
    }

    /**
     * Run a new child process using the provided executablePath. This method will wait for the
     * child process to exit before it returns.
     * @param executablePath The path to the executable to run.
     * @return The exit code of the child process.
     */
    default Result<Integer> run(Path executablePath, String... arguments)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.run(executablePath, Iterable.create(arguments));
    }

    /**
     * Run a new child process using the provided executablePath. This method will wait for the
     * child process to exit before it returns.
     * @param executablePath The path to the executable to run.
     * @return The exit code of the child process.
     */
    default Result<Integer> run(Path executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.run(ChildProcessParameters.create(executablePath).addArguments(arguments));
    }

    /**
     * Run a new child process using the provided executableFile. This method will wait for the
     * child process to exit before it returns.
     * @param executableFile The executable file to run.
     * @return The exit code of the child process.
     */
    default Result<Integer> run(File executableFile, String... arguments)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.run(executableFile.getPath(), arguments);
    }

    /**
     * Run a new child process using the provided executableFile. This method will wait for the
     * child process to exit before it returns.
     * @param executableFile The executable file to run.
     * @return The exit code of the child process.
     */
    default Result<Integer> run(File executableFile, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.run(executableFile.getPath(), arguments);
    }

    /**
     * Run a new child process using the provided parameters. This method will wait for the child
     * process to exit before it returns.
     * @param parameters The parameters needed to run the executable.
     * @return The exit code of the child process.
     */
    default Result<Integer> run(ChildProcessParameters parameters)
    {
        PreCondition.assertNotNull(parameters, "parameters");

        return Result.create(() ->
        {
            final ChildProcess childProcess = this.start(parameters).await();
            final Integer result = childProcess.await();

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    /**
     * Start a new child process using the provided executablePath. This method may return before
     * the child process exits.
     * @param executablePath The rooted path to the executable to run.
     * @return A reference to the started child process.
     */
    default Result<? extends ChildProcess> start(String executablePath, String... arguments)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.start(Path.parse(executablePath), arguments);
    }

    /**
     * Start a new child process using the provided executablePath. This method may return before
     * the child process exits.
     * @param executablePath The rooted path to the executable to run.
     * @return A reference to the started child process.
     */
    default Result<? extends ChildProcess> start(String executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.start(Path.parse(executablePath), arguments);
    }

    /**
     * Start a new child process using the provided executablePath. This method may return before
     * the child process exits.
     * @param executablePath The rooted path to the executable to run.
     * @return A reference to the started child process.
     */
    default Result<? extends ChildProcess> start(Path executablePath, String... arguments)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.start(executablePath, Iterable.create(arguments));
    }

    /**
     * Start a new child process using the provided executablePath. This method may return before
     * the child process exits.
     * @param executablePath The rooted path to the executable to run.
     * @return A reference to the started child process.
     */
    default Result<? extends ChildProcess> start(Path executablePath, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.start(ChildProcessParameters.create(executablePath).addArguments(arguments));
    }

    /**
     * Start a new child process using the provided executableFile. This method may return before
     * the child process exits.
     * @param executableFile The executable file to run.
     * @return A reference to the started child process.
     */
    default Result<? extends ChildProcess> start(File executableFile, String... arguments)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.start(executableFile.getPath(), arguments);
    }

    /**
     * Start a new child process using the provided executableFile. This method may return before
     * the child process exits.
     * @param executableFile The executable file to run.
     * @return A reference to the started child process.
     */
    default Result<? extends ChildProcess> start(File executableFile, Iterable<String> arguments)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");

        return this.start(executableFile.getPath(), arguments);
    }

    /**
     * Start a new child process using the provided parameters. This method may return before the
     * child process exits.
     * @param parameters The parameters needed to run the executable.
     * @return A reference to the started child process.
     */
    Result<? extends ChildProcess> start(ChildProcessParameters parameters);

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

        final CharacterList list = CharacterList.create();

        if (workingFolderPath != null)
        {
            list.addAll(ChildProcessRunner.escapeArgument(workingFolderPath.toString()) + ": ");
        }

        list.addAll(ChildProcessRunner.escapeArgument(executablePath.toString()));

        for (final String argument : arguments)
        {
            list.addAll(" " + ChildProcessRunner.escapeArgument(argument));
        }

        final String result = list.toString();

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
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
