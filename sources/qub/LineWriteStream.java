package qub;

public interface LineWriteStream extends Disposable
{
    default CharacterEncoding getCharacterEncoding()
    {
        return asCharacterWriteStream().getCharacterEncoding();
    }

    String getLineSeparator();

    default Result<Boolean> write(char toWrite)
    {
        return asCharacterWriteStream().write(toWrite);
    }

    default Result<Boolean> write(String toWrite, Object... formattedStringArguments)
    {
        return asCharacterWriteStream().write(toWrite, formattedStringArguments);
    }

    default Result<Boolean> writeLine()
    {
        return write(getLineSeparator());
    }

    default Result<Boolean> writeLine(String toWrite, Object... formattedStringArguments)
    {
        toWrite = Strings.concatenate(toWrite, getLineSeparator());
        return write(toWrite, formattedStringArguments);
    }

    default ByteWriteStream asByteWriteStream()
    {
        return asCharacterWriteStream().asByteWriteStream();
    }

    CharacterWriteStream asCharacterWriteStream();
}
