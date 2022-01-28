package qub;

public class FuelEfficiency implements Comparable<FuelEfficiency>
{
    private final double value;
    private final DistanceUnit distanceUnits;
    private final VolumeUnit volumeUnits;

    private FuelEfficiency(double value, DistanceUnit distanceUnits, VolumeUnit volumeUnits)
    {
        PreCondition.assertNotNull(distanceUnits, "distanceUnits");
        PreCondition.assertNotNull(volumeUnits, "volumeUnits");

        this.value = value;
        this.distanceUnits = distanceUnits;
        this.volumeUnits = volumeUnits;
    }

    public static FuelEfficiency create(double value, DistanceUnit distanceUnits, VolumeUnit volumeUnits)
    {
        return new FuelEfficiency(value, distanceUnits, volumeUnits);
    }

    public static FuelEfficiency milesPerUSGallon(double value)
    {
        return FuelEfficiency.create(value, DistanceUnit.Miles, VolumeUnit.USGallons);
    }

    public static FuelEfficiency kilometersPerLiter(double value)
    {
        return FuelEfficiency.create(value, DistanceUnit.Kilometers, VolumeUnit.Liters);
    }

    public final static FuelEfficiency zero = FuelEfficiency.milesPerUSGallon(0);

    public double getValue()
    {
        return this.value;
    }

    public DistanceUnit getDistanceUnits()
    {
        return this.distanceUnits;
    }

    public VolumeUnit getVolumeUnits()
    {
        return this.volumeUnits;
    }

    public FuelEfficiency convertTo(DistanceUnit distanceUnits)
    {
        return this.convertTo(distanceUnits, this.volumeUnits);
    }

    public FuelEfficiency convertTo(VolumeUnit volumeUnits)
    {
        return this.convertTo(this.distanceUnits, volumeUnits);
    }

    public FuelEfficiency convertTo(DistanceUnit distanceUnits, VolumeUnit volumeUnits)
    {
        PreCondition.assertNotNull(distanceUnits, "distanceUnits");
        PreCondition.assertNotNull(volumeUnits, "volumeUnits");

        double value = this.value;

        if (this.distanceUnits != distanceUnits)
        {
            value = Distance.create(value, this.distanceUnits).convertTo(distanceUnits).getValue();
        }
        if (this.volumeUnits != volumeUnits)
        {
            value = new Volume(value, this.volumeUnits).convertTo(volumeUnits).getValue();
        }

        final FuelEfficiency result = this.value == value
            ? this
            : FuelEfficiency.create(value, distanceUnits, volumeUnits);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(distanceUnits, result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(volumeUnits, result.getVolumeUnits(), "result.getVolumeUnits()");

        return result;
    }

    public FuelEfficiency toMilesPerUSGallon()
    {
        return this.convertTo(DistanceUnit.Miles, VolumeUnit.USGallons);
    }

    public FuelEfficiency toKilometersPerLiter()
    {
        return this.convertTo(DistanceUnit.Kilometers, VolumeUnit.Liters);
    }

    public FuelEfficiency negate()
    {
        return this.value == 0
            ? this
            : FuelEfficiency.create(-this.value, this.distanceUnits, this.volumeUnits);
    }

    public FuelEfficiency plus(FuelEfficiency rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final FuelEfficiency result = rhs.value == 0
            ? this
            : FuelEfficiency.create(this.value + rhs.convertTo(this.distanceUnits, this.volumeUnits).value, this.distanceUnits, this.volumeUnits);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(this.getVolumeUnits(), result.getVolumeUnits(), "result.getVolumeUnits()");

        return result;
    }

    public FuelEfficiency minus(FuelEfficiency rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final FuelEfficiency result = rhs.value == 0
            ? this
            : FuelEfficiency.create(this.value - rhs.convertTo(this.distanceUnits, this.volumeUnits).value, this.distanceUnits, this.volumeUnits);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(this.getVolumeUnits(), result.getVolumeUnits(), "result.getVolumeUnits()");

        return result;
    }

    public FuelEfficiency times(double rhs)
    {
        FuelEfficiency result;
        if (rhs == 0)
        {
            result = FuelEfficiency.zero;
        }
        else if (rhs == 1)
        {
            result = this;
        }
        else
        {
            result = FuelEfficiency.create(this.value * rhs, this.distanceUnits, this.volumeUnits);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(this.getVolumeUnits(), result.getVolumeUnits(), "result.getVolumeUnits()");

        return result;
    }

    public Distance times(Volume rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final Distance result = Distance.create(this.value * rhs.convertTo(this.volumeUnits).getValue(), this.distanceUnits);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getUnits(), "result.getUnits()");

        return result;
    }

    public FuelEfficiency dividedBy(double rhs)
    {
        PreCondition.assertNotEqual(0, rhs, "rhs");

        FuelEfficiency result;
        if (rhs == 1)
        {
            result = this;
        }
        else
        {
            result = FuelEfficiency.create(this.value / rhs, this.distanceUnits, this.volumeUnits);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(this.getVolumeUnits(), result.getVolumeUnits(), "result.getVolumeUnits()");

        return result;
    }

    public double dividedByte(FuelEfficiency rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");
        PreCondition.assertNotEqual(0, rhs.getValue(), "rhs.getValue()");

        return this.value == 0
            ? 0
            : this.value / rhs.convertTo(this.distanceUnits, this.volumeUnits).value;
    }

    public FuelEfficiency round()
    {
        final double roundedValue = Math.round(value);
        return roundedValue == value ? this : FuelEfficiency.create(roundedValue, this.distanceUnits, this.volumeUnits);
    }

    public FuelEfficiency round(FuelEfficiency scale)
    {
        PreCondition.assertNotNull(scale, "scale");
        PreCondition.assertNotEqual(0, scale.getValue(), "scale.getValue()");

        final FuelEfficiency convertedLhs = this.convertTo(scale.getDistanceUnits(), scale.getVolumeUnits());
        final double roundedValue = Math.round(convertedLhs.value, scale.value);
        final FuelEfficiency result = convertedLhs.value == roundedValue ? convertedLhs : FuelEfficiency.create(roundedValue, scale.getDistanceUnits(), scale.getVolumeUnits());

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(scale.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(scale.getVolumeUnits(), result.getVolumeUnits(), "result.getVolumeUnits()");

        return result;
    }

    public FuelEfficiency round(double scale)
    {
        PreCondition.assertNotEqual(0, scale, "scale");

        final double roundedValue = Math.round(value, scale);
        final FuelEfficiency result = value == roundedValue ? this : FuelEfficiency.create(roundedValue, this.distanceUnits, this.volumeUnits);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.getDistanceUnits(), result.getDistanceUnits(), "result.getDistanceUnits()");
        PostCondition.assertEqual(this.getVolumeUnits(), result.getVolumeUnits(), "result.getVolumeUnits()");

        return result;
    }

    @Override
    public String toString()
    {
        return value + " " + this.distanceUnits + "/" + this.volumeUnits;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof FuelEfficiency && this.equals((FuelEfficiency)rhs);
    }

    public boolean equals(FuelEfficiency rhs)
    {
        return rhs != null && rhs.convertTo(this.distanceUnits, this.volumeUnits).value == this.value;
    }

    @Override
    public int hashCode()
    {
        return Doubles.hashCode(this.toKilometersPerLiter().value);
    }

    @Override
    public Comparison compareWith(FuelEfficiency rhs)
    {
        return rhs == null
            ? Comparison.GreaterThan
            : Comparison.create(this.value - rhs.convertTo(this.distanceUnits, this.volumeUnits).value);
    }
}
