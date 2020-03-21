package qub;

/**
 * A measure of data.
 */
public class DataSize // implements Comparable<DataSize>
{
    public final static double bytesToBits = 8;

    public final static double bitsToBytes = 1.0 / DataSize.bytesToBits;

    private final double value;
    private final DataSizeUnit units;

    public DataSize(double value, DataSizeUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        this.value = value;
        this.units = units;
    }

    public static final DataSize yobibytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Yobibytes);
    }

    public static final DataSize zebibytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Zebibytes);
    }

    public static final DataSize exbibytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Exbibytes);
    }

    public static final DataSize pebibytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Pebibytes);
    }

    public static final DataSize tebibytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Tebibytes);
    }

    public static final DataSize gibibytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Gibibytes);
    }

    public static final DataSize mebibytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Mebibytes);
    }

    public static final DataSize kibibytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Kibibytes);
    }

    public static final DataSize yobibits(double value)
    {
        return new DataSize(value, DataSizeUnit.Yobibits);
    }

    public static final DataSize zebibits(double value)
    {
        return new DataSize(value, DataSizeUnit.Zebibits);
    }

    public static final DataSize exbibits(double value)
    {
        return new DataSize(value, DataSizeUnit.Exbibits);
    }

    public static final DataSize pebibits(double value)
    {
        return new DataSize(value, DataSizeUnit.Pebibits);
    }

    public static final DataSize tebibits(double value)
    {
        return new DataSize(value, DataSizeUnit.Tebibits);
    }

    public static final DataSize gibibits(double value)
    {
        return new DataSize(value, DataSizeUnit.Gibibits);
    }

    public static final DataSize mebibits(double value)
    {
        return new DataSize(value, DataSizeUnit.Mebibits);
    }

    public static final DataSize kibibits(double value)
    {
        return new DataSize(value, DataSizeUnit.Kibibits);
    }

    public static final DataSize yottabytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Yottabytes);
    }

    public static final DataSize zettabytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Zettabytes);
    }

    public static final DataSize exabytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Exabytes);
    }

    public static final DataSize petabytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Petabytes);
    }

    public static final DataSize terabytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Terabytes);
    }

    public static final DataSize gigabytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Gigabytes);
    }

    public static final DataSize megabytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Megabytes);
    }

    public static final DataSize kilobytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Kilobytes);
    }

    public static final DataSize yottabits(double value)
    {
        return new DataSize(value, DataSizeUnit.Yottabits);
    }

    public static final DataSize zettabits(double value)
    {
        return new DataSize(value, DataSizeUnit.Zettabits);
    }

    public static final DataSize exabits(double value)
    {
        return new DataSize(value, DataSizeUnit.Exabits);
    }

    public static final DataSize petabits(double value)
    {
        return new DataSize(value, DataSizeUnit.Petabits);
    }

    public static final DataSize terabits(double value)
    {
        return new DataSize(value, DataSizeUnit.Terabits);
    }

    public static final DataSize gigabits(double value)
    {
        return new DataSize(value, DataSizeUnit.Gigabits);
    }

    public static final DataSize megabits(double value)
    {
        return new DataSize(value, DataSizeUnit.Megabits);
    }

    public static final DataSize kilobits(double value)
    {
        return new DataSize(value, DataSizeUnit.Kilobits);
    }

    public static final DataSize bytes(double value)
    {
        return new DataSize(value, DataSizeUnit.Bytes);
    }

    public static final DataSize bits(double value)
    {
        return new DataSize(value, DataSizeUnit.Bits);
    }

    /**
     * The standard empty data size quantity.
     */
    public static final DataSize zero = DataSize.bytes(0);

    public double getValue()
    {
        return this.value;
    }

    public DataSizeUnit getUnits()
    {
        return this.units;
    }

    public DataSize convertTo(DataSizeUnit destinationUnits)
    {
        PreCondition.assertNotNull(destinationUnits, "destinationUnits");

        DataSize result = this;

        switch (this.getUnits())
        {
            case Yobibytes:
                switch (destinationUnits)
                {
                    case Zebibytes:
                        result = new DataSize(this.value * BinaryScale.yobiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * BinaryScale.yobiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * BinaryScale.yobiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * BinaryScale.yobiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * BinaryScale.yobiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * BinaryScale.yobiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * BinaryScale.yobiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.yobiToZebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.yobiToExbi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.yobiToPebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.yobiToTebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.yobiToGibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.yobiToMebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.yobiToKibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.yobiToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToYotta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToZetta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToExa * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToPeta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToTera * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToGiga * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToMega * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * MetricScale.uniToKilo * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.yobiToUni * DataSize.bytesToBits, destinationUnits);
                        break;
                }
                break;

            case Zebibytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * BinaryScale.zebiToYobi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * BinaryScale.zebiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * BinaryScale.zebiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * BinaryScale.zebiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * BinaryScale.zebiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * BinaryScale.zebiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * BinaryScale.zebiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.zebiToYobi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.zebiToExbi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.zebiToPebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.zebiToTebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.zebiToGibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.zebiToMebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.zebiToKibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.zebiToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToYotta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToZetta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToExa * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToPeta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToTera * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToGiga * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToMega * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * MetricScale.uniToKilo * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.zebiToUni * DataSize.bytesToBits, destinationUnits);
                        break;
                }
                break;

            case Exbibytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * BinaryScale.exbiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * BinaryScale.exbiToZebi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * BinaryScale.exbiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * BinaryScale.exbiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * BinaryScale.exbiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * BinaryScale.exbiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * BinaryScale.exbiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.exbiToYobi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.exbiToZebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.exbiToPebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.exbiToTebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.exbiToGibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.exbiToMebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.exbiToKibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToYotta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToZetta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToExa * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToPeta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToTera * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToGiga * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToMega * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToKilo * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bytesToBits, destinationUnits);
                        break;
                }
                break;

            case Pebibytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * BinaryScale.pebiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * BinaryScale.pebiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * BinaryScale.pebiToExbi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * BinaryScale.pebiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * BinaryScale.pebiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * BinaryScale.pebiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * BinaryScale.pebiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.pebiToYobi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.pebiToZebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.pebiToExbi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.pebiToTebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.pebiToGibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.pebiToMebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.pebiToKibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToYotta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToZetta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToExa * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToPeta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToTera * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToGiga * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToMega * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToKilo * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bytesToBits, destinationUnits);
                        break;
                }
                break;

            case Tebibytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * BinaryScale.tebiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * BinaryScale.tebiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * BinaryScale.tebiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * BinaryScale.tebiToPebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * BinaryScale.tebiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * BinaryScale.tebiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * BinaryScale.tebiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.tebiToYobi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.tebiToZebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.tebiToExbi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.tebiToPebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.tebiToGibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.tebiToMebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.tebiToKibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToYotta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToZetta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToExa * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToPeta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToTera * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToGiga * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToMega * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToKilo * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bytesToBits, destinationUnits);
                        break;
                }
                break;

            case Gibibytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * BinaryScale.gibiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * BinaryScale.gibiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * BinaryScale.gibiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * BinaryScale.gibiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * BinaryScale.gibiToTebi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * BinaryScale.gibiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * BinaryScale.gibiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.gibiToYobi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.gibiToZebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.gibiToExbi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.gibiToPebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.gibiToTebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.gibiToMebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.gibiToKibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToYotta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToZetta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToExa * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToPeta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToTera * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToGiga * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToMega * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToKilo * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bytesToBits, destinationUnits);
                        break;
                }
                break;

            case Mebibytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * BinaryScale.mebiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * BinaryScale.mebiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * BinaryScale.mebiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * BinaryScale.mebiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * BinaryScale.mebiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * BinaryScale.mebiToGibi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * BinaryScale.mebiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.mebiToYobi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.mebiToZebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.mebiToExbi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.mebiToPebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.mebiToTebi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.mebiToGibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.mebiToKibi * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToYotta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToZetta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToExa * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToPeta * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToTera * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToGiga * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToMega * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToKilo * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bytesToBits, destinationUnits);
                        break;
                }
                break;

            case Kibibytes:
                break;

            case Yobibits:
                break;

            case Zebibits:
                break;

            case Exbibits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.exbiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.exbiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.exbiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.exbiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.exbiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.exbiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.exbiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.exbiToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.exbiToZebi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.exbiToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.exbiToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.exbiToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.exbiToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.exbiToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bitsToBytes * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bitsToBytes * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bitsToBytes * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bitsToBytes * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bitsToBytes * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bitsToBytes * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bitsToBytes * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bitsToBytes * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.exbiToUni, destinationUnits);
                        break;
                }
                break;

            case Pebibits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.pebiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.pebiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.pebiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.pebiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.pebiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.pebiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.pebiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.pebiToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.pebiToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.pebiToExbi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.pebiToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.pebiToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.pebiToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.pebiToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bitsToBytes * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bitsToBytes * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bitsToBytes * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bitsToBytes * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bitsToBytes * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bitsToBytes * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bitsToBytes * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bitsToBytes * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.pebiToUni, destinationUnits);
                        break;
                }
                break;

            case Tebibits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.tebiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.tebiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.tebiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.tebiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.tebiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.tebiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.tebiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.tebiToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.tebiToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.tebiToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.tebiToPebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.tebiToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.tebiToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.tebiToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bitsToBytes * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bitsToBytes * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bitsToBytes * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bitsToBytes * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bitsToBytes * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bitsToBytes * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bitsToBytes * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bitsToBytes * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.tebiToUni, destinationUnits);
                        break;
                }
                break;

            case Gibibits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.gibiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.gibiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.gibiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.gibiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.gibiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.gibiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.gibiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.gibiToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.gibiToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.gibiToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.gibiToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.gibiToTebi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.gibiToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.gibiToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bitsToBytes * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bitsToBytes * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bitsToBytes * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bitsToBytes * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bitsToBytes * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bitsToBytes * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bitsToBytes * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bitsToBytes * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.gibiToUni, destinationUnits);
                        break;
                }
                break;

            case Mebibits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.mebiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.mebiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.mebiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.mebiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.mebiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.mebiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.mebiToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.mebiToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.mebiToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.mebiToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.mebiToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.mebiToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.mebiToGibi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.mebiToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bitsToBytes * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bitsToBytes * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bitsToBytes * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bitsToBytes * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bitsToBytes * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bitsToBytes * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bitsToBytes * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bitsToBytes * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.mebiToUni, destinationUnits);
                        break;
                }
                break;

            case Kibibits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.kibiToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.kibiToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.kibiToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.kibiToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.kibiToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.kibiToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.kibiToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.kibiToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.kibiToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.kibiToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.kibiToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.kibiToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.kibiToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.kibiToMebi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * DataSize.bitsToBytes * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * DataSize.bitsToBytes * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * DataSize.bitsToBytes * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * DataSize.bitsToBytes * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * DataSize.bitsToBytes * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * DataSize.bitsToBytes * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * DataSize.bitsToBytes * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * DataSize.bitsToBytes * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * BinaryScale.kibiToUni * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * BinaryScale.kibiToUni, destinationUnits);
                        break;
                }
                break;

            case Yottabytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bytesToBits * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bytesToBits * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bytesToBits * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bytesToBits * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bytesToBits * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bytesToBits * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bytesToBits * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bytesToBits * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * MetricScale.yottaToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * MetricScale.yottaToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * MetricScale.yottaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * MetricScale.yottaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * MetricScale.yottaToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * MetricScale.yottaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * MetricScale.yottaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.yottaToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.yottaToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.yottaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.yottaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.yottaToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.yottaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.yottaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.yottaToUni, destinationUnits);
                        break;
                }
                break;

            case Zettabytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bytesToBits * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bytesToBits * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bytesToBits * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bytesToBits * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bytesToBits * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bytesToBits * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bytesToBits * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bytesToBits * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * MetricScale.zettaToYotta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * MetricScale.zettaToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * MetricScale.zettaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * MetricScale.zettaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * MetricScale.zettaToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * MetricScale.zettaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * MetricScale.zettaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.zettaToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.zettaToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.zettaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.zettaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.zettaToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.zettaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.zettaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.zettaToUni, destinationUnits);
                        break;
                }
                break;

            case Exabytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bytesToBits * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bytesToBits * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bytesToBits * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bytesToBits * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bytesToBits * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bytesToBits * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bytesToBits * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bytesToBits * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * MetricScale.exaToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * MetricScale.exaToZetta, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * MetricScale.exaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * MetricScale.exaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * MetricScale.exaToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * MetricScale.exaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * MetricScale.exaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * MetricScale.exaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.exaToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.exaToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.exaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.exaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.exaToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.exaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.exaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.exaToUni, destinationUnits);
                        break;
                }
                break;

            case Petabytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bytesToBits * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bytesToBits * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bytesToBits * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bytesToBits * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bytesToBits * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bytesToBits * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bytesToBits * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bytesToBits * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * MetricScale.petaToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * MetricScale.petaToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * MetricScale.petaToExa, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * MetricScale.petaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * MetricScale.petaToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * MetricScale.petaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * MetricScale.petaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * MetricScale.petaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.petaToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.petaToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.petaToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.petaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.petaToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.petaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.petaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.petaToUni, destinationUnits);
                        break;
                }
                break;

            case Terabytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bytesToBits * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bytesToBits * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bytesToBits * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bytesToBits * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bytesToBits * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bytesToBits * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bytesToBits * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bytesToBits * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * MetricScale.teraToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * MetricScale.teraToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * MetricScale.teraToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * MetricScale.teraToPeta, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * MetricScale.teraToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * MetricScale.teraToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * MetricScale.teraToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * MetricScale.teraToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.teraToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.teraToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.teraToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.teraToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.teraToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.teraToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.teraToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.teraToUni, destinationUnits);
                        break;
                }
                break;

            case Gigabytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bytesToBits * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bytesToBits * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bytesToBits * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bytesToBits * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bytesToBits * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bytesToBits * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bytesToBits * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bytesToBits * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * MetricScale.gigaToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * MetricScale.gigaToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * MetricScale.gigaToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * MetricScale.gigaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * MetricScale.gigaToTera, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * MetricScale.gigaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * MetricScale.gigaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.gigaToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.gigaToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.gigaToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.gigaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.gigaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.gigaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.gigaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.gigaToUni, destinationUnits);
                        break;
                }
                break;

            case Megabytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bytesToBits * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bytesToBits * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bytesToBits * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bytesToBits * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bytesToBits * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bytesToBits * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bytesToBits * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bytesToBits * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * MetricScale.megaToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * MetricScale.megaToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * MetricScale.megaToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * MetricScale.megaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * MetricScale.megaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * MetricScale.megaToGiga, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * MetricScale.megaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * MetricScale.megaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.megaToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.megaToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.megaToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.megaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.megaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.megaToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.megaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.megaToUni, destinationUnits);
                        break;
                }
                break;

            case Kilobytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bytesToBits * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bytesToBits * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bytesToBits * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bytesToBits * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bytesToBits * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bytesToBits * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bytesToBits * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bytesToBits * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * MetricScale.kiloToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * MetricScale.kiloToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * MetricScale.kiloToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * MetricScale.kiloToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * MetricScale.kiloToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * MetricScale.kiloToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * MetricScale.kiloToMega, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.kiloToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.kiloToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.kiloToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.kiloToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.kiloToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.kiloToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.kiloToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.kiloToUni, destinationUnits);
                        break;
                }
                break;

            case Bytes:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * DataSize.bytesToBits * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * DataSize.bytesToBits * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * DataSize.bytesToBits * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * DataSize.bytesToBits * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * DataSize.bytesToBits * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * DataSize.bytesToBits * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * DataSize.bytesToBits * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * DataSize.bytesToBits * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * DataSize.bytesToBits * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * DataSize.bytesToBits, destinationUnits);
                        break;
                }
                break;

            case Yottabits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bitsToBytes * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bitsToBytes * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bitsToBytes * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bitsToBytes * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bitsToBytes * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bitsToBytes * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bitsToBytes * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.yottaToUni * DataSize.bitsToBytes * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.yottaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.yottaToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.yottaToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.yottaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.yottaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.yottaToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.yottaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.yottaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.yottaToUni, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * MetricScale.yottaToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * MetricScale.yottaToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * MetricScale.yottaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * MetricScale.yottaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * MetricScale.yottaToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * MetricScale.yottaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * MetricScale.yottaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * MetricScale.yottaToUni, destinationUnits);
                        break;
                }
                break;

            case Zettabits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bitsToBytes * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bitsToBytes * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bitsToBytes * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bitsToBytes * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bitsToBytes * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bitsToBytes * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bitsToBytes * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.zettaToUni * DataSize.bitsToBytes * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.zettaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.zettaToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.zettaToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.zettaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.zettaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.zettaToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.zettaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.zettaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.zettaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * MetricScale.zettaToYotta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * MetricScale.zettaToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * MetricScale.zettaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * MetricScale.zettaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * MetricScale.zettaToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * MetricScale.zettaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * MetricScale.zettaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * MetricScale.zettaToUni, destinationUnits);
                        break;
                }
                break;

            case Exabits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bitsToBytes * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bitsToBytes * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bitsToBytes * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bitsToBytes * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bitsToBytes * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bitsToBytes * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bitsToBytes * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.exaToUni * DataSize.bitsToBytes * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.exaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.exaToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.exaToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.exaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.exaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.exaToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.exaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.exaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.exaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * MetricScale.exaToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * MetricScale.exaToZetta, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * MetricScale.exaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * MetricScale.exaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * MetricScale.exaToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * MetricScale.exaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * MetricScale.exaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * MetricScale.exaToUni, destinationUnits);
                        break;
                }
                break;

            case Petabits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bitsToBytes * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bitsToBytes * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bitsToBytes * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bitsToBytes * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bitsToBytes * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bitsToBytes * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bitsToBytes * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.petaToUni * DataSize.bitsToBytes * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.petaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.petaToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.petaToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.petaToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.petaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.petaToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.petaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.petaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.petaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * MetricScale.petaToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * MetricScale.petaToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * MetricScale.petaToExa, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * MetricScale.petaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * MetricScale.petaToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * MetricScale.petaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * MetricScale.petaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * MetricScale.petaToUni, destinationUnits);
                        break;
                }
                break;

            case Terabits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bitsToBytes * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bitsToBytes * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bitsToBytes * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bitsToBytes * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bitsToBytes * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bitsToBytes * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bitsToBytes * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.teraToUni * DataSize.bitsToBytes * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.teraToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.teraToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.teraToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.teraToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.teraToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.teraToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.teraToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.teraToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.teraToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * MetricScale.teraToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * MetricScale.teraToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * MetricScale.teraToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * MetricScale.teraToPeta, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * MetricScale.teraToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * MetricScale.teraToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * MetricScale.teraToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * MetricScale.teraToUni, destinationUnits);
                        break;
                }
                break;

            case Gigabits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bitsToBytes * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bitsToBytes * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bitsToBytes * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bitsToBytes * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bitsToBytes * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bitsToBytes * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bitsToBytes * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.gigaToUni * DataSize.bitsToBytes * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.gigaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.gigaToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.gigaToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.gigaToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.gigaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.gigaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.gigaToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.gigaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.gigaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * MetricScale.gigaToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * MetricScale.gigaToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * MetricScale.gigaToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * MetricScale.gigaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * MetricScale.gigaToTera, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * MetricScale.gigaToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * MetricScale.gigaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * MetricScale.gigaToUni, destinationUnits);
                        break;
                }
                break;

            case Megabits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bitsToBytes * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bitsToBytes * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bitsToBytes * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bitsToBytes * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bitsToBytes * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bitsToBytes * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bitsToBytes * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.megaToUni * DataSize.bitsToBytes * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.megaToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.megaToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.megaToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.megaToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.megaToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.megaToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.megaToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.megaToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.megaToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * MetricScale.megaToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * MetricScale.megaToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * MetricScale.megaToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * MetricScale.megaToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * MetricScale.megaToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * MetricScale.megaToGiga, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * MetricScale.megaToKilo, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * MetricScale.megaToUni, destinationUnits);
                        break;
                }
                break;

            case Kilobits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bitsToBytes * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bitsToBytes * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bitsToBytes * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bitsToBytes * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bitsToBytes * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bitsToBytes * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bitsToBytes * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * MetricScale.kiloToUni * DataSize.bitsToBytes * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * MetricScale.kiloToUni * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.kiloToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.kiloToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.kiloToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.kiloToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.kiloToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.kiloToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.kiloToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.kiloToUni, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * MetricScale.kiloToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * MetricScale.kiloToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * MetricScale.kiloToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * MetricScale.kiloToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * MetricScale.kiloToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * MetricScale.kiloToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * MetricScale.kiloToMega, destinationUnits);
                        break;

                    case Bits:
                        result = new DataSize(this.value * MetricScale.kiloToUni, destinationUnits);
                        break;
                }
                break;

            case Bits:
                switch (destinationUnits)
                {
                    case Yobibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yobibits:
                        result = new DataSize(this.value * BinaryScale.uniToYobi, destinationUnits);
                        break;

                    case Zebibits:
                        result = new DataSize(this.value * BinaryScale.uniToZebi, destinationUnits);
                        break;

                    case Exbibits:
                        result = new DataSize(this.value * BinaryScale.uniToExbi, destinationUnits);
                        break;

                    case Pebibits:
                        result = new DataSize(this.value * BinaryScale.uniToPebi, destinationUnits);
                        break;

                    case Tebibits:
                        result = new DataSize(this.value * BinaryScale.uniToTebi, destinationUnits);
                        break;

                    case Gibibits:
                        result = new DataSize(this.value * BinaryScale.uniToGibi, destinationUnits);
                        break;

                    case Mebibits:
                        result = new DataSize(this.value * BinaryScale.uniToMebi, destinationUnits);
                        break;

                    case Kibibits:
                        result = new DataSize(this.value * BinaryScale.uniToKibi, destinationUnits);
                        break;

                    case Yottabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes * MetricScale.uniToKilo, destinationUnits);
                        break;

                    case Bytes:
                        result = new DataSize(this.value * DataSize.bitsToBytes, destinationUnits);
                        break;

                    case Yottabits:
                        result = new DataSize(this.value * MetricScale.uniToYotta, destinationUnits);
                        break;

                    case Zettabits:
                        result = new DataSize(this.value * MetricScale.uniToZetta, destinationUnits);
                        break;

                    case Exabits:
                        result = new DataSize(this.value * MetricScale.uniToExa, destinationUnits);
                        break;

                    case Petabits:
                        result = new DataSize(this.value * MetricScale.uniToPeta, destinationUnits);
                        break;

                    case Terabits:
                        result = new DataSize(this.value * MetricScale.uniToTera, destinationUnits);
                        break;

                    case Gigabits:
                        result = new DataSize(this.value * MetricScale.uniToGiga, destinationUnits);
                        break;

                    case Megabits:
                        result = new DataSize(this.value * MetricScale.uniToMega, destinationUnits);
                        break;

                    case Kilobits:
                        result = new DataSize(this.value * MetricScale.uniToKilo, destinationUnits);
                        break;
                }
                break;
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public String toString()
    {
        return this.value + " " + this.units;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof DataSize && this.equals((DataSize)rhs);
    }

    public boolean equals(DataSize rhs)
    {
        boolean result = false;
        if (rhs != null)
        {
            final DataSize convertedRhs = rhs.convertTo(this.units);
            result = this.value == convertedRhs.value;
        }
        return result;
    }
}
