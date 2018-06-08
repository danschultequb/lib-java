package qub;

public class USASCIICharacterEncoding extends CharacterEncoding
{
    @Override
    public Result<byte[]> encode(char[] characters)
    {
        Result<byte[]> result = Result.notNullAndNotEmpty(characters, "characters");
        if (result == null)
        {
            final byte[] encodedBytes = new byte[characters.length];
            for (int i = 0; i < characters.length; ++i)
            {
                encodedBytes[i] = (byte)characters[i];
            }
            result = Result.success(encodedBytes);
        }
        return result;
    }

    @Override
    public Result<char[]> decode(byte[] bytes)
    {
        Result<char[]> result = Result.notNullAndNotEmpty(bytes, "bytes");
        if (result == null)
        {
            final char[] decodedCharacters = new char[bytes.length];
            for (int i = 0; i < bytes.length; ++i)
            {
                decodedCharacters[i] = (char)Bytes.toUnsignedInt(bytes[i]);
            }
            result = Result.success(decodedCharacters);
        }
        return result;
    }

    @Override
    public Result<Character> decodeNextCharacter(Iterator<Byte> bytes)
    {
        Result<Character> result = Result.notNull(bytes, "bytes");
        if (result == null)
        {
            if (!bytes.next())
            {
                result = Result.<Character>success(null);
            }
            else
            {
                result = Result.success((char)Bytes.toUnsignedInt(bytes.getCurrent()));
            }
        }
        return result;
    }
}
