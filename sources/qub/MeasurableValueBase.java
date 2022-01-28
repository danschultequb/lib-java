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

    protected static <TUnit> void throwUnrecognizedUnitsException(TUnit unrecognizedUnits)
    {
        PreCondition.assertNotNull(unrecognizedUnits, "unrecognizedUnits");

        throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(unrecognizedUnits) + ": " + unrecognizedUnits);
    }

    @Override
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

    @Override
    @SuppressWarnings("unchecked")
    public T absoluteValue()
    {
        final double value = this.getValue();
        final T result = (value >= 0 ? (T)this : this.creator.run(-value, this.getUnits()));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result.getValue(), 0, "result.getValue()");
        PostCondition.assertEqual(this.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    @SuppressWarnings("unchecked")
    public T round(double scale)
    {
        PreCondition.assertNotEqual(0, scale, "scale");

        final double thisValue = this.getValue();
        final double roundedValue = Math.round(thisValue, scale);
        final T result = (thisValue == roundedValue ? (T)this : this.creator.run(roundedValue, this.getUnits()));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    @Override
    public T round(MeasurableValue<TUnit> scale)
    {
        PreCondition.assertNotNull(scale, "scale");
        PreCondition.assertNotEqual(0, scale.getValue(), "scale.getValue()");

        final TUnit scaleUnits = scale.getUnits();
        final T convertedLhs = this.convertTo(scaleUnits);
        final double convertedLhsValue = convertedLhs.getValue();
        final double roundedValue = Math.round(convertedLhsValue, scale.getValue());
        final T result = (convertedLhsValue == roundedValue ? convertedLhs : this.creator.run(roundedValue, scaleUnits));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(scale.getUnits(), result.getUnits(), "result.getUnits()");

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
