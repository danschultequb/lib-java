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
    public Result<Character> decodeNextCharacter(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return Result.create(() ->
        {
            if (!bytes.next())
            {
                throw new EndOfStreamException();
            }

            final Byte currentByte = bytes.getCurrent();
            if (currentByte == null)
            {
                throw new IllegalArgumentException("Cannot decode a null byte.");
            }

            return (char)Bytes.toUnsignedInt(currentByte);
        });
    }

    @Override
    public boolean equals(Object rhs)
    {
        return CharacterEncoding.equals(this, rhs);
    }
}
