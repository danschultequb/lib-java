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
        });
    }
}
