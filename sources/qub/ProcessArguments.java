package qub;

/**
 * An interface for adding and viewing arguments added to a process.
 */
public interface ProcessArguments<T>
{
    /**
     * Get the path to the executable that this ProcessBuilder will invoke.
     * @return The path to the executable that this ProcessBuilder will invoke.
     */
    Path getExecutablePath();

    /**
     * Get the path to the folder that this ProcessBuilder will run the executable in.
     * @return The path to the folder that this ProcessBuilder will run the executable in.
     */
    Path getWorkingFolderPath();

    /**
     * Add the provided argument to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param argument The argument to add.
     * @return This object for method chaining.
     */
    T addArgument(String argument);

    /**
     * Add the provided arguments to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    T addArguments(String... arguments);

    /**
     * Add the provided arguments to the list of arguments that will be provided to the executable
     * when this ProcessBuilder is run.
     * @param arguments The arguments to add.
     * @return This object for method chaining.
     */
    T addArguments(Iterable<String> arguments);

    /**
     * Get the arguments that this ProcessBuilder will provide to the executable.
     * @return The arguments that this ProcessBuilder will provide to the executable.
     */
    Iterable<String> getArguments();
}
