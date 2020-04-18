package qub;

/**
 * A CharacterReadStream that has been wrapped around a ByteReadStream.
 */
public interface CharacterToByteReadStream extends CharacterReadStream
{
    static CharacterToByteReadStream create()
    {
        return CharacterToByteReadStream.create("");
    }

    static CharacterToByteReadStream create(char[] characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return CharacterToByteReadStream.create(characters, CharacterEncoding.UTF_8);
    }

    static CharacterToByteReadStream create(char[] characters, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return CharacterToByteReadStream.create(java.lang.String.valueOf(characters), characterEncoding);
    }

    static CharacterToByteReadStream create(String characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return CharacterToByteReadStream.create(characters, CharacterEncoding.UTF_8);
    }

    static CharacterToByteReadStream create(String characters, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        final byte[] encodedBytes = characterEncoding.encode(characters).await();
        final ByteReadStream byteReadStream = ByteReadStream.create(encodedBytes);
        return CharacterToByteReadStream.create(byteReadStream, characterEncoding);
    }

    static CharacterToByteReadStream create(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");

        return BasicCharacterToByteReadStream.create(byteReadStream, CharacterEncoding.UTF_8);
    }

    static CharacterToByteReadStream create(ByteReadStream byteReadStream, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return BasicCharacterToByteReadStream.create(byteReadStream, characterEncoding);
    }

    /**
     * Get the CharacterEncoding that this CharacterToByteReadStream uses to convert bytes to
     * characters.
     * @return The CharacterEncoding that this CharacterToByteReadStream uses to convert bytes to
     * characters.
     */
    CharacterEncoding getCharacterEncoding();

    /**
     * Set the CharacterEncoding that this CharacterToByteReadStream uses to convert bytes to
     * characters.
     * @param characterEncoding The CharacterEncoding that this CharactertoByteReadStream uses to
     *                          convert bytes to characters.
     * @return This object for method chaining.
     */
    CharacterToByteReadStream setCharacterEncoding(CharacterEncoding characterEncoding);

    /**
     * Get the ByteReadStream that this CharacterToByteReadStream wraps.
     * @return The ByteReadStream that this CharacterToByteReadStream wraps.
     */
    ByteReadStream getByteReadStream();
}
