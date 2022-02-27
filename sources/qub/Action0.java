package qub;

/**
 * An interface for a function that takes no arguments and doesn't return anything.
 */
public interface Action0 extends Runnable
{
    /**
     * An {@link Action0} that does nothing.
     */
    Action0 empty = () -> {};

    /**
     * The function to run.
     */
    void run();

    /**
     * Add the provided parameter type to convert this {@link Action0} to an {@link Action1}.
     * @param parameterType The parameter type to add.
     * @param <T> The type of the parameter to add.
     * @return The new {@link Action1}.
     */
    default <T> Action1<T> add(Class<T> parameterType)
    {
        PreCondition.assertNotNull(parameterType, "parameterType");

        return (T arg) -> { this.run(); };
    }

    /**
     * Add the provided parameter types to convert this {@link Action0} to an {@link Action2}.
     * @param parameterType1 The first parameter type to add.
     * @param parameterType2 The second parameter type to add.
     * @param <T1> The type of the first parameter to add.
     * @param <T2> The type of the second parameter to add.
     * @return The new {@link Action2}.
     */
    default <T1,T2> Action2<T1,T2> add(Class<T1> parameterType1, Class<T2> parameterType2)
    {
        PreCondition.assertNotNull(parameterType1, "parameterType1");
        PreCondition.assertNotNull(parameterType2, "parameterType2");

        return (T1 arg1, T2 arg2) -> { this.run(); };
    }
}
