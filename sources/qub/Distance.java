package qub;

import java.text.DecimalFormat;

/**
 * A measurement between two points.
 */
public class Distance implements Comparable<Distance>
{
    private static final double KilometersToMeters = 1000;
    private static final double MetersToCentimeters = 100;
    private static final double CentimetersToMillimeters = 10;
    private static final double MilesToFeet = 5280;
    private static final double FeetToInches = 12;
    private static final double InchesToCentimeters = 2.54;
    private static final double InchesToFontPoints = 72;

    private static final double KilometersToCentimeters = KilometersToMeters * MetersToCentimeters;
    private static final double KilometersToMillimeters = KilometersToCentimeters * CentimetersToMillimeters;
    private static final double KilometersToInches = KilometersToCentimeters / InchesToCentimeters;
    private static final double KilometersToFeet = KilometersToInches / FeetToInches;
    private static final double KilometersToMiles = KilometersToFeet / MilesToFeet;
    private static final double KilometersToFontPoints = KilometersToInches * InchesToFontPoints;

    private static final double MetersToKilometers = 1.0 / KilometersToMeters;
    private static final double MetersToMillimeters = MetersToCentimeters * CentimetersToMillimeters;
    private static final double MetersToInches = MetersToCentimeters / InchesToCentimeters;
    private static final double MetersToFeet = MetersToInches / FeetToInches;
    private static final double MetersToMiles = MetersToFeet / MilesToFeet;
    private static final double MetersToFontPoints = MetersToInches * InchesToFontPoints;

    private static final double CentimetersToKilometers = 1.0 / KilometersToCentimeters;
    private static final double CentimetersToMeters = 1.0 / MetersToCentimeters;
    private static final double CentimeterstoInches = 1.0 / InchesToCentimeters;
    private static final double CentimetersToFeet = CentimeterstoInches / FeetToInches;
    private static final double CentimetersToMiles = CentimetersToFeet / MilesToFeet;
    private static final double CentimetersToFontPoints = CentimeterstoInches * InchesToFontPoints;

    private static final double MillimetersToKilometers = 1.0 / KilometersToMillimeters;
    private static final double MillimetersToMeters = 1.0 / MetersToMillimeters;
    private static final double MillimetersToCentimeters = 1.0 / CentimetersToMillimeters;
    private static final double MillimetersToInches = MillimetersToCentimeters / InchesToCentimeters;
    private static final double MillimetersToFeet = MillimetersToInches / FeetToInches;
    private static final double MillimetersToMiles = MillimetersToFeet / MilesToFeet;
    private static final double MillimeterstoFontPoints = MillimetersToInches * InchesToFontPoints;

    private static final double InchesToKilometers = 1.0 / KilometersToInches;
    private static final double InchesToMeters = 1.0 / MetersToInches;
    private static final double InchesToMillimeters = 1.0 / MillimetersToInches;
    private static final double InchesToFeet = 1.0 / FeetToInches;
    private static final double InchesToMiles = InchesToFeet / MilesToFeet;

    private static final double FeetToKilometers = 1.0 / KilometersToFeet;
    private static final double FeetToMeters = 1.0 / MetersToFeet;
    private static final double FeetToCentimeters = 1.0 / CentimetersToFeet;
    private static final double FeetToMillimeters = 1.0 / MillimetersToFeet;
    private static final double FeetToMiles = 1.0 / MilesToFeet;
    private static final double FeetToFontPoints = FeetToInches * InchesToFontPoints;

    private static final double MilesToKilometers = 1.0 / KilometersToMiles;
    private static final double MilesToMeters = 1.0 / MetersToMiles;
    private static final double MilesToCentimeters = 1.0 / CentimetersToMiles;
    private static final double MilesToMillimeters = 1.0 / MillimetersToMiles;
    private static final double MilesToInches = 1.0 / InchesToMiles;
    private static final double MilesToFontPoints = MilesToInches * InchesToFontPoints;

    private static final double FontPointsToKilometers = 1.0 / KilometersToFontPoints;
    private static final double FontPointsToMeters = 1.0 / MetersToFontPoints;
    private static final double FontPointsToCentimeters = 1.0 / CentimetersToFontPoints;
    private static final double FontPointsToMillimeters = 1.0 / MillimeterstoFontPoints;
    private static final double FontPointsToInches = 1.0 / InchesToFontPoints;
    private static final double FontPointsToFeet = 1.0 / FeetToFontPoints;
    private static final double FontPointsToMiles = 1.0 / MilesToFontPoints;

    public static final Distance zero = Distance.inches(0);

    public static Distance miles(double value)
    {
        return new Distance(value, DistanceUnit.Miles);
    }

    public static Distance feet(double value)
    {
        return new Distance(value, DistanceUnit.Feet);
    }

    public static Distance inches(double value)
    {
        return new Distance(value, DistanceUnit.Inches);
    }

    public static Distance fontPoints(double value)
    {
        return new Distance(value, DistanceUnit.FontPoints);
    }

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

                    case Inches:
                        result = new Distance(value * MillimetersToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Distance(value * MillimetersToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Distance(value * MillimetersToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Distance(value * MillimeterstoFontPoints, destinationUnits);
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

                    case Inches:
                        result = new Distance(value * CentimeterstoInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Distance(value * CentimetersToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Distance(value * CentimetersToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Distance(value * CentimetersToFontPoints, destinationUnits);
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

                    case Inches:
                        result = new Distance(value * MetersToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Distance(value * MetersToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Distance(value * MetersToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Distance(value * MetersToFontPoints, destinationUnits);
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

                    case Inches:
                        result = new Distance(value * KilometersToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Distance(value * KilometersToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Distance(value * KilometersToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Distance(value * KilometersToFontPoints, destinationUnits);
                        break;
                }
                break;

            case Inches:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Distance(value * InchesToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Distance(value * InchesToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Distance(value * InchesToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Distance(value * InchesToKilometers, destinationUnits);
                        break;

                    case Feet:
                        result = new Distance(value * InchesToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Distance(value * InchesToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Distance(value * InchesToFontPoints, destinationUnits);
                        break;
                }
                break;

            case Feet:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Distance(value * FeetToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Distance(value * FeetToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Distance(value * FeetToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Distance(value * FeetToKilometers, destinationUnits);
                        break;

                    case Inches:
                        result = new Distance(value * FeetToInches, destinationUnits);
                        break;

                    case Miles:
                        result = new Distance(value * FeetToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Distance(value * FeetToFontPoints, destinationUnits);
                        break;
                }
                break;

            case Miles:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Distance(value * MilesToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Distance(value * MilesToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Distance(value * MilesToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Distance(value * MilesToKilometers, destinationUnits);
                        break;

                    case Inches:
                        result = new Distance(value * MilesToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Distance(value * MilesToFeet, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Distance(value * MilesToFontPoints, destinationUnits);
                        break;
                }
                break;

            case FontPoints:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Distance(value * FontPointsToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Distance(value * FontPointsToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Distance(value * FontPointsToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Distance(value * FontPointsToKilometers, destinationUnits);
                        break;

                    case Inches:
                        result = new Distance(value * FontPointsToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Distance(value * FontPointsToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Distance(value * FontPointsToMiles, destinationUnits);
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

    public Distance toInches()
    {
        return convertTo(DistanceUnit.Inches);
    }

    public Distance toFeet()
    {
        return convertTo(DistanceUnit.Feet);
    }

    public Distance toMiles()
    {
        return convertTo(DistanceUnit.Miles);
    }

    public Distance toFontPoints()
    {
        return convertTo(DistanceUnit.FontPoints);
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
        PreCondition.assertNotNull(format, "format");

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
