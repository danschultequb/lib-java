package qub;

public class InMemoryTextReadStream extends TextReadStreamBase
{
    public InMemoryTextReadStream()
    {
        super(new InMemoryByteReadStream());
    }

    public InMemoryTextReadStream(CharacterEncoding characterEncoding)
    {
        super(new InMemoryByteReadStream(), characterEncoding);
    }

    /**
     * Get the unread bytes from this TextReadStream.
     * @return The unread bytes from this TextReadStream.
     */
    public byte[] getBytes()
    {
        return ((InMemoryByteReadStream)getByteReadStream()).getBytes();
    }

    /**
     * Get the unread text from this TextReadStream.
     * @return The unread text from this TextReadStream.
     */
    public String getText()
    {
        final char[] characters = getCharacterEncoding().decode(getBytes());
        return characters == null ? null : String.valueOf(characters);
    }

    public void add(byte... bytes)
    {
        ((InMemoryByteReadStream)getByteReadStream()).add(bytes);
    }

    public void add(String toAdd)
    {
        add(getCharacterEncoding().encode(toAdd));
    }
}
