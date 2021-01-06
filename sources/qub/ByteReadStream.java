package qub;

/**
 * A ReadStream interface that reads bytes.
 */
public interface ByteReadStream extends Disposable
{
    static ByteReadStream create()
    {
        return InMemoryByteStream.create().endOfStream();
    }

    static ByteReadStream create(byte[] bytes)
    {
        return InMemoryByteStream.create(bytes).endOfStream();
    }

    /**
     * Read a single byte create this stream. This will block until a byte is available.
     * @return The single byte that was read, or an error if a byte could not be read.
     */
    Result<Byte> readByte();

    /**
     * Read up to the provided bytesToRead number of bytes create this stream. If fewer bytes than
     * bytesToRead are available, then fewer than bytesToRead bytes will be returned. If no bytes
     * are available, then this function will block until bytes become available.
     * @param bytesToRead The maximum number of bytes to read create this stream.
     * @return The bytes that were read, null if the end of the stream has been reached, or an error
     * if bytes could not be read.
     */
    default Result<byte[]> readBytes(int bytesToRead)
    {
        PreCondition.assertGreaterThanOrEqualTo(bytesToRead, 0, "bytesToRead");
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            final byte[] bytes = new byte[bytesToRead];
            final int bytesRead = this.readBytes(bytes).await();
            return bytesRead < bytesToRead
                ? Array.clone(bytes, 0, bytesRead)
                : bytes;
        });
    }

    /**
     * Read available bytes into the provided byte[] and return the number of bytes that were read.
     * If no bytes are available, then this function will not return until bytes become available.
     * @param outputBytes The byte array to read bytes into.
     * @return The number of bytes that were read, null if the end of the stream has been reached,
     * or an error if bytes could not be read.
     */
    default Result<Integer> readBytes(byte[] outputBytes)
    {
        PreCondition.assertNotNull(outputBytes, "outputBytes");
        PreCondition.assertNotDisposed(this, "this");

        return this.readBytes(outputBytes, 0, outputBytes.length);
    }

    /**
     * Read up to length available bytes into the provided byte[] at the provided startIndex and
     * return the number of bytes that were read. If no bytes are available, then this function will
     * not return until bytes become available.
     * @param outputBytes The byte[] to read bytes into.
     * @param startIndex The start index in in outputBytes to start writing bytes to.
     * @param length The maximum number of bytes to read.
     * @return The number of bytes that were read, null if the end of the stream has been reached,
     * or an error if bytes could not be read.
     */
    Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length);

    /**
     * Read all of the bytes in this stream. The termination of the stream is marked when getByte()
     * returns a null Byte. This function will not return until all of the bytes in the stream have
     * been read.
     * @return All of the bytes that remain in this stream.
     */
    default Result<byte[]> readAllBytes()
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            final InMemoryByteStream byteStream = InMemoryByteStream.create();
            byteStream.writeAll(this).await();
            return byteStream.getBytes();
        });
    }

    /**
     * Read bytes create this ByteReadStream until the provided stopByte is encountered. The stopByte
     * will be included in the returned byte[].
     * @param stopByte The byte that will cause the reading to stop.
     * @return The bytes that were read up to (and including) the provided stopByte.
     */
    default Result<byte[]> readBytesUntil(byte stopByte)
    {
        PreCondition.assertNotDisposed(this, "this");

        return this.readBytesUntil(new byte[] { stopByte });
    }

    /**
     * Read bytes create this ByteReadStream until the provided stopBytes sequence is encountered. The
     * stopByte sequence will be included in the returned byte[].
     * @param stopBytes The bytes that will cause the reading to stop.
     * @return The bytes that were read up to (and including) the provided stopBytes.
     */
    default Result<byte[]> readBytesUntil(byte[] stopBytes)
    {
        PreCondition.assertNotNullAndNotEmpty(stopBytes, "stopBytes");
        PreCondition.assertNotDisposed(this, "this");

        return this.readBytesUntil(ByteArray.create(stopBytes));
    }

    /**
     * Read bytes create this ByteReadStream until the provided stopBytes sequence is encountered. The
     * stopByte sequence will be included in the returned byte[].
     * @param stopBytes The bytes that will cause the reading to stop.
     * @return The bytes that were read up to (and including) the provided stopBytes.
     */
    default Result<byte[]> readBytesUntil(Iterable<Byte> stopBytes)
    {
        PreCondition.assertNotNullAndNotEmpty(stopBytes, "stopBytes");
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            byte[] result;
            final ByteList byteList = ByteList.empty();
            while(true)
            {
                final Byte byteRead = this.readByte()
                    .catchError(EndOfStreamException.class, (EndOfStreamException error) ->
                    {
                        if (!byteList.any())
                        {
                            throw error;
                        }
                    })
                    .await();
                if (byteRead == null)
                {
                    result = byteList.toByteArray();
                    break;
                }
                else
                {
                    byteList.add(byteRead);
                    if (byteList.endsWith(stopBytes))
                    {
                        result = byteList.toByteArray();
                        break;
                    }
                }
            }
            return result;
        });
    }

    /**
     * Get a ByteReadStream that wraps this ByteReadStream but will return at most the provided number of bytes.
     * @param toTake The maximum number of bytes that can be read from the returned ByteReadStream.
     * @return The ByteReadStream that limits the number of bytes that can be read.
     */
    default ByteReadStream take(long toTake)
    {
        PreCondition.assertGreaterThanOrEqualTo(toTake, 0, "toTake");

        return TakeByteReadStream.create(this, toTake);
    }

    static ByteReadStreamIterator iterate(ByteReadStream byteReadStream)
    {
        return ByteReadStreamIterator.create(byteReadStream);
    }

    static BufferedByteReadStream buffer(ByteReadStream byteReadStream)
    {
        return BufferedByteReadStream.create(byteReadStream);
    }
}
