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
//        final java.math.BigDecimal valueDecimal = new java.math.BigDecimal(value);
//        final java.math.BigDecimal scaleDecimal = new java.math.BigDecimal(scale);
//        final java.math.BigDecimal scaledValue = valueDecimal.divide(scaleDecimal);
//        final java.math.BigDecimal roundedScaledValue = scaledValue.setScale(0, RoundingMode.HALF_UP);
//        final java.math.BigDecimal resultDecimal = roundedScaledValue.multiply(scaleDecimal);
//        return resultDecimal.doubleValue();
        return round(value / scale) * scale;
    }
}
