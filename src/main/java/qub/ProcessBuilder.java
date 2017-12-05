package qub;

import java.io.IOException;

/**
 * A builder that can create new Processes.
 */
public class ProcessBuilder
{
    private final File executableFile;
    private final List<String> arguments;

    /**
     * Create a new ProcessBuilder with the provided executable file.
     * @param executableFile The file to execute.
     */
    ProcessBuilder(File executableFile)
    {
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
        if (argument == null || argument.isEmpty())
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

        Integer result = null;
        try
        {
            final java.lang.Process process = builder.start();
            result = process.waitFor();
        }
        catch (IOException | InterruptedException ignored)
        {
        }

        return result;
    }
}
