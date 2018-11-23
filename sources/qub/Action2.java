package qub;

/**
 * An interface for a function that takes two arguments and doesn't return anything.
 * @param <T1> The first type of argument that this function takes.
 * @param <T2> The second type of argument that this function takes.
 */
public interface Action2<T1,T2>
{
    /**
     * The function to run.
     * @param arg1 The first argument for the function.
     * @param arg2 The second argument for the function.
     */
    void run(T1 arg1, T2 arg2);



    @SafeVarargs
    static <T1,T2> Action2<T1,T2> sequence(Action2<T1,T2>... actions)
    {
        Action2<T1,T2> result = null;
        if (actions != null && actions.length > 0)
        {
            for (final Action2<T1,T2> action : actions)
            {
                if (action != null)
                {
                    if (result == null)
                    {
                        result = action;
                    }
                    else
                    {
                        final Action2<T1,T2> previousResult = result;
                        result = (T1 value1, T2 value2) ->
                        {
                            previousResult.run(value1, value2);
                            action.run(value1, value2);
                        };
                    }
                }
            }
        }
        return result;
    }
}
