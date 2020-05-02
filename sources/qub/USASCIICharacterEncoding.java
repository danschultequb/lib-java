package qub;

public class USASCIICharacterEncoding implements CharacterEncoding
{
    @Override
    public Result<Integer> encodeCharacter(char character, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");
        PreCondition.assertBetween(0, character, 255, "character");

        return byteWriteStream.write((byte)character);
    }

    @Override
    public Iterator<Character> iterateDecodedCharacters(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return bytes.map((Byte b) ->
        {
            if (b == null)
            {
                throw new IllegalArgumentException("Cannot decode a null byte.");
            }
            return (char)Bytes.toUnsignedInt(b);
        });
    }

    @Override
    public boolean equals(Object rhs)
    {
        return CharacterEncoding.equals(this, rhs);
    }
}
