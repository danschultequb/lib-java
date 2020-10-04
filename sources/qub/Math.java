package qub;

/**
 * A collection of math related functions and function objects.
 */
public interface Math
{
    /**
     * Get the absolute (positive) value of the provided value.
     * @param value The value to get the absolute (positive) value for.
     * @return The absolute (positive) value of the provided value.
     */
    static int absoluteValue(int value)
    {
        return java.lang.Math.abs(value);
    }

    /**
     * Get the absolute (positive) value of the provided value.
     * @param value The value to get the absolute (positive) value for.
     * @return The absolute (positive) value of the provided value.
     */
    static long absoluteValue(long value)
    {
        return java.lang.Math.abs(value);
    }

    /**
     * Get the absolute (positive) value of the provided value.
     * @param value The value to get the absolute (positive) value for.
     * @return The absolute (positive) value of the provided value.
     */
    static float absoluteValue(float value)
    {
        return java.lang.Math.abs(value);
    }

    /**
     * Get the absolute (positive) value of the provided value.
     * @param value The value to get the absolute (positive) value for.
     * @return The absolute (positive) value of the provided value.
     */
    static double absoluteValue(double value)
    {
        return java.lang.Math.abs(value);
    }

    /**
     * Get the minimum value between the two provided integers.
     * @param lhs The first value.
     * @param rhs The second value.
     * @return The minimum value between the two provided integers.
     */
    static int minimum(int lhs, int rhs)
    {
        return java.lang.Math.min(lhs, rhs);
    }

    /**
     * Get the minimum value between the two provided long integers.
     * @param lhs The first value.
     * @param rhs The second value.
     * @return The minimum value between the two provided long integers.
     */
    static long minimum(long lhs, long rhs)
    {
        return java.lang.Math.min(lhs, rhs);
    }

    /**
     * Get the minimum value between the two provided doubles.
     * @param lhs The first value.
     * @param rhs The second value.
     * @return The minimum value between the two provided doubles.
     */
    static double minimum(double lhs, double rhs)
    {
        return java.lang.Math.min(lhs, rhs);
    }

    /**
     * Get the maximum value between the two provided integers.
     * @param lhs The first value.
     * @param rhs The second value.
     * @return The minimum value between the two provided integers.
     */
    static int maximum(int lhs, int rhs)
    {
        return java.lang.Math.max(lhs, rhs);
    }

    /**
     * Get the maximum value between the two provided longs.
     * @param lhs The first value.
     * @param rhs The second value.
     * @return The minimum value between the two provided longs.
     */
    static long maximum(long lhs, long rhs)
    {
        return java.lang.Math.max(lhs, rhs);
    }

    /**
     * Get the maximum value between the two provided integers.
     * @param lhs The first value.
     * @param rhs The second value.
     * @return The minimum value between the two provided integers.
     */
    static double maximum(double lhs, double rhs)
    {
        return java.lang.Math.max(lhs, rhs);
    }

    /**
     * Clip the provided value to the range provided by lowerBound and upperBound.
     * @param lowerBound The lower bound that value cannot be below.
     * @param value The value to clip.
     * @param upperBound The upper bound that value cannot be above.
     * @return The clipped value.
     */
    static int clip(int lowerBound, int value, int upperBound)
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
     * Get whether or not the provided value is odd.
     * @param value The value to check.
     * @return Whether or not the provided value is odd.
     */
    static boolean isOdd(int value)
    {
        return value % 2 != 0;
    }

    /**
     * Get whether or not the provided value is even.
     * @param value The value to check.
     * @return Whether or not the provided value is even.
     */
    static boolean isEven(int value)
    {
        return value % 2 == 0;
    }

    /**
     * If the provided value is a fractional value, then return the next higher integer. If the
     * provided vlaue is an integer, then just return the provided value.
     * @param value The value.
     * @return The ceiling of the value.
     */
    static double ceiling(double value)
    {
        return java.lang.Math.ceil(value);
    }

    /**
     * If the provided value is a fractional value, then return the next lower integer. If the
     * provided vlaue is an integer, then just return the provided value.
     * @param value The value.
     * @return The floor of the value.
     */
    static double floor(double value)
    {
        return java.lang.Math.floor(value);
    }

    /**
     * Round the provided value to the nearest whole number.
     * @param value The value to round.
     * @return The rounded value.
     */
    static double round(double value)
    {
        return java.lang.Math.round(value);
    }

    /**
     * Round the provided value to the nearest multiple of the provided scale value.
     * @param value The value to round.
     * @param scale The scale value to round the value to.
     * @return The rounded value.
     */
    static double round(double value, double scale)
    {
        return round(value / scale) * scale;
    }

    /**
     * Get the remainder when value is divided by scale.
     * @param value The dividend value.
     * @param scale The divisor value.
     * @return The remainder value.
     */
    static int modulo(int value, int scale)
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
    static long modulo(long value, long scale)
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

    /**
     * Get the summation of all of the numbers from 1 to the provided upperBound. The upperBound
     * must be between 0 and 46340 (floor of the square root of 2147483647, which is the maximum
     * positive value for a 32-bit integer).
     * @param upperBound The upperBound of the summation.
     * @return The summation of all of the numbers from 1 to the provided upperBound.
     */
    static int summation(int upperBound)
    {
        PreCondition.assertBetween(0, upperBound, 46340, "upperBound");

        return (upperBound * (upperBound + 1)) / 2;
    }

    /**
     * Get the summation of all of the numbers from 1 to the provided upperBound. The upperBound
     * must be between 0 and 3037000499 (floor of the square root of 9223372036854775807, which is
     * the maximum positive value for a 64-bit integer).
     * @param upperBound The upperBound of the summation.
     * @return The summation of all of the numbers from 1 to the provided upperBound.
     */
    static long summation(long upperBound)
    {
        PreCondition.assertBetween(0, upperBound, 3037000499L, "upperBound");

        return (upperBound * (upperBound + 1)) / 2;
    }
}
