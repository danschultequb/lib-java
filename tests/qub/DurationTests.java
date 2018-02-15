package qub;

public class DurationTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Duration", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertDuration(test,
                            new Duration(1234, DurationUnits.Hours),
                            1234,
                            DurationUnits.Hours);
                    }
                });

                runner.test("nanoseconds()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertDuration(test,
                            Duration.nanoseconds(0.57),
                            0.57,
                            DurationUnits.Nanoseconds);
                    }
                });

                runner.test("microseconds()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertDuration(test,
                            Duration.microseconds(0.1),
                            0.1,
                            DurationUnits.Microseconds);
                    }
                });

                runner.test("milliseconds()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertDuration(test,
                            Duration.milliseconds(0.2),
                            0.2,
                            DurationUnits.Milliseconds);
                    }
                });

                runner.test("seconds()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertDuration(test,
                            Duration.seconds(0.3),
                            0.3,
                            DurationUnits.Seconds);
                    }
                });

                runner.test("minutes()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertDuration(test,
                            Duration.minutes(0.3),
                            0.3,
                            DurationUnits.Minutes);
                    }
                });

                runner.test("hours()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertDuration(test,
                            Duration.hours(0.4),
                            0.4,
                            DurationUnits.Hours);
                    }
                });

                runner.test("days()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertDuration(test,
                            Duration.days(0.5),
                            0.5,
                            DurationUnits.Days);
                    }
                });

                runner.test("weeks()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        assertDuration(test,
                            Duration.weeks(0.6),
                            0.6,
                            DurationUnits.Weeks);
                    }
                });

                runner.testGroup("convertTo()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action4<Double,DurationUnits,DurationUnits,Double> convertToWithValueTest = new Action4<Double,DurationUnits, DurationUnits, Double>()
                        {
                            @Override
                            public void run(final Double startValue, final DurationUnits startUnits, final DurationUnits targetUnits, final Double expectedValue)
                            {
                                runner.test("from " + startValue + " " + startUnits + " to " + targetUnits, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Duration startDuration = new Duration(startValue, startUnits);

                                        final Duration convertedDuration = startDuration.convertTo(targetUnits);
                                        assertDuration(test, convertedDuration, expectedValue, targetUnits);

                                        final Duration convertedBackDuration = convertedDuration.convertTo(startUnits);
                                        assertDuration(test, convertedBackDuration, startValue, startUnits, 0.000000000000001);
                                    }
                                });
                            }
                        };

                        final Action3<DurationUnits,DurationUnits,Double> convertToTest = new Action3<DurationUnits, DurationUnits, Double>()
                        {
                            @Override
                            public void run(final DurationUnits startUnits, final DurationUnits targetUnits, final Double expectedValue)
                            {
                                convertToWithValueTest.run(1.0, startUnits, targetUnits, expectedValue);
                            }
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
                    }
                });
            }
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
