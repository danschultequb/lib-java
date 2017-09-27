package qub;

/**
 * A set of common functions.
 */
public final class Functions
{
    private Functions()
    {
    }

    /**
     * A function that compares two values to see if they're equal.
     */
    public static Function2<Object,Object,Boolean> equal = new Function2<Object,Object,Boolean>()
    {
        @Override
        public Boolean run(Object arg1, Object arg2)
        {
            return equal(arg1, arg2);
        }
    };

    /**
     * Compare the two values to see if they're equal.
     * @param arg1 The first argument.
     * @param arg2 The second argument.
     * @return Whether or not the two values are equal.
     */
    public static boolean equal(Object arg1, Object arg2)
    {
        return arg1 == arg2 || (arg1 != null && arg1.equals(arg2));
    }
}
