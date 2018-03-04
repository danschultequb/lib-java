package qub;

public interface LineWriteStream extends Stream
{
    CharacterEncoding getCharacterEncoding();

    String getLineSeparator();

    boolean write(char toWrite);

    boolean write(String toWrite);

    boolean writeLine();

    boolean writeLine(String toWrite);

    ByteWriteStream asByteWriteStream();

    CharacterWriteStream asCharacterWriteStream();
}
