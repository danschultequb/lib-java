package qub;

/**
 * A date and time value.
 */
public class DateTime implements Comparable<DateTime>
{
    public static final DateTime epoch = DateTime.createFromDurationSinceEpoch(Duration.zero);

    private final java.time.OffsetDateTime offsetDateTime;

    private DateTime(java.time.OffsetDateTime offsetDateTime)
    {
        PreCondition.assertNotNull(offsetDateTime, "offsetDateTime");

        this.offsetDateTime = offsetDateTime;
    }

    private static java.time.ZoneOffset getZoneOffset(Duration timeZoneOffset)
    {
        PreCondition.assertNotNull(timeZoneOffset, "timeZoneOffset");

        final java.time.ZoneOffset result = java.time.ZoneOffset.ofTotalSeconds((int)timeZoneOffset.toSeconds().getValue());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public static DateTime createFromDurationSinceEpoch(Duration durationSinceEpoch)
    {
        PreCondition.assertNotNull(durationSinceEpoch, "durationSinceEpoch");

        return DateTime.createFromDurationSinceEpoch(durationSinceEpoch, Duration.zero);
    }

    public static DateTime createFromDurationSinceEpoch(Duration durationSinceEpoch, Duration offset)
    {
        PreCondition.assertNotNull(durationSinceEpoch, "durationSinceEpoch");
        PreCondition.assertNotNull(offset, "offset");

        final long durationSinceEpochSeconds = (long)durationSinceEpoch.toSeconds().getValue();
        final long durationSinceEpochNanosecondAdjustment = (long)durationSinceEpoch.minus(Duration.seconds(durationSinceEpochSeconds)).toNanoseconds().getValue();
        final java.time.Instant instant = java.time.Instant.ofEpochSecond(durationSinceEpochSeconds, durationSinceEpochNanosecondAdjustment);
        final java.time.ZoneOffset zoneOffset = DateTime.getZoneOffset(offset);
        final java.time.OffsetDateTime offsetDateTime = java.time.OffsetDateTime.ofInstant(instant, zoneOffset);
        final DateTime result = new DateTime(offsetDateTime);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(durationSinceEpoch, result.getDurationSinceEpoch(), Duration.nanoseconds(1), "result.getDurationSinceEpoch()");
        PostCondition.assertEqual(offset, result.getOffset(), "result.getTimeZoneOffset()");

        return result;
    }

    public static DateTime create(int year, int month, int dayOfMonth)
    {
        return DateTime.create(year, month, dayOfMonth, Duration.zero);
    }

    public static DateTime create(int year, int month, int dayOfMonth, Duration offset)
    {
        PreCondition.assertNotNull(offset, "offset");

        return DateTime.create(year, month, dayOfMonth, 0, 0, offset);
    }

    public static DateTime create(int year, int month, int dayOfMonth, int hourOfDay, int minute)
    {
        return DateTime.create(year, month, dayOfMonth, hourOfDay, minute, Duration.zero);
    }

    public static DateTime create(int year, int month, int dayOfMonth, int hourOfDay, int minute, Duration offset)
    {
        PreCondition.assertNotNull(offset, "offset");

        return DateTime.create(year, month, dayOfMonth, hourOfDay, minute, 0, offset);
    }

    public static DateTime create(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second)
    {
        return DateTime.create(year, month, dayOfMonth, hourOfDay, minute, second, Duration.zero);
    }

    public static DateTime create(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, Duration offset)
    {
        PreCondition.assertNotNull(offset, "offset");

        return DateTime.create(year, month, dayOfMonth, hourOfDay, minute, second, 0, offset);
    }

    public static DateTime create(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int millisecond)
    {
        return DateTime.create(year, month, dayOfMonth, hourOfDay, minute, second, millisecond, Duration.zero);
    }

    public static DateTime create(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int millisecond, Duration offset)
    {
        PreCondition.assertNotNull(offset, "offset");

        final java.time.ZoneOffset zoneOffset = DateTime.getZoneOffset(offset);
        final java.time.LocalDateTime localDateTime = java.time.LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute, second, (int)Duration.milliseconds(millisecond).toNanoseconds().getValue());
        final java.time.Instant instant = localDateTime.toInstant(zoneOffset);
        final java.time.OffsetDateTime offsetDateTime = java.time.OffsetDateTime.ofInstant(instant, zoneOffset);
        final DateTime result = new DateTime(offsetDateTime);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(year, result.getYear(), "result.getYear()");
        PostCondition.assertEqual(month, result.getMonth(), "result.getMonth()");
        PostCondition.assertEqual(dayOfMonth, result.getDayOfMonth(), "result.getDayOfMonth()");
        PostCondition.assertEqual(hourOfDay, result.getHourOfDay(), "result.getHourOfDay()");
        PostCondition.assertEqual(minute, result.getMinute(), "result.getMinute()");
        PostCondition.assertEqual(second, result.getSecond(), "result.getSecond()");
        PostCondition.assertEqual(millisecond, result.getMillisecond(), "result.getMillisecond()");
        PostCondition.assertEqual(offset, result.getOffset(), "result.getTimeZoneOffset()");

        return result;
    }

    /**
     * Get the year of this DateTime object.
     * @return The year of this DateTime object.
     */
    public int getYear()
    {
        return this.offsetDateTime.getYear();
    }

    /**
     * Get the month of this DateTime object. January is considered 1.
     * @return The month of this DateTime object.
     */
    public int getMonth()
    {
        return this.offsetDateTime.getMonth().getValue();
    }

    public int getDayOfMonth()
    {
        return this.offsetDateTime.getDayOfMonth();
    }

    public int getHourOfDay()
    {
        return this.offsetDateTime.getHour();
    }

    public int getMinute()
    {
        return this.offsetDateTime.getMinute();
    }

    public int getSecond()
    {
        return this.offsetDateTime.getSecond();
    }

    private int scopeNanosecondAdjustment(int nanosecondModifier, int resultScope)
    {
        final int nanosecondAdjustment = this.offsetDateTime.getNano();
        final int result = (nanosecondAdjustment / nanosecondModifier) % resultScope;

        PostCondition.assertBetween(0, result, resultScope, "result");

        return result;
    }

    public int getMillisecond()
    {
        return this.scopeNanosecondAdjustment(BasicDuration.MillisecondsToNanoseconds, BasicDuration.SecondsToMilliseconds);
    }

    public int getMicrosecond()
    {
        return this.scopeNanosecondAdjustment(BasicDuration.MicrosecondsToNanoseconds, BasicDuration.MillisecondsToMicroseconds);
    }

    public int getNanosecond()
    {
        return this.scopeNanosecondAdjustment(1, BasicDuration.MicrosecondsToNanoseconds);
    }

    /**
     * Get the duration that has passed since the epoch (1970-01-01 UTC).
     * @return The duration that has passed since the epoch (1970-01-01 UTC).
     */
    public Duration getDurationSinceEpoch()
    {
        final java.time.Instant instant = this.offsetDateTime.toInstant();
        final long secondsSinceEpoch = instant.getEpochSecond();
        final int nanosecondAdjustment = instant.getNano();
        final Duration result = Duration.seconds(secondsSinceEpoch).plus(Duration.nanoseconds(nanosecondAdjustment));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the offset of this date time.
     * @return The offset of this date time.
     */
    public Duration getOffset()
    {
        return Duration.seconds(this.offsetDateTime.getOffset().getTotalSeconds());
    }

    /**
     * Convert this DateTime object to the provided offset.
     * @param offset The offset to convert this DateTime object to.
     * @return The converted DateTime object.
     */
    public DateTime toOffset(Duration offset)
    {
        PreCondition.assertNotNull(offset, "offset");

        DateTime result = this;
        if (!offset.equals(this.getOffset()))
        {
            result = DateTime.createFromDurationSinceEpoch(this.getDurationSinceEpoch(), offset);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(offset, result.getOffset(), "result.getTimeZoneOffset()");

        return result;
    }

    /**
     * Get the UTC representation of this DateTime.
     * @return The UTC representation of this DateTime.
     */
    public DateTime toUTC()
    {
        return this.toOffset(Duration.zero);
    }

    public Date toDate()
    {
        return Date.create(this);
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
        final long durationNanoseconds = (long)duration.toNanoseconds().getValue();
        if (durationNanoseconds == 0)
        {
            result = this;
        }
        else
        {
            result = new DateTime(this.offsetDateTime.plusNanos(durationNanoseconds));
        }

        return result;
    }

    public DateTime plusDays(int days)
    {
        return new DateTime(this.offsetDateTime.plusDays(days));
    }

    public DateTime plusWeeks(int weeks)
    {
        return new DateTime(this.offsetDateTime.plusWeeks(weeks));
    }

    public DateTime plusMonths(int months)
    {
        return new DateTime(this.offsetDateTime.plusMonths(months));
    }

    public DateTime plusYears(int years)
    {
        return new DateTime(this.offsetDateTime.plusYears(years));
    }

    /**
     * Get the result of subtracting the provided duration create this DateTime.
     * @param duration The Duration to subtract create this DateTime.
     * @return The result of subtracting the provided duration create this DateTime.
     */
    public DateTime minus(Duration duration)
    {
        return this.plus(duration.negate());
    }

    public DateTime minusDays(int days)
    {
        return new DateTime(this.offsetDateTime.minusDays(days));
    }

    public DateTime minusWeeks(int weeks)
    {
        return new DateTime(this.offsetDateTime.minusWeeks(weeks));
    }

    public DateTime minusMonths(int months)
    {
        return new DateTime(this.offsetDateTime.minusMonths(months));
    }

    public DateTime minusYears(int years)
    {
        return new DateTime(this.offsetDateTime.minusYears(years));
    }

    /**
     * Get the duration between this DateTime and the provided DateTime.
     * @param rhs The other DateTime.
     * @return The duration between this DateTime and the provided DateTime.
     */
    public Duration minus(DateTime rhs)
    {
        return this.getDurationSinceEpoch().minus(rhs.getDurationSinceEpoch());
    }

    @Override
    public String toString()
    {
        return this.offsetDateTime.toString();
    }

    @Override
    public Comparison compareTo(DateTime value)
    {
        return value == null ? Comparison.GreaterThan : Comparison.from(this.offsetDateTime.compareTo(value.offsetDateTime));
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

    public boolean equals(DateTime value, Duration marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");

        return value != null && this.getDurationSinceEpoch().equals(value.getDurationSinceEpoch(), marginOfError);
    }

    /**
     * Parse the provided text into a DateTime object.
     * @param text The text to parse.
     * @return The parsed DateTime object.
     */
    public static Result<DateTime> parse(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        return Result.create(() ->
        {
            return new DateTime(java.time.OffsetDateTime.parse(text));
        });
    }
}
