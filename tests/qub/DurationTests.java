package qub;

public interface DurationTests
{
    static void test(final TestRunner runner)
    {
        runner.testGroup(Duration.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                assertDuration(test,
                    new Duration(1234, DurationUnit.Hours),
                    1234,
                    DurationUnit.Hours);
            });

            runner.test("nanoseconds()", (Test test) ->
            {
                assertDuration(test,
                    Duration.nanoseconds(0.57),
                    0.57,
                    DurationUnit.Nanoseconds);
            });

            runner.test("microseconds()", (Test test) ->
            {
                assertDuration(test,
                    Duration.microseconds(0.1),
                    0.1,
                    DurationUnit.Microseconds);
            });

            runner.test("milliseconds()", (Test test) ->
            {
                assertDuration(test,
                    Duration.milliseconds(0.2),
                    0.2,
                    DurationUnit.Milliseconds);
            });

            runner.test("seconds()", (Test test) ->
            {
                assertDuration(test,
                    Duration.seconds(0.3),
                    0.3,
                    DurationUnit.Seconds);
            });

            runner.test("minutes()", (Test test) ->
            {
                assertDuration(test,
                    Duration.minutes(0.3),
                    0.3,
                    DurationUnit.Minutes);
            });

            runner.test("hours()", (Test test) ->
            {
                assertDuration(test,
                    Duration.hours(0.4),
                    0.4,
                    DurationUnit.Hours);
            });

            runner.test("days()", (Test test) ->
            {
                assertDuration(test,
                    Duration.days(0.5),
                    0.5,
                    DurationUnit.Days);
            });

            runner.test("weeks()", (Test test) ->
            {
                assertDuration(test,
                    Duration.weeks(0.6),
                    0.6,
                    DurationUnit.Weeks);
            });

            runner.testGroup("convertTo()", () ->
            {
                final Action4<Double,DurationUnit,DurationUnit,Double> convertToWithValueTest = (Double startValue, DurationUnit startUnits, DurationUnit targetUnits, Double expectedValue) ->
                {
                    runner.test("create " + startValue + " " + startUnits + " to " + targetUnits, (Test test) ->
                    {
                        final Duration startDuration = new Duration(startValue, startUnits);

                        final Duration convertedDuration = startDuration.convertTo(targetUnits);
                        assertDuration(test, convertedDuration, expectedValue, targetUnits);

                        final Duration convertedBackDuration = convertedDuration.convertTo(startUnits);
                        assertDuration(test, convertedBackDuration, startValue, startUnits, 0.000000000000001);
                    });
                };

                final Action3<DurationUnit,DurationUnit,Double> convertToTest = (DurationUnit startUnits, DurationUnit targetUnits, Double expectedValue) ->
                {
                    convertToWithValueTest.run(1.0, startUnits, targetUnits, expectedValue);
                };

                convertToTest.run(DurationUnit.Nanoseconds, DurationUnit.Nanoseconds, 1.0);
                convertToTest.run(DurationUnit.Nanoseconds, DurationUnit.Microseconds, 0.001);
                convertToTest.run(DurationUnit.Nanoseconds, DurationUnit.Milliseconds, 0.000001);
                convertToTest.run(DurationUnit.Nanoseconds, DurationUnit.Seconds, 0.000000001);
                convertToTest.run(DurationUnit.Nanoseconds, DurationUnit.Minutes, 0.000000000016666666666666667);
                convertToTest.run(DurationUnit.Nanoseconds, DurationUnit.Hours, 0.0000000000002777777777777778);
                convertToTest.run(DurationUnit.Nanoseconds, DurationUnit.Days, 0.000000000000011574074074074074);
                convertToTest.run(DurationUnit.Nanoseconds, DurationUnit.Weeks, 0.0000000000000016534391534391534);

                convertToTest.run(DurationUnit.Microseconds, DurationUnit.Nanoseconds, 1000.0);
                convertToTest.run(DurationUnit.Microseconds, DurationUnit.Microseconds, 1.0);
                convertToTest.run(DurationUnit.Microseconds, DurationUnit.Milliseconds, 0.001);
                convertToTest.run(DurationUnit.Microseconds, DurationUnit.Seconds, 0.000001);
                convertToTest.run(DurationUnit.Microseconds, DurationUnit.Minutes, 0.000000016666666666666667);
                convertToTest.run(DurationUnit.Microseconds, DurationUnit.Hours, 0.00000000027777777777777777);
                convertToTest.run(DurationUnit.Microseconds, DurationUnit.Days, 0.000000000011574074074074074);
                convertToTest.run(DurationUnit.Microseconds, DurationUnit.Weeks, 0.0000000000016534391534391534);

                convertToTest.run(DurationUnit.Milliseconds, DurationUnit.Nanoseconds, 1000000.0);
                convertToTest.run(DurationUnit.Milliseconds, DurationUnit.Microseconds, 1000.0);
                convertToTest.run(DurationUnit.Milliseconds, DurationUnit.Milliseconds, 1.0);
                convertToTest.run(DurationUnit.Milliseconds, DurationUnit.Seconds, 0.001);
                convertToTest.run(DurationUnit.Milliseconds, DurationUnit.Minutes, 0.000016666666666666667);
                convertToTest.run(DurationUnit.Milliseconds, DurationUnit.Hours, 0.00000027777777777777776);
                convertToTest.run(DurationUnit.Milliseconds, DurationUnit.Days, 0.000000011574074074074074);
                convertToTest.run(DurationUnit.Milliseconds, DurationUnit.Weeks, 0.0000000016534391534391535);

                convertToTest.run(DurationUnit.Seconds, DurationUnit.Nanoseconds, 1000000000.0);
                convertToTest.run(DurationUnit.Seconds, DurationUnit.Microseconds, 1000000.0);
                convertToTest.run(DurationUnit.Seconds, DurationUnit.Milliseconds, 1000.0);
                convertToTest.run(DurationUnit.Seconds, DurationUnit.Seconds, 1.0);
                convertToTest.run(DurationUnit.Seconds, DurationUnit.Minutes, 0.016666666666666666);
                convertToTest.run(DurationUnit.Seconds, DurationUnit.Hours, 0.0002777777777777778);
                convertToTest.run(DurationUnit.Seconds, DurationUnit.Days, 0.000011574074074074073);
                convertToTest.run(DurationUnit.Seconds, DurationUnit.Weeks, 0.0000016534391534391535);

                convertToTest.run(DurationUnit.Minutes, DurationUnit.Nanoseconds, 60000000000.0);
                convertToTest.run(DurationUnit.Minutes, DurationUnit.Microseconds, 60000000.0);
                convertToTest.run(DurationUnit.Minutes, DurationUnit.Milliseconds, 60000.0);
                convertToTest.run(DurationUnit.Minutes, DurationUnit.Seconds, 60.0);
                convertToTest.run(DurationUnit.Minutes, DurationUnit.Minutes, 1.0);
                convertToTest.run(DurationUnit.Minutes, DurationUnit.Hours, 0.016666666666666666);
                convertToTest.run(DurationUnit.Minutes, DurationUnit.Days, 0.0006944444444444445);
                convertToTest.run(DurationUnit.Minutes, DurationUnit.Weeks, 0.0000992063492063492);

                convertToTest.run(DurationUnit.Hours, DurationUnit.Nanoseconds, 3600000000000.0);
                convertToTest.run(DurationUnit.Hours, DurationUnit.Microseconds, 3600000000.0);
                convertToTest.run(DurationUnit.Hours, DurationUnit.Milliseconds, 3600000.0);
                convertToTest.run(DurationUnit.Hours, DurationUnit.Seconds, 3600.0);
                convertToTest.run(DurationUnit.Hours, DurationUnit.Minutes, 60.0);
                convertToTest.run(DurationUnit.Hours, DurationUnit.Hours, 1.0);
                convertToTest.run(DurationUnit.Hours, DurationUnit.Days, 0.041666666666666664);
                convertToTest.run(DurationUnit.Hours, DurationUnit.Weeks, 0.005952380952380952);

                convertToTest.run(DurationUnit.Days, DurationUnit.Nanoseconds, 86400000000000.0);
                convertToTest.run(DurationUnit.Days, DurationUnit.Microseconds, 86400000000.0);
                convertToTest.run(DurationUnit.Days, DurationUnit.Milliseconds, 86400000.0);
                convertToTest.run(DurationUnit.Days, DurationUnit.Seconds, 86400.0);
                convertToTest.run(DurationUnit.Days, DurationUnit.Minutes, 1440.0);
                convertToTest.run(DurationUnit.Days, DurationUnit.Hours, 24.0);
                convertToTest.run(DurationUnit.Days, DurationUnit.Days, 1.0);
                convertToTest.run(DurationUnit.Days, DurationUnit.Weeks, 0.14285714285714285);

                convertToTest.run(DurationUnit.Weeks, DurationUnit.Nanoseconds, 604800000000000.0);
                convertToTest.run(DurationUnit.Weeks, DurationUnit.Microseconds, 604800000000.0);
                convertToTest.run(DurationUnit.Weeks, DurationUnit.Milliseconds, 604800000.0);
                convertToTest.run(DurationUnit.Weeks, DurationUnit.Seconds, 604800.0);
                convertToTest.run(DurationUnit.Weeks, DurationUnit.Minutes, 10080.0);
                convertToTest.run(DurationUnit.Weeks, DurationUnit.Hours, 168.0);
                convertToTest.run(DurationUnit.Weeks, DurationUnit.Days, 7.0);
                convertToTest.run(DurationUnit.Weeks, DurationUnit.Weeks, 1.0);

                convertToWithValueTest.run(Double.MAX_VALUE, DurationUnit.Nanoseconds, DurationUnit.Weeks, 2.972376215050125E293);
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
                    test.assertThrows(() -> Duration.nanoseconds(50).dividedBy(0),
                        new PreConditionFailure("rhs (0.0) must not be 0.0."));
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
                    test.assertThrows(() -> Duration.seconds(10).dividedBy(null),
                        new PreConditionFailure("rhs cannot be null."));
                });

                runner.test("with zero", (Test test) ->
                {
                    test.assertThrows(() -> Duration.seconds(10).dividedBy(Duration.seconds(0)),
                        new PreConditionFailure("rhs.getValue() (0.0) must not be 0.0."));
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

            runner.testGroup("absoluteValue()", () ->
            {
                runner.test("with -3 seconds", (Test test) ->
                {
                    test.assertEqual(Duration.seconds(3), Duration.seconds(-3).absoluteValue());
                });

                runner.test("with 0 days", (Test test) ->
                {
                    test.assertEqual(Duration.days(0), Duration.days(0).absoluteValue());
                });

                runner.test("with 1 millisecond", (Test test) ->
                {
                    test.assertEqual(Duration.milliseconds(1), Duration.milliseconds(1).absoluteValue());
                });
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

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Duration,Object,Boolean> equalsTest = (Duration duration, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + duration + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, duration.equals(rhs));
                    });
                };

                equalsTest.run(Duration.milliseconds(1), null, false);
                equalsTest.run(Duration.milliseconds(1), "1", false);
                equalsTest.run(Duration.milliseconds(1000), Duration.milliseconds(10), false);
                equalsTest.run(Duration.milliseconds(1), Duration.seconds(1), false);
                equalsTest.run(Duration.milliseconds(1000), Duration.seconds(1), true);
                equalsTest.run(Duration.milliseconds(1), Duration.seconds(0.001), true);
                equalsTest.run(Duration.seconds(1), Duration.milliseconds(1000), true);
                equalsTest.run(Duration.seconds(0.001), Duration.milliseconds(1), true);
                equalsTest.run(Duration.milliseconds(60000), Duration.minutes(1), true);
                equalsTest.run(Duration.minutes(1), Duration.milliseconds(60000), true);
            });

            runner.testGroup("equals(Duration)", () ->
            {
                final Action3<Duration,Duration,Boolean> equalsTest = (Duration duration, Duration rhs, Boolean expected) ->
                {
                    runner.test("with " + duration + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, duration.equals(rhs));
                    });
                };

                equalsTest.run(Duration.milliseconds(1), null, false);
                equalsTest.run(Duration.milliseconds(1000), Duration.milliseconds(10), false);
                equalsTest.run(Duration.milliseconds(1), Duration.seconds(1), false);
                equalsTest.run(Duration.milliseconds(1000), Duration.seconds(1), true);
                equalsTest.run(Duration.milliseconds(1), Duration.seconds(0.001), true);
                equalsTest.run(Duration.seconds(1), Duration.milliseconds(1000), true);
                equalsTest.run(Duration.seconds(0.001), Duration.milliseconds(1), true);
                equalsTest.run(Duration.milliseconds(60000), Duration.minutes(1), true);
                equalsTest.run(Duration.minutes(1), Duration.milliseconds(60000), true);
            });

            runner.testGroup("compareTo(Duration)", () ->
            {
                final Action3<Duration,Duration,Comparison> compareToTest = (Duration duration, Duration rhs, Comparison expected) ->
                {
                    runner.test("with " + duration + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, duration.compareTo(rhs));
                    });
                };

                compareToTest.run(Duration.milliseconds(1), null, Comparison.GreaterThan);
                compareToTest.run(Duration.milliseconds(1000), Duration.milliseconds(10), Comparison.GreaterThan);
                compareToTest.run(Duration.milliseconds(1), Duration.seconds(1), Comparison.LessThan);
                compareToTest.run(Duration.milliseconds(1000), Duration.seconds(1), Comparison.Equal);
                compareToTest.run(Duration.milliseconds(1), Duration.seconds(0.001), Comparison.Equal);
                compareToTest.run(Duration.seconds(1), Duration.milliseconds(1000), Comparison.Equal);
                compareToTest.run(Duration.seconds(0.001), Duration.milliseconds(1), Comparison.Equal);
                compareToTest.run(Duration.milliseconds(60000), Duration.minutes(1), Comparison.Equal);
                compareToTest.run(Duration.minutes(1), Duration.milliseconds(60000), Comparison.Equal);
            });

            runner.testGroup("compareTo(Duration,Duration)", () ->
            {
                runner.test("with null marginOfError", (Test test) ->
                {
                    test.assertThrows(() -> Duration.seconds(1).compareTo(Duration.seconds(2), null),
                        new PreConditionFailure("marginOfError cannot be null."));
                });

                runner.test("with negative marginOfError", (Test test) ->
                {
                    test.assertThrows(() -> Duration.seconds(1).compareTo(Duration.seconds(2), Duration.nanoseconds(-1)),
                        new PreConditionFailure("marginOfError (-1.0 Nanoseconds) must be greater than or equal to 0.0 Seconds."));
                });

                final Action4<Duration,Duration,Duration,Comparison> compareToTest = (Duration duration, Duration rhs, Duration marginOfError, Comparison expected) ->
                {
                    runner.test("with " + duration + " and " + rhs + " (+/-" + marginOfError + ")", (Test test) ->
                    {
                        test.assertEqual(expected, duration.compareTo(rhs, marginOfError));
                    });
                };

                compareToTest.run(Duration.milliseconds(1), null, Duration.nanoseconds(1), Comparison.GreaterThan);
                compareToTest.run(Duration.milliseconds(1000), Duration.milliseconds(10), Duration.nanoseconds(1), Comparison.GreaterThan);
                compareToTest.run(Duration.milliseconds(1), Duration.seconds(1), Duration.nanoseconds(1), Comparison.LessThan);
                compareToTest.run(Duration.milliseconds(1000), Duration.seconds(1), Duration.nanoseconds(1), Comparison.Equal);
                compareToTest.run(Duration.milliseconds(1), Duration.seconds(0.001), Duration.nanoseconds(1), Comparison.Equal);
                compareToTest.run(Duration.seconds(1), Duration.milliseconds(1000), Duration.nanoseconds(1), Comparison.Equal);
                compareToTest.run(Duration.seconds(0.001), Duration.milliseconds(1), Duration.nanoseconds(1), Comparison.Equal);
                compareToTest.run(Duration.milliseconds(60000), Duration.minutes(1), Duration.nanoseconds(1), Comparison.Equal);
                compareToTest.run(Duration.minutes(1), Duration.milliseconds(60000), Duration.nanoseconds(1), Comparison.Equal);

                compareToTest.run(Duration.seconds(1), Duration.seconds(1.1), Duration.seconds(0.05), Comparison.LessThan);
                compareToTest.run(Duration.seconds(1), Duration.seconds(1.1), Duration.seconds(0.5), Comparison.Equal);
                compareToTest.run(Duration.seconds(1.1), Duration.seconds(1), Duration.seconds(0.099), Comparison.GreaterThan);
                compareToTest.run(Duration.seconds(0.04999995231628418), Duration.milliseconds(50), Duration.microseconds(1), Comparison.Equal);
            });
        });
    }

    static void assertDuration(Test test, Duration duration, double expectedValue, DurationUnit expectedUnits)
    {
        test.assertNotNull(duration);
        test.assertEqual(expectedValue, duration.getValue());
        test.assertEqual(expectedUnits, duration.getUnits());
    }

    static void assertDuration(Test test, Duration duration, double expectedValue, DurationUnit expectedUnits, double marginOfError)
    {
        test.assertNotNull(duration);
        test.assertEqual(expectedValue, duration.getValue(), marginOfError);
        test.assertEqual(expectedUnits, duration.getUnits());
    }
}
