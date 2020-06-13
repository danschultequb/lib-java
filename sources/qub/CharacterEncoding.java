package qub;

/**
 * An encoding that converts between characters and bytes.
 */
public interface CharacterEncoding
{
    USASCIICharacterEncoding US_ASCII = new USASCIICharacterEncoding();
    UTF8CharacterEncoding UTF_8 = new UTF8CharacterEncoding();

    /**
     * Encode the provided character as a byte[].
     * @param character The character to encode.
     * @return The encoded character as bytes.
     */
    default Result<byte[]> encodeCharacter(char character)
    {
        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            this.encodeCharacter(character, byteStream).await();
            return byteStream.getBytes();
        });
    }

    /**
     * Encode the provided character and write the encoded bytes to the provided ByteWriteStream.
     * Return the number of bytes that were written.
     * @param character The character to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    Result<Integer> encodeCharacter(char character, ByteWriteStream byteWriteStream);

    /**
     * Encode the provided String of characters into a byte[].
     * @param text The text to encode.
     * @return The encoded text as bytes.
     */
    default Result<byte[]> encodeCharacters(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            this.encodeCharacters(text, byteStream).await();
            return byteStream.getBytes();
        });

    }

    /**
     * Encode the provided String of characters and write the encoded bytes to the provided
     * ByteWriteStream. Return the number of bytes that were written.
     * @param text The String of characters to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encodeCharacters(String text, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(text, "text");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream");

        return this.encodeCharacters(Strings.iterable(text), byteWriteStream);
    }

    /**
     * Encode the provided character array into a byte[].
     * @param characters The characters to encode.
     * @return The encoded characters as bytes.
     */
    default Result<byte[]> encodeCharacters(char[] characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return this.encodeCharacters(characters, 0, characters.length);
    }

    /**
     * Encode the provided character array and write the encoded bytes to the provided
     * ByteWriteStream. Return the number of bytes that were written.
     * @param characters The character array to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encodeCharacters(char[] characters, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return this.encodeCharacters(characters, 0, characters.length, byteWriteStream);
    }

    default Result<byte[]> encodeCharacters(char[] characters, int startIndex, int length)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);

        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            this.encodeCharacters(characters, startIndex, length, byteStream).await();
            return byteStream.getBytes();
        });

    }

    default Result<Integer> encodeCharacters(char[] characters, int startIndex, int length, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream");

        return Result.create(() ->
        {
            int result = 0;

            final int endIndex = startIndex + length;
            for (int i = startIndex; i < endIndex; ++i)
            {
                result += this.encodeCharacter(characters[i], byteWriteStream).await();
            }

            return result;
        });
    }

    /**
     * Write the encoded byte representations of the provided characters to the provided
     * byteWriteStream.
     * @param characters The characters to encode.
     * @return The encoded bytes.
     */
    default Result<byte[]> encodeCharacters(Iterable<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            this.encodeCharacters(characters, byteStream).await();
            return byteStream.getBytes();
        });
    }

    /**
     * Write the encoded byte representations of the provided characters to the provided
     * byteWriteStream.
     * @param characters The characters to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded characters to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encodeCharacters(Iterable<Character> characters, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream");

        return this.encodeCharacters(characters.iterate(), byteWriteStream);
    }

    /**
     * Write the encoded byte representations of the provided characters to the provided
     * byteWriteStream.
     * @param characters The characters to encode.
     * @return The encoded bytes.
     */
    default Result<byte[]> encodeCharacters(Iterator<Character> characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            this.encodeCharacters(characters, byteStream).await();
            return byteStream.getBytes();
        });
    }

    /**
     * Write the encoded byte representations of the provided characters to the provided
     * byteWriteStream.
     * @param characters The characters to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded characters to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encodeCharacters(Iterator<Character> characters, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotDisposed(byteWriteStream, "byteWriteStream");

        return Result.create(() ->
        {
            int result = 0;
            for (final Character character : characters)
            {
                if (character == null)
                {
                    throw new IllegalArgumentException("Can't encode a null character.");
                }
                result += this.encodeCharacter(character, byteWriteStream).await();
            }
            return result;
        });
    }

    /**
     * Decode the provided byte[] into a char[].
     * @param bytes The byte[] to decode.
     * @return The characters create the decoded byte[].
     */
    default Result<char[]> decodeAsCharacters(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return this.decodeAsCharacters(bytes, 0, bytes.length);
    }

    default Result<char[]> decodeAsCharacters(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);

        return Result.create(() ->
        {
            final Iterator<Byte> byteIterator = Iterator.create(bytes, startIndex, length);
            return Array.toCharArray(CharacterList.create(this.iterateDecodedCharacters(byteIterator))).await();
        });
    }

    /**
     * Decode the provided byte[] into a String.
     * @param bytes The byte[] to decode.
     * @return The String create the decoded byte[].
     */
    default Result<String> decodeAsString(byte[] bytes)
    {
        return this.decodeAsCharacters(bytes)
            .then((char[] characters) -> String.valueOf(characters));
    }

    /**
     * Decode the provided byte[] into a String.
     * @param bytes The byte[] to decode.
     * @return The String create the decoded byte[].
     */
    default Result<String> decodeAsString(byte[] bytes, int startIndex, int length)
    {
        return this.decodeAsCharacters(bytes, startIndex, length)
            .then((char[] characters) -> String.valueOf(characters));
    }

    /**
     * Decode the provided bytes into a char[].
     * @param bytes The bytes to decode.
     * @return The decoded characters.
     */
    default Result<char[]> decodeAsCharacters(Iterable<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return this.decodeAsCharacters(bytes.iterate());
    }

    /**
     * Decode the provided bytes into a char[].
     * @param bytes The bytes to decode.
     * @return The decoded characters.
     */
    default Result<char[]> decodeAsCharacters(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return Result.create(() ->
        {
            return Array.toCharArray(CharacterList.create(this.iterateDecodedCharacters(bytes))).await();
        });
    }

    /**
     * Decode the provided bytes into a String.
     * @param bytes The bytes to decode.
     * @return The decoded characters.
     */
    default Result<String> decodeAsString(Iterable<Byte> bytes)
    {
        return this.decodeAsCharacters(bytes)
            .then((char[] characters) -> String.valueOf(characters));
    }

    /**
     * Decode the provided bytes into a String.
     * @param bytes The bytes to decode.
     * @return The decoded characters.
     */
    default Result<String> decodeAsString(Iterator<Byte> bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return this.decodeAsCharacters(bytes)
            .then((char[] characters) -> String.valueOf(characters));
    }

    /**
     * Get an Iterator that will decode the provided bytes as it iterates.
     * @param bytes The bytes to decode.
     * @return An Iterator that will decode the provided bytes as it iterates.
     */
    default Iterator<Character> iterateDecodedCharacters(ByteReadStream bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertNotDisposed(bytes, "bytes");

        return this.iterateDecodedCharacters(ByteReadStream.iterate(bytes));
    }

    /**
     * Get an Iterator that will decode the provided bytes as it iterates.
     * @param bytes The bytes to decode.
     * @return An Iterator that will decode the provided bytes as it iterates.
     */
    Iterator<Character> iterateDecodedCharacters(Iterator<Byte> bytes);

    /**
     * Get whether or not the provided CharacterEncoding equals the provided Object.
     * @param lhs The CharacterEncoding.
     * @param rhs The Object.
     * @return Whether or not the provided values are equal.
     */
    static boolean equals(CharacterEncoding lhs, Object rhs)
    {
        return rhs instanceof CharacterEncoding && lhs.equals((CharacterEncoding)rhs);
    }

    /**
     * Get whether or not this CharacterEncoding equals the provided CharacterEncoding.
     * @param rhs The CharacterEncoding to compare against this CharacterEncoding.
     * @return Whether or not this CharacterEncoding equals the provided CharacterEncoding.
     */
    default boolean equals(CharacterEncoding rhs)
    {
        return rhs != null && this.getClass().equals(rhs.getClass());
    }
}
