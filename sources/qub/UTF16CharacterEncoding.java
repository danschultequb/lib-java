package qub;

public class UTF16CharacterEncoding implements CharacterEncoding
{
    @Override
    public Result<Integer> encodeCharacter(char character, ByteWriteStream byteWriteStream)
    {
        return null;
    }

    @Override
    public Result<Integer> encodeCharacters(String text, ByteWriteStream byteWriteStream)
    {
        return null;
    }

    @Override
    public Result<Integer> encodeCharacters(char[] characters, int startIndex, int length, ByteWriteStream byteWriteStream)
    {
        return null;
    }

    @Override
    public Result<char[]> decodeAsCharacters(byte[] bytes, int startIndex, int length)
    {
        return null;
    }

    @Override
    public Result<Character> decodeNextCharacter(Iterator<Byte> bytes)
    {
        return null;
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
     * Get whether or not the provided byte is a high surrogate byte (between 0xD800 and 0xDBFF).
     * @param value The byte to check.
     * @return Whether or not the provided byte is a high surrogate byte (between 0xD800 and
     * 0xDBFF).
     */
    public static boolean isHighSurrogateByte(byte value)
    {
        return (value & 0xF8) == 0xD8;
    }

    /**
     * Get whether or not the provided byte is a low surrogate byte (between 0xDC00 and 0xDFFF).
     * @param value The byte to check.
     * @return Whether or not the provided byte is a low surrogate byte (between 0xDC00 and
     * 0xDFFF).
     */
    public static boolean isLowSurrogateByte(byte value)
    {
        return (value & 0xFC) == 0xDC;
    }
}
