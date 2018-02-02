package qub;

/**
 * An interface for a function that takes one argument and returns a value.
 * @param <T1> The type of the argument that this function takes.
 * @param <TReturn> The type of value that this function returns.
 */
public interface Function1<T1,TReturn>
{
    /**
     * The function to run.
     * @param arg1 The argument for the function.
     * @return The return value of the function.
     */
    TReturn run(T1 arg1);
}
