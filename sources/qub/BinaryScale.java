package qub;

public interface BinaryScale
{
    double yobiToZebi = 1024;
    double yobiToExbi = BinaryScale.yobiToZebi * BinaryScale.zebiToExbi;
    double yobiToPebi = BinaryScale.yobiToExbi * BinaryScale.exbiToPebi;
    double yobiToTebi = BinaryScale.yobiToPebi * BinaryScale.pebiToTebi;
    double yobiToGibi = BinaryScale.yobiToTebi * BinaryScale.tebiToGibi;
    double yobiToMebi = BinaryScale.yobiToGibi * BinaryScale.gibiToMebi;
    double yobiToKibi = BinaryScale.yobiToMebi * BinaryScale.mebiToKibi;
    double yobiToUni = BinaryScale.yobiToKibi * BinaryScale.kibiToUni;

    double zebiToYobi = 1.0 / BinaryScale.yobiToZebi;
    double zebiToExbi = 1024;
    double zebiToPebi = BinaryScale.zebiToExbi * BinaryScale.exbiToPebi;
    double zebiToTebi = BinaryScale.zebiToPebi * BinaryScale.pebiToTebi;
    double zebiToGibi = BinaryScale.zebiToTebi * BinaryScale.tebiToGibi;
    double zebiToMebi = BinaryScale.zebiToGibi * BinaryScale.gibiToMebi;
    double zebiToKibi = BinaryScale.zebiToMebi * BinaryScale.mebiToKibi;
    double zebiToUni = BinaryScale.zebiToKibi * BinaryScale.kibiToUni;

    double exbiToYobi = 1.0 / BinaryScale.yobiToExbi;
    double exbiToZebi = 1.0 / BinaryScale.zebiToExbi;
    double exbiToPebi = 1024;
    double exbiToTebi = BinaryScale.exbiToPebi * BinaryScale.pebiToTebi;
    double exbiToGibi = BinaryScale.exbiToTebi * BinaryScale.tebiToGibi;
    double exbiToMebi = BinaryScale.exbiToGibi * BinaryScale.gibiToMebi;
    double exbiToKibi = BinaryScale.exbiToMebi * BinaryScale.mebiToKibi;
    double exbiToUni = BinaryScale.exbiToKibi * BinaryScale.kibiToUni;

    double pebiToYobi = 1.0 / BinaryScale.yobiToPebi;
    double pebiToZebi = 1.0 / BinaryScale.zebiToPebi;
    double pebiToExbi = 1.0 / BinaryScale.exbiToPebi;
    double pebiToTebi = 1024;
    double pebiToGibi = BinaryScale.pebiToTebi * BinaryScale.tebiToGibi;
    double pebiToMebi = BinaryScale.pebiToGibi * BinaryScale.gibiToMebi;
    double pebiToKibi = BinaryScale.pebiToMebi * BinaryScale.mebiToKibi;
    double pebiToUni = BinaryScale.pebiToKibi * BinaryScale.kibiToUni;

    double tebiToYobi = 1.0 / BinaryScale.yobiToTebi;
    double tebiToZebi = 1.0 / BinaryScale.zebiToTebi;
    double tebiToExbi = 1.0 / BinaryScale.exbiToTebi;
    double tebiToPebi = 1.0 / BinaryScale.pebiToTebi;
    double tebiToGibi = 1024;
    double tebiToMebi = BinaryScale.tebiToGibi * BinaryScale.gibiToMebi;
    double tebiToKibi = BinaryScale.tebiToMebi * BinaryScale.mebiToKibi;
    double tebiToUni = BinaryScale.tebiToKibi * BinaryScale.kibiToUni;

    double gibiToYobi = 1.0 / BinaryScale.yobiToGibi;
    double gibiToZebi = 1.0 / BinaryScale.zebiToGibi;
    double gibiToExbi = 1.0 / BinaryScale.exbiToGibi;
    double gibiToPebi = 1.0 / BinaryScale.pebiToGibi;
    double gibiToTebi = 1.0 / BinaryScale.tebiToGibi;
    double gibiToMebi = 1024;
    double gibiToKibi = BinaryScale.gibiToMebi * BinaryScale.mebiToKibi;
    double gibiToUni = BinaryScale.gibiToKibi * BinaryScale.kibiToUni;

    double mebiToYobi = 1.0 / BinaryScale.yobiToMebi;
    double mebiToZebi = 1.0 / BinaryScale.zebiToMebi;
    double mebiToExbi = 1.0 / BinaryScale.exbiToMebi;
    double mebiToPebi = 1.0 / BinaryScale.pebiToMebi;
    double mebiToTebi = 1.0 / BinaryScale.tebiToMebi;
    double mebiToGibi = 1.0 / BinaryScale.gibiToMebi;
    double mebiToKibi = 1024;
    double mebiToUni = BinaryScale.mebiToKibi * BinaryScale.kibiToUni;

    double kibiToYobi = 1.0 / BinaryScale.yobiToKibi;
    double kibiToZebi = 1.0 / BinaryScale.zebiToKibi;
    double kibiToExbi = 1.0 / BinaryScale.exbiToKibi;
    double kibiToPebi = 1.0 / BinaryScale.pebiToKibi;
    double kibiToTebi = 1.0 / BinaryScale.tebiToKibi;
    double kibiToGibi = 1.0 / BinaryScale.gibiToKibi;
    double kibiToMebi = 1.0 / BinaryScale.mebiToKibi;
    double kibiToUni = 1024;

    double uniToYobi = 1.0 / BinaryScale.yobiToUni;
    double uniToZebi = 1.0 / BinaryScale.zebiToUni;
    double uniToExbi = 1.0 / BinaryScale.exbiToUni;
    double uniToPebi = 1.0 / BinaryScale.pebiToUni;
    double uniToTebi = 1.0 / BinaryScale.tebiToUni;
    double uniToGibi = 1.0 / BinaryScale.gibiToUni;
    double uniToMebi = 1.0 / BinaryScale.mebiToUni;
    double uniToKibi = 1.0 / BinaryScale.kibiToUni;

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

        double result = 1;

        switch (from)
        {
            case Yobi:
                switch (to)
                {
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
                switch (to)
                {
                    case Yobi:
                        result = BinaryScale.pebiToYobi;
                        break;

                    case Zebi:
                        result = BinaryScale.pebiToZebi;
                        break;

                    case Exbi:
                        result = BinaryScale.pebiToExbi;
                        break;

                    case Tebi:
                        result = BinaryScale.pebiToTebi;
                        break;

                    case Gibi:
                        result = BinaryScale.pebiToGibi;
                        break;

                    case Mebi:
                        result = BinaryScale.pebiToMebi;
                        break;

                    case Kibi:
                        result = BinaryScale.pebiToKibi;
                        break;

                    case Uni:
                        result = BinaryScale.pebiToUni;
                        break;
                }
                break;

            case Tebi:
                switch (to)
                {
                    case Yobi:
                        result = BinaryScale.tebiToYobi;
                        break;

                    case Zebi:
                        result = BinaryScale.tebiToZebi;
                        break;

                    case Exbi:
                        result = BinaryScale.tebiToExbi;
                        break;

                    case Pebi:
                        result = BinaryScale.tebiToPebi;
                        break;

                    case Gibi:
                        result = BinaryScale.tebiToGibi;
                        break;

                    case Mebi:
                        result = BinaryScale.tebiToMebi;
                        break;

                    case Kibi:
                        result = BinaryScale.tebiToKibi;
                        break;

                    case Uni:
                        result = BinaryScale.tebiToUni;
                        break;
                }
                break;

            case Gibi:
                switch (to)
                {
                    case Yobi:
                        result = BinaryScale.gibiToYobi;
                        break;

                    case Zebi:
                        result = BinaryScale.gibiToZebi;
                        break;

                    case Exbi:
                        result = BinaryScale.gibiToExbi;
                        break;

                    case Pebi:
                        result = BinaryScale.gibiToPebi;
                        break;

                    case Tebi:
                        result = BinaryScale.gibiToTebi;
                        break;

                    case Mebi:
                        result = BinaryScale.gibiToMebi;
                        break;

                    case Kibi:
                        result = BinaryScale.gibiToKibi;
                        break;

                    case Uni:
                        result = BinaryScale.gibiToUni;
                        break;
                }
                break;

            case Mebi:
                switch (to)
                {
                    case Yobi:
                        result = BinaryScale.mebiToYobi;
                        break;

                    case Zebi:
                        result = BinaryScale.mebiToZebi;
                        break;

                    case Exbi:
                        result = BinaryScale.mebiToExbi;
                        break;

                    case Pebi:
                        result = BinaryScale.mebiToPebi;
                        break;

                    case Tebi:
                        result = BinaryScale.mebiToTebi;
                        break;

                    case Gibi:
                        result = BinaryScale.mebiToGibi;
                        break;

                    case Kibi:
                        result = BinaryScale.mebiToKibi;
                        break;

                    case Uni:
                        result = BinaryScale.mebiToUni;
                        break;
                }
                break;

            case Kibi:
                switch (to)
                {
                    case Yobi:
                        result = BinaryScale.kibiToYobi;
                        break;

                    case Zebi:
                        result = BinaryScale.kibiToZebi;
                        break;

                    case Exbi:
                        result = BinaryScale.kibiToExbi;
                        break;

                    case Pebi:
                        result = BinaryScale.kibiToPebi;
                        break;

                    case Tebi:
                        result = BinaryScale.kibiToTebi;
                        break;

                    case Gibi:
                        result = BinaryScale.kibiToGibi;
                        break;

                    case Mebi:
                        result = BinaryScale.kibiToMebi;
                        break;

                    case Uni:
                        result = BinaryScale.kibiToUni;
                        break;
                }
                break;

            case Uni:
                switch (to)
                {
                    case Yobi:
                        result = BinaryScale.uniToYobi;
                        break;

                    case Zebi:
                        result = BinaryScale.uniToZebi;
                        break;

                    case Exbi:
                        result = BinaryScale.uniToExbi;
                        break;

                    case Pebi:
                        result = BinaryScale.uniToPebi;
                        break;

                    case Tebi:
                        result = BinaryScale.uniToTebi;
                        break;

                    case Gibi:
                        result = BinaryScale.uniToGibi;
                        break;

                    case Mebi:
                        result = BinaryScale.uniToMebi;
                        break;

                    case Kibi:
                        result = BinaryScale.uniToKibi;
                        break;
                }
                break;
        }

        return result;
    }
}
