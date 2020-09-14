package qub;

/**
 * A period of time.
 */
public interface Duration extends ComparableWithError<Duration>
{
    Duration zero = Duration.seconds(0);
    
    static Duration create(double value, DurationUnit units)
    {
        return BasicDuration.create(value, units);
    }

    static Duration nanoseconds(double value)
    {
        return Duration.create(value, DurationUnit.Nanoseconds);
    }

    static Duration microseconds(double value)
    {
        return Duration.create(value, DurationUnit.Microseconds);
    }

    static Duration milliseconds(double value)
    {
        return Duration.create(value, DurationUnit.Milliseconds);
    }

    static Duration seconds(double value)
    {
        return Duration.create(value, DurationUnit.Seconds);
    }

    static Duration minutes(double value)
    {
        return Duration.create(value, DurationUnit.Minutes);
    }

    static Duration hours(double value)
    {
        return Duration.create(value, DurationUnit.Hours);
    }

    static Duration days(double value)
    {
        return Duration.create(value, DurationUnit.Days);
    }

    static Duration weeks(double value)
    {
        return Duration.create(value, DurationUnit.Weeks);
    }

    double getValue();

    DurationUnit getUnits();

    Duration convertTo(DurationUnit destinationUnits);

    default Duration toNanoseconds()
    {
        return this.convertTo(DurationUnit.Nanoseconds);
    }

    default Duration toMicroseconds()
    {
        return this.convertTo(DurationUnit.Microseconds);
    }

    default Duration toMilliseconds()
    {
        return this.convertTo(DurationUnit.Milliseconds);
    }

    default Duration toSeconds()
    {
        return this.convertTo(DurationUnit.Seconds);
    }

    default Duration toMinutes()
    {
        return this.convertTo(DurationUnit.Minutes);
    }

    default Duration toHours()
    {
        return this.convertTo(DurationUnit.Hours);
    }

    default Duration toDays()
    {
        return this.convertTo(DurationUnit.Days);
    }

    default Duration toWeeks()
    {
        return this.convertTo(DurationUnit.Weeks);
    }

    /**
     * Negate this Duration's value.
     * @return The negated Duration.
     */
    default Duration negate()
    {
        Duration result = this;
        if (result.getValue() != 0)
        {
            result = Duration.create(-result.getValue(), result.getUnits());
        }
        return result;
    }

    default Duration plus(Duration rhs)
    {
        Duration result = this;
        if (rhs != null && rhs.getValue() != 0)
        {
            final Duration convertedRhs = rhs.convertTo(this.getUnits());
            result = Duration.create(this.getValue() + convertedRhs.getValue(), this.getUnits());
        }
        return result;
    }

    default Duration minus(Duration rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        return this.plus(rhs.negate());
    }

    default Duration times(double rhs)
    {
        return rhs == 1 ? this : Duration.create(this.getValue() * rhs, this.getUnits());
    }

    default Duration dividedBy(double rhs)
    {
        PreCondition.assertNotEqual(0.0, rhs, "rhs");

        final Duration result = (rhs == 1 ? this : Duration.create(this.getValue() / rhs, this.getUnits()));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    default double dividedBy(Duration rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");
        PreCondition.assertNotEqual(0, rhs.getValue(), "rhs.getValue()");

        final Duration convertedRhs = rhs.convertTo(this.getUnits());
        return this.getValue() / convertedRhs.getValue();
    }

    default Duration round()
    {
        final double roundedValue = Math.round(this.getValue());
        return roundedValue == this.getValue() ? this : Duration.create(roundedValue, this.getUnits());
    }

    default Duration round(Duration scale)
    {
        PreCondition.assertNotNull(scale, "scale");

        Duration result;
        if (scale.getValue() == 0)
        {
            result = (this.getValue() == 0 ? this : Duration.create(0, this.getUnits()));
        }
        else
        {
            final Duration convertedLhs = this.convertTo(scale.getUnits());
            final double roundedValue = Math.round(convertedLhs.getValue(), scale.getValue());
            result = convertedLhs.getValue() == roundedValue ? this : Duration.create(roundedValue, scale.getUnits());
        }
        return result;
    }

    default Duration round(double scale)
    {
        Duration result;
        if (scale == 0)
        {
            result = (this.getValue() == 0 ? this : Duration.create(0, this.getUnits()));
        }
        else
        {
            final double roundedValue = Math.round(this.getValue(), scale);
            result = this.getValue() == roundedValue ? this : Duration.create(roundedValue, this.getUnits());
        }
        return result;
    }

    /**
     * Get the positive version of this Duration. If this Duration is already positive, then this
     * Duration will be returned.
     * @return The positive version of this Duration.
     */
    default Duration absoluteValue()
    {
        return this.getValue() >= 0 ? this : Duration.create(-this.getValue(), this.getUnits());
    }



    static String toString(Duration duration)
    {
        return duration.getValue() + " " + duration.getUnits();
    }

    default String toString(String format)
    {
        PreCondition.assertNotNull(format, "format");

        return new java.text.DecimalFormat(format).format(this.getValue()) + " " + this.getUnits();
    }

    static boolean equals(Duration lhs, Object rhs)
    {
        return lhs == rhs || (lhs != null && (rhs instanceof Duration && lhs.equals((Duration)rhs)));
    }

    default boolean equals(Duration rhs)
    {
        return this.compareTo(rhs) == Comparison.Equal;
    }

    default Comparison compareTo(Duration rhs)
    {
        return this.compareTo(rhs, Duration.zero);
    }

    @Override
    default Comparison compareTo(Duration rhs, Duration marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");
        PreCondition.assertGreaterThanOrEqualTo(marginOfError, Duration.zero, "marginOfError");

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
