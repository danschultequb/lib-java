package qub;

public class USASCIICharacterEncoding implements CharacterEncoding
{
    @Override
    public Result<byte[]> encode(char[] characters, int startIndex, int length)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertBetween(0, startIndex, characters.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, characters.length - startIndex, "length");

        final byte[] encodedBytes = new byte[characters.length];
        for (int i = 0; i < length; ++i)
        {
            encodedBytes[i] = (byte)characters[startIndex + i];
        }
        return Result.success(encodedBytes);
    }

    @Override
    public Result<char[]> decode(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "characters");
        PreCondition.assertBetween(0, startIndex, bytes.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, bytes.length - startIndex, "length");

        final char[] decodedCharacters = new char[bytes.length];
        for (int i = 0; i < length; ++i)
        {
            decodedCharacters[i] = (char)Bytes.toUnsignedInt(bytes[startIndex + i]);
        }
        return Result.success(decodedCharacters);
    }

    @Override
    public Result<Character> decodeNextCharacter(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        Result<Character> result;
        if (!bytes.next())
        {
            result = Result.success(null);
        }
        else
        {
            result = Result.success((char)Bytes.toUnsignedInt(bytes.getCurrent()));
        }
        return result;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return CharacterEncoding.equals(this, rhs);
    }
}
