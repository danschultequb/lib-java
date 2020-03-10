package qub;

public class DataSize // implements Comparable<DataSize>
{
    private final static double yobiToZebi = 1024;
    private final static double yobiToExbi = DataSize.yobiToZebi * DataSize.zebiToExbi;
    private final static double yobiToPebi = DataSize.yobiToExbi * DataSize.exbiToPebi;
    private final static double yobiToTebi = DataSize.yobiToPebi * DataSize.pebiToTebi;
    private final static double yobiToGibi = DataSize.yobiToTebi * DataSize.tebiToGibi;
    private final static double yobiToMebi = DataSize.yobiToGibi * DataSize.gibiToMebi;
    private final static double yobiToKibi = DataSize.yobiToMebi * DataSize.mebiToKibi;
    private final static double yobiToUni = DataSize.yobiToKibi * DataSize.kibiToUni;

    private final static double zebiToYobi = 1.0 / DataSize.yobiToZebi;
    private final static double zebiToExbi = 1024;
    private final static double zebiToPebi = DataSize.zebiToExbi * DataSize.exbiToPebi;
    private final static double zebiToTebi = DataSize.zebiToPebi * DataSize.pebiToTebi;
    private final static double zebiToGibi = DataSize.zebiToTebi * DataSize.tebiToGibi;
    private final static double zebiToMebi = DataSize.zebiToGibi * DataSize.gibiToMebi;
    private final static double zebiToKibi = DataSize.zebiToMebi * DataSize.mebiToKibi;
    private final static double zebiToUni = DataSize.zebiToKibi * DataSize.kibiToUni;

    private final static double exbiToYobi = 1.0 / DataSize.yobiToExbi;
    private final static double exbiToZebi = 1.0 / DataSize.zebiToExbi;
    private final static double exbiToPebi = 1024;
    private final static double exbiToTebi = DataSize.exbiToPebi * DataSize.pebiToTebi;
    private final static double exbiToGibi = DataSize.exbiToTebi * DataSize.tebiToGibi;
    private final static double exbiToMebi = DataSize.exbiToGibi * DataSize.gibiToMebi;
    private final static double exbiToKibi = DataSize.exbiToMebi * DataSize.mebiToKibi;
    private final static double exbiToUni = DataSize.exbiToKibi * DataSize.kibiToUni;

    private final static double pebiToYobi = 1.0 / DataSize.yobiToPebi;
    private final static double pebiToZebi = 1.0 / DataSize.zebiToPebi;
    private final static double pebiToExbi = 1.0 / DataSize.exbiToPebi;
    private final static double pebiToTebi = 1024;
    private final static double pebiToGibi = DataSize.pebiToTebi * DataSize.tebiToGibi;
    private final static double pebiToMebi = DataSize.pebiToGibi * DataSize.gibiToMebi;
    private final static double pebiToKibi = DataSize.pebiToMebi * DataSize.mebiToKibi;
    private final static double pebiToUni = DataSize.pebiToKibi * DataSize.kibiToUni;

    private final static double tebiToYobi = 1.0 / DataSize.yobiToTebi;
    private final static double tebiToZebi = 1.0 / DataSize.zebiToTebi;
    private final static double tebiToExbi = 1.0 / DataSize.exbiToTebi;
    private final static double tebiToPebi = 1.0 / DataSize.pebiToTebi;
    private final static double tebiToGibi = 1024;
    private final static double tebiToMebi = DataSize.tebiToGibi * DataSize.gibiToMebi;
    private final static double tebiToKibi = DataSize.tebiToMebi * DataSize.mebiToKibi;
    private final static double tebiToUni = DataSize.tebiToKibi * DataSize.kibiToUni;

    private final static double gibiToYobi = 1.0 / DataSize.yobiToGibi;
    private final static double gibiToZebi = 1.0 / DataSize.zebiToGibi;
    private final static double gibiToExbi = 1.0 / DataSize.exbiToGibi;
    private final static double gibiToPebi = 1.0 / DataSize.pebiToGibi;
    private final static double gibiToTebi = 1.0 / DataSize.tebiToGibi;
    private final static double gibiToMebi = 1024;
    private final static double gibiToKibi = DataSize.gibiToMebi * DataSize.mebiToKibi;
    private final static double gibiToUni = DataSize.gibiToKibi * DataSize.kibiToUni;

    private final static double mebiToYobi = 1.0 / DataSize.yobiToMebi;
    private final static double mebiToZebi = 1.0 / DataSize.zebiToMebi;
    private final static double mebiToExbi = 1.0 / DataSize.exbiToMebi;
    private final static double mebiToPebi = 1.0 / DataSize.pebiToMebi;
    private final static double mebiToTebi = 1.0 / DataSize.tebiToMebi;
    private final static double mebiToGibi = 1.0 / DataSize.gibiToMebi;
    private final static double mebiToKibi = 1024;
    private final static double mebiToUni = DataSize.mebiToKibi * DataSize.kibiToUni;

    private final static double kibiToYobi = 1.0 / DataSize.yobiToKibi;
    private final static double kibiToZebi = 1.0 / DataSize.zebiToKibi;
    private final static double kibiToExbi = 1.0 / DataSize.exbiToKibi;
    private final static double kibiToPebi = 1.0 / DataSize.pebiToKibi;
    private final static double kibiToTebi = 1.0 / DataSize.tebiToKibi;
    private final static double kibiToGibi = 1.0 / DataSize.gibiToKibi;
    private final static double kibiToMebi = 1.0 / DataSize.mebiToKibi;
    private final static double kibiToUni = 1024;

    private final static double yottaToZetta = 1000;
    private final static double yottaToExa = DataSize.yottaToZetta * DataSize.zettaToExa;
    private final static double yottaToPeta = DataSize.yottaToExa * DataSize.exaToPeta;
    private final static double yottaToTera = DataSize.yottaToPeta * DataSize.petaToTera;
    private final static double yottaToGiga = DataSize.yottaToTera * DataSize.teraToGiga;
    private final static double yottaToMega = DataSize.yottaToGiga * DataSize.gigaToMega;
//    private final static double yottaToKilo = DataSize.

    private final static double zettaToExa = 1000;

    private final static double exaToPeta = 1000;

    private final static double petaToTera = 1000;

    private final static double teraToGiga = 1000;

    private final static double gigaToMega = 1000;

    private final static double megaToKilo = 1000;

    private final static double kiloToUni = 1000;

    private final static double bytesToBits = 8;
}
