package qub;

/**
 * A ReadStream interface that reads bytes.
 */
public interface ByteReadStream extends Disposable, Iterator<Byte>
{
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
        PreCondition.assertGreaterThanOrEqualTo(bytesToRead, 1, "bytesToRead");
        PreCondition.assertNotDisposed(this);

        final byte[] bytes = new byte[bytesToRead];
        return readBytes(bytes)
            .then((Integer bytesRead) ->
            {
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
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertNotDisposed(this);

        return readBytes(outputBytes, 0, outputBytes.length);
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
     * @return All of the bytes in this stream, null if the end of the stream has been reached, or
     * an error if bytes could not be read.
     */
    default Result<byte[]> readAllBytes()
    {
        PreCondition.assertNotDisposed(this);

        final InMemoryByteStream byteStream = new InMemoryByteStream();
        return byteStream.writeAllBytes(this)
            .thenResult(() ->
            {
                final byte[] bytes = byteStream.getBytes();
                return bytes.length == 0
                    ? Result.endOfStream()
                    : Result.success(bytes);
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
        PreCondition.assertNotDisposed(this);

        return readBytesUntil(new byte[] { stopByte });
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
        PreCondition.assertNotDisposed(this);

        return readBytesUntil(Array.createByte(stopBytes));
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
        PreCondition.assertNotDisposed(this);

        return Result.create(() ->
        {
            byte[] result;
            final ByteList byteList = ByteList.empty();
            while(true)
            {
                final Byte byteRead = readByte()
                    .catchErrorResult(EndOfStreamException.class, (EndOfStreamException error) ->
                    {
                        return byteList.any()
                            ? Result.success()
                            : Result.error(error);
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

    @Override
    default boolean next()
    {
        PreCondition.assertNotDisposed(this);

        return readByte()
            .thenResult(Result::successTrue)
            .catchErrorResult(EndOfStreamException.class, Result::successFalse)
            .await();
    }

    /**
     * Convert this ByteReadStream to a CharacterReadStream using the default CharacterEncoding.
     * @return A CharacterReadStream that uses the default CharacterEncoding.
     */
    default CharacterReadStream asCharacterReadStream()
    {
        return asCharacterReadStream(CharacterEncoding.UTF_8);
    }

    /**
     * Convert this ByteReadStream to a CharacterReadStream using the provided CharacterEncoding.
     * @return A CharacterReadStream that uses the provided CharacterEncoding.
     */
    default CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return new BasicCharacterReadStream(this, characterEncoding);
    }
}
