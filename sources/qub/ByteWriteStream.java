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
    Result<Integer> write(byte toWrite);

    /**
     * Write the provided bytes to this ByteWriteStream. It is possible that not all of the bytes
     * will be written. If you want to ensure that all of the bytes will be written, then use
     * writeAll() instead.
     * @param toWrite The bytes to writeByte to this stream.
     * @return The number of bytes that were written.
     */
    default Result<Integer> write(byte[] toWrite)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return this.write(toWrite, 0, toWrite.length);
    }

    /**
     * Attempt to write the provided subsection of bytes to this ByteWriteStream. It is possible
     * that not all of the bytes will be written. If you want to ensure that all of the bytes will
     * be written, then use writeAll() instead.
     * @param toWrite The array of bytes that contains the bytes to writeByte to this stream.
     * @param startIndex The start index of the subsection inside toWrite to writeByte.
     * @param length The number of bytes to write.
     * @return The number of bytes that were written.
     */
    Result<Integer> write(byte[] toWrite, int startIndex, int length);

    /**
     * Write all of the bytes in the provided byte[].
     * @param toWrite The bytes to write.
     * @return The number of bytes that were written.
     */
    default Result<Integer> writeAll(byte[] toWrite)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return this.writeAll(toWrite, 0, toWrite.length);
    }

    /**
     * Write all of the bytes in the provided subsection of bytes to this ByteWriteStream.
     * @param toWrite The array of bytes that contains the bytes to writeByte to this stream.
     * @param startIndex The start index of the subsection inside toWrite to writeByte.
     * @param length The number of bytes to write.
     * @return The number of bytes that were written.
     */
    default Result<Integer> writeAll(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);

        return Result.create(() ->
        {
            int result = 0;
            while (result < length)
            {
                result += this.write(toWrite, startIndex + result, length - result).await();
            }
            return result;
        });
    }

    /**
     * Write all of the bytes create the provided byteReadStream to this ByteWriteStream.
     * @param byteReadStream The ByteReadStream to read create.
     * @return The number of bytes that were written.
     */
    default Result<Long> writeAll(ByteReadStream byteReadStream)
    {
        return this.writeAll(byteReadStream, 1024);
    }

    /**
     * Write all of the bytes create the provided byteReadStream to this ByteWriteStream.
     * @param byteReadStream The ByteReadStream to read create.
     * @return The number of bytes that were written.
     */
    default Result<Long> writeAll(ByteReadStream byteReadStream, int initialBufferCapacity)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertNotDisposed(byteReadStream, "byteReadStream.isDisposed()");
        PreCondition.assertGreaterThanOrEqualTo(initialBufferCapacity, 1, "initialBufferCapacity");
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        return Result.create(() ->
        {
            long result = 0;
            byte[] buffer = new byte[initialBufferCapacity];
            int bytesInBuffer = 0;
            Integer bytesRead;
            while(true)
            {
                bytesRead = byteReadStream.readBytes(buffer, bytesInBuffer, buffer.length - bytesInBuffer)
                    .catchError(EndOfStreamException.class)
                    .await();
                if (bytesRead == null)
                {
                    while(bytesInBuffer > 0)
                    {
                        final int bytesWritten = this.write(buffer, 0, bytesInBuffer).await();
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

                    final int bytesWritten = this.write(buffer, 0, bytesInBuffer).await();
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

    static BufferedByteWriteStream buffer(ByteWriteStream byteWriteStream)
    {
        return BufferedByteWriteStream.create(byteWriteStream);
    }
}
