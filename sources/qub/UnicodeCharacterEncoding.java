package qub;

/**
 * A CharacterEncoding that works with the Unicode character set.
 */
public interface UnicodeCharacterEncoding extends CharacterEncoding
{
    @Override
    default Result<Integer> encodeCharacter(char character, ByteWriteStream byteWriteStream)
    {
        return this.encodeCharacters(Iterator.create(character), byteWriteStream);
    }

    default Result<Integer> encodeCharacters(char[] characters, int startIndex, int length, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        return this.encodeCharacters(CharacterArray.create(characters, startIndex, length).iterate(), byteWriteStream);
    }

    @Override
    default Result<Integer> encodeCharacters(Iterator<Character> characters, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        return this.encodeUnicodeCodePoints(CharacterToUnicodeCodePointIterator.create(characters), byteWriteStream);
    }

    /**
     * Encode the provided Unicode Code Point into bytes.
     * @param unicodeCodePoint The Unicode Code Point to encode.
     * @return The encoded bytes.
     */
    default Result<byte[]> encodeUnicodeCodePoint(int unicodeCodePoint)
    {
        return this.encodeUnicodeCodePoints(Iterator.create(unicodeCodePoint));
    }

    /**
     * Encode the provided Unicode Code Point into bytes.
     * @param unicodeCodePoint The Unicode Code Point to encode.
     * @return The encoded bytes.
     */
    Result<Integer> encodeUnicodeCodePoint(int unicodeCodePoint, ByteWriteStream byteWriteStream);

    /**
     * Encode the provided Unicode Code Points into bytes.
     * @param unicodeCodePoints The Unicode Code Points to encode.
     * @return The encoded bytes.
     */
    default Result<byte[]> encodeUnicodeCodePoints(Iterable<Integer> unicodeCodePoints)
    {
        PreCondition.assertNotNull(unicodeCodePoints, "unicodeCodePoints");

        return this.encodeUnicodeCodePoints(unicodeCodePoints.iterate());
    }

    /**
     * Encode the provided Unicode Code Points into bytes and write the encoded bytes into the
     * provided ByteWriteStream.
     * @param unicodeCodePoints The Unicode Code Points to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded Unicode Code Points to.
     * @return The number of encoded bytes that were written.
     */
    default Result<Integer> encodeUnicodeCodePoints(Iterable<Integer> unicodeCodePoints, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(unicodeCodePoints, "unicodeCodePoints");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return this.encodeUnicodeCodePoints(unicodeCodePoints.iterate(), byteWriteStream);
    }

    /**
     * Encode the provided Unicode Code Points into bytes.
     * @param unicodeCodePoints The Unicode Code Points to encode.
     * @return The encoded bytes.
     */
    default Result<byte[]> encodeUnicodeCodePoints(Iterator<Integer> unicodeCodePoints)
    {
        PreCondition.assertNotNull(unicodeCodePoints, "unicodeCodePoints");

        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            this.encodeUnicodeCodePoints(unicodeCodePoints, byteStream).await();
            return byteStream.getBytes();
        });
    }

    /**
     * Encode the provided Unicode Code Points into bytes and write the encoded bytes into the
     * provided ByteWriteStream.
     * @param unicodeCodePoints The Unicode Code Points to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded Unicode Code Points to.
     * @return The number of encoded bytes that were written.
     */
    default Result<Integer> encodeUnicodeCodePoints(Iterator<Integer> unicodeCodePoints, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(unicodeCodePoints, "unicodeCodePoints");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream.isDisposed()");

        return Result.create(() ->
        {
            int result = 0;

            for (int unicodeCodePoint : unicodeCodePoints)
            {
                result += this.encodeUnicodeCodePoint(unicodeCodePoint, byteWriteStream).await();
            }

            return result;
        });
    }

    @Override
    default UnicodeCodePointToCharacterIterator iterateDecodedCharacters(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return UnicodeCodePointToCharacterIterator.create(this.iterateDecodedUnicodeCodePoints(bytes));
    }

    /**
     * Get an Iterator that will decode the provided bytes into Unicode Code Points as it iterates.
     * @param bytes The bytes to decode.
     * @return An Iterator that will decode the provided bytes into Unicode Code Points as it
     * iterates.
     */
    UnicodeCodePointIterator iterateDecodedUnicodeCodePoints(Iterator<Byte> bytes);
}
