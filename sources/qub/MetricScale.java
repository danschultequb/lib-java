package qub;

public interface MetricScale
{
    double yottaToZetta = 1000;
    double yottaToExa = MetricScale.yottaToZetta * MetricScale.zettaToExa;
    double yottaToPeta = MetricScale.yottaToExa * MetricScale.exaToPeta;
    double yottaToTera = MetricScale.yottaToPeta * MetricScale.petaToTera;
    double yottaToGiga = MetricScale.yottaToTera * MetricScale.teraToGiga;
    double yottaToMega = MetricScale.yottaToGiga * MetricScale.gigaToMega;
    double yottaToKilo = MetricScale.yottaToMega * MetricScale.megaToKilo;
    double yottaToUni = MetricScale.yottaToKilo * MetricScale.kiloToUni;

    double zettaToYotta = 1.0 / MetricScale.yottaToZetta;
    double zettaToExa = 1000;
    double zettaToPeta = MetricScale.zettaToExa * MetricScale.exaToPeta;
    double zettaToTera = MetricScale.zettaToPeta * MetricScale.petaToTera;
    double zettaToGiga = MetricScale.zettaToTera * MetricScale.teraToGiga;
    double zettaToMega = MetricScale.zettaToGiga * MetricScale.gigaToMega;
    double zettaToKilo = MetricScale.zettaToMega * MetricScale.megaToKilo;
    double zettaToUni = MetricScale.zettaToKilo * MetricScale.kiloToUni;

    double exaToYotta = 1.0 / MetricScale.yottaToExa;
    double exaToZetta = 1.0 / MetricScale.zettaToExa;
    double exaToPeta = 1000;
    double exaToTera = MetricScale.exaToPeta * MetricScale.petaToTera;
    double exaToGiga = MetricScale.exaToTera * MetricScale.teraToGiga;
    double exaToMega = MetricScale.exaToGiga * MetricScale.gigaToMega;
    double exaToKilo = MetricScale.exaToMega * MetricScale.megaToKilo;
    double exaToUni = MetricScale.exaToKilo * MetricScale.kiloToUni;

    double petaToYotta = 1.0 / MetricScale.yottaToPeta;
    double petaToZetta = 1.0 / MetricScale.zettaToPeta;
    double petaToExa = 1.0 / MetricScale.exaToPeta;
    double petaToTera = 1000;
    double petaToGiga = MetricScale.petaToTera * MetricScale.teraToGiga;
    double petaToMega = MetricScale.petaToGiga * MetricScale.gigaToMega;
    double petaToKilo = MetricScale.petaToMega * MetricScale.megaToKilo;
    double petaToUni = MetricScale.petaToKilo * MetricScale.kiloToUni;

    double teraToYotta = 1.0 / MetricScale.yottaToTera;
    double teraToZetta = 1.0 / MetricScale.zettaToTera;
    double teraToExa = 1.0 / MetricScale.exaToTera;
    double teraToPeta = 1.0 / MetricScale.petaToTera;
    double teraToGiga = 1000;
    double teraToMega = MetricScale.teraToGiga * MetricScale.gigaToMega;
    double teraToKilo = MetricScale.teraToMega * MetricScale.megaToKilo;
    double teraToUni = MetricScale.teraToKilo * MetricScale.kiloToUni;

    double gigaToYotta = 1.0 / MetricScale.yottaToGiga;
    double gigaToZetta = 1.0 / MetricScale.zettaToGiga;
    double gigaToExa = 1.0 / MetricScale.exaToGiga;
    double gigaToPeta = 1.0 / MetricScale.petaToGiga;
    double gigaToTera = 1.0 / MetricScale.teraToGiga;
    double gigaToMega = 1000;
    double gigaToKilo = MetricScale.gigaToMega * MetricScale.megaToKilo;
    double gigaToUni = MetricScale.gigaToKilo * MetricScale.kiloToUni;

    double megaToYotta = 1.0 / MetricScale.yottaToMega;
    double megaToZetta = 1.0 / MetricScale.zettaToMega;
    double megaToExa = 1.0 / MetricScale.exaToMega;
    double megaToPeta = 1.0 / MetricScale.petaToMega;
    double megaToTera = 1.0 / MetricScale.teraToMega;
    double megaToGiga = 1.0 / MetricScale.gigaToMega;
    double megaToKilo = 1000;
    double megaToUni = MetricScale.megaToKilo * MetricScale.kiloToUni;

    double kiloToYotta = 1.0 / MetricScale.yottaToKilo;
    double kiloToZetta = 1.0 / MetricScale.zettaToKilo;
    double kiloToExa = 1.0 / MetricScale.exaToKilo;
    double kiloToPeta = 1.0 / MetricScale.petaToKilo;
    double kiloToTera = 1.0 / MetricScale.teraToKilo;
    double kiloToGiga = 1.0 / MetricScale.gigaToKilo;
    double kiloToMega = 1.0 / MetricScale.megaToKilo;
    double kiloToUni = 1000;

    double uniToYotta = 0.000000000000000000000001;
    double uniToZetta = 1.0 / MetricScale.zettaToUni;
    double uniToExa = 1.0 / MetricScale.exaToUni;
    double uniToPeta = 1.0 / MetricScale.petaToUni;
    double uniToTera = 1.0 / MetricScale.teraToUni;
    double uniToGiga = 1.0 / MetricScale.gigaToUni;
    double uniToMega = 1.0 / MetricScale.megaToUni;
    double uniToKilo = 1.0 / MetricScale.kiloToUni;

    /**
     * Get the conversion multiplier that can be used to convert a value with the from scale unit
     * to the to scale unit.
     * @param from The scale unit a value is being converted from.
     * @param to The scale unit a value is being converted to.
     * @return The conversion multiplier that can be used to convert a value with the from scale
     * unit to the to scale unit.
     */
    static double getConversionMultiplier(MetricScaleUnit from, MetricScaleUnit to)
    {
        PreCondition.assertNotNull(from, "from");
        PreCondition.assertNotNull(to, "to");

        double result = 1;

        switch (from)
        {
            case Yotta:
                switch (to)
                {
                    case Zetta:
                        result = MetricScale.yottaToZetta;
                        break;

                    case Exa:
                        result = MetricScale.yottaToExa;
                        break;

                    case Peta:
                        result = MetricScale.yottaToPeta;
                        break;

                    case Tera:
                        result = MetricScale.yottaToTera;
                        break;

                    case Giga:
                        result = MetricScale.yottaToGiga;
                        break;

                    case Mega:
                        result = MetricScale.yottaToMega;
                        break;

                    case Kilo:
                        result = MetricScale.yottaToKilo;
                        break;

                    case Uni:
                        result = MetricScale.yottaToUni;
                        break;
                }
                break;

            case Zetta:
                switch (to)
                {
                    case Yotta:
                        result = MetricScale.zettaToYotta;
                        break;

                    case Exa:
                        result = MetricScale.zettaToExa;
                        break;

                    case Peta:
                        result = MetricScale.zettaToPeta;
                        break;

                    case Tera:
                        result = MetricScale.zettaToTera;
                        break;

                    case Giga:
                        result = MetricScale.zettaToGiga;
                        break;

                    case Mega:
                        result = MetricScale.zettaToMega;
                        break;

                    case Kilo:
                        result = MetricScale.zettaToKilo;
                        break;

                    case Uni:
                        result = MetricScale.zettaToUni;
                        break;
                }
                break;

            case Exa:
                switch (to)
                {
                    case Yotta:
                        result = MetricScale.exaToYotta;
                        break;

                    case Zetta:
                        result = MetricScale.exaToZetta;
                        break;

                    case Peta:
                        result = MetricScale.exaToPeta;
                        break;

                    case Tera:
                        result = MetricScale.exaToTera;
                        break;

                    case Giga:
                        result = MetricScale.exaToGiga;
                        break;

                    case Mega:
                        result = MetricScale.exaToMega;
                        break;

                    case Kilo:
                        result = MetricScale.exaToKilo;
                        break;

                    case Uni:
                        result = MetricScale.exaToUni;
                        break;
                }
                break;

            case Peta:
                switch (to)
                {
                    case Yotta:
                        result = MetricScale.petaToYotta;
                        break;

                    case Zetta:
                        result = MetricScale.petaToZetta;
                        break;

                    case Exa:
                        result = MetricScale.petaToExa;
                        break;

                    case Tera:
                        result = MetricScale.petaToTera;
                        break;

                    case Giga:
                        result = MetricScale.petaToGiga;
                        break;

                    case Mega:
                        result = MetricScale.petaToMega;
                        break;

                    case Kilo:
                        result = MetricScale.petaToKilo;
                        break;

                    case Uni:
                        result = MetricScale.petaToUni;
                        break;
                }
                break;

            case Tera:
                switch (to)
                {
                    case Yotta:
                        result = MetricScale.teraToYotta;
                        break;

                    case Zetta:
                        result = MetricScale.teraToZetta;
                        break;

                    case Exa:
                        result = MetricScale.teraToExa;
                        break;

                    case Peta:
                        result = MetricScale.teraToPeta;
                        break;

                    case Giga:
                        result = MetricScale.teraToGiga;
                        break;

                    case Mega:
                        result = MetricScale.teraToMega;
                        break;

                    case Kilo:
                        result = MetricScale.teraToKilo;
                        break;

                    case Uni:
                        result = MetricScale.teraToUni;
                        break;
                }
                break;

            case Giga:
                switch (to)
                {
                    case Yotta:
                        result = MetricScale.gigaToYotta;
                        break;

                    case Zetta:
                        result = MetricScale.gigaToZetta;
                        break;

                    case Exa:
                        result = MetricScale.gigaToExa;
                        break;

                    case Peta:
                        result = MetricScale.gigaToPeta;
                        break;

                    case Tera:
                        result = MetricScale.gigaToTera;
                        break;

                    case Mega:
                        result = MetricScale.gigaToMega;
                        break;

                    case Kilo:
                        result = MetricScale.gigaToKilo;
                        break;

                    case Uni:
                        result = MetricScale.gigaToUni;
                        break;
                }
                break;

            case Mega:
                switch (to)
                {
                    case Yotta:
                        result = MetricScale.megaToYotta;
                        break;

                    case Zetta:
                        result = MetricScale.megaToZetta;
                        break;

                    case Exa:
                        result = MetricScale.megaToExa;
                        break;

                    case Peta:
                        result = MetricScale.megaToPeta;
                        break;

                    case Tera:
                        result = MetricScale.megaToTera;
                        break;

                    case Giga:
                        result = MetricScale.megaToGiga;
                        break;

                    case Kilo:
                        result = MetricScale.megaToKilo;
                        break;

                    case Uni:
                        result = MetricScale.megaToUni;
                        break;
                }
                break;

            case Kilo:
                switch (to)
                {
                    case Yotta:
                        result = MetricScale.kiloToYotta;
                        break;

                    case Zetta:
                        result = MetricScale.kiloToZetta;
                        break;

                    case Exa:
                        result = MetricScale.kiloToExa;
                        break;

                    case Peta:
                        result = MetricScale.kiloToPeta;
                        break;

                    case Tera:
                        result = MetricScale.kiloToTera;
                        break;

                    case Giga:
                        result = MetricScale.kiloToGiga;
                        break;

                    case Mega:
                        result = MetricScale.kiloToMega;
                        break;

                    case Uni:
                        result = MetricScale.kiloToUni;
                        break;
                }
                break;

            case Uni:
                switch (to)
                {
                    case Yotta:
                        result = MetricScale.uniToYotta;
                        break;

                    case Zetta:
                        result = MetricScale.uniToZetta;
                        break;

                    case Exa:
                        result = MetricScale.uniToExa;
                        break;

                    case Peta:
                        result = MetricScale.uniToPeta;
                        break;

                    case Tera:
                        result = MetricScale.uniToTera;
                        break;

                    case Giga:
                        result = MetricScale.uniToGiga;
                        break;

                    case Mega:
                        result = MetricScale.uniToMega;
                        break;

                    case Kilo:
                        result = MetricScale.uniToKilo;
                        break;
                }
                break;
        }

        return result;
    }
}
