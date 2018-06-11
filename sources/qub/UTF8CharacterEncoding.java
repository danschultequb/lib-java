package qub;

public class UTF8CharacterEncoding extends CharacterEncoding
{
    /**
     * A character that will be used to represent an invalid character encoding.
     * https://en.wikipedia.org/wiki/Specials_(Unicode_block)#Replacement_character
     */
    public static final char replacementCharacter = (char)0xFFFD;

    @Override
    public Result<byte[]> encode(char[] characters)
    {
        Result<byte[]> result = Result.notNullAndNotEmpty(characters, "characters");
        if (result == null)
        {
            final List<Byte> encodedBytes = new ArrayList<Byte>();
            for (final char character : characters)
            {
                final int characterAsInt = (int)character;
                if (characterAsInt <= 0x00007F)
                {
                    encodedBytes.add((byte)characterAsInt);
                }
                else if (characterAsInt <= 0x0007FF)
                {
                    final int firstFiveBits = (characterAsInt >>> 6) & 0x1F;
                    final byte firstByte = (byte)(0xC0 | firstFiveBits);
                    encodedBytes.add(firstByte);

                    final int lastSixBits = characterAsInt & 0x3F;
                    final byte secondByte = (byte)(0x80 | lastSixBits);
                    encodedBytes.add(secondByte);
                }
                else if (characterAsInt <= 0x00FFFF)
                {
                    final int firstFourBits = (characterAsInt >>> 12) & 0xF;
                    final byte firstByte = (byte)(0xB0 | firstFourBits);
                    encodedBytes.add(firstByte);

                    final int middleSixBits = (characterAsInt >>> 6) & 0x3F;
                    final byte secondByte = (byte)(0x80 | middleSixBits);
                    encodedBytes.add(secondByte);

                    final int lastSixBites = characterAsInt & 0x3F;
                    final byte thirdByte = (byte)(0x80 | lastSixBites);
                    encodedBytes.add(thirdByte);
                }
                else if (characterAsInt <= 0x10FFFF)
                {
                    final int firstThreeBits = (characterAsInt >>> 18) & 0x7;
                    final byte firstByte = (byte)(0xF0 | firstThreeBits);
                    encodedBytes.add(firstByte);

                    final int firstMiddleSixBits = (characterAsInt >>> 12) & 0x3F;
                    final byte secondByte = (byte)(0x80 | firstMiddleSixBits);
                    encodedBytes.add(secondByte);

                    final int secondMiddleSixBits = (characterAsInt >>> 6) & 0x3F;
                    final byte thirdByte = (byte)(0x80 | secondMiddleSixBits);
                    encodedBytes.add(thirdByte);

                    final int lastSixBits = (characterAsInt & 0x3F);
                    final byte fourthByte = (byte)(0x80 | lastSixBits);
                    encodedBytes.add(fourthByte);
                }
            }
            result = Result.success(Array.toByteArray(encodedBytes));
        }
        return result;
    }

    @Override
    public Result<char[]> decode(byte[] bytes)
    {
        Result<char[]> result = Result.notNullAndNotEmpty(bytes, "bytes");
        if (result == null)
        {
            final List<Character> characters = new ArrayList<>();
            final List<Throwable> errors = new ArrayList<>();
            final Iterator<Byte> byteIterator = Array.fromValues(bytes).iterate();
            while (true)
            {
                final Result<Character> nextCharacter = decodeNextCharacter(byteIterator);
                if (nextCharacter.hasError())
                {
                    errors.add(nextCharacter.getError());
                }

                if (nextCharacter.getValue() != null)
                {
                    characters.add(nextCharacter.getValue());
                }
                else
                {
                    break;
                }
            }

            result = Result.done(Array.toCharArray(characters), ErrorIterable.from(errors));
        }
        return result;
    }

    @Override
    public Result<Character> decodeNextCharacter(Iterator<Byte> bytes)
    {
        Result<Character> result = Result.notNull(bytes, "bytes");
        if (result == null)
        {
            if (!bytes.next() || bytes.getCurrent() == null)
            {
                result = Result.<Character>success(null);
            }
            else
            {
                final Byte firstByte = bytes.getCurrent();
                final int firstByteSignificantBitCount = Bytes.getSignificantBitCount(firstByte);
                switch (firstByteSignificantBitCount)
                {
                    case 0:
                        result = Result.success((char)firstByte.intValue());
                        break;

                    case 1:
                        result = Result.done(replacementCharacter, new IllegalArgumentException("Expected a leading byte, but found a continuation byte (" + Bytes.toHexString(firstByte) + ") instead."));
                        break;

                    case 2:
                    case 3:
                    case 4:
                        result = decodeMultiByteCharacter(firstByte, firstByteSignificantBitCount, bytes);
                        break;

                    default:
                        result = Result.done(replacementCharacter, new IllegalArgumentException("Found an invalid leading byte (" + Bytes.toHexString(firstByte) + ")."));
                        break;
                }
            }
        }
        return result;
    }

    private static Result<Character> decodeMultiByteCharacter(byte firstByte, int expectedBytesInCharacter, Iterator<Byte> bytes)
    {
        Result<Character> result;
        switch (expectedBytesInCharacter)
        {
            case 2:
                if (!bytes.next() || bytes.getCurrent() == null)
                {
                    result = Result.done(replacementCharacter, new IllegalArgumentException("Missing second byte in 2-byte character sequence."));
                }
                else
                {
                    final int unsignedFirstByte = Bytes.toUnsignedInt(firstByte);
                    if (0xD8 <= unsignedFirstByte && unsignedFirstByte <= 0xDF)
                    {
                        result = Result.done(replacementCharacter, new IllegalArgumentException("Byte " + Bytes.toHexString(firstByte, true) + " is invalid because bytes between 0xD800 and 0xDFFF are reserved in UTF-8 encoding."));
                    }
                    else
                    {
                        final Byte secondByte = bytes.getCurrent();
                        final int secondByteSignificantBitCount = Bytes.getSignificantBitCount(secondByte);
                        if (secondByteSignificantBitCount != 1)
                        {
                            result = Result.done(replacementCharacter, new IllegalArgumentException("Expected continuation byte (10xxxxxx), but found " + Bytes.toHexString(secondByte) + " instead."));
                        }
                        else
                        {
                            final int firstByteLastFiveBits = Bytes.toUnsignedInt(firstByte) & 0x1F;
                            final int secondByteLastSixBits = Bytes.toUnsignedInt(secondByte) & 0x3F;
                            final int resultBits = (firstByteLastFiveBits << 6) | secondByteLastSixBits;
                            result = Result.success((char)resultBits);
                        }
                    }
                }
                break;

            default:
                for (int i = 1; bytes.hasCurrent() && i < expectedBytesInCharacter; ++i)
                {
                    bytes.next();
                }
                result = Result.done(replacementCharacter, new NotSupportedException("Decoding UTF-8 encoded byte streams with characters composed of 3 or more bytes are not supported."));
                break;
        }

        return result;
    }
}
