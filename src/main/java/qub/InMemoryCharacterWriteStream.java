package qub;

public class InMemoryCharacterWriteStream extends CharacterWriteStreamBase
{
    public InMemoryCharacterWriteStream()
    {
        this(new InMemoryByteWriteStream());
    }

    public InMemoryCharacterWriteStream(CharacterEncoding encoding)
    {
        super(new InMemoryByteWriteStream(), encoding);
    }

    public InMemoryCharacterWriteStream(InMemoryByteWriteStream byteWriteStream)
    {
        super(byteWriteStream);
    }

    @Override
    protected InMemoryByteWriteStream getByteWriteStream()
    {
        return (InMemoryByteWriteStream)super.getByteWriteStream();
    }

    public byte[] getBytes()
    {
        return getByteWriteStream().getBytes();
    }

    public String getText()
    {
        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final byte[] bytes = getBytes();
        return characterEncoding.decode(bytes);
    }

    @Override
    public boolean write(char toWrite)
    {
        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final byte[] bytes = characterEncoding.encode(toWrite);
        return write(bytes);
    }

    @Override
    public boolean write(String toWrite)
    {
        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final byte[] bytes = characterEncoding.encode(toWrite);
        return write(bytes);
    }
}
