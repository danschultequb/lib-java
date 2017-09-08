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
        return ((InMemoryByteWriteStream)getByteWriteStream()).getBytes();
    }

    public String getText()
    {
        return !isOpen() ? null : getCharacterEncoding().decode(getBytes());
    }
}
