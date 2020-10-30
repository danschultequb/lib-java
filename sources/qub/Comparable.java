package qub;

/**
 * Describes a type that can be compared to another type.
 * @param <T> The Type that this type can be compared to.
 */
public interface Comparable<T> extends java.lang.Comparable<T>
{
    default int compareTo(T rhs)
    {
        int result = 0;
        switch (this.compareWith(rhs))
        {
            case LessThan:
                result = -1;
                break;

            case GreaterThan:
                result = 1;
                break;
        }
        return result;
    }

    /**
     * Compare this value with the provided value.
     * @param value The value to compare this value with.
     * @return The comparison result.
     */
    Comparison compareWith(T value);

    /**
     * Get whether or not this value is less than the provided value.
     * @param value The value to compare this value with.
     * @return Whether or not this value is less than the provided value.
     */
    default boolean lessThan(T value)
    {
        return this.compareWith(value) == Comparison.LessThan;
    }

    /**
     * Get whether or not this value is less than or equal to the provided value.
     * @param value The value to compare this value with.
     * @return Whether or not this value is less than or equal to the provided value.
     */
    default boolean lessThanOrEqualTo(T value)
    {
        return this.compareWith(value) != Comparison.GreaterThan;
    }

    /**
     * Get whether or not this value is greater than or equal to the provided value.
     * @param value The value to compare this value with.
     * @return Whether or not this value is greater than or equal to the provided value.
     */
    default boolean greaterThanOrEqualTo(T value)
    {
        return this.compareWith(value) != Comparison.LessThan;
    }

    /**
     * Get whether or not this value is greater than the provided value.
     * @param value The value to compare this value with.
     * @return Whether or not this value is greater than the provided value.
     */
    default boolean greaterThan(T value)
    {
        return this.compareWith(value) == Comparison.GreaterThan;
    }

    /**
     * Get whether or not the two values are equal based on their comparison.
     * @param lhs The first value.
     * @param rhs The second value.
     * @param <T> The type of the values.
     * @return Whether or not the two values are equal based on their comparison.
     */
    static <T> boolean equals(Comparable<T> lhs, T rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return lhs.compareWith(rhs) == Comparison.Equal;
    }
}
