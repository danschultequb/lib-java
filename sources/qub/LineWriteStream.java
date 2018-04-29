package qub;

public interface LineWriteStream extends Disposable
{
    CharacterEncoding getCharacterEncoding();

    String getLineSeparator();

    Result<Boolean> write(char toWrite);

    Result<Boolean> write(String toWrite, Object... formattedStringArguments);

    Result<Boolean> writeLine();

    Result<Boolean> writeLine(String toWrite, Object... formattedStringArguments);

    ByteWriteStream asByteWriteStream();

    CharacterWriteStream asCharacterWriteStream();
}
