package qub;

/**
 * A measurement between two points.
 */
public class Distance extends MeasurableValueBase<DistanceUnit, Distance>
{
    private static final double MilesToFeet = 5280;
    private static final double FeetToInches = 12;
    private static final double InchesToCentimeters = 2.54;
    private static final double InchesToFontPoints = 72;

    private static final double KilometersToInches = MetricScale.kiloToCenti / InchesToCentimeters;
    private static final double KilometersToFeet = KilometersToInches / FeetToInches;
    private static final double KilometersToMiles = KilometersToFeet / MilesToFeet;
    private static final double KilometersToFontPoints = KilometersToInches * InchesToFontPoints;

    private static final double MetersToInches = MetricScale.uniToCenti / InchesToCentimeters;
    private static final double MetersToFeet = MetersToInches / FeetToInches;
    private static final double MetersToMiles = MetersToFeet / MilesToFeet;
    private static final double MetersToFontPoints = MetersToInches * InchesToFontPoints;

    private static final double CentimeterstoInches = 1.0 / InchesToCentimeters;
    private static final double CentimetersToFeet = CentimeterstoInches / FeetToInches;
    private static final double CentimetersToMiles = CentimetersToFeet / MilesToFeet;
    private static final double CentimetersToFontPoints = CentimeterstoInches * InchesToFontPoints;

    private static final double MillimetersToInches = MetricScale.milliToCenti / InchesToCentimeters;
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

    public static Distance create(double value, DistanceUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        return new Distance(value, units);
    }

    private Distance(double value, DistanceUnit units)
    {
        super(value, units, Distance::create);
    }

    @Override
    protected double getConversionMultiplier(DistanceUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        double result;

        switch (this.getUnits())
        {
            case Millimeters:
                switch (units)
                {
                    case Millimeters:
                        result = 1;
                        break;

                    case Centimeters:
                        result = MetricScale.milliToCenti;
                        break;

                    case Meters:
                        result = MetricScale.milliToUni;
                        break;

                    case Kilometers:
                        result = MetricScale.milliToKilo;
                        break;

                    case Inches:
                        result = Distance.MillimetersToInches;
                        break;

                    case Feet:
                        result = Distance.MillimetersToFeet;
                        break;

                    case Miles:
                        result = Distance.MillimetersToMiles;
                        break;

                    case FontPoints:
                        result = Distance.MillimeterstoFontPoints;
                        break;

                    default:
                        throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(DistanceUnit.class) + ": " + units);
                }
                break;

            case Centimeters:
                switch (units)
                {
                    case Millimeters:
                        result = MetricScale.centiToMilli;
                        break;

                    case Centimeters:
                        result = 1;
                        break;

                    case Meters:
                        result = MetricScale.centiToUni;
                        break;

                    case Kilometers:
                        result = MetricScale.centiToKilo;
                        break;

                    case Inches:
                        result = Distance.CentimeterstoInches;
                        break;

                    case Feet:
                        result = Distance.CentimetersToFeet;
                        break;

                    case Miles:
                        result = Distance.CentimetersToMiles;
                        break;

                    case FontPoints:
                        result = Distance.CentimetersToFontPoints;
                        break;

                    default:
                        throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(DistanceUnit.class) + ": " + units);
                }
                break;

            case Meters:
                switch (units)
                {
                    case Millimeters:
                        result = MetricScale.uniToMilli;
                        break;

                    case Centimeters:
                        result = MetricScale.uniToCenti;
                        break;

                    case Meters:
                        result = 1;
                        break;

                    case Kilometers:
                        result = MetricScale.uniToKilo;
                        break;

                    case Inches:
                        result = Distance.MetersToInches;
                        break;

                    case Feet:
                        result = Distance.MetersToFeet;
                        break;

                    case Miles:
                        result = Distance.MetersToMiles;
                        break;

                    case FontPoints:
                        result = Distance.MetersToFontPoints;
                        break;

                    default:
                        throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(DistanceUnit.class) + ": " + units);
                }
                break;

            case Kilometers:
                switch (units)
                {
                    case Millimeters:
                        result = MetricScale.kiloToMilli;
                        break;

                    case Centimeters:
                        result = MetricScale.kiloToCenti;
                        break;

                    case Meters:
                        result = MetricScale.kiloToUni;
                        break;

                    case Kilometers:
                        result = 1;
                        break;

                    case Inches:
                        result = Distance.KilometersToInches;
                        break;

                    case Feet:
                        result = Distance.KilometersToFeet;
                        break;

                    case Miles:
                        result = Distance.KilometersToMiles;
                        break;

                    case FontPoints:
                        result = Distance.KilometersToFontPoints;
                        break;

                    default:
                        throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(DistanceUnit.class) + ": " + units);
                }
                break;

            case Inches:
                switch (units)
                {
                    case Millimeters:
                        result = Distance.InchesToMillimeters;
                        break;

                    case Centimeters:
                        result = Distance.InchesToCentimeters;
                        break;

                    case Meters:
                        result = Distance.InchesToMeters;
                        break;

                    case Kilometers:
                        result = Distance.InchesToKilometers;
                        break;

                    case Inches:
                        result = 1;
                        break;

                    case Feet:
                        result = Distance.InchesToFeet;
                        break;

                    case Miles:
                        result = Distance.InchesToMiles;
                        break;

                    case FontPoints:
                        result = Distance.InchesToFontPoints;
                        break;

                    default:
                        throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(DistanceUnit.class) + ": " + units);
                }
                break;

            case Feet:
                switch (units)
                {
                    case Millimeters:
                        result = Distance.FeetToMillimeters;
                        break;

                    case Centimeters:
                        result = Distance.FeetToCentimeters;
                        break;

                    case Meters:
                        result = Distance.FeetToMeters;
                        break;

                    case Kilometers:
                        result = Distance.FeetToKilometers;
                        break;

                    case Inches:
                        result = Distance.FeetToInches;
                        break;

                    case Feet:
                        result = 1;
                        break;

                    case Miles:
                        result = Distance.FeetToMiles;
                        break;

                    case FontPoints:
                        result = Distance.FeetToFontPoints;
                        break;

                    default:
                        throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(DistanceUnit.class) + ": " + units);
                }
                break;

            case Miles:
                switch (units)
                {
                    case Millimeters:
                        result = Distance.MilesToMillimeters;
                        break;

                    case Centimeters:
                        result = Distance.MilesToCentimeters;
                        break;

                    case Meters:
                        result = Distance.MilesToMeters;
                        break;

                    case Kilometers:
                        result = Distance.MilesToKilometers;
                        break;

                    case Inches:
                        result = Distance.MilesToInches;
                        break;

                    case Feet:
                        result = Distance.MilesToFeet;
                        break;

                    case Miles:
                        result = 1;
                        break;

                    case FontPoints:
                        result = Distance.MilesToFontPoints;
                        break;

                    default:
                        throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(DistanceUnit.class) + ": " + units);
                }
                break;

            case FontPoints:
                switch (units)
                {
                    case Millimeters:
                        result = Distance.FontPointsToMillimeters;
                        break;

                    case Centimeters:
                        result = Distance.FontPointsToCentimeters;
                        break;

                    case Meters:
                        result = Distance.FontPointsToMeters;
                        break;

                    case Kilometers:
                        result = Distance.FontPointsToKilometers;
                        break;

                    case Inches:
                        result = Distance.FontPointsToInches;
                        break;

                    case Feet:
                        result = Distance.FontPointsToFeet;
                        break;

                    case Miles:
                        result = Distance.FontPointsToMiles;
                        break;

                    case FontPoints:
                        result = 1;
                        break;

                    default:
                        throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(DistanceUnit.class) + ": " + units);
                }
                break;

            default:
                throw new java.lang.IllegalArgumentException("Unrecognized " + Types.getTypeName(DistanceUnit.class) + ": " + units);
        }

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Distance toMillimeters()
    {
        return this.convertTo(DistanceUnit.Millimeters);
    }

    public Distance toCentimeters()
    {
        return this.convertTo(DistanceUnit.Centimeters);
    }

    public Distance toMeters()
    {
        return this.convertTo(DistanceUnit.Meters);
    }

    public Distance toKilometers()
    {
        return this.convertTo(DistanceUnit.Kilometers);
    }

    public Distance toInches()
    {
        return this.convertTo(DistanceUnit.Inches);
    }

    public Distance toFeet()
    {
        return this.convertTo(DistanceUnit.Feet);
    }

    public Distance toMiles()
    {
        return this.convertTo(DistanceUnit.Miles);
    }

    public Distance toFontPoints()
    {
        return this.convertTo(DistanceUnit.FontPoints);
    }

    @Override
    public int hashCode()
    {
        return Doubles.hashCode(this.toMeters().getValue());
    }

    @Override
    public Comparison compareWith(MeasurableValue<DistanceUnit> value)
    {
        return this.compareTo(value, Distance.zero);
    }
}
