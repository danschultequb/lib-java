package qub;

/**
 * A period of time.
 */
public class Duration
{
    private static final double WeeksToDays = 7.0;
    private static final double DaysToHours = 24.0;
    private static final double HoursToMinutes = 60.0;
    private static final double MinutesToSeconds = 60.0;
    private static final double SecondsToMilliseconds = 1000.0;
    private static final double MillisecondsToMicroseconds = 1000.0;
    private static final double MicrosecondsToNanoseconds = 1000.0;

    private static final double WeeksToHours = WeeksToDays * DaysToHours;
    private static final double WeeksToMinutes = WeeksToHours * HoursToMinutes;
    private static final double WeeksToSeconds = WeeksToMinutes * MinutesToSeconds;
    private static final double WeeksToMilliseconds = WeeksToSeconds * SecondsToMilliseconds;
    private static final double WeeksToMicroseconds = WeeksToMilliseconds * MillisecondsToMicroseconds;
    private static final double WeeksToNanoseconds = WeeksToMicroseconds * MicrosecondsToNanoseconds;

    private static final double DaysToWeeks = 1.0 / WeeksToDays;
    private static final double DaysToMinutes = DaysToHours * HoursToMinutes;
    private static final double DaysToSeconds = DaysToMinutes * MinutesToSeconds;
    private static final double DaysToMilliseconds = DaysToSeconds * SecondsToMilliseconds;
    private static final double DaysToMicroseconds = DaysToMilliseconds * MillisecondsToMicroseconds;
    private static final double DaysToNanoseconds = DaysToMicroseconds * MicrosecondsToNanoseconds;

    private static final double HoursToWeeks = 1.0 / WeeksToHours;
    private static final double HoursToDays = 1.0 / DaysToHours;
    private static final double HoursToSeconds = HoursToMinutes * MinutesToSeconds;
    private static final double HoursToMilliseconds = HoursToSeconds * SecondsToMilliseconds;
    private static final double HoursToMicroseconds = HoursToMilliseconds * MillisecondsToMicroseconds;
    private static final double HoursToNanoseconds = HoursToMicroseconds * MicrosecondsToNanoseconds;

    private static final double MinutesToWeeks = 1.0 / WeeksToMinutes;
    private static final double MinutesToDays = 1.0 / DaysToMinutes;
    private static final double MinutesToHours = 1.0 / HoursToMinutes;
    private static final double MinutesToMilliseconds = MinutesToSeconds * SecondsToMilliseconds;
    private static final double MinutesToMicroseconds = MinutesToMilliseconds * MillisecondsToMicroseconds;
    private static final double MinutesToNanoseconds = MinutesToMicroseconds * MicrosecondsToNanoseconds;

    private static final double SecondsToWeeks = 1.0 / WeeksToSeconds;
    private static final double SecondsToDays = 1.0 / DaysToSeconds;
    private static final double SecondsToHours = 1.0 / HoursToSeconds;
    private static final double SecondsToMinutes = 1.0 / MinutesToSeconds;
    private static final double SecondsToMicroseconds = SecondsToMilliseconds * MillisecondsToMicroseconds;
    private static final double SecondsToNanoseconds = SecondsToMicroseconds * MicrosecondsToNanoseconds;

    private static final double MillisecondsToWeeks = 1.0 / WeeksToMilliseconds;
    private static final double MillisecondsToDays = 1.0 / DaysToMilliseconds;
    private static final double MillisecondsToHours = 1.0 / HoursToMilliseconds;
    private static final double MillisecondsToMinutes = 1.0 / MinutesToMilliseconds;
    private static final double MillisecondsToSeconds = 1.0 / SecondsToMilliseconds;
    private static final double MillisecondsToNanoseconds = MillisecondsToMicroseconds * MicrosecondsToNanoseconds;

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

    public static Duration nanoseconds(double value)
    {
        return new Duration(value, DurationUnits.Nanoseconds);
    }

    public static Duration microseconds(double value)
    {
        return new Duration(value, DurationUnits.Microseconds);
    }

    public static Duration milliseconds(double value)
    {
        return new Duration(value, DurationUnits.Milliseconds);
    }

    public static Duration seconds(double value)
    {
        return new Duration(value, DurationUnits.Seconds);
    }

    public static Duration minutes(double value)
    {
        return new Duration(value, DurationUnits.Minutes);
    }

    public static Duration hours(double value)
    {
        return new Duration(value, DurationUnits.Hours);
    }

    public static Duration days(double value)
    {
        return new Duration(value, DurationUnits.Days);
    }

    public static Duration weeks(double value)
    {
        return new Duration(value, DurationUnits.Weeks);
    }

    private final double value;
    private final DurationUnits units;

    public Duration(double value, DurationUnits units)
    {
        this.value = value;
        this.units = units;
    }

    public double getValue()
    {
        return value;
    }

    public DurationUnits getUnits()
    {
        return units;
    }

    public Duration convertTo(DurationUnits destinationUnits)
    {
        Duration result = this;
        switch (units)
        {
            case Nanoseconds:
                switch (destinationUnits)
                {
                    case Microseconds:
                        result = new Duration(value * NanosecondsToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new Duration(value * NanosecondsToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new Duration(value * NanosecondsToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new Duration(value * NanosecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new Duration(value * NanosecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new Duration(value * NanosecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new Duration(value * NanosecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Microseconds:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new Duration(value * MicrosecondsToNanoseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new Duration(value * MicrosecondsToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new Duration(value * MicrosecondsToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new Duration(value * MicrosecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new Duration(value * MicrosecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new Duration(value * MicrosecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new Duration(value * MicrosecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Milliseconds:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new Duration(value * MillisecondsToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new Duration(value * MillisecondsToMicroseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new Duration(value * MillisecondsToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new Duration(value * MillisecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new Duration(value * MillisecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new Duration(value * MillisecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new Duration(value * MillisecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Seconds:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new Duration(value * SecondsToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new Duration(value * SecondsToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new Duration(value * SecondsToMilliseconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new Duration(value * SecondsToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new Duration(value * SecondsToHours, destinationUnits);
                        break;

                    case Days:
                        result = new Duration(value * SecondsToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new Duration(value * SecondsToWeeks, destinationUnits);
                        break;
                }
                break;

            case Minutes:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new Duration(value * MinutesToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new Duration(value * MinutesToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new Duration(value * MinutesToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new Duration(value * MinutesToSeconds, destinationUnits);
                        break;

                    case Hours:
                        result = new Duration(value * MinutesToHours, destinationUnits);
                        break;

                    case Days:
                        result = new Duration(value * MinutesToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new Duration(value * MinutesToWeeks, destinationUnits);
                        break;
                }
                break;

            case Hours:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new Duration(value * HoursToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new Duration(value * HoursToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new Duration(value * HoursToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new Duration(value * HoursToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new Duration(value * HoursToMinutes, destinationUnits);
                        break;

                    case Days:
                        result = new Duration(value * HoursToDays, destinationUnits);
                        break;

                    case Weeks:
                        result = new Duration(value * HoursToWeeks, destinationUnits);
                        break;
                }
                break;

            case Days:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new Duration(value * DaysToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new Duration(value * DaysToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new Duration(value * DaysToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new Duration(value * DaysToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new Duration(value * DaysToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new Duration(value * DaysToHours, destinationUnits);
                        break;

                    case Weeks:
                        result = new Duration(value * DaysToWeeks, destinationUnits);
                        break;
                }
                break;

            case Weeks:
                switch (destinationUnits)
                {
                    case Nanoseconds:
                        result = new Duration(value * WeeksToNanoseconds, destinationUnits);
                        break;

                    case Microseconds:
                        result = new Duration(value * WeeksToMicroseconds, destinationUnits);
                        break;

                    case Milliseconds:
                        result = new Duration(value * WeeksToMilliseconds, destinationUnits);
                        break;

                    case Seconds:
                        result = new Duration(value * WeeksToSeconds, destinationUnits);
                        break;

                    case Minutes:
                        result = new Duration(value * WeeksToMinutes, destinationUnits);
                        break;

                    case Hours:
                        result = new Duration(value * WeeksToHours, destinationUnits);
                        break;

                    case Days:
                        result = new Duration(value * WeeksToDays, destinationUnits);
                        break;
                }
                break;
        }
        return result;
    }
}
