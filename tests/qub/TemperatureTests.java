package qub;

public interface TemperatureTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Temperature.class, () ->
        {
            runner.test("fahrenheit(double)", (Test test) ->
            {
                final Temperature temperature = Temperature.fahrenheit(52);
                test.assertNotNull(temperature);
                test.assertEqual(52, temperature.getValue());
                test.assertEqual(TemperatureUnit.Fahrenheit, temperature.getUnits());
            });

            runner.test("celsius(double)", (Test test) ->
            {
                final Temperature temperature = Temperature.celsius(3);
                test.assertNotNull(temperature);
                test.assertEqual(3, temperature.getValue());
                test.assertEqual(TemperatureUnit.Celsius, temperature.getUnits());
            });

            runner.test("kelvin(double)", (Test test) ->
            {
                final Temperature temperature = Temperature.kelvin(1759);
                test.assertNotNull(temperature);
                test.assertEqual(1759, temperature.getValue());
                test.assertEqual(TemperatureUnit.Kelvin, temperature.getUnits());
            });

            runner.testGroup("convertTo(TemperatureUnit)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Temperature temperature = Temperature.celsius(100);
                    test.assertThrows(() -> temperature.convertTo(null),
                        new PreConditionFailure("destinationUnits cannot be null."));
                });

                final Action3<Temperature,TemperatureUnit,Double> convertToTest = (Temperature temperature, TemperatureUnit destinationUnits, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(temperature, destinationUnits), (Test test) ->
                    {
                        final Temperature convertedTemperature = temperature.convertTo(destinationUnits);
                        test.assertNotNull(convertedTemperature);
                        test.assertEqual(destinationUnits, convertedTemperature.getUnits());
                        test.assertEqual(expectedValue, convertedTemperature.getValue());
                    });
                };

                convertToTest.run(Temperature.fahrenheit(0), TemperatureUnit.Fahrenheit, 0.0);

                convertToTest.run(Temperature.fahrenheit(0), TemperatureUnit.Celsius, -17.77777777777778);
                convertToTest.run(Temperature.fahrenheit(32), TemperatureUnit.Celsius, 0.0);
                convertToTest.run(Temperature.fahrenheit(100), TemperatureUnit.Celsius, 37.77777777777778);
                convertToTest.run(Temperature.fahrenheit(212), TemperatureUnit.Celsius, 100.0);

                convertToTest.run(Temperature.fahrenheit(0), TemperatureUnit.Kelvin, 255.3722222222222);
                convertToTest.run(Temperature.fahrenheit(32), TemperatureUnit.Kelvin, 273.15);
                convertToTest.run(Temperature.fahrenheit(100), TemperatureUnit.Kelvin, 310.92777777777775);
                convertToTest.run(Temperature.fahrenheit(212), TemperatureUnit.Kelvin, 373.15);

                convertToTest.run(Temperature.celsius(0), TemperatureUnit.Celsius, 0.0);

                convertToTest.run(Temperature.celsius(0), TemperatureUnit.Fahrenheit, 32.0);
                convertToTest.run(Temperature.celsius(32), TemperatureUnit.Fahrenheit, 89.6);
                convertToTest.run(Temperature.celsius(100), TemperatureUnit.Fahrenheit, 212.0);
                convertToTest.run(Temperature.celsius(212), TemperatureUnit.Fahrenheit, 413.6);

                convertToTest.run(Temperature.celsius(0), TemperatureUnit.Kelvin, 273.15);
                convertToTest.run(Temperature.celsius(32), TemperatureUnit.Kelvin, 305.15);
                convertToTest.run(Temperature.celsius(100), TemperatureUnit.Kelvin, 373.15);
                convertToTest.run(Temperature.celsius(212), TemperatureUnit.Kelvin, 485.15);

                convertToTest.run(Temperature.kelvin(0), TemperatureUnit.Kelvin, 0.0);

                convertToTest.run(Temperature.kelvin(0), TemperatureUnit.Fahrenheit, -459.66999999999996);
                convertToTest.run(Temperature.kelvin(32), TemperatureUnit.Fahrenheit, -402.07);
                convertToTest.run(Temperature.kelvin(100), TemperatureUnit.Fahrenheit, -279.66999999999996);
                convertToTest.run(Temperature.kelvin(212), TemperatureUnit.Fahrenheit, -78.06999999999996);

                convertToTest.run(Temperature.kelvin(0), TemperatureUnit.Celsius, -273.15);
                convertToTest.run(Temperature.kelvin(32), TemperatureUnit.Celsius, -241.14999999999998);
                convertToTest.run(Temperature.kelvin(100), TemperatureUnit.Celsius, -173.14999999999998);
                convertToTest.run(Temperature.kelvin(212), TemperatureUnit.Celsius, -61.14999999999998);
            });

            runner.testGroup("toFahrenheit()", () ->
            {
                final Action2<Temperature,Double> toFahrenheitTest = (Temperature temperature, Double expectedValue) ->
                {
                    runner.test("with " + temperature, (Test test) ->
                    {
                        final Temperature fahrenheit = temperature.toFahrenheit();
                        test.assertNotNull(fahrenheit);
                        test.assertEqual(TemperatureUnit.Fahrenheit, fahrenheit.getUnits());
                        test.assertEqual(expectedValue, fahrenheit.getValue());
                    });
                };

                toFahrenheitTest.run(Temperature.fahrenheit(0), 0.0);
                toFahrenheitTest.run(Temperature.celsius(0), 32.0);
                toFahrenheitTest.run(Temperature.celsius(100), 212.0);
                toFahrenheitTest.run(Temperature.kelvin(30), -405.66999999999996);
            });

            runner.testGroup("toCelsius()", () ->
            {
                final Action2<Temperature,Double> toCelsiusTest = (Temperature temperature, Double expectedValue) ->
                {
                    runner.test("with " + temperature, (Test test) ->
                    {
                        final Temperature celsius = temperature.toCelsius();
                        test.assertNotNull(celsius);
                        test.assertEqual(TemperatureUnit.Celsius, celsius.getUnits());
                        test.assertEqual(expectedValue, celsius.getValue());
                    });
                };

                toCelsiusTest.run(Temperature.fahrenheit(0), -17.77777777777778);
                toCelsiusTest.run(Temperature.celsius(10), 10.0);
                toCelsiusTest.run(Temperature.kelvin(30), -243.14999999999998);
            });

            runner.testGroup("toKelvin()", () ->
            {
                final Action2<Temperature,Double> toKelvinTest = (Temperature temperature, Double expectedValue) ->
                {
                    runner.test("with " + temperature, (Test test) ->
                    {
                        final Temperature kelvin = temperature.toKelvin();
                        test.assertNotNull(kelvin);
                        test.assertEqual(TemperatureUnit.Kelvin, kelvin.getUnits());
                        test.assertEqual(expectedValue, kelvin.getValue());
                    });
                };

                toKelvinTest.run(Temperature.fahrenheit(0), 255.3722222222222);
                toKelvinTest.run(Temperature.celsius(10), 283.15);
                toKelvinTest.run(Temperature.kelvin(30), 30.0);
            });

            runner.testGroup("round()", () ->
            {
                final Action2<Temperature,Double> roundTest = (Temperature temperature, Double expectedValue) ->
                {
                    runner.test("with " + temperature, (Test test) ->
                    {
                        final Temperature rounded = temperature.round();
                        test.assertNotNull(rounded);
                        test.assertEqual(temperature.getUnits(), rounded.getUnits());
                        test.assertEqual(expectedValue, rounded.getValue());
                    });
                };

                roundTest.run(Temperature.fahrenheit(0), 0.0);
                roundTest.run(Temperature.fahrenheit(72.4), 72.0);
                roundTest.run(Temperature.celsius(10), 10.0);
                roundTest.run(Temperature.celsius(33.8), 34.0);
                roundTest.run(Temperature.kelvin(30), 30.0);
                roundTest.run(Temperature.kelvin(352.5993), 353.0);
            });

            runner.testGroup("round(double)", () ->
            {
                runner.test("with " + English.andList(Temperature.fahrenheit(50), 0.0), (Test test) ->
                {
                    test.assertThrows(() -> Temperature.fahrenheit(50).round(0),
                        new PreConditionFailure("scale (0.0) must not be 0.0."));
                });

                final Action3<Temperature,Double,Double> roundTest = (Temperature temperature, Double scale, Double expectedValue) ->
                {
                    runner.test("with " + English.andList(temperature, scale), (Test test) ->
                    {
                        final Temperature rounded = temperature.round(scale);
                        test.assertNotNull(rounded);
                        test.assertEqual(temperature.getUnits(), rounded.getUnits());
                        test.assertEqual(expectedValue, rounded.getValue());
                    });
                };

                roundTest.run(Temperature.fahrenheit(0), 1.0, 0.0);
                roundTest.run(Temperature.fahrenheit(72.4), 2.0, 72.0);
                roundTest.run(Temperature.celsius(10), 3.0, 9.0);
                roundTest.run(Temperature.celsius(33.8), 4.0, 32.0);
                roundTest.run(Temperature.kelvin(30), 5.0, 30.0);
                roundTest.run(Temperature.kelvin(352.5993), 6.0, 354.0);
            });

            runner.testGroup("compareTo(Temperature)", () ->
            {
                final Action3<Temperature,Temperature,Comparison> compareToTest = (Temperature lhs, Temperature rhs, Comparison expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.compareWith(rhs));
                    });
                };

                compareToTest.run(Temperature.fahrenheit(50), null, Comparison.GreaterThan);
                compareToTest.run(Temperature.fahrenheit(50), Temperature.fahrenheit(49), Comparison.GreaterThan);
                compareToTest.run(Temperature.fahrenheit(50), Temperature.fahrenheit(50), Comparison.Equal);
                compareToTest.run(Temperature.fahrenheit(50), Temperature.fahrenheit(51), Comparison.LessThan);
                compareToTest.run(Temperature.fahrenheit(50), Temperature.celsius(0), Comparison.GreaterThan);
                compareToTest.run(Temperature.fahrenheit(50), Temperature.celsius(25), Comparison.LessThan);
            });

            runner.test("toString()", (Test test) ->
            {
                test.assertEqual("12.3 Fahrenheit", Temperature.fahrenheit(12.3).toString());
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Temperature,Object,Boolean> equalsTest = (Temperature lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(Temperature.celsius(10), null, false);
                equalsTest.run(Temperature.celsius(10), Distance.zero, false);
                equalsTest.run(Temperature.celsius(100), Temperature.fahrenheit(99), false);
                equalsTest.run(Temperature.celsius(100), Temperature.fahrenheit(212), true);
            });

            runner.testGroup("equals(Temperature)", () ->
            {
                final Action3<Temperature,Temperature,Boolean> equalsTest = (Temperature lhs, Temperature rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(lhs, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(Temperature.celsius(10), null, false);
                equalsTest.run(Temperature.celsius(100), Temperature.fahrenheit(99), false);
                equalsTest.run(Temperature.celsius(100), Temperature.fahrenheit(212), true);
            });
        });
    }
}
