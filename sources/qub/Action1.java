package qub;

/**
 * An interface for a function that takes one argument and returns a value.
 * @param <T1> The type of argument that this function takes.
 */
public interface Action1<T1>
{
    /**
     * The function to run.
     * @param arg1 The argument for the function.
     */
    void run(T1 arg1);
}
