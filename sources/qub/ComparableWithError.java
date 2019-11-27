package qub;

/**
 * Describes a type that can be compared to another type.
 * @param <T> The Type that this type can be compared to.
 */
public interface ComparableWithError<T> extends Comparable<T>
{
    /**
     * Compare this value with the provided value.
     * @param value The value to compare this value with.
     * @param marginOfError The amount of difference between the values being compared that will
     *                      still be considered equal.
     * @return The comparison result.
     */
    Comparison compareTo(T value, T marginOfError);

    /**
     * Get whether or not this value is less than the provided value.
     * @param value The value to compare this value with.
     * @param marginOfError The amount of difference between the values being compared that will
     *                      still be considered equal.
     * @return Whether or not this value is less than the provided value.
     */
    default boolean lessThan(T value, T marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");

        return this.compareTo(value, marginOfError) == Comparison.LessThan;
    }

    /**
     * Get whether or not this value is less than or equal to the provided value.
     * @param value The value to compare this value with.
     * @param marginOfError The amount of difference between the values being compared that will
     *                      still be considered equal.
     * @return Whether or not this value is less than or equal to the provided value.
     */
    default boolean lessThanOrEqualTo(T value, T marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");

        return this.compareTo(value, marginOfError) != Comparison.GreaterThan;
    }

    /**
     * Get whether or not this value is greater than or equal to the provided value.
     * @param value The value to compare this value with.
     * @param marginOfError The amount of difference between the values being compared that will
     *                      still be considered equal.
     * @return Whether or not this value is greater than or equal to the provided value.
     */
    default boolean greaterThanOrEqualTo(T value, T marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");

        return this.compareTo(value, marginOfError) != Comparison.LessThan;
    }

    /**
     * Get whether or not this value is greater than the provided value.
     * @param value The value to compare this value with.
     * @param marginOfError The amount of difference between the values being compared that will
     *                      still be considered equal.
     * @return Whether or not this value is greater than the provided value.
     */
    default boolean greaterThan(T value, T marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");

        return this.compareTo(value, marginOfError) == Comparison.GreaterThan;
    }

    /**
     * Get whether or not this value equals the provided value with the provided margin of error.
     * @param rhs The value to compare against.
     * @param marginOfError The amount of difference between the values being compared that will
     *                      still be considered equal.
     * @return Whether or not this value equals the provided value.
     */
    default boolean equals(T rhs, T marginOfError)
    {
        return ComparableWithError.equals(this, rhs, marginOfError);
    }

    /**
     * Get whether or not the two values are equal based on their comparison.
     * @param lhs The first value.
     * @param rhs The second value.
     * @param marginOfError The amount of difference between the values being compared that will
     *                      still be considered equal.
     * @param <T> The type of the values.
     * @return Whether or not the two values are equal based on their comparison.
     */
    static <T> boolean equals(ComparableWithError<T> lhs, T rhs, T marginOfError)
    {
        PreCondition.assertNotNull(lhs, "lhs");
        PreCondition.assertNotNull(marginOfError, "marginOfError");

        return lhs.compareTo(rhs, marginOfError) == Comparison.Equal;
    }
}
