package qub;

/**
 * The different types of duration units.
 */
public enum DurationUnits
{
    /**
     * One billionth of a second.
     */
    Nanoseconds,

    /**
     * One millionth of a second.
     */
    Microseconds,

    /**
     * One thousandth of a second.
     */
    Milliseconds,

    /**
     * 1 second.
     */
    Seconds,

    /**
     * 60 seconds.
     */
    Minutes,

    /**
     * 60 minutes (3600 seconds).
     */
    Hours,

    /**
     * 24 hours (86400 seconds).
     */
    Days,

    /**
     * 7 days (604800 seconds).
     */
    Weeks
}
