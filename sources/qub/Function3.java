package qub;

/**
 * An interface for a function that takes two arguments and returns a value.
 * @param <T1> The first type of the argument that this function takes.
 * @param <T2> The second type of the argument that this function takes.
 * @param <T3> The third type of argument that this function takes.
 * @param <TReturn> The type of value that this function returns.
 */
public interface Function3<T1,T2,T3,TReturn>
{
    /**
     * The function to run.
     * @param arg1 The first argument for the function.
     * @param arg2 The second argument for the function.
     * @param arg3 The third argument for the function.
     * @return The return value of the function.
     */
    TReturn run(T1 arg1, T2 arg2, T3 arg3);
}
