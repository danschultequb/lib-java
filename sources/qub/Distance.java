package qub;

import java.text.DecimalFormat;

/**
 * A measurement between two points.
 */
public class Distance extends ComparableBase<Distance>
{
    private static final double KilometersToMeters = 1000;
    private static final double MetersToCentimeters = 100;
    private static final double CentimetersToMillimeters = 10;

    private static final double KilometersToCentimeters = KilometersToMeters * MetersToCentimeters;
    private static final double KilometersToMillimeters = KilometersToCentimeters * CentimetersToMillimeters;

    private static final double MetersToKilometers = 1.0 / KilometersToMeters;
    private static final double MetersToMillimeters = MetersToCentimeters * CentimetersToMillimeters;

    private static final double CentimetersToKilometers = 1.0 / KilometersToCentimeters;
    private static final double CentimetersToMeters = 1.0 / MetersToCentimeters;

    private static final double MillimetersToKilometers = 1.0 / KilometersToMillimeters;
    private static final double MillimetersToMeters = 1.0 / MetersToMillimeters;
    private static final double MillimetersToCentimeters = 1.0 / CentimetersToMillimeters;

    public static Distance millimeters(double value)
    {
        return new Distance(value, DistanceUnit.Millimeters);
    }

    public static Distance centimeters(double value)
    {
        return new Distance(value, DistanceUnit.Centimeters);
    }

    public static Distance meters(double value)
    {
        return new Distance(value, DistanceUnit.Meters);
    }

    public static Distance kilometers(double value)
    {
        return new Distance(value, DistanceUnit.Kilometers);
    }

    private final double value;
    private final DistanceUnit units;

    public Distance(double value, DistanceUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        this.value = value;
        this.units = units;
    }

    public double getValue()
    {
        return value;
    }

    public DistanceUnit getUnits()
    {
        return units;
    }

    public Distance convertTo(DistanceUnit destinationUnits)
    {
        PreCondition.assertNotNull(destinationUnits, "destinationUnits");

        Distance result = this;

        switch (units)
        {
            case Millimeters:
                switch (destinationUnits)
                {
                    case Centimeters:
                        result = new Distance(value * MillimetersToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Distance(value * MillimetersToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Distance(value * MillimetersToKilometers, destinationUnits);
                        break;
                }
                break;

            case Centimeters:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Distance(value * CentimetersToMillimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Distance(value * CentimetersToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Distance(value * CentimetersToKilometers, destinationUnits);
                        break;
                }
                break;

            case Meters:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Distance(value * MetersToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Distance(value * MetersToCentimeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Distance(value * MetersToKilometers, destinationUnits);
                        break;
                }
                break;

            case Kilometers:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Distance(value * KilometersToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Distance(value * KilometersToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Distance(value * KilometersToMeters, destinationUnits);
                        break;
                }
                break;
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Distance toMillimeters()
    {
        return convertTo(DistanceUnit.Millimeters);
    }

    public Distance toCentimeters()
    {
        return convertTo(DistanceUnit.Centimeters);
    }

    public Distance toMeters()
    {
        return convertTo(DistanceUnit.Meters);
    }

    public Distance toKilometers()
    {
        return convertTo(DistanceUnit.Kilometers);
    }

    public Distance negate()
    {
        final Distance result = (value == 0 ? this : new Distance(-value, units));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Distance plus(Distance rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final Distance result = (rhs.getValue() == 0 ? this : new Distance(value + rhs.convertTo(units).getValue(), units));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(units, result.getUnits(), "result.getUnits()");

        return result;
    }

    public Distance times(double value)
    {
        final Distance result = (value == 1 ? this : new Distance(this.value * value, units));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(units, result.getUnits(), "result.getUnits()");

        return result;
    }

    public Distance dividedBy(double rhs)
    {
        PreCondition.assertNotEqual(0.0, rhs, "rhs");

        final Distance result = (rhs == 1 ? this : new Distance(value / rhs, units));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public double dividedBy(Distance rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");
        PreCondition.assertNotEqual(0, rhs.getValue(), "rhs.getValue()");

        final Distance convertedRhs = rhs.convertTo(units);
        final double result = value / convertedRhs.value;

        return result;
    }

    public Distance round()
    {
        final double roundedValue = Math.round(value);
        return roundedValue == value ? this : new Distance(roundedValue, units);
    }

    public Distance round(Distance scale)
    {
        PreCondition.assertNotNull(scale, "scale");
        PreCondition.assertNotEqual(0, scale.getValue(), "scale.getValue()");

        final Distance convertedLhs = this.convertTo(scale.units);
        final double roundedValue = Math.round(convertedLhs.value, scale.value);
        final Distance result = convertedLhs.value == roundedValue ? convertedLhs : new Distance(roundedValue, scale.units);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(scale.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    public Distance round(double scale)
    {
        PreCondition.assertNotEqual(0, scale, "scale");

        final double roundedValue = Math.round(value, scale);
        final Distance result = value == roundedValue ? this : new Distance(roundedValue, units);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    @Override
    public String toString()
    {
        return value + " " + units;
    }

    public String toString(String format)
    {
        return new DecimalFormat(format).format(value) + " " + units;
    }

    @Override
    public boolean equals(Object value)
    {
        return value instanceof Distance && equals((Distance)value);
    }

    public boolean equals(Distance rhs)
    {
        return rhs != null && rhs.convertTo(units).value == value;
    }

    @Override
    public int hashCode()
    {
        return units != DistanceUnit.Meters ? toMeters().hashCode() : Doubles.hashCode(value);
    }

    @Override
    public Comparison compareTo(Distance value)
    {
        return value == null ? Comparison.GreaterThan : Comparison.from(getValue() - value.convertTo(getUnits()).getValue());
    }
}
