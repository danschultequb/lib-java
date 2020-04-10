package qub;

public class USASCIICharacterEncoding implements CharacterEncoding
{
    @Override
    public Result<Integer> encode(char character, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDiposed()");

        return byteWriteStream.write(encodeCharacter(character))
            .then(() -> 1);
    }

    @Override
    public Result<Integer> encode(String text, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(text, "text");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        final byte[] encodedBytes = new byte[text.length()];
        for (int i = 0; i < text.length(); ++i)
        {
            encodedBytes[i] = encodeCharacter(text.charAt(i));
        }
        return byteWriteStream.writeAll(encodedBytes)
            .then(() -> encodedBytes.length);
    }

    @Override
    public Result<Integer> encode(char[] characters, int startIndex, int length, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        final byte[] encodedBytes = new byte[characters.length];
        for (int i = 0; i < length; ++i)
        {
            encodedBytes[i] = encodeCharacter(characters[startIndex + i]);
        }
        return byteWriteStream.writeAll(encodedBytes)
            .then(() -> encodedBytes.length);
    }

    @Override
    public Result<char[]> decode(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "characters");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);

        final char[] decodedCharacters = new char[bytes.length];
        for (int i = 0; i < length; ++i)
        {
            decodedCharacters[i] = decodeByte(bytes[startIndex + i]);
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
            result = Result.endOfStream();
        }
        else if (bytes.getCurrent() == null)
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

    private static byte encodeCharacter(char value)
    {
        return (byte)value;
    }

    private static char decodeByte(byte value)
    {
        return (char)Bytes.toUnsignedInt(value);
    }
}
