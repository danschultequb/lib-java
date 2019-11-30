package qub;

public interface DateTimeTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(DateTime.class, () ->
        {
            runner.testGroup("createFromDurationSinceEpoch(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.createFromDurationSinceEpoch(null),
                        new PreConditionFailure("durationSinceEpoch cannot be null."));
                });

                runner.test("with " + Duration.zero, (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.zero);
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.zero, dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.nanoseconds(1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(1, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.nanoseconds(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.nanoseconds(-1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(-1));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(999, dateTime.getMicrosecond());
                    test.assertEqual(999, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.nanoseconds(-1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.microseconds(1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.microseconds(1));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(1, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.microseconds(1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.microseconds(-1), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.microseconds(-1);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch);
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(999, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.milliseconds(0.1), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.milliseconds(0.1);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch);
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(100, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.milliseconds(-0.1), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.milliseconds(-0.1);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch);
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(900, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.milliseconds(0.99999999), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.milliseconds(0.99999999);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch);
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(999, dateTime.getMicrosecond());
                    test.assertEqual(999, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.milliseconds(-0.99999999), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.milliseconds(-0.99999999);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch);
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(1, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.milliseconds(1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(1));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(1, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.milliseconds(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.milliseconds(-1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(-1));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.milliseconds(-1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.seconds(1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.seconds(1));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(1, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.seconds(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.seconds(-1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.seconds(-1));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.seconds(-1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.minutes(1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.minutes(1));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(1, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.minutes(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.minutes(-1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.minutes(-1));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.minutes(-1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.hours(1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.hours(1));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(1, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.hours(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.hours(-1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.hours(-1));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.hours(-1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.days(1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.days(1));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(2, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.days(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.days(-1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.days(-1));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.days(-1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.weeks(1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.weeks(1));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(8, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.weeks(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.weeks(-1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.weeks(-1));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(25, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.weeks(-1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.milliseconds(1234567890123L), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(1234567890123L));
                    test.assertEqual(2009, dateTime.getYear());
                    test.assertEqual(2, dateTime.getMonth());
                    test.assertEqual(13, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(31, dateTime.getMinute());
                    test.assertEqual(30, dateTime.getSecond());
                    test.assertEqual(123, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.milliseconds(1234567890123L), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.milliseconds(-1234567890123L), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(-1234567890123L));
                    test.assertEqual(1930, dateTime.getYear());
                    test.assertEqual(11, dateTime.getMonth());
                    test.assertEqual(18, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(28, dateTime.getMinute());
                    test.assertEqual(29, dateTime.getSecond());
                    test.assertEqual(877, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertEqual(Duration.milliseconds(-1234567890123L), dateTime.getDurationSinceEpoch());
                });
            });

            runner.testGroup("createFromDurationSinceEpoch(Duration,Duration)", () ->
            {
                runner.test("with null duration", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.createFromDurationSinceEpoch(null, Duration.zero),
                        new PreConditionFailure("durationSinceEpoch cannot be null."));
                });

                runner.test("with null offset", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.createFromDurationSinceEpoch(Duration.days(5), null),
                        new PreConditionFailure("offset cannot be null."));
                });

                runner.test("with " + Duration.zero + " and " + Duration.seconds(1), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.zero, Duration.seconds(1));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(1, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.seconds(1), dateTime.getOffset());
                    test.assertEqual(Duration.zero, dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.nanoseconds(1) + " and " + Duration.minutes(-2), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1), Duration.minutes(-2));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(58, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(1, dateTime.getNanosecond());
                    test.assertEqual(Duration.minutes(-2), dateTime.getOffset());
                    test.assertEqual(Duration.nanoseconds(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.nanoseconds(-1) + " and " + Duration.hours(3), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(-1), Duration.hours(3));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(2, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(999, dateTime.getMicrosecond());
                    test.assertEqual(999, dateTime.getNanosecond());
                    test.assertEqual(Duration.hours(3), dateTime.getOffset());
                    test.assertEqual(Duration.nanoseconds(-1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.microseconds(1) + " and " + Duration.seconds(-4), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.microseconds(1), Duration.seconds(-4));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(56, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(1, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.seconds(-4), dateTime.getOffset());
                    test.assertEqual(Duration.microseconds(1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.microseconds(-1) + " and " + Duration.minutes(5), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.microseconds(-1);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch, Duration.minutes(5));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(4, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(999, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.minutes(5), dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.milliseconds(0.1) + " and " + Duration.hours(-6), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.milliseconds(0.1);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch, Duration.hours(-6));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(18, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(100, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.hours(-6), dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.milliseconds(-0.1) + " and " + Duration.seconds(7), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.milliseconds(-0.1);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch, Duration.seconds(7));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(6, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(900, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.seconds(7), dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.milliseconds(0.99999999) + " and " + Duration.minutes(-8), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.milliseconds(0.99999999);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch, Duration.minutes(-8));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(52, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(999, dateTime.getMicrosecond());
                    test.assertEqual(999, dateTime.getNanosecond());
                    test.assertEqual(Duration.minutes(-8), dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.milliseconds(-0.99999999) + " and " + Duration.hours(9), (Test test) ->
                {
                    final Duration durationSinceEpoch = Duration.milliseconds(-0.99999999);
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(durationSinceEpoch, Duration.hours(9));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(8, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(1, dateTime.getNanosecond());
                    test.assertEqual(Duration.hours(9), dateTime.getOffset());
                    test.assertEqual(durationSinceEpoch, dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.milliseconds(1) + " and " + Duration.seconds(-10), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(1), Duration.seconds(-10));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(50, dateTime.getSecond());
                    test.assertEqual(1, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.seconds(-10), dateTime.getOffset());
                    test.assertEqual(Duration.milliseconds(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.milliseconds(-1) + " and " + Duration.minutes(11), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(-1), Duration.minutes(11));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(10, dateTime.getMinute());
                    test.assertEqual(59, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.minutes(11), dateTime.getOffset());
                    test.assertEqual(Duration.milliseconds(-1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.seconds(1) + " and " + Duration.hours(-12), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.seconds(1), Duration.hours(-12));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(12, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(1, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.hours(-12), dateTime.getOffset());
                    test.assertEqual(Duration.seconds(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.seconds(-1) + " and " + Duration.seconds(13), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.seconds(-1), Duration.seconds(13));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(12, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.seconds(13), dateTime.getOffset());
                    test.assertEqual(Duration.seconds(-1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.minutes(1) + " and " + Duration.minutes(-14), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.minutes(1), Duration.minutes(-14));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(47, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.minutes(-14), dateTime.getOffset());
                    test.assertEqual(Duration.minutes(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.minutes(-1) + " and " + Duration.hours(15), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.minutes(-1), Duration.hours(15));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(14, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.hours(15), dateTime.getOffset());
                    test.assertEqual(Duration.minutes(-1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.hours(1) + " and " + Duration.seconds(-16), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.hours(1), Duration.seconds(-16));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(59, dateTime.getMinute());
                    test.assertEqual(44, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.seconds(-16), dateTime.getOffset());
                    test.assertEqual(Duration.hours(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.hours(-1) + " and " + Duration.minutes(17), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.hours(-1), Duration.minutes(17));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(17, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.minutes(17), dateTime.getOffset());
                    test.assertEqual(Duration.hours(-1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.days(1) + " and " + Duration.hours(-18), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.days(1), Duration.hours(-18));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(6, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.hours(-18), dateTime.getOffset());
                    test.assertEqual(Duration.days(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.days(-1) + " and " + Duration.seconds(19), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.days(-1), Duration.seconds(19));
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(19, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.seconds(19), dateTime.getOffset());
                    test.assertEqual(Duration.days(-1), dateTime.getDurationSinceEpoch(), Duration.nanoseconds(1));
                });

                runner.test("with " + Duration.weeks(1) + " and " + Duration.minutes(-20), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.weeks(1), Duration.minutes(-20));
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(7, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(40, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.minutes(-20), dateTime.getOffset());
                    test.assertEqual(Duration.weeks(1), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.weeks(-1) + " and " + Duration.hours(21), (Test test) ->
                {
                    test.assertThrows(() -> DateTime.createFromDurationSinceEpoch(Duration.weeks(-1), Duration.hours(21)),
                        new java.time.DateTimeException("Zone offset not in valid range: -18:00 to +18:00"));
                });

                runner.test("with " + Duration.milliseconds(1234567890123L) + " and " + Duration.seconds(-22), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(1234567890123L), Duration.seconds(-22));
                    test.assertEqual(2009, dateTime.getYear());
                    test.assertEqual(2, dateTime.getMonth());
                    test.assertEqual(13, dateTime.getDayOfMonth());
                    test.assertEqual(23, dateTime.getHourOfDay());
                    test.assertEqual(31, dateTime.getMinute());
                    test.assertEqual(8, dateTime.getSecond());
                    test.assertEqual(123, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.seconds(-22), dateTime.getOffset());
                    test.assertEqual(Duration.milliseconds(1234567890123L), dateTime.getDurationSinceEpoch());
                });

                runner.test("with " + Duration.milliseconds(-1234567890123L) + " and " + Duration.minutes(23), (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(-1234567890123L), Duration.minutes(23));
                    test.assertEqual(1930, dateTime.getYear());
                    test.assertEqual(11, dateTime.getMonth());
                    test.assertEqual(18, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(51, dateTime.getMinute());
                    test.assertEqual(29, dateTime.getSecond());
                    test.assertEqual(877, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.minutes(23), dateTime.getOffset());
                    test.assertEqual(Duration.milliseconds(-1234567890123L), dateTime.getDurationSinceEpoch());
                });
            });

            runner.testGroup("create(int,int,int)", () ->
            {
                runner.test("with 1, 2, 3", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 2, 3);
                    test.assertEqual(1, dateTime.getYear());
                    test.assertEqual(2, dateTime.getMonth());
                    test.assertEqual(3, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertSame(dateTime, dateTime.toUTC());
                });

                runner.test("with 1, 1, 1", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 1, 1);
                    test.assertEqual(1, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertSame(dateTime, dateTime.toUTC());
                });

                runner.test("with 1, 12, 1", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 12, 1);
                    test.assertEqual(1, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertSame(dateTime, dateTime.toUTC());
                });

                runner.test("with 10, 0, 30", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(10, 0, 30),
                        new java.time.DateTimeException("Invalid value for MonthOfYear (valid values 1 - 12): 0"));
                });

                runner.test("with 1, 13, 1", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 13, 1),
                        new java.time.DateTimeException("Invalid value for MonthOfYear (valid values 1 - 12): 13"));
                });

                runner.test("with 1, 1, 0", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 1, 0),
                        new java.time.DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 0"));
                });

                runner.test("with 1, 1, 32", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 1, 32),
                        new java.time.DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 32"));
                });

                runner.test("with 1, 2, 0", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 0),
                        new java.time.DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 0"));
                });

                runner.test("with 1, 2, 29", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 29),
                        new java.time.DateTimeException("Invalid date 'February 29' as '1' is not a leap year"));
                });

                runner.test("with 1, 2, 30", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 30),
                        new java.time.DateTimeException("Invalid date 'FEBRUARY 30'"));
                });

                runner.test("with 1, 2, 31", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 31),
                        new java.time.DateTimeException("Invalid date 'FEBRUARY 31'"));
                });

                runner.test("with 1, 2, 32", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 32),
                        new java.time.DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 32"));
                });
            });

            runner.testGroup("create(int,int,int,Duration)", () ->
            {
                runner.test("with 1, 2, 3, and null offset", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 3, null),
                        new PreConditionFailure("offset cannot be null."));
                });

                runner.test("with 1, 1, 1, and non-null offset", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 1, 1, Duration.hours(2));
                    test.assertEqual(1, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.hours(2), dateTime.getOffset());
                    test.assertEqual(DateTime.create(0, 12, 31, 22, 0), dateTime.toUTC());
                });

                runner.test("with 1, 12, 1", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 12, 1);
                    test.assertEqual(1, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertSame(dateTime, dateTime.toUTC());
                });

                runner.test("with 10, 0, 30", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(10, 0, 30),
                        new java.time.DateTimeException("Invalid value for MonthOfYear (valid values 1 - 12): 0"));
                });

                runner.test("with 1, 13, 1", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 13, 1),
                        new java.time.DateTimeException("Invalid value for MonthOfYear (valid values 1 - 12): 13"));
                });

                runner.test("with 1, 1, 0", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 1, 0),
                        new java.time.DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 0"));
                });

                runner.test("with 1, 1, 32", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 1, 32),
                        new java.time.DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 32"));
                });

                runner.test("with 1, 2, 0", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 0),
                        new java.time.DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 0"));
                });

                runner.test("with 1, 2, 29", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 29),
                        new java.time.DateTimeException("Invalid date 'February 29' as '1' is not a leap year"));
                });

                runner.test("with 1, 2, 30", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 30),
                        new java.time.DateTimeException("Invalid date 'FEBRUARY 30'"));
                });

                runner.test("with 1, 2, 31", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 31),
                        new java.time.DateTimeException("Invalid date 'FEBRUARY 31'"));
                });

                runner.test("with 1, 2, 32", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 32),
                        new java.time.DateTimeException("Invalid value for DayOfMonth (valid values 1 - 28/31): 32"));
                });
            });

            runner.testGroup("create(int,int,int,int,int,int,int)", () ->
            {
                runner.test("with 2018, 1, 2, 3, 4, 5, 6", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(2018, 1, 2, 3, 4, 5, 6);
                    test.assertEqual(2018, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(2, dateTime.getDayOfMonth());
                    test.assertEqual(3, dateTime.getHourOfDay());
                    test.assertEqual(4, dateTime.getMinute());
                    test.assertEqual(5, dateTime.getSecond());
                    test.assertEqual(6, dateTime.getMillisecond());
                    test.assertEqual(0, dateTime.getMicrosecond());
                    test.assertEqual(0, dateTime.getNanosecond());
                    test.assertEqual(Duration.zero, dateTime.getOffset());
                    test.assertSame(dateTime, dateTime.toUTC());
                });
            });

            runner.testGroup("toOffset(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 2, 3, 4, 5);
                    test.assertThrows(() -> dateTime.toOffset(null),
                        new PreConditionFailure("offset cannot be null."));
                });

                runner.test("with same offset", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 2, 3, 4, 5);
                    test.assertSame(dateTime, dateTime.toOffset(dateTime.getOffset()));
                });

                runner.test("from +00:00 to +03:00", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 2, 3, 4, 5);
                    final DateTime convertedDateTime = dateTime.toOffset(Duration.hours(3));
                    test.assertEqual(1, convertedDateTime.getYear());
                    test.assertEqual(2, convertedDateTime.getMonth());
                    test.assertEqual(3, convertedDateTime.getDayOfMonth());
                    test.assertEqual(7, convertedDateTime.getHourOfDay());
                    test.assertEqual(5, convertedDateTime.getMinute());
                    test.assertEqual(Duration.hours(3), convertedDateTime.getOffset());
                });

                runner.test("from +00:00 to +15:01", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 2, 3, 4, 5);
                    final DateTime convertedDateTime = dateTime.toOffset(Duration.hours(15).plus(Duration.minutes(1)));
                    test.assertEqual(1, convertedDateTime.getYear());
                    test.assertEqual(2, convertedDateTime.getMonth());
                    test.assertEqual(3, convertedDateTime.getDayOfMonth());
                    test.assertEqual(19, convertedDateTime.getHourOfDay());
                    test.assertEqual(6, convertedDateTime.getMinute());
                    test.assertEqual(Duration.hours(15).plus(Duration.minutes(1)), convertedDateTime.getOffset());
                });

                runner.test("from +00:00 to -09:30", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 2, 3, 4, 5);
                    final DateTime convertedDateTime = dateTime.toOffset(Duration.hours(-9).plus(Duration.minutes(-30)));
                    test.assertEqual(1, convertedDateTime.getYear());
                    test.assertEqual(2, convertedDateTime.getMonth());
                    test.assertEqual(2, convertedDateTime.getDayOfMonth());
                    test.assertEqual(18, convertedDateTime.getHourOfDay());
                    test.assertEqual(35, convertedDateTime.getMinute());
                    test.assertEqual(Duration.hours(-9).plus(Duration.minutes(-30)), convertedDateTime.getOffset());
                });

                runner.test("from -08:00 to +00:00", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(1, 2, 3, 4, 5, Duration.hours(-8));
                    final DateTime convertedDateTime = dateTime.toOffset(Duration.zero);
                    test.assertEqual(1, convertedDateTime.getYear());
                    test.assertEqual(2, convertedDateTime.getMonth());
                    test.assertEqual(3, convertedDateTime.getDayOfMonth());
                    test.assertEqual(12, convertedDateTime.getHourOfDay());
                    test.assertEqual(5, convertedDateTime.getMinute());
                    test.assertEqual(Duration.zero, convertedDateTime.getOffset());
                });
            });

            runner.testGroup("lessThan(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.lessThan(null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.lessThan(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(9))));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.lessThan(dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.lessThan(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10))));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.lessThan(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(11))));
                });
            });

            runner.testGroup("lessThanOrEqualTo(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.lessThanOrEqualTo(null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.lessThanOrEqualTo(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(9))));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.lessThanOrEqualTo(dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.lessThanOrEqualTo(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10))));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.lessThanOrEqualTo(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(11))));
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.equals((Object)null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.equals((Object)DateTime.createFromDurationSinceEpoch(Duration.milliseconds(9))));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.equals((Object)dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.equals((Object)DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10))));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.equals((Object)DateTime.createFromDurationSinceEpoch(Duration.milliseconds(11))));
                });
            });

            runner.testGroup("equals(DateTime)", () ->
            {
                final Action3<DateTime,DateTime,Boolean> equalsTest = (DateTime lhs, DateTime rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    null,
                    false);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(9)),
                    false);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    true);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1.57379537769488179E18)),
                    DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1.57379537769488179E18)),
                    true);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1574907047894711040.0)),
                    DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1574907047894711040.0)),
                    true);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(11)),
                    false);
            });

            runner.testGroup("equals(DateTime,Duration)", () ->
            {
                runner.test("with null marginOfError", (Test test) ->
                {
                    test.assertThrows(() -> DateTime.create(1, 2, 3).equals(null, null),
                        new PreConditionFailure("marginOfError cannot be null."));
                });

                final Action4<DateTime,DateTime,Duration,Boolean> equalsTest = (DateTime lhs, DateTime rhs, Duration marginOfError, Boolean expected) ->
                {
                    runner.test("with " + lhs + ", " + rhs + ", and " + marginOfError, (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs, marginOfError));
                    });
                };

                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    null,
                    Duration.zero,
                    false);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    null,
                    Duration.minutes(5),
                    false);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(9)),
                    Duration.zero,
                    false);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(9)),
                    Duration.milliseconds(0.001),
                    false);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(9)),
                    Duration.milliseconds(1),
                    true);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    Duration.zero,
                    true);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1.57379537769488179E18)),
                    DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1.57379537769488179E18)),
                    Duration.zero,
                    true);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1574907047894711040.0)),
                    DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1574907047894711040.0)),
                    Duration.zero,
                    true);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(11)),
                    Duration.zero,
                    false);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(11)),
                    Duration.milliseconds(0.9999),
                    false);
                equalsTest.run(
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)),
                    DateTime.createFromDurationSinceEpoch(Duration.milliseconds(11)),
                    Duration.milliseconds(1.00001),
                    true);
            });

            runner.testGroup("greaterThanOrEqualTo(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.greaterThanOrEqualTo(null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.greaterThanOrEqualTo(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(9))));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.greaterThanOrEqualTo(dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.greaterThanOrEqualTo(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10))));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.greaterThanOrEqualTo(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(11))));
                });
            });

            runner.testGroup("greaterThan(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.greaterThan(null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertTrue(dateTime.greaterThan(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(9))));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.greaterThan(dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.greaterThan(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10))));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertFalse(dateTime.greaterThan(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(11))));
                });
            });

            runner.testGroup("plus(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertThrows(() -> dateTime.plus(null),
                        new PreConditionFailure("duration cannot be null."));
                });

                runner.test("with 0 seconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertSame(dateTime, dateTime.plus(Duration.seconds(0)));
                });

                runner.test("with 100 milliseconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(2018, 11, 3, 16, 8, 59, 437);
                    final Duration duration = Duration.milliseconds(100);
                    final DateTime actual = dateTime.plus(duration);
                    final DateTime expected = DateTime.create(2018, 11, 3, 16, 8, 59, 537);
                    test.assertEqual(expected, actual);
                });

                runner.test("with 0.1 seconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.create(2018, 11, 3, 16, 8, 59, 437);
                    final Duration duration = Duration.seconds(0.1);
                    final DateTime actual = dateTime.plus(duration);
                    final DateTime expected = DateTime.create(2018, 11, 3, 16, 8, 59, 537);
                    test.assertEqual(expected, actual);
                });

                runner.test("with 10 seconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10010)), dateTime.plus(Duration.seconds(10)));
                });

                runner.test("with -2 milliseconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(8)), dateTime.plus(Duration.milliseconds(-2)));
                });
            });

            runner.testGroup("minus(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertThrows(
                        () -> dateTime.minus((Duration)null),
                        new NullPointerException());
                });

                runner.test("with 0 seconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertSame(dateTime, dateTime.minus(Duration.seconds(0)));
                });

                runner.test("with 10 seconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10010));
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10)), dateTime.minus(Duration.seconds(10)));
                });

                runner.test("with -2 milliseconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.createFromDurationSinceEpoch(Duration.milliseconds(10));
                    test.assertEqual(DateTime.createFromDurationSinceEpoch(Duration.milliseconds(12)), dateTime.minus(Duration.milliseconds(-2)));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with UTC time", (Test test) ->
                {
                    test.assertEqual("2009-02-13T23:31:30.123Z", DateTime.createFromDurationSinceEpoch(Duration.milliseconds(1234567890123L)).toString());
                });

                runner.test("with negative offset", (Test test) ->
                {
                    test.assertEqual("2009-02-13T16:31:30.123-07:00", DateTime.createFromDurationSinceEpoch(Duration.milliseconds(1234567890123L), Duration.hours(-7)).toString());
                });

                runner.test("with milliseconds and positive offset", (Test test) ->
                {
                    test.assertEqual("2009-02-14T10:01:30.123+10:30", DateTime.createFromDurationSinceEpoch(Duration.milliseconds(1234567890123L), Duration.hours(10.5)).toString());
                });

                runner.test("with microseconds and positive offset", (Test test) ->
                {
                    test.assertEqual("1970-01-15T17:26:07.890123+10:30", DateTime.createFromDurationSinceEpoch(Duration.microseconds(1234567890123L), Duration.hours(10.5)).toString());
                });

                runner.test("with nanoseconds and positive offset", (Test test) ->
                {
                    test.assertEqual("1970-01-01T10:50:34.567890123+10:30", DateTime.createFromDurationSinceEpoch(Duration.nanoseconds(1234567890123L), Duration.hours(10.5)).toString());
                });
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> DateTime.parse(text).await(), expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseErrorTest.run("", new PreConditionFailure("text cannot be empty."));
                parseErrorTest.run("2009-02-13", new java.time.format.DateTimeParseException("Text '2009-02-13' could not be parsed at index 10", "2009-02-13", 10));
                parseErrorTest.run("2009/02/13", new java.time.format.DateTimeParseException("Text '2009/02/13' could not be parsed at index 4", "2009/02/13", 4));

                final Action2<String,DateTime> parseTest = (String text, DateTime expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, DateTime.parse(text).await());
                    });
                };

                parseTest.run("2009-02-13T23:31:30Z", DateTime.create(2009, 2, 13, 23, 31, 30));
                parseTest.run("2009-02-13T23:31:30.123Z", DateTime.create(2009, 2, 13, 23, 31, 30, 123));
                parseTest.run("2009-02-13T16:31:30.123-07:00", DateTime.create(2009, 2, 13, 16, 31, 30, 123, Duration.hours(-7)));
                parseTest.run("2019-11-17T00:53:34.400000Z", DateTime.create(2019, 11, 17, 0, 53, 34, 400));
            });
        });
    }
}
