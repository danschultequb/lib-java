package qub;

public abstract class BinaryScale
{
    private final static double yobiToZebi = 1024;
    private final static double yobiToExbi = BinaryScale.yobiToZebi * BinaryScale.zebiToExbi;
    private final static double yobiToPebi = BinaryScale.yobiToExbi * BinaryScale.exbiToPebi;
    private final static double yobiToTebi = BinaryScale.yobiToPebi * BinaryScale.pebiToTebi;
    private final static double yobiToGibi = BinaryScale.yobiToTebi * BinaryScale.tebiToGibi;
    private final static double yobiToMebi = BinaryScale.yobiToGibi * BinaryScale.gibiToMebi;
    private final static double yobiToKibi = BinaryScale.yobiToMebi * BinaryScale.mebiToKibi;
    private final static double yobiToUni = BinaryScale.yobiToKibi * BinaryScale.kibiToUni;

    private final static double zebiToYobi = 1.0 / BinaryScale.yobiToZebi;
    private final static double zebiToExbi = 1024;
    private final static double zebiToPebi = BinaryScale.zebiToExbi * BinaryScale.exbiToPebi;
    private final static double zebiToTebi = BinaryScale.zebiToPebi * BinaryScale.pebiToTebi;
    private final static double zebiToGibi = BinaryScale.zebiToTebi * BinaryScale.tebiToGibi;
    private final static double zebiToMebi = BinaryScale.zebiToGibi * BinaryScale.gibiToMebi;
    private final static double zebiToKibi = BinaryScale.zebiToMebi * BinaryScale.mebiToKibi;
    private final static double zebiToUni = BinaryScale.zebiToKibi * BinaryScale.kibiToUni;

    private final static double exbiToYobi = 1.0 / BinaryScale.yobiToExbi;
    private final static double exbiToZebi = 1.0 / BinaryScale.zebiToExbi;
    private final static double exbiToPebi = 1024;
    private final static double exbiToTebi = BinaryScale.exbiToPebi * BinaryScale.pebiToTebi;
    private final static double exbiToGibi = BinaryScale.exbiToTebi * BinaryScale.tebiToGibi;
    private final static double exbiToMebi = BinaryScale.exbiToGibi * BinaryScale.gibiToMebi;
    private final static double exbiToKibi = BinaryScale.exbiToMebi * BinaryScale.mebiToKibi;
    private final static double exbiToUni = BinaryScale.exbiToKibi * BinaryScale.kibiToUni;

    private final static double pebiToYobi = 1.0 / BinaryScale.yobiToPebi;
    private final static double pebiToZebi = 1.0 / BinaryScale.zebiToPebi;
    private final static double pebiToExbi = 1.0 / BinaryScale.exbiToPebi;
    private final static double pebiToTebi = 1024;
    private final static double pebiToGibi = BinaryScale.pebiToTebi * BinaryScale.tebiToGibi;
    private final static double pebiToMebi = BinaryScale.pebiToGibi * BinaryScale.gibiToMebi;
    private final static double pebiToKibi = BinaryScale.pebiToMebi * BinaryScale.mebiToKibi;
    private final static double pebiToUni = BinaryScale.pebiToKibi * BinaryScale.kibiToUni;

    private final static double tebiToYobi = 1.0 / BinaryScale.yobiToTebi;
    private final static double tebiToZebi = 1.0 / BinaryScale.zebiToTebi;
    private final static double tebiToExbi = 1.0 / BinaryScale.exbiToTebi;
    private final static double tebiToPebi = 1.0 / BinaryScale.pebiToTebi;
    private final static double tebiToGibi = 1024;
    private final static double tebiToMebi = BinaryScale.tebiToGibi * BinaryScale.gibiToMebi;
    private final static double tebiToKibi = BinaryScale.tebiToMebi * BinaryScale.mebiToKibi;
    private final static double tebiToUni = BinaryScale.tebiToKibi * BinaryScale.kibiToUni;

    private final static double gibiToYobi = 1.0 / BinaryScale.yobiToGibi;
    private final static double gibiToZebi = 1.0 / BinaryScale.zebiToGibi;
    private final static double gibiToExbi = 1.0 / BinaryScale.exbiToGibi;
    private final static double gibiToPebi = 1.0 / BinaryScale.pebiToGibi;
    private final static double gibiToTebi = 1.0 / BinaryScale.tebiToGibi;
    private final static double gibiToMebi = 1024;
    private final static double gibiToKibi = BinaryScale.gibiToMebi * BinaryScale.mebiToKibi;
    private final static double gibiToUni = BinaryScale.gibiToKibi * BinaryScale.kibiToUni;

    private final static double mebiToYobi = 1.0 / BinaryScale.yobiToMebi;
    private final static double mebiToZebi = 1.0 / BinaryScale.zebiToMebi;
    private final static double mebiToExbi = 1.0 / BinaryScale.exbiToMebi;
    private final static double mebiToPebi = 1.0 / BinaryScale.pebiToMebi;
    private final static double mebiToTebi = 1.0 / BinaryScale.tebiToMebi;
    private final static double mebiToGibi = 1.0 / BinaryScale.gibiToMebi;
    private final static double mebiToKibi = 1024;
    private final static double mebiToUni = BinaryScale.mebiToKibi * BinaryScale.kibiToUni;

    private final static double kibiToYobi = 1.0 / BinaryScale.yobiToKibi;
    private final static double kibiToZebi = 1.0 / BinaryScale.zebiToKibi;
    private final static double kibiToExbi = 1.0 / BinaryScale.exbiToKibi;
    private final static double kibiToPebi = 1.0 / BinaryScale.pebiToKibi;
    private final static double kibiToTebi = 1.0 / BinaryScale.tebiToKibi;
    private final static double kibiToGibi = 1.0 / BinaryScale.gibiToKibi;
    private final static double kibiToMebi = 1.0 / BinaryScale.mebiToKibi;
    private final static double kibiToUni = 1024;

    /**
     * Get the conversion multiplier that can be used to convert a value with the from scale unit
     * to the to scale unit.
     * @param from The scale unit a value is being converted from.
     * @param to The scale unit a value is being converted to.
     * @return The conversion multiplier that can be used to convert a value with the from scale
     * unit to the to scale unit.
     */
    static double getConversionMultiplier(BinaryScaleUnit from, BinaryScaleUnit to)
    {
        PreCondition.assertNotNull(from, "from");
        PreCondition.assertNotNull(to, "to");

        double result = 0;

        switch (from)
        {
            case Yobi:
                switch (to)
                {
                    case Yobi:
                        result = 1;
                        break;

                    case Zebi:
                        result = BinaryScale.yobiToZebi;
                        break;

                    case Exbi:
                        result = BinaryScale.yobiToExbi;
                        break;

                    case Pebi:
                        result = BinaryScale.yobiToPebi;
                        break;

                    case Tebi:
                        result = BinaryScale.yobiToTebi;
                        break;

                    case Gibi:
                        result = BinaryScale.yobiToGibi;
                        break;

                    case Mebi:
                        result = BinaryScale.yobiToMebi;
                        break;

                    case Kibi:
                        result = BinaryScale.yobiToKibi;
                        break;

                    case Uni:
                        result = BinaryScale.yobiToUni;
                        break;
                }
                break;

            case Zebi:
                switch (to)
                {
                    case Yobi:
                        result = BinaryScale.zebiToYobi;
                        break;

                    case Zebi:
                        result = 1;
                        break;

                    case Exbi:
                        result = BinaryScale.zebiToExbi;
                        break;

                    case Pebi:
                        result = BinaryScale.zebiToPebi;
                        break;

                    case Tebi:
                        result = BinaryScale.zebiToTebi;
                        break;

                    case Gibi:
                        result = BinaryScale.zebiToGibi;
                        break;

                    case Mebi:
                        result = BinaryScale.zebiToMebi;
                        break;

                    case Kibi:
                        result = BinaryScale.zebiToKibi;
                        break;

                    case Uni:
                        result = BinaryScale.zebiToUni;
                        break;
                }
                break;

            case Exbi:
                switch (to)
                {
                    case Yobi:
                        result = BinaryScale.exbiToYobi;
                        break;

                    case Zebi:
                        result = BinaryScale.exbiToZebi;
                        break;

                    case Exbi:
                        result = 1;
                        break;

                    case Pebi:
                        result = BinaryScale.exbiToPebi;
                        break;

                    case Tebi:
                        result = BinaryScale.exbiToTebi;
                        break;

                    case Gibi:
                        result = BinaryScale.exbiToGibi;
                        break;

                    case Mebi:
                        result = BinaryScale.exbiToMebi;
                        break;

                    case Kibi:
                        result = BinaryScale.exbiToKibi;
                        break;

                    case Uni:
                        result = BinaryScale.exbiToUni;
                        break;
                }
                break;

            case Pebi:
                break;

            case Tebi:
                break;

            case Gibi:
                break;

            case Mebi:
                break;

            case Kibi:
                break;

            case Uni:
                break;
        }

        return result;
    }
}
