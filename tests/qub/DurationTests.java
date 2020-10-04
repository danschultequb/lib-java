package qub;

public interface DurationTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Duration.class, () ->
        {
            runner.test("create(double,DurationUnit)", (Test test) ->
            {
                DurationTests.assertDuration(test,
                    Duration.create(1234, DurationUnit.Hours),
                    1234,
                    DurationUnit.Hours);
            });

            runner.test("nanoseconds()", (Test test) ->
            {
                DurationTests.assertDuration(test,
                    Duration.nanoseconds(0.57),
                    0.57,
                    DurationUnit.Nanoseconds);
            });

            runner.test("microseconds()", (Test test) ->
            {
                DurationTests.assertDuration(test,
                    Duration.microseconds(0.1),
                    0.1,
                    DurationUnit.Microseconds);
            });

            runner.test("milliseconds()", (Test test) ->
            {
                DurationTests.assertDuration(test,
                    Duration.milliseconds(0.2),
                    0.2,
                    DurationUnit.Milliseconds);
            });

            runner.test("seconds()", (Test test) ->
            {
                DurationTests.assertDuration(test,
                    Duration.seconds(0.3),
                    0.3,
                    DurationUnit.Seconds);
            });

            runner.test("minutes()", (Test test) ->
            {
                DurationTests.assertDuration(test,
                    Duration.minutes(0.3),
                    0.3,
                    DurationUnit.Minutes);
            });

            runner.test("hours()", (Test test) ->
            {
                DurationTests.assertDuration(test,
                    Duration.hours(0.4),
                    0.4,
                    DurationUnit.Hours);
            });

            runner.test("days()", (Test test) ->
            {
                DurationTests.assertDuration(test,
                    Duration.days(0.5),
                    0.5,
                    DurationUnit.Days);
            });

            runner.test("weeks()", (Test test) ->
            {
                DurationTests.assertDuration(test,
                    Duration.weeks(0.6),
                    0.6,
                    DurationUnit.Weeks);
            });
        });
    }

    static void test(TestRunner runner, Function2<Double,DurationUnit,? extends Duration> creator)
    {
        runner.testGroup(Duration.class, () ->
        {
            runner.testGroup("convertTo()", () ->
            {
                final Action4<Double,DurationUnit,DurationUnit,Double> convertToWithValueTest = (Double startValue, DurationUnit startUnits, DurationUnit targetUnits, Double expectedValue) ->
                {
                    runner.test("change " + startValue + " " + startUnits + " to " + targetUnits, (Test test) ->
                    {
                        final Duration startDuration = creator.run(startValue, startUnits);

                        final Duration convertedDuration = startDuration.convertTo(targetUnits);
                        DurationTests.assertDuration(test, convertedDuration, expectedValue, targetUnits, 0.00000000000001);

                        final Duration convertedBackDuration = convertedDuration.convertTo(startUnits);
                        DurationTests.assertDuration(test, convertedBackDuration, startValue, startUnits, 0.000000000000001);
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
                    final Duration duration = creator.run(0.0, DurationUnit.Seconds);
                    DurationTests.assertDuration(test, duration.negate(), duration);
                });

                runner.test("with non-0 value", (Test test) ->
                {
                    final Duration duration = creator.run(10.0, DurationUnit.Hours);
                    DurationTests.assertDuration(test, duration.negate(), -10.0, DurationUnit.Hours);
                });
            });

            runner.testGroup("plus()", () ->
            {
                final Action3<Duration,Duration,Throwable> plusErrorTest = (Duration lhs, Duration rhs, Throwable expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertThrows(() -> lhs.plus(rhs), expected);
                    });
                };

                plusErrorTest.run(creator.run(1.0, DurationUnit.Seconds), null, new PreConditionFailure("rhs cannot be null."));

                final Action3<Duration,Duration,Duration> plusTest = (Duration lhs, Duration rhs, Duration expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        DurationTests.assertDuration(test, lhs.plus(rhs), expected);
                    });
                };

                plusTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(-0.5, DurationUnit.Seconds), creator.run(0.5, DurationUnit.Seconds));
                plusTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(0.0, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Seconds));
                plusTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(600.0, DurationUnit.Milliseconds), creator.run(1.6, DurationUnit.Seconds));
            });

            runner.testGroup("times()", () ->
            {
                final Action3<Duration,Double,Duration> timesTest = (Duration lhs, Double rhs, Duration expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        DurationTests.assertDuration(test, lhs.times(rhs), expected);
                    });
                };

                timesTest.run(creator.run(100.0, DurationUnit.Milliseconds), -5.0, creator.run(-500.0, DurationUnit.Milliseconds));
                timesTest.run(creator.run(1.0, DurationUnit.Milliseconds), 0.0, creator.run(0.0, DurationUnit.Milliseconds));
                timesTest.run(creator.run(2.0, DurationUnit.Minutes), 1.0, creator.run(2.0, DurationUnit.Minutes));
                timesTest.run(creator.run(1.0, DurationUnit.Hours), 10.0, creator.run(10.0, DurationUnit.Hours));
            });

            runner.testGroup("dividedBy(double)", () ->
            {
                final Action3<Duration,Double,Throwable> dividedByErrorTest = (Duration dividend, Double divisor, Throwable expected) ->
                {
                    runner.test(dividend + " divided by " + divisor, (Test test) ->
                    {
                        test.assertThrows(() -> dividend.dividedBy(divisor),
                            expected);
                    });
                };
                
                dividedByErrorTest.run(creator.run(50.0, DurationUnit.Nanoseconds), 0.0, new PreConditionFailure("rhs (0.0) must not be 0.0."));

                final Action3<Duration,Double,Duration> dividedByTest = (Duration dividend, Double divisor, Duration expectedQuotient) ->
                {
                    runner.test(dividend + " divided by " + divisor, (Test test) ->
                    {
                        DurationTests.assertDuration(test, dividend.dividedBy(divisor), expectedQuotient);
                    });
                };

                dividedByTest.run(creator.run(20.0, DurationUnit.Nanoseconds), -5.0, creator.run(-4.0, DurationUnit.Nanoseconds));
                dividedByTest.run(creator.run(5.0, DurationUnit.Days), 1.0, creator.run(5.0, DurationUnit.Days));
                dividedByTest.run(creator.run(100.0, DurationUnit.Weeks), 10.0, creator.run(10.0, DurationUnit.Weeks));
            });

            runner.testGroup("dividedBy(Duration)", () ->
            {
                final Action3<Duration,Duration,Throwable> dividedByErrorTest = (Duration dividend, Duration divisor, Throwable expected) ->
                {
                    runner.test(dividend + " divided by " + divisor, (Test test) ->
                    {
                        test.assertThrows(() -> dividend.dividedBy(divisor),
                            expected);
                    });
                };
                
                dividedByErrorTest.run(creator.run(10.0, DurationUnit.Seconds), null, new PreConditionFailure("rhs cannot be null."));
                dividedByErrorTest.run(creator.run(10.0, DurationUnit.Seconds), creator.run(0.0, DurationUnit.Seconds), new PreConditionFailure("rhs.getValue() (0.0) must not be 0.0."));

                final Action3<Duration, Duration,Double> dividedByTest = (Duration dividend, Duration divisor, Double expectedQuotient) ->
                {
                    runner.test("with " + dividend + " divided by " + divisor, (Test test) ->
                    {
                        test.assertEqual(expectedQuotient, dividend.dividedBy(divisor));
                    });
                };

                dividedByTest.run(creator.run(10.0, DurationUnit.Seconds), creator.run(-2.0, DurationUnit.Seconds), -5.0);
                dividedByTest.run(creator.run(10.0, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Seconds), 10.0);
                dividedByTest.run(creator.run(100.0, DurationUnit.Seconds), creator.run(50.0, DurationUnit.Seconds), 2.0);
            });

            runner.testGroup("round()", () ->
            {
                final Action2<Duration, Duration> roundTest = (Duration value, Duration expectedRoundedValue) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expectedRoundedValue, value.round());
                    });
                };

                roundTest.run(creator.run(20.0, DurationUnit.Nanoseconds), creator.run(20.0, DurationUnit.Nanoseconds));
                roundTest.run(creator.run(10.1, DurationUnit.Seconds), creator.run(10.0, DurationUnit.Seconds));
                roundTest.run(creator.run(0.9, DurationUnit.Days), creator.run(1.0, DurationUnit.Days));
            });

            runner.testGroup("round(Duration)", () ->
            {
                final Action3<Duration,Duration,Throwable> roundErrorTest = (Duration value, Duration scale, Throwable expected) ->
                {
                    runner.test("with " + value + " rounded to nearest " + scale, (Test test) ->
                    {
                        test.assertThrows(() -> value.round(scale), expected);
                    });
                };

                roundErrorTest.run(creator.run(10.0, DurationUnit.Seconds), null, new PreConditionFailure("scale cannot be null."));

                final Action3<Duration,Duration,Duration> roundTest = (Duration value, Duration scale, Duration expectedRoundedValue) ->
                {
                    runner.test("with " + value + " rounded to nearest " + scale, (Test test) ->
                    {
                        DurationTests.assertDuration(test, value.round(scale), expectedRoundedValue);
                    });
                };

                roundTest.run(creator.run(0.0, DurationUnit.Seconds), creator.run(0.0, DurationUnit.Minutes), creator.run(0.0, DurationUnit.Minutes));
                roundTest.run(creator.run(1.5, DurationUnit.Minutes), creator.run(0.0, DurationUnit.Hours), creator.run(0.0, DurationUnit.Hours));
                roundTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Seconds));
                roundTest.run(creator.run(9.0, DurationUnit.Seconds), creator.run(5.0, DurationUnit.Seconds), creator.run(10.0, DurationUnit.Seconds));
                roundTest.run(creator.run(1.555, DurationUnit.Seconds), creator.run(100.0, DurationUnit.Milliseconds), creator.run(1600.0, DurationUnit.Milliseconds));
            });

            runner.testGroup("round(double)", () ->
            {
                final Action3<Duration,Double, Duration> roundTest = (Duration value, Double scale, Duration expectedRoundedValue) ->
                {
                    runner.test("with " + value + " rounded to nearest " + scale, (Test test) ->
                    {
                        test.assertEqual(expectedRoundedValue, value.round(scale));
                    });
                };

                roundTest.run(creator.run(0.0, DurationUnit.Seconds), 0.0, creator.run(0.0, DurationUnit.Seconds));
                roundTest.run(creator.run(1.0, DurationUnit.Seconds), 0.0, creator.run(0.0, DurationUnit.Seconds));
                roundTest.run(creator.run(1.0, DurationUnit.Seconds), 1.0, creator.run(1.0, DurationUnit.Seconds));
                roundTest.run(creator.run(9.0, DurationUnit.Seconds), 5.0, creator.run(10.0, DurationUnit.Seconds));
                roundTest.run(creator.run(1.555, DurationUnit.Seconds), 0.1, creator.run(1.6, DurationUnit.Seconds));
            });

            runner.test("toNanoseconds()", (Test test) ->
            {
                final Duration duration = creator.run(1.0, DurationUnit.Seconds);
                DurationTests.assertDuration(test, duration.toNanoseconds(), creator.run(1000000000.0, DurationUnit.Nanoseconds));
            });

            runner.test("toMicroseconds()", (Test test) ->
            {
                final Duration duration = creator.run(1.0, DurationUnit.Seconds);
                DurationTests.assertDuration(test, duration.toMicroseconds(), creator.run(1000000.0, DurationUnit.Microseconds));
            });

            runner.test("toMilliseconds()", (Test test) ->
            {
                final Duration duration = creator.run(1.0, DurationUnit.Seconds);
                DurationTests.assertDuration(test, duration.toMilliseconds(), creator.run(1000.0, DurationUnit.Milliseconds));
            });

            runner.test("toSeconds()", (Test test) ->
            {
                final Duration duration = creator.run(1.0, DurationUnit.Minutes);
                DurationTests.assertDuration(test, duration.toSeconds(), creator.run(60.0, DurationUnit.Seconds));
            });

            runner.test("toMinutes()", (Test test) ->
            {
                final Duration duration = creator.run(2.0, DurationUnit.Hours);
                DurationTests.assertDuration(test, duration.toMinutes(), 120, DurationUnit.Minutes);
            });

            runner.test("toHours()", (Test test) ->
            {
                final Duration duration = Duration.minutes(150);
                DurationTests.assertDuration(test, duration.toHours(), 2.5, DurationUnit.Hours);
            });

            runner.test("toDays()", (Test test) ->
            {
                final Duration duration = Duration.weeks(1);
                DurationTests.assertDuration(test, duration.toDays(), 7, DurationUnit.Days);
            });

            runner.test("toWeeks()", (Test test) ->
            {
                final Duration duration = Duration.days(21);
                DurationTests.assertDuration(test, duration.toWeeks(), 3, DurationUnit.Weeks);
            });

            runner.testGroup("absoluteValue()", () ->
            {
                final Action2<Duration,Duration> absoluteValueTest = (Duration duration, Duration expected) ->
                {
                    runner.test("with " + duration, (Test test) ->
                    {
                        DurationTests.assertDuration(test, duration.absoluteValue(), expected);
                    });
                };

                absoluteValueTest.run(creator.run(-3.0, DurationUnit.Seconds), creator.run(3.0, DurationUnit.Seconds));
                absoluteValueTest.run(creator.run(0.0, DurationUnit.Days), creator.run(0.0, DurationUnit.Days));
                absoluteValueTest.run(creator.run(1.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Milliseconds));
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Duration,String> toStringTest = (Duration duration, String expected) ->
                {
                    runner.test("with " + duration, (Test test) ->
                    {
                        test.assertEqual(expected, duration.toString());
                    });
                };

                toStringTest.run(creator.run(1.0, DurationUnit.Nanoseconds), "1.0 Nanoseconds");
                toStringTest.run(creator.run(30.0, DurationUnit.Days), "30.0 Days");
            });

            runner.testGroup("toString(String)", () ->
            {
                final Action3<Duration,String,Throwable> toStringErrorTest = (Duration duration, String format, Throwable expected) ->
                {
                    runner.test("with " + English.andList(duration, Strings.escapeAndQuote(format)), (Test test) ->
                    {
                        test.assertThrows(() -> duration.toString(format), expected);
                    });
                };

                toStringErrorTest.run(creator.run(1.23, DurationUnit.Seconds), null, new PreConditionFailure("format cannot be null."));

                final Action3<Duration,String,String> toStringTest = (Duration duration, String format, String expected) ->
                {
                    runner.test("with " + English.andList(duration, Strings.escapeAndQuote(format)), (Test test) ->
                    {
                        test.assertEqual(expected, duration.toString(format));
                    });
                };

                toStringTest.run(creator.run(123.456, DurationUnit.Microseconds), "#.#", "123.5 Microseconds");
                toStringTest.run(creator.run(123.456, DurationUnit.Microseconds), "#", "123 Microseconds");
                toStringTest.run(creator.run(123.456, DurationUnit.Microseconds), "#0.0#", "123.46 Microseconds");
                toStringTest.run(creator.run(123.0, DurationUnit.Microseconds), "#0.0#", "123.0 Microseconds");
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

                equalsTest.run(creator.run(1.0, DurationUnit.Milliseconds), null, false);
                equalsTest.run(creator.run(1.0, DurationUnit.Milliseconds), "1", false);
                equalsTest.run(creator.run(1000.0, DurationUnit.Milliseconds), creator.run(10.0, DurationUnit.Milliseconds), false);
                equalsTest.run(creator.run(1.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Seconds), false);
                equalsTest.run(creator.run(1000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Seconds), true);
                equalsTest.run(creator.run(1.0, DurationUnit.Milliseconds), creator.run(0.001, DurationUnit.Seconds), true);
                equalsTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(1000.0, DurationUnit.Milliseconds), true);
                equalsTest.run(creator.run(0.001, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Milliseconds), true);
                equalsTest.run(creator.run(60000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Minutes), true);
                equalsTest.run(creator.run(1.0, DurationUnit.Minutes), creator.run(60000.0, DurationUnit.Milliseconds), true);
            });

            runner.testGroup("equals(Duration)", () ->
            {
                final Action3<Duration, Duration,Boolean> equalsTest = (Duration duration, Duration rhs, Boolean expected) ->
                {
                    runner.test("with " + duration + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, duration.equals(rhs));
                    });
                };

                equalsTest.run(creator.run(1.0, DurationUnit.Milliseconds), null, false);
                equalsTest.run(creator.run(1000.0, DurationUnit.Milliseconds), creator.run(10.0, DurationUnit.Milliseconds), false);
                equalsTest.run(creator.run(1.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Seconds), false);
                equalsTest.run(creator.run(1000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Seconds), true);
                equalsTest.run(creator.run(1.0, DurationUnit.Milliseconds), creator.run(0.001, DurationUnit.Seconds), true);
                equalsTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(1000.0, DurationUnit.Milliseconds), true);
                equalsTest.run(creator.run(0.001, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Milliseconds), true);
                equalsTest.run(creator.run(60000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Minutes), true);
                equalsTest.run(creator.run(1.0, DurationUnit.Minutes), creator.run(60000.0, DurationUnit.Milliseconds), true);
            });

            runner.testGroup("compareTo(Duration)", () ->
            {
                final Action3<Duration, Duration,Comparison> compareToTest = (Duration duration, Duration rhs, Comparison expected) ->
                {
                    runner.test("with " + duration + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, duration.compareTo(rhs));
                    });
                };

                compareToTest.run(creator.run(1.0, DurationUnit.Milliseconds), null, Comparison.GreaterThan);
                compareToTest.run(creator.run(1000.0, DurationUnit.Milliseconds), creator.run(10.0, DurationUnit.Milliseconds), Comparison.GreaterThan);
                compareToTest.run(creator.run(1.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Seconds), Comparison.LessThan);
                compareToTest.run(creator.run(1000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Seconds), Comparison.Equal);
                compareToTest.run(creator.run(1.0, DurationUnit.Milliseconds), creator.run(0.001, DurationUnit.Seconds), Comparison.Equal);
                compareToTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(1000.0, DurationUnit.Milliseconds), Comparison.Equal);
                compareToTest.run(creator.run(0.001, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Milliseconds), Comparison.Equal);
                compareToTest.run(creator.run(60000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Minutes), Comparison.Equal);
                compareToTest.run(creator.run(1.0, DurationUnit.Minutes), creator.run(60000.0, DurationUnit.Milliseconds), Comparison.Equal);
            });

            runner.testGroup("compareTo(Duration,Duration)", () ->
            {
                runner.test("with null marginOfError", (Test test) ->
                {
                    test.assertThrows(() -> creator.run(1.0, DurationUnit.Seconds).compareTo(creator.run(2.0, DurationUnit.Seconds), null),
                        new PreConditionFailure("marginOfError cannot be null."));
                });

                runner.test("with negative marginOfError", (Test test) ->
                {
                    test.assertThrows(() -> creator.run(1.0, DurationUnit.Seconds).compareTo(creator.run(2.0, DurationUnit.Seconds), creator.run(-1.0, DurationUnit.Nanoseconds)),
                        new PreConditionFailure("marginOfError (-1.0 Nanoseconds) must be greater than or equal to 0.0 Seconds."));
                });

                final Action4<Duration, Duration, Duration,Comparison> compareToTest = (Duration duration, Duration rhs, Duration marginOfError, Comparison expected) ->
                {
                    runner.test("with " + duration + " and " + rhs + " (+/-" + marginOfError + ")", (Test test) ->
                    {
                        test.assertEqual(expected, duration.compareTo(rhs, marginOfError));
                    });
                };

                compareToTest.run(creator.run(1.0, DurationUnit.Milliseconds), null, creator.run(1.0, DurationUnit.Nanoseconds), Comparison.GreaterThan);
                compareToTest.run(creator.run(1000.0, DurationUnit.Milliseconds), creator.run(10.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Nanoseconds), Comparison.GreaterThan);
                compareToTest.run(creator.run(1.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Nanoseconds), Comparison.LessThan);
                compareToTest.run(creator.run(1000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Nanoseconds), Comparison.Equal);
                compareToTest.run(creator.run(1.0, DurationUnit.Milliseconds), creator.run(0.001, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Nanoseconds), Comparison.Equal);
                compareToTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(1000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Nanoseconds), Comparison.Equal);
                compareToTest.run(creator.run(0.001, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Nanoseconds), Comparison.Equal);
                compareToTest.run(creator.run(60000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Minutes), creator.run(1.0, DurationUnit.Nanoseconds), Comparison.Equal);
                compareToTest.run(creator.run(1.0, DurationUnit.Minutes), creator.run(60000.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Nanoseconds), Comparison.Equal);

                compareToTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(1.1, DurationUnit.Seconds), creator.run(0.05, DurationUnit.Seconds), Comparison.LessThan);
                compareToTest.run(creator.run(1.0, DurationUnit.Seconds), creator.run(1.1, DurationUnit.Seconds), creator.run(0.5, DurationUnit.Seconds), Comparison.Equal);
                compareToTest.run(creator.run(1.1, DurationUnit.Seconds), creator.run(1.0, DurationUnit.Seconds), creator.run(0.099, DurationUnit.Seconds), Comparison.GreaterThan);
                compareToTest.run(creator.run(0.04999995231628418, DurationUnit.Seconds), creator.run(50.0, DurationUnit.Milliseconds), creator.run(1.0, DurationUnit.Microseconds), Comparison.Equal);
            });
        });
    }

    static void assertDuration(Test test, Duration duration, Duration expected)
    {
        DurationTests.assertDuration(test, duration, expected.getValue(), expected.getUnits());
    }

    static void assertDuration(Test test, Duration duration, Duration expected, double marginOfError)
    {
        DurationTests.assertDuration(test, duration, expected.getValue(), expected.getUnits(), marginOfError);
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
