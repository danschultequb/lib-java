package qub;

public class DurationTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Duration", () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                assertDuration(test,
                    new Duration(1234, DurationUnits.Hours),
                    1234,
                    DurationUnits.Hours);
            });

            runner.test("nanoseconds()", (Test test) ->
            {
                assertDuration(test,
                    Duration.nanoseconds(0.57),
                    0.57,
                    DurationUnits.Nanoseconds);
            });

            runner.test("microseconds()", (Test test) ->
            {
                assertDuration(test,
                    Duration.microseconds(0.1),
                    0.1,
                    DurationUnits.Microseconds);
            });

            runner.test("milliseconds()", (Test test) ->
            {
                assertDuration(test,
                    Duration.milliseconds(0.2),
                    0.2,
                    DurationUnits.Milliseconds);
            });

            runner.test("seconds()", (Test test) ->
            {
                assertDuration(test,
                    Duration.seconds(0.3),
                    0.3,
                    DurationUnits.Seconds);
            });

            runner.test("minutes()", (Test test) ->
            {
                assertDuration(test,
                    Duration.minutes(0.3),
                    0.3,
                    DurationUnits.Minutes);
            });

            runner.test("hours()", (Test test) ->
            {
                assertDuration(test,
                    Duration.hours(0.4),
                    0.4,
                    DurationUnits.Hours);
            });

            runner.test("days()", (Test test) ->
            {
                assertDuration(test,
                    Duration.days(0.5),
                    0.5,
                    DurationUnits.Days);
            });

            runner.test("weeks()", (Test test) ->
            {
                assertDuration(test,
                    Duration.weeks(0.6),
                    0.6,
                    DurationUnits.Weeks);
            });

            runner.testGroup("convertTo()", () ->
            {
                final Action4<Double,DurationUnits,DurationUnits,Double> convertToWithValueTest = (Double startValue, DurationUnits startUnits, DurationUnits targetUnits, Double expectedValue) ->
                {
                    runner.test("from " + startValue + " " + startUnits + " to " + targetUnits, (Test test) ->
                    {
                        final Duration startDuration = new Duration(startValue, startUnits);

                        final Duration convertedDuration = startDuration.convertTo(targetUnits);
                        assertDuration(test, convertedDuration, expectedValue, targetUnits);

                        final Duration convertedBackDuration = convertedDuration.convertTo(startUnits);
                        assertDuration(test, convertedBackDuration, startValue, startUnits, 0.000000000000001);
                    });
                };

                final Action3<DurationUnits,DurationUnits,Double> convertToTest = (DurationUnits startUnits, DurationUnits targetUnits, Double expectedValue) ->
                {
                    convertToWithValueTest.run(1.0, startUnits, targetUnits, expectedValue);
                };

                convertToTest.run(DurationUnits.Nanoseconds, DurationUnits.Nanoseconds, 1.0);
                convertToTest.run(DurationUnits.Nanoseconds, DurationUnits.Microseconds, 0.001);
                convertToTest.run(DurationUnits.Nanoseconds, DurationUnits.Milliseconds, 0.000001);
                convertToTest.run(DurationUnits.Nanoseconds, DurationUnits.Seconds, 0.000000001);
                convertToTest.run(DurationUnits.Nanoseconds, DurationUnits.Minutes, 0.000000000016666666666666667);
                convertToTest.run(DurationUnits.Nanoseconds, DurationUnits.Hours, 0.0000000000002777777777777778);
                convertToTest.run(DurationUnits.Nanoseconds, DurationUnits.Days, 0.000000000000011574074074074074);
                convertToTest.run(DurationUnits.Nanoseconds, DurationUnits.Weeks, 0.0000000000000016534391534391534);

                convertToTest.run(DurationUnits.Microseconds, DurationUnits.Nanoseconds, 1000.0);
                convertToTest.run(DurationUnits.Microseconds, DurationUnits.Microseconds, 1.0);
                convertToTest.run(DurationUnits.Microseconds, DurationUnits.Milliseconds, 0.001);
                convertToTest.run(DurationUnits.Microseconds, DurationUnits.Seconds, 0.000001);
                convertToTest.run(DurationUnits.Microseconds, DurationUnits.Minutes, 0.000000016666666666666667);
                convertToTest.run(DurationUnits.Microseconds, DurationUnits.Hours, 0.00000000027777777777777777);
                convertToTest.run(DurationUnits.Microseconds, DurationUnits.Days, 0.000000000011574074074074074);
                convertToTest.run(DurationUnits.Microseconds, DurationUnits.Weeks, 0.0000000000016534391534391534);

                convertToTest.run(DurationUnits.Milliseconds, DurationUnits.Nanoseconds, 1000000.0);
                convertToTest.run(DurationUnits.Milliseconds, DurationUnits.Microseconds, 1000.0);
                convertToTest.run(DurationUnits.Milliseconds, DurationUnits.Milliseconds, 1.0);
                convertToTest.run(DurationUnits.Milliseconds, DurationUnits.Seconds, 0.001);
                convertToTest.run(DurationUnits.Milliseconds, DurationUnits.Minutes, 0.000016666666666666667);
                convertToTest.run(DurationUnits.Milliseconds, DurationUnits.Hours, 0.00000027777777777777776);
                convertToTest.run(DurationUnits.Milliseconds, DurationUnits.Days, 0.000000011574074074074074);
                convertToTest.run(DurationUnits.Milliseconds, DurationUnits.Weeks, 0.0000000016534391534391535);

                convertToTest.run(DurationUnits.Seconds, DurationUnits.Nanoseconds, 1000000000.0);
                convertToTest.run(DurationUnits.Seconds, DurationUnits.Microseconds, 1000000.0);
                convertToTest.run(DurationUnits.Seconds, DurationUnits.Milliseconds, 1000.0);
                convertToTest.run(DurationUnits.Seconds, DurationUnits.Seconds, 1.0);
                convertToTest.run(DurationUnits.Seconds, DurationUnits.Minutes, 0.016666666666666666);
                convertToTest.run(DurationUnits.Seconds, DurationUnits.Hours, 0.0002777777777777778);
                convertToTest.run(DurationUnits.Seconds, DurationUnits.Days, 0.000011574074074074073);
                convertToTest.run(DurationUnits.Seconds, DurationUnits.Weeks, 0.0000016534391534391535);

                convertToTest.run(DurationUnits.Minutes, DurationUnits.Nanoseconds, 60000000000.0);
                convertToTest.run(DurationUnits.Minutes, DurationUnits.Microseconds, 60000000.0);
                convertToTest.run(DurationUnits.Minutes, DurationUnits.Milliseconds, 60000.0);
                convertToTest.run(DurationUnits.Minutes, DurationUnits.Seconds, 60.0);
                convertToTest.run(DurationUnits.Minutes, DurationUnits.Minutes, 1.0);
                convertToTest.run(DurationUnits.Minutes, DurationUnits.Hours, 0.016666666666666666);
                convertToTest.run(DurationUnits.Minutes, DurationUnits.Days, 0.0006944444444444445);
                convertToTest.run(DurationUnits.Minutes, DurationUnits.Weeks, 0.0000992063492063492);

                convertToTest.run(DurationUnits.Hours, DurationUnits.Nanoseconds, 3600000000000.0);
                convertToTest.run(DurationUnits.Hours, DurationUnits.Microseconds, 3600000000.0);
                convertToTest.run(DurationUnits.Hours, DurationUnits.Milliseconds, 3600000.0);
                convertToTest.run(DurationUnits.Hours, DurationUnits.Seconds, 3600.0);
                convertToTest.run(DurationUnits.Hours, DurationUnits.Minutes, 60.0);
                convertToTest.run(DurationUnits.Hours, DurationUnits.Hours, 1.0);
                convertToTest.run(DurationUnits.Hours, DurationUnits.Days, 0.041666666666666664);
                convertToTest.run(DurationUnits.Hours, DurationUnits.Weeks, 0.005952380952380952);

                convertToTest.run(DurationUnits.Days, DurationUnits.Nanoseconds, 86400000000000.0);
                convertToTest.run(DurationUnits.Days, DurationUnits.Microseconds, 86400000000.0);
                convertToTest.run(DurationUnits.Days, DurationUnits.Milliseconds, 86400000.0);
                convertToTest.run(DurationUnits.Days, DurationUnits.Seconds, 86400.0);
                convertToTest.run(DurationUnits.Days, DurationUnits.Minutes, 1440.0);
                convertToTest.run(DurationUnits.Days, DurationUnits.Hours, 24.0);
                convertToTest.run(DurationUnits.Days, DurationUnits.Days, 1.0);
                convertToTest.run(DurationUnits.Days, DurationUnits.Weeks, 0.14285714285714285);

                convertToTest.run(DurationUnits.Weeks, DurationUnits.Nanoseconds, 604800000000000.0);
                convertToTest.run(DurationUnits.Weeks, DurationUnits.Microseconds, 604800000000.0);
                convertToTest.run(DurationUnits.Weeks, DurationUnits.Milliseconds, 604800000.0);
                convertToTest.run(DurationUnits.Weeks, DurationUnits.Seconds, 604800.0);
                convertToTest.run(DurationUnits.Weeks, DurationUnits.Minutes, 10080.0);
                convertToTest.run(DurationUnits.Weeks, DurationUnits.Hours, 168.0);
                convertToTest.run(DurationUnits.Weeks, DurationUnits.Days, 7.0);
                convertToTest.run(DurationUnits.Weeks, DurationUnits.Weeks, 1.0);

                convertToWithValueTest.run(Double.MAX_VALUE, DurationUnits.Nanoseconds, DurationUnits.Weeks, 2.972376215050125E293);
            });

            runner.testGroup("negate()", () ->
            {
                runner.test("with 0 value", (Test test) ->
                {
                    final Duration duration = Duration.seconds(0);
                    test.assertSame(duration, duration.negate());
                });

                runner.test("with non-0 value", (Test test) ->
                {
                    final Duration duration = Duration.hours(10);
                    test.assertEqual(Duration.hours(-10), duration.negate());
                });
            });

            runner.testGroup("plus()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Duration duration = Duration.seconds(1);
                    test.assertEqual(duration, duration.plus(null));
                });

                runner.test("with negative duration", (Test test) ->
                {
                    final Duration duration = Duration.seconds(1);
                    test.assertEqual(Duration.seconds(0.5), duration.plus(Duration.seconds(-0.5)));
                });

                runner.test("with zero duration", (Test test) ->
                {
                    final Duration duration = Duration.seconds(1);
                    test.assertEqual(duration, duration.plus(Duration.seconds(0)));
                });

                runner.test("with positive duration", (Test test) ->
                {
                    final Duration duration = Duration.seconds(1);
                    test.assertEqual(Duration.seconds(1.6), duration.plus(Duration.milliseconds(600)));
                });
            });

            runner.testGroup("times()", () ->
            {
                runner.test("by negative value", (Test test) ->
                {
                    test.assertEqual(Duration.seconds(-0.5), Duration.milliseconds(100).times(-5));
                });

                runner.test("by zero", (Test test) ->
                {
                    test.assertEqual(Duration.seconds(0), Duration.microseconds(1).times(0));
                });

                runner.test("by one", (Test test) ->
                {
                    test.assertEqual(Duration.minutes(2), Duration.minutes(2).times(1));
                });

                runner.test("by positive value", (Test test) ->
                {
                    test.assertEqual(Duration.hours(10), Duration.hours(1).times(10));
                });
            });

            runner.testGroup("dividedBy(double)", () ->
            {
                runner.test("by zero", (Test test) ->
                {
                    try
                    {
                        Duration.nanoseconds(50).dividedBy(0);
                        test.fail("Expected ArithmeticException");
                    }
                    catch (ArithmeticException e)
                    {
                        test.assertEqual("/ by zero", e.getMessage());
                    }
                });

                final Action3<Duration,Double,Duration> dividedByTest = (Duration dividend, Double divisor, Duration expectedQuotient) ->
                {
                    runner.test(dividend + " divided by " + divisor, (Test test) ->
                    {
                        test.assertEqual(expectedQuotient, dividend.dividedBy(divisor));
                    });
                };

                dividedByTest.run(Duration.nanoseconds(20), -5.0, Duration.nanoseconds(-4));
                dividedByTest.run(Duration.days(5), 1.0, Duration.days(5));
                dividedByTest.run(Duration.weeks(100), 10.0, Duration.weeks(10));
            });

            runner.testGroup("dividedBy(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try
                    {
                        Duration.seconds(10).dividedBy(null);
                        test.fail("Expected NullPointerException");
                    }
                    catch (NullPointerException ignored)
                    {
                    }
                });

                runner.test("with zero", (Test test) ->
                {
                    try
                    {
                        Duration.seconds(10).dividedBy(0);
                        test.fail("Expected ArithmeticException");
                    }
                    catch (ArithmeticException e)
                    {
                        test.assertEqual("/ by zero", e.getMessage());
                    }
                });

                final Action3<Duration,Duration,Double> dividedByTest = (Duration dividend, Duration divisor, Double expectedQuotient) ->
                {
                    runner.test("with " + dividend + " divided by " + divisor, (Test test) ->
                    {
                        test.assertEqual(expectedQuotient, dividend.dividedBy(divisor));
                    });
                };

                dividedByTest.run(Duration.seconds(10), Duration.seconds(-2), -5.0);
                dividedByTest.run(Duration.seconds(10), Duration.seconds(1), 10.0);
                dividedByTest.run(Duration.seconds(100), Duration.seconds(50), 2.0);
            });

            runner.testGroup("round()", () ->
            {
                final Action2<Duration,Duration> roundTest = (Duration value, Duration expectedRoundedValue) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expectedRoundedValue, value.round());
                    });
                };

                roundTest.run(Duration.nanoseconds(20), Duration.nanoseconds(20));
                roundTest.run(Duration.seconds(10.1), Duration.seconds(10));
                roundTest.run(Duration.days(0.9), Duration.days(1));
            });

            runner.testGroup("round(Duration)", () ->
            {
                runner.test("with null scale", (Test test) ->
                {
                    try
                    {
                        Duration.seconds(10).round(null);
                        test.fail("Expected NullPointerException");
                    }
                    catch (NullPointerException ignored)
                    {
                    }
                });

                final Action3<Duration,Duration,Duration> roundTest = (Duration value, Duration scale, Duration expectedRoundedValue) ->
                {
                    runner.test("with " + value + " rounded to nearest " + scale, (Test test) ->
                    {
                        test.assertEqual(expectedRoundedValue, value.round(scale));
                    });
                };

                roundTest.run(Duration.seconds(0), Duration.minutes(0), Duration.minutes(0));
                roundTest.run(Duration.minutes(1.5), Duration.hours(0), Duration.hours(0));
                roundTest.run(Duration.seconds(1), Duration.seconds(1), Duration.seconds(1));
                roundTest.run(Duration.seconds(9), Duration.seconds(5), Duration.seconds(10));
                roundTest.run(Duration.seconds(1.555), Duration.milliseconds(100), Duration.seconds(1.6));
            });

            runner.testGroup("round(double)", () ->
            {
                final Action3<Duration,Double,Duration> roundTest = (Duration value, Double scale, Duration expectedRoundedValue) ->
                {
                    runner.test("with " + value + " rounded to nearest " + scale, (Test test) ->
                    {
                        test.assertEqual(expectedRoundedValue, value.round(scale));
                    });
                };

                roundTest.run(Duration.seconds(0), 0.0, Duration.seconds(0));
                roundTest.run(Duration.seconds(1), 0.0, Duration.seconds(0));
                roundTest.run(Duration.seconds(1), 1.0, Duration.seconds(1));
                roundTest.run(Duration.seconds(9), 5.0, Duration.seconds(10));
                roundTest.run(Duration.seconds(1.555), 0.1, Duration.seconds(1.6));
            });

            runner.test("toNanoseconds()", (Test test) ->
            {
                final Duration duration = Duration.seconds(1);
                test.assertEqual(Duration.nanoseconds(1000000000), duration.toNanoseconds());
            });

            runner.test("toMicroseconds()", (Test test) ->
            {
                final Duration duration = Duration.seconds(1);
                test.assertEqual(Duration.microseconds(1000000), duration.toMicroseconds());
            });

            runner.test("toMilliseconds()", (Test test) ->
            {
                final Duration duration = Duration.seconds(1);
                test.assertEqual(Duration.milliseconds(1000), duration.toMilliseconds());
            });

            runner.test("toSeconds()", (Test test) ->
            {
                final Duration duration = Duration.minutes(1);
                test.assertEqual(Duration.seconds(60), duration.toSeconds());
            });

            runner.test("toMinutes()", (Test test) ->
            {
                final Duration duration = Duration.hours(2);
                test.assertEqual(Duration.minutes(120), duration.toMinutes());
            });

            runner.test("toHours()", (Test test) ->
            {
                final Duration duration = Duration.minutes(150);
                test.assertEqual(Duration.hours(2.5), duration.toHours());
            });

            runner.test("toDays()", (Test test) ->
            {
                final Duration duration = Duration.weeks(1);
                test.assertEqual(Duration.days(7), duration.toDays());
            });

            runner.test("toWeeks()", (Test test) ->
            {
                final Duration duration = Duration.days(21);
                test.assertEqual(Duration.weeks(3), duration.toWeeks());
            });

            runner.test("toString()", (Test test) ->
            {
                test.assertEqual("1.0 Nanoseconds", Duration.nanoseconds(1).toString());
                test.assertEqual("30.0 Days", Duration.days(30).toString());
            });

            runner.test("toString(String)", (Test test) ->
            {
                test.assertEqual("123.5 Microseconds", Duration.microseconds(123.456).toString("#.#"));
                test.assertEqual("123 Microseconds", Duration.microseconds(123.456).toString("#"));
                test.assertEqual("123.46 Microseconds", Duration.microseconds(123.456).toString("#0.0#"));
                test.assertEqual("123.0 Microseconds", Duration.microseconds(123).toString("#0.0#"));
            });

            runner.test("equals()", (Test test) ->
            {
                final Duration duration = Duration.milliseconds(123);

                test.assertFalse(duration.equals((Object)null));
                test.assertFalse(duration.equals((Duration)null));

                test.assertFalse(duration.equals((Object)"123"));

                test.assertTrue(duration.equals((Object)duration));
                test.assertTrue(duration.equals(duration));

                test.assertFalse(duration.equals(Duration.milliseconds(122)));

                test.assertFalse(duration.equals(Duration.microseconds(123)));
            });
        });
    }

    private static void assertDuration(Test test, Duration duration, double expectedValue, DurationUnits expectedUnits)
    {
        test.assertNotNull(duration);
        test.assertEqual(expectedValue, duration.getValue());
        test.assertEqual(expectedUnits, duration.getUnits());
    }

    private static void assertDuration(Test test, Duration duration, double expectedValue, DurationUnits expectedUnits, double marginOfError)
    {
        test.assertNotNull(duration);
        test.assertEqual(expectedValue, duration.getValue(), marginOfError);
        test.assertEqual(expectedUnits, duration.getUnits());
    }
}
