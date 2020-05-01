package qub;

/**
 * An iterator that converts UTF-8 encoded bytes to Unicode code point integers.
 */
public class UTF8BytesToUnicodeCodePointIterator implements Iterator<Integer>
{
    private final Iterator<Byte> bytes;
    private Integer currentUnicodeCodePoint;

    private UTF8BytesToUnicodeCodePointIterator(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        this.bytes = bytes;
    }

    public static UTF8BytesToUnicodeCodePointIterator create(Iterator<Byte> bytes)
    {
        return new UTF8BytesToUnicodeCodePointIterator(bytes);
    }

    @Override
    public boolean hasStarted()
    {
        return this.bytes.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return this.currentUnicodeCodePoint != null || this.bytes.hasCurrent();
    }

    @Override
    public Integer getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        if (this.currentUnicodeCodePoint == null)
        {
            this.populateCurrentUnicodeCodePoint();
        }

        return this.currentUnicodeCodePoint;
    }

    @Override
    public boolean next()
    {
        this.currentUnicodeCodePoint = null;
        if (this.bytes.next())
        {
            this.populateCurrentUnicodeCodePoint();
        }
        return this.hasCurrent();
    }

    private void populateCurrentUnicodeCodePoint()
    {
        PreCondition.assertTrue(this.bytes.hasCurrent(), "this.bytes.hasCurrent()");

        final Byte firstByte = bytes.getCurrent();
        if (firstByte == null)
        {
            throw new IllegalArgumentException("1st byte in decoded character cannot be null.");
        }

        if (UTF8CharacterEncoding.isContinuationByte(firstByte))
        {
            throw new IllegalArgumentException("Expected a leading byte, but found a continuation byte (" + Bytes.toHexString(firstByte) + ") instead.");
        }

        final int bytesInCharacter = Bytes.getLeadingOneBits(firstByte);

        if (bytesInCharacter == 0)
        {
            this.currentUnicodeCodePoint = Bytes.toUnsignedInt(firstByte);
        }
        else if (bytesInCharacter >= 5)
        {
            throw new IllegalArgumentException("Found an invalid leading byte (" + Bytes.toHexString(firstByte) + ").");
        }
        else
        {
            if (!bytes.next())
            {
                throw new IllegalArgumentException("Missing 2nd byte of " + bytesInCharacter + " in decoded character.");
            }

            final Byte secondByte = bytes.getCurrent();
            if (secondByte == null)
            {
                throw new IllegalArgumentException("2nd byte of " + bytesInCharacter + " in decoded character cannot be null.");
            }

            if (!UTF8CharacterEncoding.isContinuationByte(secondByte))
            {
                throw new IllegalArgumentException("Expected 2nd byte of " + bytesInCharacter + " to be a continuation byte (10xxxxxx), but found " + Bytes.toHexString(secondByte) + " instead.");
            }

            final int secondByteLastSixBits = Bytes.toUnsignedInt(secondByte) & 0x3F;

            if (bytesInCharacter == 2)
            {
                final int firstByteLastFiveBits = Bytes.toUnsignedInt(firstByte) & 0x1F;
                this.currentUnicodeCodePoint = (firstByteLastFiveBits << 6) | secondByteLastSixBits;
            }
            else
            {
                if (!bytes.next())
                {
                    throw new IllegalArgumentException("Missing 3rd byte of " + bytesInCharacter + " in decoded character.");
                }

                final Byte thirdByte = bytes.getCurrent();
                if (thirdByte == null)
                {
                    throw new IllegalArgumentException("3rd byte of " + bytesInCharacter + " in decoded character cannot be null.");
                }

                if (!UTF8CharacterEncoding.isContinuationByte(thirdByte))
                {
                    throw new IllegalArgumentException("Expected 3rd byte of " + bytesInCharacter + " to be a continuation byte (10xxxxxx), but found " + Bytes.toHexString(thirdByte) + " instead.");
                }

                final int thirdByteLastSixBits = Bytes.toUnsignedInt(thirdByte) & 0x3F;

                if (bytesInCharacter == 3)
                {
                    final int firstByteLastFourBits = Bytes.toUnsignedInt(firstByte) & 0x0F;
                    this.currentUnicodeCodePoint = (firstByteLastFourBits << 12) | (secondByteLastSixBits << 6) | thirdByteLastSixBits;
                }
                else
                {
                    if (!bytes.next())
                    {
                        throw new IllegalArgumentException("Missing 4th byte of " + bytesInCharacter + " in decoded character.");
                    }

                    final Byte fourthByte = bytes.getCurrent();
                    if (fourthByte == null)
                    {
                        throw new IllegalArgumentException("4th byte of " + bytesInCharacter + " in decoded character cannot be null.");
                    }

                    if (!UTF8CharacterEncoding.isContinuationByte(fourthByte))
                    {
                        throw new IllegalArgumentException("Expected 4th byte of " + bytesInCharacter + " to be a continuation byte (10xxxxxx), but found " + Bytes.toHexString(fourthByte) + " instead.");
                    }

                    final int fourthByteLastSixBits = Bytes.toUnsignedInt(fourthByte) & 0x3F;

                    final int firstByteLastThreeBits = Bytes.toUnsignedInt(firstByte) & 0x07;
                    this.currentUnicodeCodePoint = (firstByteLastThreeBits << 18) | (secondByteLastSixBits << 12) | (thirdByteLastSixBits << 6) | fourthByteLastSixBits;
                }
            }
        }
    }
}
