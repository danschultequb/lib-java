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
     * Add the provided parameter type to convert this {@link Action1} to an {@link Action2}.
     * @param parameterType The parameter type to add.
     * @param <T2> The type of the parameter to add.
     * @return The new {@link Action2}.
     */
    default <T2> Action2<T1,T2> add(Class<T2> parameterType)
    {
        PreCondition.assertNotNull(parameterType, "parameterType");

        return (T1 arg1, T2 arg2) -> { this.run(arg1); };
    }

    /**
     * Insert the provided parameter type before this {@link Action1}'s existing parameter to
     * convert this {@link Action1} to an {@link Action2}.
     * @param parameterType The type of parameter to insert.
     * @param <T0> The type of parameter to insert.
     * @return The new {@link Action2}.
     */
    default <T0> Action2<T0,T1> insert0(Class<T0> parameterType)
    {
        PreCondition.assertNotNull(parameterType, "parameterType");

        return (T0 arg0, T1 arg1) -> { this.run(arg1); };
    }

    /**
     * Bind the provided value to this {@link Action1}'s parameter to convert this {@link Action1}
     * to an {@link Action0}.
     * @param argument The value to bind.
     * @return The new {@link Action0}.
     */
    default Action0 bind1(T1 argument)
    {
        return () -> { this.run(argument); };
    }

    /**
     * Bind the provided return value to this {@link Action1} to convert this {@link Action1} to a
     * {@link Function1}.
     * @param returnValue The value to bind.
     * @param <TReturn> The type of value to return.
     * @return The new {@link Function1}.
     */
    default <TReturn> Function1<T1,TReturn> bindReturn(TReturn returnValue)
    {
        return (T1 arg1) ->
        {
            this.run(arg1);
            return returnValue;
        };
    }

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
