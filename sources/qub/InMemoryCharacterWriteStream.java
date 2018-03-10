package qub;

public class InMemoryCharacterWriteStream extends CharacterWriteStreamBase
{
    private final InMemoryByteWriteStream byteWriteStream;
    private final CharacterEncoding characterEncoding;

    public InMemoryCharacterWriteStream()
    {
        this(new InMemoryByteWriteStream());
    }

    public InMemoryCharacterWriteStream(InMemoryByteWriteStream byteWriteStream)
    {
        this(byteWriteStream, CharacterEncoding.UTF_8);
    }

    public InMemoryCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        this(new InMemoryByteWriteStream(), characterEncoding);
    }

    public InMemoryCharacterWriteStream(InMemoryByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
    {
        this.byteWriteStream = byteWriteStream;
        this.characterEncoding = characterEncoding;
    }

    public byte[] getBytes()
    {
        return byteWriteStream.getBytes();
    }

    public String getText()
    {
        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final byte[] bytes = getBytes();
        return characterEncoding.decode(bytes);
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
    }

    @Override
    public boolean write(char toWrite)
    {
        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final byte[] bytes = characterEncoding.encode(toWrite);
        return byteWriteStream.write(bytes);
    }

    @Override
    public boolean write(String toWrite, Object... formattedStringArguments)
    {
        if (CharacterWriteStreamBase.shouldFormat(toWrite, formattedStringArguments))
        {
            toWrite = String.format(toWrite, formattedStringArguments);
        }
        final CharacterEncoding characterEncoding = getCharacterEncoding();
        final byte[] bytes = characterEncoding.encode(toWrite);
        return byteWriteStream.write(bytes);
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return byteWriteStream;
    }

    @Override
    public boolean isOpen()
    {
        return byteWriteStream.isOpen();
    }

    @Override
    public void close()
    {
        byteWriteStream.close();
    }
}
