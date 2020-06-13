package qub;

public class UTF8CharacterEncoding implements UnicodeCharacterEncoding
{
    private static final byte[] byteOrderMark = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };

    /**
     * Get the bytes that make up this CharacterEncoding's BOM (byte order mark). If the
     * CharacterEncoding doesn't have a byte order mark, then an empty byte[] will be returned.
     * @return The bytes that make up this CharacterEncoding's BOM (byte order mark).
     */
    public byte[] getByteOrderMark()
    {
        return UTF8CharacterEncoding.byteOrderMark;
    }

    /**
     * Write this CharacterEncoding's BOM (byte order mark) to the provided ByteWriteStream.
     * @param byteWriteStream The ByteWriteStream to write this CharacterEncoding's BOM to.
     * @return The number of bytes that were written.
     */
    public Result<Integer> writeByteOrderMark(ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return byteWriteStream.writeAll(UTF8CharacterEncoding.byteOrderMark);
    }

    @Override
    public Result<Integer> encodeUnicodeCodePoint(int unicodeCodePoint, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertGreaterThanOrEqualTo(unicodeCodePoint, 0, "unicodeCodePoint");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream");

        return Result.create(() ->
        {
            int result = 0;

            if (unicodeCodePoint <= 0x7F)
            {
                byteWriteStream.write((byte)unicodeCodePoint).await();
                result = 1;
            }
            else if (unicodeCodePoint <= 0x0007FF)
            {
                final int firstFiveBits = (unicodeCodePoint >>> 6) & 0x1F;
                final byte firstByte = (byte)(0xC0 | firstFiveBits);
                byteWriteStream.write(firstByte).await();

                final int lastSixBits = unicodeCodePoint & 0x3F;
                final byte secondByte = (byte)(0x80 | lastSixBits);
                byteWriteStream.write(secondByte).await();
                result = 2;
            }
            else if (unicodeCodePoint <= 0x00FFFF)
            {
                final int firstFourBits = (unicodeCodePoint >>> 12) & 0xF;
                final byte firstByte = (byte)(0xE0 | firstFourBits);
                byteWriteStream.write(firstByte).await();

                final int middleSixBits = (unicodeCodePoint >>> 6) & 0x3F;
                final byte secondByte = (byte)(0x80 | middleSixBits);
                byteWriteStream.write(secondByte).await();

                final int lastSixBites = unicodeCodePoint & 0x3F;
                final byte thirdByte = (byte)(0x80 | lastSixBites);
                byteWriteStream.write(thirdByte).await();
                result = 3;
            }
            else if (unicodeCodePoint <= 0x10FFFF)
            {
                final int firstThreeBits = (unicodeCodePoint >>> 18) & 0x7;
                final byte firstByte = (byte)(0xF0 | firstThreeBits);
                byteWriteStream.write(firstByte).await();

                final int firstMiddleSixBits = (unicodeCodePoint >>> 12) & 0x3F;
                final byte secondByte = (byte)(0x80 | firstMiddleSixBits);
                byteWriteStream.write(secondByte).await();

                final int secondMiddleSixBits = (unicodeCodePoint >>> 6) & 0x3F;
                final byte thirdByte = (byte)(0x80 | secondMiddleSixBits);
                byteWriteStream.write(thirdByte).await();

                final int lastSixBits = (unicodeCodePoint & 0x3F);
                final byte fourthByte = (byte)(0x80 | lastSixBits);
                byteWriteStream.write(fourthByte).await();
                result = 4;
            }

            PostCondition.assertBetween(1, result, 4, "result");

            return result;
        });
    }

    @Override
    public UTF8BytesToUnicodeCodePointIterator iterateDecodedUnicodeCodePoints(Iterator<Byte> bytes)
    {
        return UTF8BytesToUnicodeCodePointIterator.create(bytes);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return CharacterEncoding.equals(this, rhs);
    }

    /**
     * Get whether or not the provided byte is a continuation byte (has an unsigned binary prefix of
     * 10xxxxxx).
     * @param value The byte to check.
     * @return Whether or not the provided byte is a continuation byte (has an unsigned binary
     * prefix of 10xxxxxx).
     */
    public static boolean isContinuationByte(byte value)
    {
        return (value & 0xC0) == 0x80;
    }
}
