package qub;

import java.io.InputStream;

public abstract class ByteReadStreamBase extends IteratorBase<Byte> implements ByteReadStream
{
    @Override
    public byte[] readBytes(int bytesToRead)
    {
        byte[] result = null;
        if (bytesToRead >= 1)
        {
            result = new byte[bytesToRead];
            final int bytesRead = readBytes(result);
            if (bytesRead < 0)
            {
                result = null;
            }
            else if (bytesRead < bytesToRead)
            {
                result = Array.clone(result, 0, bytesRead);
            }
        }
        return result;
    }

    @Override
    public int readBytes(byte[] outputBytes)
    {
        return readBytes(outputBytes, 0, outputBytes.length);
    }

    @Override
    public InputStream asInputStream()
    {
        return asInputStream(this);
    }

    @Override
    public CharacterReadStream asCharacterReadStream()
    {
        return asCharacterReadStream(this);
    }

    @Override
    public LineReadStream asLineReadStream()
    {
        return asLineReadStream(this);
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding encoding)
    {
        return asLineReadStream(this, encoding);
    }

    @Override
    public LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return asLineReadStream(this, includeNewLines);
    }

    @Override
    public LineReadStream asLineReadStream(CharacterEncoding encoding, boolean includeNewLines)
    {
        return asLineReadStream(this, encoding, includeNewLines);
    }

    public static InputStream asInputStream(ByteReadStream byteReadStream)
    {
        return byteReadStream == null ? null : new ByteReadStreamToInputStream(byteReadStream);
    }

    public static CharacterReadStream asCharacterReadStream(ByteReadStream byteReadStream)
    {
        return asCharacterReadStream(byteReadStream, CharacterEncoding.UTF_8);
    }

    public static CharacterReadStream asCharacterReadStream(ByteReadStream byteReadStream, CharacterEncoding encoding)
    {
        return byteReadStream == null ? null : byteReadStream.asCharacterReadStream(encoding);
    }

    public static LineReadStream asLineReadStream(ByteReadStream byteReadStream)
    {
        final CharacterReadStream characterReadStream = asCharacterReadStream(byteReadStream);
        return characterReadStream == null ? null : characterReadStream.asLineReadStream();
    }

    public static LineReadStream asLineReadStream(ByteReadStream byteReadStream, CharacterEncoding encoding)
    {
        final CharacterReadStream characterReadStream = asCharacterReadStream(byteReadStream, encoding);
        return characterReadStream == null ? null : characterReadStream.asLineReadStream();
    }

    public static LineReadStream asLineReadStream(ByteReadStream byteReadStream, boolean includeNewLines)
    {
        final CharacterReadStream characterReadStream = asCharacterReadStream(byteReadStream);
        return characterReadStream == null ? null : characterReadStream.asLineReadStream(includeNewLines);
    }

    public static LineReadStream asLineReadStream(ByteReadStream byteReadStream, CharacterEncoding encoding, boolean includeNewLines)
    {
        final CharacterReadStream characterReadStream = asCharacterReadStream(byteReadStream, encoding);
        return characterReadStream == null ? null : characterReadStream.asLineReadStream(includeNewLines);
    }
}
