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

            runner.testGroup("inches(double)", () ->
            {
                final Action1<Double> inchesTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        assertDistance(test, Distance.inches(value), value, DistanceUnit.Inches);
                    });
                };

                inchesTest.run(-4.0);
                inchesTest.run(0.0);
                inchesTest.run(17.0);
            });

            runner.testGroup("feet(double)", () ->
            {
                final Action1<Double> feetTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        assertDistance(test, Distance.feet(value), value, DistanceUnit.Feet);
                    });
                };

                feetTest.run(-4.0);
                feetTest.run(0.0);
                feetTest.run(17.0);
            });

            runner.testGroup("miles(double)", () ->
            {
                final Action1<Double> milesTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        assertDistance(test, Distance.miles(value), value, DistanceUnit.Miles);
                    });
                };

                milesTest.run(-4.0);
                milesTest.run(0.0);
                milesTest.run(17.0);
            });

            runner.testGroup("fontPoints(double)", () ->
            {
                final Action1<Double> fontPointsTest = (Double value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        assertDistance(test, Distance.fontPoints(value), value, DistanceUnit.FontPoints);
                    });
                };

                fontPointsTest.run(-4.0);
                fontPointsTest.run(0.0);
                fontPointsTest.run(17.0);
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
                convertToTest.run(Distance.millimeters(7), DistanceUnit.Inches, 0.2755905511811024);
                convertToTest.run(Distance.millimeters(12345), DistanceUnit.Feet, 40.50196850393701);
                convertToTest.run(Distance.millimeters(967), DistanceUnit.Miles, 0.0006008659428935019);
                convertToTest.run(Distance.millimeters(967), DistanceUnit.FontPoints, 2741.102362204724);

                convertToTest.run(Distance.centimeters(373), DistanceUnit.Millimeters, 3730.0);
                convertToTest.run(Distance.centimeters(5), DistanceUnit.Centimeters, 5.0);
                convertToTest.run(Distance.centimeters(1500), DistanceUnit.Meters, 15.0);
                convertToTest.run(Distance.centimeters(3), DistanceUnit.Kilometers, 0.000030000000000000004);
                convertToTest.run(Distance.centimeters(7), DistanceUnit.Inches, 2.755905511811023);
                convertToTest.run(Distance.centimeters(12345), DistanceUnit.Feet, 405.0196850393701);
                convertToTest.run(Distance.centimeters(967), DistanceUnit.Miles, 0.006008659428935019);
                convertToTest.run(Distance.centimeters(967), DistanceUnit.FontPoints, 27411.023622047243);

                convertToTest.run(Distance.meters(373), DistanceUnit.Millimeters, 373000.0);
                convertToTest.run(Distance.meters(5), DistanceUnit.Centimeters, 500.0);
                convertToTest.run(Distance.meters(1500), DistanceUnit.Meters, 1500.0);
                convertToTest.run(Distance.meters(3), DistanceUnit.Kilometers, 0.003);
                convertToTest.run(Distance.meters(7), DistanceUnit.Inches, 275.59055118110234);
                convertToTest.run(Distance.meters(12345), DistanceUnit.Feet, 40501.96850393701);
                convertToTest.run(Distance.meters(967), DistanceUnit.Miles, 0.600865942893502);
                convertToTest.run(Distance.meters(1600), DistanceUnit.Miles, 0.9941939075797345);
                convertToTest.run(Distance.meters(34), DistanceUnit.FontPoints, 96377.95275590553);

                convertToTest.run(Distance.kilometers(373), DistanceUnit.Millimeters, 373000000.0);
                convertToTest.run(Distance.kilometers(5), DistanceUnit.Centimeters, 500000.0);
                convertToTest.run(Distance.kilometers(1500), DistanceUnit.Meters, 1500000.0);
                convertToTest.run(Distance.kilometers(3), DistanceUnit.Kilometers, 3.0);
                convertToTest.run(Distance.kilometers(7), DistanceUnit.Inches, 275590.55118110235);
                convertToTest.run(Distance.kilometers(12345), DistanceUnit.Feet, 40501968.503937006);
                convertToTest.run(Distance.kilometers(967), DistanceUnit.Miles, 600.8659428935019);
                convertToTest.run(Distance.kilometers(1), DistanceUnit.Miles, 0.621371192237334);
                convertToTest.run(Distance.kilometers(5), DistanceUnit.Miles, 3.1068559611866697);
                convertToTest.run(Distance.kilometers(5), DistanceUnit.FontPoints, 14173228.346456692);

                convertToTest.run(Distance.inches(373), DistanceUnit.Millimeters, 9474.199999999999);
                convertToTest.run(Distance.inches(5), DistanceUnit.Centimeters, 12.7);
                convertToTest.run(Distance.inches(1500), DistanceUnit.Meters, 38.1);
                convertToTest.run(Distance.inches(3), DistanceUnit.Kilometers, 0.00007620000000000001);
                convertToTest.run(Distance.inches(7), DistanceUnit.Inches, 7.0);
                convertToTest.run(Distance.inches(12345), DistanceUnit.Feet, 1028.75);
                convertToTest.run(Distance.inches(967), DistanceUnit.Miles, 0.015261994949494949);
                convertToTest.run(Distance.inches(10), DistanceUnit.FontPoints, 720.0);

                convertToTest.run(Distance.feet(373), DistanceUnit.Millimeters, 113690.40000000001);
                convertToTest.run(Distance.feet(5), DistanceUnit.Centimeters, 152.4);
                convertToTest.run(Distance.feet(1500), DistanceUnit.Meters, 457.20000000000005);
                convertToTest.run(Distance.feet(3), DistanceUnit.Kilometers, 0.0009144000000000001);
                convertToTest.run(Distance.feet(7), DistanceUnit.Inches, 84.0);
                convertToTest.run(Distance.feet(12345), DistanceUnit.Feet, 12345.0);
                convertToTest.run(Distance.feet(967), DistanceUnit.Miles, 0.1831439393939394);
                convertToTest.run(Distance.feet(12), DistanceUnit.FontPoints, 10368.0);

                convertToTest.run(Distance.miles(373), DistanceUnit.Millimeters, 600285312.0);
                convertToTest.run(Distance.miles(5), DistanceUnit.Centimeters, 804672.0000000001);
                convertToTest.run(Distance.miles(1500), DistanceUnit.Meters, 2414015.9999999995);
                convertToTest.run(Distance.miles(1), DistanceUnit.Meters, 1609.3439999999998);
                convertToTest.run(Distance.miles(3), DistanceUnit.Kilometers, 4.828032);
                convertToTest.run(Distance.miles(3.1), DistanceUnit.Kilometers, 4.988966400000001);
                convertToTest.run(Distance.miles(7), DistanceUnit.Inches, 443520.0);
                convertToTest.run(Distance.miles(12345), DistanceUnit.Feet, 65181600.0);
                convertToTest.run(Distance.miles(967), DistanceUnit.Miles, 967.0);
                convertToTest.run(Distance.miles(9), DistanceUnit.FontPoints, 41057280.0);

                convertToTest.run(Distance.fontPoints(373), DistanceUnit.Millimeters, 131.5861111111111);
                convertToTest.run(Distance.fontPoints(5), DistanceUnit.Centimeters, 0.17638888888888887);
                convertToTest.run(Distance.fontPoints(1500), DistanceUnit.Meters, 0.5291666666666667);
                convertToTest.run(Distance.fontPoints(3), DistanceUnit.Kilometers, 0.0000010583333333333333);
                convertToTest.run(Distance.fontPoints(7), DistanceUnit.Inches, 0.09722222222222221);
                convertToTest.run(Distance.fontPoints(12345), DistanceUnit.Feet, 14.288194444444443);
                convertToTest.run(Distance.fontPoints(967), DistanceUnit.Miles, 0.00021197215207631874);
                convertToTest.run(Distance.fontPoints(12), DistanceUnit.FontPoints, 12.0);
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

                runner.test("from Inches", (Test test) ->
                {
                    assertDistance(test, Distance.inches(3).toMillimeters(), 76.19999999999999, DistanceUnit.Millimeters);
                });

                runner.test("from Feet", (Test test) ->
                {
                    assertDistance(test, Distance.feet(3).toMillimeters(), 914.4000000000001, DistanceUnit.Millimeters);
                });

                runner.test("from Miles", (Test test) ->
                {
                    assertDistance(test, Distance.miles(3).toMillimeters(), 4828032.0, DistanceUnit.Millimeters);
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

                runner.test("from Inches", (Test test) ->
                {
                    assertDistance(test, Distance.inches(3).toCentimeters(), 7.62, DistanceUnit.Centimeters);
                });

                runner.test("from Feet", (Test test) ->
                {
                    assertDistance(test, Distance.feet(3).toCentimeters(), 91.44, DistanceUnit.Centimeters);
                });

                runner.test("from Miles", (Test test) ->
                {
                    assertDistance(test, Distance.miles(3).toCentimeters(), 482803.20000000007, DistanceUnit.Centimeters);
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

                runner.test("from Inches", (Test test) ->
                {
                    assertDistance(test, Distance.inches(3).toMeters(), 0.07619999999999999, DistanceUnit.Meters);
                });

                runner.test("from Feet", (Test test) ->
                {
                    assertDistance(test, Distance.feet(3).toMeters(), 0.9144000000000001, DistanceUnit.Meters);
                });

                runner.test("from Miles", (Test test) ->
                {
                    assertDistance(test, Distance.miles(3).toMeters(), 4828.031999999999, DistanceUnit.Meters);
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

                runner.test("from Inches", (Test test) ->
                {
                    assertDistance(test, Distance.inches(3).toKilometers(), 0.00007620000000000001, DistanceUnit.Kilometers);
                });

                runner.test("from Feet", (Test test) ->
                {
                    assertDistance(test, Distance.feet(3).toKilometers(), 0.0009144000000000001, DistanceUnit.Kilometers);
                });

                runner.test("from Miles", (Test test) ->
                {
                    assertDistance(test, Distance.miles(3).toKilometers(), 4.828032, DistanceUnit.Kilometers);
                });
            });

            runner.testGroup("toInches()", () ->
            {
                runner.test("from Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).toInches(), 0.11811023622047244, DistanceUnit.Inches);
                });

                runner.test("from Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(3).toInches(), 1.1811023622047243, DistanceUnit.Inches);
                });

                runner.test("from Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(3).toInches(), 118.11023622047244, DistanceUnit.Inches);
                });

                runner.test("from Kilometers", (Test test) ->
                {
                    assertDistance(test, Distance.kilometers(3).toInches(), 118110.23622047243, DistanceUnit.Inches);
                });

                runner.test("from Inches", (Test test) ->
                {
                    assertDistance(test, Distance.inches(3).toInches(), 3, DistanceUnit.Inches);
                });

                runner.test("from Feet", (Test test) ->
                {
                    assertDistance(test, Distance.feet(3).toInches(), 36.0, DistanceUnit.Inches);
                });

                runner.test("from Miles", (Test test) ->
                {
                    assertDistance(test, Distance.miles(3).toInches(), 190080.0, DistanceUnit.Inches);
                });
            });

            runner.testGroup("toFeet()", () ->
            {
                runner.test("from Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).toFeet(), 0.00984251968503937, DistanceUnit.Feet);
                });

                runner.test("from Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(3).toFeet(), 0.09842519685039369, DistanceUnit.Feet);
                });

                runner.test("from Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(3).toFeet(), 9.84251968503937, DistanceUnit.Feet);
                });

                runner.test("from Kilometers", (Test test) ->
                {
                    assertDistance(test, Distance.kilometers(3).toFeet(), 9842.51968503937, DistanceUnit.Feet);
                });

                runner.test("from Inches", (Test test) ->
                {
                    assertDistance(test, Distance.inches(3).toFeet(), 0.25, DistanceUnit.Feet);
                });

                runner.test("from Feet", (Test test) ->
                {
                    assertDistance(test, Distance.feet(3).toFeet(), 3, DistanceUnit.Feet);
                });

                runner.test("from Miles", (Test test) ->
                {
                    assertDistance(test, Distance.miles(3).toFeet(), 15840.0, DistanceUnit.Feet);
                });
            });

            runner.testGroup("toMiles()", () ->
            {
                runner.test("from Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).toMiles(), 0.000001864113576712002, DistanceUnit.Miles);
                });

                runner.test("from Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(3).toMiles(), 0.00001864113576712002, DistanceUnit.Miles);
                });

                runner.test("from Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(3).toMiles(), 0.001864113576712002, DistanceUnit.Miles);
                });

                runner.test("from Kilometers", (Test test) ->
                {
                    assertDistance(test, Distance.kilometers(3).toMiles(), 1.8641135767120018, DistanceUnit.Miles);
                });

                runner.test("from Inches", (Test test) ->
                {
                    assertDistance(test, Distance.inches(3).toMiles(), 0.00004734848484848485, DistanceUnit.Miles);
                });

                runner.test("from Feet", (Test test) ->
                {
                    assertDistance(test, Distance.feet(3).toMiles(), 0.0005681818181818182, DistanceUnit.Miles);
                });

                runner.test("from Miles", (Test test) ->
                {
                    assertDistance(test, Distance.miles(3).toMiles(), 3, DistanceUnit.Miles);
                });
            });

            runner.testGroup("toFontPoints()", () ->
            {
                runner.test("from Millimeters", (Test test) ->
                {
                    assertDistance(test, Distance.millimeters(3).toFontPoints(), 8.503937007874015, DistanceUnit.FontPoints);
                });

                runner.test("from Centimeters", (Test test) ->
                {
                    assertDistance(test, Distance.centimeters(3).toFontPoints(), 85.03937007874015, DistanceUnit.FontPoints);
                });

                runner.test("from Meters", (Test test) ->
                {
                    assertDistance(test, Distance.meters(3).toFontPoints(), 8503.937007874018, DistanceUnit.FontPoints);
                });

                runner.test("from Kilometers", (Test test) ->
                {
                    assertDistance(test, Distance.kilometers(3).toFontPoints(), 8503937.007874016, DistanceUnit.FontPoints);
                });

                runner.test("from Inches", (Test test) ->
                {
                    assertDistance(test, Distance.inches(3).toFontPoints(), 216.0, DistanceUnit.FontPoints);
                });

                runner.test("from Feet", (Test test) ->
                {
                    assertDistance(test, Distance.feet(3).toFontPoints(), 2592.0, DistanceUnit.FontPoints);
                });

                runner.test("from Miles", (Test test) ->
                {
                    assertDistance(test, Distance.miles(3).toFontPoints(), 13685760.0, DistanceUnit.FontPoints);
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
