package qub;

/**
 * Describes a type that can be compared to another type.
 * @param <T> The Type that this type can be compared to.
 */
public interface Comparable<T>
{
    /**
     * Compare this value with the provided value.
     * @param value The value to compare this value with.
     * @return The comparison result.
     */
    Comparison compareTo(T value);

    /**
     * Get whether or not this value is less than the provided value.
     * @param value The value to compare this value with.
     * @return Whether or not this value is less than the provided value.
     */
    default boolean lessThan(T value)
    {
        return compareTo(value) == Comparison.LessThan;
    }

    /**
     * Get whether or not this value is less than or equal to the provided value.
     * @param value The value to compare this value with.
     * @return Whether or not this value is less than or equal to the provided value.
     */
    default boolean lessThanOrEqualTo(T value)
    {
        return compareTo(value) != Comparison.GreaterThan;
    }

    /**
     * Get whether or not this value is greater than or equal to the provided value.
     * @param value The value to compare this value with.
     * @return Whether or not this value is greater than or equal to the provided value.
     */
    default boolean greaterThanOrEqualTo(T value)
    {
        return compareTo(value) != Comparison.LessThan;
    }

    /**
     * Get whether or not this value is greater than the provided value.
     * @param value The value to compare this value with.
     * @return Whether or not this value is greater than the provided value.
     */
    default boolean greaterThan(T value)
    {
        return compareTo(value) == Comparison.GreaterThan;
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
        return lhs == rhs || (lhs != null && lhs.compareTo(rhs) == Comparison.Equal);
    }
}
