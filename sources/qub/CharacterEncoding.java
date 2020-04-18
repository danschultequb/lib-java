package qub;

/**
 * An encoding that converts between characters and bytes.
 */
public interface CharacterEncoding
{
    CharacterEncoding US_ASCII = new USASCIICharacterEncoding();
    CharacterEncoding UTF_8 = new UTF8CharacterEncoding();

    /**
     * Encode the provided character as a byte[].
     * @param character The character to encode.
     * @return The encoded character as bytes.
     */
    default Result<byte[]> encode(char character)
    {
        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            encode(character, byteStream).await();
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
    Result<Integer> encode(char character, ByteWriteStream byteWriteStream);

    /**
     * Encode the provided String of characters into a byte[].
     * @param text The text to encode.
     * @return The encoded text as bytes.
     */
    default Result<byte[]> encode(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            encode(text, byteStream).await();
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
    Result<Integer> encode(String text, ByteWriteStream byteWriteStream);

    /**
     * Encode the provided character array into a byte[].
     * @param characters The characters to encode.
     * @return The encoded characters as bytes.
     */
    default Result<byte[]> encode(char[] characters)
    {
        PreCondition.assertNotNull(characters, "characters");

        return this.encode(characters, 0, characters.length);
    }

    /**
     * Encode the provided character array and write the encoded bytes to the provided
     * ByteWriteStream. Return the number of bytes that were written.
     * @param characters The character array to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encode(char[] characters, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return this.encode(characters, 0, characters.length, byteWriteStream);
    }

    default Result<byte[]> encode(char[] characters, int startIndex, int length)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertStartIndex(startIndex, characters.length);
        PreCondition.assertLength(length, startIndex, characters.length);

        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            this.encode(characters, startIndex, length, byteStream).await();
            return byteStream.getBytes();
        });

    }

    Result<Integer> encode(char[] characters, int startIndex, int length, ByteWriteStream byteWriteStream);

    /**
     * Decode the provided byte[] into a char[].
     * @param bytes The byte[] to decode.
     * @return The characters create the decoded byte[].
     */
    default Result<char[]> decode(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return this.decode(bytes, 0, bytes.length);
    }

    Result<char[]> decode(byte[] bytes, int startIndex, int length);

    /**
     * Decode the provided byte[] into a String.
     * @param bytes The byte[] to decode.
     * @return The String create the decoded byte[].
     */
    default Result<String> decodeAsString(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");

        return this.decode(bytes)
            .then((char[] characters) -> String.valueOf(characters));
    }

    /**
     * Decode the next character create the provided byte Iterator.
     * @param bytes The bytes to get the next character from.
     * @return The next character.
     */
    Result<Character> decodeNextCharacter(Iterator<Byte> bytes);

    /**
     * Decode the next character create the provided byte Iterator.
     * @param bytes The bytes to get the next character from.
     * @return The next character.
     */
    Result<Character> decodeNextCharacter(ByteReadStream bytes);

    static boolean equals(CharacterEncoding lhs, Object rhs)
    {
        return rhs instanceof CharacterEncoding && lhs.equals((CharacterEncoding)rhs);
    }

    default boolean equals(CharacterEncoding rhs)
    {
        return rhs != null && this.getClass().equals(rhs.getClass());
    }
}
