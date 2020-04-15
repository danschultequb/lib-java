package qub;

@Deprecated
public class InMemoryCharacterStream extends InMemoryCharacterToByteStream
{
    protected InMemoryCharacterStream(InMemoryByteStream byteStream, CharacterEncoding characterEncoding)
    {
        super(byteStream, characterEncoding);
    }

    public static InMemoryCharacterStream create()
    {
        return InMemoryCharacterStream.create("");
    }

    public static InMemoryCharacterStream create(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return InMemoryCharacterStream.create(text, CharacterEncoding.UTF_8);
    }

    public static InMemoryCharacterStream create(String text, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(text, "text");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        final byte[] encodedBytes = characterEncoding.encode(text).await();
        final InMemoryByteStream byteStream = new InMemoryByteStream(encodedBytes);
        return InMemoryCharacterStream.create(byteStream, characterEncoding);
    }

    public static InMemoryCharacterStream create(InMemoryByteStream byteStream)
    {
        PreCondition.assertNotNull(byteStream, "byteStream");

        return InMemoryCharacterStream.create(byteStream, CharacterEncoding.UTF_8);
    }

    public static InMemoryCharacterStream create(InMemoryByteStream byteStream, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(byteStream, "byteStream");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return new InMemoryCharacterStream(byteStream, characterEncoding);
    }
}
