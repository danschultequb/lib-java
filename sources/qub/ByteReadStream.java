package qub;

import java.io.IOException;
import java.io.InputStream;

public interface ByteReadStream extends Stream, Iterator<Byte>
{
    Byte readByte();

    default byte[] readBytes(int bytesToRead)
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

    default int readBytes(byte[] outputBytes)
    {
        return readBytes(outputBytes, 0, outputBytes.length);
    }

    int readBytes(byte[] outputBytes, int startIndex, int length);

    void setExceptionHandler(Action1<IOException> exceptionHandler);

    default InputStream asInputStream()
    {
        return new ByteReadStreamToInputStream(this);
    }

    default CharacterReadStream asCharacterReadStream()
    {
        return asCharacterReadStream(CharacterEncoding.UTF_8);
    }

    default CharacterReadStream asCharacterReadStream(CharacterEncoding encoding)
    {
        return new InputStreamReaderToCharacterReadStream(this, encoding);
    }

    default LineReadStream asLineReadStream()
    {
        return asCharacterReadStream().asLineReadStream();
    }

    default LineReadStream asLineReadStream(CharacterEncoding encoding)
    {
        return asCharacterReadStream(encoding).asLineReadStream();
    }

    default LineReadStream asLineReadStream(boolean includeNewLines)
    {
        return asCharacterReadStream().asLineReadStream(includeNewLines);
    }

    default LineReadStream asLineReadStream(CharacterEncoding encoding, boolean includeNewLines)
    {
        return asCharacterReadStream(encoding).asLineReadStream(includeNewLines);
    }
}
