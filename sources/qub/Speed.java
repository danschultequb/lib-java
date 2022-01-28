package qub;

public class Speed implements Comparable<Speed>
{
    private final double value;
    private final DistanceUnit distanceUnits;
    private final DurationUnit durationUnits;

    private Speed(double value, DistanceUnit distanceUnits, DurationUnit durationUnits)
    {
        PreCondition.assertNotNull(distanceUnits, "distanceUnits");
        PreCondition.assertNotNull(durationUnits, "durationUnits");

        this.value = value;
        this.distanceUnits = distanceUnits;
        this.durationUnits = durationUnits;
    }

    public static Speed create(double value, DistanceUnit distanceUnits, DurationUnit durationUnits)
    {
        return new Speed(value, distanceUnits, durationUnits);
    }

    public static Speed milesPerHour(double value)
    {
        return Speed.create(value, DistanceUnit.Miles, DurationUnit.Hours);
    }

    public static Speed kilometersPerHour(double value)
    {
        return Speed.create(value, DistanceUnit.Kilometers, DurationUnit.Hours);
    }

    public static Speed feetPerSecond(double value)
    {
        return Speed.create(value, DistanceUnit.Feet, DurationUnit.Seconds);
    }

    public static Speed metersPerSecond(double value)
    {
        return Speed.create(value, DistanceUnit.Meters, DurationUnit.Seconds);
    }

    public final static Speed zero = Speed.milesPerHour(0);

    public double getValue()
    {
        return this.value;
    }

    public DistanceUnit getDistanceUnits()
    {
        return this.distanceUnits;
    }

    public DurationUnit getDurationUnits()
    {
        return this.durationUnits;
    }

    public Speed convertTo(DistanceUnit distanceUnits)
    {
        return this.convertTo(distanceUnits, this.durationUnits);
    }

    public Speed convertTo(DurationUnit durationUnits)
    {
        return this.convertTo(this.distanceUnits, durationUnits);
    }

    public Speed convertTo(DistanceUnit distanceUnits, DurationUnit durationUnits)
    {
        PreCondition.assertNotNull(distanceUnits, "distanceUnits");
        PreCondition.assertNotNull(durationUnits, "durationUnits");

        double value = this.value;

        if (this.distanceUnits != distanceUnits)
        {
            value = Distance.create(value, this.distanceUnits).convertTo(distanceUnits).getValue();
        }
        if (this.durationUnits != durationUnits)
        {
            value = Duration.create(value, durationUnits).convertTo(this.durationUnits).getValue();
        }

        final Speed result = this.value == value && this.distanceUnits == distanceUnits && this.durationUnits == durationUnits
            ? this
            : Speed.create(value, distanceUnits, durationUnits);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(distanceUnits, result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(durationUnits, result.getDurationUnits(), "result.getDurationUnits()");

        return result;
    }

    public Speed toMilesPerHour()
    {
        return this.convertTo(DistanceUnit.Miles, DurationUnit.Hours);
    }

    public Speed toMetersPerSecond()
    {
        return this.convertTo(DistanceUnit.Meters, DurationUnit.Seconds);
    }

    public Speed toKilometersPerHour()
    {
        return this.convertTo(DistanceUnit.Kilometers, DurationUnit.Hours);
    }

    public Speed negate()
    {
        return this.value == 0
            ? this
            : Speed.create(-this.value, this.distanceUnits, this.durationUnits);
    }

    public Speed plus(Speed rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final Speed result = rhs.value == 0
            ? this
            : Speed.create(this.value + rhs.convertTo(this.distanceUnits, this.durationUnits).value, this.distanceUnits, this.durationUnits);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(this.getDurationUnits(), result.getDurationUnits(), "result.getDurationUnits()");

        return result;
    }

    public Speed minus(Speed rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final Speed result = rhs.value == 0
            ? this
            : Speed.create(this.value - rhs.convertTo(this.distanceUnits, this.durationUnits).value, this.distanceUnits, this.durationUnits);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(this.getDurationUnits(), result.getDurationUnits(), "result.getDurationUnits()");

        return result;
    }

    public Speed times(double rhs)
    {
        Speed result;
        if (rhs == 0)
        {
            if (this.value == 0)
            {
                result = this;
            }
            else
            {
                result = Speed.create(0, this.distanceUnits, this.durationUnits);
            }
        }
        else if (rhs == 1)
        {
            result = this;
        }
        else
        {
            result = Speed.create(this.value * rhs, this.distanceUnits, this.durationUnits);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(this.getDurationUnits(), result.getDurationUnits(), "result.getDurationUnits()");

        return result;
    }

    public Distance times(Duration rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final Distance result = Distance.create(this.value * rhs.convertTo(this.durationUnits).getValue(), this.distanceUnits);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    public Speed dividedBy(double rhs)
    {
        PreCondition.assertNotEqual(0, rhs, "rhs");

        Speed result;
        if (rhs == 1)
        {
            result = this;
        }
        else
        {
            result = Speed.create(this.value / rhs, this.distanceUnits, this.durationUnits);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(this.getDurationUnits(), result.getDurationUnits(), "result.getDurationUnits()");

        return result;
    }

    public double dividedBy(Speed rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");
        PreCondition.assertNotEqual(0, rhs.getValue(), "rhs.getValue()");

        return this.value == 0
            ? 0
            : this.value / rhs.convertTo(this.distanceUnits, this.durationUnits).value;
    }

    public Speed round()
    {
        final double roundedValue = Math.round(value);
        return roundedValue == value ? this : Speed.create(roundedValue, this.distanceUnits, this.durationUnits);
    }

    public Speed round(Speed scale)
    {
        PreCondition.assertNotNull(scale, "scale");
        PreCondition.assertNotEqual(0, scale.getValue(), "scale.getValue()");

        final Speed convertedLhs = this.convertTo(scale.getDistanceUnits(), scale.getDurationUnits());
        final double roundedValue = Math.round(convertedLhs.value, scale.value);
        final Speed result = convertedLhs.value == roundedValue ? convertedLhs : Speed.create(roundedValue, scale.getDistanceUnits(), scale.getDurationUnits());

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(scale.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(scale.getDurationUnits(), result.getDurationUnits(), "result.getDurationUnits()");

        return result;
    }

    @Override
    public String toString()
    {
        return value + " " + this.distanceUnits + "/" + this.durationUnits;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Speed && this.equals((Speed)rhs);
    }

    public boolean equals(Speed rhs)
    {
        return rhs != null && rhs.convertTo(this.distanceUnits, this.durationUnits).value == this.value;
    }

    @Override
    public int hashCode()
    {
        return Doubles.hashCode(this.toMetersPerSecond().value);
    }

    @Override
    public Comparison compareWith(Speed rhs)
    {
        return rhs == null
            ? Comparison.GreaterThan
            : Comparison.create(this.value - rhs.convertTo(this.distanceUnits, this.durationUnits).value);
    }
}
