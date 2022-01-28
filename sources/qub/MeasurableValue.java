package qub;

/**
 * An interface for values that are based on a type of measurable units.
 * @param <TUnit> The type of units that this value uses.
 */
public interface MeasurableValue<TUnit> extends ComparableWithError<MeasurableValue<TUnit>>
{
    /**
     * Get the scalar value of this {@link MeasurableValue}.
     * @return The scalar value of this {@link MeasurableValue}.
     */
    double getValue();

    /**
     * Get the units of this {@link MeasurableValue}.
     * @return The units of this {@link MeasurableValue}.
     */
    TUnit getUnits();

    /**
     * Convert this {@link MeasurableValue} to the provided units.
     * @param units The units to convert this {@link MeasurableValue} to.
     * @return The converted {@link MeasurableValue}.
     */
    MeasurableValue<TUnit> convertTo(TUnit units);

    /**
     * Get the negated version of this {@link MeasurableValue}.
     * @return The negated version of this {@link MeasurableValue}.
     */
    MeasurableValue<TUnit> negate();

    /**
     * Add this {@link MeasurableValue} to the provided {@link MeasurableValue}.
     * @param rhs The {@link MeasurableValue} to add to this {@link MeasurableValue}.
     * @return The sum of this {@link MeasurableValue} and the provided {@link MeasurableValue}.
     */
    MeasurableValue<TUnit> plus(MeasurableValue<TUnit> rhs);

    /**
     * Subtract the provided {@link MeasurableValue} from this {@link MeasurableValue}.
     * @param rhs The {@link MeasurableValue} to subtract from this {@link MeasurableValue}.
     * @return The difference between this {@link MeasurableValue} and the provided {@link MeasurableValue}.
     */
    MeasurableValue<TUnit> minus(MeasurableValue<TUnit> rhs);

    /**
     * Multiply this {@link MeasurableValue} by the provided value.
     * @param rhs The value to multiply this {@link MeasurableValue} by.
     * @return The product of multiplying this {@link MeasurableValue} by the provided value.
     */
    MeasurableValue<TUnit> times(double rhs);

    /**
     * Divide this {@link MeasurableValue} by the provided value.
     * @param rhs The value to divide this {@link MeasurableValue} by.
     * @return The quotient of dividing this {@link MeasurableValue} by the provided value.
     */
    MeasurableValue<TUnit> dividedBy(double rhs);

    /**
     * Divide this {@link MeasurableValue} by the provided {@link MeasurableValue}.
     * @param rhs The {@link MeasurableValue} to divide this {@link MeasurableValue} by.
     * @return The quotient of dividing this {@link MeasurableValue} by the provided {@link MeasurableValue}.
     */
    double dividedBy(MeasurableValue<TUnit> rhs);

    /**
     * Round this {@link MeasurableValue} to the nearest whole number value.
     * @return The nearest whole number value of this {@link MeasurableValue}.
     */
    MeasurableValue<TUnit> round();

    /**
     * Round this {@link MeasurableValue} to the nearest multiple of the provided value.
     * @param value The value to find the nearest multiple of.
     * @return The multiple of the provided value nearest to this {@link MeasurableValue}.
     */
    MeasurableValue<TUnit> round(double value);

    /**
     * Round this {@link MeasurableValue} to the nearest multiple of the provided {@link MeasurableValue}.
     * @param value The {@link MeasurableValue} to find the nearest multiple of.
     * @return The multiple of the provided {@link MeasurableValue} nearest to this {@link MeasurableValue}.
     */
    MeasurableValue<TUnit> round(MeasurableValue<TUnit> value);

    default String toString(String format)
    {
        PreCondition.assertNotNull(format, "format");

        return new java.text.DecimalFormat(format).format(this.getValue()) + " " + this.getUnits();
    }

    default boolean equals(MeasurableValue<TUnit> rhs)
    {
        return rhs != null && this.getValue() == rhs.convertTo(this.getUnits()).getValue();
    }

    @Override
    default Comparison compareWith(MeasurableValue<TUnit> value)
    {
        Comparison result;
        if (value == null)
        {
            result = Comparison.GreaterThan;
        }
        else
        {
            result = Comparison.create(this.getValue() - value.convertTo(this.getUnits()).getValue());
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    default Comparison compareTo(MeasurableValue<TUnit> value, MeasurableValue<TUnit> marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");

        Comparison result;
        if (value == null)
        {
            result = Comparison.GreaterThan;
        }
        else
        {
            final TUnit units = this.getUnits();
            result = Comparison.create(this.getValue() - value.convertTo(units).getValue(), marginOfError.convertTo(units).getValue());
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
