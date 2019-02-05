package qub;

import java.math.RoundingMode;

/**
 * A collection of math related functions and function objects.
 */
public class Math {
    Math()
    {
    }

    /**
     * Get the absolute (positive) value of the provided value.
     * @param value The value to get the absolute (positive) value for.
     * @return The absolute (positive) value of the provided value.
     */
    public static double absoluteValue(double value)
    {
        return value < 0 ? -value : value;
    }

    /**
     * Get the minimum value between the two provided integers.
     * @param lhs The first value.
     * @param rhs The second value.
     * @return The minimum value between the two provided integers.
     */
    public static int minimum(int lhs, int rhs)
    {
        return lhs < rhs ? lhs : rhs;
    }

    /**
     * Get the minimum value between the two provided long integers.
     * @param lhs The first value.
     * @param rhs The second value.
     * @return The minimum value between the two provided long integers.
     */
    public static long minimum(long lhs, long rhs)
    {
        return lhs < rhs ? lhs : rhs;
    }

    /**
     * Get the maximum value between the two provided integers.
     * @param lhs The first value.
     * @param rhs The second value.
     * @return The minimum value between the two provided integers.
     */
    public static int maximum(int lhs, int rhs)
    {
        return lhs > rhs ? lhs : rhs;
    }

    /**
     * Clip the provided value to the range provided by lowerBound and upperBound.
     * @param lowerBound The lower bound that value cannot be below.
     * @param value The value to clip.
     * @param upperBound The upper bound that value cannot be above.
     * @return The clipped value.
     */
    public static int clip(int lowerBound, int value, int upperBound)
    {
        int result;
        if (value < lowerBound)
        {
            result = lowerBound;
        }
        else if (value > upperBound)
        {
            result = upperBound;
        }
        else
        {
            result = value;
        }
        return result;
    }

    /**
     * A function object for determining if an Integer is odd.
     */
    public static final Function1<Integer,Boolean> isOdd = new Function1<Integer,Boolean>() {
        @Override
        public Boolean run(Integer value) {
            return value != null && isOdd(value);
        }
    };

    /**
     * Get whether or not the provided value is odd.
     * @param value The value to check.
     * @return Whether or not the provided value is odd.
     */
    public static boolean isOdd(int value) {
        return value % 2 != 0;
    }

    /**
     * A function object for determining if an Integer is even.
     */
    public static final Function1<Integer,Boolean> isEven = new Function1<Integer,Boolean>() {
        @Override
        public Boolean run(Integer value) {
            return value != null && isEven(value);
        }
    };

    /**
     * Get whether or not the provided value is even.
     * @param value The value to check.
     * @return Whether or not the provided value is even.
     */
    public static boolean isEven(int value) {
        return value % 2 == 0;
    }

    /**
     * If the provided value is a fractional value, then return the next higher integer. If the
     * provided vlaue is an integer, then just return the provided value.
     * @param value The value.
     * @return The ceiling of the value.
     */
    public static double ceiling(double value)
    {
        return java.lang.Math.ceil(value);
    }

    /**
     * If the provided value is a fractional value, then return the next lower integer. If the
     * provided vlaue is an integer, then just return the provided value.
     * @param value The value.
     * @return The floor of the value.
     */
    public static double floor(double value)
    {
        return java.lang.Math.floor(value);
    }

    /**
     * Round the provided value to the nearest whole number.
     * @param value The value to round.
     * @return The rounded value.
     */
    public static double round(double value)
    {
        return java.lang.Math.round(value);
    }

    /**
     * Round the provided value to the nearest multiple of the provided scale value.
     * @param value The value to round.
     * @param scale The scale value to round the value to.
     * @return The rounded value.
     */
    public static double round(double value, double scale)
    {
        return round(value / scale) * scale;
    }

    /**
     * Get the remainder when value is divided by scale.
     * @param value The dividend value.
     * @param scale The divisor value.
     * @return The remainder value.
     */
    public static int modulo(int value, int scale)
    {
        PreCondition.assertNotEqual(0, scale, "scale");

        int result;
        if (0 <= value && 0 <= scale)
        {
            result = value % scale;
        }
        else
        {
            result = value - (scale * (int)Math.floor((double)value / (double)scale));
        }

        PostCondition.assertBetween(0, result, scale, "result");

        return result;
    }

    /**
     * Get the remainder when value is divided by scale.
     * @param value The dividend value.
     * @param scale The divisor value.
     * @return The remainder value.
     */
    public static long modulo(long value, long scale)
    {
        PreCondition.assertNotEqual(0, scale, "scale");

        long result;
        if (0 <= value && 0 <= scale)
        {
            result = value % scale;
        }
        else
        {
            result = value - (scale * (long)Math.floor((double)value / (double)scale));
        }

        PostCondition.assertBetween(0, result, scale, "result");

        return result;
    }
}
