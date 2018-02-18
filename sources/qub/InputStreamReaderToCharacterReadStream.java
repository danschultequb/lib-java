package qub;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * A class for reading characters from a stream.
 */
class InputStreamReaderToCharacterReadStream implements CharacterReadStream
{
    private final ByteReadStream byteReadStream;
    private final CharacterEncoding characterEncoding;
    private final InputStreamReader reader;
    private boolean hasStarted;
    private Character current;

    InputStreamReaderToCharacterReadStream(ByteReadStream byteReadStream, CharacterEncoding characterEncoding)
    {
        this.byteReadStream = byteReadStream;
        byteReadStream.setExceptionHandler((IOException e) ->
        {
            throw new RuntimeException(e);
        });
        this.characterEncoding = characterEncoding;

        final InputStream inputStream = byteReadStream.asInputStream();
        final Charset charset = characterEncoding.getCharset();
        this.reader = new InputStreamReader(inputStream, charset);
    }

    @Override
    public boolean isOpen()
    {
        return byteReadStream.isOpen();
    }

    @Override
    public void close()
    {
        byteReadStream.close();
    }

    @Override
    public Character readCharacter()
    {
        hasStarted = true;

        try
        {
            final int characterAsInt = reader.read();
            if (characterAsInt >= 0)
            {
                current = (char) characterAsInt;
            }
            else
            {
                current = null;
            }
        }
        catch (Exception ignored)
        {
            current = null;
        }

        return current;
    }

    @Override
    public int readCharacters(char[] characters)
    {
        hasStarted = true;

        int result = -1;
        try
        {
            result = reader.read(characters);
        }
        catch (Exception ignored)
        {
        }

        if (result < 0)
        {
            current = null;
        }
        else if (result > 0)
        {
            current = characters[result - 1];
        }

        return result;
    }

    @Override
    public int readCharacters(char[] characters, int startIndex, int length)
    {
        hasStarted = true;

        int result = -1;
        try
        {
            result = reader.read(characters, startIndex, length);
        }
        catch (Exception ignored)
        {
        }

        if (result < 0)
        {
            current = null;
        }
        else if (result > 0)
        {
            current = characters[startIndex + result - 1];
        }

        return result;
    }

    @Override
    public CharacterEncoding getEncoding()
    {
        return characterEncoding;
    }

    @Override
    public ByteReadStream asByteReadStream()
    {
        return byteReadStream;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return current != null;
    }

    @Override
    public Character getCurrent()
    {
        return current;
    }

    @Override
    public boolean next()
    {
        return readCharacter() != null;
    }
}
