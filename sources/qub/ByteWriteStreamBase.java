package qub;

public abstract class ByteWriteStreamBase extends DisposableBase implements ByteWriteStream
{
    @Override
    public boolean write(byte[] toWrite)
    {
        return ByteWriteStreamBase.write(this, toWrite);
    }

    @Override
    public boolean write(byte[] toWrite, int startIndex, int length)
    {
        return ByteWriteStreamBase.write(this, toWrite, startIndex, length);
    }

    @Override
    public boolean writeAll(ByteReadStream byteReadStream)
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
    public static boolean write(ByteWriteStream byteWriteStream, byte[] toWrite)
    {
        return byteWriteStream.write(toWrite, 0, toWrite == null ? 0 : toWrite.length);
    }

    /**
     * Write the provided subsection of bytes to this ByteWriteStream.
     * @param toWrite The array of bytes that contains the bytes to write to this stream.
     * @param startIndex The start index of the subsection inside toWrite to write.
     * @param length The number of bytes to write.
     */
    public static boolean write(ByteWriteStream byteWriteStream, byte[] toWrite, int startIndex, int length)
    {
        boolean result = true;
        if (toWrite != null && length > 0)
        {
            final int afterEndIndex = Math.minimum(startIndex + length, toWrite.length);
            for (int i = startIndex; i < afterEndIndex; ++i)
            {
                result = byteWriteStream.write(toWrite[i]);
                if (!result)
                {
                    break;
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
    public static boolean writeAll(ByteWriteStream byteWriteStream, ByteReadStream byteReadStream)
    {
        boolean result = false;

        if (byteReadStream != null && !byteWriteStream.isDisposed() && !byteReadStream.isDisposed())
        {
            final byte[] buffer = new byte[1024];
            int bytesRead = byteReadStream.readBytes(buffer);

            if (bytesRead > 0)
            {
                result = true;
            }

            while (bytesRead > 0)
            {
                byteWriteStream.write(buffer, 0, bytesRead);
                bytesRead = byteReadStream.readBytes(buffer);
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
        return encoding == null ? null : new ByteWriteStreamToCharacterWriteStream(byteWriteStream, encoding);
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
}
