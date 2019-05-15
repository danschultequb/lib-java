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

    /**
     * Create a new Action1 that will run the provided actions in sequence when it is invoked.
     * @param actions The actions to run in sequence.
     * @param <T1> The type of argument the Action1s take.
     * @return An Action1 that will run the provided Action1s in sequence.
     */
    @SafeVarargs
    static <T1> Action1<T1> sequence(Action1<T1>... actions)
    {
        PreCondition.assertNotNullAndNotEmpty(actions, "actions");

        Action1<T1> result = null;

        for (final Action1<T1> action : actions)
        {
            if (action != null)
            {
                if (result == null)
                {
                    result = action;
                }
                else
                {
                    final Action1<T1> previousResult = result;
                    result = (T1 value1) ->
                    {
                        previousResult.run(value1);
                        action.run(value1);
                    };
                }
            }
        }

        if (result == null)
        {
            result = (T1 arg1) -> {};
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
