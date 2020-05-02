package qub;

public class UTF16CharacterEncoding implements CharacterEncoding
{
    @Override
    public Result<Integer> encodeCharacter(char character, ByteWriteStream byteWriteStream)
    {
        return null;
    }

    @Override
    public Iterator<Character> iterateDecodedCharacters(Iterator<Byte> bytes)
    {
        throw new NotSupportedException();
    }

    /**
     * Get whether or not the provided byte is a high or low surrogate byte (between 0xD800 and
     * 0xDFFF).
     * @param value The byte to check.
     * @return Whether or not the provided byte is a high or low surrogate byte (between 0xD800 and
     * 0xDFFF).
     */
    public static boolean isHighOrLowSurrogateByte(byte value)
    {
        return (value & 0xF8) == 0xD8;
    }

    /**
     * Get whether or not the provided byte is a high surrogate byte (between 0xD8 and 0xDB).
     * @param value The byte to check.
     * @return Whether or not the provided byte is a high surrogate byte (between 0xD8 and 0xDB).
     */
    public static boolean isHighSurrogate(byte value)
    {
        // return (value & 1111 1000) == 1101 1000;
        return (value & 0xF8) == 0xD8;
    }

    /**
     * Get whether or not the provided byte is a low surrogate byte (between 0xDC and 0xDF).
     * @param value The byte to check.
     * @return Whether or not the provided byte is a low surrogate byte (between 0xDC and 0xDF).
     */
    public static boolean isLowSurrogate(byte value)
    {
        // return (value & 1111 1100) == 1101 1100;
        return (value & 0xFC) == 0xDC;
    }

    /**
     * Get whether or not the provided character is a high surrogate character (between 0xD800 and
     * 0xDBFF).
     * @param value The character to check.
     * @return Whether or not the provided character is a high surrogate character (between 0xD800 and
     * 0xDBFF).
     */
    public static boolean isHighSurrogate(char value)
    {
        // return (value & 1111 1000 0000 0000) == 1101 1000 0000 0000;
        return (value & 0xFC00) == 0xD800;
    }

    /**
     * Get whether or not the provided character is a low surrogate character (between 0xDC00 and 0xDFFF).
     * @param value The character to check.
     * @return Whether or not the provided character is a low surrogate character (between 0xDC00
     * and 0xDFFF).
     */
    public static boolean isLowSurrogate(char value)
    {
        // return (value & 1111 1100 0000 0000) == 1101 1100 0000 0000;
        return (value & 0xFC00) == 0xDC00;
    }
}
