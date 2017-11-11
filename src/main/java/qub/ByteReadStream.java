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

    CharacterReadStream asCharacterReadStream(CharacterEncoding encoding);

    LineReadStream asLineReadStream();

    LineReadStream asLineReadStream(CharacterEncoding encoding);

    LineReadStream asLineReadStream(boolean includeNewLines);

    LineReadStream asLineReadStream(CharacterEncoding encoding, boolean includeNewLines);
}
