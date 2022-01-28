package qub;

/**
 * A period of time.
 */
public interface Duration extends MeasurableValue<DurationUnit>
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

    @Override
    Duration convertTo(DurationUnit units);

    @Override
    Duration absoluteValue();

    @Override
    Duration negate();

    @Override
    Duration plus(MeasurableValue<DurationUnit> rhs);

    @Override
    Duration minus(MeasurableValue<DurationUnit> rhs);

    @Override
    Duration times(double rhs);

    @Override
    Duration dividedBy(double rhs);

    @Override
    Duration round();

    @Override
    Duration round(double scale);

    @Override
    Duration round(MeasurableValue<DurationUnit> scale);

    default Duration toNanoseconds()
    {
        return (Duration)this.convertTo(DurationUnit.Nanoseconds);
    }

    default Duration toMicroseconds()
    {
        return (Duration)this.convertTo(DurationUnit.Microseconds);
    }

    default Duration toMilliseconds()
    {
        return (Duration)this.convertTo(DurationUnit.Milliseconds);
    }

    default Duration toSeconds()
    {
        return (Duration)this.convertTo(DurationUnit.Seconds);
    }

    default Duration toMinutes()
    {
        return (Duration)this.convertTo(DurationUnit.Minutes);
    }

    default Duration toHours()
    {
        return (Duration)this.convertTo(DurationUnit.Hours);
    }

    default Duration toDays()
    {
        return (Duration)this.convertTo(DurationUnit.Days);
    }

    default Duration toWeeks()
    {
        return (Duration)this.convertTo(DurationUnit.Weeks);
    }
}
