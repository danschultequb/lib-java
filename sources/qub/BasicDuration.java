package qub;

/**
 * A period of time.
 */
public class BasicDuration extends MeasurableValueBase<DurationUnit,BasicDuration> implements Duration
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

    private static final double MillisecondsToWeeks = 1.0 / WeeksToMilliseconds;
    private static final double MillisecondsToDays = 1.0 / DaysToMilliseconds;
    private static final double MillisecondsToHours = 1.0 / HoursToMilliseconds;
    private static final double MillisecondsToMinutes = 1.0 / MinutesToMilliseconds;
    public static final int MillisecondsToNanoseconds = MillisecondsToMicroseconds * MicrosecondsToNanoseconds;

    private static final double MicrosecondsToWeeks = 1.0 / WeeksToMicroseconds;
    private static final double MicrosecondsToDays = 1.0 / DaysToMicroseconds;
    private static final double MicrosecondsToHours = 1.0 / HoursToMicroseconds;
    private static final double MicrosecondsToMinutes = 1.0 / MinutesToMicroseconds;

    public static final double NanosecondsToWeeks = 1.0 / WeeksToNanoseconds;
    public static final double NanosecondsToDays = 1.0 / DaysToNanoseconds;
    public static final double NanosecondsToHours = 1.0 / HoursToNanoseconds;
    public static final double NanosecondsToMinutes = 1.0 / MinutesToNanoseconds;

    private BasicDuration(double value, DurationUnit units)
    {
        super(value, units, BasicDuration::create, DurationUnit.Seconds);
    }

    public static BasicDuration create(double value, DurationUnit units)
    {
        return new BasicDuration(value, units);
    }

    @Override
    protected double getConversionMultiplier(DurationUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        double result = 0;

        switch (this.getUnits())
        {
            case Nanoseconds:
                switch (units)
                {
                    case Nanoseconds:
                        result = 1;
                        break;

                    case Microseconds:
                        result = MetricScale.nanoToMicro;
                        break;

                    case Milliseconds:
                        result = MetricScale.nanoToMilli;
                        break;

                    case Seconds:
                        result = MetricScale.nanoToUni;
                        break;

                    case Minutes:
                        result = BasicDuration.NanosecondsToMinutes;
                        break;

                    case Hours:
                        result = BasicDuration.NanosecondsToHours;
                        break;

                    case Days:
                        result = BasicDuration.NanosecondsToDays;
                        break;

                    case Weeks:
                        result = BasicDuration.NanosecondsToWeeks;
                        break;

                    default:
                        MeasurableValueBase.throwUnrecognizedUnitsException(units);
                        break;
                }
                break;

            case Microseconds:
                switch (units)
                {
                    case Nanoseconds:
                        result = MetricScale.microToNano;
                        break;

                    case Microseconds:
                        result = 1;
                        break;

                    case Milliseconds:
                        result = MetricScale.microToMilli;
                        break;

                    case Seconds:
                        result = MetricScale.microToUni;
                        break;

                    case Minutes:
                        result = BasicDuration.MicrosecondsToMinutes;
                        break;

                    case Hours:
                        result = BasicDuration.MicrosecondsToHours;
                        break;

                    case Days:
                        result = BasicDuration.MicrosecondsToDays;
                        break;

                    case Weeks:
                        result = BasicDuration.MicrosecondsToWeeks;
                        break;

                    default:
                        MeasurableValueBase.throwUnrecognizedUnitsException(units);
                        break;
                }
                break;

            case Milliseconds:
                switch (units)
                {
                    case Nanoseconds:
                        result = MetricScale.milliToNano;
                        break;

                    case Microseconds:
                        result = MetricScale.milliToMicro;
                        break;

                    case Milliseconds:
                        result = 1;
                        break;

                    case Seconds:
                        result = MetricScale.milliToUni;
                        break;

                    case Minutes:
                        result = BasicDuration.MillisecondsToMinutes;
                        break;

                    case Hours:
                        result = BasicDuration.MillisecondsToHours;
                        break;

                    case Days:
                        result = BasicDuration.MillisecondsToDays;
                        break;

                    case Weeks:
                        result = BasicDuration.MillisecondsToWeeks;
                        break;

                    default:
                        MeasurableValueBase.throwUnrecognizedUnitsException(units);
                        break;
                }
                break;

            case Seconds:
                switch (units)
                {
                    case Nanoseconds:
                        result = MetricScale.uniToNano;
                        break;

                    case Microseconds:
                        result = MetricScale.uniToMicro;
                        break;

                    case Milliseconds:
                        result = MetricScale.uniToMilli;
                        break;

                    case Seconds:
                        result = 1;
                        break;

                    case Minutes:
                        result = BasicDuration.SecondsToMinutes;
                        break;

                    case Hours:
                        result = BasicDuration.SecondsToHours;
                        break;

                    case Days:
                        result = BasicDuration.SecondsToDays;
                        break;

                    case Weeks:
                        result = BasicDuration.SecondsToWeeks;
                        break;

                    default:
                        MeasurableValueBase.throwUnrecognizedUnitsException(units);
                        break;
                }
                break;

            case Minutes:
                switch (units)
                {
                    case Nanoseconds:
                        result = BasicDuration.MinutesToNanoseconds;
                        break;

                    case Microseconds:
                        result = BasicDuration.MinutesToMicroseconds;
                        break;

                    case Milliseconds:
                        result = BasicDuration.MinutesToMilliseconds;
                        break;

                    case Seconds:
                        result = BasicDuration.MinutesToSeconds;
                        break;

                    case Minutes:
                        result = 1;
                        break;

                    case Hours:
                        result = BasicDuration.MinutesToHours;
                        break;

                    case Days:
                        result = BasicDuration.MinutesToDays;
                        break;

                    case Weeks:
                        result = BasicDuration.MinutesToWeeks;
                        break;

                    default:
                        MeasurableValueBase.throwUnrecognizedUnitsException(units);
                        break;
                }
                break;

            case Hours:
                switch (units)
                {
                    case Nanoseconds:
                        result = BasicDuration.HoursToNanoseconds;
                        break;

                    case Microseconds:
                        result = BasicDuration.HoursToMicroseconds;
                        break;

                    case Milliseconds:
                        result = BasicDuration.HoursToMilliseconds;
                        break;

                    case Seconds:
                        result = BasicDuration.HoursToSeconds;
                        break;

                    case Minutes:
                        result = BasicDuration.HoursToMinutes;
                        break;

                    case Hours:
                        result = 1;
                        break;

                    case Days:
                        result = BasicDuration.HoursToDays;
                        break;

                    case Weeks:
                        result = BasicDuration.HoursToWeeks;
                        break;

                    default:
                        MeasurableValueBase.throwUnrecognizedUnitsException(units);
                        break;
                }
                break;

            case Days:
                switch (units)
                {
                    case Nanoseconds:
                        result = BasicDuration.DaysToNanoseconds;
                        break;

                    case Microseconds:
                        result = BasicDuration.DaysToMicroseconds;
                        break;

                    case Milliseconds:
                        result = BasicDuration.DaysToMilliseconds;
                        break;

                    case Seconds:
                        result = BasicDuration.DaysToSeconds;
                        break;

                    case Minutes:
                        result = BasicDuration.DaysToMinutes;
                        break;

                    case Hours:
                        result = BasicDuration.DaysToHours;
                        break;

                    case Days:
                        result = 1;
                        break;

                    case Weeks:
                        result = BasicDuration.DaysToWeeks;
                        break;

                    default:
                        MeasurableValueBase.throwUnrecognizedUnitsException(units);
                        break;
                }
                break;

            case Weeks:
                switch (units)
                {
                    case Nanoseconds:
                        result = BasicDuration.WeeksToNanoseconds;
                        break;

                    case Microseconds:
                        result = BasicDuration.WeeksToMicroseconds;
                        break;

                    case Milliseconds:
                        result = BasicDuration.WeeksToMilliseconds;
                        break;

                    case Seconds:
                        result = BasicDuration.WeeksToSeconds;
                        break;

                    case Minutes:
                        result = BasicDuration.WeeksToMinutes;
                        break;

                    case Hours:
                        result = BasicDuration.WeeksToHours;
                        break;

                    case Days:
                        result = BasicDuration.WeeksToDays;
                        break;

                    case Weeks:
                        result = 1;
                        break;

                    default:
                        MeasurableValueBase.throwUnrecognizedUnitsException(units);
                        break;
                }
                break;

            default:
                MeasurableValueBase.throwUnrecognizedUnitsException(this.getUnits());
                break;
        }

        PostCondition.assertGreaterThan(result, 0, "result");

        return result;
    }
}
