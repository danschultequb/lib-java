package qub;

/**
 * A collection of functions that help in interacting with function objects.
 */
public interface Functions
{
    /**
     * Get a function that will invoke the provided function and negate its result.
     * @param function The function to wrap.
     * @return A function that will invoke the provided function and negate its result.
     */
    static Function0<Boolean> not(Function0<Boolean> function)
    {
        PreCondition.assertNotNull(function, "function");

        return () -> !function.run();
    }

    /**
     * Get a function that will invoke the provided function and negate its result.
     * @param function The function to wrap.
     * @return A function that will invoke the provided function and negate its result.
     */
    static <T1> Function1<T1,Boolean> not(Function1<T1,Boolean> function)
    {
        PreCondition.assertNotNull(function, "function");

        return (T1 arg1) -> !function.run(arg1);
    }

    /**
     * Get a function that will invoke the provided function and negate its result.
     * @param function The function to wrap.
     * @return A function that will invoke the provided function and negate its result.
     */
    static <T1,T2> Function2<T1,T2,Boolean> not(Function2<T1,T2,Boolean> function)
    {
        PreCondition.assertNotNull(function, "function");

        return (T1 arg1, T2 arg2) -> !function.run(arg1, arg2);
    }

    /**
     * Get a function that will invoke the provided function and negate its result.
     * @param function The function to wrap.
     * @return A function that will invoke the provided function and negate its result.
     */
    static <T1,T2,T3> Function3<T1,T2,T3,Boolean> not(Function3<T1,T2,T3,Boolean> function)
    {
        PreCondition.assertNotNull(function, "function");

        return (T1 arg1, T2 arg2, T3 arg3) -> !function.run(arg1, arg2, arg3);
    }
}
