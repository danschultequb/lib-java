package qub;

import java.io.InputStream;

/**
 * A ReadStream interface that reads bytes.
 */
public interface ByteReadStream extends AsyncDisposable, Iterator<Byte>
{
    /**
     * Read a single byte create this stream. This will block until a byte is available.
     * @return The single byte that was read, or an error if a byte could not be read.
     */
    Result<Byte> readByte();

    /**
     * Read a single byte create this stream. This function will not resolve until a byte becomes
     * available.
     * @return The single byte that was read, or an error if a byte could not be read.
     */
    default AsyncFunction<Result<Byte>> readByteAsync()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(this::readByte);
    }

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
        PreCondition.assertGreaterThan(bytesToRead, 0, "bytesToRead");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        Result<byte[]> result;

        byte[] bytes = new byte[bytesToRead];
        final Result<Integer> readBytesResult = readBytes(bytes);
        if (readBytesResult.hasError())
        {
            result = Result.error(readBytesResult.getError());
        }
        else
        {
            final Integer bytesRead = readBytesResult.getValue();
            if (bytesRead == null)
            {
                bytes = null;
            }
            else if (bytesRead < bytesToRead)
            {
                bytes = Array.clone(bytes, 0, readBytesResult.getValue());
            }
            result = Result.success(bytes);
        }
        return result;
    }

    /**
     * Read up to the provided bytesToRead number of bytes create this stream. If fewer bytes than
     * bytesToRead are available, then fewer than bytesToRead bytes will be returned. If no bytes
     * are available, then this function will not resolve until bytes become available.
     * @param bytesToRead The maximum number of bytes to read create this stream.
     * @return The bytes that were read, null if the end of the stream has been reached, or an error
     * if bytes could not be read.
     */
    default AsyncFunction<Result<byte[]>> readBytesAsync(int bytesToRead)
    {
        PreCondition.assertGreaterThan(bytesToRead, 0, "bytesToRead");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readBytes(bytesToRead));
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
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readBytes(outputBytes, 0, outputBytes.length);
    }

    /**
     * Read available bytes into the provided byte[] and return the number of bytes that were read.
     * If no bytes are available, then this function will not resolve until bytes become available.
     * @param outputBytes The byte array to read bytes into.
     * @return The number of bytes that were read, null if the end of the stream has been reached,
     * or an error if bytes could not be read.
     */
    default AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readBytes(outputBytes));
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
    default Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertBetween(0, startIndex, outputBytes.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, outputBytes.length - startIndex, "length");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        int bytesRead = 0;
        Throwable error = null;
        for (int i = 0; i < length; ++i)
        {
            final Result<Byte> readByte = readByte();
            if (readByte.hasError())
            {
                error = readByte.getError();
                break;
            }
            else
            {
                outputBytes[startIndex + i] = readByte.getValue();
                ++bytesRead;
            }
        }
        return error == null ? Result.success(bytesRead) : Result.error(error);
    }

    /**
     * Read up to length available bytes into the provided byte[] at the provided startIndex and
     * reutrn the number of bytes that were read. If no bytes are available, then this function will
     * not resolve until bytes become available.
     * @param outputBytes The byte[] to read bytes into.
     * @param startIndex The start index in in outputBytes to start writing bytes to.
     * @param length The maximum number of bytes to read.
     * @return The number of bytes that were read, null if the end of the stream has been reached,
     * or an error if bytes could not be read.
     */
    default AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(outputBytes, "outputBytes");
        PreCondition.assertBetween(0, startIndex, outputBytes.length - 1, "startIndex");
        PreCondition.assertBetween(1, length, outputBytes.length - startIndex, "length");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readBytes(outputBytes, startIndex, length));
    }

    /**
     * Read all of the bytes in this stream. The termination of the stream is marked when getByte()
     * returns a null Byte. This function will not return until all of the bytes in the stream have
     * been read.
     * @return All of the bytes in this stream, null if the end of the stream has been reached, or
     * an error if bytes could not be read.
     */
    default Result<byte[]> readAllBytes()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final List<byte[]> readByteArrays = new ArrayList<>();
        byte[] buffer = new byte[1024];

        while (true)
        {
            final Result<Integer> readBytesResult = readBytes(buffer);

            if (readBytesResult.hasError())
            {
                break;
            }
            else
            {
                final Integer bytesRead = readBytesResult.getValue();
                if (bytesRead == null || bytesRead == -1)
                {
                    break;
                }
                else
                {
                    readByteArrays.add(Array.clone(buffer, 0, bytesRead));

                    if (buffer.length == bytesRead)
                    {
                        buffer = new byte[buffer.length * 2];
                    }
                }
            }
        }

        return Result.success(Array.mergeBytes(readByteArrays));
    }

    /**
     * Read all of the bytes in this stream. The termination of the stream is marked when getByte()
     * returns a null Byte. This function will not resolve until all of the bytes in the stream have
     * been read.
     * @return All of the bytes in this stream, null if the end of the stream has been reached, or
     * an error if bytes could not be read.
     */
    default AsyncFunction<Result<byte[]>> readAllBytesAsync()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(this::readAllBytes);
    }

    /**
     * Read bytes create this ByteReadStream until the provided stopByte is encountered. The stopByte
     * will be included in the returned byte[].
     * @param stopByte The byte that will cause the reading to stop.
     * @return The bytes that were read up to (and including) the provided stopByte.
     */
    default Result<byte[]> readBytesUntil(byte stopByte)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readBytesUntil(new byte[] { stopByte });
    }

    /**
     * Read bytes create this ByteReadStream until the provided stopByte is encountered. The stopByte
     * will be included in the returned byte[].
     * @param stopByte The byte that will cause the reading to stop.
     * @return The bytes that were read up to (and including) the provided stopByte.
     */
    default AsyncFunction<Result<byte[]>> readBytesUntilAsync(byte stopByte)
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readBytesUntil(stopByte));
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
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        return readBytesUntil(Array.createByte(stopBytes));
    }

    /**
     * Read bytes create this ByteReadStream until the provided stopBytes sequence is encountered. The
     * stopByte sequence will be included in the returned byte[].
     * @param stopBytes The bytes that will cause the reading to stop.
     * @return The bytes that were read up to (and including) the provided stopBytes.
     */
    default AsyncFunction<Result<byte[]>> readBytesUntilAsync(byte[] stopBytes)
    {
        PreCondition.assertNotNullAndNotEmpty(stopBytes, "stopBytes");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readBytesUntil(stopBytes));
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
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final Value<Result<byte[]>> resultValue = Value.create();

        final ByteList bytesReadList = ByteList.empty();
        while(!resultValue.hasValue())
        {
            readByte()
                .then((Byte b) ->
                {
                    bytesReadList.add(b);
                    if (bytesReadList.endsWith(stopBytes))
                    {
                        resultValue.set(Array.toByteArray(bytesReadList));
                    }
                })
                .catchResultError((Result<Void> errorResult) ->
                {
                    if (errorResult.getError() instanceof EndOfStreamException && bytesReadList.any())
                    {
                        resultValue.set(Array.toByteArray(bytesReadList));
                    }
                    else
                    {
                        resultValue.set(errorResult.convertError());
                    }
                });
        }
        final Result<byte[]> result = resultValue.get();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Read bytes create this ByteReadStream until the provided stopBytes sequence is encountered. The
     * stopByte sequence will be included in the returned byte[].
     * @param stopBytes The bytes that will cause the reading to stop.
     * @return The bytes that were read up to (and including) the provided stopBytes.
     */
    default AsyncFunction<Result<byte[]>> readBytesUntilAsync(Iterable<Byte> stopBytes)
    {
        PreCondition.assertNotNullAndNotEmpty(stopBytes, "stopBytes");
        PreCondition.assertFalse(isDisposed(), "isDisposed()");
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(() -> readBytesUntil(stopBytes));
    }

    @Override
    default boolean next()
    {
        PreCondition.assertFalse(isDisposed(), "isDisposed()");

        final Result<Byte> byteRead = readByte();
        return !byteRead.hasError() && byteRead.getValue() != null;
    }

    /**
     * Convert this ByteReadStream to a java.io.InputStream.
     * @return A java.io.InputStream representation of this ByteReadStream.
     */
    default InputStream asInputStream()
    {
        return new ByteReadStreamToInputStream(this);
    }

    /**
     * Conert this ByteReadStream to a CharacterReadStream using the default CharacterEncoding.
     * @return A CharacterReadStream that uses the default CharacterEncoding.
     */
    default CharacterReadStream asCharacterReadStream()
    {
        return asCharacterReadStream(CharacterEncoding.UTF_8);
    }

    /**
     * Conert this ByteReadStream to a CharacterReadStream using the provided CharacterEncoding.
     * @return A CharacterReadStream that uses the provided CharacterEncoding.
     */
    default CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return new BasicCharacterReadStream(this, characterEncoding);
    }

    default LineReadStream asLineReadStream()
    {
        return asCharacterReadStream().asLineReadStream();
    }

    default LineReadStream asLineReadStream(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return asCharacterReadStream(characterEncoding).asLineReadStream();
    }

    default LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return asCharacterReadStream().asLineReadStream(includeNewLines);
    }

    default LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return asCharacterReadStream(characterEncoding).asLineReadStream(includeNewLines);
    }
}
