package qub;

public abstract class ByteWriteStreamBase implements ByteWriteStream
{
    @Override
    public Result<Boolean> write(byte[] toWrite)
    {
        return ByteWriteStreamBase.write(this, toWrite);
    }

    @Override
    public Result<Boolean> write(byte[] toWrite, int startIndex, int length)
    {
        return ByteWriteStreamBase.write(this, toWrite, startIndex, length);
    }

    @Override
    public Result<Boolean> writeAll(ByteReadStream byteReadStream)
    {
        return ByteWriteStreamBase.writeAll(this, byteReadStream);
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return ByteWriteStreamBase.asCharacterWriteStream(this);
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream(CharacterEncoding encoding)
    {
        return ByteWriteStreamBase.asCharacterWriteStream(this, encoding);
    }

    @Override
    public LineWriteStream asLineWriteStream()
    {
        return ByteWriteStreamBase.asLineWriteStream(this);
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding encoding)
    {
        return ByteWriteStreamBase.asLineWriteStream(this, encoding);
    }

    @Override
    public LineWriteStream asLineWriteStream(String lineSeparator)
    {
        return ByteWriteStreamBase.asLineWriteStream(this, lineSeparator);
    }

    @Override
    public LineWriteStream asLineWriteStream(CharacterEncoding encoding, String lineSeparator)
    {
        return ByteWriteStreamBase.asLineWriteStream(this, encoding, lineSeparator);
    }

    /**
     * Write the provided bytes to this ByteWriteStream.
     * @param toWrite The bytes to write to this stream.
     */
    public static Result<Boolean> write(ByteWriteStream byteWriteStream, byte[] toWrite)
    {
        Result<Boolean> result = Disposable.validateNotDisposed(byteWriteStream, "byteWriteStream");
        if (result == null)
        {
            result = ByteWriteStreamBase.validateToWrite(toWrite);
            if (result == null)
            {
                result = byteWriteStream.write(toWrite, 0, toWrite.length);
            }
        }
        return result;
    }

    /**
     * Write the provided subsection of bytes to this ByteWriteStream.
     * @param toWrite The array of bytes that contains the bytes to write to this stream.
     * @param startIndex The start index of the subsection inside toWrite to write.
     * @param length The number of bytes to write.
     */
    public static Result<Boolean> write(ByteWriteStream byteWriteStream, byte[] toWrite, int startIndex, int length)
    {
        Result<Boolean> result = Disposable.validateNotDisposed(byteWriteStream, "byteWriteStream");
        if (result == null)
        {
            result = ByteWriteStreamBase.validateToWrite(toWrite);
            if (result == null)
            {
                result = ByteWriteStreamBase.validateStartIndex(startIndex, toWrite);
                if (result == null)
                {
                    result = ByteWriteStreamBase.validateLength(length, toWrite, startIndex);
                    if (result == null)
                    {
                        for (int i = startIndex; i < startIndex + length; ++i)
                        {
                            result = byteWriteStream.write(toWrite[i]);
                            if (result.getValue() == null || !result.getValue())
                            {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Write all of the bytes from the provided byteReadStream to this ByteWriteStream.
     * @param byteReadStream The ByteReadStream to read from.
     * @return Whether or not the write was successful.
     */
    public static Result<Boolean> writeAll(ByteWriteStream byteWriteStream, ByteReadStream byteReadStream)
    {
        Result<Boolean> result = Disposable.validateNotDisposed(byteWriteStream, "byteWriteStream");
        if (result == null)
        {
            result = Disposable.validateNotDisposed(byteReadStream, "byteReadStream");
            if (result == null)
            {
                final byte[] buffer = new byte[1024];
                Result<Integer> bytesRead = byteReadStream.readBytes(buffer);

                while (bytesRead.getValue() != null && !bytesRead.hasError())
                {
                    byteWriteStream.write(buffer, 0, bytesRead.getValue());
                    bytesRead = byteReadStream.readBytes(buffer);
                }

                if (bytesRead.hasError())
                {
                    result = Result.error(bytesRead.getError());
                }
                else
                {
                    result = Result.successTrue();
                }
            }
        }

        return result;
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses UTF-8 for its character
     * encoding.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    public static CharacterWriteStream asCharacterWriteStream(ByteWriteStream byteWriteStream)
    {
        return byteWriteStream.asCharacterWriteStream(CharacterEncoding.UTF_8);
    }

    /**
     * Convert this ByteWriteStream to a CharacterWriteStream that uses the provided character
     * encoding.
     * @param encoding The encoding to use to convert characters to bytes.
     * @return A CharacterWriteStream that wraps around this ByteWriteStream.
     */
    public static CharacterWriteStream asCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding encoding)
    {
        return encoding == null ? null : new BasicCharacterWriteStream(byteWriteStream, encoding);
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and '\n' as its line separator.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    public static LineWriteStream asLineWriteStream(ByteWriteStream byteWriteStream)
    {
        return byteWriteStream.asCharacterWriteStream().asLineWriteStream();
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses the provided character encoding
     * and '\n' as its line separator.
     * @param encoding The encoding to use to convert characters to bytes.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    public static LineWriteStream asLineWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding encoding)
    {
        final CharacterWriteStream characterWriteStream = byteWriteStream.asCharacterWriteStream(encoding);
        return characterWriteStream == null ? null : characterWriteStream.asLineWriteStream();
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses UTF-8 for its character
     * encoding and the provided line separator.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    public static LineWriteStream asLineWriteStream(ByteWriteStream byteWriteStream, String lineSeparator)
    {
        return byteWriteStream.asCharacterWriteStream().asLineWriteStream(lineSeparator);
    }

    /**
     * Convert this ByteWriteStream to a LineWriteStream that uses the provided character encoding
     * and the provided line separator.
     * @param encoding The encoding to use to convert characters to bytes.
     * @param lineSeparator The separator to insert between lines.
     * @return A LineWriteStream that wraps around this ByteWriteStream.
     */
    public static LineWriteStream asLineWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding encoding, String lineSeparator)
    {
        final CharacterWriteStream characterWriteStream = byteWriteStream.asCharacterWriteStream(encoding);
        return characterWriteStream == null ? null : characterWriteStream.asLineWriteStream(lineSeparator);
    }

    /**
     * Validate that the provided byte[].
     * @param toWrite The byte[] to validate.
     * @param <T> The type of Result that will be returned.
     * @return The validation result of the byte[], or null if the byte[] was valid.
     */
    public static <T> Result<T> validateToWrite(byte[] toWrite)
    {
        Result<T> result = Result.notNull(toWrite, "toWrite");
        if (result == null)
        {
            result = Result.greaterThan(toWrite.length, 0, "toWrite.length");
        }
        return result;
    }

    /**
     * Validate that the provided startIndex is between 0 and the last index of the provided toWrite
     * byte[].
     * @param startIndex The startIndex to validate.
     * @param toWrite The byte[] that startIndex indexes into.
     * @param <T> The type of Result to return.
     * @return The validation result of the startIndex, or null if the startIndex was valid.
     */
    public static <T> Result<T> validateStartIndex(int startIndex, byte[] toWrite)
    {
        return Result.between(0, startIndex, toWrite.length - 1, "startIndex");
    }

    /**
     * Validate that the provided length is between 1 and the length of the toWrite array minus the
     * startIndex.
     * @param length The length to validate.
     * @param toWrite The byte[] that contains the bytes to write.
     * @param startIndex The start index into the byte[].
     * @param <T> The type of Result to return.
     * @return The validation result of the length, or null if the length was valid.
     */
    public static <T> Result<T> validateLength(int length, byte[] toWrite, int startIndex)
    {
        return Result.between(1, length, toWrite.length - startIndex, "length");
    }
}
