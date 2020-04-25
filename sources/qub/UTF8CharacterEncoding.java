package qub;

public class UTF8CharacterEncoding implements CharacterEncoding
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
    public Result<Integer> encodeCharacter(char character, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return Result.create(() -> UTF8CharacterEncoding.writeEncodedCharacter(character, byteWriteStream));
    }

    @Override
    public Result<Integer> encodeCharacters(String text, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(text, "text");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        return Result.create(() ->
        {
            int result = 0;
            for (final char character : Strings.iterate(text))
            {
                result += UTF8CharacterEncoding.writeEncodedCharacter(character, byteWriteStream);
            }
            return result;
        });
    }

    @Override
    public Result<Integer> encodeCharacters(char[] characters, int startIndex, int length, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        return Result.create(() ->
        {
            int result = 0;
            for (int i = 0; i < length; ++i)
            {
                result += UTF8CharacterEncoding.writeEncodedCharacter(characters[startIndex + i], byteWriteStream);
            }
            return result;
        });
    }

    @Override
    public Result<char[]> decodeAsCharacters(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);

        return Result.create(() ->
        {
            final List<Character> characters = List.create();
            final Iterator<Byte> byteIterator = Iterator.create(bytes, startIndex, length);
            while (true)
            {
                final Character decodedCharacter = this.decodeNextCharacter(byteIterator)
                    .catchError(EndOfStreamException.class)
                    .await();
                if (decodedCharacter != null)
                {
                    characters.add(decodedCharacter);
                }
                else
                {
                    break;
                }
            }
            return Array.toCharArray(characters).await();
        });
    }

    @Override
    public Result<Character> decodeNextCharacter(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return Result.create(() ->
        {
            char result;

            if (!bytes.next())
            {
                throw new EndOfStreamException();
            }

            final Byte firstByte = bytes.getCurrent();
            if (firstByte == null)
            {
                throw new IllegalArgumentException("1st byte in decoded character cannot be null.");
            }

            if (UTF8CharacterEncoding.isContinuationByte(firstByte))
            {
                throw new IllegalArgumentException("Expected a leading byte, but found a continuation byte (" + Bytes.toHexString(firstByte) + ") instead.");
            }

            if (UTF8CharacterEncoding.isHighOrLowSurrogateByte(firstByte))
            {
                throw new IllegalArgumentException("1st byte in decoded character " + Bytes.toHexString(firstByte, true) + " is invalid because bytes between 0xD800 and 0xDFFF are reserved in UTF-8 encoding.");
            }

            final int bytesInCharacter = Bytes.getLeadingOneBits(firstByte);

            if (bytesInCharacter == 0)
            {
                result = (char)firstByte.intValue();
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
                    final int resultBits = (firstByteLastFiveBits << 6) | secondByteLastSixBits;
                    result = (char)resultBits;
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
                        throw new NotSupportedException("Decoding UTF-8 encoded byte streams with characters composed of 3 bytes is not supported.");
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

                        throw new NotSupportedException("Decoding UTF-8 encoded byte streams with characters composed of 4 bytes is not supported.");
                    }
                }
            }

            return result;
        });
    }

    @Override
    public Result<Character> decodeNextCharacter(ByteReadStream bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return this.decodeNextCharacter(ByteReadStream.iterate(bytes));
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

    /**
     * Get whether or not the provided byte is a UTF-16 high or low surrogate byte (has an unsigned
     * binary prefix of 11011xxx).
     * @param value The byte to check.
     * @return Whether or not the provided byte is a continuation byte (has an unsigned binary
     * prefix of 11011xxx).
     */
    public static boolean isHighOrLowSurrogateByte(byte value)
    {
        return (value & 0xF8) == 0xD8;
    }

    /**
     * Get whether or not the provided character is a single byte character (has an unsigned binary
     * prefix of 000000000xxxxxxx).
     * @param value The byte to check.
     * @return Whether or not the provided character is a single byte character (has an unsigned
     * binary prefix of 000000000xxxxxxx).
     */
    public static boolean isSingleByteCharacter(char value)
    {
        return (value & 0xFF80) == 0x0000;
    }

    private static int writeEncodedCharacter(char value, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        int result = 0;

        final int characterAsInt = (int)value;
        if (UTF8CharacterEncoding.isSingleByteCharacter(value))
        {
            byteWriteStream.write((byte)characterAsInt).await();
            result = 1;
        }
        else if (characterAsInt <= 0x0007FF)
        {
            final int firstFiveBits = (characterAsInt >>> 6) & 0x1F;
            final byte firstByte = (byte)(0xC0 | firstFiveBits);
            byteWriteStream.write(firstByte).await();

            final int lastSixBits = characterAsInt & 0x3F;
            final byte secondByte = (byte)(0x80 | lastSixBits);
            byteWriteStream.write(secondByte).await();
            result = 2;
        }
        else if (characterAsInt <= 0x00FFFF)
        {
            final int firstFourBits = (characterAsInt >>> 12) & 0xF;
            final byte firstByte = (byte)(0xB0 | firstFourBits);
            byteWriteStream.write(firstByte).await();

            final int middleSixBits = (characterAsInt >>> 6) & 0x3F;
            final byte secondByte = (byte)(0x80 | middleSixBits);
            byteWriteStream.write(secondByte).await();

            final int lastSixBites = characterAsInt & 0x3F;
            final byte thirdByte = (byte)(0x80 | lastSixBites);
            byteWriteStream.write(thirdByte).await();
            result = 3;
        }
        else if (characterAsInt <= 0x10FFFF)
        {
            final int firstThreeBits = (characterAsInt >>> 18) & 0x7;
            final byte firstByte = (byte)(0xF0 | firstThreeBits);
            byteWriteStream.write(firstByte).await();

            final int firstMiddleSixBits = (characterAsInt >>> 12) & 0x3F;
            final byte secondByte = (byte)(0x80 | firstMiddleSixBits);
            byteWriteStream.write(secondByte).await();

            final int secondMiddleSixBits = (characterAsInt >>> 6) & 0x3F;
            final byte thirdByte = (byte)(0x80 | secondMiddleSixBits);
            byteWriteStream.write(thirdByte).await();

            final int lastSixBits = (characterAsInt & 0x3F);
            final byte fourthByte = (byte)(0x80 | lastSixBits);
            byteWriteStream.write(fourthByte).await();
            result = 4;
        }

        PostCondition.assertBetween(1, result, 4, "result");

        return result;
    }
}
