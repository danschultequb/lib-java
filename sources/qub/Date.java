package qub;

/**
 * A date value.
 */
public class Date implements Comparable<Date>
{
    public static final Date epoch = Date.create(DateTime.epoch);

    private final DateTime dateTime;

    private Date(DateTime dateTime)
    {
        PreCondition.assertNotNull(dateTime, "dateTime");
        PreCondition.assertEqual(0, dateTime.getHourOfDay(), "dateTime.getHourOfDay()");
        PreCondition.assertEqual(0, dateTime.getMinute(), "dateTime.getMinute()");
        PreCondition.assertEqual(0, dateTime.getSecond(), "dateTime.getSecond()");
        PreCondition.assertEqual(0, dateTime.getNanosecond(), "dateTime.getNanosecond()");

        this.dateTime = dateTime;
    }

    public static Date create(DateTime dateTime)
    {
        PreCondition.assertNotNull(dateTime, "dateTime");

        if (dateTime.getHourOfDay() != 0)
        {
            dateTime = dateTime.minus(Duration.hours(dateTime.getHourOfDay()));
        }
        if (dateTime.getMinute() != 0)
        {
            dateTime = dateTime.minus(Duration.minutes(dateTime.getMinute()));
        }
        if (dateTime.getSecond() != 0)
        {
            dateTime = dateTime.minus(Duration.seconds(dateTime.getSecond()));
        }
        if (dateTime.getNanosecond() != 0)
        {
            dateTime = dateTime.minus(Duration.nanoseconds(dateTime.getNanosecond()));
        }
        final Date result = new Date(dateTime);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(dateTime.getYear(), result.getYear(), "result.getYear()");
        PostCondition.assertEqual(dateTime.getMonth(), result.getMonth(), "result.getMonth()");
        PostCondition.assertEqual(dateTime.getDayOfMonth(), result.getDayOfMonth(), "result.getDayOfMonth()");

        return result;
    }

    public static Date create(int year, int month, int dayOfMonth)
    {
        return Date.create(DateTime.create(year, month, dayOfMonth));
    }

    /**
     * Get the year of this Date object.
     * @return The year of this Date object.
     */
    public int getYear()
    {
        return this.dateTime.getYear();
    }

    /**
     * Get the month of this Date object. January is considered 1.
     * @return The month of this Date object.
     */
    public int getMonth()
    {
        return this.dateTime.getMonth();
    }

    public int getDayOfMonth()
    {
        return this.dateTime.getDayOfMonth();
    }

    /**
     * Get the duration that has passed since the epoch (1970-01-01 UTC).
     * @return The duration that has passed since the epoch (1970-01-01 UTC).
     */
    public Duration getDurationSinceEpoch()
    {
        return this.dateTime.getDurationSinceEpoch();
    }

    public Date plusDays(int days)
    {
        return this.dateTime.plusDays(days).toDate();
    }

    public Date plusWeeks(int weeks)
    {
        return this.dateTime.plusWeeks(weeks).toDate();
    }

    public Date plusMonths(int months)
    {
        return this.dateTime.plusMonths(months).toDate();
    }

    public Date plusYears(int years)
    {
        return this.dateTime.plusYears(years).toDate();
    }

    public Date minusDays(int days)
    {
        return this.dateTime.minusDays(days).toDate();
    }

    public Date minusWeeks(int weeks)
    {
        return this.dateTime.minusWeeks(weeks).toDate();
    }

    public Date minusMonths(int months)
    {
        return this.dateTime.minusMonths(months).toDate();
    }

    public Date minusYears(int years)
    {
        return this.dateTime.minusYears(years).toDate();
    }

    /**
     * Get the duration between this Date and the provided Date.
     * @param rhs The other Date.
     * @return The duration between this Date and the provided Date.
     */
    public Duration minus(Date rhs)
    {
        return this.dateTime.minus(rhs.dateTime);
    }

    /**
     * Get the duration between this Date and the provided DateTime.
     * @param rhs The other DateTime.
     * @return The duration between this Date and the provided DateTime.
     */
    public Duration minus(DateTime rhs)
    {
        return this.dateTime.minus(rhs);
    }

    public DateTime toDateTime()
    {
        return this.dateTime;
    }

    @Override
    public String toString()
    {
        return this.dateTime.toString();
    }

    @Override
    public Comparison compareWith(Date value)
    {
        return value == null ? Comparison.GreaterThan : this.dateTime.compareWith(value.dateTime);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Date && this.equals((Date)rhs);
    }

    public boolean equals(Date rhs)
    {
        return rhs != null && this.dateTime.equals(rhs.dateTime);
    }

    /**
     * Parse the provided text into a Date object.
     * @param text The text to parse.
     * @return The parsed Date object.
     */
    public static Result<Date> parse(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        return Result.create(() ->
        {
            return Date.create(DateTime.parse(text).await());
        });
    }
}
