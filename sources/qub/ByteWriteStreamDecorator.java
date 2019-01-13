package qub;

/**
 * A ByteWriteStream decorator that can add funtionality to an existing ByteWriteStream.
 */
public abstract class ByteWriteStreamDecorator implements ByteWriteStream
{
    protected final ByteWriteStream innerStream;

    public ByteWriteStreamDecorator(ByteWriteStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.innerStream = innerStream;
    }

    @Override
    final public boolean isDisposed()
    {
        return innerStream.isDisposed();
    }

    @Override
    final public Result<Boolean> dispose()
    {
        return innerStream.dispose();
    }

    /**
     * Write the provided byte to this ByteWriteStream.
     * @param toWrite The byte to writeByte to this stream.
     */
    @Override
    final public Result<Boolean> writeByte(byte toWrite)
    {
        return ByteWriteStream.super.writeByte(toWrite);
    }

    /**
     * Write the provided bytes to this ByteWriteStream.
     * @param toWrite The bytes to writeByte to this stream.
     */
    @Override
    final public Result<Integer> writeBytes(byte[] toWrite)
    {
        return ByteWriteStream.super.writeBytes(toWrite);
    }

    @Override
    final public Result<Boolean> writeAllBytes(byte[] toWrite)
    {
        return ByteWriteStream.super.writeAllBytes(toWrite);
    }

    /**
     * Write all of the bytes from the provided byteReadStream to this ByteWriteStream.
     * @param byteReadStream The ByteReadStream to read from.
     * @return Whether or not the writeByte was successful.
     */
    @Override
    final public Result<Boolean> writeAllBytes(ByteReadStream byteReadStream)
    {
        return ByteWriteStream.super.writeAllBytes(byteReadStream);
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses UTF-8 for its character
     * encoding.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    @Override
    final public CharacterWriteStream asCharacterWriteStream()
    {
        return ByteWriteStream.super.asCharacterWriteStream();
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses the provided character
     * encoding.
     * @param characterEncoding The encoding to use to convert characters to bytes.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    @Override
    final public CharacterWriteStream asCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        return ByteWriteStream.super.asCharacterWriteStream(characterEncoding);
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and '\n' as its line separator.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    @Override
    final public LineWriteStream asLineWriteStream()
    {
        return ByteWriteStream.super.asLineWriteStream();
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses the provided character encoding
     * and '\n' as its line separator.
     * @param characterEncoding The encoding to use to convert characters to bytes.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    @Override
    final public LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding)
    {
        return ByteWriteStream.super.asLineWriteStream(characterEncoding);
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and the provided line separator.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    @Override
    final public LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return ByteWriteStream.super.asLineWriteStream(lineSeparator);
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses the provided character encoding
     * and the provided line separator.
     * @param characterEncoding The encoding to use to convert characters to bytes.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    @Override
    final public LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding, String lineSeparator)
    {
        return ByteWriteStream.super.asLineWriteStream(characterEncoding, lineSeparator);
    }
}
