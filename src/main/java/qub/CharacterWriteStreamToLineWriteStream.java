package qub;

public class CharacterWriteStreamToLineWriteStream implements LineWriteStream
{
    private final CharacterWriteStream stream;
    private final String lineSeparator;

    CharacterWriteStreamToLineWriteStream(CharacterWriteStream stream)
    {
        this(stream, "\n");
    }

    CharacterWriteStreamToLineWriteStream(CharacterWriteStream stream, String lineSeparator)
    {
        this.stream = stream;
        this.lineSeparator = lineSeparator;
    }

    protected CharacterWriteStream getCharacterWriteStream()
    {
        return stream;
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return stream.getCharacterEncoding();
    }

    @Override
    public String getLineSeparator()
    {
        return lineSeparator;
    }

    @Override
    public boolean write(byte toWrite)
    {
        return stream.write(toWrite);
    }

    @Override
    public boolean write(byte[] toWrite)
    {
        return stream.write(toWrite);
    }

    @Override
    public boolean write(byte[] toWrite, int startIndex, int length)
    {
        return stream.write(toWrite, startIndex, length);
    }

    @Override
    public boolean write(char toWrite)
    {
        return stream.write(toWrite);
    }

    @Override
    public boolean write(String toWrite)
    {
        return stream.write(toWrite);
    }

    @Override
    public boolean writeLine()
    {
        return stream.write(lineSeparator);
    }

    @Override
    public boolean writeLine(String toWrite)
    {
        return stream.write(toWrite) && stream.write(lineSeparator);
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return stream.asByteWriteStream();
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return stream;
    }
}
