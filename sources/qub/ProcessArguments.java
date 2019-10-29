package qub;

/**
 * An interface for adding and viewing arguments added to a process.
 */
public interface ProcessArguments<T>
{
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
