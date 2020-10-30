package qub;

public interface SpeedTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Speed.class, () ->
        {
            runner.testGroup("create(double,DistanceUnit,DurationUnit)", () ->
            {
                final Action4<Double,DistanceUnit,DurationUnit,Throwable> createErrorTest = (Double value, DistanceUnit distanceUnits, DurationUnit durationUnits, Throwable expected) ->
                {
                    runner.test("with " + English.andList(value, distanceUnits, durationUnits), (Test test) ->
                    {
                        test.assertThrows(() -> Speed.create(value, distanceUnits, durationUnits), expected);
                    });
                };

                createErrorTest.run(1.0, null, DurationUnit.Seconds, new PreConditionFailure("distanceUnits cannot be null."));
                createErrorTest.run(1.0, DistanceUnit.Meters, null, new PreConditionFailure("durationUnits cannot be null."));

                final Action3<Double,DistanceUnit,DurationUnit> createTest = (Double value, DistanceUnit distanceUnits, DurationUnit durationUnits) ->
                {
                    runner.test("with " + English.andList(value, distanceUnits, durationUnits), (Test test) ->
                    {
                        final Speed speed = Speed.create(value, distanceUnits, durationUnits);
                        test.assertNotNull(speed);
                        test.assertEqual(value, speed.getValue());
                        test.assertEqual(distanceUnits, speed.getDistanceUnits());
                        test.assertEqual(durationUnits, speed.getDurationUnits());
                    });
                };

                createTest.run(7.0, DistanceUnit.Feet, DurationUnit.Seconds);
                createTest.run(0.0, DistanceUnit.Miles, DurationUnit.Hours);
                createTest.run(55.0, DistanceUnit.Centimeters, DurationUnit.Nanoseconds);
            });

            runner.testGroup("milesPerHour(double)", () ->
            {
                final Action1<Double> milesPerHourTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        final Speed speed = Speed.milesPerHour(value);
                        test.assertNotNull(speed);
                        test.assertEqual(value, speed.getValue());
                        test.assertEqual(DistanceUnit.Miles, speed.getDistanceUnits());
                        test.assertEqual(DurationUnit.Hours, speed.getDurationUnits());
                    });
                };

                milesPerHourTest.run(-1.0);
                milesPerHourTest.run(0.0);
                milesPerHourTest.run(60.0);
            });

            runner.testGroup("kilometersPerHour(double)", () ->
            {
                final Action1<Double> kilometersPerHourTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        final Speed speed = Speed.kilometersPerHour(value);
                        test.assertNotNull(speed);
                        test.assertEqual(value, speed.getValue());
                        test.assertEqual(DistanceUnit.Kilometers, speed.getDistanceUnits());
                        test.assertEqual(DurationUnit.Hours, speed.getDurationUnits());
                    });
                };

                kilometersPerHourTest.run(-1.0);
                kilometersPerHourTest.run(0.0);
                kilometersPerHourTest.run(60.0);
            });

            runner.testGroup("feetPerSecond(double)", () ->
            {
                final Action1<Double> feetPerSecondTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        final Speed speed = Speed.feetPerSecond(value);
                        test.assertNotNull(speed);
                        test.assertEqual(value, speed.getValue());
                        test.assertEqual(DistanceUnit.Feet, speed.getDistanceUnits());
                        test.assertEqual(DurationUnit.Seconds, speed.getDurationUnits());
                    });
                };

                feetPerSecondTest.run(-1.0);
                feetPerSecondTest.run(0.0);
                feetPerSecondTest.run(60.0);
            });

            runner.testGroup("metersPerSecond(double)", () ->
            {
                final Action1<Double> metersPerSecondTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        final Speed speed = Speed.metersPerSecond(value);
                        test.assertNotNull(speed);
                        test.assertEqual(value, speed.getValue());
                        test.assertEqual(DistanceUnit.Meters, speed.getDistanceUnits());
                        test.assertEqual(DurationUnit.Seconds, speed.getDurationUnits());
                    });
                };

                metersPerSecondTest.run(-1.0);
                metersPerSecondTest.run(0.0);
                metersPerSecondTest.run(60.0);
            });

            runner.test("zero", (Test test) ->
            {
                final Speed zero = Speed.zero;
                test.assertNotNull(zero);
                test.assertEqual(0, zero.getValue());
                test.assertEqual(DistanceUnit.Miles, zero.getDistanceUnits());
                test.assertEqual(DurationUnit.Hours, zero.getDurationUnits());
            });

            runner.testGroup("convertTo(DistanceUnit)", () ->
            {
                final Action3<Speed,DistanceUnit,Throwable> convertToErrorTest = (Speed speed, DistanceUnit distanceUnit, Throwable expected) ->
                {
                    runner.test("with " + English.andList(speed, distanceUnit), (Test test) ->
                    {
                        test.assertThrows(() -> speed.convertTo(distanceUnit), expected);
                    });
                };

                convertToErrorTest.run(Speed.zero, null, new PreConditionFailure("distanceUnits cannot be null."));

                final Action3<Speed,DistanceUnit,Double> convertToTest = (Speed speed, DistanceUnit distanceUnits, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(speed, distanceUnits), (Test test) ->
                    {
                        final Speed converted = speed.convertTo(distanceUnits);
                        test.assertNotNull(converted);
                        test.assertEqual(distanceUnits, converted.getDistanceUnits());
                        test.assertEqual(speed.getDurationUnits(), converted.getDurationUnits());
                        test.assertEqual(expectedValue, converted.getValue());
                    });
                };

                convertToTest.run(Speed.create(1, DistanceUnit.Kilometers, DurationUnit.Seconds), DistanceUnit.Meters, 1000.0);
                convertToTest.run(Speed.create(1, DistanceUnit.Kilometers, DurationUnit.Seconds), DistanceUnit.Kilometers, 1.0);
            });

            runner.testGroup("convertTo(DurationUnit)", () ->
            {
                final Action3<Speed,DurationUnit,Throwable> convertToErrorTest = (Speed speed, DurationUnit durationUnits, Throwable expected) ->
                {
                    runner.test("with " + English.andList(speed, durationUnits), (Test test) ->
                    {
                        test.assertThrows(() -> speed.convertTo(durationUnits), expected);
                    });
                };

                convertToErrorTest.run(Speed.zero, null, new PreConditionFailure("durationUnits cannot be null."));

                final Action3<Speed,DurationUnit,Double> convertToTest = (Speed speed, DurationUnit durationUnits, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(speed, durationUnits), (Test test) ->
                    {
                        final Speed converted = speed.convertTo(durationUnits);
                        test.assertNotNull(converted);
                        test.assertEqual(speed.getDistanceUnits(), converted.getDistanceUnits());
                        test.assertEqual(durationUnits, converted.getDurationUnits());
                        test.assertEqual(expectedValue, converted.getValue());
                    });
                };

                convertToTest.run(Speed.create(1, DistanceUnit.Kilometers, DurationUnit.Seconds), DurationUnit.Milliseconds, 0.001);
                convertToTest.run(Speed.create(1, DistanceUnit.Kilometers, DurationUnit.Seconds), DurationUnit.Seconds, 1.0);
                convertToTest.run(Speed.create(60, DistanceUnit.Miles, DurationUnit.Minutes), DurationUnit.Seconds, 1.0);
            });

            runner.testGroup("convertTo(DistanceUnit,DurationUnit)", () ->
            {
                final Action4<Speed,DistanceUnit,DurationUnit,Throwable> convertToErrorTest = (Speed speed, DistanceUnit distanceUnits, DurationUnit durationUnits, Throwable expected) ->
                {
                    runner.test("with " + English.andList(speed, distanceUnits, durationUnits), (Test test) ->
                    {
                        test.assertThrows(() -> speed.convertTo(distanceUnits, durationUnits), expected);
                    });
                };

                convertToErrorTest.run(Speed.zero, null, DurationUnit.Days, new PreConditionFailure("distanceUnits cannot be null."));
                convertToErrorTest.run(Speed.zero, DistanceUnit.Feet, null, new PreConditionFailure("durationUnits cannot be null."));

                final Action4<Speed,DistanceUnit,DurationUnit,Double> convertToTest = (Speed speed, DistanceUnit distanceUnits, DurationUnit durationUnits, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(speed, distanceUnits, durationUnits), (Test test) ->
                    {
                        final Speed converted = speed.convertTo(distanceUnits, durationUnits);
                        test.assertNotNull(converted);
                        test.assertEqual(distanceUnits, converted.getDistanceUnits());
                        test.assertEqual(durationUnits, converted.getDurationUnits());
                        test.assertEqual(expectedValue, converted.getValue());
                    });
                };

                convertToTest.run(Speed.create(1, DistanceUnit.Kilometers, DurationUnit.Seconds), DistanceUnit.Kilometers, DurationUnit.Milliseconds, 0.001);
                convertToTest.run(Speed.create(1, DistanceUnit.Kilometers, DurationUnit.Seconds), DistanceUnit.Kilometers, DurationUnit.Seconds, 1.0);
                convertToTest.run(Speed.create(60, DistanceUnit.Miles, DurationUnit.Minutes), DistanceUnit.Feet, DurationUnit.Seconds, 5280.0);
            });

            runner.testGroup("toMilesPerHour()", () ->
            {
                final Action2<Speed,Double> toMilesPerHourTest = (Speed speed, Double expectedValue) ->
                {
                    runner.test("with " + speed, (Test test) ->
                    {
                        final Speed converted = speed.toMilesPerHour();
                        test.assertNotNull(converted);
                        test.assertEqual(DistanceUnit.Miles, converted.getDistanceUnits());
                        test.assertEqual(DurationUnit.Hours, converted.getDurationUnits());
                        test.assertEqual(expectedValue, converted.getValue());
                    });
                };

                toMilesPerHourTest.run(Speed.milesPerHour(1), 1.0);
                toMilesPerHourTest.run(Speed.create(2, DistanceUnit.Miles, DurationUnit.Minutes), 120.0);
            });

            runner.testGroup("toMetersPerSecond()", () ->
            {
                final Action2<Speed,Double> toMetersPerSecondTest = (Speed speed, Double expectedValue) ->
                {
                    runner.test("with " + speed, (Test test) ->
                    {
                        final Speed converted = speed.toMetersPerSecond();
                        test.assertNotNull(converted);
                        test.assertEqual(DistanceUnit.Meters, converted.getDistanceUnits());
                        test.assertEqual(DurationUnit.Seconds, converted.getDurationUnits());
                        test.assertEqual(expectedValue, converted.getValue());
                    });
                };

                toMetersPerSecondTest.run(Speed.kilometersPerHour(1), 0.2777777777777778);
                toMetersPerSecondTest.run(Speed.create(2, DistanceUnit.Millimeters, DurationUnit.Milliseconds), 2.0);
            });

            runner.testGroup("toKilometersPerHour()", () ->
            {
                final Action2<Speed,Double> toKilometersPerHourTest = (Speed speed, Double expectedValue) ->
                {
                    runner.test("with " + speed, (Test test) ->
                    {
                        final Speed converted = speed.toKilometersPerHour();
                        test.assertNotNull(converted);
                        test.assertEqual(DistanceUnit.Kilometers, converted.getDistanceUnits());
                        test.assertEqual(DurationUnit.Hours, converted.getDurationUnits());
                        test.assertEqual(expectedValue, converted.getValue());
                    });
                };

                toKilometersPerHourTest.run(Speed.kilometersPerHour(1), 1.0);
                toKilometersPerHourTest.run(Speed.create(2, DistanceUnit.Millimeters, DurationUnit.Milliseconds), 7.199999999999999);
            });

            runner.testGroup("negate()", () ->
            {
                final Action1<Speed> negateTest = (Speed speed) ->
                {
                    runner.test("with " + speed, (Test test) ->
                    {
                        final Speed negatedSpeed = speed.negate();
                        test.assertNotNull(negatedSpeed);
                        test.assertEqual(-speed.getValue(), negatedSpeed.getValue());
                        test.assertEqual(speed.getDistanceUnits(), negatedSpeed.getDistanceUnits());
                        test.assertEqual(speed.getDurationUnits(), negatedSpeed.getDurationUnits());
                    });
                };

                negateTest.run(Speed.zero);
                negateTest.run(Speed.feetPerSecond(0));
                negateTest.run(Speed.kilometersPerHour(200));
            });

            runner.testGroup("plus(Speed)", () ->
            {
                final Action3<Speed,Speed,Throwable> plusErrorTest = (Speed lhs, Speed rhs, Throwable expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertThrows(() -> lhs.plus(rhs), expected);
                    });
                };

                plusErrorTest.run(Speed.milesPerHour(5), null, new PreConditionFailure("rhs cannot be null."));

                final Action3<Speed,Speed,Double> plusTest = (Speed lhs, Speed rhs, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        final Speed sum = lhs.plus(rhs);
                        test.assertNotNull(sum);
                        test.assertEqual(expectedValue, sum.getValue());
                        test.assertEqual(lhs.getDistanceUnits(), sum.getDistanceUnits());
                        test.assertEqual(lhs.getDurationUnits(), sum.getDurationUnits());
                    });
                };

                plusTest.run(Speed.zero, Speed.zero, 0.0);
                plusTest.run(Speed.zero, Speed.metersPerSecond(3), 6.710808876163208);
                plusTest.run(Speed.milesPerHour(10), Speed.zero, 10.0);
                plusTest.run(Speed.kilometersPerHour(100), Speed.kilometersPerHour(-100), 0.0);
            });

            runner.testGroup("minus(Speed)", () ->
            {
                final Action3<Speed,Speed,Throwable> minusErrorTest = (Speed lhs, Speed rhs, Throwable expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertThrows(() -> lhs.minus(rhs), expected);
                    });
                };

                minusErrorTest.run(Speed.milesPerHour(5), null, new PreConditionFailure("rhs cannot be null."));

                final Action3<Speed,Speed,Double> minusTest = (Speed lhs, Speed rhs, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        final Speed difference = lhs.minus(rhs);
                        test.assertNotNull(difference);
                        test.assertEqual(expectedValue, difference.getValue());
                        test.assertEqual(lhs.getDistanceUnits(), difference.getDistanceUnits());
                        test.assertEqual(lhs.getDurationUnits(), difference.getDurationUnits());
                    });
                };

                minusTest.run(Speed.zero, Speed.zero, 0.0);
                minusTest.run(Speed.zero, Speed.metersPerSecond(3), -6.710808876163208);
                minusTest.run(Speed.milesPerHour(10), Speed.zero, 10.0);
                minusTest.run(Speed.kilometersPerHour(100), Speed.kilometersPerHour(-100), 200.0);
            });

            runner.testGroup("times(double)", () ->
            {
                final Action3<Speed,Double,Double> timesTest = (Speed lhs, Double rhs, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        final Speed product = lhs.times(rhs);
                        test.assertNotNull(product);
                        test.assertEqual(expectedValue, product.getValue());
                        test.assertEqual(lhs.getDistanceUnits(), product.getDistanceUnits());
                        test.assertEqual(lhs.getDurationUnits(), product.getDurationUnits());
                    });
                };

                timesTest.run(Speed.milesPerHour(60), 0.0, 0.0);
                timesTest.run(Speed.metersPerSecond(60), 0.0, 0.0);
                timesTest.run(Speed.milesPerHour(60), 1.0, 60.0);
                timesTest.run(Speed.feetPerSecond(7), -2.0, -14.0);
                timesTest.run(Speed.feetPerSecond(0), 111.0, 0.0);
                timesTest.run(Speed.feetPerSecond(0), 0.0, 0.0);
            });

            runner.testGroup("times(Duration)", () ->
            {
                final Action3<Speed, Duration,Throwable> timesErrorTest = (Speed lhs, Duration rhs, Throwable expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertThrows(() -> lhs.times(rhs), expected);
                    });
                };

                timesErrorTest.run(Speed.zero, null, new PreConditionFailure("rhs cannot be null."));

                final Action3<Speed, Duration,Double> timesTest = (Speed lhs, Duration rhs, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        final Distance product = lhs.times(rhs);
                        test.assertNotNull(product);
                        test.assertEqual(expectedValue, product.getValue());
                        test.assertEqual(lhs.getDistanceUnits(), product.getUnits());
                    });
                };

                timesTest.run(Speed.milesPerHour(60), Duration.zero, 0.0);
                timesTest.run(Speed.metersPerSecond(60), Duration.seconds(1), 60.0);
                timesTest.run(Speed.milesPerHour(60), Duration.minutes(120), 120.0);
                timesTest.run(Speed.feetPerSecond(7), Duration.milliseconds(3), 0.021);
            });

            runner.testGroup("dividedBy(double)", () ->
            {
                final Action3<Speed,Double,Throwable> dividedByErrorTest = (Speed lhs, Double rhs, Throwable expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertThrows(() -> lhs.dividedBy(rhs), expected);
                    });
                };

                dividedByErrorTest.run(Speed.milesPerHour(1), 0.0,
                        new PreConditionFailure("rhs (0.0) must not be 0.0."));

                final Action3<Speed,Double,Double> dividedByTest = (Speed lhs, Double rhs, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        final Speed result = lhs.dividedBy(rhs);
                        test.assertNotNull(result);
                        test.assertEqual(expectedValue, result.getValue());
                        test.assertEqual(lhs.getDistanceUnits(), result.getDistanceUnits());
                        test.assertEqual(lhs.getDurationUnits(), result.getDurationUnits());
                    });
                };

                dividedByTest.run(Speed.milesPerHour(10), 1.0, 10.0);
                dividedByTest.run(Speed.milesPerHour(10), 2.0, 5.0);
                dividedByTest.run(Speed.milesPerHour(10), -5.0, -2.0);
            });

            runner.testGroup("dividedBy(Speed)", () ->
            {
                final Action3<Speed,Speed,Throwable> dividedByErrorTest = (Speed lhs, Speed rhs, Throwable expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertThrows(() -> lhs.dividedBy(rhs), expected);
                    });
                };

                dividedByErrorTest.run(Speed.milesPerHour(1), null,
                        new PreConditionFailure("rhs cannot be null."));
                dividedByErrorTest.run(Speed.milesPerHour(1), Speed.zero,
                        new PreConditionFailure("rhs.getValue() (0.0) must not be 0.0."));

                final Action3<Speed,Speed,Double> dividedByTest = (Speed lhs, Speed rhs, Double expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.dividedBy(rhs));
                    });
                };

                dividedByTest.run(Speed.milesPerHour(10), Speed.milesPerHour(10), 1.0);
                dividedByTest.run(Speed.milesPerHour(10), Speed.feetPerSecond(1), 14.666666666666668);
                dividedByTest.run(Speed.milesPerHour(10), Speed.metersPerSecond(-7), -0.6386285714285714);
                dividedByTest.run(Speed.zero, Speed.kilometersPerHour(10), 0.0);
            });

            runner.testGroup("round()", () ->
            {
                final Action2<Speed,Double> roundTest = (Speed speed, Double expectedValue) ->
                {
                    runner.test("with " + speed, (Test test) ->
                    {
                        final Speed roundedSpeed = speed.round();
                        test.assertNotNull(roundedSpeed);
                        test.assertEqual(speed.getDistanceUnits(), roundedSpeed.getDistanceUnits());
                        test.assertEqual(speed.getDurationUnits(), roundedSpeed.getDurationUnits());
                        test.assertEqual(expectedValue, roundedSpeed.getValue());
                    });
                };

                roundTest.run(Speed.zero, 0.0);
                roundTest.run(Speed.milesPerHour(1), 1.0);
                roundTest.run(Speed.kilometersPerHour(0.9), 1.0);
                roundTest.run(Speed.kilometersPerHour(0.5), 1.0);
                roundTest.run(Speed.kilometersPerHour(0.49), 0.0);
                roundTest.run(Speed.kilometersPerHour(0.1), 0.0);
            });

            runner.testGroup("round(Speed)", () ->
            {
                final Action3<Speed,Speed,Throwable> roundErrorTest = (Speed speed, Speed scale, Throwable expected) ->
                {
                    runner.test("with " + English.andList(speed, scale), (Test test) ->
                    {
                        test.assertThrows(() -> speed.round(scale), expected);
                    });
                };

                roundErrorTest.run(Speed.milesPerHour(30), null, new PreConditionFailure("scale cannot be null."));
                roundErrorTest.run(Speed.milesPerHour(30), Speed.zero, new PreConditionFailure("scale.getValue() (0.0) must not be 0.0."));

                final Action3<Speed,Speed,Double> roundTest = (Speed speed, Speed scale, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(speed, scale), (Test test) ->
                    {
                        final Speed roundedSpeed = speed.round(scale);
                        test.assertNotNull(roundedSpeed);
                        test.assertEqual(scale.getDistanceUnits(), roundedSpeed.getDistanceUnits());
                        test.assertEqual(scale.getDurationUnits(), roundedSpeed.getDurationUnits());
                        test.assertEqual(expectedValue, roundedSpeed.getValue());
                    });
                };

                roundTest.run(Speed.zero, Speed.feetPerSecond(30), 0.0);
                roundTest.run(Speed.feetPerSecond(6000), Speed.milesPerHour(0.1), 4090.9);
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Speed,Object,Boolean> equalsTest = (Speed lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(Speed.milesPerHour(30), null, false);
                equalsTest.run(Speed.milesPerHour(30), "hello", false);
                equalsTest.run(Speed.milesPerHour(30), Speed.milesPerHour(30), true);
                equalsTest.run(Speed.metersPerSecond(37), Speed.create(37, DistanceUnit.Millimeters, DurationUnit.Milliseconds), true);
                equalsTest.run(Speed.kilometersPerHour(5), Speed.feetPerSecond(5), false);
                equalsTest.run(Speed.milesPerHour(10), Speed.milesPerHour(9), false);
            });

            runner.testGroup("equals(Speed)", () ->
            {
                final Action3<Speed,Speed,Boolean> equalsTest = (Speed lhs, Speed rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(Speed.milesPerHour(30), null, false);
                equalsTest.run(Speed.milesPerHour(30), Speed.milesPerHour(30), true);
                equalsTest.run(Speed.metersPerSecond(37), Speed.create(37, DistanceUnit.Millimeters, DurationUnit.Milliseconds), true);
                equalsTest.run(Speed.kilometersPerHour(5), Speed.feetPerSecond(5), false);
                equalsTest.run(Speed.milesPerHour(10), Speed.milesPerHour(9), false);
            });

            runner.test("hashCode()", (Test test) ->
            {
                final Speed s1 = Speed.metersPerSecond(1);
                test.assertEqual(s1.hashCode(), s1.hashCode());

                final Speed s2 = Speed.create(1000, DistanceUnit.Millimeters, DurationUnit.Seconds);
                test.assertEqual(s1.hashCode(), s2.hashCode());
            });

            runner.testGroup("compareTo(Speed)", () ->
            {
                final Action3<Speed,Speed,Comparison> compareToTest = (Speed lhs, Speed rhs, Comparison expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.compareWith(rhs));
                    });
                };

                compareToTest.run(Speed.zero, null, Comparison.GreaterThan);
                compareToTest.run(Speed.milesPerHour(100), Speed.milesPerHour(50), Comparison.GreaterThan);
                compareToTest.run(Speed.milesPerHour(50), Speed.milesPerHour(100), Comparison.LessThan);
                compareToTest.run(Speed.milesPerHour(30), Speed.milesPerHour(30), Comparison.Equal);
                compareToTest.run(Speed.milesPerHour(10), Speed.kilometersPerHour(10), Comparison.GreaterThan);
            });
        });
    }
}
