package qub;

/**
 * An interface for a function that takes three arguments and doesn't return anything.
 * @param <T1> The first type of argument that this function takes.
 * @param <T2> The second type of argument that this function takes.
 * @param <T3> The third type of argument that this function takes.
 */
public interface Action3<T1,T2,T3>
{
    /**
     * The function to run.
     * @param arg1 The first argument for the function.
     * @param arg2 The second argument for the function.
     * @param arg3 The third argument for the function.
     */
    void run(T1 arg1, T2 arg2, T3 arg3);
}
