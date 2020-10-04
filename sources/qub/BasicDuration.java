package qub;

/**
 * A period of time.
 */
public class BasicDuration implements Duration
{
    public static final int WeeksToDays = 7;
    public static final int DaysToHours = 24;
    public static final int HoursToMinutes = 60;
    public static final int MinutesToSeconds = 60;
    public static final int SecondsToMilliseconds = 1000;
    public static final int MillisecondsToMicroseconds = 1000;
    public static final int MicrosecondsToNanoseconds = 1000;

    public static final int WeeksToHours = WeeksToDays * DaysToHours;
    private static final int WeeksToMinutes = WeeksToHours * HoursToMinutes;
    public static final int WeeksToSeconds = WeeksToMinutes * MinutesToSeconds;
    private static final long WeeksToMilliseconds = WeeksToSeconds * SecondsToMilliseconds;
    private static final long WeeksToMicroseconds = WeeksToMilliseconds * MillisecondsToMicroseconds;
    public static final long WeeksToNanoseconds = WeeksToMicroseconds * MicrosecondsToNanoseconds;

    private static final double DaysToWeeks = 1.0 / WeeksToDays;
    private static final int DaysToMinutes = DaysToHours * HoursToMinutes;
    public static final int DaysToSeconds = DaysToMinutes * MinutesToSeconds;
    private static final long DaysToMilliseconds = DaysToSeconds * SecondsToMilliseconds;
    private static final long DaysToMicroseconds = DaysToMilliseconds * MillisecondsToMicroseconds;
    public static final long DaysToNanoseconds = DaysToMicroseconds * MicrosecondsToNanoseconds;

    private static final double HoursToWeeks = 1.0 / WeeksToHours;
    private static final double HoursToDays = 1.0 / DaysToHours;
    public static final int HoursToSeconds = HoursToMinutes * MinutesToSeconds;
    private static final long HoursToMilliseconds = HoursToSeconds * SecondsToMilliseconds;
    private static final long HoursToMicroseconds = HoursToMilliseconds * MillisecondsToMicroseconds;
    public static final long HoursToNanoseconds = HoursToMicroseconds * MicrosecondsToNanoseconds;

    private static final double MinutesToWeeks = 1.0 / WeeksToMinutes;
    private static final double MinutesToDays = 1.0 / DaysToMinutes;
    private static final double MinutesToHours = 1.0 / HoursToMinutes;
    private static final int MinutesToMilliseconds = MinutesToSeconds * SecondsToMilliseconds;
    private static final long MinutesToMicroseconds = MinutesToMilliseconds * MillisecondsToMicroseconds;
    public static final long MinutesToNanoseconds = MinutesToMicroseconds * MicrosecondsToNanoseconds;

    public static final double SecondsToWeeks = 1.0 / WeeksToSeconds;
    public static final double SecondsToDays = 1.0 / DaysToSeconds;
    public static final double SecondsToHours = 1.0 / HoursToSeconds;
    public static final double SecondsToMinutes = 1.0 / MinutesToSeconds;
    public static final int SecondsToMicroseconds = SecondsToMilliseconds * MillisecondsToMicroseconds;
    public static final int SecondsToNanoseconds = SecondsToMicroseconds * MicrosecondsToNanoseconds;

    private static final double MillisecondsToWeeks = 1.0 / WeeksToMilliseconds;
    private static final double MillisecondsToDays = 1.0 / DaysToMilliseconds;
    private static final double MillisecondsToHours = 1.0 / HoursToMilliseconds;
    private static final double MillisecondsToMinutes = 1.0 / MinutesToMilliseconds;
    public static final double MillisecondsToSeconds = 1.0 / SecondsToMilliseconds;
    public static final int MillisecondsToNanoseconds = MillisecondsToMicroseconds * MicrosecondsToNanoseconds;

    private static final double MicrosecondsToWeeks = 1.0 / WeeksToMicroseconds;
    private static final double MicrosecondsToDays = 1.0 / DaysToMicroseconds;
    private static final double MicrosecondsToHours = 1.0 / HoursToMicroseconds;
    private static final double MicrosecondsToMinutes = 1.0 / MinutesToMicroseconds;
    public static final double MicrosecondsToSeconds = 1.0 / SecondsToMicroseconds;
    private static final double MicrosecondsToMilliseconds = 1.0 / MillisecondsToMicroseconds;

    public static final double NanosecondsToWeeks = 1.0 / WeeksToNanoseconds;
    public static final double NanosecondsToDays = 1.0 / DaysToNanoseconds;
    public static final double NanosecondsToHours = 1.0 / HoursToNanoseconds;
    public static final double NanosecondsToMinutes = 1.0 / MinutesToNanoseconds;
    public static final double NanosecondsToSeconds = 1.0 / SecondsToNanoseconds;
    public static final double NanosecondsToMilliseconds = 1.0 / MillisecondsToNanoseconds;
    public static final double NanosecondsToMicroseconds = 1.0 / MicrosecondsToNanoseconds;

    private final double value;
    private final DurationUnit units;

    private BasicDuration(double value, DurationUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        this.value = value;
        this.units = units;
    }

    public static BasicDuration create(double value, DurationUnit units)
    {
        return new BasicDuration(value, units);
    }

    @Override
    public double getValue()
    {
        return value;
    }

    @Override
    public DurationUnit getUnits()
    {
        return units;
    }

    @Override
    public BasicDuration convertTo(DurationUnit destinationUnits)
    {
        BasicDuration result = this;
        switch (units)
        {
            case Nanoseconds:
                switch (destinationUnits)
                {
                    case Microseconds:
                        result = new BasicDuration(value * NanosecondsToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration(value * NanosecondsToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration(value * NanosecondsToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration(value * NanosecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration(value * NanosecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration(value * NanosecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration(value * NanosecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Microseconds:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration(value * MicrosecondsToNanoseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration(value * MicrosecondsToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration(value * MicrosecondsToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration(value * MicrosecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration(value * MicrosecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration(value * MicrosecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration(value * MicrosecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Milliseconds:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration(value * MillisecondsToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration(value * MillisecondsToMicroseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration(value * MillisecondsToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration(value * MillisecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration(value * MillisecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration(value * MillisecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration(value * MillisecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Seconds:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration(value * SecondsToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration(value * SecondsToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration(value * SecondsToMilliseconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration(value * SecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration(value * SecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration(value * SecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration(value * SecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Minutes:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration(value * MinutesToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration(value * MinutesToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration(value * MinutesToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration(value * MinutesToSeconds, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration(value * MinutesToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration(value * MinutesToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration(value * MinutesToWeeks, destinationUnits);
                        break;
                }
                break;

            case Hours:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration(value * HoursToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration(value * HoursToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration(value * HoursToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration(value * HoursToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration(value * HoursToMinutes, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration(value * HoursToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration(value * HoursToWeeks, destinationUnits);
                        break;
                }
                break;

            case Days:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration(value * DaysToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration(value * DaysToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration(value * DaysToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration(value * DaysToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration(value * DaysToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration(value * DaysToHours, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration(value * DaysToWeeks, destinationUnits);
                        break;
                }
                break;

            case Weeks:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration(value * WeeksToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration(value * WeeksToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration(value * WeeksToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration(value * WeeksToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration(value * WeeksToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration(value * WeeksToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration(value * WeeksToDays, destinationUnits);
                        break;
                }
                break;
        }
        return result;
    }

    @Override
    public String toString()
    {
        return Duration.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Duration.equals(this, rhs);
    }
}
