package qub;

public class DateTimeTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(DateTime.class, () ->
        {
            runner.test("localNow()", (Test test) ->
            {
                final DateTime dateTime = DateTime.localNow();
                test.assertNotNull(dateTime);
            });

            runner.test("utcNow()", (Test test) ->
            {
                final DateTime dateTime = DateTime.utcNow();
                test.assertNotNull(dateTime);
                test.assertEqual(Duration.milliseconds(0), dateTime.getTimeZoneOffset());
            });

            runner.testGroup("local(long)", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    final DateTime dateTime = DateTime.local(0);
                    test.assertNotNull(dateTime);
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(16, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                });

                runner.test("with 1", (Test test) ->
                {
                    final DateTime dateTime = DateTime.local(1);
                    test.assertNotNull(dateTime);
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(16, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(1, dateTime.getMillisecond());
                });

                runner.test("with 999", (Test test) ->
                {
                    final DateTime dateTime = DateTime.local(999);
                    test.assertNotNull(dateTime);
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(16, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                });

                runner.test("with 1000", (Test test) ->
                {
                    final DateTime dateTime = DateTime.local(1000);
                    test.assertNotNull(dateTime);
                    test.assertEqual(1969, dateTime.getYear());
                    test.assertEqual(12, dateTime.getMonth());
                    test.assertEqual(31, dateTime.getDayOfMonth());
                    test.assertEqual(16, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(1, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                });
            });

            runner.testGroup("utc(long)", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(0);
                    test.assertNotNull(dateTime);
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(Duration.milliseconds(0), dateTime.getTimeZoneOffset());
                });

                runner.test("with 1", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(1);
                    test.assertNotNull(dateTime);
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(1, dateTime.getMillisecond());
                    test.assertEqual(Duration.milliseconds(0), dateTime.getTimeZoneOffset());
                });

                runner.test("with 999", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(999);
                    test.assertNotNull(dateTime);
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(999, dateTime.getMillisecond());
                    test.assertEqual(Duration.milliseconds(0), dateTime.getTimeZoneOffset());
                });

                runner.test("with 1000", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(1000);
                    test.assertNotNull(dateTime);
                    test.assertEqual(1970, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(1, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(1, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(Duration.milliseconds(0), dateTime.getTimeZoneOffset());
                });
            });

            runner.testGroup("date(int,int,int)", () ->
            {
                runner.test("with 1, 2, 3", (Test test) ->
                {
                    final DateTime dateTime = DateTime.date(1, 2, 3);
                    test.assertEqual(1, dateTime.getYear());
                    test.assertEqual(2, dateTime.getMonth());
                    test.assertEqual(3, dateTime.getDayOfMonth());
                    test.assertEqual(0, dateTime.getHourOfDay());
                    test.assertEqual(0, dateTime.getMinute());
                    test.assertEqual(0, dateTime.getSecond());
                    test.assertEqual(0, dateTime.getMillisecond());
                    test.assertEqual(Duration.milliseconds(0), dateTime.getTimeZoneOffset());
                });
            });

            runner.testGroup("local(int,int,int,int,int,int,int)", () ->
            {
                runner.test("with 2018, 1, 2, 3, 4, 5, 6", (Test test) ->
                {
                    final DateTime dateTime = DateTime.local(2018, 1, 2, 3, 4, 5, 6);
                    test.assertEqual(2018, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(2, dateTime.getDayOfMonth());
                    test.assertEqual(3, dateTime.getHourOfDay());
                    test.assertEqual(4, dateTime.getMinute());
                    test.assertEqual(5, dateTime.getSecond());
                    test.assertEqual(6, dateTime.getMillisecond());
                });
            });

            runner.testGroup("utc(int,int,int,int,int,int,int)", () ->
            {
                runner.test("with 2018, 1, 2, 3, 4, 5, 6", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(2018, 1, 2, 3, 4, 5, 6);
                    test.assertEqual(2018, dateTime.getYear());
                    test.assertEqual(1, dateTime.getMonth());
                    test.assertEqual(2, dateTime.getDayOfMonth());
                    test.assertEqual(3, dateTime.getHourOfDay());
                    test.assertEqual(4, dateTime.getMinute());
                    test.assertEqual(5, dateTime.getSecond());
                    test.assertEqual(6, dateTime.getMillisecond());
                    test.assertEqual(Duration.milliseconds(0), dateTime.getTimeZoneOffset());
                });
            });

            runner.testGroup("lessThan(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.lessThan(null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.lessThan(DateTime.utc(9)));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.lessThan(dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.lessThan(DateTime.utc(10)));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.lessThan(DateTime.utc(11)));
                });
            });

            runner.testGroup("lessThanOrEqualTo(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.lessThanOrEqualTo(null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.lessThanOrEqualTo(DateTime.utc(9)));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.lessThanOrEqualTo(dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.lessThanOrEqualTo(DateTime.utc(10)));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.lessThanOrEqualTo(DateTime.utc(11)));
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.equals((Object)null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.equals((Object)DateTime.utc(9)));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.equals((Object)dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.equals((Object)DateTime.utc(10)));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.equals((Object)DateTime.utc(11)));
                });
            });

            runner.testGroup("equals(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.equals(null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.equals(DateTime.utc(9)));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.equals(dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.equals(DateTime.utc(10)));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.equals(DateTime.utc(11)));
                });
            });

            runner.testGroup("greaterThanOrEqualTo(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.greaterThanOrEqualTo(null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.greaterThanOrEqualTo(DateTime.utc(9)));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.greaterThanOrEqualTo(dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.greaterThanOrEqualTo(DateTime.utc(10)));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.greaterThanOrEqualTo(DateTime.utc(11)));
                });
            });

            runner.testGroup("greaterThan(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.greaterThan(null));
                });

                runner.test("with lesser", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertTrue(dateTime.greaterThan(DateTime.utc(9)));
                });

                runner.test("with same", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.greaterThan(dateTime));
                });

                runner.test("with equal", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.greaterThan(DateTime.utc(10)));
                });

                runner.test("with greater", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertFalse(dateTime.greaterThan(DateTime.utc(11)));
                });
            });

            runner.testGroup("plus(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertThrows(() -> dateTime.plus(null));
                });

                runner.test("with 0 seconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertSame(dateTime, dateTime.plus(Duration.seconds(0)));
                });

                runner.test("with 100 milliseconds", (Test test) ->
                {
                    // 2018-11-03T16:08:59.437-06:00
                    final DateTime dateTime = DateTime.local(2018, 11, 3, 16, 8, 59, 437);
                    final Duration duration = Duration.milliseconds(100);
                    final DateTime actual = dateTime.plus(duration);
                    final DateTime expected = DateTime.local(2018, 11, 3, 16, 8, 59, 537);
                    test.assertEqual(expected, actual);
                });

                runner.test("with 0.1 seconds", (Test test) ->
                {
                    // 2018-11-03T16:08:59.437-06:00
                    final DateTime dateTime = DateTime.local(2018, 11, 3, 16, 8, 59, 437);
                    final Duration duration = Duration.seconds(0.1);
                    final DateTime actual = dateTime.plus(duration);
                    final DateTime expected = DateTime.local(2018, 11, 3, 16, 8, 59, 537);
                    test.assertEqual(expected, actual);
                });

                runner.test("with 10 seconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertEqual(DateTime.utc(10010), dateTime.plus(Duration.seconds(10)));
                });

                runner.test("with -2 milliseconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertEqual(DateTime.utc(8), dateTime.plus(Duration.milliseconds(-2)));
                });
            });

            runner.testGroup("minus(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertThrows(
                        () -> dateTime.minus((Duration)null),
                        new NullPointerException());
                });

                runner.test("with 0 seconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertSame(dateTime, dateTime.minus(Duration.seconds(0)));
                });

                runner.test("with 10 seconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10010);
                    test.assertEqual(DateTime.utc(10), dateTime.minus(Duration.seconds(10)));
                });

                runner.test("with -2 milliseconds", (Test test) ->
                {
                    final DateTime dateTime = DateTime.utc(10);
                    test.assertEqual(DateTime.utc(12), dateTime.minus(Duration.milliseconds(-2)));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with UTC time", (Test test) ->
                {
                    test.assertEqual("2009-02-13T23:31:30.123+00:00", DateTime.utc(1234567890123L).toString());
                });

                runner.test("with non-UTC time", (Test test) ->
                {
                    test.assertEqual("2009-02-13T15:31:30.123-07:00", DateTime.local(1234567890123L).toString());
                });
            });
        });
    }
}
