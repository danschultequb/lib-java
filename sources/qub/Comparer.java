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
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null)
        {
            result = arg1.equals(arg2);
            if (!result)
            {
                final Class<?> arg1Type = arg1.getClass();
                final Class<?> arg2Type = arg2.getClass();
                if (arg1Type.equals(arg2Type) && arg1Type.isArray())
                {
                    if (arg1 instanceof boolean[])
                    {
                        result = equal((boolean[])arg1, (boolean[])arg2);
                    }
                    else if (arg1 instanceof byte[])
                    {
                        result = equal((byte[])arg1, (byte[])arg2);
                    }
                    else if (arg1 instanceof char[])
                    {
                        result = equal((char[])arg1, (char[])arg2);
                    }
                    else if (arg1 instanceof short[])
                    {
                        result = equal((short[])arg1, (short[])arg2);
                    }
                    else if (arg1 instanceof int[])
                    {
                        result = equal((int[])arg1, (int[])arg2);
                    }
                    else if (arg1 instanceof long[])
                    {
                        result = equal((long[])arg1, (long[])arg2);
                    }
                    else if (arg1 instanceof float[])
                    {
                        result = equal((float[])arg1, (float[])arg2);
                    }
                    else if (arg1 instanceof double[])
                    {
                        result = equal((double[])arg1, (double[])arg2);
                    }
                    else
                    {
                        result = equal((Object[]) arg1, (Object[]) arg2);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Compare the two values to see if they're equals.
     * @param arg1 The first argument.
     * @param arg2 The second argument.
     * @param marginOfError The margin of error to allow when comparing the two values.
     * @return Whether or not the two values are equals.
     */
    public static boolean equal(double arg1, double arg2, double marginOfError)
    {
        return Math.absoluteValue(arg1 - arg2) <= marginOfError;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    public static boolean equal(boolean[] arg1, boolean[] arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null && arg1.length == arg2.length)
        {
            result = true;
            for (int i = 0; i < arg1.length; ++i)
            {
                if (arg1[i] != arg2[i])
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    public static boolean equal(byte[] arg1, byte[] arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null && arg1.length == arg2.length)
        {
            result = true;
            for (int i = 0; i < arg1.length; ++i)
            {
                if (arg1[i] != arg2[i])
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    public static boolean equal(char[] arg1, char[] arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null && arg1.length == arg2.length)
        {
            result = true;
            for (int i = 0; i < arg1.length; ++i)
            {
                if (!Comparer.equal(arg1[i], arg2[i]))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    public static boolean equal(short[] arg1, short[] arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null && arg1.length == arg2.length)
        {
            result = true;
            for (int i = 0; i < arg1.length; ++i)
            {
                if (!Comparer.equal(arg1[i], arg2[i]))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    public static boolean equal(int[] arg1, int[] arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null && arg1.length == arg2.length)
        {
            result = true;
            for (int i = 0; i < arg1.length; ++i)
            {
                if (!Comparer.equal(arg1[i], arg2[i]))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    public static boolean equal(long[] arg1, long[] arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null && arg1.length == arg2.length)
        {
            result = true;
            for (int i = 0; i < arg1.length; ++i)
            {
                if (!Comparer.equal(arg1[i], arg2[i]))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    public static boolean equal(float[] arg1, float[] arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null && arg1.length == arg2.length)
        {
            result = true;
            for (int i = 0; i < arg1.length; ++i)
            {
                if (!Comparer.equal(arg1[i], arg2[i]))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    public static boolean equal(double[] arg1, double[] arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null && arg1.length == arg2.length)
        {
            result = true;
            for (int i = 0; i < arg1.length; ++i)
            {
                if (!Comparer.equal(arg1[i], arg2[i]))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @param <T> The type of elements stored in the two arrays.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    public static <T> boolean equal(T[] arg1, T[] arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null && arg1.length == arg2.length)
        {
            result = true;
            for (int i = 0; i < arg1.length; ++i)
            {
                if (!Comparer.equal(arg1[i], arg2[i]))
                {
                    result = false;
                    break;
                }
            }
        }

        return result;
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

    public static <T> boolean same(T arg1, T arg2)
    {
        return arg1 == arg2;
    }

    /**
     * Return whether or not the provided value is greater than or equal to the provided lowerBound
     * and is less than or equal to the provided upper bound.
     * @param lowerBound The lower bound.
     * @param value The value to compare.
     * @param upperBound The upper bound.
     * @return Whether or not the value is between the provided lower and upper bounds.
     */
    public static boolean between(int lowerBound, int value, int upperBound)
    {
        return lowerBound <= value && value <= upperBound;
    }
}
