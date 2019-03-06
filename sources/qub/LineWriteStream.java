package qub;

public interface LineWriteStream extends Disposable
{
    default CharacterEncoding getCharacterEncoding()
    {
        return asCharacterWriteStream().getCharacterEncoding();
    }

    String getLineSeparator();

    default Result<Void> write(char toWrite)
    {
        return asCharacterWriteStream().write(toWrite);
    }

    default Result<Void> write(String toWrite, Object... formattedStringArguments)
    {
        return asCharacterWriteStream().write(toWrite, formattedStringArguments);
    }

    default Result<Void> writeLine()
    {
        return write(getLineSeparator());
    }

    default Result<Void> writeLine(String toWrite, Object... formattedStringArguments)
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
