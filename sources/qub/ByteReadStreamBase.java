package qub;

import java.io.InputStream;

public abstract class ByteReadStreamBase extends IteratorBase<Byte> implements ByteReadStream
{
    @Override
    public final AsyncFunction<Result<Boolean>> disposeAsync()
    {
        return AsyncDisposableBase.disposeAsync(this);
    }

    @Override
    public final void close() throws Exception
    {
        DisposableBase.close(this);
    }

    @Override
    public abstract Result<Byte> readByte();

    @Override
    public AsyncFunction<Result<Byte>> readByteAsync()
    {
        return ByteReadStreamBase.readByteAsync(this);
    }

    @Override
    public Result<byte[]> readBytes(int bytesToRead)
    {
        return ByteReadStreamBase.readBytes(this, bytesToRead);
    }

    @Override
    public AsyncFunction<Result<byte[]>> readBytesAsync(int bytesToRead)
    {
        return ByteReadStreamBase.readBytesAsync(this, bytesToRead);
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes)
    {
        return ByteReadStreamBase.readBytes(this, outputBytes);
    }

    @Override
    public AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes)
    {
        return ByteReadStreamBase.readBytesAsync(this, outputBytes);
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return ByteReadStreamBase.readBytes(this, outputBytes, startIndex, length);
    }

    @Override
    public AsyncFunction<Result<Integer>> readBytesAsync(byte[] outputBytes, int startIndex, int length)
    {
        return ByteReadStreamBase.readBytesAsync(this, outputBytes, startIndex, length);
    }

    @Override
    public Result<byte[]> readAllBytes()
    {
        return ByteReadStreamBase.readAllBytes(this);
    }

    @Override
    public AsyncFunction<Result<byte[]>> readAllBytesAsync()
    {
        return ByteReadStreamBase.readAllBytesAsync(this);
    }

    @Override
    public InputStream asInputStream()
    {
        return ByteReadStreamBase.asInputStream(this);
    }

    @Override
    public CharacterReadStream asCharacterReadStream()
    {
        return ByteReadStreamBase.asCharacterReadStream(this);
    }

    @Override
    public CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding)
    {
        return ByteReadStreamBase.asCharacterReadStream(this, characterEncoding);
    }

    @Override
    public LineReadStream asLineReadStream()
    {
        return ByteReadStreamBase.asLineReadStream(this);
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding characterEncoding)
    {
        return ByteReadStreamBase.asLineReadStream(this, characterEncoding);
    }

    @Override
    public LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return ByteReadStreamBase.asLineReadStream(this, includeNewLines);
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines)
    {
        return ByteReadStreamBase.asLineReadStream(this, characterEncoding, includeNewLines);
    }

    public static AsyncFunction<Result<Byte>> readByteAsync(final ByteReadStream byteReadStream)
    {
        AsyncFunction<Result<Byte>> result = ByteReadStreamBase.validateByteReadStreamAsync(byteReadStream);
        if (result == null)
        {
            result = async(byteReadStream, new Function0<Result<Byte>>()
            {
                @Override
                public Result<Byte> run()
                {
                    return byteReadStream.readByte();
                }
            });
        }
        return result;
    }

    public static Result<byte[]> readBytes(ByteReadStream byteReadStream, int bytesToRead)
    {
        Result<byte[]> result = ByteReadStreamBase.validateByteReadStream(byteReadStream);
        if (result == null)
        {
            result = Result.greaterThan(0, bytesToRead, "bytesToRead");
            if (result == null)
            {
                byte[] bytes = new byte[bytesToRead];
                final Result<Integer> readBytesResult = byteReadStream.readBytes(bytes);
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
            }
        }
        return result;
    }

    public static AsyncFunction<Result<byte[]>> readBytesAsync(final ByteReadStream byteReadStream, final int bytesToRead)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<byte[]>> result = ByteReadStreamBase.validateByteReadStreamAsync(byteReadStream);
        if (result == null)
        {
            result = currentAsyncRunner.greaterThan(0, bytesToRead, "bytesToRead");
            if (result == null)
            {
                result = async(byteReadStream, new Function0<Result<byte[]>>()
                {
                    @Override
                    public Result<byte[]> run()
                    {
                        return byteReadStream.readBytes(bytesToRead);
                    }
                });
            }
        }

        return result;
    }

    public static Result<Integer> readBytes(ByteReadStream byteReadStream, byte[] outputBytes)
    {
        Result<Integer> result = ByteReadStreamBase.validateByteReadStream(byteReadStream);
        if (result == null)
        {
            result = ByteReadStreamBase.validateOutputBytes(outputBytes);
            if (result == null)
            {
                result = byteReadStream.readBytes(outputBytes, 0, outputBytes.length);
            }
        }
        return result;
    }

    public static AsyncFunction<Result<Integer>> readBytesAsync(final ByteReadStream byteReadStream, final byte[] outputBytes)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Integer>> result = ByteReadStreamBase.validateByteReadStreamAsync(byteReadStream);
        if (result == null)
        {
            result = ByteReadStreamBase.validateOutputBytesAsync(outputBytes);
            if (result == null)
            {
                result = async(byteReadStream, new Function0<Result<Integer>>()
                {
                    @Override
                    public Result<Integer> run()
                    {
                        return byteReadStream.readBytes(outputBytes);
                    }
                });
            }
        }

        return result;
    }

    public static Result<Integer> readBytes(ByteReadStream byteReadStream, byte[] outputBytes, int startIndex, int length)
    {
        Result<Integer> result = ByteReadStreamBase.validateByteReadStream(byteReadStream);
        if (result == null)
        {
            result = ByteReadStreamBase.validateOutputBytes(outputBytes);
            if (result == null)
            {
                result = ByteReadStreamBase.validateStartIndex(startIndex, outputBytes);
                if (result == null)
                {
                    result = ByteReadStreamBase.validateLength(length, outputBytes, startIndex);
                    if (result == null)
                    {
                        int bytesRead = 0;
                        Throwable error = null;
                        for (int i = 0; i < length; ++i)
                        {
                            final Result<Byte> readByte = byteReadStream.readByte();
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
                        result = Result.done(bytesRead, error);
                    }
                }
            }
        }
        return result;
    }

    public static AsyncFunction<Result<Integer>> readBytesAsync(final ByteReadStream byteReadStream, final byte[] outputBytes, final int startIndex, final int length)
    {
        AsyncFunction<Result<Integer>> result = ByteReadStreamBase.validateByteReadStreamAsync(byteReadStream);
        if (result == null)
        {
            result = ByteReadStreamBase.validateOutputBytesAsync(outputBytes);
            if (result == null)
            {
                result = ByteReadStreamBase.validateStartIndexAsync(startIndex, outputBytes);
                if (result == null)
                {
                    result = ByteReadStreamBase.validateLengthAsync(length, outputBytes, startIndex);
                    if (result == null)
                    {
                        result = async(byteReadStream, new Function0<Result<Integer>>()
                        {
                            @Override
                            public Result<Integer> run()
                            {
                                return byteReadStream.readBytes(outputBytes, startIndex, length);
                            }
                        });
                    }
                }
            }
        }
        return result;
    }

    public static Result<byte[]> readAllBytes(ByteReadStream byteReadStream)
    {
        Result<byte[]> result = ByteReadStreamBase.validateByteReadStream(byteReadStream);
        if (result == null)
        {
            final List<byte[]> readByteArrays = new ArrayList<>();
            final byte[] buffer = new byte[1024];
            Result<Integer> readBytesResult = byteReadStream.readBytes(buffer);

            while (!readBytesResult.hasError() && readBytesResult.getValue() != null)
            {
                final int bytesRead = readBytesResult.getValue();
                readByteArrays.add(Array.clone(buffer, 0, bytesRead));
                readBytesResult = byteReadStream.readBytes(buffer);
            }

            result = Result.success(Array.merge(readByteArrays));
        }
        return result;
    }

    public static AsyncFunction<Result<byte[]>> readAllBytesAsync(final ByteReadStream byteReadStream)
    {
        AsyncFunction<Result<byte[]>> result = ByteReadStreamBase.validateByteReadStreamAsync(byteReadStream);
        if (result == null)
        {
            result = async(byteReadStream, new Function0<Result<byte[]>>()
            {
                @Override
                public Result<byte[]> run()
                {
                    return byteReadStream.readAllBytes();
                }
            });
        }
        return result;
    }

    public static InputStream asInputStream(ByteReadStream byteReadStream)
    {
        return new ByteReadStreamToInputStream(byteReadStream);
    }

    public static CharacterReadStream asCharacterReadStream(ByteReadStream byteReadStream)
    {
        return byteReadStream.asCharacterReadStream(CharacterEncoding.UTF_8);
    }

    public static CharacterReadStream asCharacterReadStream(ByteReadStream byteReadStream, CharacterEncoding encoding)
    {
        return byteReadStream == null || encoding == null ? null : new InputStreamReaderToCharacterReadStream(byteReadStream, encoding);
    }

    public static LineReadStream asLineReadStream(ByteReadStream byteReadStream)
    {
        return byteReadStream.asCharacterReadStream().asLineReadStream();
    }

    public static LineReadStream asLineReadStream(ByteReadStream byteReadStream, CharacterEncoding encoding)
    {
        return byteReadStream.asCharacterReadStream(encoding).asLineReadStream();
    }

    public static LineReadStream asLineReadStream(ByteReadStream byteReadStream, boolean includeNewLines)
    {
        return byteReadStream.asCharacterReadStream().asLineReadStream(includeNewLines);
    }

    public static LineReadStream asLineReadStream(ByteReadStream byteReadStream, CharacterEncoding encoding, boolean includeNewLines)
    {
        return byteReadStream.asCharacterReadStream(encoding).asLineReadStream(includeNewLines);
    }

    public static <T> Result<T> validateByteReadStream(ByteReadStream byteReadStream)
    {
        Result<T> result = Result.notNull(byteReadStream, "byteReadStream");
        if (result == null)
        {
            result = Result.equal(false, byteReadStream.isDisposed(), "byteReadStream.isDisposed()");
        }
        return result;
    }

    public static <T> AsyncFunction<Result<T>> validateByteReadStreamAsync(ByteReadStream byteReadStream)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = ByteReadStreamBase.validateByteReadStream(byteReadStream);
        return result == null ? null : currentAsyncRunner.done(result);
    }

    public static <T> Result<T> validateOutputBytes(byte[] outputBytes)
    {
        Result<T> result = Result.notNull(outputBytes, "outputBytes");
        if (result == null)
        {
            result = Result.greaterThan(0, outputBytes.length, "outputBytes.length");
        }
        return result;
    }

    public static <T> AsyncFunction<Result<T>> validateOutputBytesAsync(byte[] outputBytes)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = ByteReadStreamBase.validateOutputBytes(outputBytes);
        return result == null ? null : currentAsyncRunner.done(result);
    }

    public static <T> Result<T> validateStartIndex(int startIndex, byte[] outputBytes)
    {
        return Result.between(0, startIndex, outputBytes.length - 1, "startIndex");
    }

    public static <T> AsyncFunction<Result<T>> validateStartIndexAsync(int startIndex, byte[] outputBytes)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = ByteReadStreamBase.validateStartIndex(startIndex, outputBytes);
        return result == null ? null : currentAsyncRunner.done(result);
    }

    public static <T> Result<T> validateLength(int length, byte[] outputBytes, int startIndex)
    {
        return Result.between(1, length, outputBytes.length - startIndex, "length");
    }

    public static <T> AsyncFunction<Result<T>> validateLengthAsync(int length, byte[] outputBytes, int startIndex)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final Result<T> result = ByteReadStreamBase.validateLength(length, outputBytes, startIndex);
        return result == null ? null : currentAsyncRunner.done(result);
    }

    private static <T> AsyncFunction<Result<T>> async(ByteReadStream byteReadStream, Function0<Result<T>> function)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final AsyncRunner streamAsyncRunner = byteReadStream.getAsyncRunner();

        AsyncFunction<Result<T>> result;
        if (streamAsyncRunner == null)
        {
            result = currentAsyncRunner.error(new IllegalArgumentException("Cannot invoke ByteReadStream asynchronous functions when the ByteReadStream has not been assigned an AsyncRunner."));
        }
        else
        {
            result = streamAsyncRunner.schedule(function)
                .thenOn(currentAsyncRunner);
        }
        return result;
    }
}
