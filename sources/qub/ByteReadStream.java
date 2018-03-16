package qub;

import java.io.IOException;
import java.io.InputStream;

public interface ByteReadStream extends Stream, Iterator<Byte>
{
    Byte readByte();

    byte[] readBytes(int bytesToRead);

    int readBytes(byte[] outputBytes);

    int readBytes(byte[] outputBytes, int startIndex, int length);

    void setExceptionHandler(Action1<IOException> exceptionHandler);

    InputStream asInputStream();

    CharacterReadStream asCharacterReadStream();

    CharacterReadStream asCharacterReadStream(CharacterEncoding characterEncoding);

    LineReadStream asLineReadStream();

    LineReadStream asLineReadStream(CharacterEncoding characterEncoding);

    LineReadStream asLineReadStream(boolean includeNewLines);

    LineReadStream asLineReadStream(CharacterEncoding characterEncoding, boolean includeNewLines);
}
