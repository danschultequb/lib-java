package qub;

/**
 * An interface for encoding bytes.
 */
public interface ByteEncoding
{
    Base64ByteEncoding Base64 = Base64ByteEncoding.create();

    /**
     * Encode the provided value as a byte[].
     * @param value The value to encode.
     * @return The encoded bytes as a byte[].
     */
    default Result<byte[]> encodeByte(byte value)
    {
        return this.encodeBytes(new byte[] { value });
    }

    /**
     * Encode the provided value and write the encoded bytes to the provided ByteWriteStream.
     * Return the number of bytes that were written.
     * @param value The value to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encodeByte(byte value, ByteWriteStream byteWriteStream)
    {
        return this.encodeBytes(new byte[] { value }, byteWriteStream);
    }

    /**
     * Encode the provided values as a byte[].
     * @param values The values to encode.
     * @return The encoded bytes as a byte[].
     */
    default Result<byte[]> encodeBytes(byte[] values)
    {
        PreCondition.assertNotNull(values, "values");

        return this.encodeBytes(values, 0, values.length);
    }

    /**
     * Encode the provided values and write the encoded bytes to the provided ByteWriteStream.
     * Return the number of bytes that were written.
     * @param values The character array to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encodeBytes(byte[] values, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return this.encodeBytes(values, 0, values.length, byteWriteStream);
    }

    /**
     * Encode the provided values as a byte[].
     * @param values The values to encode.
     * @return The encoded bytes as a byte[].
     */
    default Result<byte[]> encodeBytes(byte[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            this.encodeBytes(values, startIndex, length, byteStream).await();
            return byteStream.getBytes();
        });
    }

    /**
     * Encode the provided values and write the encoded bytes to the provided ByteWriteStream.
     * Return the number of bytes that were written.
     * @param values The values to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encodeBytes(byte[] values, int startIndex, int length, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return this.encodeBytes(ByteArray.create(values, startIndex, length), byteWriteStream);
    }

    /**
     * Encode the provided values and write the encoded bytes to the provided ByteWriteStream.
     * Return the number of bytes that were written.
     * @param values The values to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encodeBytes(ByteReadStream values, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return this.encodeBytes(ByteReadStream.iterate(values), byteWriteStream);
    }

    /**
     * Encode the provided values and write the encoded bytes to the provided ByteWriteStream.
     * Return the number of bytes that were written.
     * @param values The values to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    default Result<Integer> encodeBytes(Iterable<Byte> values, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return this.encodeBytes(values.iterate(), byteWriteStream);
    }

    /**
     * Encode the provided values and write the encoded bytes to the provided ByteWriteStream.
     * Return the number of bytes that were written.
     * @param values The values to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    Result<Integer> encodeBytes(Iterator<Byte> values, ByteWriteStream byteWriteStream);

    /**
     * Decode the provided encoded bytes into a byte[].
     * @param encodedBytes The encoded bytes to decode.
     * @return The the decoded bytes as a byte[].
     */
    default Result<byte[]> decodeAsBytes(byte[] encodedBytes)
    {
        PreCondition.assertNotNull(encodedBytes, "encodedBytes");

        return Result.create(() ->
        {
            final ByteList list = new ByteList();
            list.addAll(this.iterateDecodedBytes(encodedBytes));
            final byte[] result = list.toByteArray();

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    /**
     * Get an Iterator that will decode the provided bytes as it iterates.
     * @param encodedBytes The encoded bytes to decode.
     * @return An Iterator that will decode the provided bytes as it iterates.
     */
    default Iterator<Byte> iterateDecodedBytes(byte[] encodedBytes)
    {
        PreCondition.assertNotNull(encodedBytes, "encodedBytes");

        return this.iterateDecodedBytes(InMemoryByteStream.create(encodedBytes).endOfStream());
    }

    /**
     * Get an Iterator that will decode the provided bytes as it iterates.
     * @param encodedBytes The encoded bytes to decode.
     * @return An Iterator that will decode the provided bytes as it iterates.
     */
    default Iterator<Byte> iterateDecodedBytes(ByteReadStream encodedBytes)
    {
        PreCondition.assertNotNull(encodedBytes, "encodedBytes");

        return this.iterateDecodedBytes(ByteReadStream.iterate(encodedBytes));
    }

    default Iterator<Byte> iterateDecodedBytes(Iterable<Byte> encodedBytes)
    {
        PreCondition.assertNotNull(encodedBytes, "encodedBytes");

        return this.iterateDecodedBytes(encodedBytes.iterate());
    }

    Iterator<Byte> iterateDecodedBytes(Iterator<Byte> encodedBytes);
}
