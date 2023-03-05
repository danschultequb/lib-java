package qub;

/**
 * A collection of functions that help in interacting with action objects.
 */
public interface Actions
{
    /**
     * Run the provided {@link Action0} if it isn't null.
     * @param action The {@link Action0} to run if it isn't null.
     */
    public static void run(Action0 action)
    {
        if (action != null)
        {
            action.run();
        }
    }

    /**
     * Run the provided {@link Action1} with the provided argument if the {@link Action1} isn't
     * null.
     * @param action The {@link Action1} to run if it isn't null.
     * @param arg1 The argument to pass to the {@link Action1}.
     * @param <T1> The type of the argument.
     */
    public static <T1> void run(Action1<T1> action, T1 arg1)
    {
        if (action != null)
        {
            action.run(arg1);
        }
    }

    /**
     * Run the provided {@link Action2} with the provided arguments if the {@link Action2} isn't
     * null.
     * @param action The {@link Action2} to run if it isn't null.
     * @param arg1 The first argument to pass to the {@link Action2}.
     * @param arg2 The second argument to pass to the {@link Action2}.
     * @param <T1> The type of the first argument.
     * @param <T2> The type of the second argument.
     */
    public static <T1,T2> void run(Action2<T1,T2> action, T1 arg1, T2 arg2)
    {
        if (action != null)
        {
            action.run(arg1, arg2);
        }
    }
}
