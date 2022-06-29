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
    public static Comparison create(double value)
    {
        return Comparison.create(value, 0);
    }

    /**
     * Convert the provided difference value into a Comparison value.
     * @param value The difference between the two values.
     * @param marginOfError The allowed margin of error for the difference to still be considered
     *                      equal.
     * @return The Comparison value.
     */
    public static Comparison create(double value, double marginOfError)
    {
        PreCondition.assertGreaterThanOrEqualTo(marginOfError, 0, "marginOfError");

        return value < -marginOfError ? LessThan : value <= marginOfError ? Equal : GreaterThan;
    }

    /**
     * Invert the provided {@link Comparison} value.
     * @param value The value to invert.
     */
    public static Comparison invert(Comparison value)
    {
        PreCondition.assertNotNull(value, "value");

        final Comparison result;
        switch (value)
        {
            case LessThan:
                result = Comparison.GreaterThan;
                break;

            case GreaterThan:
                result = Comparison.LessThan;
                break;

            default:
                result = value;
                break;
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
