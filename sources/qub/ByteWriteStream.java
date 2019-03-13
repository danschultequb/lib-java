package qub;

/**
 * An interface for writing bytes to a stream.
 */
public interface ByteWriteStream extends Disposable
{
    /**
     * Write the provided byte to this ByteWriteStream.
     * @param toWrite The byte to writeByte to this stream.
     */
    default Result<Boolean> writeByte(byte toWrite)
    {
        return writeBytes(new byte[] { toWrite })
            .thenResult(Result::successTrue);
    }

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
    default Result<Void> writeAllBytes(byte[] toWrite)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
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
    default Result<Void> writeAllBytes(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);

        return Result.create(() ->
        {
            int result = 0;
            while (result < length)
            {
                result += writeBytes(toWrite, startIndex + result, length - result).awaitError();
            }
        });
    }

    /**
     * Write all of the bytes create the provided byteReadStream to this ByteWriteStream.
     * @param byteReadStream The ByteReadStream to read create.
     * @return The number of bytes that were written.
     */
    default Result<Void> writeAllBytes(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertFalse(byteReadStream.isDisposed(), "byteReadStream.isDisposed()");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return Result.create(() ->
        {
            byte[] buffer = new byte[1024];
            int bytesInBuffer = 0;
            while(true)
            {
                final Integer bytesRead = byteReadStream.readBytes(buffer, 0, buffer.length - bytesInBuffer)
                    .catchError(EndOfStreamException.class)
                    .awaitError();
                if (bytesRead == null)
                {
                    break;
                }
                else
                {
                    bytesInBuffer += bytesRead;
                    final int bytesWritten = writeBytes(buffer, 0, bytesInBuffer).awaitError();
                    if (bytesWritten < bytesInBuffer)
                    {
                        Array.copy(buffer, 0, buffer, bytesWritten, bytesInBuffer - bytesWritten);
                        bytesInBuffer -= bytesWritten;
                    }
                    else
                    {
                        if (bytesRead == bytesInBuffer)
                        {
                            buffer = new byte[buffer.length * 2];
                        }
                        bytesInBuffer = 0;
                    }
                }
            }
        });
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses UTF-8 for its character
     * encoding.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    default CharacterWriteStream asCharacterWriteStream()
    {
        return asCharacterWriteStream(CharacterEncoding.UTF_8);
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses the provided character
     * encoding.
     * @param characterEncoding The encoding to use to convert characters to bytes.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    default CharacterWriteStream asCharacterWriteStream(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return new BasicCharacterWriteStream(this, characterEncoding);
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and '\n' as its line separator.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    default LineWriteStream asLineWriteStream()
    {
        return asCharacterWriteStream().asLineWriteStream();
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses the provided character encoding
     * and '\n' as its line separator.
     * @param characterEncoding The encoding to use to convert characters to bytes.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    default LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return asCharacterWriteStream(characterEncoding).asLineWriteStream();
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and the provided line separator.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    default LineWriteStream asLineWriteStream(String lineSeparator)
    {
        PreCondition.assertNotNullAndNotEmpty(lineSeparator, "lineSeparator");

        return asCharacterWriteStream().asLineWriteStream(lineSeparator);
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses the provided character encoding
     * and the provided line separator.
     * @param characterEncoding The encoding to use to convert characters to bytes.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    default LineWriteStream asLineWriteStream(CharacterEncoding characterEncoding, String lineSeparator)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");
        PreCondition.assertNotNullAndNotEmpty(lineSeparator, "lineSeparator");

        return asCharacterWriteStream(characterEncoding).asLineWriteStream(lineSeparator);
    }
}
