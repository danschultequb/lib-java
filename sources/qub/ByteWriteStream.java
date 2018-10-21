package qub;

/**
 * An interface for writing bytes to a stream.
 */
public interface ByteWriteStream extends Disposable
{
    /**
     * Write the provided byte to this ByteWriteStream.
     * @param toWrite The byte to write to this stream.
     */
    Result<Boolean> write(byte toWrite);

    /**
     * Write the provided bytes to this ByteWriteStream.
     * @param toWrite The bytes to write to this stream.
     */
    default Result<Boolean> write(byte[] toWrite)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return write(toWrite, 0, toWrite.length);
    }

    /**
     * Write the provided subsection of bytes to this ByteWriteStream.
     * @param toWrite The array of bytes that contains the bytes to write to this stream.
     * @param startIndex The start index of the subsection inside toWrite to write.
     * @param length The number of bytes to write.
     */
    default Result<Boolean> write(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertBetween(0, startIndex, toWrite.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, toWrite.length - startIndex, "length");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<Boolean> result = null;
        for (int i = startIndex; i < startIndex + length; ++i)
        {
            result = write(toWrite[i]);
            if (result.getValue() == null || !result.getValue())
            {
                break;
            }
        }

        return result;
    }

    /**
     * Write all of the bytes from the provided byteReadStream to this ByteWriteStream.
     * @param byteReadStream The ByteReadStream to read from.
     * @return Whether or not the write was successful.
     */
    default Result<Boolean> writeAll(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");
        PreCondition.assertFalse(byteReadStream.isDisposed(), "byteReadStream.isDisposed()");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        byte[] buffer = new byte[1024];

        Result<Boolean> result = null;
        while (result == null)
        {
            final Result<Integer> readBytesResult = byteReadStream.readBytes(buffer);
            if (readBytesResult.hasError())
            {
                result = Result.error(readBytesResult.getError());
            }
            else
            {
                final Integer bytesRead = readBytesResult.getValue();
                if (bytesRead == null || bytesRead == -1)
                {
                    result = Result.successTrue();
                }
                else
                {
                    write(buffer, 0, bytesRead);

                    if (bytesRead == buffer.length)
                    {
                        buffer = new byte[buffer.length * 2];
                    }
                }
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
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
