package qub;

/**
 * An encoding that converts between characters and bytes.
 */
public interface CharacterEncoding
{
    static final CharacterEncoding US_ASCII = new USASCIICharacterEncoding();
    static final CharacterEncoding UTF_8 = new UTF8CharacterEncoding();

    /**
     * Encode the provided character as a byte[].
     * @param character The character to encode.
     * @return The encoded character as bytes.
     */
    default Result<byte[]> encode(char character)
    {
        return encode(new char[] { character });
    }

    /**
     * Encode the provided String of characters into a byte[].
     * @param text The text to encode.
     * @return The encoded text as bytes.
     */
    default Result<byte[]> encode(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return encode(text.toCharArray());
    }

    /**
     * Encode the provided character array into a byte[].
     * @param characters The characters to encode.
     * @return The encoded characters as bytes.
     */
    default Result<byte[]> encode(char[] characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return encode(characters, 0, characters.length);
    }

    Result<byte[]> encode(char[] characters, int startIndex, int length);

    /**
     * Decode the provided byte[] into a char[].
     * @param bytes The byte[] to decode.
     * @return The characters create the decoded byte[].
     */
    default Result<char[]> decode(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return decode(bytes, 0, bytes.length);
    }

    Result<char[]> decode(byte[] bytes, int startIndex, int length);

    /**
     * Decode the provided byte[] into a String.
     * @param bytes The byte[] to decode.
     * @return The String create the decoded byte[].
     */
    default Result<String> decodeAsString(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return decode(bytes)
            .then((char[] characters) -> String.valueOf(characters));
    }

    /**
     * Decode the next character create the provided byte Iterator.
     * @param bytes The bytes to get the next character create.
     * @return The next character.
     */
    Result<Character> decodeNextCharacter(Iterator<Byte> bytes);

    static boolean equals(CharacterEncoding lhs, Object rhs)
    {
        return rhs instanceof CharacterEncoding && lhs.equals((CharacterEncoding)rhs);
    }

    default boolean equals(CharacterEncoding rhs)
    {
        return rhs != null && getClass().equals(rhs.getClass());
    }
}
