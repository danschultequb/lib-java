package qub;

/**
 * An interface for a function that takes two arguments and returns a value.
 * @param <T1> The first type of the argument that this function takes.
 * @param <T2> The second type of the argument that this function takes.
 * @param <TReturn> The type of value that this function returns.
 */
public interface Function2<T1,T2,TReturn>
{
    /**
     * The function to run.
     * @param arg1 The first argument for the function.
     * @param arg2 The second argument for the function.
     * @return The return value of the function.
     */
    TReturn run(T1 arg1, T2 arg2);
}
