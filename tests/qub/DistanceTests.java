package qub;

public class DistanceTests
{
    private static void assertDistance(Test test, Distance distance, double expectedValue, DistanceUnit expectedUnits)
    {
        test.assertNotNull(distance);
        test.assertEqual(expectedValue, distance.getValue());
        test.assertEqual(expectedUnits, distance.getUnits());
    }

    public static void test(TestRunner runner)
    {
        runner.testGroup(Distance.class, () ->
        {
            runner.testGroup("millimeters(double)", () ->
            {
                final Action1<Double> millimetersTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        assertDistance(test, Distance.millimeters(value), value, DistanceUnit.Millimeters);
                    });
                };

                millimetersTest.run(-4.0);
                millimetersTest.run(0.0);
                millimetersTest.run(17.0);
            });

            runner.testGroup("centimeters(double)", () ->
            {
                final Action1<Double> centimetersTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        assertDistance(test, Distance.centimeters(value), value, DistanceUnit.Centimeters);
                    });
                };

                centimetersTest.run(-4.0);
                centimetersTest.run(0.0);
                centimetersTest.run(17.0);
            });

            runner.testGroup("meters(double)", () ->
            {
                final Action1<Double> metersTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        assertDistance(test, Distance.meters(value), value, DistanceUnit.Meters);
                    });
                };

                metersTest.run(-4.0);
                metersTest.run(0.0);
                metersTest.run(17.0);
            });

            runner.testGroup("kilometers(double)", () ->
            {
                final Action1<Double> kilometersTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        assertDistance(test, Distance.kilometers(value), value, DistanceUnit.Kilometers);
                    });
                };

                kilometersTest.run(-4.0);
                kilometersTest.run(0.0);
                kilometersTest.run(17.0);
            });

            runner.testGroup("convertTo(DistanceUnit)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Distance value = Distance.meters(3);
                    test.assertThrows(() -> value.convertTo(null));
                });

                final Action3<Distance,DistanceUnit,Double> convertToTest = (Distance value, DistanceUnit expectedUnits, Double expectedValue) ->
                {
                    runner.test("with " + value + " to " + expectedUnits, (Test test) ->
                    {
                        assertDistance(test, value.convertTo(expectedUnits), expectedValue, expectedUnits);
                    });
                };

                convertToTest.run(Distance.millimeters(373), DistanceUnit.Millimeters, 373.0);
                convertToTest.run(Distance.millimeters(5), DistanceUnit.Centimeters, 0.5);
                convertToTest.run(Distance.millimeters(1500), DistanceUnit.Meters, 1.5);
                convertToTest.run(Distance.millimeters(3), DistanceUnit.Kilometers, 0.000003);

                convertToTest.run(Distance.centimeters(373), DistanceUnit.Millimeters, 3730.0);
                convertToTest.run(Distance.centimeters(5), DistanceUnit.Centimeters, 5.0);
                convertToTest.run(Distance.centimeters(1500), DistanceUnit.Meters, 15.0);
                convertToTest.run(Distance.centimeters(3), DistanceUnit.Kilometers, 0.000030000000000000004);

                convertToTest.run(Distance.meters(373), DistanceUnit.Millimeters, 373000.0);
                convertToTest.run(Distance.meters(5), DistanceUnit.Centimeters, 500.0);
                convertToTest.run(Distance.meters(1500), DistanceUnit.Meters, 1500.0);
                convertToTest.run(Distance.meters(3), DistanceUnit.Kilometers, 0.003);

                convertToTest.run(Distance.kilometers(373), DistanceUnit.Millimeters, 373000000.0);
                convertToTest.run(Distance.kilometers(5), DistanceUnit.Centimeters, 500000.0);
                convertToTest.run(Distance.kilometers(1500), DistanceUnit.Meters, 1500000.0);
                convertToTest.run(Distance.kilometers(3), DistanceUnit.Kilometers, 3.0);
            });

            runner.testGroup("toMillimeters()", () ->
            {
                runner.test("from Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).toMillimeters(), 3, DistanceUnit.Millimeters);
                });

                runner.test("from Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(3).toMillimeters(), 30, DistanceUnit.Millimeters);
                });

                runner.test("from Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(3).toMillimeters(), 3000, DistanceUnit.Millimeters);
                });

                runner.test("from Kilometers", (Test test) ->
                {
                    assertDistance(test, Distance.kilometers(3).toMillimeters(), 3000000, DistanceUnit.Millimeters);
                });
            });

            runner.testGroup("toCentimeters()", () ->
            {
                runner.test("from Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).toCentimeters(), 0.30000000000000004, DistanceUnit.Centimeters);
                });

                runner.test("from Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(3).toCentimeters(), 3, DistanceUnit.Centimeters);
                });

                runner.test("from Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(3).toCentimeters(), 300, DistanceUnit.Centimeters);
                });

                runner.test("from Kilometers", (Test test) ->
                {
                    assertDistance(test, Distance.kilometers(3).toCentimeters(), 300000, DistanceUnit.Centimeters);
                });
            });

            runner.testGroup("toMeters()", () ->
            {
                runner.test("from Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).toMeters(), 0.003, DistanceUnit.Meters);
                });

                runner.test("from Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(3).toMeters(), 0.03, DistanceUnit.Meters);
                });

                runner.test("from Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(3).toMeters(), 3, DistanceUnit.Meters);
                });

                runner.test("from Kilometers", (Test test) ->
                {
                    assertDistance(test, Distance.kilometers(3).toMeters(), 3000, DistanceUnit.Meters);
                });
            });

            runner.testGroup("toKilometers()", () ->
            {
                runner.test("from Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).toKilometers(), 0.000003, DistanceUnit.Kilometers);
                });

                runner.test("from Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(3).toKilometers(), 0.000030000000000000004, DistanceUnit.Kilometers);
                });

                runner.test("from Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(3).toKilometers(), 0.003, DistanceUnit.Kilometers);
                });

                runner.test("from Kilometers", (Test test) ->
                {
                    assertDistance(test, Distance.kilometers(3).toKilometers(), 3, DistanceUnit.Kilometers);
                });
            });

            runner.testGroup("negate()", () ->
            {
                runner.test("with -1 Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(-1).negate(), 1, DistanceUnit.Meters);
                });

                runner.test("with 0 Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(0).negate(), 0, DistanceUnit.Millimeters);
                });

                runner.test("with 10 Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(10).negate(), -10, DistanceUnit.Centimeters);
                });
            });

            runner.testGroup("plus(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Distance.meters(5).plus(null));
                });

                runner.test("with negative", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).plus(Distance.centimeters(-5)), -47, DistanceUnit.Millimeters);
                });

                runner.test("with zero", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).plus(Distance.centimeters(0)), 3, DistanceUnit.Millimeters);
                });

                runner.test("with positive", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).plus(Distance.centimeters(2)), 23, DistanceUnit.Millimeters);
                });
            });

            runner.testGroup("times(double)", () ->
            {
                runner.test("with -5", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).times(-5), -15, DistanceUnit.Millimeters);
                });

                runner.test("with -1", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).times(-1), -3, DistanceUnit.Millimeters);
                });

                runner.test("with 0", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).times(0), 0, DistanceUnit.Millimeters);
                });

                runner.test("with 1", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).times(1), 3, DistanceUnit.Millimeters);
                });

                runner.test("with 12", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).times(4), 12, DistanceUnit.Millimeters);
                });
            });

            runner.testGroup("dividedBy(double)", () ->
            {
                runner.test("with -5", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).dividedBy(-5), -0.6, DistanceUnit.Millimeters);
                });

                runner.test("with -1", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).dividedBy(-1), -3, DistanceUnit.Millimeters);
                });

                runner.test("with 0", (Test test) ->
                {
                    test.assertThrows(() -> Distance.millimeters(3).dividedBy(0));
                });

                runner.test("with 1", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).dividedBy(1), 3, DistanceUnit.Millimeters);
                });

                runner.test("with 4", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).dividedBy(4), 0.75, DistanceUnit.Millimeters);
                });
            });

            runner.testGroup("dividedBy(Distance)", () ->
            {
                runner.test("with -5", (Test test) ->
                {
                    test.assertEqual(-0.6, Distance.millimeters(3).dividedBy(Distance.millimeters(-5)));
                });

                runner.test("with -1", (Test test) ->
                {
                    test.assertEqual(-3, Distance.millimeters(3).dividedBy(Distance.millimeters(-1)));
                });

                runner.test("with 0", (Test test) ->
                {
                    test.assertThrows(() -> Distance.millimeters(3).dividedBy(Distance.meters(0)));
                });

                runner.test("with 1", (Test test) ->
                {
                    test.assertEqual(3, Distance.millimeters(3).dividedBy(Distance.millimeters(1)));
                });

                runner.test("with 4", (Test test) ->
                {
                    test.assertEqual(0.75, Distance.millimeters(3).dividedBy(Distance.millimeters(4)));
                });
            });

            runner.testGroup("round()", () ->
            {
                runner.test("with 0 Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(0).round(), 0, DistanceUnit.Millimeters);
                });

                runner.test("with 1.4999 Kilometers", (Test test) ->
                {
                    assertDistance(test, Distance.kilometers(1.4999).round(), 1, DistanceUnit.Kilometers);
                });

                runner.test("with 1.5 Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(1.5).round(), 2, DistanceUnit.Centimeters);
                });

                runner.test("with 2 Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(2).round(), 2, DistanceUnit.Meters);
                });
            });

            runner.testGroup("round(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Distance.meters(5).round(null));
                });

                runner.test("with 0 Millimeters and 0 Meter scale", (Test test) ->
                {
                    test.assertThrows(() -> Distance.millimeters(0).round(Distance.meters(0)));
                });

                runner.test("with 0 Millimeters and 1 Meter scale", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(0).round(Distance.meters(1)), 0, DistanceUnit.Meters);
                });

                runner.test("with 1 Centimeter and 3 Millimeter scale", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(1).round(Distance.millimeters(3)), 9, DistanceUnit.Millimeters);
                });
            });

            runner.testGroup("round(double)", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    test.assertThrows(() -> Distance.meters(5).round(0));
                });

                runner.test("with 0 Millimeters and 1 scale", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(0).round(1), 0, DistanceUnit.Millimeters);
                });

                runner.test("with 1 Centimeter and 3 scale", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(1).round(3), 0, DistanceUnit.Centimeters);
                });
            });

            runner.testGroup("toString(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Distance.meters(1.23).toString(null));
                });

                runner.test("with " + Strings.escapeAndQuote(""), (Test test) ->
                {
                    test.assertEqual("1.23 Meters", Distance.meters(1.23).toString(""));
                });

                runner.test("with " + Strings.escapeAndQuote("0"), (Test test) ->
                {
                    test.assertEqual("1 Meters", Distance.meters(1.23).toString("0"));
                });

                runner.test("with " + Strings.escapeAndQuote("00.0"), (Test test) ->
                {
                    test.assertEqual("01.2 Meters", Distance.meters(1.23).toString("00.0"));
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Distance.meters(1).equals((Object)null));
                });

                runner.test("with String", (Test test) ->
                {
                    test.assertFalse(Distance.meters(1).equals((Object)"Distance"));
                });

                runner.test("with same", (Test test) ->
                {
                    final Distance distance = Distance.meters(2);
                    test.assertTrue(distance.equals((Object)distance));
                });

                runner.test("with equal value and units", (Test test) ->
                {
                    test.assertTrue(Distance.meters(1).equals((Object)Distance.meters(1)));
                });

                runner.test("with equal converted value and units", (Test test) ->
                {
                    test.assertTrue(Distance.meters(1).equals((Object)Distance.centimeters(100)));
                });

                runner.test("with equal value but different units", (Test test) ->
                {
                    test.assertFalse(Distance.meters(1).equals((Object)Distance.kilometers(1)));
                });

                runner.test("with different value but equal units", (Test test) ->
                {
                    test.assertFalse(Distance.meters(1).equals((Object)Distance.meters(2)));
                });
            });

            runner.testGroup("equals(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Distance.meters(1).equals((Distance)null));
                });

                runner.test("with same", (Test test) ->
                {
                    final Distance distance = Distance.meters(2);
                    test.assertTrue(distance.equals(distance));
                });

                runner.test("with equal value and units", (Test test) ->
                {
                    test.assertTrue(Distance.meters(1).equals(Distance.meters(1)));
                });

                runner.test("with equal converted value and units", (Test test) ->
                {
                    test.assertTrue(Distance.meters(1).equals(Distance.centimeters(100)));
                });

                runner.test("with equal value but different units", (Test test) ->
                {
                    test.assertFalse(Distance.meters(1).equals(Distance.kilometers(1)));
                });

                runner.test("with different value but equal units", (Test test) ->
                {
                    test.assertFalse(Distance.meters(1).equals(Distance.meters(2)));
                });
            });

            runner.testGroup("hashCode()", () ->
            {
                runner.test("with same", (Test test) ->
                {
                    final Distance distance = Distance.millimeters(25);
                    test.assertEqual(distance.hashCode(), distance.hashCode());
                });

                runner.test("with equal", (Test test) ->
                {
                    test.assertEqual(Distance.centimeters(10).hashCode(), Distance.centimeters(10).hashCode());
                });

                runner.test("with converted equal", (Test test) ->
                {
                    test.assertEqual(Distance.meters(500).hashCode(), Distance.kilometers(0.5).hashCode());
                });

                runner.test("with different", (Test test) ->
                {
                    test.assertNotEqual(Distance.millimeters(23).hashCode(), Distance.centimeters(1).hashCode());
                });
            });

            runner.testGroup("compareTo(Distance)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual(Comparison.GreaterThan, Distance.millimeters(1).compareTo(null));
                });

                runner.test("with less than with equal units", (Test test) ->
                {
                    test.assertEqual(Comparison.LessThan, Distance.centimeters(1).compareTo(Distance.centimeters(2)));
                });

                runner.test("with less than with different units", (Test test) ->
                {
                    test.assertEqual(Comparison.LessThan, Distance.centimeters(10).compareTo(Distance.meters(1)));
                });
            });
        });
    }
}
