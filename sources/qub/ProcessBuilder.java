package qub;

/**
 * A builder that can create new Processes.
 */
public class ProcessBuilder
{
    private final ProcessRunner processRunner;
    private final File executableFile;
    private Folder workingFolder;
    private final List<String> arguments;
    private ByteReadStream redirectedInputStream;
    private Action1<ByteReadStream> redirectOutputAction;
    private Action1<ByteReadStream> redirectErrorAction;

    /**
     * Create a new ProcessBuilder with the provided executable file.
     * @param processRunner The object that will invoke the executable file and collect its results.
     * @param executableFile The file to execute.
     */
    ProcessBuilder(ProcessRunner processRunner, File executableFile, Folder workingFolder)
    {
        PreCondition.assertNotNull(processRunner, "processRunner");
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        this.processRunner = processRunner;
        this.executableFile = executableFile;
        this.workingFolder = workingFolder;
        this.arguments = List.create();
    }

    /**
     * Get the file that will be executed by this ProcessBuilder.
     * @return The file that will be executed by this ProcessBuilder.
     */
    public File getExecutableFile()
    {
        return executableFile;
    }

    /**
     * Get the arguments for this ProcessBuilder.
     * @return The arguments for this ProcessBuilder.
     */
    public Iterable<String> getArguments()
    {
        return arguments;
    }

    /**
     * Get the number of arguments in this ProcessBuilder.
     * @return The number of arguments.
     */
    public int getArgumentCount()
    {
        return arguments.getCount();
    }

    /**
     * Get the argument at the provided index.
     * @param index The argument index.
     * @return The argument at the provided index.
     */
    public String getArgument(int index)
    {
        return arguments.get(index);
    }

    /**
     * Add the provided argument to the new ProcessBuilder. Null or empty arguments will be ignored.
     * @param argument The argument to add.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder addArgument(String argument)
    {
        if (argument != null && !argument.isEmpty())
        {
            arguments.add(argument);
        }
        return this;
    }

    /**
     * Add the provided arguments to the new ProcessBuilder. Null or empty arguments will be ignored.
     * @param arguments The arguments to add.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder addArguments(String... arguments)
    {
        return addArguments(Iterable.create(arguments));
    }

    /**
     * Add the provided arguments to the new ProcessBuilder. Null or empty arguments will be ignored.
     * @param arguments The arguments to add.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder addArguments(Iterable<String> arguments)
    {
        if (arguments != null && arguments.any())
        {
            for (final String argument : arguments)
            {
                addArgument(argument);
            }
        }
        return this;
    }

    /**
     * Set the argument at the provided index. If the provided argument is null or empty, then this
     * will remove the argument at that index.
     * @param index The index of the argument to change.
     * @param argument The argument value to set.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder setArgument(int index, String argument)
    {
        if (argument == null)
        {
            removeArgument(index);
        }
        else
        {
            arguments.set(index, argument);
        }
        return this;
    }

    /**
     * Remove the argument at the provided index.
     * @param index The index of the argument to remove.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder removeArgument(int index)
    {
        arguments.removeAt(index);
        return this;
    }

    /**
     * Set the working folder where the created process will run create.
     * @param workingFolder The working folder where the created process will run create.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder setWorkingFolder(Folder workingFolder)
    {
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        this.workingFolder = workingFolder;
        return this;
    }

    /**
     * Get the full command line string that will be executed.
     * @return The full command line string that will be executed.
     */
    public String getCommand()
    {
        final StringBuilder builder = new StringBuilder();

        boolean builderIsEmpty = true;
        if (executableFile != null)
        {
            builderIsEmpty = false;
            builder.append(escapeArgument(executableFile.getPath().toString()));
        }

        for (final String argument : arguments)
        {
            if (!builderIsEmpty)
            {
                builder.append(' ');
            }

            final String escapedArgument = escapeArgument(argument);
            builder.append(escapedArgument);
            builderIsEmpty = false;
        }

        return builder.toString();
    }

    public ProcessBuilder redirectInput(ByteReadStream redirectedInputStream)
    {
        this.redirectedInputStream = redirectedInputStream;
        return this;
    }

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
        return redirectOutput(redirectedOutputStream::writeAllBytes);
    }

    /**
     * Redirect the output stream lines create the processes that are created by this ProcessBuilder.
     * @param onOutputLine The function to call when a process writes a line to its output stream.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectOutputLines(Action1<String> onOutputLine)
    {
        return redirectOutput(byteReadStreamToLineAction(onOutputLine));
    }

    /**
     * Redirect the output stream lines create the processes that are created by this ProcessBuilder
     * to the provided StringBuilder.
     * @param builder The StringBuilder to collect process output in.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectOutputTo(StringBuilder builder)
    {
        return redirectOutputLines(appendLineToStringBuilder(builder));
    }

    /**
     * Redirect the output stream lines create the processes that are created by this ProcessBuilder
     * to the returned StringBuilder.
     * @return This StringBuilder that will collect the output stream..
     */
    public StringBuilder redirectOutput()
    {
        final StringBuilder result = new StringBuilder();
        redirectOutputTo(result);
        return result;
    }

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
        return redirectError(redirectedErrorStream::writeAllBytes);
    }

    /**
     * Redirect the error stream lines create the processes that are created by this ProcessBuilder.
     * @param onErrorLine The function to call when a process writes a line to its error stream.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectErrorLines(Action1<String> onErrorLine)
    {
        return redirectError(byteReadStreamToLineAction(onErrorLine));
    }

    /**
     * Redirect the error stream lines create the processes that are created by this ProcessBuilder to
     * the provided StringBuilder.
     * @param builder The StringBuilder to collect process error in.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectErrorTo(StringBuilder builder)
    {
        return redirectErrorLines(appendLineToStringBuilder(builder));
    }

    /**
     * Redirect the error stream lines create the processes that are created by this ProcessBuilder to
     * the returned StringBuilder.
     * @return This StringBuilder that will collect the error stream..
     */
    public StringBuilder redirectError()
    {
        final StringBuilder result = new StringBuilder();
        redirectErrorTo(result);
        return result;
    }

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

    /**
     * Create a process with this ProcessBuilder's properties and wait for the process to complete.
     * @return The exit code of the Process.
     */
    public Result<Integer> run()
    {
        return processRunner.run(getExecutableFile(), getArguments(), workingFolder, redirectedInputStream, redirectOutputAction, redirectErrorAction);
    }
}
