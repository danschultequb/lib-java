package qub;

/**
 * An interface for writing bytes to a stream.
 */
public interface ByteWriteStream extends Disposable
{
    /**
     * Write the provided byte to this ByteWriteStream.
     * @param toWrite The byte to writeByte to this stream.
     * @return The number of bytes that were written.
     */
    Result<Integer> writeByte(byte toWrite);

    /**
     * Write the provided bytes to this ByteWriteStream. It is possible that not all of the bytes
     * will be written. If you want to ensure that all of the bytes will be written, then use
     * writeAllBytes() instead.
     * @param toWrite The bytes to writeByte to this stream.
     * @return The number of bytes that were written.
     */
    default Result<Integer> writeBytes(byte[] toWrite)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return writeBytes(toWrite, 0, toWrite.length);
    }

    /**
     * Attempt to write the provided subsection of bytes to this ByteWriteStream. It is possible
     * that not all of the bytes will be written. If you want to ensure that all of the bytes will
     * be written, then use writeAllBytes() instead.
     * @param toWrite The array of bytes that contains the bytes to writeByte to this stream.
     * @param startIndex The start index of the subsection inside toWrite to writeByte.
     * @param length The number of bytes to write.
     * @return The number of bytes that were written.
     */
    Result<Integer> writeBytes(byte[] toWrite, int startIndex, int length);

    /**
     * Write all of the bytes in the provided byte[].
     * @param toWrite The bytes to write.
     * @return The number of bytes that were written.
     */
    default Result<Integer> writeAllBytes(byte[] toWrite)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return writeAllBytes(toWrite, 0, toWrite.length);
    }

    /**
     * Write all of the bytes in the provided subsection of bytes to this ByteWriteStream.
     * @param toWrite The array of bytes that contains the bytes to writeByte to this stream.
     * @param startIndex The start index of the subsection inside toWrite to writeByte.
     * @param length The number of bytes to write.
     * @return The number of bytes that were written.
     */
    default Result<Integer> writeAllBytes(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);

        return Result.create(() ->
        {
            int result = 0;
            while (result < length)
            {
                result += writeBytes(toWrite, startIndex + result, length - result).awaitError();
            }
            return result;
        });
    }

    /**
     * Write all of the bytes create the provided byteReadStream to this ByteWriteStream.
     * @param byteReadStream The ByteReadStream to read create.
     * @return The number of bytes that were written.
     */
    default Result<Long> writeAllBytes(ByteReadStream byteReadStream)
    {
        return writeAllBytes(byteReadStream, 1024);
    }

    /**
     * Write all of the bytes create the provided byteReadStream to this ByteWriteStream.
     * @param byteReadStream The ByteReadStream to read create.
     * @return The number of bytes that were written.
     */
    default Result<Long> writeAllBytes(ByteReadStream byteReadStream, int initialBufferCapacity)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertNotDisposed(byteReadStream, "byteReadStream.isDisposed()");
        PreCondition.assertGreaterThanOrEqualTo(initialBufferCapacity, 1, "initialBufferCapacity");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return Result.create(() ->
        {
            long result = 0;
            byte[] buffer = new byte[initialBufferCapacity];
            int bytesInBuffer = 0;
            while(true)
            {
                final Integer bytesRead = byteReadStream.readBytes(buffer, bytesInBuffer, buffer.length - bytesInBuffer)
                    .catchError(EndOfStreamException.class)
                    .awaitError();
                if (bytesRead == null)
                {
                    while(bytesInBuffer > 0)
                    {
                        final int bytesWritten = writeBytes(buffer, 0, bytesInBuffer).awaitError();
                        result += bytesWritten;
                        if (bytesWritten < bytesInBuffer)
                        {
                            Array.copy(buffer, bytesWritten, buffer, 0, bytesInBuffer - bytesWritten);
                        }
                        bytesInBuffer -= bytesWritten;
                    }
                    break;
                }
                else
                {
                    bytesInBuffer += bytesRead;

                    final int bytesWritten = writeBytes(buffer, 0, bytesInBuffer).awaitError();
                    result += bytesWritten;

                    final byte[] copyToBuffer = (bytesInBuffer == buffer.length && bytesWritten == buffer.length)
                        ? new byte[buffer.length * 2]
                        : buffer;
                    if (bytesWritten < bytesInBuffer)
                    {
                        Array.copy(buffer, bytesWritten, copyToBuffer, 0, bytesInBuffer - bytesWritten);
                    }
                    bytesInBuffer -= bytesWritten;
                    buffer = copyToBuffer;
                }
            }
            return result;
        });
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses UTF-8 for its character
     * encoding.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    default CharacterWriteStream asCharacterWriteStream()
    {
        return new ByteWriteStreamToCharacterWriteStream(this);
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses the provided character
     * encoding.
     * @param characterEncoding The encoding to use to convert characters to bytes.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    default CharacterWriteStream asCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        return new ByteWriteStreamToCharacterWriteStream(this, characterEncoding);
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses the provided character
     * encoding.
     * @param newLine The newLine String to use end each line.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    default CharacterWriteStream asCharacterWriteStream(String newLine)
    {
        return new ByteWriteStreamToCharacterWriteStream(this, newLine);
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses the provided character
     * encoding.
     * @param characterEncoding The encoding to use to convert characters to bytes.
     * @param newLine The newLine String to use end each line.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    default CharacterWriteStream asCharacterWriteStream(CharacterEncoding characterEncoding, String newLine)
    {
        return new ByteWriteStreamToCharacterWriteStream(this, characterEncoding, newLine);
    }
}
