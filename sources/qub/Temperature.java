package qub;

/**
 * A measurement of heat.
 */
public class Temperature implements Comparable<Temperature>
{
    private static double fahrenheitToCelsius(double fahrenheit)
    {
        return (fahrenheit - 32) * 5.0 / 9.0;
    }

    private static double fahrenheitToKelvin(double fahrenheit)
    {
        return Temperature.celsiusToKelvin(Temperature.fahrenheitToCelsius(fahrenheit));
    }

    private static double celsiusToFahrenheit(double celsius)
    {
        return (celsius * 9.0 / 5.0) + 32;
    }

    private static double celsiusToKelvin(double celsius)
    {
        return celsius + 273.15;
    }

    private static double kelvinToCelsius(double kelvins)
    {
        return kelvins - 273.15;
    }

    private static double kelvinToFahrenheit(double kelvins)
    {
        return Temperature.celsiusToFahrenheit(Temperature.kelvinToCelsius(kelvins));
    }

    public static Temperature fahrenheit(double value)
    {
        return new Temperature(value, TemperatureUnit.Fahrenheit);
    }

    public static Temperature celsius(double value)
    {
        return new Temperature(value, TemperatureUnit.Celsius);
    }

    public static Temperature kelvin(double value)
    {
        return new Temperature(value, TemperatureUnit.Kelvin);
    }

    private final double value;
    private final TemperatureUnit units;

    private Temperature(double value, TemperatureUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        this.value = value;
        this.units = units;
    }

    public double getValue()
    {
        return value;
    }

    public TemperatureUnit getUnits()
    {
        return this.units;
    }

    public Temperature convertTo(TemperatureUnit destinationUnits)
    {
        PreCondition.assertNotNull(destinationUnits, "destinationUnits");

        Temperature result = this;

        switch (this.units)
        {
            case Fahrenheit:
                switch (destinationUnits)
                {
                    case Celsius:
                        result = new Temperature(Temperature.fahrenheitToCelsius(this.value), destinationUnits);
                        break;

                    case Kelvin:
                        result = new Temperature(Temperature.fahrenheitToKelvin(this.value), destinationUnits);
                        break;
                }
                break;

            case Celsius:
                switch (destinationUnits)
                {
                    case Fahrenheit:
                        result = new Temperature(Temperature.celsiusToFahrenheit(this.value), destinationUnits);
                        break;

                    case Kelvin:
                        result = new Temperature(Temperature.celsiusToKelvin(this.value), destinationUnits);
                        break;
                }
                break;

            case Kelvin:
                switch (destinationUnits)
                {
                    case Fahrenheit:
                        result = new Temperature(Temperature.kelvinToFahrenheit(this.value), destinationUnits);
                        break;

                    case Celsius:
                        result = new Temperature(Temperature.kelvinToCelsius(this.value), destinationUnits);
                        break;
                }
                break;
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(destinationUnits, result.getUnits(), "result.getUnits()");

        return result;
    }

    public Temperature toFahrenheit()
    {
        return this.convertTo(TemperatureUnit.Fahrenheit);
    }

    public Temperature toCelsius()
    {
        return this.convertTo(TemperatureUnit.Celsius);
    }

    public Temperature toKelvin()
    {
        return this.convertTo(TemperatureUnit.Kelvin);
    }

    public Temperature round()
    {
        final double roundedValue = Math.round(this.value);
        final Temperature result = roundedValue == this.value ? this : new Temperature(roundedValue, this.units);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    public Temperature round(double scale)
    {
        PreCondition.assertNotEqual(0, scale, "scale");

        final double roundedValue = Math.round(this.value, scale);
        final Temperature result = this.value == roundedValue ? this : new Temperature(roundedValue, this.units);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    @Override
    public Comparison compareTo(Temperature value)
    {
        Comparison result;
        if (value == null)
        {
            result = Comparison.GreaterThan;
        }
        else
        {
            result = Comparison.from(this.toCelsius().getValue() - value.toCelsius().getValue());
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public String toString()
    {
        return this.value + " " + this.units;
    }

    public String toString(String format)
    {
        PreCondition.assertNotNull(format, "format");

        return new java.text.DecimalFormat(format).format(this.value) + " " + this.units;
    }

    @Override
    public boolean equals(Object value)
    {
        return value instanceof Temperature && this.equals((Temperature)value);
    }

    public boolean equals(Temperature rhs)
    {
        return rhs != null && rhs.convertTo(this.units).value == this.value;
    }

    @Override
    public int hashCode()
    {
        return this.units != TemperatureUnit.Celsius ? this.toCelsius().hashCode() : Doubles.hashCode(this.value);
    }
}
