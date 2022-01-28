package qub;

public abstract class MeasurableValueBase<TUnit, T extends MeasurableValue<TUnit>> implements MeasurableValue<TUnit>
{
    private final Function2<Double,TUnit,T> creator;
    private final double value;
    private final TUnit units;

    protected MeasurableValueBase(double value, TUnit units, Function2<Double,TUnit,T> creator)
    {
        PreCondition.assertNotNull(units, "units");
        PreCondition.assertNotNull(creator, "creator");

        this.value = value;
        this.units = units;
        this.creator = creator;
    }

    @Override
    public double getValue()
    {
        return this.value;
    }

    @Override
    public TUnit getUnits()
    {
        return this.units;
    }

    protected abstract double getConversionMultiplier(TUnit units);

    /**
     * Convert this {@link MeasurableValue} to the provided units.
     * @param units The units to convert this {@link MeasurableValue} to.
     * @return The converted {@link MeasurableValue}.
     */
    @SuppressWarnings("unchecked")
    public T convertTo(TUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        T result = (T)this;
        if (!this.getUnits().equals(units))
        {
            final double conversionMultiplier = this.getConversionMultiplier(units);
            if (conversionMultiplier != 1)
            {
                result = this.creator.run(this.getValue() * conversionMultiplier, units);
            }
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(units, result.getUnits(), "result.getUnits()");

        return result;
    }

    /**
     * Get the negated version of this {@link MeasurableValue}.
     * @return The negated version of this {@link MeasurableValue}.
     */
    @SuppressWarnings("unchecked")
    public T negate()
    {
        final T result;
        if (this.getValue() == 0)
        {
            result = (T)this;
        }
        else
        {
            result = this.creator.run(-this.getValue(), this.getUnits());
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    /**
     * Add this {@link MeasurableValue} to the provided {@link MeasurableValue}.
     * @param rhs The {@link MeasurableValue} to add to this {@link MeasurableValue}.
     * @return The sum of this {@link MeasurableValue} and the provided {@link MeasurableValue}.
     */
    @SuppressWarnings("unchecked")
    public T plus(MeasurableValue<TUnit> rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final T result;
        if (rhs.getValue() == 0)
        {
            result = (T)this;
        }
        else
        {
            final TUnit lhsUnits = this.getUnits();
            result = this.creator.run(this.getValue() + rhs.convertTo(lhsUnits).getValue(), lhsUnits);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    /**
     * Subtract the provided {@link MeasurableValue} from this {@link MeasurableValue}.
     * @param rhs The {@link MeasurableValue} to subtract from this {@link MeasurableValue}.
     * @return The difference between this {@link MeasurableValue} and the provided {@link MeasurableValue}.
     */
    @SuppressWarnings("unchecked")
    public T minus(MeasurableValue<TUnit> rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        T result;
        if (rhs.getValue() == 0)
        {
            result = (T)this;
        }
        else
        {
            final TUnit lhsUnits = this.getUnits();
            result = this.creator.run(this.getValue() - rhs.convertTo(lhsUnits).getValue(), lhsUnits);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    /**
     * Multiply this {@link MeasurableValue} by the provided value.
     * @param rhs The value to multiply this {@link MeasurableValue} by.
     * @return The product of multiplying this {@link MeasurableValue} by the provided value.
     */
    @SuppressWarnings("unchecked")
    public T times(double rhs)
    {
        T result;
        if (rhs == 1)
        {
            result = (T)this;
        }
        else
        {
            result = this.creator.run(this.getValue() * rhs, this.getUnits());
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    /**
     * Divide this {@link MeasurableValue} by the provided value.
     * @param rhs The value to divide this {@link MeasurableValue} by.
     * @return The quotient of dividing this {@link MeasurableValue} by the provided value.
     */
    @SuppressWarnings("unchecked")
    public T dividedBy(double rhs)
    {
        PreCondition.assertNotEqual(0, rhs, "rhs");

        T result;
        if (rhs == 1)
        {
            result = (T)this;
        }
        else
        {
            result = this.creator.run(this.getValue() / rhs, this.getUnits());
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    @Override
    public double dividedBy(MeasurableValue<TUnit> rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");
        PreCondition.assertNotEqual(0, rhs.getValue(), "rhs.getValue()");

        return this.getValue() / rhs.convertTo(this.getUnits()).getValue();
    }

    /**
     * Round this {@link MeasurableValue} to the nearest whole number value.
     * @return The nearest whole number value of this {@link MeasurableValue}.
     */
    @SuppressWarnings("unchecked")
    public T round()
    {
        final double value = this.getValue();
        final double roundedValue = Math.round(value);
        final T result;
        if (roundedValue == value)
        {
            result = (T)this;
        }
        else
        {
            result = this.creator.run(roundedValue, this.getUnits());
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    /**
     * Round this {@link MeasurableValue} to the nearest multiple of the provided value.
     * @param value The value to find the nearest multiple of.
     * @return The multiple of the provided value nearest to this {@link MeasurableValue}.
     */
    @SuppressWarnings("unchecked")
    public T round(double value)
    {
        PreCondition.assertNotEqual(0, value, "scale");

        final double thisValue = this.getValue();
        final double roundedValue = Math.round(thisValue, value);
        final T result = (thisValue == roundedValue ? (T)this : this.creator.run(roundedValue, this.getUnits()));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    /**
     * Round this {@link MeasurableValue} to the nearest multiple of the provided {@link MeasurableValue}.
     * @param value The {@link MeasurableValue} to find the nearest multiple of.
     * @return The multiple of the provided {@link MeasurableValue} nearest to this {@link MeasurableValue}.
     */
    public T round(MeasurableValue<TUnit> value)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotEqual(0, value.getValue(), "value.getValue()");

        final TUnit valueUnits = value.getUnits();
        final T convertedLhs = this.convertTo(valueUnits);
        final double convertedLhsValue = convertedLhs.getValue();
        final double roundedValue = Math.round(convertedLhsValue, value.getValue());
        final T result = (convertedLhsValue == roundedValue ? convertedLhs : this.creator.run(roundedValue, valueUnits));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(value.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    @Override
    public Comparison compareTo(MeasurableValue<TUnit> value, MeasurableValue<TUnit> marginOfError)
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

    @Override
    public String toString()
    {
        return this.getValue() + " " + this.getUnits();
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object rhs)
    {
        return this == rhs ||
            (rhs instanceof MeasurableValue && this.equals((MeasurableValue<TUnit>)rhs));
    }
}
