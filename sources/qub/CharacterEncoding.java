package qub;

/**
 * An encoding that converts between characters and bytes.
 */
public abstract class CharacterEncoding
{
    public static final CharacterEncoding US_ASCII = new USASCIICharacterEncoding();
    public static final CharacterEncoding UTF_8 = new UTF8CharacterEncoding();

    /**
     * Encode the provided character as a byte[].
     * @param character The character to encode.
     * @return The encoded character as bytes.
     */
    public Result<byte[]> encode(char character)
    {
        return encode(new char[] { character });
    }

    /**
     * Encode the provided String of characters into a byte[].
     * @param text The text to encode.
     * @return The encoded text as bytes.
     */
    public Result<byte[]> encode(String text)
    {
        Result<byte[]> result = Result.notNullAndNotEmpty(text, "text");
        if (result == null)
        {
            result = encode(text.toCharArray());
        }
        return result;
    }

    /**
     * Encode the provided character array into a byte[].
     * @param characters The characters to encode.
     * @return The encoded characters as bytes.
     */
    public abstract Result<byte[]> encode(char[] characters);

    /**
     * Decode the provided byte[] into a char[].
     * @param bytes The byte[] to decode.
     * @return The characters from the decoded byte[].
     */
    public abstract Result<char[]> decode(byte[] bytes);

    /**
     * Decode the provided byte[] into a String.
     * @param bytes The byte[] to decode.
     * @return The String from the decoded byte[].
     */
    public Result<String> decodeAsString(byte[] bytes)
    {
        final Result<char[]> result = decode(bytes);
        final char[] decodedCharacters = result.getValue();
        return Result.done(decodedCharacters == null ? null : new String(decodedCharacters), result.getError());
    }

    /**
     * Decode the next character from the provided byte Iterator.
     * @param bytes The bytes to get the next character from.
     * @return The next character.
     */
    public abstract Result<Character> decodeNextCharacter(Iterator<Byte> bytes);

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof CharacterEncoding && equals((CharacterEncoding)rhs);
    }

    public boolean equals(CharacterEncoding rhs)
    {
        return rhs != null && getClass().equals(rhs.getClass());
    }
}
