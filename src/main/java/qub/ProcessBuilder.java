package qub;

import java.io.IOException;

/**
 * A builder that can create new Processes.
 */
public class ProcessBuilder
{
    private final AsyncRunner asyncRunner;
    private final File executableFile;
    private final List<String> arguments;
    private Action1<ByteReadStream> redirectOutputAction;
    private Action1<ByteReadStream> redirectErrorAction;

    /**
     * Create a new ProcessBuilder with the provided executable file.
     * @param executableFile The file to execute.
     */
    ProcessBuilder(AsyncRunner asyncRunner, File executableFile)
    {
        this.asyncRunner = asyncRunner;
        this.executableFile = executableFile;
        this.arguments = new ArrayList<>();
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
    public List<String> getArguments()
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
        if (arguments != null && arguments.length > 0)
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
            builder.append(executableFile.getPath().toString());
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

    public ProcessBuilder redirectOutput(Action1<ByteReadStream> redirectOutputAction)
    {
        this.redirectOutputAction = redirectOutputAction;
        return this;
    }

    /**
     * Redirect the output stream from the created processes to the provided ByteWriteStream.
     * @param redirectedOutputStream The ByteWriteStream to redirect process output to.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectOutput(final ByteWriteStream redirectedOutputStream)
    {
        return redirectOutput(new Action1<ByteReadStream>()
        {
            @Override
            public void run(ByteReadStream processOutput)
            {
                redirectedOutputStream.writeAll(processOutput);
            }
        });
    }

    /**
     * Redirect the output stream lines from the processes that are created by this ProcessBuilder.
     * @param onOutputLine The function to call when a process writes a line to its output stream.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectOutputLines(Action1<String> onOutputLine)
    {
        return redirectOutput(byteReadStreamToLineAction(onOutputLine));
    }

    /**
     * Redirect the output stream lines from the processes that are created by this ProcessBuilder
     * to the provided StringBuilder.
     * @param builder The StringBuilder to collect process output in.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectOutputTo(StringBuilder builder)
    {
        return redirectOutputLines(appendLineToStringBuilder(builder));
    }

    /**
     * Redirect the output stream lines from the processes that are created by this ProcessBuilder
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
     * Redirect the error stream from the created processes to the provided ByteWriteStream.
     * @param redirectedErrorStream The ByteWriteStream to redirect process error to.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectError(final ByteWriteStream redirectedErrorStream)
    {
        return redirectError(new Action1<ByteReadStream>()
        {
            @Override
            public void run(ByteReadStream processError)
            {
                redirectedErrorStream.writeAll(processError);
            }
        });
    }

    /**
     * Redirect the error stream lines from the processes that are created by this ProcessBuilder.
     * @param onErrorLine The function to call when a process writes a line to its error stream.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectErrorLines(Action1<String> onErrorLine)
    {
        return redirectError(byteReadStreamToLineAction(onErrorLine));
    }

    /**
     * Redirect the error stream lines from the processes that are created by this ProcessBuilder to
     * the provided StringBuilder.
     * @param builder The StringBuilder to collect process error in.
     * @return This ProcessBuilder.
     */
    public ProcessBuilder redirectErrorTo(StringBuilder builder)
    {
        return redirectErrorLines(appendLineToStringBuilder(builder));
    }

    /**
     * Redirect the error stream lines from the processes that are created by this ProcessBuilder to
     * the returned StringBuilder.
     * @return This StringBuilder that will collect the error stream..
     */
    public StringBuilder redirectError()
    {
        final StringBuilder result = new StringBuilder();
        redirectErrorTo(result);
        return result;
    }

    private static Action1<ByteReadStream> byteReadStreamToLineAction(final Action1<String> onLineAction)
    {
        return onLineAction == null ? null : new Action1<ByteReadStream>()
        {
            @Override
            public void run(ByteReadStream byteReadStream)
            {
                final LineReadStream lineReadStream = byteReadStream.asLineReadStream(true);
                String errorLine;
                do
                {
                    errorLine = lineReadStream.readLine();
                    onLineAction.run(errorLine);
                }
                while (errorLine != null);
            }
        };
    }

    private static Action1<String> appendLineToStringBuilder(final StringBuilder builder)
    {
        return builder == null ? null : new Action1<String>()
        {
            @Override
            public void run(String line)
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
     * @return The exit code of the Process, or null if the process couldn't start.
     */
    public Integer run()
    {
        final java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(executableFile.getPath().toString());

        if (arguments.any())
        {
            for (final String argument : arguments)
            {
                builder.command().add(argument);
            }
        }

        if (redirectOutputAction != null)
        {
            builder.redirectOutput();
        }

        if (redirectErrorAction != null)
        {
            builder.redirectError();
        }

        Integer result = null;
        try
        {
            final java.lang.Process process = builder.start();
            AsyncAction outputAction = null;
            AsyncAction errorAction = null;

            if (redirectOutputAction != null)
            {
                outputAction = asyncRunner.schedule(new Action0()
                {
                    @Override
                    public void run()
                    {
                        redirectOutputAction.run(new InputStreamToByteReadStream(process.getInputStream()));
                    }
                });
            }

            if (redirectErrorAction != null)
            {
                errorAction = asyncRunner.schedule(new Action0()
                {
                    @Override
                    public void run()
                    {
                        redirectErrorAction.run(new InputStreamToByteReadStream(process.getErrorStream()));
                    }
                });
            }

            result = process.waitFor();
            if (outputAction != null)
            {
                outputAction.await();
            }
            if (errorAction != null)
            {
                errorAction.await();
            }
        }
        catch (IOException | InterruptedException ignored)
        {
        }

        return result;
    }
}
