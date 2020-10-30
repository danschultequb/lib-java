package qub;

/**
 * A set of comparison functions.
 */
public interface Comparer<T> extends Function2<T,T,Comparison>
{
    static Comparison compare(int lhs, int rhs)
    {
        return Comparison.create(lhs - rhs);
    }

    static Comparison compare(int lhs, Integer rhs)
    {
        return rhs == null ? Comparison.GreaterThan : Comparer.compare(lhs, rhs.intValue());
    }

    static Comparison compare(Integer lhs, int rhs)
    {
        return lhs == null ? Comparison.LessThan : Comparer.compare(lhs.intValue(), rhs);
    }

    static <U, T extends java.lang.Comparable<U>> Comparison compare(T lhs, U rhs)
    {
        Comparison result;

        if (lhs == rhs)
        {
            result = Comparison.Equal;
        }
        else if (lhs == null)
        {
            result = Comparison.LessThan;
        }
        else if (rhs == null)
        {
            result = Comparison.GreaterThan;
        }
        else
        {
            result = Comparison.create(lhs.compareTo(rhs));
        }

        return result;
    }

    static <T> Comparison compare(Comparable<T> lhs, T rhs)
    {
        return lhs == rhs ? Comparison.Equal :
               lhs == null ? Comparison.LessThan :
               lhs.compareWith(rhs);
    }

    static <T> Comparison compare(ComparableWithError<T> lhs, T rhs, T marginOfError)
    {
        return lhs == rhs ? Comparison.Equal :
               lhs == null ? Comparison.LessThan :
               lhs.compareTo(rhs, marginOfError);
    }

    static <T> boolean equal(Comparable<T> lhs, T rhs)
    {
        return Comparer.compare(lhs, rhs) == Comparison.Equal;
    }

    static <T> boolean equal(ComparableWithError<T> lhs, T rhs, T marginOfError)
    {
        return Comparer.compare(lhs, rhs, marginOfError) == Comparison.Equal;
    }

    /**
     * Compare the two values to see if they're equals.
     * @param arg1 The first argument.
     * @param arg2 The second argument.
     * @return Whether or not the two values are equals.
     */
    static boolean equal(Object arg1, Object arg2)
    {
        boolean result = arg1 == arg2;

        if (!result && arg1 != null && arg2 != null)
        {
            result = arg1.equals(arg2);
            if (!result)
            {
                final Class<?> arg1Type = arg1.getClass();
                final Class<?> arg2Type = arg2.getClass();
                if (arg1Type.equals(arg2Type))
                {
                    if (Types.instanceOf(arg1Type, Throwable.class))
                    {
                        result = Comparer.equal((Throwable)arg1, (Throwable)arg2);
                    }
                    else if (arg1Type.isArray())
                    {
                        if (arg1 instanceof boolean[])
                        {
                            result = Comparer.equal((boolean[])arg1, (boolean[])arg2);
                        }
                        else if (arg1 instanceof byte[])
                        {
                            result = Comparer.equal((byte[])arg1, (byte[])arg2);
                        }
                        else if (arg1 instanceof char[])
                        {
                            result = Comparer.equal((char[])arg1, (char[])arg2);
                        }
                        else if (arg1 instanceof short[])
                        {
                            result = Comparer.equal((short[])arg1, (short[])arg2);
                        }
                        else if (arg1 instanceof int[])
                        {
                            result = Comparer.equal((int[])arg1, (int[])arg2);
                        }
                        else if (arg1 instanceof long[])
                        {
                            result = Comparer.equal((long[])arg1, (long[])arg2);
                        }
                        else if (arg1 instanceof float[])
                        {
                            result = Comparer.equal((float[])arg1, (float[])arg2);
                        }
                        else if (arg1 instanceof double[])
                        {
                            result = Comparer.equal((double[])arg1, (double[])arg2);
                        }
                        else
                        {
                            result = Comparer.equal((Object[]) arg1, (Object[]) arg2);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get whether or not the provided Strings are equal when their casing is ignored.
     * @param arg1 The first String.
     * @param arg2 The second String.
     * @return Whether or not the provided Strings are equal when their casing is ignored.
     */
    static boolean equalIgnoreCase(String arg1, String arg2)
    {
        return arg1 == arg2 || (arg1 != null && arg1.equalsIgnoreCase(arg2));
    }

    /**
     * Get whether or not the provided Throwable errors are equal, ignoring their stack traces.
     * @param arg1 The first Throwable error.
     * @param arg2 The second Throwable error.
     * @return True if they are equal, false if they are not.
     */
    static boolean equal(Throwable arg1, Throwable arg2)
    {
        return Comparer.equal(arg1, arg2, null);
    }

    /**
     * Get whether or not the provided Throwable errors are equal, ignoring their stack traces.
     * @param error1 The first Throwable error.
     * @param error2 The second Throwable error.
     * @param errorTypesToGoPast Error types that will be invested into to see if a matching error exists as the cause
     *                           of the error.
     * @return True if they are equal, false if they are not.
     */
    static boolean equal(Throwable error1, Throwable error2, Iterable<Class<? extends Throwable>> errorTypesToGoPast)
    {
        boolean result = error1 == error2;
        final boolean hasErrorTypesToGoPast = !Iterable.isNullOrEmpty(errorTypesToGoPast);
        while (error1 != null)
        {
            final Throwable error2Backup = error2;
            while (error2 != null)
            {
                result = Comparer.equal(error1.getClass(), error2.getClass()) &&
                    Comparer.equal(error1.getMessage(), error2.getMessage()) &&
                    Comparer.equal(error1.getCause(), error2.getCause());

                if (result || !hasErrorTypesToGoPast || !errorTypesToGoPast.contains(error2.getClass()) || error2.getCause() == error2)
                {
                    error2 = null;
                }
                else
                {
                    error2 = error2.getCause();
                }
            }

            if (result || !hasErrorTypesToGoPast || !errorTypesToGoPast.contains(error1.getClass()) || error1.getCause() == error1)
            {
                error1 = null;
            }
            else
            {
                error2 = error2Backup;
                error1 = error1.getCause();
            }
        }
        return result;
    }

    /**
     * Get whether or not the provided Throwable errors are equal, ignoring their stack traces.
     * @param arg1 The first Throwable error.
     * @param arg2 The second Throwable error.
     * @return True if they are equal, false if they are not.
     */
    static boolean equal(Class<? extends Throwable> arg1, Throwable arg2)
    {
        return Comparer.equal(arg1, arg2, null);
    }

    /**
     * Get whether or not the provided Throwable errors are equal, ignoring their stack traces.
     * @param error1 The first Throwable error.
     * @param error2 The second Throwable error.
     * @param errorTypesToGoPast Error types that will be invested into to see if a matching error exists as the cause
     *                           of the error.
     * @return True if they are equal, false if they are not.
     */
    static boolean equal(Class<? extends Throwable> error1, Throwable error2, Iterable<Class<? extends Throwable>> errorTypesToGoPast)
    {
        boolean result = false;
        final boolean hasErrorTypesToGoPast = !Iterable.isNullOrEmpty(errorTypesToGoPast);
        while (error2 != null)
        {
            result = Comparer.equal(error1, error2.getClass());

            if (result || !hasErrorTypesToGoPast || !errorTypesToGoPast.contains(error2.getClass()) || error2.getCause() == error2)
            {
                error2 = null;
            }
            else
            {
                error2 = error2.getCause();
            }
        }

        return result;
    }

    /**
     * Compare the two values to see if they're equal.
     * @param arg1 The first argument.
     * @param arg2 The second argument.
     * @param marginOfError The margin of error to allow when comparing the two values.
     * @return Whether or not the two values are equals.
     */
    static boolean equal(double arg1, double arg2, double marginOfError)
    {
        PreCondition.assertGreaterThanOrEqualTo(marginOfError, 0, "marginOfError");

        return Math.absoluteValue(arg1 - arg2) <= marginOfError;
    }

    /**
     * Compare the contents of the two arrays to see if they're equal.
     * @param arg1 The first array.
     * @param arg2 The second array.
     * @return Whether or not the two arrays have equal contents in equal order.
     */
    static boolean equal(boolean[] arg1, boolean[] arg2)
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
    static boolean equal(byte[] arg1, byte[] arg2)
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
    static boolean equal(char[] arg1, char[] arg2)
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
    static boolean equal(short[] arg1, short[] arg2)
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
    static boolean equal(int[] arg1, int[] arg2)
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
    static boolean equal(long[] arg1, long[] arg2)
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
    static boolean equal(float[] arg1, float[] arg2)
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
    static boolean equal(double[] arg1, double[] arg2)
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
    static <T> boolean equal(T[] arg1, T[] arg2)
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
    static <T> Function1<T,Boolean> equal(T lhs)
    {
        return (T rhs) -> Comparer.equal(lhs, rhs);
    }

    static <T> boolean same(T arg1, T arg2)
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
    static boolean between(int lowerBound, int value, int upperBound)
    {
        return lowerBound <= value && value <= upperBound;
    }

    /**
     * Return whether or not the provided value is greater than or equal to the provided lowerBound
     * and is less than or equal to the provided upper bound.
     * @param lowerBound The lower bound.
     * @param value The value to compare.
     * @param upperBound The upper bound.
     * @return Whether or not the value is between the provided lower and upper bounds.
     */
    static boolean between(long lowerBound, long value, long upperBound)
    {
        return lowerBound <= value && value <= upperBound;
    }

    /**
     * Return whether or not the provided value is greater than or equal to the provided lowerBound
     * and is less than or equal to the provided upper bound.
     * @param lowerBound The lower bound.
     * @param value The value to compare.
     * @param upperBound The upper bound.
     * @return Whether or not the value is between the provided lower and upper bounds.
     */
    static boolean between(double lowerBound, double value, double upperBound)
    {
        return lowerBound <= value && value <= upperBound;
    }

    /**
     * Return whether or not the provided value is greater than or equal to the provided lowerBound
     * and is less than or equal to the provided upper bound.
     * @param lowerBound The lower bound.
     * @param value The value to compare.
     * @param upperBound The upper bound.
     * @return Whether or not the value is between the provided lower and upper bounds.
     */
    static <T extends Comparable<T>> boolean between(T lowerBound, T value, T upperBound)
    {
        return Comparer.lessThanOrEqualTo(lowerBound, value) &&
            Comparer.lessThanOrEqualTo(value, upperBound);
    }

    /**
     * Get whether or not the provided lhs value is less than the provided rhs value.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @param <T> The type of the values to compare.
     * @return Whether or not the provided lhs value is less than the provided rhs value.
     */
    static <T extends Comparable<T>> boolean lessThan(T lhs, T rhs)
    {
        return lhs == null ? rhs != null : lhs.lessThan(rhs);
    }

    /**
     * Get whether or not the provided lhs value is less than the provided rhs value.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @return Whether or not the provided lhs value is less than the provided rhs value.
     */
    static  boolean lessThan(int lhs, int rhs)
    {
        return lhs < rhs;
    }

    /**
     * Get whether or not the provided lhs value is less than the provided rhs value.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @return Whether or not the provided lhs value is less than the provided rhs value.
     */
    static  boolean lessThan(int lhs, Integer rhs)
    {
        return rhs != null && lessThan(lhs, rhs.intValue());
    }

    /**
     * Get whether or not the provided lhs value is less than the provided rhs value.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @return Whether or not the provided lhs value is less than the provided rhs value.
     */
    static  boolean lessThan(Integer lhs, int rhs)
    {
        return lhs == null || lessThan(lhs.intValue(), rhs);
    }

    /**
     * Get whether or not the provided lhs value is less than the provided rhs value.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @return Whether or not the provided lhs value is less than the provided rhs value.
     */
    static  boolean lessThan(Integer lhs, Integer rhs)
    {
        return lhs == null ? rhs != null : lessThan(lhs.intValue(), rhs);
    }

    /**
     * Get whether or not the provided lhs value is less than or equal to the provided rhs value.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @param <T> The type of the values to compare.
     * @return Whether or not the provided lhs value is less than or equal to the provided rhs
     * value.
     */
    static <T extends Comparable<T>> boolean lessThanOrEqualTo(T lhs, T rhs)
    {
        return lhs == null || lhs.lessThanOrEqualTo(rhs);
    }

    /**
     * Get whether or not the provided lhs value is less than or equal to the provided rhs value.
     * @param value The value to compare.
     * @param upperBound The second value to compare.
     * @return Whether or not the provided value is less than or equal to the provided upperBound.
     */
    static boolean lessThanOrEqualTo(char value, char upperBound)
    {
        return value <= upperBound;
    }

    /**
     * Get whether or not the provided lhs value is less than or equal to the provided rhs value.
     * @param value The value to compare.
     * @param upperBound The second value to compare.
     * @return Whether or not the provided value is less than or equal to the provided upperBound.
     */
    static boolean lessThanOrEqualTo(int value, int upperBound)
    {
        return value <= upperBound;
    }

    /**
     * Get whether or not the provided lhs value is less than or equal to the provided rhs value.
     * @param value The value to compare.
     * @param upperBound The second value to compare.
     * @return Whether or not the provided value is less than or equal to the provided upperBound.
     */
    static boolean lessThanOrEqualTo(Integer value, int upperBound)
    {
        return value == null || lessThanOrEqualTo(value.intValue(), upperBound);
    }

    /**
     * Get whether or not the provided lhs value is less than or equal to the provided rhs value.
     * @param value The value to compare.
     * @param upperBound The second value to compare.
     * @return Whether or not the provided value is less than or equal to the provided upperBound.
     */
    static boolean lessThanOrEqualTo(int value, Integer upperBound)
    {
        return upperBound != null && lessThanOrEqualTo(value, upperBound.intValue());
    }

    /**
     * Get whether or not the provided lhs value is less than or equal to the provided rhs value.
     * @param value The value to compare.
     * @param upperBound The second value to compare.
     * @return Whether or not the provided value is less than or equal to the provided upperBound.
     */
    static boolean lessThanOrEqualTo(Integer value, Integer upperBound)
    {
        return value == null || lessThanOrEqualTo(value.intValue(), upperBound);
    }

    /**
     * Get whether or not the provided lhs value is less than or equal to the provided rhs value.
     * @param value The value to compare.
     * @param upperBound The second value to compare.
     * @return Whether or not the provided value is less than or equal to the provided upperBound.
     */
    static boolean lessThanOrEqualTo(long value, long upperBound)
    {
        return value <= upperBound;
    }

    /**
     * Get whether or not the provided value is greater than or equal to the lowerBound.
     * @param value The value to compare.
     * @param lowerBound The second value to compare.
     * @param <T> The type of the values to compare.
     * @return Whether or not the value is greater than or equal to the provided lowerBound.
     */
    static <T> boolean greaterThanOrEqualTo(Comparable<T> value, T lowerBound)
    {
        return value == lowerBound || (value != null && value.greaterThanOrEqualTo(lowerBound));
    }

    /**
     * Get whether or not the provided value is greater than or equal to the lowerBound.
     * @param value The value to compare.
     * @param lowerBound The second value to compare.
     * @param marginOfError The margin of error allowed
     * @param <T> The type of the values to compare.
     * @return Whether or not the value is greater than or equal to the provided lowerBound.
     */
    static <T> boolean greaterThanOrEqualTo(ComparableWithError<T> value, T lowerBound, T marginOfError)
    {
        return value == lowerBound || (value != null && value.greaterThanOrEqualTo(lowerBound, marginOfError));
    }

    /**
     * Get whether or not the provided value is null or greater than or equal to the lowerBound.
     * @param value The value to compare.
     * @param lowerBound The second value to compare.
     * @param <T> The type of the values to compare.
     * @return Whether or not the value is null or greater than or equal to the provided lowerBound.
     */
    static <T> boolean nullOrGreaterThanOrEqualTo(Comparable<T> value, T lowerBound)
    {
        return value == null || greaterThanOrEqualTo(value, lowerBound);
    }

    /**
     * Get whether or not the provided value is greater than or equal to the lowerBound.
     * @param value The value to compare.
     * @param lowerBound The second value to compare.
     * @return Whether or not the value is greater than or equal to the provided lowerBound.
     */
    static boolean greaterThanOrEqualTo(int value, int lowerBound)
    {
        return value >= lowerBound;
    }

    /**
     * Get whether or not the provided value is greater than or equal to the lowerBound.
     * @param value The value to compare.
     * @param lowerBound The second value to compare.
     * @return Whether or not the value is greater than or equal to the provided lowerBound.
     */
    static boolean greaterThanOrEqualTo(long value, long lowerBound)
    {
        return value >= lowerBound;
    }

    /**
     * Get whether or not the provided value is greater than or equal to the lowerBound.
     * @param value The value to compare.
     * @param lowerBound The second value to compare.
     * @return Whether or not the value is greater than or equal to the provided lowerBound.
     */
    static boolean greaterThanOrEqualTo(double value, double lowerBound)
    {
        return value >= lowerBound;
    }

    /**
     * Get whether or not the provided lhs value is greater than the provided rhs value.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @return Whether or not the provided lhs value is greater than the provided rhs value.
     */
    static boolean greaterThan(int lhs, int rhs)
    {
        return lhs > rhs;
    }

    /**
     * Get whether or not the provided lhs value is greater than the provided rhs value.
     * @param lhs The first value to compare.
     * @param rhs The second value to compare.
     * @param <T> The type of the values to compare.
     * @return Whether or not the provided lhs value is greater than the provided rhs value.
     */
    static <T extends Comparable<T>> boolean greaterThan(T lhs, T rhs)
    {
        return lhs != null && lhs.greaterThan(rhs);
    }

    static <T extends Comparable<T>> T minimum(Iterable<T> values)
    {
        return Comparer.minimum(values, Comparer::compare);
    }

    static <T> T minimum(Iterable<T> values, Function2<T,T,Comparison> comparer)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(comparer, "comparer");

        return values.minimum(comparer);
    }

    @SafeVarargs
    static <T extends Comparable<T>> T maximum(T... values)
    {
        return Comparer.maximum(Iterable.create(values));
    }

    static <T extends Comparable<T>> T maximum(Iterable<T> values)
    {
        return Comparer.maximum(values, Comparer::compare);
    }

    static <T> T maximum(Iterable<T> values, Function2<T,T,Comparison> comparer)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(comparer, "comparer");

        return values.maximum(comparer);
    }

    /**
     * Get whether or not the provided value contains only the provided characters. It doesn't have
     * to contain all of the characters and it can contain multiple instances of each character, but
     * each character in the provided value must be contained in the provided set of characters.
     * @param value The value to check.
     * @param characters The characters to allow.
     * @preCondition value != null
     * @preCondition characters != null && characters.length > 0
     */
    static boolean containsOnly(String value, char[] characters)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(characters, "characters");

        boolean result = true;
        final int valueLength = value.length();
        for (int i = 0; i < valueLength; ++i)
        {
            final char valueCharacter = value.charAt(i);
            if (!Array.contains(characters, valueCharacter))
            {
                result = false;
                break;
            }
        }

        return result;
    }
}
