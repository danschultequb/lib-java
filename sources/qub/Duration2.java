package qub;

/**
 * A period of time.
 */
public interface Duration2 extends ComparableWithError<Duration2>
{
    Duration2 zero = Duration2.seconds(0);
    
    static Duration2 create(double value, DurationUnit units)
    {
        return BasicDuration.create(value, units);
    }

    static Duration2 nanoseconds(double value)
    {
        return Duration2.create(value, DurationUnit.Nanoseconds);
    }

    static Duration2 microseconds(double value)
    {
        return Duration2.create(value, DurationUnit.Microseconds);
    }

    static Duration2 milliseconds(double value)
    {
        return Duration2.create(value, DurationUnit.Milliseconds);
    }

    static Duration2 seconds(double value)
    {
        return Duration2.create(value, DurationUnit.Seconds);
    }

    static Duration2 minutes(double value)
    {
        return Duration2.create(value, DurationUnit.Minutes);
    }

    static Duration2 hours(double value)
    {
        return Duration2.create(value, DurationUnit.Hours);
    }

    static Duration2 days(double value)
    {
        return Duration2.create(value, DurationUnit.Days);
    }

    static Duration2 weeks(double value)
    {
        return Duration2.create(value, DurationUnit.Weeks);
    }

    double getValue();

    DurationUnit getUnits();

    Duration2 convertTo(DurationUnit destinationUnits);

    default Duration2 toNanoseconds()
    {
        return this.convertTo(DurationUnit.Nanoseconds);
    }

    default Duration2 toMicroseconds()
    {
        return this.convertTo(DurationUnit.Microseconds);
    }

    default Duration2 toMilliseconds()
    {
        return this.convertTo(DurationUnit.Milliseconds);
    }

    default Duration2 toSeconds()
    {
        return this.convertTo(DurationUnit.Seconds);
    }

    default Duration2 toMinutes()
    {
        return this.convertTo(DurationUnit.Minutes);
    }

    default Duration2 toHours()
    {
        return this.convertTo(DurationUnit.Hours);
    }

    default Duration2 toDays()
    {
        return this.convertTo(DurationUnit.Days);
    }

    default Duration2 toWeeks()
    {
        return this.convertTo(DurationUnit.Weeks);
    }

    /**
     * Negate this Duration's value.
     * @return The negated Duration.
     */
    default Duration2 negate()
    {
        Duration2 result = this;
        if (result.getValue() != 0)
        {
            result = Duration2.create(-result.getValue(), result.getUnits());
        }
        return result;
    }

    default Duration2 plus(Duration2 rhs)
    {
        Duration2 result = this;
        if (rhs != null && rhs.getValue() != 0)
        {
            final Duration2 convertedRhs = rhs.convertTo(this.getUnits());
            result = Duration2.create(this.getValue() + convertedRhs.getValue(), this.getUnits());
        }
        return result;
    }

    default Duration2 minus(Duration2 rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        return this.plus(rhs.negate());
    }

    default Duration2 times(double rhs)
    {
        return rhs == 1 ? this : Duration2.create(this.getValue() * rhs, this.getUnits());
    }

    default Duration2 dividedBy(double rhs)
    {
        PreCondition.assertNotEqual(0.0, rhs, "rhs");

        final Duration2 result = (rhs == 1 ? this : Duration2.create(this.getValue() / rhs, this.getUnits()));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    default double dividedBy(Duration2 rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");
        PreCondition.assertNotEqual(0, rhs.getValue(), "rhs.getValue()");

        final Duration2 convertedRhs = rhs.convertTo(this.getUnits());
        return this.getValue() / convertedRhs.getValue();
    }

    default Duration2 round()
    {
        final double roundedValue = Math.round(this.getValue());
        return roundedValue == this.getValue() ? this : Duration2.create(roundedValue, this.getUnits());
    }

    default Duration2 round(Duration2 scale)
    {
        PreCondition.assertNotNull(scale, "scale");

        Duration2 result;
        if (scale.getValue() == 0)
        {
            result = (this.getValue() == 0 ? this : Duration2.create(0, this.getUnits()));
        }
        else
        {
            final Duration2 convertedLhs = this.convertTo(scale.getUnits());
            final double roundedValue = Math.round(convertedLhs.getValue(), scale.getValue());
            result = convertedLhs.getValue() == roundedValue ? this : Duration2.create(roundedValue, scale.getUnits());
        }
        return result;
    }

    default Duration2 round(double scale)
    {
        Duration2 result;
        if (scale == 0)
        {
            result = (this.getValue() == 0 ? this : Duration2.create(0, this.getUnits()));
        }
        else
        {
            final double roundedValue = Math.round(this.getValue(), scale);
            result = this.getValue() == roundedValue ? this : Duration2.create(roundedValue, this.getUnits());
        }
        return result;
    }

    /**
     * Get the positive version of this Duration. If this Duration is already positive, then this
     * Duration will be returned.
     * @return The positive version of this Duration.
     */
    default Duration2 absoluteValue()
    {
        return this.getValue() >= 0 ? this : Duration2.create(-this.getValue(), this.getUnits());
    }



    static String toString(Duration2 duration)
    {
        return duration.getValue() + " " + duration.getUnits();
    }

    default String toString(String format)
    {
        PreCondition.assertNotNull(format, "format");

        return new java.text.DecimalFormat(format).format(this.getValue()) + " " + this.getUnits();
    }

    static boolean equals(Duration2 lhs, Object rhs)
    {
        return lhs == rhs || (lhs != null && rhs instanceof Duration2 && lhs.equals((Duration2)rhs));
    }

    default boolean equals(Duration2 rhs)
    {
        return Comparer.equal(this, rhs);
    }

    default Comparison compareTo(Duration2 rhs)
    {
        return this.compareTo(rhs, Duration2.zero);
    }

    @Override
    default Comparison compareTo(Duration2 rhs, Duration2 marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");
        PreCondition.assertGreaterThanOrEqualTo(marginOfError, Duration2.zero, "marginOfError");

        Comparison result = Comparison.GreaterThan;
        if (rhs != null)
        {
            final double thisValue = this.getValue();
            final double convertedRhsValue = rhs.convertTo(this.getUnits()).getValue();
            final double convertedMarginOfErrorValue = marginOfError.convertTo(this.getUnits()).getValue();
            result = Comparison.from(thisValue - convertedRhsValue, convertedMarginOfErrorValue);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
