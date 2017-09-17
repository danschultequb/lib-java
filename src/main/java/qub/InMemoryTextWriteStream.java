package qub;

public class InMemoryTextWriteStream extends TextWriteStreamBase
{
    public InMemoryTextWriteStream()
    {
        super(new InMemoryByteWriteStream());
    }

    public InMemoryTextWriteStream(CharacterEncoding characterEncoding)
    {
        super(new InMemoryByteWriteStream(), characterEncoding);
    }

    public InMemoryTextWriteStream(String newLine)
    {
        super(new InMemoryByteWriteStream(), newLine);
    }

    public InMemoryTextWriteStream(CharacterEncoding characterEncoding, String newLine)
    {
        super(new InMemoryByteWriteStream(), characterEncoding, newLine);
    }

    public byte[] getBytes()
    {
        final InMemoryByteWriteStream byteWriteStream = getInMemoryByteWriteStream();
        return byteWriteStream.getBytes();
    }

    public String getText()
    {
        return !isOpen() ? null : String.valueOf(getCharacterEncoding().decode(getBytes()));
    }

    public void clear()
    {
        final InMemoryByteWriteStream byteWriteStream = getInMemoryByteWriteStream();
        byteWriteStream.clear();
    }

    private InMemoryByteWriteStream getInMemoryByteWriteStream()
    {
        return (InMemoryByteWriteStream)getByteWriteStream();
    }
}
