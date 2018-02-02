package qub;

/**
 * An interface for a function that takes no arguments and returns a value.
 * @param <TReturn> The type of value that this function returns.
 */
public interface Function0<TReturn>
{
    /**
     * The function to run.
     * @return The return value of the function.
     */
    TReturn run();
}
