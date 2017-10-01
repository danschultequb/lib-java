package qub;

/**
 * A set of comparison functions.
 */
public final class Comparer
{
    Comparer()
    {
    }

    /**
     * Compare the two values to see if they're equals.
     * @param arg1 The first argument.
     * @param arg2 The second argument.
     * @return Whether or not the two values are equals.
     */
    public static boolean equal(Object arg1, Object arg2)
    {
        return arg1 == arg2 || (arg1 != null && arg1.equals(arg2));
    }

    /**
     * Create an equals Function1 that compares other values against the provided lhs value.
     * @param lhs The value to compare to other values.
     * @param <T> The type of the values to compare.
     * @return A Function1 that compares other values against the provided lhs value.
     */
    public static <T> Function1<T,Boolean> equal(final T lhs)
    {
        return new Function1<T,Boolean>()
        {
            @Override
            public Boolean run(T rhs)
            {
                return Comparer.equal(lhs, rhs);
            }
        };
    }
}
