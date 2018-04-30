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
    public Result<byte[]> readBytes(int bytesToRead)
    {
        return ByteReadStreamBase.readBytes(this, bytesToRead);
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes)
    {
        return ByteReadStreamBase.readBytes(this, outputBytes);
    }

    @Override
    public Result<Integer> readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return ByteReadStreamBase.readBytes(this, outputBytes, startIndex, length);
    }

    @Override
    public Result<byte[]> readAllBytes()
    {
        return ByteReadStreamBase.readAllBytes(this);
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

    public static Result<byte[]> readBytes(ByteReadStream byteReadStream, int bytesToRead)
    {
        Result<byte[]> result = ByteReadStreamBase.validateByteReadStream(byteReadStream);
        if (result == null)
        {
            if (bytesToRead <= 0)
            {
                result = Result.error(new IllegalArgumentException("bytesToRead must be greater than 0."));
            }
            else
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

    public static Result<Integer> readBytes(ByteReadStream byteReadStream, byte[] outputBytes)
    {
        return byteReadStream.readBytes(outputBytes, 0, outputBytes.length);
    }

    public static Result<Integer> readBytes(ByteReadStream byteReadStream, byte[] outputBytes, int startIndex, int length)
    {
        Result<Integer> result = ByteReadStreamBase.validateByteReadStream(byteReadStream);
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
        return new InputStreamReaderToCharacterReadStream(byteReadStream, encoding);
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
        Result<T> result = null;
        if (byteReadStream == null)
        {
            result = Result.error(new IllegalArgumentException("byteReadStream cannot be null."));
        }
        else if (byteReadStream.isDisposed())
        {
            result = Result.error(new IllegalArgumentException("byteReadStream cannot be disposed."));
        }
        return result;
    }
}
