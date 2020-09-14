package qub;

/**
 * A period of time.
 */
@Deprecated
public class BasicDuration2 implements Duration2
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
    private static final int WeeksToSeconds = WeeksToMinutes * MinutesToSeconds;
    private static final long WeeksToMilliseconds = WeeksToSeconds * SecondsToMilliseconds;
    private static final long WeeksToMicroseconds = WeeksToMilliseconds * MillisecondsToMicroseconds;
    private static final long WeeksToNanoseconds = WeeksToMicroseconds * MicrosecondsToNanoseconds;

    private static final double DaysToWeeks = 1.0 / WeeksToDays;
    private static final int DaysToMinutes = DaysToHours * HoursToMinutes;
    private static final int DaysToSeconds = DaysToMinutes * MinutesToSeconds;
    private static final long DaysToMilliseconds = DaysToSeconds * SecondsToMilliseconds;
    private static final long DaysToMicroseconds = DaysToMilliseconds * MillisecondsToMicroseconds;
    private static final long DaysToNanoseconds = DaysToMicroseconds * MicrosecondsToNanoseconds;

    private static final double HoursToWeeks = 1.0 / WeeksToHours;
    private static final double HoursToDays = 1.0 / DaysToHours;
    private static final int HoursToSeconds = HoursToMinutes * MinutesToSeconds;
    private static final long HoursToMilliseconds = HoursToSeconds * SecondsToMilliseconds;
    private static final long HoursToMicroseconds = HoursToMilliseconds * MillisecondsToMicroseconds;
    private static final long HoursToNanoseconds = HoursToMicroseconds * MicrosecondsToNanoseconds;

    private static final double MinutesToWeeks = 1.0 / WeeksToMinutes;
    private static final double MinutesToDays = 1.0 / DaysToMinutes;
    private static final double MinutesToHours = 1.0 / HoursToMinutes;
    private static final int MinutesToMilliseconds = MinutesToSeconds * SecondsToMilliseconds;
    private static final long MinutesToMicroseconds = MinutesToMilliseconds * MillisecondsToMicroseconds;
    private static final long MinutesToNanoseconds = MinutesToMicroseconds * MicrosecondsToNanoseconds;

    private static final double SecondsToWeeks = 1.0 / WeeksToSeconds;
    private static final double SecondsToDays = 1.0 / DaysToSeconds;
    private static final double SecondsToHours = 1.0 / HoursToSeconds;
    private static final double SecondsToMinutes = 1.0 / MinutesToSeconds;
    public static final int SecondsToMicroseconds = SecondsToMilliseconds * MillisecondsToMicroseconds;
    public static final int SecondsToNanoseconds = SecondsToMicroseconds * MicrosecondsToNanoseconds;

    private static final double MillisecondsToWeeks = 1.0 / WeeksToMilliseconds;
    private static final double MillisecondsToDays = 1.0 / DaysToMilliseconds;
    private static final double MillisecondsToHours = 1.0 / HoursToMilliseconds;
    private static final double MillisecondsToMinutes = 1.0 / MinutesToMilliseconds;
    private static final double MillisecondsToSeconds = 1.0 / SecondsToMilliseconds;
    public static final int MillisecondsToNanoseconds = MillisecondsToMicroseconds * MicrosecondsToNanoseconds;

    private static final double MicrosecondsToWeeks = 1.0 / WeeksToMicroseconds;
    private static final double MicrosecondsToDays = 1.0 / DaysToMicroseconds;
    private static final double MicrosecondsToHours = 1.0 / HoursToMicroseconds;
    private static final double MicrosecondsToMinutes = 1.0 / MinutesToMicroseconds;
    private static final double MicrosecondsToSeconds = 1.0 / SecondsToMicroseconds;
    private static final double MicrosecondsToMilliseconds = 1.0 / MillisecondsToMicroseconds;

    private static final double NanosecondsToWeeks = 1.0 / WeeksToNanoseconds;
    private static final double NanosecondsToDays = 1.0 / DaysToNanoseconds;
    private static final double NanosecondsToHours = 1.0 / HoursToNanoseconds;
    private static final double NanosecondsToMinutes = 1.0 / MinutesToNanoseconds;
    private static final double NanosecondsToSeconds = 1.0 / SecondsToNanoseconds;
    private static final double NanosecondsToMilliseconds = 1.0 / MillisecondsToNanoseconds;
    private static final double NanosecondsToMicroseconds = 1.0 / MicrosecondsToNanoseconds;

    private final double value;
    private final DurationUnit units;

    private BasicDuration2(double value, DurationUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        this.value = value;
        this.units = units;
    }

    public static BasicDuration2 create(double value, DurationUnit units)
    {
        return new BasicDuration2(value, units);
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
    public BasicDuration2 convertTo(DurationUnit destinationUnits)
    {
        BasicDuration2 result = this;
        switch (units)
        {
            case Nanoseconds:
                switch (destinationUnits)
                {
                    case Microseconds:
                        result = new BasicDuration2(value * NanosecondsToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration2(value * NanosecondsToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration2(value * NanosecondsToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration2(value * NanosecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration2(value * NanosecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration2(value * NanosecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration2(value * NanosecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Microseconds:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration2(value * MicrosecondsToNanoseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration2(value * MicrosecondsToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration2(value * MicrosecondsToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration2(value * MicrosecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration2(value * MicrosecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration2(value * MicrosecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration2(value * MicrosecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Milliseconds:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration2(value * MillisecondsToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration2(value * MillisecondsToMicroseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration2(value * MillisecondsToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration2(value * MillisecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration2(value * MillisecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration2(value * MillisecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration2(value * MillisecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Seconds:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration2(value * SecondsToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration2(value * SecondsToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration2(value * SecondsToMilliseconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration2(value * SecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration2(value * SecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration2(value * SecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration2(value * SecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Minutes:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration2(value * MinutesToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration2(value * MinutesToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration2(value * MinutesToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration2(value * MinutesToSeconds, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration2(value * MinutesToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration2(value * MinutesToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration2(value * MinutesToWeeks, destinationUnits);
                        break;
                }
                break;

            case Hours:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration2(value * HoursToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration2(value * HoursToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration2(value * HoursToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration2(value * HoursToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration2(value * HoursToMinutes, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration2(value * HoursToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration2(value * HoursToWeeks, destinationUnits);
                        break;
                }
                break;

            case Days:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration2(value * DaysToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration2(value * DaysToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration2(value * DaysToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration2(value * DaysToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration2(value * DaysToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration2(value * DaysToHours, destinationUnits);
                        break;

                    case Weeks:
                        result = new BasicDuration2(value * DaysToWeeks, destinationUnits);
                        break;
                }
                break;

            case Weeks:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new BasicDuration2(value * WeeksToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new BasicDuration2(value * WeeksToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new BasicDuration2(value * WeeksToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new BasicDuration2(value * WeeksToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new BasicDuration2(value * WeeksToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new BasicDuration2(value * WeeksToHours, destinationUnits);
                        break;

                    case Days:
                        result = new BasicDuration2(value * WeeksToDays, destinationUnits);
                        break;
                }
                break;
        }
        return result;
    }

    @Override
    public String toString()
    {
        return Duration2.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Duration2.equals(this, rhs);
    }
}
