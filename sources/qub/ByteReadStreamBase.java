package qub;

import java.io.InputStream;

public abstract class ByteReadStreamBase extends IteratorBase<Byte> implements ByteReadStream
{
    @Override
    public Result<byte[]> readBytes(int bytesToRead)
    {
        return ByteReadStreamBase.readBytes(this, bytesToRead);
    }

    @Override
    public int readBytes(byte[] outputBytes)
    {
        return ByteReadStreamBase.readBytes(this, outputBytes);
    }

    @Override
    public int readBytes(byte[] outputBytes, int startIndex, int length)
    {
        return ByteReadStreamBase.readBytes(this, outputBytes, startIndex, length);
    }

    @Override
    public byte[] readAllBytes()
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
        Result<byte[]> result;
        if (byteReadStream.isDisposed())
        {
            result = Result.error(new IllegalArgumentException("Cannot read bytes from a disposed ByteReadStream."));
        }
        else if (bytesToRead <= 0)
        {
            result = Result.error(new IllegalArgumentException("bytesToRead must be greater than 0."));
        }
        else
        {
            byte[] bytes = new byte[bytesToRead];
            final int bytesRead = byteReadStream.readBytes(bytes);
            if (bytesRead < 0)
            {
                bytes = null;
            }
            else if (bytesRead < bytesToRead)
            {
                bytes = Array.clone(bytes, 0, bytesRead);
            }
            result = Result.success(bytes);
        }
        return result;
    }

    public static int readBytes(ByteReadStream byteReadStream, byte[] outputBytes)
    {
        return byteReadStream.readBytes(outputBytes, 0, outputBytes.length);
    }

    public static int readBytes(ByteReadStream byteReadStream, byte[] outputBytes, int startIndex, int length)
    {
        int bytesRead = 0;

        for (int i = 0; i < length; ++i)
        {
            final Result<Byte> readByte = byteReadStream.readByte();
            if (readByte.hasError())
            {
                break;
            }
            else
            {
                outputBytes[startIndex + i] = readByte.getValue();
                ++bytesRead;
            }
        }

        return bytesRead;
    }

    public static byte[] readAllBytes(ByteReadStream byteReadStream)
    {
        byte[] result = null;

        if (byteReadStream != null && !byteReadStream.isDisposed())
        {
            final List<byte[]> readByteArrays = new ArrayList<>();
            final byte[] buffer = new byte[1024];
            int bytesRead = byteReadStream.readBytes(buffer);

            while (bytesRead > 0)
            {
                readByteArrays.add(Array.clone(buffer, 0, bytesRead));
                bytesRead = byteReadStream.readBytes(buffer);
            }

            result = Array.merge(readByteArrays);
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
}
