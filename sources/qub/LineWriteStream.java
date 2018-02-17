package qub;

public interface LineWriteStream extends Stream
{
    CharacterEncoding getCharacterEncoding();

    String getLineSeparator();

    boolean write(char toWrite);

    boolean write(String toWrite);

    default boolean writeLine()
    {
        return write(getLineSeparator());
    }

    default boolean writeLine(String toWrite)
    {
        return write(toWrite) && writeLine();
    }

    ByteWriteStream asByteWriteStream();

    CharacterWriteStream asCharacterWriteStream();
}
