package qub;

public class DataSize // implements Comparable<DataSize>
{
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
