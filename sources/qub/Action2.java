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

    /**
     * Create a new Action2 that will run the provided actions in sequence when it is invoked.
     * @param actions The actions to run in sequence.
     * @param <T1> The first type of argument the Action2s take.
     * @param <T2> The second type of argument the Action2s take.
     * @return An Action2 that will run the provided Action2s in sequence.
     */
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

        if (result == null)
        {
            result = (T1 arg1, T2 arg2) -> {};
        }

        return result;
    }
}
