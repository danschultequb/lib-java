package qub;

/**
 * A measurement between two points.
 */
public class Length implements Comparable<Length>
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

    public static final Length zero = Length.inches(0);

    public static Length miles(double value)
    {
        return new Length(value, DistanceUnit.Miles);
    }

    public static Length feet(double value)
    {
        return new Length(value, DistanceUnit.Feet);
    }

    public static Length inches(double value)
    {
        return new Length(value, DistanceUnit.Inches);
    }

    public static Length fontPoints(double value)
    {
        return new Length(value, DistanceUnit.FontPoints);
    }

    public static Length millimeters(double value)
    {
        return new Length(value, DistanceUnit.Millimeters);
    }

    public static Length centimeters(double value)
    {
        return new Length(value, DistanceUnit.Centimeters);
    }

    public static Length meters(double value)
    {
        return new Length(value, DistanceUnit.Meters);
    }

    public static Length kilometers(double value)
    {
        return new Length(value, DistanceUnit.Kilometers);
    }

    private final double value;
    private final DistanceUnit units;

    public Length(double value, DistanceUnit units)
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

    public Length convertTo(DistanceUnit destinationUnits)
    {
        PreCondition.assertNotNull(destinationUnits, "destinationUnits");

        Length result = this;

        switch (units)
        {
            case Millimeters:
                switch (destinationUnits)
                {
                    case Centimeters:
                        result = new Length(value * MillimetersToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Length(value * MillimetersToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Length(value * MillimetersToKilometers, destinationUnits);
                        break;

                    case Inches:
                        result = new Length(value * MillimetersToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Length(value * MillimetersToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Length(value * MillimetersToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Length(value * MillimeterstoFontPoints, destinationUnits);
                        break;
                }
                break;

            case Centimeters:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Length(value * CentimetersToMillimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Length(value * CentimetersToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Length(value * CentimetersToKilometers, destinationUnits);
                        break;

                    case Inches:
                        result = new Length(value * CentimeterstoInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Length(value * CentimetersToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Length(value * CentimetersToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Length(value * CentimetersToFontPoints, destinationUnits);
                        break;
                }
                break;

            case Meters:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Length(value * MetersToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Length(value * MetersToCentimeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Length(value * MetersToKilometers, destinationUnits);
                        break;

                    case Inches:
                        result = new Length(value * MetersToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Length(value * MetersToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Length(value * MetersToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Length(value * MetersToFontPoints, destinationUnits);
                        break;
                }
                break;

            case Kilometers:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Length(value * KilometersToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Length(value * KilometersToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Length(value * KilometersToMeters, destinationUnits);
                        break;

                    case Inches:
                        result = new Length(value * KilometersToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Length(value * KilometersToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Length(value * KilometersToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Length(value * KilometersToFontPoints, destinationUnits);
                        break;
                }
                break;

            case Inches:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Length(value * InchesToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Length(value * InchesToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Length(value * InchesToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Length(value * InchesToKilometers, destinationUnits);
                        break;

                    case Feet:
                        result = new Length(value * InchesToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Length(value * InchesToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Length(value * InchesToFontPoints, destinationUnits);
                        break;
                }
                break;

            case Feet:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Length(value * FeetToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Length(value * FeetToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Length(value * FeetToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Length(value * FeetToKilometers, destinationUnits);
                        break;

                    case Inches:
                        result = new Length(value * FeetToInches, destinationUnits);
                        break;

                    case Miles:
                        result = new Length(value * FeetToMiles, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Length(value * FeetToFontPoints, destinationUnits);
                        break;
                }
                break;

            case Miles:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Length(value * MilesToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Length(value * MilesToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Length(value * MilesToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Length(value * MilesToKilometers, destinationUnits);
                        break;

                    case Inches:
                        result = new Length(value * MilesToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Length(value * MilesToFeet, destinationUnits);
                        break;

                    case FontPoints:
                        result = new Length(value * MilesToFontPoints, destinationUnits);
                        break;
                }
                break;

            case FontPoints:
                switch (destinationUnits)
                {
                    case Millimeters:
                        result = new Length(value * FontPointsToMillimeters, destinationUnits);
                        break;

                    case Centimeters:
                        result = new Length(value * FontPointsToCentimeters, destinationUnits);
                        break;

                    case Meters:
                        result = new Length(value * FontPointsToMeters, destinationUnits);
                        break;

                    case Kilometers:
                        result = new Length(value * FontPointsToKilometers, destinationUnits);
                        break;

                    case Inches:
                        result = new Length(value * FontPointsToInches, destinationUnits);
                        break;

                    case Feet:
                        result = new Length(value * FontPointsToFeet, destinationUnits);
                        break;

                    case Miles:
                        result = new Length(value * FontPointsToMiles, destinationUnits);
                        break;
                }
                break;
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Length toMillimeters()
    {
        return convertTo(DistanceUnit.Millimeters);
    }

    public Length toCentimeters()
    {
        return convertTo(DistanceUnit.Centimeters);
    }

    public Length toMeters()
    {
        return convertTo(DistanceUnit.Meters);
    }

    public Length toKilometers()
    {
        return convertTo(DistanceUnit.Kilometers);
    }

    public Length toInches()
    {
        return convertTo(DistanceUnit.Inches);
    }

    public Length toFeet()
    {
        return convertTo(DistanceUnit.Feet);
    }

    public Length toMiles()
    {
        return convertTo(DistanceUnit.Miles);
    }

    public Length toFontPoints()
    {
        return convertTo(DistanceUnit.FontPoints);
    }

    public Length negate()
    {
        final Length result = (value == 0 ? this : new Length(-value, units));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public Length plus(Length rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final Length result = (rhs.getValue() == 0 ? this : new Length(value + rhs.convertTo(units).getValue(), units));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(units, result.getUnits(), "result.getUnits()");

        return result;
    }

    public Length times(double value)
    {
        final Length result = (value == 1 ? this : new Length(this.value * value, units));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(units, result.getUnits(), "result.getUnits()");

        return result;
    }

    public Length dividedBy(double rhs)
    {
        PreCondition.assertNotEqual(0.0, rhs, "rhs");

        final Length result = (rhs == 1 ? this : new Length(value / rhs, units));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public double dividedBy(Length rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");
        PreCondition.assertNotEqual(0, rhs.getValue(), "rhs.getValue()");

        final Length convertedRhs = rhs.convertTo(units);
        final double result = value / convertedRhs.value;

        return result;
    }

    public Length round()
    {
        final double roundedValue = Math.round(value);
        return roundedValue == value ? this : new Length(roundedValue, units);
    }

    public Length round(Length scale)
    {
        PreCondition.assertNotNull(scale, "scale");
        PreCondition.assertNotEqual(0, scale.getValue(), "scale.getValue()");

        final Length convertedLhs = this.convertTo(scale.units);
        final double roundedValue = Math.round(convertedLhs.value, scale.value);
        final Length result = convertedLhs.value == roundedValue ? convertedLhs : new Length(roundedValue, scale.units);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(scale.getUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    public Length round(double scale)
    {
        PreCondition.assertNotEqual(0, scale, "scale");

        final double roundedValue = Math.round(value, scale);
        final Length result = value == roundedValue ? this : new Length(roundedValue, units);

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

        return new java.text.DecimalFormat(format).format(value) + " " + units;
    }

    @Override
    public boolean equals(Object value)
    {
        return value instanceof Length && equals((Length)value);
    }

    public boolean equals(Length rhs)
    {
        return rhs != null && rhs.convertTo(units).value == value;
    }

    @Override
    public int hashCode()
    {
        return units != DistanceUnit.Meters ? toMeters().hashCode() : Doubles.hashCode(value);
    }

    @Override
    public Comparison compareTo(Length value)
    {
        return value == null ? Comparison.GreaterThan : Comparison.from(getValue() - value.convertTo(getUnits()).getValue());
    }
}
