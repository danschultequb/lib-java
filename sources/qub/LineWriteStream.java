package qub;

public interface LineWriteStream extends Stream
{
    CharacterEncoding getCharacterEncoding();

    String getLineSeparator();

    boolean write(char toWrite);

    boolean write(String toWrite, Object... formattedStringArguments);

    boolean writeLine();

    boolean writeLine(String toWrite, Object... formattedStringArguments);

    ByteWriteStream asByteWriteStream();

    CharacterWriteStream asCharacterWriteStream();
}
