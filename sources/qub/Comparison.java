package qub;

/**
 * The result of comparing two values.
 */
public enum Comparison
{
    LessThan,

    Equal,

    GreaterThan;

    /**
     * Convert the provided difference value into a Comparison value.
     * @param value The difference between the two values.
     * @return The Comparison value.
     */
    public static Comparison from(double value)
    {
        return Comparison.from(value, 0);
    }

    /**
     * Convert the provided difference value into a Comparison value.
     * @param value The difference between the two values.
     * @param marginOfError The allowed margin of error for the difference to still be considered
     *                      equal.
     * @return The Comparison value.
     */
    public static Comparison from(double value, double marginOfError)
    {
        PreCondition.assertGreaterThanOrEqualTo(marginOfError, 0, "marginOfError");

        return value < -marginOfError ? LessThan : value <= marginOfError ? Equal : GreaterThan;
    }
}
