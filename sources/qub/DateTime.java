package qub;

import java.util.Calendar;
import java.util.TimeZone;

public class DateTime implements Comparable<DateTime>
{
    private final Calendar calendar;

    private DateTime(Calendar calendar)
    {
        this.calendar = calendar;
    }

    /**
     * Get the year of this DateTime object.
     * @return The year of this DateTime object.
     */
    public int getYear()
    {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Get the month of this DateTime object. January is considered 1.
     * @return The month of this DateTime object.
     */
    public int getMonth()
    {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getDayOfMonth()
    {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHourOfDay()
    {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute()
    {
        return calendar.get(Calendar.MINUTE);
    }

    public int getSecond()
    {
        return calendar.get(Calendar.SECOND);
    }

    public int getMillisecond()
    {
        return calendar.get(Calendar.MILLISECOND);
    }

    public long getMillisecondsSinceEpoch()
    {
        return calendar.getTimeInMillis();
    }

    public Duration getTimeZoneOffset()
    {
        final TimeZone timeZone = calendar.getTimeZone();
        final int offsetInMilliseconds = timeZone.getOffset(getMillisecondsSinceEpoch());
        return Duration.milliseconds(offsetInMilliseconds);
    }

    /**
     * Get the current DateTime for the local time zone.
     * @return The current DateTime for the local time zone.
     */
    public static DateTime localNow()
    {
        return new DateTime(Calendar.getInstance());
    }

    public static DateTime local(long millisecondsSinceEpoch)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecondsSinceEpoch);
        return new DateTime(calendar);
    }

    public static DateTime utc(long millisecondsSinceEpoch)
    {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(millisecondsSinceEpoch);
        return new DateTime(calendar);
    }

    public static DateTime local(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int millisecond)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return new DateTime(calendar);
    }

    public static DateTime utcNow()
    {
        return new DateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
    }

    public static DateTime utc(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int millisecond)
    {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
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
        return Comparable.equalTo(this, value);
    }
}
