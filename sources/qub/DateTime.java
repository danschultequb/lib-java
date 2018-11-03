package qub;

import java.util.Calendar;

public class DateTime implements Comparable<DateTime>
{
    private final java.util.Calendar calendar;

    private DateTime(java.util.Calendar calendar)
    {
        this.calendar = calendar;
    }

    /**
     * Get the year of this DateTime object.
     * @return The year of this DateTime object.
     */
    public int getYear()
    {
        return calendar.get(java.util.Calendar.YEAR);
    }

    /**
     * Get the month of this DateTime object. January is considered 1.
     * @return The month of this DateTime object.
     */
    public int getMonth()
    {
        return calendar.get(java.util.Calendar.MONTH) + 1;
    }

    public int getDayOfMonth()
    {
        return calendar.get(java.util.Calendar.DAY_OF_MONTH);
    }

    public int getHourOfDay()
    {
        return calendar.get(java.util.Calendar.HOUR_OF_DAY);
    }

    public int getMinute()
    {
        return calendar.get(java.util.Calendar.MINUTE);
    }

    public int getSecond()
    {
        return calendar.get(java.util.Calendar.SECOND);
    }

    public int getMillisecond()
    {
        return calendar.get(java.util.Calendar.MILLISECOND);
    }

    public long getMillisecondsSinceEpoch()
    {
        return calendar.getTimeInMillis();
    }

    public Duration getTimeZoneOffset()
    {
        final java.util.TimeZone timeZone = calendar.getTimeZone();
        final int offsetInMilliseconds = timeZone.getOffset(getMillisecondsSinceEpoch());
        return Duration.milliseconds(offsetInMilliseconds);
    }

    /**
     * Get the result of adding the provided duration to this DateTime.
     * @param duration The Duration to add to this DateTime.
     * @return The result of adding the provided duration to this DateTime.
     */
    public DateTime plus(Duration duration)
    {
        PreCondition.assertNotNull(duration, "duration");

        DateTime result;
        final int durationValue = (int)duration.toMilliseconds().getValue();
        if (durationValue == 0)
        {
            result = this;
        }
        else
        {
            final java.util.Calendar resultCalendar = (java.util.Calendar)calendar.clone();
            resultCalendar.add(java.util.Calendar.MILLISECOND, durationValue);
            result = new DateTime(resultCalendar);
        }

        return result;
    }

    /**
     * Get the result of subtracting the provided duration from this DateTime.
     * @param duration The Duration to subtract from this DateTime.
     * @return The result of subtracting the provided duration from this DateTime.
     */
    public DateTime minus(Duration duration)
    {
        return plus(duration.negate());
    }

    /**
     * Get the duration between this DateTime and the provided DateTime.
     * @param rhs The other DateTime.
     * @return The duration between this DateTime and the provided DateTime.
     */
    public Duration minus(DateTime rhs)
    {
        final long millisecondDifference = getMillisecondsSinceEpoch() - rhs.getMillisecondsSinceEpoch();
        return Duration.milliseconds(millisecondDifference);
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(Strings.padLeft(getYear(), 4, '0'));
        builder.append('-');
        builder.append(Strings.padLeft(getMonth(), 2, '0'));
        builder.append('-');
        builder.append(Strings.padLeft(getDayOfMonth(), 2, '0'));
        builder.append('T');
        builder.append(Strings.padLeft(getHourOfDay(), 2, '0'));
        builder.append(':');
        builder.append(Strings.padLeft(getMinute(), 2, '0'));
        builder.append(':');
        builder.append(Strings.padLeft(getSecond(), 2, '0'));
        builder.append('.');
        builder.append(Strings.padLeft(getMillisecond(), 3, '0'));
        final Duration timeZoneOffset = getTimeZoneOffset();
        builder.append(timeZoneOffset.lessThan(Duration.zero) ? '-' : '+');
        final Duration positiveTimeZoneOffset = timeZoneOffset.absoluteValue();
        builder.append(Strings.padLeft((int)positiveTimeZoneOffset.toHours().getValue(), 2, '0'));
        builder.append(':');
        builder.append(Strings.padLeft((int)positiveTimeZoneOffset.toMinutes().getValue() % 60, 2, '0'));
        return builder.toString();
    }

    /**
     * Get the current DateTime for the local time zone.
     * @return The current DateTime for the local time zone.
     */
    public static DateTime localNow()
    {
        return new DateTime(java.util.Calendar.getInstance());
    }

    public static DateTime local(long millisecondsSinceEpoch)
    {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(millisecondsSinceEpoch);
        return new DateTime(calendar);
    }

    public static DateTime utc(long millisecondsSinceEpoch)
    {
        final java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(millisecondsSinceEpoch);
        return new DateTime(calendar);
    }

    public static DateTime local(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int millisecond)
    {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(java.util.Calendar.MINUTE, minute);
        calendar.set(java.util.Calendar.SECOND, second);
        calendar.set(java.util.Calendar.MILLISECOND, millisecond);
        return new DateTime(calendar);
    }

    public static DateTime utcNow()
    {
        return new DateTime(java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")));
    }

    public static DateTime utc(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int millisecond)
    {
        final java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(java.util.Calendar.MINUTE, minute);
        calendar.set(java.util.Calendar.SECOND, second);
        calendar.set(java.util.Calendar.MILLISECOND, millisecond);
        return new DateTime(calendar);
    }

    public static DateTime date(int year, int month, int dayOfMonth)
    {
        return utc(year, month, dayOfMonth, 0, 0, 0, 0);
    }

    @Override
    public Comparison compareTo(DateTime value)
    {
        return value == null ? Comparison.GreaterThan : Comparison.from(getMillisecondsSinceEpoch() - value.getMillisecondsSinceEpoch());
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof DateTime && equals((DateTime)obj);
    }

    public boolean equals(DateTime value)
    {
        return Comparable.equals(this, value);
    }
}
