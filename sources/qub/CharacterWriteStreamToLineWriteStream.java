package qub;

public class CharacterWriteStreamToLineWriteStream implements LineWriteStream
{
    private final CharacterWriteStream characterWriteStream;
    private final String lineSeparator;

    CharacterWriteStreamToLineWriteStream(CharacterWriteStream characterWriteStream)
    {
        this(characterWriteStream, "\n");
    }

    CharacterWriteStreamToLineWriteStream(CharacterWriteStream characterWriteStream, String lineSeparator)
    {
        this.characterWriteStream = characterWriteStream;
        this.lineSeparator = lineSeparator;
    }

    protected CharacterWriteStream getCharacterWriteStream()
    {
        return characterWriteStream;
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return characterWriteStream.getCharacterEncoding();
    }

    @Override
    public String getLineSeparator()
    {
        return lineSeparator;
    }

    @Override
    public boolean write(char toWrite)
    {
        return characterWriteStream.write(toWrite);
    }

    @Override
    public boolean write(String toWrite)
    {
        return characterWriteStream.write(toWrite);
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return characterWriteStream.asByteWriteStream();
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return characterWriteStream;
    }

    @Override
    public boolean isOpen()
    {
        return characterWriteStream.isOpen();
    }

    @Override
    public void close()
    {
        characterWriteStream.close();
    }
}
